/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import java.awt.*;

import static edu.mines.jtk.ogl.Gl.*;

/**
 * OpenGL blend state.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.05.31
 */
public class BlendState implements State {

  /**
   * Constructs blend state.
   */
  public BlendState() {
  }

  /**
   * Determines whether blend color is set.
   * @return true, if set; false, otherwise.
   */
  public boolean hasColor() {
    return _colorSet;
  }

  /**
   * Gets the blend color.
   * @return the blend color.
   */
  public Color getColor() {
    return _color;
  }

  /**
   * Sets the blend color.
   * @param color the blend color.
   */
  public void setColor(Color color) {
    _color = color;
    _colorSet = true;
  }

  /**
   * Unsets the blend color.
   */
  public void unsetColor() {
    _color = _colorDefault;
    _colorSet = false;
  }

  /**
   * Determines whether blend equation is set.
   * @return true, if set; false, otherwise.
   */
  public boolean hasEquation() {
    return _equationSet;
  }

  /**
   * Gets the blend equation.
   * @return the blend equation.
   */
  public int getEquation() {
    return _equation;
  }

  /**
   * Sets the blend equation.
   * @param mode the blend equation.
   */
  public void setEquation(int mode) {
    _equation = mode;
    _equationSet = true;
  }

  /**
   * Unsets the blend equation.
   */
  public void unsetEquation() {
    _equation = _equationDefault;
    _equationSet = false;
  }

  /**
   * Determines whether blend function is set.
   * @return true, if set; false, otherwise.
   */
  public boolean hasBlendFunction() {
    return _functionSet;
  }

  /**
   * Gets the blend function sfactor.
   * @return the blend function sfactor.
   */
  public int getSfactor() {
    return _sfactor;
  }

  /**
   * Gets the blend function dfactor.
   * @return the blend function dfactor.
   */
  public int getDfactor() {
    return _dfactor;
  }

  /**
   * Sets the blend function.
   * @param sfactor the source factor.
   * @param dfactor the destination factor.
   */
  public void setFunction(int sfactor, int dfactor) {
    _sfactor = sfactor;
    _dfactor = dfactor;
    _functionSet = true;
  }

  /**
   * Unsets the blend function.
   */
  public void unsetFunction() {
    _sfactor = _sfactorDefault;
    _dfactor = _dfactorDefault;
    _functionSet = false;
  }

  public void apply() {
    glEnable(GL_BLEND);
    if (_colorSet) {
      float r = _color.getRed()/255.0f;
      float g = _color.getGreen()/255.0f;
      float b = _color.getBlue()/255.0f;
      float a = _color.getAlpha()/255.0f;
      glBlendColor(r,g,b,a);
    }
    if (_equationSet)
      glBlendEquation(_equation);
    if (_functionSet)
      glBlendFunc(_sfactor,_dfactor);
  }

  public int getAttributeBits() {
    return GL_ENABLE_BIT | GL_COLOR_BUFFER_BIT;
  }

  private static Color _colorDefault = new Color(0.0f,0.0f,0.0f,0.0f);
  private Color _color = _colorDefault;
  private boolean _colorSet;

  private static int _equationDefault = GL_FUNC_ADD;
  private int _equation = _equationDefault;
  private boolean _equationSet;

  private static int _sfactorDefault = GL_ONE;
  private static int _dfactorDefault = GL_ZERO;
  private int _sfactor = _sfactorDefault;
  private int _dfactor = _dfactorDefault;
  private boolean _functionSet;
}
