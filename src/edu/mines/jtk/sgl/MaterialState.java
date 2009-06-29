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
 * OpenGL material state. 
 * <p>
 * When applied, this state enables GL_LIGHTING, always.
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
   * Determines whether ambient color for front faces is set.
   * @return true, if set; false, otherwise.
   */
  public boolean hasAmbientFront() {
    return _ambientFrontSet;
  }

  /**
   * Determines whether ambient color for back faces is set.
   * @return true, if set; false, otherwise.
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

  /**
   * Determines whether diffuse color for front faces is set.
   * @return true, if set; false, otherwise.
   */
  public boolean hasDiffuseFront() {
    return _diffuseFrontSet;
  }

  /**
   * Determines whether diffuse color for back faces is set.
   * @return true, if set; false, otherwise.
   */
  public boolean hasDiffuseBack() {
    return _diffuseBackSet;
  }

  /**
   * Gets the diffuse color for front faces.
   * @return the diffuse color.
   */
  public Color getDiffuseFront() {
    return toColor(_diffuseFront);
  }

  /**
   * Gets the diffuse color for back faces.
   * @return the diffuse color.
   */
  public Color getDiffuseBack() {
    return toColor(_diffuseBack);
  }

  /**
   * Sets the diffuse color for front and back faces.
   * @param diffuse the diffuse color.
   */
  public void setDiffuse(Color diffuse) {
    _diffuseFront = _diffuseBack = toArray(diffuse);
    _diffuseFrontSet = _diffuseBackSet = true;
  }

  /**
   * Sets the diffuse color for front faces.
   * @param diffuse the diffuse color.
   */
  public void setDiffuseFront(Color diffuse) {
    _diffuseFront = toArray(diffuse);
    _diffuseFrontSet = true;
  }

  /**
   * Sets the diffuse color for back faces.
   * @param diffuse the diffuse color.
   */
  public void setDiffuseBack(Color diffuse) {
    _diffuseBack = toArray(diffuse);
    _diffuseBackSet = true;
  }

  /**
   * Unsets the diffuse color for front and back faces.
   */
  public void unsetDiffuse() {
    _diffuseFront = _diffuseBack = _diffuseDefault;
    _diffuseFrontSet = _diffuseBackSet = false;
  }

  /**
   * Unsets the diffuse color for front faces.
   */
  public void unsetDiffuseFront() {
    _diffuseFront = _diffuseDefault;
    _diffuseFrontSet = false;
  }

  /**
   * Unsets the diffuse color for back faces.
   */
  public void unsetDiffuseBack() {
    _diffuseBack = _diffuseDefault;
    _diffuseBackSet = false;
  }

  /**
   * Determines whether specular color for front faces is set.
   * @return true, if set; false, otherwise.
   */
  public boolean hasSpecularFront() {
    return _specularFrontSet;
  }

  /**
   * Determines whether specular color for back faces is set.
   * @return true, if set; false, otherwise.
   */
  public boolean hasSpecularBack() {
    return _specularBackSet;
  }

  /**
   * Gets the specular color for front faces.
   * @return the specular color.
   */
  public Color getSpecularFront() {
    return toColor(_specularFront);
  }

  /**
   * Gets the specular color for back faces.
   * @return the specular color.
   */
  public Color getSpecularBack() {
    return toColor(_specularBack);
  }

  /**
   * Sets the specular color for front and back faces.
   * @param specular the specular color.
   */
  public void setSpecular(Color specular) {
    _specularFront = _specularBack = toArray(specular);
    _specularFrontSet = _specularBackSet = true;
  }

  /**
   * Sets the specular color for front faces.
   * @param specular the specular color.
   */
  public void setSpecularFront(Color specular) {
    _specularFront = toArray(specular);
    _specularFrontSet = true;
  }

  /**
   * Sets the specular color for back faces.
   * @param specular the specular color.
   */
  public void setSpecularBack(Color specular) {
    _specularBack = toArray(specular);
    _specularBackSet = true;
  }

  /**
   * Unsets the specular color for front and back faces.
   */
  public void unsetSpecular() {
    _specularFront = _specularBack = _specularDefault;
    _specularFrontSet = _specularBackSet = false;
  }

  /**
   * Unsets the specular color for front faces.
   */
  public void unsetSpecularFront() {
    _specularFront = _specularDefault;
    _specularFrontSet = false;
  }

  /**
   * Unsets the specular color for back faces.
   */
  public void unsetSpecularBack() {
    _specularBack = _specularDefault;
    _specularBackSet = false;
  }

  /**
   * Determines whether emissive color for front faces is set.
   * @return true, if set; false, otherwise.
   */
  public boolean hasEmissiveFront() {
    return _emissiveFrontSet;
  }

  /**
   * Determines whether emissive color for back faces is set.
   * @return true, if set; false, otherwise.
   */
  public boolean hasEmissiveBack() {
    return _emissiveBackSet;
  }

  /**
   * Gets the emissive color for front faces.
   * @return the emissive color.
   */
  public Color getEmissiveFront() {
    return toColor(_emissiveFront);
  }

  /**
   * Gets the emissive color for back faces.
   * @return the emissive color.
   */
  public Color getEmissiveBack() {
    return toColor(_emissiveBack);
  }

  /**
   * Sets the emissive color for front and back faces.
   * @param emissive the emissive color.
   */
  public void setEmissive(Color emissive) {
    _emissiveFront = _emissiveBack = toArray(emissive);
    _emissiveFrontSet = _emissiveBackSet = true;
  }

  /**
   * Sets the emissive color for front faces.
   * @param emissive the emissive color.
   */
  public void setEmissiveFront(Color emissive) {
    _emissiveFront = toArray(emissive);
    _emissiveFrontSet = true;
  }

  /**
   * Sets the emissive color for back faces.
   * @param emissive the emissive color.
   */
  public void setEmissiveBack(Color emissive) {
    _emissiveBack = toArray(emissive);
    _emissiveBackSet = true;
  }

  /**
   * Unsets the emissive color for front and back faces.
   */
  public void unsetEmissive() {
    _emissiveFront = _emissiveBack = _emissiveDefault;
    _emissiveFrontSet = _emissiveBackSet = false;
  }

  /**
   * Unsets the emissive color for front faces.
   */
  public void unsetEmissiveFront() {
    _emissiveFront = _emissiveDefault;
    _emissiveFrontSet = false;
  }

  /**
   * Unsets the emissive color for back faces.
   */
  public void unsetEmissiveBack() {
    _emissiveBack = _emissiveDefault;
    _emissiveBackSet = false;
  }

  /**
   * Determines whether shininess for front faces is set.
   * @return true, if set; false, otherwise.
   */
  public boolean hasShininessFront() {
    return _shininessFrontSet;
  }

  /**
   * Determines whether shininess for back faces is set.
   * @return true, if set; false, otherwise.
   */
  public boolean hasShininessBack() {
    return _shininessBackSet;
  }

  /**
   * Gets the shininess for front faces.
   * @return the shininess.
   */
  public float getShininessFront() {
    return _shininessFront;
  }

  /**
   * Gets the shininess for back faces.
   * @return the shininess.
   */
  public float getShininessBack() {
    return _shininessBack;
  }

  /**
   * Sets the shininess for front and back faces.
   * @param shininess the shininess.
   */
  public void setShininess(float shininess) {
    _shininessFront = _shininessBack = shininess;
    _shininessFrontSet = _shininessBackSet = true;
  }

  /**
   * Sets the shininess for front faces.
   * @param shininess the shininess.
   */
  public void setShininessFront(float shininess) {
    _shininessFront = shininess;
    _shininessFrontSet = true;
  }

  /**
   * Sets the shininess for back faces.
   * @param shininess the shininess.
   */
  public void setShininessBack(float shininess) {
    _shininessBack = shininess;
    _shininessBackSet = true;
  }

  /**
   * Unsets the shininess for front and back faces.
   */
  public void unsetShininess() {
    _shininessFront = _shininessBack = _shininessDefault;
    _shininessFrontSet = _shininessBackSet = false;
  }

  /**
   * Unsets the shininess for front faces.
   */
  public void unsetShininessFront() {
    _shininessFront = _shininessDefault;
    _shininessFrontSet = false;
  }

  /**
   * Unsets the shininess for back faces.
   */
  public void unsetShininessBack() {
    _shininessBack = _shininessDefault;
    _shininessBackSet = false;
  }

  /**
   * Determines whether color material mode for front faces is set.
   * @return true, if set; false, otherwise.
   */
  public boolean hasColorMaterialFront() {
    return _colorMaterialFrontSet;
  }

  /**
   * Determines whether color material mode for back faces is set.
   * @return true, if set; false, otherwise.
   */
  public boolean hasColorMaterialBack() {
    return _colorMaterialBackSet;
  }

  /**
   * Gets the color material mode for front faces.
   * @return the color material mode.
   */
  public int getColorMaterialFront() {
    return _colorMaterialFront;
  }

  /**
   * Gets the color material mode for back faces.
   * @return the color material mode.
   */
  public int getColorMaterialBack() {
    return _colorMaterialBack;
  }

  /**
   * Sets the color material mode for front and back faces.
   * @param mode the color material mode.
   */
  public void setColorMaterial(int mode) {
    _colorMaterialFront = _colorMaterialBack = mode;
    _colorMaterialFrontSet = _colorMaterialBackSet = true;
  }

  /**
   * Sets the color material mode for front faces.
   * @param mode the color material mode.
   */
  public void setColorMaterialFront(int mode) {
    _colorMaterialFront = mode;
    _colorMaterialFrontSet = true;
  }

  /**
   * Sets the color material mode for back faces.
   * @param mode the color material mode.
   */
  public void setColorMaterialBack(int mode) {
    _colorMaterialBack = mode;
    _colorMaterialBackSet = true;
  }

  /**
   * Unsets the color material mode for front and back faces.
   */
  public void unsetColorMaterial() {
    _colorMaterialFront = _colorMaterialBack = _colorMaterialDefault;
    _colorMaterialFrontSet = _colorMaterialBackSet = false;
  }

  /**
   * Unsets the color material mode for front faces.
   */
  public void unsetColorMaterialFront() {
    _colorMaterialFront = _colorMaterialDefault;
    _colorMaterialFrontSet = false;
  }

  /**
   * Unsets the color material mode for back faces.
   */
  public void unsetColorMaterialBack() {
    _colorMaterialBack = _colorMaterialDefault;
    _colorMaterialBackSet = false;
  }

  public void apply() {
    glEnable(GL_LIGHTING);
    if (_ambientFrontSet && _ambientBackSet && _ambientFront==_ambientBack) {
      glMaterialfv(GL_FRONT_AND_BACK,GL_AMBIENT,_ambientFront,0);
    } else {
      if (_ambientFrontSet)
        glMaterialfv(GL_FRONT,GL_AMBIENT,_ambientFront,0);
      if (_ambientBackSet)
        glMaterialfv(GL_BACK,GL_AMBIENT,_ambientBack,0);
    }
    if (_diffuseFrontSet && _diffuseBackSet && _diffuseFront==_diffuseBack) {
      glMaterialfv(GL_FRONT_AND_BACK,GL_DIFFUSE,_diffuseFront,0);
    } else {
      if (_diffuseFrontSet)
        glMaterialfv(GL_FRONT,GL_DIFFUSE,_diffuseFront,0);
      if (_diffuseBackSet)
        glMaterialfv(GL_BACK,GL_DIFFUSE,_diffuseBack,0);
    }
    if (_specularFrontSet && 
        _specularBackSet && 
        _specularFront==_specularBack) {
      glMaterialfv(GL_FRONT_AND_BACK,GL_SPECULAR,_specularFront,0);
    } else {
      if (_specularFrontSet)
        glMaterialfv(GL_FRONT,GL_SPECULAR,_specularFront,0);
      if (_specularBackSet)
        glMaterialfv(GL_BACK,GL_SPECULAR,_specularBack,0);
    }
    if (_emissiveFrontSet && 
        _emissiveBackSet && 
        _emissiveFront==_emissiveBack) {
      glMaterialfv(GL_FRONT_AND_BACK,GL_EMISSION,_emissiveFront,0);
    } else {
      if (_emissiveFrontSet)
        glMaterialfv(GL_FRONT,GL_EMISSION,_emissiveFront,0);
      if (_emissiveBackSet)
        glMaterialfv(GL_BACK,GL_EMISSION,_emissiveBack,0);
    }
    if (_shininessFrontSet && 
        _shininessBackSet && 
        _shininessFront==_shininessBack) {
      glMaterialf(GL_FRONT_AND_BACK,GL_SHININESS,_shininessFront);
    } else {
      if (_shininessFrontSet)
        glMaterialf(GL_FRONT,GL_SHININESS,_shininessFront);
      if (_shininessBackSet)
        glMaterialf(GL_BACK,GL_SHININESS,_shininessBack);
    }
    if (_colorMaterialFrontSet || _colorMaterialBackSet)
      glEnable(GL_COLOR_MATERIAL);
    if (_colorMaterialFrontSet && 
        _colorMaterialBackSet && 
        _colorMaterialFront==_colorMaterialBack) {
      glColorMaterial(GL_FRONT_AND_BACK,_colorMaterialFront);
    } else {
      if (_colorMaterialFrontSet)
        glColorMaterial(GL_FRONT,_colorMaterialFront);
      if (_colorMaterialBackSet)
        glColorMaterial(GL_BACK,_colorMaterialBack);
    }
  }

  public int getAttributeBits() {
    return GL_ENABLE_BIT | GL_LIGHTING_BIT;
  }

  private static float[] _ambientDefault = {0.2f,0.2f,0.2f,1.0f};
  private float[] _ambientFront = _ambientDefault;
  private float[] _ambientBack = _ambientDefault;
  private boolean _ambientFrontSet;
  private boolean _ambientBackSet;

  private static float[] _diffuseDefault = {0.8f,0.8f,0.8f,1.0f};
  private float[] _diffuseFront = _diffuseDefault;
  private float[] _diffuseBack = _diffuseDefault;
  private boolean _diffuseFrontSet;
  private boolean _diffuseBackSet;

  private static float[] _specularDefault = {0.0f,0.0f,0.0f,1.0f};
  private float[] _specularFront = _specularDefault;
  private float[] _specularBack = _specularDefault;
  private boolean _specularFrontSet;
  private boolean _specularBackSet;

  private static float[] _emissiveDefault = {0.0f,0.0f,0.0f,1.0f};
  private float[] _emissiveFront = _emissiveDefault;
  private float[] _emissiveBack = _emissiveDefault;
  private boolean _emissiveFrontSet;
  private boolean _emissiveBackSet;

  private static float _shininessDefault = 0.0f;
  private float _shininessFront = _shininessDefault;
  private float _shininessBack = _shininessDefault;
  private boolean _shininessFrontSet;
  private boolean _shininessBackSet;

  private static int _colorMaterialDefault = GL_AMBIENT_AND_DIFFUSE;
  private int _colorMaterialFront = _colorMaterialDefault;
  private int _colorMaterialBack = _colorMaterialDefault;
  private boolean _colorMaterialFrontSet;
  private boolean _colorMaterialBackSet;

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
