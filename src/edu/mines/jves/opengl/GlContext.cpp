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
#if defined(MWIN)
  SwapBuffers((HDC)toPointer(handle));
#elif defined(XWIN)
  glXSwapBuffers((Display*)toPointer(display),(Drawable)toPointer(handle));
#endif
}

extern "C" JNIEXPORT jlong JNICALL
Java_edu_mines_jves_opengl_Gl_getProcAddress(
  JNIEnv* env, jclass cls,
  jstring jfunctionName)
{
  Jstring functionName(env,jfunctionName);
#if defined(MWIN)
  void (*p)() = wglGetProcAddress(functionName);
#elif defined(XWIN)
  void (*p)() = glXGetProcAddressARB(functionName);
#endif
  return (jlong)(intptr_t)p;
}
