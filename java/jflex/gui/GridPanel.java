/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * JFlex 1.8.2                                                             *
 * Copyright (C) 1998-2018  Gerwin Klein <lsf@jflex.de>                    *
 * All rights reserved.                                                    *
 *                                                                         *
 * License: BSD                                                            *
 *                                                                         *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

package jflex.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Panel;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

/**
 * Grid layout manager like GridLayout but with predefinable grid size.
 *
 * @author Gerwin Klein
 * @version JFlex 1.8.2
 */
public class GridPanel extends Panel implements Handles {

  private static final long serialVersionUID = -2846472856883709721L;

  private final int cols;
  private final int rows;

  private final int hgap;
  private final int vgap;

  private final List<GridPanelConstraint> constraints = new ArrayList<>();
  private Insets insets = new Insets(0, 0, 0, 0);

  /** {@inheritDoc} */
  public GridPanel(int cols, int rows) {
    this(cols, rows, 0, 0);
  }

  /**
   * Constructor for Grid Panel.
   *
   * @param cols number of columns.
   * @param rows number of rwos.
   * @param hgap a int.
   * @param vgap a int.
   */
  public GridPanel(int cols, int rows, int hgap, int vgap) {
    this.cols = cols;
    this.rows = rows;
    this.hgap = hgap;
    this.vgap = vgap;
  }

  /** Lays out the views. */
  @Override
  public void doLayout() {
    Dimension size = getSize();
    size.height -= insets.top + insets.bottom;
    size.width -= insets.left + insets.right;

    float cellWidth = size.width / cols;
    float cellHeight = size.height / rows;

    for (int i = 0; i < constraints.size(); i++) {
      GridPanelConstraint c = constraints.get(i);

      float x = cellWidth * c.x + insets.left + hgap / 2;
      float y = cellHeight * c.y + insets.right + vgap / 2;

      float width, height;

      if (c.handle == FILL) {
        width = (cellWidth - hgap) * c.width;
        height = (cellHeight - vgap) * c.height;
      } else {
        Dimension d = c.component.getPreferredSize();
        width = d.width;
        height = d.height;
      }

      switch (c.handle) {
        case TOP_CENTER:
          x += (cellWidth + width) / 2;
          break;
        case TOP_RIGHT:
          x += cellWidth - width;
          break;
        case CENTER_LEFT:
          y += (cellHeight + height) / 2;
          break;
        case CENTER:
          x += (cellWidth + width) / 2;
          y += (cellHeight + height) / 2;
          break;
        case CENTER_RIGHT:
          y += (cellHeight + height) / 2;
          x += cellWidth - width;
          break;
        case BOTTOM:
          y += cellHeight - height;
          break;
        case BOTTOM_CENTER:
          x += (cellWidth + width) / 2;
          y += cellHeight - height;
          break;
        case BOTTOM_RIGHT:
          y += cellHeight - height;
          x += cellWidth - width;
          break;
        default:
          // do nothing
      }

      c.component.setBounds(new Rectangle((int) x, (int) y, (int) width, (int) height));
    }
  }

  /**
   * getPreferredSize.
   *
   * @return a {@link java.awt.Dimension} object.
   */
  @Override
  public Dimension getPreferredSize() {
    float dy = 0;
    float dx = 0;

    for (int i = 0; i < constraints.size(); i++) {
      GridPanelConstraint c = constraints.get(i);

      Dimension d = c.component.getPreferredSize();

      dx = Math.max(dx, d.width / c.width);
      dy = Math.max(dy, d.height / c.height);
    }

    dx += hgap;
    dy += vgap;

    dx *= cols;
    dy *= rows;

    dx += insets.left + insets.right;
    dy += insets.top + insets.bottom;

    return new Dimension((int) dx, (int) dy);
  }

  /**
   * Sets the insets.
   *
   * @param insets a {@link java.awt.Insets} object.
   */
  public void setInsets(Insets insets) {
    this.insets = insets;
  }

  /** {@inheritDoc} */
  public void add(int x, int y, Component c) {
    add(x, y, 1, 1, FILL, c);
  }

  /** {@inheritDoc} */
  public void add(int x, int y, int handle, Component c) {
    add(x, y, 1, 1, handle, c);
  }

  /** {@inheritDoc} */
  public void add(int x, int y, int dx, int dy, Component c) {
    add(x, y, dx, dy, FILL, c);
  }

  /** Add a component to this panel. */
  public void add(int x, int y, int dx, int dy, int handle, Component c) {
    super.add(c);
    constraints.add(new GridPanelConstraint(x, y, dx, dy, handle, c));
  }
}
