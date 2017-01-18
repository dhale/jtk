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

import java.util.Arrays;

import static edu.mines.jtk.ogl.Gl.*;

/**
 * Lighting for {@link edu.mines.jtk.sgl.OrbitView}.
 * <p>
 * There are three light sources available within the JTK, and
 * each light can be individually positioned in the canvas and have color
 * properties assigned. Furthermore, each light can be defined to be one of
 * two types:
 * <ol>
 *   <li>
 *     Directional light (default): the light source placed at an infinite
 *     distance and all light rays are parallel.
 *   </li>
 *   <li>
 *     Positional light: the light source is positioned in the frustum and
 *     light rays extend spherically in all directions.
 *   </li>
 * </ol>
 * The default scene lighting matches the previously-hardwired lighting
 * setup used by the JTK, that is a single <em>directional</em> light
 * positioned at <code>(-0.1,-0.1, 0.0)</code> with black ambient light, and
 * white specular and diffuse lights. This light is considered to be
 * the "primary light".
 * </p>
 * @author Chris Engelsma
 * @version 2017.01.17
 */
public class OrbitViewLighting {

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
   * Constructs an OrbitView Lighting
   */
  public OrbitViewLighting() {
    for (int i=0; i<3; ++i) {
      _positions[i]  = _posDefault;
      _ambients[i]   = _ambientDefault;
      _diffuses[i]   = _diffuseDefault;
      _speculars[i]  = _specularDefault;
      _lightTypes[i] = LightSourceType.DIRECTIONAL;
      _lightSet[i]   = false;
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
   * Toggles the primary light source.
   */
  public void toggleLight() {
    toggleLight(0);
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
   * Determines if the primary light source is on.
   * @return true, if light source is on; false, otherwise.
   */
  public boolean isLightOn() {
    return isLightOn(0);
  }

  /**
   * Determines if a light source is on.
   * @param i a light source [0-2]
   * @return true, if light is on; false, otherwise.
   */
  public boolean isLightOn(int i) {
    return _lightSet[i];
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
  public void setAmbientAndDiffuse(float[] rgba) {
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
   * Sets the position of the primary light source.
   * @param pos array[3] of (x,y,z) coordinates.
   */
  public void setPosition(float[] pos) {
    setPosition(0,pos);
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

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    OrbitViewLighting that = (OrbitViewLighting) o;

    if (!Arrays.equals(_lightSet, that._lightSet)) return false;
    // Probably incorrect - comparing Object[] arrays with Arrays.equals
    if (!Arrays.equals(_lightTypes, that._lightTypes)) return false;
    if (!Arrays.deepEquals(_ambients, that._ambients)) return false;
    if (!Arrays.deepEquals(_speculars, that._speculars)) return false;
    if (!Arrays.deepEquals(_diffuses, that._diffuses)) return false;
    return Arrays.deepEquals(_positions, that._positions);
  }

  @Override
  public int hashCode() {
    int result = Arrays.hashCode(_lightSet);
    result = 31 * result + Arrays.hashCode(_lightTypes);
    result = 31 * result + Arrays.deepHashCode(_ambients);
    result = 31 * result + Arrays.deepHashCode(_speculars);
    result = 31 * result + Arrays.deepHashCode(_diffuses);
    result = 31 * result + Arrays.deepHashCode(_positions);
    return result;
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
