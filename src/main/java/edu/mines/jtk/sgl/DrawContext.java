/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

/**
 * A transform context for drawing.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.05.21
 */
public class DrawContext extends TransformContext {

  /**
   * Constructs a draw context for the specified view canvas.
   * @param canvas the view canvas.
   */
  public DrawContext(ViewCanvas canvas) {
    super(canvas);
  }
}
