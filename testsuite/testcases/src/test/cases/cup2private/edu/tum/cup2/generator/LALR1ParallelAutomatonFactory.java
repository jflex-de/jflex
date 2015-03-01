package edu.tum.cup2.generator;

import static edu.tum.cup2.generator.Edge.createAcceptEdge;
import static edu.tum.cup2.grammar.SpecialTerminals.EndOfInputStream;
import static edu.tum.cup2.grammar.SpecialTerminals.Placeholder;
import static edu.tum.cup2.util.CollectionTools.*;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import edu.tum.cup2.generator.exceptions.GeneratorException;
import edu.tum.cup2.generator.items.CPGoToLink;
import edu.tum.cup2.generator.items.LALR1CPItem;
import edu.tum.cup2.generator.items.LR0Item;
import edu.tum.cup2.generator.items.LR1Item;
import edu.tum.cup2.generator.states.LALR1CPState;
import edu.tum.cup2.generator.states.LR1State;
import edu.tum.cup2.generator.terminals.EfficientTerminalSet;
import edu.tum.cup2.grammar.Symbol;
import edu.tum.cup2.util.Tuple2;


/**
 * Factory for a LALR(1) automaton, that is computed concurrently.
 * (based on {@link LALR1CPAutomatonFactory}).
 * 
 * @author Andreas Wenger
 * @author Daniel Altmann
 * @author Michael Hausmann
 * @author Johannes Schamburger
 */
public class LALR1ParallelAutomatonFactory extends AutomatonFactory<LR1Item, LR1State>
{

	protected Map<LALR1CPState, LALR1CPState> kernel2closure;
	protected Map<LALR1CPItem, LALR1CPState> itemStates;
	protected Set<Edge> lr0Edges;
	protected Map<LALR1CPItem, CPGoToLink> goToLinks;
	private Map<LALR1CPItem, EfficientTerminalSet> lookaheads;
	
	/**
	 * Task for the first step of the efficient LALR(1) parser generation.
	 * Creates the LR(0) automaton together with context propagation links.
	 */
	private class WorkerTaskLALR1 extends WorkerTask<LALR1CPState> {

		public WorkerTaskLALR1(LALR1CPState state, ThreadPoolExecutor threadPool, ReentrantLock lock) {
			super(state, threadPool, lock);
		}
		
		/**
		 * Handles one state; creates new tasks by shifting.
		 * @return 0 - we don't care about the return value, but the {@link Callable}
		 * 				interface forces us to return an Integer.
		 */
		public Integer call() {

			try {
				//handle next state in queue
		
				//debug messages
				printDebugMessages();
				
				//first, create a LALR(1)-with-CP-Links automaton and remember the closures
				//for performance reasons
				currentState = kernel2closure.get(stateKernel);
				
				Set<Symbol> shiftedSymbols = new HashSet<Symbol>();
				for (LALR1CPItem item : currentState.getItems())
				{
					//remember the state this item belongs to
					itemStates.put(item, currentState);
					//try to shift
					if (item.isShiftable())
					{
						Symbol symbol = item.getNextSymbol();
						if (symbol == EndOfInputStream)
						{
							//$-symbol: here we accept
							lr0Edges.add(createAcceptEdge(stateKernel, symbol)); //GOON: with or without closure?
						}
						else if (shiftedSymbols.add(symbol)) //shift each symbol only once
						{
							//terminal or non-terminal
							
							//shift to other state
							Tuple2<LALR1CPState, List<CPGoToLink>> s = currentState.goToCP(symbol);
							LALR1CPState shiftedStateKernel = s.get1();
							List<CPGoToLink> shiftedStateCPLinks = s.get2();
							
							//we try to find out if there is already some state which has an equal
							//kernel to the shifted state (LALR1CPState equals on kernel)
							LALR1CPState equalStateLALR1CP;
							LALR1CPState gotoLinkTargetState;
							
							//we need to synchronize here because we access kernel2closure twice;
							//there must be no interruption between the get() and the put() call
							synchronized(kernel2closure) {
								equalStateLALR1CP = kernel2closure.get(shiftedStateKernel);
								gotoLinkTargetState = equalStateLALR1CP;
								if (equalStateLALR1CP == null)
								{
									//add new state
									LALR1CPState shiftedState = shiftedStateKernel.closure(grammarInfo);
									kernel2closure.put(shiftedStateKernel, shiftedState);
									threadPool.submit(new WorkerTaskLALR1(shiftedStateKernel, threadPool, taskCountLock));
									gotoLinkTargetState = shiftedState;
								}
							}
						
							//remember CP links (cp link contains closure of target state, not only kernel)
							for (CPGoToLink link : shiftedStateCPLinks)
							{
								LALR1CPItem todoItem = link.getSource();
								if(goToLinks.put(todoItem, link.withTargetState(gotoLinkTargetState)) != null) {
									throw new RuntimeException("Double gotoLink!");
								}
							}
							
							//add edge
							lr0Edges.add(new Edge(stateKernel, symbol, shiftedStateKernel, item.getLR0Item()));
						}
					}
				}
				
				decrementTaskCount();
			} catch (Throwable e) {
				e.printStackTrace();
			}
			return 0;

		}
		
	}
	
	/**
	 * Task for the second step of the efficient LALR(1) parser generation.
	 * Propagates the lookahead symbols along the context propagation links.
	 */
	private class WorkerTaskCPLinks extends WorkerTask<LALR1CPItem> {
		
		private LALR1CPItem currentItem;
		
		public String toString() {
			return currentItem.toString();
		}
		
		public WorkerTaskCPLinks(LALR1CPItem item, ThreadPoolExecutor threadPool, ReentrantLock lock) {
			super(null, threadPool, lock); // we don't care for the current state in this WorkerTask
			currentItem = item;
		}

		/**
		 * Handles one state; creates new tasks by following context propagation links.
		 * @return 0 - we don't care about the return value, but the {@link Callable}
		 * 				interface forces us to return an Integer.
		 */
		public Integer call() {
			
			try {
				EfficientTerminalSet sourceItemLookaheads = lookaheads.get(currentItem);
				
				//go-to-links: propagate lookaheads to all target items
				CPGoToLink gotoLink = goToLinks.get(currentItem);
				if (gotoLink != null)
				{
					LALR1CPState targetState = gotoLink.getTargetState();
					LALR1CPItem targetItem = targetState.getItemWithLookaheadByLR0Item(gotoLink.getTargetItem());
					//add lookaheads to target item
					//if new lookaheads were found, create a new WorkerTask for the target item
					synchronized(lookaheads) {
						EfficientTerminalSet before = lookaheads.get(targetItem);
						EfficientTerminalSet after = before.plusAll(sourceItemLookaheads);
						if(!before.equals(after)) {
							lookaheads.put(targetItem,after);
							threadPool.submit(new WorkerTaskCPLinks(targetItem, threadPool, taskCountLock));
						}
					}
				}
				
				//closure-links
				for (LR0Item closureLink : currentItem.getClosureLinks())
				{
					LALR1CPState targetState = itemStates.get(currentItem); //same state as current item
					LALR1CPItem targetItem = targetState.getItemWithLookaheadByLR0Item(closureLink);
					//add lookaheads to target item
					//if new lookaheads were found, create a new WorkerTask for the target item
					synchronized(lookaheads) {
						EfficientTerminalSet before = lookaheads.get(targetItem);
						EfficientTerminalSet after = before.plusAll(sourceItemLookaheads);
						after = after.plusAll(targetItem.getLookaheads());
						if(!before.equals(after)) {
							lookaheads.put(targetItem, after);
							threadPool.submit(new WorkerTaskCPLinks(targetItem, threadPool, taskCountLock));
						}
					}
				}
			} catch (Throwable e) {
				e.printStackTrace();
			}
			
			decrementTaskCount();
			
			return 0;
				
		}
	}

	/**
	 * Create a LALR(1)-Automaton concurrently.
	 * @param generator
	 * @param grammarInfo
	 * @param numThreads the number of threads that will be used for the creation.
	 * @return the generated automaton.
	 * @throws GeneratorException
	 */
	public Automaton<LR1Item, LR1State> createAutomaton(LRGenerator<LR1Item, LR1State> generator, 
		GrammarInfo grammarInfo, int numThreads)
		throws GeneratorException
	{

		this.generator = generator;
		this.grammarInfo = grammarInfo;
		this.numThreads = numThreads;
		
		initCreation();
		
		//create the start state (start production with dot at position 0 and "Placeholder" as lookahead)
		LR0Item startStateKernelItem = this.queue.remove(0).getLR0Kernel().getFirstItem();
		Set<LR0Item> startStateItemKernel = set();
		startStateItemKernel.add(startStateKernelItem);
		Set<LALR1CPItem> startStateItem = set();
		startStateItem.add(new LALR1CPItem(startStateKernelItem, grammarInfo.getTerminalSet(Placeholder)));
		LALR1CPState startStateKernel = new LALR1CPState(
			 startStateItem);
		
		kernel2closure = synchronizedMap();
		goToLinks = synchronizedMap();
		itemStates = synchronizedMap();
		lr0Edges = synchronizedSet();
		
		threadPool = new ThreadPoolExecutor(numThreads, numThreads, Long.MAX_VALUE, TimeUnit.NANOSECONDS, new LinkedBlockingQueue<Runnable>());
		taskCountLock = new ReentrantLock();

		shutDownThread = new Thread() {
			@Override public void run() {
				try {
					synchronized(threadPool) {
						threadPool.wait();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				threadPool.shutdown();
			}
		};
		// we need to ensure that the shutDownThread is started before the taskCount can be decremented to 0
		shutDownThread.start();
		// we need to ensure that the shutDownThread is already waiting to be notified
		while(!(shutDownThread.getState()).equals(Thread.State.WAITING)) {
		}
		
		// add one task for state0 to the thread pool and start the thread pool
		LinkedList<WorkerTaskLALR1> tasks = new LinkedList<WorkerTaskLALR1>();
		kernel2closure.put(startStateKernel, startStateKernel.closure(grammarInfo));
		
		taskCountLock.lock();
		try {
			taskCount = 0;
		} finally {
			taskCountLock.unlock();
		}
		tasks.add(new WorkerTaskLALR1(startStateKernel, threadPool, taskCountLock));
		try {
			threadPool.invokeAll(tasks);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// wait until the ThreadPool has been shut down
		try {
			shutDownThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		lookaheads = synchronizedMap();

		//now, since we have built the LALR(1)-CP automaton, we compute
		//all lookaheads by just following the CP links. Therefore, we just save the lookaheads
		//for each LALR1CPItem in a hashmap
		
		final ThreadPoolExecutor threadPoolCP = new ThreadPoolExecutor(numThreads, numThreads, Long.MAX_VALUE, TimeUnit.NANOSECONDS, new LinkedBlockingQueue<Runnable>());
		ReentrantLock taskCountLockCP = new ReentrantLock();

		// create a new thread that waits until all tasks are done
		Thread shutDownThreadCP = new Thread() {
			@Override public void run() {
				try {
					synchronized(threadPoolCP) {
						threadPoolCP.wait();
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				threadPoolCP.shutdown();
			}
		};
		shutDownThreadCP.start();
		
		// we need to ensure that the shutDownThread is already waiting to be notified
		while(!(shutDownThreadCP.getState()).equals(Thread.State.WAITING)) {
		}
		
		taskCountLockCP.lock();
		try {
			taskCount = 0;
		} finally {
			taskCountLockCP.unlock();
		}

		//initialize queue (consisting of kernels) with the start item kernel
		LinkedList<WorkerTaskCPLinks> tasksCP = new LinkedList<WorkerTaskCPLinks>();
		
		LALR1CPState st = kernel2closure.get(startStateKernel);
		LALR1CPItem firstItem = st.getItemWithLookaheadByLR0Item(startStateKernelItem);
		tasksCP.add(new WorkerTaskCPLinks(firstItem, threadPoolCP, taskCountLockCP));
		lookaheads.put(firstItem, firstItem.getLookaheads());

		EfficientTerminalSet empty = firstItem.getLookaheads().empty();
		for (LALR1CPState sta: kernel2closure.values()){
			for (LALR1CPItem ite : sta.getItems()){
				if (ite.getPosition()==0) { 
					tasksCP.add(new WorkerTaskCPLinks(ite, threadPoolCP, taskCountLockCP));
					lookaheads.put(ite,ite.getLookaheads());
				}
				else
					lookaheads.put(ite,empty);
			}
		}
		
		
		try {
			threadPoolCP.invokeAll(tasksCP);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// wait until the ThreadPool has been shut down
		try {
			shutDownThreadCP.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		//create states and edges from collected information
		Map<LALR1CPState, LR1State> lalr1CPToLR1Map = map(); 
		for (LALR1CPState state : kernel2closure.keySet())
		{
			HashSet<LR1Item> lr1Items = new HashSet<LR1Item>();
			LALR1CPState stateWithClosure = kernel2closure.get(state);
			
			for (LR0Item strippedItem : state.getStrippedItems()){
				LALR1CPItem item = stateWithClosure.getItemWithLookaheadByLR0Item(strippedItem);
				EfficientTerminalSet terminals = lookaheads.get(item);
				lr1Items.add(new LR1Item(strippedItem, terminals));
			}
			LR1State lr1State = new LR1State(lr1Items);
			lalr1CPToLR1Map.put(state, lr1State);
			dfaStates.add(lr1State);
		}
		
		//fill dfaEdges
		for (Edge edge : lr0Edges)
		{
			this.dfaEdges.add(new Edge(lalr1CPToLR1Map.get(edge.getSrc()),
				edge.getSymbol(), lalr1CPToLR1Map.get(edge.getDest()), edge.getSrcItem()));
		}
		
		printDebugResult();
		
		return ret;
	}

	@Override
	public Automaton<LR1Item, LR1State> createAutomaton(
			LRGenerator<LR1Item, LR1State> generator, GrammarInfo grammarInfo)
			throws GeneratorException {
		return createAutomaton(generator, grammarInfo, defaultNumThreads);
	}
	

}
