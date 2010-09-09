/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

/**
 * An interface implemented by nodes that can be dragged with a mouse.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.06.30
 */
public interface Dragable {

  /**
   * Begins dragging.
   * @param dc the drag context.
   */
  public void dragBegin(DragContext dc);

  /**
   * During dragging, this method is called when the mouse moves.
   * @param dc the drag context.
   */
  public void drag(DragContext dc);

  /**
   * Ends dragging.
   * @param dc the drag context.
   */
  public void dragEnd(DragContext dc);
}
