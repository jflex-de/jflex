package edu.tum.cup2.generator;

import static edu.tum.cup2.generator.Verbosity.Detailled;
import static edu.tum.cup2.generator.Verbosity.None;
import static edu.tum.cup2.generator.Verbosity.Sparse;
import static edu.tum.cup2.generator.Verbosity.Verbose;
import edu.tum.cup2.grammar.Grammar;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.ReentrantLock;
import java.io.PrintStream;

import edu.tum.cup2.generator.exceptions.GeneratorException;
import edu.tum.cup2.generator.items.*;
import edu.tum.cup2.generator.states.*;

/**
 * AutomatonFactory is a Factory for an Automaton that creates
 * an Automaton (used in an LRGenerator)
 * 
 * @author Daniel Altmann
 * @author Michael Hausmann
 * 
 */
public abstract class AutomatonFactory<I extends Item, S extends LRState<I>>
	implements IAutomatonFactory<I,S>
{
	Automaton<I, S> ret = null; //Automaton that is going to be returned by createAutomaton
	GrammarInfo grammarInfo = null; //grammarInfo used during creation
	Verbosity verbosity = null;
	PrintStream debugOut = null;
	Grammar grammar = null;
	LRGenerator<I,S> generator = null;
	List<S> queue = null;
	S state = null; //current state during creation of the automaton
	S stateKernel = null; //current state kernel 
	Set<Edge> dfaEdges = null;
	Set<S> dfaStates = null;
	int statesCounterMsgStep;
	int statesCounterMsgNext;
	int iterationsCounter;
	boolean debug = false;
	
	// the following variables are used for the parallel AutomatonFactories
	protected int numThreads;
	protected final static int defaultNumThreads = 4;
	protected ThreadPoolExecutor threadPool;
	protected S state0;
	protected int taskCount = 0;
	protected ReentrantLock taskCountLock;
	protected Thread shutDownThread;
	
	/**
	 * Used in the parallel AutomatonFactories.
	 * A WorkerTask handles one state of the automaton and creates new 
	 * WorkerTasks for states acquired by shifting.
	 * @author Johannes Schamburger
	 *
	 * @param <WS> type of state used
	 */
	protected abstract class WorkerTask<WS> implements Callable<Integer> {

		protected WS stateKernel;
		protected WS currentState;
		protected ThreadPoolExecutor threadPool;
		protected ReentrantLock taskCountLock;

		/**
		 * Constructor for a new WorkerTask.
		 * @param state the state to be handled by this task.
		 */
		public WorkerTask(WS state, ThreadPoolExecutor threadPool, ReentrantLock lock) {
			this.stateKernel = state;
			this.threadPool = threadPool;
			this.taskCountLock = lock;
			incrementTaskCount();
		}
		
		/**
		 * When a thread for this task is started by the ThreadPool, this method is called.
		 */
		public Integer call() {
			return 0;
		}
		
		/**
		 * Decrements the number of running tasks.
		 * If this number is 0, the ThreadPool is notified.
		 */
		protected void decrementTaskCount() {
			taskCountLock.lock();
			try {
				taskCount--;
//				System.out.println("\ttaskCount decremented to "+taskCount+" by "+Thread.currentThread());
				// check if this task was the last
				if(taskCount == 0) { 
					// wake up shutdown-thread
					synchronized(threadPool) {
						threadPool.notify();
					}
				}
			} catch(Exception e) {
				e.printStackTrace();
			} finally {
				taskCountLock.unlock();
			}
		}

		/**
		 * Increments the number of running tasks.
		 */
		protected void incrementTaskCount() {
			taskCountLock.lock();
			try {
				taskCount++;
//				System.out.println("taskCount incremented to "+taskCount+" by "+Thread.currentThread());
			} finally {
				taskCountLock.unlock();
			}
		}
		
	}
	
	/**
	 * Method for creating an Automaton - to be overridden in subclasses
	 */
	public abstract Automaton<I, S> createAutomaton ( 
			LRGenerator<I, S> generator, 
			GrammarInfo grammarInfo)
	throws GeneratorException;
	
	/**
	 * init variables
	 */
	protected void initCreation()
	{
		//Debug Output
		verbosity = generator.getVerbosity();
		debugOut = generator.getDebugOut();
		grammar = generator.getGrammar();
		
		//start state
		state0 = (S) generator.createStartState();
		
		queue = new LinkedList<S>();
		queue.add(state0);
		
		//set of DFA states and edges created so far
		ret = new Automaton<I, S>(state0);
		dfaStates = ret.getStates(); //shortcut
		dfaEdges = ret.getEdges(); //shortcut
		
		//debug messages
		debug = (verbosity != None); //flag if no debugging is requested
		if (debug && verbosity.ordinal() >= Sparse.ordinal())
		{
			debugOut.println("Terminals:    " + grammar.getTerminals().size());
			debugOut.println("NonTerminals: " + grammar.getNonTerminals().size());
			debugOut.println("Productions:  " + grammar.getProductionCount());
		}
		statesCounterMsgStep = verbosity.getStatesCounterStep();
		statesCounterMsgNext = statesCounterMsgStep;
		iterationsCounter = 0;	
	}
	
	/**
	 * print debug information
	 */
	protected final void printDebugMessages()
	{
		//debug messages
		iterationsCounter++;
		if (debug)
		{
			if (verbosity.getStatesCounterStep() > 0 && dfaStates.size() >= statesCounterMsgNext)
			{
				debugOut.println("Number of states >= " + statesCounterMsgNext);
				if (verbosity.ordinal() >= Verbose.ordinal())
				{
					debugOut.println("  Queue: " + queue.size() + 1 + ", DFA states: " + dfaStates.size() + ", DFA edges: " + dfaEdges.size() +
						", Iterations: " + iterationsCounter);
				}
				statesCounterMsgNext += statesCounterMsgStep;
			}
			if (verbosity == Detailled)
			{
				debugOut.println(stateKernel);
			}
			//find all shiftable items and shift them
			if (verbosity == Detailled)
			{
				debugOut.println("  Number of kernel items in current state:      " + stateKernel.getItemsCount());
				debugOut.println("  Number of items in closure of current state:  " + state.getItemsCount());
			}
		}
	}
	
	/**
	 * print debug information after automaton creation
	 */
	protected void printDebugResult()
	{
		if (verbosity != Verbosity.None)
		{
			debugOut.println("Automaton: DFA states: " + dfaStates.size() + ", DFA edges: " + dfaEdges.size() +
				", Iterations: " + iterationsCounter);
		}
	}

} /*end of AutomatonFactory*/
