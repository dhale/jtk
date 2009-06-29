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
 * OpenGL light model state.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.05.31
 */
public class LightModelState implements State {

  /**
   * Constructs light model state.
   */
  public LightModelState() {
  }

  /**
   * Determines whether the ambient color attribute is set.
   * @return true, if set; false, otherwise.
   */
  public boolean hasAmbient() {
    return _ambientSet;
  }

  /**
   * Gets the ambient color attribute.
   * @return the ambient color.
   */
  public Color getAmbient() {
    return toColor(_ambient);
  }

  /**
   * Sets the ambient color attribute.
   * @param color the ambient color.
   */
  public void setAmbient(Color color) {
    _ambient = toArray(color);
    _ambientSet = true;
  }

  /**
   * Unsets the ambient color attribute.
   */
  public void unsetAmbient() {
    _ambient = _ambientDefault;
    _ambientSet = false;
  }

  /**
   * Determines whether the color control attribute is set.
   * @return true, if set; false, otherwise.
   */
  public boolean hasColorControl() {
    return _colorControlSet;
  }

  /**
   * Gets the color control attribute.
   * @return the color control.
   */
  public int getColorControl() {
    return _colorControl;
  }

  /**
   * Sets the color control attribute.
   * @param control the color control.
   */
  public void setColorControl(int control) {
    _colorControl = control;
    _colorControlSet = true;
  }

  /**
   * Unsets the color control attribute.
   */
  public void unsetColorControl() {
    _colorControl = _colorControlDefault;
    _colorControlSet = false;
  }

  /**
   * Determines whether the local viewer attribute is set.
   * @return true, if set; false, otherwise.
   */
  public boolean hasLocalViewer() {
    return _localViewerSet;
  }

  /**
   * Gets the local viewer attribute.
   * @return the local viewer.
   */
  public boolean getLocalViewer() {
    return _localViewer;
  }

  /**
   * Sets the local viewer attribute.
   * @param local the local viewer.
   */
  public void setLocalViewer(boolean local) {
    _localViewer = local;
    _localViewerSet = true;
  }

  /**
   * Unsets the local viewer attribute.
   */
  public void unsetLocalViewer() {
    _localViewer = _localViewerDefault;
    _localViewerSet = false;
  }

  /**
   * Determines whether the two-sided lighting attribute is set.
   * @return true, if set; false, otherwise.
   */
  public boolean hasTwoSide() {
    return _twoSideSet;
  }

  /**
   * Gets the two-sided lighting attribute.
   * @return the two-sided lighting.
   */
  public boolean getTwoSide() {
    return _twoSide;
  }

  /**
   * Sets the two-sided lighting attribute.
   * @param local the two-sided lighting.
   */
  public void setTwoSide(boolean local) {
    _twoSide = local;
    _twoSideSet = true;
  }

  /**
   * Unsets the two-sided lighting attribute.
   */
  public void unsetTwoSide() {
    _twoSide = _twoSideDefault;
    _twoSideSet = false;
  }

  public void apply() {
    if (_ambientSet)
      glLightModelfv(GL_LIGHT_MODEL_AMBIENT,_ambient,0);
    if (_colorControlSet)
      glLightModelf(GL_LIGHT_MODEL_COLOR_CONTROL,_colorControl);
    if (_localViewerSet)
      glLightModelf(GL_LIGHT_MODEL_LOCAL_VIEWER,(_localViewer?1.0f:0.0f));
    if (_twoSideSet)
      glLightModelf(GL_LIGHT_MODEL_TWO_SIDE,(_twoSide?1.0f:0.0f));
  }

  public int getAttributeBits() {
    return GL_LIGHTING;
  }

  private static float[] _ambientDefault = {0.2f,0.2f,0.2f,1.0f};
  private float[] _ambient = _ambientDefault;
  private boolean _ambientSet;

  private static int _colorControlDefault = GL_SINGLE_COLOR;
  private int _colorControl = _colorControlDefault;
  private boolean _colorControlSet;

  private static boolean _localViewerDefault = false;
  private boolean _localViewer = _localViewerDefault;
  private boolean _localViewerSet;

  private static boolean _twoSideDefault = false;
  private boolean _twoSide = _twoSideDefault;
  private boolean _twoSideSet;

  private static float[] toArray(Color c) {
    float r = c.getRed()/255.0f;
    float g = c.getGreen()/255.0f;
    float b = c.getBlue()/255.0f;
    float a = c.getAlpha()/255.0f;
    return new float[]{r,g,b,a};
  }

  private static Color toColor(float[] a) {
    return new Color(a[0],a[1],a[2],a[3]);
  }
}
