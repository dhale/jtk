//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2004, Colorado School of Mines and others.
// All rights reserved. This program and the accompanying materials
// are made available under the terms of the Common Public License v1.0
// which accompanies this distribution, and is available at
// http://www.eclipse.org/legal/cpl-v10.html
// Contributors:
//   Dave Hale, Colorado School of Mines
//////////////////////////////////////////////////////////////////////////////
package edu.mines.jves.opengl;

public class GlContext {

  public GlContext(long handle) {
  }

  public void makeCurrent() {
  }

  public void swapBuffers() {
    nswapBuffers(_display,_handle);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private long _display;
  private long _handle;
  private long _context;

  private native void nswapBuffers(long display, long handle);

  private native long makeContext(
    int type, long display, long handle);

  ///////////////////////////////////////////////////////////////////////////
  // pointers to OpenGL functions after version 1.1

  // OpenGL 1.2
  long glBlendColor;

  // OpenGL 1.3
  long glActiveTexture;

  // OpenGL 1.4
  long glBlendFuncSeparate;

  // OpenGL 1.5
  long glGenQueries;

  private void init() {
    // OpenGL 1.2
    glBlendColor = getProcAddress("glBlendColor");
    // OpenGL 1.3
    glActiveTexture = getProcAddress("glActiveTexture");
    // OpenGL 1.4
    glBlendFuncSeparate = getProcAddress("glBlendFuncSeparate");
    // OpenGL 1.5
    glGenQueries = getProcAddress("glGenQueries");
  }
  private static native long getProcAddress(String functionName);
}

