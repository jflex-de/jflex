package edu.tum.cup2.parser;

import static edu.tum.cup2.semantics.SymbolValue.NoValue;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import edu.tum.cup2.grammar.AuxiliaryLHS4SemanticShiftAction;
import edu.tum.cup2.grammar.Production;
import edu.tum.cup2.grammar.SpecialTerminals;
import edu.tum.cup2.grammar.Terminal;
import edu.tum.cup2.parser.actions.Accept;
import edu.tum.cup2.parser.actions.ConsecutiveNonAssocAction;
import edu.tum.cup2.parser.actions.ErrorAction;
import edu.tum.cup2.parser.actions.LRAction;
import edu.tum.cup2.parser.actions.Reduce;
import edu.tum.cup2.parser.actions.Shift;
import edu.tum.cup2.parser.exceptions.ConsecutiveNonAssocException;
import edu.tum.cup2.parser.exceptions.EndOfInputstreamException;
import edu.tum.cup2.parser.exceptions.ErrorActionException;
import edu.tum.cup2.parser.exceptions.ErrorStateException;
import edu.tum.cup2.parser.exceptions.LRParserException;
import edu.tum.cup2.parser.exceptions.MissingErrorRecoveryException;
import edu.tum.cup2.parser.states.ErrorState;
import edu.tum.cup2.parser.states.LRParserState;
import edu.tum.cup2.parser.tables.LRParsingTable;
import edu.tum.cup2.scanner.InsertedScannerToken;
import edu.tum.cup2.scanner.Scanner;
import edu.tum.cup2.scanner.ScannerToken;
import edu.tum.cup2.semantics.Action;
import edu.tum.cup2.semantics.ActionPerformer;
import edu.tum.cup2.semantics.ErrorInformation;


/**
 * Implementation of an LR parser.
 * 
 * It is initialized with a {@link LRParsingTable} which is used over the whole
 * lifetime of this parser.
 * 
 * When a {@link Scanner} is given, the input is parsed accordingly.
 * 
 * @author Andreas Wenger
 * @author Stefan Dangl (SD)
 */

public class LRParser extends AParser implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private final boolean DEBUG = false; // TODO: debug messages on the terminal
	private int maxErrors = Integer.MIN_VALUE;
	
	// big fat table
	protected LRParsingTable table;
	
	// stacks
	private Stack<LRParserState> stack;
	private Stack<Object> valueStack;
	private Stack<Integer> tokenCountStack;
	
	// list of parsed tokens (backtracking,error-recovery)
	private boolean saveTokens;
	private List<ScannerToken<? extends Object>> parsedTokens;
	
	// stuff for error-recovery
	private int lastError_sync_size;
	private int lastError_start_sync_size;
	private Stack<LRParserState> lastError_stateStack = null;
	private Stack<Integer> lastError_tokenCountStack = null;
	private List<LRAction> dryRun_savedActions = null;
	ErrorInformation lastErrorInformation = null;
	
	// other things
	Scanner input = null;
	ScannerToken<? extends Object> currentToken;
	LRParserState currentState;
	
	
	/**
	 * Creates a new {@link LRParser}, using the given {@link LRParsingTable}.
	 */
	public LRParser(LRParsingTable table)
	{
		this.table = table;
	}
	
	
/**
	 * Parses the stream of symbols that can be read from the given
	 * {@link Scanner}. A single parser-instance may not be used for
	 * concurrent parsing! If used so, the second call to {@link #parse)
	 * returns null.
	 * 
	 * @return
	 * If the start-production has an action returning a value,
	 * it will be returned. Otherwise null.
	 * 
	 * @throws MissingErrorRecoveryException
	 *          If an error was encountered in the input and error-recovery
	 *          failed due to missing error-recovery productions.
	 * @throws EndOfInputstreamException
	 * 					If the input ended too early, e.g. if the
	 * 					{@link edu.tum.cup2.scanner.Scanner Scanner} returned
	 * 					the special token EndOfInputStream in the middle of a 
	 * 					production.
	 * @throws IOException
	 *          Re-thrown in case of the scanner throwing IOException on
	 *          {@link edu.tum.cup2.scanner.Scanner#readNextTerminal()
	 *          readNextTerminal()}.
	 * @throws ErrorStateException
	 *          In case of the critical error of the parser (in detail the
	 *          goto-table) not being compliant to the productions (in detail the
	 *          left-hand-side).
	 * @throws ErrorActionException
	 *          In case of the critical error of a non-compliant action-table.
	 */
	
	public synchronized Object parse(Scanner input, Object... initArgs) throws LRParserException, IOException
	{
		return parse(input, false, initArgs);
	}
	
	
	public synchronized Object parse(Scanner input, boolean saveTokens, Object... initArgs) throws LRParserException,
			IOException
	{
		try
		{
			if (this.input != null)
				return null;
			
			// initialization
			this.input = input;
			this.saveTokens = saveTokens;
			table.getParserInterface().init(this, initArgs);
			
			// create stack and initialize it with start state
			stack = new Stack<LRParserState>();
			stack.push(table.getStartState());
			
			// create stack for the semantic values
			valueStack = new Stack<Object>();
			valueStack.push(NoValue);
			
			if (saveTokens)
			{
				// create stack indicating number of tokens
				tokenCountStack = new Stack<Integer>();
				tokenCountStack.push(new Integer(0));
				
				// create list of parsed tokens
				parsedTokens = new ArrayList<ScannerToken<? extends Object>>();
			}
			
			// initialize variables for error-recovery (mainly dry run)
			lastError_sync_size = 0;
			lastError_start_sync_size = 0;
			lastError_stateStack = null;
			lastError_tokenCountStack = null;
			dryRun_savedActions = null;
			
			// read all symbols
			currentToken = readNextToken();
			while (true)
			{
				currentState = stack.peek();
				// look up an action in the action table
				LRAction action = table.getActionTable().get(currentState, currentToken.getSymbol());
				
                                // if consecutive non-associative terminals are encountered,
                                // we yield a compiler error!
                                if (action instanceof ConsecutiveNonAssocAction){
                                    throw new ConsecutiveNonAssocException(currentToken);
                                }
                                
				// if error-action (i.e. unexpected terminal) encountered,
				// try reducing first - but only if only one single reduction is possible!
				if (action instanceof ErrorAction)
				{
					LRAction reduction = searchSingleReduction();
					if (reduction != null)
					{
						if (DEBUG)
							System.out.println("Trying to reduce before recovering error!");
						action = reduction;
					}
				}
				
				// ACTION : SHIFT
				
				if (action instanceof Shift)
				{
					// shift action
					Shift shift = (Shift) action;
					if (DEBUG)
						System.out.println("S: to state " + shift.getState().getID() + " (" + currentToken.getSymbol() + ")"); // TEST
					// push the given state onto the stack, i.e. it becomes the current
					// state
					LRParserState s = shift.getState();
					if (currentToken.getLine() != -1 || currentToken.getColumn() != -1)
					{
						s = (LRParserState) (shift.getState().clone());
						s.beginLine = currentToken.getLine();
						s.beginColumn = currentToken.getColumn();
					}
					stack.push(s);
					
					if (parsedTokens != null)
					{
						// save read symbol as single item in a list on top of the tokenStack
						parsedTokens.add(currentToken);
						if (saveTokens)
							tokenCountStack.push(1);
						// System.out.println("saving "+currentToken);
					}
					
					/**
					 * if the parser is currently in error state, i.e. dry run
					 * - indicated by lastError_sync_size > 0 - do not perform
					 * semantic actions, but save them and execute
					 * them if and when dry run completes correctly.
					 **/
					if (lastError_sync_size > 0)
					{
						dryRun_savedActions.add(action);
						lastError_sync_size--;
						if (lastError_sync_size == 0)
						{
							if (DEBUG)
								System.out.println("DRY RUN COMPLETE - now performing actions on valueStack ..");
							dryRun_doReturnToNormal();
						}
					} else
					{
						// put semantic value of the symbol onto the stack (or NoValue if it has none)
						valueStack.push(currentToken.hasValue() ? currentToken.getValue() : NoValue);
					}
					// read next symbol from input stream
					currentToken = readNextToken();
					
					
					// ACTION : REDUCE
					
				} else if (action instanceof Reduce)
				{
					// reduce action
					Reduce reduce = (Reduce) action;
					Production production = reduce.getProduction();
					int numTokensPopped = 0;
					
					if (DEBUG)
						System.out.println("R: " + production.toString() + " (" + currentToken.getSymbol() + ")"); // TEST
						
					LRParserState lastBeginInfo = stack.peek();
					
					/**
					 * if the parser is currently in error state, i.e. dry run
					 * - indicated by lastError_sync_size > 0 - do not perform
					 * semantic actions, but save them and execute
					 * them if and when dry run completes correctly.
					 **/
					if (lastError_sync_size > 0)
					{
						dryRun_savedActions.add(action);
						// for each symbol in the right-hand side of rule, remove one state from
						// the stack and NOT from the value stack
						for (int i = 0; i < production.getRHSSizeWithoutEpsilon(); i++)
						{
							lastBeginInfo = stack.pop();
							if (saveTokens)
								numTokensPopped += tokenCountStack.pop();
						}
					} else
					{
						// perform the assigned semantic action
						Action reduceAction = reduce.getAction();
						Object newValue = NoValue;
						;
						if (reduceAction != null)
						{
							newValue = ActionPerformer
									.perform(
											reduceAction,
											valueStack,
											production.getRHSSizeWithoutEpsilon()
													+ ((production.getLHS() instanceof AuxiliaryLHS4SemanticShiftAction) ? ((AuxiliaryLHS4SemanticShiftAction) production
															.getLHS()).numPrecedingSymbolsNotEpsilon : 0));
						}
						// for each symbol in the right-hand side of rule, remove one state from
						// the stack and from the value stack
						for (int i = 0; i < production.getRHSSizeWithoutEpsilon(); i++)
						{
							lastBeginInfo = stack.pop();
							valueStack.pop();
							if (saveTokens)
								numTokensPopped += tokenCountStack.pop();
						}
						// put the semantic value of this production onto the value stack
						valueStack.push(newValue);
					}
					// push a new state onto the stack (i.e. set the current state),
					// namely the state that is found in the goto table at the position
					// of the current state and the left-hand side of the rule which was
					// reduced
					LRParserState newState = table.getGotoTable().get(stack.peek(), production.getLHS());
					if (newState instanceof ErrorState)
					{
						table.getParserInterface().exit(this);
						throw new ErrorStateException(stack.peek(), production.getLHS());
					} else
					{
						LRParserState s = (LRParserState) newState.clone();
						s.beginColumn = lastBeginInfo.beginColumn;
						s.beginLine = lastBeginInfo.beginLine;
						stack.push(s);
						if (DEBUG)
							System.out.println("G: to state " + newState.getID() + " (" + currentToken.getSymbol() + ")"); // TEST
					}
					// put the number of tokens used for this production onto the tokenStack
					if (saveTokens)
						tokenCountStack.push(numTokensPopped);
					
					
					// ACTION : ACCEPT
					
				} else if (action instanceof Accept)
				{
					/*
					 * // assume here is highest memory-footprint
					 * System.gc();
					 * System.runFinalization();
					 * System.gc();
					 * System.out.println(Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory());
					 */
					// accept action
					if (lastError_sync_size > 0)
					{
						// DRY RUN ACTIVE ON COMPLETION
						if (DEBUG)
							System.out.println("FINISHED, BUT DRY RUN STILL ACTIVE - now performing actions on valueStack ..");
						parsedTokens.add(currentToken); // parsedTokens != null
						lastError_sync_size--;
						dryRun_doReturnToNormal();
					}
					if (DEBUG)
						System.out.println("FINISHED :-)");
					Object item = valueStack.peek();
					// free memory and indicate deinitialization
					stack = null;
					valueStack = null;
					tokenCountStack = null;
					this.input = null;
					table.getParserInterface().exit(this);
					// return item
					return item;
					
					
					// ACTION : ERROR
				} else if (action instanceof ErrorAction)
				{
					
					// ANDI TEST
					System.err.println("LINE: " + currentToken.getLine() + " / COLUMN: " + currentToken.getColumn());
					
					if (DEBUG)
						System.out.println("ErrorAction found! Starting error-recovery...");
					if (maxErrors > 0 || maxErrors == Integer.MIN_VALUE)
						startPhraseBasedErrorRecovery();
					else
						throw new LRParserException("Too many errors!");
					
					
					// ACTION : UNKNOWN
				} else
				{
					// TODO : Must never occur!
					if (DEBUG)
					{
						System.err.println("Critical internal error!\nUnknown action : " + action);
						if (action != null)
							System.err.println("         class : " + action.getClass().getSimpleName());
					}
					table.getParserInterface().exit(this);
					throw new ErrorActionException(currentState, currentToken.getSymbol());
				}
			}
		} catch (Exception e)
		{
			// free memory
			stack = null;
			valueStack = null;
			tokenCountStack = null;
			this.input = null;
			
			// runtime exceptions may occur in actions
			if (e instanceof RuntimeException)
				throw (RuntimeException) e;
			table.getParserInterface().exit(this);
			if (e instanceof LRParserException)
				throw (LRParserException) e;
			if (e instanceof IOException)
				throw (IOException) e;
			System.err.println("Internal error : Exception " + e.getClass() + " caught by the parser!");
			return null; // no other exceptions allowed!
		}
		/**
		 * unreachable code
		 * // free memory and inform parser-interface
		 * stack = null;
		 * valueStack = null;
		 * tokenCountStack = null;
		 * this.input = null;
		 * table.getParserInterface().exit(this);
		 **/
	}
	
	
	@SuppressWarnings("unchecked")
	private ScannerToken<? extends Object> readNextToken() throws IOException
	{
		ScannerToken<? extends Object> cur = input.readNextTerminal();
		/** Safety check **/
		if (cur == null)
			throw new IOException("Null-token received! Check your scanner implementation!");
		/** Check for inserted scanner-tokens **/
		if (cur instanceof InsertedScannerToken)
		{
			InsertedScannerToken<Object> inserted = (InsertedScannerToken<Object>) cur;
			if (inserted.getErrorInformation() != null)
				doNotifyObserversAbout(inserted.getErrorInformation());
		}
		
		// TEST ANDI
		// System.out.println(cur.getSymbol());
		
		return cur;
	}
	
	
	/**
	 * If in the current state only one single
	 * reduction is possible, this function returns it.
	 * Otherwise returns null.
	 **/
	private LRAction searchSingleReduction()
	{
		Reduce act = null;
		for (Terminal t : table.getActionTable().getColumns())
		{
			LRAction act2 = table.getActionTable().get(currentState, t);
			if (act2 instanceof Reduce)
			{
				if (act == null)
					act = (Reduce) act2;
				else if (((Reduce) act2).getProduction() != act.getProduction())
					return null;
			} else if (act2 instanceof Shift)
				return null;
		}
		return act;
	}
	
	
	/**
	 * This method may start dry-run.
	 * 
	 * @author Stefan Dangl
	 * 
	 *         <b>Phrase-based Error-Recovery</b>
	 * 
	 *         Definition: An error-recovering state or a state which is
	 *         able to perform error-recovery is a state which has an action
	 *         (shift or reduce) associated with the special terminal Error.
	 * 
	 *         While there is a state on top of the stack which is not able
	 *         to perform error-recovery, pop that state. If the stack runs
	 *         empty before any error-recovering state could be found, this
	 *         method has to throw a MissingErrorRecoveryException.
	 * 
	 *         After this procedure, the earliest production which recovers
	 *         from the error has been chosen and must be supplied with an
	 *         appropriate object of type ErrorInformation. It is until now
	 *         unknown to which extent the error must be recovered from.
	 *         Thus, it is required for the algorithm to read as much tokens
	 *         from the input-stream (the scanner) until normal parsing may
	 *         proceed.
	 * 
	 *         The state indicated to by the terminal Error in the action
	 *         table is the next state from which to continue reading
	 *         correctly fitting terminals. Pushing that state on the stack
	 *         early (before it's value has been determined) is possible,
	 *         because parsing is paused. As long as the currently processed
	 *         terminal is not adequate (i.e. no actions for this state and
	 *         current terminal are found in the action-table), it must be
	 *         added to the list of "bad" terminals and the next terminal is
	 *         to be read and checked the same way. This procedure continues
	 *         until a fitting terminal was read or the end of the input
	 *         stream was reached. In the latter case, the error could not
	 *         be recovered from and a suitable exception will be thrown
	 *         (currently EndOfInputstreamException). Otherwise, recovery
	 *         seems to be complete.
	 * 
	 *         A new instance of ErrorInformation - supplied with the
	 *         information about popped values, popped terminals and read
	 *         terminals - is to be pushed onto the stack.
	 * 
	 *         <b>Dry run</b>
	 * 
	 *         To prevent a single error from producing many error messages,
	 *         the parser remains in an error state until it processes
	 *         enough tokens following the error without hitting a new
	 *         error. The number of sufficient tokens must be provided by
	 *         the author of the grammar. Currently, this is a hard-coded value
	 *         (see method error_sync_size() below ). The procedure of
	 *         reading a specific amount of terminals before switching back
	 *         to normal processing-mode is also called "dry run", because
	 *         no semantic action is executed during this simulated
	 *         parsing-procedure.
	 * 
	 *         If another error is found during the dry run, the parser
	 *         merges the two errors as if they occured as one single error
	 *         before starting to recover from that second error (which
	 *         occured in error state and indicates only, that the decision
	 *         about completion of recovery based on one single terminal was
	 *         probably incomplete). The merging of two errors follows a
	 *         simple procedure: All terminals on the stack on top of the
	 *         previously pushed ErrorInformation are popped from the stack
	 *         and added to the end of the list of read terminals of the
	 *         ErrorInformation instance. States have to be popped
	 *         accordingly and are to be dismissed. There must not be any
	 *         changes on the value-stack except for the ErrorInformation
	 *         instance to be popped (and saved), because merging must only
	 *         occur when the parser is currently simulating (dry run). Now
	 *         the stacks are reverted to the point where the first error
	 *         occured and recovery can start again, while adding newly read
	 *         terminals to the saved ErrorInformation-instance, which will
	 *         then be pushed onto the stack.
	 * 
	 *         After completion of the dry run, the error is assumed to be
	 *         recovered from. At this point in time, the parser has to be
	 *         reverted to the position before entering the dry run in order
	 *         to parse the now following correct input which has been
	 *         consumed without executing appropriate production's semantic
	 *         actions. Alternatively, the semantic actions have to be
	 *         executed afterwards and the value-stack updated accordingly.
	 *         In CUP2 error-recovery implementation the later solution has
	 *         been chosen as it seems more straightforward.
	 * 
	 * @throws EndOfInputstreamException
	 * @throws MissingErrorRecoveryException
	 * @throws IOException .
	 * 
	 **/
	@SuppressWarnings("unchecked")
	private void startPhraseBasedErrorRecovery() throws IOException, EndOfInputstreamException,
			MissingErrorRecoveryException
	{
		if (DEBUG)
			System.out.println("Recover: PBER");
		
		LRParserState handleState;
		LinkedList<Object> popped_values = new LinkedList<Object>();
		LinkedList<ScannerToken<? extends Object>> popped_tokens = new LinkedList<ScannerToken<? extends Object>>();
		LinkedList<ScannerToken<? extends Object>> read_tokens = new LinkedList<ScannerToken<? extends Object>>();
		List<Terminal> expected_terminals = new ArrayList<Terminal>();
		ScannerToken<? extends Object> crash_token;
		int beginLine = -2;
		int beginColumn = -2;
		
		// Check if previous error has completely been recovered from (sync)
		if (lastError_sync_size > 0)
		{
			// previous error is still unsynchronized -> combine errors.
			if (DEBUG)
				System.out.println("Previous error is still unsynchronized (sync:" + lastError_sync_size
						+ ")! Combining errors...");
			if (saveTokens)
			{
				// find all terminals read between errors and
				// copy them to read_tokens
				int parsedTokensOffset = parsedTokens.size() - (lastError_start_sync_size - lastError_sync_size);
				for (int i = parsedTokensOffset; i < parsedTokens.size(); i++)
					read_tokens.add(parsedTokens.get(i));
			}
			// revert stacks
			stack = lastError_stateStack;
			tokenCountStack = lastError_tokenCountStack;
			handleState = stack.pop();
			ErrorInformation oldErrInfo = (ErrorInformation) valueStack.pop();
			beginLine = oldErrInfo.getBeginLine();
			beginColumn = oldErrInfo.getBeginColumn();
			crash_token = oldErrInfo.getCrashToken();
			if (!saveTokens)
			{
				tokenCountStack = null;
				parsedTokens = null;
			} else
				tokenCountStack.pop();
			// adapt information for new ErrorInformation object
			for (Object x : oldErrInfo.getPoppedValues())
				popped_values.add(x);
			if (saveTokens)
			{
				for (ScannerToken<? extends Object> x : oldErrInfo.getCorrectTokens())
					popped_tokens.add(x);
				for (int i = oldErrInfo.getBadTokens().length - 1; i >= 0; i--)
					read_tokens.addFirst(oldErrInfo.getBadTokens()[i]);
			}
			expected_terminals = Arrays.asList(oldErrInfo.getExpectedTerminals());
			// LR(0)
			if (lastError_sync_size == lastError_start_sync_size)
			{
				// nothing changed => read one token (LR(0))
				if (currentToken.getSymbol() != SpecialTerminals.EndOfInputStream)
				{
					read_tokens.add(currentToken);
				}
				currentToken = readNextToken();
			}
		} else
		{ // no previous unsynchronized error
			crash_token = currentToken;
			// This is a new error!
			// Create list of expected terminals
			for (Terminal t : table.getActionTable().getColumns())
			{
				if (t == SpecialTerminals.Error)
					continue;
				if (table.getActionTable().getWithNull(currentState, t) != null)
					expected_terminals.add(t);
			}
			// Search for state which can shift an error -> Reduces stack
			LRAction handleAction;
			try
			{
				LRParserState catchState = stack.peek();
				handleAction = table.getActionTable().get(catchState, SpecialTerminals.Error);
				int parsedTokensOffset = 0;
				beginColumn = currentToken.getColumn();
				beginLine = currentToken.getLine();
				while (!(handleAction instanceof Shift))
				{
					beginColumn = catchState.beginColumn;
					beginLine = catchState.beginLine;
					@SuppressWarnings("unused")
					LRParserState popped_state = stack.pop();
					Object pop = valueStack.pop();
					if (pop == null || !pop.equals(NoValue))
						popped_values.addFirst(pop);
					if (saveTokens)
					{
						int cnt = tokenCountStack.pop();
						int end = parsedTokens.size() + parsedTokensOffset;
						for (int i = end - 1; i >= end - cnt; i--)
							popped_tokens.addFirst(parsedTokens.get(i));
						parsedTokensOffset -= cnt;
					}
					catchState = stack.peek();
					handleAction = table.getActionTable().get(catchState, SpecialTerminals.Error);
				}
				if (DEBUG)
				{
					System.out.println("Found error-catching state  : " + catchState);
					System.out.println("  with popped values        : " + popped_values);
					System.out.println("  and popped tokens      : " + popped_tokens);
				}
			} catch (java.util.EmptyStackException e)
			{
				// notify observers about non-recoverable error.
				ErrorInformation errInf = new ErrorInformation(crash_token, false, popped_values.toArray(),
						popped_tokens.toArray(new ScannerToken[] {}), read_tokens.toArray(new ScannerToken[] {}),
						expected_terminals.toArray(new Terminal[] {}), crash_token.getLine(), crash_token.getColumn(),
						crash_token.getLine(), crash_token.getColumn());
				doNotifyObserversAbout(errInf);
				// throw exception.
				if (currentToken.getSymbol() == SpecialTerminals.EndOfInputStream)
				{
					if (DEBUG)
						System.out.println("End of input during error-recovery :(");
					throw new EndOfInputstreamException("Input does not match grammar, recovery was not possible.",
							currentState, currentToken);
				}
				if (DEBUG)
					System.out.println("No production for phrase-based error-recovery found :(");
				throw new MissingErrorRecoveryException(
						"Input does not match grammar. Grammar does not provide error-correction for current parsing.",
						currentState, currentToken, errInf);
			}
			
			handleState = ((Shift) handleAction).getState();
			if (DEBUG)
				System.out.println("  Error-shifting to state   : " + handleState);
		}
		
		// Search for terminal which let's us proceed.
		while (table.getActionTable().get(handleState, currentToken.getSymbol()) instanceof ErrorAction
				|| currentToken.getSymbol() == SpecialTerminals.EndOfInputStream)
		{
			if (currentToken.getSymbol() == SpecialTerminals.EndOfInputStream)
			{
				// notify observers about unrecovered error.
				doNotifyObserversAbout(new ErrorInformation(crash_token, false, popped_values.toArray(),
						popped_tokens.toArray(new ScannerToken[] {}), read_tokens.toArray(new ScannerToken[] {}),
						expected_terminals.toArray(new Terminal[] {}), beginLine, beginColumn, currentToken.getLine(),
						currentToken.getColumn()));
				// throw useful exception
				if (DEBUG)
					System.out.println("End of input during error-recovery :(");
				throw new EndOfInputstreamException("Input does not match grammar, recovery was not possible.",
						handleState, currentToken);
			}
			if (saveTokens)
				read_tokens.addLast(currentToken);
			currentToken = readNextToken();
		}
		
		// Push the correct state and the error-information object on the
		// value-stack
		LRParserState s = (LRParserState) handleState.clone();
		s.beginColumn = beginColumn;
		s.beginLine = beginLine;
		stack.push(s);
		lastErrorInformation = new ErrorInformation(crash_token, true, popped_values.toArray(),
				popped_tokens.toArray(new ScannerToken[] {}), read_tokens.toArray(new ScannerToken[] {}),
				expected_terminals.toArray(new Terminal[] {}), beginLine, beginColumn, currentToken.getLine(),
				currentToken.getColumn());
		valueStack.push(lastErrorInformation);
		if (saveTokens)
			tokenCountStack.push(lastErrorInformation.getTokens().length);
		
		// Initialize values for error-synchronization (dry run)
		lastError_stateStack = (Stack<LRParserState>) stack.clone();
		lastError_sync_size = table.getParserInterface().getErrorSyncSize();
		if (lastError_sync_size < 0)
			lastError_sync_size = 0;
		if (saveTokens)
			lastError_tokenCountStack = (Stack<Integer>) tokenCountStack.clone();
		else if (lastError_sync_size != 0)
			parsedTokens = new ArrayList<ScannerToken<? extends Object>>();
		
		lastError_start_sync_size = lastError_sync_size;
		dryRun_savedActions = new ArrayList<LRAction>();
		if (DEBUG)
			System.out.println("Switching to dry-run mode for " + lastError_sync_size + " tokens.");
	}
	
	
	/** This method performs everything to return from dry-run to normal mode **/
	private void dryRun_doReturnToNormal()
	{
		if (maxErrors > 0)
			maxErrors--;
		// Create list of tokens which have been read during dry run.
		int parsedTokensOffset = parsedTokens.size() - (lastError_start_sync_size - lastError_sync_size);
		// Perform saved semantic actions
		int tokenIndex = 0;
		doNotifyObserversAbout(lastErrorInformation);
		lastErrorInformation = null;
		for (LRAction action2 : dryRun_savedActions)
		{
			if (action2 instanceof Shift)
			{
				Shift shift2 = (Shift) action2;
				if (DEBUG)
					System.out.println("DryRunReturn2Normal S: to state " + shift2.getState().getID() + " ("
							+ parsedTokens.get(parsedTokensOffset + tokenIndex).getSymbol() + ")"); // TEST
				// put semantic value of the symbol onto the stack (or NoValue if it has
				// none)
				valueStack.push(parsedTokens.get(parsedTokensOffset + tokenIndex).hasValue() ? parsedTokens.get(
						parsedTokensOffset + tokenIndex).getValue() : NoValue);
				// "read" next token
				tokenIndex++;
			} else if (action2 instanceof Reduce)
			{
				Reduce reduce = (Reduce) action2;
				if (DEBUG)
					System.out.println("DryRunReturn2Normal R: " + reduce.getProduction().toString() + " ("
							+ parsedTokens.get(parsedTokensOffset + tokenIndex).getSymbol() + ")"); // TEST
				// perform the assigned semantic action
				Action reduceAction = reduce.getAction();
				Object newValue = NoValue;
				if (reduceAction != null)
				{
					newValue = ActionPerformer
							.perform(
									reduceAction,
									valueStack,
									reduce.getProduction().getRHSSizeWithoutEpsilon()
											+ ((reduce.getProduction().getLHS() instanceof AuxiliaryLHS4SemanticShiftAction) ? ((AuxiliaryLHS4SemanticShiftAction) reduce
													.getProduction().getLHS()).numPrecedingSymbolsNotEpsilon : 0));
				}
				// for each symbol in the right-hand side of rule, remove one
				// item from the value stack
				for (int i = 0; i < reduce.getProduction().getRHSSizeWithoutEpsilon(); i++)
				{
					valueStack.pop();
				}
				// put the semantic value of this production onto the value stack
				valueStack.push(newValue);
			} else
			{
				// MUST NEVER OCCUR! CAN NOT OCCUR! IS EVIL!
				throw new RuntimeException(
						"Critical internal error in parser driver : Found illegal action while returning to normal mode from dry run!");
			}
		}
		// kill data-structures required for incomplete dry run.
		if (!saveTokens)
		{
			parsedTokens = null;
			tokenCountStack = null;
		}
		lastError_stateStack = null;
		lastError_tokenCountStack = null;
		dryRun_savedActions = null;
		// reset to normal mode
		lastError_sync_size = 0;
		lastError_start_sync_size = 0;
	}
	
	
	/*
	 * Methods called from outside
	 * ( mainly from inside the current specification /
	 * the current ParserInterface )
	 */
	
	public boolean initialized()
	{
		return stack != null;
	}
	
	
	private void doNotifyObserversAbout(ErrorInformation e)
	{
		table.getParserInterface().error(e);
		notifyObserversAbout(e);
	}
	
}
