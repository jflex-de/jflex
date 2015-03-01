package edu.tum.cup2.generator.terminals;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import edu.tum.cup2.grammar.Terminal;


/**
 * Naive, mutable implementation of {@link ITerminalSeqSet}. Every given {@link ITerminalSeq} is {@link #seal()}ed
 * before stored!
 * 
 * @author Gero
 * 
 */
public class TerminalSeqSet implements ITerminalSeqSet
{
	private static final long serialVersionUID = -196504058962114385L;
	
	protected final Set<TerminalSeqf> set;
	
	
	public TerminalSeqSet()
	{
		super();
		this.set = new HashSet<TerminalSeqf>();
	}
	
	
	/**
	 * The sequence is based on the orderering of the iterator
	 * 
	 * @param sequences
	 */
	public TerminalSeqSet(Iterable<ITerminalSeq> sequences)
	{
		super();
		this.set = new HashSet<TerminalSeqf>();
		for (ITerminalSeq seq : sequences)
		{
			this.set.add(seq.seal());
		}
	}
	
	
	/**
	 * The sequence is based on the orderering of the array
	 * 
	 * @param sequences
	 */
	public TerminalSeqSet(ITerminalSeq... sequences)
	{
		super();
		this.set = new HashSet<TerminalSeqf>();
		for (ITerminalSeq seq : sequences)
		{
			this.set.add(new TerminalSeqf(seq.getTerminals()));
		}
	}
	
	
	/**
	 * The sequence is based on the orderering of the array
	 * 
	 * @param sequences
	 */
	public TerminalSeqSet(Terminal[]... sequences)
	{
		super();
		this.set = new HashSet<TerminalSeqf>();
		for (Terminal[] seqArr : sequences)
		{
			this.set.add(new TerminalSeqf(seqArr));
		}
	}
	
	
	public ITerminalSeqSet plus(ITerminalSeq terminalSeq)
	{
		this.set.add(terminalSeq.seal());
		return this;
	}
	
	
	public ITerminalSeqSet plusAll(ITerminalSeqSet terminalSeqSet)
	{
		for (ITerminalSeq seq : terminalSeqSet)
		{
			this.set.add(seq.seal());
		}
		return this;
	}
	
	
	public TerminalSeqSetf seal()
	{
		return new TerminalSeqSetf(this);
	}
	
	
	public Iterator<TerminalSeqf> iterator()
	{
		return this.set.iterator();
	}
	
	
	public int getMaxLength()
	{
		int result = 0;
		for (ITerminalSeq seq : this.set)
		{
			result = Math.max(result, seq.size());
		}
		return result;
	}
	
	
	public int size()
	{
		return this.set.size();
	}
	
	
	public boolean isEmpty()
	{
		return this.set.isEmpty();
	}
	
	
	protected int calcHashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((set == null) ? 0 : set.hashCode());
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
		if (!(obj instanceof TerminalSeqSet))
			return false;
		TerminalSeqSet other = (TerminalSeqSet) obj;
		if (set == null)
		{
			if (other.set != null)
				return false;
		} else
		{
			if (this.set.size() != other.set.size())
			{
				return false;
			}
			for (ITerminalSeq seq : this.set)
			{
				if (!other.set.contains(seq))
				{
					return false;
				}
			}
		}
		return true;
	}
	
	
	@Override
	public String toString()
	{
		StringBuilder b = new StringBuilder();
		b.append("{ ");
		boolean first = true;
		for (ITerminalSeq seq : set)
		{
			if (first)
			{
				first = false;
			} else
			{
				b.append(", ");
			}
			b.append(seq.toString());
		}
		b.append(" }");
		return b.toString();
	}
}
