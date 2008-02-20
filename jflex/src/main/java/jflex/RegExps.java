/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.4.1                                                             *
 * Copyright (C) 1998-2004  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * This program is free software; you can redistribute it and/or modify    *
 * it under the terms of the GNU General Public License. See the file      *
 * COPYRIGHT for more information.                                         *
 *                                                                         *
 * This program is distributed in the hope that it will be useful,         *
 * but WITHOUT ANY WARRANTY; without even the implied warranty of          *
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the           *
 * GNU General Public License for more details.                            *
 *                                                                         *
 * You should have received a copy of the GNU General Public License along *
 * with this program; if not, write to the Free Software Foundation, Inc., *
 * 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA                 *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package jflex;

import java.util.*;


/**
 * Stores all rules of the specification for later access in RegExp -> NFA
 *
 * @author Gerwin Klein
 * @version JFlex 1.4.1, $Revision$, $Date$
 */
public class RegExps {
  
  /** the spec line in which a regexp is used */
  List<Integer> lines;

  /** the lexical states in wich the regexp is used */
  List<List<Integer>> states;

  /** the regexp */
  List<RegExp> regExps;

  /** the action of a regexp */
  List<Action> actions;
  
  /** flag if it is a BOL regexp */
  List<Boolean> BOL;

  /** the lookahead expression */
  List<RegExp> look;

  public RegExps() {
    states = new ArrayList<List<Integer>>();
    regExps = new ArrayList<RegExp>();
    actions = new ArrayList<Action>();
    BOL = new ArrayList<Boolean>();
    look = new ArrayList<RegExp>();
    lines = new ArrayList<Integer>();
  }

  public int insert(int line, List<Integer> stateList, RegExp regExp, Action action, 
                     Boolean isBOL, RegExp lookAhead) {      
    if (Options.DEBUG) {
      Out.debug("Inserting regular expression with statelist :"+Out.NL+stateList);  //$NON-NLS-1$
      Out.debug("and action code :"+Out.NL+action.content+Out.NL);     //$NON-NLS-1$
      Out.debug("expression :"+Out.NL+regExp);  //$NON-NLS-1$
    }

    states.add(stateList);
    regExps.add(regExp);
    actions.add(action);
    BOL.add(isBOL);
    look.add(lookAhead);
    lines.add(line);
    
    return states.size()-1;
  }

  public int insert(List<Integer> stateList, Action action) {

    if (Options.DEBUG) {
      Out.debug("Inserting eofrule with statelist :"+Out.NL+stateList);   //$NON-NLS-1$
      Out.debug("and action code :"+Out.NL+action.content+Out.NL);      //$NON-NLS-1$
    }

    states.add(stateList);
    regExps.add(null);
    actions.add(action);
    BOL.add(null);
    look.add(null);
    lines.add(null);
    
    return states.size()-1;
  }

  public void addStates(int regNum, List<Integer> newStates) {
    states.get(regNum).addAll(newStates);      
  }

  public int getNum() {
    return states.size();
  }

  public boolean isBOL(int num) {
    return BOL.get(num);
  }
  
  public RegExp getLookAhead(int num) {
    return look.get(num);
  }

  public boolean isEOF(int num) {
    return BOL.get(num) == null;
  }

  public List<Integer> getStates(int num) {
    return states.get(num);
  }

  public RegExp getRegExp(int num) {
    return regExps.get(num);
  }

  public int getLine(int num) {
    return lines.get(num);
  }

  public void checkActions() {
    if ( actions.get(actions.size()-1) == null ) {
      Out.error(ErrorMessages.NO_LAST_ACTION);
      throw new GeneratorException();
    }
  }

  public Action getAction(int num) {
    while ( num < actions.size() && actions.get(num) == null )
      num++;

    return actions.get(num);
  }

  public int NFASize(Macros macros) {
    int size = 0;
    for (RegExp r : regExps)
      if (r != null) size += r.size(macros);
    
    for (RegExp r : look)
      if (r != null) size += r.size(macros);

    return size;
  }
}
