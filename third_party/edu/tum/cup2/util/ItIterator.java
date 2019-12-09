package edu.tum.cup2.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


/**
 * A generic {@link Iterator} over {@link Iterator}s...
 * 
 * @author Gero
 * @param <T>
 */
public class ItIterator<T> implements Iterator<T>
{
	
	private final List<Iterator<T>> arr;
	private final Iterator<Iterator<T>> arrIterator;
	private Iterator<T> currIterator = null;
	
	
	public <TX extends Iterable<T>> ItIterator(Collection<TX> iterables)
	{
		arr = new ArrayList<Iterator<T>>(iterables.size());
		
		for (Iterable<T> iterable : iterables)
		{
			arr.add(iterable.iterator());
		}
		
		arrIterator = arr.iterator();
	}
	
	
	public <TX extends Iterable<T>> ItIterator(TX iterable1, TX iterable2)
	{
		arr = new ArrayList<Iterator<T>>(2);
		
		arr.add(iterable1.iterator());
		arr.add(iterable2.iterator());
		
		arrIterator = arr.iterator();
	}
	
	
	public boolean hasNext()
	{
		if (currIterator != null && currIterator.hasNext())
		{
			return true;
		}
		
		while (arrIterator.hasNext())
		{
			currIterator = arrIterator.next();
			if (currIterator.hasNext())
			{
				return true;
			}
		}
		return false;
	}
	
	
	public T next()
	{
		if (currIterator != null && currIterator.hasNext())
		{
			return currIterator.next();
		}
		
		while (arrIterator.hasNext())
		{
			currIterator = arrIterator.next();
			if (currIterator.hasNext())
			{
				return currIterator.next();
			}
		}
		return null;
	}
	
	
	public void remove()
	{
		if (currIterator != null && currIterator.hasNext())
		{
			currIterator.remove();
		}
		
		while (arrIterator.hasNext())
		{
			currIterator = arrIterator.next();
			if (currIterator.hasNext())
			{
				currIterator.remove();
				return;
			}
		}
	}
}