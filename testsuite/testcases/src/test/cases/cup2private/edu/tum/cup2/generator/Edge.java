package edu.tum.cup2.generator;

import edu.tum.cup2.generator.items.Item;
import edu.tum.cup2.generator.items.LR0Item;
import edu.tum.cup2.generator.states.State;
import edu.tum.cup2.grammar.Symbol;
import edu.tum.cup2.io.IAutomatonVisitor;
import edu.tum.cup2.io.IVisitedElement;


/**
 * An edge within a LR state transition diagram.
 * 
 * An edge is identified by the source {@link State} and a
 * {@link Symbol}, since it is a deterministic finite automata.
 * The destination {@link State} is also saved, because the
 * edges are needed for constructing the parsing table later.
 * 
 * The {@link Item} of the source {@link State} is saved too,
 * because we need the corresponding production and its position
 * for assigning semantic actions to shift operations later.
 * 
 * @author Andreas Wenger
 */
public final class Edge
	implements IVisitedElement
{

	private final State<?> src, dest;
	private final Symbol symbol;
	private final LR0Item srcItem;
        private final int hashCache;
	
	
	/**
	 * Edge which leads to another non-accepting state.
	 */
	public Edge(State<?> src, Symbol symbol, State<?> dest, LR0Item srcItem)
	{
		this.src = src;
		this.symbol = symbol;
		this.dest = dest;
		this.srcItem = srcItem;
                this.hashCache = src.hashCode() * 100 + symbol.hashCode();
	}
	
	
	/**
	 * Edge which leads to the accepting state.
	 */
	public static Edge createAcceptEdge(State<?> src, Symbol symbol)
	{
		return new Edge(src, symbol, null, null);
	}
	
	
	public State<?> getSrc()
	{
		return src;
	}

	
	public State<?> getDest()
	{
		return dest;
	}

	
	public Symbol getSymbol()
	{
		return symbol;
	}
	
	
	public LR0Item getSrcItem()
	{
		return srcItem;
	}
	
	
	public boolean isDestAccepting()
	{
		return dest == null;
	}
	
	
	/**
	 * Returns true, if the given object is an edge and if it starts from
	 * the same state using the same symbol.
	 * The destination state and the source item are not checked!
	 */
	@Override public boolean equals(Object obj)
	{
		if (obj instanceof Edge)
		{
			Edge e = (Edge) obj;
			return src.equals(e.src) && symbol.equals(e.symbol);
		}
		return false;
	}
	
	
	@Override public int hashCode()
	{
		return hashCache;
	}

	/**
	 * method to accept a visitor (also see "visitor pattern")
	 */
	public void visited(IAutomatonVisitor visitor) 
	{
		visitor.visit(this);
	}

    @Override
    public String toString() {
        return srcItem + " "+symbol;
    }
        
	
}
