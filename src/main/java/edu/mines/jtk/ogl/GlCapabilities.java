/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.ogl;

import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLProfile;

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
