/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.opengl;

/**
 * OpenGL utility constants and methods.
 * <p>
 * <em>
 * Does not yet include constants or methods for NURBs, tesselators 
 * or quadrics.
 * </em>
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.12.20
 */
public class Glu {

  ///////////////////////////////////////////////////////////////////////////
  // constants

  public static final int GLU_FALSE                                   =0;
  public static final int GLU_TRUE                                    =1;

  public static final int GLU_VERSION_1_1                             =1;
  public static final int GLU_VERSION_1_2                             =1;
  public static final int GLU_VERSION_1_3                             =1;

  public static final int GLU_VERSION                            =100800;
  public static final int GLU_EXTENSIONS                         =100801;

  public static final int GLU_INVALID_ENUM                       =100900;
  public static final int GLU_INVALID_VALUE                      =100901;
  public static final int GLU_OUT_OF_MEMORY                      =100902;
  public static final int GLU_INVALID_OPERATION                  =100904;

  ///////////////////////////////////////////////////////////////////////////
  // methods

  public static native int gluBuild1DMipmaps(
    int target, int internalFormat, int width, 
    int format, int type, byte[] data);
  public static native int gluBuild1DMipmaps(
    int target, int internalFormat, int width, 
    int format, int type, short[] data);
  public static native int gluBuild1DMipmaps(
    int target, int internalFormat, int width, 
    int format, int type, int[] data);
  public static native int gluBuild2DMipmaps(
    int target, int internalFormat, int width, int height, 
    int format, int type, byte[] data);
  public static native int gluBuild2DMipmaps(
    int target, int internalFormat, int width, int height, 
    int format, int type, short[] data);
  public static native int gluBuild2DMipmaps(
    int target, int internalFormat, int width, int height, 
    int format, int type, int[] data);
  public static native String gluGetString(int name);
  public static native void gluLookAt(
    double eyeX, double eyeY, double eyeZ,
    double centerX, double centerY, double centerZ, 
    double upX, double upY, double upZ);
  public static native void gluOrtho2D(
    double left, double right, double bottom, double top);
  public static native void gluPerspective(
    double fovy, double aspect, double zNear, double zFar);
  public static native void gluPickMatrix(
    double x, double y, double delX, double delY, int[] viewport);
  public static native int gluProject(
    double objX, double objY, double objZ, double[] model, double[] proj,
    int[] view, double[] winX, double[] winY, double[] winZ);
  public static native int gluScaleImage(
    int format, 
    int wIn, int hIn, int typeIn, byte[] dataIn, 
    int wOut, int hOut, int typeOut, byte[] dataOut);
  public static native int gluScaleImage(
    int format, 
    int wIn, int hIn, int typeIn, short[] dataIn, 
    int wOut, int hOut, int typeOut, short[] dataOut);
  public static native int gluScaleImage(
    int format, 
    int wIn, int hIn, int typeIn, int[] dataIn, 
    int wOut, int hOut, int typeOut, int[] dataOut);
  public static native int gluUnProject(
    double winX, double winY, double winZ, 
    double[] model, double[] proj, int[] view, 
    double[] objX, double[] objY, double[] objZ);

  /* GLU version 1.3
  public static native int gluBuild1DMipmapLevels(
    int target, int internalFormat, int width, 
    int format, int type, int level, int base, int max, byte[] data);
  public static native int gluBuild1DMipmapLevels(
    int target, int internalFormat, int width, 
    int format, int type, int level, int base, int max, short[] data);
  public static native int gluBuild1DMipmapLevels(
    int target, int internalFormat, int width, 
    int format, int type, int level, int base, int max, int[] data);
  public static native int gluBuild2DMipmapLevels(
    int target, int internalFormat, int width, int height, 
    int format, int type, int level, int base, int max, byte[] data);
  public static native int gluBuild2DMipmapLevels(
    int target, int internalFormat, int width, int height, 
    int format, int type, int level, int base, int max, short[] data);
  public static native int gluBuild2DMipmapLevels(
    int target, int internalFormat, int width, int height, 
    int format, int type, int level, int base, int max, int[] data);
  public static native int gluBuild3DMipmapLevels(
    int target, int internalFormat, int width, int height, int depth, 
    int format, int type, int level, int base, int max, byte[] data);
  public static native int gluBuild3DMipmapLevels(
    int target, int internalFormat, int width, int height, int depth, 
    int format, int type, int level, int base, int max, short[] data);
  public static native int gluBuild3DMipmapLevels(
    int target, int internalFormat, int width, int height, int depth, 
    int format, int type, int level, int base, int max, int[] data);
  public static native int gluBuild3DMipmaps(
    int target, int internalFormat, int width, int height, int depth, 
    int format, int type, byte[] data);
  public static native int gluBuild3DMipmaps(
    int target, int internalFormat, int width, int height, int depth, 
    int format, int type, short[] data);
  public static native int gluBuild3DMipmaps(
    int target, int internalFormat, int width, int height, int depth, 
    int format, int type, int[] data);
  public static native boolean gluCheckExtension(
    String extName, String extString);
  public static int gluUnProject4(
    double winX, double winY, double winZ, double clipW, 
    double[] model, double[] proj, int[] view, double near, double far, 
    double[] objX, double[] objY, double[] objZ, double[] objW) 
  {
    return ngluUnProject4(getContext().gluUnProject4,
      winX,winY,winZ,clipW,model,proj,view,near,far,objX,objY,objZ,objW);
  }
  private static int ngluUnProject4(long pfunc,
    double winX, double winY, double winZ, double clipW, 
    double[] model, double[] proj, int[] view, double near, double far, 
    double[] objX, double[] objY, double[] objZ, double[] objW);
  */

  ///////////////////////////////////////////////////////////////////////////
  // package

  static GlContext getContext() {
    return _context.get();
  }

  static void setContext(GlContext context) {
    _context.set(context);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private static ThreadLocal<GlContext> _context = 
    new ThreadLocal<GlContext>();

  private Glu() {
  }

  static {
    System.loadLibrary("edu_mines_jtk_opengl");
  }
}

