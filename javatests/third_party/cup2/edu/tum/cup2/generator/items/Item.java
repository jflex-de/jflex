package edu.tum.cup2.generator.items;

import edu.tum.cup2.grammar.Production;
import edu.tum.cup2.grammar.Symbol;


/**
 * Interface for LR items.
 * 
 * @author Andreas Wenger
 */
public interface Item
{
	
	public Production getProduction();
	
	public int getPosition();
	
	public Symbol getNextSymbol();
	
	public boolean isShiftable();
	
	public boolean isComplete();
	
	public Item shift();

}
