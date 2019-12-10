package edu.tum.cup2.scanner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

import edu.tum.cup2.grammar.Production;
import edu.tum.cup2.grammar.SpecialTerminals;
import edu.tum.cup2.grammar.Terminal;
import edu.tum.cup2.parser.actions.Accept;
import edu.tum.cup2.parser.actions.ErrorAction;
import edu.tum.cup2.parser.actions.LRAction;
import edu.tum.cup2.parser.actions.Reduce;
import edu.tum.cup2.parser.actions.Shift;
import edu.tum.cup2.parser.states.ErrorState;
import edu.tum.cup2.parser.states.LRParserState;
import edu.tum.cup2.parser.tables.LRParsingTable;
import edu.tum.cup2.semantics.ErrorInformation;
import edu.tum.cup2.spec.Insertable;
import edu.tum.cup2.util.Reflection;
import edu.tum.cup2.util.TokenNumberGenerator;


public class InsertionScanner implements Scanner
{
	private final boolean								DEBUG				= false;	// TODO: debug messages on the terminal
	private final boolean								DEBUG_possibilities	= true;	// TODO: debug messages on the terminal
	private final boolean								DEBUG_statistics	= false;	// TODO: debug messages on the terminal
	private int											maxErrors			= 20;

	//private final long								maxTime4Insertion	= 1000;	// 1s // TODO : implement??

	/** working sets **/
	private Queue<ScannerToken<? extends Object>>		processedTokens;				// tokens which can not be cured
	private ArrayList<ScannerToken<? extends Object>>	changeAbleTokens;				// tokens which may be changed
	private LinkedList<LRAction>						changeAbleActions;				// actions for the changeable-stack
	private TokenNumberGenerator						numGen				= null;	// number generator

	/** parameters **/
	private int											insertionBufferLength;
	private int											maxIns;
	private int											maxOversize;
	private int											maxPossibilities;

	/** normal parsing with simple pber**/
	private LRParsingTable								table;
	private BufferedScanner								input;
	private ScannerToken<? extends Object>				currentToken		= null;

	private Stack<LRParserState>						rdStack;
	private LRParserState								rdState;
	private LRAction									rdAction;
	private Stack<LRParserState>						chgStack;

	/** statistics **/
	private int											bufferPeak;
	private int											insertedTokens;

	/** other **/

	/** read a token **/
	public ScannerToken<? extends Object> readNextTerminal() throws IOException {
		if (!DEBUG)
		{
			/** return tokens which can not be cured immediately **/
			ScannerToken<? extends Object> pt = processedTokens.poll();
			if (pt!=null)
				return pt;
		}
		
		/** end not reached? **/
		if (maxErrors>0 &&
			(
				currentToken==null || 
				currentToken.getSymbol() != SpecialTerminals.EndOfInputStream
			)
		) {
			if (DEBUG_statistics)
				if (changeAbleTokens.size()>bufferPeak) bufferPeak = changeAbleTokens.size();
			/** find a solution using the insertionBufferLength tokens.**/
			while_bufferFill:while(
				( numGen!=null && processedTokens.size()==0)
				||
				changeAbleTokens.size()+processedTokens.size()<insertionBufferLength)
			{
				/*if (DEBUG_statistics)
					if (changeAbleTokens.size()>bufferPeak) bufferPeak = changeAbleTokens.size();*/
				rdState = rdStack.peek();
				if (currentToken==null)
				{
					/** - ask the oracle numGen : shall we insert or read some token?**/
					if (numGen!=null &&	 				// if oracle unavailable, we try it without insertion ;)
						numGen.tokenInsertTries>=0 && 	// no token-solution found (optimistic sloppy stop)
						numGen.hasNumberNotMinimumOrNull(numGen.position++) // does oracle say "insert"?
					) {
						/** oracle says : you may succeed by inserting a token now ! **/
						/*if (DEBUG)
							System.out.println("Inserting token...");*/
						
						/** do not allow too much insertions **/
						/** and do only insert if not enough possibilities known **/
						
						
						if (numGen.possibilitiesBefore-numGen.startPosition*numGen.startPosition>maxPossibilities ||
							numGen.position>numGen.startPosition+maxOversize-numGen.anzTooMuch)
						{
							/*if ((numGen.inserted&0x01)!=0) // some "random" factor
							{*/
								if (numGen.tooMuchPosition==-1)
								{
									if (DEBUG) // TODO
									{
										if (numGen.possibilitiesBefore>maxPossibilities)
											System.out.println("!!! Too much possibilities before : "+numGen.possibilitiesBefore);
										else
											System.out.println("!!! Too much insertions, indicating previous insertion erroneous");
									}
									numGen.tooMuchPosition = numGen.position-1;
									numGen.possibilitiesBeforeTooMuch = numGen.possibilitiesBefore;
									if (numGen.anzTooMuch<maxOversize-2) // spare two token-insertions before startPosition.
										numGen.anzTooMuch++;
								}
								currentToken = getNextToken(); // if we're lucky, we succeed now.
								continue while_bufferFill;
							//}
						}
						
						/** - create list of expected and insertable tokens **/
						ArrayList<Insertable> insertables = new ArrayList<Insertable>();
						List<Terminal> expected = new ArrayList<Terminal>();
						boolean noInsertablesAnyMore = false;
						currentToken = getNextToken();
						//boolean[] ;
						for (Terminal t : table.getActionTable().getColumns()) {
							if (t == currentToken.getSymbol()
								|| t == SpecialTerminals.EndOfInputStream
								|| t == SpecialTerminals.Error)
								continue;
							LRAction act = table.getActionTable().getWithNull(rdState, t);
							if (act == null) continue; // error-action
							expected.add(t); // calculate expected set								
							if (noInsertablesAnyMore) continue; // columns are sorted by insertion
							Insertable ins = null;
							try { ins = (Insertable) Reflection.getFieldValueFromObject(t, "insert");
							} catch (Exception e) {}
							if (ins == null)
							{
								noInsertablesAnyMore = true;
								continue;
							}
							insertables.add(ins);
						}
						// return lookahead
						input.pushBackToken(currentToken);// currentToken = null;
						
						/** - get terminal (and count insertion) **/
						Integer terminalToChoose = -1;
						boolean doCount = (numGen.position > numGen.lastBreakPosition);
						try{
							terminalToChoose = numGen.getTokenNumber(
								numGen.position-1, -1, insertables.size() - 1);
						}catch (RuntimeException e)
						{
							if (DEBUG) System.out.println("No solution found!");
							numGen.tokenInsertTries = 0;
						}
						
						/** - Check value further (TODO:Remove)**/		// TODO : Remove
						if (terminalToChoose >= insertables.size()) {
							System.err.println(
								"Reinterpreting token due to internal error: " +
								"numGen-oracle exceeded maxtoken");
							// TODO if this block stays here!!! numGen.possibilitiesBefore -=
							numGen.position--; // no token read or inserted
							currentToken = null;
							continue while_bufferFill;
						}
						/**		- 	no token! (numGen changed it's mind because it had no info before get)
						 * 			=> reinterpret token again  **/
						if (terminalToChoose==-1) {
							//System.out.println("Oracle was unlucky :("); // TODO : numGenBetter
							//numGen.position--; // current token has not changed yet
							currentToken = getNextToken();
							continue while_bufferFill;
						}
						/**		- repeating token (bad for recovery) **/
						Insertable chosenInsertable = insertables.get(terminalToChoose);
						while (chosenInsertable.times>0)
						{
							int t = chosenInsertable.times;
							if (t>changeAbleTokens.size())
								t = 0;
							else
								t = changeAbleTokens.size()-t;
							boolean oneEven = false;
							for (int i=t; i<changeAbleTokens.size(); i++) {
								ScannerToken<? extends Object> chg = changeAbleTokens.get(i);
								if ( /*chg instanceof InsertedScannerToken &&*/ chg.getSymbol() == chosenInsertable.symbol)
								{
									oneEven = true;
									break;
								}
							}
							if (oneEven)
							{
								//if (DEBUG) // TODO
									System.out.println("!!! Nearly inserted : " + chosenInsertable);
								numGen.possibilitiesBefore--; // this token is not possible anymore.
								if (++terminalToChoose>=insertables.size())
								{
									/** total recall **/
									currentToken = getNextToken();
									continue while_bufferFill;
								}
								else
								{
									/** All possibilities are continuations.
									 * 	Continue with next token, set value to max **/
									chosenInsertable = insertables.get(terminalToChoose);
									numGen.setValue(numGen.position-1,terminalToChoose);
								}
							}
							else
							{
								break; // everything ok.
							}
						}

						if (DEBUG) // TODO
							System.out.println("!!! Inserting token : " + chosenInsertable);
						
						/**		- optimistic (sloppy) stop **/
						if (doCount && numGen.tokenInsertTries > 0 )
							numGen.tokenInsertTries--;
						
						/** - insert artificial token with error information **/
						currentToken = new InsertedScannerToken<Object>(
							(Terminal) chosenInsertable.symbol,
							chosenInsertable.value,
							currentToken.getLine(), currentToken.getColumn(),
							new ErrorInformation(
								currentToken, true, new Object[] {},
								(ScannerToken<? extends Object>[]) new ScannerToken[] {},
								(ScannerToken<? extends Object>[]) new ScannerToken[] {},
								expected.toArray(new Terminal[expected.size()]),
								currentToken.getLine(), currentToken.getColumn(),
								currentToken.getLine(), currentToken.getColumn(),
								chosenInsertable.possible, chosenInsertable.value
							)
						);
						numGen.inserted++;
					}
					else
					{
						/** oracle says : you may succeed by not inserting a token now ! **/
						currentToken = getNextToken();
					}
				}
				
				/** get action from table **/
				rdAction = table.getActionTable().get(rdState, currentToken.getSymbol());
				
				/*if (DEBUG)
					System.out.println("   <- "+currentToken);*/
				
				/** is erroneous token **/
				if (rdAction instanceof ErrorAction)
				{
					/** - start token recovery **/
					if (DEBUG)
						System.out.println("Token-recovery...");
					

					/** 	- if (numGen==null) **/
					if (numGen==null)
					{ 
						if (DEBUG)
							System.out.println("Starting TBER");
						if (DEBUG_possibilities) {
							System.out.print("START    : ");
							System.out.print(this.processedTokens); //;//
							System.out.print(this.changeAbleTokens);
							System.out.println(currentToken);
						}
						/**			- create numGen **/
						numGen = new TokenNumberGenerator(changeAbleTokens.size());
						updateErrorSyncSize(true);
						numGen.startPosition = changeAbleTokens.size();
						numGen.lastInsertStart = changeAbleTokens.size();
						numGen.tokenInsertTries = maxIns;
						numGen.tooMuchPosition = -1;
						numGen.anzTooMuch = 0;
						/** 		- create checked-states (not impl) **/
						//checkedStates = new HashMap<IntState, Object>();
						/**			- return read-stack to change-stack*/
						rdStack = (Stack<LRParserState>) chgStack.clone();
						numGen.inserted = 0;
						/**			  & return truly read tokens (using new abstract scannerObject)**/
						if (!(currentToken instanceof InsertedScannerToken))
				    		input.pushBackToken(currentToken);
						for (int i=changeAbleTokens.size()-1; i>=0; i--)
					    {
					    	ScannerToken<? extends Object> tk = changeAbleTokens.remove(i);
					    	if (!(tk instanceof InsertedScannerToken))
					    		input.pushBackToken(tk);
					    }
						changeAbleActions.clear();
					    // start over (with empty changeAbleTokens list)
					    currentToken = null;
					    continue while_bufferFill;
					}else{
						/** numGen!=null **/
						//errorSyncSize = table.getParserInterface().getErrorSyncSize();
						//numGen.position
						if (numGen.tokenInsertTries == 0) {
							numGen.inserted = 0;
							numGen.tokenInsertTries = -1;
							/**			- return read-stack to change-stack*/
							rdStack = (Stack<LRParserState>) chgStack.clone();
							/**			  & return truly read tokens (using new abstract scannerObject)**/
							if (!(currentToken instanceof InsertedScannerToken))
					    		input.pushBackToken(currentToken);
							for (int i=changeAbleTokens.size()-1; i>=0; i--)
						    {
						    	ScannerToken<? extends Object> tk = changeAbleTokens.remove(i);
						    	if (!(tk instanceof InsertedScannerToken))
						    		input.pushBackToken(tk);
						    }
							changeAbleActions.clear();
							numGen.position = 0; // current token has not changed yet
							numGen.possibilitiesBefore = 0;
							currentToken = null;
							continue while_bufferFill;
						}
						else if (numGen.tokenInsertTries < 0)
							fail();
						else 
						{
					    	if (DEBUG_possibilities) {
								System.out.print("           ");
								System.out.print(this.processedTokens); //;//
								System.out.print(this.changeAbleTokens);
								if (currentToken!=null)
									System.out.print(currentToken);
								System.out.println();
							}
					    	if (numGen.tooMuchPosition!=-1 && numGen.tooMuchPosition<numGen.position)
					    	{
								if (DEBUG_possibilities)
									System.out.println("    I think I should change somthing before here!");
					    		numGen.possibilitiesBefore = numGen.possibilitiesBeforeTooMuch;
					    		numGen.position = numGen.tooMuchPosition;
					    	}
					    	numGen.tooMuchPosition = -1;
					    	
					    	/** indicate bad ! **/
							if (!numGen.indicateBad(numGen.position - 1)) {
								if (DEBUG)
									System.out.println("!!! No further ideas. Could not recover by looking back");
								numGen.tokenInsertTries = 0;numGen.inserted = 0;
								continue while_bufferFill;
							}
							else
							{
								/**TODO :
								 * 	- only return completely if changes on
								 * 	  tokens earlier than numGen.pos-1
								 * 	  occured (->boost) 
								 * **/
								
								/**			- return read-stack to change-stack*/
								rdStack = (Stack<LRParserState>) chgStack.clone();
								numGen.inserted = 0;
								/**			  & return truly read tokens (using new abstract scannerObject)**/
								if (!(currentToken instanceof InsertedScannerToken))
						    		input.pushBackToken(currentToken);
								for (int i=changeAbleTokens.size()-1; i>=0; i--)
							    {
							    	ScannerToken<? extends Object> tk = changeAbleTokens.remove(i);
							    	if (!(tk instanceof InsertedScannerToken))
							    		input.pushBackToken(tk);
							    }
								changeAbleActions.clear();
								numGen.position = 0; // current token has not changed yet
								numGen.possibilitiesBefore = 0;
								currentToken = null;
								continue while_bufferFill;
							}
						}
					}
				}else{
					/** Returned / Recovered completely from token-run (insertion) ? **/
					if (	numGen!=null				// recovery takes place 
						&&	numGen.tokenInsertTries>=0	// and is not aborted
						&&	numGen.lastInsertStart+numGen.inserted+numGen.errorSyncSize
						<	numGen.position // and errorsyncsize tokens have been read
					)
						{
				    	if (DEBUG_possibilities || DEBUG)
				    	{
				    		System.out.print("ACCEPTED : ");
							System.out.print(this.processedTokens); //;//
							System.out.println(this.changeAbleTokens);
							maxErrors--;
							updateErrorSyncSize(false);
				    	}
				    	/*if (numGen.errorSyncSize<=2)*/
				    	{
					    	chgStack = (Stack<LRParserState>) rdStack.clone();
					        processedTokens.addAll(changeAbleTokens);
					    	changeAbleTokens.clear();
					    	changeAbleActions.clear();
				    	}/**/
				    	if (DEBUG_statistics)
				    		insertedTokens+=numGen.inserted;
						numGen=null;
					}
				}
				/** currentToken is not erroneous (label 5) **/
				/** but currentToken may indicate failure in pber! **/
				if (currentToken==null)
				{
					/** end input, do not buffer anymore **/
					processedTokens.add(new ScannerToken<Object>(SpecialTerminals.EndOfInputStream,"$is3$"));
					maxErrors = 0;
					break while_bufferFill; // -> see fail()
				}
				/*if (currentToken.getSymbol()==SpecialTerminals.EndOfInputStream)
					System.out.println("EOF");*/
				/** - save action for changeable-stack **/
				changeAbleActions.add(rdAction);
				/** - perform action on read-stack **/
				if (rdAction instanceof Shift) {
					Shift shift = (Shift) rdAction;
					/*if (DEBUG)
						System.out.println("S: to state " + shift.getState().getID() + " ("+ currentToken + ")");*/
					LRParserState s = shift.getState();
			        if (currentToken.getLine()!=-1 || currentToken.getColumn()!=-1)
			        {
			          s = (LRParserState)(shift.getState().clone());
			          s.beginLine = currentToken.getLine();
			          s.beginColumn = currentToken.getColumn();
			        }
					rdStack.push(s); // push / shift token
					changeAbleTokens.add(currentToken);
					currentToken = null; // read next symbol from input stream
				}
				else if(rdAction instanceof Reduce) {
					Reduce reduce = (Reduce) rdAction;
					Production production = reduce.getProduction();
					/*if (DEBUG)
						System.out.println("R: " + production.toString()); // TEST*/
					LRParserState lastBeginInfo = rdState;
					for (int i = 0; i < production.getRHSSizeWithoutEpsilon(); i++) {
						lastBeginInfo = rdStack.pop();
					}
					LRParserState newState = table.getGotoTable().get(rdStack.peek(),
							production.getLHS());
					if (newState instanceof ErrorState) {
						throw new RuntimeException("Table bad (goto missing!) : "+rdStack.peek()+", "+production.getLHS());
					} else {
						LRParserState s = (LRParserState) newState.clone();
						s.beginColumn = lastBeginInfo.beginColumn;
						s.beginLine = lastBeginInfo.beginLine;
						rdStack.push(s);
						/*if (DEBUG)
							System.out.println("G: to state " + newState.getID()); // TEST*/
					}
				}
				else if (rdAction instanceof Accept) {
			        if (DEBUG)
				          System.out.println("FINISHED :-)");
					if (currentToken.getSymbol()!=SpecialTerminals.EndOfInputStream)
						currentToken = new ScannerToken<Object>(SpecialTerminals.EndOfInputStream);
					changeAbleTokens.add(currentToken);
					break while_bufferFill;
				}
				else
					System.err.println("Internal error: Impossible action!!!");
				if (DEBUG_statistics)
					if (changeAbleTokens.size()>bufferPeak) bufferPeak = changeAbleTokens.size();
			}
		}
		else{
			if (DEBUG_statistics && bufferPeak>0)
			{
				System.out.println("Buffer peak : "+bufferPeak+" ( max. "+(this.insertionBufferLength+"+"+maxOversize)+" )");
				System.out.println("Inserted    : "+insertedTokens+" token(s)");
				bufferPeak = 0; // also responsible for doing this exactly once.
			}
		}
		
		/** FINALLY : return a token **/
		
		/** processed tokens which can not be parsed! (result of simple PBER) **/
		ScannerToken<? extends Object> pt = processedTokens.poll();
		if (pt!=null)
		{
			return pt;
		}
		
		ScannerToken<? extends Object> returnToken;
		if (changeAbleTokens.size()>0)
			returnToken = changeAbleTokens.remove(0);
		else{
			if (maxErrors==0)
				return getNextToken();
			if (currentToken.getSymbol()!=SpecialTerminals.EndOfInputStream)
			{
				System.err.println("!!! Critical internal error in InsertionScanner");
				currentToken = new ScannerToken<Object>(SpecialTerminals.EndOfInputStream,"$is4 - critical error in InsertionScanner$");
			}
			return currentToken;
		}
		if (returnToken.getSymbol()!=SpecialTerminals.EndOfInputStream)
		{
			/** update change-stack **/
			LRAction act;
			do {
				act = changeAbleActions.poll();
				if (act instanceof Shift) {
					Shift shift = (Shift) act;
					if (DEBUG)
						System.out.println("  DryRunReturn2Normal S: to state "
							+ shift.getState().getID() + " (" + returnToken + ")");
					LRParserState s = shift.getState();
					if (returnToken.getLine() != -1 || returnToken.getColumn() != -1) {
						s = (LRParserState) (shift.getState().clone());
						s.beginLine = returnToken.getLine();
						s.beginColumn = returnToken.getColumn();
					}
					chgStack.push(s);
				} else
					if (act instanceof Reduce) {
						Reduce reduce = (Reduce) act;
						if (DEBUG)
							System.out.println("  DryRunReturn2Normal R: "
								+ reduce.getProduction().toString());
						LRParserState lastBeginInfo = chgStack.peek();
						for (int i = 0; i < reduce.getProduction().getRHSSizeWithoutEpsilon(); i++) {
							lastBeginInfo = chgStack.pop();
						}
						LRParserState newState = table.getGotoTable().get(chgStack.peek(),
							reduce.getProduction().getLHS());
						if (newState instanceof ErrorState) {
							throw new RuntimeException("errorstate for changedstack : "
								+ chgStack.peek() + ", " + reduce.getProduction().getLHS());
						} else {
							LRParserState s = (LRParserState) newState.clone();
							s.beginColumn = lastBeginInfo.beginColumn;
							s.beginLine = lastBeginInfo.beginLine;
							chgStack.push(s);
							if (DEBUG)
								System.out
									.println("  DryRunReturn2Normal G: to state "
										+ newState.getID() + " (" + returnToken.getSymbol()
										+ ")");
						}
					} else if (!(act instanceof Accept)) {
						System.err
							.println("Critical internal error in parser driver : Found illegal action while returning to normal mode from dry run!");
					}
			} while (!(act instanceof Shift));
			/** end : perform actions on chgStk **/
		}
		/** update numgen**/
		if (numGen != null)
			numGen.removeFirst();
		return returnToken;
	}

	private ScannerToken<? extends Object> getNextToken() throws IOException {
		ScannerToken<? extends Object> tok = input.readNextTerminal();
		if (tok==null)
			throw new IOException("Null-token received! Check your scanner implementation!");
		return tok;
	}

	/** scan4allInsertions 
	 * @throws IOException **/
	public InsertedScannerToken<?>[] scan4allInsertions() throws IOException
	{
		LinkedList<InsertedScannerToken<?>> list = new LinkedList<InsertedScannerToken<?>>();
		ScannerToken<? extends Object> t;
		do{
			t = readNextTerminal();
			if (t instanceof InsertedScannerToken<?>)
				list.add((InsertedScannerToken<?>)t);
		}while(t.getSymbol()!=SpecialTerminals.EndOfInputStream);
		return list.toArray(new InsertedScannerToken<?>[list.size()]);
	}

	
	
	
	
	/** must continue with label 5 and must ensure that rdStack is most advanced 
	 * @throws IOException **/
	private void fail() throws IOException {
		 /** - do not delete numGen (indicates: token-insertion-start)*/
		if (numGen != null)
			numGen.tokenInsertTries = -1;
		LRAction reduction = searchSingleReduction();
	    if (reduction!=null)
	    {
	      if (DEBUG)
	        System.out.println("Trying to reduce before recovering error!");
	      rdAction = reduction;
	      return;
	    }
	    
		/** simple PBER **/
		if (DEBUG)
			System.out.println("Recover: Simple PBER");
		/** 	- find error-shifting stack position*/
		LRAction handleAction;
	    try {
			LRParserState catchState = rdStack.peek();
			handleAction = table.getActionTable().get(catchState, SpecialTerminals.Error);
			while (!(handleAction instanceof Shift)) {
				rdStack.pop();
				catchState = rdStack.peek();
				handleAction = table.getActionTable().get(catchState,
					SpecialTerminals.Error);
			}
			if (DEBUG) {
				System.out.println("Found error-catching state  : " + catchState);
			}
		} catch (java.util.EmptyStackException e) {
	    	// no recovery available => end input
		    if (DEBUG)
		    	System.out.println("!!! EmptyStackException during search for error-terminal.");
	        // TODO : ErrorInformation !
    		processedTokens.addAll(changeAbleTokens);
	    	processedTokens.add(currentToken);
	    	processedTokens.add(new ScannerToken<Object>(SpecialTerminals.EndOfInputStream,"$is1$"));
	    	changeAbleTokens.clear();
	    	changeAbleActions.clear();
	    	currentToken = null;
	    	return;
	    }
	    LRParserState handleState = ((Shift) handleAction).getState();
	    if (DEBUG)
	    	System.out.println("  Error-shifting to state   : " + handleState);
	    // Push the correct state and the error-information object on the
	    // value-stack
	    LRParserState s = (LRParserState)handleState.clone();
	    s.beginColumn   = currentToken.getColumn();
	    s.beginLine     = currentToken.getLine();
	    rdStack.push(s);

	    numGen = null;
		/** - fix stack-setup
		 *		- chgStack = rdStk.clone**/
	    chgStack = (Stack<LRParserState>) rdStack.clone();
		//numGen.inserted = 0; //numGen = null above
		/**		- processedTokens <- changeAbleTokens **/
	    if (changeAbleTokens.size()==0)
	    {	// nothing changed?
	    	//System.err.println("nothing changed!"); // TODO : Occurs often for <LR
	    	processedTokens.add(currentToken);
		    currentToken = getNextToken();
	    	changeAbleActions.clear();
	    }
	    else
	    {
	        processedTokens.addAll(changeAbleTokens);
	    	changeAbleTokens.clear();
	    	changeAbleActions.clear();
	    }
		/** 				- read bad tokens **/
	    rdAction = table.getActionTable().get(handleState,currentToken.getSymbol());
	    while ( rdAction instanceof ErrorAction || currentToken.getSymbol() == SpecialTerminals.EndOfInputStream ) {
		  processedTokens.add(currentToken);
	      if (currentToken.getSymbol() == SpecialTerminals.EndOfInputStream) {
	        if (DEBUG)
	          System.out.println("!!! End of input during error-recovery :(");
	        // TODO : ErrorInformation ! (???)
	    	processedTokens.add(new ScannerToken<Object>(SpecialTerminals.EndOfInputStream,"$is2$"));
	    	changeAbleTokens.clear();
	    	changeAbleActions.clear();
	    	currentToken = null;
	    	return;
	      }
	      currentToken = getNextToken();
		  rdAction = table.getActionTable().get(handleState,currentToken.getSymbol());
	    }
		if (DEBUG_possibilities) {
			System.out.print("DENIED   : ");
			System.out.print(this.processedTokens); //;//
			System.out.println(this.changeAbleTokens);
			maxErrors--;
		}
	}


	/**
	 * If in the current state only one single
	 * reduction is possible, this function returns it.
	 * Otherwise returns null.
	 **/
	private LRAction searchSingleReduction() {
		Reduce act = null;
		for (Terminal t : table.getActionTable().getColumns()) {
			LRAction act2 = table.getActionTable().get(rdState, t);
			if (act2 instanceof Reduce) {
				if (act == null)
					act = (Reduce) act2;
				else
					if (((Reduce) act2).getProduction() != act.getProduction())
						return null;
			} else
				if (act2 instanceof Shift)
					return null;
		}
		return act;
	}

	
	private void updateErrorSyncSize(boolean newlyStarted) {
		if (newlyStarted)
		{
			numGen.errorSyncSize = 3*changeAbleTokens.size()/insertionBufferLength+1;
			//numGen.errorSyncSize = 3;
			if (numGen.errorSyncSize<1)
				numGen.errorSyncSize = 1;
			if (numGen.errorSyncSize>3)
				numGen.errorSyncSize = 3;
			if (DEBUG)
				System.out.println("Choosing error-sync-size : "+numGen.errorSyncSize);
		}
		else
		{
			
		}
	}
	
	/**
	 * Creates a token-inserting scanner for a given parse-table
	 * and an input-scanner using a buffer-size of 5.
	 * **/
	public InsertionScanner(LRParsingTable parsingTable, Scanner input) {
		this(parsingTable, input, 5, 8, 8);
	}
	/**
	 * Creates a token-inserting scanner for a given parse-table
	 * and an input-scanner using a given buffer-size.
	 * **/
	public InsertionScanner(LRParsingTable parsingTable, Scanner input,
		int insertionBufferLength, int maxOversize, int maxPossibilities) {
		if (input==null || parsingTable==null)
			throw new NullPointerException("Insertion scanner does not allow null table or input!");
		if (input instanceof BufferedScanner)
			this.input = (BufferedScanner) input;
		else
			this.input = new BufferedScanner(input);
		this.table = parsingTable;
		
		if (insertionBufferLength < 0)
			insertionBufferLength = 0;
		this.insertionBufferLength = insertionBufferLength;
		this.changeAbleTokens = new ArrayList<ScannerToken<? extends Object>>();
		changeAbleActions = new LinkedList<LRAction>();
		
		rdStack = new Stack<LRParserState>();
		rdStack.push(table.getStartState());
		chgStack = new Stack<LRParserState>();
		chgStack.push(table.getStartState());
		
		processedTokens = new LinkedList<ScannerToken<? extends Object>>();
		
		maxIns = table.getParserInterface().getMaxTokenInsertTries();
		if (maxOversize<0) maxOversize = 0;
		this.maxOversize = maxOversize;
		insertedTokens = 0;
		bufferPeak = 0;
		this.maxPossibilities = maxPossibilities;
	}

}
