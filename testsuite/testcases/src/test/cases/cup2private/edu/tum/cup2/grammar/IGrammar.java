package edu.tum.cup2.grammar;

import java.util.LinkedList;

/**
 * 
 * @author Michael Hausmann
 *
 */
public interface IGrammar 
{
	public IGrammar extendByAuxStartProduction();
	
	/**
	 * Gets the terminals.
	 */
	public LinkedList<Terminal> getTerminals();
	
	/**
	 * Gets the non-terminals.
	 */
	public LinkedList<NonTerminal>  getNonTerminals();
	
	/**
	 * Gets the number of productions.
	 */
	public int getProductionCount();
	
	
	/**
	 * Gets the start production.
	 */
	public Production getStartProduction();
	
	/**
	 * Gets the production with the given index.
	 */
	public Production getProductionAt(int index);
	
	
	/**
	 * Gets the productions. TIDY
	 */
	public LinkedList<Production> getProductions();

}
