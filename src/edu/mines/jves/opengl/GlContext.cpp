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
#include "../util/jniglue.h"


extern "C" JNIEXPORT void JNICALL
Java_edu_mines_jves_opengl_Gl_nswapBuffers(
  JNIEnv* env, jclass cls,
  jlong display, jlong handle)
{
  JNI_TRY
#if defined(MWIN)
  SwapBuffers((HDC)toPointer(handle));
#elif defined(XWIN)
  glXSwapBuffers((Display*)toPointer(display),(Drawable)toPointer(handle));
#endif
  JNI_CATCH
}

extern "C" JNIEXPORT jlong JNICALL
Java_edu_mines_jves_opengl_Gl_getProcAddress(
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
