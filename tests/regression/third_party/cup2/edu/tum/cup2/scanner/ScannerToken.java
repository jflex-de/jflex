package edu.tum.cup2.scanner;

import edu.tum.cup2.grammar.Symbol;
import edu.tum.cup2.grammar.Terminal;
import edu.tum.cup2.semantics.SymbolValue;


/**
 * Class for tokens delivered by the scanner.
 * 
 * They either consist of a {@link Terminal} or of
 * a {@link Terminal} with a semantic value.
 * 
 * @author Andreas Wenger
 */
public class ScannerToken<T>
	extends SymbolValue<T>
{
	
	private int line = -1; //unknown
	private int column = -1; //unknown
	
	
	public ScannerToken(Terminal terminal, int line, int column)
	{
		this.symbol = terminal;
		this.line = line;
		this.column = column;
	}
	
	
	public ScannerToken(Terminal terminal, T value, int line, int column)
	{
		this.symbol = terminal;
		this.value = value;
		this.hasValue = true;
		this.line = line;
		this.column = column;
	}
	
	
	public ScannerToken(Terminal terminal)
	{
		this.symbol = terminal;
	}
	
	
	public ScannerToken(Terminal terminal, T value)
	{
		this.symbol = terminal;
		this.value = value;
		this.hasValue = true;
	}
	
	
	@Override public Terminal getSymbol()
	{
		return (Terminal) symbol;
	}

	
	@Override public void setSymbol(Symbol terminal)
	{
		if (!(symbol instanceof Terminal))
			throw new IllegalArgumentException("symbol is no Terminal");
		super.setSymbol(symbol);
	}
	
	
	@Override public String toString()
	{
		if (hasValue)
			return symbol + " (" + value + ")";
		else
			return symbol + "";
	}
	
	
	/**
	 * Gets the number of the line in the text file where this
	 * token was found, or -1 if unknown.
	 */
	public int getLine()
	{
		return line;
	}
	
	
	/**
	 * Gets the number of the column in the text file where this
	 * token was found, or -1 if unknown.
	 */
	public int getColumn()
	{
		return column;
	}
	

}
