#include <stdio.h>
#include "jni.h"

extern "C" JNIEXPORT void JNICALL
Java_edu_mines_jves_bench_TlBench_foo(
  JNIEnv* env, jclass cls,
  jint i)
{
}

extern "C" JNIEXPORT void JNICALL
Java_edu_mines_jves_bench_TlBench_bar(
  JNIEnv* env, jclass cls,
  jlong pointer, jint i)
{
}
