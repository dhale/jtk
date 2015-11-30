/****************************************************************************
Copyright 2004, Colorado School of Mines and others.
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

/**
 * A transform context for drawing.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.05.21
 */
public class DrawContext extends TransformContext {

  /**
   * Constructs a draw context for the specified view canvas.
   * @param canvas the view canvas.
   */
  public DrawContext(ViewCanvas canvas) {
    super(canvas);
  }
}
