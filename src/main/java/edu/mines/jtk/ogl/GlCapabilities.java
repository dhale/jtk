/****************************************************************************
Copyright 2006, Colorado School of Mines and others.
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
package edu.mines.jtk.ogl;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;

/**
 * OpenGL capabilities.
 * @author Dave Hale, Colorado School of Mines
 * @version 2012.08.17
 */
public class GlCapabilities extends GLCapabilities {
  public GlCapabilities() {
    super(GLProfile.getDefault());
  }
}
