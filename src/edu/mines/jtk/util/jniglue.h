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
#ifdef _MSC_VER // If MSVC on Windows, ...
#include <stddef.h>
#elif __MINGW32__ // Else, if MinGW on Windows, ...
#include <stdint.h>
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
typedef Jarray<unsigned char> JbooleanArray;
typedef Jarray<signed char> JbyteArray;
typedef Jarray<unsigned char> JubyteArray;
typedef Jarray<unsigned short> JcharArray;
typedef Jarray<short> JshortArray;
typedef Jarray<unsigned short> JushortArray;
typedef Jarray<int> JintArray;
typedef Jarray<unsigned int> JuintArray;
typedef Jarray<float> JfloatArray;
typedef Jarray<double> JdoubleArray;
typedef Jarray<void> JvoidArray;

// Java array of arrays
template<typename T> class Jarray2 {
public:
  Jarray2(JNIEnv* env, jobjectArray arr) {
    _env = env;
    _arr = arr;
    int n2 = _env->GetArrayLength(_arr);
    _ptr = new T*[n2];
    for (int i2=0; i2<n2; ++i2) {
      jobject e2 = _env->GetObjectArrayElement(_arr,i2);
      _ptr[i2] = (T*)_env->GetPrimitiveArrayCritical((jarray)e2,0);
      _env->DeleteLocalRef(e2);
    }
  }
  ~Jarray2() {
    int n2 = _env->GetArrayLength(_arr);
    for (int i2=0; i2<n2; ++i2) {
      jobject e2 = _env->GetObjectArrayElement(_arr,i2);
      _env->ReleasePrimitiveArrayCritical((jarray)e2,_ptr[i2],0);
      _env->DeleteLocalRef(e2);
    }
    delete[] _ptr;
  }
  operator const T* const*() const {
    return (const T* const*)_ptr;
  }
  operator T* const*() const {
    return (T* const*)_ptr;
  }
  operator const T**() const {
    return (const T**)_ptr;
  }
  operator T**() const {
    return _ptr;
  }
private:
  JNIEnv* _env;
  jobjectArray _arr;
  T** _ptr;
};
typedef Jarray2<unsigned char> JbooleanArray2;
typedef Jarray2<signed char> JbyteArray2;
typedef Jarray2<unsigned char> JubyteArray2;
typedef Jarray2<unsigned short> JcharArray2;
typedef Jarray2<short> JshortArray2;
typedef Jarray2<unsigned short> JushortArray2;
typedef Jarray2<int> JintArray2;
typedef Jarray2<unsigned int> JuintArray2;
typedef Jarray2<float> JfloatArray2;
typedef Jarray2<double> JdoubleArray2;
typedef Jarray2<void> JvoidArray2; 

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
typedef Jbuffer<unsigned char> JbooleanBuffer;
typedef Jbuffer<signed char> JbyteBuffer;
typedef Jbuffer<unsigned char> JubyteBuffer;
typedef Jbuffer<unsigned short> JcharBuffer;
typedef Jbuffer<short> JshortBuffer;
typedef Jbuffer<unsigned short> JushortBuffer;
typedef Jbuffer<int> JintBuffer;
typedef Jbuffer<unsigned int> JuintBuffer;
typedef Jbuffer<float> JfloatBuffer;
typedef Jbuffer<double> JdoubleBuffer;
typedef Jbuffer<void> JvoidBuffer;
