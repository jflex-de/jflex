package edu.tum.cup2.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

//TODO: doc
public class CollectionTools
{
	
	
	/**
	 * Creates a linked list.
	 */
	public static <T1> List<T1> llist()
	{
		return new LinkedList<T1>();
	}
	
	
	/**
	 * Creates a new map.
	 */
	public static <T1, T2> Map<T1, T2> map()
	{
		return new HashMap<T1, T2>();
	}
	
	/**
	 * Creates a new standard stack.
	 */
	public static <T> Stack<T> stack()
	{
		return new Stack<T>();
	}
	
	
	/**
	 * Creates a new set.
	 */
	public static <T> Set<T> set()
	{
		return new HashSet<T>();
	}	
	
	/**
	 * Creates a new synchronized map.
	 */
	public static <T1, T2> Map<T1, T2> synchronizedMap()
	{
		return Collections.synchronizedMap(new HashMap<T1, T2>());
	}
	
	
	/**
	 * Creates a new synchronized set.
	 */
	public static <T> Set<T> synchronizedSet()
	{
		return Collections.synchronizedSet(new HashSet<T>());
	}
	
	
	/**
	 * Adds the given element to the given set
	 * and returns it. If the given set is null,
	 * a new set is created and returned.
	 */
	public static <T> Set<T> add(Set<T> set, T e)
	{
		if (set == null)
		{
			Set<T> ret = new HashSet<T>();
			ret.add(e);
			return ret;
		}
		else
		{
			set.add(e);
			return set;
		}
	}


}
