/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/

/****************************************************************************
AwtBench JNI glue
@author Dave Hale, Colorado School of Mines
****************************************************************************/
#ifdef WIN32 // if Microsoft Windows, ...
#define MWIN
#include <windows.h>
#else // else, assume X Windows, ...
#define XWIN
#include <X11/Xlib.h>
#endif
#include <stdio.h>
#include "jawt_md.h"
#include "../util/jniglue.h"

// Note: _00024 is the escape sequence for Unicode character 0024, 
// which represents $. The fully-qualified native method name is 
// edu.mines.jtk.bench.AwtBench$NativeCanvas.paintNative
extern "C" JNIEXPORT void JNICALL
Java_edu_mines_jtk_bench_AwtBench_00024NativeCanvas_paintNative(
  JNIEnv* env, jclass cls, jobject canvas)
{
  JNI_TRY
  JAWT awt;
  awt.version = JAWT_VERSION_1_3;
  if (JAWT_GetAWT(env,&awt)==JNI_FALSE) {
    printf("cannot get AWT\n");
    return;
  }
  JAWT_DrawingSurface* ds = awt.GetDrawingSurface(env,canvas);
  if (ds==0) {
    printf("cannot get drawing surface\n");
    return;
  }
  jint lock = ds->Lock(ds);
  if ((lock&JAWT_LOCK_ERROR)!=0) {
    printf("cannot lock drawing surface\n");
    awt.FreeDrawingSurface(ds);
  }
  JAWT_DrawingSurfaceInfo* dsi = ds->GetDrawingSurfaceInfo(ds);
  if (dsi==NULL) {
    printf("cannot get drawing surface info\n");
    ds->Unlock(ds);
    awt.FreeDrawingSurface(ds);
    return;
  }
#if defined(MWIN)
  JAWT_Win32DrawingSurfaceInfo* pdsi = 
    (JAWT_Win32DrawingSurfaceInfo*)dsi->platformInfo;
  HWND hwnd = pdsi->hwnd;
  HDC hdc = pdsi->hdc;
  // TODO: draw something with Microsoft Windows
  printf("Should draw something via Microsoft Windows!\n");
#elif defined(XWIN)
  JAWT_X11DrawingSurfaceInfo* pdsi = 
    (JAWT_X11DrawingSurfaceInfo*)dsi->platformInfo;
  Display* display = pdsi->display;
  Drawable drawable = pdsi->drawable;
  GC gc = XCreateGC(display,drawable,0,0);
  XSetBackground(display,gc,0);
  for (int i=0; i<36; ++i) {
    XSetForeground(display,gc,10*i);
    XFillRectangle(display,drawable,gc,10*i,5,90,90);
  }
  XSetForeground(display,gc,155);
  const char* testString = "native canvas";
  XDrawImageString(display,drawable,gc,100,110,testString,strlen(testString));
  XFreeGC(display,gc);
#endif
  ds->FreeDrawingSurfaceInfo(dsi);
  ds->Unlock(ds);
  awt.FreeDrawingSurface(ds);
  JNI_CATCH
}
