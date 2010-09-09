/****************************************************************************
Copyright (c) 2009, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import java.nio.FloatBuffer;

import static edu.mines.jtk.ogl.Gl.*;
import edu.mines.jtk.util.Direct;

/**
 * A group of unstructured points.
 * @author Dave Hale, Colorado School of Mines
 * @version 2009.01.13
 */
public class PointGroup extends Group {

  /**
   * Constructs a points group with specified coordinates.
   * <p>
   * The (x,y,z) coordinates of points are packed into the specified 
   * array xyz. The number of points is np = xyz.length/3.
   * @param xyz array[3*np] of packed point coordinates.
   */
  public PointGroup(float[] xyz) {
    this(xyz,null);
  }

  /**
   * Constructs a points group with specified coordinates and colors.
   * <p>
   * The (x,y,z) coordinates of points are packed into the specified 
   * array xyz. The number of points is np = xyz.length/3.
   * <p>
   * The (r,g,b) components of colors are packed into the specified 
   * array rgb. The number of colors equals the number of points.
   * @param xyz array[3*np] of packed point coordinates.
   * @param rgb array[3*np] of packed color components.
   */
  public PointGroup(float[] xyz, float[] rgb) {
    buildTree(xyz,rgb);
  }

  /**
   * Constructs a points group with specified coordinates.
   * <p>
   * The (x,y,z) coordinates of points are packed into the specified 
   * array xyz. The number of points is np = xyz.length/3.
   * @param size size of cubes used to represent points.
   * @param xyz array[3*np] of packed point coordinates.
   */
  public PointGroup(float size, float[] xyz) {
    this(size,xyz,null);
  }

  /**
   * Constructs a points group with specified coordinates and colors.
   * <p>
   * The (x,y,z) coordinates of points are packed into the specified 
   * array xyz. The number of points is np = xyz.length/3.
   * <p>
   * The (r,g,b) components of colors are packed into the specified 
   * array rgb. The number of colors equals the number of points.
   * @param size size of cubes used to represent points.
   * @param xyz array[3*np] of packed point coordinates.
   * @param rgb array[3*np] of packed color components.
   */
  public PointGroup(float size, float[] xyz, float[] rgb) {
    _size = size;
    buildTree(xyz,rgb);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  // Constants for indexing packed arrays.
  private static final int X = 0,  Y = 1,  Z = 2;
  private static final int R = 0,  G = 1,  B = 2;

  private static final int MIN_POINT_PER_NODE = 2048;

  private float _size; // size of cubes used to represent points

  /**
   * Recursively builds a binary tree with leaf point nodes.
   */
  private void buildTree(float[] xyz, float[] rgb) {
    BoundingBoxTree bbt = new BoundingBoxTree(MIN_POINT_PER_NODE,xyz);
    buildTree(this,bbt.getRoot(),xyz,rgb);
  }
  private void buildTree(
    Group parent, BoundingBoxTree.Node bbtNode, 
    float[] xyz, float[] rgb) 
  {
    if (bbtNode.isLeaf()) {
      PointNode pn;
      if (_size>0.0f) {
        pn = new PointNode(bbtNode,_size,xyz,rgb);
      } else {
        pn = new PointNode(bbtNode,xyz,rgb);
      }
      parent.addChild(pn);
    } else {
      Group group = new Group();
      parent.addChild(group);
      buildTree(group,bbtNode.getLeft(),xyz,rgb);
      buildTree(group,bbtNode.getRight(),xyz,rgb);
    }
  }

  /**
   * A leaf point node in the hierarchy of nodes for this group.
   */
  private class PointNode extends Node {

    public PointNode(
      BoundingBoxTree.Node bbtNode, float[] xyz, float[] rgb) 
    {
      BoundingBox bb = bbtNode.getBoundingBox();
      _bs = new BoundingSphere(bb);
      _np = bbtNode.getSize();
      int np = _np;
      int nv = np;
      int nc = np;
      int[] index = bbtNode.getIndices();
      _vb = Direct.newFloatBuffer(3*nv);
      _cb = (rgb!=null)?Direct.newFloatBuffer(3*nc):null;
      for (int ip=0,iv=0,ic=0; ip<np; ++ip) {
        int i = 3*index[ip];
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

    public PointNode(
      BoundingBoxTree.Node bbtNode, float size, float[] xyz, float[] rgb) 
    {
      BoundingBox bb = bbtNode.getBoundingBox();
      _bs = new BoundingSphere(bb);
      _np = bbtNode.getSize();
      int np = _np;
      int nv = np;
      int nn = np;
      int nc = np;
      int[] index = bbtNode.getIndices();
      _vb = Direct.newFloatBuffer(3*4*6*nv);
      _nb = Direct.newFloatBuffer(3*4*6*nn);
      _cb = (rgb!=null)?Direct.newFloatBuffer(3*4*6*nc):null;
      float d = 0.5f*size;
      for (int ip=0,iv=0,in=0,ic=0; ip<np; ++ip) {
        int i = 3*index[ip];
        float xi = xyz[i+X];
        float yi = xyz[i+Y];
        float zi = xyz[i+Z];

        _vb.put(iv++,xi-d); _vb.put(iv++,yi-d); _vb.put(iv++,zi-d);
        _vb.put(iv++,xi-d); _vb.put(iv++,yi-d); _vb.put(iv++,zi+d);
        _vb.put(iv++,xi-d); _vb.put(iv++,yi+d); _vb.put(iv++,zi+d);
        _vb.put(iv++,xi-d); _vb.put(iv++,yi+d); _vb.put(iv++,zi-d);

        _vb.put(iv++,xi-d); _vb.put(iv++,yi-d); _vb.put(iv++,zi-d);
        _vb.put(iv++,xi+d); _vb.put(iv++,yi-d); _vb.put(iv++,zi-d);
        _vb.put(iv++,xi+d); _vb.put(iv++,yi-d); _vb.put(iv++,zi+d);
        _vb.put(iv++,xi-d); _vb.put(iv++,yi-d); _vb.put(iv++,zi+d);

        _vb.put(iv++,xi-d); _vb.put(iv++,yi-d); _vb.put(iv++,zi-d);
        _vb.put(iv++,xi-d); _vb.put(iv++,yi+d); _vb.put(iv++,zi-d);
        _vb.put(iv++,xi+d); _vb.put(iv++,yi+d); _vb.put(iv++,zi-d);
        _vb.put(iv++,xi+d); _vb.put(iv++,yi-d); _vb.put(iv++,zi-d);

        _vb.put(iv++,xi+d); _vb.put(iv++,yi-d); _vb.put(iv++,zi-d);
        _vb.put(iv++,xi+d); _vb.put(iv++,yi+d); _vb.put(iv++,zi-d);
        _vb.put(iv++,xi+d); _vb.put(iv++,yi+d); _vb.put(iv++,zi+d);
        _vb.put(iv++,xi+d); _vb.put(iv++,yi-d); _vb.put(iv++,zi+d);

        _vb.put(iv++,xi-d); _vb.put(iv++,yi+d); _vb.put(iv++,zi-d);
        _vb.put(iv++,xi-d); _vb.put(iv++,yi+d); _vb.put(iv++,zi+d);
        _vb.put(iv++,xi+d); _vb.put(iv++,yi+d); _vb.put(iv++,zi+d);
        _vb.put(iv++,xi+d); _vb.put(iv++,yi+d); _vb.put(iv++,zi-d);

        _vb.put(iv++,xi-d); _vb.put(iv++,yi-d); _vb.put(iv++,zi+d);
        _vb.put(iv++,xi+d); _vb.put(iv++,yi-d); _vb.put(iv++,zi+d);
        _vb.put(iv++,xi+d); _vb.put(iv++,yi+d); _vb.put(iv++,zi+d);
        _vb.put(iv++,xi-d); _vb.put(iv++,yi+d); _vb.put(iv++,zi+d);

        _nb.put(in++,-1.0f); _nb.put(in++, 0.0f); _nb.put(in++, 0.0f);
        _nb.put(in++,-1.0f); _nb.put(in++, 0.0f); _nb.put(in++, 0.0f);
        _nb.put(in++,-1.0f); _nb.put(in++, 0.0f); _nb.put(in++, 0.0f);
        _nb.put(in++,-1.0f); _nb.put(in++, 0.0f); _nb.put(in++, 0.0f);

        _nb.put(in++, 0.0f); _nb.put(in++,-1.0f); _nb.put(in++, 0.0f);
        _nb.put(in++, 0.0f); _nb.put(in++,-1.0f); _nb.put(in++, 0.0f);
        _nb.put(in++, 0.0f); _nb.put(in++,-1.0f); _nb.put(in++, 0.0f);
        _nb.put(in++, 0.0f); _nb.put(in++,-1.0f); _nb.put(in++, 0.0f);

        _nb.put(in++, 0.0f); _nb.put(in++, 0.0f); _nb.put(in++,-1.0f);
        _nb.put(in++, 0.0f); _nb.put(in++, 0.0f); _nb.put(in++,-1.0f);
        _nb.put(in++, 0.0f); _nb.put(in++, 0.0f); _nb.put(in++,-1.0f);
        _nb.put(in++, 0.0f); _nb.put(in++, 0.0f); _nb.put(in++,-1.0f);

        _nb.put(in++, 1.0f); _nb.put(in++, 0.0f); _nb.put(in++, 0.0f);
        _nb.put(in++, 1.0f); _nb.put(in++, 0.0f); _nb.put(in++, 0.0f);
        _nb.put(in++, 1.0f); _nb.put(in++, 0.0f); _nb.put(in++, 0.0f);
        _nb.put(in++, 1.0f); _nb.put(in++, 0.0f); _nb.put(in++, 0.0f);

        _nb.put(in++, 0.0f); _nb.put(in++, 1.0f); _nb.put(in++, 0.0f);
        _nb.put(in++, 0.0f); _nb.put(in++, 1.0f); _nb.put(in++, 0.0f);
        _nb.put(in++, 0.0f); _nb.put(in++, 1.0f); _nb.put(in++, 0.0f);
        _nb.put(in++, 0.0f); _nb.put(in++, 1.0f); _nb.put(in++, 0.0f);

        _nb.put(in++, 0.0f); _nb.put(in++, 0.0f); _nb.put(in++, 1.0f);
        _nb.put(in++, 0.0f); _nb.put(in++, 0.0f); _nb.put(in++, 1.0f);
        _nb.put(in++, 0.0f); _nb.put(in++, 0.0f); _nb.put(in++, 1.0f);
        _nb.put(in++, 0.0f); _nb.put(in++, 0.0f); _nb.put(in++, 1.0f);

        if (_cb!=null) {
          float ri = rgb[i+R];
          float gi = rgb[i+G];
          float bi = rgb[i+B];
          for (int j=0; j<24; ++j) {
            _cb.put(ic++,ri);
            _cb.put(ic++,gi);
            _cb.put(ic++,bi);
          }
        }
      }
    }

    protected BoundingSphere computeBoundingSphere(boolean finite) {
      return _bs;
    }

    protected void draw(DrawContext dc) {
      glEnableClientState(GL_VERTEX_ARRAY);
      glVertexPointer(3,GL_FLOAT,0,_vb);
      if (_nb!=null) {
        glEnableClientState(GL_NORMAL_ARRAY);
        glNormalPointer(GL_FLOAT,0,_nb);
      }
      if (_cb!=null) {
        glEnableClientState(GL_COLOR_ARRAY);
        glColorPointer(3,GL_FLOAT,0,_cb);
      }
      if (_size>0.0f) {
        glDrawArrays(GL_QUADS,0,4*6*_np);
      } else {
        glDrawArrays(GL_POINTS,0,_np);
      }
      if (_cb!=null)
        glDisableClientState(GL_COLOR_ARRAY);
      if (_nb!=null)
        glDisableClientState(GL_NORMAL_ARRAY);
      glDisableClientState(GL_VERTEX_ARRAY);
    }
    
    private BoundingSphere _bs; // pre-computed bounding sphere
    private int _np; // number of points
    private FloatBuffer _vb; // vertex buffer
    private FloatBuffer _nb; // normal buffer
    private FloatBuffer _cb; // color buffer
  }
}
