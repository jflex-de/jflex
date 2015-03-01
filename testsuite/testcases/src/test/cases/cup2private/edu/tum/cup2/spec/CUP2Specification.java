package edu.tum.cup2.spec;


import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import edu.tum.cup2.grammar.AuxiliaryLHS4SemanticShiftAction;
import edu.tum.cup2.grammar.Grammar;
import edu.tum.cup2.grammar.NonTerminal;
import edu.tum.cup2.grammar.Production;
import edu.tum.cup2.grammar.SpecialTerminals;
import edu.tum.cup2.grammar.Symbol;
import edu.tum.cup2.grammar.Terminal;
import edu.tum.cup2.precedences.Associativity;
import edu.tum.cup2.precedences.LeftAssociativity;
import edu.tum.cup2.precedences.Precedences;
import edu.tum.cup2.precedences.RightAssociativity;
import edu.tum.cup2.precedences.NonAssocAssociativity;
import edu.tum.cup2.semantics.Action;
import edu.tum.cup2.semantics.ParserInterface;
import edu.tum.cup2.semantics.SymbolValue;
import edu.tum.cup2.semantics.SymbolValueClasses;
import edu.tum.cup2.spec.exceptions.IllegalSpecException;
import edu.tum.cup2.spec.util.RHSItem;
import edu.tum.cup2.spec.util.RHSSymbols;
import edu.tum.cup2.util.ArrayTools;
import edu.tum.cup2.util.It;
import edu.tum.cup2.util.Reflection;


/**
 * Base class for all CUP specifications.
 *
 * @author Andreas Wenger
 * @author Stefan Dangl
 */

public abstract class CUP2Specification
{

	public boolean isInit = false;
	private LinkedList<NonTerminal> auxNonTerminals = new LinkedList<NonTerminal>();
	public SymbolValueClasses symbolValueClasses;

	private Precedences precedences =
	  new Precedences(
	      Arrays.asList(
	          (Associativity)left(SpecialTerminals.Error/*,SpecialTerminals.BestError*/)
	      )
	  );
	private Grammar grammar = null;
	private ParserInterface grammarInterface = new ParserInterface();

	private int productionCount = 0;
	
  // semantic shift-actions
  private ArrayList<Production> auxiliaryProductions = new ArrayList<Production>();


	/**
	 * Initializes this class. The grammar method would be too late, because
	 * we need some information earlier.
	 */
	protected void init()
	{
		if (!isInit)
		{
			//collect symbol-value-bindings
			Terminal[] terminals = Reflection.getTerminals();
			NonTerminal[] nonTerminals = Reflection.getNonTerminals();
			symbolValueClasses = Reflection.getSymbolValueClasses(terminals, nonTerminals);
			//inited
			isInit = true;
		}
	}


	/**
	 * Creates the {@link Precedences} from the given list of
	 * {@link Associativity} instances. The first associativity has
	 * lowest priority, the last one highest priority.
	 */
	protected void precedences(Associativity... list)
	{
	  // transform array to list.
	  List<Associativity> l = Arrays.asList(list);
	  // make list editable
	  l = new ArrayList<Associativity>(l);
	  // add precedence for Error
	  l.add(left(SpecialTerminals.Error/*,SpecialTerminals.BestError*/));
	  precedences = new Precedences(l);
	}


	/**
	 * Creates a left associativity for the given list of {@link Terminal}s.
	 */
	protected LeftAssociativity left(Terminal... terminals)
	{
		return new LeftAssociativity(Arrays.asList(terminals));
	}


	/**
	 * Creates a right associativity for the given list of {@link Terminal}s.
	 */
	protected RightAssociativity right(Terminal... terminals)
	{
		return new RightAssociativity(Arrays.asList(terminals));
	}
	
	/**
	 * Creates a nonassociative associativity for the given list of {@link Terminal}s.
	 */
	protected NonAssocAssociativity nonassoc(Terminal... terminals)
	{
		return new NonAssocAssociativity(Arrays.asList(terminals));
	}


	/**
	 * Creates a new {@link Grammar} with the given list of {@link Production}s lists.
	 */
	protected void grammar(Production[]... productions) {
		//get terminals and non-terminals from the enums "Terminals" and "NonTerminals" of the subclass
		//collect productions
		LinkedList<Production> productionsList = new LinkedList<Production>();
		for (Production[] prods : new It<Production[]>(productions)) {
			for (Production prod : new It<Production>(prods)) {
				productionsList.add(prod);
			}
		}
		productionsList.addAll(auxiliaryProductions);
		LinkedList<Terminal> allTerminals ;
		/*/{
    		// create simple list.
			allTerminals = new ArrayTools<Terminal>().toLinkedList(Reflection.getTerminals());
    	}/*/{
			// SD : sort list by insertables
			TreeSet<Terminal> insertables = new TreeSet<Terminal>( new Comparator<Terminal>() {
				public int compare(Terminal o1, Terminal o2) {
					Insertable ins1 = null; Insertable ins2 = null;
					try { ins1 = (Insertable) Reflection.getFieldValueFromObject(o1, "insert"); } catch (Exception e) {}
					try { ins2 = (Insertable) Reflection.getFieldValueFromObject(o2, "insert"); } catch (Exception e) {}
					return (ins1 == null)?((ins2==null)?-1:1):((ins2==null)?-1:(
						(ins2.precedence == ins1.precedence)?-1:(
							(ins2.precedence < ins1.precedence) ? 1 : -1
						)
					));
				}
			});
			insertables.addAll(Arrays.asList(getTerminals()));
    		allTerminals = new LinkedList<Terminal>(insertables);
    	}/**/
		// SD : Add special terminal used as error-hendl.
    	//GOON: commented out at the moment by Andi (2010-06-17), because not needed now!
		allTerminals.add(SpecialTerminals.Error); //- GOON

		LinkedList<NonTerminal> allNonTerminals = new ArrayTools<NonTerminal>()
			.toLinkedList(getNonTerminals());

		// SD : add the auxiliary non-terminals to the non-terminals list
		allNonTerminals.addAll(auxNonTerminals);

		//create grammar
		this.grammar = new Grammar(allTerminals, allNonTerminals, productionsList);
	}
	
	
	public Terminal[] getTerminals() {
		return Reflection.getTerminals();
	}
	
	public NonTerminal[] getNonTerminals() {
		return Reflection.getNonTerminals();
	}


	/**
	 * Creates a list of {@link Production}s each with the given left hand side {@link NonTerminal} and
	 * the given list of {@link RHSSymbols}s (there must be at least one), and optionally inbetween lists
	 * of {@link Action}s which belong to the preceding right hand side.
	 * @throws IllegalSpecException 
	 */
	public Production[] prod(NonTerminal lhs, RHSItem... rhsItems)
		throws IllegalSpecException {
		init();
		if (rhsItems.length == 0 || !(rhsItems[0] instanceof RHSSymbols)) {
			throw new IllegalSpecException("There must be at least one right hand side, "
				+ "and there may be no action defined before the first right hand side!");
		}

		/**
		 * run through list of items...
		 * when hitting an action, create empty auxiliary production using the action as reduce-action
		 * and replace action with corresponding new auxiliary non-terminal.
		 **/
		List<Symbol> emptyRHS = new ArrayList<Symbol>(1);
		emptyRHS.add(SpecialTerminals.Epsilon);

		LinkedList<List<Symbol>> prodsRHS = new LinkedList<List<Symbol>>();
		LinkedList<Action> prodsReduceAction = new LinkedList<Action>();
		LinkedList<Terminal> prodsPrecedence = new LinkedList<Terminal>();
		LinkedList<AuxiliaryLHS4SemanticShiftAction> auxNTs = new LinkedList<AuxiliaryLHS4SemanticShiftAction>();
		for (RHSItem rhsItem : rhsItems) {
			if (rhsItem instanceof RHSSymbols) {
				// list of terminals,non-terminals and actionmarkers ($a)
				List<Symbol> rhsWithActionMarkers = ((RHSSymbols) rhsItem).getSymbols();

				//action queue from the last RHS must be empty
				if (auxNTs.size() > 0) {
					throw new IllegalSpecException(
						"Action missing for production with RHS " + prodsRHS.getLast());
				}
				auxNTs = new LinkedList<AuxiliaryLHS4SemanticShiftAction>();

				ArrayList<Symbol> expurgatedRHS = new ArrayList<Symbol>(
					rhsWithActionMarkers.size());
				int symbolsNotBeingEpsilonCount = 0;
				int symbolsCount = 0;
				for (int i = 0; i < rhsWithActionMarkers.size(); i++) {
					Symbol e = rhsWithActionMarkers.get(i);
					if (e == SpecialTerminals.$a) {
						// semantic shift-action

						if (symbolsNotBeingEpsilonCount == 0)
							throw new IllegalSpecException(
								"Actions before the first symbol differing from epsilon are not allowed!");

						AuxiliaryLHS4SemanticShiftAction auxNT = Reflection
							.createAuxLHS4SemanticShiftAction("$a:AUX_"
								+ auxNonTerminals.size());
						auxNT.numPrecedingSymbols = symbolsCount;
						auxNT.numPrecedingSymbolsNotEpsilon = symbolsNotBeingEpsilonCount;
						StringBuilder orig = new StringBuilder();
						orig.append(lhs).append(" →");
						for (int j = 0; j < rhsWithActionMarkers.size(); j++) {
							if (j == i) {
								orig.append(" >");
								orig.append(rhsWithActionMarkers.get(j));
								orig.append("<");
							} else {
								orig.append(" ");
								orig.append(rhsWithActionMarkers.get(j));
							}
						}
						auxNT.originatedFrom = orig.toString();

						// add to list of open auxiliary NTs
						auxNTs.add(auxNT);
						// add to current RHS (replacement of $a)
						expurgatedRHS.add(auxNT);
						// add to list of all auxiliary NTs (list already defined)
						auxNonTerminals.add(auxNT);
					} else {
						// normal symbol
						expurgatedRHS.add(e);
					}
					if (e != SpecialTerminals.Epsilon)
						symbolsNotBeingEpsilonCount++;
					symbolsCount++;
				}

				expurgatedRHS.trimToSize();
				prodsRHS.add(expurgatedRHS);
				prodsReduceAction.add(null);
				prodsPrecedence.add(((RHSSymbols) rhsItem).getPrecedence());
			} else
				if (rhsItem instanceof Action) {
					// action
					Action action = (Action) rhsItem;
					if (auxNTs.size() == 0) {
						// reduce action
						//reduce action already set?
						if (prodsReduceAction.getLast() != null)
							throw new IllegalSpecException(
								"Only one reduce action may be defined for each production!");

						prodsReduceAction.set(prodsReduceAction.size() - 1, action);

						checkAction(action, prodsRHS.getLast().size(),
							prodsRHS.getLast(), symbolValueClasses);
					} else {
						// create auxiliary production using this shift-action as reduce-action
						// and an open auxiliary LHS.
						AuxiliaryLHS4SemanticShiftAction auxNT = auxNTs.remove();
						Production auxProd = new Production(++productionCount, auxNT,
							emptyRHS, action,null);
						auxNT.symbolValueType = Reflection.getReturnTypeOfAction(action);
						auxiliaryProductions.add(auxProd);
						
						checkAction(action, auxNT.numPrecedingSymbols,
							prodsRHS.getLast(), symbolValueClasses);
						//System.out.println(auxProd.getLHS()+" -> "+auxProd.getRHS());
					}

				} else
					// something else
					throw new IllegalSpecException(
						"Right hand side contains an object which is neither RHSSymbols nor Action : "
							+ rhsItem.getClass().getName());
		}
		/*
		for (List<Symbol> production:prodsRHS)
		{
		  System.out.println(lhs+" -> "+production);
		}*/

		if (auxNTs.size() > 0)
			throw new IllegalSpecException("Action missing for production with RHS "
				+ auxNTs.getLast().originatedFrom);

		//collect all non-auxiliary productions and return them
		Production[] ret = new Production[prodsRHS.size()];
		for (int i = 0; i < prodsRHS.size(); i++) {
			/* // Do not allow missing actions  
			 * Action reduceAction = prodsReduceAction.get(i);
			 * if (reduceAction==null)
			 * throw new IllegalSpecException("No reduce-action specified for the production with the RHS " + prodsRHS.getLast());
			**/
			ret[i] = new Production(productionCount + i + 1, lhs, prodsRHS.get(i),
				prodsReduceAction.get(i),prodsPrecedence.get(i)); //id 0 is for aux start production
		}
		productionCount += prodsRHS.size();
		return ret;
	}
	
	
	/**
	 * Define within this method a way to check if the action is valid. By default
	 * the method calls {@link Reflection#checkAction(Action, int, List, SymbolValueClasses)}.
	 * 
	 * @param action
	 * @param position
	 * @param rhsSymbols
	 * @param symbolValueClasses
	 */
	protected void checkAction(Action action, int position,
			List<Symbol> rhsSymbols, SymbolValueClasses symbolValueClasses) {
		Reflection.checkAction(action, position, rhsSymbols, symbolValueClasses);
	}
	


	/**
	 * Creates a new list of right hand side symbols.
	 */
	protected RHSSymbols rhs(Symbol... symbols)
	{
		return new RHSSymbols(symbols);
	}

	/**
	 * Wraps around an rhs providing means to specify %prec like precedences 
	 */

	protected RHSSymbols prec(RHSSymbols syms,Terminal prec){
		return syms.setPrec(prec);
	}
	
	public Grammar getGrammar()
	{
		return grammar;
	}


	public Precedences getPrecedences()
	{
		return precedences;
	}

	/**
	 * Register an interface for requests from the parser
	 * in case an error occurs.
	 *  
	 * @see ParserInterface
	 **/
	protected void register(ParserInterface parserInterface) {
		if (parserInterface == null)
			grammarInterface = new ParserInterface();
		else
			grammarInterface = parserInterface;
	}

	public ParserInterface getParserInterface() {
		return grammarInterface;
	}

  	public void insert(Terminal t) {
  		insert(t, 0, null, SymbolValue.NoValue);
	}
	public void insert(Terminal t, Object[] proposals) {
		insert(t, 0, proposals, SymbolValue.NoValue);
	}
	public void insert(Terminal t, Object value) {
		insert(t, 0, null, value);
	}
	public void insert(Terminal t, Object[] proposals, Object value) {
		insert(t, 0, proposals, value);
	}
  	public void insert( Terminal t, int times) {
		insert(t, times, null, SymbolValue.NoValue);
	}
	public void insert(Terminal t, int times, Object[] proposals) {
		insert(t, times, proposals, SymbolValue.NoValue);
	}
	public void insert(Terminal t, int times, Object value) {
		insert(t, times, null, value);
	}

	public void insert(Terminal t, int times, Object[] possible4user, Object value) {
		Class<?> c = t.getClass();
		if (c == null)
			return;
		Field fld = null;
		try {
			fld = c.getField("insert");
		} catch (SecurityException e1) {
			e1.printStackTrace();
			return;
		} catch (NoSuchFieldException e) {
			throw new IllegalSpecException("Insertable terminal " + t
				+ " has no field named \"insert\"");
		}
		fld.setAccessible(true);
		if (fld == null)
			throw new IllegalSpecException("Insertable terminal " + t
				+ " has no field named \"insert\"");
		try {
			fld.set(t, new Insertable(times, t, possible4user, value));
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			IllegalSpecException s = new IllegalSpecException("Insertable terminal " + t
				+ " not accessible : " + e.toString());
			s.initCause(e);
			throw s;
		} finally {
			fld.setAccessible(true);
		}

	}
  
}
