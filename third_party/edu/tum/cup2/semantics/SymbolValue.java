package edu.tum.cup2.semantics;

import edu.tum.cup2.grammar.Symbol;
import edu.tum.cup2.spec.Insertable;


/**
 * This class allows to bind an object of any type
 * to any {@link Symbol}.
 * 
 * @author Andreas Wenger
 */
public class SymbolValue<T>
{
	
	/**
	 * Placeholder for "no value" on the stack.
	 * Don't use null instead, because null has a semantic meaning
	 * and would be ambiguous then.
	 */
	public static final Object NoValue = new Object() { @Override public String toString(){return "NoValue";}};

	protected Symbol symbol = null;
	protected T value = null;
	protected boolean hasValue = false;
	
	
	public Symbol getSymbol()
	{
		return symbol;
	}

	
	public void setSymbol(Symbol symbol)
	{
		this.symbol = symbol;
	}
	
	
	public T getValue()
	{
		return value;
	}

	
	public void setValue(T value)
	{
		this.value = value;
		this.hasValue = true;
	}
	
	
	public boolean hasValue()
	{
		return hasValue;
	}
	

}
