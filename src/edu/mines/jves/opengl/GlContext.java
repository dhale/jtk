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

import java.awt.Canvas;

public class GlContext {

  public GlContext(java.awt.Canvas canvas) {
    _peer = makeGlAwtCanvasContext(canvas);
  }

  public void lock() {
    // TODO: acquire mutex
    lock(_peer);
  }

  public void unlock() {
    unlock(_peer);
    // TODO: release mutex
  }

  public void swapBuffers() {
    // TODO: ensure context is locked
    swapBuffers(_peer);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private long _peer; // C++ peer of this OpenGL context

  private static native long makeGlAwtCanvasContext(java.awt.Canvas canvas);
  private static native void lock(long peer);
  private static native void unlock(long peer);
  private static native void swapBuffers(long peer);

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

