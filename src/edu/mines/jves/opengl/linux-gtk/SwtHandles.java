package edu.mines.jves.opengl;

import org.eclipse.swt.SWTException;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.graphics.Drawable;
import org.eclipse.swt.graphics.GCData;
import org.eclipse.swt.internal.gtk.OS;

/**
 * SWT handles required to create, access, and destroy OpenGL contexts.
 * For internal use only. This class is platform-dependent.
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.11.30
 */
class SwtHandles {

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

  SwtHandles(Canvas canvas) {
    drawable = canvas;
    data = new GCData();
    handle = canvas.handle;
    _gdkGC = drawable.internal_new_GC(data);
    if (_gdkGC==0)
      throw new RuntimeException("cannot new GC");
    long gdkWindow = data.drawable;
    xdisplay = OS.gdk_x11_drawable_get_xdisplay((int)gdkWindow); // 64-bit!
    xdrawable = OS.gdk_x11_drawable_get_xid((int)gdkWindow); // 64-bit!

    // Disable double-buffering, since we do that with OpenGL.
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
