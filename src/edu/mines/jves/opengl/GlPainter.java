/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jves.opengl;

/**
 * An OpenGL painter. Paints via the OpenGL context locked for the current 
 * thread.
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.11.24
 */
public interface GlPainter {

  /**
   * Paints via the OpenGL context locked for the calling thread.
   */
  public void glPaint();
}
