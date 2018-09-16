/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.6.1                                                             *
 * Copyright (C) 1998-2015  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex;


/**
 * Regular expression with two children (e.g. a | b)
 *
 * @author Gerwin Klein
 * @version JFlex 1.6.1
 */
public class RegExp2 extends RegExp {

  RegExp r1, r2;

  public RegExp2(int type, RegExp r1, RegExp r2) {
    super(type);
    this.r1 = r1;
    this.r2 = r2;
  }
  
  public String print(String tab) {
    return tab+"type = "+type+Out.NL+tab+"child 1 :"+Out.NL+ //$NON-NLS-1$ //$NON-NLS-2$
           r1.print(tab+"  ")+Out.NL+tab+"child 2 :"+Out.NL+ //$NON-NLS-1$ //$NON-NLS-2$
           r2.print(tab+"  "); //$NON-NLS-1$
  }
  
  public String toString() {
    return print(""); //$NON-NLS-1$
  }
}
  
