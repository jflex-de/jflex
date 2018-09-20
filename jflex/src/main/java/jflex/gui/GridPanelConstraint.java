/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.7.0                                                             *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */
package jflex.gui;

import java.awt.Component;

/**
 * Constraints for layout elements of GridLayout
 *
 * @author Gerwin Klein
 * @version JFlex 1.7.0
 */
public class GridPanelConstraint {

  int x, y, width, height, handle;
  Component component;

  /**
   * Constructor for GridPanelConstraint.
   *
   * @param x horizontal position.
   * @param y vertical position.
   * @param width width in pixels.
   * @param height height in pixels.
   * @param handle a int.
   * @param component a {@link java.awt.Component} object.
   */
  public GridPanelConstraint(int x, int y, int width, int height, int handle, Component component) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.handle = handle;
    this.component = component;
  }
}
