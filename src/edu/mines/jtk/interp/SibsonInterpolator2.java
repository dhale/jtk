/****************************************************************************
Copyright (c) 2009, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.interp;

import java.util.ArrayList;

import edu.mines.jtk.dsp.*;
import edu.mines.jtk.mesh.*;
import edu.mines.jtk.util.*;

/**
 * Sibson's natural neighbor interpolation for 2D functions f(x1,x2).
 * Sibson's interpolant at any point (x1,x2) is a weighted sum of values 
 * for a nearby subset of samples, the so-called "natural neighbors" of 
 * that point.
 *
 * The basic Sibson interpolant is C1 (that is, it's gradient is continuous) 
 * at all points (x1,x2) except at the sample points, where it is C0. Sibson
 * als described an extension of his method that is everywhere C1, but that
 * extension is not yet implemented here.
 *
 * The interpolation weights are positive areas of overlapping Voronoi
 * polygons, normalized so that they sum to one for any interpolation 
 * point (x1,x2). Various implementations of Sibson's method differ 
 * primarily in how those areas are computed.
 * 
 * References:
 * Sibson, R., 1981, A brief description of natural neighbor interpolation, 
 * in V. Barnett, ed., Interpreting Multivariate Data, John Wiley and Sons,
 * 21-36.
 * 
 * @author Dave Hale, Colorado School of Mines
 * @version 2009.06.13
 */
public class SibsonInterpolator2 {

  /**
   * The implementation method.
   * The Watson-Sambridge algorithm computes areas using what Watson
   * calls compound signed decomposition.
   * The Braun-Sambridge method uses Lasserre's algorithm to compute 
   * areas of polygons. 
   * The Hale-Liang method uses Voronoi vertices (circumcenters of
   * Delaunay triangles) to compute those areas.
   */
  public enum Method {
    WATSON_SAMBRIDGE,
    BRAUN_SAMBRIDGE,
    HALE_LIANG
  }

  /**
   * Constructs an interpolator with specified method and samples.
   * @param method the method.
   * @param f array of sample values f(x1,x2).
   * @param x1 array of sample x1 coordinates.
   * @param x2 array of sample x2 coordinates.
   */
  public SibsonInterpolator2(
    Method method, float[] f, float[] x1, float[] x2) 
  {
    if (method==Method.WATSON_SAMBRIDGE) {
      _impl = new SambridgeWatsonImpl(f,x1,x2);
    } else if (method==Method.BRAUN_SAMBRIDGE) {
      _impl = new BraunSambridgeImpl(f,x1,x2);
    } else if (method==Method.HALE_LIANG) {
      _impl = new HaleLiangImpl(f,x1,x2);
    }
  }

  /**
   * Constructs an interpolator with specified samples.
   * Uses the most efficient implementation method.
   * @param f array of sample values f(x1,x2).
   * @param x1 array of sample x1 coordinates.
   * @param x2 array of sample x2 coordinates.
   */
  public SibsonInterpolator2(float[] f, float[] x1, float[] x2) {
    this(Method.HALE_LIANG,f,x1,x2);
  }

  /**
   * Sets the null value for this interpolator.
   * This null value is returned when an attempt is made to 
   * interpolate outside the convex hull of sample coordinates.
   * @param fnull the null value.
   */
  public void setNullValue(float fnull) {
    _impl.setNullValue(fnull);
  }

  /**
   * Returns a value interpolated at the specified point.
   * @param x1 the x1 coordinate of the point.
   * @param x2 the x2 coordinate of the point.
   * @return the interpolated value.
   */
  public float interpolate(float x1, float x2) {
    return _impl.interpolate(x1,x2);
  }

  /**
   * Returns an array of interpolated values sampled on a grid.
   * @param s1 the sampling of n1 x1 coordinates.
   * @param s2 the sampling of n2 x2 coordinates.
   * @return array[n2][n1] of interpolated values.
   */
  public float[][] interpolate(Sampling s1, Sampling s2) {
    int n1 = s1.getCount();
    int n2 = s2.getCount();
    float[][] f = new float[n2][n1];
    for (int i2=0; i2<n2; ++i2) {
      float x2 = (float)s2.getValue(i2);
      for (int i1=0; i1<n1; ++i1) {
        float x1 = (float)s1.getValue(i1);
        f[i2][i1] = interpolate(x1,x2);
      }
    }
    return f;
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  // All implementations extend this abstract base class.
  private abstract static class Implementation {
    public abstract float interpolate(float x1, float x2);
    public void setNullValue(float fnull) {
      _fnull = fnull;
    }
    protected float _fnull;
  }

  private Implementation _impl;

  ///////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////////////////////////////////////////////
  private static class SambridgeWatsonImpl extends Implementation {

    public SambridgeWatsonImpl(float[] f, float[] x1, float[] x2) {
      int n = f.length;
      _mesh = new TriMesh();
      for (int i=0; i<n; ++i) {
        TriMesh.Node node = new TriMesh.Node(x1[i],x2[i]);
        if (_mesh.addNode(node)) {
          NodeData ndata = new NodeData();
          node.data = ndata;
          ndata.f = f[i];
        }
      }
      _triList = new TriMesh.TriList();
    }

    public float interpolate(float x1, float x2) {

      // Find natural neighbor tris of the interpolation point (x1,x2). 
      // If none, skip this point (outside convex hull).
      boolean gotTriList = makeTriList(x1,x2);
      if (!gotTriList)
        return _fnull;
      double xp = x1;
      double yp = x2;

      // Sums for numerator and denominator.
      double anum = 0.0;
      double aden = 0.0;
      
      // For all tris in the list, ...
      int ntri = _triList.ntri();
      TriMesh.Tri[] tris = _triList.tris();
      for (int itri=0; itri<ntri; ++itri) {
        TriMesh.Tri tri = tris[itri];

        // The three nodes of one tri.
        TriMesh.Node na = tri.nodeA();
        TriMesh.Node nb = tri.nodeB();
        TriMesh.Node nc = tri.nodeC();
        double xa = na.x(), ya = na.y();
        double xb = nb.x(), yb = nb.y();
        double xc = nc.x(), yc = nc.y();

        // Four circumcenters.
        Geometry.centerCircle(xb,yb,xc,yc,xp,yp,_ca);
        Geometry.centerCircle(xc,yc,xa,ya,xp,yp,_cb);
        Geometry.centerCircle(xa,ya,xb,yb,xp,yp,_cc);
        Geometry.centerCircle(xa,ya,xb,yb,xc,yc,_cv);

        // Accumulate signed areas and float values.
        double aa = area(_cb,_cc,_cv);
        double ab = area(_cc,_ca,_cv);
        double ac = area(_ca,_cb,_cv);
        float fa = ((NodeData)na.data).f;
        float fb = ((NodeData)nb.data).f;
        float fc = ((NodeData)nc.data).f;
        anum += aa*fa+ab*fb+ac*fc;
        aden += aa+ab+ac;
      }

      // The interpolated value.
      return (float)(anum/aden);
    }

    private static class NodeData {
      float f,gx,gy; // function values and gradient
    }

    private TriMesh _mesh; // Delaunay tri mesh
    private double[] _ca = new double[2]; // circumcenter of fake tri bcp
    private double[] _cb = new double[2]; // circumcenter of fake tri cap
    private double[] _cc = new double[2]; // circumcenter of fake tri abp
    private double[] _cv = new double[2]; // circumcenter of tri abc
    private TriMesh.TriList _triList; // list of natural neighbor tris

    private boolean makeTriList(float x, float y) {
      _triList.clear();
      TriMesh.PointLocation pl = _mesh.locatePoint(x,y);
      if (pl.isOutside())
        return false;
      _mesh.clearTriMarks();
      makeTriList(x,y,pl.tri());
      return true;
    }
    private void makeTriList(double xp, double yp, TriMesh.Tri tri) {
      if (tri==null || _mesh.isMarked(tri))
        return;
      _mesh.mark(tri);
      if (inCircle(xp,yp,tri)) {
        _triList.add(tri);
        makeTriList(xp,yp,tri.triA());
        makeTriList(xp,yp,tri.triB());
        makeTriList(xp,yp,tri.triC());
      }
    }
    private double area(double[] cj, double[] ck, double[] cv) {
      double xj = cj[0]-cv[0];
      double yj = cj[1]-cv[1];
      double xk = ck[0]-cv[0];
      double yk = ck[1]-cv[1];
      return 0.5*(xj*yk-yj*xk);
    }
    private boolean inCircle(double xp, double yp, TriMesh.Tri tri) {
      if (tri==null)
        return false;
      TriMesh.Node na = tri.nodeA();
      TriMesh.Node nb = tri.nodeB();
      TriMesh.Node nc = tri.nodeC();
      double xa = na.x(), ya = na.y();
      double xb = nb.x(), yb = nb.y();
      double xc = nc.x(), yc = nc.y();
      return Geometry.inCircleFast(xa,ya,xb,yb,xc,yc,xp,yp)>0;
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////////////////////////////////////////////
  private static class BraunSambridgeImpl extends Implementation {

    public BraunSambridgeImpl(float[] f, float[] x1, float[] x2) {
      int n = f.length;
      _mesh = new TriMesh();
      for (int i=0; i<n; ++i) {
        TriMesh.Node node = new TriMesh.Node(x1[i],x2[i]);
        if (_mesh.addNode(node)) {
          NodeData ndata = new NodeData();
          node.data = ndata;
          ndata.f = f[i];
        }
      }
      int nnode = _mesh.countNodes();
      TriMesh.NodeIterator ni = _mesh.getNodes();
      while (ni.hasNext()) {
        TriMesh.Node node = ni.next();
        NodeData ndata = (NodeData)node.data;
        ndata.nabors = _mesh.getNodeNabors(node);
      }
      _nodeList = new TriMesh.NodeList();
    }

    public float interpolate(float x1, float x2) {
      double x1i = x1;
      double x2i = x2;

      // Find all natural neighbors of the interpolation point (x1,x2). 
      // If none, skip this point (outside convex hull).
      boolean gotNabors = getNaturalNabors(x1,x2);
      if (!gotNabors)
        return _fnull;

      // Sums for numerator and denominator.
      double anum = 0.0;
      double aden = 0.0;

      // For all natural neighbors, ...
      int nnabor = _nodeList.nnode();
      TriMesh.Node[] nabors = _nodeList.nodes();
      for (int j=0; j<nnabor; ++j) {
        TriMesh.Node jnode = nabors[j];
        double x1j = jnode.x();
        double x2j = jnode.y();
        double fj = ((NodeData)jnode.data).f;

        // Clear the polygon for the j'th natural neighbor.
        _lv.clear();

        // Add half-plane of points closer to (x1i,x2i) than (x1j,x2j).
        // For this half-plane we set b = 0, which is equivalent to 
        // making the midpoint (x1s,x2s) the origin for the polygon.
        // A half-space with b = 0 speeds up Lassere's algorithm.
        double x1s = 0.5*(x1j+x1i);
        double x2s = 0.5*(x2j+x2i);
        double x1d = x1j-x1i;
        double x2d = x2j-x2i;
        _lv.addHalfSpace(x1d,x2d,0.0); // note b = 0 here

        // For all other natural neighbors, ...
        for (int k=0; k<nnabor; ++k) {
          if (j==k) continue;
          TriMesh.Node knode = nabors[k];

          // Skip pair if they are not node neighbors in the mesh.
          if (!nodesAreNabors(jnode,knode))
            continue;

          // Add half-plane of points closer to (x1j,x2j) than (x1k,x2k).
          // Here we must account for the shift (x1s,x2s) described above.
          double x1k = knode.x();
          double x2k = knode.y();
          double x1e = x1k-x1j;
          double x2e = x2k-x2j;
          double x1t = 0.5*(x1k+x1j)-x1s;
          double x2t = 0.5*(x2k+x2j)-x2s;
          _lv.addHalfSpace(x1e,x2e,x1e*x1t+x2e*x2t);
        }

        // Area of polygon for this natural neighbor.
        double aj = _lv.getVolume();

        // Accumulate numerator and denominator.
        anum += aj*fj;
        aden += aj;
      }

      // The interpolated value.
      return (float)(anum/aden);
    }
    private static float f(TriMesh.Node node) {
      return ((NodeData)node.data).f;
    }
    private boolean nodesAreNabors(TriMesh.Node jnode, TriMesh.Node knode) {
      TriMesh.Node[] nabors = ((NodeData)jnode.data).nabors;
      int nnabor = nabors.length;
      for (int jnabor=0; jnabor<nnabor; ++jnabor)
        if (nabors[jnabor]==knode)
          return true;
      return false;
    }

    private static class NodeData {
      float f,gx,gy; // function values and gradient
      TriMesh.Node[] nabors; // cached node nabors
    }

    private float _fnull; // null value when interpolation impossible
    private TriMesh _mesh; // Delaunay tri mesh
    private TriMesh.NodeList _nodeList; // natural neighbor nodes
    private LasserreVolume _lv = new LasserreVolume(2); // for polygon areas
    private double[] _center = new double[2];
    private boolean getNaturalNabors(float x, float y) {
      _nodeList.clear();
      TriMesh.PointLocation pl = _mesh.locatePoint(x,y);
      if (pl.isOutside())
        return false;
      _mesh.clearNodeMarks();
      _mesh.clearTriMarks();
      getNaturalNeighbors(x,y,pl.tri());
      return true;
    }
    private void getNaturalNeighbors(
      double xp, double yp, TriMesh.Tri tri) 
    {
      if (tri!=null && !_mesh.isMarked(tri)) {
        _mesh.mark(tri);
        double rs = tri.centerCircle(_center);
        double xc = _center[0];
        double yc = _center[1];
        double dx = xp-xc;
        double dy = yp-yc;
        if (dx*dx+dy*dy<rs) {
          TriMesh.Node na = tri.nodeA(), nb = tri.nodeB(), nc = tri.nodeC();
          TriMesh.Tri  ta = tri.triA(),  tb = tri.triB(),  tc = tri.triC();
          if (!_mesh.isMarked(na)) {
            _mesh.mark(na); 
            _nodeList.add(na);
          }
          if (!_mesh.isMarked(nb)) {
            _mesh.mark(nb); 
            _nodeList.add(nb);
          }
          if (!_mesh.isMarked(nc)) {
            _mesh.mark(nc); 
            _nodeList.add(nc);
          }
          getNaturalNeighbors(xp,yp,ta);
          getNaturalNeighbors(xp,yp,tb);
          getNaturalNeighbors(xp,yp,tc);
        }
      }
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////////////////////////////////////////////
  // A two-step method developed by Dave Hale and Luming Liang at Colorado
  // School of Mines. The first step is to build lists of natural-neighbor
  // tris and nodes, while also computing and storing information such as
  // Voronoi vertices, circumcenters of natural-neighbor triangles, and a 
  // local origin for each natural-neighbor node. In the second step, this
  // information is used to compute polygon areas, which are then used to
  // compute an area-weighted average of natural-neighbor sample values.
  private static class HaleLiangImpl extends Implementation {

    public HaleLiangImpl(float[] f, float[] x1, float[] x2) {
      int n = f.length;
      _mesh = new TriMesh();
      for (int i=0; i<n; ++i) {
        TriMesh.Node node = new TriMesh.Node(x1[i],x2[i]);
        if (_mesh.addNode(node)) {
          NodeData ndata = new NodeData();
          node.data = ndata;
          ndata.f = f[i];
        }
      }
      TriMesh.TriIterator ti = _mesh.getTris();
      while (ti.hasNext()) {
        TriMesh.Tri tri = ti.next();
        TriData tdata = new TriData();
        tri.data = tdata;
        tdata.rs = tri.centerCircle(_center);
        tdata.xt = _center[0];
        tdata.yt = _center[1];
      }
      _nodeList = new TriMesh.NodeList();
      _triList = new TriMesh.TriList();
    }

    public float interpolate(float x1, float x2) {
      boolean gotLists = makeLists(x1,x2);
      if (!gotLists)
        return _fnull;
      return processLists();
    }

    private static class NodeData {
      float f,gx,gy; // function values and gradient
      double x0,y0; // local origins when computing polygon areas
      double area; // area for Sibson weight
    }

    private static class TriData {
      double rs; // radius squared of circumcircle of tri
      double xt,yt; // center of tri
      double xa,ya; // center opposite node A
      double xb,yb; // center opposite node B
      double xc,yc; // center opposite node C
      boolean ra,rb,rc; // true, if corresponding center is for a real tri
    }

    private float _fnull; // null value
    private TriMesh _mesh; // the Delaunay tri mesh
    private TriMesh.NodeList _nodeList; // list of natural neighbors
    private TriMesh.TriList _triList; // list of natural neighbor tris
    private double[] _center = new double[2]; // a circumcircle center

    // Recursively makes lists of natural neighbor tris and nodes for the
    // point (x,y). As each tri is added to the list, a set of four 
    // circumcenters are stored in its Object data. One of these is the 
    // circumcenter of the tri. The three other circumcenters correspond 
    // to the three neighbor tris opposite the tri's nodes A, B, and C.
    // For example, if the circumcircle of a non-null neighbor tri opposite
    // node A contains the point (x,y), then the corresponding circumcenter 
    // of that neighbor tri is stored in the set. Otherwise, the circumcenter
    // of a virtual tri (not in the mesh), comprising the point (x,y) and
    // the nodes B and C, is stored. 
    // Circumcenters of virtual tris are vertices of a virtual Voronoi 
    // polygon for the point (x,y). While building the tri list, this 
    // method also assigns to their nodes points (x0,y0) that will serve
    // as local origins when accumulating polygon areas. The nodes are 
    // also added to the node list.
    private boolean makeLists(float x, float y) {
      _nodeList.clear();
      _triList.clear();
      TriMesh.PointLocation pl = _mesh.locatePoint(x,y);
      if (pl.isOutside())
        return false;
      _mesh.clearNodeMarks();
      _mesh.clearTriMarks();
      makeLists(x,y,pl.tri());
      return true;
    }
    private void makeLists(double xp, double yp, TriMesh.Tri tri) {
      if (tri==null || _mesh.isMarked(tri))
        return;
      TriData tdata = (TriData)tri.data;
      _mesh.mark(tri);
      _triList.add(tri);

      // Prepare natural neighbor nodes for interpolation.
      TriMesh.Node na = tri.nodeA();
      TriMesh.Node nb = tri.nodeB();
      TriMesh.Node nc = tri.nodeC();
      prepareNode(na);
      prepareNode(nb);
      prepareNode(nc);
      
      // Neighbor tri opposite node A, may not be a real tri in the mesh.
      TriMesh.Tri ta = tri.triA();
      boolean ra = tdata.ra = inCircle(xp,yp,ta);
      if (!ra) {
        double xb = nb.x(), yb = nb.y();
        double xc = nc.x(), yc = nc.y();
        Geometry.centerCircle(xp,yp,xb,yb,xc,yc,_center);
        tdata.xa = _center[0];
        tdata.ya = _center[1];
        setOrigin(nb,tdata.xa,tdata.ya);
        setOrigin(nc,tdata.xa,tdata.ya);
      } else {
        TriData adata = (TriData)ta.data;
        tdata.xa = adata.xt;
        tdata.ya = adata.yt;
      }

      // Neighbor tri opposite node B, may not be a real tri in the mesh.
      TriMesh.Tri tb = tri.triB();
      boolean rb = tdata.rb = inCircle(xp,yp,tb);
      if (!rb) {
        double xc = nc.x(), yc = nc.y();
        double xa = na.x(), ya = na.y();
        Geometry.centerCircle(xp,yp,xc,yc,xa,ya,_center);
        tdata.xb = _center[0];
        tdata.yb = _center[1];
        setOrigin(nc,tdata.xb,tdata.yb);
        setOrigin(na,tdata.xb,tdata.yb);
      } else {
        TriData bdata = (TriData)tb.data;
        tdata.xb = bdata.xt;
        tdata.yb = bdata.yt;
      }

      // Neighbor tri opposite node C; may not be a real tri in the mesh.
      TriMesh.Tri tc = tri.triC();
      boolean rc = tdata.rc = inCircle(xp,yp,tc);
      if (!rc) {
        double xa = na.x(), ya = na.y();
        double xb = nb.x(), yb = nb.y();
        Geometry.centerCircle(xp,yp,xa,ya,xb,yb,_center);
        tdata.xc = _center[0];
        tdata.yc = _center[1];
        setOrigin(na,tdata.xc,tdata.yc);
        setOrigin(nb,tdata.xc,tdata.yc);
      } else {
        TriData cdata = (TriData)tc.data;
        tdata.xc = cdata.xt;
        tdata.yc = cdata.yt;
      }

      // Recursively add any real (not virtual) neighbor tris to the list.
      if (ra) makeLists(xp,yp,ta);
      if (rb) makeLists(xp,yp,tb);
      if (rc) makeLists(xp,yp,tc);
    }

    // Sets the locate origin for the specified node.
    private static void setOrigin(TriMesh.Node node, double x0, double y0) {
      NodeData nd = (NodeData)node.data;
      nd.x0 = x0;
      nd.y0 = y0;
    }

    // If unmarked, prepare a natural neighbor node for interpolation.
    private void prepareNode(TriMesh.Node node) {
      if (!_mesh.isMarked(node)) {
        _mesh.mark(node);
        _nodeList.add(node);
        NodeData ndata = (NodeData)node.data;
        ndata.area = 0.0;
      }
    }

    // Returns true if point (xp,yp) is inside the circumcircle of the tri.
    private boolean inCircle(double xp, double yp, TriMesh.Tri tri) {
      if (tri==null)
        return false;
      TriData tdata = (TriData)tri.data;
      double rs = tdata.rs;
      double xt = tdata.xt;
      double yt = tdata.yt;
      double dx = xp-xt;
      double dy = yp-yt;
      return dx*dx+dy*dy<rs;
    }

    // Processes the natural neighbor tri and node lists. Computes 
    // areas of polygons associated with each natural neighbor node,
    // and then uses those areas to weight natural neighbor values.
    private float processLists() {
      int ntri = _triList.ntri();
      TriMesh.Tri[] tris = _triList.tris();
      for (int itri=0; itri<ntri; ++itri) {
        TriMesh.Tri tri = tris[itri];
        TriMesh.Node na = tri.nodeA();
        TriMesh.Node nb = tri.nodeB();
        TriMesh.Node nc = tri.nodeC();
        TriData tdata = (TriData)tri.data;
        NodeData nadata = (NodeData)na.data;
        NodeData nbdata = (NodeData)nb.data;
        NodeData ncdata = (NodeData)nc.data;
        double xa = tdata.xa, ya = tdata.ya;
        double xb = tdata.xb, yb = tdata.yb;
        double xc = tdata.xc, yc = tdata.yc;
        double xt = tdata.xt, yt = tdata.yt;
        boolean ra = tdata.ra, rb = tdata.rb, rc = tdata.rc;
        double xa0 = nadata.x0, ya0 = nadata.y0;
        double xb0 = nbdata.x0, yb0 = nbdata.y0;
        double xc0 = ncdata.x0, yc0 = ncdata.y0;
        double fa = nadata.f, fb = nbdata.f, fc = ncdata.f;
        double x1,y1,x2,y2,area;

        // Edge a->b (and perhaps b->a), opposite node c.
        x1 = xc-xa0; y1 = yc-ya0;
        x2 = xt-xa0; y2 = yt-ya0;
        area = x1*y2-x2*y1;
        nadata.area += area;
        if (!rc) {
          x1 = xt-xb0; y1 = yt-yb0;
          x2 = xc-xb0; y2 = yc-yb0;
          area = x1*y2-x2*y1;
          nbdata.area += area;
        }

        // Edge b->c (and perhaps c->b), opposite node a.
        x1 = xa-xb0; y1 = ya-yb0;
        x2 = xt-xb0; y2 = yt-yb0;
        area = x1*y2-x2*y1;
        nbdata.area += area;
        if (!ra) {
          x1 = xt-xc0; y1 = yt-yc0;
          x2 = xa-xc0; y2 = ya-yc0;
          area = x1*y2-x2*y1;
          ncdata.area += area;
        }

        // Edge c->a (and perhaps a->c), opposite node b.
        x1 = xb-xc0; y1 = yb-yc0;
        x2 = xt-xc0; y2 = yt-yc0;
        area = x1*y2-x2*y1;
        ncdata.area += area;
        if (!rb) {
          x1 = xt-xa0; y1 = yt-ya0;
          x2 = xb-xa0; y2 = yb-ya0;
          area = x1*y2-x2*y1;
          nadata.area += area;
        }
      }

      // Return area-weighted sum of values.
      double anum = 0.0;
      double aden = 0.0;
      int nnode = _nodeList.nnode();
      TriMesh.Node[] nodes = _nodeList.nodes();
      for (int inode=0; inode<nnode; ++inode) {
        TriMesh.Node node = nodes[inode];
        NodeData ndata = (NodeData)node.data;
        float f = ndata.f;
        double area = ndata.area;
        anum += area*f;
        aden += area;
      }
      return (float)(anum/aden);
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // testing

  public static void main(String[] args) {
    float[][] fx = makeRandomData(100);
    float[] f = fx[0];
    float[] x1 = fx[1];
    float[] x2 = fx[2];
    int n1 = 501; double d1 = 0.5/(n1-1); double f1 = 0.25;
    int n2 = 501; double d2 = 0.5/(n2-1); double f2 = 0.25;
    Sampling s1 = new Sampling(n1,d1,f1);
    Sampling s2 = new Sampling(n2,d2,f2);
    float[][] g;
    Stopwatch sw = new Stopwatch();
    for (int iter=0; iter<1; ++iter) {
      sw.restart();
      g = testInterpolateNew(f,x1,x2,s1,s2);
      sw.stop();
      System.out.println("new: "+sw.time());
      if (iter==0) plot(g);
      sw.restart();
      g = testInterpolateOld(f,x1,x2,s1,s2);
      sw.stop();
      System.out.println("old: "+sw.time());
      if (iter==0) plot(g);
    }
  }
  private static float[][] makeRandomData(int n) {
    java.util.Random r = new java.util.Random();
    float[] f = new float[n];
    float[] x1 = new float[n];
    float[] x2 = new float[n];
    for (int i=0; i<n; ++i) {
      x1[i] = r.nextFloat();
      x2[i] = r.nextFloat();
      f[i] = (float)(Math.sin(Math.PI*x1[i])*Math.sin(Math.PI*x2[i]));
    }
    return new float[][]{f,x1,x2};
  }
  private static float[][] testInterpolateNew(
    float[] f, float[] x1, float[] x2, Sampling s1, Sampling s2)
  {
    SibsonInterpolator2.Method method = 
      SibsonInterpolator2.Method.WATSON_SAMBRIDGE;
      //SibsonInterpolator2.Method.BRAUN_SAMBRIDGE;
      //SibsonInterpolator2.Method.HALE_LIANG;
    SibsonInterpolator2 si = new SibsonInterpolator2(method,f,x1,x2);
    float[][] g = si.interpolate(s1,s2);
    return g;
  }
  private static float[][] testInterpolateOld(
    float[] f, float[] x1, float[] x2, Sampling s1, Sampling s2)
  {
    float[][] g = interpolateSibsonOld(f,x1,x2,s1,s2);
    return g;
  }
  private static float[][] interpolateSibsonOld(
    float[] f, float[] x, float[] y, Sampling sx, Sampling sy)
  {
    int n = x.length;
    int nx = sx.getCount();
    int ny = sy.getCount();
    float[][] fi = new float[ny][nx];
    TriMesh mesh = new TriMesh();
    TriMesh.NodePropertyMap zmap = mesh.getNodePropertyMap("z");
    for (int i=0; i<n; ++i) {
      TriMesh.Node node = new TriMesh.Node(x[i],y[i]);
      if (mesh.addNode(node))
        zmap.put(node,new Float(f[i]));
    }
    float znull = 0.0f; // values assigned to points outside convex hull
    for (int iy=0; iy<ny; ++iy) {
      float yi = (float)sy.getValue(iy);
      for (int ix=0; ix<nx; ++ix) {
        float xi = (float)sx.getValue(ix);
        fi[iy][ix] = mesh.interpolateSibson(xi,yi,zmap,znull);
      }
    }
    return fi;
  }
  private static void plot(float[][] g) {
    System.out.println("min="+Array.min(g)+" max="+Array.max(g));
    edu.mines.jtk.mosaic.SimplePlot sp = 
      new edu.mines.jtk.mosaic.SimplePlot();
    edu.mines.jtk.mosaic.PixelsView pv = sp.addPixels(g);
    pv.setColorModel(edu.mines.jtk.awt.ColorMap.PRISM);
  }
}
