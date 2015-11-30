/****************************************************************************
Copyright 2004, Colorado School of Mines and others.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
****************************************************************************/
package edu.mines.jtk.sgl;

import java.awt.*;

import static edu.mines.jtk.ogl.Gl.*;

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
   * Determines whether current color is set.
   * @return true, if set; false, otherwise.
   */
  public boolean hasColor() {
    return _colorSet;
  }

  /**
   * Gets the current color.
   * @return the current color.
   */
  public Color getColor() {
    return _color;
  }

  /**
   * Sets the current color.
   * @param color the current color.
   */
  public void setColor(Color color) {
    _color = color;
    _colorSet = true;
  }

  /**
   * Unsets the current color.  
   */
  public void unsetColor() {
    _color = _colorDefault;
    _colorSet = false;
  }

  /**
   * Determines whether shade model is set.
   * @return true, if set; false, otherwise.
   */
  public boolean hasShadeModel() {
    return _shadeModelSet;
  }

  /**
   * Gets the shade model.
   * @return the shade model.
   */
  public int getShadeModel() {
    return _shadeModel;
  }

  /**
   * Sets the shade model.
   * @param shadeModel the shade model.
   */
  public void setShadeModel(int shadeModel) {
    _shadeModel = shadeModel;
    _shadeModelSet = true;
  }

  /**
   * Unsets the shade model.
   */
  public void unsetShadeModel() {
    _shadeModel = _shadeModelDefault;
    _shadeModelSet = false;
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

  private static Color _colorDefault = new Color(1.0f,1.0f,1.0f,1.0f);
  private Color _color = _colorDefault;
  private boolean _colorSet;

  private static int _shadeModelDefault = GL_SMOOTH;
  private int _shadeModel = _shadeModelDefault;
  private boolean _shadeModelSet;
}
