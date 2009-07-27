/****************************************************************************
Copyright (c) 2009, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.interp;

import java.util.ArrayList;
import java.util.logging.Logger;

import edu.mines.jtk.dsp.Sampling;
import edu.mines.jtk.la.DMatrix;
import edu.mines.jtk.la.DMatrixQrd;
import edu.mines.jtk.mesh.Geometry;
import edu.mines.jtk.mesh.TetMesh;
import edu.mines.jtk.util.Check;

/**
 * Sibson interpolation of scattered samples of 3D functions f(x1,x2,x3).
 * Sibson's (1981) interpolant at any point (x1,x2,x3) is a weighted sum of 
 * values for a nearby subset of samples, the so-called natural neighbors
 * of that point. Sibson interpolation is often called "natural neighbor
 * interpolation."
 * <p>
 * The interpolation weights, also called "Sibson coordinates", are volumes 
 * of overlapping Voronoi polyhedra, normalized so that they sum to one for 
 * any interpolation point (x1,x2,x3). Various implementations of Sibson 
 * interpolation differ primarily in how those volumes are computed.
 * <p>
 * The basic Sibson interpolant is C1 (that is, it's gradient is continuous) 
 * at all points (x1,x2,x3) except at the sample points, where it is C0. 
 * Sibson (1981) also described an extension of his interpolant that is 
 * everywhere C1 and therefore smoother. This smoother interpolant requires 
 * gradients at the sample points, and those gradients can be estimated or
 * specified explicitly.
 * <p>
 * The use of gradients is controlled by a gradient power. If this power 
 * is zero (the default), then gradients are not used. Sibson's (1981) 
 * smoother C1 interpolant corresponds to a power of 1.0. Larger powers 
 * cause the interpolant to more rapidly approach the linear functions 
 * defined by the values and gradients at the sample points.
 * <p>
 * Sibson's interpolant is undefined at points on or outside the convex 
 * hull of sample points. In this sense, Sibson interpolation does not 
 * extrapolate; the interpolant is implicitly bounded by the convex hull,
 * and null values are returned when attempting to interpolate outside
 * those bounds.
 * <p>
 * To extend the interpolant outside the convex hull, this class enables
 * bounds to be set explicitly. When bounds are set, extra ghost samples 
 * are added outside the convex hull. These ghost samples have no values,
 * but they create a larger convex hull so that Sibson coordinates can be
 * computed anywhere within the specified bounds. While often useful, this 
 * extrapolation feature should be used with care, because the added ghost 
 * samples may alter the Sibson interpolant at points inside but near the 
 * original convex hull.
 * <p>
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
   * Sibson coordinates (polyhedron volumes) are computed for natural 
   * neighbors.
   */
  public enum Method {
    /**
     * The Hale-Liang algorithm.
     * Developed by Dave Hale and Luming Liang at the Colorado School of
     * Mines. Accurate, robust and fast, this method is the default.
     */
    HALE_LIANG,
    /**
     * The Braun-Sambridge algorithm. Developed by Braun and Sambridge (1995),
     * this method uses Lasserre's (1983) algorithm for computing the volumes 
     * of polyhedra without computing their vertices. This method is much
     * slower than the other methods provided here. It may also suffer from 
     * numerical instability due to divisions in Lasserre's algorithm.
     */
    BRAUN_SAMBRIDGE,
    /**
     * The Watson-Sambridge algorithm. Developed by Watson (1992) and
     * described further by Sambridge et al. (1995). Though simplest to
     * implement and fast, this method is inaccurate (sometimes wildly so) 
     * at interpolation points (x1,x2,x3) that lie on or near edges of a 
     * Delaunay triangulation of the scattered sample points. 
     */
    WATSON_SAMBRIDGE,
  }

  /**
   * Sample index and corresponding interpolation weight (Sibson coordinate).
   */
  public static class IndexWeight {

    /** Index for one sample. */
    public int index;

    /** Interpolation weight for one sample; in the range [0,1].*/
    public float weight;

    IndexWeight(int index, float weight) {
      this.index = index;
      this.weight = weight;
    }
  }

  /**
   * Constructs an interpolator with specified sample coordinates.
   * Function values f(x1,x2,x3) are not set and are assumed to be zero.
   * Uses the most accurate and efficient implementation.
   * @param x1 array of sample x1 coordinates.
   * @param x2 array of sample x2 coordinates.
   * @param x3 array of sample x3 coordinates.
   */
  public SibsonInterpolator3(float[] x1, float[] x2, float[] x3) {
    this(Method.HALE_LIANG,null,x1,x2,x3);
  }

  /**
   * Constructs an interpolator with specified samples.
   * Uses the most accurate and efficient implementation.
   * @param f array of sample values f(x1,x2,x3).
   * @param x1 array of sample x1 coordinates.
   * @param x2 array of sample x2 coordinates.
   * @param x3 array of sample x3 coordinates.
   */
  public SibsonInterpolator3(float[] f, float[] x1, float[] x2, float[] x3) {
    this(Method.HALE_LIANG,f,x1,x2,x3);
  }

  /**
   * Constructs an interpolator with specified method and sample coordinates.
   * Function values f(x1,x2,x3) are not set and are assumed to be zero.
   * This constructor is provided primarily for testing.
   * The default Hale-Liang method is accurate and fast.
   * @param method the implementation method.
   * @param x1 array of sample x1 coordinates.
   * @param x2 array of sample x2 coordinates.
   * @param x3 array of sample x3 coordinates.
   */
  public SibsonInterpolator3(
    Method method, float[] x1, float[] x2, float[] x3) 
  {
    this(method,null,x1,x2,x3);
  }

  /**
   * Constructs an interpolator with specified method and samples.
   * This constructor is provided primarily for testing.
   * The default Hale-Liang method is accurate and fast.
   * @param method the implementation method.
   * @param f array of sample values f(x1,x2,x3).
   * @param x1 array of sample x1 coordinates.
   * @param x2 array of sample x2 coordinates.
   * @param x3 array of sample x3 coordinates.
   */
  public SibsonInterpolator3(
    Method method, float[] f, float[] x1, float[] x2, float[] x3) 
  {
    makeMesh(f,x1,x2,x3);
    _nodeList = new TetMesh.NodeList();
    _tetList = new TetMesh.TetList();
    if (method==Method.WATSON_SAMBRIDGE) {
      _va = new WatsonSambridge();
    } else if (method==Method.BRAUN_SAMBRIDGE) {
      _va = new BraunSambridge();
    } else if (method==Method.HALE_LIANG) {
      _va = new HaleLiang();
    }
  }

  /**
   * Sets the samples to be interpolated.
   * Any sample coordinates, values or gradients set previously are forgotten.
   * @param f array of sample values f(x1,x2,x3).
   * @param x1 array of sample x1 coordinates.
   * @param x2 array of sample x2 coordinates.
   * @param x3 array of sample x3 coordinates.
   */
  public void setSamples(float[] f, float[] x1, float[] x2, float[] x3) {
    makeMesh(f,x1,x2,x3);
    _haveGradients = false;
    if (_gradientPower>0.0)
      estimateGradients();
  }

  /**
   * Sets gradients for all samples. If the gradient power is currently 
   * zero, then this method also sets the gradient power to one. To later
   * ignore gradients that have been set, the gradient power can be reset
   * to zero.
   * @param g1 array of 1st components of gradients.
   * @param g2 array of 2nd components of gradients.
   * @param g3 array of 3rd components of gradients.
   */
  public void setGradients(float[] g1, float[] g2, float[] g3) {
    int n = g1.length;
    for (int i=0; i<n; ++i) {
      TetMesh.Node node = _nodes[i];
      NodeData data = data(node);
      data.gx = g1[i];
      data.gy = g2[i];
      data.gz = g3[i];
    }
    _haveGradients = true;
    if (_gradientPower==0.0)
      _gradientPower = 1.0;
  }

  /**
   * Sets the power of gradients for this interpolator. The default 
   * gradient power is zero, which implies no use of gradients and
   * a basic Sibson interpolant that is smooth (C1) everywhere except 
   * at the specified sample points (where it is C0).
   * <p>
   * If the gradient power is set to a non-zero value, and if gradients 
   * have not been set explicitly, then this method will also estimate 
   * gradients for all samples, as described by Sibson (1981).
   * <p>
   * If bounds are set explicitly, gradient estimates can be improved 
   * by setting the bounds before calling this method.
   * @param gradientPower the gradient power.
   */
  public void setGradientPower(double gradientPower) {
    if (!_haveGradients && gradientPower>0.0)
      estimateGradients();
    _gradientPower = gradientPower;
  }

  /**
   * Sets the null value for this interpolator.
   * This null value is returned when interpolation is attempted at a
   * point that lies outside the bounds of this interpolator. Those 
   * bounds are by default the convex hull of the sample points, but 
   * may also be set explicitly. The default null value is zero.
   * @param fnull the null value.
   */
  public void setNullValue(float fnull) {
    _fnull = fnull;
  }

  /**
   * Sets a bounding box for this interpolator.
   * Sibson interpolation is undefined for points (x1,x2,x3) outside the 
   * convex hull of sample points, so the default bounds are that convex 
   * hull. This method enables extrapolation for points outside the convex 
   * hull, while restricting interpolation to points inside the box.
   * <p>
   * If gradients are to be computed (not specified explicitly), it is best 
   * to set bounds by calling this method before computing gradients.
   * @param x1min lower bound on x1.
   * @param x1max upper bound on x1.
   * @param x2min lower bound on x2.
   * @param x2max upper bound on x2.
   * @param x3min lower bound on x3.
   * @param x3max upper bound on x3.
   */
  public void setBounds(
    float x1min, float x1max,
    float x2min, float x2max,
    float x3min, float x3max) 
  {
    Check.argument(x1min<x1max,"x1min<x1max");
    Check.argument(x2min<x2max,"x2min<x2max");
    Check.argument(x3min<x3max,"x3min<x3max");

    // Remember the specified bounding box.
    _x1bmn = x1min; _x1bmx = x1max;
    _x2bmn = x2min; _x2bmx = x2max;
    _x3bmn = x3min; _x3bmx = x3max;
    _useBoundingBox = true;

    // Compute coordinates for ghost nodes, and add them to the mesh.
    float scale = 1.0f;
    float x1avg = 0.5f*(x1min+x1max);
    float x2avg = 0.5f*(x2min+x2max);
    float x3avg = 0.5f*(x3min+x3max);
    float x1dif = Math.max(x1max-_x1min,_x1max-x1min);
    float x2dif = Math.max(x2max-_x2min,_x2max-x2min);
    float x3dif = Math.max(x3max-_x3min,_x3max-x3min);
    float x1pad = scale*x1dif;
    float x2pad = scale*x2dif;
    float x3pad = scale*x3dif;
    x1min -= x1pad; x1max += x1pad;
    x2min -= x2pad; x2max += x2pad;
    x3min -= x3pad; x3max += x2pad;
    float[] x1g = {x1min,x1max,x1avg,x1avg,x1avg,x1avg};
    float[] x2g = {x2avg,x2avg,x2min,x2max,x2avg,x2avg};
    float[] x3g = {x3avg,x3avg,x3avg,x3avg,x3min,x3max};
    addGhostNodes(x1g,x2g,x3g);
  }

  /**
   * Sets bounds for this interpolator using specified samplings.
   * Values interpolated within the bounding box of these samplings
   * are never null, even when the interpolation point (x1,x2,x3) 
   * lies outside that box.
   * <p>
   * If gradients are to be computed (not specified explicitly), it is best 
   * to set bounds by calling this method before computing gradients.
   * @param s1 sampling of x1.
   * @param s2 sampling of x2.
   * @param s3 sampling of x3.
   */
  public void setBounds(Sampling s1, Sampling s2, Sampling s3)
  {
    setBounds((float)s1.getFirst(),(float)s1.getLast(),
              (float)s2.getFirst(),(float)s2.getLast(),
              (float)s3.getFirst(),(float)s3.getLast());
  }

  /**
   * If bounds have been set explicitly, this method unsets them,
   * so that the convex hull of sample points will be used instead.
   */
  public void useConvexHullBounds() {
    _useBoundingBox = false;
    removeGhostNodes();
  }

  /**
   * Returns a value interpolated at the specified point.
   * @param x1 the x1 coordinate of the point.
   * @param x2 the x2 coordinate of the point.
   * @param x3 the x3 coordinate of the point.
   * @return the interpolated value.
   */
  public float interpolate(float x1, float x2, float x3) {
    if (!inBounds(x1,x2,x3))
      return _fnull;
    double vsum = computeVolumes(x1,x2,x3);
    if (vsum<=0.0)
      return _fnull;
    if (usingGradients()) {
      return interpolate1(vsum,x1,x2,x3);
    } else {
      return interpolate0(vsum);
    }
  }

  /**
   * Returns an array of interpolated values sampled on a grid.
   * @param s1 the sampling of n1 x1 coordinates.
   * @param s2 the sampling of n2 x2 coordinates.
   * @param s3 the sampling of n3 x3 coordinates.
   * @return array[n3][n2][n1] of interpolated values.
   */
  public float[][][] interpolate(Sampling s1, Sampling s2, Sampling s3) {
    log.fine("interpolate: begin");
    int n1 = s1.getCount();
    int n2 = s2.getCount();
    int n3 = s2.getCount();
    float[][][] f = new float[n3][n2][n1];
    for (int i3=0; i3<n3; ++i3) {
      log.fine("interpolate: i3="+i3);
      float x3 = (float)s3.getValue(i3);
      for (int i2=0; i2<n2; ++i2) {
        log.finer("interpolate: i2="+i2);
        float x2 = (float)s2.getValue(i2);
        for (int i1=0; i1<n1; ++i1) {
          float x1 = (float)s1.getValue(i1);
          f[i3][i2][i1] = interpolate(x1,x2,x3);
        }
      }
    }
    log.fine("interpolate: end");
    return f;
  }

  /**
   * Gets sample indices and interpolation weights for the specified point.
   * Given a point (x1,x2,x3), the sample indices represent the natural
   * neighbors of that point, and the interpolation weights are its Sibson
   * coordinates. Indices correspond to the arrays that are specified when
   * constructing this interpolator.
   * <p>
   * Indices and weights are especially useful in applications where they 
   * can be reused, say, to interpolate multiple function values at a 
   * single point.
   * @param x1 the x1 coordinate of the point.
   * @param x2 the x2 coordinate of the point.
   * @param x3 the x3 coordinate of the point.
   * @return array of sample indices and weights; null if none.
   */
  public IndexWeight[] getIndexWeights(float x1, float x2, float x3) {
    if (!inBounds(x1,x2,x3))
      return null;
    float wsum = (float)computeVolumes(x1,x2,x3);
    if (wsum==0.0f)
      return null;
    float wscl = 1.0f/wsum;
    int nnode = _nodeList.nnode();
    TetMesh.Node[] nodes = _nodeList.nodes();
    IndexWeight[] iw = new IndexWeight[nnode];
    for (int inode=0; inode<nnode; ++inode) {
      TetMesh.Node node = nodes[inode];
      int i = node.index;
      float w = (float)volume(node)*wscl;
      iw[inode] = new IndexWeight(i,w);
    }
    return iw;
  }

  /**
   * Interpolates at the i'th sample point without using the i'th sample.
   * This method implements leave-one-out cross-validation. The difference
   * between the i'th sample value and the returned interpolated value is
   * a measure of error at the i'th sample.
   * <p>
   * If bounds have not been set explicitly, then this method will return 
   * a null value if the validated sample is on the convex hull of samples.
   * <p>
   * This method does not recompute gradients that may have been estimated 
   * using the sample to be validated. Therefore, validation should be 
   * performed without using gradients. 
   * @param i the index of the sample to validate.
   * @return the value interpolated at the validated sample point.
   */
  public float validate(int i) {
    return validate(new int[]{i})[0];
  }

  /**
   * Interpolates at specified sample points without using those samples.
   * This method implements a form of cross-validation. Differences
   * between the values of the specified samples and the returned 
   * interpolated values are measures of errors for those samples.
   * <p>
   * If bounds have not been set explicitly, then this method will return 
   * null values if the validated sample is on the convex hull of samples.
   * <p>
   * This method does not recompute gradients that may have been estimated 
   * using the samples to be validated. Therefore, validation should be 
   * performed without using gradients. 
   * @param i array of indices of samples to validate.
   * @return array of values interpolated at validated sample points.
   */
  public float[] validate(int[] i) {
    int nv = i.length;
    for (int iv=0; iv<nv; ++iv)
      _mesh.removeNode(_nodes[i[iv]]);
    float[] fv = new float[nv];
    for (int iv=0; iv<nv; ++iv) {
      TetMesh.Node node = _nodes[i[iv]];
      float xn = node.x();
      float yn = node.y();
      float zn = node.z();
      fv[iv] = interpolate(xn,yn,zn);
    }
    for (int iv=0; iv<nv; ++iv)
      _mesh.addNode(_nodes[i[iv]]);
    return fv;
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private static Logger log = 
    Logger.getLogger(SibsonInterpolator3.class.getName());

  // Data associated with all nodes in the tet mesh.
  private static class NodeData {
    float f,gx,gy,gz; // function values and gradient
    double volume; // volume for Sibson weight
  }
  private static NodeData data(TetMesh.Node node) {
    return (NodeData)node.data;
  }
  private static float f(TetMesh.Node node) {
    return data(node).f;
  }
  private static float gx(TetMesh.Node node) {
    return data(node).gx;
  }
  private static float gy(TetMesh.Node node) {
    return data(node).gy;
  }
  private static float gz(TetMesh.Node node) {
    return data(node).gz;
  }
  private static double volume(TetMesh.Node node) {
    return data(node).volume;
  }
  private static boolean ghost(TetMesh.Node node) {
    return node.index<0;
  }

  private TetMesh _mesh; // the mesh
  private TetMesh.Node[] _nodes; // array of real (not ghost) nodes
  private TetMesh.NodeList _nodeList; // list of natural neighbor nodes
  private TetMesh.TetList _tetList; // list of natural neighbor tets
  private VolumeAccumulator _va; // accumulates Sibson's volumes
  private boolean _haveGradients; // true if mesh nodes have gradients
  private double _gradientPower; // power of gradients
  private float _fnull; // returned when interpolation point out of bounds
  private float _x1min,_x1max,_x2min,_x2max,_x3min,_x3max; // for (x1,x2,x3)
  private float _x1bmn,_x1bmx,_x2bmn,_x2bmx,_x3bmn,_x3bmx; // if bounds set
  private boolean _useBoundingBox; // true if using bounding box

  // Returns a tet mesh built from specified scattered samples.
  private void makeMesh(float[] f, float[] x1, float[] x2, float[] x3) {

    // Find bounding box for sample points.
    int n = x1.length;
    _x1min = x1[0]; _x1max = x1[0];
    _x2min = x2[0]; _x2max = x2[0];
    _x3min = x3[0]; _x3max = x3[0];
    for (int i=1; i<n; ++i) {
      if (x1[i]<_x1min) _x1min = x1[i]; 
      if (x1[i]>_x1max) _x1max = x1[i];
      if (x2[i]<_x2min) _x2min = x2[i]; 
      if (x2[i]>_x2max) _x2max = x2[i];
      if (x3[i]<_x3min) _x3min = x3[i]; 
      if (x3[i]>_x3max) _x3max = x3[i];
    }

    // ArrayMath of nodes, one for each specified sample.
    _nodes = new TetMesh.Node[n];

    // Construct the mesh with nodes at sample points.
    _mesh = new TetMesh();
    for (int i=0; i<n; ++i) {
      float x1i = x1[i];
      float x2i = x2[i];
      float x3i = x3[i];

      // Push out slightly any points that are on the bounding box. This 
      // perturbation inflates the convex hull of sample points, so that 
      // interpolation can be performed at points that would otherwise be 
      // on (or, due to rounding errors, outside) the convex hull.
      if (x1i==_x1min) x1i -= Math.ulp(x1i); 
      if (x1i==_x1max) x1i += Math.ulp(x1i);
      if (x2i==_x2min) x2i -= Math.ulp(x2i); 
      if (x2i==_x2max) x2i += Math.ulp(x2i);
      if (x3i==_x3min) x3i -= Math.ulp(x3i); 
      if (x3i==_x3max) x3i += Math.ulp(x3i);

      // Add a new node to the mesh, unless one already exists there.
      TetMesh.Node node = new TetMesh.Node(x1i,x2i,x3i);
      boolean added = _mesh.addNode(node);
      Check.argument(added,"each sample has unique coordinates");
      NodeData data = new NodeData();
      node.data = data;
      node.index = i;
      if (f!=null) 
        data.f = f[i];
      _nodes[i] = node;
    }
  }

  // Returns true if gradients are being used in interpolation.
  private boolean usingGradients() {
    return _haveGradients && _gradientPower>0.0;
  }

  // Adds ghost nodes to the mesh without any values or gradients.
  // Arrays x[ng], y[ng] and z[ng] contain coordinates of ng ghost nodes.
  private void addGhostNodes(float[] x, float[] y, float[] z) {
    int ng = x.length;
    for (int ig=0; ig<ng; ++ig) {
      float xg = x[ig];
      float yg = y[ig];
      float zg = z[ig];
      TetMesh.PointLocation pl = _mesh.locatePoint(xg,yg,zg);
      if (pl.isOutside()) {
        TetMesh.Node n = new TetMesh.Node(xg,yg,zg);
        n.data = new NodeData();
        n.index = -1-ig; // ghost nodes have negative indices
        _mesh.addNode(n);
      }
    }
  }

  // Adds ghost nodes to the mesh with estimated values and gradients.
  // Arrays x[ng], y[ng] and z[ng] contain coordinates of ng ghost nodes.
  // Function values and gradients are computed by inverse-distance 
  // weighted least-squares fitting of function values for real nodes.
  private void addGhostNodesWithValues(float[] x, float[] y, float[] z) {
    int ng = x.length;
    int nn = _nodes.length;

    // Construct ng systems of nn equations for 4 fitting parameters
    DMatrix[] a = new DMatrix[ng];
    DMatrix[] b = new DMatrix[ng];
    for (int ig=0; ig<ng; ++ig) {
      a[ig] = new DMatrix(nn,4); // nn equations for 4 parameters
      b[ig] = new DMatrix(nn,1); // nn function values to fit
    }
    for (int in=0; in<nn; ++in) {
      TetMesh.Node n = _nodes[in];
      double fn = f(n);
      double xn = n.xp();
      double yn = n.yp();
      double zn = n.zp();
      for (int ig=0; ig<ng; ++ig) {
        double xg = x[ig];
        double yg = y[ig];
        double zg = z[ig];
        double dx = xn-xg;
        double dy = yn-yg;
        double dz = zn-zg;
        double wn = 1.0/Math.sqrt(dx*dx+dy*dy+dz*dz);
        a[ig].set(in,0,wn);
        a[ig].set(in,1,wn*dx);
        a[ig].set(in,2,wn*dy);
        a[ig].set(in,3,wn*dz);
        b[ig].set(in,0,wn*fn);
      }
    }

    // Construct and add ghost nodes that are outside the convex hull.
    // For each ghost node added, use weighted least-squares fitting to
    // estimate the function value and gradient.
    for (int ig=0; ig<ng; ++ig) {
      float xg = x[ig];
      float yg = y[ig];
      float zg = z[ig];
      TetMesh.PointLocation pl = _mesh.locatePoint(xg,yg,zg);
      if (pl.isOutside()) {
        TetMesh.Node n = new TetMesh.Node(xg,yg,zg);
        n.index = -1-ig; // ghost nodes have negative indices
        _mesh.addNode(n);
        NodeData data = new NodeData();
        n.data = data;
        DMatrixQrd qrd = new DMatrixQrd(a[ig]);
        if (qrd.isFullRank()) { // if more than three nodes (or what?), ...
          DMatrix s = qrd.solve(b[ig]);
          data.f = (float)s.get(0,0);
          data.gx = (float)s.get(1,0);
          data.gy = (float)s.get(2,0);
          data.gz = (float)s.get(3,0);
        } else { // otherwise, just use value of nearest node
          TetMesh.Node m = _mesh.findNodeNearest(xg,yg,zg);
          data.f = f(m);
          data.gx = 0.0f;
          data.gy = 0.0f;
          data.gz = 0.0f;
        }
      }
    }
  }

  // Removes any ghost nodes from the mesh.
  private void removeGhostNodes() {

    // First make a list of all ghost nodes.
    ArrayList<TetMesh.Node> gnodes = new ArrayList<TetMesh.Node>(8);
    TetMesh.NodeIterator ni = _mesh.getNodes();
    while (ni.hasNext()) {
      TetMesh.Node n = ni.next();
      if (ghost(n))
        gnodes.add(n);
    }

    // Then remove the ghost nodes from the mesh.
    for (TetMesh.Node gnode:gnodes)
      _mesh.removeNode(gnode);
  }

  // Computes Sibson volumes for the specified point (x,y,z).
  // Returns true, if successful; false, otherwise.
  private double computeVolumes(float x, float y, float z) {
    if (!getNaturalNabors(x,y,z))
      return 0.0;
    return _va.accumulateVolumes(x,y,z,_mesh,_nodeList,_tetList);
  }

  // Returns true if not using bounding box or if point is inside the box.
  private boolean inBounds(float x1, float x2, float x3) {
    return !_useBoundingBox ||
           _x1bmn<=x1 && x1<=_x1bmx &&
           _x2bmn<=x2 && x2<=_x2bmx &&
           _x3bmn<=x3 && x3<=_x3bmx;
  }

  // Gets lists of natural neighbor nodes and tets of point (x,y,z).
  // Before building the lists, node and tet marks are cleared. Then,
  // as nodes and tets are added to the lists, they are marked, and 
  // node volumes are initialized to zero.
  // Returns true, if the lists are not empty; false, otherwise.
  private boolean getNaturalNabors(float x, float y, float z) {
    _mesh.clearNodeMarks();
    _mesh.clearTetMarks();
    _nodeList.clear();
    _tetList.clear();
    TetMesh.PointLocation pl = _mesh.locatePoint(x,y,z);
    if (pl.isOutside())
      return false;
    addTet(x,y,z,pl.tet());
    return true;
  }
  private void addTet(double xp, double yp, double zp, TetMesh.Tet tet) {
    _mesh.mark(tet);
    _tetList.add(tet);
    addNode(tet.nodeA());
    addNode(tet.nodeB());
    addNode(tet.nodeC());
    addNode(tet.nodeD());
    TetMesh.Tet ta = tet.tetA();
    TetMesh.Tet tb = tet.tetB();
    TetMesh.Tet tc = tet.tetC();
    TetMesh.Tet td = tet.tetD();
    if (needTet(xp,yp,zp,ta)) addTet(xp,yp,zp,ta);
    if (needTet(xp,yp,zp,tb)) addTet(xp,yp,zp,tb);
    if (needTet(xp,yp,zp,tc)) addTet(xp,yp,zp,tc);
    if (needTet(xp,yp,zp,td)) addTet(xp,yp,zp,td);
  }
  private void addNode(TetMesh.Node node) {
    if (_mesh.isMarked(node))
      return;
    _mesh.mark(node);
    _nodeList.add(node);
    NodeData data = data(node);
    data.volume = 0.0;
  }
  private boolean needTet(double xp, double yp, double zp, TetMesh.Tet tet) {
    if (tet==null || _mesh.isMarked(tet))
      return false;
    TetMesh.Node na = tet.nodeA();
    TetMesh.Node nb = tet.nodeB();
    TetMesh.Node nc = tet.nodeC();
    TetMesh.Node nd = tet.nodeD();
    double xa = na.xp(), ya = na.yp(), za = na.zp();
    double xb = nb.xp(), yb = nb.yp(), zb = nb.zp();
    double xc = nc.xp(), yc = nc.yp(), zc = nc.zp();
    double xd = nd.xp(), yd = nd.yp(), zd = nd.zp();
    return Geometry.inSphere(xa,ya,za,xb,yb,zb,xc,yc,zc,xd,yd,zd,xp,yp,zp)>0.0;
  }

  // C0 interpolation; does not use gradients.
  private float interpolate0(double vsum) {
    double vfsum = 0.0;
    int nnode = _nodeList.nnode();
    TetMesh.Node[] nodes = _nodeList.nodes();
    for (int inode=0; inode<nnode; ++inode) {
      TetMesh.Node node = nodes[inode];
      float f = f(node);
      double v = volume(node);
      vfsum += v*f;
    }
    return (float)(vfsum/vsum);
  }

  // C1 interpolation; uses gradients.
  private float interpolate1(double vsum, double x, double y, double z) {
    int nnode = _nodeList.nnode();
    TetMesh.Node[] nodes = _nodeList.nodes();
    double fs = 0.0;
    double es = 0.0;
    double wds = 0.0;
    double wdds = 0.0;
    double wods = 0.0;
    for (int inode=0; inode<nnode; ++inode) {
      TetMesh.Node n = nodes[inode];
      double f = f(n);
      double gx = gx(n);
      double gy = gy(n);
      double gz = gz(n);
      double v = volume(n);
      double w = v/vsum;
      double xn = n.xp();
      double yn = n.yp();
      double zn = n.zp();
      double dx = x-xn;
      double dy = y-yn;
      double dz = z-zn;
      double dd = dx*dx+dy*dy+dz*dz;
      if (dd==0.0)
        return (float)f;
      double d = Math.pow(dd,0.5*_gradientPower);
      double wd = w*d;
      double wod = w/d;
      double wdd = w*dd;
      es += wod*(f+gx*dx+gy*dy+gz*dz);
      fs += w*f;
      wds += wd;
      wdds += wdd;
      wods += wod;
    }
    es /= wods;
    double alpha = wds/wods;
    double beta = wdds;
    return (float)((alpha*fs+beta*es)/(alpha+beta));
  }

  // Estimates gradient vectors for real (not ghost) nodes in the mesh. 
  // Uses Sibson's (1981) method, which yields gradients that will 
  // interpolate precisely a spherical quadratic of the form 
  // f(x) = f + g'x + h*x'x, for scalars f and h and gradient vector g. 
  // Gradients for nodes on the convex hull are not modified by this method.
  private void estimateGradients() {
    int nnode = _nodes.length;
    for (int inode=0; inode<nnode; ++inode)
      estimateGradient(_nodes[inode]);
    _haveGradients = true;
  }

  // Estimates the gradient for one node. This method temporarily
  // removes the node from from the mesh, and then adds it back,
  // after computing the gradient. Gradients for nodes on the convex
  // hull are not modified.
  private void estimateGradient(TetMesh.Node n) {
    NodeData data = data(n);
    double fn = data.f;
    double xn = n.xp();
    double yn = n.yp();
    double zn = n.zp();
    _mesh.removeNode(n);
    double vsum = computeVolumes((float)xn,(float)yn,(float)zn);
    _mesh.addNode(n);
    if (vsum>0.0) {
      int nm = _nodeList.nnode();
      TetMesh.Node[] ms = _nodeList.nodes();
      double hxx = 0.0, hxy = 0.0, hxz = 0.0,
                        hyy = 0.0, hyz = 0.0,
                                   hzz = 0.0;
      double px = 0.0, py = 0.0, pz = 0.0;
      double nr = 0; // number of real (not ghost) natural neighbor nodes
      for (int im=0; im<nm; ++im) {
        TetMesh.Node m = ms[im];
        if (!ghost(m)) {
          double fm = f(m);
          double wm = volume(m);
          double xm = m.xp();
          double ym = m.yp();
          double zm = m.zp();
          double df = fn-fm;
          double dx = xn-xm;
          double dy = yn-ym;
          double dz = zn-zm;
          double ds = wm/(dx*dx+dy*dy+dz*dz);
          hxx += ds*dx*dx; // 3x3 symmetric positive-definite matrix
          hxy += ds*dx*dy;
          hxz += ds*dx*dz;
          hyy += ds*dy*dy;
          hyz += ds*dy*dz;
          hzz += ds*dz*dz;
          px += ds*dx*df; // right-hand-side column vector
          py += ds*dy*df;
          pz += ds*dz*df;
          ++nr;
        }
      }
      if (nr>2) { // need at least three real natural neighbor nodes
        double lxx = Math.sqrt(hxx); // Cholesky decomposition
        double lxy = hxy/lxx;
        double lxz = hxz/lxx;
        double dyy = hyy-lxy*lxy;
        double lyy = Math.sqrt(dyy);
        double lyz = (hyz-lxz*lxy)/lyy;
        double dzz = hzz-lxz*lxz-lyz*lyz;
        double lzz = Math.sqrt(dzz);
        double qx = px/lxx; // forward elimination
        double qy = (py-lxy*qx)/lyy;
        double qz = (pz-lxz*qx-lyz*qy)/lzz;
        double gz = qz/lzz; // back substitution
        double gy = (qy-lyz*gz)/lyy;
        double gx = (qx-lxy*gy-lxz*gz)/lxx;
        data.gx = (float)gx;
        data.gy = (float)gy;
        data.gz = (float)gz;
      }
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////////////////////////////////////////////
  // Given a point (xp,yp,zp) at which to interpolate, an implementation of 
  // natural neighbor interpolation must accumulate volumes for all natural 
  // neighbor nodes in the the specified node list. This abstract base
  // class maintains the total volume accumulated for all nodes.
  private static abstract class VolumeAccumulator {
    public abstract double accumulateVolumes(
      double xp, double yp, double zp,
      TetMesh mesh, TetMesh.NodeList nodeList, TetMesh.TetList tetList);
    protected void clear() {
      _sum = 0.0;
    }
    protected double sum() {
      return _sum;
    }
    protected void accumulate(TetMesh.Node node, double volume) {
      if (ghost(node)) return; // ignore ghost nodes!
      NodeData data = data(node);
      data.volume += volume;
      _sum += volume;
    }
    private double _sum;
  }
  
  ///////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////////////////////////////////////////////
  private static class WatsonSambridge extends VolumeAccumulator {

    public double accumulateVolumes(
      double xp, double yp, double zp,
      TetMesh mesh, TetMesh.NodeList nodeList, TetMesh.TetList tetList)
    {
      clear();
      int ntet = tetList.ntet();
      TetMesh.Tet[] tets = tetList.tets();
      for (int itet=0; itet<ntet; ++itet) {
        TetMesh.Tet tet = tets[itet];
        TetMesh.Node na = tet.nodeA();
        TetMesh.Node nb = tet.nodeB();
        TetMesh.Node nc = tet.nodeC();
        TetMesh.Node nd = tet.nodeD();
        double xa = na.xp(), ya = na.yp(), za = na.zp();
        double xb = nb.xp(), yb = nb.yp(), zb = nb.zp();
        double xc = nc.xp(), yc = nc.yp(), zc = nc.zp();
        double xd = nd.xp(), yd = nd.yp(), zd = nd.zp();
        Geometry.centerSphere(xp,yp,zp,xb,yb,zb,xc,yc,zc,xd,yd,zd,_ca);
        Geometry.centerSphere(xp,yp,zp,xa,ya,za,xd,yd,zd,xc,yc,zc,_cb);
        Geometry.centerSphere(xp,yp,zp,xa,ya,za,xb,yb,zb,xd,yd,zd,_cc);
        Geometry.centerSphere(xp,yp,zp,xa,ya,za,xc,yc,zc,xb,yb,zb,_cd);
        Geometry.centerSphere(xa,ya,za,xb,yb,zb,xc,yc,zc,xd,yd,zd,_ct);
        double va = volume(_cb,_cc,_cd,_ct);
        double vb = volume(_ca,_cd,_cc,_ct);
        double vc = volume(_ca,_cb,_cd,_ct);
        double vd = volume(_ca,_cc,_cb,_ct);
        accumulate(na,va);
        accumulate(nb,vb);
        accumulate(nc,vc);
        accumulate(nd,vd);
      }
      return sum();
    }
    private double[] _ca = new double[3]; // circumcenter of fake tet pbcd
    private double[] _cb = new double[3]; // circumcenter of fake tet padc
    private double[] _cc = new double[3]; // circumcenter of fake tet pabd
    private double[] _cd = new double[3]; // circumcenter of fake tet pacb
    private double[] _ct = new double[3]; // circumcenter of real tet abcd
    private double volume(double[] ci, double[] cj, double[] ck, double[] ct) {
      double xt = ct[0],    yt = ct[1],    zt = ct[2];
      double xi = ci[0]-xt, yi = ci[1]-yt, zi = ci[2]-zt;
      double xj = cj[0]-xt, yj = cj[1]-yt, zj = cj[2]-zt;
      double xk = ck[0]-xt, yk = ck[1]-yt, zk = ck[2]-zt;
      return xi*(yj*zk-yk*zj)+yi*(zj*xk-zk*xj)+zi*(xj*yk-xk*yj);
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////////////////////////////////////////////
  private static class BraunSambridge extends VolumeAccumulator {

    public double accumulateVolumes(
      double x1i, double x2i, double x3i,
      TetMesh mesh, TetMesh.NodeList nodeList, TetMesh.TetList tetList)
    {
      clear();

      // For all natural neighbors, ...
      int nnode = nodeList.nnode();
      TetMesh.Node[] nodes = nodeList.nodes();
      for (int j=0; j<nnode; ++j) {
        TetMesh.Node jnode = nodes[j];
        double x1j = jnode.xp();
        double x2j = jnode.yp();
        double x3j = jnode.zp();

        // Clear the polyhedron for the j'th natural neighbor.
        _lv.clear();

        // Add half-space of points closer to point pi than to point pj.
        // For this half-space we set b = 0, which is equivalent to 
        // making the midpoint ps the origin for the polyhedron.
        // A half-space with b = 0 speeds up Lassere's algorithm.
        double x1s = 0.5*(x1j+x1i);
        double x2s = 0.5*(x2j+x2i);
        double x3s = 0.5*(x3j+x3i);
        double x1d = x1j-x1i;
        double x2d = x2j-x2i;
        double x3d = x3j-x3i;
        _lv.addHalfSpace(x1d,x2d,x3d,0.0); // note b = 0 here

        // For all other natural neighbors, ...
        for (int k=0; k<nnode; ++k) {
          if (j==k) continue;
          TetMesh.Node knode = nodes[k];

          // Skip pair if they are not node neighbors in the mesh.
          if (mesh.findTet(jnode,knode)==null)
            continue;

          // Add half-space of points closer to pj than pk. 
          // Here we must account for the shift ps described above.
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

        // Volume of polyhedron for this natural neighbor.
        double vj = _lv.getVolume();
        accumulate(jnode,vj);
      }
      return sum();
    }
    private LasserreVolume _lv = new LasserreVolume(3);
  }

  ///////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////////////////////////////////////////////
  // An implementation developed by Dave Hale and Luming Liang at Colorado
  // School of Mines. The first step is to process the natural-neighbor 
  // tets while accumulating polyhedron volumes for natural-neighbor nodes.
  // During this first step, faces bounding the region of natural-neighbor
  // tets are stored in a list. In the second step, that list of faces is 
  // processed to complete the computation of volumes.
  // All geometric computations are performed in a shifted coordinate
  // system in which the interpolation point (xp,yp,zp) is the origin.
  private static class HaleLiang extends VolumeAccumulator {

    public double accumulateVolumes(
      double xp, double yp, double zp,
      TetMesh mesh, TetMesh.NodeList nodeList, TetMesh.TetList tetList)
    {
      clear();
      processTets(xp,yp,zp,mesh,tetList);
      boolean ok = processFaces(xp,yp,zp);
      return (ok)?sum():0.0;
    }

    private FaceList _faceList = new FaceList(); // list of tet faces
    private double[] _xyz = new double[3]; // a circumsphere center

    // Processes all natural-neighbor tets.
    private void processTets(
      double xp, double yp, double zp, 
      TetMesh mesh, TetMesh.TetList tetList) 
    {
      _faceList.clear();
      int ntet = tetList.ntet();
      TetMesh.Tet[] tets = tetList.tets();
      for (int itet=0; itet<ntet; ++itet) {
        TetMesh.Tet tet = tets[itet];
        TetMesh.Tet ta = tet.tetA();
        TetMesh.Tet tb = tet.tetB();
        TetMesh.Tet tc = tet.tetC();
        TetMesh.Tet td = tet.tetD();
        TetMesh.Node na = tet.nodeA();
        TetMesh.Node nb = tet.nodeB();
        TetMesh.Node nc = tet.nodeC();
        TetMesh.Node nd = tet.nodeD();
        tet.centerSphere(_xyz);
        double xt = _xyz[0]-xp, yt = _xyz[1]-yp, zt = _xyz[2]-zp;
        processTetNabor(xp,yp,zp,xt,yt,zt,mesh,ta,nb,nc,nd);
        processTetNabor(xp,yp,zp,xt,yt,zt,mesh,tb,nc,na,nd);
        processTetNabor(xp,yp,zp,xt,yt,zt,mesh,tc,nd,na,nb);
        processTetNabor(xp,yp,zp,xt,yt,zt,mesh,td,na,nc,nb);
      }
    }
    private void processTetNabor(
      double xp, double yp, double zp, 
      double xt, double yt, double zt, 
      TetMesh mesh, TetMesh.Tet ta,
      TetMesh.Node nb, TetMesh.Node nc, TetMesh.Node nd)
    {
      boolean saveFace = true;
      if (ta!=null && mesh.isMarked(ta)) {
        ta.centerSphere(_xyz);
        double xa = _xyz[0]-xp, ya = _xyz[1]-yp, za = _xyz[2]-zp;
        double xb = nb.xp()-xp, yb = nb.yp()-yp, zb = nb.zp()-zp;
        double xd = nd.xp()-xp, yd = nd.yp()-yp, zd = nd.zp()-zp;
        double xc = nc.xp()-xp, yc = nc.yp()-yp, zc = nc.zp()-zp;
        double xbd = xb+xd, ybd = yb+yd, zbd = zb+zd;
        double xdc = xd+xc, ydc = yd+yc, zdc = zd+zc;
        double xcb = xc+xb, ycb = yc+yb, zcb = zc+zb;
        double xyz = yt*za-ya*zt, yzx = zt*xa-za*xt, zxy = xt*ya-xa*yt;
        accumulate(nb,xbd*xyz+ybd*yzx+zbd*zxy);
        accumulate(nd,xdc*xyz+ydc*yzx+zdc*zxy);
        accumulate(nc,xcb*xyz+ycb*yzx+zcb*zxy);
        saveFace = false;
      }
      if (saveFace)
        addFace(xp,yp,zp,xt,yt,zt,nb,nc,nd);
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

    // Computes fake circumcenter and adds face to the face list.
    private void addFace(
      double xp, double yp, double zp,
      double xr, double yr, double zr, 
      TetMesh.Node na, TetMesh.Node nb, TetMesh.Node nc)
    {
      double xa = na.xp(), ya = na.yp(), za = na.zp();
      double xb = nb.xp(), yb = nb.yp(), zb = nb.zp();
      double xc = nc.xp(), yc = nc.yp(), zc = nc.zp();
      Geometry.centerSphere(xp,yp,zp,xa,ya,za,xb,yb,zb,xc,yc,zc,_xyz);
      double xf = _xyz[0]-xp, yf = _xyz[1]-yp, zf = _xyz[2]-zp;
      _faceList.add(na,nb,nc,xf,yf,zf,xr,yr,zr);
    }

    // Determines whether all faces in the face set have exactly three
    // neighbor faces. Should always be true, except for points (x,y,z)
    // that lie on (over very near?) the convex hull of the mesh.
    private boolean faceNaborsOk() {
      int nface = _faceList.nface();
      ArrayList<Face> faces = _faceList.faces();
      for (int iface=0; iface<nface; ++iface) {
        Face face = faces.get(iface);
        if (face.fa==null || face.fb==null || face.fc==null)
          return false;
      }
      return true;
    }

    // Processes all faces in the face list.
    private boolean processFaces(double xp, double yp, double zp) {
      if (!faceNaborsOk())
        return false;
      int nface = _faceList.nface();
      ArrayList<Face> faces = _faceList.faces();
      for (int iface=0; iface<nface; ++iface)
        processFace(xp,yp,zp,faces.get(iface));
      return true;
    }

    // Processes one face.
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
      accumulate(na,vab); accumulate(nb,-vab); // here we
      accumulate(nb,vbc); accumulate(nc,-vbc); // must go
      accumulate(nc,vca); accumulate(na,-vca); // both ways!

     // Accumulate volumes for Voronoi edges between face neighbors.
      Face fa = face.fa; x2 = fa.xf; y2 = fa.yf; z2 = fa.zf;
      xyz = y1*z2-y2*z1; yzx = z1*x2-z2*x1; zxy = x1*y2-x2*y1;
      accumulate(nb,xb *xyz+yb *yzx+zb *zxy);
      accumulate(nc,xbc*xyz+ybc*yzx+zbc*zxy);
      Face fb = face.fb; x2 = fb.xf; y2 = fb.yf; z2 = fb.zf;
      xyz = y1*z2-y2*z1; yzx = z1*x2-z2*x1; zxy = x1*y2-x2*y1;
      accumulate(nc,xc *xyz+yc *yzx+zc *zxy);
      accumulate(na,xca*xyz+yca*yzx+zca*zxy);
      Face fc = face.fc; x2 = fc.xf; y2 = fc.yf; z2 = fc.zf;
      xyz = y1*z2-y2*z1; yzx = z1*x2-z2*x1; zxy = x1*y2-x2*y1;
      accumulate(na,xa *xyz+ya *yzx+za *zxy);
      accumulate(nb,xab*xyz+yab*yzx+zab*zxy);
    }

    // A list of faces that represent the boundary of the Voronoi
    // polyhedron of the point (x,y,z) at which to interpolate.
    // As faces are added to this list, we hook them up to any
    // face neighbors that are already in the list. Therefore, after 
    // all faces have been added to the list, each face should have 
    // exactly three face neighbors.
    private static class FaceList {
      private int _nface;
      private ArrayList<Face> _faces = new ArrayList<Face>(48);
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
  }
}
