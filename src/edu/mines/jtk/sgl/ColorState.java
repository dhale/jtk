/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import java.awt.*;

import static edu.mines.jtk.opengl.Gl.*;

/**
 * OpenGL color state.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.05.31
 */
public class ColorState implements State {

  /**
   * Constructs color state.
   */
  public ColorState() {
  }

  /**
   * Sets the current color.
   * @param color the current color.
   */
  public void setColor(Color color) {
    _color = color;
    _colorSet = color!=null;
  }

  /**
   * Sets the shade model.
   * @param mode the shade model (GL_FLAT or GL_SMOOTH).
   */
  public void setShadeModel(int mode) {
    _shadeModel = mode;
    _shadeModelSet = true;
  }

  public void apply() {
    if (_colorSet) {
      byte r = (byte)_color.getRed();
      byte g = (byte)_color.getGreen();
      byte b = (byte)_color.getBlue();
      byte a = (byte)_color.getAlpha();
      glColor4ub(r,g,b,a);
    }
    if (_shadeModelSet) {
      glShadeModel(_shadeModel);
    }
  }

  public int getAttributeBits() {
    int bits = 0;
    if (_colorSet) 
      bits |= GL_CURRENT_BIT;
    if (_shadeModelSet) 
      bits |= GL_LIGHTING_BIT;
    return bits;
  }

  private Color _color;
  private boolean _colorSet;
  private int _shadeModel;
  private boolean _shadeModelSet;
}
