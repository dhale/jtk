#include <jni.h>

#ifdef WIN32
#define MWIN // Microsoft Windows
#define getProcAddress wglGetProcAddress
#include <windows.h>
#else
#define XWIN // X Windows
#define getProcAddress glXGetProcAddressARB
#endif

#include <GL/gl.h>

#ifdef XWIN
#include <GL/glx.h> 
#endif

class Jstring {
public:
  Jstring(JNIEnv* env, jstring str) {
    _env = env;
    _str = str;
    jboolean isCopy;
    _utf = env->GetStringUTFChars(str,&isCopy);
  }
  ~Jstring() {
    _env->ReleaseStringUTFChars(_str,_utf);
  }
  operator const char*() const {
    return _utf;
  }
  operator const unsigned char*() const {
    return (const unsigned char*)_utf;
  }
private:
  JNIEnv* _env;
  jstring _str;
  const char* _utf;
};

extern "C" JNIEXPORT void JNICALL
Java_edu_mines_jves_opengl_Gl_nswapBuffers(
  JNIEnv* env, jclass cls,
  jlong display, jlong handle)
{
#if defined(MWIN)
  SwapBuffers((HDC)handle);
#elif defined(XWIN)
  glXSwapBuffers((Display*)display,(Drawable)handle);
#endif
}

extern "C" JNIEXPORT jlong JNICALL
Java_edu_mines_jves_opengl_Gl_getProcAddress(
  JNIEnv* env, jclass cls,
  jstring jfunctionName)
{
  Jstring functionName(env,jfunctionName);
  return (jlong)getProcAddress(functionName);
}
