//////////////////////////////////////////////////////////////////////////////
// Copyright (c) 2004, Colorado School of Mines and others.
// All rights reserved. This program and the accompanying materials
// are made available under the terms of the Common Public License v1.0
// which accompanies this distribution, and is available at
// http://www.eclipse.org/legal/cpl-v10.html
// Contributors:
//   Dave Hale, Colorado School of Mines
//////////////////////////////////////////////////////////////////////////////
#ifdef WIN32 // If Microsoft Windows, ...
#define MWIN
#include <windows.h>
#else // Else, assume X Windows, ...
#define XWIN
#include <GL/glx.h> 
#endif
#include <GL/gl.h>
#include "jawt_md.h"
#include "../util/jniglue.h"

// OpenGL context for AWT Canvas
class GlContext {
public:
  virtual jboolean lock(JNIEnv* env) = 0;
  virtual jboolean unlock(JNIEnv* env) = 0;
  virtual jboolean swapBuffers() = 0;
  virtual ~GlContext() {
  }
};

// OpenGL context for AWT Canvas
class GlAwtCanvasContext : public GlContext {
public:
  GlAwtCanvasContext(jobject canvas) : _canvas(canvas) {
  }
  virtual jboolean lock(JNIEnv* env) {
    _awt.version = JAWT_VERSION_1_3;
    if (JAWT_GetAWT(env,&_awt)==JNI_FALSE)
      return JNI_FALSE;
    _ds = _awt.GetDrawingSurface(env,_canvas);
    if (_ds==0)
      return JNI_FALSE;
    jint lock = _ds->Lock(_ds);
    if ((lock&JAWT_LOCK_ERROR)!=0) {
      _awt.FreeDrawingSurface(_ds);
      return JNI_FALSE;
    }
    _dsi = _ds->GetDrawingSurfaceInfo(_ds);
    if (_dsi==0) {
      _ds->Unlock(_ds);
      _awt.FreeDrawingSurface(_ds);
      return JNI_FALSE;
    }
    makeCurrent();
    return JNI_TRUE;
  }
  virtual jboolean unlock(JNIEnv* env) {
    // TODO: implement this!
    return JNI_FALSE;
  }
protected:
  jobject _canvas;
  JAWT _awt;
  JAWT_DrawingSurface* _ds;
  JAWT_DrawingSurfaceInfo* _dsi;
  virtual void makeCurrent() = 0;
};

// Platform-dependent OpenGL context for AWT Canvas
#if defined(MWIN)
class WglAwtCanvasContext : public GlAwtCanvasContext {
public:
  WglAwtCanvasContext(jobject canvas) : 
    GlAwtCanvasContext(canvas),_hglrc(0) 
  {
  }
  virtual void makeCurrent() {
    _dsi_win32 = (JAWT_Win32DrawingSurfaceInfo*)_dsi->platformInfo;
    _hwnd = _dsi_win32->hwnd;
    _hdc = _dsi_win32->hdc;
    if (_hglrc==0) {
      PIXELFORMATDESCRIPTOR pfd;
      ZeroMemory(&pfd,sizeof(pdf));
      pfd.nSize = sizeof(pfd);
      pfd.nVersion = 1;
      pfd.dwFlags = 
        PFD_DRAW_TO_WINDOW |
        PFD_SUPPORT_OPENGL |
        PFD_DOUBLEBUFFER;
      pfd.iPixelType = PFD_TYPE_RGBA;
      pfd.cColorBits = 16;
      pfd.cDepthBits = 16;
      pfd.iLayerType = PFD_MAIN_PLANE;
      int format = ChoosePixelFormat(_hdc,&pfd);
      SetPixelFormat(_hdc,format,&pfd);
      _hglrc = wglCreateContext(_hdc);
    }
    wglMakeCurrent(_hdc,_hglrc);
  }
  virtual jboolean swapBuffers() {
    SwapBuffers(_hdc);
    return JNI_TRUE;
  }
private:
  JAWT_Win32DrawingSurfaceInfo* _dsi_win32;
  HWND _hwnd;
  HDC _hdc;
  HGLRC _hglrc;
};
#elif defined(XWIN)
class GlxAwtCanvasContext : public GlAwtCanvasContext {
public:
  GlxAwtCanvasContext(jobject canvas) : 
    GlAwtCanvasContext(canvas),_context(0) 
  {
  }
  virtual void makeCurrent() {
    _dsi_x11 = (JAWT_X11DrawingSurfaceInfo*)_dsi->platformInfo;
    _display = _dsi_x11->display;
    _drawable = _dsi_x11->drawable;
    if (_context==0) {
      int config[] = {
        GLX_DOUBLEBUFFER,GLX_RGBA,
        GLX_DEPTH_SIZE,16,
        GLX_RED_SIZE,1,
        GLX_GREEN_SIZE,1,
        GLX_BLUE_SIZE,1,
        None};
      XVisualInfo* visualInfo = glXChooseVisual(
        _display,DefaultScreen(_display),config);
      _context = glXCreateContext(_display,visualInfo,0,GL_TRUE);
    }
    glXMakeCurrent(_display,_drawable,_context);
    XFlush(_display);
  }
  virtual jboolean swapBuffers() {
    glXSwapBuffers(_display,_drawable);
    return JNI_TRUE;
  }
private:
  JAWT_X11DrawingSurfaceInfo* _dsi_x11;
  Display* _display;
  Drawable _drawable;
  GLXContext _context;
};
#endif


/////////////////////////////////////////////////////////////////////////////
// native methods

extern "C" JNIEXPORT jlong JNICALL
Java_edu_mines_jves_opengl_GlContext_makeGlAwtCanvasContext(
  JNIEnv* env, jclass cls,
  jobject canvas) {
  JNI_TRY
#if defined(MWIN)
  GlContext* context = new WglAwtCanvasContext(canvas);
#elif defined(XWIN)
  GlContext* context = new GlxAwtCanvasContext(canvas);
#endif
  return fromPointer(context);
  JNI_CATCH
}

extern "C" JNIEXPORT jboolean JNICALL
Java_edu_mines_jves_opengl_GlContext_lock(
  JNIEnv* env, jclass cls,
  jlong peer)
{
  JNI_TRY
  GlContext* context = (GlContext*)toPointer(peer);
  return context->lock(env);
  JNI_CATCH
}

extern "C" JNIEXPORT jboolean JNICALL
Java_edu_mines_jves_opengl_GlContext_unlock(
  JNIEnv* env, jclass cls,
  jlong peer)
{
  JNI_TRY
  GlContext* context = (GlContext*)toPointer(peer);
  return context->unlock(env);
  JNI_CATCH
}

extern "C" JNIEXPORT jboolean JNICALL
Java_edu_mines_jves_opengl_GlContext_swapBuffers(
  JNIEnv* env, jclass cls,
  jlong peer)
{
  JNI_TRY
  GlContext* context = (GlContext*)toPointer(peer);
  return context->swapBuffers();
  JNI_CATCH
}

extern "C" JNIEXPORT jlong JNICALL
Java_edu_mines_jves_opengl_GlContext_getProcAddress(
  JNIEnv* env, jclass cls,
  jstring jfunctionName)
{
  JNI_TRY
  Jstring functionName(env,jfunctionName);
#if defined(MWIN)
  void (*p)() = wglGetProcAddress(functionName);
#elif defined(XWIN)
  void (*p)() = glXGetProcAddressARB(functionName);
#endif
  return (jlong)(intptr_t)p;
  JNI_CATCH
}
