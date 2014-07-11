/****************************************************************************
Copyright (c) 2010, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import java.nio.FloatBuffer;

import static edu.mines.jtk.ogl.Gl.*;
import edu.mines.jtk.util.Direct;

/**
 * A group of one or more sets of connected line segments.
 * @author Dave Hale, Colorado School of Mines
 * @version 2010.01.10
 */
public class LineGroup extends Group {

  /**
   * Constructs a line group with one set of connected line segments.
   * <p>
   * The (x,y,z) coordinates of points are packed into the specified 
   * array xyz. The number of points is np = xyz.length/3.
   * @param xyz array[3*np] of packed point coordinates.
   */
  public LineGroup(float[] xyz) {
    this(xyz,null);
  }

  /**
   * Constructs a line group with one set of connected line segments.
   * <p>
   * The (x,y,z) coordinates of points are packed into the specified 
   * array xyz. The number of points is np = xyz.length/3.
   * <p>
   * The (r,g,b) components of colors are packed into the specified 
   * array rgb. The number of colors equals the number of points.
   * @param xyz array[3*np] of packed point coordinates.
   * @param rgb array[3*np] of packed color components.
   */
  public LineGroup(float[] xyz, float[] rgb) {
    this.addChild(new LineNode(xyz,rgb));
  }

  /**
   * Constructs a line group with multiple sets of connected line segments.
   * <p>
   * The number of sets is ns = xyz.length. For the set with index is, (x,y,z)
   * coordinates of points are packed into the array xyz[is]. The number of
   * points in that set is np = xyz[is].length/3.
   * @param xyz array[ns][3*np] of packed point coordinates.
   */
  public LineGroup(float[][] xyz) {
    this(xyz,null);
  }

  /**
   * Constructs a line group with multiple sets of connected line segments.
   * <p>
   * The number of sets is ns = xyz.length. For the set with index is, (x,y,z)
   * coordinates of points are packed into the array xyz[is]. The number of
   * points in that set is np = xyz[is].length/3.
   * <p>
   * If rgb is not null, this array contains similarly packed (r,g,b)
   * components of colors. The number of colors equals the number of points.
   * @param xyz array[ns][3*np] of packed point coordinates.
   * @param rgb array[ns][3*np] of packed color components.
   */
  public LineGroup(float[][] xyz, float[][] rgb) {
    int ns = xyz.length;
    for (int is=0; is<ns; ++is) {
      if (rgb==null) {
        this.addChild(new LineNode(xyz[is],null));
      } else {
        this.addChild(new LineNode(xyz[is],rgb[is]));
      }
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  // Constants for indexing packed arrays.
  private static final int X = 0,  Y = 1,  Z = 2;
  private static final int R = 0,  G = 1,  B = 2;

  /**
   * A line node in this group.
   */
  private class LineNode extends Node {

    public LineNode(float[] xyz, float[] rgb) {
      BoundingBox bb = new BoundingBox(xyz);
      _bs = new BoundingSphere(bb);
      _np = xyz.length/3;
      int np = _np;
      int nv = np;
      int nc = np;
      _vb = Direct.newFloatBuffer(3*nv);
      _cb = (rgb!=null)?Direct.newFloatBuffer(3*nc):null;
      for (int ip=0,iv=0,ic=0; ip<np; ++ip) {
        int i = 3*ip;
        _vb.put(iv++,xyz[i+X]);
        _vb.put(iv++,xyz[i+Y]);
        _vb.put(iv++,xyz[i+Z]);
        if (_cb!=null) {
          _cb.put(ic++,rgb[i+R]);
          _cb.put(ic++,rgb[i+G]);
          _cb.put(ic++,rgb[i+B]);
        }
      }
    }

    protected BoundingSphere computeBoundingSphere(boolean finite) {
      return _bs;
    }

    protected void draw(DrawContext dc) {
      glEnableClientState(GL_VERTEX_ARRAY);
      glVertexPointer(3,GL_FLOAT,0,_vb);
      if (_cb!=null) {
        glEnableClientState(GL_COLOR_ARRAY);
        glColorPointer(3,GL_FLOAT,0,_cb);
      }
      glDrawArrays(GL_LINE_STRIP,0,_np);
      if (_cb!=null)
        glDisableClientState(GL_COLOR_ARRAY);
      glDisableClientState(GL_VERTEX_ARRAY);
    }
    
    private BoundingSphere _bs; // pre-computed bounding sphere
    private int _np; // number of points
    private FloatBuffer _vb; // vertex buffer
    private FloatBuffer _cb; // color buffer
  }
}
