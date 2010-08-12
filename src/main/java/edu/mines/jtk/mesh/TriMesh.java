/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mesh;

import java.io.*;
import java.util.*;
import javax.swing.event.EventListenerList;

import edu.mines.jtk.util.Check;
import static edu.mines.jtk.util.MathPlus.*;

/**
 * A triangular mesh.
 * <p>
 * Each tri in the mesh references three nodes. Depending on the context,
 * these three nodes are labelled as 0, 1, and 2, or A, B, and C, in
 * counter-clockwise (CCW) order. Here is a picture:
 * <pre><code>
 *
 *          2(C)
 *          *
 *         / \
 *        /    \
 *  0(A)*-------* 1(B)
 *
 * </code></pre>
 * Each tri has up to three neighbors (nabors), corresponding to the three
 * edges of the tri. Tris on the convex hull of the mesh have one or more
 * null nabors. Each nabor is labelled by the node opposite its edge. For
 * example, the tri nabor 0 (or A) is opposite the node 0 (or A).
 * <p>
 * Nodes are constructed with float coordinates that are stored internally 
 * as perturbed doubles. This perturbation minimizes the likelihood that 
 * three or more nodes are exactly co-linear, or that four or more nodes 
 * lie exactly on the circumcircle of any tri in the mesh. Only the least 
 * significant bits of the double coordinates are altered, so that casting 
 * the perturbed doubles to floats always yields the float coordinates 
 * with which nodes are constructed.
 *
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.03.08, 2006.08.02
 */
public class TriMesh implements Serializable {
  private static final long serialVersionUID = 1L;

  /**
   * A node, which may be added or removed from the mesh.
   */
  public static class Node implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * An integer index associated with this node.
     * Intended for external use only; the mesh does not use it.
     */
    public int index;

    /**
     * A data object associated with this node.
     * Intended for external use only; the mesh does not use it.
     */
    public Object data;

    /**
     * Constructs a node with the specified coordinates.
     * (Does not add the node to the mesh.)
     * @param x the x coordinate.
     * @param y the y coordinate.
     */
    public Node(float x, float y) {
      _prev = null;
      _next = null;
      _mark = 0;
      _tri = null;
      _hash = System.identityHashCode(this);
      setPosition(x,y);
    }

    /**
     * Returns the x coordinate of this node.
     * @return the x coordinate.
     */
    public final float x() {
      return (float)_x;
    }

    /**
     * Returns the y coordinate of this node.
     * @return the y coordinate.
     */
    public final float y() {
      return (float)_y;
    }

    /**
     * Returns the x coordinate of this node as a perturbed double.
     * @return the x coordinate.
     */
    public final double xp() { 
      return _x; 
    }

    /**
     * Returns the y coordinate of this node as a perturbed double.
     * @return the y coordinate.
     */
    public final double yp() { 
      return _y; 
    }

    /**
     * Returns a tri that references this node.
     * @return the tri; null, if this node is not in the mesh.
     */
    public final Tri tri() {
      return _tri;
    }

    @Override
    public String toString() {
      return "("+x()+","+y()+")";
    }

    // The outer class handles serialization of all private fields.
    private transient double _x,_y;
    private transient Node _prev,_next;
    private transient int _mark;
    private transient Tri _tri;
    private transient int _hash;
    private transient Object[] _values;

    /**
     * Perturbs a float into a double with pseudo-random least-significant bits.
     * Perturbation helps prevent degeneracies in Delaunay triangularization.
     */
    private static double perturb(float x, float p) {
      final int m = 2147483647;
      int i = Float.floatToIntBits(p);
      int j = 0;
      for (int k=0; k<32; ++k,i>>=1,j<<=1) {
        j |= i&1;
      }
      double xp = (x!=0.0f)?x:0.1*FLT_MIN;
      xp *= 1.0+((double)j/(double)m)*0.1*FLT_EPSILON;
      assert (float)xp==x;
      return xp;
    }

    /**
     * Sets the position of a node that is not in the mesh.
     */
    private void setPosition(float x, float y) {
      assert _tri==null;
      _x = perturb(x,0.450599f*y);
      _y = perturb(y,0.298721f*x);
      //_x = perturb(x,y);
      //_y = perturb(y,x);
    }
  }

  /**
   * A type-safe iterator for nodes.
   */
  public interface NodeIterator {
    public boolean hasNext();
    public Node next();
  }

  /**
   * One triangle (tri) in the mesh.
   * Each tri references three nodes (A, B, and C), and up to three
   * tri neighbors (nabors) opposite those nodes. A null nabor denotes
   * an edge (opposite the corresponding node) on the mesh boundary.
   * The nodes A, B, and C are in CCW order.
   */
  public static class Tri implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * An integer index associated with this tri.
     * Intended for external use only; the mesh does not use it.
     */
    public int index;

    /**
     * A data object associated with this tri.
     * Intended for external use only; the mesh does not use it.
     */
    public Object data;

    /**
     * Returns the node A referenced by this tri.
     * @return the node A.
     */
    public final Node nodeA() {
      return _n0;
    }

    /**
     * Returns the node B referenced by this tri.
     * @return the node B.
     */
    public final Node nodeB() {
      return _n1;
    }

    /**
     * Returns the node C referenced by this tri.
     * @return the node C.
     */
    public final Node nodeC() {
      return _n2;
    }

    /**
     * Returns the tri nabor A (opposite node A) referenced by this tri.
     * @return the tri nabor A.
     */
    public final Tri triA() {
      return _t0;
    }

    /**
     * Returns the tri nabor B (opposite node B) referenced by this tri.
     * @return the tri nabor B.
     */
    public final Tri triB() {
      return _t1;
    }

    /**
     * Returns the tri nabor C (opposite node C) referenced by this tri.
     * @return the tri nabor C.
     */
    public final Tri triC() {
      return _t2;
    }

    /**
     * Returns the node referenced by this tri that is nearest to
     * the point with specified coordinates.
     * @param x the x coordinate.
     * @param y the y coordinate.
     * @return the node nearest to the point (x,y).
     */
    public final Node nodeNearest(float x, float y) {
      double d0 = distanceSquared(_n0,x,y);
      double d1 = distanceSquared(_n1,x,y);
      double d2 = distanceSquared(_n2,x,y);
      double dmin = d0;
      Node nmin = _n0;
      if (d1<dmin) {
        dmin = d1;
        nmin = _n1;
      }
      if (d2<dmin) {
        nmin = _n2;
      }
      return nmin;
    }

    /**
     * Gets the tri nabor opposite the specified node.
     */
    public final Tri triNabor(Node node) {
      if (node==_n0) return _t0;
      if (node==_n1) return _t1;
      if (node==_n2) return _t2;
      Check.argument(false,"node is referenced by tri");
      return null;
    }

    /**
     * Gets the node in the specified tri nabor that is opposite this tri.
     */
    public final Node nodeNabor(Tri triNabor) {
      if (triNabor._t0==this) return triNabor._n0;
      if (triNabor._t1==this) return triNabor._n1;
      if (triNabor._t2==this) return triNabor._n2;
      Check.argument(false,"triNabor is a nabor of tri");
      return null;
    }

    /**
     * Computes the circumcenter of this tri.
     * @param c array of circumcenter coordinates {xc,yc}.
     * @return radius-squared of circumcircle.
     */
    public double centerCircle(double[] c) {
      if (hasCenter()) {
        c[0] = _xc;
        c[1] = _yc;
      } else {
        double x0 = _n0._x;
        double y0 = _n0._y;
        double x1 = _n1._x;
        double y1 = _n1._y;
        double x2 = _n2._x;
        double y2 = _n2._y;
        Geometry.centerCircle(x0,y0,x1,y1,x2,y2,c);
        setCenter(c[0],c[1]);
      }
      double dx = _xc-_n2._x;
      double dy = _yc-_n2._y;
      return dx*dx+dy*dy;
    }

    /**
     * Returns the circumcenter of this tri.
     * @return array of circumcenter coordinates {xc,yc}.
     */
    public double[] centerCircle() {
      double[] c = new double[2];
      centerCircle(c);
      return c;
    }

    /**
     * Returns the quality of this tri.
     * Quality is a number between 0 and 1, inclusive.
     * Quality equals 1 for equilateral tris.
     * Quality equals 0 for degenerate tris with co-linear nodes.
     * @return triangle quality.
     */
    public double quality() {
      if (_quality<0.0)
        _quality = quality(_n0,_n1,_n2);
      return _quality;
    }

    /**
     * Determines whether this tri references the specified node.
     * @param node the node.
     * @return true, if this tri references the node; false, otherwise.
     */
    public boolean references(Node node) {
      return node==_n0 || node==_n1 || node==_n2;
    }

    /**
     * Determines whether this tri references the specified nodes.
     * @param na a node.
     * @param nb a node.
     * @return true, if this tri references the nodes; false, otherwise.
     */
    public boolean references(Node na, Node nb) {
      if (na==_n0) {
        return nb==_n1 || nb==_n2;
      } else if (na==_n1) {
        return nb==_n0 || nb==_n2;
      } else if (na==_n2) {
        return nb==_n0 || nb==_n1;
      } else {
        return false;
      }
    }

    /**
     * Determines whether this tri references the specified nodes.
     * @param na a node.
     * @param nb a node.
     * @param nc a node.
     * @return true, if this tri references the nodes; false, otherwise.
     */
    public boolean references(Node na, Node nb, Node nc) {
      if (na==_n0) {
        if (nb==_n1) {
          return nc==_n2;
        } else if (nb==_n2) {
          return nc==_n1;
        } else {
          return false;
        }
      } else if (na==_n1) {
        if (nb==_n0) {
          return nc==_n2;
        } else if (nb==_n2) {
          return nc==_n0;
        } else {
          return false;
        }
      } else if (na==_n2) {
        if (nb==_n0) {
          return nc==_n1;
        } else if (nb==_n1) {
          return nc==_n0;
        } else {
          return false;
        }
      } else {
        return false;
      }
    }

    /**
     * Inner bit. If set, this tri is inner (and outer bit is clear).
     * If both inner and outer bits are clear, then status is unknown.
     */
    private static final int INNER_BIT = 1;

    /**
     * Outer bit. If set, this tri is outer (and inner bit is clear).
     * If both inner and outer bits are clear, then status is unknown.
     */
    private static final int OUTER_BIT = 2;

    /**
     * Center bit. If set, then the center for this tri is valid.
     */
    private static final int CENTER_BIT = 4;

    /**
     * Constant factor used to compute tri quality.
     */
    private static double QUALITY_FACTOR = 2.0/sqrt(3.0);

    /**
     * Nodes referenced by this tri.
     */
    private transient Node _n0,_n1,_n2;

    /**
     * Tri nabors; null, for tris on the hull.
     */
    private transient Tri _t0,_t1,_t2;

    /**
     * For recursively visiting tris.
     */
    private transient int _mark;

    /**
     * Useful bits.
     */
    private transient int _bits;

    /**
     * Quality of tri.
     */
    private transient double _quality = -1.0;
    
    /**
     * Coordinates of tri's center (typically, the circumcenter).
     */
    private transient double _xc,_yc;

    /**
     * Constructs a new tri.
     * The nodes n0, n1, and n2 must be in CCW order.
     */
    private Tri(Node n0, Node n1, Node n2) {
      init(n0,n1,n2);
    }

    /**
     * Initializes a tri, as in the constructor.
     */
    private void init(Node n0, Node n1, Node n2) {
      if (DEBUG) {
        double orient = Geometry.leftOfLine(
          n0._x,n0._y,
          n1._x,n1._y,
          n2._x,n2._y);
        Check.argument(orient>0.0,"orient>0.0");
      }
      _n0 = n0;
      _n1 = n1;
      _n2 = n2;
      _n0._tri = this;
      _n1._tri = this;
      _n2._tri = this;
      _t0 = null;
      _t1 = null;
      _t2 = null;
      _mark = 0;
      _bits = 0;
      _quality = -1.0;
    }

    private void setInner() {
      _bits |= INNER_BIT;
    }

    private void clearInner() {
      _bits &= ~INNER_BIT;
    }

    private boolean isInner() {
      return (_bits&INNER_BIT)!=0;
    }

    private void setOuter() {
      _bits |= OUTER_BIT;
    }

    private void clearOuter() {
      _bits &= ~OUTER_BIT;
    }

    private boolean isOuter() {
      return (_bits&OUTER_BIT)!=0;
    }

    private void setCenter(double xc, double yc) {
      _xc = xc;
      _yc = yc;
      _bits |= CENTER_BIT;
    }

    private boolean hasCenter() {
      return (_bits&CENTER_BIT)!=0;
    }

    /**
     * Determines if this tri intersects the specified line.
     */
    /*
    private boolean intersectsLine(double a, double b, double c)
    {
      int nn = 0;
      int np = 0;
      double s0 = a*_n0._x+b*_n0._y+c;
      if (s0<0.0) ++nn;
      if (s0>0.0) ++np;
      double s1 = a*_n1._x+b*_n1._y+c;
      if (s1<0.0) ++nn;
      if (s1>0.0) ++np;
      double s2 = a*_n2._x+b*_n2._y+c;
      if (s2<0.0) ++nn;
      if (s2>0.0) ++np;
      return nn<3 && np<3;
    }
    */

    /**
     * Returns the quality of a tri implied by the specified nodes.
     * The nodes need not be in any standard order.
     */
    private static double quality(Node na, Node nb, Node nc) {
      double xa = na._x;
      double ya = na._y;
      double xb = nb._x;
      double yb = nb._y;
      double xc = nc._x;
      double yc = nc._y;
      double xab = xa-xb;
      double yab = ya-yb;
      double xac = xa-xc;
      double yac = ya-yc;
      double xbc = xb-xc;
      double ybc = yb-yc;
      double det = xac*ybc-yac*xbc;
      double dab = xab*xab+yab*yab;
      double dac = xac*xac+yac*yac;
      double dbc = xbc*xbc+ybc*ybc;
      double dmx = dab;
      if (dac>dmx) dmx = dac;
      if (dbc>dmx) dmx = dbc;
      double quality = QUALITY_FACTOR*det/dmx;
      //Debug.trace("quality="+quality+" det="+det+" dmx="+dmx);
      if (quality<0.0) quality = -quality;
      if (quality>1.0) quality = 1.0;
      return (float)quality;
    }
  }

  /**
   * A type-safe iterator for tris.
   */
  public interface TriIterator {
    public boolean hasNext();
    public Tri next();
  }

  /**
   * A directed edge.
   * <p>
   * An edge is specified by two nodes A and B. The order of these nodes 
   * is significant. An edge is directed from A to B.
   * <p>
   * Every edge has a mate. An edge and its mate reference the same two
   * nodes, but in the opposite order, so they have opposite directions.
   * Therefore, an edge does not equal its mate.
   * <p>
   * When constructing an edge, a tri that references the two nodes A and
   * B may be specified. If non-null, this tri may be used to quickly
   * determine the two tris left and right of the edge that references its 
   * nodes. For an edge on the hull of the mesh, either the left or right 
   * tri is null.
   */
  public static class Edge {

    /**
     * Constructs a directed edge that references the specified nodes.
     * @param a a node of the edge.
     * @param b a node of the edge.
     */
    public Edge(Node a, Node b) {
      this(a,b,null);
    }

    /**
     * Constructs a directed edge that references the specified nodes.
     * Optionally, a tri may be specified. If non-null, that tri must
     * reference the specified nodes.
     * @param a a node of the edge.
     * @param b a node of the edge.
     * @param abc a tri that references nodes A and B; null, if none.
     */
    public Edge(Node a, Node b, Tri abc) {
      Node c = (abc!=null)?otherNode(abc,a,b):null;
      Check.argument(abc==null || c!=null,"tri references nodes");
      _a = a;
      _b = b;
      if (c!=null) {
        if (nodesInOrder(abc,a,b,c)) {
          _triLeft = abc;
          _nodeLeft = c;
          _triRight = abc.triNabor(c);
          _nodeRight = (_triRight!=null)?abc.nodeNabor(_triRight):null;
        } else {
          _triRight = abc;
          _nodeRight = c;
          _triLeft = abc.triNabor(c);
          _nodeLeft = (_triLeft!=null)?abc.nodeNabor(_triLeft):null;
        }
      }
    }

    /**
     * Returns the node A in the edge.
     * @return the node A.
     */
    public final Node nodeA() {
      return _a;
    }

    /**
     * Returns the node B in the edge.
     * @return the node B.
     */
    public final Node nodeB() {
      return _b;
    }

    /**
     * Returns the tri left of this face.
     * @return the tri left of this face; null, if none.
     */
    public Tri triLeft() {
      return _triLeft;
    }

    /**
     * Returns the tri right of this face.
     * @return the tri right of this face; null, if none.
     */
    public Tri triRight() {
      return _triRight;
    }

    /**
     * Returns the other node (not in this face) in the tri left of this face.
     * @return the other node in the tri left of this face; null, if none.
     */
    public Node nodeLeft() {
      return _nodeLeft;
    }

    /**
     * Returns the other node (not in this face) in the tri right of this face.
     * @return the other node in the tri right of this face; null, if none.
     */
    public Node nodeRight() {
      return _nodeRight;
    }

    /**
     * Returns the mate of this edge. If the tri ABC for this edge is 
     * null, then the mate cannot be determined.
     * @return the mate; null, if the mate cannot be determined.
     */
    public final Edge mate() {
      return new Edge(_b,_a,_triRight,_nodeRight,_triLeft,_nodeLeft);
    }

    /**
     * Determines whether this edge is visible from the specified point.
     * This edge is visible if the point lies strictly right of the directed
     * line AB of this edge.
     * @param x the x coordinate of the point.
     * @param y the y coordinate of the point.
     * @return true, if visible; false, otherwise.
     */
    public boolean isVisibleFromPoint(double x, double y) {
      return Geometry.leftOfLine(_a._x,_a._y,_b._x,_b._y,x,y)<0.0;
    }

    @Override
    public boolean equals(Object object) {
      if (object==this)
        return true;
      if (object!=null && object.getClass()==this.getClass()) {
        Edge edge = (Edge)object;
        return _a==edge._a && _b==edge._b;
      }
      return false;
    }

    @Override
    public int hashCode() {
      return _a._hash^_b._hash;
    }

    private Node _a,_b;
    private Tri _triLeft,_triRight;
    private Node _nodeLeft,_nodeRight;

    private Edge(
      Node a, Node b,
      Tri triLeft, Node nodeLeft,
      Tri triRight, Node nodeRight)
    {
      _a = a;
      _b = b;
      _triLeft = triLeft;
      _nodeLeft = nodeLeft;
      _triRight = triRight;
      _nodeRight = nodeRight;
    }

    private Edge(Tri triLeft, Node nodeLeft) {
      initLeft(triLeft,nodeLeft);
      _triLeft = triLeft;
      _nodeLeft = nodeLeft;
      _triRight = triLeft.triNabor(nodeLeft);
      _nodeRight = (_triRight!=null)?_triLeft.nodeNabor(_triRight):null;
    }

    private Edge(Tri triLeft, Node nodeLeft, Tri triRight, Node nodeRight) {
      if (triLeft!=null) {
        initLeft(triLeft,nodeLeft);
      } else if (triRight!=null) {
        initRight(triRight,nodeRight);
      } else {
        assert false:"either triLeft or triRight is not null";
      }
      _triLeft = triLeft;
      _triRight = triRight;
      _nodeLeft = nodeLeft;
      _nodeRight = nodeRight;
    }

    private void initLeft(Tri triLeft, Node nodeLeft) {
      if (nodeLeft==triLeft._n0) {
        _a = triLeft._n1;
        _b = triLeft._n2;
      } else if (nodeLeft==triLeft._n1) {
        _a = triLeft._n2;
        _b = triLeft._n0;
      } else if (nodeLeft==triLeft._n2) {
        _a = triLeft._n0;
        _b = triLeft._n1;
      } else {
        assert false:"nodeLeft referenced by triLeft";
      }
    }

    private void initRight(Tri triRight, Node nodeRight) {
      if (nodeRight==triRight._n0) {
        _a = triRight._n2;
        _b = triRight._n1;
      } else if (nodeRight==triRight._n1) {
        _a = triRight._n0;
        _b = triRight._n2;
      } else if (nodeRight==triRight._n2) {
        _a = triRight._n1;
        _b = triRight._n0;
      } else {
        assert false:"nodeRight referenced by triRight";
      }
    }

    /*
    private boolean isVisibleFromNode(Node node) {
      return isVisibleFromPoint(node._x,node._y);
    }
    */
  }

  /**
   * Type-safe iterator for edges.
   */
  public interface EdgeIterator {
    public boolean hasNext();
    public Edge next();
  }

  /**
   * A dynamically growing list of nodes.
   */
  public static class NodeList {

    /**
     * Appends the specified node to this list.
     * @param node the node to append.
     */
    public final void add(Node node) {
      if (_n==_a.length) {
        Node[] t = new Node[_a.length*2];
        System.arraycopy(_a,0,t,0,_n);
        _a = t;
      }
      _a[_n++] = node;
    }

    /**
     * Removes the node with specified index from this list.
     * @param index the index of the node to remove.
     * @return the node removed.
     */
    public final Node remove(int index) {
      Node node = _a[index];
      --_n;
      if (_n>index)
        System.arraycopy(_a,index+1,_a,index,_n-index);
      return node;
    }

    /**
     * Trims this list so that its array length equals the number of nodes.
     * @return the array of nodes in this list, after trimming.
     */
    public final Node[] trim() {
      if (_n<_a.length) {
        Node[] t = new Node[_n];
        System.arraycopy(_a,0,t,0,_n);
        _a = t;
      }
      return _a;
    }

    /**
     * Removes all nodes from this list.
     */
    public final void clear() {
      _n = 0;
    }

    /**
     * Returns the number of nodes in this list.
     * @return the number of nodes.
     */
    public final int nnode() {
      return _n;
    }

    /**
     * Returns (by reference) the array of nodes in this list.
     * @return the array of nodes.
     */
    public final Node[] nodes() {
      return _a;
    }
    private int _n = 0;
    private Node[] _a = new Node[64];
  }

  /**
   * A dynamically growing list of tris.
   */
  public static class TriList {

    /**
     * Appends the specified tri to this list.
     * @param tri the tri to append.
     */
    public final void add(Tri tri) {
      if (_n==_a.length) {
        Tri[] t = new Tri[_a.length*2];
        System.arraycopy(_a,0,t,0,_n);
        _a = t;
      }
      _a[_n++] = tri;
    }

    /**
     * Removes the tri with specified index from this list.
     * @param index the index of the tri to remove.
     * @return the tri removed.
     */
    public final Tri remove(int index) {
      Tri tri = _a[index];
      --_n;
      if (_n>index)
        System.arraycopy(_a,index+1,_a,index,_n-index);
      return tri;
    }

    /**
     * Trims this list so that its array length equals the number of tris.
     * @return the array of tris in this list, after trimming.
     */
    public final Tri[] trim() {
      if (_n<_a.length) {
        Tri[] t = new Tri[_n];
        System.arraycopy(_a,0,t,0,_n);
        _a = t;
      }
      return _a;
    }

    /**
     * Removes all tris from this list.
     */
    public final void clear() {
      _n = 0;
    }

    /**
     * Returns the number of tris in this list.
     * @return the number of tris.
     */
    public final int ntri() {
      return _n;
    }

    /**
     * Returns (by reference) the array of tris in this list.
     * @return the array of tris.
     */
    public final Tri[] tris() {
      return _a;
    }
    private int _n = 0;
    private Tri[] _a = new Tri[64];
  }

  /**
   * A dynamically growing list of edges.
   */
  public static class EdgeList {

    /**
     * Appends the specified edge to this list.
     * @param edge the edge to append.
     */
    public final void add(Edge edge) {
      if (_n==_a.length) {
        Edge[] t = new Edge[_a.length*2];
        System.arraycopy(_a,0,t,0,_n);
        _a = t;
      }
      _a[_n++] = edge;
    }

    /**
     * Removes the edge with specified index from this list.
     * @param index the index of the edge to remove.
     * @return the edge removed.
     */
    public final Edge remove(int index) {
      Edge edge = _a[index];
      --_n;
      if (_n>index)
        System.arraycopy(_a,index+1,_a,index,_n-index);
      return edge;
    }

    /**
     * Trims this list so that its array length equals the number of edges.
     * @return the array of edges in this list, after trimming.
     */
    public final Edge[] trim() {
      if (_n<_a.length) {
        Edge[] t = new Edge[_n];
        System.arraycopy(_a,0,t,0,_n);
        _a = t;
      }
      return _a;
    }

    /**
     * Removes all edges from this list.
     */
    public final void clear() {
      _n = 0;
    }

    /**
     * Returns the number of edges in this list.
     * @return the number of edges.
     */
    public final int nedge() {
      return _n;
    }

    /**
     * Returns (by reference) the array of edges in this list.
     * @return the array of edges.
     */
    public final Edge[] edges() {
      return _a;
    }
    private int _n = 0;
    private Edge[] _a = new Edge[64];
  }

  /**
   * A dynamically growing list of nodes and steps.
   * Such a list may be, for example, the result of a search for node 
   * nabors within a specified maximum number of steps of a specified node.
   */
  public static class NodeStepList {

    /**
     * Appends the specified node and step to this list.
     * @param node the node to append.
     */
    public final void add(Node node, int step) {
      if (_n==_a.length) {
        Node[] s = new Node[_a.length*2];
        int[] t = new int[_a.length*2];
        System.arraycopy(_a,0,s,0,_n);
        System.arraycopy(_b,0,t,0,_n);
        _a = s;
        _b = t;
      }
      _a[_n] = node;
      _b[_n] = step;
      ++_n;
    }

    /**
     * Trims this list so that its array length equals the number of nodes.
     */
    public final void trim() {
      if (_n<_a.length) {
        Node[] s = new Node[_n];
        int[] t = new int[_n];
        System.arraycopy(_a,0,s,0,_n);
        System.arraycopy(_b,0,t,0,_n);
        _a = s;
        _b = t;
      }
    }

    /**
     * Removes all nodes (and steps) from this list.
     */
    public final void clear() {
      _n = 0;
    }

    /**
     * Returns the number of nodes (and steps) in this list.
     * @return the number of nodes.
     */
    public final int nnode() {
      return _n;
    }

    /**
     * Returns (by reference) the array of nodes in this list.
     * @return the array of nodes.
     */
    public final Node[] nodes() {
      return _a;
    }

    /**
     * Returns (by reference) the array of steps in this list.
     * @return the array of steps.
     */
    public final int[] steps() {
      return _b;
    }
    private int _n = 0;
    private Node[] _a = new Node[64];
    private int[] _b = new int[64];
  }

  /**
   * The location of a point relative to the mesh. A point is either on a
   * node, on an edge, inside a triangle, or outside the mesh.
   */
  public static class PointLocation {

    /**
     * Determines whether this location is on a node.
     * @return true, if on a node; false, otherwise.
     */
    public boolean isOnNode() {
      return _node!=null;
    }

    /**
     * Determines whether this location is on an edge.
     * @return true, if on an edge; false, otherwise.
     */
    public boolean isOnEdge() {
      return _edge!=null;
    }

    /**
     * Determines whether this location is inside a tri.
     * @return true, if inside a tri; false, otherwise.
     */
    public boolean isInside() {
      return _inside;
    }

    /**
     * Determines whether this location is outside the mesh.
     * @return true, if outside the mesh; false, otherwise.
     */
    public boolean isOutside() {
      return !_inside;
    }

    /**
     * Returns the node.
     * @return the node; null, if not on a node.
     */
    public Node node() {
      return _node;
    }

    /**
     * Returns the edge.
     * @return the edge; null, if not on an edge.
     */
    public Edge edge() {
      return _edge;
    }

    /**
     * Returns the tri corresponding to the point location.
     * If the point location is on a node or edge, the tri is one
     * that references that node or edge. If the point location is
     * outside the mesh, the tri is one that is visible from the point.
     * @return the tri.
     */
    public Tri tri() {
      return _tri;
    }

    private Node _node; // if on node, the node; otherwise, null
    private Edge _edge; // if on edge, the edge; otherwise, null
    private Tri _tri; // if inside, the tri; otherwise, a visible tri
    private boolean _inside; // if inside, true; otherwise, false

    private PointLocation(Tri tri) {
      _tri = tri;
      _inside = true;
    }
    private PointLocation(Tri tri, boolean inside) {
      _tri = tri;
      _inside = inside;
    }
    private PointLocation(Node node) {
      _tri = node._tri;
      _node = node;
      _inside = true;
    }
    private PointLocation(Edge edge) {
      _tri = edge._triLeft;
      _edge = edge;
      _inside = true;
    }
  }

  /**
   * Implemented by maps that associate property values with mesh nodes.
   * This interface is implemented by all node property maps created
   * by this mesh. However, other implementations are possible.
   */
  public interface NodePropertyMap extends Serializable {

    /**
     * Gets the value of the property associated with the specified node.
     * @param node the node.
     * @return the property value.
     */
    public Object get(Node node);

    /**
     * Puts the value of the property associated with the specified node.
     * @param node the node.
     * @param value the property value.
     */
    public void put(Node node, Object value);
  }

  /**
   * Gets the node property map with the specified name.
   * If this mesh does not have a node property map with the specified 
   * name, this method creates one.
   * @param name the property map name.
   * @return the node property map.
   */
  public synchronized NodePropertyMap getNodePropertyMap(String name) {
    NodePropertyMap map = _nodePropertyMaps.get(name);
    if (map==null) {
      if (_nnodeValues==_lnodeValues) {
        if (_lnodeValues==0) {
          _lnodeValues = 4;
        } else {
          _lnodeValues *= 2;
        }
        NodeIterator ni = getNodes();
        while (ni.hasNext()) {
          Node node = ni.next();
          Object[] valuesOld = node._values;
          Object[] valuesNew = new Object[_lnodeValues];
          for (int i=0; i<_nnodeValues; ++i)
            valuesNew[i] = valuesOld[i];
          node._values = valuesNew;
        }
      }
      int index = _nnodeValues++;
      map = new NodePropertyMapInternal(index);
      _nodePropertyMaps.put(name,map);
    }
    return map;
  }

  /**
   * Determines whether this mesh has a node property map with specified name.
   * @param name the property map name.
   * @return true, if this mesh has the map; false, otherwise.
   */
  public synchronized boolean hasNodePropertyMap(String name) {
    return _nodePropertyMaps.containsKey(name);
  }

  /**
   * Returns the names of the node property maps.
   * @return an array containing the names of the property maps
   * @see #getNodePropertyMap(String)
   */
  public synchronized String[] getNodePropertyMapNames() {
    Set<String> nameSet = _nodePropertyMaps.keySet();
    String[] names = new String[nameSet.size()];
    return nameSet.toArray(names);
  }

  /**
   * Implemented to monitor the addition and removal of nodes in the mesh.
   * When node listeners are called, the mesh is valid. Therefore, classes
   * can safely call any tri mesh method in their implementations of this
   * interface.
   * <p>
   * Classes that maintain references to nodes in a tri mesh should 
   * implement this interface. Those references are almost certainly
   * invalid (and may prevent garbage collection) after nodes are 
   * removed from the mesh.
   */
  public interface NodeListener extends EventListener {

    /**
     * Called when the specified node will be added to the mesh.
     * @param mesh this mesh.
     * @param node the node that will be added.
     */
    public void nodeWillBeAdded(TriMesh mesh, Node node);

    /**
     * Called after the specified node has been added to the mesh.
     * @param mesh this mesh.
     * @param node the node added.
     */
    public void nodeAdded(TriMesh mesh, Node node);

    /**
     * Called when the specified node will be removed from the mesh.
     * @param mesh this mesh.
     * @param node the node that will be removed.
     */
    public void nodeWillBeRemoved(TriMesh mesh, Node node);

    /**
     * Called after the specified node has been removed from the mesh.
     * @param mesh this mesh.
     * @param node the node removed.
     */
    public void nodeRemoved(TriMesh mesh, Node node);
  }

  /**
   * Implemented to monitor the addition and removal of tris in the mesh.
   * When tri listeners are called, the mesh may be in an <em>invalid</em>
   * state. Specifically, the tri nabors of a tri added or removed may be 
   * invalid. However, all other state of that tri is valid.
   * <p>
   * Classes that maintain references to tris in a tri mesh should 
   * implement this interface. Those references are almost certainly
   * invalid (and may prevent garbage collection) after tris are
   * removed from the mesh. This is especially important because 
   * tris removed may be reused, and given entirely different state,
   * as tris are added to the mesh.
   * <p>
   * Tri listeners may be costly. Typically, for each node added to 
   * a tri mesh, roughly 4 tris are removed and 6 tris are added. 
   * Therefore, tri listeners should be added just prior to making a 
   * small number of incremental changes to the mesh, and removed after 
   * such changes are completed.
   */
  public interface TriListener extends EventListener {

    /**
     * Called after the specified tri has been added to the mesh.
     * When this method is called, the tri nabors of the specified tri are 
     * not valid. All other state of the specified tri, such as its nodes, 
     * is valid during this method call.
     * @param mesh this mesh.
     * @param tri the tri added.
     */
    public void triAdded(TriMesh mesh, Tri tri);

    /**
     * Called after the specified tri has been removed from the mesh.
     * When this method is called, the tri nabors of the specified tri are 
     * not valid. All other state of the specified tri, such as its nodes, 
     * is valid during this method call.
     * @param mesh this mesh.
     * @param tri the tri removed.
     */
    public void triRemoved(TriMesh mesh, Tri tri);
  }

  /**
   * Adds the specified node listener.
   * @param nl the node listener.
   */
  public synchronized void addNodeListener(NodeListener nl) {
    _listeners.add(NodeListener.class,nl);
    ++_nnodeListeners;
  }

  /**
   * Removes the specified node listener.
   * @param nl the node listener.
   */
  public synchronized void removeNodeListener(NodeListener nl) {
    _listeners.remove(NodeListener.class,nl);
    --_nnodeListeners;
  }

  /**
   * Adds the specified tri listener.
   * @param tl the tri listener.
   */
  public synchronized void addTriListener(TriListener tl) {
    _listeners.add(TriListener.class,tl);
    ++_ntriListeners;
  }

  /**
   * Removes the specified tri listener.
   * @param tl the tri listener.
   */
  public synchronized void removeTriListener(TriListener tl) {
    _listeners.remove(TriListener.class,tl);
    --_ntriListeners;
  }

  /**
   * Constructs an empty mesh.
   */
  public TriMesh() {
    init();
  }

  /**
   * Returns the number of nodes in the mesh.
   * @return the number of nodes.
   */
  public int countNodes() {
    return _nnode;
  }

  /**
   * Returns the number of tris in the mesh.
   * @return the number of tris.
   */
  public int countTris() {
    return _ntri;
  }

  /**
   * Gets the version number for the mesh. The version number is incremented
   * whenever the mesh changes. Therefore, this number can be used to lazily
   * determine if the mesh has changed. Comparing version numbers may in 
   * some applications serve as a cheap alternative to adding node and tri 
   * listeners to the mesh.
   * @return the version number.
   */
  public long getVersion() {
    return _version;
  }

  /**
   * Adds a node to the mesh, if the mesh does not already contain
   * a node with the same (x,y) coordinates.
   * @param node the node to add.
   * @return true, if the node was added; false, otherwise.
   */
  public synchronized boolean addNode(Node node) {

    // Where is the point?
    PointLocation pl = locatePoint(node._x,node._y);

    // Cannot have two nodes with the same coordinates.
    if (pl.isOnNode())
      return false;

    // Tell listeners that node will be added.
    fireNodeWillBeAdded(node);

    // The new node becomes the root node.
    if (_nroot==null) {
      _nroot = node;
      _nroot._prev = _nroot._next = _nroot;
    } else {
      node._next = _nroot;
      node._prev = _nroot._prev;
      _nroot._prev._next = node;
      _nroot._prev = node;
      _nroot = node;
    }
    ++_nnode;

    // Update node property values so they are consistent with this mesh.
    updatePropertyValues(node);

    // Maintain adequate sampling of O(N^(1/3)) nodes for fast point location.
    // The scale factor 0.45 was used by Shewchuk, 1997.
    double factor = 0.45*_sampledNodes.size();
    if (factor*factor*factor<_nnode) {
      _sampledNodes.add(node);
      //trace("addNode: sampling "+_sampledNodes.size()+" nodes");
    }

    // If we do not yet have a tri, perhaps we have enough nodes to make one.
    if (pl.isOutside() && _nnode<=3) {
      if (_nnode==3)
        createFirstTri();

    // Otherwise, if we have at least one tri, ...
    } else {

      // Get the set of Delaunay edges that bound the star-shaped 
      // polygon containing all tris that are not Delaunay with 
      // respect to the new node.
      clearTriMarks();
      _edgeSet.clear();
      if (pl.isInside()) {
        getDelaunayEdgesInside(node,pl.tri());
      } else {
        getDelaunayEdgesOutside(node,pl.tri());
      }

      // With each Delaunay edge in the set, create a new tri with 
      // the new node. Use a node set to link tris when a tri and 
      // its nabor have been created.
      _nodeSet.clear();
      for (boolean more=_edgeSet.first(); more; more=_edgeSet.next()) {
        Node a = _edgeSet.a;
        Node b = _edgeSet.b;
        Node c = _edgeSet.c;
        Tri abc = _edgeSet.abc;
        Tri nba = makeTri(node,b,a);
        linkTris(nba,node,abc,c);
        if (!_nodeSet.add(a,b,nba))
          linkTris(_nodeSet.nba,_nodeSet.b,nba,b);
        if (!_nodeSet.add(b,a,nba))
          linkTris(_nodeSet.nba,_nodeSet.b,nba,a);
      }
    }

    if (DEBUG)
      validate();

    // Tell listeners that node has been added.
    fireNodeAdded(node);

    return true;
  }

  /**
   * Removes a node from the mesh, if the node is in the mesh.
   * @param node the node to remove.
   * @return true, if the node was (in the mesh and) removed; false, otherwise.
   */
  public synchronized boolean removeNode(Node node) {

    // Tri that references this node.
    Tri tri = node._tri;

    // Nodes that reference no tri are not in the mesh.
    if (tri==null)
      return false;

    // Tell listeners that node will be removed.
    fireNodeWillBeRemoved(node);

    // Unlink the node from the mesh, leaving only references from tris.
    _nroot = node._next;
    _nmin = node._next;
    if (_nroot==node) {
      _nroot = null;
      _nmin = null;
    }
    node._prev._next = node._next;
    node._next._prev = node._prev;
    node._prev = null;
    node._next = null;
    node._tri = null;
    _sampledNodes.remove(node);
    --_nnode;

    // If fewer than three nodes, the mesh contains no tris.
    if (_nnode<3) {
      if (_nnode==2) {
        Node n0 = _nroot;
        Node n1 = n0._next;
        n0._tri = null;
        n1._tri = null;
        killTri(_troot);
        _troot = null;
      }
      return true;
    }

    // Get the Delaunay edges opposite the node to be removed,
    // and collect the nodes in those edges.
    _edgeSet.clear();
    _nodeList.clear();
    clearTriMarks();
    clearNodeMarks();
    getDelaunayEdgesOpposite(node,tri);
    int nnode = _nodeList.nnode();
    Node[] nodes = _nodeList.nodes();

    // Use a simple gift-wrapping algorithm to fill in the hole left by the
    // removed node. While edges remain in the edge set, remove one and ...
    for (boolean more=_edgeSet.remove(); more; more=_edgeSet.remove()) {

      // Get the edge parameters.
      Node a = _edgeSet.a;
      Node b = _edgeSet.b;
      Node c = _edgeSet.c;
      Tri abc = _edgeSet.abc;

      // Search for a node n that is right of the edge ab. If one or more
      // such nodes exist, choose the node n so that nba has an empty
      // circumcircle.
      Node n = null;
      for (int inode=0; inode<nnode; ++inode) {
        Node m = nodes[inode];
        if (m!=a && m!=b && !leftOfLine(a,b,m)) {
          if (n==null || inCircle(n,b,a,m))
            n = m;
        }
      }

      // If there exists a node n that is right of the edge ab, ...
      if (n!=null) {

        // Create a new tri nba.
        Tri nba = makeTri(n,b,a);

        // Link the tris nba and abc.
        linkTris(nba,n,abc,c);

        // Add the new edges of nba to the edge set. If, instead of
        // adding an edge, its mate is removed from the set, link the
        // corresponding tris.
        if (!_edgeSet.add(nba,a))
          linkTris(_edgeSet.abc,_edgeSet.c,nba,a);
        if (!_edgeSet.add(nba,b))
          linkTris(_edgeSet.abc,_edgeSet.c,nba,b);

      // Otherwise, the edge ab will now be on the convex hull of the mesh.
      } else {

        // Unlink tri abc and its nabor, which references the edge ab.
        linkTris(abc,c,null,null);
        a._tri = abc;
        b._tri = abc;
        _troot = abc;
      }
    }

    if (DEBUG)
      validate();

    // Tell listeners that node was removed.
    fireNodeRemoved(node);

    return true;
  }

  /**
   * Moves a node in the mesh to the specified (x,y) coordinates.
   * Roughly equivalent to (but potentially more efficient than)
   * first removing and then adding the node to the mesh at the 
   * specified coordinates. However, if the node is not in the mesh, 
   * then it will be moved, but not added to the mesh. Also, if the 
   * specified coordinates are already occupied by another node in 
   * the mesh, then the specified node is not moved.
   * @param node a node in the mesh.
   * @param x the x coordinate of the moved node.
   * @param y the y coordinate of the moved node.
   * @return true, if the node was moved; false, otherwise.
   */
  public synchronized boolean moveNode(Node node, float x, float y) {
    // TODO: optimize for small movements that require no retriangulation.
    if (x!=node.x() || y!=node.y()) {
      Node nodeNearest = findNodeNearest(x,y);
      if (node==nodeNearest || x!=nodeNearest.x() || y!=nodeNearest.y()) {
        boolean nodeInMesh = removeNode(node);
        node.setPosition(x,y);
        if (nodeInMesh) {
          boolean addedNode = addNode(node);
          assert addedNode;
        }
        return true;
      }
    }
    return false;
  }

  /**
   * Finds the node nearest to the point with specified coordinates.
   * @param x the x coordinate.
   * @param y the y coordinate.
   * @return the nearest node.
   */
  public synchronized Node findNodeNearest(float x, float y) {
    return findNodeNearest((double)x,(double)y);
  }

  /**
   * Finds an edge of a tri in the mesh that references the specified nodes.
   * The nodes may be specified in any order. However, if not null,
   * the returned edge always has a mate, and either the returned 
   * edge or its mate references the specified nodes in the specified 
   * order.
   * @param na a node.
   * @param nb a node.
   * @return an edge of a tri that references the specified nodes.
   *  If no such edge exists in this mesh, returns null.
   */
  public synchronized Edge findEdge(Node na, Node nb) {
    Tri tri = findTri(na,nb);
    if (tri!=null) {
      return edgeOfTri(tri,na,nb);
    } else {
      return null;
    }
  }

  /**
   * Returns a tri that references the specified node.
   * @param node the node.
   * @return a tri that references the specified node;
   *  or null, if the node is not in the mesh or the mesh
   *  has no tris.
   */
  public Tri findTri(Node node) {
    return node._tri;
  }

  /**
   * Returns a tri that references the specified nodes.
   * @param na a node.
   * @param nb a node.
   * @return a tri that references the specified nodes;
   *  or null, if a node is not in the mesh or the mesh
   *  has no tris.
   */
  public synchronized Tri findTri(Node na, Node nb) {
    Tri tri = findTri(na);
    if (tri!=null) {
      clearTriMarks();
      tri = findTri(tri,na,nb);
    }
    return tri;
  }

  /**
   * Returns a tri that references the specified nodes.
   * @param na a node.
   * @param nb a node.
   * @param nc a node.
   * @return a tri that references the specified nodes;
   *  or null, if a node is not in the mesh or the mesh
   *  has no tris.
   */
  public synchronized Tri findTri(Node na, Node nb, Node nc) {
    Tri tri = findTri(na,nb);
    if (tri!=null) {
      clearTriMarks();
      tri = findTri(tri,na,nb,nc);
    }
    return tri;
  }

  /**
   * Locates a point with specified coordinates.
   * @param x the x coordinate.
   * @param y the y coordinate.
   * @return the {@link PointLocation}.
   */
  public synchronized PointLocation locatePoint(float x, float y) {
    return locatePoint((double)x,(double)y);
  }

  /**
   * Gets an iterator for all nodes in the mesh.
   * @return the iterator.
   */
  public synchronized NodeIterator getNodes() {
    return new NodeIterator() {
      public boolean hasNext() {
        return _nnext!=null;
      }
      public Node next() {
        if (_nnext==null)
          throw new NoSuchElementException();
        Node node = _nnext;
        _nnext = node._next;
        if (_nnext==_nroot) _nnext = null;
        return node;
      }
      private Node _nroot = TriMesh.this._nroot;
      private Node _nnext = _nroot;
    };
  }

  /**
   * Gets an iterator for all tris in the mesh.
   * @return the iterator.
   */
  public synchronized TriIterator getTris() {
    return new TriIterator() {
      public final boolean hasNext() {
        return _i.hasNext();
      }
      public final Tri next() {
        return _i.next();
      }
      private Iterator<Tri> _i;
      {
        clearTriMarks();
        ArrayList<Tri> stack = new ArrayList<Tri>(128);
        ArrayList<Tri> list = new ArrayList<Tri>(_ntri);
        if (_troot!=null) {
          stack.add(_troot);
          mark(_troot);
        }
        for (int n=stack.size(); n>0; n=stack.size()) {
          Tri tri = stack.remove(n-1);
          list.add(tri);
          Tri t0 = tri._t0;
          if (t0!=null && !isMarked(t0)) {
            stack.add(t0);
            mark(t0);
          }
          Tri t1 = tri._t1;
          if (t1!=null && !isMarked(t1)) {
            stack.add(t1);
            mark(t1);
          }
          Tri t2 = tri._t2;
          if (t2!=null && !isMarked(t2)) {
            stack.add(t2);
            mark(t2);
          }
        }
        _i = list.iterator();
      }
    };
  }

  /**
   * Gets an iterator for all edges on the hull of the mesh.
   * @return the iterator.
   */
  public synchronized EdgeIterator getEdgesOnHull() {
    clearTriMarks();
    Edge edge = getEdgeOnHull(_troot);
    final HashSet<Edge> edges = new HashSet<Edge>(128);
    getEdgesOnHull(edge,edges);
    return new EdgeIterator() {
      private Iterator<Edge> i = edges.iterator();
      public final boolean hasNext() {
        return i.hasNext();
      }
      public final Edge next() {
        return i.next();
      }
    };
  }

  /**
   * Gets an array of node nabors of the specified node.
   * @param node the node for which to get nabors.
   * @return the array of nabors. 
   */
  public synchronized Node[] getNodeNabors(Node node) {
    NodeList nabors = new NodeList();
    getNodeNabors(node,nabors);
    return nabors.trim();
  }

  /** 
   * Appends the node nabors of the specified node to the specified list.
   * @param node the node for which to get nabors.
   * @param nabors the list to which nabors are appended.
   */
  public synchronized void getNodeNabors(Node node, NodeList nabors) {
    clearNodeMarks();
    clearTriMarks();
    getNodeNabors(node,node._tri,nabors);
  }

  /**
   * Finds all node nabors that are within the specified maximum
   * number of steps of the specified node. Also, determines the 
   * number of steps from the specified node to each nabor node.
   * @param node the node for which to get nabors.
   * @param stepMax the maximum number of steps; must not exceed 256.
   * @return a list of nodes with corresponding steps.
   */
  public synchronized NodeStepList getNodeNabors(Node node, int stepMax) {
    Check.argument(stepMax<=256,"stepMax <= 256");

    // Nodes are marked as they are found.
    clearNodeMarks();

    // Do not include the specified node.
    mark(node);

    // List of nabors.
    NodeStepList list = new NodeStepList();

    // For all steps, ...
    for (int step=1,nnabor1=0; step<=stepMax; ++step) {

      // If first step, ...
      if (step==1) {

        // Find immediate nabors of the specified node.
        getNodeNabors(node,step,list);

      // Else, for subsequent steps, ...
      } else {

        // Number of nabors at end of previous step.
        int nnabor2 = list.nnode();

        // Find nabors of nabors found in previous step.
        Node[] naborNodes = list.nodes();
        for (int inabor=nnabor1; inabor<nnabor2; ++inabor) {
          node = naborNodes[inabor];
          getNodeNabors(node,step,list);
        }

        // Index of node at which to begin search in next step.
        nnabor1 = nnabor2;
      }
    }
    return list;
  }

  /**
   * Gets an array of tri nabors of the specified node.
   * @param node the node for which to get nabors.
   * @return the array of nabors. 
   */
  public synchronized Tri[] getTriNabors(Node node) {
    TriList nabors = new TriList();
    getTriNabors(node,nabors);
    return nabors.trim();
  }

  /** 
   * Appends the tri nabors of the specified node to the specified list.
   * @param node the node for which to get nabors.
   * @param nabors the list to which nabors are appended.
   */
  public synchronized void getTriNabors(Node node, TriList nabors) {
    clearTriMarks();
    getTriNabors(node,node._tri,nabors);
  }

  /**
   * Gets an array of tri nabors of the specified edge.
   * @param edge the edge for which to get nabors.
   * @return the array of nabors. 
   */
  public synchronized Tri[] getTriNabors(Edge edge) {
    TriList nabors = new TriList();
    getTriNabors(edge,nabors);
    return nabors.trim();
  }

  /**
   * Appends the tri nabors of the specified edge to the specified list.
   * @param edge the edge for which to get nabors.
   * @param nabors the list to which nabors are appended.
   */
  public synchronized void getTriNabors(Edge edge, TriList nabors) {
    Tri triLeft = edge.triLeft();
    Tri triRight = edge.triRight();
    if (triLeft==null && triRight==null) {
      Node na = edge.nodeA();
      Node nb = edge.nodeB();
      edge = findEdge(na,nb);
      triLeft = edge.triLeft();
      triRight = edge.triRight();
    }
    if (triLeft!=null)
      nabors.add(triLeft);
    if (triRight!=null)
      nabors.add(triRight);
  }

  /**
   * Gets a new array containing the edge nabors of the specified node.
   * The edges are stored in CCW order, and all are directed towards the
   * specified node. (In other words, node B of each edge nabor is the 
   * specified node.) Their corresponding edge mates are not stored.
   * @param node the node for which to get nabors.
   * @return the array of edge nabors. 
   *  The array length equals the number of nabors.
   */
  public synchronized Edge[] getEdgeNabors(Node node) {
    EdgeList nabors = new EdgeList();
    getEdgeNabors(node,nabors);
    return nabors.trim();
  }

  /**
   * Stores the edge nabors of the specified node in the specified array.
   * The edges are stored in CCW order, and all are directed towards the
   * specified node. (In other words, node B of each edge nabor is the 
   * specified node.) Their corresponding edge mates are not stored.
   * <p>
   * This method is the most efficient way to get the nabors, because it
   * does not create a new array. However, it throws an exception if the 
   * specified array has insufficient length to store all of the nabors.
   * @param node the node for which to get nabors.
   * @param nabors the array in which to store the nabors.
   */
  public synchronized void getEdgeNabors(Node node, EdgeList nabors) {

    // Tri nodes.
    Tri tri = node.tri();
    Node ta = tri.nodeA();
    Node tb = tri.nodeB();
    Node tc = tri.nodeC();

    // Which edge of tri is directed toward the node? (One must be!)
    Edge edge = null;
    if (node==ta) {
      edge = new Edge(tc,ta,tri);
    } else if (node==tb) {
      edge = new Edge(ta,tb,tri);
    } else if (node==tc) {
      edge = new Edge(tb,tc,tri);
    } else {
      assert false:"tri references node";
    }
    
    // Loop over edges until first edge is reached.
    Edge firstEdge = edge;
    do {

      // Collect edges.
      nabors.add(edge);
    
      // Node at other end of edge. 
      node = edge.nodeA();

      // Get next edge in ring. If the right tri for the current edge is
      // null, then current edge is external. In this case, we search
      // backwards for the next edge, which is also external.
      tri = edge.triRight();
      if (tri==null) {
        tri = edge.triLeft();
        Node nodeBack = edge.nodeLeft();
        Tri triBack = tri.triNabor(node);
        while (triBack!=null) {
          node = nodeBack;
          nodeBack = tri.nodeNabor(triBack);
          tri = triBack;
          triBack = tri.triNabor(node);
        }
        edge = new Edge(null,null,tri,node);
      } else {
        edge = new Edge(tri,node);
      }
    } while (!edge.equals(firstEdge));
  }

  /**
   * Sets and enables the outer box for this mesh.
   * Tris with circumcircles entirely within the specified box
   * are <em>inner</em> tris. All other tris are outer tris.
   * An inner node or edge has one or more inner tri nabors;
   * an outer node or edge has no inner tri nabors.
   * <p>
   * The outer box is typically set to be slightly larger than the
   * bounding box of the convex hull of the mesh, so that outer tris 
   * lie near the convex hull. These outer tris tend to have poor
   * quality, and are often ignored in iterations over tris.
   * @param xmin minimum x coordinate of box.
   * @param ymin minimum y coordinate of box.
   * @param xmax maximum x coordinate of box.
   * @param ymax maximum y coordinate of box.
   */
  public synchronized void setOuterBox(
    float xmin, float ymin, float xmax, float ymax)
  {
    Check.argument(xmin<xmax,"outer box is valid");
    Check.argument(ymin<ymax,"outer box is valid");

    // If the outer box is changing, ...
    if (xmin!=_xminOuter ||
        xmax!=_xmaxOuter ||
        ymin!=_yminOuter ||
        ymax!=_ymaxOuter) {

      // Remember the outer box.
      _xminOuter = xmin;
      _yminOuter = ymin;
      _xmaxOuter = xmax;
      _ymaxOuter = ymax;

      // Clear both the inner and outer bits of all tris.
      // We will lazily mark the tri as inner or outer.
      TriIterator ti = getTris();
      while (ti.hasNext()) {
        Tri tri = ti.next();
        tri.clearInner();
        tri.clearOuter();
      }
    }

    ++_version;
    _outerEnabled = true;
  }

  /**
   * Enables outer box testing.
   * With outer box testing enabled, tris are either inner or outer.
   * By default, outer box testing is disabled.
   */
  public void enableOuterBox() {
    ++_version;
    _outerEnabled = true;
  }

  /**
   * Disables outer box testing, without altering the outer box.
   * With outer box testing disabled, all tris are inner.
   * By default, outer box testing is disabled.
   */
  public void disableOuterBox() {
    ++_version;
    _outerEnabled = false;
  }

  /**
   * Determines whether the specified node is an inner node.
   * @return true, if inner; false, otherwise.
   */
  public boolean isInner(Node node) {
    Tri tri = node.tri();
    if (tri==null || isInner(tri))
      return true;
    Tri[] tris = getTriNabors(node);
    int ntri = tris.length;
    for (int itri=0; itri<ntri; ++itri) {
      if (isInner(tris[itri]))
        return true;
    }
    return false;
  }

  /**
   * Determines whether the specified node is an outer node.
   * @return true, if outer; false, otherwise.
   */
  public boolean isOuter(Node node) {
    return !isInner(node);
  }

  /**
   * Determines whether the specified tri is an inner tri.
   * @return true, if inner; false, otherwise.
   */
  public boolean isInner(Tri tri) {
    if (!_outerEnabled)
      return true;
    if (!tri.isInner() && !tri.isOuter())
      markTriInnerOrOuter(tri);
    return tri.isInner();
  }

  /**
   * Determines whether the specified tri is an outer tri.
   * @return true, if outer; false, otherwise.
   */
  public boolean isOuter(Tri tri) {
    return !isInner(tri);
  }

  /**
   * Determines whether the specified edge is an inner edge.
   * @return true, if inner; false, otherwise.
   */
  public boolean isInner(Edge edge) {
    Tri triLeft = edge.triLeft();
    if (triLeft!=null && isInner(triLeft))
      return true;
    Tri triRight = edge.triRight();
    return triRight!=null && isInner(triRight);
  }

  /**
   * Determines whether the specified edge is an outer edge.
   * @return true, if outer; false, otherwise.
   */
  public boolean isOuter(Edge edge) {
    return !isInner(edge);
  }

  /**
   * Marks the specified node (red). Marks are used during iterations
   * over nodes. Because nodes (e.g., those nodes adjacent to a
   * particular node) are linked in an unordered structure, such
   * iterations are often performed by recursively visiting nodes,
   * and marks are used to tag nodes that have already been visited.
   * @param node the node to mark (red).
   */
  public final void mark(Node node) {
    node._mark = _nodeMarkRed;
  }

  /**
   * Marks the specified node red.
   * This is equivalent to simply marking the node.
   * @param node the node to mark red.
   */
  public final void markRed(Node node) {
    node._mark = _nodeMarkRed;
  }

  /**
   * Marks the specified node blue.
   * @param node the node to mark blue.
   */
  public final void markBlue(Node node) {
    node._mark = _nodeMarkBlue;
  }

  /**
   * Determines whether the specified node is marked (red).
   * @param node the node.
   * @return true, if the node is marked (red); false, otherwise.
   */
  public final boolean isMarked(Node node) {
    return node._mark==_nodeMarkRed;
  }

  /**
   * Determines whether the specified node is marked red.
   * @param node the node.
   * @return true, if the node is marked red; false, otherwise.
   */
  public final boolean isMarkedRed(Node node) {
    return node._mark==_nodeMarkRed;
  }

  /**
   * Determines whether the specified node is marked blue.
   * @param node the node.
   * @return true, if the node is marked blue; false, otherwise.
   */
  public final boolean isMarkedBlue(Node node) {
    return node._mark==_nodeMarkBlue;
  }

  /**
   * Clears all node marks, so that no node is marked. This can usually
   * be accomplished without iterating over all nodes in the mesh.
   */
  public synchronized void clearNodeMarks() {

    // If the mark is about to overflow, we must zero all the marks.
    if (_nodeMarkRed==NODE_MARK_MAX) {
      Node node = _nroot;
      do {
        node._mark = 0;
        node = node._next;
      } while (node!=_nroot);
      _nodeMarkRed = 0;
      _nodeMarkBlue = 0;
    }

    // Usually, can simply increment/decrement the mark values.
    ++_nodeMarkRed;
    --_nodeMarkBlue;
  }

  /**
   * Marks the specified tri (red). Marks are used during iterations
   * over tris. Because tris (e.g., those tris containing a particular
   * node) are linked in an unordered structure, such iterations are
   * often performed by recursively visiting tris, and marks are used
   * to tag tris that have already been visited.
   * @param tri the tri to mark (red).
   */
  public final void mark(Tri tri) {
    tri._mark = _triMarkRed;
  }

  /**
   * Marks the specified tri red.
   * This is equivalent to simply marking the tri.
   * @param tri the tri to mark red.
   */
  public final void markRed(Tri tri) {
    tri._mark = _triMarkRed;
  }

  /**
   * Marks the specified tri blue.
   * @param tri the tri to mark blue.
   */
  public final void markBlue(Tri tri) {
    tri._mark = _triMarkBlue;
  }

  /**
   * Determines whether the specified tri is marked (red).
   * @param tri the tri.
   * @return true, if the tri is marked (red); false, otherwise.
   */
  public final boolean isMarked(Tri tri) {
    return tri._mark==_triMarkRed;
  }

  /**
   * Determines whether the specified tri is marked red.
   * @param tri the tri.
   * @return true, if the tri is marked red; false, otherwise.
   */
  public final boolean isMarkedRed(Tri tri) {
    return tri._mark==_triMarkRed;
  }

  /**
   * Determines whether the specified tri is marked blue.
   * @param tri the tri.
   * @return true, if the tri is marked blue; false, otherwise.
   */
  public final boolean isMarkedBlue(Tri tri) {
    return tri._mark==_triMarkBlue;
  }

  /**
   * Clears all tri marks, so that no tri is marked. This can usually
   * be accomplished without iterating over all tris in the mesh.
   */
  public synchronized void clearTriMarks() {

    // If the mark is about to overflow, we must zero all the marks.
    // This is tricky, because tris form a cyclic graph. First, we
    // recursively mark all tris, so that no marks are zero. Then,
    // we recursively zero all tri marks.
    if (_triMarkRed==TRI_MARK_MAX) {
      ++_triMarkRed;
      --_triMarkBlue;
      markAllTris(_troot);
      zeroTriMarks(_troot);
      _triMarkRed = 0;
      _triMarkBlue = 0;
    }

    // Usually, we simply increment/decrement the mark values.
    ++_triMarkRed;
    --_triMarkBlue;
  }

  /**
   * Validates the internal consistency of the mesh.
   * @exception RuntimeException if the mesh is invalid.
   */
  public synchronized void validate() {

    // Check nodes.
    int nnode = 0;
    NodeIterator ni = getNodes();
    while (ni.hasNext()) {
      ++nnode;
      Node node = ni.next();
      validate(node);
    }
    assert nnode==_nnode;

    // Check tris.
    int ntri = 0;
    TriIterator ti = getTris();
    while (ti.hasNext()) {
      ++ntri;
      Tri tri = ti.next();
      validate(tri);
    }
    assert ntri==_ntri;
  }

  ///////////////////////////////////////////////////////////////////////////
  // protected

  /**
   * Initialization for use in constructers and serialization (readObject).
   */
  protected void init() {
    _version = 0;
    _nnode = 0;
    _ntri = 0;
    _nroot = null;
    _troot = null;
    _sampledNodes = new HashSet<Node>(256);
    _triMarkRed = 0;
    _triMarkBlue = 0;
    _nodeMarkRed = 0;
    _nodeMarkBlue = 0;
    _edgeSet = new EdgeSet(256,0.25);
    _nodeSet = new NodeSet(256,0.25);
    _nodeList = new NodeList();
    _nmin = null;
    _dmin = 0.0;
    _deadTris = new TriList();
    _nnodeListeners = 0;
    _ntriListeners = 0;
    _listeners = new EventListenerList();
    _outerEnabled = false;
    _xminOuter = 0.0;
    _yminOuter = 0.0;
    _xmaxOuter = 0.0;
    _ymaxOuter = 0.0;
    _nnodeValues = 0;
    _lnodeValues = 0;
    _nodePropertyMaps = new HashMap<String,NodePropertyMap>();
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private static final int NODE_MARK_MAX = Integer.MAX_VALUE-1;
  private static final int TRI_MARK_MAX = Integer.MAX_VALUE-1;

  private long _version; // the version number
  private int _nnode; // number of nodes
  private int _ntri; // number of tris
  private Node _nroot; // a node in the mesh
  private Tri _troot; // a tri in the mesh
  private HashSet<Node> _sampledNodes; // for point location
  private int _triMarkRed; // current value of red tri mark
  private int _triMarkBlue; // current value of blue tri mark
  private int _nodeMarkRed; // current value of red node mark
  private int _nodeMarkBlue; // current value of blue node mark
  private EdgeSet _edgeSet; // fast hashed edges
  private NodeSet _nodeSet; // fast hashed nodes
  private NodeList _nodeList; // temporary list of nodes
  private Node _nmin; // the nearest node (used only during searches)
  private double _dmin; // distance squared to nearest node
  private TriList _deadTris; // tri graveyard
  private int _nnodeListeners; // number of node listeners
  private int _ntriListeners; // number of tri listeners
  private EventListenerList _listeners;
  private boolean _outerEnabled; // true, if outer box testing is on
  private double _xminOuter; // min x of outer box
  private double _yminOuter; // min y of outer box
  private double _xmaxOuter; // max x of outer box
  private double _ymaxOuter; // max y of outer box
  private int _nnodeValues; // number of internal node property values
  private int _lnodeValues; // length of internal node property arrays
  private Map<String,NodePropertyMap> _nodePropertyMaps; // node property maps

  /**
   * Internal node property map uses the array of values in each node.
   */
  private static class NodePropertyMapInternal implements NodePropertyMap {
    public Object get(Node node) {
      return node._values[_index];
    }
    public void put(Node node, Object value) {
      node._values[_index] = value;
    }
    NodePropertyMapInternal(int index) {
      _index = index;
    }
    private int _index;
  }

  /**
   * Updates node property values for consistency with this mesh.
   */
  private synchronized void updatePropertyValues(Node node) {
    if (_lnodeValues==0) {
      node._values = null;
    } else if (node._values==null) {
      node._values = new Object[_lnodeValues];
    } else if (node._values.length!=_lnodeValues) {
      Object[] valuesOld = node._values;
      Object[] valuesNew = new Object[_lnodeValues];
      int n = min(valuesOld.length,valuesNew.length);
      for (int i=0; i<n; ++i)
        valuesNew[i] = valuesOld[i];
      node._values = valuesNew;
    }
  }

  /**
   * Copies node property values to node data.
   * Each node has a data field that can store a value of any type.
   * Access to this data field is often more efficient than getting 
   * a value via a node property map. For each node in the mesh, this
   * method copies the value from the node property map to the node
   * data field. This method should be used only if values in the
   * node data field are likely to be used more than once.
   * <p>
   * <em>Is this useful? Keep private until we have a need for it.</em>
   * @param map the map from which to get node property values.
   */
  /*
  private synchronized void cacheNodeProperty(NodePropertyMap map) {
    NodeIterator ni = getNodes();
    while (ni.hasNext()) {
      Node node = ni.next();
      node.data = map.get(node);
    }
  }
  */

  /**
   * Returns a new tri, possibly one resurrected from the dead.
   * Resurrection reduces the need for garbage collection of dead tris.
   */
  private Tri makeTri(Node n0, Node n1, Node n2) {
    ++_ntri;
    int ndead = _deadTris.ntri();
    if (ndead==0) {
      _troot = new Tri(n0,n1,n2);
    } else {
      _troot = _deadTris.remove(ndead-1);
      _troot.init(n0,n1,n2);
    }
    if (_ntriListeners>0)
      fireTriAdded(_troot);
    return _troot;
  }

  /**
   * Kills a tri and, if there is room, buries it in the graveyard,
   * from where it may be resurrected later.
   */
  private void killTri(Tri tri) {
    --_ntri;
    fireTriRemoved(tri);
    int ndead = _deadTris.ntri();
    if (ndead<256) {
      _deadTris.add(tri);
    }
    // We assume that killTri is called only within contexts in
    // which _troot is updated to point to a live tri in the mesh.
    // Therefore, we do not update _troot here, when _troot==tri.
  }

  /**
   * Returns the distance squared between the specified node and a point
   * with specified coordinates.
   */
  private static double distanceSquared(
    Node node, double x, double y)
  {
    double dx = x-node._x;
    double dy = y-node._y;
    return dx*dx+dy*dy;
  }

  /**
   * Returns true iff node n is left of oriented line ab.
   * Perturbation of coordinates ensures that the node is not in the line.
   */
  private static boolean leftOfLine(Node a, Node b, Node n) {
    return Geometry.leftOfLine(a._x,a._y,b._x,b._y,n._x,n._y)>0.0;
  }

  /**
   * Returns true iff point (x,y) is left of oriented line ab.
   */
  private static boolean leftOfLine(
    Node a, Node b,
    double x, double y)
  {
    return Geometry.leftOfLine(a._x,a._y,b._x,b._y,x,y)>0.0;
  }

  /**
   * Returns true iff node n is in circumcircle of tri abc.
   * Perturbation of coordinates ensures that the node is not on the circle.
   */
  private static boolean inCircle(
    Node a, Node b, Node c, Node n)
  {
    return Geometry.inCircle(a._x,a._y,b._x,b._y,c._x,c._y,n._x,n._y)>0.0;
  }

  /**
   * Returns true iff point (x,y) is in circumcircle of tri abc.
   */
  private static boolean inCircle(
    Node a, Node b, Node c,
    double x, double y)
  {
    return Geometry.inCircle(a._x,a._y,b._x,b._y,c._x,c._y,x,y)>0.0;
  }

  /**
   * Creates the first tri in the mesh from three nodes.
   */
  private void createFirstTri() {
    Check.state(_nnode==3,"exactly three nodes available for first tri");
    Node n0 = _nroot;
    Node n1 = n0._next;
    Node n2 = n1._next;
    double orient = Geometry.leftOfLine(
      n0._x,n0._y,
      n1._x,n1._y,
      n2._x,n2._y);
    Check.state(orient!=0,"three nodes for first tri are not co-linear");
    if (orient>0.0) {
      makeTri(n0,n1,n2);
    } else {
      makeTri(n0,n2,n1);
    }
  }

  /**
   * Recursively adds nabors of the specified node to the specified list.
   * Both tri and node marks must be cleared before calling this method. 
   * This method could be made shorter by using another recursive method, 
   * but this longer inlined version is more efficient.
   */
  private void getNodeNabors(
    Node node, Tri tri, NodeList nabors)
  {
    mark(tri);
    Node n0 = tri._n0;
    Node n1 = tri._n1;
    Node n2 = tri._n2;
    Tri t0 = tri._t0;
    Tri t1 = tri._t1;
    Tri t2 = tri._t2;
    if (node==n0) {
      if (!isMarked(n1)) {
        mark(n1);
        nabors.add(n1);
      }
      if (!isMarked(n2)) {
        mark(n2);
        nabors.add(n2);
      }
      if (t1!=null && !isMarked(t1))
        getNodeNabors(node,t1,nabors);
      if (t2!=null && !isMarked(t2))
        getNodeNabors(node,t2,nabors);
    } else if (node==n1) {
      if (!isMarked(n2)) {
        mark(n2);
        nabors.add(n2);
      }
      if (!isMarked(n0)) {
        mark(n0);
        nabors.add(n0);
      }
      if (t2!=null && !isMarked(t2))
        getNodeNabors(node,t2,nabors);
      if (t0!=null && !isMarked(t0))
        getNodeNabors(node,t0,nabors);
    } else if (node==n2) {
      if (!isMarked(n0)) {
        mark(n0);
        nabors.add(n0);
      }
      if (!isMarked(n1)) {
        mark(n1);
        nabors.add(n1);
      }
      if (t0!=null && !isMarked(t0))
        getNodeNabors(node,t0,nabors);
      if (t1!=null && !isMarked(t1))
        getNodeNabors(node,t1,nabors);
    } else {
      assert false:"node is referenced by tri";
    }
  }

  /**
   * Accumulates nabor nodes of the specified node. For each nabor node 
   * found, sets the corresponding step to the specified value. Returns 
   * the total number of nabor nodes and steps.
   * <p>
   * Note that this method does not clear the node marks.
   */
  private void getNodeNabors(Node node, int step, NodeStepList nabors) {
    Tri[] tris = getTriNabors(node);
    int ntri = tris.length;
    for (int itri=0; itri<ntri; ++itri) {
      Tri tri = tris[itri];
      Node n0 = tri._n0;
      Node n1 = tri._n1;
      Node n2 = tri._n2;
      if (node==n0) {
        if (!isMarked(n1)) {
          mark(n1);
          nabors.add(n1,step);
        }
        if (!isMarked(n2)) {
          mark(n2);
          nabors.add(n2,step);
        }
      } else if (node==n1) {
        if (!isMarked(n0)) {
          mark(n0);
          nabors.add(n0,step);
        }
        if (!isMarked(n2)) {
          mark(n2);
          nabors.add(n2,step);
        }
      } else if (node==n2) {
        if (!isMarked(n0)) {
          mark(n0);
          nabors.add(n0,step);
        }
        if (!isMarked(n1)) {
          mark(n1);
          nabors.add(n1,step);
        }
      }
    }
  }

  /**
   * Recursively adds tri nabors of the specified node to the specified list.
   * The tri marks must be cleared before calling this method. This method 
   * could be made shorter by using another recursive method, but this longer 
   * inlined version is more efficient.
   */
  private void getTriNabors(Node node, Tri tri, TriList nabors) {
    if (tri!=null) {
      mark(tri);
      nabors.add(tri);
      Node n0 = tri._n0;
      Node n1 = tri._n1;
      Node n2 = tri._n2;
      Tri t0 = tri._t0;
      Tri t1 = tri._t1;
      Tri t2 = tri._t2;
      if (node==n0) {
        if (t1!=null && !isMarked(t1))
          getTriNabors(node,t1,nabors);
        if (t2!=null && !isMarked(t2))
          getTriNabors(node,t2,nabors);
      } else if (node==n1) {
        if (t2!=null && !isMarked(t2))
          getTriNabors(node,t2,nabors);
        if (t0!=null && !isMarked(t0))
          getTriNabors(node,t0,nabors);
      } else if (node==n2) {
        if (t0!=null && !isMarked(t0))
          getTriNabors(node,t0,nabors);
        if (t1!=null && !isMarked(t1))
          getTriNabors(node,t1,nabors);
      } else {
        assert false:"node is referenced by tri";
      }
    }
  }

  /**
   * Finds the node nearest to the point with specified coordinates.
   */
  private Node findNodeNearest(double x, double y) {

    // First, find the nearest node among the sampled nodes.
    _nmin = _nroot;
    _dmin = distanceSquared(_nmin,x,y);
    for (Node n:_sampledNodes) {
      double d = distanceSquared(n,x,y);
      if (d<_dmin) {
        _dmin = d;
        _nmin = n;
      }
    }

    // Then, recursively search node nabors to find the nearest node.
    // Note that we clear the node marks once, so that we never compute
    // the distance to a node twice, but that we clear the tri marks
    // *inside* the loop, to ensure that we visit all tri nabors of the
    // current nearest node.
    clearNodeMarks();
    double dmin;
    do {
      clearTriMarks();
      dmin = _dmin;
      findNodeNaborNearest(x,y,_nmin,_nmin._tri);
    } while (_dmin<dmin);
    return _nmin;
  }

  /**
   * Recursively finds the node nabor of the specified node that is
   * nearest to the specified point, beginning with the nabors in the
   * specified tri, which must reference the specified node.
   * Both tri and node marks must be cleared before calling this method.
   */
  private void findNodeNaborNearest(
    double x, double y, Node node, Tri tri)
  {
    mark(tri);
    Node n0 = tri._n0;
    Node n1 = tri._n1;
    Node n2 = tri._n2;
    Tri t0 = tri._t0;
    Tri t1 = tri._t1;
    Tri t2 = tri._t2;
    if (node==n0) {
      findNodeNaborNearest(x,y,node,n1,n2,t1,t2);
    } else if (node==n1) {
      findNodeNaborNearest(x,y,node,n2,n0,t2,t0);
    } else if (node==n2) {
      findNodeNaborNearest(x,y,node,n0,n1,t0,t1);
    } else {
      assert false:"node is referenced by tri";
    }
  }
  private void findNodeNaborNearest(
    double x, double y, Node node,
    Node na, Node nb, Tri ta, Tri tb)
  {
    if (!isMarked(na)) {
      mark(na);
      double da = distanceSquared(na,x,y);
      if (da<_dmin) {
        _dmin = da;
        _nmin = na;
      }
    }
    if (!isMarked(nb)) {
      mark(nb);
      double db = distanceSquared(nb,x,y);
      if (db<_dmin) {
        _dmin = db;
        _nmin = nb;
      }
    }
    if (ta!=null && !isMarked(ta))
      findNodeNaborNearest(x,y,node,ta);
    if (tb!=null && !isMarked(tb))
      findNodeNaborNearest(x,y,node,tb);
  }

  /**
   * Recursively searches for any tri that references nodes na and nb,
   * given a tri that references node na. If no such tri exists, then
   * returns null. Tri marks must be cleared before calling this method.
   */
  private Tri findTri(Tri tri, Node na, Node nb) {
    if (tri!=null) {
      mark(tri);
      Node n0 = tri._n0;
      Node n1 = tri._n1;
      Node n2 = tri._n2;
      Tri t0 = tri._t0;
      Tri t1 = tri._t1;
      Tri t2 = tri._t2;
      if (na==n0) {
        if (nb==n1 || nb==n2 ||
            t1!=null && !isMarked(t1) && (tri=findTri(t1,na,nb))!=null ||
            t2!=null && !isMarked(t2) && (tri=findTri(t2,na,nb))!=null)
          return tri;
      } else if (na==n1) {
        if (nb==n2 || nb==n0 ||
            t2!=null && !isMarked(t2) && (tri=findTri(t2,na,nb))!=null ||
            t0!=null && !isMarked(t0) && (tri=findTri(t0,na,nb))!=null)
          return tri;
      } else if (na==n2) {
        if (nb==n0 || nb==n1 ||
            t0!=null && !isMarked(t0) && (tri=findTri(t0,na,nb))!=null ||
            t1!=null && !isMarked(t1) && (tri=findTri(t1,na,nb))!=null)
          return tri;
      } else {
        assert false:"node na is referenced by tri";
      }
    }
    return null;
  }

  /**
   * Recursively searches for any tri that references nodes na, nb, and nc,
   * given a tri that references nodes na and nb. If no such tri exists, 
   * then returns null. Tri marks must be cleared before calling this method.
   */
  private Tri findTri(Tri tri, Node na, Node nb, Node nc) {
    if (tri!=null) {
      mark(tri);
      Node n0 = tri._n0;
      Node n1 = tri._n1;
      Node n2 = tri._n2;
      Tri t0 = tri._t0;
      Tri t1 = tri._t1;
      Tri t2 = tri._t2;
      if (na==n0) {
        if (nb==n1) {
          if (nc==n2 ||
              t2!=null && !isMarked(t2) && (tri=findTri(t2,na,nb,nc))!=null)
          return tri;
        } else if (nb==n2) {
          if (nc==n1 ||
              t1!=null && !isMarked(t1) && (tri=findTri(t1,na,nb,nc))!=null)
          return tri;
        } else {
          assert false:"node nb is referenced by tri";
        }
      } else if (na==n1) {
        if (nb==n0) {
          if (nc==n2 ||
              t2!=null && !isMarked(t2) && (tri=findTri(t2,na,nb,nc))!=null)
          return tri;
        } else if (nb==n2) {
          if (nc==n0 ||
              t0!=null && !isMarked(t0) && (tri=findTri(t0,na,nb,nc))!=null)
          return tri;
        } else {
          assert false:"node nb is referenced by tri";
        }
      } else if (na==n2) {
        if (nb==n0) {
          if (nc==n1 ||
              t1!=null && !isMarked(t1) && (tri=findTri(t1,na,nb,nc))!=null)
          return tri;
        } else if (nb==n1) {
          if (nc==n0 ||
              t0!=null && !isMarked(t0) && (tri=findTri(t0,na,nb,nc))!=null)
          return tri;
        } else {
          assert false:"node nb is referenced by tri";
        }
      } else {
        assert false:"node na is referenced by tri";
      }
    }
    return null;
  }

  /**
   * Locates a point.
   */
  private PointLocation locatePoint(double x, double y) {

    // If no tris yet, search the node list for an exact match.
    // Here, we use unperturbed node coordinates.
    if (_troot==null) {
      if (_nroot!=null) {
        Node node = _nroot;
        do {
          if (x==node.x() && y==node.y())
            return new PointLocation(node);
          node = node._next;
        } while (node!=_nroot);
      }
      return new PointLocation(null,false);
    }

    // Otherwise, find a good tri in which to begin the recursive search.
    Node nmin = _nroot;
    double dmin = distanceSquared(nmin,x,y);
    for (Node n:_sampledNodes) {
      double d = distanceSquared(n,x,y);
      if (d<dmin) {
        dmin = d;
        nmin = n;
      }
    }
    Tri tri = nmin._tri;
    return locatePoint(tri,x,y);
  }

  /**
   * Recursively searches tris beginning with the specified tri,
   * to locate the point (x,y).
   */
  private PointLocation locatePoint(Tri tri, double x, double y) {

    // Begin future searches in the specified tri.
    _troot = tri;

    // Node coordinates.
    Node n0 = tri._n0;
    Node n1 = tri._n1;
    Node n2 = tri._n2;
    double x0 = n0._x;
    double y0 = n0._y;
    double x1 = n1._x;
    double y1 = n1._y;
    double x2 = n2._x;
    double y2 = n2._y;

    // If exactly on a node, the search is complete.
    // We assume that this scenario is rare, but that the cost of testing
    // for it is small compared to the left-of-plane tests below.
    if (x==x0 && y==y0) {
      return new PointLocation(n0);
    } else if (x==x1 && y==y1) {
      return new PointLocation(n1);
    } else if (x==x2 && y==y2) {
      return new PointLocation(n2);
    }

    // Locate the search point with respect to the three edges of the tri.
    // If any left-of-line test is positive, then recursively search in
    // the corresponding nabor tri, unless that nabor tri is null, in which
    // case the search point lies outside the mesh and the specified tri is
    // on the convex hull and is visible from the search point.
    // TODO: experiment to determine whether it is more efficient to
    // go through the edge with the most positive left-of-line test.
    double d0 = Geometry.leftOfLine(x2,y2,x1,y1,x,y);
    if (d0>0.0) {
      Tri triNabor = tri.triNabor(n0);
      if (triNabor!=null) {
        return locatePoint(triNabor,x,y);
      } else {
        return new PointLocation(tri,false);
      }
    }
    double d1 = Geometry.leftOfLine(x0,y0,x2,y2,x,y);
    if (d1>0.0) {
      Tri triNabor = tri.triNabor(n1);
      if (triNabor!=null) {
        return locatePoint(triNabor,x,y);
      } else {
        return new PointLocation(tri,false);
      }
    }
    double d2 = Geometry.leftOfLine(x1,y1,x0,y0,x,y);
    if (d2>0.0) {
      Tri triNabor = tri.triNabor(n2);
      if (triNabor!=null) {
        return locatePoint(triNabor,x,y);
      } else {
        return new PointLocation(tri,false);
      }
    }

    // If strictly inside the tri, the search is complete.
    if (d0<0.0 && d1<0.0 && d2<0.0) {
      return new PointLocation(tri);
    }

    // Must be on an edge of the specified tri.
    if (d0==0.0) {
      return new PointLocation(new Edge(tri,n0));
    } else if (d1==0.0) {
      return new PointLocation(new Edge(tri,n1));
    } else if (d2==0.0) {
      return new PointLocation(new Edge(tri,n2));
    }

    // Where are we?!
    assert false:"successfully located the point";
    return null;
  }

  /**
   * Beginning with a tri that contains a node located inside the
   * mesh, recursively adds Delaunay edges to the edge set.
   * The tri marks must be cleared before calling this method.
   */
  private void getDelaunayEdgesInside(Node node, Tri tri) {
    if (tri!=null && !isMarked(tri)) {
      mark(tri);
      Node n0 = tri._n0;
      Node n1 = tri._n1;
      Node n2 = tri._n2;
      if (inCircle(n0,n1,n2,node)) {
        killTri(tri);
        Tri t0 = tri._t0;
        Tri t1 = tri._t1;
        Tri t2 = tri._t2;
        _edgeSet.addMate(tri,n0);
        _edgeSet.addMate(tri,n1);
        _edgeSet.addMate(tri,n2);
        getDelaunayEdgesInside(node,t0);
        getDelaunayEdgesInside(node,t1);
        getDelaunayEdgesInside(node,t2);
      }
    }
  }

  /**
   * Beginning with a tri that is visible from a node located outside
   * the mesh, recursively adds Delaunay edges to the edge set.
   * The tri marks must be cleared before calling this method.
   */
  private void getDelaunayEdgesOutside(Node node, Tri tri) {
    if (tri!=null && !isMarked(tri)) {
      mark(tri);
      Node n0 = tri._n0;
      Node n1 = tri._n1;
      Node n2 = tri._n2;
      Tri t0 = tri._t0;
      Tri t1 = tri._t1;
      Tri t2 = tri._t2;
      if (t0==null && leftOfLine(n2,n1,node)) {
        _edgeSet.add(tri,n0);
        getDelaunayEdgesOutside(node,getNextTriOnHull(tri,n1,n0));
        getDelaunayEdgesOutside(node,getNextTriOnHull(tri,n2,n0));
      }
      if (t1==null && leftOfLine(n0,n2,node)) {
        _edgeSet.add(tri,n1);
        getDelaunayEdgesOutside(node,getNextTriOnHull(tri,n2,n1));
        getDelaunayEdgesOutside(node,getNextTriOnHull(tri,n0,n1));
      }
      if (t2==null && leftOfLine(n1,n0,node)) {
        _edgeSet.add(tri,n2);
        getDelaunayEdgesOutside(node,getNextTriOnHull(tri,n0,n2));
        getDelaunayEdgesOutside(node,getNextTriOnHull(tri,n1,n2));
      }
      if (inCircle(n0,n1,n2,node)) {
        killTri(tri);
        _edgeSet.addMate(tri,n0);
        _edgeSet.addMate(tri,n1);
        _edgeSet.addMate(tri,n2);
        getDelaunayEdgesOutside(node,t0);
        getDelaunayEdgesOutside(node,t1);
        getDelaunayEdgesOutside(node,t2);
      }
    }
  }

  /**
   * Beginning with a tri that references the specified node, recursively
   * adds the Delaunay edges opposite this node to the edge set. Each
   * edge corresponds to a tri that will no longer exist when the node
   * is removed. Both the node and tri marks must be cleared before
   * calling this method.
   */
  private void getDelaunayEdgesOpposite(Node node, Tri tri) {
    if (tri!=null && !isMarked(tri)) {
      mark(tri);
      killTri(tri);
      Node n0 = tri._n0;
      Node n1 = tri._n1;
      Node n2 = tri._n2;
      Tri t0 = tri._t0;
      Tri t1 = tri._t1;
      Tri t2 = tri._t2;
      if (node==n0) {
        _edgeSet.addMate(tri,n0);
        getDelaunayEdgesOpposite(node,n1,n2,t1,t2);
      } else if (node==n1) {
        _edgeSet.addMate(tri,n1);
        getDelaunayEdgesOpposite(node,n2,n0,t2,t0);
      } else if (node==n2) {
        _edgeSet.addMate(tri,n2);
        getDelaunayEdgesOpposite(node,n0,n1,t0,t1);
      } else {
        assert false:"node is referenced by tri";
      }
    }
  }
  private void getDelaunayEdgesOpposite(
    Node node, Node na, Node nb, Tri ta, Tri tb)
  {
    if (!isMarked(na)) {
      mark(na);
      _nodeList.add(na);
    }
    if (!isMarked(nb)) {
      mark(nb);
      _nodeList.add(nb);
    }
    getDelaunayEdgesOpposite(node,ta);
    getDelaunayEdgesOpposite(node,tb);
  }

  /**
   * Given a tri and node on the hull, and another node, typically
   * inside (not on) the hull, gets the next tri on the hull that
   * is opposite the node on the hull. If the other node is also
   * on the hull, this method simply returns the specified tri.
   */
  private Tri getNextTriOnHull(Tri tri, Node node, Node nodeOther) {
    for (Tri tnext=tri.triNabor(node); tnext!=null; tnext=tri.triNabor(node)) {
      node = nodeOther;
      nodeOther = tri.nodeNabor(tnext);
      tri = tnext;
    }
    return tri;
  }

  /**
   * Finds the node nearest to the point with specified coordinates.
   * This method appears to be 2-3 times slower than findNodeNearest,
   * but it is a lot of code, so we make it private for now.
   * (Currently unused.)
   */
  public synchronized Node findNodeNearestSlow(float x, float y) {

    // Clear the marks.
    clearTriMarks();
    clearNodeMarks();

    // If no tris, simply search all of the nodes to find the nearest.
    _dmin = Double.MAX_VALUE;
    _nmin = null;
    if (_troot==null) {
      if (_nroot!=null) {
        Node node = _nroot;
        do {
          updateNodeNearest(x,y,node);
          node = node._next;
        } while (node!=_nroot);
      }
      assert _nmin!=null;
      return _nmin;
    }

    // Otherwise, where is the point?
    PointLocation pl = locatePoint(x,y);

    // If the point is exactly on a node, that node is nearest.
    if (pl.isOnNode()) {
      updateNodeNearest(x,y,pl.node());
      return _nmin;
    }

    // Otherwise (if not on a node), search for the nearest node,
    // beginning with the tri containing or visible from the point.
    if (pl.isInside()) {
      findNodeNearestInside(x,y,pl.tri());
    } else {
      findNodeNearestOutside(x,y,pl.tri());
    }
    return _nmin;
  }

  /**
   * Recursively updates the node nearest to a point with specified (x,y)
   * coordinates inside the mesh, beginning with the nodes referenced by
   * the specified tri. Both node and tri marks must be cleared before
   * calling this method. The logic is much like that for adding a node.
   * (Currently unused.)
   */
  private void findNodeNearestInside(double x, double y, Tri tri)
  {
    if (tri!=null && !isMarked(tri)) {
      mark(tri);
      Node n0 = tri._n0;
      Node n1 = tri._n1;
      Node n2 = tri._n2;
      updateNodeNearest(x,y,n0);
      updateNodeNearest(x,y,n1);
      updateNodeNearest(x,y,n2);
      if (inCircle(n0,n1,n2,x,y)) {
        Tri t0 = tri._t0;
        Tri t1 = tri._t1;
        Tri t2 = tri._t2;
        findNodeNearestInside(x,y,t0);
        findNodeNearestInside(x,y,t1);
        findNodeNearestInside(x,y,t2);
      }
    }
  }

  /**
   * Recursively updates the node nearest to a point with specified (x,y)
   * coordinates outside the mesh, beginning with the nodes referenced by
   * the specified tri. Both node and tri marks must be cleared before
   * calling this method. The logic is much like that for adding a node.
   * (Currently unused.)
   */
  private void findNodeNearestOutside(double x, double y, Tri tri) {
    if (tri!=null && !isMarked(tri)) {
      mark(tri);
      Node n0 = tri._n0;
      Node n1 = tri._n1;
      Node n2 = tri._n2;
      updateNodeNearest(x,y,n0);
      updateNodeNearest(x,y,n1);
      updateNodeNearest(x,y,n2);
      Tri t0 = tri._t0;
      Tri t1 = tri._t1;
      Tri t2 = tri._t2;
      if (t0==null && leftOfLine(n2,n1,x,y)) {
        findNodeNearestOutside(x,y,getNextTriOnHull(tri,n1,n0));
        findNodeNearestOutside(x,y,getNextTriOnHull(tri,n2,n0));
      }
      if (t1==null && leftOfLine(n0,n2,x,y)) {
        findNodeNearestOutside(x,y,getNextTriOnHull(tri,n2,n1));
        findNodeNearestOutside(x,y,getNextTriOnHull(tri,n0,n1));
      }
      if (t2==null && leftOfLine(n1,n0,x,y)) {
        findNodeNearestOutside(x,y,getNextTriOnHull(tri,n0,n2));
        findNodeNearestOutside(x,y,getNextTriOnHull(tri,n1,n2));
      }
      if (inCircle(n0,n1,n2,x,y)) {
        findNodeNearestOutside(x,y,t0);
        findNodeNearestOutside(x,y,t1);
        findNodeNearestOutside(x,y,t2);
      }
    }
  }

  /**
   * If the point with specified (x,y) coordinates is nearer to the
   * specified node than the nearest node found so far, then replaces
   * the nearest node (and the node root) with the specified node.
   * Does nothing if the specified node is marked.
   * (Currently unused.)
   */
  private void updateNodeNearest(double x, double y, Node n) {
    if (!isMarked(n)) {
      mark(n);
      double d = distanceSquared(n,x,y);
      if (d<_dmin) {
        _dmin = d;
        _nmin = n;
        _nroot = n;
      }
    }
  }

  /**
   * Links one tri with its tri nabor.
   */
  private void linkTris(Tri tri, Node node, Tri triNabor, Node nodeNabor) {
    if (tri!=null) {
      if (node==tri._n0) {
        tri._t0 = triNabor;
      } else if (node==tri._n1) {
        tri._t1 = triNabor;
      } else if (node==tri._n2) {
        tri._t2 = triNabor;
      } else {
        assert false:"node referenced by tri";
      }
    }
    if (triNabor!=null) {
      if (nodeNabor==triNabor._n0) {
        triNabor._t0 = tri;
      } else if (nodeNabor==triNabor._n1) {
        triNabor._t1 = tri;
      } else if (nodeNabor==triNabor._n2) {
        triNabor._t2 = tri;
      } else {
        assert false:"nodeNabor referenced by triNabor";
      }
    }
  }

  /**
   * Recursively marks the specified tri and all unmarked neighbor tris.
   */
  private void markAllTris(Tri tri) {
    tri._mark = _triMarkRed;
    Tri t0 = tri._t0;
    if (t0!=null && t0._mark!=_triMarkRed)
      markAllTris(t0);
    Tri t1 = tri._t1;
    if (t1!=null && t1._mark!=_triMarkRed)
      markAllTris(t1);
    Tri t2 = tri._t2;
    if (t2!=null && t2._mark!=_triMarkRed)
      markAllTris(t2);
  }

  /**
   * Recursively zeros the mark in the specified tri and in all neighbor
   * tris with non-zero marks.
   */
  private void zeroTriMarks(Tri tri) {
    tri._mark = 0;
    Tri t0 = tri._t0;
    if (t0!=null && t0._mark!=0)
      zeroTriMarks(t0);
    Tri t1 = tri._t1;
    if (t1!=null && t1._mark!=0)
      zeroTriMarks(t1);
    Tri t2 = tri._t2;
    if (t2!=null && t2._mark!=0)
      zeroTriMarks(t2);
  }

  /**
   * Beginning with any tri, searches for an edge on the hull.
   * Because this method recursively marks the tris, all tri marks must
   * be cleared before calling this method.
   */
  private Edge getEdgeOnHull(Tri tri) {
    mark(tri);
    if (tri._t0==null)
      return new Edge(tri,tri._n0);
    if (tri._t1==null)
      return new Edge(tri,tri._n1);
    if (tri._t2==null)
      return new Edge(tri,tri._n2);
    if (!isMarked(tri._t0)) {
      Edge edge = getEdgeOnHull(tri._t0);
      if (edge!=null)
        return edge;
    }
    if (!isMarked(tri._t1)) {
      Edge edge = getEdgeOnHull(tri._t1);
      if (edge!=null)
        return edge;
    }
    if (!isMarked(tri._t2)) {
      Edge edge = getEdgeOnHull(tri._t2);
      if (edge!=null)
        return edge;
    }
    return null;
  }

  /**
   * Beginning with any edge on the hull, recursively adds all edges
   * on the hull to the specified set of edges.
   */
  private void getEdgesOnHull(Edge edge, HashSet<Edge> edges) {
    if (!edges.contains(edge)) {
      edges.add(edge);
      getEdgesOnHull(getNextEdgeOnHull(edge.nodeA(),edge),edges);
      getEdgesOnHull(getNextEdgeOnHull(edge.nodeB(),edge),edges);
    }
  }

  /**
   * Given a node referenced by one edge of the hull, gets the next
   * edge on the hull that is opposite that node.
   */
  private Edge getNextEdgeOnHull(Node node, Edge edge) {
    Tri tri = edge.triLeft();
    Node next = edge.nodeLeft();
    for (Tri tnext=tri.triNabor(node); tnext!=null; tnext=tri.triNabor(node)) {
      node = next;
      next = tri.nodeNabor(tnext);
      tri = tnext;
    }
    return new Edge(tri,node);
  }

  /**
   * Beginning with any edge on the hull that is visible from the
   * specified node (not yet in the mesh), recursively adds all
   * visible edges on the hull to the specified set of edges.
   */
  /*
  private void getVisibleEdgesOnHull(
    Node node, Edge edge, HashSet<Edge> edges) {
    if (!edges.contains(edge) && edge.isVisibleFromNode(node)) {
      edges.add(edge);
      getVisibleEdgesOnHull(node,getNextEdgeOnHull(edge.nodeA(),edge),edges);
      getVisibleEdgesOnHull(node,getNextEdgeOnHull(edge.nodeB(),edge),edges);
    }
  }
  */

  /**
   * Returns the edge in the specified tri that references the specified 
   * nodes. If the tri does not reference the specified nodes, returns null.
   */
  private static Edge edgeOfTri(Tri tri, Node na, Node nb) {
    Node n0 = tri._n0;
    Node n1 = tri._n1;
    Node n2 = tri._n2;
    if (na==n0) {
      if (nb==n1) {
        return new Edge(tri,n2);
      } else if (nb==n2) {
        return new Edge(tri,n1);
      } else {
        return null;
      }
    } else if (na==n1) {
      if (nb==n0) {
        return new Edge(tri,n2);
      } else if (nb==n2) {
        return new Edge(tri,n0);
      } else {
        return null;
      }
    } else if (na==n2) {
      if (nb==n0) {
        return new Edge(tri,n1);
      } else if (nb==n1) {
        return new Edge(tri,n0);
      } else {
        return null;
      }
    } else {
      return null;
    }
  }

  /**
   * Determines whether a specified tri has the specified nodes in order.
   */
  private static boolean nodesInOrder(
    Tri tri, Node na, Node nb, Node nc) 
  {
    Node n0 = tri._n0;
    Node n1 = tri._n1;
    Node n2 = tri._n2;
    return na==n0 && nb==n1 && nc==n2 ||
           na==n1 && nb==n2 && nc==n0 ||
           na==n2 && nb==n0 && nc==n1;
  }

  /**
   * Returns the other (third) node in the specified tri that references the 
   * specified two nodes. If the tri does not reference the specified nodes, 
   * returns null.
   */
  private static Node otherNode(Tri tri, Node na, Node nb) {
    Node n0 = tri._n0;
    Node n1 = tri._n1;
    Node n2 = tri._n2;
    if (na==n0) {
      if (nb==n1) {
        return n2;
      } else if (nb==n2) {
        return n1;
      } else {
        return null;
      }
    } else if (na==n1) {
      if (nb==n0) {
        return n2;
      } else if (nb==n2) {
        return n0;
      } else {
        return null;
      }
    } else if (na==n2) {
      if (nb==n0) {
        return n1;
      } else if (nb==n1) {
        return n0;
      } else {
        return null;
      }
    } else {
      return null;
    }
  }

  /**
   * Marks the specified tri inner or outer.
   * The outer box must valid.
   */
  private synchronized void markTriInnerOrOuter(Tri tri) {
    assert _xminOuter<_xmaxOuter:"outer box is valid";
    assert _yminOuter<_ymaxOuter:"outer box is valid";
    double[] po = {0.0,0.0};
    double s = tri.centerCircle(po);
    double r = sqrt(s);
    double xo = po[0];
    double yo = po[1];
    if (xo-r>=_xminOuter &&
        yo-r>=_yminOuter &&
        xo+r<=_xmaxOuter &&
        yo+r<=_ymaxOuter) {
      tri.setInner();
      tri.clearOuter();
    } else {
      tri.setOuter();
      tri.clearInner();
    }
  }

  private void fireNodeWillBeAdded(Node node) {
    ++_version;
    if (_nnodeListeners>0) {
      Object[] list = _listeners.getListenerList();
      for (int i=list.length-2; i>=0; i-=2)
        if (list[i]==NodeListener.class)
          ((NodeListener)list[i+1]).nodeWillBeAdded(this,node);
    }
  }

  private void fireNodeAdded(Node node) {
    ++_version;
    if (_nnodeListeners>0) {
      Object[] list = _listeners.getListenerList();
      for (int i=list.length-2; i>=0; i-=2)
        if (list[i]==NodeListener.class)
          ((NodeListener)list[i+1]).nodeAdded(this,node);
    }
  }

  private void fireNodeWillBeRemoved(Node node) {
    ++_version;
    if (_nnodeListeners>0) {
      Object[] list = _listeners.getListenerList();
      for (int i=list.length-2; i>=0; i-=2)
        if (list[i]==NodeListener.class)
          ((NodeListener)list[i+1]).nodeWillBeRemoved(this,node);
    }
  }

  private void fireNodeRemoved(Node node) {
    ++_version;
    if (_nnodeListeners>0) {
      Object[] list = _listeners.getListenerList();
      for (int i=list.length-2; i>=0; i-=2)
        if (list[i]==NodeListener.class)
          ((NodeListener)list[i+1]).nodeRemoved(this,node);
    }
  }

  private void fireTriAdded(Tri tri) {
    ++_version;
    if (_ntriListeners>0) {
      Object[] list = _listeners.getListenerList();
      for (int i=list.length-2; i>=0; i-=2)
        if (list[i]==TriListener.class)
          ((TriListener)list[i+1]).triAdded(this,tri);
    }
  }

  private void fireTriRemoved(Tri tri) {
    ++_version;
    if (_ntriListeners>0) {
      Object[] list = _listeners.getListenerList();
      for (int i=list.length-2; i>=0; i-=2)
        if (list[i]==TriListener.class)
          ((TriListener)list[i+1]).triRemoved(this,tri);
    }
  }

  /**
   * Validates the specified node.
   */
  private void validate(Node node) {
    Check.state(node==node._prev._next,"node==node._prev._next");
    Check.state(node==node._next._prev,"node==node._next._prev");
    Tri tri = node.tri();
    if (_troot!=null) {
      Check.state(tri!=null,"tri!=null");
      Check.state(node==tri.nodeA() ||
                  node==tri.nodeB() ||
                  node==tri.nodeC(),
                  "node is one of tri nodes");
    }
  }

  /**
   * Validates the specified tri.
   */
  private void validate(Tri tri) {
    Check.state(tri!=null,"tri not null");
    Node na = tri.nodeA();
    Node nb = tri.nodeB();
    Node nc = tri.nodeC();
    validate(na);
    validate(nb);
    validate(nc);
    Tri ta = tri.triA();
    Tri tb = tri.triB();
    Tri tc = tri.triC();
    if (ta!=null)
      Check.state(ta.triNabor(tri.nodeNabor(ta))==tri,"a nabors ok");
    if (tb!=null)
      Check.state(tb.triNabor(tri.nodeNabor(tb))==tri,"b nabors ok");
    if (tc!=null)
      Check.state(tc.triNabor(tri.nodeNabor(tc))==tri,"c nabors ok");
  }

  /*
  private static void trace(String s) {
    if (TRACE) System.out.println(s);
  }
  */
  static final boolean DEBUG = false;
  static final boolean TRACE = false;


  ///////////////////////////////////////////////////////////////////////////
  // Private classes for internal use only. (Big, so we put them at the end.)

  /**
   * A set of tri edges, specifically tuned for Delaunay tri meshing.
   * <p>
   * In the Bowyer-Watson-like method of Delaunay triangulation
   * used here, star-shaped polygons are constructed and filled by
   * adding edges of non-Delaunay tris to this set. As edges are added,
   * those internal to the star-shaped polygon are matched with their
   * mates and removed from this set, leaving only the Delaunay edges on
   * the polygon border, which is then filled with Delaunay triangles.
   * <p>
   * The edges stored in this set are defined as in the class TriMesh.Edge.
   * although, for efficiency, this set does not store objects of this class.
   * Rather, this set stores the components of an edge (the nodes A, B, and
   * C, and the tri ABC) separately in arrays. This scheme significantly
   * reduces the cost of creating and adding new edges, and then removing
   * them as they are matched by their mates. This scheme also makes this set
   * unsuitable for general use.
   */
  private static class EdgeSet {

    /**
     * The current edge, typically, the edge added, or the mate removed.
     */
    Node a,b,c;
    Tri abc;

    /**
     * Constructs an empty set with specified initial capacity and load factor.
     * The capacity will be rounded up to a power of two. The number of edges
     * that can be added without rehashing is approximately capacity/factor.
     * @param capacity the initial capacity.
     * @param factor the load factor.
     */
    EdgeSet(int capacity, double factor) {
      if (capacity>MAX_CAPACITY) capacity = MAX_CAPACITY;
      if (factor<=0.0) factor = 0.0001;
      if (factor>=1.0) factor = 0.9999;
      for (_nmax=2,_shift=MAX_SHIFT; _nmax<capacity; _nmax*=2)
        --_shift;
      _n = 0;
      _factor = factor;
      _mask = _nmax-1;
      _a = new Node[_nmax];
      _b = new Node[_nmax];
      _c = new Node[_nmax];
      _abc = new Tri[_nmax];
      _filled = new boolean[_nmax];
    }

    /**
     * Clears the set, so that it is empty, without changing its capacity.
     */
    void clear() {
      _n = 0;
      for (int i=0; i<_nmax; ++i)
        _filled[i] = false;
    }

    /**
     * Determines whether this set contains the edge of the specified tri 
     * opposite the specified node. If true, sets the current edge to that
     * found; otherwise, simply returns false.
     * @param tri the tri that references nodes in the edge.
     * @param node the other node in the tri that is not in the edge.
     * @return true, if the edge is in this set; false, otherwise.
     */
    boolean contains(Tri tri, Node node) {
      if (node==tri._n0) {
        _index = indexOfMate(tri._n2,tri._n1); // index of edge 1->2
      } else if (node==tri._n1) {
        _index = indexOfMate(tri._n0,tri._n2); // index of edge 2->0
      } else if (node==tri._n2) {
        _index = indexOfMate(tri._n1,tri._n0); // index of edge 0->1
      } else {
        assert false:"node is referenced by tri";
        return false;
      }
      if (_filled[_index]) {
        setCurrent();
        return true;
      }
      return false;
    }

    /**
     * Adds the edge of the specified tri that is opposite the specified node,
     * unless its mate is in the set, in which case that mate is removed.
     * Sets the current edge to the edge added or the mate removed.
     * @param tri the tri that references nodes in the edge.
     * @param node the other node in the tri that is not in the edge.
     * @return true, if the edge was added; false, if the mate was removed.
     */
    boolean add(Tri tri, Node node) {
      if (node==tri._n0) {
        return add(tri._n1,tri._n2,node,tri);
      } else if (node==tri._n1) {
        return add(tri._n2,tri._n0,node,tri);
      } else if (node==tri._n2) {
        return add(tri._n0,tri._n1,node,tri);
      } else {
        assert false:"node is referenced by tri";
        return false;
      }
    }

    /**
     * Adds the mate of the edge of the specified tri that is opposite
     * the specified node, unless the edge is in the set, in which case
     * that edge is removed. Sets the current edge to the mate added or
     * the edge removed.
     * @param tri the tri that references the nodes in the edge.
     * @param node the other node in the tri that is not in the edge.
     * @return true, if the mate was added; false, if the edge was removed.
     */
    boolean addMate(Tri tri, Node node) {
      Tri triNabor = tri.triNabor(node);
      Node nodeNabor = (triNabor!=null)?tri.nodeNabor(triNabor):null;
      if (node==tri._n0) {
        return add(tri._n2,tri._n1,nodeNabor,triNabor);
      } else if (node==tri._n1) {
        return add(tri._n0,tri._n2,nodeNabor,triNabor);
      } else if (node==tri._n2) {
        return add(tri._n1,tri._n0,nodeNabor,triNabor);
      } else {
        assert false:"node is referenced by tri";
        return false;
      }
    }

    /**
     * If the set is not empty, removes an edge from the set.
     * Sets the current edge to the edge removed.
     * @return true, if the set was not empty; false, otherwise.
     */
    boolean remove() {
      if (_n>0) {
        int start = _index;
        for (; _index<_nmax; ++_index) {
          if (_filled[_index]) {
            setCurrent();
            remove(_index);
            return true;
          }
        }
        for (_index=0; _index<start; ++_index) {
          if (_filled[_index]) {
            setCurrent();
            remove(_index);
            return true;
          }
        }
      }
      return false;
    }

    /**
     * Determines whether this set is empty.
     * @return true, if empty; false, otherwise.
     */
    boolean isEmpty() {
      return _n>0;
    }

    /**
     * Returns the number of edges in this set.
     * @return the number of edges.
     */
    int count() {
      return _n;
    }

    /**
     * Sets the current edge to the first edge in the set.
     * <em>While iterating over edges, the set should not be modified.</em>
     * @return true, if the set is not empty; false, otherwise.
     */
    boolean first() {
      _index = -1;
      return next();
    }

    /**
     * Sets the current edge to the next edge in the set.
     * <em>While iterating over edges, the set should not be modified.</em>
     * @return true, if the set contains another edge; false, otherwise.
     */
    boolean next() {
      for (++_index; _index<_nmax; ++_index) {
        if (_filled[_index]) {
          setCurrent();
          return true;
        }
      }
      return false;
    }

    private static final int MAX_SHIFT = 30; // max bit shift.
    private static final int MAX_CAPACITY = 1<<MAX_SHIFT; // 2^MAX_SHIFT

    private Node[] _a,_b,_c; // arrays of nodes.
    private Tri[] _abc; // array of tris.
    private boolean[] _filled; // _filled[i]==true if i'th entry is used.
    private int _nmax; // 0 <= _nmax = 2^nbits <= MAX_CAPACITY
    private int _n; // 0 <= _n <= _nmax
    private double _factor; // 0.0001 <= _factor <= 0.9999
    private int _shift; // _shift = 31 - nbits
    private int _mask; // _mask = _nmax - 1
    private int _index; // index of recent add, remove, etc.

    /**
     * Hash scatter function. For the specified nodes, this function
     * produces a pseudo-random int in the range [0:nmax-1].
     */
    private int hash(Node a, Node b) {
      int key = a._hash^b._hash;
      // Knuth, v. 3, 509-510. Randomize the 31 low-order bits of c*key
      // and return the highest nbits (where nbits <= 30) bits of these.
      // The constant c = 1327217885 approximates 2^31 * (sqrt(5)-1)/2.
      return ((1327217885*key)>>_shift)&_mask;
    }

    /**
     * Returns the index corresponding to the mate of the specified edge,
     * or, if the mate is not found, the index of an empty slot in the set.
     */
    private int indexOfMate(Node a, Node b) {
      int i = hash(a,b);
      while (_filled[i]) {
        if (a==_b[i] && b==_a[i])
          return i;
        i = (i-1)&_mask;
      }
      return i;
    }

    /**
     * Sets the current edge.
     */
    private void setCurrent() {
      this.a = _a[_index];
      this.b = _b[_index];
      this.c = _c[_index];
      this.abc = _abc[_index];
    }

    /**
     * If the set does not contain the mate of the specified edge, adds
     * the edge to the set, remembers the edge added, and returns true.
     * Otherwise, if the set already contains the mate, removes the mate
     * from the set, remembers the mate removed, and returns false.
     */
    private boolean add(Node a, Node b, Node c, Tri abc) {
      _index = indexOfMate(a,b);
      if (_filled[_index]) {
        setCurrent();
        remove(_index);
        return false;
      } else {
        _a[_index] = a;
        _b[_index] = b;
        _c[_index] = c;
        _abc[_index] = abc;
        _filled[_index] = true;
        ++_n;
        if (_n>_nmax*_factor && _nmax<MAX_CAPACITY)
          doubleCapacity();
        setCurrent();
        return true;
      }
    }

    /**
     * Removes the edge with specified index.
     */
    private void remove(int i) {
      // Knuth, v. 3, 527, Algorithm R.
      --_n;
      for (;;) {
        _filled[i] = false;
        int j = i;
        int r;
        do {
          i = (i-1)&_mask;
          if (!_filled[i])
            return;
          r = hash(_a[i],_b[i]);
        } while ((i<=r && r<j) || (r<j && j<i) || (j<i && i<=r));
        _a[j] = _a[i];
        _b[j] = _b[i];
        _c[j] = _c[i];
        _abc[j] = _abc[i];
        _filled[j] = _filled[i];
      }
    }

    /**
     * Doubles the capacity of the set.
     */
    private void doubleCapacity() {
      //trace("EdgeSet.doubleCapacity");
      EdgeSet set = new EdgeSet(2*_nmax,_factor);
      if (_n>0) {
        for (int i=0; i<_nmax; ++i) {
          if (_filled[i])
            set.add(_a[i],_b[i],_c[i],_abc[i]);
        }
      }
      _a = set._a;
      _b = set._b;
      _c = set._c;
      _abc = set._abc;
      _filled = set._filled;
      _nmax = set._nmax;
      _n = set._n;
      _factor = set._factor;
      _shift = set._shift;
      _mask = set._mask;
      _index = set._index;
    }
  }

  /**
   * A set of tri nodes, used with an edge set in Delaunay tri meshing.
   * A node set works like an edge set, but supports fewer operations.
   * Although the edge set alone is sufficient for meshing, the node
   * set improves the efficiency of the method addNode; nodes can be
   * added/removed faster than edges.
   */
  private static class NodeSet {

    /**
     * The current node; typically, the node added or removed.
     */
    Node a,b;
    Tri nba;

    /**
     * Constructs an empty set with specified initial capacity and load factor.
     * The capacity will be rounded up to a power of two. The number of edges
     * that can be added without rehashing is approximately capacity/factor.
     * @param capacity the initial capacity.
     * @param factor the load factor.
     */
    NodeSet(int capacity, double factor) {
      if (capacity>MAX_CAPACITY) capacity = MAX_CAPACITY;
      if (factor<=0.0) factor = 0.0001;
      if (factor>=1.0) factor = 0.9999;
      for (_nmax=2,_shift=MAX_SHIFT; _nmax<capacity; _nmax*=2)
        --_shift;
      _n = 0;
      _factor = factor;
      _mask = _nmax-1;
      _a = new Node[_nmax];
      _b = new Node[_nmax];
      _nba = new Tri[_nmax];
      _filled = new boolean[_nmax];
    }

    /**
     * Clears the set, so that it is empty, without changing its capacity.
     */
    void clear() {
      _n = 0;
      for (int i=0; i<_nmax; ++i)
        _filled[i] = false;
    }

    /**
     * If the set does not contain the specified node, adds the node to
     * the set, remembers the node added, and returns true.
     * Otherwise, if the set already contains the node, removes the node
     * from the set, remembers the node removed, and returns false.
     */
    boolean add(Node a, Node b, Tri nba) {
      _index = indexOfNode(a);
      if (_filled[_index]) {
        setCurrent();
        remove(_index);
        return false;
      } else {
        _a[_index] = a;
        _b[_index] = b;
        _nba[_index] = nba;
        _filled[_index] = true;
        ++_n;
        if (_n>_nmax*_factor && _nmax<MAX_CAPACITY)
          doubleCapacity();
        setCurrent();
        return true;
      }
    }

    private static final int MAX_SHIFT = 30; // max bit shift.
    private static final int MAX_CAPACITY = 1<<MAX_SHIFT; // 2^MAX_SHIFT

    private Node[] _a,_b; // arrays of nodes.
    private Tri[] _nba; // array of tris.
    private boolean[] _filled; // _filled[i]==true if i'th entry is used.
    private int _nmax; // 0 <= _nmax = 2^nbits <= MAX_CAPACITY
    private int _n; // 0 <= _n <= _nmax
    private double _factor; // 0.0001 <= _factor <= 0.9999
    private int _shift; // _shift = 31 - nbits
    private int _mask; // _mask = _nmax - 1
    private int _index; // index of recent add, remove, etc.

    /**
     * Hash scatter function. For the specified node, this function
     * produces a pseudo-random int in the range [0:nmax-1].
     */
    private int hash(Node a) {
      int key = a._hash;
      // Knuth, v. 3, 509-510. Randomize the 31 low-order bits of c*key
      // and return the highest nbits (where nbits <= 30) bits of these.
      // The constant c = 1327217885 approximates 2^31 * (sqrt(5)-1)/2.
      return ((1327217885*key)>>_shift)&_mask;
    }

    /**
     * Returns the index corresponding to the specified node, or, if the
     * node is not found, the index of an empty slot in the set.
     */
    private int indexOfNode(Node a) {
      int i = hash(a);
      while (_filled[i]) {
        if (a==_a[i])
          return i;
        i = (i-1)&_mask;
      }
      return i;
    }

    /**
     * Sets the current node.
     */
    private void setCurrent() {
      this.a = _a[_index];
      this.b = _b[_index];
      this.nba = _nba[_index];
    }

    /**
     * Removes the node with specified index.
     */
    private void remove(int i) {
      // Knuth, v. 3, 527, Algorithm R.
      --_n;
      for (;;) {
        _filled[i] = false;
        int j = i;
        int r;
        do {
          i = (i-1)&_mask;
          if (!_filled[i])
            return;
          r = hash(_a[i]);
        } while ((i<=r && r<j) || (r<j && j<i) || (j<i && i<=r));
        _a[j] = _a[i];
        _b[j] = _b[i];
        _nba[j] = _nba[i];
        _filled[j] = _filled[i];
      }
    }

    /**
     * Doubles the capacity of the set.
     */
    private void doubleCapacity() {
      //trace("EdgeSet.doubleCapacity");
      NodeSet set = new NodeSet(2*_nmax,_factor);
      if (_n>0) {
        for (int i=0; i<_nmax; ++i) {
          if (_filled[i])
            set.add(_a[i],_b[i],_nba[i]);
        }
      }
      _a = set._a;
      _b = set._b;
      _nba = set._nba;
      _filled = set._filled;
      _nmax = set._nmax;
      _n = set._n;
      _factor = set._factor;
      _shift = set._shift;
      _mask = set._mask;
      _index = set._index;
    }
  }

  @SuppressWarnings("unchecked")
  private void readObject(ObjectInputStream in)
    throws IOException, ClassNotFoundException 
  {
    // Initialize as in no-arg constructor.
    init();

    // Input format.
    int format = in.readInt();
    if (format==1) {

      // Mesh version.
      _version = in.readLong();

      // Nodes.
      int nnode = _nnode = in.readInt();
      Node[] nodes = new Node[nnode];
      for (int inode=0; inode<nnode; ++inode) {
        Node node = nodes[inode] = (Node)in.readObject();
        node._x = in.readDouble();
        node._y = in.readDouble();
        int nvalue = in.readInt();
        node._values = new Object[nvalue];
        for (int ivalue=0; ivalue<nvalue; ++ivalue) {
          Object value = in.readObject();
          node._values[ivalue] = value;
        }
      }

      // Tris.
      int ntri = _ntri = in.readInt();
      Tri[] tris = new Tri[ntri];
      for (int itri=0; itri<ntri; ++itri) {
        Tri tri = tris[itri] = (Tri)in.readObject();
        tri._quality = -1.0;
      }

      // Nodes linkage.
      _nroot = (Node)in.readObject();
      for (int inode=0; inode<nnode; ++inode) {
        Node node = nodes[inode];
        node._prev = (Node)in.readObject();
        node._next = (Node)in.readObject();
        node._tri = (Tri)in.readObject();
      }

      // Tris linkage.
      _troot = (Tri)in.readObject();
      for (int itri=0; itri<ntri; ++itri) {
        Tri tri = tris[itri];
        tri._n0 = (Node)in.readObject();
        tri._n1 = (Node)in.readObject();
        tri._n2 = (Node)in.readObject();
        tri._t0 = (Tri)in.readObject();
        tri._t1 = (Tri)in.readObject();
        tri._t2 = (Tri)in.readObject();
      }

      // Outer box.
      _outerEnabled = in.readBoolean();
      _xminOuter = in.readDouble();
      _yminOuter = in.readDouble();
      _xmaxOuter = in.readDouble();
      _ymaxOuter = in.readDouble();

      // Property maps.
      _nnodeValues = in.readInt();
      _lnodeValues = in.readInt();
      _nodePropertyMaps = (Map<String,NodePropertyMap>)in.readObject();

  //} else if (format==2) {
  //  ...
    } else {
      throw new InvalidClassException("invalid external format");
    }

    // Sample nodes.
    sampleNodes();

    // Ensure mesh is valid.
    try {
      validate();
    } catch (IllegalStateException ise) {
      throw new IOException(ise.getMessage());
    }
  }

  private void writeObject(ObjectOutputStream out)
    throws IOException 
  {
    
    // Output format.
    out.writeInt(1);

    // Mesh version.
    out.writeLong(_version);

    // Nodes.
    int nnode = _nnode;
    out.writeInt(nnode);
    Node[] nodes = new Node[nnode];
    NodeIterator ni = getNodes();
    for (int inode=0; inode<nnode; ++inode) {
      Node node = nodes[inode] = ni.next();
      out.writeObject(node);
      out.writeDouble(node._x);
      out.writeDouble(node._y);
      int nvalue = node._values.length;
      out.writeInt(nvalue);
      for (int ivalue=0; ivalue<nvalue; ++ivalue) {
        Object value = node._values[ivalue];
        out.writeObject((value instanceof Serializable)?value:null);
      }
    }

    // Tris.
    int ntri = _ntri;
    out.writeInt(ntri);
    Tri[] tris = new Tri[ntri];
    TriIterator ti = getTris();
    for (int itri=0; itri<ntri; ++itri) {
      Tri tri = tris[itri] = ti.next();
      out.writeObject(tri);
    }

    // Nodes linkage.
    out.writeObject(_nroot);
    for (int inode=0; inode<nnode; ++inode) {
      Node node = nodes[inode];
      out.writeObject(node._prev);
      out.writeObject(node._next);
      out.writeObject(node._tri);
    }

    // Tris linkage.
    out.writeObject(_troot);
    for (int itri=0; itri<ntri; ++itri) {
      Tri tri = tris[itri];
      out.writeObject(tri._n0);
      out.writeObject(tri._n1);
      out.writeObject(tri._n2);
      out.writeObject(tri._t0);
      out.writeObject(tri._t1);
      out.writeObject(tri._t2);
    }

    // Outer box.
    out.writeBoolean(_outerEnabled);
    out.writeDouble(_xminOuter);
    out.writeDouble(_yminOuter);
    out.writeDouble(_xmaxOuter);
    out.writeDouble(_ymaxOuter);

    // Property maps and values.
    out.writeInt(_nnodeValues);
    out.writeInt(_lnodeValues);
    out.writeObject(_nodePropertyMaps);
  }

  /**
   * Randomly samples nodes to facilitate fast searches. Following
   * Mucke et al., 1996, the number of samples is proportional to
   * N^(1/3), where N equals the number of nodes. The factor of 0.45
   * was used by Shewchuk, 1997.
   */
  private void sampleNodes() {
    Random random = new Random();
    _sampledNodes.clear();
    int nsamp = (int)(pow(_nnode,0.33)/0.45);
    Node node = _nroot;
    while (_sampledNodes.size()<nsamp) {
      int nskip = 1+random.nextInt(_nnode/2);
      while (--nskip>0)
        node = node._next;
      _sampledNodes.add(node);
    }
  }
}
