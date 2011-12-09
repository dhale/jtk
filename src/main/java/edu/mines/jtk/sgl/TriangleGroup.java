/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
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
 * A group of triangles that represents a triangulated surface.
 * <p>
 * Triangles may be specified by providing an array of packed vertex
 * (x,y,z) coordinates and an array of packed (i,j,k) vertex indices.
 * Each triplet (i,j,k) of vertex indices corresponds to one triangle.
 * <p>
 * Alternatively, triangles may be specified by providing only the array 
 * of packed vertex (x,y,z) coordinates. In this case, a vertex index is
 * assigned automatically to each vertex.
 * <p>
 * Normal vectors are computed for each vertex as an area-weighted 
 * average of the vectors normal to all triangles that reference that 
 * vertex with the same index. These area-weighted normal vectors are 
 * used in lighting.
 * @author Dave Hale, Christine Brady, Adam McCormick, Zachary Pember, 
 *  Danielle Schulte, Colorado School of Mines
 * @version 2006.06.27
 */
public class TriangleGroup extends Group implements Selectable {

  /**
   * Constructs a triangle group with specified vertex coordinates.
   * <p>
   * The (x,y,z) coordinates of vertices are packed into the specified 
   * array xyz. The number of vertices is nv = xyz.length/3. The number
   * of triangles is nt = nv/3 = xyz.length/9.
   * <p>
   * Normal vectors may be computed for either vertices or triangles.
   * When computed for a vertex, a normal vector is the area-weighted 
   * average of the normal vectors for all triangles with that vertex.
   * <p>
   * If no vertices have the same (x,y,z) coordinates, then vertex and
   * triangle normal vectors are the same vectors, but triangle normal
   * vectors are less costly to compute.
   * @param vn true, for vertex normals; false, for triangle normals.
   * @param xyz array[3*nv] of packed vertex coordinates.
   */
  public TriangleGroup(boolean vn, float[] xyz) {
    this(vn,xyz,null);
  }

  /**
   * Constructs a triangle group with specified vertex coordinates.
   * <p>
   * The (x,y,z) coordinates of vertices are packed into the specified 
   * array xyz. The number of vertices is nv = xyz.length/3. The number
   * of triangles is nt = nv/3 = xyz.length/9.
   * <p>
   * The (r,g,b) components of colors are packed into the specified 
   * array rgb. The number of colors equals the number of vertices.
   * <p>
   * Normal vectors may be computed for either vertices or triangles.
   * When computed for a vertex, a normal vector is the area-weighted 
   * average of the normal vectors for all triangles with that vertex.
   * <p>
   * If no vertices have the same (x,y,z) coordinates, then vertex and
   * triangle normal vectors are the same vectors, but triangle normal
   * vectors are less costly to compute.
   * @param vn true, for vertex normals; false, for triangle normals.
   * @param xyz array[3*nv] of packed vertex coordinates.
   * @param rgb array[3*nv] of packed color components.
   */
  public TriangleGroup(boolean vn, float[] xyz, float[] rgb) {
    int[] ijk = indexVertices(!vn,xyz);
    float[] uvw = computeNormals(ijk,xyz);
    buildTree(ijk,xyz,uvw,rgb);
    setDefaultStates();
  }

  /**
   * Constructs a triangle group for a sampled function z = f(x,y).
   * @param vn true, for vertex normals; false, for triangle normals.
   * @param sx sampling of x coordinates; may be non-uniform.
   * @param sy sampling of y coordinates; may be non-uniform.
   * @param z array[nx][ny] of z coordinates z = f(x,y).
   */
  public TriangleGroup(boolean vn, Sampling sx, Sampling sy, float[][] z) {
    this(vn,makeVertices(sx,sy,z));
  }

  /**
   * Constructs a triangle group for a sampled function z = f(x,y).
   * @param vn true, for vertex normals; false, for triangle normals.
   * @param sx sampling of x coordinates; may be non-uniform.
   * @param sy sampling of y coordinates; may be non-uniform.
   * @param z array[nx][ny] of z coordinates z = f(x,y).
   * @param r array[nx][ny] of red color components.
   * @param g array[nx][ny] of green color components.
   * @param b array[nx][ny] of blue color components.
   */
  public TriangleGroup(
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
   * Constructs a triangle group with specified vertex coordinates.
   * <p>
   * Triangles are specified by triplets of vertex indices (i,j,k), one 
   * triplet per triangle, packed into the specified array of integers 
   * ijk. The number of triangles is nt = ijk.length/3.
   * <p>
   * The (x,y,z) coordinates of vertices are packed into the specified 
   * array xyz. The number of vertices is nv = xyz.length/3.
   * <p>
   * For any vertex with index iv, this method computes a normal vector 
   * as an area-weighted average of the normal vectors for all triangles 
   * specified with index iv.
   * @param ijk array[3*nt] of packed vertex indices.
   * @param xyz array[3*nv] of packed vertex coordinates.
   */
  public TriangleGroup(int[] ijk, float[] xyz) {
    this(ijk,xyz,null);
  }

  /**
   * Constructs a triangle group with specified vertex coordinates.
   * <p>
   * The (x,y,z) coordinates of vertices are packed into the specified 
   * array xyz. The number of vertices is nv = xyz.length/3.
   * <p>
   * The (u,v,w) components of normal vectors are packed into the specified 
   * array uvw. The number of normal vectors equals the number of vertices.
   * @param xyz array[3*nv] of packed vertex coordinates.
   * @param uvw array[3*nv] of packed normal vector components.
   */
  public TriangleGroup(float[] xyz, float[] uvw) {
    this(indexVertices(true,xyz),xyz,uvw,null);
  }

  /**
   * Constructs a triangle group with specified vertex coordinates.
   * <p>
   * Triangles are specified by triplets of vertex indices (i,j,k), one 
   * triplet per triangle, packed into the specified array of integers 
   * ijk. The number of triangles is nt = ijk.length/3.
   * <p>
   * The (x,y,z) coordinates of vertices are packed into the specified 
   * array xyz. The number of vertices is nv = xyz.length/3.
   * <p>
   * The (u,v,w) components of normal vectors are packed into the specified 
   * array uvw. The number of normal vectors equals the number of vertices.
   * @param ijk array[3*nt] of packed vertex indices.
   * @param xyz array[3*nv] of packed vertex coordinates.
   * @param uvw array[3*nv] of packed normal vector components.
   */
  public TriangleGroup(int[] ijk, float[] xyz, float[] uvw) {
    this(ijk,xyz,uvw,null);
  }

  /**
   * Constructs a triangle group with specified vertex coordinates
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
  public TriangleGroup(float[] xyz, float[] uvw, float[] rgb) {
    this(indexVertices(true,xyz),xyz,uvw,rgb);
  }

  /**
   * Constructs a triangle group with specified vertex coordinates
   * and optional corresponding normal vectors and colors.
   * <p>
   * Triangles are specified by triplets of vertex indices (i,j,k), one 
   * triplet per triangle, packed into the specified array of integers 
   * ijk. The number of triangles is nt = ijk.length/3.
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
   * @param ijk array[3*nt] of packed vertex indices.
   * @param xyz array[3*nv] of packed vertex coordinates.
   * @param uvw array[3*nv] of packed normal vector components.
   * @param rgb array[3*nv] of packed color components.
   */
  public TriangleGroup(int[] ijk, float[] xyz, float[] uvw, float[] rgb) {
    if (uvw==null)
      uvw = computeNormals(ijk,xyz);
    buildTree(ijk,xyz,uvw,rgb);
    setDefaultStates();
  }

  /**
   * Computes indices ijk for triangle vertex coordinates xyz.
   * <p>
   * The (x,y,z) coordinates of vertices are packed into the specified 
   * array xyz. The number of vertices is nv = xyz.length/3. The number 
   * of triangles is nt = nv/3 = xyz.length/9.
   * <p>
   * For each triangle, this method computes a triplet (i,j,k) of integer 
   * vertex indices. A vertex index is an integer in the range [0,nv-1].
   * The (x,y,z) coordinates of a vertex with index iv are xyz[3*iv+0],
   * xyz[3*iv+1] and xyz[3*iv+2], respectively.
   * <p>
   * The simplest indexing is the sequence {0, 1, 2, ..., nv-1}. In this
   * case, indices are assigned sequentially, so that every vertex of 
   * every triangle has a different index.
   * <p>
   * In non-sequential indexing, vertices with the same (x,y,z) coordinates 
   * are assigned the same index. Again, index vertices will be in the range
   * [0,nv-1], but some integers in this range may not be used. Whereas
   * sequential indexing would assign integers ia and ib to two vertices 
   * that have the same (x,y,z) coordinates, non-sequential indexing will 
   * assign the smaller index min(ia,ib) to both vertices; the larger index
   * max(ia,ib) will be unused.
   * <p>
   * Triplets of indices (i,j,k), one triplet per triangle, are packed 
   * into the returned array of integers ijk, which has length 3*nt.
   * @param sequential true, for sequential indexing; false, otherwise.
   * @param xyz array[3*nv] of packed vertex coordinates.
   * @return an array[3*nt] of packed vertex indices.
   */
  public static int[] indexVertices(boolean sequential, float[] xyz) {

    // Number of vertices and triangles.
    int nv = xyz.length/3;
    int nt = nv/3;

    // Array of vertex indices, one per vertex.
    int[] ijk = new int[nv];

    // For sequential indexing, simply fill the array. For non-sequential 
    // indexing, map each unique vertex to a unique vertex index.
    if (sequential) {
      for (int iv=0; iv<nv; ++iv)
        ijk[iv] = iv;
    } else {
      HashMap<Vertex,Integer> vimap = new HashMap<Vertex,Integer>(nv);
      for (int it=0; it<nt; ++it) {
        for (int iv=0,jv=3*it,kv=3*jv; iv<3; ++iv,++jv,kv+=3) {
          Vertex v = new Vertex(xyz[kv+X],xyz[kv+Y],xyz[kv+Z]);
          Integer i = vimap.get(v);
          if (i==null) {
            i = jv;
            vimap.put(v,i);
          }
          ijk[jv] = i;
        }
      }
    }
    return ijk;
  }

  /**
   * Sets the color of the triangles in this triangle group.
   * Note that if per-vertex colors were specified when this triangle group was
   * constructed, then the color specified here is not used.
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
    System.out.println("TriangleGroup: "+this+" selected="+isSelected());
    dirtyDraw();
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  // Constants for indexing packed arrays.
  private static final int I = 0,  J = 1,  K = 2;
  private static final int X = 0,  Y = 1,  Z = 2;
  private static final int U = 0,  V = 1,  W = 2;
  private static final int R = 0,  G = 1,  B = 2;

  private static final int MIN_TRI_PER_NODE = 1024;

  /**
   * Recursively builds a binary tree with leaf triangle nodes.
   */
  private void buildTree(int[] ijk, float[] xyz, float[] uvw, float[] rgb) {
    float[] c = computeCenters(ijk,xyz);
    BoundingBoxTree bbt = new BoundingBoxTree(MIN_TRI_PER_NODE,c);
    buildTree(this,bbt.getRoot(),ijk,xyz,uvw,rgb);
  }
  private void buildTree(Group parent, BoundingBoxTree.Node bbtNode, 
    int[] ijk, float[] xyz, float[] uvw, float[] rgb) 
  {
    if (bbtNode.isLeaf()) {
      TriangleNode tn = new TriangleNode(bbtNode,ijk,xyz,uvw,rgb);
      parent.addChild(tn);
    } else {
      Group group = new Group();
      parent.addChild(group);
      buildTree(group,bbtNode.getLeft(),ijk,xyz,uvw,rgb);
      buildTree(group,bbtNode.getRight(),ijk,xyz,uvw,rgb);
    }
  }

  /**
   * A leaf triangle node in the hierarchy of nodes for this group.
   */
  private class TriangleNode extends Node {

    public TriangleNode(BoundingBoxTree.Node bbtNode, 
      int[] ijk, float[] xyz, float[] uvw, float[] rgb) 
    {
      BoundingBox bb = bbtNode.getBoundingBox();
      _bs = new BoundingSphere(bb);
      _nt = bbtNode.getSize();
      int nt = _nt;
      int nv = 3*nt;
      int nn = 3*nt;
      int nc = 3*nt;
      int[] index = bbtNode.getIndices();
      _vb = Direct.newFloatBuffer(3*nv);
      _nb = (uvw!=null)?Direct.newFloatBuffer(3*nn):null;
      _cb = (rgb!=null)?Direct.newFloatBuffer(3*nc):null;
      for (int it=0,iv=0,in=0,ic=0; it<nt; ++it) {
        int jt = 3*index[it];
        int i = 3*ijk[jt+I];
        int j = 3*ijk[jt+J];
        int k = 3*ijk[jt+K];
        _vb.put(iv++,xyz[i+X]);
        _vb.put(iv++,xyz[i+Y]);
        _vb.put(iv++,xyz[i+Z]);
        _vb.put(iv++,xyz[j+X]);
        _vb.put(iv++,xyz[j+Y]);
        _vb.put(iv++,xyz[j+Z]);
        _vb.put(iv++,xyz[k+X]);
        _vb.put(iv++,xyz[k+Y]);
        _vb.put(iv++,xyz[k+Z]);
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
        }
      }
    }

    protected BoundingSphere computeBoundingSphere(boolean finite) {
      return _bs;
    }

    protected void draw(DrawContext dc) {
      boolean selected = TriangleGroup.this.isSelected();
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
      glDrawArrays(GL_TRIANGLES,0,3*_nt);
      if (_nb!=null)
        glDisableClientState(GL_NORMAL_ARRAY);
      if (_cb!=null)
        glDisableClientState(GL_COLOR_ARRAY);
      if (selected) {
        glPolygonMode(GL_FRONT_AND_BACK,GL_LINE);
        glDisable(GL_LIGHTING);
        glColor3d(1.0,1.0,1.0);
        glDrawArrays(GL_TRIANGLES,0,3*_nt);
      }
      glDisableClientState(GL_VERTEX_ARRAY);
    }

    public void pick(PickContext pc) {
      Segment ps = pc.getPickSegment();
      for (int it=0,jt=0; it<_nt; ++it) {
        double xi = _vb.get(jt++);
        double yi = _vb.get(jt++);
        double zi = _vb.get(jt++);
        double xj = _vb.get(jt++);
        double yj = _vb.get(jt++);
        double zj = _vb.get(jt++);
        double xk = _vb.get(jt++);
        double yk = _vb.get(jt++);
        double zk = _vb.get(jt++);
        Point3 p = ps.intersectWithTriangle(xi,yi,zi,xj,yj,zj,xk,yk,zk);
        if (p!=null)
          pc.addResult(p);
      }
    }
    
    private BoundingSphere _bs; // pre-computed bounding sphere
    private int _nt; // number of triangles
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
   * Computes (u,v,w) components of normal vectors for triangle vertices.
   * <p>
   * The (x,y,z) coordinates of vertices are packed into the specified 
   * array xyz. The number of vertices is nv = xyz.length/3.
   * <p>
   * Triangles are specified by triplets of vertex indices (i,j,k), one 
   * triplet per triangle, packed into the specified array of integers 
   * ijk. The number of triangles is nt = ijk.length/3.
   * <p>
   * The same vertex index may occur in more than one triplet. For each 
   * indexed vertex, the normal vector is an area-weighted sum of the 
   * normal vectors of all triangles that reference that vertex.
   * <p>
   * The returned array uvw of packed (u,v,w) components of vertex normal 
   * vectors has length equal to the specified packed array xyz of vertex
   * (x,y,z) coordinates. However, normal vectors are computed for only 
   * indexed vertices. All (u,v,w) components corresponding to vertices 
   * not indexed are zero.
   * @param ijk array[3*nt] of packed vertex indices.
   * @param xyz array[3*nv] of packed vertex coordinates.
   * @return array[3*nv] of packed indexed normal vectors.
   */
  private static float[] computeNormals(int[] ijk, float[] xyz) {
    int nv = xyz.length/3;
    int nt = ijk.length/3;
    float[] uvw = new float[3*nv];

    // For each triangle, ...
    for (int it=0,jt=0; it<nt; ++it) {

      // (x,y,z) coordinates for each of 3 triangle vertices.
      int i = 3*ijk[jt++];
      int j = 3*ijk[jt++];
      int k = 3*ijk[jt++];
      float xi = xyz[i+X];
      float yi = xyz[i+Y];
      float zi = xyz[i+Z];
      float xj = xyz[j+X];
      float yj = xyz[j+Y];
      float zj = xyz[j+Z];
      float xk = xyz[k+X];
      float yk = xyz[k+Y];
      float zk = xyz[k+Z];

      // Compute normal vector (xn,yn,zn) as a vector cross product
      // with length proportional to triangle area.
      float xa = xj-xi;
      float ya = yj-yi;
      float za = zj-zi;
      float xb = xk-xi;
      float yb = yk-yi;
      float zb = zk-zi;
      float un = ya*zb-yb*za;
      float vn = za*xb-zb*xa;
      float wn = xa*yb-xb*ya;

      // Accumulate area-weighted normal vectors for 3 triangle vertices.
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
    }
    return uvw;
  }

  /**
   * Computes (x,y,z) components of triangle centers (centroids).
   * <p>
   * The (x,y,z) coordinates of vertices are packed into the specified 
   * array xyz. The number of vertices is nv = xyz.length/3.
   * <p>
   * Triangles are specified by triplets of vertex indices (i,j,k), one 
   * triplet per triangle, packed into the specified array of integers 
   * ijk. The number of triangles is nt = ijk.length/3.
   * <p>
   * The returned array of packed (x,y,z) coordinates of triangle centers
   * has length equal to ijk.length. Each center (centroid) is the average
   * of the corresponding triangle vertices.
   * @param ijk array[3*nt] of packed vertex indices.
   * @param xyz array[3*nv] of packed vertex coordinates.
   * @return array[3*nt] of packed center coordinates.
   */
  private float[] computeCenters(int[] ijk, float[] xyz) {
    int nt = ijk.length/3;
    float[] c = new float[3*nt];
    float o3 = 1.0f/3.0f;
    for (int it=0,jt=0,jc=0; it<nt; ++it) {
      int i = 3*ijk[jt++];
      int j = 3*ijk[jt++];
      int k = 3*ijk[jt++];
      float xi = xyz[i+X];
      float yi = xyz[i+Y];
      float zi = xyz[i+Z];
      float xj = xyz[j+X];
      float yj = xyz[j+Y];
      float zj = xyz[j+Z];
      float xk = xyz[k+X];
      float yk = xyz[k+Y];
      float zk = xyz[k+Z];
      c[jc++] = (xi+xj+xk)*o3;
      c[jc++] = (yi+yj+yk)*o3;
      c[jc++] = (zi+zj+zk)*o3;
    }
    return c;
  }

  /**
   * Unindexes the specified indexed triangle vertices. After unindexing,
   * the indices are not valid for the return array of vertex coordinates.
   * @param i array[3*nt] of packed vertex indices, where nt is the number 
   *  of triangles.
   * @param v array[3*nv] of packed vertex coordinates, where nv is the 
   *  number of vertices.
   * @return array[3*nu] of unindexed packed vertex coordinates, where 
   *  nu = 3*nt is the number of unindexed vertices.
   */
  /*
  private float[] unindexVertices(int[] i, float[] v) {
    int nt = i.length/3;
    int nu = 3*nt;
    float[] u = new float[3*nu];
    for (int it=0,jt=0,j=0; it<nt; ++it,j+=9) {
      int iv = 3*i[jt++];
      int jv = 3*i[jt++];
      int kv = 3*i[jt++];
      u[j+0] = v[iv+X];
      u[j+1] = v[iv+Y];
      u[j+2] = v[iv+Z];
      u[j+3] = v[jv+X];
      u[j+4] = v[jv+Y];
      u[j+5] = v[jv+Z];
      u[j+6] = v[kv+X];
      u[j+7] = v[kv+Y];
      u[j+8] = v[kv+Z];
    }
    return u;
  }
  */

  /**
   * Initializes the triangle group states.
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
