/****************************************************************************
Copyright 2017, Colorado School of Mines and others.
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

import static edu.mines.jtk.ogl.Gl.*;

/**
 * OpenGL light source.
 * @author Chris Engelsma
 * @version 2017.01.16
 */
public class LightSource {

  public LightSource() {
  }

  public boolean hasAmbient() {
    return _ambientSet;
  }

  public void setAmbient(boolean ambient) {
    _ambient = ambient;
    _ambientSet = true;
  }

  public boolean getAmbient() {
    return _ambient;
  }

  public void unsetAmbient() {
    _ambientSet = false;
  }

  public boolean hasSpecular() {
    return _specularSet;
  }

  public void setSpecular(boolean specular) {
    _specular = specular;
    _specularSet = true;
  }

  public boolean getSpecular() {
    return _specular;
  }

  public void unsetSpecular() {
    _specularSet = false;
  }

  public boolean hasDiffuse() {
    return _diffuseSet;
  }

  public void setDiffuse(boolean diffuse) {
    _diffuse = diffuse;
    _diffuseSet = true;
  }

  public boolean getDiffuse() {
    return _diffuse;
  }

  public void unsetDiffuse() {
    _diffuseSet = false;
  }

  /**
   * Sets the position of this light.
   * @param lightPosition array[4] of light position.
   */
  public void setPosition(float[] lightPosition) {
    _lightPosition = lightPosition;
  }

  /**
   * Sets the position of this light state.
   * @param lx x-position of this light state.
   * @param ly y-position of this light state.
   * @param lz z-position of this light state.
   * @param lw w-position of this light state.
   */
  public void setPosition(float lx, float ly, float lz, float lw) {
    setPosition(new float[] { lx, ly, lz, lw });
  }

  public float[] getPosition() {
    return _lightPosition;
  }

  public void draw() {
    glLightfv(GL_LIGHT0, GL_POSITION, _lightPosition, 0);
    if (_ambientSet) {
      if (_ambient) {
        glLightfv(GL_LIGHT0, GL_AMBIENT, _ambientColor, 0);
      }
    }
    if (_diffuseSet) {
      if (_diffuse) {
        glLightfv(GL_LIGHT0, GL_DIFFUSE, _diffuseColor, 0);
      }
    }
    if (_specularSet) {
      if (_diffuse) {
        glLightfv(GL_LIGHT0, GL_SPECULAR, _specularColor, 0);
      }
    }
    glEnable(GL_LIGHT0);
  }

  ////////////////////////////////////////////////////////////////////////////
  // private

  private boolean _ambientSet = false,_ambient;
  private boolean _specularSet = false,_specular;
  private boolean _diffuseSet = false,_diffuse;

  private static final float[] _ambientColorDefault  = { 0,0,0,0 };
  private static final float[] _specularColorDefault = { 0,0,0,0 };
  private static final float[] _diffuseColorDefault  = { 0,0,0,0 };
  private static final float[] _positionDefault  = { -0.1f,-0.1f,1.0f,0.0f };

  private float[] _ambientColor  = _ambientColorDefault;
  private float[] _specularColor = _specularColorDefault;
  private float[] _diffuseColor  = _diffuseColorDefault;
  private float[] _lightPosition = _positionDefault;


}
