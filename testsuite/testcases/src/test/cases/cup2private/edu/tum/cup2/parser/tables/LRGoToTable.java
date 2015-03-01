package edu.tum.cup2.parser.tables;

import java.util.Hashtable;

import edu.tum.cup2.grammar.NonTerminal;
import edu.tum.cup2.parser.states.ErrorState;
import edu.tum.cup2.parser.states.LRParserState;
import edu.tum.cup2.util.It;
import java.io.Serializable;

/**
 * GoTo table, needed by an LR parser.
 * 
 * Each cell is indexed by an {@link LRParserState} and a {@link NonTerminal}
 * and contains the following state of the parser when the according non-terminal
 * has been recognized.
 * 
 * @author Andreas Wenger
 */
public final class LRGoToTable implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	//the parent parsing table
	private final LRParsingTable parentTable;
	
	//internally realized as a hashmap of keys created by StateSymbolKey.
	//if a cell for a given key is missing, this signifies an error state.
	private final Hashtable<StateSymbolKey, LRParserState> table = new Hashtable<StateSymbolKey, LRParserState>();
	
	
	/**
	 * Creates a new {@link LRGoToTable} for the given parsing table.
	 */
	public LRGoToTable(LRParsingTable parentTable)
	{
		this.parentTable = parentTable;
	}
	
	
	/**
	 * Gets the {@link LRParserState} at the given position ({@link LRParserState} and {@link NonTerminal}).
	 */
	public LRParserState get(LRParserState atState, NonTerminal atNonTerminal)
	{
		LRParserState ret = table.get(new StateSymbolKey(atState, atNonTerminal));
		if (ret != null)
			return ret;
		else
			return new ErrorState(); //since empty cells signify errors
	}
	
	
	/**
	 * Sets the {@link LRParserState} at the given position ({@link LRParserState} and {@link NonTerminal}).
	 */
	public void set(LRParserState newState, LRParserState atState, NonTerminal atNonTerminal)
	{
		table.put(new StateSymbolKey(atState, atNonTerminal), newState);
	}

	
	/**
	 * Gets the number of cells filled with non-error states.
	 */
	public int getNonErrorCellsCount()
	{
		return table.values().size();
	}
	
	
	/**
	 * Gets an iterator over all columns, returning the corresponding non-terminals
	 * in the order in which they were defined.
	 */
	public It<NonTerminal> getColumns()
	{
		return new It<NonTerminal>(parentTable.getGrammar().getNonTerminals());
	}
	
}
