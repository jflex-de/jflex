package edu.tum.cup2.generator;

import static edu.tum.cup2.generator.Edge.createAcceptEdge;
import static edu.tum.cup2.grammar.SpecialTerminals.EndOfInputStream;
import static edu.tum.cup2.grammar.SpecialTerminals.Placeholder;
import static edu.tum.cup2.util.CollectionTools.llist;
import static edu.tum.cup2.util.CollectionTools.map;
import static edu.tum.cup2.util.CollectionTools.set;
import static edu.tum.cup2.util.CollectionTools.stack;
import static edu.tum.cup2.util.Tuple2.t;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import edu.tum.cup2.generator.exceptions.GeneratorException;
import edu.tum.cup2.generator.items.CPGoToLink;
import edu.tum.cup2.generator.items.LALR1CPItem;
import edu.tum.cup2.generator.items.LR0Item;
import edu.tum.cup2.generator.items.LR1Item;
import edu.tum.cup2.generator.states.LALR1CPState;
import edu.tum.cup2.generator.states.LR1State;
import edu.tum.cup2.generator.terminals.EfficientTerminalSet;
import edu.tum.cup2.grammar.Symbol;
import edu.tum.cup2.util.FibonacciHeap;
import edu.tum.cup2.util.Tuple2;

/**
 * Factory for a LALR(1) automaton, that improves on 
 * {@link LALR1CPAutomatonFactory} by computing the context propagation
 * with a StronglyConnectedComponents approach instead of slow fixpoint 
 * iteration.
 * 
 * @author Andreas Wenger
 * @author Daniel Altmann
 * @author Michael Hausmann
 * @author Michael Petter
 */
public class LALR1SCCAutomatonFactory extends AutomatonFactory<LR1Item, LR1State> {

    /**
     * Create an LALR(1)-Automaton.
     */
    public Automaton<LR1Item, LR1State> createAutomaton(LRGenerator<LR1Item, LR1State> generator,
            GrammarInfo grammarInfo)
            throws GeneratorException {

        this.generator = generator;
        this.grammarInfo = grammarInfo;
        initCreation();

        long start = System.currentTimeMillis();
        //debug=true;
        //create the start state (start production with dot at position 0 and "Placeholder" as lookahead)
        LR0Item startStateKernelItem = this.queue.remove(0).getLR0Kernel().getFirstItem();
        Set<LR0Item> startStateItemKernel = set();
        startStateItemKernel.add(startStateKernelItem);
        Set<LALR1CPItem> startStateItem = set();
        startStateItem.add(new LALR1CPItem(startStateKernelItem, grammarInfo.getTerminalSet(Placeholder)));
        LALR1CPState startStateKernel = new LALR1CPState(
                startStateItem);

        //hashmap which matches a kernel of a state
        //to its complete (with closure) state
        Map<LALR1CPState, LALR1CPState> kernel2closure = map();

        //collected go-to context propagation links
        //(key: source item, value contains target state *closure*)
        Map<LALR1CPItem, CPGoToLink> goToLinks = map();

        //which items belong to which state (closure)
        Map<LALR1CPItem, LALR1CPState> itemStates = map();

        //set of edges
        Set<Edge> lr0Edges = set();

        //TODO: own function
        {

            //initialize queue, which consists of states
            Set<LALR1CPState> queue = set();
            queue.add(startStateKernel);
            kernel2closure.put(startStateKernel, startStateKernel.closure(grammarInfo));

            //for all states, find their edges to other (possibly new) states
            while (!queue.isEmpty()) //Appel says: "until E and T did not change in this iteration".
            //but: do we really need E here? I ignored it
            {
                //handle next state in queue
                LALR1CPState stateKernel = queue.iterator().next();
                queue.remove(stateKernel);

                //debug messages
                printDebugMessages();

                //first, create a LALR(1)-with-CP-Links automaton and remember the closures
                //for performance reasons
                LALR1CPState state = kernel2closure.get(stateKernel);
                Set<Symbol> shiftedSymbols = new HashSet<Symbol>();
                for (LALR1CPItem item : state.getItems()) {
                    //remember the state this item belongs to
                    itemStates.put(item, state);
                    //try to shift
                    if (item.isShiftable()) {
                        Symbol symbol = item.getNextSymbol();
                        if (symbol == EndOfInputStream) {
                            //$-symbol: here we accept
                            lr0Edges.add(createAcceptEdge(stateKernel, symbol)); //GOON: with or without closure?
                        } else if (shiftedSymbols.add(symbol)) //shift each symbol only once
                        {
                            //terminal or non-terminal

                            //shift to other state
                            Tuple2<LALR1CPState, List<CPGoToLink>> s = state.goToCP(symbol);
                            LALR1CPState shiftedStateKernel = s.get1();
                            List<CPGoToLink> shiftedStateCPLinks = s.get2();

                            //we try to find out if there is already some state which has an equal
                            //kernel to the shifted state (LALR1CPState equals on kernel)
                            LALR1CPState equalStateLALR1CP = kernel2closure.get(shiftedStateKernel);
                            LALR1CPState gotoLinkTargetState = equalStateLALR1CP;
                            if (equalStateLALR1CP == null) {
                                //add new state
                                LALR1CPState shiftedState = shiftedStateKernel.closure(grammarInfo);
                                kernel2closure.put(shiftedStateKernel, shiftedState);
                                queue.add(shiftedStateKernel);
                                gotoLinkTargetState = shiftedState;

                            }

                            //remember CP links (cp link contains closure of target state, not only kernel)
                            for (CPGoToLink link : shiftedStateCPLinks) {
                                LALR1CPItem todoItem = link.getSource();
                                if (goToLinks.containsKey(todoItem)) {
                                    throw new RuntimeException("Double gotoLink!");
                                }
                                goToLinks.put(todoItem, link.withTargetState(gotoLinkTargetState));
                            }

                            //add edge
                            lr0Edges.add(new Edge(stateKernel, symbol, shiftedStateKernel, item.getLR0Item()));
                        }
                    }
                }
            }
        }

        if (debug) {
            long actual = System.currentTimeMillis();
            start = actual - start;
            System.out.println("LR(0) Generierung: " + (start / 1000.0));
            start = actual;
        }

        Map<LALR1CPItem, EfficientTerminalSet> lookaheads = map();

        // Iterative depth-first search through the CP-graph for SCCs
        {
            // stack to keep track of path through the item-cp-graph
            Stack<LALR1CPItem> dfsStack = stack();
            Stack<LALR1CPItem> sccStack = stack();
            Set<LALR1CPItem> sccSet = set();
            Set<Tuple2<LALR1CPItem, LALR1CPItem>> edges = set();
            // DFS search and index
            int dfsIndex = 0;
            Map<LALR1CPItem, Integer> dfsIndices = map();
            Map<LALR1CPItem, Integer> lowlink = map();
            Map<Integer, LALR1CPItem> sccRoots = map();
            // set to keep track whether all nodes were touched
            Set<LALR1CPItem> dfsSet = set();
            Set<LALR1CPItem> visited = set();
            for (LALR1CPState sta : kernel2closure.values()) {
                Collection<LALR1CPItem> col = sta.getItemsAsCollection();
                for (LALR1CPItem it : col) {
                    EfficientTerminalSet la = it.getLookaheads();
                    lookaheads.put(it, la);
                    dfsSet.add(it);
                }

            }

            // initialise dfsStack with startstate's item
            LALR1CPState startstate = kernel2closure.get(startStateKernel);
            LALR1CPItem firstItem = startstate.getItemWithLookaheadByLR0Item(startStateKernelItem);
            dfsStack.push(firstItem);
            sccStack.push(firstItem);
            sccSet.add(firstItem);
            dfsIndices.put(firstItem, 0);
            lowlink.put(firstItem, 0);
            LALR1CPItem found;

            // iterate through the graph until we touched all states 
            // also compute SCC-internal LA-propagation for SCC
            do {
                dfsSet.remove(dfsStack.peek());

                while (!(dfsStack.isEmpty())) {
                    found = null;
                    LALR1CPItem item = dfsStack.peek();
                    // visit all links outgoing from item
                    if (visited.add(item)) {
                        CPGoToLink gotoLink = goToLinks.get(item);
                        int ll = lowlink.get(item);
                        if (gotoLink != null) {
                            LALR1CPState targetState = gotoLink.getTargetState();
                            LALR1CPItem targetItem = targetState.getItemWithLookaheadByLR0Item(gotoLink.getTargetItem());
                            if (dfsSet.contains(targetItem)) {
                                found = targetItem;
                                dfsStack.push(found);
                                sccStack.push(found);
                                sccSet.add(found);
                                dfsSet.remove(found);
                                dfsIndex++;
                                dfsIndices.put(found, dfsIndex);
                                lowlink.put(found, dfsIndex);

                            } else {
                                if (sccSet.contains(targetItem)) {
                                    lowlink.put(item, Math.min(ll, dfsIndices.get(targetItem)));
                                }
                            }
                            edges.add(t(item, targetItem));
                        }
                        for (LR0Item closureLink : item.getClosureLinks()) {
                            LALR1CPState ownState = itemStates.get(item); //same state as current item
                            LALR1CPItem targetItem = ownState.getItemWithLookaheadByLR0Item(closureLink);
                            if (dfsSet.contains(targetItem)) {
                                found = targetItem;
                                dfsStack.push(found);
                                sccStack.push(found);
                                sccSet.add(found);
                                dfsSet.remove(found);
                                dfsIndex++;
                                dfsIndices.put(found, dfsIndex);
                                lowlink.put(found, dfsIndex);
                            } else {
                                if (sccSet.contains(targetItem)) {
                                    lowlink.put(item, Math.min(ll, dfsIndices.get(targetItem)));
                                }
                            }
                            edges.add(t(item, targetItem));
                        }
                    }
                    if (found == null) {                      // not found a child  
                        dfsStack.pop();           // ascend
                        if (!dfsStack.isEmpty()) {
                            lowlink.put(dfsStack.peek(), Math.min(lowlink.get(dfsStack.peek()), lowlink.get(item)));
                        }

                        if (lowlink.get(item).equals(dfsIndices.get(item))) {
                            sccRoots.put(dfsIndices.get(item), item);
                            Collection<LALR1CPItem> l = llist();
                            EfficientTerminalSet ts = lookaheads.get(item);
                            l.add(item);
                            while ((found = sccStack.pop()) != item) {
                                lowlink.put(found, dfsIndices.get(item));
                                ts = ts.plusAll(lookaheads.get(found));
                                l.add(found);
                                sccSet.remove(found);
                            }
                            lookaheads.put(item, ts);
                            sccSet.remove(item);
                        }
                    }


                }



                LALR1CPItem item = null;
                LALR1CPItem backup = null;
                for (LALR1CPItem it : dfsSet) {
                    backup = it;
                    if (it.getPosition() == 0) {
                        item = it;
                        break;
                    }
                }
                if (item == null && backup == null) {
                    break;
                } else {
                    item = backup;
                }

                dfsStack.push(item);
                sccStack.push(item);
                sccSet.add(item);
                dfsIndex++;
                dfsIndices.put(item, dfsIndex);
                lowlink.put(item, dfsIndex);
            } while (!dfsStack.isEmpty());

            if (debug) {
                long actual = System.currentTimeMillis();
                start = actual - start;
                System.out.println("Determining SCCs: " + (start / 1000.0));
                start = actual;
            }


            // found SCCs (lowlink(item) == SCC-id)


            // now record SCC-graph-edges-relation (SCC-id -> 2^SCC-id) map)
            // as well as SCC-incoming-count incoming(SCC-id) == count

            final Map<LALR1CPItem, Integer> incoming = map();
            Map<LALR1CPItem, Set<LALR1CPItem>> edgerelation = map();
            for (LALR1CPItem it : sccRoots.values()) {
                incoming.put(it, 0);
                edgerelation.put(it, new HashSet<LALR1CPItem>());
            }

            for (Tuple2<LALR1CPItem, LALR1CPItem> e : edges) {
                int source = lowlink.get(e.get1());
                int target = lowlink.get(e.get2());
                if (source == target) {
                    continue;
                }
                LALR1CPItem t = sccRoots.get(target);
                if (edgerelation.get(sccRoots.get(source)).add(t)) {
                    incoming.put(t, incoming.get(t) + 1);
                }
            }

            if (debug) {
                long actual = System.currentTimeMillis();
                start = actual - start;
                System.out.println("Inter SCC-edges: " + (start / 1000.0));
                start = actual;
            }

            // compute Lookaheadpropagation in lookahead(SCC-id)
            FibonacciHeap<LALR1CPItem> pq = new FibonacciHeap<LALR1CPItem>();
            for (LALR1CPItem it : sccRoots.values()) {
                pq.insert(it, incoming.get(it));
            }
            while (!pq.isEmpty()) {
                LALR1CPItem it = pq.extractMin();
                EfficientTerminalSet itla = lookaheads.get(it);
                for (LALR1CPItem t : edgerelation.get(it)) {
                    EfficientTerminalSet edgela = lookaheads.get(t);
                    pq.decrementKey(t);
                    lookaheads.put(t, edgela.plusAll(itla));
                }
            }
            if (debug) {
                long actual = System.currentTimeMillis();
                start = actual - start;
                System.out.println("Propagating LAs: " + (start / 1000.0));
                start = actual;
            }

            // refresh lookaheads of SCC-members
            for (Map.Entry<LALR1CPItem, Integer> me : lowlink.entrySet()) {
                lookaheads.put(me.getKey(), lookaheads.get(sccRoots.get(me.getValue())));
            }
        }







        //create states and edges from collected information
        Map<LALR1CPState, LR1State> lalr1CPToLR1Map = map();
        for (LALR1CPState state : kernel2closure.keySet()) {
            HashSet<LR1Item> lr1Items = new HashSet<LR1Item>();
            LALR1CPState stateWithClosure = kernel2closure.get(state);

            for (LR0Item strippedItem : state.getStrippedItems()) {
                LALR1CPItem item = stateWithClosure.getItemWithLookaheadByLR0Item(strippedItem);
                EfficientTerminalSet terminals = lookaheads.get(item);
                lr1Items.add(new LR1Item(strippedItem, terminals));
            }
            LR1State lr1State = new LR1State(lr1Items);
            lalr1CPToLR1Map.put(state, lr1State);
            dfaStates.add(lr1State);
        }

        //fill dfaEdges
        for (Edge edge : lr0Edges) {
            this.dfaEdges.add(new Edge(lalr1CPToLR1Map.get(edge.getSrc()),
                    edge.getSymbol(), lalr1CPToLR1Map.get(edge.getDest()), edge.getSrcItem()));
        }

        printDebugResult();

        return ret;
    }
}
