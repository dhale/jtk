#include <stdio.h>
#include "jni.h"
#include "GL/gl.h"

extern "C" JNIEXPORT void JNICALL
Java_edu_mines_jves_bench_SwtBench_printHandle(
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
