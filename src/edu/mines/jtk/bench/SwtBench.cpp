/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/

/****************************************************************************
SwtBench JNI glue
@author Dave Hale, Colorado School of Mines
****************************************************************************/
#include <stdio.h>
#ifdef WIN32
#include <windows.h>
#endif
#include <GL/gl.h>
#include "../util/jniglue.h"

extern "C" JNIEXPORT void JNICALL
Java_edu_mines_jves_bench_SwtBench_printHandleNative(
  JNIEnv* env, jclass cls,
  jint handle)
{
  JNI_TRY
  printf("native method: handle=%d\n",handle);
  char* version = (char*)glGetString(GL_VERSION);
  if (version==0) {
    printf("Cannot yet get OpenGL version.\n");
  } else {
    printf("OpenGL version=%s\n",version);
  }
  JNI_CATCH
}
