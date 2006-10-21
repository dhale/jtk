/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import static edu.mines.jtk.ogl.Gl.*;

/**
 * OpenGL line state.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.05.31
 */
public class LineState implements State {

  /**
   * Constructs line state.
   */
  public LineState() {
  }

  /**
   * Determines whether line smooth is set.
   * @return true, if set; false, otherwise.
   */
  public boolean hasSmooth() {
    return _smoothSet;
  }

  /**
   * Gets the line smooth.
   * @return the line smooth.
   */
  public boolean getSmooth() {
    return _smooth;
  }

  /**
   * Sets the line smooth.
   * @param smooth the smooth.
   */
  public void setSmooth(boolean smooth) {
    _smooth = smooth;
    _smoothSet = true;
  }

  /**
   * Unsets the line smooth.  
   */
  public void unsetSmooth() {
    _smooth = _smoothDefault;
    _smoothSet = false;
  }

  /**
   * Determines whether line stipple is set.
   * @return true, if set; false, otherwise.
   */
  public boolean hasStipple() {
    return _stippleSet;
  }

  /**
   * Gets the line stipple factor.
   * @return the line stipple factor.
   */
  public int getStippleFactor() {
    return _factor;
  }

  /**
   * Gets the line stipple pattern.
   * @return the line stipple pattern.
   */
  public short getStipplePattern() {
    return _pattern;
  }

  /**
   * Sets the line stipple.
   * @param factor the stipple factor.
   * @param pattern the stipple pattern.
   */
  public void setStipple(int factor, short pattern) {
    _factor = factor;
    _pattern = pattern;
    _stippleSet = true;
  }

  /**
   * Unsets the line stipple.  
   */
  public void unsetStipple() {
    _factor = _factorDefault;
    _pattern = _patternDefault;
    _stippleSet = false;
  }

  /**
   * Determines whether line width is set.
   * @return true, if set; false, otherwise.
   */
  public boolean hasWidth() {
    return _widthSet;
  }

  /**
   * Gets the line width.
   * @return the line width.
   */
  public float getWidth() {
    return _width;
  }

  /**
   * Sets the line width.
   * @param width the width.
   */
  public void setWidth(float width) {
    _width = width;
    _widthSet = true;
  }

  /**
   * Unsets the line width.  
   */
  public void unsetWidth() {
    _width = _widthDefault;
    _widthSet = false;
  }

  public void apply() {
    if (_smoothSet) {
      if (_smooth) {
        glEnable(GL_LINE_SMOOTH);
      } else {
        glDisable(GL_LINE_SMOOTH);
      }
    }
    if (_stippleSet) {
      glEnable(GL_LINE_STIPPLE);
      glLineStipple(_factor,_pattern);
    }
    if (_widthSet)
      glLineWidth(_width);
  }

  public int getAttributeBits() {
    return GL_ENABLE_BIT | GL_LINE_BIT;
  }

  private static boolean _smoothDefault = false;
  private boolean _smooth = _smoothDefault;
  private boolean _smoothSet;

  private static int _factorDefault = 1;
  private int _factor = _factorDefault;
  private static short _patternDefault = (short)0xFFFF;
  private short _pattern = _patternDefault;
  private boolean _stippleSet;

  private static float _widthDefault = 1.0f;
  private float _width = _widthDefault;
  private boolean _widthSet;
}
