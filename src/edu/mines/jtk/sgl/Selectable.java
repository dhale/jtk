/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

/**
 * An interface implemented by nodes that can be selected.
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
   * Sets the selected state for this node. If the selection is exclusive 
   * and this node is in a world, then selection of this node will cause 
   * any other selected nodes in this node's world to be deselected.
   * @param selected true, if selected; false, otherwise.
   * @param exclusive true, for exclusive selection; false, otherwise.
   */
  public void setSelected(boolean selected, boolean exclusive);
}
