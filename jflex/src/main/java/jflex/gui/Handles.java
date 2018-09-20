/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.7.0                                                             *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex.gui;

/**
 * Constants used for GridLayout
 *
 * @author Gerwin Klein
 * @version JFlex 1.7.0
 */
public interface Handles {

  /** Constant <code>FILL=0</code> */
  int FILL = 0;

  /** Constant <code>TOP=1</code> */
  int TOP = 1;
  /** Constant <code>TOP_LEFT=TOP</code> */
  int TOP_LEFT = TOP;
  /** Constant <code>TOP_CENTER=2</code> */
  int TOP_CENTER = 2;
  /** Constant <code>TOP_RIGHT=3</code> */
  int TOP_RIGHT = 3;

  /** Constant <code>CENTER_LEFT=4</code> */
  int CENTER_LEFT = 4;
  /** Constant <code>CENTER=5</code> */
  int CENTER = 5;
  /** Constant <code>CENTER_CENTER=CENTER</code> */
  int CENTER_CENTER = CENTER;
  /** Constant <code>CENTER_RIGHT=6</code> */
  int CENTER_RIGHT = 6;

  /** Constant <code>BOTTOM=7</code> */
  int BOTTOM = 7;
  /** Constant <code>BOTTOM_LEFT=BOTTOM</code> */
  int BOTTOM_LEFT = BOTTOM;
  /** Constant <code>BOTTOM_CENTER=8</code> */
  int BOTTOM_CENTER = 8;
  /** Constant <code>BOTTOM_RIGHT=9</code> */
  int BOTTOM_RIGHT = 9;
}
