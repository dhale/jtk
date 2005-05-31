/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

/**
 * OpenGL state.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.05.31
 */
public interface State {

  /**
   * Applies this OpenGL state.
   * This method calls OpenGL functions that reflect the state.
   */
  public void apply();

  /**
   * Gets the OpenGL attribute bits for this state. 
   * These bits indicate what OpenGL state is changed by the method 
   * {@link #apply()}. If these bits are passed to glPushAttrib before 
   * calling {@link #apply()}, then glPopAttrib will restore any OpenGL 
   * state that is changed by that method.
   */
  public int getAttributeBits();
}

/*
BlendState
  setBlend(boolean blend)
    glEnable(GL_BLEND)
    GL_COLOR_BUFFER_BIT
    GL_ENABLE_BIT
  setBlendColor(Color color)
    glBlendColor
    GL_COLOR_BUFFER_BIT
  setBlendEquation(int mode)
    glBlendEquation
    GL_COLOR_BUFFER_BIT
  setBlendFunc(int sfactor, int dfactor)
    glBlendFunc
    GL_COLOR_BUFFER_BIT

ColorState
  setColor(Color color)
    glColor4ub
    GL_CURRENT_BIT
  setShadeModel(int mode)
    glShadeModel
    GL_LIGHTING_BIT

LineState
  setSmooth(boolean smooth)
    glEnable(GL_LINE_SMOOTH)
    GL_LINE_BIT
    GL_ENABLE_BIT
  setStipple(int factor, short pattern)
    glEnable(GL_LINE_STIPPLE)
    glLineStipple
    GL_LINE_BIT
    GL_ENABLE_BIT
  setWidth(float width)
    glLineWidth
    GL_LINE_BIT

PointState
  setSmooth(boolean smooth)
    glEnable(GL_POINT_SMOOTH)
    GL_POINT_BIT
    GL_ENABLE_BIT
  setSize(float size)
    glPointSize
    GL_POINT_BIT

PolygonState
  setCullFace(int mode)
    glEnable(GL_CULL_FACE)
    glCullFace
    GL_POLYGON_BIT
    GL_ENABLE_BIT
  setFrontFace(int mode)
    glFrontFace
    GL_POLYGON_BIT
  setPolygonMode(int face, int mode)
    glPolygonMode
    GL_POLYGON_BIT
  setPolygonOffset(float factor, float units)
    glPolygonOffset
    GL_POLYGON_BIT
  setPolygonOffsetFill(boolean fill)
    glEnable(GL_POLYGON_OFFSET_FILL)
    GL_POLYGON_BIT
    GL_ENABLE_BIT
  setPolygonOffsetLine(boolean line)
    glEnable(GL_POLYGON_OFFSET_LINE)
    GL_POLYGON_BIT
    GL_ENABLE_BIT
  setPolygonOffsetPoint(boolean point)
    glEnable(GL_POLYGON_OFFSET_POINT)
    GL_POLYGON_BIT
    GL_ENABLE_BIT
  setPolygonStipple(byte[] mask)
    glEnable(GL_POLYGON_STIPPLE)
    glPolygonStipple
    GL_POLYGON_BIT
    GL_ENABLE_BIT

LightModelState
  setAmbient(Color color)
    glLightMOdelfv
    glEnable(GL_LIGHTING)
    GL_LIGHTING_BIT
    GL_ENABLE_BIT
  setColorControl(int mode)
    glLightModeli(GL_LIGHT_MODEL_COLOR_CONTROL,mode)
    glEnable(GL_LIGHTING)
    GL_LIGHTING_BIT
    GL_ENABLE_BIT
  setTwoSide(boolean twoSide)
    glLightModeli(GL_LIGHT_MODEL_TWO_SIDE,(twoSide?1:0));
    glEnable(GL_LIGHTING)
    GL_LIGHTING_BIT
    GL_ENABLE_BIT
  setLocalViewer(boolean localViewer)
    glLightModeli(GL_LIGHT_MODEL_LOCAL_VIEWER,(localViewer?1:0));
    glEnable(GL_LIGHTING)
    GL_LIGHTING_BIT
    GL_ENABLE_BIT

MaterialState
  setAmbient(Color color)
    glMaterialfv(GL_FRONT_AND_BACK,GL_AMBIENT,...)
    glEnable(GL_LIGHTING)
    GL_LIGHTING_BIT
    GL_ENABLE_BIT
  setAmbientFront(Color color)
    glMaterialfv(GL_FRONT,GL_AMBIENT,...)
    glEnable(GL_LIGHTING)
    GL_LIGHTING_BIT
    GL_ENABLE_BIT
  setAmbientBack(Color color)
    glMaterialfv(GL_BACK,GL_AMBIENT,...)
    glEnable(GL_LIGHTING)
    GL_LIGHTING_BIT
    GL_ENABLE_BIT
  setDiffuse(Color color)
    glMaterialfv(GL_FRONT_AND_BACK,GL_DIFFUSE,...)
    glEnable(GL_LIGHTING)
    GL_LIGHTING_BIT
    GL_ENABLE_BIT
  setDiffuseFront(Color color)
    glMaterialfv(GL_FRONT,GL_DIFFUSE,...)
    glEnable(GL_LIGHTING)
    GL_LIGHTING_BIT
    GL_ENABLE_BIT
  setDiffuseBack(Color color)
    glMaterialfv(GL_BACK,GL_DIFFUSE,...)
    glEnable(GL_LIGHTING)
    GL_LIGHTING_BIT
    GL_ENABLE_BIT
  setSpecular(Color color)
    glMaterialfv(GL_FRONT_AND_BACK,GL_SPECULAR,...)
    glEnable(GL_LIGHTING)
    GL_LIGHTING_BIT
    GL_ENABLE_BIT
  setSpecularFront(Color color)
    glMaterialfv(GL_FRONT,GL_SPECULAR,...)
    glEnable(GL_LIGHTING)
    GL_LIGHTING_BIT
    GL_ENABLE_BIT
  setSpecularBack(Color color)
    glMaterialfv(GL_BACK,GL_SPECULAR,...)
    glEnable(GL_LIGHTING)
    GL_LIGHTING_BIT
    GL_ENABLE_BIT
  setEmissive(Color color)
    glMaterialfv(GL_FRONT_AND_BACK,GL_EMISSION,...)
    glEnable(GL_LIGHTING)
    GL_LIGHTING_BIT
    GL_ENABLE_BIT
  setEmissiveFront(Color color)
    glMaterialfv(GL_FRONT,GL_EMISSION,...)
    glEnable(GL_LIGHTING)
    GL_LIGHTING_BIT
    GL_ENABLE_BIT
  setEmissiveBack(Color color)
    glMaterialfv(GL_BACK,GL_EMISSION,...)
    glEnable(GL_LIGHTING)
    GL_LIGHTING_BIT
    GL_ENABLE_BIT
  setShininess(float exponent)
    glMaterialf(GL_FRONT_AND_BACK,GL_EMISSION,exponent)
    glEnable(GL_LIGHTING)
    GL_LIGHTING_BIT
    GL_ENABLE_BIT
  setShininessFront(float exponent)
    glMaterialf(GL_FRONT,GL_EMISSION,exponent)
    glEnable(GL_LIGHTING)
    GL_LIGHTING_BIT
    GL_ENABLE_BIT
  setShininessBack(float exponent)
    glMaterialf(GL_BACK,GL_EMISSION,exponent)
    glEnable(GL_LIGHTING)
    GL_LIGHTING_BIT
    GL_ENABLE_BIT
  setColorMaterial(int mode)
    glColorMaterial(GL_FRONT_AND_BACK,mode)
    glEnable(GL_LIGHTING)
    glEnable(GL_COLOR_MATERIAL)
    GL_LIGHTING_BIT
    GL_ENABLE_BIT
  setColorMaterialFront(int mode)
    glColorMaterial(GL_FRONT,mode)
    glEnable(GL_LIGHTING)
    glEnable(GL_COLOR_MATERIAL)
    GL_LIGHTING_BIT
    GL_ENABLE_BIT
  setColorMaterialBack(int mode)
    glColorMaterial(GL_BACK,mode)
    glEnable(GL_LIGHTING)
    glEnable(GL_COLOR_MATERIAL)
    GL_LIGHTING_BIT
    GL_ENABLE_BIT
*/
