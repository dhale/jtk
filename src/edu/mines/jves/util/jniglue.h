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

// Throw a Java Exception with the specified name.
inline static void throwException(
  JNIEnv* env, const char* exceptionClassName, const char* message) 
{
  env->ThrowNew(env->FindClass(exceptionClassName),message);
}

// Throw a Java RuntimeException with the specified name.
inline static void throwRuntimeException(JNIEnv* env, const char* message) {
  throwException(env,"java/lang/RuntimeException",message); 
}

// Macros that try and catch C++ exceptions thrown by native code.
#define JNI_TRY try {
#define JNI_CATCH \
} catch (...) { \
  throwRuntimeException(env,"C++ exception thrown by native code"); \
}
#define JNI_CATCH_RETURN(retval) \
} catch (...) { \
  throwRuntimeException(env,"C++ exception thrown by native code"); \
  return retval; \
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
