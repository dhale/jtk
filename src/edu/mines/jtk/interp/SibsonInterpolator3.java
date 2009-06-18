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
 * Sibson interpolation of scattered samples of 3D functions f(x1,x2,x3).
 * Sibson's (1981) interpolant at any point (x1,x2,x3) is a weighted sum of 
 * values for a nearby subset of samples, the so-called natural neighbors
 * of that point. Sibson interpolation is often called "natural neighbor
 * interpolation."
 *
 * The basic Sibson interpolant is C1 --- it's gradient is continuous --- at 
 * all points (x1,x2,x3) except at the sample points, where it is C0. Sibson
 * (1981) also described an extension of his method that is everywhere C1 
 * (and therefore smoother), but that extension is not yet implemented here.
 *
 * The interpolation weights, also called "Sibson coordinates", are volumes 
 * of overlapping Voronoi polyhedra, normalized so that they sum to one for 
 * any interpolation point (x1,x2,x3). Various implementations of Sibson 
 * interpolation differ primarily in how those volumes are computed.
 * 
 * References:
 * <ul><li>
 * Braun, J. and M. Sambridge, 1995, A numerical method for solving partial
 * differential equations on highly irregular evolving grids: Nature, 376,
 * 655--660.
 * </li><li>
 * Lasserre J.B., 1983, An analytical expression and an algorithm for the 
 * volume of a convex polyhedron in R^n: Journal of Optimization Theory 
 * and Applications, 39, 363--377.
 * </li><li>
 * Sambridge, M., J. Braun, and H. McQueen, 1995, Geophysical
 * parameterization and interpolation of irregular data using
 * natural neighbors.
 * </li><li>
 * Sibson, R., 1981, A brief description of natural neighbor interpolation, 
 * in V. Barnett, ed., Interpreting Multivariate Data, John Wiley and Sons,
 * 21--36.
 * </li><li>
 * Watson, D.F., 1992, Contouring: a guide to the analysis and display
 * of spatial data: Pergamon, Oxford.
 * </li></ul>
 * 
 * @author Dave Hale, Colorado School of Mines
 * @version 2009.06.14
 */
public class SibsonInterpolator3 {

  /**
   * The implementation method. Methods differ in the algorithm by which 
   * Sibson coordinates (polyhedral volumes) are computed for natural 
   * neighbors.
   */
  public enum Method {
   /**
    * The Hale-Liang method.
    * Developed by Dave Hale and Luming Liang at the Colorado School of
    * Mines. Accurate and fast, this method is the default.
    */
    HALE_LIANG,
   /**
    * The Braun-Sambridge method. Developed by Braun and Sambridge (1995),
    * this method uses Lasserre's (1983) algorithm for computing the volumes 
    * of polyhedra without computing their vertices. This method is slower
    * slower than the other methods provided here. It may also suffer from 
    * numerical instability due to divisions in Lasserre's algorithm.
    */
    BRAUN_SAMBRIDGE,
   /**
    * The Watson-Sambridge algorithm. Developed by Watson (1992) and
    * described further by Sambridge et al. (1995). Though simplest to
    * implement, this method is inaccurate (sometimes wildly so) at 
    * interpolation points (x1,x2,x3) that lie on or near edges of a 
    * Delaunay triangulation of the scattered sample points. 
    */
    WATSON_SAMBRIDGE,
  }

  /**
   * Constructs an interpolator with specified samples.
   * Uses the most accurate and efficient implementation.
   * @param f array of sample values f(x1,x2).
   * @param x1 array of sample x1 coordinates.
   * @param x2 array of sample x2 coordinates.
   * @param x3 array of sample x3 coordinates.
   */
  public SibsonInterpolator3(float[] f, float[] x1, float[] x2, float[] x3) {
    this(Method.HALE_LIANG,f,x1,x2,x3);
  }

  /**
   * Constructs an interpolator with specified method and samples.
   * This constructor is provided primarily for testing.
   * The default Hale-Liang method is fast and accurate.
   * @param method the implementation method.
   * @param f array of sample values f(x1,x2).
   * @param x1 array of sample x1 coordinates.
   * @param x2 array of sample x2 coordinates.
   * @param x3 array of sample x3 coordinates.
   */
  public SibsonInterpolator3(
    Method method, float[] f, float[] x1, float[] x2, float[] x3) 
  {
    if (method==Method.WATSON_SAMBRIDGE) {
      _impl = new WatsonSambridgeImpl(f,x1,x2,x3);
    } else if (method==Method.BRAUN_SAMBRIDGE) {
      _impl = new BraunSambridgeImpl(f,x1,x2,x3);
    } else if (method==Method.HALE_LIANG) {
      _impl = new HaleLiangImpl(f,x1,x2,x3);
    }
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
   * @param x3 the x3 coordinate of the point.
   * @return the interpolated value.
   */
  public float interpolate(float x1, float x2, float x3) {
    return _impl.interpolate(x1,x2,x3);
  }

  /**
   * Returns an array of interpolated values sampled on a grid.
   * @param s1 the sampling of n1 x1 coordinates.
   * @param s2 the sampling of n2 x2 coordinates.
   * @param s3 the sampling of n3 x3 coordinates.
   * @return array[n3][n2][n1] of interpolated values.
   */
  public float[][][] interpolate(Sampling s1, Sampling s2, Sampling s3) {
    int n1 = s1.getCount();
    int n2 = s2.getCount();
    int n3 = s2.getCount();
    float[][][] f = new float[n3][n2][n1];
    for (int i3=0; i3<n3; ++i3) {
      float x3 = (float)s3.getValue(i3);
      for (int i2=0; i2<n2; ++i2) {
        float x2 = (float)s2.getValue(i2);
        for (int i1=0; i1<n1; ++i1) {
          float x1 = (float)s1.getValue(i1);
          f[i3][i2][i1] = interpolate(x1,x2,x3);
        }
      }
    }
    return f;
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  // All implementations extend this abstract base class.
  private abstract static class Implementation {
    public abstract float interpolate(float x1, float x2, float x3);
    public void setNullValue(float fnull) {
      _fnull = fnull;
    }
    protected float _fnull;
  }

  private Implementation _impl;

  ///////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////////////////////////////////////////////
  private static class WatsonSambridgeImpl extends Implementation {

    public WatsonSambridgeImpl(float[] f, float[] x1, float[] x2, float[] x3) {
      int n = f.length;
      _mesh = new TetMesh();
      for (int i=0; i<n; ++i) {
        TetMesh.Node node = new TetMesh.Node(x1[i],x2[i],x3[i]);
        if (_mesh.addNode(node)) {
          NodeData ndata = new NodeData();
          node.data = ndata;
          ndata.f = f[i];
        }
      }
      _tetList = new TetMesh.TetList();
    }

    public float interpolate(float x1, float x2, float x3) {

      // Find natural neighbor tets of the interpolation point (x1,x2,x3). 
      // If none, skip this point (outside convex hull).
      boolean gotTetList = makeTetList(x1,x2,x3);
      if (!gotTetList)
        return _fnull;
      double xp = x1;
      double yp = x2;
      double zp = x3;

      // Sums for numerator and denominator.
      double vnum = 0.0;
      double vden = 0.0;
      
      // For all tets in the list, ...
      int ntet = _tetList.ntet();
      TetMesh.Tet[] tets = _tetList.tets();
      for (int itet=0; itet<ntet; ++itet) {
        TetMesh.Tet tet = tets[itet];

        // The three nodes of one tet.
        TetMesh.Node na = tet.nodeA();
        TetMesh.Node nb = tet.nodeB();
        TetMesh.Node nc = tet.nodeC();
        TetMesh.Node nd = tet.nodeD();
        double xa = na.xp(), ya = na.yp(), za = na.zp();
        double xb = nb.xp(), yb = nb.yp(), zb = nb.zp();
        double xc = nc.xp(), yc = nc.yp(), zc = nc.zp();
        double xd = nd.xp(), yd = nd.yp(), zd = nd.zp();

        // Four circumcenters.
        Geometry.centerSphere(xp,yp,zp,xb,yb,zb,xc,yc,zc,xd,yd,zd,_ca);
        Geometry.centerSphere(xp,yp,zp,xa,ya,za,xd,yd,zd,xc,yc,zc,_cb);
        Geometry.centerSphere(xp,yp,zp,xa,ya,za,xb,yb,zb,xd,yd,zd,_cc);
        Geometry.centerSphere(xp,yp,zp,xa,ya,za,xc,yc,zc,xb,yb,zb,_cd);
        Geometry.centerSphere(xa,ya,za,xb,yb,zb,xc,yc,zc,xd,yd,zd,_cv);

        // Accumulate signed areas and float values.
        double va = volume(_cv,_cb,_cc,_cd);
        double vb = volume(_cv,_ca,_cd,_cc);
        double vc = volume(_cv,_ca,_cb,_cd);
        double vd = volume(_cv,_ca,_cc,_cb);
        float fa = ((NodeData)na.data).f;
        float fb = ((NodeData)nb.data).f;
        float fc = ((NodeData)nc.data).f;
        float fd = ((NodeData)nd.data).f;
        vnum += va*fa+vb*fb+vc*fc+vd*fd;
        vden += va+vb+vc+vd;
      }

      // The interpolated value.
      return (float)(vnum/vden);
    }

    private static class NodeData {
      float f,gx,gy,gz; // function values and gradient
    }

    private TetMesh _mesh; // Delaunay tet mesh
    private double[] _ca = new double[3]; // circumcenter of fake tet pbcd
    private double[] _cb = new double[3]; // circumcenter of fake tet padc
    private double[] _cc = new double[3]; // circumcenter of fake tet pabd
    private double[] _cd = new double[3]; // circumcenter of fake tet pacb
    private double[] _cv = new double[3]; // circumcenter of tet abcd
    private TetMesh.TetList _tetList; // list of natural neighbor tets

    private boolean makeTetList(float x, float y, float z) {
      _tetList.clear();
      TetMesh.PointLocation pl = _mesh.locatePoint(x,y,z);
      if (pl.isOutside())
        return false;
      _mesh.clearTetMarks();
      makeTetList(x,y,z,pl.tet());
      return true;
    }
    private void makeTetList(
      double xp, double yp, double zp, TetMesh.Tet tet) 
    {
      if (tet==null || _mesh.isMarked(tet))
        return;
      _mesh.mark(tet);
      if (inSphere(xp,yp,zp,tet)) {
        _tetList.add(tet);
        makeTetList(xp,yp,zp,tet.tetA());
        makeTetList(xp,yp,zp,tet.tetB());
        makeTetList(xp,yp,zp,tet.tetC());
        makeTetList(xp,yp,zp,tet.tetD());
      }
    }
    private double volume(double[] ci, double[] cj, double[] ck, double[] cv) {
      double xv = cv[0];
      double yv = cv[1];
      double zv = cv[2];
      double xi = ci[0]-xv;
      double yi = ci[1]-yv;
      double zi = ci[2]-zv;
      double xj = cj[0]-xv;
      double yj = cj[1]-yv;
      double zj = cj[2]-zv;
      double xk = ck[0]-xv;
      double yk = ck[1]-yv;
      double zk = ck[2]-zv;
      return xi*(yj*zk-yk*zj)+yi*(zj*xk-zk*xj)+zi*(xj*yk-xk*yj);
    }
    private boolean inSphere(
      double xp, double yp, double zp, TetMesh.Tet tet) 
    {
      if (tet==null)
        return false;
      TetMesh.Node na = tet.nodeA();
      TetMesh.Node nb = tet.nodeB();
      TetMesh.Node nc = tet.nodeC();
      TetMesh.Node nd = tet.nodeD();
      double xa = na.xp(), ya = na.yp(), za = na.zp();
      double xb = nb.xp(), yb = nb.yp(), zb = nb.zp();
      double xc = nc.xp(), yc = nc.yp(), zc = nc.zp();
      double xd = nd.xp(), yd = nd.yp(), zd = nd.zp();
      return Geometry.inSphereFast(xa,ya,za,
                                   xb,yb,zb,
                                   xc,yc,zc,
                                   xd,yd,zd,
                                   xp,yp,zp)>0;
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////////////////////////////////////////////
  private static class BraunSambridgeImpl extends Implementation {

    public BraunSambridgeImpl(float[] f, float[] x1, float[] x2, float[] x3) {
      int n = f.length;
      _mesh = new TetMesh();
      for (int i=0; i<n; ++i) {
        TetMesh.Node node = new TetMesh.Node(x1[i],x2[i],x3[i]);
        if (_mesh.addNode(node)) {
          NodeData ndata = new NodeData();
          node.data = ndata;
          ndata.f = f[i];
        }
      }
      int nnode = _mesh.countNodes();
      TetMesh.NodeIterator ni = _mesh.getNodes();
      while (ni.hasNext()) {
        TetMesh.Node node = ni.next();
        NodeData ndata = (NodeData)node.data;
        ndata.nabors = _mesh.getNodeNabors(node);
      }
      _nodeList = new TetMesh.NodeList();
    }

    public float interpolate(float x1, float x2, float x3) {
      double x1i = x1;
      double x2i = x2;
      double x3i = x3;

      // Find all natural neighbors of the interpolation point (x1,x2,x3). 
      // If none, skip this point (outside convex hull).
      boolean gotNabors = getNaturalNabors(x1,x2,x3);
      if (!gotNabors)
        return _fnull;

      // Sums for numerator and denominator.
      double vnum = 0.0;
      double vden = 0.0;

      // For all natural neighbors, ...
      int nnabor = _nodeList.nnode();
      TetMesh.Node[] nabors = _nodeList.nodes();
      for (int j=0; j<nnabor; ++j) {
        TetMesh.Node jnode = nabors[j];
        double x1j = jnode.xp();
        double x2j = jnode.yp();
        double x3j = jnode.zp();
        double fj = ((NodeData)jnode.data).f;

        // Clear the polygon for the j'th natural neighbor.
        _lv.clear();

        // Add half-plane of points closer to (x1i,x2i,x3i) than (x1j,x2j,x3j).
        // For this half-plane we set b = 0, which is equivalent to 
        // making the midpoint (x1s,x2s,x3s) the origin for the polygon.
        // A half-space with b = 0 speeds up Lassere's algorithm.
        double x1s = 0.5*(x1j+x1i);
        double x2s = 0.5*(x2j+x2i);
        double x3s = 0.5*(x3j+x3i);
        double x1d = x1j-x1i;
        double x2d = x2j-x2i;
        double x3d = x3j-x3i;
        _lv.addHalfSpace(x1d,x2d,x3d,0.0); // note b = 0 here

        // For all other natural neighbors, ...
        for (int k=0; k<nnabor; ++k) {
          if (j==k) continue;
          TetMesh.Node knode = nabors[k];

          // Skip pair if they are not node neighbors in the mesh.
          if (!nodesAreNabors(jnode,knode))
            continue;

          // Add half-plane of points closer to (x1j,x2j,x3j) than 
          // (x1k,x2k,x3k). Here we must account for the shift 
          // (x1s,x2s,x3s) described above.
          double x1k = knode.xp();
          double x2k = knode.yp();
          double x3k = knode.zp();
          double x1e = x1k-x1j;
          double x2e = x2k-x2j;
          double x3e = x3k-x3j;
          double x1t = 0.5*(x1k+x1j)-x1s;
          double x2t = 0.5*(x2k+x2j)-x2s;
          double x3t = 0.5*(x3k+x3j)-x3s;
          _lv.addHalfSpace(x1e,x2e,x3e,x1e*x1t+x2e*x2t+x3e*x3t);
        }

        // Area of polygon for this natural neighbor.
        double aj = _lv.getVolume();

        // Accumulate numerator and denominator.
        vnum += aj*fj;
        vden += aj;
      }

      // The interpolated value.
      return (float)(vnum/vden);
    }
    private static float f(TetMesh.Node node) {
      return ((NodeData)node.data).f;
    }
    private boolean nodesAreNabors(TetMesh.Node jnode, TetMesh.Node knode) {
      TetMesh.Node[] nabors = ((NodeData)jnode.data).nabors;
      int nnabor = nabors.length;
      for (int jnabor=0; jnabor<nnabor; ++jnabor)
        if (nabors[jnabor]==knode)
          return true;
      return false;
    }

    private static class NodeData {
      float f,gx,gy,gz; // function values and gradient
      TetMesh.Node[] nabors; // cached node nabors
    }

    private float _fnull; // null value when interpolation impossible
    private TetMesh _mesh; // Delaunay tet mesh
    private TetMesh.NodeList _nodeList; // natural neighbor nodes
    private LasserreVolume _lv = new LasserreVolume(3); // for poly volumes
    private double[] _center = new double[3];
    private boolean getNaturalNabors(float x, float y, float z) {
      _nodeList.clear();
      TetMesh.PointLocation pl = _mesh.locatePoint(x,y,z);
      if (pl.isOutside())
        return false;
      _mesh.clearNodeMarks();
      _mesh.clearTetMarks();
      getNaturalNeighbors(x,y,z,pl.tet());
      return true;
    }
    private void getNaturalNeighbors(
      double xp, double yp, double zp, TetMesh.Tet tet) 
    {
      if (tet!=null && !_mesh.isMarked(tet)) {
        _mesh.mark(tet);
        double rs = tet.centerSphere(_center);
        double xc = _center[0];
        double yc = _center[1];
        double zc = _center[2];
        double dx = xp-xc;
        double dy = yp-yc;
        double dz = zp-zc;
        if (dx*dx+dy*dy+dz*dz<rs) {
          TetMesh.Node na = tet.nodeA();
          TetMesh.Node nb = tet.nodeB();
          TetMesh.Node nc = tet.nodeC();
          TetMesh.Node nd = tet.nodeD();
          TetMesh.Tet ta = tet.tetA();
          TetMesh.Tet tb = tet.tetB();
          TetMesh.Tet tc = tet.tetC();
          TetMesh.Tet td = tet.tetD();
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
          if (!_mesh.isMarked(nd)) {
            _mesh.mark(nd); 
            _nodeList.add(nd);
          }
          getNaturalNeighbors(xp,yp,zp,ta);
          getNaturalNeighbors(xp,yp,zp,tb);
          getNaturalNeighbors(xp,yp,zp,tc);
          getNaturalNeighbors(xp,yp,zp,td);
        }
      }
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////////////////////////////////////////////
  // An implementation developed by Dave Hale and Luming Liang at Colorado
  // School of Mines. The first step is to find and process natural-neighbor 
  // tets while accumulating polyhedron volumes for natural-neighbor nodes.
  // During this first step, faces bounding the region of natural-neighbor
  // tets are stored in a list. In the second step, that list of faces is 
  // processed to complete the computation of volumes and a volume-weighted 
  // average of natural-neighbor node sample values. 
  // All geometric computations are performed in a shifted coordinate
  // system in which the interpolation point (x1,x2,x3) is the origin.
  private static class HaleLiangImpl extends Implementation {

    public HaleLiangImpl(float[] f, float[] x1, float[] x2, float[] x3) {
      int n = f.length;
      _mesh = new TetMesh();
      for (int i=0; i<n; ++i) {
        TetMesh.Node node = new TetMesh.Node(x1[i],x2[i],x3[i]);
        if (_mesh.addNode(node)) {
          NodeData ndata = new NodeData();
          node.data = ndata;
          ndata.f = f[i];
        }
      }
      _nodeList = new TetMesh.NodeList();
      _faceList = new FaceList();
    }

    public float interpolate(float x1, float x2, float x3) {
      boolean gotFaces = processTets(x1,x2,x3);
      return (gotFaces)?processFaces(x1,x2,x3):_fnull;
    }

    // A face has three nodes and both fake and real circumcenters.
    // After all faces have been added to the face list, each face
    // will also have exactly three neighbor faces.
    private static class Face {
      TetMesh.Node na,nb,nc; // three nodes of this face
      double xf,yf,zf; // circumcenter of fake tet pabc
      double xr,yr,zr; // circumcenter of real tet dabc
      Face fa,fb,fc; // three neighbors of this face
    }

    // Computes fake and real circumcenters and adds face to the face list.
    private void addFace(
      double xp, double yp, double zp,
      TetMesh.Node na, TetMesh.Node nb, TetMesh.Node nc, TetMesh.Tet tet)
    {
      double xa = na.xp(), ya = na.yp(), za = na.zp();
      double xb = nb.xp(), yb = nb.yp(), zb = nb.zp();
      double xc = nc.xp(), yc = nc.yp(), zc = nc.zp();
      Geometry.centerSphere(xp,yp,zp,xa,ya,za,xb,yb,zb,xc,yc,zc,_xyz);
      double xf = _xyz[0]-xp, yf = _xyz[1]-yp, zf = _xyz[2]-zp;
      tet.centerSphere(_xyz);
      double xr = _xyz[0]-xp, yr = _xyz[1]-yp, zr = _xyz[2]-zp;
      _faceList.add(na,nb,nc,xf,yf,zf,xr,yr,zr);
    }

    // A list of faces that represent the boundary of the Voronoi
    // polyhedron of the point (x,y,z) at which to interpolate.
    // As faces are added to this list, they are hooked up to any
    // face neighbors that are already in the list. Therefore, after 
    // all faces have been added to the list, each face should have 
    // exactly three face neighbors.
    private static class FaceList {
      private int _nface;
      private ArrayList<Face> _faces = new ArrayList<Face>();
      int nface() {
        return _nface;
      }
      ArrayList<Face> faces() {
        return _faces;
      }
      void clear() {
        _nface = 0;
      }
      void add(
        TetMesh.Node na, TetMesh.Node nb, TetMesh.Node nc,
        double xf, double yf, double zf,
        double xr, double yr, double zr)
      {
        if (_nface==_faces.size()) // rarely must we construct a
          _faces.add(new Face()); // new face like this, because we
        Face face = _faces.get(_nface); // can reuse an existing face
        int nfound = 0;
        Face fa = null, fb = null, fc = null;
        for (int iface=0; iface<_nface && nfound<3; ++iface) {
          Face fi = _faces.get(iface);
          TetMesh.Node nai = fi.na, nbi = fi.nb, nci = fi.nc;
          if (fa==null) {
            if (nb==nci && nc==nbi) {
              fa = fi; fi.fa = face; ++nfound;
            } else if (nb==nbi && nc==nai) {
              fa = fi; fi.fc = face; ++nfound;
            } else if (nb==nai && nc==nci) {
              fa = fi; fi.fb = face; ++nfound;
            }
          }
          if (fb==null) {
            if (nc==nai && na==nci) {
              fb = fi; fi.fb = face; ++nfound;
            } else if (nc==nci && na==nbi) {
              fb = fi; fi.fa = face; ++nfound;
            } else if (nc==nbi && na==nai) {
              fb = fi; fi.fc = face; ++nfound;
            }
          }
          if (fc==null) {
            if (na==nbi && nb==nai) {
              fc = fi; fi.fc = face; ++nfound;
            } else if (na==nai && nb==nci) {
              fc = fi; fi.fb = face; ++nfound;
            } else if (na==nci && nb==nbi) {
              fc = fi; fi.fa = face; ++nfound;
            }
          }
        }
        face.na = na; face.nb = nb; face.nc = nc;
        face.xf = xf; face.yf = yf; face.zf = zf;
        face.xr = xr; face.yr = yr; face.zr = zr;
        face.fa = fa; face.fb = fb; face.fc = fc;
        ++_nface;
      }
    }

    // Accumulates volume for a specified node. If the node is not yet
    // marked, then we first mark the node, add it to the node list, 
    // and initialize its volume to zero.
    private void accumulateVolume(TetMesh.Node node, double volume) {
      NodeData ndata = (NodeData)node.data;
      if (!_mesh.isMarked(node)) {
        _mesh.mark(node);
        _nodeList.add(node);
        ndata.volume = 0.0;
      }
      ndata.volume += volume;
    }

    // Processes natural-neighbor tets and returns true if any exist.
    private boolean processTets(float x, float y, float z) {
      _nodeList.clear();
      _faceList.clear();
      TetMesh.PointLocation pl = _mesh.locatePoint(x,y,z);
      if (pl.isOutside())
        return false;
      _mesh.clearNodeMarks();
      _mesh.clearTetMarks();
      processTet(x,y,z,pl.tet());
      return true;
    }

   // Processes one tet and (recursively) its tet neighbors.
    private void processTet(double xp, double yp, double zp, TetMesh.Tet tet) {
      if (tet==null || _mesh.isMarked(tet))
        return;
      _mesh.mark(tet);
      TetMesh.Node na = tet.nodeA();
      TetMesh.Node nb = tet.nodeB();
      TetMesh.Node nc = tet.nodeC();
      TetMesh.Node nd = tet.nodeD();
      TetMesh.Tet ta = tet.tetA();
      TetMesh.Tet tb = tet.tetB();
      TetMesh.Tet tc = tet.tetC();
      TetMesh.Tet td = tet.tetD();

      // Circumsphere of tet is assumed to contain the point (x,y,z).
      tet.centerSphere(_xyz);
      double xt = _xyz[0]-xp, yt = _xyz[1]-yp, zt = _xyz[2]-zp;

      // For each tet tabor, this flag is true when the point (x,y,z) is not 
      // inside the circumsphere of that tet, so that the corresponding face 
      // must be added to the face list for processing later.
      boolean saveFace;

      // Process for tet neighbor opposite node A.
      saveFace = true;
      if (ta!=null) {
        double rs = ta.centerSphere(_xyz);
        double xa = _xyz[0]-xp, ya = _xyz[1]-yp, za = _xyz[2]-zp;
        if (xa*xa+ya*ya+za*za<rs) {
          double xb = nb.xp()-xp, yb = nb.yp()-yp, zb = nb.zp()-zp;
          double xd = nd.xp()-xp, yd = nd.yp()-yp, zd = nd.zp()-zp;
          double xc = nc.xp()-xp, yc = nc.yp()-yp, zc = nc.zp()-zp;
          double xbd = xb+xd, ybd = yb+yd, zbd = zb+zd;
          double xdc = xd+xc, ydc = yd+yc, zdc = zd+zc;
          double xcb = xc+xb, ycb = yc+yb, zcb = zc+zb;
          double xyz = yt*za-ya*zt, yzx = zt*xa-za*xt, zxy = xt*ya-xa*yt;
          accumulateVolume(nb,xbd*xyz+ybd*yzx+zbd*zxy);
          accumulateVolume(nd,xdc*xyz+ydc*yzx+zdc*zxy);
          accumulateVolume(nc,xcb*xyz+ycb*yzx+zcb*zxy);
          processTet(xp,yp,zp,ta);
          saveFace = false;
        }
      }
      if (saveFace)
        addFace(xp,yp,zp,nb,nc,nd,tet);

      // Process for tet neighbor opposite node B.
      saveFace = true;
      if (tb!=null) {
        double rs = tb.centerSphere(_xyz);
        double xb = _xyz[0]-xp, yb = _xyz[1]-yp, zb = _xyz[2]-zp;
        if (xb*xb+yb*yb+zb*zb<rs) {
          double xc = nc.xp()-xp, yc = nc.yp()-yp, zc = nc.zp()-zp;
          double xd = nd.xp()-xp, yd = nd.yp()-yp, zd = nd.zp()-zp;
          double xa = na.xp()-xp, ya = na.yp()-yp, za = na.zp()-zp;
          double xcd = xc+xd, ycd = yc+yd, zcd = zc+zd;
          double xda = xd+xa, yda = yd+ya, zda = zd+za;
          double xac = xa+xc, yac = ya+yc, zac = za+zc;
          double xyz = yt*zb-yb*zt, yzx = zt*xb-zb*xt, zxy = xt*yb-xb*yt;
          accumulateVolume(nc,xcd*xyz+ycd*yzx+zcd*zxy);
          accumulateVolume(nd,xda*xyz+yda*yzx+zda*zxy);
          accumulateVolume(na,xac*xyz+yac*yzx+zac*zxy);
          processTet(xp,yp,zp,tb);
          saveFace = false;
        }
      }
      if (saveFace)
        addFace(xp,yp,zp,nc,na,nd,tet);

      // Process for tet neighbor opposite node C.
      saveFace = true;
      if (tc!=null) {
        double rs = tc.centerSphere(_xyz);
        double xc = _xyz[0]-xp, yc = _xyz[1]-yp, zc = _xyz[2]-zp;
        if (xc*xc+yc*yc+zc*zc<rs) {
          double xd = nd.xp()-xp, yd = nd.yp()-yp, zd = nd.zp()-zp;
          double xb = nb.xp()-xp, yb = nb.yp()-yp, zb = nb.zp()-zp;
          double xa = na.xp()-xp, ya = na.yp()-yp, za = na.zp()-zp;
          double xdb = xd+xb, ydb = yd+yb, zdb = zd+zb;
          double xba = xb+xa, yba = yb+ya, zba = zb+za;
          double xad = xa+xd, yad = ya+yd, zad = za+zd;
          double xyz = yt*zc-yc*zt, yzx = zt*xc-zc*xt, zxy = xt*yc-xc*yt;
          accumulateVolume(nd,xdb*xyz+ydb*yzx+zdb*zxy);
          accumulateVolume(nb,xba*xyz+yba*yzx+zba*zxy);
          accumulateVolume(na,xad*xyz+yad*yzx+zad*zxy);
          processTet(xp,yp,zp,tc);
          saveFace = false;
        }
      }
      if (saveFace)
        addFace(xp,yp,zp,nd,na,nb,tet);

      // Process for tet neighbor opposite node D.
      saveFace = true;
      if (td!=null) {
        double rs = td.centerSphere(_xyz);
        double xd = _xyz[0]-xp, yd = _xyz[1]-yp, zd = _xyz[2]-zp;
        if (xd*xd+yd*yd+zd*zd<rs) {
          double xa = na.xp()-xp, ya = na.yp()-yp, za = na.zp()-zp;
          double xb = nb.xp()-xp, yb = nb.yp()-yp, zb = nb.zp()-zp;
          double xc = nc.xp()-xp, yc = nc.yp()-yp, zc = nc.zp()-zp;
          double xab = xa+xb, yab = ya+yb, zab = za+zb;
          double xbc = xb+xc, ybc = yb+yc, zbc = zb+zc;
          double xca = xc+xa, yca = yc+ya, zca = zc+za;
          double xyz = yt*zd-yd*zt, yzx = zt*xd-zd*xt, zxy = xt*yd-xd*yt;
          accumulateVolume(na,xab*xyz+yab*yzx+zab*zxy);
          accumulateVolume(nb,xbc*xyz+ybc*yzx+zbc*zxy);
          accumulateVolume(nc,xca*xyz+yca*yzx+zca*zxy);
          processTet(xp,yp,zp,td);
          saveFace = false;
        }
      }
      if (saveFace)
        addFace(xp,yp,zp,na,nc,nb,tet);
    }

    // Determines whether all faces in the face set have exactly three
    // neighbor faces. Should always be true, except for points (x,y,z)
    // that lie on (over very near?) the convex hull of the mesh.
    private boolean faceNaborsOk(float x, float y, float z) {
      int nface = _faceList.nface();
      ArrayList<Face> faces = _faceList.faces();
      for (int iface=0; iface<nface; ++iface) {
        Face face = faces.get(iface);
        if (face.fa==null || face.fb==null || face.fc==null) {
          //System.out.println("null face nabor: x="+x+" y="+y+" z="+z);
          return false;
        }
      }
      return true;
    }

    // Processes faces in the face list and returns the interpolated value.
    private float processFaces(float x, float y, float z) {
      if (!faceNaborsOk(x,y,z))
        return _fnull;
      int nface = _faceList.nface();
      ArrayList<Face> faces = _faceList.faces();
      for (int iface=0; iface<nface; ++iface)
        processFace(x,y,z,faces.get(iface));
      double vnum = 0.0;
      double vden = 0.0;
      int nnode = _nodeList.nnode();
      TetMesh.Node[] nodes = _nodeList.nodes();
      for (int inode=0; inode<nnode; ++inode) {
        TetMesh.Node node = nodes[inode];
        NodeData ndata = (NodeData)node.data;
        float f = ndata.f;
        double volume = ndata.volume;
        vnum += volume*f;
        vden += volume;
      }
      return (float)(vnum/vden);
    }

    private void processFace(double xp, double yp, double zp, Face face) {

      // Coordinates of three face nodes, shifted for local origin.
      TetMesh.Node na = face.na, nb = face.nb, nc = face.nc;
      double xa = na.xp()-xp, ya = na.yp()-yp, za = na.zp()-zp;
      double xb = nb.xp()-xp, yb = nb.yp()-yp, zb = nb.zp()-zp;
      double xc = nc.xp()-xp, yc = nc.yp()-yp, zc = nc.zp()-zp;

      // Midpoints of edges ab, bc, and ca of face. (Omit scaling by 0.5.)
      double xab = xa+xb, yab = ya+yb, zab = za+zb;
      double xbc = xb+xc, ybc = yb+yc, zbc = zb+zc;
      double xca = xc+xa, yca = yc+ya, zca = zc+za;

      // Accumulate volumes for the Voronoi edge through the face.
      double x1 = face.xf, y1 = face.yf, z1 = face.zf;
      double x2 = face.xr, y2 = face.yr, z2 = face.zr;
      double xyz = y1*z2-y2*z1, yzx = z1*x2-z2*x1, zxy = x1*y2-x2*y1;
      double vab = xab*xyz+yab*yzx+zab*zxy;
      double vbc = xbc*xyz+ybc*yzx+zbc*zxy;
      double vca = xca*xyz+yca*yzx+zca*zxy;
      accumulateVolume(na,vab); accumulateVolume(nb,-vab); // here we
      accumulateVolume(nb,vbc); accumulateVolume(nc,-vbc); // must go
      accumulateVolume(nc,vca); accumulateVolume(na,-vca); // both ways!

     // Accumulate volumes for Voronoi edges between face neighbors.
      Face fa = face.fa; x2 = fa.xf; y2 = fa.yf; z2 = fa.zf;
      xyz = y1*z2-y2*z1; yzx = z1*x2-z2*x1; zxy = x1*y2-x2*y1;
      accumulateVolume(nb,xb *xyz+yb *yzx+zb *zxy);
      accumulateVolume(nc,xbc*xyz+ybc*yzx+zbc*zxy);
      Face fb = face.fb; x2 = fb.xf; y2 = fb.yf; z2 = fb.zf;
      xyz = y1*z2-y2*z1; yzx = z1*x2-z2*x1; zxy = x1*y2-x2*y1;
      accumulateVolume(nc,xc *xyz+yc *yzx+zc *zxy);
      accumulateVolume(na,xca*xyz+yca*yzx+zca*zxy);
      Face fc = face.fc; x2 = fc.xf; y2 = fc.yf; z2 = fc.zf;
      xyz = y1*z2-y2*z1; yzx = z1*x2-z2*x1; zxy = x1*y2-x2*y1;
      accumulateVolume(na,xa *xyz+ya *yzx+za *zxy);
      accumulateVolume(nb,xab*xyz+yab*yzx+zab*zxy);
    }

    private static class NodeData {
      float f,gx,gy,gz; // function values and gradient
      double volume; // volume for Sibson weight
    }

    private float _fnull; // null value
    private TetMesh _mesh; // the Delaunay tet mesh
    private FaceList _faceList; // list of tet faces
    private TetMesh.NodeList _nodeList; // list of natural neighbors
    private double[] _xyz = new double[3]; // a circumsphere center
  }
}
