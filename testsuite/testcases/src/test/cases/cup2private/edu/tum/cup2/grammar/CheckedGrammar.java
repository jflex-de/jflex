package edu.tum.cup2.grammar;

import java.util.Hashtable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

import edu.tum.cup2.util.ArrayTools;

/**
 * CheckedGrammar 
 * also has additional information (reachability and productivity) for symbols,
 * 
 * CheckedGrammar is a decorator for Grammars
 *
 * @author Michael Hausmann
 * @author Dangl Stefan
 *
 */
public class CheckedGrammar implements IGrammar 
{
	public static final long serialVersionUID = 1L;
	private final IGrammar grammar;
	private Hashtable<Symbol, SymbolInfo> symbolInfos = 
		new Hashtable<Symbol, SymbolInfo>();
	
	//grammar doesn't change therefore checking the productivity and reachability once is enough
	private boolean bProductivityChecked = false;
	private boolean bReachabilityChecked = false;
	private final boolean	DEBUG = false;
	
	/**
	 * Creates a new {@link CheckedGrammar}, using the given Grammar
	 */
	public CheckedGrammar(IGrammar g)
	{
		grammar = g;
	}
	
	/**
	 * Creates a new {@link Grammar}, using the given list of
	 * {@link Production}s. The first production is the start
	 * production of the grammar. 
	 */
	public CheckedGrammar(LinkedList<Terminal> terminals, LinkedList<NonTerminal> nonTerminals, LinkedList<Production> productions)
	{
		grammar = new Grammar(terminals, nonTerminals, productions);
	}
	
	/**
	 * Creates a new {@link Grammar}, using the given list of
	 * {@link Production}s. The first production is the start
	 * production of the grammar. 
	 */
	public CheckedGrammar(Terminal[] terminals, NonTerminal[] nonTerminals, Production[] productions)
	{
		this(ArrayTools.toLinkedListT(terminals),
			ArrayTools.toLinkedListT(nonTerminals),
			ArrayTools.toLinkedListT(productions));
	}
	
	/**
	 * SymbolInfo
	 * stores additional information to symbols in the grammar
	 * e.g: is a NonTerminal reachable?
	 *
	 * @author Michael Hausmann
	 *
	 */
	private class SymbolInfo
	{
		public Symbol symbol = null; //symbol that a SymbolInfo refers to
		public boolean bReachable = false;
		public HashSet<NonTerminal> producedNonTerminalsSet = new HashSet<NonTerminal>();
		public boolean bProductive = false;
		
		public int hashCode()
		{
			return symbol.hashCode();
		}
		
		/**
		 * SymbolInfos are equal if they belong to same symbol
		 */
		public boolean equals(Object obj)
		{
			if(obj == null) return false;
			if(!(obj instanceof SymbolInfo)) return false;
			SymbolInfo otherSymbolInfo = (SymbolInfo) obj;
			return (this.symbol.equals(otherSymbolInfo.symbol));
		}
	} /* end of class */
	
	/**
	 * creates the dependency graph for NonTerminals
	 */
	private void createProducedNonTerminalInfo()
	{
		LinkedList<Production> prods = this.getProductions(); //all productions
		for(Production prod : prods)
		{
			SymbolInfo i = this.symbolInfos.get(prod.getLHS());
			if(i != null)
			{
				List<Symbol> rhsSymbols = prod.getRHS();
				for(Symbol rhsSymbol : rhsSymbols )
				{
					if(rhsSymbol instanceof NonTerminal)
					{
						NonTerminal nt = (NonTerminal) rhsSymbol;
						
						//for every production add every NonTerminal on the rhs to the SymbolInfo of the lhs
						i.producedNonTerminalsSet.add(nt);
					}
				}
			}
		}
	}
	
	/**
	 * set NonTerminal lhs reachable 
	 * and also set dependent symbols reachable recursively
	 * @param lhs
	 */
	private void setReachable(NonTerminal lhs)
	{
		SymbolInfo i = this.symbolInfos.get(lhs);
		if(i.bReachable == true) return; //exit loops
		i.bReachable = true;
		for(NonTerminal nt : i.producedNonTerminalsSet)
		{
			setReachable(nt);
		}
	}
	
	/**
	 * method to check if all NonTerminals in the grammar are reachable
	 */
	protected void checkReachability()
	{
		//grammar doesn't change therefore checking the productivity and reachability once is enough
		if(this.bReachabilityChecked) return;
		
		//get all NonTerminals
		LinkedList<NonTerminal> ntList = getNonTerminals();
		
		//create a SymbolInfo for every NonTerminal 
		for(NonTerminal nt : ntList)
		{
			if(nt == null) continue;
			if(!this.symbolInfos.containsKey(nt))
			{
				SymbolInfo i = new SymbolInfo();
				i.symbol = nt;
				this.symbolInfos.put(nt, i);
			}
		}
		
		createProducedNonTerminalInfo();
		
		//check reachability with depth first search starting with the start symbol
		Production startProd = this.getStartProduction();
		NonTerminal startState = startProd.getLHS();
		setReachable(startState);
		
		this.bReachabilityChecked = true;
	}
	
	/**
	 * getReachableNonTerminals
	 * returns a LinkedList of reachable NonTerminals
	 * @return list of reachable NonTerminals
	 */
	public LinkedList<NonTerminal> getReachableNonTerminals()
	{
		this.checkReachability();
		
		LinkedList<NonTerminal> ret = new LinkedList<NonTerminal>();
		Collection<SymbolInfo> c = this.symbolInfos.values(); 
		for(SymbolInfo i : c)
		{
			if((i.symbol instanceof NonTerminal)
				&& i.bReachable)
			{
				ret.add((NonTerminal) i.symbol);
			}
		}
		return ret;
	}
	
	/**
	 * getNotReachableNonTerminals
	 * returns a LinkedList of  n o t  reachable NonTerminals
	 * @return list of reachable NonTerminals
	 */
	public LinkedList<NonTerminal> getNotReachableNonTerminals()
	{
		this.checkReachability();
		
		LinkedList<NonTerminal> ret = new LinkedList<NonTerminal>();
		Collection<SymbolInfo> c = this.symbolInfos.values(); 
		for(SymbolInfo i : c)
		{
			if((i.symbol instanceof NonTerminal)
				&& !i.bReachable)
			{
				ret.add((NonTerminal) i.symbol);
			}
		}
		return ret;
	}

	/**
	 * convenience method to query if a grammar has  n o t reachable NonTerminals
	 * @return
	 */
	public boolean hasNotReachableNonTerminals()
	{
		this.checkReachability();
		Collection<SymbolInfo> c = this.symbolInfos.values(); 
		for(SymbolInfo i : c)
		{
			if((i.symbol instanceof NonTerminal)
				&& !i.bReachable)
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * convenience method to query if a grammar has  n o t productive NonTerminals
	 * @return
	 */
	public boolean hasNotProductiveNonTerminals()
	{
		this.checkProductivity();
		Collection<SymbolInfo> c = this.symbolInfos.values(); 
		for(SymbolInfo i : c)
		{
			if((i.symbol instanceof NonTerminal)
				&& !i.bProductive)
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * convenience method to query if the corresponding original grammar is reduced
	 * @return true if the grammar is reduced; i.e: has no unreachable and no unproductive NonTerminals
	 */
	public boolean isReduced()
	{
		return ( !hasNotReachableNonTerminals() && !hasNotProductiveNonTerminals() );
	}
	
	/**
	 * getProductiveNonTerminals
	 * returns a LinkedList of productive NonTerminals
	 * @return list of productive NonTerminals
	 */
	public LinkedList<NonTerminal> getProductiveNonTerminals()
	{
		this.checkProductivity();
		
		LinkedList<NonTerminal> ret = new LinkedList<NonTerminal>();
		Collection<SymbolInfo> c = this.symbolInfos.values(); 
		for(SymbolInfo i : c)
		{
			if((i.symbol instanceof NonTerminal)
				&& i.bProductive)
			{
				ret.add((NonTerminal) i.symbol);
			}
		}
		return ret;
	}
	
	/**
	 * getNotProductiveNonTerminals
	 * returns a LinkedList of  n o t  productive NonTerminals
	 * @return list of productive NonTerminals
	 */
	public LinkedList<NonTerminal> getNotProductiveNonTerminals()
	{
		this.checkProductivity();
		
		LinkedList<NonTerminal> ret = new LinkedList<NonTerminal>();
		Collection<SymbolInfo> c = this.symbolInfos.values(); 
		for(SymbolInfo i : c)
		{
			if((i.symbol instanceof NonTerminal)
				&& !i.bProductive)
			{
				ret.add((NonTerminal) i.symbol);
			}
		}
		return ret;
	}
	
	/**
	 * getProductiveAndReachableNonTerminals
	 * returns a LinkedList of productive and reachable NonTerminals
	 * @return list of productive and reachable NonTerminals
	 */
	public LinkedList<NonTerminal> getProductiveAndReachableNonTerminals()
	{
		this.checkReachability();
		this.checkProductivity();
		
		LinkedList<NonTerminal> ret = new LinkedList<NonTerminal>();
		Collection<SymbolInfo> c = this.symbolInfos.values(); 
		for(SymbolInfo i : c)
		{
			if((i.symbol instanceof NonTerminal)
				&& i.bProductive
				&& i.bReachable)
			{
				ret.add((NonTerminal) i.symbol);
			}
		}
		return ret;
	}
	
	/** checkProductivity 
	 * checks the productivity of each NonTerminal using the 
	 * algorithm from the compiler construction lecture (page 92).
	 * (fixed point iteration)
	 */
	protected void checkProductivity()
	{
		//grammar doesn't change therefore checking the productivity and reachability once is enough
		if (this.bProductivityChecked) return;
		
		//result set containing PRODUCTIVE NTs
		List<NonTerminal> result = new LinkedList<NonTerminal>();
		
		//set of all productions
		List<Production> P = this.getProductions();
		
		//how many NonTerminals (potentially not productive ones) does a production contain?
		Hashtable<Production, Integer> count = new Hashtable<Production, Integer>();
		
		//set of all NTs
		List<NonTerminal> N = this.getNonTerminals();
		
		//rhs: mapping from NT to right hand sides in which it occurs
		//NOTE: if NT occurs multiple times in one production, then the list contains the productions multiple times 
		Hashtable<NonTerminal, LinkedList<Production>> rhs = 
			new Hashtable<NonTerminal, LinkedList<Production>>();
		
		//Initialization
		for(NonTerminal A : N)
		{
			rhs.put(A, new LinkedList<Production>());
		}
		for(Production Ai : P)
		{
			int iNumberOfNonTerminals = 0;
			for(Symbol s : Ai.getRHS())
			{
				if(s instanceof NonTerminal)
				{
					iNumberOfNonTerminals++;
					LinkedList<Production> productionsWithSonRhs = rhs.get(s);
					productionsWithSonRhs.addFirst(Ai);
				}
			}
			count.put(Ai, iNumberOfNonTerminals);
		}
		
		//create Workset
		LinkedList<Production> W = new LinkedList<Production>();
		for(Entry<Production,Integer> r : count.entrySet())
		{
			if(r.getValue().equals(0))
			{
				if (DEBUG)
					System.out.println("working set entry: "+r.getKey());
				W.add(r.getKey());
			}
		}
		
		while(W.size() > 0)
		{
			Production Ai = W.removeFirst(); //extract one rule of a productive NonTerminal from the Workset 
			NonTerminal A = Ai.getLHS();
			if( !(result.contains(A)) )
			{
				result.add(A);
				if (DEBUG )
					System.out.println(A+" productive");
				for(Production r : rhs.get(A)) //get productions that contain the productive NT A in their rhs
				{
					Integer i = count.get(r);
					assert(i != null); //i != null  must be valid
					i--; //for every occurrence of A decrease the counter
					//NOTE: if production r contains A more than once, this loop body is executed also more than once 
					
					count.put(r, i);
					if (DEBUG)
						System.out.println(" -> "+r+" ("+i+")");
					if(i == 0) W.add(r);
				}
			}
		}
		
		//create a SymbolInfo for every NonTerminal
		LinkedList<NonTerminal> ntList = getNonTerminals();
		for(NonTerminal nt : ntList)
		{
			if(nt == null) continue;
			if(!this.symbolInfos.containsKey(nt))
			{
				SymbolInfo i = new SymbolInfo();
				i.symbol = nt;
				this.symbolInfos.put(nt, i);
			}
		}
		
		//copy result
		for(NonTerminal nt : result)
		{
			SymbolInfo i = this.symbolInfos.get(nt);
			i.bProductive = true;
		}
		
		this.bProductivityChecked = true;
	}
	
	/**
	 * returns a reduced grammar
	 * @return
	 */
	public IGrammar getReducedGrammar()
	{
		this.checkProductivity();
		this.checkReachability();
		LinkedList<NonTerminal> requiredNonTerminals = this.getProductiveAndReachableNonTerminals();
		LinkedList<Production> allProductions = this.getProductions();
		LinkedList<Production> reducedProductions = new LinkedList<Production>(allProductions);
		for(Production p : allProductions)
		{
			boolean bKeepProduction = true;
			NonTerminal lhs = p.getLHS();
			List<Symbol> rhs = p.getRHS();
			if(!requiredNonTerminals.contains(lhs)) 
				bKeepProduction = false;
			else
			{
				for(Symbol s : rhs)
				{
					if(s instanceof NonTerminal)
					{
						NonTerminal nt = (NonTerminal) s;
						if(!requiredNonTerminals.contains(nt))
						{
							bKeepProduction = false;
						}
					}
				} /*end for*/
			}
			if(!bKeepProduction)
			{
				reducedProductions.remove(p);
			}
		} /*end for*/
		
		//creating a new Grammar without not required productions
		IGrammar ret = new Grammar(this.getTerminals(), requiredNonTerminals, reducedProductions);
		return ret;
	}
	
	public IGrammar extendByAuxStartProduction()
	{
		if(grammar == null) return null;
		return this.grammar.extendByAuxStartProduction();
	}
	
	/**
	 * Gets the terminals.
	 */
	public LinkedList<Terminal> getTerminals()
	{
		if(grammar == null) return null;
		return this.grammar.getTerminals();
	}
	
	/**
	 * Gets the non-terminals.
	 */
	public LinkedList<NonTerminal>  getNonTerminals()
	{
		if(grammar == null) return null;
		return this.grammar.getNonTerminals();
	}
	
	/**
	 * Gets the number of productions.
	 */
	public int getProductionCount()
	{
		if(grammar == null) return 0;
		return this.grammar.getProductionCount();
	}
	
	
	/**
	 * Gets the start production.
	 */
	public Production getStartProduction()
	{
		if(grammar == null) return null;
		return this.grammar.getStartProduction();
	}
	
	/**
	 * Gets the production with the given index.
	 */
	public Production getProductionAt(int index)
	{
		if(grammar == null) return null;
		return this.grammar.getProductionAt(index);
	}
	
	/**
	 * Gets the productions. TIDY
	 */
	public LinkedList<Production> getProductions()
	{
		if(grammar == null) return null;
		return this.grammar.getProductions();
	}

}