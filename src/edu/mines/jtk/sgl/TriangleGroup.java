/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import java.nio.FloatBuffer;
import java.util.HashMap;
import static java.lang.Math.*;

import edu.mines.jtk.util.*;
import static edu.mines.jtk.opengl.Gl.*;

// For testing only.
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import edu.mines.jtk.gui.*;

/**
 * A group of triangles that represents a triangulated surface.
 * Triangles can be specified by only the (x,y,z) coordinates of
 * their vertices. Then
 * 
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
   * For each vertex, this method computes a normal vector as an 
   * area-weighted average of the normal vectors for each triangle 
   * that references that vertex.
   * @param xyz array[3*nv] of packed vertex coordinates.
   */
  public TriangleGroup(float[] xyz) {
    this(xyz,null);
  }

  /**
   * Constructs a triangle group with specified vertex coordinates.
   * <p>
   * The (x,y,z) coordinates of vertices are packed into the specified 
   * array xyz. The number of vertices is nv = xyz.length/3. The number
   * of triangles is nt = nv/3 = xyz.length/9.
   * <p>
   * The (r,g,b) components of colors are packed into the specified 
   * array rgb. The number of colors is nc = rgb.length/3. The number
   * of triangles is nt = nc/3 = rgb.length/9.
   * <p>
   * For each vertex, this method computes a normal vector as an 
   * area-weighted average of the normal vectors for each triangle 
   * that references that vertex.
   * @param xyz array[3*nv] of packed vertex coordinates.
   * @param rgb array[3*nc] of packed color components.
   */
  public TriangleGroup(float[] xyz, float[] rgb) {
    int[] ijk = indexVertices(xyz);
    float[] uvw = computeNormals(ijk,xyz);
    buildTree(ijk,xyz,uvw,rgb);
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
   * For each vertex, this method computes a normal vector as an 
   * area-weighted average of the normal vectors for each triangle 
   * that references that vertex.
   * @param ijk array[3*nt] of packed vertex indices.
   * @param xyz array[3*nv] of packed vertex coordinates.
   */
  public TriangleGroup(int[] ijk, float[] xyz) {
    this(ijk,xyz,null);
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
   * The (r,g,b) components of colors are packed into the specified 
   * array rgb. The number of colors is nc = rgb.length/3.
   * <p>
   * For each vertex, this method computes a normal vector as an 
   * area-weighted average of the normal vectors for each triangle 
   * that references that vertex.
   * @param ijk array[3*nt] of packed vertex indices.
   * @param xyz array[3*nv] of packed vertex coordinates.
   * @param rgb array[3*nc] of packed color components.
   */
  public TriangleGroup(int[] ijk, float[] xyz, float[] rgb) {
    float[] uvw = computeNormals(ijk,xyz);
    buildTree(ijk,xyz,uvw,rgb);
  }

  ///////////////////////////////////////////////////////////////////////////
  // protected

  protected void selectedChanged() {
    System.out.println("TriangleGroup: "+this+" selected="+isSelected());
    dirtyDraw();
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private static final int I = 0,  J = 1,  K = 2;
  private static final int X = 0,  Y = 1,  Z = 2;
  private static final int U = 0,  V = 1,  W = 2;
  private static final int R = 0,  G = 1,  B = 2;

  private static final int MIN_TRI_PER_NODE = 1024;

  /**
   * Recursively builds the binary tree of triangle nodes.
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

    ///////////////////////////////////////////////////////////////////////////
    // protected

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

    protected void pick(PickContext pc) {
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

    ///////////////////////////////////////////////////////////////////////////
    // private
    
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
    public boolean equals(Vertex v) {
      return x==v.x &&  y==v.y && z==v.z;
    }
    public int hashCode() {
      return Float.floatToIntBits(x) ^
             Float.floatToIntBits(y) ^
             Float.floatToIntBits(z);
    }
  }

  /**
   * Computes indices ijk for triangle vertex coordinates xyz.
   * <p>
   * The (x,y,z) coordinates of vertices are packed into the specified 
   * array xyz. The number of vertices is nv = xyz.length/3. The number 
   * of triangles is nt = xyz.length/9.
   * <p>
   * For each triangle, this method computes a triplet (i,j,k) of integer 
   * vertex indices. A vertex index is an integer in the range [0,nu-1],
   * where nu equals the number of unique vertices. That number nu cannot 
   * exceed, but may be less than, the number nv of specified vertices.
   * <p>
   * The number nu of unique vertices is less than the number nv of 
   * vertices when the same triplet of vertex (x,y,z) coordinates occurs 
   * multiple times in the array xyz. In this case, only the index of the 
   * first occurence will be present in the returned array of indices.
   * <p>
   * Triplets of indices (i,j,k), one triplet per triangle, are packed 
   * into the returned array of integers ijk, which has length 3*nt.
   * @param xyz array[3*nv] of packed triangle vertex coordinates.
   * @return an array[3*nt] of packed integer vertex indices.
   */
  private static int[] indexVertices(float[] xyz) {

    // Number of vertices and triangles (not necessarily unique).
    int nv = xyz.length/3;
    int nt = nv/3;

    // Array of vertex indices, three per triangle.
    int[] ijk = new int[3*nt];

    // Map each unique vertex to a unique index.
    HashMap<Vertex,Integer> vimap = new HashMap<Vertex,Integer>(nv);

    // Number of unique vertices.
    int nu = 0;

    // For each triangle, ...
    for (int it=0; it<nt; ++it) {
      for (int iv=0,jv=3*it,kv=3*jv; iv<3; ++iv,++jv,kv+=3) {
        Vertex v = new Vertex(xyz[kv+X],xyz[kv+Y],xyz[kv+Z]);
        Integer i = vimap.get(v);
        if (i==null) {
          i = new Integer(nu);
          vimap.put(v,i);
          ++nu;
        }
        ijk[jv] = i.intValue();
      }
    }
    return ijk;
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
   * @param ijk array[3*nt] of packed integer vertex indices.
   * @param xyz array[3*nv] of packed triangle vertex coordinates.
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
   * Unindexes the specified indexed triangle vertices. After unindexing,
   * the indices are not valid for the return array of vertex coordinates.
   * @param i array[3*nt] of packed integer vertex indices, where nt is
   *  the number of triangles.
   * @param v array[3*nv] of packed triangle vertex coordinates, where 
   *  nv is the number of vertices.
   * @return array[3*nu] of unindexed packed triangle vertex coordinates,
   *  where nu = 3*nt is the number of unindexed triangle vertices.
   */
  private float[] unindexVertices(int[] i, float[] v) {
    int nv = v.length/3;
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
   * @param ijk array[3*nt] of packed integer vertex indices.
   * @param xyz array[3*nv] of packed triangle vertex coordinates.
   * @return array[3*nt] of packed triangle center coordinates.
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

  ///////////////////////////////////////////////////////////////////////////
  // testing

  private static final int SIZE = 600;
  private static float sin(float a, float b, float x, float y) {
    return (float)(5.0+a*Math.sin(b*(x+y)));
  }
  private static float[] makeSineWave() {
    int nx = 128;
    int ny = 128;
    float a = 0.05f*(float)(nx+ny);
    float b = 20.0f/(float)(nx+ny);
    float[] xyz = new float[3*6*nx*ny];
    for (int ix=0,i=0; ix<nx; ++ix) {
      float x0 = (float)ix;
      float x1 = x0+1.0f;
      for (int iy=0; iy<ny; ++iy) {
        float y0 = (float)iy;
        float y1 = y0+1.0f;
        xyz[i++] = x0;  xyz[i++] = y0;  xyz[i++] = sin(a,b,x0,y0);
        xyz[i++] = x0;  xyz[i++] = y1;  xyz[i++] = sin(a,b,x0,y1);
        xyz[i++] = x1;  xyz[i++] = y0;  xyz[i++] = sin(a,b,x1,y0);
        xyz[i++] = x1;  xyz[i++] = y0;  xyz[i++] = sin(a,b,x1,y0);
        xyz[i++] = x0;  xyz[i++] = y1;  xyz[i++] = sin(a,b,x0,y1);
        xyz[i++] = x1;  xyz[i++] = y1;  xyz[i++] = sin(a,b,x1,y1);
      }
    }
    return xyz;
  }
  private static float[] makeColors(float[] xyz) {
    int nv = xyz.length/3;
    float[] rgb = new float[3*nv];
    for (int iv=0,jv=0,jc=0; iv<nv; ++iv) {
      float x = xyz[jv++];
      float y = xyz[jv++];
      float z = xyz[jv++];
      float s = 1.0f/(float)Math.sqrt(x*x+y*y+z*z);
      rgb[jc++] = x*s;
      rgb[jc++] = y*s;
      rgb[jc++] = z*s;
    }
    return rgb;
  }
  public static void main(String[] args) {
    float[] xyz = makeSineWave();
    float[] rgb = makeColors(xyz);
    TriangleGroup tg = new TriangleGroup(xyz,rgb);

    StateSet states = new StateSet();
    //ColorState cs = new ColorState();
    //cs.setColor(Color.CYAN);
    //states.add(cs);
    LightModelState lms = new LightModelState();
    lms.setTwoSide(true);
    states.add(lms);
    MaterialState ms = new MaterialState();
    ms.setColorMaterial(GL_AMBIENT_AND_DIFFUSE);
    ms.setSpecular(Color.white);
    ms.setShininess(100.0f);
    states.add(ms);
    tg.setStates(states);

    World world = new World();
    world.addChild(tg);

    OrbitView view = new OrbitView(world);
    view.setAxesOrientation(View.AxesOrientation.XRIGHT_YOUT_ZDOWN);
    ViewCanvas canvas = new ViewCanvas(view);
    canvas.setView(view);

    ModeManager mm = new ModeManager();
    mm.add(canvas);
    OrbitViewMode ovm = new OrbitViewMode(mm);
    SelectDragMode sdm = new SelectDragMode(mm);

    JPopupMenu.setDefaultLightWeightPopupEnabled(false);
    ToolTipManager.sharedInstance().setLightWeightPopupEnabled(false);

    JMenu fileMenu = new JMenu("File");
    fileMenu.setMnemonic('F');
    Action exitAction = new AbstractAction("Exit") {
      public void actionPerformed(ActionEvent event) {
        System.exit(0);
      }
    };
    JMenuItem exitItem = fileMenu.add(exitAction);
    exitItem.setMnemonic('x');

    JMenu modeMenu = new JMenu("Mode");
    modeMenu.setMnemonic('M');
    JMenuItem ovmItem = new ModeMenuItem(ovm);
    modeMenu.add(ovmItem);
    JMenuItem sdmItem = new ModeMenuItem(sdm);
    modeMenu.add(sdmItem);

    JMenuBar menuBar = new JMenuBar();
    menuBar.add(fileMenu);
    menuBar.add(modeMenu);

    JToolBar toolBar = new JToolBar(SwingConstants.VERTICAL);
    toolBar.setRollover(true);
    JToggleButton ovmButton = new ModeToggleButton(ovm);
    toolBar.add(ovmButton);
    JToggleButton sdmButton = new ModeToggleButton(sdm);
    toolBar.add(sdmButton);

    ovm.setActive(true);

    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(new Dimension(SIZE,SIZE));
    frame.add(canvas,BorderLayout.CENTER);
    frame.add(toolBar,BorderLayout.WEST);
    frame.setJMenuBar(menuBar);
    frame.setVisible(true);
  }
}
