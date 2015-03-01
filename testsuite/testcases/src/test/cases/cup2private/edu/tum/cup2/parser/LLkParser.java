package edu.tum.cup2.parser;

import java.io.IOException;
import java.util.Collection;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Stack;

import edu.tum.cup2.generator.items.LLkItem;
import edu.tum.cup2.generator.states.LLkState;
import edu.tum.cup2.generator.terminals.ITerminalSeq;
import edu.tum.cup2.generator.terminals.TerminalSeq;
import edu.tum.cup2.grammar.AuxiliaryLHS4SemanticShiftAction;
import edu.tum.cup2.grammar.NonTerminal;
import edu.tum.cup2.grammar.Production;
import edu.tum.cup2.grammar.SpecialTerminals;
import edu.tum.cup2.parser.exceptions.LLkParserException;
import edu.tum.cup2.parser.states.LLkErrorState;
import edu.tum.cup2.parser.tables.LLkParsingTable;
import edu.tum.cup2.scanner.InsertedScannerToken;
import edu.tum.cup2.scanner.Scanner;
import edu.tum.cup2.scanner.ScannerToken;
import edu.tum.cup2.semantics.Action;
import edu.tum.cup2.semantics.ActionPerformer;
import edu.tum.cup2.semantics.SymbolValue;


/**
 * This class implements a (strong) LL(k)-Parsing pushdown automaton. Its state is representsed by the {@link LLkItem}s
 * on the {@link #stack} - or a {@link LLkParserState} containing these one (or two) items.
 * 
 * @author Gero
 * 
 */
public class LLkParser extends AParser
{
	private final boolean DEBUG = false;
	
	protected final LLkParsingTable table;
	private final int k;
	
	private final Stack<LLkItem> stack;
	private final Stack<Object> valueStack;
	
	
	/**
	 * @param table
	 */
	public LLkParser(LLkParsingTable table)
	{
		this.table = table;
		this.k = table.getK();
		
		this.stack = new Stack<LLkItem>();
		this.valueStack = new Stack<Object>();
	}
	
	
	public synchronized Object parse(Scanner input, Object... initArgs) throws IOException, LLkParserException
	{
		// ### Initialization
		// table.getParserInterface().init(this, initArgs);
		
		// # Prepare parser stack
		stack.clear();
		final LLkState startState = table.getStartState();
		stack.push(startState.getFirstItem());
		
		// # Prepare semantic stack
		valueStack.clear();
		valueStack.push(SymbolValue.NoValue);
		
		// # Prepare Lookahead
		final LinkedList<ScannerToken<?>> tokenBuffer = new LinkedList<ScannerToken<?>>();
		
		
		// ### Process input
		final LLkItem endItem = table.getEndState().getFirstItem();
		LLkItem currentItem = null;
		
		ScannerToken<?> currentToken = readNextToken(input, tokenBuffer, k);
		
		while (stack.size() > 0)
		{
			// # Init cycle
			currentItem = stack.peek();
			
			// Check for Accept..
			if (currentItem.equals(endItem))
			{
				// # Accept!
				if (DEBUG)
				{
					if (!(currentToken.getSymbol() == SpecialTerminals.EndOfInputStream))
					{
						System.err.println("Accepted, but not end of input stream... error?");
					}
					
					if (stack.size() != 0)
					{
						System.err
								.println("Something went wrong; somehow we think we should accept but the stack is not empty...???");
					}
					
					System.out.println("Accepted!");
				}
				
				// table.getParserInterface().exit(this);
				
				return valueStack.peek();
			}
			
			
			// # What to do?
			final ITerminalSeq lookahead = createLookahead(tokenBuffer, k);
			
			if (!currentItem.isComplete())
			{
				// # Push/Expand or Shift/Consume...
				// Get new state
				final LLkState currentState = getTopMostItems(1);
				
				// ...?
				if (currentItem.getNextSymbol() instanceof NonTerminal)
				{
					// # Expand! Add both incoming items on top of the stack
					final LLkState to = table.get(currentState, SpecialTerminals.Epsilon, lookahead);
					checkError(currentToken, to);
					
					
					stack.pop();
					stack.push(to.getSecondItem());
					stack.push(to.getFirstItem());
					
					if (DEBUG)
					{
						System.out.println("Expand to: " + to);
					}
				} else
				{
					// # Shift!
					final LLkState to = table.get(currentState, currentToken.getSymbol(), lookahead);
					checkError(currentToken, to);
					
					valueStack.push(currentToken.hasValue() ? currentToken.getValue() : SymbolValue.NoValue);
					
					stack.pop();
					stack.push(to.getFirstItem());
					
					if (DEBUG)
					{
						System.out.println("Consume: " + currentToken.getSymbol() + ", change to: " + to);
					}
					currentToken = readNextToken(input, tokenBuffer, k); // Consume one input!
				}
			} else
			{
				// # Reduce!
				final LLkState currentState = getTopMostItems(2);
				final LLkState to = table.get(currentState, SpecialTerminals.Epsilon, lookahead);
				checkError(currentToken, to);
				
				// Perform reduce action
				final Production currentProduction = currentItem.getProduction();
				final Action reduceAction = currentProduction.getReduceAction();
				Object newValue = SymbolValue.NoValue;
				if (reduceAction != null)
				{
					newValue = ActionPerformer.perform(reduceAction, valueStack, calcRhsSize(currentProduction));
				}
				// For each symbol on the right hand side pop one value from the stack
				for (int i = 0; i < currentProduction.getRHSSizeWithoutEpsilon(); i++)
				{
					valueStack.pop();
				}
				valueStack.push(newValue);
				
				stack.pop();
				stack.pop();
				stack.push(to.getFirstItem());
				
				if (DEBUG)
				{
					System.out.println("Reduce to: " + to);
				}
			}
		}
		
		// Should never happen!
		throw new RuntimeException("Incomplete statement: Stack is emtpy but no accept!!!");
	}
	
	
	/**
	 * TODO Relevant for LL-Parser???
	 * 
	 * @param production
	 * @return Size of RHS
	 */
	private int calcRhsSize(Production production)
	{
		int result = production.getRHSSizeWithoutEpsilon();
		if (production.getLHS() instanceof AuxiliaryLHS4SemanticShiftAction)
		{
			final AuxiliaryLHS4SemanticShiftAction auxAction = (AuxiliaryLHS4SemanticShiftAction) production.getLHS();
			result += auxAction.numPrecedingSymbolsNotEpsilon;
		}
		return result;
	}
	
	
	private void checkError(ScannerToken<?> currentToken, LLkState state) throws LLkParserException
	{
		if (state == null)
		{
			throw new LLkParserException("No new state returned from table!");
		}
		if (state instanceof LLkErrorState)
		{
			final LLkErrorState errState = (LLkErrorState) state;
			throw new LLkParserException(errState.getMsg());
		}
	}
	
	
	private LLkState getTopMostItems(int numOfItems)
	{
		if (numOfItems < 0 || numOfItems > 2)
		{
			throw new IllegalArgumentException("NumOfItems must 1 or 2!!!");
		}
		
		if (!(stack.size() >= numOfItems))
		{
			throw new RuntimeException("Stack has less items then expexted! (" + stack.size() + " < " + numOfItems + ")");
		}
		
		
		if (numOfItems == 1)
		{
			return new LLkState(stack.peek());
		} else if (numOfItems == 2)
		{
			return new LLkState(stack.get(stack.size() - 2), stack.peek());
		}
		
		return null; // Logically unreachable!!!
	}
	
	
	/**
	 * Reads a token from the stream and maintains the lookahead and buffered token-list
	 * 
	 * @param input
	 * @param tokens
	 * @param k
	 * @return The current token!
	 */
	private ScannerToken<?> readNextToken(final Scanner input, final Deque<ScannerToken<?>> tokens, final int k)
	{
		final int nominalSize = k + 1; // 1 extra for currentToken
		
		// Remove head in case there is anything to remove
		if (!tokens.isEmpty())
		{
			tokens.pop();
		}
		
		// Fill up the buffer
		final ScannerToken<?> last = tokens.peekLast();
		if (last == null || last.getSymbol() != SpecialTerminals.EndOfInputStream)
		{
			try
			{
				// Empty or there are still tokens left: Read tokens into the buffer until we have enough for lookahead
				// creation plus the current symbol
				ScannerToken<?> nextToken = null;
				do
				{
					nextToken = input.readNextTerminal();
					// Safety check
					if (nextToken == null)
					{
						throw new IOException("Null-token received! Check your scanner implementation!");
					}
					
					tokens.add(nextToken);
				} while (tokens.size() < nominalSize && nextToken.getSymbol() != SpecialTerminals.EndOfInputStream);
				
			} catch (IOException e)
			{
				System.err.println("Error while reading from scanner: " + input);
				e.printStackTrace();
				return null;
			}
		}
		
		// Keep lookahead and tokens in sync
		final ScannerToken<?> currentToken = tokens.peek();
		if (currentToken == null)
		{
			System.err.println("Tried to read over END_OF_INPUTSTREAM!");
			return null;
		}
		
		// Check for inserted scanner-tokens
		if (currentToken instanceof InsertedScannerToken)
		{
			InsertedScannerToken<?> inserted = (InsertedScannerToken<?>) currentToken;
			if (inserted.getErrorInformation() != null)
			{
				notifyObserversAbout(inserted.getErrorInformation());
			}
		}
		return currentToken;
	}
	
	
	private ITerminalSeq createLookahead(Collection<ScannerToken<?>> buffer, final int k)
	{
		final ITerminalSeq seq = new TerminalSeq();
		
		final Iterator<ScannerToken<?>> it = buffer.iterator();
		while (seq.size() < k && it.hasNext())
		{
			seq.append(it.next().getSymbol());
		}
		
		return seq;
	}
}
