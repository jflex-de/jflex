package jflex.dfa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import jflex.core.NFA;
import jflex.logging.Out;
import jflex.option.Options;
import jflex.state.StateSet;
import jflex.state.StateSetEnumerator;

public class DfaFactory {
  /**
   * Returns a DFA that accepts the same language as the NFA.
   *
   * <p>This DFA is usually not minimal.
   *
   * @return a DFA that accepts the same language as the NFA.
   */
  public static DFA createFromNfa(NFA nfa) {

    int numStates = nfa.numStates();
    Map<StateSet, Integer> dfaStates = new HashMap<>(numStates);
    List<StateSet> dfaList = new ArrayList<>(numStates);

    DFA dfa = new DFA(nfa.numEntryStates(), nfa.numInput(), nfa.numLexStates());

    int numDFAStates = 0;
    int currentDFAState = 0;

    Out.println("Converting NFA to DFA : ");
    nfa.epsilonFill();

    StateSet currentState;
    // will be reused
    StateSet newState;

    // create the initial states of the DFA
    for (int i = 0; i < nfa.numEntryStates(); i++) {
      newState = nfa.epsilon(i);

      dfaStates.put(newState, numDFAStates);
      dfaList.add(newState);

      dfa.setEntryState(i, numDFAStates);

      dfa.setFinal(numDFAStates, nfa.containsFinal(newState));
      dfa.setAction(numDFAStates, nfa.getAction(newState));

      numDFAStates++;
    }

    numDFAStates--;

    //    if (Build.DEBUG)
    //      Out.debug(
    //          "DFA start states are :"
    //              + Out.NL
    //              + dfaStates
    //              + Out.NL
    //              + Out.NL
    //              + "ordered :"
    //              + Out.NL
    //              + dfaList);

    StateSet tempStateSet = nfa.tempStateSet();
    StateSetEnumerator states = nfa.states();
    newState = new StateSet(numStates);
    while (currentDFAState <= numDFAStates) {

      currentState = dfaList.get(currentDFAState);

      for (int input = 0; input < nfa.numInput(); input++) {

        // newState = DFAEdge(currentState, input);

        // inlining DFAEdge for performance:

        // Out.debug("Calculating DFAEdge for state set "+currentState+" and input '"+input+"'");

        tempStateSet.clear();
        states.reset(currentState);
        while (states.hasMoreElements())
          tempStateSet.add(nfa.reachableStates(states.nextElement(), input));

        newState.copy(tempStateSet);

        states.reset(tempStateSet);
        while (states.hasMoreElements()) newState.add(nfa.epsilon(states.nextElement()));

        // Out.debug("DFAEdge is : "+newState);

        if (newState.containsElements()) {

          // Out.debug("DFAEdge for input "+(int)input+" and state set "+currentState+" is
          // "+newState);

          // Out.debug("Looking for state set "+newState);
          Integer nextDFAState = dfaStates.get(newState);

          if (nextDFAState != null) {
            dfa.addTransition(currentDFAState, input, nextDFAState);
          } else {
            if (Options.progress) Out.print(".");
            // Out.debug("Table was "+dfaStates);
            numDFAStates++;

            // make a new copy of newState to store in dfaStates
            StateSet storeState = new StateSet(newState);

            dfaStates.put(storeState, numDFAStates);
            dfaList.add(storeState);

            dfa.addTransition(currentDFAState, input, numDFAStates);
            dfa.setFinal(numDFAStates, nfa.containsFinal(storeState));
            dfa.setAction(numDFAStates, nfa.getAction(storeState));
          }
        }
      }

      currentDFAState++;
    }

    if (Options.verbose) Out.println("");
    return dfa;
  }

  private DfaFactory() {}
}
