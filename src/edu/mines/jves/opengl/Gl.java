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

public class Gl {

  public static native void glFlush();

  ///////////////////////////////////////////////////////////////////////////
  // OpenGL 1.2

  public static void glBlendColor(
    float red, float green, float blue, float alpha) 
  {
    nglBlendColor(context().glBlendColor,
      red,green,blue,alpha);
  }
  private static native void nglBlendColor(long glBlendColor,
    float red, float green, float blue, float alpha);

  ///////////////////////////////////////////////////////////////////////////
  // private

  private static ThreadLocal _context = new ThreadLocal();
  private static GlContext context() {
    return (GlContext)_context.get();
  }
}
