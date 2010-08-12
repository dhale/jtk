/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import static edu.mines.jtk.ogl.Gl.*;

/**
 * OpenGL polygon state.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.05.31
 */
public class PolygonState implements State {

  /**
   * Constructs polygon state.
   */
  public PolygonState() {
  }

  /**
   * Determines whether cull face mode is set.
   * @return true, if set; false, otherwise.
   */
  public boolean hasCullFace() {
    return _cullFaceSet;
  }

  /**
   * Gets the cull face mode.
   * @return the cull face mode.
   */
  public int getCullFace() {
    return _cullFace;
  }

  /**
   * Sets the cull face mode.
   * @param mode the cull face mode.
   */
  public void setCullFace(int mode) {
    _cullFace = mode;
    _cullFaceSet = true;
  }

  /**
   * Unsets the cull face mode.  
   */
  public void unsetCullFace() {
    _cullFace = _cullFaceDefault;
    _cullFaceSet = false;
  }

  /**
   * Determines whether front face mode is set.
   * @return true, if set; false, otherwise.
   */
  public boolean hasFrontFace() {
    return _frontFaceSet;
  }

  /**
   * Gets the front face mode.
   * @return the front face mode.
   */
  public int getFrontFace() {
    return _frontFace;
  }

  /**
   * Sets the front face mode.
   * @param mode the front face mode.
   */
  public void setFrontFace(int mode) {
    _frontFace = mode;
    _frontFaceSet = true;
  }

  /**
   * Unsets the front face mode.  
   */
  public void unsetFrontFace() {
    _frontFace = _frontFaceDefault;
    _frontFaceSet = false;
  }

  /**
   * Determines whether polygon mode for front faces is set.
   * @return true, if set; false, otherwise.
   */
  public boolean hasPolygonModeFront() {
    return _polygonModeFrontSet;
  }

  /**
   * Determines whether polygon mode for back faces is set.
   * @return true, if set; false, otherwise.
   */
  public boolean hasPolygonModeBack() {
    return _polygonModeBackSet;
  }

  /**
   * Gets the polygon mode for front faces.
   * @return the polygon mode.
   */
  public int getPolygonModeFront() {
    return _polygonModeFront;
  }

  /**
   * Gets the polygon mode for back faces.
   * @return the polygon mode.
   */
  public int getPolygonModeBack() {
    return _polygonModeBack;
  }

  /**
   * Sets the polygon mode for front and back faces.
   * @param mode the polygon mode.
   */
  public void setPolygonMode(int mode) {
    _polygonModeFront = _polygonModeBack = mode;
    _polygonModeFrontSet = _polygonModeBackSet = true;
  }

  /**
   * Sets the polygon mode for front faces.
   * @param mode the polygon mode.
   */
  public void setPolygonModeFront(int mode) {
    _polygonModeFront = mode;
    _polygonModeFrontSet = true;
  }

  /**
   * Sets the polygon mode for back faces.
   * @param mode the polygon mode.
   */
  public void setPolygonModeBack(int mode) {
    _polygonModeBack = mode;
    _polygonModeBackSet = true;
  }

  /**
   * Unsets the polygon mode for front and back faces.
   */
  public void unsetPolygonMode() {
    _polygonModeFront = _polygonModeBack = _polygonModeDefault;
    _polygonModeFrontSet = _polygonModeBackSet = false;
  }

  /**
   * Unsets the polygon mode for front faces.
   */
  public void unsetPolygonModeFront() {
    _polygonModeFront = _polygonModeDefault;
    _polygonModeFrontSet = false;
  }

  /**
   * Unsets the polygon mode for back faces.
   */
  public void unsetPolygonModeBack() {
    _polygonModeBack = _polygonModeDefault;
    _polygonModeBackSet = false;
  }

  /**
   * Determines whether polygon offset is set.
   * @return true, if set; false, otherwise.
   */
  public boolean hasPolygonOffset() {
    return _polygonOffsetSet;
  }

  /**
   * Gets the polygon offset factor.
   * @return the polygon offset factor.
   */
  public float getPolygonOffsetFactor() {
    return _polygonOffsetFactor;
  }

  /**
   * Gets the polygon offset units.
   * @return the polygon offset units.
   */
  public float getPolygonOffsetUnits() {
    return _polygonOffsetUnits;
  }

  /**
   * Sets the polygon offset.
   * @param factor the polygon offset factor.
   * @param units the polygon offset units.
   */
  public void setPolygonOffset(float factor, float units) {
    _polygonOffsetFactor = factor;
    _polygonOffsetUnits = units;
    _polygonOffsetSet = true;
  }

  /**
   * Unsets the polygon offset.
   */
  public void unsetPolygonOffset() {
    _polygonOffsetFactor = _polygonOffsetFactorDefault;
    _polygonOffsetUnits = _polygonOffsetUnitsDefault;
    _polygonOffsetSet = false;
  }

  /**
   * Determines whether polygon offset fill is set.
   * @return true, if set; false, otherwise.
   */
  public boolean hasPolygonOffsetFill() {
    return _polygonOffsetFillSet;
  }

  /**
   * Gets the polygon offset fill.
   * @return the polygon offset fill.
   */
  public boolean getPolygonOffsetFill() {
    return _polygonOffsetFill;
  }

  /**
   * Sets the polygon offset fill.
   * @param fill the polygon offset fill.
   */
  public void setPolygonOffsetFill(boolean fill) {
    _polygonOffsetFill = fill;
    _polygonOffsetFillSet = true;
  }

  /**
   * Unsets the polygon offset fill.
   */
  public void unsetPolygonOffsetFill() {
    _polygonOffsetFill = _polygonOffsetFillDefault;
    _polygonOffsetFillSet = false;
  }

  /**
   * Determines whether polygon offset line is set.
   * @return true, if set; false, otherwise.
   */
  public boolean hasPolygonOffsetLine() {
    return _polygonOffsetLineSet;
  }

  /**
   * Gets the polygon offset line.
   * @return the polygon offset line.
   */
  public boolean getPolygonOffsetLine() {
    return _polygonOffsetLine;
  }

  /**
   * Sets the polygon offset line.
   * @param line the polygon offset line.
   */
  public void setPolygonOffsetLine(boolean line) {
    _polygonOffsetLine = line;
    _polygonOffsetLineSet = true;
  }

  /**
   * Unsets the polygon offset line.
   */
  public void unsetPolygonOffsetLine() {
    _polygonOffsetLine = _polygonOffsetLineDefault;
    _polygonOffsetLineSet = false;
  }

  /**
   * Determines whether polygon offset point is set.
   * @return true, if set; false, otherwise.
   */
  public boolean hasPolygonOffsetPoint() {
    return _polygonOffsetPointSet;
  }

  /**
   * Gets the polygon offset point.
   * @return the polygon offset point.
   */
  public boolean getPolygonOffsetPoint() {
    return _polygonOffsetPoint;
  }

  /**
   * Sets the polygon offset point.
   * @param point the polygon offset point.
   */
  public void setPolygonOffsetPoint(boolean point) {
    _polygonOffsetPoint = point;
    _polygonOffsetPointSet = true;
  }

  /**
   * Determines whether polygon stipple is set.
   * @return true, if set; false, otherwise.
   */
  public boolean hasPolygonStipple() {
    return _polygonStippleSet;
  }

  /**
   * Gets the polygon stipple.
   * @return the polygon stipple.
   */
  public byte[] getPolygonStipple() {
    return _polygonStipple.clone();
  }

  /**
   * Sets the polygon stipple.
   * @param mask the polygon stipple.
   */
  public void setPolygonStipple(byte[] mask) {
    _polygonStipple = mask.clone();
    _polygonStippleSet = true;
  }

  /**
   * Unsets the polygon stipple.
   */
  public void unsetPolygonStipple() {
    _polygonStipple = _polygonStippleDefault;
    _polygonStippleSet = false;
  }

  public void apply() {
    if (_cullFaceSet) {
      glEnable(GL_CULL_FACE);
      glCullFace(_cullFace);
    }
    if (_frontFaceSet) {
      glEnable(GL_FRONT_FACE);
      glFrontFace(_frontFace);
    }
    if (_polygonModeFrontSet && 
        _polygonModeBackSet && 
        _polygonModeFront==_polygonModeBack) {
      glPolygonMode(GL_FRONT_AND_BACK,_polygonModeFront);
    } else {
      if (_polygonModeFrontSet)
        glPolygonMode(GL_FRONT,_polygonModeFront);
      if (_polygonModeBackSet)
        glPolygonMode(GL_BACK,_polygonModeBack);
    }
    if (_polygonOffsetSet)
      glPolygonOffset(_polygonOffsetFactor,_polygonOffsetUnits);
    if (_polygonOffsetFillSet) {
      if (_polygonOffsetFill) {
        glEnable(GL_POLYGON_OFFSET_FILL);
      } else {
        glDisable(GL_POLYGON_OFFSET_FILL);
      }
    }
    if (_polygonOffsetLineSet) {
      if (_polygonOffsetLine) {
        glEnable(GL_POLYGON_OFFSET_LINE);
      } else {
        glDisable(GL_POLYGON_OFFSET_LINE);
      }
    }
    if (_polygonOffsetPointSet) {
      if (_polygonOffsetPoint) {
        glEnable(GL_POLYGON_OFFSET_POINT);
      } else {
        glDisable(GL_POLYGON_OFFSET_POINT);
      }
    }
    if (_polygonStippleSet) {
      glEnable(GL_POLYGON_STIPPLE);
      glPolygonStipple(_polygonStipple,0);
    }
  }

  public int getAttributeBits() {
    return GL_ENABLE_BIT | GL_POLYGON_BIT;
  }

  private static int _cullFaceDefault = GL_BACK;
  private int _cullFace = _cullFaceDefault;
  private boolean _cullFaceSet;

  private static int _frontFaceDefault = GL_CCW;
  private int _frontFace = _frontFaceDefault;
  private boolean _frontFaceSet;

  private static int _polygonModeDefault = GL_FILL;
  private int _polygonModeFront = _polygonModeDefault;
  private int _polygonModeBack = _polygonModeDefault;
  private boolean _polygonModeFrontSet;
  private boolean _polygonModeBackSet;

  private static float _polygonOffsetFactorDefault = 0.0f;
  private static float _polygonOffsetUnitsDefault = 0.0f;
  private float _polygonOffsetFactor = _polygonOffsetFactorDefault;
  private float _polygonOffsetUnits = _polygonOffsetUnitsDefault;
  private boolean _polygonOffsetSet;

  private static boolean _polygonOffsetFillDefault = false;
  private boolean _polygonOffsetFill = _polygonOffsetFillDefault;
  private boolean _polygonOffsetFillSet;

  private static boolean _polygonOffsetLineDefault = false;
  private boolean _polygonOffsetLine = _polygonOffsetLineDefault;
  private boolean _polygonOffsetLineSet;

  private static boolean _polygonOffsetPointDefault = false;
  private boolean _polygonOffsetPoint = _polygonOffsetPointDefault;
  private boolean _polygonOffsetPointSet;

  private static byte ONES = (byte)0xFF;
  private static byte[] _polygonStippleDefault = {
    ONES,ONES,ONES,ONES,
    ONES,ONES,ONES,ONES,
    ONES,ONES,ONES,ONES,
    ONES,ONES,ONES,ONES,
    ONES,ONES,ONES,ONES,
    ONES,ONES,ONES,ONES,
    ONES,ONES,ONES,ONES,
    ONES,ONES,ONES,ONES,
    ONES,ONES,ONES,ONES,
    ONES,ONES,ONES,ONES,
    ONES,ONES,ONES,ONES,
    ONES,ONES,ONES,ONES,
    ONES,ONES,ONES,ONES,
    ONES,ONES,ONES,ONES,
    ONES,ONES,ONES,ONES,
    ONES,ONES,ONES,ONES,
    ONES,ONES,ONES,ONES,
    ONES,ONES,ONES,ONES,
    ONES,ONES,ONES,ONES,
    ONES,ONES,ONES,ONES,
    ONES,ONES,ONES,ONES,
    ONES,ONES,ONES,ONES,
    ONES,ONES,ONES,ONES,
    ONES,ONES,ONES,ONES,
    ONES,ONES,ONES,ONES,
    ONES,ONES,ONES,ONES,
    ONES,ONES,ONES,ONES,
    ONES,ONES,ONES,ONES,
    ONES,ONES,ONES,ONES,
    ONES,ONES,ONES,ONES,
    ONES,ONES,ONES,ONES,
    ONES,ONES,ONES,ONES,
  };
  private byte[] _polygonStipple = _polygonStippleDefault;
  private boolean _polygonStippleSet;
}
