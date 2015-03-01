package edu.tum.cup2.parser.tables;

import java.util.Enumeration;
import java.util.Hashtable;

import edu.tum.cup2.grammar.SpecialTerminals;
import edu.tum.cup2.grammar.Terminal;
import edu.tum.cup2.parser.actions.ErrorAction;
import edu.tum.cup2.parser.actions.LRAction;
import edu.tum.cup2.parser.actions.Shift;
import edu.tum.cup2.parser.states.LRParserState;
import edu.tum.cup2.util.It;
import java.io.Serializable;

/**
 * Action table, needed by an LR parser.
 * 
 * Each cell is indexed by an {@link LRParserState} and a {@link Terminal}
 * (including the {@code EndOfInputStream} terminal) and contains exactly one
 * {@link LRAction}.
 * 
 * @author Andreas Wenger
 * @author Michael Hausmann
 */

public class LRActionTable implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	//the parent parsing table
	private final LRParsingTable parentTable;
	
	//internally realized as a hashmap of keys created by StateSymbolKey.
	//if a cell for a given key is missing, this signifies an error action.
	private final Hashtable<StateSymbolKey, LRAction> table = new Hashtable<StateSymbolKey, LRAction>();
	
	
	/**
	 * Creates a new {@link LRActionTable} for the given parsing table.
	 */
	public LRActionTable(LRParsingTable parentTable)
	{
		this.parentTable = parentTable;
	}
	
	
	/**
	 * Gets the {@link LRAction} at the given position ({@link LRParserState} and {@link Terminal}).
	 */
	public LRAction get(LRParserState atState, Terminal atTerminal)
	{
		LRAction ret = getWithNull(atState, atTerminal);
		if (ret != null)
			return ret;
		else
			return ErrorAction.getInstance(); //since empty cells signify errors
	}
	
	
	/**
	 * Gets the {@link LRAction} at the given position ({@link LRParserState} and {@link Terminal}),
	 * or null if there is an error action.
	 * If the given Terminal is <code>WholeRow</code>, all columns are visited
	 * and the first found action is returned, otherwise null.
	 */
	public LRAction getWithNull(LRParserState atState, Terminal atTerminal)
	{
		if (atTerminal != SpecialTerminals.WholeRow)
		{
			//normal terminal
			LRAction ret = table.get(new StateSymbolKey(atState, atTerminal));
			if (ret != null)
				return ret;
			else
			{
				//look if there is an action for the whole row
				ret = table.get(new StateSymbolKey(atState, SpecialTerminals.WholeRow));
				if (ret != null)
					return ret;
				else
					return null;
			}
		}
		else
		{
			//whole-row
			//first look if there is an action for the whole row
			LRAction ret = table.get(new StateSymbolKey(atState, SpecialTerminals.WholeRow));
			if (ret != null)
				return ret;
			//find the first terminal-specific action
			for (Terminal terminal : parentTable.getGrammar().getTerminals())
			{
				ret = table.get(new StateSymbolKey(atState, terminal));
				if (ret != null)
					return ret;
			}
			return null;
		}
	}
	
	
	/**
	 * Sets the {@link LRAction} at the given ({@link LRParserState} and {@link Terminal}).
	 */
	public void set(LRAction action, LRParserState atState, Terminal atTerminal)
	{
	  if (action==null)
	    table.remove(new StateSymbolKey(atState, atTerminal));
	  else
	    table.put(new StateSymbolKey(atState, atTerminal), action);
	}
	
	
	/**
	 * Sets the {@link LRAction} at the whole row of the given {@link LRParserState}.
	 */
	public void setRow(LRAction action, LRParserState atState)
	{
		set(action, atState, SpecialTerminals.WholeRow);
	}
	
	
	/**
	 * Gets the number of cells filled with non-error actions.
	 */
	public int getNonErrorCellsCount()
	{
		int sum = 0;
		Enumeration<StateSymbolKey> keys = table.keys();
		while (keys.hasMoreElements())
		{
			StateSymbolKey key = keys.nextElement();
			if (key.getSymbol() == SpecialTerminals.WholeRow)
				sum += parentTable.getGrammar().getTerminals().size();
			else
				sum++;
		}
		return sum;
	}
	
	
	/**
	 * Gets an iterator over all columns, returning the corresponding terminals
	 * in the order in which they were defined.
	 */
	public It<Terminal> getColumns()
	{
		return new It<Terminal>(parentTable.getGrammar().getTerminals());
	}
	
	/**
	 * returns a reference to the internal hashtable containing the semantic actions
	 * (is is required for testing purposes) 
	 */
	protected Hashtable<StateSymbolKey, LRAction> getTable()
	{
		return this.table;
	}


	public Terminal getTerminalOfFirstShiftAction(LRParserState atState) {
		for (Terminal terminal : parentTable.getGrammar().getTerminals())
		{
			LRAction act = table.get(new StateSymbolKey(atState, terminal));
			if (act != null && act instanceof Shift)
				return terminal;
		}
		return null;
	}

}
