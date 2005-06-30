/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

/**
 * An interface implemented by nodes that can be selected via picking.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.06.23
 */
public interface Selectable {

  /**
   * Begins selection.
   * @param pr the pick result that initiated.
   */
  public void beginSelect(PickResult pr);

  /*
  public void endSelect();

  public boolean isSelected();

  public boolean isSelectable();

  public void setSelectable(boolean);
  */
}
