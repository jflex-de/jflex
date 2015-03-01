package edu.tum.cup2.generator;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import edu.tum.cup2.generator.exceptions.LLkGeneratorException;
import edu.tum.cup2.generator.items.Item;
import edu.tum.cup2.generator.terminals.ITerminalSeq;
import edu.tum.cup2.generator.terminals.ITerminalSeqSet;
import edu.tum.cup2.generator.terminals.TerminalSeqSet;
import edu.tum.cup2.generator.terminals.TerminalSeqf;
import edu.tum.cup2.grammar.Grammar;
import edu.tum.cup2.grammar.NonTerminal;
import edu.tum.cup2.grammar.Production;
import edu.tum.cup2.grammar.SpecialTerminals;
import edu.tum.cup2.grammar.Symbol;
import edu.tum.cup2.grammar.Terminal;


/**
 * This class serves as factory for lookaheads of arbitrary and fixed length for a given grammar. The algorithm is a
 * somewhat intuitive approach.
 * 
 * @author Gero
 * 
 */
public class LookaheadGenerator
{
	// private final int terminalsSize;
	private final Map<NonTerminal, List<Production>> productionsFor;
	private final Map<NonTerminal, List<BasicItem>> followItems;
	
	
	/**
	 * @param gr
	 */
	public LookaheadGenerator(Grammar gr)
	{
		// this.terminalsSize = gr.getTerminals().contains(SpecialTerminals.EndOfInputStream) ? gr.getTerminals().size() -
		// 1
		// : gr.getTerminals().size();
		//
		this.productionsFor = calcProductionsStartingWith(gr);
		this.followItems = calcFollowItems(gr);
	}
	
	
	/**
	 * @param gr
	 */
	public LookaheadGenerator(GrammarInfo gri)
	{
		this.productionsFor = gri.getProductionsFrom();
		this.followItems = calcFollowItems(gri.getGrammar());
	}
	
	
	/**
	 * Build map NonTerminal (1) -> (*) productions (starting with NonTerminal)
	 * 
	 * @param gr
	 * @return
	 */
	private static Map<NonTerminal, List<Production>> calcProductionsStartingWith(Grammar gr)
	{
		final HashMap<NonTerminal, List<Production>> productionsStartingWith = new HashMap<NonTerminal, List<Production>>();
		for (NonTerminal nonTerminal : gr.getNonTerminals())
		{
			final List<Production> list = new LinkedList<Production>();
			for (Production p : gr.getProductions())
			{
				if (p.getLHS() == nonTerminal)
					list.add(p);
			}
			productionsStartingWith.put(nonTerminal, Collections.unmodifiableList(list));
		}
		return productionsStartingWith;
	}
	
	
	private static Map<NonTerminal, List<BasicItem>> calcFollowItems(Grammar gr)
	{
		// Build map of NonTerminal (1) -> (*) "follow Item" associations
		HashMap<NonTerminal, List<BasicItem>> followItems = new HashMap<NonTerminal, List<BasicItem>>();
		for (Production production : gr.getProductions())
		{
			final List<Symbol> rhs = production.getRHS();
			int i = 0;
			for (Symbol symbol : rhs)
			{
				if (symbol instanceof NonTerminal)
				{
					final NonTerminal nonTerminal = (NonTerminal) symbol;
					List<BasicItem> items = followItems.get(nonTerminal);
					if (items == null)
					{
						items = new LinkedList<BasicItem>();
						followItems.put(nonTerminal, items);
					}
					items.add(new BasicItem(production, i + 1));
				}
				
				i++;
			}
		}
		// // Assure that the lookaheads for the transition start -> (end) will be generated
		// if (gr.getStartProduction().getLHS() == SpecialNonTerminals.StartLHS)
		// {
		// final Production pseudoProduction = new Production(-1, SpecialNonTerminals.StartLHS,
		// SpecialTerminals.EndOfInputStream);
		// addRelation(SpecialNonTerminals.StartLHS, pseudoProduction, 0);
		// }
		return followItems;
	}
	
	
	public ITerminalSeqSet calcLookahead(Item forItem, int laLength) throws LLkGeneratorException
	{
		return calcLookahead(forItem.getProduction(), forItem.getPosition(), laLength);
	}
	
	
	public ITerminalSeqSet calcLookahead(Production production, int position, int laLength) throws LLkGeneratorException
	{
		final BasicItem startItem = new BasicItem(production, position);
		final TerminalSeqf emptyStartPath = new TerminalSeqf();
		final TerminalSeqSet result = new TerminalSeqSet();
		try
		{
			descend(startItem, 0, laLength, emptyStartPath, result);
		} catch (StackOverflowError err)
		{
			throw new LLkGeneratorException(
					"Cannot create lookahead; the grammar seems to contain non-trivial left-recursions!", err);
		}
		return result;
	}
	
	
	/**
	 * @param curItem The item which is currently handled
	 * @param curDepth The recusrion depth the algorithm is actually in. > 0 means processing the items symbols (first),
	 *           < 0 means somewhere behind the item (follow), == 0 means current item.
	 * @param maxLength The length the branches/paths should try to reach
	 * @param parentPath The path which lays before the curItem
	 * @param result Here all finished paths are stored
	 * @return The currently not finished paths
	 * @throws LLkGeneratorException In case the given a left-recursion is detected
	 */
	private ITerminalSeqSet descend(BasicItem curItem, int curDepth, final int maxLength, TerminalSeqf parentPath,
			TerminalSeqSet result) throws LLkGeneratorException
	{
		TerminalSeqSet curPaths = new TerminalSeqSet(parentPath);
		
		// Walk the path along the current item..
		{
			final List<Symbol> rhs = curItem.getProduction().getRHS();
			for (int pos = curItem.getPosition(); pos < rhs.size() && curPaths.size() > 0; pos++)
			{
				// Handle symbol
				final Symbol curSymbol = rhs.get(pos);
				if (curSymbol instanceof Terminal)
				{
					// Handle terminal: Simply add it to all current paths
					final Terminal curTerminal = (Terminal) curSymbol;
					
					// Jump over Epsilon as it's not visible
					if (curTerminal == SpecialTerminals.Epsilon)
					{
						continue;
					}
					
					// Else: add Terminal to current path!
					final Iterator<TerminalSeqf> it = curPaths.iterator();
					final TerminalSeqSet newCurPaths = new TerminalSeqSet();
					while (it.hasNext())
					{
						final TerminalSeqf curPath = it.next();
						it.remove(); // Remove it, it has to be changed anyway
						final ITerminalSeq curPathFinished = curPath.append(curTerminal);
						
						// Check for length: if...
						if (curPath.size() + 1 == maxLength || curTerminal == SpecialTerminals.EndOfInputStream)
						{
							// ... we'll reach maxLength in this step:
							// Stop walking this path, and save it as branch in result
							result.plus(curPathFinished);
						} else
						{
							// else: We have to follow this path a bit longer. Add it so it is not forgotten
							newCurPaths.plus(curPathFinished);
						}
					}
					curPaths = newCurPaths;
				} else if (curSymbol instanceof NonTerminal)
				{
					// Handle non-terminal: Add all possible paths to all current paths
					final NonTerminal curNonTerminal = (NonTerminal) curSymbol;
					
					// For all currently open paths: Follow all paths the non-terminal allows/resolves to
					// (and store the result as new current paths, as the non-terminal is then consumed!)
					final TerminalSeqSet newCurPaths = new TerminalSeqSet();
					for (TerminalSeqf curPath : curPaths)
					{
						// For the current path: Follow all paths the non-terminal allows
						for (Production nextProduction : this.productionsFor.get(curNonTerminal))
						{
							// // Check for trivial left recursion, 0 and 1 step deep
							// final Symbol nextFirstRhsSymbol = nextProduction.getRHS().get(0);
							// if (nextFirstRhsSymbol == curNonTerminal)
							// {
							// // 0: rule resolves to itself
							// throw new LLkGeneratorException("The production " + nextProduction
							// + " is left-recursive; LLkGenerator cannot handle such a grammar!");
							// } else if (nextFirstRhsSymbol == curItem.getProduction().getLHS())
							// {
							// // Check productivity before
							// if (!isProductiveBefore(curItem, nextProduction.getLHS()))
							// { // 1: rule resolves to itself with one intermediate rule
							// throw new LLkGeneratorException("The production " + nextProduction
							// + " is left-recursive; LLkGenerator cannot handle such a grammar!");
							// }
							// }
							
							final ITerminalSeqSet newPossiblePaths = descend(new BasicItem(nextProduction, 0), curDepth + 1,
									maxLength, curPath, result);
							newCurPaths.plusAll(newPossiblePaths);
						}
					}
					
					// Now we consumed the current non-terminal, and have to procceed with the paths we resolved it to
					curPaths = newCurPaths;
				}
			}
		}
		
		// At this point, we finished the item, but there might still be branches/paths which are not long enough.
		// How we have to decide: If we are somewhere down in the tree, we simply return our current paths and can rely
		// on the upper contexts to handle the following symbols.
		// But if we are at the the top, we have to take into account all possible following productions.
		if (curDepth > 0)
		{
			return curPaths;
		} else
		{
			if (!curPaths.isEmpty())
			{
				// As long as there are items following the one we just resolved:
				// Resolve all possible 'follow' items to new paths and return them
				final ITerminalSeqSet newCurPaths = new TerminalSeqSet();
				final List<BasicItem> followups = this.followItems.get(curItem.getProduction().getLHS());
				if (followups != null && !followups.isEmpty())
				{
					for (TerminalSeqf curPath : curPaths)
					{
						for (BasicItem item : followups)
						{
							// Check
							final List<Symbol> rhs = item.getProduction().getRHS();
							final Symbol lastSymbol = rhs.get(rhs.size() - 1);
							if (lastSymbol == curItem.getProduction().getLHS())
							{
								// Nothing to gain here, except an StackOverflow!
								continue;
							}
							
							final ITerminalSeqSet newPossiblePaths = descend(item, curDepth - 1, maxLength, curPath, result);
							newCurPaths.plusAll(newPossiblePaths);
						}
					}
					
					return newCurPaths;
				} else
				{
					// There might be still some paths which are not finished yet because they are not resolveable for
					// maxLength.
					// Simply add them to the tree!
					final Iterator<TerminalSeqf> it = curPaths.iterator();
					while (it.hasNext())
					{
						final TerminalSeqf pathStub = it.next();
						if (!pathStub.isEmtpy())
						{
							result.plus(pathStub);
							it.remove();
						}
					}
				}
			}
			
			return curPaths;
		}
	}
	
	
	// /**
	// * Not yet ready!!!
	// *
	// * @param item
	// * @param targetSymbol
	// * @return
	// */
	// private static boolean isProductiveBefore(BasicItem item, Symbol targetSymbol)
	// {
	// final Iterator<Symbol> rhsIt = item.getProduction().getRHS().iterator();
	// while (rhsIt.hasNext())
	// {
	// final Symbol curSymbol = rhsIt.next();
	//
	// if (curSymbol.equals(targetSymbol))
	// {
	// return false;
	// }
	//
	// if (curSymbol instanceof Terminal)
	// {
	// return true;
	// }
	// }
	// return false;
	// }
	
	/**
	 * Simple container
	 */
	private static class BasicItem
	{
		private final Production production;
		private final int position;
		
		
		public BasicItem(Production production, int position)
		{
			this.production = production;
			this.position = position;
		}
		
		
		public Production getProduction()
		{
			return production;
		}
		
		
		public int getPosition()
		{
			return position;
		}
		
		
		@Override
		public String toString()
		{
			return production.toString(position);
		}
	}
}
