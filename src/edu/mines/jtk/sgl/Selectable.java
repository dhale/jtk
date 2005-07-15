/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

/**
 * An interface implemented by nodes that can be selected. This interface
 * serves as a <em>marker</em> interface, because its methods are implemented 
 * by the abstract base class {@link Node} for all nodes, whether selectable 
 * or not. The behavior of those implementations depends on whether or not
 * the class that extends {@link Node} also implements this marker interface.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.07.10
 */
public interface Selectable {

  /**
   * Determines whether this node is currently selected.
   * @return true, if selected; false, otherwise.
   */
  public boolean isSelected();

  /**
   * Sets the selected state for this node.
   * @param selected true, for selected; false, otherwise.
   */
  public void setSelected(boolean selected);
}
