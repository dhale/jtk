/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import static edu.mines.jtk.ogl.Gl.*;

/**
 * OpenGL point state.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.05.31
 */
public class PointState implements State {

  /**
   * Constructs point state.
   */
  public PointState() {
  }

  /**
   * Determines whether point smooth is set.
   * @return true, if set; false, otherwise.
   */
  public boolean hasSmooth() {
    return _smoothSet;
  }

  /**
   * Gets the point smooth.
   * @return the point smooth.
   */
  public boolean getSmooth() {
    return _smooth;
  }

  /**
   * Sets the point smooth.
   * @param smooth the smooth.
   */
  public void setSmooth(boolean smooth) {
    _smooth = smooth;
    _smoothSet = true;
  }

  /**
   * Unsets the point smooth.  
   */
  public void unsetSmooth() {
    _smooth = _smoothDefault;
    _smoothSet = false;
  }

  /**
   * Determines whether point size is set.
   * @return true, if set; false, otherwise.
   */
  public boolean hasSize() {
    return _sizeSet;
  }

  /**
   * Gets the point size.
   * @return the point size.
   */
  public float getSize() {
    return _size;
  }

  /**
   * Sets the point size.
   * @param size the size.
   */
  public void setSize(float size) {
    _size = size;
    _sizeSet = true;
  }

  /**
   * Unsets the point size.  
   */
  public void unsetSize() {
    _size = _sizeDefault;
    _sizeSet = false;
  }

  public void apply() {
    if (_smoothSet) {
      if (_smooth) {
        glEnable(GL_POINT_SMOOTH);
      } else {
        glDisable(GL_POINT_SMOOTH);
      }
    }
    if (_sizeSet)
      glPointSize(_size);
  }

  public int getAttributeBits() {
    return GL_ENABLE_BIT | GL_POINT_BIT;
  }

  private static boolean _smoothDefault = false;
  private boolean _smooth = _smoothDefault;
  private boolean _smoothSet;

  private static float _sizeDefault = 1.0f;
  private float _size = _sizeDefault;
  private boolean _sizeSet;
}
