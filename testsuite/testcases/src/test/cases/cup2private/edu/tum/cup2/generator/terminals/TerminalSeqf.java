package edu.tum.cup2.generator.terminals;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import edu.tum.cup2.grammar.Terminal;
import edu.tum.cup2.util.ItIterator;

/**
 * An inmutable implementation of {@link ITerminalSeq}. Mutating methods return new instances.
 * 
 * @author Gero
 * 
 */
public class TerminalSeqf extends TerminalSeq
{
	/**  */
	private static final long serialVersionUID = 4705376116940749033L;
	
	
	private final int hashCode;
	
	
	public TerminalSeqf() {
		super();
		this.hashCode = calcHashCode();
	}
	
	
	/**
	 * The sequence is based on the orderering of the collections iterator
	 * 
	 * @param terminals
	 */
	public TerminalSeqf(Collection<Terminal> terminals)
	{
		super(terminals);
		this.hashCode = calcHashCode();
	}
	
	
	/**
	 * The sequence is based on the orderering of the iterator
	 * 
	 * @param iterator
	 */
	public TerminalSeqf(Iterator<Terminal> iterator)
	{
		super(iterator);
		this.hashCode = calcHashCode();
	}
	
	
	/**
	 * @param terminals
	 */
	public TerminalSeqf(Terminal... terminals)
	{
		super(terminals);
		this.hashCode = calcHashCode();
	}
	
	
	@Override
	public ITerminalSeq concatenate(ITerminalSeq other)
	{
		final ItIterator<Terminal> it = new ItIterator<Terminal>(this.terminals, other.getTerminals());
		return new TerminalSeqf(it);
	}
	
	
	@Override
	public ITerminalSeq append(Terminal terminal)
	{
		final LinkedList<Terminal> newTerminals = new LinkedList<Terminal>(this.terminals);
		newTerminals.add(terminal);
		return new TerminalSeqf(newTerminals);
	}

	
	@Override
	public Terminal pop()
	{
		throw new UnsupportedOperationException("TerminalSeqf.pop() is not implemented!");
	}
	

	public TerminalSeqf seal()
	{
		return this;
	}
	
	
	@Override
	public int hashCode()
	{
		return hashCode;
	}
}
