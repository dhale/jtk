/****************************************************************************
Copyright 2005, Colorado School of Mines and others.
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
package edu.mines.jtk.awt;

import java.util.EventListener;

/**
 * A color map listener listens for changes to a color map.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.12.24
 */
public interface ColorMapListener extends EventListener {

  /**
   * Called when the color map changes.
   * @param cm the color mapper.
   */
  public void colorMapChanged(ColorMap cm);
}
