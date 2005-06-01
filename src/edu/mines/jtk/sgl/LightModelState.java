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
   * Determines whether this state has ambient color.
   * @return true, if ambient color; false, otherwise.
   */
  public boolean hasAmbientColor() {
    return _ambientColorSet;
  }

  /**
   * Gets the ambient color.
   * @return the ambient color.
   */
  public Color getAmbientColor() {
    return toColor(_ambientColor);
  }

  /**
   * Sets the ambient color.
   * @param color the ambient color.
   */
  public void setAmbientColor(Color color) {
    _ambientColor = toArray(color);
    _ambientColorSet = true;
  }

  /**
   * Unsets the ambient color.  
   */
  public void unsetAmbientColor() {
    _ambientColor = _ambientColorDefault;
    _ambientColorSet = false;
  }

  public void apply() {
    if (_ambientColorSet)
      glLightModelfv(GL_LIGHT_MODEL_AMBIENT,_ambientColor);
  }

  public int getAttributeBits() {
    return GL_LIGHTING;
  }

  private static float[] _ambientColorDefault = {0.2f,0.2f,0.2f,1.0f};
  private float[] _ambientColor = _ambientColorDefault;
  private boolean _ambientColorSet;

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

/*
LightModelState
  setAmbient(Color color)
    glLightMOdelfv
    glEnable(GL_LIGHTING)
    GL_LIGHTING_BIT
    GL_ENABLE_BIT
  setColorControl(int mode)
    glLightModeli(GL_LIGHT_MODEL_COLOR_CONTROL,mode)
    glEnable(GL_LIGHTING)
    GL_LIGHTING_BIT
    GL_ENABLE_BIT
  setTwoSide(boolean twoSide)
    glLightModeli(GL_LIGHT_MODEL_TWO_SIDE,(twoSide?1:0));
    glEnable(GL_LIGHTING)
    GL_LIGHTING_BIT
    GL_ENABLE_BIT
  setLocalViewer(boolean localViewer)
    glLightModeli(GL_LIGHT_MODEL_LOCAL_VIEWER,(localViewer?1:0));
    glEnable(GL_LIGHTING)
    GL_LIGHTING_BIT
    GL_ENABLE_BIT
*/
