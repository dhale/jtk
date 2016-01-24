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
 * Interface used to define methods required for changing axis scaling of TiledViews.
 */
public interface AxisScalable {

  public AxisScalable setHScale(Scale s);
  
  public AxisScalable setVScale(Scale s);
  
  public Scale getHScale();
  
  public Scale getVScale();
  
}