package edu.tum.cup2.generator;

import static edu.tum.cup2.grammar.SpecialTerminals.EndOfInputStream;

import java.util.LinkedList;
import java.util.concurrent.Callable;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import edu.tum.cup2.generator.exceptions.GeneratorException;
import edu.tum.cup2.generator.items.*;
import edu.tum.cup2.generator.states.*;
import edu.tum.cup2.grammar.Symbol;

/**
 * Factory for a LR(1) automaton, that is computed concurrently.
 * 
 * @author Daniel Altmann
 * @author Michael Hausmann
 * @author Johannes Schamburger
 * 
 */
public class LR1ParallelAutomatonFactory extends AutomatonFactory<LR1Item, LR1State>
{
	private class WorkerTaskLR1 extends WorkerTask<LR1State> {

		public WorkerTaskLR1(LR1State state, ThreadPoolExecutor threadPool, ReentrantLock lock) {
			super(state, threadPool, lock);
		}
		
		/**
		 * Handles one state; creates new tasks by shifting.
		 * @return 0 - we don't care about the return value, but the {@link Callable}
		 * 				interface forces us to return an Integer.
		 */
		public Integer call() {

			// for all states, find their edges to other (possibly new) states
			// Appel says: "until E and T did not change in this iteration".
			// but: do we really need E here? I ignored it
//			System.out.println(Thread.currentThread() + " working on... " + stateKernel);

			// debug messages
			printDebugMessages();

			currentState = stateKernel.closure(grammarInfo); // unpack state (from kernel to closure)

			for (LR1Item item : currentState.getItems()) {

				if (item.isShiftable()) {
					Symbol symbol = item.getNextSymbol();
					if (symbol == EndOfInputStream) {
						// $-symbol: here we accept
						dfaEdges.add(Edge.createAcceptEdge(stateKernel, symbol));
					} else {
						// terminal or non-terminal
						LR1State shiftedState = currentState.goTo(symbol);
						// new state?
						if (dfaStates.add(shiftedState)) {
							threadPool.submit(new WorkerTaskLR1(shiftedState, threadPool, taskCountLock));
						}
						// add the edge
						dfaEdges.add(new Edge(stateKernel, symbol, shiftedState, item.getLR0Kernel()));

					} /* end else */
				} /* end if */
			} /* end for */
			
			decrementTaskCount();
			
			return 0;

		}

		
	}

	/**
	 * Creates a LR(1) automaton concurrently.
	 * @param generator
	 * @param grammarInfo
	 * @param numThreads the number of threads that will be used for the creation.
	 * @return the generated automaton.
	 * @throws GeneratorException
	 */
	public Automaton<LR1Item, LR1State> createAutomaton(
			LRGenerator<LR1Item, LR1State> generator, GrammarInfo grammarInfo, int numThreads)
			throws GeneratorException {
		this.generator = generator;
		this.grammarInfo = grammarInfo;
		this.numThreads = numThreads;
		
		initCreation();
		
		threadPool = new ThreadPoolExecutor(numThreads, numThreads, Long.MAX_VALUE, TimeUnit.NANOSECONDS, new LinkedBlockingQueue<Runnable>());
		taskCountLock = new ReentrantLock();

		// create a new thread that waits until all tasks are done
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
		shutDownThread.start();
		
		// we need to ensure that the shutDownThread is already waiting to be notified
		while(!(shutDownThread.getState()).equals(Thread.State.WAITING)) {
		}
		
		taskCountLock.lock();
		try {
			taskCount = 0;
		} finally {
			taskCountLock.unlock();
		}
		// add one task for state0 to the thread pool and start the thread pool
		LinkedList<WorkerTaskLR1> tasks = new LinkedList<WorkerTaskLR1>();
		tasks.add(new WorkerTaskLR1(state0, threadPool, taskCountLock));
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
		printDebugResult();

		return ret;
	}

	@Override
	public Automaton<LR1Item, LR1State> createAutomaton(
			LRGenerator<LR1Item, LR1State> generator, GrammarInfo grammarInfo)
			throws GeneratorException {
		return createAutomaton(generator, grammarInfo, defaultNumThreads);
	}


} /*end of AutomatonFactory*/
