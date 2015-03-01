package edu.tum.cup2.generator.terminals;

import java.util.Iterator;

import edu.tum.cup2.grammar.Terminal;
import edu.tum.cup2.util.It;


/**
 * Immutable version of {@link TerminalSeqSet} which caches its hashCode. Mutating methods return new instances.
 * 
 * @author Gero
 * 
 */
public class TerminalSeqSetf extends TerminalSeqSet
{
	private static final long serialVersionUID = 1187751793935067440L;
	private final int hashCode;
	private final int maxLength;
	
	
	public TerminalSeqSetf()
	{
		super();
		this.maxLength = 0;
		this.hashCode = calcHashCode();
	}
	
	
	public TerminalSeqSetf(ITerminalSeqSet sequences)
	{
		super();
		for (ITerminalSeq seq : sequences)
		{
			this.set.add(seq.seal());
		}
		this.maxLength = super.getMaxLength();
		this.hashCode = calcHashCode();
	}
	
	
	/**
	 * The sequence is based on the orderering of the iterator
	 * 
	 * @param sequences
	 */
	public TerminalSeqSetf(Iterable<ITerminalSeq> sequences)
	{
		super();
		for (ITerminalSeq seq : sequences)
		{
			this.set.add(seq.seal());
		}
		this.maxLength = super.getMaxLength();
		this.hashCode = calcHashCode();
	}
	
	
	/**
	 * The sequence is based on the orderering of the array
	 * 
	 * @param sequences
	 */
	public TerminalSeqSetf(ITerminalSeq... sequences)
	{
		super();
		for (ITerminalSeq seq : sequences)
		{
			this.set.add(seq.seal());
		}
		this.maxLength = super.getMaxLength();
		this.hashCode = calcHashCode();
	}
	
	
	/**
	 * The sequence is based on the orderering of the array
	 * 
	 * @param sequences
	 */
	public TerminalSeqSetf(Terminal[]... sequences)
	{
		super();
		for (Terminal[] seqArr : sequences)
		{
			this.set.add(new TerminalSeqf(seqArr));
		}
		this.maxLength = super.getMaxLength();
		this.hashCode = calcHashCode();
	}
	
	
	public ITerminalSeqSet plus(ITerminalSeq terminalSeq)
	{
		throw new UnsupportedOperationException("TerminalSeqSetf.plus(ITerminalSeq) is not implemented!");
	}
	
	
	public ITerminalSeqSet plusAll(ITerminalSeqSet terminalSeqSet)
	{
		throw new UnsupportedOperationException("TerminalSeqSetf.plusAll(ITerminalSeqSet) is not implemented!");
	}
	
	
	public TerminalSeqSetf seal()
	{
		return this;
	}
	
	
	public Iterator<TerminalSeqf> iterator()
	{
		return new It<TerminalSeqf>(this.set);
	}
	
	
	public int getMaxLength()
	{
		return maxLength;
	}
	
	
	@Override
	public int hashCode()
	{
		return hashCode;
	}
}
