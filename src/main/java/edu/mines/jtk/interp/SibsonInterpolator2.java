/****************************************************************************
Copyright (c) 2009, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.interp;

import java.util.ArrayList;

import edu.mines.jtk.dsp.Sampling;
import edu.mines.jtk.la.DMatrix;
import edu.mines.jtk.la.DMatrixQrd;
import edu.mines.jtk.mesh.Geometry;
import edu.mines.jtk.mesh.TriMesh;
import edu.mines.jtk.util.Check;

/**
 * Sibson interpolation of scattered samples of 2D functions f(x1,x2).
 * Sibson's (1981) interpolant at any point (x1,x2) is a weighted sum of 
 * values for a nearby subset of samples, the so-called natural neighbors
 * of that point. Sibson interpolation is often called "natural neighbor
 * interpolation."
 * <p>
 * The interpolation weights, also called "Sibson coordinates", are areas 
 * of overlapping Voronoi polygons, normalized so that they sum to one for 
 * any interpolation point (x1,x2). Various implementations of Sibson 
 * interpolation differ primarily in how those areas are computed.
 * <p>
 * The basic Sibson interpolant is C1 (that is, it's gradient is continuous) 
 * at all points (x1,x2) except at the sample points, where it is C0. 
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
public class SibsonInterpolator2 {

  /**
   * The implementation method. Methods differ in the algorithm by which 
   * Sibson coordinates (polygon areas) are computed for natural neighbors.
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
     * this method uses Lasserre's (1983) algorithm for computing the areas 
     * of polygons without computing their vertices. This method is much
     * slower than the other methods provided here. It may also suffer from 
     * numerical instability due to divisions in Lasserre's algorithm.
     */
    BRAUN_SAMBRIDGE,
    /**
     * The Watson-Sambridge algorithm. Developed by Watson (1992) and
     * described further by Sambridge et al. (1995). Though simplest to
     * implement and fast, this method is inaccurate (sometimes wildly so) 
     * at interpolation points (x1,x2) that lie on or near edges of a 
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
   * Function values f(x1,x2) are not set and are assumed to be zero.
   * Uses the most accurate and efficient implementation.
   * Coordinates for each sample must be unique.
   * @param x1 array of sample x1 coordinates.
   * @param x2 array of sample x2 coordinates.
   */
  public SibsonInterpolator2(float[] x1, float[] x2) {
    this(Method.HALE_LIANG,null,x1,x2);
  }

  /**
   * Constructs an interpolator with specified samples.
   * Uses the most accurate and efficient implementation.
   * Coordinates for each sample must be unique.
   * @param f array of sample values f(x1,x2).
   * @param x1 array of sample x1 coordinates.
   * @param x2 array of sample x2 coordinates.
   */
  public SibsonInterpolator2(float[] f, float[] x1, float[] x2) {
    this(Method.HALE_LIANG,f,x1,x2);
  }

  /**
   * Constructs an interpolator with specified method and sample coordinates.
   * Function values f(x1,x2) are not set and are assumed to be zero.
   * Coordinates for each sample must be unique.
   * <p>
   * This constructor is provided primarily for testing.
   * The default Hale-Liang method is accurate and fast.
   * @param method the implementation method.
   * @param x1 array of sample x1 coordinates.
   * @param x2 array of sample x2 coordinates.
   */
  public SibsonInterpolator2(Method method, float[] x1, float[] x2) {
    this(method,null,x1,x2);
  }

  /**
   * Constructs an interpolator with specified method and samples.
   * Coordinates for each sample must be unique.
   * <p>
   * This constructor is provided primarily for testing.
   * The default Hale-Liang method is accurate and fast.
   * @param method the implementation method.
   * @param f array of sample values f(x1,x2).
   * @param x1 array of sample x1 coordinates.
   * @param x2 array of sample x2 coordinates.
   */
  public SibsonInterpolator2(
    Method method, float[] f, float[] x1, float[] x2) 
  {
    makeMesh(f,x1,x2);
    _nodeList = new TriMesh.NodeList();
    _triList = new TriMesh.TriList();
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
   * @param f array of sample values f(x1,x2).
   * @param x1 array of sample x1 coordinates.
   * @param x2 array of sample x2 coordinates.
   */
  public void setSamples(float[] f, float[] x1, float[] x2) {
    makeMesh(f,x1,x2);
    _haveGradients = false;
    if (_gradientPower>0.0)
      estimateGradients();
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
   * Sets gradients for all samples. If the gradient power is currently 
   * zero, then this method also sets the gradient power to one. To later
   * ignore gradients that have been set, the gradient power can be reset
   * to zero.
   * @param g1 array of 1st components of gradients.
   * @param g2 array of 2nd components of gradients.
   */
  public void setGradients(float[] g1, float[] g2) {
    int n = g1.length;
    for (int i=0; i<n; ++i) {
      TriMesh.Node node = _nodes[i];
      NodeData data = data(node);
      data.gx = g1[i];
      data.gy = g2[i];
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
   * Sets a bounding box for this interpolator.
   * Sibson interpolation is undefined for points (x1,x2) outside the 
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
   */
  public void setBounds(float x1min, float x1max, float x2min, float x2max) {
    Check.argument(x1min<x1max,"x1min<x1max");
    Check.argument(x2min<x2max,"x2min<x2max");

    // Remember the specified bounding box.
    _x1bmn = x1min; _x1bmx = x1max;
    _x2bmn = x2min; _x2bmx = x2max;
    _useBoundingBox = true;

    // Compute coordinates for ghost nodes, and add them to the mesh.
    float scale = 1.0f;
    float x1avg = 0.5f*(x1min+x1max);
    float x2avg = 0.5f*(x2min+x2max);
    float x1dif = Math.max(x1max-_x1min,_x1max-x1min);
    float x2dif = Math.max(x2max-_x2min,_x2max-x2min);
    float x1pad = scale*x1dif;
    float x2pad = scale*x2dif;
    x1min -= x1pad; x1max += x1pad;
    x2min -= x2pad; x2max += x2pad;
    float[] x1g = {x1min,x1max,x1avg,x1avg};
    float[] x2g = {x2avg,x2avg,x2min,x2max};
    addGhostNodes(x1g,x2g);
  }

  /**
   * Sets bounds for this interpolator using specified samplings.
   * Values interpolated within the bounding box of these samplings
   * are never null, even when the interpolation point (x1,x2) 
   * lies outside that box.
   * <p>
   * If gradients are to be computed (not specified explicitly), it is best 
   * to set bounds by calling this method before computing gradients.
   * @param s1 sampling of x1.
   * @param s2 sampling of x2.
   */
  public void setBounds(Sampling s1, Sampling s2) {
    setBounds((float)s1.getFirst(),(float)s1.getLast(),
              (float)s2.getFirst(),(float)s2.getLast());
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
   * @return the interpolated value.
   */
  public float interpolate(float x1, float x2) {
    if (!inBounds(x1,x2))
      return _fnull;
    double asum = computeAreas(x1,x2);
    if (asum<=0.0)
      return _fnull;
    if (usingGradients()) {
      return interpolate1(asum,x1,x2);
    } else {
      return interpolate0(asum);
    }
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

  /**
   * Gets sample indices and interpolation weights for the specified point.
   * Given a point (x1,x2), the sample indices represent the natural
   * neighbors of that point, and the interpolation weights are its Sibson
   * coordinates. Indices correspond to the arrays that are specified when
   * constructing this interpolator.
   * <p>
   * Indices and weights are especially useful in applications where they 
   * can be reused, say, to interpolate multiple function values at a 
   * single point.
   * @param x1 the x1 coordinate of the point.
   * @param x2 the x2 coordinate of the point.
   * @return array of sample indices and weights; null if none.
   */
  public IndexWeight[] getIndexWeights(float x1, float x2) {
    if (!inBounds(x1,x2))
      return null;
    float wsum = (float)computeAreas(x1,x2);
    if (wsum==0.0f)
      return null;
    float wscl = 1.0f/wsum;
    int nnode = _nodeList.nnode();
    TriMesh.Node[] nodes = _nodeList.nodes();
    IndexWeight[] iw = new IndexWeight[nnode];
    for (int inode=0; inode<nnode; ++inode) {
      TriMesh.Node node = nodes[inode];
      int i = node.index;
      float w = (float)area(node)*wscl;
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
      TriMesh.Node node = _nodes[i[iv]];
      float xn = node.x();
      float yn = node.y();
      fv[iv] = interpolate(xn,yn);
    }
    for (int iv=0; iv<nv; ++iv)
      _mesh.addNode(_nodes[i[iv]]);
    return fv;
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  // Data associated with all nodes in the tri mesh.
  private static class NodeData {
    float f,gx,gy; // function values and gradient
    double area; // area for Sibson weight
  }
  private static NodeData data(TriMesh.Node node) {
    return (NodeData)node.data;
  }
  private static float f(TriMesh.Node node) {
    return data(node).f;
  }
  private static float gx(TriMesh.Node node) {
    return data(node).gx;
  }
  private static float gy(TriMesh.Node node) {
    return data(node).gy;
  }
  private static double area(TriMesh.Node node) {
    return data(node).area;
  }
  private static boolean ghost(TriMesh.Node node) {
    return node.index<0;
  }

  private TriMesh _mesh; // the mesh
  private TriMesh.Node[] _nodes; // array of real (not ghost) nodes
  private TriMesh.NodeList _nodeList; // list of natural neighbor nodes
  private TriMesh.TriList _triList; // list of natural neighbor tris
  private AreaAccumulator _va; // accumulates Sibson's areas
  private boolean _haveGradients; // true if mesh nodes have gradients
  private double _gradientPower; // power of gradients
  private float _fnull; // returned when interpolation point out of bounds
  private float _x1min,_x1max,_x2min,_x2max; // bounds on specified (x1,x2)
  private float _x1bmn,_x1bmx,_x2bmn,_x2bmx; // bounding box if bounds set
  private boolean _useBoundingBox; // true if using bounding box

  // Builds the tri mesh from specified scattered samples.
  private void makeMesh(float[] f, float[] x1, float[] x2) {

    // Find bounding box for sample points.
    int n = x1.length;
    _x1min = x1[0]; _x1max = x1[0];
    _x2min = x2[0]; _x2max = x2[0];
    for (int i=1; i<n; ++i) {
      if (x1[i]<_x1min) _x1min = x1[i]; 
      if (x1[i]>_x1max) _x1max = x1[i];
      if (x2[i]<_x2min) _x2min = x2[i]; 
      if (x2[i]>_x2max) _x2max = x2[i];
    }

    // ArrayMath of nodes, one for each specified sample.
    _nodes = new TriMesh.Node[n];

    // Construct the mesh with nodes at sample points.
    _mesh = new TriMesh();
    for (int i=0; i<n; ++i) {
      float x1i = x1[i];
      float x2i = x2[i];

      // Push out slightly any points that are on the bounding box. This 
      // perturbation inflates the convex hull of sample points, so that 
      // interpolation can be performed at points that would otherwise be 
      // on (or, due to rounding errors, outside) the convex hull.
      if (x1i==_x1min) x1i -= Math.ulp(x1i); 
      if (x1i==_x1max) x1i += Math.ulp(x1i);
      if (x2i==_x2min) x2i -= Math.ulp(x2i); 
      if (x2i==_x2max) x2i += Math.ulp(x2i);

      // Add a new node to the mesh.
      TriMesh.Node node = new TriMesh.Node(x1i,x2i);
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
  // The arrays x[ng] and y[ng] contain coordinates of ng ghost nodes.
  private void addGhostNodes(float[] x, float[] y) {
    int ng = x.length;
    for (int ig=0; ig<ng; ++ig) {
      float xg = x[ig];
      float yg = y[ig];
      TriMesh.PointLocation pl = _mesh.locatePoint(xg,yg);
      if (pl.isOutside()) {
        TriMesh.Node n = new TriMesh.Node(xg,yg);
        n.data = new NodeData();
        n.index = -1-ig; // ghost nodes have negative indices
        _mesh.addNode(n);
      }
    }
  }

  // Adds ghost nodes to the mesh with estimated values and gradients.
  // The arrays x[ng] and y[ng] contain coordinates of ng ghost nodes.
  // Function values and gradients are computed by inverse-distance 
  // weighted least-squares fitting of function values for real nodes.
  private void addGhostNodesWithValues(float[] x, float[] y) {
    int ng = x.length;
    int nn = _nodes.length;

    // Construct ng systems of nn equations for 3 fitting parameters
    DMatrix[] a = new DMatrix[ng];
    DMatrix[] b = new DMatrix[ng];
    for (int ig=0; ig<ng; ++ig) {
      a[ig] = new DMatrix(nn,3); // nn equations for 3 parameters
      b[ig] = new DMatrix(nn,1); // nn function values to fit
    }
    for (int in=0; in<nn; ++in) {
      TriMesh.Node n = _nodes[in];
      double fn = f(n);
      double xn = n.xp();
      double yn = n.yp();
      for (int ig=0; ig<ng; ++ig) {
        double xg = x[ig];
        double yg = y[ig];
        double dx = xn-xg;
        double dy = yn-yg;
        double wn = 1.0/Math.sqrt(dx*dx+dy*dy);
        a[ig].set(in,0,wn);
        a[ig].set(in,1,wn*dx);
        a[ig].set(in,2,wn*dy);
        b[ig].set(in,0,wn*fn);
      }
    }

    // Construct and add ghost nodes that are outside the convex hull.
    // For each ghost node added, use weighted least-squares fitting to
    // estimate the function value and gradient.
    for (int ig=0; ig<ng; ++ig) {
      float xg = x[ig];
      float yg = y[ig];
      TriMesh.PointLocation pl = _mesh.locatePoint(xg,yg);
      if (pl.isOutside()) {
        TriMesh.Node n = new TriMesh.Node(xg,yg);
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
        } else { // otherwise, just use value of nearest node
          TriMesh.Node m = _mesh.findNodeNearest(xg,yg);
          data.f = f(m);
          data.gx = 0.0f;
          data.gy = 0.0f;
        }
      }
    }
  }

  // Removes any ghost nodes from the mesh.
  private void removeGhostNodes() {

    // First make a list of all ghost nodes.
    ArrayList<TriMesh.Node> gnodes = new ArrayList<TriMesh.Node>(4);
    TriMesh.NodeIterator ni = _mesh.getNodes();
    while (ni.hasNext()) {
      TriMesh.Node n = ni.next();
      if (ghost(n))
        gnodes.add(n);
    }

    // Then remove the ghost nodes from the mesh.
    for (TriMesh.Node gnode:gnodes)
      _mesh.removeNode(gnode);
  }

  // Computes Sibson areas for the specified point (x,y).
  // Returns true, if successful; false, otherwise.
  private double computeAreas(float x, float y) {
    if (!getNaturalNabors(x,y))
      return 0.0;
    return _va.accumulateAreas(x,y,_mesh,_nodeList,_triList);
  }

  // Returns true if not using bounding box or if point is inside the box.
  private boolean inBounds(float x1, float x2) {
    return !_useBoundingBox ||
           _x1bmn<=x1 && x1<=_x1bmx &&
           _x2bmn<=x2 && x2<=_x2bmx;
  }

  // Gets lists of natural neighbor nodes and tris of point (x,y).
  // Before building the lists, node and tri marks are cleared. Then,
  // as nodes and tris are added to the lists, they are marked, and 
  // node areas are initialized to zero.
  // Returns true, if the lists are not empty; false, otherwise.
  private boolean getNaturalNabors(float x, float y) {
    _mesh.clearNodeMarks();
    _mesh.clearTriMarks();
    _nodeList.clear();
    _triList.clear();
    TriMesh.PointLocation pl = _mesh.locatePoint(x,y);
    if (pl.isOutside())
      return false;
    addTri(x,y,pl.tri());
    return true;
  }
  private void addTri(double xp, double yp, TriMesh.Tri tri) {
    _mesh.mark(tri);
    _triList.add(tri);
    addNode(tri.nodeA());
    addNode(tri.nodeB());
    addNode(tri.nodeC());
    TriMesh.Tri ta = tri.triA();
    TriMesh.Tri tb = tri.triB();
    TriMesh.Tri tc = tri.triC();
    if (needTri(xp,yp,ta)) addTri(xp,yp,ta);
    if (needTri(xp,yp,tb)) addTri(xp,yp,tb);
    if (needTri(xp,yp,tc)) addTri(xp,yp,tc);
  }
  private void addNode(TriMesh.Node node) {
    if (_mesh.isMarked(node))
      return;
    _mesh.mark(node);
    _nodeList.add(node);
    NodeData data = data(node);
    data.area = 0.0;
  }
  private boolean needTri(double xp, double yp, TriMesh.Tri tri) {
    if (tri==null || _mesh.isMarked(tri))
      return false;
    TriMesh.Node na = tri.nodeA();
    TriMesh.Node nb = tri.nodeB();
    TriMesh.Node nc = tri.nodeC();
    double xa = na.xp(), ya = na.yp();
    double xb = nb.xp(), yb = nb.yp();
    double xc = nc.xp(), yc = nc.yp();
    return Geometry.inCircle(xa,ya,xb,yb,xc,yc,xp,yp)>0.0;
  }

  // C0 interpolation; does not use gradients.
  private float interpolate0(double asum) {
    double afsum = 0.0;
    int nnode = _nodeList.nnode();
    TriMesh.Node[] nodes = _nodeList.nodes();
    for (int inode=0; inode<nnode; ++inode) {
      TriMesh.Node node = nodes[inode];
      float f = f(node);
      double a = area(node);
      afsum += a*f;
    }
    return (float)(afsum/asum);
  }

  // C1 interpolation; uses gradients.
  private float interpolate1(double asum, double x, double y) {
    int nnode = _nodeList.nnode();
    TriMesh.Node[] nodes = _nodeList.nodes();
    double fs = 0.0;
    double es = 0.0;
    double wds = 0.0;
    double wdds = 0.0;
    double wods = 0.0;
    for (int inode=0; inode<nnode; ++inode) {
      TriMesh.Node n = nodes[inode];
      double f = f(n);
      double gx = gx(n);
      double gy = gy(n);
      double a = area(n);
      double w = a/asum;
      double xn = n.xp();
      double yn = n.yp();
      double dx = x-xn;
      double dy = y-yn;
      double dd = dx*dx+dy*dy;
      if (dd==0.0)
        return (float)f;
      double d = Math.pow(dd,0.5*_gradientPower);
      double wd = w*d;
      double wod = w/d;
      double wdd = w*dd;
      es += wod*(f+gx*dx+gy*dy);
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
  private void estimateGradient(TriMesh.Node n) {
    NodeData data = data(n);
    double fn = data.f;
    double xn = n.xp();
    double yn = n.yp();
    _mesh.removeNode(n);
    double asum = computeAreas((float)xn,(float)yn);
    _mesh.addNode(n);
    if (asum>0.0) {
      int nm = _nodeList.nnode();
      TriMesh.Node[] ms = _nodeList.nodes();
      double hxx = 0.0, hxy = 0.0, hyy = 0.0;
      double px = 0.0, py = 0.0;
      double nr = 0; // number of real (not ghost) natural neighbor nodes
      for (int im=0; im<nm; ++im) {
        TriMesh.Node m = ms[im];
        if (!ghost(m)) {
          double fm = f(m);
          double wm = area(m);
          double xm = m.xp();
          double ym = m.yp();
          double df = fn-fm;
          double dx = xn-xm;
          double dy = yn-ym;
          double ds = wm/(dx*dx+dy*dy);
          hxx += ds*dx*dx; // 2x2 symmetric positive-definite matrix
          hxy += ds*dx*dy;
          hyy += ds*dy*dy;
          px += ds*dx*df; // right-hand-side column vector
          py += ds*dy*df;
          ++nr;
        }
      }
      if (nr>1) { // need at least two real natural neighbor nodes
        double lxx = Math.sqrt(hxx); // Cholesky decomposition
        double lxy = hxy/lxx;
        double dyy = hyy-lxy*lxy;
        double lyy = Math.sqrt(dyy);
        double qx = px/lxx; // forward elimination
        double qy = (py-lxy*qx)/lyy;
        double gy = qy/lyy; // back substitution
        double gx = (qx-lxy*gy)/lxx;
        data.gx = (float)gx;
        data.gy = (float)gy;
      }
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////////////////////////////////////////////
  // Given a point (xp,yp) at which to interpolate, an implementation of 
  // natural neighbor interpolation must accumulate areas for all natural 
  // neighbor nodes in the the specified node list. This abstract base
  // class maintains the total area accumulated for all nodes.
  private static abstract class AreaAccumulator {
    public abstract double accumulateAreas(
      double xp, double yp,
      TriMesh mesh, TriMesh.NodeList nodeList, TriMesh.TriList triList);
    protected void clear() {
      _sum = 0.0;
    }
    protected double sum() {
      return _sum;
    }
    protected void accumulate(TriMesh.Node node, double area) {
      if (ghost(node)) return; // ignore ghost nodes!
      NodeData data = data(node);
      data.area += area;
      _sum += area;
    }
    private double _sum;
  }
 
  ///////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////////////////////////////////////////////
  private static class WatsonSambridge extends AreaAccumulator {

    public double accumulateAreas(
      double xp, double yp,
      TriMesh mesh, TriMesh.NodeList nodeList, TriMesh.TriList triList)
    {
      clear();
      int ntri = triList.ntri();
      TriMesh.Tri[] tris = triList.tris();
      for (int itri=0; itri<ntri; ++itri) {
        TriMesh.Tri tri = tris[itri];
        TriMesh.Node na = tri.nodeA();
        TriMesh.Node nb = tri.nodeB();
        TriMesh.Node nc = tri.nodeC();
        double xa = na.xp(), ya = na.yp();
        double xb = nb.xp(), yb = nb.yp();
        double xc = nc.xp(), yc = nc.yp();
        Geometry.centerCircle(xp,yp,xb,yb,xc,yc,_ca);
        Geometry.centerCircle(xp,yp,xc,yc,xa,ya,_cb);
        Geometry.centerCircle(xp,yp,xa,ya,xb,yb,_cc);
        Geometry.centerCircle(xa,ya,xb,yb,xc,yc,_ct);
        double aa = area(_cb,_cc,_ct);
        double ab = area(_cc,_ca,_ct);
        double ac = area(_ca,_cb,_ct);
        accumulate(na,aa);
        accumulate(nb,ab);
        accumulate(nc,ac);
      }
      return sum();
    }
    private double[] _ca = new double[2]; // circumcenter of fake tri pbc
    private double[] _cb = new double[2]; // circumcenter of fake tri pca
    private double[] _cc = new double[2]; // circumcenter of fake tri pab
    private double[] _ct = new double[2]; // circumcenter of real tri abc
    private double area(double[] ci, double[] cj, double[] ct) {
      double xt = ct[0],    yt = ct[1];
      double xi = ci[0]-xt, yi = ci[1]-yt;
      double xj = cj[0]-xt, yj = cj[1]-yt;
      return xi*yj-xj*yi;
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////////////////////////////////////////////
  private static class BraunSambridge extends AreaAccumulator {

    public double accumulateAreas(
      double x1i, double x2i,
      TriMesh mesh, TriMesh.NodeList nodeList, TriMesh.TriList triList)
    {
      clear();

      // For all natural neighbors, ...
      int nnode = nodeList.nnode();
      TriMesh.Node[] nodes = nodeList.nodes();
      for (int j=0; j<nnode; ++j) {
        TriMesh.Node jnode = nodes[j];
        double x1j = jnode.xp();
        double x2j = jnode.yp();

        // Clear the polygon for the j'th natural neighbor.
        _lv.clear();

        // Add half-space of points closer to point pi than to point pj.
        // For this half-space we set b = 0, which is equivalent to 
        // making the midpoint ps the origin for the polygon.
        // A half-space with b = 0 speeds up Lassere's algorithm.
        double x1s = 0.5*(x1j+x1i);
        double x2s = 0.5*(x2j+x2i);
        double x1d = x1j-x1i;
        double x2d = x2j-x2i;
        _lv.addHalfSpace(x1d,x2d,0.0); // note b = 0 here

        // For all other natural neighbors, ...
        for (int k=0; k<nnode; ++k) {
          if (j==k) continue;
          TriMesh.Node knode = nodes[k];

          // Skip pair if they are not node neighbors in the mesh.
          if (mesh.findTri(jnode,knode)==null)
            continue;

          // Add half-space of points closer to pj than pk. 
          // Here we must account for the shift ps described above.
          double x1k = knode.xp();
          double x2k = knode.yp();
          double x1e = x1k-x1j;
          double x2e = x2k-x2j;
          double x1t = 0.5*(x1k+x1j)-x1s;
          double x2t = 0.5*(x2k+x2j)-x2s;
          _lv.addHalfSpace(x1e,x2e,x1e*x1t+x2e*x2t);
        }

        // Area of polygon for this natural neighbor.
        double aj = _lv.getVolume();
        accumulate(jnode,aj);
      }
      return sum();
    }
    private LasserreVolume _lv = new LasserreVolume(2);
  }

  ///////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////////////////////////////////////////////
  // An implementation developed by Dave Hale and Luming Liang at Colorado
  // School of Mines. The first step is to process the natural-neighbor 
  // tris while accumulating polygon areas for natural-neighbor nodes.
  // During this first step, edges bounding the region of natural-neighbor
  // tris are stored in a list. In the second step, that list of edges is 
  // processed to complete the computation of areas.
  // All geometric computations are performed in a shifted coordinate
  // system in which the interpolation point (xp,yp) is the origin.
  private static class HaleLiang extends AreaAccumulator {

    public double accumulateAreas(
      double xp, double yp,
      TriMesh mesh, TriMesh.NodeList nodeList, TriMesh.TriList triList)
    {
      clear();
      processTris(xp,yp,mesh,triList);
      boolean ok = processEdges();
      return (ok)?sum():0.0;
    }

    private EdgeList _edgeList = new EdgeList(); // list of tri edges
    private double[] _xy = new double[2]; // a circumsphere center

    // Processes all natural-neighbor tris.
    private void processTris(
      double xp, double yp,
      TriMesh mesh, TriMesh.TriList triList) 
    {
      _edgeList.clear();
      int ntri = triList.ntri();
      TriMesh.Tri[] tris = triList.tris();
      for (int itri=0; itri<ntri; ++itri) {
        TriMesh.Tri tri = tris[itri];
        TriMesh.Tri ta = tri.triA();
        TriMesh.Tri tb = tri.triB();
        TriMesh.Tri tc = tri.triC();
        TriMesh.Node na = tri.nodeA();
        TriMesh.Node nb = tri.nodeB();
        TriMesh.Node nc = tri.nodeC();
        tri.centerCircle(_xy);
        double xt = _xy[0]-xp, yt = _xy[1]-yp;
        processTriNabor(xp,yp,xt,yt,mesh,ta,nb,nc);
        processTriNabor(xp,yp,xt,yt,mesh,tb,nc,na);
        processTriNabor(xp,yp,xt,yt,mesh,tc,na,nb);
      }
    }
    private void processTriNabor(
      double xp, double yp,
      double xt, double yt,
      TriMesh mesh, TriMesh.Tri ta,
      TriMesh.Node nb, TriMesh.Node nc)
    {
      boolean saveEdge = true;
      if (ta!=null && mesh.isMarked(ta)) {
        ta.centerCircle(_xy);
        double xa = _xy[0]-xp;
        double ya = _xy[1]-yp;
        double xy = xt*ya-xa*yt;
        accumulate(nc,xy);
        saveEdge = false;
      }
      if (saveEdge)
        addEdge(xp,yp,xt,yt,nb,nc);
    }

    // A edge has two nodes and both fake and real circumcenters.
    // After all edges have been added to the edge list, they form
    // a Voronoi polygon, and the edges are in counter-clockwise 
    // (CCW) order around the interpolation point, and each edge has 
    // a reference to its CCW neighbor. Node B of one edge equals 
    // node A of its neighbor edge.
    private static class Edge {
      TriMesh.Node na,nb; // two nodes of this edge
      double xf,yf; // circumcenter of fake tri pab
      double xr,yr; // circumcenter of real tri abc
      Edge eb; // CCW neighbor of this edge
    }

    // Computes a fake circumcenter and adds an edge to the edge list.
    private void addEdge(
      double xp, double yp,
      double xr, double yr, 
      TriMesh.Node na, TriMesh.Node nb)
    {
      double xa = na.xp(), ya = na.yp();
      double xb = nb.xp(), yb = nb.yp();
      Geometry.centerCircle(xp,yp,xa,ya,xb,yb,_xy);
      double xf = _xy[0]-xp, yf = _xy[1]-yp;
      _edgeList.add(na,nb,xf,yf,xr,yr);
    }

    // Determines whether all edges in the edge set have a non-null
    // neighbor edge. This should always be true, except for points 
    // (x,y) that lie on (over very near?) the convex hull of the mesh.
    private boolean edgeNaborsOk() {
      int nedge = _edgeList.nedge();
      ArrayList<Edge> edges = _edgeList.edges();
      for (int iedge=0; iedge<nedge; ++iedge) {
        Edge edge = edges.get(iedge);
        if (edge.eb==null)
          return false;
      }
      return true;
    }

    // Processes all edges in the edge list.
    private boolean processEdges() {
      if (!edgeNaborsOk())
        return false;
      int nedge = _edgeList.nedge();
      ArrayList<Edge> edges = _edgeList.edges();
      for (int iedge=0; iedge<nedge; ++iedge)
        processEdge(edges.get(iedge));
      return true;
    }

    // Processes one edge.
    private void processEdge(Edge edge) {

      // Two edge nodes.
      TriMesh.Node na = edge.na, nb = edge.nb;

      // Accumulate areas for the Voronoi edge through the tri edge.
      double x1 = edge.xf;
      double y1 = edge.yf;
      double x2 = edge.xr;
      double y2 = edge.yr;
      double xy = x1*y2-x2*y1;
      accumulate(na, xy); 
      accumulate(nb,-xy);

     // Accumulate area for Voronoi edge between tri edge and its neighbor.
      Edge eb = edge.eb; 
      x2 = eb.xf; 
      y2 = eb.yf; 
      xy = x1*y2-x2*y1;
      accumulate(nb,xy);
    }

    // A list of edges that represent the boundary of the Voronoi
    // polygon of the point (x,y) at which to interpolate.
    // As edges are added to this list, we hook them up to their
    // edge neighbors if already in the list. Therefore, after 
    // all edges have been added to the list, each edge should have 
    // two edge neighbors, but has a reference to only one of them.
    private static class EdgeList {
      private int _nedge;
      private ArrayList<Edge> _edges = new ArrayList<Edge>(12);
      int nedge() {
        return _nedge;
      }
      ArrayList<Edge> edges() {
        return _edges;
      }
      void clear() {
        _nedge = 0;
      }
      void add(
        TriMesh.Node na, TriMesh.Node nb,
        double xf, double yf,
        double xr, double yr)
      {
        if (_nedge==_edges.size()) // rarely must we construct a
          _edges.add(new Edge()); // new edge like this, because we
        Edge edge = _edges.get(_nedge); // can reuse an existing edge
        Edge eb = null;
        int nfound = 0;
        for (int iedge=0; iedge<_nedge && nfound<2; ++iedge) {
          Edge ei = _edges.get(iedge);
          if (nb==ei.na) {
            eb = ei;
            ++nfound;
          }
          if (na==ei.nb) {
            ei.eb = edge;
            ++nfound;
          }
        }
        edge.na = na; edge.nb = nb;
        edge.xf = xf; edge.yf = yf;
        edge.xr = xr; edge.yr = yr;
        edge.eb = eb;
        ++_nedge;
      }
    }
  }
}
