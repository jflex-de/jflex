package edu.tum.cup2.generator;

import static edu.tum.cup2.grammar.SpecialTerminals.Epsilon;

import java.util.HashSet;
import java.util.LinkedList;

import edu.tum.cup2.grammar.NonTerminal;
import edu.tum.cup2.grammar.Production;
import edu.tum.cup2.grammar.Symbol;


/**
 * Set of non-terminals that can derive the empty string.
 * 
 * @author Andreas Wenger
 */
public class NullableSet extends HashSet<NonTerminal>
{
	private static final long serialVersionUID = -5744818657858711176L;
	
	
	/**
	 * Computes the non-terminals that can derive the empty string.
	 * This algorithm is inspired by Appel's book, page 51.
	 */
	public NullableSet(LinkedList<Production> productions)
	{
		// iterate as long as we get new nullable productions.
		// in the first step, we only get the trivial ones (X → ɛɛɛ...),
		// then the ones using X (e.g. Y -> XɛX), and so on
		boolean changed;
		do
		{
			changed = false;
			for (Production production : productions)
			{
				NonTerminal lhs = production.getLHS();
				// only check non-terminals which are not nullable already
				if (!this.contains(lhs))
				{
					// all rhs symbols nullable?
					boolean nullable = true;
					for (Symbol symbol : production.getRHS())
					{
						if (!(symbol == Epsilon || this.contains(symbol)))
						{
							nullable = false;
							break;
						}
					}
					// then remember it as a nullable terminal
					if (nullable)
					{
						this.add(lhs);
						changed = true;
					}
				}
			}
		} while (changed);
	}
	
}
