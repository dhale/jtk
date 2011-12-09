/****************************************************************************
Copyright (c) 2011, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import java.nio.FloatBuffer;
import java.util.HashMap;

import edu.mines.jtk.dsp.Sampling;
import static edu.mines.jtk.ogl.Gl.*;
import edu.mines.jtk.util.Direct;
import java.awt.Color;

/**
 * A group of quads that represents a surface.
 * <p>
 * Quads may be specified by providing an array of packed vertex
 * (x,y,z) coordinates and an array of packed (i,j,k,l) vertex indices.
 * Each set (i,j,k,l) of four vertex indices corresponds to one quad.
 * <p>
 * Alternatively, quads may be specified by providing only the array 
 * of packed vertex (x,y,z) coordinates. In this case, a vertex index is
 * assigned automatically to each vertex.
 * <p>
 * Normal vectors are computed for each vertex as an area-weighted 
 * average of the vectors normal to all quads that reference that 
 * vertex with the same index. These area-weighted normal vectors are 
 * used in lighting.
 * @author Dave Hale, Colorado School of Mines
 * @version 2011.12.05
 */
public class QuadGroup extends Group implements Selectable {

  /**
   * Constructs a quad group with specified vertex coordinates.
   * <p>
   * The (x,y,z) coordinates of vertices are packed into the specified 
   * array xyz. The number of vertices is nv = xyz.length/3. The number
   * of quads is nq = nv/4 = xyz.length/12.
   * <p>
   * Normal vectors may be computed for either vertices or quads.
   * When computed for a vertex, a normal vector is the area-weighted 
   * average of the normal vectors for all quads with that vertex.
   * <p>
   * If no vertices have the same (x,y,z) coordinates, then vertex and
   * quad normal vectors are the same vectors, but quad normal vectors 
   * are less costly to compute.
   * @param vn true, for vertex normals; false, for quad normals.
   * @param xyz array[3*nv] of packed vertex coordinates.
   */
  public QuadGroup(boolean vn, float[] xyz) {
    this(vn,xyz,null);
  }

  /**
   * Constructs a quad group with specified vertex coordinates.
   * <p>
   * The (x,y,z) coordinates of vertices are packed into the specified 
   * array xyz. The number of vertices is nv = xyz.length/3. The number
   * of quads is nq = nv/4 = xyz.length/12.
   * <p>
   * The (r,g,b) components of colors are packed into the specified 
   * array rgb. The number of colors equals the number of vertices.
   * <p>
   * Normal vectors may be computed for either vertices or quads.
   * When computed for a vertex, a normal vector is the area-weighted 
   * average of the normal vectors for all quads with that vertex.
   * <p>
   * If no vertices have the same (x,y,z) coordinates, then vertex and
   * quad normal vectors are the same vectors, but quad normal vectors 
   * are less costly to compute.
   * @param vn true, for vertex normals; false, for quad normals.
   * @param xyz array[3*nv] of packed vertex coordinates.
   * @param rgb array[3*nv] of packed color components.
   */
  public QuadGroup(boolean vn, float[] xyz, float[] rgb) {
    int[] ijkl = indexVertices(!vn,xyz);
    float[] uvw = computeNormals(ijkl,xyz);
    buildTree(ijkl,xyz,uvw,rgb);
    setDefaultStates();
  }

  /**
   * Constructs a quad group for a sampled function z = f(x,y).
   * @param vn true, for vertex normals; false, for quad normals.
   * @param sx sampling of x coordinates; may be non-uniform.
   * @param sy sampling of y coordinates; may be non-uniform.
   * @param z array[nx][ny] of z coordinates z = f(x,y).
   */
  public QuadGroup(boolean vn, Sampling sx, Sampling sy, float[][] z) {
    this(vn,makeVertices(sx,sy,z));
  }

  /**
   * Constructs a quad group for a sampled function z = f(x,y).
   * @param vn true, for vertex normals; false, for quad normals.
   * @param sx sampling of x coordinates; may be non-uniform.
   * @param sy sampling of y coordinates; may be non-uniform.
   * @param z array[nx][ny] of z coordinates z = f(x,y).
   * @param r array[nx][ny] of red color components.
   * @param g array[nx][ny] of green color components.
   * @param b array[nx][ny] of blue color components.
   */
  public QuadGroup(
    boolean vn, Sampling sx, Sampling sy, float[][] z,
    float[][] r, float[][] g, float[][] b)
  {
    this(vn,makeVertices(sx,sy,z),makeColors(r,g,b));
  }
  private static float[] makeVertices(Sampling sx, Sampling sy, float[][] z) {
    int nx = sx.getCount()-1;
    int ny = sy.getCount()-1;
    float[] xyz = new float[3*6*nx*ny];
    for (int ix=0,i=0; ix<nx; ++ix) {
      float x0 = (float)sx.getValue(ix  );
      float x1 = (float)sx.getValue(ix+1);
      for (int iy=0; iy<ny; ++iy) {
        float y0 = (float)sy.getValue(iy  );
        float y1 = (float)sy.getValue(iy+1);
        xyz[i++] = x0;  xyz[i++] = y0;  xyz[i++] = z[ix  ][iy  ];
        xyz[i++] = x0;  xyz[i++] = y1;  xyz[i++] = z[ix  ][iy+1];
        xyz[i++] = x1;  xyz[i++] = y0;  xyz[i++] = z[ix+1][iy  ];
        xyz[i++] = x1;  xyz[i++] = y0;  xyz[i++] = z[ix+1][iy  ];
        xyz[i++] = x0;  xyz[i++] = y1;  xyz[i++] = z[ix  ][iy+1];
        xyz[i++] = x1;  xyz[i++] = y1;  xyz[i++] = z[ix+1][iy+1];
      }
    }
    return xyz;
  }
  private static float[] makeColors(float[][] r, float[][] g, float[][] b) {
    int nx = r.length-1;
    int ny = r[0].length-1;
    float[] rgb = new float[3*6*nx*ny];
    for (int ix=0,i=0; ix<nx; ++ix) {
      for (int iy=0; iy<ny; ++iy) {
        rgb[i++] = r[ix  ][iy  ];
        rgb[i++] = g[ix  ][iy  ];
        rgb[i++] = b[ix  ][iy  ];
        rgb[i++] = r[ix  ][iy+1];
        rgb[i++] = g[ix  ][iy+1];
        rgb[i++] = b[ix  ][iy+1];
        rgb[i++] = r[ix+1][iy  ];
        rgb[i++] = g[ix+1][iy  ];
        rgb[i++] = b[ix+1][iy  ];
        rgb[i++] = r[ix+1][iy  ];
        rgb[i++] = g[ix+1][iy  ];
        rgb[i++] = b[ix+1][iy  ];
        rgb[i++] = r[ix  ][iy+1];
        rgb[i++] = g[ix  ][iy+1];
        rgb[i++] = b[ix  ][iy+1];
        rgb[i++] = r[ix+1][iy+1];
        rgb[i++] = g[ix+1][iy+1];
        rgb[i++] = b[ix+1][iy+1];
      }
    }
    return rgb;
  }

  /**
   * Constructs a quad group with specified vertex coordinates.
   * <p>
   * Quads are specified by sets of four vertex indices (i,j,k,l), 
   * one set per quad, packed into the specified array of integers 
   * ijkl. The number of quads is nq = ijkl.length/4.
   * <p>
   * The (x,y,z) coordinates of vertices are packed into the specified 
   * array xyz. The number of vertices is nv = xyz.length/3.
   * <p>
   * For any vertex with index iv, this method computes a normal vector 
   * as an area-weighted average of the normal vectors for all quads 
   * specified with index iv.
   * @param ijkl array[4*nq] of packed vertex indices.
   * @param xyz array[3*nv] of packed vertex coordinates.
   */
  public QuadGroup(int[] ijkl, float[] xyz) {
    this(ijkl,xyz,null);
  }

  /**
   * Constructs a quad group with specified vertex coordinates.
   * <p>
   * The (x,y,z) coordinates of vertices are packed into the specified 
   * array xyz. The number of vertices is nv = xyz.length/3.
   * <p>
   * The (u,v,w) components of normal vectors are packed into the specified 
   * array uvw. The number of normal vectors equals the number of vertices.
   * @param xyz array[3*nv] of packed vertex coordinates.
   * @param uvw array[3*nv] of packed normal vector components.
   */
  public QuadGroup(float[] xyz, float[] uvw) {
    this(indexVertices(true,xyz),xyz,uvw,null);
  }

  /**
   * Constructs a quad group with specified vertex coordinates.
   * <p>
   * Quads are specified by sets of four vertex indices (i,j,k,l), 
   * one set per quad, packed into the specified array of integers 
   * ijkl. The number of quads is nq = ijkl.length/4.
   * <p>
   * The (x,y,z) coordinates of vertices are packed into the specified 
   * array xyz. The number of vertices is nv = xyz.length/3.
   * <p>
   * The (u,v,w) components of normal vectors are packed into the specified 
   * array uvw. The number of normal vectors equals the number of vertices.
   * @param ijkl array[4*nq] of packed vertex indices.
   * @param xyz array[3*nv] of packed vertex coordinates.
   * @param uvw array[3*nv] of packed normal vector components.
   */
  public QuadGroup(int[] ijkl, float[] xyz, float[] uvw) {
    this(ijkl,xyz,uvw,null);
  }

  /**
   * Constructs a quad group with specified vertex coordinates
   * and optional corresponding normal vectors and colors.
   * <p>
   * The (x,y,z) coordinates of vertices are packed into the specified 
   * array xyz. The number of vertices is nv = xyz.length/3.
   * <p>
   * The (u,v,w) components of normal vectors are packed into the specified 
   * array uvw. The number of normal vectors equals the number of vertices.
   * <p>
   * The (r,g,b) components of colors are packed into the specified array 
   * rgb. The number of colors equals the number of vertices.
   * @param xyz array[3*nv] of packed vertex coordinates.
   * @param uvw array[3*nv] of packed normal vector components.
   * @param rgb array[3*nv] of packed color components.
   */
  public QuadGroup(float[] xyz, float[] uvw, float[] rgb) {
    this(indexVertices(true,xyz),xyz,uvw,rgb);
  }

  /**
   * Constructs a quad group with specified vertex coordinates
   * and optional corresponding normal vectors and colors.
   * <p>
   * Quads are specified by sets of four vertex indices (i,j,k), 
   * one set per quad, packed into the specified array of integers 
   * ijkl. The number of quads is nq = ijkl.length/4.
   * <p>
   * The (x,y,z) coordinates of vertices are packed into the specified 
   * array xyz. The number of vertices is nv = xyz.length/3.
   * <p>
   * The (u,v,w) components of normal vectors are packed into the specified 
   * array uvw. If not null, the number of normal vectors equals the number
   * of vertices.
   * <p>
   * The (r,g,b) components of colors are packed into the specified array 
   * rgb. If not null, the number of colors equals the number of vertices.
   * @param ijkl array[4*nq] of packed vertex indices.
   * @param xyz array[3*nv] of packed vertex coordinates.
   * @param uvw array[3*nv] of packed normal vector components.
   * @param rgb array[3*nv] of packed color components.
   */
  public QuadGroup(int[] ijkl, float[] xyz, float[] uvw, float[] rgb) {
    if (uvw==null)
      uvw = computeNormals(ijkl,xyz);
    buildTree(ijkl,xyz,uvw,rgb);
    setDefaultStates();
  }

  /**
   * Computes indices ijkl for quad vertex coordinates xyz.
   * <p>
   * The (x,y,z) coordinates of vertices are packed into the specified 
   * array xyz. The number of vertices is nv = xyz.length/3. The number 
   * of quads is nq = nv/4 = xyz.length/12.
   * <p>
   * For each quad, this method computes a set (i,j,k,l) of integer 
   * vertex indices. A vertex index is an integer in the range [0,nv-1].
   * The (x,y,z) coordinates of a vertex with index iv are xyz[3*iv+0],
   * xyz[3*iv+1] and xyz[3*iv+2], respectively.
   * <p>
   * The simplest indexing is the sequence {0, 1, 2, ..., nv-1}. In this
   * case, indices are assigned sequentially, so that every vertex of 
   * every quad has a different index.
   * <p>
   * In non-sequential indexing, vertices with the same (x,y,z) coordinates 
   * are assigned the same index. Again, index vertices will be in the range
   * [0,nv-1], but some integers in this range may not be used. Whereas
   * sequential indexing would assign integers ia and ib to two vertices 
   * that have the same (x,y,z) coordinates, non-sequential indexing will 
   * assign the smaller index min(ia,ib) to both vertices; the larger index
   * max(ia,ib) will be unused.
   * <p>
   * Sets of four indices (i,j,k,l), one set per quad, are packed 
   * into the returned array of integers ijkl, which has length 4*nq.
   * @param sequential true, for sequential indexing; false, otherwise.
   * @param xyz array[3*nv] of packed vertex coordinates.
   * @return an array[4*nq] of packed vertex indices.
   */
  public static int[] indexVertices(boolean sequential, float[] xyz) {

    // Number of vertices and quads.
    int nv = xyz.length/3;
    int nq = nv/4;

    // Array of vertex indices, one per vertex.
    int[] ijkl = new int[nv];

    // For sequential indexing, simply fill the array. For non-sequential 
    // indexing, map each unique vertex to a unique vertex index.
    if (sequential) {
      for (int iv=0; iv<nv; ++iv)
        ijkl[iv] = iv;
    } else {
      HashMap<Vertex,Integer> vimap = new HashMap<Vertex,Integer>(nv);
      for (int iq=0; iq<nq; ++iq) {
        for (int iv=0,jv=4*iq,kv=3*jv; iv<4; ++iv,++jv,kv+=3) {
          Vertex v = new Vertex(xyz[kv+X],xyz[kv+Y],xyz[kv+Z]);
          Integer i = vimap.get(v);
          if (i==null) {
            i = jv;
            vimap.put(v,i);
          }
          ijkl[jv] = i;
        }
      }
    }
    return ijkl;
  }

  /**
   * Sets the color of the quads in this quad group.
   * Note that if per-vertex colors were specified when this quad 
   * group was constructed, then the color specified here is not used.
   * @param color the color.
   */
  public void setColor(Color color) {
    StateSet states = getStates();
    ColorState cs = (ColorState)states.find(ColorState.class);
    if (cs==null)
      cs = new ColorState();
    cs.setColor(color);
  }

  ///////////////////////////////////////////////////////////////////////////
  // protected

  protected void selectedChanged() {
    System.out.println("QuadGroup: "+this+" selected="+isSelected());
    dirtyDraw();
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  // Constants for indexing packed arrays.
  private static final int I = 0,  J = 1,  K = 2, L = 3;
  private static final int X = 0,  Y = 1,  Z = 2;
  private static final int U = 0,  V = 1,  W = 2;
  private static final int R = 0,  G = 1,  B = 2;

  private static final int MIN_QUAD_PER_NODE = 1024;

  /**
   * Recursively builds a binary tree with leaf quad nodes.
   */
  private void buildTree(int[] ijkl, float[] xyz, float[] uvw, float[] rgb) {
    float[] c = computeCenters(ijkl,xyz);
    BoundingBoxTree bbt = new BoundingBoxTree(MIN_QUAD_PER_NODE,c);
    buildTree(this,bbt.getRoot(),ijkl,xyz,uvw,rgb);
  }
  private void buildTree(Group parent, BoundingBoxTree.Node bbtNode, 
    int[] ijkl, float[] xyz, float[] uvw, float[] rgb) 
  {
    if (bbtNode.isLeaf()) {
      QuadNode qn = new QuadNode(bbtNode,ijkl,xyz,uvw,rgb);
      parent.addChild(qn);
    } else {
      Group group = new Group();
      parent.addChild(group);
      buildTree(group,bbtNode.getLeft(),ijkl,xyz,uvw,rgb);
      buildTree(group,bbtNode.getRight(),ijkl,xyz,uvw,rgb);
    }
  }

  /**
   * A leaf quad node in the hierarchy of nodes for this group.
   */
  private class QuadNode extends Node {

    public QuadNode(BoundingBoxTree.Node bbtNode, 
      int[] ijkl, float[] xyz, float[] uvw, float[] rgb) 
    {
      BoundingBox bb = bbtNode.getBoundingBox();
      _bs = new BoundingSphere(bb);
      _nq = bbtNode.getSize();
      int nq = _nq;
      int nv = 4*nq;
      int nn = 4*nq;
      int nc = 4*nq;
      int[] index = bbtNode.getIndices();
      _vb = Direct.newFloatBuffer(3*nv);
      _nb = (uvw!=null)?Direct.newFloatBuffer(3*nn):null;
      _cb = (rgb!=null)?Direct.newFloatBuffer(3*nc):null;
      for (int iq=0,iv=0,in=0,ic=0; iq<nq; ++iq) {
        int jq = 4*index[iq];
        int i = 3*ijkl[jq+I];
        int j = 3*ijkl[jq+J];
        int k = 3*ijkl[jq+K];
        int l = 3*ijkl[jq+L];
        _vb.put(iv++,xyz[i+X]);
        _vb.put(iv++,xyz[i+Y]);
        _vb.put(iv++,xyz[i+Z]);
        _vb.put(iv++,xyz[j+X]);
        _vb.put(iv++,xyz[j+Y]);
        _vb.put(iv++,xyz[j+Z]);
        _vb.put(iv++,xyz[k+X]);
        _vb.put(iv++,xyz[k+Y]);
        _vb.put(iv++,xyz[k+Z]);
        _vb.put(iv++,xyz[l+X]);
        _vb.put(iv++,xyz[l+Y]);
        _vb.put(iv++,xyz[l+Z]);
        if (_nb!=null) {
          _nb.put(in++,uvw[i+U]);
          _nb.put(in++,uvw[i+V]);
          _nb.put(in++,uvw[i+W]);
          _nb.put(in++,uvw[j+U]);
          _nb.put(in++,uvw[j+V]);
          _nb.put(in++,uvw[j+W]);
          _nb.put(in++,uvw[k+U]);
          _nb.put(in++,uvw[k+V]);
          _nb.put(in++,uvw[k+W]);
          _nb.put(in++,uvw[l+U]);
          _nb.put(in++,uvw[l+V]);
          _nb.put(in++,uvw[l+W]);
        }
        if (_cb!=null) {
          _cb.put(ic++,rgb[i+R]);
          _cb.put(ic++,rgb[i+G]);
          _cb.put(ic++,rgb[i+B]);
          _cb.put(ic++,rgb[j+R]);
          _cb.put(ic++,rgb[j+G]);
          _cb.put(ic++,rgb[j+B]);
          _cb.put(ic++,rgb[k+R]);
          _cb.put(ic++,rgb[k+G]);
          _cb.put(ic++,rgb[k+B]);
          _cb.put(ic++,rgb[l+R]);
          _cb.put(ic++,rgb[l+G]);
          _cb.put(ic++,rgb[l+B]);
        }
      }
    }

    protected BoundingSphere computeBoundingSphere(boolean finite) {
      return _bs;
    }

    protected void draw(DrawContext dc) {
      boolean selected = QuadGroup.this.isSelected();
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
      if (selected) {
        glEnable(GL_POLYGON_OFFSET_FILL);
        glPolygonOffset(1.0f,1.0f);
      }
      glDrawArrays(GL_QUADS,0,4*_nq);
      if (_nb!=null)
        glDisableClientState(GL_NORMAL_ARRAY);
      if (_cb!=null)
        glDisableClientState(GL_COLOR_ARRAY);
      if (selected) {
        glPolygonMode(GL_FRONT_AND_BACK,GL_LINE);
        glDisable(GL_LIGHTING);
        glColor3d(1.0,1.0,1.0);
        glDrawArrays(GL_QUADS,0,4*_nq);
      }
      glDisableClientState(GL_VERTEX_ARRAY);
    }

    public void pick(PickContext pc) {
      Segment ps = pc.getPickSegment();
      for (int iq=0,j=0; iq<_nq; ++iq) {
        double xi = _vb.get(j++);
        double yi = _vb.get(j++);
        double zi = _vb.get(j++);
        double xj = _vb.get(j++);
        double yj = _vb.get(j++);
        double zj = _vb.get(j++);
        double xk = _vb.get(j++);
        double yk = _vb.get(j++);
        double zk = _vb.get(j++);
        double xl = _vb.get(j++);
        double yl = _vb.get(j++);
        double zl = _vb.get(j++);
        Point3 p = ps.intersectWithTriangle(xi,yi,zi,xj,yj,zj,xk,yk,zk);
        if (p==null)
          p = ps.intersectWithTriangle(xk,yk,zk,xl,yl,zl,xi,yi,zi);
        if (p!=null)
          pc.addResult(p);
      }
    }
    
    private BoundingSphere _bs; // pre-computed bounding sphere
    private int _nq; // number of quads
    private FloatBuffer _vb; // vertex buffer
    private FloatBuffer _nb; // normal buffer
    private FloatBuffer _cb; // color buffer
  }

  private static class Vertex {
    float x,y,z;
    Vertex(float x, float y, float z) {
      this.x = x;
      this.y = y;
      this.z = z;
    }
    public boolean equals(Object o) {
      Vertex v = (Vertex)o;
      return x==v.x &&  y==v.y && z==v.z;
    }
    public int hashCode() {
      return Float.floatToIntBits(x) ^
             Float.floatToIntBits(y) ^
             Float.floatToIntBits(z);
    }
  }

  /**
   * Computes (u,v,w) components of normal vectors for quad vertices.
   * <p>
   * The (x,y,z) coordinates of vertices are packed into the specified 
   * array xyz. The number of vertices is nv = xyz.length/3.
   * <p>
   * Quads are specified by sets of four vertex indices (i,j,k,l), one 
   * set per quad, packed into the specified array of integers ijkl. 
   * The number of quads is nq = ijkl.length/4.
   * <p>
   * The same vertex index may occur in more than one quad. For each 
   * indexed vertex, the normal vector is an area-weighted sum of the 
   * normal vectors of all quads that reference that vertex.
   * <p>
   * The returned array uvw of packed (u,v,w) components of vertex normal 
   * vectors has length equal to the specified packed array xyz of vertex
   * (x,y,z) coordinates. However, normal vectors are computed for only 
   * indexed vertices. All (u,v,w) components corresponding to vertices 
   * not indexed are zero.
   * @param ijkl array[4*nq] of packed vertex indices.
   * @param xyz array[3*nv] of packed vertex coordinates.
   * @return array[3*nv] of packed indexed normal vectors.
   */
  private static float[] computeNormals(int[] ijkl, float[] xyz) {
    int nv = xyz.length/3;
    int nq = ijkl.length/4;
    float[] uvw = new float[3*nv];

    // For each quad, ...
    for (int iq=0,jq=0; iq<nq; ++iq) {

      // (x,y,z) coordinates for each of 4 quad vertices.
      int i = 3*ijkl[jq++];
      int j = 3*ijkl[jq++];
      int k = 3*ijkl[jq++];
      int l = 3*ijkl[jq++];
      float xi = xyz[i+X];
      float yi = xyz[i+Y];
      float zi = xyz[i+Z];
      float xj = xyz[j+X];
      float yj = xyz[j+Y];
      float zj = xyz[j+Z];
      float xk = xyz[k+X];
      float yk = xyz[k+Y];
      float zk = xyz[k+Z];
      float xl = xyz[l+X];
      float yl = xyz[l+Y];
      float zl = xyz[l+Z];

      // Compute normal vector (xn,yn,zn) as a vector cross product
      // with length proportional to quad area.
      float xa = xk-xi;
      float ya = yk-yi;
      float za = zk-zi;
      float xb = xl-xj;
      float yb = yl-yj;
      float zb = zl-zj;
      float un = ya*zb-yb*za;
      float vn = za*xb-zb*xa;
      float wn = xa*yb-xb*ya;

      // Accumulate area-weighted normal vectors for 4 quad vertices.
      // Use the same indices for normal vectors as for vertex coordinates.
      uvw[i+U] += un;
      uvw[i+V] += vn;
      uvw[i+W] += wn;
      uvw[j+U] += un;
      uvw[j+V] += vn;
      uvw[j+W] += wn;
      uvw[k+U] += un;
      uvw[k+V] += vn;
      uvw[k+W] += wn;
      uvw[l+U] += un;
      uvw[l+V] += vn;
      uvw[l+W] += wn;
    }
    return uvw;
  }

  /**
   * Computes (x,y,z) components of quad centers (centroids).
   * <p>
   * The (x,y,z) coordinates of vertices are packed into the specified 
   * array xyz. The number of vertices is nv = xyz.length/3.
   * <p>
   * Quads are specified by sets of four vertex indices (i,j,k,l), one 
   * set per quad, packed into the specified array of integers ijkl. 
   * The number of quads is nq = ijkl.length/4.
   * <p>
   * The returned array of packed (x,y,z) coordinates of quad centers
   * has length equal to ijkl.length. Each center (centroid) is the 
   * average of the corresponding quad vertices.
   * @param ijkl array[4*nq] of packed vertex indices.
   * @param xyz array[3*nv] of packed vertex coordinates.
   * @return array[3*nq] of packed center coordinates.
   */
  private float[] computeCenters(int[] ijkl, float[] xyz) {
    int nq = ijkl.length/4;
    float[] c = new float[3*nq];
    float o4 = 1.0f/4.0f;
    for (int iq=0,jq=0,jc=0; iq<nq; ++iq) {
      int i = 3*ijkl[jq++];
      int j = 3*ijkl[jq++];
      int k = 3*ijkl[jq++];
      int l = 3*ijkl[jq++];
      float xi = xyz[i+X];
      float yi = xyz[i+Y];
      float zi = xyz[i+Z];
      float xj = xyz[j+X];
      float yj = xyz[j+Y];
      float zj = xyz[j+Z];
      float xk = xyz[k+X];
      float yk = xyz[k+Y];
      float zk = xyz[k+Z];
      float xl = xyz[l+X];
      float yl = xyz[l+Y];
      float zl = xyz[l+Z];
      c[jc++] = (xi+xj+xk+xl)*o4;
      c[jc++] = (yi+yj+yk+yl)*o4;
      c[jc++] = (zi+zj+zk+zl)*o4;
    }
    return c;
  }

  /**
   * Initializes the quad group states.
   */
  private static StateSet defaultStateSet(Color color) {
    StateSet states = new StateSet();
    ColorState cs = new ColorState();
    cs.setColor(color);
    LightModelState lms = new LightModelState();
    lms.setTwoSide(true);
    MaterialState ms = new MaterialState();
    ms.setColorMaterial(GL_AMBIENT_AND_DIFFUSE);
    ms.setSpecular(Color.WHITE);
    ms.setShininess(100.0f);
    states.add(cs);
    states.add(lms);
    states.add(ms);
    return states;
  }

  private void setDefaultStates() {
    setStates(defaultStateSet(Color.LIGHT_GRAY));
  }
}
