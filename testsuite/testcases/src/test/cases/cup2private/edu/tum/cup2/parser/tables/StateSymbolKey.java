package edu.tum.cup2.parser.tables;

import edu.tum.cup2.grammar.Symbol;
import edu.tum.cup2.parser.states.LRParserState;
import java.io.Serializable;

/**
 * Helper class to create a one-dimensional key
 * from a two-dimensional table index
 * ({@link LRParserState} and a {@link Symbol}).
 * 
 * The hash code is always the same for the same
 * state and symbol given, so this class can serve
 * as a hash table key.
 * 
 * This design was chosen to avoid a big tow-dimensional table
 * and use a hashtable instead. The mapping is done as showed here:
 * 
 * <pre>
 *        | a | b | c | ... (Terminals)
 * -------------------------------
 * state1 |   |   |   |
 * -------------------------------
 * state2 |   | x |   |
 * -------------------------------
 * state3 |   |   |   |
 * -------------------------------
 * </pre>
 * 
 * For example, the <code>StateSymbolKey</code> for the action
 * <code>x</code> would be <code>&lt;state2, b&gt;</code>.
 * 
 * @author Andreas Wenger
 */
public final class StateSymbolKey implements Serializable
{
	private static final long serialVersionUID = 1L;
	
	private final LRParserState state;
	private final Symbol symbol;
	
	private final int hashCode;
	
	
	/**
	 * Creates a key for the given state and symbol.
	 */
	public StateSymbolKey(LRParserState state, Symbol symbol)
	{
		//save state and symbol
		this.state = state;
		this.symbol = symbol;
		//compute hash code
		int stateNo = state.getID();
		int symbolNo = 0;
		if (Enum.class.isInstance(symbol))
		{
			//symbols should be enums in most (if not any) cases, but if not,
			//hashing works anyway (just not as good).
			symbolNo = ((Enum<?>)symbol).ordinal();
		}
		hashCode = symbolNo * 1000000 + stateNo; //TODO: find good hash code
	}
	
	
	public Symbol getSymbol()
	{
		return symbol;
	}
	
	
	/**
   * Compares this object to the specified object.
   * The result is {@code true} if and only if the argument is not
   * {@code null} and is an {@link StateSymbolKey} object that
   * contains the same state and symbol as this object.
   */
  public boolean equals(Object obj)
  {
		if (obj instanceof StateSymbolKey)
		{
			StateSymbolKey s = (StateSymbolKey) obj;
			//TODO: or equals? i think == should be ok, since we're working with unique instances
			return state.equals(s.state) && symbol.equals(s.symbol);
		}
		return false;
  }
	
	
	/**
   * Returns the hash code.
   */
  public int hashCode()
  {
  	return hashCode;
  }

}
