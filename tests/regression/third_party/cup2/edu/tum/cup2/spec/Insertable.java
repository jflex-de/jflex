package edu.tum.cup2.spec;

import java.util.List;

import edu.tum.cup2.grammar.Symbol;
import edu.tum.cup2.grammar.Terminal;
import edu.tum.cup2.scanner.ScannerToken;

import static edu.tum.cup2.semantics.SymbolValue.NoValue;


public class Insertable
{

	public Object		value		= NoValue;
	public Object[]		possible	= null;
	public int			precedence;
	public Symbol		symbol		= null;
	public int			times		= 0;

	private static int	cur_prec	= Integer.MIN_VALUE;

	public Insertable(int times, Symbol x, Object[] possible4User, Object value) {
		this.times = times;
		this.symbol = x;
		this.precedence = cur_prec++;
		// TODO: is wrong symbol-value-binding given? -> error
		this.possible = possible4User;
		this.value = value;
	}
	public String toString() {
		return value + " (" + symbol + "," + precedence + ")";
	}
}
