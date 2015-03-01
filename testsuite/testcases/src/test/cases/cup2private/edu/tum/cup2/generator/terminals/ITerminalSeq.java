package edu.tum.cup2.generator.terminals;

import java.io.Serializable;

import edu.tum.cup2.grammar.Terminal;
import edu.tum.cup2.util.It;


/**
 * This defines a sequence of {@link Terminal}s.
 * 
 * @author Gero
 * 
 */
public interface ITerminalSeq extends Serializable, Iterable<Terminal>
{
	/**
	 * @param other
	 * @return Returns this, after other has been appended
	 */
	public ITerminalSeq concatenate(ITerminalSeq other);
	
	
	/**
	 * @param terminal
	 * @return Return this, aber terminal has been appended.
	 */
	public ITerminalSeq append(Terminal terminal);
	
	
	/**
	 * @return The first element, w\o removing it! <code>null</code> if empty!
	 */
	public Terminal peek();
	
	
	/**
	 * @return The last element, w\o removing it! <code>null</code> if empty!
	 */
	public Terminal peekLast();
	
	
	/**
	 * @return The first element, and removes it from the sequence. <code>null</code> if empty!
	 */
	public Terminal pop();
	
	
	/**
	 * @return An immutable version of this (an instance of {@link TerminalSeqf}). May be <code>this</code>, if this
	 *         already is immutable!
	 */
	public TerminalSeqf seal();
	
	
	public It<Terminal> getTerminals();
	
	
	public int size();
	
	
	public boolean isEmtpy();
}
