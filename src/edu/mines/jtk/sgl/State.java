/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

/**
 * OpenGL state.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.05.31
 */
public interface State {

  /**
   * Applies this OpenGL state.
   * This method calls OpenGL functions that reflect the state.
   */
  public void apply();

  /**
   * Gets the OpenGL attribute bits for this state. 
   * These bits indicate what OpenGL state is changed by the method 
   * {@link #apply()}. If these bits are passed to glPushAttrib before 
   * calling {@link #apply()}, then glPopAttrib will restore any OpenGL 
   * state that is changed by that method.
   */
  public int getAttributeBits();
}
