/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/

/****************************************************************************
JNI glue utilities.
@author Dave Hale, Colorado School of Mines
****************************************************************************/
#ifdef WIN32 // If Microsoft Windows, ...
#include <stddef.h>
#else // Else, assume Linux, Unix, ...
#include <unistd.h>
#endif
#include <jni.h>

// Avoid compiler warnings, but work on both 32-bit and 64-bit platforms.
inline static void* toPointer(jlong plong) {
  return (void*)((intptr_t)plong);
}
inline static jlong fromPointer(void* pvoid) {
  return (jlong)((intptr_t)pvoid);
}

// Throw a Java Error with the specified message.
inline static void throwJavaError(JNIEnv* env, const char* message) {
  env->ThrowNew(env->FindClass("java/lang/Error"),message);
}

// Throw a Java RuntimeException with the specified name.
inline static void throwJavaRuntimeException(JNIEnv* env, const char* message) {
  env->ThrowNew(env->FindClass("java/lang/RuntimeException"),message);
}

// Macros that try and catch C++ exceptions thrown by native code.
// Bracket all native code with JNI_TRY and JNI_CATCH. Then, if
// an unhandled C++ exception is thrown, the JNI_CATCH macro will
// throw a Java Error, which should be not be caught. But, just in
// case it is caught, this macro rethrows the C++ exception. Also,
// if we did not rethrow the C++ exception, then compilers will warn
// that functions declared to return a value do not do so.
#define JNI_TRY try {
#define JNI_CATCH \
} catch (...) { \
  throwJavaError(env,"C++ exception thrown by native code"); \
  throw; \
}

// Java string
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

// Java array
template<typename T> class Jarray {
public:
  Jarray(JNIEnv* env, jarray arr) {
    _env = env;
    _arr = arr;
    _ptr = (T*)env->GetPrimitiveArrayCritical(arr,0);
  }
  ~Jarray() {
    _env->ReleasePrimitiveArrayCritical(_arr,_ptr,0);
  }
  operator const T*() const {
    return _ptr;
  }
  operator T*() const {
    return _ptr;
  }
private:
  JNIEnv* _env;
  jarray _arr;
  T* _ptr;
};
typedef Jarray<jboolean> JbooleanArray;
typedef Jarray<jbyte> JbyteArray;
typedef Jarray<unsigned char> JubyteArray;
typedef Jarray<jchar> JcharArray;
typedef Jarray<jshort> JshortArray;
typedef Jarray<unsigned short> JushortArray;
#ifdef WIN32
// On Windows, jint is declared as a long, so we must provide a
// conversion operator to int*.
class JintArray : public Jarray<jint> {
public:
  JintArray(JNIEnv* env, jarray arr) : Jarray<jint>(env,arr) {}
  operator const int*() const {
    return (const int*)(this->operator const jint*());
  }
  operator int*() const {
    return (int*)(this->operator jint*());
  }
};
#else
typedef Jarray<jint> JintArray;
#endif /* WIN32 */
typedef Jarray<unsigned int> JuintArray;
typedef Jarray<jfloat> JfloatArray;
typedef Jarray<jdouble> JdoubleArray;
typedef Jarray<void> JvoidArray;

// Java NIO buffer
template<typename T> class Jbuffer {
public:
  Jbuffer(JNIEnv* env, jobject buf) {
    _ptr = (T*)env->GetDirectBufferAddress(buf);
    // TODO: check pointer; will be zero if not a direct buffer
    //if (_ptr==0)
    //  throw ???
  }
  operator const T*() const {
    return _ptr;
  }
  operator T*() const {
    return _ptr;
  }
private:
  T* _ptr;
};
typedef Jbuffer<jboolean> JbooleanBuffer;
typedef Jbuffer<jbyte> JbyteBuffer;
typedef Jbuffer<unsigned char> JubyteBuffer;
typedef Jbuffer<jchar> JcharBuffer;
typedef Jbuffer<jshort> JshortBuffer;
typedef Jbuffer<unsigned short> JushortBuffer;
typedef Jbuffer<jint> JintBuffer;
typedef Jbuffer<unsigned int> JuintBuffer;
typedef Jbuffer<jfloat> JfloatBuffer;
typedef Jbuffer<jdouble> JdoubleBuffer;
typedef Jbuffer<void> JvoidBuffer;
