package edu.tum.cup2.parser.states;

import edu.tum.cup2.parser.tables.LRParsingTable;
import java.io.Serializable;

/**
 * State of an LR parser.
 * 
 * For simplicity, each state is indexed by a unique number.
 * In most cases, the states of a parser will simply be
 * subsequent numbers from 1 to n or 0 to n-1.
 * 
 * @author Andreas Wenger
 */
public class LRParserState implements Serializable, Cloneable
{
	private static final long serialVersionUID = 1L;
	
	private final int id;
	
  public int beginLine = -1;
  public int beginColumn = -1;
	
	
	/**
	 * Creates a new state of an LR parser.
	 * Should be only called from the {@link LRParsingTable} class.
	 */
	public LRParserState(int id)
	{
		this.id = id;
	}
	
	
	/**
	 * Gets the unique index of this state.
	 */
	public int getID()
	{
		return id;
	}
	
	
	@Override public boolean equals(Object obj)
	{
		if (obj instanceof LRParserState)
		{
			LRParserState a = (LRParserState) obj;
			return a.id == this.id;
		}
		return false;
	}
	
	
	@Override public int hashCode()
	{
		return id;
	}
	
	
	@Override public String toString()
	{
		return "State " + id;
	}
	
	@Override public Object clone()
	{
	  LRParserState s = new LRParserState(id);
	  s.beginColumn = beginColumn;
    s.beginLine   = beginLine;
    return s;
	}
	

}
