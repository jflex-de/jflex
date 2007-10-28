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
package JFlex;

import java.util.*;


/**
 * Stores all rules of the specification for later access in RegExp -> NFA
 *
 * @author Gerwin Klein
 * @version JFlex 1.4.1, $Revision$, $Date$
 */
public class RegExps {
  
  /** the spec line in which a regexp is used */
  Vector<Integer> lines;

  /** the lexical states in wich the regexp is used */
  Vector<Vector<Integer>> states;

  /** the regexp */
  Vector<RegExp> regExps;

  /** the action of a regexp */
  Vector<Action> actions;
  
  /** flag if it is a BOL regexp */
  Vector<Boolean> BOL;

  /** the lookahead expression */
  Vector<RegExp> look;

  public RegExps() {
    states = new Vector<Vector<Integer>>();
    regExps = new Vector<RegExp>();
    actions = new Vector<Action>();
    BOL = new Vector<Boolean>();
    look = new Vector<RegExp>();
    lines = new Vector<Integer>();
  }

  public int insert(int line, Vector<Integer> stateList, RegExp regExp, Action action, 
                     Boolean isBOL, RegExp lookAhead) {      
    if (Options.DEBUG) {
      Out.debug("Inserting regular expression with statelist :"+Out.NL+stateList);  //$NON-NLS-1$
      Out.debug("and action code :"+Out.NL+action.content+Out.NL);     //$NON-NLS-1$
      Out.debug("expression :"+Out.NL+regExp);  //$NON-NLS-1$
    }

    states.addElement(stateList);
    regExps.addElement(regExp);
    actions.addElement(action);
    BOL.addElement(isBOL);
    look.addElement(lookAhead);
    lines.addElement(new Integer(line));
    
    return states.size()-1;
  }

  public int insert(Vector<Integer> stateList, Action action) {

    if (Options.DEBUG) {
      Out.debug("Inserting eofrule with statelist :"+Out.NL+stateList);   //$NON-NLS-1$
      Out.debug("and action code :"+Out.NL+action.content+Out.NL);      //$NON-NLS-1$
    }

    states.addElement(stateList);
    regExps.addElement(null);
    actions.addElement(action);
    BOL.addElement(null);
    look.addElement(null);
    lines.addElement(null);
    
    return states.size()-1;
  }

  public void addStates(int regNum, Vector<Integer> newStates) {
    Enumeration<Integer> s = newStates.elements();
    
    while (s.hasMoreElements()) 
      (getStates(regNum)).addElement(s.nextElement());      
  }

  public int getNum() {
    return states.size();
  }

  public boolean isBOL(int num) {
    return ((Boolean) BOL.elementAt(num)).booleanValue();
  }
  
  public RegExp getLookAhead(int num) {
    return (RegExp) look.elementAt(num);
  }

  public boolean isEOF(int num) {
    return BOL.elementAt(num) == null;
  }

  public Vector<Integer> getStates(int num) {
    return (Vector<Integer>) states.elementAt(num);
  }

  public RegExp getRegExp(int num) {
    return (RegExp) regExps.elementAt(num);
  }

  public int getLine(int num) {
    return ((Integer) lines.elementAt(num)).intValue();
  }

  public void checkActions() {
    if ( actions.elementAt(actions.size()-1) == null ) {
      Out.error(ErrorMessages.NO_LAST_ACTION);
      throw new GeneratorException();
    }
  }

  public Action getAction(int num) {
    while ( num < actions.size() && actions.elementAt(num) == null )
      num++;

    return (Action) actions.elementAt(num);
  }

  public int NFASize(Macros macros) {
    int size = 0;
    Enumeration<RegExp> e = regExps.elements();
    while (e.hasMoreElements()) {
      RegExp r = (RegExp) e.nextElement();
      if (r != null) size += r.size(macros);
    }
    e = look.elements();
    while (e.hasMoreElements()) {
      RegExp r = (RegExp) e.nextElement();
      if (r != null) size += r.size(macros);
    }
    return size;
  }
}
