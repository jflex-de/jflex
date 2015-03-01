package edu.tum.cup2.precedences;

import java.util.List;

import edu.tum.cup2.grammar.Production;
import edu.tum.cup2.grammar.Terminal;


/**
 * Define the precedences of a grammer.
 * These are needed for ambiguous grammars to resolve
 * shift-reduce conflicts.
 * 
 * @author Andreas Wenger
 */
public class Precedences
{
	
	private List<Associativity> list;
	
	
	/**
	 * Creates the {@link Precedences} from the given list of
	 * {@link Associativity} instances. The first associativity has
	 * lowest priority, the last one highest priority.
	 */
	public Precedences(List<Associativity> list)
	{
		this.list = list;
	}
	
	
	/**
	 * Returns 1, when the given production has higher precedence than the given
	 * terminal, -1 if it is vice versa or 0 if they have the same precedence.
	 */
	public int compare(Production production, Terminal terminal)
	{
		Terminal prodTerm = production.getPrecedenceTerminal();
		if (prodTerm == null)
		{
			//precedence is equal, if there is also no precedence for the given terminal,
			//otherwise the terminal wins
			for (Associativity ass : list)
			{
				if (ass.contains(terminal))
					return -1;
			}
			return 0;
		}
		else
		{
			return compare(prodTerm, terminal);
		}
	}
	
	
	/**
	 * Returns 1, when the first given terminal has higher precedence than the second given
	 * terminal, -1 if it is vice versa or 0 if they have the same precedence.
	 */
	public int compare(Terminal terminal1, Terminal terminal2)
	{
		//if terminals are equal, return 0
		if (terminal1 == terminal2)
			return 0;
		//go from highest precedence level to lowest one. the first terminal found
		//(without the other one being there, too) wins
		for (Associativity ass : list)
		{
			boolean c1 = ass.contains(terminal1);
			boolean c2 = ass.contains(terminal2);
			if (!c1)
			{
				if (!c2)
					continue; //next round
				else
					return -1; //2 wins
			}
			else
			{
				if (!c2)
					return 1; //1 wins
				else
					return 0; //equal
			}
		}
		return 0; //both unknown
	}
	
	
	/**
	 * Returns the associativity of the given terminal, or null if unknown.
	 */
	public Associativity getAssociativity(Terminal terminal)
	{
		for (Associativity ass : list)
		{
			if (ass.contains(terminal))
				return ass;
		}
		return null;
	}


}
