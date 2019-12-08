package edu.tum.cup2.generator.states;

import java.util.Collection;

import edu.tum.cup2.generator.items.Item;
import edu.tum.cup2.io.IAutomatonVisitor;
import edu.tum.cup2.io.IVisitedElement;
import edu.tum.cup2.util.It;

/**
 * State, consisting of a list of {@link Item}s
 * of the given type.
 * 
 * @author Andreas Wenger
 * 
 */
public abstract class State<T extends Item>
	implements IVisitedElement
{
	
	protected Collection<T> items;
	
	public State(Collection<T> items)
	{
		this.items = items;
	}
	
	
	public It<T> getItems()
	{
		return new It<T>(items);
	}
	
	
	public T getFirstItem()
	{
		return items.iterator().next();
	}
	
	
	public Collection<T> getItemsAsCollection()
	{
		return items;
	}
	
	
	public int getItemsCount()
	{
		return items.size();
	}

		
	@Override public String toString()
	{
		StringBuilder s = new StringBuilder("State:\n");
		for (Item item : items)
		{
			s.append("  ");
			s.append(item.toString());
			s.append("\n");
		}
		return s.toString();
	}
	
    /**
	 * implements the visitor pattern
	 */
	public void visited(IAutomatonVisitor visitor) {
		visitor.visit(this);
		
	}
	
}
