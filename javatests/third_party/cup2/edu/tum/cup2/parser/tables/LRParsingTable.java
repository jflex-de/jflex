package edu.tum.cup2.parser.tables;

import java.util.LinkedList;

import edu.tum.cup2.grammar.Grammar;
import edu.tum.cup2.parser.states.LRParserState;
import edu.tum.cup2.semantics.ParserInterface;
import edu.tum.cup2.util.It;
import java.io.Serializable;


/**
 * An LR parsing table, which consists of an 
 * {@link LRActionTable} and an {@link LRGoToTable}.
 * 
 * The {@link LRParserState}s, representing the
 * rows of the table, are stored here, too. The first
 * state is always the start state.
 * 
 * @author Andreas Wenger
 */
public final class LRParsingTable implements Serializable
{
	static final long serialVersionUID = 1L;
	
	private final Grammar grammar;
	private final LinkedList<LRParserState> states;
	
	private final LRActionTable actionTable;
	private final LRGoToTable gotoTable;
	private final ParserInterface erI;
	
	
	/**
	 * Creates an empty {@link LRParsingTable} with the
	 * given number of states, using the given terminals and non-terminals
	 * for column headers.
	 */
	public LRParsingTable(Grammar grammar, ParserInterface parserInterface, int statesCount)
	{
		this.grammar  = grammar;
		this.erI      = parserInterface;
		//initialize the states
		this.states = new LinkedList<LRParserState>();
		for (int i = 0; i < statesCount; i++)
		{
			this.states.add(new LRParserState(i));
		}
		//create an empty action table and an empty goto table
		this.actionTable = new LRActionTable(this);
		this.gotoTable = new LRGoToTable(this);
	}
	
	
	/**
	 * Gets the grammar behind the parsing table.
	 */
	public Grammar getGrammar()
	{
		return grammar;
	}
	
	
	/**
	 * Gets the start state.
	 */
	public LRParserState getStartState()
	{
		//the start state is always state 0
		return states.getFirst();
	}
	
	
	/**
	 * Gets the number of states.
	 */
	public int getStatesCount()
	{
		return states.size();
	}
	
	
	/**
	 * Gets an iterator over the states.
	 */
	public It<LRParserState> getStates()
	{
		return new It<LRParserState>(states);
	}
	
	
	/**
	 * Gets the action table.
	 */
	public LRActionTable getActionTable()
	{
		return actionTable;
	}
	
	
	/**
	 * Gets the goto table.
	 */
	public LRGoToTable getGotoTable()
	{
		return gotoTable;
	}

  /**
   * Gets the ParserInterface.
   */
  public ParserInterface getParserInterface() {
    return erI;
  }
	

}
