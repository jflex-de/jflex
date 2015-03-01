package edu.tum.cup2.generator;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import edu.tum.cup2.generator.exceptions.GeneratorException;
import edu.tum.cup2.generator.exceptions.ReduceReduceConflict;
import edu.tum.cup2.generator.exceptions.ShiftReduceConflict;
import edu.tum.cup2.generator.items.Item;
import edu.tum.cup2.generator.states.LRState;
import edu.tum.cup2.generator.states.State;
import edu.tum.cup2.grammar.NonTerminal;
import edu.tum.cup2.grammar.Production;
import edu.tum.cup2.grammar.SpecialTerminals;
import edu.tum.cup2.grammar.Symbol;
import edu.tum.cup2.grammar.Terminal;
import edu.tum.cup2.parser.actions.Accept;
import edu.tum.cup2.parser.actions.ConsecutiveNonAssocAction;
import edu.tum.cup2.parser.actions.LRAction;
import edu.tum.cup2.parser.actions.Reduce;
import edu.tum.cup2.parser.actions.Shift;
import edu.tum.cup2.parser.states.LRParserState;
import edu.tum.cup2.parser.tables.LRActionTable;
import edu.tum.cup2.parser.tables.LRGoToTable;
import edu.tum.cup2.parser.tables.LRParsingTable;
import edu.tum.cup2.precedences.Associativity;
import edu.tum.cup2.precedences.LeftAssociativity;
import edu.tum.cup2.precedences.NonAssocAssociativity;
import edu.tum.cup2.precedences.RightAssociativity;
import edu.tum.cup2.spec.CUP2Specification;
import edu.tum.cup2.util.It;


/**
 * Base class for the LR(0) and LR(1) parser generator.
 * Contains the common code of both generators.
 * 
 * @author Andreas Wenger
 * @author Stefan Dangl
 */
public abstract class LRGenerator<I extends Item, S extends LRState<I>>
	extends Generator
{
	
	protected final GrammarInfo grammarInfo;
	private final LRParsingTable parsingTable;
	private Automaton<I,S> automaton;
	protected static int numThreads = 4;
	
	/**
	 * Computes a {@link LRParsingTable} for the given LR grammar.
	 * The given verbosity tells the generator how many debug messages are requested.
	 * 
	 * For a description of this algorithm, see Appel's book, pages 62 to 66.
	 */
	//public LRGenerator(Grammar grammar, Precedences precedences, Verbosity verbosity, boolean extendGrammar)
	public LRGenerator(CUP2Specification spec, final Verbosity verbosity, boolean extendGrammar)
		throws GeneratorException
	{
		//extend grammar by "S' → S$" production
		super(extendGrammar ? spec.getGrammar().extendByAuxStartProduction() : spec.getGrammar(), spec.getPrecedences(), verbosity);
		
		//compute additional information about the grammar
		grammarInfo = new GrammarInfo(grammar);
		
		//construct the DFA
		Automaton<I, S> dfa = createAutomaton();
		automaton = dfa; //cache it
		S state0 = dfa.getStartState();
		
		//construct the parsing tables
		
		//create empty tables
		LRParsingTable table = new LRParsingTable(grammar, spec.getParserInterface(), dfa.getStates().size());
		LRActionTable actionTable = table.getActionTable();
		LRGoToTable goToTable = table.getGotoTable();
		
		//create a mapping from our complicated LR parser generator states
		//to the simple states of the LR parser. the order of the states does not matter,
		//but we have to pay attention that the start state has index 0.
		HashMap<S, LRParserState> statesMap = new HashMap<S, LRParserState>();
		It<LRParserState> tableStates = table.getStates();
		statesMap.put(state0, tableStates.next()); //start state
		for (S dfaState : dfa.getStates())
		{
			if (!dfaState.equals(state0))
				statesMap.put(dfaState, tableStates.next()); //other state
		}
		
		//create shift, goto and accept actions
		for (Edge edge : dfa.getEdges())
		{
			LRParserState src = statesMap.get(edge.getSrc());
			if (edge.getSymbol() instanceof Terminal)
			{
				Terminal t = (Terminal) edge.getSymbol();
				if (edge.isDestAccepting())
				{
					//accept
					actionTable.set(new Accept(), src, t);
				}
				else
				{
					//shift
					actionTable.set(new Shift(statesMap.get(edge.getDest()),
						edge.getSrcItem().getProduction(), edge.getSrcItem().getPosition() + 1), src, t);
				}
			}
			else if (edge.getSymbol() instanceof NonTerminal)
			{
				//goto
				goToTable.set(statesMap.get(edge.getDest()), src, (NonTerminal) edge.getSymbol());
			}
		}
		
		//create the reduce actions
		for (LRState<I> stateKernel : statesMap.keySet()) //for each generator state
		{
			LRState<I> state = stateKernel.closure(grammarInfo); //unpack state (from kernel to closure)
			//if any item is not shiftable any more, reduce it to the left hand side of its production
			for (I item : state.getItems())
			{
				if (!item.isShiftable())
				{
					try {
						createReduceActions(actionTable, item, statesMap.get(stateKernel));
					} catch (GeneratorException c) {
						//String word = getMinWordForState(stateKernel,dfa,state0, new HashSet<State<?>>());
						String word = getShortestWord4State(stateKernel,state0);
						// word may be null if state can not be reached from state0
						// (should never occur if automaton is calculated correctly)
						if (word!=null){
							if (c instanceof ShiftReduceConflict)
								((ShiftReduceConflict)c).setWord(word.toString());
							if (c instanceof ReduceReduceConflict)
								((ReduceReduceConflict)c).setWord(word.toString());
						}
						throw c; // TODO : Tell java-developers to provide rethrow!!
					}
				}
			}
		}
		
		//finished :-)
		this.parsingTable = table;
		
		//TODO
		if (verbosity != Verbosity.None)
		{
			debugOut.println("Result: Unique parse states: " + table.getStatesCount());
		}
		
	}
	/** new algorithm
	 * 		- traversing automaton backwards
	 * 		- uses broad search of length N to find a word
	 * 		- working set consists of words of length == M
	 * 		- processed set contains words of length <= M
	 **/
	
	private final boolean LOG_shortestWordFinder = false;
	
	private String getShortestWord4State( State<?> crashState, S state0 )
	{
		if (crashState==state0)
			return "";
		HashMap<State<?>, LinkedList<String>> workingSet = new HashMap<State<?>,LinkedList<String>>();
		LinkedList<State<?>> workingSetSortedKeyList = new LinkedList<State<?>>();
		HashSet<State<?>> processedStates = new HashSet<State<?>>();
		workingSet.put(crashState, new LinkedList<String>());
		workingSetSortedKeyList.add(crashState);
		while (true)
		{
			State<?> currentState = workingSetSortedKeyList.removeFirst();
			processedStates.add(currentState);
			LinkedList<String> currentWord = workingSet.remove(currentState);
			List<Edge> currentEdges = automaton.getEdgeTo(currentState);
			if (LOG_shortestWordFinder){
				System.out.println("\t\t---");
				System.out.println("current word      : "+currentWord);
				System.out.println("current state     : "+currentState/*.closure(grammarInfo)*/);
				System.out.println("working-set size  : "+workingSetSortedKeyList.size());
				System.out.println("number of edges   : "+currentEdges.size());
			}
			nextEdge: for (Edge currentEdge : currentEdges )
			{
				State<?> nextState = currentEdge.getSrc();
				// we already processed this state with a word of length <=M
				// or will process it with a word of length of M?
				// currently we are creating a word of length M+1 for this state
				// so our word is not so good in this case :(
				if (workingSet.containsKey(nextState) || processedStates.contains(nextState))
				{
					if (LOG_shortestWordFinder){
						System.out.println(nextState.hashCode()+" already visited or in list of states to be visited ..");
					}
					continue nextEdge; // TODO : what about epsilon
				}
				
				// yay, we found the shortest word for this state 
				LinkedList<String> nextWord = new LinkedList<String>(currentWord);
				List<String> symbolStrings = null;
				Symbol symbol = currentEdge.getSymbol();
				if (symbol instanceof Terminal)
					symbolStrings = Arrays.asList(symbol.toString());
				else if (symbol instanceof NonTerminal) {
					symbolStrings = getShortestWordForNT((NonTerminal)symbol);
					if (symbolStrings==null)
						symbolStrings = Arrays.asList(symbol.toString());
				} else
					symbolStrings = Arrays.asList("[unknown symbol-type]");
				nextWord.addAll(0,symbolStrings);
				if (nextState==state0) {
					String word = "";
					boolean first = true;
					for (String s : nextWord){
						if (first) first = false;
						else word+=" ";
						word += s;
					}
					return word;
				}
				if (LOG_shortestWordFinder){
					System.out.println("Adding to working-set : "+nextState.hashCode()+" for word : "+nextWord);
				}
				workingSet.put(nextState, nextWord);
				workingSetSortedKeyList.addLast(nextState);
			}
		}
	}


	private List<String> getShortestWordForNT( NonTerminal nt ) {
		return getShortestWordForNT(nt,new HashMap<NonTerminal,List<String>>());
	}
	private List<String> getShortestWordForNT( NonTerminal nt, HashMap<NonTerminal,List<String>> visited ) {
		LinkedList<String> minNTWord = null;
		if (visited.containsKey(nt)) // TODO : should never happen
		{
			System.err.println("getMinWordForNT algorithm does things that should not happen!");
			List<String> word = visited.get(nt);
			if (word==null)
				throw new RuntimeException("getMinWordForNT algorithm is not behaving to it's implementor's satisfaction :?");
			else
				return word;
		}
		visited.put(nt,null);
		FOR_PRODUCTION : for (Production p : grammar.getProductions()) {
			if (p.getLHS()==nt) {
				LinkedList<String> NTWord = new LinkedList<String>();
				for (Symbol s : p.getRHS()) {
					if (s instanceof Terminal) {
						if (s!=SpecialTerminals.Epsilon)
							NTWord.add(s.toString());
					} else if (s instanceof NonTerminal) {
						NonTerminal nt2 = (NonTerminal)s;
						// if the non-terminal is being visited in recursive call,
						// do not use this production
						// otherwise if the non-terminal has already been visited, take the
						// minimal word which has been calculated before.
						if (visited.containsKey(nt2)) {
							List<String> got = visited.get(nt2);
							if (got==null)
								continue FOR_PRODUCTION;
							else
								NTWord.addAll(got); // TODO: is this word truly minimal??? -> counter-example of grammar!
						} else {
							// nt2 is not currently processed and no minimal word is known for it
							// thus we calculate the minimal word.
							List<String> minWord4S = getShortestWordForNT(nt2, visited);
							if (minWord4S==null) {
								// we were unable to calculate a word for nt2
								// because either nt2 is not productive
								// or it uses a non-terminal which is already
								// being processed
								continue FOR_PRODUCTION;
							}
							NTWord.addAll(minWord4S);
						}
					}	
				}
				if (minNTWord==null || minNTWord.size()>NTWord.size())
					minNTWord = NTWord;
			}
		}
		if (minNTWord==null)
			visited.remove(nt);
		else
			visited.put(nt, minNTWord);
		return minNTWord;
	}


	/**
	 * Creates the DFA of the parser, consisting of states and edges
	 * (for shift and goto) connecting them.
	 */
	public abstract Automaton<I, S> createAutomaton() throws GeneratorException;
	
	
	
	/**
	 * Creates and returns the start state.
	 * This is the item, which consists of the start production at position 0.
	 */
	protected abstract S createStartState();


	/**
	 * Fills the given {@link LRActionTable} with reduce actions, using the
	 * given non-shiftable item and its parent parser state.
	 */
	protected abstract void createReduceActions(LRActionTable actionTable, I item, LRParserState state)
		throws GeneratorException;
	
	
	/**
	 * Sets the given {@link Reduce} action to the given {@link LRActionTable} at the given
	 * position. If that cell is already filled and the conflict can not be solved by
	 * the precedences, an appropriate exception is thrown.
	 */
	protected void setReduceAction(LRActionTable actionTable, Reduce reduce,
		LRParserState atState, Terminal atTerminal)
		throws GeneratorException
	{
		//check, if the cells is already filled with a shift or reduction. if so,
		//we have a conflict (which can be resolved when there are appropriate precedences)
		LRAction action = actionTable.getWithNull(atState, atTerminal);
		if (action != null)
		{
			if (action instanceof Shift)
			{
				//try to find out if production or terminal has higher precedence
				int prec = 0;
				if (precedences != null)
				{
					prec = precedences.compare(reduce.getProduction(), atTerminal);
					if (prec == 1)
					{
						//production has higher precedence, hence reduce
						actionTable.set(reduce, atState, atTerminal);
					}
					else if (prec == -1)
					{
						//terminal has higher precedence, hence shift
						//do not change current cell
					}
					else
					{
						//equal precedence. if associativity is left, then reduce, if it is right,
						//then shift, else it is a conflict
						Associativity ass = precedences.getAssociativity(atTerminal);
						if (ass instanceof LeftAssociativity)
							actionTable.set(reduce, atState, atTerminal); //reduce
						else if (ass instanceof RightAssociativity)
							/* do not change the current cell */; //shift
						else if (ass instanceof NonAssocAssociativity) {
							/*two consecutive occurrences of equal precedence non-associative 
							  terminals generates an error.*/
							Terminal crashTerminal = atTerminal;
							if (atTerminal==SpecialTerminals.WholeRow)
								crashTerminal = actionTable.getTerminalOfFirstShiftAction(atState);
							// throw new ConsecutiveNonAssocException((Shift) action, reduce, crashTerminal);
                                                        // to be consistent with CUP, an in general to be more logical, we defer the error reporting to the runtime
                                                        actionTable.set(new ConsecutiveNonAssocAction(), atState, atTerminal);
						} else {
							Terminal crashTerminal = atTerminal;
							if (atTerminal==SpecialTerminals.WholeRow)
								crashTerminal = actionTable.getTerminalOfFirstShiftAction(atState);
							throw new ShiftReduceConflict((Shift) action, reduce, crashTerminal);
						}
					}
				}
				else
				{
					Terminal crashTerminal = atTerminal;
					if (atTerminal==SpecialTerminals.WholeRow)
						crashTerminal = actionTable.getTerminalOfFirstShiftAction(atState);
					throw new ShiftReduceConflict((Shift) action, reduce, crashTerminal);
				}
			}
			else if (action instanceof Reduce)
			{
				//try to find out if production or terminal has higher precedence
				Reduce reduce2=(Reduce)action;
				int prec = 0;
				if (precedences != null)
				{
					prec = precedences.compare(reduce.getProduction(), reduce2.getProduction().getPrecedenceTerminal());
					if (prec == 1)
					{
						//reduction has higher precedence, hence reduce
						actionTable.set(reduce, atState, atTerminal);
					}
					else if (prec == -1)
					{
						//reduction2 has higher precedence, hence shift
						actionTable.set(reduce2,atState,atTerminal);
					}else
						throw new ReduceReduceConflict(reduce,(Reduce)action);
				}else
					throw new ReduceReduceConflict(reduce,(Reduce)action);
				
			}
			else
				throw new GeneratorException("Unknown conflict");
		}
		else
		{
			//empty cell, no conflict
			actionTable.set(reduce, atState, atTerminal);
		}
	}

	
	/**
	 * Gets the resulting parsing table.
	 */
	public LRParsingTable getParsingTable()
	{
		return parsingTable;
	}
	
	/**
	 * Gets the automaton that was used to create the parsing table.
	 */
	public Automaton<I,S> getAutomaton()
	{
		return automaton;
	}
	
	/**
	 * Sets the number of threads (only for parallel programs)
	 * @param n the new number of threads
	 */
	public static void setNumThreads(int n) {
		numThreads = n;
	}
	
}
