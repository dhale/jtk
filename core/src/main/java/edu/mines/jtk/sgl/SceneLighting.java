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
 * <p>
 * There are three lights available within the JTK. Each light can be
 * positioned independently and have color properties assigned.
 *
 * The default scene lighting matches the default lighting used
 * by the JTK: a single <em>directional</em> light positioned at
 * <code>(-0.1,-0.1, 0.0)</code> with no ambient light, and both specular and
 * diffuse light colors are set to white.
 * </p>
 * @author Chris Engelsma
 * @version 2017.01.17
 */
public class SceneLighting {

  /**
   * A light source type.
   */
  public enum LightSourceType {
    /**
     * Directional light: Light source is at an infinite distance and all
     * rays are parallel and have the direction (x,y,z).
     */
    DIRECTIONAL,
    /**
     * Positional light: Light source is positioned at (x,y,z) and the light
     * points towards all directions.
     */
    POSITIONAL
  }

  /**
   * Constructs new scene lighting.
   */
  public SceneLighting() {
    for (int i=0; i<3; ++i) {
      _positions[i] = _posDefault;
      _ambients[i]  = _ambientDefault;
      _diffuses[i]  = _diffuseDefault;
      _speculars[i] = _specularDefault;
      _lightTypes[i] = LightSourceType.DIRECTIONAL;
      _lightSet[i] = false;
    }
    setLight(0,true);
  }

  /**
   * Toggles a light source.
   * @param i a light source [0-2]
   */
  public void toggleLight(int i) {
    _lightSet[i] = !_lightSet[i];
  }

  /**
   * Sets the state of a light source.
   * @param i a light source [0-2]
   * @param isOn true, if light is on; false, otherwise.
   */
  public void setLight(int i, boolean isOn) {
    _lightSet[i] = isOn;
  }

  /**
   * Sets the ambient color of the primary light source.
   * @param rgba array[4] of color components.
   */
  public void setAmbient(float[] rgba) {
    setAmbient(0,rgba);
  }

  /**
   * Sets the ambient color of a light source.
   * @param i a light source [0-2].
   * @param rgba array[4] of color components.
   */
  public void setAmbient(int i, float[] rgba) {
    _ambients[i] = rgba;
  }

  /**
   * Gets the ambient color of the primary light source.
   * @return ambient color of the primary light source.
   */
  public float[] getAmbient() {
    return getAmbient(0);
  }

  /**
   * Gets the ambient color of a light source.
   * @param i a light source [0-2].
   * @return ambient color of the light source.
   */
  public float[] getAmbient(int i) {
    return _ambients[i];
  }

  /**
   * Sets the specular color of the primary light source.
   * @param rgba array[4] of color components.
   */
  public void setSpecular(float[] rgba) {
    setSpecular(0,rgba);
  }

  /**
   * Sets the specular color of a light source.
   * @param i a light source [0-2].
   * @param rgba specular color of a light source.
   */
  public void setSpecular(int i, float[] rgba) {
    _speculars[i] = rgba;
  }

  /**
   * Gets the specular color of the primary light source.
   * @return the specular color of the primary light source.
   */
  public float[] getSpecular() {
    return getSpecular(0);
  }

  /**
   * Gets the specular color of a light source.
   * @param i a light source [0-2].
   * @return specular color of a light source.
   */
  public float[] getSpecular(int i) {
    return _speculars[i];
  }

  /**
   * Sets the diffuse color of the primary light source.
   * @param rgba diffuse color of the primary light source.
   */
  public void setDiffuse(float[] rgba) {
    setDiffuse(0,rgba);
  }

  /**
   * Sets the diffuse color of a light source.
   * @param i a light source [0-2].
   * @param rgba diffuse color of a light source.
   */
  public void setDiffuse(int i, float[] rgba) {
    _diffuses[i] = rgba;
  }

  /**
   * Gets the diffuse color of the primary light source.
   * @return diffuse color of the primary light source.
   */
  public float[] getDiffuse() {
    return getDiffuse(0);
  }

  /**
   * Gets the diffuse color of a light source.
   * @param i a light source [0-2].
   * @return diffuse color of a light source.
   */
  public float[] getDiffuse(int i) {
    return _diffuses[i];
  }

  /**
   * Sets the ambient and diffuse colors of the primary light source.
   * @param rgba array[4] of color components.
   */
  public void setAmbientDiffuse(float[] rgba) {
    setAmbientAndDiffuse(0,rgba);
  }

  /**
   * Sets the ambient and diffuse colors of a light source.
   * @param i a light source [0-2].
   * @param rgba array[4] of color components.
   */
  public void setAmbientAndDiffuse(int i, float[] rgba) {
    setAmbient(i,rgba);
    setDiffuse(i,rgba);
  }

  /**
   * Sets the position of the primary light source.
   * @param lx the x-coordinate.
   * @param ly the y-coordinate.
   * @param lz the z-coordinate.
   */
  public void setPosition(float lx, float ly, float lz) {
    setPosition(0,lx,ly,lz);
  }

  /**
   * Gets the position of the primary light source.
   * @return the (x,y,z,w) coordinates of the primary light source.
   */
  public float[] getPosition() {
    return getPosition(0);
  }

  /**
   * Sets the position of a light source.
   * @param i a light source [0-2].
   * @param lx the x-coordinate.
   * @param ly the y-coordinate.
   * @param lz the z-coordinate.
   */
  public void setPosition(int i, float lx, float ly, float lz) {
    setPosition(i, new float[] { lx, ly, lz});
  }

  /**
   * Sets the position of a light source.
   * @param i a light source [0-2].
   * @param pos array[4] containing (x,y,z,w) coordinates.
   */
  public void setPosition(int i, float[] pos) {
    _positions[i] = pos;
  }

  /**
   * Gets the position of a light source.
   * @param i a light source [0-2].
   * @return the (x,y,z,w) position of the light source.
   */
  public float[] getPosition(int i) {
    return _positions[i];
  }

  /**
   * Sets the {@link LightSourceType} for the primary light source.
   * @param type a {@link LightSourceType}.
   */
  public void setLightSourceType(LightSourceType type) {
    setLightSourceType(0,type);
  }

  /**
   * Sets the {@link LightSourceType} for a light source.
   * @param i a light source [0-2].
   * @param type a {@link LightSourceType}.
   */
  public void setLightSourceType(int i, LightSourceType type) {
    _lightTypes[i] = type;
  }

  /**
   * Gets the {@link LightSourceType} for the primary light source.
   * @return the {@link LightSourceType}.
   */
  public LightSourceType getLightSourceType() {
    return getLightSourceType(0);
  }

  /**
   * Gets the {@link LightSourceType} for a light source.
   * @param i a light source [0-2].
   * @return a {@link LightSourceType}.
   */
  public LightSourceType getLightSourceType(int i) {
    return _lightTypes[i];
  }
  public void draw() {
    for (int i=0; i<_sources.length; ++i) {
      if (_lightSet[i]) {

        float w = (_lightTypes[i]==LightSourceType.DIRECTIONAL)?0.0f:1.0f;

        float[] pos = new float[]{
          _positions[i][0],
          _positions[i][1],
          _positions[i][2],
          w
        };

        glLightfv(_sources[i],GL_POSITION,pos,0);
        glLightfv(_sources[i],GL_AMBIENT ,_ambients[i] ,0);
        glLightfv(_sources[i],GL_DIFFUSE ,_diffuses[i] ,0);
        glLightfv(_sources[i],GL_SPECULAR,_speculars[i],0);

        glEnable(_sources[i]);
      } else {
        glDisable(_sources[i]);
      }
    }
  }

  ////////////////////////////////////////////////////////////////////////////
  // private

  private static final int[] _sources  = {GL_LIGHT0,GL_LIGHT1,GL_LIGHT2 };

  private static final float[] _ambientDefault =
    new float[] { 0.0f, 0.0f, 0.0f, 1.0f };
  private static final float[] _specularDefault =
    new float[] { 1.0f, 1.0f, 1.0f, 1.0f };
  private static final float[] _diffuseDefault =
    new float[] { 1.0f, 1.0f, 1.0f, 1.0f };
  private static final float[] _posDefault =
    new float[] {-0.1f,-0.1f,1.0f };

  private boolean[] _lightSet    = new boolean[3];

  private LightSourceType[] _lightTypes = new LightSourceType[3];
  private float[][] _ambients  = new float[3][4];
  private float[][] _speculars = new float[3][4];
  private float[][] _diffuses  = new float[3][4];
  private float[][] _positions = new float[3][3];
}
