/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/

/****************************************************************************
Glu JNI glue.
@author Dave Hale, Colorado School of Mines
****************************************************************************/
#ifdef WIN32
#include <windows.h>
#endif
#include <GL/glu.h>
#include "../util/jniglue.h"

inline static GLboolean fromJava(jboolean b) {
  return (b==JNI_TRUE)?GLU_TRUE:GLU_FALSE;
}

inline static jboolean toJava(GLboolean b) {
  return (b==GLU_TRUE)?JNI_TRUE:JNI_FALSE;
}

#define JNI_GLU_DECLARE0(name) extern "C" JNIEXPORT void JNICALL \
Java_edu_mines_jtk_opengl_Glu_##name(\
  JNIEnv* env, jclass cls)
#define JNI_GLU_DECLARE1(name) extern "C" JNIEXPORT void JNICALL \
Java_edu_mines_jtk_opengl_Glu_##name(\
  JNIEnv* env, jclass cls,
#define JNI_GLU_DECLARE_RETURN0(type,name) extern "C" JNIEXPORT type JNICALL \
Java_edu_mines_jtk_opengl_Glu_##name(\
  JNIEnv* env, jclass cls)
#define JNI_GLU_DECLARE_RETURN1(type,name) extern "C" JNIEXPORT type JNICALL \
Java_edu_mines_jtk_opengl_Glu_##name(\
  JNIEnv* env, jclass cls,
#define JNI_GLU_BEGIN0 {\
  JNI_TRY
#define JNI_GLU_BEGIN1 ) {\
  JNI_TRY
#define JNI_GLU_END JNI_CATCH \
}

JNI_GLU_DECLARE_RETURN1(jint,gluBuild1DMipmaps__IIIII_3B)
  jint target, jint internalFormat, jint width, 
  jint format, jint type, jarray jdata
JNI_GLU_BEGIN1
  JvoidArray data(env,jdata);
  return gluBuild1DMipmaps(
    target,internalFormat,width,
    format,type,data);
JNI_GLU_END

JNI_GLU_DECLARE_RETURN1(jint,gluBuild1DMipmaps__IIIII_3I)
  jint target, jint internalFormat, jint width, 
  jint format, jint type, jarray jdata
JNI_GLU_BEGIN1
  JvoidArray data(env,jdata);
  return gluBuild1DMipmaps(
    target,internalFormat,width,
    format,type,data);
JNI_GLU_END

JNI_GLU_DECLARE_RETURN1(jint,gluBuild1DMipmaps__IIIII_3S)
  jint target, jint internalFormat, jint width, 
  jint format, jint type, jarray jdata
JNI_GLU_BEGIN1
  JvoidArray data(env,jdata);
  return gluBuild1DMipmaps(
    target,internalFormat,width,
    format,type,data);
JNI_GLU_END

JNI_GLU_DECLARE_RETURN1(jint,gluBuild2DMipmaps__IIIIII_3B)
  jint target, jint internalFormat, jint width, jint height, 
  jint format, jint type, jarray jdata
JNI_GLU_BEGIN1
  JvoidArray data(env,jdata);
  return gluBuild2DMipmaps(
    target,internalFormat,width,height,
    format,type,data);
JNI_GLU_END

JNI_GLU_DECLARE_RETURN1(jint,gluBuild2DMipmaps__IIIIII_3I)
  jint target, jint internalFormat, jint width, jint height, 
  jint format, jint type, jarray jdata
JNI_GLU_BEGIN1
  JvoidArray data(env,jdata);
  return gluBuild2DMipmaps(
    target,internalFormat,width,height,
    format,type,data);
JNI_GLU_END

JNI_GLU_DECLARE_RETURN1(jint,gluBuild2DMipmaps__IIIIII_3S)
  jint target, jint internalFormat, jint width, jint height, 
  jint format, jint type, jarray jdata
JNI_GLU_BEGIN1
  JvoidArray data(env,jdata);
  return gluBuild2DMipmaps(
    target,internalFormat,width,height,
    format,type,data);
JNI_GLU_END

JNI_GLU_DECLARE_RETURN1(jstring,gluGetString)
  jint name
JNI_GLU_BEGIN1
  return env->NewStringUTF((const char*)gluGetString(name));
JNI_GLU_END

JNI_GLU_DECLARE1(gluLookAt)
  jdouble eyeX, jdouble eyeY, jdouble eyeZ,
  jdouble centerX, jdouble centerY, jdouble centerZ, 
  jdouble upX, jdouble upY, jdouble upZ
JNI_GLU_BEGIN1
  gluLookAt(eyeX,eyeY,eyeZ,centerX,centerY,centerZ,upX,upY,upZ);
JNI_GLU_END

JNI_GLU_DECLARE1(gluOrtho2D)
  jdouble left, jdouble right, jdouble bottom, jdouble top
JNI_GLU_BEGIN1
  gluOrtho2D(left,right,bottom,top);
JNI_GLU_END

JNI_GLU_DECLARE1(gluPerspective)
  jdouble fovy, jdouble aspect, jdouble zNear, jdouble zFar
JNI_GLU_BEGIN1
  gluPerspective(fovy,aspect,zNear,zFar);
JNI_GLU_END

JNI_GLU_DECLARE1(gluPickMatrix)
  jdouble x, jdouble y, jdouble delX, jdouble delY, jintArray jviewport
JNI_GLU_BEGIN1
  JintArray viewport(env,jviewport);
  gluPickMatrix(x,y,delX,delY,viewport);
JNI_GLU_END

JNI_GLU_DECLARE_RETURN1(jint,gluProject)
  jdouble objX, jdouble objY, jdouble objZ, 
  jdoubleArray jmodel, jdoubleArray jproj, jintArray jview, 
  jdoubleArray jwinX, jdoubleArray jwinY, jdoubleArray jwinZ
JNI_GLU_BEGIN1
  JdoubleArray model(env,jmodel);
  JdoubleArray proj(env,jproj);
  JintArray view(env,jview);
  JdoubleArray winX(env,jwinX);
  JdoubleArray winY(env,jwinY);
  JdoubleArray winZ(env,jwinZ);
  return gluProject(objX,objY,objZ,model,proj,view,winX,winY,winZ);
JNI_GLU_END

JNI_GLU_DECLARE_RETURN1(jint,gluScaleImage__IIII_3BIII_3B)
  jint format, 
  jint wIn, jint hIn, jint typeIn, jarray jdataIn, 
  jint wOut, jint hOut, jint typeOut, jarray jdataOut
JNI_GLU_BEGIN1
  JvoidArray dataIn(env,jdataIn);
  JvoidArray dataOut(env,jdataOut);
  return gluScaleImage(
    format,
    wIn,hIn,typeIn,dataIn,
    wOut,hOut,typeOut,dataOut);
JNI_GLU_END

JNI_GLU_DECLARE_RETURN1(jint,gluScaleImage__IIII_3BIII_3I)
  jint format, 
  jint wIn, jint hIn, jint typeIn, jarray jdataIn, 
  jint wOut, jint hOut, jint typeOut, jarray jdataOut
JNI_GLU_BEGIN1
  JvoidArray dataIn(env,jdataIn);
  JvoidArray dataOut(env,jdataOut);
  return gluScaleImage(
    format,
    wIn,hIn,typeIn,dataIn,
    wOut,hOut,typeOut,dataOut);
JNI_GLU_END

JNI_GLU_DECLARE_RETURN1(jint,gluScaleImage__IIII_3BIII_3S)
  jint format, 
  jint wIn, jint hIn, jint typeIn, jarray jdataIn, 
  jint wOut, jint hOut, jint typeOut, jarray jdataOut
JNI_GLU_BEGIN1
  JvoidArray dataIn(env,jdataIn);
  JvoidArray dataOut(env,jdataOut);
  return gluScaleImage(
    format,
    wIn,hIn,typeIn,dataIn,
    wOut,hOut,typeOut,dataOut);
JNI_GLU_END

JNI_GLU_DECLARE_RETURN1(jint,gluUnProject)
  jdouble winX, jdouble winY, jdouble winZ, 
  jdoubleArray jmodel, jdoubleArray jproj, jintArray jview, 
  jdoubleArray jobjX, jdoubleArray jobjY, jdoubleArray jobjZ
JNI_GLU_BEGIN1
  JdoubleArray model(env,jmodel);
  JdoubleArray proj(env,jproj);
  JintArray view(env,jview);
  JdoubleArray objX(env,jobjX);
  JdoubleArray objY(env,jobjY);
  JdoubleArray objZ(env,jobjZ);
  return gluUnProject(winX,winY,winZ,model,proj,view,objX,objY,objZ);
JNI_GLU_END

/* GLU version 1.3
JNI_GLU_DECLARE_RETURN1(jint,gluBuild1DMipmapLevels__IIIIIIII_3B)
  jint target, jint internalFormat, jint width, 
  jint format, jint type, jint level, jint base, jint max, jarray jdata
JNI_GLU_BEGIN1
  JvoidArray data(env,jdata);
  return gluBuild1DMipmapLevels(
    target,internalFormat,width,
    format,type,level,base,max,data);
JNI_GLU_END

JNI_GLU_DECLARE_RETURN1(jint,gluBuild1DMipmapLevels__IIIIIIII_3I)
  jint target, jint internalFormat, jint width, 
  jint format, jint type, jint level, jint base, jint max, jarray jdata
JNI_GLU_BEGIN1
  JvoidArray data(env,jdata);
  return gluBuild1DMipmapLevels(
    target,internalFormat,width,
    format,type,level,base,max,data);
JNI_GLU_END

JNI_GLU_DECLARE_RETURN1(jint,gluBuild1DMipmapLevels__IIIIIIII_3S)
  jint target, jint internalFormat, jint width, 
  jint format, jint type, jint level, jint base, jint max, jarray jdata
JNI_GLU_BEGIN1
  JvoidArray data(env,jdata);
  return gluBuild1DMipmapLevels(
    target,internalFormat,width,
    format,type,level,base,max,data);
JNI_GLU_END

JNI_GLU_DECLARE_RETURN1(jint,gluBuild2DMipmapLevels__IIIIIIIII_3B)
  jint target, jint internalFormat, jint width, jint height,
  jint format, jint type, jint level, jint base, jint max, jarray jdata
JNI_GLU_BEGIN1
  JvoidArray data(env,jdata);
  return gluBuild2DMipmapLevels(
    target,internalFormat,width,height,
    format,type,level,base,max,data);
JNI_GLU_END

JNI_GLU_DECLARE_RETURN1(jint,gluBuild2DMipmapLevels__IIIIIIIII_3I)
  jint target, jint internalFormat, jint width, jint height,
  jint format, jint type, jint level, jint base, jint max, jarray jdata
JNI_GLU_BEGIN1
  JvoidArray data(env,jdata);
  return gluBuild2DMipmapLevels(
    target,internalFormat,width,height,
    format,type,level,base,max,data);
JNI_GLU_END

JNI_GLU_DECLARE_RETURN1(jint,gluBuild2DMipmapLevels__IIIIIIIII_3S)
  jint target, jint internalFormat, jint width, jint height,
  jint format, jint type, jint level, jint base, jint max, jarray jdata
JNI_GLU_BEGIN1
  JvoidArray data(env,jdata);
  return gluBuild2DMipmapLevels(
    target,internalFormat,width,height,
    format,type,level,base,max,data);
JNI_GLU_END

JNI_GLU_DECLARE_RETURN1(jint,gluBuild3DMipmapLevels__IIIIIIIIII_3B)
  jint target, jint internalFormat, jint width, jint height, jint depth,
  jint format, jint type, jint level, jint base, jint max, jarray jdata
JNI_GLU_BEGIN1
  JvoidArray data(env,jdata);
  return gluBuild3DMipmapLevels(
    target,internalFormat,width,height,depth,
    format,type,level,base,max,data);
JNI_GLU_END

JNI_GLU_DECLARE_RETURN1(jint,gluBuild3DMipmapLevels__IIIIIIIIII_3I)
  jint target, jint internalFormat, jint width, jint height, jint depth,
  jint format, jint type, jint level, jint base, jint max, jarray jdata
JNI_GLU_BEGIN1
  JvoidArray data(env,jdata);
  return gluBuild3DMipmapLevels(
    target,internalFormat,width,height,depth,
    format,type,level,base,max,data);
JNI_GLU_END

JNI_GLU_DECLARE_RETURN1(jint,gluBuild3DMipmapLevels__IIIIIIIIII_3S)
  jint target, jint internalFormat, jint width, jint height, jint depth,
  jint format, jint type, jint level, jint base, jint max, jarray jdata
JNI_GLU_BEGIN1
  JvoidArray data(env,jdata);
  return gluBuild3DMipmapLevels(
    target,internalFormat,width,height,depth,
    format,type,level,base,max,data);
JNI_GLU_END

JNI_GLU_DECLARE_RETURN1(jint,gluBuild3DMipmaps__IIIIIII_3B)
  jint target, jint internalFormat, jint width, jint height, jint depth,
  jint format, jint type, jarray jdata
JNI_GLU_BEGIN1
  JvoidArray data(env,jdata);
  return gluBuild3DMipmaps(
    target,internalFormat,width,height,depth,
    format,type,data);
JNI_GLU_END

JNI_GLU_DECLARE_RETURN1(jint,gluBuild3DMipmaps__IIIIIII_3I)
  jint target, jint internalFormat, jint width, jint height, jint depth,
  jint format, jint type, jarray jdata
JNI_GLU_BEGIN1
  JvoidArray data(env,jdata);
  return gluBuild3DMipmaps(
    target,internalFormat,width,height,depth,
    format,type,data);
JNI_GLU_END

JNI_GLU_DECLARE_RETURN1(jint,gluBuild3DMipmaps__IIIIIII_3S)
  jint target, jint internalFormat, jint width, jint height, jint depth,
  jint format, jint type, jarray jdata
JNI_GLU_BEGIN1
  JvoidArray data(env,jdata);
  return gluBuild3DMipmaps(
    target,internalFormat,width,height,depth,
    format,type,data);
JNI_GLU_END

JNI_GLU_DECLARE_RETURN1(jboolean,gluCheckExtension)
  jstring jextName, jstring jextString
JNI_GLU_BEGIN1
  Jstring extName(env,jextName);
  Jstring extString(env,jextString);
  return toJava(gluCheckExtension(extName,extString));
JNI_GLU_END

JNI_GLU_DECLARE_RETURN1(jint,gluUnProject4)
  jdouble winX, jdouble winY, jdouble winZ, jdouble clipW,
  jdoubleArray jmodel, jdoubleArray jproj, jintArray jview, 
  jdouble nearVal, jdouble farVal,
  jdoubleArray jobjX, jdoubleArray jobjY, 
  jdoubleArray jobjZ, jdoubleArray jobjW
JNI_GLU_BEGIN1
  JdoubleArray model(env,jmodel);
  JdoubleArray proj(env,jproj);
  JintArray view(env,jview);
  JdoubleArray objX(env,jobjX);
  JdoubleArray objY(env,jobjY);
  JdoubleArray objZ(env,jobjZ);
  JdoubleArray objW(env,jobjW);
  return gluUnProject4(
    winX,winY,winZ,clipW,
    model,proj,view,
    nearVal,farVal,
    objX,objY,objZ,objW);
JNI_GLU_END
*/
