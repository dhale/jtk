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
import org.eclipse.swt.internal.gtk.OS;

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
  long handle; // the GtkWidget*

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
    _gdkGC = drawable.internal_new_GC(data);
    if (_gdkGC==0)
      throw new RuntimeException("cannot new GC");
    long gdkWindow = data.drawable;
    xdisplay = OS.gdk_x11_drawable_get_xdisplay((int)gdkWindow); // 64-bit!
    xdrawable = OS.gdk_x11_drawable_get_xid((int)gdkWindow); // 64-bit!

    // Disable GTK double-buffering! By default, GTK and, hence, SWT
    // widgets paint themselves in offscreen pixmaps, which GTK then 
    // copies to the widget's window. With OpenGL, we perform double-
    // buffering directly to the widget's window. Therefore, GTK's
    // double-buffering is redundant. Furthermore, GTK's copy from 
    // the pixmap to the widget window replaces anything we draw in
    // that window via OpenGL. So we must disable this GTK feature.
    OS.gtk_widget_set_double_buffered((int)handle,false); // 64-bit!
  }
  void dispose() {
    try {
      drawable.internal_dispose_GC((int)_gdkGC,data); // 64-bit!
    } catch (SWTException swte) {
      throw new RuntimeException("cannot dispose GC");
    }
  }
  private long _gdkGC;
}
