package edu.tum.cup2.parser.actions;

import java.io.Serializable;

import edu.tum.cup2.grammar.Production;
import edu.tum.cup2.parser.states.LRParserState;

/**
 * LR shift parser action.
 * 
 * @author Andreas Wenger
 */
public final class Shift 
	implements LRAction, Serializable
{
	private static final long serialVersionUID = 1L;
	
	private final LRParserState state;
	private final Production production;
	private final int position;

	
	public Shift(LRParserState state, Production production, int position)
	{
		this.state = state;
		this.production = production;
		this.position = position;
	}
	
	
	public LRParserState getState()
	{
		return state;
	}
	
	
	public Production getProduction()
	{
		return production;
	}
	
	
	public int getPosition()
	{
		return position;
	}
	
	@Override public boolean equals(Object obj)
	{
		if (obj instanceof Shift)
		{
			Shift a = (Shift) obj;
			return this.state.equals(a.state);
		}
		return false;
	}
	
	
	@Override public String toString()
	{
		return "s" + state.getID();
	}
	

}
