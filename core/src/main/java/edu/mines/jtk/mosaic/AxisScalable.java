/****************************************************************************
Copyright 2016, Colorado School of Mines.
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
package edu.mines.jtk.mosaic;

/**
 * The interface used to define methods required for changing 
 * axis scaling of TiledViews.
 * <p>
 * An object that implements the AxisScalable interface allows
 * its plot axes to be set to either a linear or logarithmic
 * scale. The only methods defined in this interface are the 
 * getters and setters for the horizontal and vertical scales.
 * 
 * @author Eric Addison
 * @version 2016.1.25
 */
public interface AxisScalable {

  /**
   * Sets the horizontal axis scaling.
   * @param s the new scale
   * @return an AxisScalable object
   */
  public AxisScalable setHScale(AxisScale s);
  
  /**
   * Sets the vertical axis scaling.
   * @param s the new scale
   * @return an AxisScalable object
   */
  public AxisScalable setVScale(AxisScale s);
 
  /**
   * Gets the current horizontal axis scaling.
   * @return the horizontal scale
   */
  public AxisScale getHScale();
  
  /**
   * Gets the current vertical axis scaling.
   * @return the vertical scale
   */
  public AxisScale getVScale();
  
}