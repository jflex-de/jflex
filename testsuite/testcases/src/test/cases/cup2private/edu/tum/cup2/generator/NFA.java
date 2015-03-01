package edu.tum.cup2.generator;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import edu.tum.cup2.generator.items.Item;
import edu.tum.cup2.generator.states.State;
import edu.tum.cup2.grammar.Symbol;
import edu.tum.cup2.util.It;


/**
 * Represents a non-deterministic automaton (in opposite to {@link Automaton}, which identifies edges only by src and
 * symbol!)
 * 
 * @author Gero
 */
public class NFA<I extends Item, S extends State<I>>
{
	private final Set<S> states = new HashSet<S>();
	private final Set<Edge<S>> edges = new HashSet<Edge<S>>();
	private final Set<S> endStates = new HashSet<S>();
	private S startState = null;
	
	
	public NFA(S startState)
	{
		states.add(startState);
		this.startState = startState;
	}
	
	
	public Set<S> getStates()
	{
		return states;
	}
	
	
	public Set<Edge<S>> getEdges()
	{
		return edges;
	}
	
	
	public S getStartState()
	{
		return startState;
	}
	
	
	public Set<S> getEndStates()
	{
		return endStates;
	}
	
	
	public void addEdge(S src, Symbol symbol, S dst)
	{
		addEdge(new Edge<S>(src, symbol, dst));
	}
	
	
	public void addEdge(Edge<S> newEdge)
	{
		edges.add(newEdge);
		states.add(newEdge.getSrc());
		states.add(newEdge.getDest());
	}
	
	
	public void addAcceptingEdge(S src, Symbol symbol, S dest)
	{
		addEdge(new Edge<S>(src, symbol, dest));
		endStates.add(dest);
	}
	
	
	/**
	 * Gets all edges starting at the given state.
	 * This implementation is not optimized and runs in O(n).
	 */
	public LinkedList<Edge<S>> getEdgesFrom(S state)
	{
		final LinkedList<Edge<S>> ret = new LinkedList<Edge<S>>();
		for (Edge<S> edge : edges)
		{
			if (edge.getSrc().equals(state))
			{
				ret.add(edge);
			}
		}
		return ret;
	}
	
	
	/**
	 * Gets the edge arriving in the given state.
	 * This implementation is not optimized and runs in O(n).
	 */
	public List<Edge<S>> getEdgeTo(S state)
	{
		final LinkedList<Edge<S>> ret = new LinkedList<Edge<S>>();
		for (Edge<S> edge : edges)
		{
			S dest = edge.getDest();
			if (dest != null && dest.equals(state))
			{
				ret.add(edge);
			}
		}
		return ret;
	}
	
	
	public boolean isEndState(S state)
	{
		return endStates.contains(state);
	}
	
	
	public boolean isAcceptingEdge(Edge<S> edge)
	{
		return endStates.contains(edge.getDest());
	}
	
	
	/**
	 * Pattern: [ srcItem.getPrdocution() ]... | symbol | [ dstItems ]...([ ACCEPT ])
	 */
	@Override
	public String toString()
	{
		final StringBuilder sb = new StringBuilder();
		for (Edge<S> edge : edges)
		{
			final S src = edge.getSrc();
			sb.append(printItems(src.getItems()));
			
			sb.append(" | " + edge.getSymbol() + " | ");
			
			final S dest = edge.getDest();
			sb.append(printItems(dest.getItems()));
			if (isEndState(dest))
			{
				sb.append("[ ACCEPT ]");
			}
			sb.append("\n");
		}
		return sb.toString();
	}
	
	
	private String printItems(It<I> items)
	{
		final StringBuilder sb = new StringBuilder();
		for (I item : items)
		{
			sb.insert(0, "[" + item.getProduction().toString(item.getPosition()) + "]");
		}
		return sb.toString();
	}
	
	
	public static final class Edge<S extends State<?>>
	{
		private final S src;
		private final Symbol symbol;
		private final S dest;
		
		private final Item srcItem;
		private final int hashCache;
		
		
		/**
		 * Edge which leads to another non-accepting state.
		 */
		public Edge(S src, Symbol symbol, S dest)
		{
			this.src = src;
			this.symbol = symbol;
			this.dest = dest;
			
			this.srcItem = src.getFirstItem();
			
			this.hashCache = calcHashCode();
		}
		
		
		public S getSrc()
		{
			return src;
		}
		
		
		public S getDest()
		{
			return dest;
		}
		
		
		public Symbol getSymbol()
		{
			return symbol;
		}
		
		
		public Item getSrcItem()
		{
			return srcItem;
		}
		
		
		private int calcHashCode()
		{
			final int prime = 31;
			int result = 1;
			result = prime * result + ((dest == null) ? 0 : dest.hashCode());
			result = prime * result + ((src == null) ? 0 : src.hashCode());
			result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
			return result;
		}
		
		
		@Override
		public int hashCode()
		{
			return hashCache;
		}
		
		
		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			final Edge<?> other = (Edge<?>) obj;
			if (dest == null)
			{
				if (other.dest != null)
					return false;
			} else if (!dest.equals(other.dest))
				return false;
			if (src == null)
			{
				if (other.src != null)
					return false;
			} else if (!src.equals(other.src))
				return false;
			if (symbol == null)
			{
				if (other.symbol != null)
					return false;
			} else if (!symbol.equals(other.symbol))
				return false;
			return true;
		}
		
		
		@Override
		public String toString()
		{
			return "<" + srcItem + "| " + symbol + ">";
		}
	}
}
