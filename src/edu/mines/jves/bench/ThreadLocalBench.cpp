#include <stdio.h>
#include "jni.h"

extern "C" JNIEXPORT void JNICALL
Java_edu_mines_jves_bench_ThreadLocalBench_nativeMethod(
  JNIEnv* env, jclass cls,
  jint i)
{
}

extern "C" JNIEXPORT void JNICALL
Java_edu_mines_jves_bench_ThreadLocalBench_nativeMethodWithContext(
  JNIEnv* env, jclass cls,
  jlong pointer, jint i)
{
}
