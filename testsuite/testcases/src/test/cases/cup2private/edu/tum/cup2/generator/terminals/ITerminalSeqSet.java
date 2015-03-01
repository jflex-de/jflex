package edu.tum.cup2.generator.terminals;

import java.io.Serializable;
import java.util.Iterator;


/**
 * This represents a set of {@link ITerminalSeq}uences.
 * 
 * @author Gero
 * 
 */
public interface ITerminalSeqSet extends Iterable<TerminalSeqf>, Serializable
{
	public static final ITerminalSeqSet EMPTY = new TerminalSeqSetf();
	
	/**
	 * Returns this set of terminal sequences, with the given one added.
	 */
	public ITerminalSeqSet plus(ITerminalSeq terminalSeq);
	
	
	/**
	 * Returns this set of terminal sequences merged with the given one.
	 */
	public ITerminalSeqSet plusAll(ITerminalSeqSet terminalSeqSet);
	
	
	/**
	 * @return An immutable version of this (an instance of {@link TerminalSeqSetf}). May be <code>this</code>, if this
	 *         already is immutable!
	 */
	public TerminalSeqSetf seal();
	
	
	public Iterator<TerminalSeqf> iterator();
	
	
	/**
	 * @return The maximal length of an {@link ITerminalSeq} in this set.
	 */
	public int getMaxLength();
	
	
	/**
	 * @return The number of {@link ITerminalSeq} this set contains
	 */
	public int size();
	
	
	/**
	 * @return Whether this set is empty or not
	 */
	public boolean isEmpty();
}
