#include <stdio.h>
#include <jni.h>
#ifdef WIN32
#include <windows.h>
#endif
#include <GL/gl.h>

extern "C" JNIEXPORT void JNICALL
Java_edu_mines_jves_bench_SwtBench_printHandleNative(
  JNIEnv* env, jclass cls,
  jint handle)
{
  printf("native method: handle=%d\n",handle);
  char* version = (char*)glGetString(GL_VERSION);
  if (version==0) {
    printf("Cannot yet get OpenGL version.\n");
  } else {
    printf("OpenGL version=%s\n",version);
  }
}
