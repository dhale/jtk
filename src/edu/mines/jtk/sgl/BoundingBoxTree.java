/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import static edu.mines.jtk.util.ArrayMath.*;
import edu.mines.jtk.util.Check;
import static edu.mines.jtk.util.MathPlus.max;

/**
 * A binary tree of axis-aligned bounding boxes for an array of points.
 * This tree is useful when constructing a bounding volume hierarchy for
 * large sets of geometric primitives such as triangles or quads.
 * <p>
 * Each node in the tree contains a bounding box for a a subset of points.
 * Those points are represented by a subarray of indices of point (x,y,z) 
 * coordinates. Point coordinates are specified when constructing a tree.
 * <p>
 * The bounding box for the root node is that for the entire set of points,
 * with indices 0 through n-1, where n is the total number of points in the 
 * tree. The tree recursively splits this bounding box in two so that each
 * child represents roughly half of the points in its parent. 
 * <p>
 * This recursive splitting continues while splits will create child nodes 
 * with numbers of points not less than a specified minimum. When the total
 * number of points in the tree is less than the specified minimum, then the
 * tree consists of only the root node.
 * <p>
 * A bounding box tree is much like a k-d tree for k=3 dimensions.
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.06.24
 */
public class BoundingBoxTree {

  /**
   * A node in the binary tree of bounding boxes.
   */
  public class Node {

    /**
     * Gets the bounding box for this node.
     * @return the bounding box.
     */
    public BoundingBox getBoundingBox() {
      return new BoundingBox(_bb);
    }

    /**
     * Gets the number of points in this node.
     * @return the number of points.
     */
    public int getSize() {
      return 1+_kmax-_kmin;
    }

    /**
     * Gets indices of points in this node.
     * @return the array of indices.
     */
    public int[] getIndices() {
      return copy(1+_kmax-_kmin,_kmin,_i);
    }

    /**
     * Gets the left child of this node.
     * @return the left child.
     */
    public Node getLeft() {
      return _left;
    }

    /**
     * Gets the right child of this node.
     * @return the right child.
     */
    public Node getRight() {
      return _right;
    }

    /**
     * Determines whether this node is a leaf node (with no children).
     * @return true, if leaf node; false, otherwise.
     */
    public boolean isLeaf() {
      return _left==null;
    }

    private BoundingBox _bb; // bounding box for points in this node
    private int _kmin,_kmax; // indices i[kmin:kmax] are in this node
    private Node _left,_right; // child nodes; null, if a leaf node
  }

  /**
   * Constructs a bounding box tree for points with specified coordinates.
   * The (x,y,z) coordinates are packed into the specified array such
   * that (xyz[0],xyz[1],xyz[2]) are the (x,y,z) coordinates of the 
   * 1st point, (xyz[3],xyz[4],xyz[5]) are the (x,y,z) coordinates of 
   * the 2nd point, and so on.
   * @param minSize the minimum number of points in a child node.
   * @param xyz array of packed (x,y,z) coordinates.
   */
  public BoundingBoxTree(int minSize, float[] xyz) {
    Check.argument(minSize>0,"minSize>0");
    _n = xyz.length/3;
    _i = rampint(0,1,_n);
    _x = copy(_n,0,3,xyz);
    _y = copy(_n,1,3,xyz);
    _z = copy(_n,2,3,xyz);
    _root = new Node();
    _root._bb = new BoundingBox(_x,_y,_z);
    _root._kmin = 0;
    _root._kmax = _n-1;
    split(minSize,_root);
    _x = _y = _z = null;
  }

  /**
   * Constructs a bounding box tree for points with specified coordinates.
   * @param minSize the minimum number of points in a child node.
   * @param x array of x coordinates.
   * @param y array of y coordinates.
   * @param z array of z coordinates.
   */
  public BoundingBoxTree(int minSize, float[] x, float[] y, float[] z) {
    Check.argument(minSize>0,"minSize>0");
    Check.argument(x.length==y.length,"x.length==y.length");
    Check.argument(x.length==z.length,"x.length==z.length");
    _n = x.length;
    _i = rampint(0,1,_n);
    _x = copy(x);
    _y = copy(y);
    _z = copy(z);
    _root = new Node();
    _root._bb = new BoundingBox(_x,_y,_z);
    _root._kmin = 0;
    _root._kmax = _n-1;
    split(minSize,_root);
    _x = _y = _z = null;
  }

  /**
   * Gets the root node of this tree.
   * @return the root node.
   */
  public Node getRoot() {
    return _root;
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private int _n; // number of points
  private int[] _i; // array of indices
  private float[] _x; // array of x coordinates; temporary
  private float[] _y; // array of y coordinates; temporary
  private float[] _z; // array of z coordinates; temporary
  private Node _root; // the root node

  /**
   * Recursively splits a node that is not too small. A node is small and
   * not split when either of its child nodes would contain fewer than
   * the specified number of points.
   * @param minSize the minimum number of points in a child node.
   * @param node the node to split.
   */
  private void split(int minSize, Node node) {
    int kmin = node._kmin;
    int kmax = node._kmax;
    int n = 1+kmax-kmin;
    if (n/2<minSize)
      return;

    // Which coordinate (x, y, or z) has the greatest max-min spread?
    BoundingBox bb = node._bb;
    Point3 min = bb.getMin();
    Point3 max = bb.getMax();
    double xdif = max.x-min.x;
    double ydif = max.y-min.y;
    double zdif = max.z-min.z;
    double adif = max(xdif,ydif,zdif);
    float[] a;
    if (adif==xdif) {
      a = _x;
    } else if (adif==ydif) {
      a = _y;
    } else {
      a = _z;
    }

    // Partially sort indices for that coordinate so that the index of the 
    // point with the median coordinate value is in sorted position. That 
    // is, indices of points with smaller coordinate values are to the left,
    // and indices of points with larger coordinate values are to the right.
    int[] i = new int[n];
    for (int k=kmin; k<=kmax; ++k)
      i[k-kmin] = _i[k];
    int kmid = kmin+n/2;
    quickPartialIndexSort(kmid-kmin,a,i);
    for (int k=kmin; k<=kmax; ++k)
      _i[k] = i[k-kmin];

    // Left and right child nodes.
    Node left = new Node();
    Node right = new Node();
    if (adif==xdif) {
      float spltx = _x[_i[kmid]];
      left._bb = new BoundingBox(min.x,min.y,min.z,
                                 spltx,max.y,max.z);
      right._bb = new BoundingBox(spltx,min.y,min.z,
                                  max.x,max.y,max.z);
    } else if (adif==ydif) {
      float splty = _y[_i[kmid]];
      left._bb = new BoundingBox(min.x,min.y,min.z,
                                 max.x,splty,max.z);
      right._bb = new BoundingBox(min.x,splty,min.z,
                                  max.x,max.y,max.z);
    } else {
      float spltz = _z[_i[kmid]];
      left._bb = new BoundingBox(min.x,min.y,min.z,
                                 max.x,max.y,spltz);
      right._bb = new BoundingBox(min.x,min.y,spltz,
                                  max.x,max.y,max.z);
    }
    left._kmin  = kmin;
    left._kmax  = kmid-1;
    right._kmin = kmid;
    right._kmax = kmax;
    node._left = left;
    node._right = right;

    // Recursively split children.
    split(minSize,left);
    split(minSize,right);
  }
}
