/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.opengl;

import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.graphics.Drawable;
import org.eclipse.swt.graphics.GCData;
import org.eclipse.swt.internal.win32.OS;

/**
 * SWT hooks required to create, access, and destroy OpenGL contexts.
 * For internal use only. This class is platform-dependent.
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.11.30
 */
class SwtHooks {

  // Generic SWT.
  Drawable drawable;
  GCData data;
  long handle; // the HWND, same as below

  // For X Windows.
  long xdisplay;
  long xdrawable;

  // For Microsoft Windows.
  long hwnd;
  long hdc;

  SwtHooks(Canvas canvas) {
    drawable = canvas;
    data = new GCData();
    handle = canvas.handle;
    hwnd = canvas.handle;
    hdc = drawable.internal_new_GC(data);
    if (hdc==0)
      throw new RuntimeException("cannot new hdc");
  }
  void dispose() {
    try {
      drawable.internal_dispose_GC((int)hdc,data); // not 32-bit clean!
    } catch (SWTException swte) {
      throw new RuntimeException("cannot dispose hdc");
    }
  }
}
