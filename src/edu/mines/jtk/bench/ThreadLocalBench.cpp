/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/

/****************************************************************************
ThreadLocalBench JNI glue
@author Dave Hale, Colorado School of Mines
****************************************************************************/
#include "../util/jniglue.h"

extern "C" JNIEXPORT void JNICALL
Java_edu_mines_jtk_bench_ThreadLocalBench_nativeMethod(
  JNIEnv* env, jclass cls,
  jint i)
{
  JNI_TRY
  JNI_CATCH
}

extern "C" JNIEXPORT void JNICALL
Java_edu_mines_jtk_bench_ThreadLocalBench_nativeMethodWithContext(
  JNIEnv* env, jclass cls,
  jlong pointer, jint i)
{
  JNI_TRY
  JNI_CATCH
}
