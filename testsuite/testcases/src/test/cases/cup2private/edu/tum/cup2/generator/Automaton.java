package edu.tum.cup2.generator;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import edu.tum.cup2.generator.items.Item;
import edu.tum.cup2.generator.states.State;
import edu.tum.cup2.io.IAutomatonVisitor;
import edu.tum.cup2.io.IVisitedElement;


/**
 * An automaton consists of states and edges
 * connecting the states.
 * 
 * @author Andreas Wenger
 */
public class Automaton<I extends Item, S extends State<I>> implements IVisitedElement
{
	
	private Set<S> states = Collections.synchronizedSet(new HashSet<S>());
	private Set<Edge> edges = Collections.synchronizedSet(new HashSet<Edge>());
	private S startState = null;
	
	
	public Automaton(S startState)
	{
		states.add(startState);
		this.startState = startState;
	}
	
	
	public Set<S> getStates()
	{
		return states;
	}
	
	
	public Set<Edge> getEdges()
	{
		return edges;
	}
	
	
	public S getStartState()
	{
		return startState;
	}
	
	
	public void visited(IAutomatonVisitor visitor)
	{
		visitor.visit(this);
	}
	
	
	/**
	 * Gets all edges starting at the given state.
	 * This implementation is not optimized and runs in O(n).
	 */
	public LinkedList<Edge> getEdgesFrom(S state)
	{
		LinkedList<Edge> ret = new LinkedList<Edge>();
		for (Edge edge : edges)
		{
			if (edge.getSrc().equals(state))
				ret.add(edge);
		}
		return ret;
	}
	
	
	/**
	 * Gets the edge arriving in the given state.
	 * This implementation is not optimized and runs in O(n).
	 */
	public List<Edge> getEdgeTo(State<?> state)
	{
		LinkedList<Edge> ret = new LinkedList<Edge>();
		for (Edge edge : edges)
		{
			State<?> dest = edge.getDest();
			if (dest != null && dest.equals(state))
				ret.add(edge);
		}
		return ret;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	public String toString()
	{
		Comparator<Edge> comp = new Comparator<Edge>() {
			public int compare(Edge o1, Edge o2)
			{
				return o1.toString().compareTo(o2.toString());
			}
		};
		StringBuffer sb = new StringBuffer();
		LinkedList<S> queue = new LinkedList<S>();
		HashSet<S> set = new HashSet<S>();
		queue.add(getStartState());
		while (!queue.isEmpty())
		{
			S s = queue.poll();
			sb.append(s.toString());
			set.add(s);
			List<Edge> sortedList = getEdgesFrom(s);
			Collections.sort(sortedList, comp);
			for (Edge e : sortedList)
			{
				if (set.contains(e.getDest()))
					continue;
				State<?> st = e.getDest();
				if (st != null)
					queue.add((S) st);
				
			}
		}
		
		return sb.toString();// +"\n"+edges.toString();
	}
	
}
