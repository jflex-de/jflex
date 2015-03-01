package edu.tum.cup2.generator;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import edu.tum.cup2.generator.terminals.EfficientTerminalSet;
import edu.tum.cup2.grammar.Grammar;
import edu.tum.cup2.grammar.NonTerminal;
import edu.tum.cup2.grammar.Production;
import edu.tum.cup2.grammar.SpecialTerminals;
import edu.tum.cup2.grammar.Symbol;


/**
 * Computes the FOLLOW sets of all non-terminals of the given grammar, using the precomputed set of nullable
 * non-terminals and first sets. This algorithm is inspired by ASU99, page 230 (german edition) (rules).
 * 
 * @author Gero
 */
public class FollowSets
{
	private final Map<NonTerminal, EfficientTerminalSet> data;
	
	
	/**
	 * @param grammar
	 * @param firstSets
	 */
	public FollowSets(Grammar grammar, FirstSets firstSets)
	{
		data = new HashMap<NonTerminal, EfficientTerminalSet>();
		
		// Initialize data map
		final EfficientTerminalSet emptySet = new EfficientTerminalSet(grammar.getTerminals());
		for (NonTerminal nonTerminal : grammar.getNonTerminals())
		{
			data.put(nonTerminal, emptySet); // Viable because all modifiying operations will create new instances
		}
		
		// Rule 1: For start symbol S, add $ to FOLLOW(S)
		final NonTerminal startSymbol = grammar.getStartProduction().getLHS();
		final EfficientTerminalSet startSet = emptySet.plus(SpecialTerminals.EndOfInputStream);
		data.put(startSymbol, startSet);
		
		
		// Actual algorithm: Apply rule 2 and 3 until nothing changes anymore
		boolean changed;
		do
		{
			changed = false;
			
			for (Production production : grammar.getProductions())
			{
				// Rule 2: For a production A → αBβ, every element of FIRST(β) (w/o ɛ) is an element to FOLLOW(B)
				// Rule 3: For productions A → αB (3.1) or A → αBβ with FIRST(β) containing ɛ (3.2), every element of
				// FOLLOW(A) is also an element of FOLLOW(B)
				
				
				// ### Find B (A → αBβ / A → αB => first non-terminal in rhs) ...
				final List<Symbol> rhs = production.getRHS();
				
				NonTerminal nonTerminalB = null;
				Symbol nextSymbol = null;
				
				final Iterator<Symbol> rhsIt = rhs.iterator();
				for (; rhsIt.hasNext();)
				{
					Symbol symbol = rhsIt.next();
					if (symbol instanceof NonTerminal)
					{
						nonTerminalB = (NonTerminal) symbol;
						
						// Great, found non-terminal B! Check for β...
						if (rhsIt.hasNext())
						{
							nextSymbol = rhsIt.next();
						}
						
						break;
					}
				}
				
				if (nonTerminalB == null)
				{
					// If there's no non-terminal: continue with next production
					continue;
				}
				
				
				// ### Check for rule 2 and 3
				final boolean notLastSymbol = !(nextSymbol == null);
				final NonTerminal nonTerminalA = production.getLHS();
				final EfficientTerminalSet followB = data.get(nonTerminalB);
				
				// Differ between αBβ and αB ...
				if (notLastSymbol)
				{
					// Identified A → αBβ => Apply rule 2: Every element of FIRST(β) (w/o ɛ) is added to FOLLOW(B)
					// Important: β may be a list of Symbols! If a first set for a symbol contains ɛ, the following must be considered, to!
					final Symbol beta = nextSymbol;
					EfficientTerminalSet firstBeta = firstSets.get(beta);
					
					// Aggregate first sets if all sets before contained ɛ
					while (rhsIt.hasNext() && firstBeta.contains(SpecialTerminals.Epsilon))
					{
						Symbol symbol = rhsIt.next();
						final EfficientTerminalSet firstNextBeta = firstSets.get(symbol);
						firstBeta = firstBeta.plusAll(firstNextBeta);
					}
					EfficientTerminalSet newFollowB = followB.plusAllExceptEpsilon(firstBeta);
					
					
					// Check for condition 3.2: FIRST(β) contains ɛ
					if (firstBeta.contains(SpecialTerminals.Epsilon))
					{
						// Apply rule 3: Every element of FOLLOW(A) is also an element of FOLLOW(B)
						final EfficientTerminalSet followA = data.get(nonTerminalA);
						newFollowB = newFollowB.plusAll(followA);
					}
					
					
					// Finish: put new FOLLOW(B) into data map and check for changes
					data.put(nonTerminalB, newFollowB);
					changed |= !newFollowB.equals(followB);
				} else
				{
					// Identified A → αB => Apply rule 3: Every element of FOLLOW(A) is also an element of FOLLOW(B)
					final EfficientTerminalSet followA = data.get(nonTerminalA);
					final EfficientTerminalSet newFollowB = followB.plusAll(followA);
					
					
					// Finish: put new FOLLOW(B) into data map and check for changes
					data.put(nonTerminalB, newFollowB);
					changed |= !newFollowB.equals(followB);
				}
			}
		} while (changed);
	}
	
	
	/**
	 * @param nonTerminal
	 * @return Gets the FOLLOW set for the given non-terminal.
	 */
	public EfficientTerminalSet get(NonTerminal nonTerminal)
	{
		return data.get(nonTerminal);
	}
}
