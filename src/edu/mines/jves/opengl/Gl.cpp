/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/

/****************************************************************************
Gl JNI glue.
@author Dave Hale, Colorado School of Mines
****************************************************************************/
#ifdef WIN32
#include <windows.h>
#endif
#include <GL/gl.h>
#include <GL/glext.h>
#include "../util/jniglue.h"

#define JNI_GL_DECLARE0(name) extern "C" JNIEXPORT void JNICALL \
Java_edu_mines_jves_opengl_Gl_##name(\
  JNIEnv* env, jclass cls)
#define JNI_GL_DECLARE1(name) extern "C" JNIEXPORT void JNICALL \
Java_edu_mines_jves_opengl_Gl_##name(\
  JNIEnv* env, jclass cls,
#define JNI_GL_DECLARE2(name) extern "C" JNIEXPORT void JNICALL \
Java_edu_mines_jves_opengl_Gl_n##name(\
  JNIEnv* env, jclass cls, jlong name,
#define JNI_GL_BEGIN0 {\
  JNI_TRY
#define JNI_GL_BEGIN1 ) {\
  JNI_TRY
#define JNI_GL_BEGIN2 ) {\
  JNI_TRY
#define JNI_GL_END JNI_CATCH \
}

JNI_GL_DECLARE0(glFlush)
JNI_GL_BEGIN0
  glFlush();
JNI_GL_END
/*
extern "C" JNIEXPORT void JNICALL
Java_edu_mines_jves_opengl_Gl_glFlush(
  JNIEnv* env, jclass cls)
{
  JNI_TRY
  glFlush();
  JNI_CATCH
}
*/

/////////////////////////////////////////////////////////////////////////////
// OpenGL 1.2

JNI_GL_DECLARE2(glBlendColor)
  jfloat red, jfloat green, jfloat blue, jfloat alpha
JNI_GL_BEGIN2
  (*(PFNGLBLENDCOLORPROC)toPointer(glBlendColor))
    (red,green,blue,alpha);
JNI_GL_END
/*
extern "C" JNIEXPORT void JNICALL
Java_edu_mines_jves_opengl_Gl_nglBlendColor(
  JNIEnv* env, jclass cls, jlong glBlendColor,
  jfloat red, jfloat green, jfloat blue, jfloat alpha)
{
  JNI_TRY
  (*(PFNGLBLENDCOLORPROC)toPointer(glBlendColor))
    (red,green,blue,alpha);
  JNI_CATCH
}
*/
