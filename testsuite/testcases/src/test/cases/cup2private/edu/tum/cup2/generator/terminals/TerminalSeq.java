package edu.tum.cup2.generator.terminals;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import edu.tum.cup2.grammar.Terminal;
import edu.tum.cup2.util.It;


/**
 * Naive, mutable implementation of {@link ITerminalSeq}.
 * 
 * @author Gero
 * 
 */
public class TerminalSeq implements ITerminalSeq
{
	private static final long serialVersionUID = -2583663816926783360L;
	
	protected final LinkedList<Terminal> terminals;
	
	
	public TerminalSeq()
	{
		this.terminals = new LinkedList<Terminal>();
	}
	
	
	/**
	 * The sequence is based on the orderering of the collections iterator
	 * 
	 * @param terminals
	 */
	public TerminalSeq(Collection<Terminal> terminals)
	{
		this.terminals = new LinkedList<Terminal>(terminals);
	}
	
	
	/**
	 * The sequence is based on the orderering of the iterator
	 * 
	 * @param iterator
	 */
	public TerminalSeq(Iterator<Terminal> iterator)
	{
		this.terminals = new LinkedList<Terminal>();
		while (iterator.hasNext())
		{
			this.terminals.add(iterator.next());
		}
	}
	
	
	/**
	 * @param terminals
	 */
	public TerminalSeq(Terminal... terminals)
	{
		this.terminals = new LinkedList<Terminal>();
		for (Terminal terminal : terminals)
		{
			this.terminals.add(terminal);
		}
	}
	
	
	public ITerminalSeq concatenate(ITerminalSeq other)
	{
		for (Terminal terminal : other.getTerminals())
		{
			terminals.add(terminal);
		}
		return this;
	}
	
	
	public ITerminalSeq append(Terminal terminal)
	{
		terminals.add(terminal);
		return this;
	}
	
	
	public Terminal peek()
	{
		return terminals.peek();
	}
	
	
	public Terminal peekLast()
	{
		return terminals.peekLast();
	}
	
	
	public Terminal pop()
	{
		return terminals.pop();
	}


	public TerminalSeqf seal()
	{
		return new TerminalSeqf(terminals);
	}
	
	
	public It<Terminal> getTerminals()
	{
		return new It<Terminal>(terminals);
	}
	
	
	public int size()
	{
		return terminals.size();
	}
	
	
	public boolean isEmtpy()
	{
		return terminals.isEmpty();
	}
	
	
	public Iterator<Terminal> iterator()
	{
		return terminals.iterator();
	}
	
	
	protected int calcHashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((terminals == null) ? 0 : terminals.hashCode());
		return result;
	}
	
	
	@Override
	public int hashCode()
	{
		return calcHashCode();
	}


	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof TerminalSeq))
			return false;
		TerminalSeq other = (TerminalSeq) obj;
		if (terminals == null)
		{
			if (other.terminals != null)
				return false;
		} else {
			final Iterator<Terminal> thisIt = this.terminals.iterator();
			final Iterator<Terminal> otherIt = other.terminals.iterator();
			while (thisIt.hasNext() && otherIt.hasNext())
			{
				if (!thisIt.next().equals(otherIt.next()))
				{
					return false;
				}
			}
			return thisIt.hasNext() == otherIt.hasNext();
		}
		return true;
	}


	@Override
	public String toString()
	{
		StringBuilder b = new StringBuilder();
		boolean first = true;
		for (Terminal t : terminals)
		{
			if (first)
			{
				first = false;
			} else {
				b.append('-');
			}
			b.append(t);
		}
		return b.toString();
	}
}
