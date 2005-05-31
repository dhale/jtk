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
 * OpenGL material state.
 * <p>
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.05.31
 */
public class MaterialState implements State {

  /**
   * Constructs material state.
   */
  public MaterialState() {
  }

  /**
   * Determines whether this state has ambient color for front faces.
   * @return true, if ambient color; false, otherwise.
   */
  public boolean hasAmbientFront() {
    return _ambientFrontSet;
  }

  /**
   * Determines whether this state has ambient color for back faces.
   * @return true, if ambient color; false, otherwise.
   */
  public boolean hasAmbientBack() {
    return _ambientBackSet;
  }

  /**
   * Gets the ambient color for front faces.
   * @return the ambient color.
   */
  public Color getAmbientFront() {
    return toColor(_ambientFront);
  }

  /**
   * Gets the ambient color for back faces.
   * @return the ambient color.
   */
  public Color getAmbientBack() {
    return toColor(_ambientBack);
  }

  /**
   * Sets the ambient color for front and back faces.
   * @param ambient the ambient color.
   */
  public void setAmbient(Color ambient) {
    _ambientFront = _ambientBack = toArray(ambient);
    _ambientFrontSet = _ambientBackSet = true;
  }

  /**
   * Sets the ambient color for front faces.
   * @param ambient the ambient color.
   */
  public void setAmbientFront(Color ambient) {
    _ambientFront = toArray(ambient);
    _ambientFrontSet = true;
  }

  /**
   * Sets the ambient color for back faces.
   * @param ambient the ambient color.
   */
  public void setAmbientBack(Color ambient) {
    _ambientBack = toArray(ambient);
    _ambientBackSet = true;
  }

  /**
   * Unsets the ambient color for front and back faces.
   */
  public void unsetAmbient() {
    _ambientFront = _ambientBack = _ambientDefault;
    _ambientFrontSet = _ambientBackSet = false;
  }

  /**
   * Unsets the ambient color for front faces.
   */
  public void unsetAmbientFront() {
    _ambientFront = _ambientDefault;
    _ambientFrontSet = false;
  }

  /**
   * Unsets the ambient color for back faces.
   */
  public void unsetAmbientBack() {
    _ambientBack = _ambientDefault;
    _ambientBackSet = false;
  }

  public void apply() {
    glEnable(GL_LIGHTING);
    if (_ambientFrontSet && _ambientBackSet && _ambientFront==_ambientBack) {
      glMaterialfv(GL_FRONT_AND_BACK,GL_AMBIENT,_ambientFront);
    } else {
      if (_ambientFrontSet)
        glMaterialfv(GL_FRONT,GL_AMBIENT,_ambientFront);
      if (_ambientBackSet)
        glMaterialfv(GL_BACK,GL_AMBIENT,_ambientBack);
    }
  }

  public int getAttributeBits() {
    return GL_ENABLE_BIT | GL_LIGHTING_BIT;
  }


  private static float[] _ambientDefault = {0.2f,0.2f,0.2f,1.0f};
  private float[] _ambient = _ambientDefault;
  private float[] _ambientFront = _ambientDefault;
  private float[] _ambientBack = _ambientDefault;
  private boolean _ambientSet;
  private boolean _ambientFrontSet;
  private boolean _ambientBackSet;

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
  setAmbient(Color color)
    glMaterialfv(GL_FRONT_AND_BACK,GL_AMBIENT,...)
    glEnable(GL_LIGHTING)
    GL_LIGHTING_BIT
    GL_ENABLE_BIT
  setAmbientFront(Color color)
    glMaterialfv(GL_FRONT,GL_AMBIENT,...)
    glEnable(GL_LIGHTING)
    GL_LIGHTING_BIT
    GL_ENABLE_BIT
  setAmbientBack(Color color)
    glMaterialfv(GL_BACK,GL_AMBIENT,...)
    glEnable(GL_LIGHTING)
    GL_LIGHTING_BIT
    GL_ENABLE_BIT
  setDiffuse(Color color)
    glMaterialfv(GL_FRONT_AND_BACK,GL_DIFFUSE,...)
    glEnable(GL_LIGHTING)
    GL_LIGHTING_BIT
    GL_ENABLE_BIT
  setDiffuseFront(Color color)
    glMaterialfv(GL_FRONT,GL_DIFFUSE,...)
    glEnable(GL_LIGHTING)
    GL_LIGHTING_BIT
    GL_ENABLE_BIT
  setDiffuseBack(Color color)
    glMaterialfv(GL_BACK,GL_DIFFUSE,...)
    glEnable(GL_LIGHTING)
    GL_LIGHTING_BIT
    GL_ENABLE_BIT
  setSpecular(Color color)
    glMaterialfv(GL_FRONT_AND_BACK,GL_SPECULAR,...)
    glEnable(GL_LIGHTING)
    GL_LIGHTING_BIT
    GL_ENABLE_BIT
  setSpecularFront(Color color)
    glMaterialfv(GL_FRONT,GL_SPECULAR,...)
    glEnable(GL_LIGHTING)
    GL_LIGHTING_BIT
    GL_ENABLE_BIT
  setSpecularBack(Color color)
    glMaterialfv(GL_BACK,GL_SPECULAR,...)
    glEnable(GL_LIGHTING)
    GL_LIGHTING_BIT
    GL_ENABLE_BIT
  setEmissive(Color color)
    glMaterialfv(GL_FRONT_AND_BACK,GL_EMISSION,...)
    glEnable(GL_LIGHTING)
    GL_LIGHTING_BIT
    GL_ENABLE_BIT
  setEmissiveFront(Color color)
    glMaterialfv(GL_FRONT,GL_EMISSION,...)
    glEnable(GL_LIGHTING)
    GL_LIGHTING_BIT
    GL_ENABLE_BIT
  setEmissiveBack(Color color)
    glMaterialfv(GL_BACK,GL_EMISSION,...)
    glEnable(GL_LIGHTING)
    GL_LIGHTING_BIT
    GL_ENABLE_BIT
  setShininess(float exponent)
    glMaterialf(GL_FRONT_AND_BACK,GL_EMISSION,exponent)
    glEnable(GL_LIGHTING)
    GL_LIGHTING_BIT
    GL_ENABLE_BIT
  setShininessFront(float exponent)
    glMaterialf(GL_FRONT,GL_EMISSION,exponent)
    glEnable(GL_LIGHTING)
    GL_LIGHTING_BIT
    GL_ENABLE_BIT
  setShininessBack(float exponent)
    glMaterialf(GL_BACK,GL_EMISSION,exponent)
    glEnable(GL_LIGHTING)
    GL_LIGHTING_BIT
    GL_ENABLE_BIT
  setColorMaterial(int mode)
    glColorMaterial(GL_FRONT_AND_BACK,mode)
    glEnable(GL_LIGHTING)
    glEnable(GL_COLOR_MATERIAL)
    GL_LIGHTING_BIT
    GL_ENABLE_BIT
  setColorMaterialFront(int mode)
    glColorMaterial(GL_FRONT,mode)
    glEnable(GL_LIGHTING)
    glEnable(GL_COLOR_MATERIAL)
    GL_LIGHTING_BIT
    GL_ENABLE_BIT
  setColorMaterialBack(int mode)
    glColorMaterial(GL_BACK,mode)
    glEnable(GL_LIGHTING)
    glEnable(GL_COLOR_MATERIAL)
    GL_LIGHTING_BIT
    GL_ENABLE_BIT
    */
