package edu.tum.cup2.generator.terminals;

import static edu.tum.cup2.grammar.SpecialTerminals.Epsilon;
import static edu.tum.cup2.util.CollectionTools.set;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import edu.tum.cup2.grammar.SpecialTerminals;
import edu.tum.cup2.grammar.Terminal;
import edu.tum.cup2.util.CollectionTools;


/**
 * Implementation of a {@link TerminalSet}, which is highly
 * optimized by using a bit array.
 * 
 * GOON: TESTS!!
 * 
 * @author Andreas Wenger
 */
public final class EfficientTerminalSet
	implements TerminalSet
{
	
	private final int[] data;
	
	private final Map<Terminal, Integer> indices;
	private final Collection<Terminal> terminals;
	
	//cache
	private final int indexEpsilon;
	
	
	/**
	 * Creates a new {@link EfficientTerminalSet}
	 * @param terminals  the list of possible terminals (without {@link SpecialTerminals}).
	 */
	public EfficientTerminalSet(Collection<Terminal> terminals)
	{
		this.terminals = terminals;
		//compute indices
		this.indices = CollectionTools.map();
		int count = 0;
		for (Terminal specialTerminal : SpecialTerminals.values())
		{
			indices.put(specialTerminal, count);
			count++;
		}
		for (Terminal terminal : terminals)
		{
			indices.put(terminal, count);
			count++;
		}
		int dataCount = (count - 1) / 32 + 1;
		this.data = new int[dataCount];
		//cache
		this.indexEpsilon = indices.get(Epsilon);
	}
	
	
	/**
	 * Creates a new {@link EfficientTerminalSet}.
	 * @param terminals  the list of possible terminals (without {@link SpecialTerminals}).
	 * @param indices    the mapping of terminals to bit indices
	 * @param data       the bit array
	 */
	private EfficientTerminalSet(Collection<Terminal> terminals,
		Map<Terminal, Integer> indices, int[] data)
	{
		this.terminals = terminals;
		this.indices = indices;
		this.data = data;
		//cache
		this.indexEpsilon = indices.get(Epsilon);
	}
	
	
	/**
	 * Returns an empty EfficientTerminalSet for the same terminals as this one.
	 */
	public EfficientTerminalSet empty()
	{
		return new EfficientTerminalSet(terminals, indices, new int[data.length]);
	}
	
	
	/**
	 * Returns this set of terminals, with the given one added.
	 */
	public EfficientTerminalSet plus(Terminal terminal)
	{
		Integer index = indices.get(terminal);
		if (index == null)
			throw new IllegalArgumentException("Unregistered terminal: " + terminal);
		return setBit(index, true);
	}

	
	/**
	 * Returns this set of terminals merged with the given one.
	 * Optimized implementation for other {@link EfficientTerminalSet}.
	 */
	public EfficientTerminalSet plusAll(EfficientTerminalSet terminals)
	{
		if (this.terminals != terminals.terminals)
			throw new IllegalArgumentException("The two sets were initialized with different terminal arrays");
		int[] newData = new int[data.length];
		for (int i = 0; i < data.length; i++)
		{
			newData[i] = data[i] | terminals.data[i];
		}
		return new EfficientTerminalSet(this.terminals, indices, newData);
	}
	
	
	/**
	 * Returns this set of terminals merged with the given one
	 * (but {@link SpecialTerminals#Epsilon} is ignored).
	 * Optimized implementation for other {@link EfficientTerminalSet}.
	 */
	public EfficientTerminalSet plusAllExceptEpsilon(EfficientTerminalSet terminals)
	{
		boolean valueEpsilon = getBit(indexEpsilon);
		return plusAll(terminals).setBit(indexEpsilon, valueEpsilon);
	}
	
	
	/**
	 * Returns this set of terminals merged with the given one.
	 * Default implementation for any kind of {@link TerminalSet}.
	 */
	public TerminalSet plusAll(TerminalSet terminals)
	{
		if (terminals instanceof EfficientTerminalSet)
		{
			return plusAll((EfficientTerminalSet) terminals);
		}
		else
		{
			TerminalSet ret = this;
			for (Terminal terminal : terminals.getTerminals())
			{
				ret = ret.plus(terminal);
			}
			return ret;
		}
	}
	
	
	/**
	 * Returns this set of terminals merged with the given one
	 * (but {@link SpecialTerminals#Epsilon} is ignored).
	 * Default implementation for any kind of {@link TerminalSet}.
	 */
	public TerminalSet plusAllExceptEpsilon(TerminalSet terminals)
	{
		if (terminals instanceof EfficientTerminalSet)
		{
			return plusAllExceptEpsilon((EfficientTerminalSet) terminals);
		}
		else
		{
			TerminalSet ret = this;
			for (Terminal terminal : terminals.getTerminals())
			{
				if (terminal != SpecialTerminals.Epsilon)
					ret = ret.plus(terminal);
			}
			return ret;
		}
	}
	
	
	/**
	 * Gets the bit with the given index.
	 */
	private boolean getBit(int index)
	{
		int blockIndex = index / 32;
		int bitIndex = index % 32;
		return (data[blockIndex] & (1 << bitIndex)) != 0;
	}
	
	
	/**
	 * Sets the bit with the given index.
	 */
	private EfficientTerminalSet setBit(int index, boolean value)
	{
		int[] newData = data.clone();
		int blockIndex = index / 32;
		int bitIndex = index % 32;
		if (value)
			newData[blockIndex] |= 1 << bitIndex;
		else
			newData[blockIndex] &= ~(1 << bitIndex);
		return new EfficientTerminalSet(terminals, indices, newData);
	}
	
	
	/**
	 * Returns true, if the given terminal is element of this set,
	 * otherwise false.
	 */
	public boolean contains(Terminal terminal)
	{
		Integer index = indices.get(terminal);
		if (index == null)
			throw new IllegalArgumentException("Unregistered terminal: " + terminal);
		return getBit(index);
	}

	
	/**
	 * Gets the set of terminals.
	 * This method is not efficient (linear time), so use it only at the end
	 * of the computing or for debugging purposes.
	 */
	public Set<Terminal> getTerminals()
	{
		Set<Terminal> ret = set();
		for (Terminal terminal : indices.keySet())
		{
			if (contains(terminal))
				ret.add(terminal);
		}
		return ret;
	}
	
	
	/**
	 * Optimized implementation for another {@link EfficientTerminalSet}.
	 */
	public boolean equals(EfficientTerminalSet terminals)
	{
//		if (this.terminals != terminals.terminals)
//			return false;
		for (int i = 0; i < data.length; i++)
		{
			if (data[i] != terminals.data[i])
				return false;
		}
		return true;
	}
	
	
	/**
	 * Implementation for all classes.
	 */
	@Override public boolean equals(Object o)
	{
		if (o instanceof EfficientTerminalSet)
		{
			return equals((EfficientTerminalSet) o);
		}
		else if (o instanceof TerminalSet)
		{
			Set<Terminal> otherTerminals = ((TerminalSet) o).getTerminals();
			if (otherTerminals.size() != indices.size())
				return false;
			for (Terminal terminal : otherTerminals)
			{
				if (!contains(terminal))
					return false;
			}
			return true;
		}
		else
		{
			return false;
		}
	}
	
	
	@Override public int hashCode()
	{
		int ret = 0;
		for (int d : data)
			ret ^= d;
		return ret;
	}
	
	
	@Override public String toString()
	{
		return "" + getTerminals();
	}
	

}
