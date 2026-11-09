package edu.tum.cup2.util;

import java.util.HashSet;
import java.util.LinkedList;


/**
 * Useful methods for working with arrays.
 * 
 * @author Andreas Wenger
 */
public class ArrayTools<T>
{
	
	
	/**
	 * Creates and returns a {@link LinkedList} from the given array.
	 */
	public LinkedList<T> toLinkedList(T... array)
	{
		LinkedList<T> ret = new LinkedList<T>();
		for (T a : array)
			ret.add(a);
		return ret;
	}
	
	
	/**
	 * Creates and returns a {@link HashSet} from the given array.
	 */
	public static <T> HashSet<T> toHashSet(T... array)
	{
		HashSet<T> ret = new HashSet<T>();
		for (T a : array)
			ret.add(a);
		return ret;
	}
	
	
	/**
	 * Creates and returns a {@link LinkedList} from the given array.
	 */
	public static <T> LinkedList<T> toLinkedListT(T... array)
	{
		LinkedList<T> ret = new LinkedList<T>();
		for (T a : array)
			ret.add(a);
		return ret;
	}
	

}
