#ifdef WIN32
#include <windows.h>
#endif
#include <GL/gl.h>
#include <GL/glext.h>
#include "../util/jniglue.h"

extern "C" JNIEXPORT void JNICALL
Java_edu_mines_jves_opengl_Gl_glFlush(
  JNIEnv* env, jclass cls)
{
  glFlush();
}

/////////////////////////////////////////////////////////////////////////////
// OpenGL 1.2

extern "C" JNIEXPORT void JNICALL
Java_edu_mines_jves_opengl_Gl_nglBlendColor(
  JNIEnv* env, jclass cls, jlong glBlendColor,
  jfloat red, jfloat green, jfloat blue, jfloat alpha)
{
  (*(PFNGLBLENDCOLORPROC)toPointer(glBlendColor))
    (red,green,blue,alpha);
}
