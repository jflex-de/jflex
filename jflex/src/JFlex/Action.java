/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.3.5                                                             *
 * Copyright (C) 1998-2001  Gerwin Klein <lsf@jflex.de>                    *
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


/**
 * Encapsulates an action in the specification.
 *
 * It stores the Java code as String together with a priority (line number in the specification).
 *
 * @author Gerwin Klein
 * @version JFlex 1.3.5, $Revision$, $Date$
 */
final public class Action {


  /**
   * The Java code this Action represents
   */
  String content;

  /**
   * The priority (i.e. line number in the specification) of this Action. 
   */
  int priority;

  /**
   * True iff the action belongs to an lookahead expresstion 
   * (<code>a/b</code> or <code>r$</code>)
   */
  boolean isLookAction;


  /**
   * Creates a new Action object with specified content and line number.
   */
  public Action(String content, int priority) {
    this.content = content;
    this.priority = priority;
  }  

  public Action(String content, int priority, boolean isLookAction) {
    this(content, priority);
    this.isLookAction = isLookAction;
  }


  /**
   * Compares the priority value of this Action with the specified action.
   *
   * @param other  the other Action to compare this Action with.
   *
   * @return this Action if it has higher priority - the specified one, if not.
   */
  public Action getHigherPriority(Action other) {
    if (other == null) return this;

    // the smaller the number the higher the priority
    if (other.priority > this.priority) 
      return this;
    else
      return other;
  }


  /**
   * Returns the String representation of this object.
   */
  public String toString() {
    return "Action (priority "+priority+", lookahead "+isLookAction+") :"+Out.NL+content;
  }


  /**
   * Returns <code>true</code> iff the parameter is an
   * Action with the same content as this one.
   *
   * @param obj   the object to compare this Action with
   */
  public boolean isEquiv(Action a) {
    return this == a || this.content.trim().equals(a.content.trim());
  }


  /**
   * Returns a hash value for this Action
   */
  public int hashCode() {
    if (content.length() < 1) return 0;
    return content.charAt(0)+content.charAt(content.length()/2);
  }
}
