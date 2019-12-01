package edu.tum.cup2.spec.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edu.tum.cup2.grammar.SpecialTerminals;
import edu.tum.cup2.grammar.Symbol;
import edu.tum.cup2.grammar.Terminal;


/**
 * {@link RHSItem}-compatible wrapper around a {@link List}
 * of {@link Symbol}s.
 * 
 * @author Andreas Wenger
 */
public final class RHSSymbols
	implements RHSItem
{
	
	private final List<Symbol> symbols;
	private final Terminal precedence;
	
	public RHSSymbols(Symbol... symbols)
	{
		if (symbols.length == 0)
		{
			//empty RHS means epsilon
			symbols = new Symbol[]{SpecialTerminals.Epsilon};
		}
		else
		{
			//check, that there is no epsilon in a RHS
			for (Symbol symbol : symbols)
			{
				if (symbol == SpecialTerminals.Epsilon)
				{
					throw new IllegalArgumentException("Epsilon may not be used in a RHS!");
				}
			}
		}
		this.symbols = Arrays.asList(symbols);
		precedence=null;
	}
	private RHSSymbols(Terminal precedence, List<Symbol> syms){
		this.precedence=precedence;
		this.symbols=syms;
	}
	
	public RHSSymbols setPrec(Terminal precedence){
		return new RHSSymbols(precedence,new ArrayList<Symbol>(symbols));
	}
	public boolean hasSpecialPrecedence(){
		return precedence!=null;
	}
	public Terminal getPrecedence(){
		return precedence;
	}
	public List<Symbol> getSymbols()
	{
		return symbols;
	}
	

}
