/****************************************************************************
Copyright 2009, Colorado School of Mines and others.
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
package edu.mines.jtk.interp;

import edu.mines.jtk.dsp.Sampling;

/**
 * Gridded interpolation of scattered samples of 2D functions f(x1,x2).
 * The orthogonal Cartesian grid is defined by uniform samplings of the 
 * independent variables x1 and x2. A gridder can interpolate on such a 
 * uniform sampling grid, but may or may not be capable of interpolating at 
 * other locations (x1,x2) not on the grid, depending on its implementation.
 * @author Dave Hale, Colorado School of Mines
 * @version 2009.07.22
 */
public interface Gridder2 {

  /**
   * Sets the known (scattered) samples.
   * The specified arrays may be either referenced or copied,
   * depending on the implementation. Unless stated otherwise,
   * assume that these arrays are referenced, not copied.
   * @param f array of sample values f(x1,x2).
   * @param x1 array of sample x1 coordinates.
   * @param x2 array of sample x2 coordinates.
   */
  public void setScattered(float[] f, float[] x1, float[] x2);

  /**
   * Computes gridded sample values from the known sample values.
   * Known (scattered) samples must be set before calling this method.
   * Some implementations require that the specified samplings be uniform.
   * @param s1 sampling of x1.
   * @param s2 sampling of x2.
   * @return array of gridded sample values.
   */
  public float[][] grid(Sampling s1, Sampling s2);
}
