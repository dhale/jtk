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
 * A tetrahedral mesh.
 * <p>
 * Each tet in the mesh references four nodes. Depending on the context,
 * these four nodes are labelled as 0, 1, 2, and 3, or A, B, C, D. The 
 * nodes are ordered such that 1, 2, and 3 (or B, C, and D) are in
 * counter-clockwise (CCW) order as viewed from node 0 (or A).
 * Here is a picture:
 * <pre><code>
 *
 *          2(C)
 *          *
 *         /|\
 *        / |  \
 *  0(A)*---|---* 3(D)
 *       \  |  /
 *        \ | /
 *         \|/
 *          *
 *         1(B) 
 *
 * </code></pre>
 * Each tet has up to four neighbors (nabors), corresponding to the four 
 * faces of the tet. Tets on the convex hull of the mesh have one or more 
 * null nabors. Each nabor is labelled by the node opposite its face. For
 * example, the tet nabor 0 (or A) is opposite the node 0 (or A).
 * <p>
 * Faces and edges of each tet are enumerated as follows.
 * The four oriented faces of each tet are ABC|D, BDC|A, CDA|B, 
 * and DBA|C, where the fourth node in each group is referenced
 * by the tet, but not the face, and is left of the plane 
 * defined by the first three nodes.
 * Likewise, the six directed edges of each tet are AB|CD, AC|DB, 
 * AD|BC, BC|AD, BD|CA, and CD|AB, where the third and fourth 
 * nodes in each group are referenced by the tet, but not the 
 * edge, and the fourth node is left of the plane defined by the 
 * first three nodes.
 * <p>
 * Nodes are constructed with float coordinates that are stored internally 
 * as perturbed doubles. This perturbation minimizes the likelihood that 
 * four or more nodes are exactly co-planar, or that five or more nodes 
 * lie exactly on the circumsphere of any tet in the mesh. Only the least 
 * significant bits of the double coordinates are altered, so that casting 
 * the perturbed doubles to floats always yields the float coordinates 
 * with which nodes are constructed.
 * <p>
 * A tet mesh is serializable. When written to an object output stream,
 * a tet mesh writes its nodes and tets so that any references to them 
 * from serialized objects not in the mesh will remain valid.
 * <p>
 * While mesh nodes and tets are serializable alone, most of their state 
 * is serialized only when the entire mesh is serialized.
 * <p>
 * Listeners to a tet mesh are not serialized. When a tet mesh is read
 * from an object input stream, it will have no listeners.
 * 
 * @author Dave Hale, Colorado School of Mines
 * @version 2003.08.21, 2006.08.02
 */
public class TetMesh implements Serializable {
  private static final long serialVersionUID = 1L;

  /**
   * A node, which may be added or removed from the mesh.
   * <p>
   * A node is serializable, but all of its private fields are transient.
   * Those fields are serialized only when the mesh is serialized. Public 
   * fields are not transient, and so are valid without the mesh.
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
     * @param z the z coordinate.
     */
    public Node(float x, float y, float z) {
      _prev = null;
      _next = null;
      _tet = null;
      _mark = 0;
      _hash = System.identityHashCode(this);
      setPosition(x,y,z);
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
     * Returns the z coordinate of this node.
     * @return the z coordinate.
     */
    public final float z() {
      return (float)_z;
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
     * Returns the z coordinate of this node as a perturbed double.
     * @return the z coordinate.
     */
    public final double zp() { 
      return _z; 
    }

    /**
     * Returns a tet that references this node.
     * @return the tet; null, if this node is not in the mesh.
     */
    public final Tet tet() {
      return _tet;
    }

    public String toString() {
      return "("+x()+","+y()+","+z()+")";
    }

    // The outer class handles serialization of all private fields.
    private transient double _x,_y,_z;
    private transient Node _prev,_next;
    private transient Tet _tet;
    private transient int _mark;
    private transient int _hash;
    private transient Object[] _values;

    /**
     * Perturbs a float into a double with pseudo-random least-significant bits.
     * Perturbation helps prevent degeneracies in Delaunay tetrahedralization.
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
    private void setPosition(float x, float y, float z) {
      assert _tet==null;
      _x = perturb(x,0.450599f*y+0.374507f*z);
      _y = perturb(y,0.298721f*x+0.983298f*z);
      _z = perturb(z,0.653901f*x+0.598723f*y);
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
   * One tetrahedron (tet) in the mesh.
   * Each tet references four nodes (A, B, C, and D), and up to four
   * tet neighbors (nabors) opposite those nodes. A null nabor denotes a
   * triangular face (opposite the corresponding node) on the mesh boundary.
   * As viewed from node A, the nodes B, C, and D are in CCW order.
   * <p>
   * A tet is serializable, but all of its private fields are transient.
   * Those fields are serialized only when the mesh is serialized. Public 
   * fields are not transient, and so are valid without the mesh.
   */
  public static class Tet implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * An integer index associated with this tet.
     * Intended for external use only; the mesh does not use it.
     */
    public int index;

    /**
     * A data object associated with this tet.
     * Intended for external use only; the mesh does not use it.
     */
    public Object data;

    /**
     * Returns the node A referenced by this tet.
     * @return the node A.
     */
    public final Node nodeA() {
      return _n0;
    }

    /**
     * Returns the node B referenced by this tet.
     * @return the node B.
     */
    public final Node nodeB() {
      return _n1;
    }

    /**
     * Returns the node C referenced by this tet.
     * @return the node C.
     */
    public final Node nodeC() {
      return _n2;
    }

    /**
     * Returns the node D referenced by this tet.
     * @return the node D.
     */
    public final Node nodeD() {
      return _n3;
    }

    /**
     * Returns the tet nabor A (opposite node A) referenced by this tet.
     * @return the tet nabor A.
     */
    public final Tet tetA() {
      return _t0;
    }

    /**
     * Returns the tet nabor B (opposite node B) referenced by this tet.
     * @return the tet nabor B.
     */
    public final Tet tetB() {
      return _t1;
    }

    /**
     * Returns the tet nabor C (opposite node C) referenced by this tet.
     * @return the tet nabor C.
     */
    public final Tet tetC() {
      return _t2;
    }

    /**
     * Returns the tet nabor D (opposite node D) referenced by this tet.
     * @return the tet nabor D.
     */
    public final Tet tetD() {
      return _t3;
    }

    /**
     * Returns the node referenced by this tet that is nearest to 
     * the point with specified coordinates.
     * @param x the x coordinate.
     * @param y the y coordinate.
     * @param z the z coordinate.
     * @return the node nearest to the point (x,y,z).
     */
    public final Node nodeNearest(float x, float y, float z) {
      double d0 = distanceSquared(_n0,x,y,z);
      double d1 = distanceSquared(_n1,x,y,z);
      double d2 = distanceSquared(_n2,x,y,z);
      double d3 = distanceSquared(_n3,x,y,z);
      double dmin = d0;
      Node nmin = _n0;
      if (d1<dmin) {
        dmin = d1;
        nmin = _n1;
      }
      if (d2<dmin) {
        dmin = d2;
        nmin = _n2;
      }
      if (d3<dmin) {
        nmin = _n3;
      }
      return nmin;
    }

    /**
     * Returns the tet nabor opposite the specified node.
     * The specified node cannot be null, and must be referenced by this tet.
     */
    public final Tet tetNabor(Node node) {
      if (node==_n0) return _t0;
      if (node==_n1) return _t1;
      if (node==_n2) return _t2;
      if (node==_n3) return _t3;
      Check.argument(false,"node is referenced by tet");
      return null;
    }

    /**
     * Returns the node in the specified tet nabor that is opposite this tet.
     * The specified tet nabor cannot be null, and must be referenced by 
     * this tet.
     */
    public final Node nodeNabor(Tet tetNabor) {
      if (tetNabor._t0==this) return tetNabor._n0;
      if (tetNabor._t1==this) return tetNabor._n1;
      if (tetNabor._t2==this) return tetNabor._n2;
      if (tetNabor._t3==this) return tetNabor._n3;
      Check.argument(false,"tetNabor is a nabor of tet");
      return null;
    }

    /**
     * Returns the node opposite this tet in the tet nabor that is opposite 
     * the specified node. The specified node cannot be null. If the tet 
     * nabor opposite the specified node is null, then null is returned.
     */
    public final Node nodeNabor(Node node) {
      Tet tetNabor = tetNabor(node);
      return (tetNabor!=null)?nodeNabor(tetNabor):null;
    }

    /**
     * Computes the circumcenter of this tet.
     * @param c array of circumcenter coordinates {xc,yc,zc}.
     * @return radius-squared of circumsphere.
     */
    public double centerSphere(double[] c) {
      if (hasCenter()) {
        c[0] = _xc;
        c[1] = _yc;
        c[2] = _zc;
      } else {
        double x0 = _n0._x;
        double y0 = _n0._y;
        double z0 = _n0._z;
        double x1 = _n1._x;
        double y1 = _n1._y;
        double z1 = _n1._z;
        double x2 = _n2._x;
        double y2 = _n2._y;
        double z2 = _n2._z;
        double x3 = _n3._x;
        double y3 = _n3._y;
        double z3 = _n3._z;
        Geometry.centerSphere(x0,y0,z0,x1,y1,z1,x2,y2,z2,x3,y3,z3,c);
        setCenter(c[0],c[1],c[2]);
      }
      double dx = _xc-_n3._x;
      double dy = _yc-_n3._y;
      double dz = _zc-_n3._z;
      return dx*dx+dy*dy+dz*dz;
    }

    /**
     * Returns the circumcenter of this tet.
     * @return array of circumcenter coordinates {xc,yc,zc}.
     */
    public double[] centerSphere() {
      double[] c = new double[3];
      centerSphere(c);
      return c;
    }

    /**
     * Returns the quality of this tet.
     * Quality is a number between 0 and 1, inclusive.
     * Quality equals 1 for equilateral tets.
     * Quality equals 0 for degenerate tets with co-planar nodes.
     * @return tet quality.
     */
    public double quality() {
      if (_quality<0.0)
        _quality = quality(_n0,_n1,_n2,_n3);
      return _quality;
    }

    /**
     * Determines whether this tet references the specified node.
     * @param node the node.
     * @return true, if this tet references the node; false, otherwise.
     */
    public boolean references(Node node) {
      return node==_n0 || node==_n1 || node==_n2 ||node==_n3;
    }

    /**
     * Determines whether this tet references the specified nodes.
     * @param na a node.
     * @param nb a node.
     * @return true, if this tet references the nodes; false, otherwise.
     */
    public boolean references(Node na, Node nb) {
      if (na==_n0) {
        return nb==_n1 || nb==_n2 || nb==_n3;
      } else if (na==_n1) {
        return nb==_n0 || nb==_n2 || nb==_n3;
      } else if (na==_n2) {
        return nb==_n0 || nb==_n1 || nb==_n3;
      } else if (na==_n3) {
        return nb==_n0 || nb==_n1 || nb==_n2;
      } else {
        return false;
      }
    }

    /**
     * Determines whether this tet references the specified nodes.
     * @param na a node.
     * @param nb a node.
     * @param nc a node.
     * @return true, if this tet references the nodes; false, otherwise.
     */
    public boolean references(Node na, Node nb, Node nc) {
      if (na==_n0) {
        if (nb==_n1) {
          return nc==_n2 || nc==_n3;
        } else if (nb==_n2) {
          return nc==_n1 || nc==_n3;
        } else if (nb==_n3) {
          return nc==_n1 || nc==_n2;
        } else {
          return false;
        }
      } else if (na==_n1) {
        if (nb==_n0) {
          return nc==_n2 || nc==_n3;
        } else if (nb==_n2) {
          return nc==_n0 || nc==_n3;
        } else if (nb==_n3) {
          return nc==_n0 || nc==_n2;
        } else {
          return false;
        }
      } else if (na==_n2) {
        if (nb==_n0) {
          return nc==_n1 || nc==_n3;
        } else if (nb==_n1) {
          return nc==_n0 || nc==_n3;
        } else if (nb==_n3) {
          return nc==_n0 || nc==_n1;
        } else {
          return false;
        }
      } else if (na==_n3) {
        if (nb==_n0) {
          return nc==_n1 || nc==_n2;
        } else if (nb==_n1) {
          return nc==_n0 || nc==_n2;
        } else if (nb==_n2) {
          return nc==_n0 || nc==_n1;
        } else {
          return false;
        }
      } else {
        return false;
      }
    }

    /**
     * Determines whether this tet references the specified nodes.
     * @param na a node.
     * @param nb a node.
     * @param nc a node.
     * @param nd a node.
     * @return true, if this tet references the nodes; false, otherwise.
     */
    public boolean references(Node na, Node nb, Node nc, Node nd) {
      if (na==_n0) {
        if (nb==_n1) {
          if (nc==_n2) {
            return nd==_n3;
          } else if (nc==_n3) {
            return nd==_n2;
          } else {
            return false;
          }
        } else if (nb==_n2) {
          if (nc==_n1) {
            return nd==_n3;
          } else if (nc==_n3) {
            return nd==_n1;
          } else {
            return false;
          }
        } else if (nb==_n3) {
          if (nc==_n1) {
            return nd==_n2;
          } else if (nc==_n2) {
            return nd==_n1;
          } else {
            return false;
          }
        } else {
          return false;
        }
      } else if (na==_n1) {
        if (nb==_n0) {
          if (nc==_n2) {
            return nd==_n3;
          } else if (nc==_n3) {
            return nd==_n2;
          } else {
            return false;
          }
        } else if (nb==_n2) {
          if (nc==_n0) {
            return nd==_n3;
          } else if (nc==_n3) {
            return nd==_n0;
          } else {
            return false;
          }
        } else if (nb==_n3) {
          if (nc==_n0) {
            return nd==_n2;
          } else if (nc==_n2) {
            return nd==_n0;
          } else {
            return false;
          }
        } else {
          return false;
        }
      } else if (na==_n2) {
        if (nb==_n0) {
          if (nc==_n1) {
            return nd==_n3;
          } else if (nc==_n3) {
            return nd==_n1;
          } else {
            return false;
          }
        } else if (nb==_n1) {
          if (nc==_n0) {
            return nd==_n3;
          } else if (nc==_n3) {
            return nd==_n0;
          } else {
            return false;
          }
        } else if (nb==_n3) {
          if (nc==_n0) {
            return nd==_n1;
          } else if (nc==_n1) {
            return nd==_n0;
          } else {
            return false;
          }
        } else {
          return false;
        }
      } else if (na==_n3) {
        if (nb==_n0) {
          if (nc==_n1) {
            return nd==_n2;
          } else if (nc==_n2) {
            return nd==_n1;
          } else {
            return false;
          }
        } else if (nb==_n1) {
          if (nc==_n0) {
            return nd==_n2;
          } else if (nc==_n2) {
            return nd==_n0;
          } else {
            return false;
          }
        } else if (nb==_n2) {
          if (nc==_n0) {
            return nd==_n1;
          } else if (nc==_n1) {
            return nd==_n0;
          } else {
            return false;
          }
        } else {
          return false;
        }
      } else {
        return false;
      }
    }

    /**
     * Inner bit. If set, this tet is inner (and outer bit is clear).
     * If both inner and outer bits are clear, then status is unknown.
     */
    private static final int INNER_BIT = 1;

    /**
     * Outer bit. If set, this tet is outer (and inner bit is clear).
     * If both inner and outer bits are clear, then status is unknown.
     */
    private static final int OUTER_BIT = 2;

    /**
     * Center bit. If set, then the center for this tet is valid.
     */
    private static final int CENTER_BIT = 4;

    /**
     * Nodes referenced by this tet.
     */
    private transient Node _n0,_n1,_n2,_n3;

    /**
     * Tet nabors; null, for tets on the hull.
     */
    private transient Tet _t0,_t1,_t2,_t3;

    /**
     * For recursively visiting tets.
     */
    private transient int _mark = 0;

    /**
     * Useful bits.
     */
    private transient int _bits = 0;

    /**
     * Quality of tet.
     */
    private transient double _quality = -1.0;
    
    /**
     * Coordinates of tet's center (typically, the circumcenter).
     */
    private transient double _xc,_yc,_zc;

    /**
     * Constructs a new tet.
     * The nodes n1, n2, and n3 must be in CCW order as viewed from n0.
     * Alternatively, the nodes n0, n1, and n2 must be in CW order as
     * viewed from n3.
     */
    private Tet(Node n0, Node n1, Node n2, Node n3) {
      init(n0,n1,n2,n3);
    }
    
    /**
     * Initializes a tet, as in the constructor.
     * For efficiency, the quality of the tet is computed lazily.
     */
    private void init(Node n0, Node n1, Node n2, Node n3) {
      if (DEBUG) {
        double orient = Geometry.leftOfPlane(
          n0._x,n0._y,n0._z,
          n1._x,n1._y,n1._z,
          n2._x,n2._y,n2._z,
          n3._x,n3._y,n3._z);
        if (orient<=0.0) {
          trace("orient="+orient);
          trace("n0=("+n0._x+","+n0._y+","+n0._z+")");
          trace("n1=("+n1._x+","+n1._y+","+n1._z+")");
          trace("n2=("+n2._x+","+n2._y+","+n2._z+")");
          trace("n3=("+n3._x+","+n3._y+","+n3._z+")");
        }
        Check.argument(orient>0.0,"orient="+orient+" is greater than zero");
      }
      _n0 = n0;
      _n1 = n1;
      _n2 = n2;
      _n3 = n3;
      _n0._tet = this;
      _n1._tet = this;
      _n2._tet = this;
      _n3._tet = this;
      _t0 = null;
      _t1 = null;
      _t2 = null;
      _t3 = null;
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

    private void setCenter(double xc, double yc, double zc) {
      _xc = xc;
      _yc = yc;
      _zc = zc;
      _bits |= CENTER_BIT;
    }

    private boolean hasCenter() {
      return (_bits&CENTER_BIT)!=0;
    }

    /**
     * Determines if this tet intersects the specified plane.
     */
    private boolean intersectsPlane(double a, double b, double c, double d) 
    {
      int nn = 0;
      int np = 0;
      double s0 = a*_n0._x+b*_n0._y+c*_n0._z+d;
      if (s0<0.0) ++nn;
      if (s0>0.0) ++np;
      double s1 = a*_n1._x+b*_n1._y+c*_n1._z+d;
      if (s1<0.0) ++nn;
      if (s1>0.0) ++np;
      double s2 = a*_n2._x+b*_n2._y+c*_n2._z+d;
      if (s2<0.0) ++nn;
      if (s2>0.0) ++np;
      double s3 = a*_n3._x+b*_n3._y+c*_n3._z+d;
      if (s3<0.0) ++nn;
      if (s3>0.0) ++np;
      return nn<4 && np<4;
    }

    /**
     * Returns the quality of a tet implied by the specified nodes, where 
     * quality is one of the measures defined below, with range [0:1]. 
     * The nodes need not be in any standard order.
     */
    private static double quality(Node na, Node nb, Node nc, Node nd) {
      //return qualityInRadiusOverCircumRadius(na,nb,nc,nd);
      return qualityVolumeOverLongestEdge(na,nb,nc,nd);
    }

    /**
     * Returns the quality of a tet implied by the specified nodes, where 
     * quality is 12/sqrt(2) times the volume/(longest edge) ratio, with
     * range [0:1]. The nodes need not be in any standard order.
     */
    private static double qualityVolumeOverLongestEdge(
      Node na, Node nb, Node nc, Node nd)
    {
      double xa = na._x;
      double ya = na._y;
      double za = na._z;
      double xb = nb._x;
      double yb = nb._y;
      double zb = nb._z;
      double xc = nc._x;
      double yc = nc._y;
      double zc = nc._z;
      double xd = nd._x;
      double yd = nd._y;
      double zd = nd._z;

      // Edges.
      double xab = xa-xb;
      double yab = ya-yb;
      double zab = za-zb;
      double xac = xa-xc;
      double yac = ya-yc;
      double zac = za-zc;
      double xbc = xb-xc;
      double ybc = yb-yc;
      double zbc = zb-zc;
      double xad = xa-xd;
      double yad = ya-yd;
      double zad = za-zd;
      double xbd = xb-xd;
      double ybd = yb-yd;
      double zbd = zb-zd;
      double xcd = xc-xd;
      double ycd = yc-yd;
      double zcd = zc-zd;

      // Determininant = 6 * volume of tet.
      double det = xad*(ybd*zcd-zbd*ycd) +
                   xbd*(ycd*zad-zcd*yad) +
                   xcd*(yad*zbd-zad*ybd);
      
      // Edge lengths squared.
      double dab = xab*xab+yab*yab+zab*zab;
      double dac = xac*xac+yac*yac+zac*zac;
      double dbc = xbc*xbc+ybc*ybc+zbc*zbc;
      double dad = xad*xad+yad*yad+zad*zad;
      double dbd = xbd*xbd+ybd*ybd+zbd*zbd;
      double dcd = xcd*xcd+ycd*ycd+zcd*zcd;

      // Maximum edge length.
      double dmx = dab;
      if (dac>dmx) dmx = dac;
      if (dbc>dmx) dmx = dbc;
      if (dad>dmx) dmx = dad;
      if (dbd>dmx) dmx = dbd;
      if (dcd>dmx) dmx = dcd;
      dmx = sqrt(dmx);

      // Quality.
      double quality = QUALITY_VOL_LONGEST_EDGE_FACTOR*det/(dmx*dmx*dmx);
      if (quality<0.0) quality = -quality;
      if (quality>1.0) quality = 1.0;
      return quality;
    }
    private static double QUALITY_VOL_LONGEST_EDGE_FACTOR = 2.0/sqrt(2.0);

    /**
     * Returns the quality of a tet implied by the specified nodes, 
     * where quality is 3 times the inradius/circumradius ratio, with 
     * range [0:1]. The nodes need not be in any standard order.
     */
    /*
    private static double qualityInRadiusOverCircumRadius(
      Node na, Node nb, Node nc, Node nd)
    {
      double xa = na._x;
      double ya = na._y;
      double za = na._z;
      double xb = nb._x;
      double yb = nb._y;
      double zb = nb._z;
      double xc = nc._x;
      double yc = nc._y;
      double zc = nc._z;
      double xd = nd._x;
      double yd = nd._y;
      double zd = nd._z;

      // Edges.
      double xab = xa-xb;
      double yab = ya-yb;
      double zab = za-zb;
      double xac = xa-xc;
      double yac = ya-yc;
      double zac = za-zc;
      double xbc = xb-xc;
      double ybc = yb-yc;
      double zbc = zb-zc;
      double xad = xa-xd;
      double yad = ya-yd;
      double zad = za-zd;
      double xbd = xb-xd;
      double ybd = yb-yd;
      double zbd = zb-zd;
      double xcd = xc-xd;
      double ycd = yc-yd;
      double zcd = zc-zd;

      // Edge lengths squared.
      double dab = xab*xab+yab*yab+zab*zab;
      double dac = xac*xac+yac*yac+zac*zac;
      double dbc = xbc*xbc+ybc*ybc+zbc*zbc;
      double dad = xad*xad+yad*yad+zad*zad;
      double dbd = xbd*xbd+ybd*ybd+zbd*zbd;
      double dcd = xcd*xcd+ycd*ycd+zcd*zcd;

      // From Barry Joe's GEOMPACK.
      double pb = sqrt(dab*dcd);
      double pc = sqrt(dac*dbd);
      double pd = sqrt(dad*dbc);
      double xcp,ycp,zcp;
      xcp = yab*zac-zab*yac;
      ycp = zab*xac-xab*zac;
      zcp = xab*yac-yab*xac;
      double fd = sqrt(xcp*xcp+ycp*ycp+zcp*zcp);
      xcp = yab*zad-zab*yad;
      ycp = zab*xad-xab*zad;
      zcp = xab*yad-yab*xad;
      double fc = sqrt(xcp*xcp+ycp*ycp+zcp*zcp);
      xcp = ybc*zbd-zbc*ybd;
      ycp = zbc*xbd-xbc*zbd;
      zcp = xbc*ybd-ybc*xbd;
      double fa = sqrt(xcp*xcp+ycp*ycp+zcp*zcp);
      xcp = yac*zad-zac*yad;
      ycp = zac*xad-xac*zad;
      zcp = xac*yad-yac*xad;
      double fb = sqrt(xcp*xcp+ycp*ycp+zcp*zcp);
      double tp = pb+pc;
      double tm = pb-pc;
      double den = (fa+fb+fc+fd) *
                   sqrt(abs((tp+pd)*(tp-pd)*(pd+tm)*(pd-tm)));
      double quality = 0.0;
      if (den>0.0) {
        double vol = xab*xcp+yab*ycp+zab*zcp;
        quality = 12.0*vol*vol/den;
      }
      if (quality>1.0) quality = 1.0;
      return quality;
    }
    */
  }

  /**
   * A type-safe iterator for tets.
   */
  public interface TetIterator {
    public boolean hasNext();
    public Tet next();
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
   * When constructing an edge, a tet that references the two nodes A and
   * B may be specified. If non-null, this tet may used to quickly get any 
   * face or tet nabors of the edge.
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
     * Optionally, a tet may be specified. If non-null, that tet must
     * reference the specified nodes.
     * @param a a node of the edge.
     * @param b a node of the edge.
     * @param abcd a tet that references nodes A and B; null, if none.
     */
    public Edge(Node a, Node b, Tet abcd) {
      Check.argument(
        abcd==null || abcd.references(a,b),
        "tet references nodes");
      _a = a;
      _b = b;
      _tet = abcd;
    }

    /**
     * Returns the node A at which this edge begins.
     * @return the node A.
     */
    public Node nodeA() {
      return _a;
    }

    /**
     * Returns the node B at which this edge ends.
     * @return the node B.
     */
    public Node nodeB() {
      return _b;
    }

    /**
     * Returns the tet that references the nodes in this edge.
     * @return the tet; null, if none specified when edge was constructed.
     */
    public Tet tet() {
      return _tet;
    }

    /**
     * Returns the mate of this edge.
     * @return the mate.
     */
    public Edge mate() {
      return new Edge(_b,_a,_tet);
    }

    /**
     * Computes the midpoint of this edge.
     * @param c array of midpoint coordinates {xc,yc,zc}.
     * @return distance-squared from midpoint to nodes.
     */
    public double midpoint(double[] c) {
      double xa = _a._x;
      double ya = _a._y;
      double za = _a._z;
      double xb = _b._x;
      double yb = _b._y;
      double zb = _b._z;
      c[0] = 0.5*(xa+xb);
      c[1] = 0.5*(ya+yb);
      c[2] = 0.5*(za+zb);
      double dx = c[0]-xb;
      double dy = c[1]-yb;
      double dz = c[2]-zb;
      return dx*dx+dy*dy+dz*dz;
    }

    /**
     * Returns the midpoint of this face.
     * @return array of midpoint coordinates {xc,yc,zc}.
     */
    public double[] midpoint() {
      double[] c = new double[3];
      midpoint(c);
      return c;
    }

    public boolean equals(Object object) {
      if (object==this)
        return true;
      if (object!=null && object.getClass()==this.getClass()) {
        Edge edge = (Edge)object;
        return _a==edge._a && _b==edge._b;
      }
      return false;
    }

    public int hashCode() {
      return _a._hash^_b._hash;
    }

    private Node _a,_b;
    private Tet _tet;

    private Edge(Tet abcd, Node a, Node b) {
      _a = a;
      _b = b;
      _tet = abcd;
    }
  }

  /**
   * Type-safe iterator for edges.
   */
  public interface EdgeIterator {
    public boolean hasNext();
    public Edge next();
  }

  /**
   * An oriented triangular face.
   * <p>
   * A face is specified by three nodes A, B, and C. The order of these
   * nodes is significant. They are in counter-clockwise (CCW) order, as 
   * viewed from a point right of the face.
   * <p>
   * Every face has a mate. A face and its mate reference the same three
   * nodes, but in the opposite order, so they have opposite orientations.
   * Therefore, a face does not equal its mate.
   * <p>
   * When constructing a face, a tet that references the three nodes A, B, 
   * and C may be specified. If non-null, this tet may be used to quickly
   * determine the two tets left and right of the face that references its 
   * nodes. For a face on the hull of the mesh, either the left or right 
   * tet is null.
   */
  public static class Face {

    /**
     * Constructs an oriented face that references the specified nodes.
     * @param a a node of the face.
     * @param b a node of the face.
     * @param c a node of the face.
     */
    public Face(Node a, Node b, Node c) {
      this(a,b,c,null);
    }

    /**
     * Constructs an oriented face that references the specified nodes.
     * Optionally, a tet may be specified. If non-null, that tet may be 
     * right or left of the face, but it must reference the specified nodes.
     * @param a a node of the face.
     * @param b a node of the face.
     * @param c a node of the face.
     * @param abcd a tet that references the specified nodes; null, if none.
     */
    public Face(Node a, Node b, Node c, Tet abcd) {
      Node d = (abcd!=null)?otherNode(abcd,a,b,c):null;
      Check.argument(abcd==null || d!=null,"tet references nodes");
      _a = a;
      _b = b;
      _c = c;
      if (d!=null) {
        if (nodesInOrder(abcd,a,b,c,d)) {
          _tetLeft = abcd;
          _nodeLeft = d;
          _tetRight = abcd.tetNabor(d);
          _nodeRight = (_tetRight!=null)?abcd.nodeNabor(_tetRight):null;
        } else {
          _tetRight = abcd;
          _nodeRight = d;
          _tetLeft = abcd.tetNabor(d);
          _nodeLeft = (_tetLeft!=null)?abcd.nodeNabor(_tetLeft):null;
        }
      }
    }

    /**
     * Returns the node A in this face.
     * @return the node A.
     */
    public final Node nodeA() {
      return _a;
    }

    /**
     * Returns the node B in this face.
     * @return the node B.
     */
    public final Node nodeB() {
      return _b;
    }

    /**
     * Returns the node C in this face.
     * @return the node C.
     */
    public final Node nodeC() {
      return _c;
    }

    /**
     * Returns the tet left of this face.
     * @return the tet left of this face; null, if none.
     */
    public Tet tetLeft() {
      return _tetLeft;
    }

    /**
     * Returns the tet right of this face.
     * @return the tet right of this face; null, if none.
     */
    public Tet tetRight() {
      return _tetRight;
    }

    /**
     * Returns the other node (not in this face) in the tet left of this face.
     * @return the other node in the tet left of this face; null, if none.
     */
    public Node nodeLeft() {
      return _nodeLeft;
    }

    /**
     * Returns the other node (not in this face) in the tet right of this face.
     * @return the other node in the tet right of this face; null, if none.
     */
    public Node nodeRight() {
      return _nodeRight;
    }

    /**
     * Returns the mate of this face.
     * @return the mate of this face.
     */
    public Face mate() {
      return new Face(_b,_a,_c,_tetRight,_nodeRight,_tetLeft,_nodeLeft);
    }

    /**
     * Determines whether this face is visible from the specified point.
     * This face is visible if the point lies strictly right of the oriented
     * plane ABC of this face.
     * @param x the x coordinate of the point.
     * @param y the y coordinate of the point.
     * @param z the z coordinate of the point.
     * @return true, if visible; false, otherwise.
     */
    public boolean isVisibleFromPoint(double x, double y, double z) {
      return Geometry.leftOfPlane(
        _a._x,_a._y,_a._z,
        _b._x,_b._y,_b._z,
        _c._x,_c._y,_c._z,
        x,y,z)<0.0;
    }

    /**
     * Computes the circumcenter of this face.
     * @param c array of circumcenter coordinates {xc,yc,zc}.
     * @return radius-squared of circumcircle.
     */
    public double centerCircle(double[] c) {
      double xa = _a._x;
      double ya = _a._y;
      double za = _a._z;
      double xb = _b._x;
      double yb = _b._y;
      double zb = _b._z;
      double xc = _c._x;
      double yc = _c._y;
      double zc = _c._z;
      Geometry.centerCircle3D(xa,ya,za,xb,yb,zb,xc,yc,zc,c);
      double dx = c[0]-xc;
      double dy = c[1]-yc;
      double dz = c[2]-zc;
      return dx*dx+dy*dy+dz*dz;
    }

    /**
     * Returns the circumcenter of this face.
     * @return array of circumcenter coordinates {xc,yc,zc}.
     */
    public double[] centerCircle() {
      double[] c = new double[3];
      centerCircle(c);
      return c;
    }

    public boolean equals(Object object) {
      if (object==this)
        return true;
      if (object!=null && object.getClass()==this.getClass()) {
        Face face = (Face)object;
        return (_a==face._a && _b==face._b && _c==face._c) ||
               (_a==face._b && _b==face._c && _c==face._a) ||
               (_a==face._c && _b==face._a && _c==face._b);
      }
      return false;
    }

    public int hashCode() {
      return _a._hash^_b._hash^_c._hash;
    }

    private Node _a,_b,_c;
    private Tet _tetLeft,_tetRight;
    private Node _nodeLeft,_nodeRight;

    private Face(
      Node a, Node b, Node c,
      Tet tetLeft, Node nodeLeft, 
      Tet tetRight, Node nodeRight) 
    {
      _a = a;
      _b = b;
      _c = c;
      _tetLeft = tetLeft;
      _tetRight = tetRight;
      _nodeLeft = nodeLeft;
      _nodeRight = nodeRight;
    }

    private Face(Tet tetLeft, Node nodeLeft) {
      initLeft(tetLeft,nodeLeft);
      _tetLeft = tetLeft;
      _nodeLeft = nodeLeft;
      _tetRight = tetLeft.tetNabor(nodeLeft);
      _nodeRight = (_tetRight!=null)?_tetLeft.nodeNabor(_tetRight):null;
    }

    private Face(Tet tetLeft, Node nodeLeft, Tet tetRight, Node nodeRight) {
      if (tetLeft!=null) {
        initLeft(tetLeft,nodeLeft);
      } else if (tetRight!=null) {
        initRight(tetRight,nodeRight);
      } else {
        assert false:"either tetLeft or tetRight is not null";
      }
      _tetLeft = tetLeft;
      _tetRight = tetRight;
      _nodeLeft = nodeLeft;
      _nodeRight = nodeRight;
    }

    private void initLeft(Tet tetLeft, Node nodeLeft) {
      if (nodeLeft==tetLeft._n0) {
        _a = tetLeft._n1;
        _b = tetLeft._n3;
        _c = tetLeft._n2;
      } else if (nodeLeft==tetLeft._n1) {
        _a = tetLeft._n2;
        _b = tetLeft._n3;
        _c = tetLeft._n0;
      } else if (nodeLeft==tetLeft._n2) {
        _a = tetLeft._n3;
        _b = tetLeft._n1;
        _c = tetLeft._n0;
      } else if (nodeLeft==tetLeft._n3) {
        _a = tetLeft._n0;
        _b = tetLeft._n1;
        _c = tetLeft._n2;
      } else {
        assert false:"nodeLeft referenced by tetLeft";
      }
    }

    private void initRight(Tet tetRight, Node nodeRight) {
      if (nodeRight==tetRight._n0) {
        _a = tetRight._n1;
        _b = tetRight._n2;
        _c = tetRight._n3;
      } else if (nodeRight==tetRight._n1) {
        _a = tetRight._n2;
        _b = tetRight._n0;
        _c = tetRight._n3;
      } else if (nodeRight==tetRight._n2) {
        _a = tetRight._n3;
        _b = tetRight._n0;
        _c = tetRight._n1;
      } else if (nodeRight==tetRight._n3) {
        _a = tetRight._n0;
        _b = tetRight._n2;
        _c = tetRight._n1;
      } else {
        assert false:"nodeRight referenced by tetRight";
      }
    }

    /*
    private boolean isVisibleFromNode(Node node) {
      return isVisibleFromPoint(node._x,node._y,node._z);
    }
    */
  }

  /**
   * Type-safe iterator for faces.
   */
  public interface FaceIterator {
    public boolean hasNext();
    public Face next();
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
   * A dynamically growing list of tets.
   */
  public static class TetList {

    /**
     * Appends the specified tet to this list.
     * @param tet the tet to append.
     */
    public final void add(Tet tet) {
      if (_n==_a.length) {
        Tet[] t = new Tet[_a.length*2];
        System.arraycopy(_a,0,t,0,_n);
        _a = t;
      }
      _a[_n++] = tet;
    }

    /**
     * Removes the tet with specified index from this list.
     * @param index the index of the tet to remove.
     * @return the tet removed.
     */
    public final Tet remove(int index) {
      Tet tet = _a[index];
      --_n;
      if (_n>index)
        System.arraycopy(_a,index+1,_a,index,_n-index);
      return tet;
    }

    /**
     * Trims this list so that its array length equals the number of tets.
     * @return the array of tets in this list, after trimming.
     */
    public final Tet[] trim() {
      if (_n<_a.length) {
        Tet[] t = new Tet[_n];
        System.arraycopy(_a,0,t,0,_n);
        _a = t;
      }
      return _a;
    }

    /**
     * Removes all tets from this list.
     */
    public final void clear() {
      _n = 0;
    }

    /**
     * Returns the number of tets in this list.
     * @return the number of tets.
     */
    public final int ntet() {
      return _n;
    }

    /**
     * Returns (by reference) the array of tets in this list.
     * @return the array of tets.
     */
    public final Tet[] tets() {
      return _a;
    }
    private int _n = 0;
    private Tet[] _a = new Tet[64];
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
   * A dynamically growing list of faces.
   */
  public static class FaceList {

    /**
     * Appends the specified face to this list.
     * @param face the face to append.
     */
    public final void add(Face face) {
      if (_n==_a.length) {
        Face[] t = new Face[_a.length*2];
        System.arraycopy(_a,0,t,0,_n);
        _a = t;
      }
      _a[_n++] = face;
    }

    /**
     * Removes the face with specified index from this list.
     * @param index the index of the face to remove.
     * @return the face removed.
     */
    public final Face remove(int index) {
      Face face = _a[index];
      --_n;
      if (_n>index)
        System.arraycopy(_a,index+1,_a,index,_n-index);
      return face;
    }

    /**
     * Trims this list so that its array length equals the number of faces.
     * @return the array of faces in this list, after trimming.
     */
    public final Face[] trim() {
      if (_n<_a.length) {
        Face[] t = new Face[_n];
        System.arraycopy(_a,0,t,0,_n);
        _a = t;
      }
      return _a;
    }

    /**
     * Removes all faces from this list.
     */
    public final void clear() {
      _n = 0;
    }

    /**
     * Returns the number of faces in this list.
     * @return the number of faces.
     */
    public final int nface() {
      return _n;
    }

    /**
     * Returns (by reference) the array of faces in this list.
     * @return the array of faces.
     */
    public final Face[] faces() {
      return _a;
    }
    private int _n = 0;
    private Face[] _a = new Face[64];
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
   * node, on an edge, on a face, inside a tetrahedron, or outside the mesh.
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
     * Determines whether this location is on a face.
     * @return true, if on a face; false, otherwise.
     */
    public boolean isOnFace() {
      return _face!=null;
    }

    /**
     * Determines whether this location is inside a tet.
     * @return true, if inside a tet; false, otherwise.
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
     * Returns the face.
     * @return the face; null, if not on a face.
     */
    public Face face() {
      return _face;
    }

    /**
     * Returns the tet corresponding to the point location.
     * If the point location is on a node, edge, or face, the tet is one
     * that references that node, edge, or face. If the point location is
     * outside the mesh, the tet is one that is visible from the point.
     * @return the tet.
     */
    public Tet tet() {
      return _tet;
    }

    private Node _node; // if on node, the node; otherwise, null
    private Edge _edge; // if on edge, the edge; otherwise, null
    private Face _face; // if on face, the face; otherwise, null
    private Tet _tet; // if inside, the tet; otherwise, a visible tet
    private boolean _inside; // if inside, true; otherwise, false

    private PointLocation(Tet tet) {
      _tet = tet;
      _inside = true;
    }
    private PointLocation(Tet tet, boolean inside) {
      _tet = tet;
      _inside = inside;
    }
    private PointLocation(Node node) {
      _tet = node._tet;
      _node = node;
      _inside = true;
    }
    private PointLocation(Face face) {
      _tet = face._tetLeft;
      _face = face;
      _inside = true;
    }
    private PointLocation(Edge edge) {
      _tet = edge._tet;
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
   * Implemented to monitor the addition and removal of nodes in the mesh.
   * When node listeners are called, the mesh is valid. Therefore, classes
   * can safely call any tet mesh method in their implementations of this
   * interface.
   * <p>
   * Classes that maintain references to nodes in a tet mesh should 
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
    public void nodeWillBeAdded(TetMesh mesh, Node node);

    /**
     * Called after the specified node has been added to the mesh.
     * @param mesh this mesh.
     * @param node the node added.
     */
    public void nodeAdded(TetMesh mesh, Node node);

    /**
     * Called when the specified node will be removed from the mesh.
     * @param mesh this mesh.
     * @param node the node that will be removed.
     */
    public void nodeWillBeRemoved(TetMesh mesh, Node node);

    /**
     * Called after the specified node has been removed from the mesh.
     * @param mesh this mesh.
     * @param node the node removed.
     */
    public void nodeRemoved(TetMesh mesh, Node node);
  }

  /**
   * Implemented to monitor the addition and removal of tets in the mesh.
   * When tet listeners are called, the mesh may be in an <em>invalid</em>
   * state. Specifically, the tet nabors of a tet added or removed may be 
   * invalid. However, all other state of that tet is valid.
   * <p>
   * Classes that maintain references to tets in a tet mesh should 
   * implement this interface. Those references are almost certainly
   * invalid (and may prevent garbage collection) after tets are
   * removed from the mesh. This is especially important because 
   * tets removed may be reused, and given entirely different state,
   * as tets are added to the mesh.
   * <p>
   * Tet listeners may be costly. Typically, for each node added to 
   * a tet mesh, roughly 18 tets are removed and 24 tets are added. 
   * Therefore, tet listeners should be added just prior to making a 
   * small number of incremental changes to the mesh, and removed after 
   * such changes are completed.
   */
  public interface TetListener extends EventListener {

    /**
     * Called after the specified tet has been added to the mesh.
     * When this method is called, the tet nabors of the specified tet are 
     * not valid. All other state of the specified tet, such as its nodes, 
     * is valid during this method call.
     * @param mesh this mesh.
     * @param tet the tet added.
     */
    public void tetAdded(TetMesh mesh, Tet tet);

    /**
     * Called after the specified tet has been removed from the mesh.
     * When this method is called, the tet nabors of the specified tet are 
     * not valid. All other state of the specified tet, such as its nodes, 
     * is valid during this method call.
     * @param mesh this mesh.
     * @param tet the tet removed.
     */
    public void tetRemoved(TetMesh mesh, Tet tet);
  }

  /**
   * Constructs an empty mesh.
   */
  public TetMesh() {
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
   * Returns the number of tets in the mesh.
   * @return the number of tets.
   */
  public int countTets() {
    return _ntet;
  }

  /**
   * Gets the version number for the mesh. The version number is incremented
   * whenever the mesh changes. Therefore, this number can be used to lazily
   * determine if the mesh has changed since the version number was last got.
   * Comparing version numbers may in some applications serve as a cheap 
   * alternative to adding node and tet listeners to the mesh.
   * @return the version number.
   */
  public long getVersion() {
    return _version;
  }

  /**
   * Adds a node to the mesh, if the mesh does not already contain
   * a node with the same (x,y,z) coordinates.
   * @param node the node to add.
   * @return true, if the node was added; false, otherwise.
   */
  public synchronized boolean addNode(Node node) {

    // Where is the point?
    PointLocation pl = locatePoint(node._x,node._y,node._z);

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

    // Maintain adequate sampling of O(N^(1/4)) nodes for fast point location.
    // The scale factor 0.5 was used by Mucke et al., 1996.
    double factor = 0.5*_sampledNodes.size();
    if (factor*factor*factor*factor<_nnode) {
      _sampledNodes.add(node);
      //trace("addNode: sampling "+_sampledNodes.size()+" nodes");
    }

    // If we do not yet have a tet, perhaps we have enough nodes to make one.
    if (pl.isOutside() && _nnode<=4) {
      if (_nnode==4)
        createFirstTet();

    // Otherwise, if we have at least one tet, ...
    } else {

      // Get the set of Delaunay faces that bound the star-shaped 
      // polyhedron containing all tets that are not Delaunay with 
      // respect to the new node.
      clearTetMarks();
      _faceSet.clear();
      if (pl.isInside()) {
        getDelaunayFacesInside(node,pl.tet());
      } else {
        getDelaunayFacesOutside(node,pl.tet());
      }

      // With each Delaunay face in the set, create a new tet with 
      // the new node. Use an edge set to link tets when a tet and 
      // its nabor have been created.
      _edgeSet.clear();
      for (boolean more=_faceSet.first(); more; more=_faceSet.next()) {
        Node a = _faceSet.a;
        Node b = _faceSet.b;
        Node c = _faceSet.c;
        Node d = _faceSet.d;
        Tet abcd = _faceSet.abcd;
        Tet nabc = makeTet(node,a,b,c);
        linkTets(nabc,node,abcd,d);
        if (!_edgeSet.add(a,b,c,nabc))
          linkTets(_edgeSet.nabc,_edgeSet.c,nabc,c);
        if (!_edgeSet.add(b,c,a,nabc))
          linkTets(_edgeSet.nabc,_edgeSet.c,nabc,a);
        if (!_edgeSet.add(c,a,b,nabc))
          linkTets(_edgeSet.nabc,_edgeSet.c,nabc,b);
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

    // Tet that references this node.
    Tet tet = node._tet;

    // Nodes that reference no tet are not in the mesh.
    if (tet==null)
      return false;

    // Tell listeners that node will be removed.
    fireNodeWillBeRemoved(node);

    // Unlink the node from the mesh, leaving only references from tets.
    unlinkNode(node);

    // If fewer than four nodes, the mesh contains no tets.
    if (_nnode<4) {
      if (_nnode==3) {
        Node n0 = _nroot;
        Node n1 = n0._next;
        Node n2 = n1._next;
        n0._tet = null;
        n1._tet = null;
        n2._tet = null;
        killTet(_troot);
        _troot = null;
      }

    // Otherwise, if the mesh contains at least one tet, ...
    } else {

      // Get the Delaunay faces opposite the node to be removed, 
      // and collect the nodes in those faces.
      _faceSet.clear();
      _nodeList.clear();
      clearTetMarks();
      clearNodeMarks();
      getDelaunayFacesOpposite(node,tet);
      int nnode = _nodeList.nnode();
      Node[] nodes = _nodeList.nodes();

      // Use a simple gift-wrapping algorithm to fill in the hole 
      // left by the removed node. While faces remain in the face 
      // set, remove one and ...
      for (boolean more=_faceSet.remove(); more; more=_faceSet.remove()) {

        // Get the face parameters.
        Node a = _faceSet.a;
        Node b = _faceSet.b;
        Node c = _faceSet.c;
        Node d = _faceSet.d;
        Tet abcd = _faceSet.abcd;

        // Search for a node n that is right of the face abc. If one or 
        // more such nodes exist, choose the node n so that nabc has an 
        // empty circumsphere.
        Node n = null;
        for (int inode=0; inode<nnode; ++inode) {
          Node m = nodes[inode];
          if (m!=a && m!=b && m!=c && !leftOfPlane(a,b,c,m)) {
            if (n==null || inSphere(n,a,b,c,m))
              n = m;
          }
        }

        // If there exists a node n that is right of the face abc, ...
        if (n!=null) {

          // Create a new tet nabc.
          Tet nabc = makeTet(n,a,b,c);

          // Link the tets nabc and abcd.
          linkTets(nabc,n,abcd,d);

          // Add the new faces of nabc to the face set. If, instead of
          // adding a face, its mate is removed from the set, link the
          // corresponding tets.
          if (!_faceSet.add(nabc,a))
            linkTets(_faceSet.abcd,_faceSet.d,nabc,a);
          if (!_faceSet.add(nabc,b))
            linkTets(_faceSet.abcd,_faceSet.d,nabc,b);
          if (!_faceSet.add(nabc,c))
            linkTets(_faceSet.abcd,_faceSet.d,nabc,c);

        // Otherwise, the face abc will now be on the convex hull of the mesh.
        } else {

          // Unlink tet abcd and its nabor, which references the face abc.
          linkTets(abcd,d,null,null);
          a._tet = abcd;
          b._tet = abcd;
          c._tet = abcd;
          _troot = abcd;
        }
      }
    }

    if (DEBUG)
      validate();

    // Tell listeners that node was removed.
    fireNodeRemoved(node);

    return true;
  }

  /**
   * Moves a node in the mesh to the specified (x,y,z) coordinates.
   * Roughly equivalent to (but potentially more efficient than)
   * first removing and then adding the node to the mesh at the 
   * specified coordinates. However, if the node is not in the mesh, 
   * then it will be moved, but not added to the mesh. Also, if the 
   * specified coordinates are already occupied by another node in 
   * the mesh, then the specified node is not moved.
   * @param node a node in the mesh.
   * @param x the x coordinate of the moved node.
   * @param y the y coordinate of the moved node.
   * @param z the z coordinate of the moved node.
   * @return true, if the node was moved; false, otherwise.
   */
  public synchronized boolean moveNode(Node node, float x, float y, float z) {
    // TODO: optimize for small movements that require no retriangulation.
    if (x!=node.x() || y!=node.y() || z!=node.z()) {
      Node nodeNearest = findNodeNearest(x,y,z);
      if (node==nodeNearest || 
          x!=nodeNearest.x() || 
          y!=nodeNearest.y() || 
          z!=nodeNearest.z()) {
        boolean nodeInMesh = removeNode(node);
        node.setPosition(x,y,z);
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
   * @param z the z coordinate.
   * @return the nearest node.
   */
  public synchronized Node findNodeNearest(float x, float y, float z) {
    return findNodeNearest((double)x,(double)y,(double)z);
  }

  /**
   * Finds an edge of a tet in the mesh that references the specified nodes.
   * The nodes may be specified in any order.
   * @param na a node.
   * @param nb a node.
   * @return an edge of a tet that references the specified nodes.
   *  If no such edge exists in this mesh, returns null.
   */
  public synchronized Edge findEdge(Node na, Node nb) {
    Tet tet = findTet(na,nb);
    if (tet!=null) {
      return edgeOfTet(tet,na,nb);
    } else {
      return null;
    }
  }

  /**
   * Finds a face of a tet in the mesh that references the specified nodes.
   * The nodes may be specified in any order. However, if not null,
   * the returned face always has a mate, and either the returned 
   * face or its mate references the specified nodes in the specified 
   * order.
   * @param na a node.
   * @param nb a node.
   * @param nc a node.
   * @return a face of a tet that references the specified nodes.
   *  If no such face exists in this mesh, returns null.
   */
  public synchronized Face findFace(Node na, Node nb, Node nc) {
    Tet tet = findTet(na,nb,nc);
    if (tet!=null) {
      return faceOfTet(tet,na,nb,nc);
    } else {
      return null;
    }
  }

  /**
   * Returns a tet that references the specified node.
   * @param node the node.
   * @return a tet that references the specified node;
   *  or null, if the node is not in the mesh or the mesh
   *  has no tets.
   */
  public Tet findTet(Node node) {
    return node._tet;
  }

  /**
   * Returns a tet that references the specified nodes.
   * @param na a node.
   * @param nb a node.
   * @return a tet that references the specified nodes;
   *  or null, if a node is not in the mesh or the mesh
   *  has no tets.
   */
  public synchronized Tet findTet(Node na, Node nb) {
    Tet tet = findTet(na);
    if (tet!=null) {
      clearTetMarks();
      tet = findTet(tet,na,nb);
    }
    return tet;
  }

  /**
   * Returns a tet that references the specified nodes.
   * @param na a node.
   * @param nb a node.
   * @param nc a node.
   * @return a tet that references the specified nodes;
   *  or null, if a node is not in the mesh or the mesh
   *  has no tets.
   */
  public synchronized Tet findTet(Node na, Node nb, Node nc) {
    Tet tet = findTet(na,nb);
    if (tet!=null) {
      clearTetMarks();
      tet = findTet(tet,na,nb,nc);
    }
    return tet;
  }

  /**
   * Returns a tet that references the specified nodes.
   * @param na a node.
   * @param nb a node.
   * @param nc a node.
   * @param nd a node.
   * @return a tet that references the specified nodes;
   *  or null, if a node is not in the mesh or the mesh
   *  has no tets.
   */
  public synchronized Tet findTet(Node na, Node nb, Node nc, Node nd) {
    Tet tet = findTet(na,nb,nc);
    if (tet!=null) {
      clearTetMarks();
      tet = findTet(tet,na,nb,nc,nd);
    }
    return tet;
  }

  /**
   * Locates a point with specified coordinates.
   * @param x the x coordinate.
   * @param y the y coordinate.
   * @param z the z coordinate.
   * @return the {@link PointLocation}.
   */
  public synchronized PointLocation locatePoint(float x, float y, float z) {
    return locatePoint((double)x,(double)y,(double)z);
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
      private Node _nroot = TetMesh.this._nroot;
      private Node _nnext = _nroot;
    };
  }

  /**
   * Gets an iterator for all tets in the mesh.
   * @return the iterator.
   */
  public synchronized TetIterator getTets() {
    return new TetIterator() {
      public final boolean hasNext() {
        return _i.hasNext();
      }
      public final Tet next() {
        return _i.next();
      }
      private Iterator<Tet> _i;
      {
        TetIterator i = getTetsInternal();
        ArrayList<Tet> list = new ArrayList<Tet>(_ntet);
        while (i.hasNext())
          list.add(i.next());
        _i = list.iterator();
      }
    };
  }

  /**
   * Gets an iterator for all edges in the mesh.
   * @return the iterator.
   */
  public synchronized EdgeIterator getEdges() {
    return new EdgeIterator() {
      public final boolean hasNext() {
        return _i.hasNext();
      }
      public final Edge next() {
        return _i.next();
      }
      private Iterator<Edge> _i;
      {
        EdgeSet edgeSet = new EdgeSet(16*countNodes(),0.5f);
        ArrayList<Edge> edgeList = new ArrayList<Edge>(8*countNodes());
        NodeIterator inode = getNodes();
        while (inode.hasNext()) {
          Node na = inode.next();
          Node[] nabors = getNodeNabors(na);
          int nnabor = nabors.length;
          for (int inabor=0; inabor<nnabor; ++inabor) {
            Node nb = nabors[inabor];
            if (edgeSet.add(na,nb,null,null)) {
              Tet tet = findTet(na,nb);
              Edge edge = new Edge(tet,na,nb);
              edgeList.add(edge);
            }
          }
        }
        assert edgeSet.size()==0:"edges matched, size="+edgeSet.size();
        edgeList.trimToSize();
        _i = edgeList.iterator();
      }
    };
  }

  /**
   * Gets an iterator for all tets in the mesh that intersect a plane.
   * The equation for the specified plane is a*x+b*y+c*z+d = 0.
   * @param a the coefficient a in the equation for the plane.
   * @param b the coefficient b in the equation for the plane.
   * @param c the coefficient c in the equation for the plane.
   * @param d the coefficient d in the equation for the plane.
   * @return the iterator.
   */
  public synchronized TetIterator getTetsInPlane(
    final double a, final double b, final double c, final double d) {
    return new TetIterator() {
      public final boolean hasNext() {
        return _tnext!=null;
      }
      public final Tet next() {
        if (_tnext==null)
          throw new NoSuchElementException();
        Tet tet = _tnext;
        addTet(tet._t0);
        addTet(tet._t1);
        addTet(tet._t2);
        addTet(tet._t3);
        int ntet = _tets.size();
        if (ntet==0) {
          _tnext = null;
        } else {
          _tnext = _tets.remove(ntet-1);
        }
        return tet;
      }
      private Tet _tnext = null;
      private ArrayList<Tet> _tets = new ArrayList<Tet>(128);
      {
        _tnext = findTetInPlane(a,b,c,d);
        if (_tnext!=null) {
          clearTetMarks();
          mark(_tnext);
        }
      }
      private void addTet(Tet tet) {
        if (tet!=null && !isMarked(tet)) {
          mark(tet);
          if (tet.intersectsPlane(a,b,c,d))
            _tets.add(tet);
        }
      }
    };
  }

  /**
   * Gets an iterator for all nodes in the mesh that are nearest to a plane.
   * The equation for the specified plane is a*x+b*y+c*z+d = 0.
   * <p>
   * Nodes nearest the plane are those with Voronoi polyhedra that intersect 
   * the plane. In other words, a node is nearest the plane if there exists 
   * some point in the plane that is nearer to that node than to all other 
   * nodes in the mesh.
   * @param a the coefficient a in the equation for the plane.
   * @param b the coefficient b in the equation for the plane.
   * @param c the coefficient c in the equation for the plane.
   * @param d the coefficient d in the equation for the plane.
   * @return the iterator.
   */
  public synchronized NodeIterator getNodesNearestPlane(
    final double a, final double b, final double c, final double d) {
    return new NodeIterator() {
      public final boolean hasNext() {
        return _iterator.hasNext();
      }
      public final Node next() {
        return _iterator.next();
      }
      private Iterator<Node> _iterator = null;
      {
        // Find node nearest plane, and push its tet nabors onto a stack.
        Node node = findNodeNearestPlane(a,b,c,d);
        Tet[] tets = getTetNabors(node);
        ArrayList<Tet> tetStack = new ArrayList<Tet>(512);
        int ntet = tets.length;
        for (int itet=0; itet<ntet; ++itet)
          tetStack.add(tets[itet]);

        // Will mark nodes that have been added to the node list.
        clearNodeMarks();

        // Will mark tets that have been popped from the tet stack.
        clearTetMarks();

        // Add the nearest node to the node list.
        ArrayList<Node> nodeList = new ArrayList<Node>(512);
        nodeList.add(node);
        mark(node);

        // While the tet stack is not empty, ...
        while (!tetStack.isEmpty()) {

          // Pop tet from stack.
          Tet tet = tetStack.remove(tetStack.size()-1);
          mark(tet);

          // If any node referenced by tet has not been marked, ...
          Node n0 = tet._n0;
          Node n1 = tet._n1;
          Node n2 = tet._n2;
          Node n3 = tet._n3;
          boolean m0 = isMarked(n0);
          boolean m1 = isMarked(n1);
          boolean m2 = isMarked(n2);
          boolean m3 = isMarked(n3);
          if (!m0 || !m1 || !m2 || !m3) {

            // On which side of plane is the tet circumcenter?
            double x0 = n0._x;
            double y0 = n0._y;
            double z0 = n0._z;
            double x1 = n1._x;
            double y1 = n1._y;
            double z1 = n1._z;
            double x2 = n2._x;
            double y2 = n2._y;
            double z2 = n2._z;
            double x3 = n3._x;
            double y3 = n3._y;
            double z3 = n3._z;
            double[] po = new double[3];
            Geometry.centerSphere(x0,y0,z0,x1,y1,z1,x2,y2,z2,x3,y3,z3,po);
            double xo = po[0];
            double yo = po[1];
            double zo = po[2];
            double so = a*xo+b*yo+c*zo+d;

            // Look for intersections of Voronoi polyhedra with the plane.
            // If any are found, add the corresponding nodes to the list,
            // and add tet nabors of the current tet to the tet stack.
            double s0 = a*x0+b*y0+c*z0+d;
            double s1 = a*x1+b*y1+c*z1+d;
            double s2 = a*x2+b*y2+c*z2+d;
            double s3 = a*x3+b*y3+c*z3+d;
            boolean intersects = false;
            if (so*s0<=0.0) {
              intersects = true;
              if (!m0) {
                nodeList.add(n0);
                mark(n0);
              }
            }
            if (so*s1<=0.0) {
              intersects = true;
              if (!m1) {
                nodeList.add(n1);
                mark(n1);
              }
            }
            if (so*s2<=0.0) {
              intersects = true;
              if (!m2) {
                nodeList.add(n2);
                mark(n2);
              }
            }
            if (so*s3<=0.0) {
              intersects = true;
              if (!m3) {
                nodeList.add(n3);
                mark(n3);
              }
            }
            if (intersects) {
              Tet t0 = tet._t0;
              Tet t1 = tet._t1;
              Tet t2 = tet._t2;
              Tet t3 = tet._t3;
              if (t0!=null && !isMarked(t0))
                tetStack.add(t0);
              if (t1!=null && !isMarked(t1))
                tetStack.add(t1);
              if (t2!=null && !isMarked(t2))
                tetStack.add(t2);
              if (t3!=null && !isMarked(t3))
                tetStack.add(t3);
            }
          }
        }
        _iterator = nodeList.iterator();
      }
    };
  }

  /**
   * Finds a tet that intersects the specified plane a*x+b*y+c*z+d = 0.
   * @param a the coefficient a in the equation for the plane.
   * @param b the coefficient b in the equation for the plane.
   * @param c the coefficient c in the equation for the plane.
   * @param d the coefficient d in the equation for the plane.
   * @return the tet; null if none intersects the plane.
   */
  public Tet findTetInPlane(double a, double b, double c, double d) {
    Node node = findNodeNearestPlane(a,b,c,d);
    Tet[] tets = getTetNabors(node);
    for (Tet tet:tets) {
      if (tet.intersectsPlane(a,b,c,d))
        return tet;
    }
    return null;
  }

  /**
   * Gets an iterator for all faces on the hull of the mesh.
   * @return the iterator.
   */
  public synchronized FaceIterator getFacesOnHull() {
    clearTetMarks();
    Face face = getFaceOnHull(_troot);
    final HashSet<Face> faces = new HashSet<Face>(128);
    getFacesOnHull(face,faces);
    return new FaceIterator() {
      private Iterator<Face> i = faces.iterator();
      public final boolean hasNext() {
        return i.hasNext();
      }
      public final Face next() {
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
    Tet tet = node._tet;
    if (tet==null)
      return;
    clearNodeMarks();
    clearTetMarks();
    getNodeNabors(node,tet,nabors);
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
   * Gets an array of tet nabors of the specified node.
   * @param node the node for which to get nabors.
   * @return the array of nabors. 
   */
  public synchronized Tet[] getTetNabors(Node node) {
    TetList nabors = new TetList();
    getTetNabors(node,nabors);
    return nabors.trim();
  }

  /** 
   * Appends the tet nabors of the specified node to the specified list.
   * @param node the node for which to get nabors.
   * @param nabors the list to which nabors are appended.
   */
  public synchronized void getTetNabors(Node node, TetList nabors) {
    clearTetMarks();
    getTetNabors(node,node._tet,nabors);
  }

  /**
   * Gets an array of tet nabors of the specified edge.
   * @param edge the edge for which to get nabors.
   * @return the array of nabors. 
   */
  public synchronized Tet[] getTetNabors(Edge edge) {
    TetList nabors = new TetList();
    getTetNabors(edge,nabors);
    return nabors.trim();
  }

  /**
   * Appends the tet nabors of the specified edge to the specified list.
   * @param edge the edge for which to get nabors.
   * @param nabors the list to which nabors are appended.
   */
  public synchronized void getTetNabors(Edge edge, TetList nabors) {
    Node na = edge.nodeA();
    Node nb = edge.nodeB();
    Tet tet = edge.tet();
    if (tet==null)
      tet = findTet(na,nb);
    if (tet==null)
      return;
    clearTetMarks();
    getTetNabors(na,nb,tet,nabors);
  }

  /**
   * Gets an array of tet nabors of the specified face.
   * @param face the face for which to get nabors.
   * @return the array of nabors. 
   */
  public synchronized Tet[] getTetNabors(Face face) {
    TetList nabors = new TetList();
    getTetNabors(face,nabors);
    return nabors.trim();
  }

  /**
   * Appends the tet nabors of the specified face to the specified list.
   * @param face the face for which to get nabors.
   * @param nabors the list to which nabors are appended.
   */
  public synchronized void getTetNabors(Face face, TetList nabors) {
    Tet tetLeft = face.tetLeft();
    Tet tetRight = face.tetRight();
    if (tetLeft==null && tetRight==null) {
      Node na = face.nodeA();
      Node nb = face.nodeB();
      Node nc = face.nodeC();
      face = findFace(na,nb,nc);
      tetLeft = face.tetLeft();
      tetRight = face.tetRight();
    }
    if (tetLeft!=null)
      nabors.add(tetLeft);
    if (tetRight!=null)
      nabors.add(tetRight);
  }

  /**
   * Gets an array of edge nabors of the specified node.
   * @param node the node for which to get nabors.
   * @return the array of nabors. 
   */
  public synchronized Edge[] getEdgeNabors(Node node) {
    EdgeList nabors = new EdgeList();
    getEdgeNabors(node,nabors);
    return nabors.trim();
  }

  /** 
   * Appends the edge nabors of the specified node to the specified list.
   * @param node the node for which to get nabors.
   * @param nabors the list to which nabors are appended.
   */
  public synchronized void getEdgeNabors(Node node, EdgeList nabors) {
    Tet[] tets = getTetNabors(node);
    int ntet = tets.length;
    clearNodeMarks();
    for (int itet=0; itet<ntet; ++itet) {
      Tet tet = tets[itet];
      Node n0 = tet._n0;
      Node n1 = tet._n1;
      Node n2 = tet._n2;
      Node n3 = tet._n3;
      if (node==n0) {
        if (!isMarked(n1)) {
          mark(n1);
          nabors.add(new Edge(tet,n1,node));
        }
        if (!isMarked(n2)) {
          mark(n2);
          nabors.add(new Edge(tet,n2,node));
        }
        if (!isMarked(n3)) {
          mark(n3);
          nabors.add(new Edge(tet,n3,node));
        }
      } else if (node==n1) {
        if (!isMarked(n0)) {
          mark(n0);
          nabors.add(new Edge(tet,n0,node));
        }
        if (!isMarked(n2)) {
          mark(n2);
          nabors.add(new Edge(tet,n2,node));
        }
        if (!isMarked(n3)) {
          mark(n3);
          nabors.add(new Edge(tet,n3,node));
        }
      } else if (node==n2) {
        if (!isMarked(n0)) {
          mark(n0);
          nabors.add(new Edge(tet,n0,node));
        }
        if (!isMarked(n1)) {
          mark(n1);
          nabors.add(new Edge(tet,n1,node));
        }
        if (!isMarked(n3)) {
          mark(n3);
          nabors.add(new Edge(tet,n3,node));
        }
      } else if (node==n3) {
        if (!isMarked(n0)) {
          mark(n0);
          nabors.add(new Edge(tet,n0,node));
        }
        if (!isMarked(n1)) {
          mark(n1);
          nabors.add(new Edge(tet,n1,node));
        }
        if (!isMarked(n2)) {
          mark(n2);
          nabors.add(new Edge(tet,n2,node));
        }
      }
    }
  }

  /**
   * Gets an array of face nabors of the specified edge.
   * The edge is directed, and the faces are stored in CCW order, as 
   * viewed from a point towards which the edge is directed. Only those
   * faces aligned with the edge are stored. Specifically, their mates
   * are not stored.
   * @param edge the edge for which to get nabors.
   * @return the array of nabors. 
   */
  public synchronized Face[] getFaceNabors(Edge edge) {
    FaceList nabors = new FaceList();
    getFaceNabors(edge,nabors);
    return nabors.trim();
  }

  /** 
   * Appends the face nabors of the specified edge to the specified list.
   * The edge is directed, and the faces are stored in CCW order, as 
   * viewed from a point towards which the edge is directed. Only those
   * faces aligned with the edge are stored. Specifically, their mates
   * are not stored.
   * @param edge the edge for which to get nabors.
   * @param nabors the list to which nabors are appended.
   */
  public synchronized void getFaceNabors(Edge edge, FaceList nabors) {

    // Edge nodes.
    Node ea = edge.nodeA();
    Node eb = edge.nodeB();

    // Any tet for this edge?
    Tet tet = edge.tet();
    if (tet==null)
      tet = findTet(ea,eb);
    if (tet==null)
      return;

    // Tet nodes.
    Node ta = tet.nodeA();
    Node tb = tet.nodeB();
    Node tc = tet.nodeC();
    Node td = tet.nodeD();

    // Which face of tet is aligned with the edge? (One must be!)
    Face face = null;
    if (ea==ta && eb==tb || ea==tb && eb==tc || ea==tc && eb==ta) {
      face = new Face(ta,tb,tc,tet);
    } else if (ea==tb && eb==td || ea==td && eb==tc || ea==tc && eb==tb) {
      face = new Face(tb,td,tc,tet);
    } else if (ea==tc && eb==td || ea==td && eb==ta || ea==ta && eb==tc) {
      face = new Face(tc,td,ta,tet);
    } else if (ea==td && eb==tb || ea==tb && eb==ta || ea==ta && eb==td) {
      face = new Face(td,tb,ta,tet);
    } else {
      assert false:"tet references edge";
    }
    
    // Loop over faces in ring until first face is reached.
    Face firstFace = face;
    do {

      // Collect faces.
      nabors.add(face);
    
      // Face nodes.
      Node fa = face.nodeA();
      Node fb = face.nodeB();
      Node fc = face.nodeC();

      // Which node of face is opposite the edge?
      Node node = null;
      if (ea==fa && eb==fb) {
        node = fc;
      } else if (ea==fb && eb==fc) {
        node = fa;
      } else if (ea==fc && eb==fa) {
        node = fb;
      } else {
        assert false:"edge is aligned with face";
      }

      // Get next face in ring. If the right tet for the current face is 
      // null, then the current face is external. In this case, we search 
      // backwards for the next face, which is also external.
      tet = face.tetRight();
      if (tet==null) {
        tet = face.tetLeft();
        Node nodeBack = face.nodeLeft();
        Tet tetBack = tet.tetNabor(node);
        while (tetBack!=null) {
          node = nodeBack;
          nodeBack = tet.nodeNabor(tetBack);
          tet = tetBack;
          tetBack = tet.tetNabor(node);
        }
        face = new Face(null,null,tet,node);
      } else {
        face = new Face(tet,node);
      }
    } while (!face.equals(firstFace));
  }

  /**
   * Sets and enables the outer box for this mesh.
   * Tets with circumspheres entirely within the specified box
   * are <em>inner</em> tets. All other tets are outer tets.
   * An inner node, edge, or face has one or more inner tet nabors;
   * an outer node, edge, or face has no inner tet nabors.
   * <p>
   * The outer box is typically set to be slightly larger than the
   * bounding box of the convex hull of the mesh, so that outer tets 
   * lie near the convex hull. These outer tets tend to have poor
   * quality, and are often ignored in iterations over tets.
   * @param xmin minimum x coordinate of box.
   * @param ymin minimum y coordinate of box.
   * @param zmin minimum z coordinate of box.
   * @param xmax maximum x coordinate of box.
   * @param ymax maximum y coordinate of box.
   * @param zmax maximum z coordinate of box.
   */
  public synchronized void setOuterBox(
    float xmin, float ymin, float zmin, float xmax, float ymax, float zmax)
  {
    Check.argument(xmin<xmax,"outer box is valid");
    Check.argument(ymin<ymax,"outer box is valid");
    Check.argument(zmin<zmax,"outer box is valid");

    // If the outer box is changing, ...
    if (xmin!=_xminOuter ||
        xmax!=_xmaxOuter ||
        ymin!=_yminOuter ||
        ymax!=_ymaxOuter ||
        zmin!=_zminOuter ||
        zmax!=_zmaxOuter) {

      // Remember the outer box.
      _xminOuter = xmin;
      _yminOuter = ymin;
      _zminOuter = zmin;
      _xmaxOuter = xmax;
      _ymaxOuter = ymax;
      _zmaxOuter = zmax;

      // Clear both the inner and outer bits of all tets.
      // We will lazily mark the tet as inner or outer.
      TetIterator ti = getTets();
      while (ti.hasNext()) {
        Tet tet = ti.next();
        tet.clearInner();
        tet.clearOuter();
      }
    }

    ++_version;
    _outerEnabled = true;
  }

  /**
   * Enables outer box testing.
   * With outer box testing enabled, tets are either inner or outer.
   * By default, outer box testing is disabled.
   */
  public void enableOuterBox() {
    ++_version;
    _outerEnabled = true;
  }

  /**
   * Disables outer box testing, without altering the outer box.
   * With outer box testing disabled, all tets are inner.
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
    Tet tet = node.tet();
    if (tet==null || isInner(tet))
      return true;
    Tet[] tets = getTetNabors(node);
    int ntet = tets.length;
    for (int itet=0; itet<ntet; ++itet) {
      if (isInner(tets[itet]))
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
   * Determines whether the specified tet is an inner tet.
   * @return true, if inner; false, otherwise.
   */
  public boolean isInner(Tet tet) {
    if (!_outerEnabled)
      return true;
    if (!tet.isInner() && !tet.isOuter())
      markTetInnerOrOuter(tet);
    return tet.isInner();
  }

  /**
   * Determines whether the specified tet is an outer tet.
   * @return true, if outer; false, otherwise.
   */
  public boolean isOuter(Tet tet) {
    return !isInner(tet);
  }

  /**
   * Determines whether the specified edge is an inner edge.
   * @return true, if inner; false, otherwise.
   */
  public boolean isInner(Edge edge) {
    Tet tet = edge.tet();
    if (tet==null)
      tet = findTet(edge.nodeA(),edge.nodeB());
    if (tet==null)
      return false;
    if (tet.isInner())
      return true;
    Tet[] tets = getTetNabors(edge);
    int ntet = tets.length;
    for (int itet=0; itet<ntet; ++itet) {
      if (isInner(tets[itet]))
        return true;
    }
    return false;
  }

  /**
   * Determines whether the specified edge is an outer edge.
   * @return true, if outer; false, otherwise.
   */
  public boolean isOuter(Edge edge) {
    return !isInner(edge);
  }

  /**
   * Determines whether the specified face is an inner face.
   * @return true, if inner; false, otherwise.
   */
  public boolean isInner(Face face) {
    Tet tetLeft = face.tetLeft();
    if (tetLeft!=null && isInner(tetLeft))
      return true;
    Tet tetRight = face.tetRight();
    return tetRight!=null && isInner(tetRight);
  }

  /**
   * Determines whether the specified face is an outer face.
   * @return true, if outer; false, otherwise.
   */
  public boolean isOuter(Face face) {
    return !isInner(face);
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
   * Unmarks the specified node.
   * @param node the node to unmark.
   */
  public final void unmark(Node node) {
    node._mark = _nodeMarkRed-1;
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
   * Marks the specified tet (red). Marks are used during iterations
   * over tets. Because tets (e.g., those tets containing a particular
   * node) are linked in an unordered structure, such iterations are
   * often performed by recursively visiting tets, and marks are used
   * to tag tets that have already been visited.
   * @param tet the tet to mark (red).
   */
  public final void mark(Tet tet) {
    tet._mark = _tetMarkRed;
  }

  /**
   * Marks the specified tet red.
   * This is equivalent to simply marking the tet.
   * @param tet the tet to mark red.
   */
  public final void markRed(Tet tet) {
    tet._mark = _tetMarkRed;
  }

  /**
   * Marks the specified tet blue.
   * @param tet the tet to mark blue.
   */
  public final void markBlue(Tet tet) {
    tet._mark = _tetMarkBlue;
  }

  /**
   * Unmarks the specified tet.
   * @param tet the tet to unmark.
   */
  public final void unmark(Tet tet) {
    tet._mark = _tetMarkRed-1;
  }

  /**
   * Determines whether the specified tet is marked (red).
   * @param tet the tet.
   * @return true, if the tet is marked (red); false, otherwise.
   */
  public final boolean isMarked(Tet tet) {
    return tet._mark==_tetMarkRed;
  }

  /**
   * Determines whether the specified tet is marked red.
   * @param tet the tet.
   * @return true, if the tet is marked red; false, otherwise.
   */
  public final boolean isMarkedRed(Tet tet) {
    return tet._mark==_tetMarkRed;
  }

  /**
   * Determines whether the specified tet is marked blue.
   * @param tet the tet.
   * @return true, if the tet is marked blue; false, otherwise.
   */
  public final boolean isMarkedBlue(Tet tet) {
    return tet._mark==_tetMarkBlue;
  }

  /**
   * Clears all tet marks, so that no tet is marked. This can usually
   * be accomplished without iterating over all tets in the mesh.
   */
  public synchronized void clearTetMarks() {

    // If the mark is about to overflow, we must zero all the marks.
    // This is tricky, because tets form a cyclic graph. First, we
    // recursively mark all tets, so that no marks are zero. Then,
    // we recursively zero all tet marks.
    if (_tetMarkRed==TET_MARK_MAX) {
      ++_tetMarkRed;
      --_tetMarkBlue;
      markAllTets(_troot);
      zeroTetMarks(_troot);
      _tetMarkRed = 0;
      _tetMarkBlue = 0;
    }

    // Usually, we simply increment/decrement the mark values.
    ++_tetMarkRed;
    --_tetMarkBlue;
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
   * Adds the specified tet listener.
   * @param tl the tet listener.
   */
  public synchronized void addTetListener(TetListener tl) {
    _listeners.add(TetListener.class,tl);
    ++_ntetListeners;
  }

  /**
   * Removes the specified tet listener.
   * @param tl the tet listener.
   */
  public synchronized void removeTetListener(TetListener tl) {
    _listeners.remove(TetListener.class,tl);
    --_ntetListeners;
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
    Check.state(nnode==_nnode,"nnode==_nnode");

    // Check tets.
    int ntet = 0;
    TetIterator ti = getTets();
    while (ti.hasNext()) {
      ++ntet;
      Tet tet = ti.next();
      validate(tet);
    }
    Check.state(ntet==_ntet,"ntet==_ntet");
  }

  ///////////////////////////////////////////////////////////////////////////
  // protected

  /**
   * Initialization for use in constructers and serialization (readObject).
   */
  protected void init() {
    _version = 0;
    _nnode = 0;
    _ntet = 0;
    _nroot = null;
    _troot = null;
    _sampledNodes = new HashSet<Node>(256);
    _tetMarkRed = 0;
    _tetMarkBlue = 0;
    _nodeMarkRed = 0;
    _nodeMarkBlue = 0;
    _faceSet = new FaceSet(256,0.25);
    _edgeSet = new EdgeSet(256,0.25);
    _nodeList = new NodeList();
    _nmin = null;
    _dmin = 0.0;
    _deadTets = new TetList();
    _nnodeListeners = 0;
    _ntetListeners = 0;
    _listeners = new EventListenerList();
    _outerEnabled = false;
    _xminOuter = 0.0;
    _yminOuter = 0.0;
    _zminOuter = 0.0;
    _xmaxOuter = 0.0;
    _ymaxOuter = 0.0;
    _zmaxOuter = 0.0;
    _nnodeValues = 0;
    _lnodeValues = 0;
    _nodePropertyMaps = new HashMap<String,NodePropertyMap>();
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private static final int NODE_MARK_MAX = Integer.MAX_VALUE-1;
  private static final int TET_MARK_MAX = Integer.MAX_VALUE-1;

  private long _version; // the version number
  private int _nnode; // number of nodes
  private int _ntet; // number of tets
  private Node _nroot = null; // a node in the mesh
  private Tet _troot = null; // a tet in the mesh
  private HashSet<Node> _sampledNodes; // for point location
  private int _tetMarkRed; // current value of red tet mark
  private int _tetMarkBlue; // current value of blue tet mark
  private int _nodeMarkRed; // current value of red node mark
  private int _nodeMarkBlue; // current value of blue node mark
  private FaceSet _faceSet; // fast hashed faces
  private EdgeSet _edgeSet; // fast hashed edges
  private NodeList _nodeList; // temporary list of nodes
  private Node _nmin; // the nearest node (used only during searches)
  private double _dmin; // distance squared to nearest node
  private TetList _deadTets; // tet graveyard
  private int _nnodeListeners; // number of node listeners
  private int _ntetListeners; // number of tet listeners
  private EventListenerList _listeners;
  private boolean _outerEnabled; // true, if outer box testing
  private double _xminOuter; // min x of outer box
  private double _yminOuter; // min y of outer box
  private double _zminOuter; // min z of outer box
  private double _xmaxOuter; // max x of outer box
  private double _ymaxOuter; // max y of outer box
  private double _zmaxOuter; // max z of outer box
  private int _nnodeValues; // number of internal node prop values
  private int _lnodeValues; // length of internal node prop arrays
  private Map<String,NodePropertyMap> _nodePropertyMaps; // node property maps

  /**
   * Internal node property map uses the array of values in each node.
   */
  private static class NodePropertyMapInternal implements NodePropertyMap {
    public Object get(Node node) {
      Object[] values = node._values;
      return values[_index];
    }
    public void put(Node node, Object value) {
      Object[] values = node._values;
      values[_index] = value;
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
   * Gets a tet iterator for internal use. This iterator checks and
   * sets tet marks, and is not thread safe. However, it requires only
   * O(1) time to construct. This speed is useful when iteration is
   * canceled before it is completed.
   */
  private TetIterator getTetsInternal() {
    return new TetIterator() {
      public final boolean hasNext() {
        return !_stack.isEmpty();
      }
      public final Tet next() {
        int ntet = _stack.size();
        if (ntet==0)
          throw new NoSuchElementException();
        Tet tet = _stack.remove(ntet-1);
        stackTet(tet._t0);
        stackTet(tet._t1);
        stackTet(tet._t2);
        stackTet(tet._t3);
        return tet;
      }
      //private Tet _tnext = TetMesh.this._troot;
      private ArrayList<Tet> _stack = new ArrayList<Tet>(128);
      {
        clearTetMarks();
        stackTet(_troot);
      }
      private void stackTet(Tet tet) {
        if (tet!=null && !isMarked(tet)) {
          mark(tet);
          _stack.add(tet);
        }
      }
    };
  }

  /**
   * Returns a new tet, possibly one resurrected from the dead.
   * Resurrection reduces the need for garbage collection of dead tets.
   * Assuming that the new tet will be linked into the mesh, the root
   * tet is set to the new tet.
   */
  private Tet makeTet(Node n0, Node n1, Node n2, Node n3) {
    ++_ntet;
    int ndead = _deadTets.ntet();
    if (ndead==0) {
      _troot = new Tet(n0,n1,n2,n3);
    } else {
      _troot = _deadTets.remove(ndead-1);
      _troot.init(n0,n1,n2,n3);
    }
    if (_ntetListeners>0)
      fireTetAdded(_troot);
    return _troot;
  }

  /**
   * Kills a tet and, if there is room, buries it in the graveyard,
   * from where it may be resurrected later.
   * Never updates the root tet, even when the tet being killed is the 
   * root tet. The caller is responsible for setting the root tet, as
   * necessary.
   */
  private void killTet(Tet tet) {
    --_ntet;
    fireTetRemoved(tet);
    int ndead = _deadTets.ntet();
    if (ndead<256)
      _deadTets.add(tet);
    // We assume that killTet is called only within contexts in
    // which _troot is updated to point to a live tet in the mesh.
    // Therefore, we do not update _troot here, when _troot==tet.
  }

  /**
   * Unlinks a node from the mesh, leaving only references from tets.
   */
  private void unlinkNode(Node node) {
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
    node._tet = null;
    _sampledNodes.remove(node);
    --_nnode;
  }

  /**
   * Returns the distance squared between two specified nodes.
   */
  /*
  private static double distanceSquared(Node a, Node b) {
    double dx = a._x-b._x;
    double dy = a._y-b._y;
    double dz = a._z-b._z;
    return dx*dx+dy*dy+dz*dz;
  }
  */

  /**
   * Returns the distance squared between the specified node and a point
   * with specified coordinates.
   */
  private static double distanceSquared(
    Node node, double x, double y, double z)
  {
    double dx = x-node._x;
    double dy = y-node._y;
    double dz = z-node._z;
    return dx*dx+dy*dy+dz*dz;
  }

  /**
   * Returns the distance squared from the specified node to the 
   * specified plane.
   */
  private static double distanceToPlaneSquared(
    Node node, double a, double b, double c, double d)
  {
    double dp = a*node._x+b*node._y+c*node._z+d;
    return dp*dp;
  }

  /**
   * Returns true iff node n is left of oriented plane abc.
   * Perturbation of coordinates ensures that the node is not in the plane.
   */
  private static boolean leftOfPlane(Node a, Node b, Node c, Node n) {
    return Geometry.leftOfPlane(
      a._x,a._y,a._z,
      b._x,b._y,b._z,
      c._x,c._y,c._z,
      n._x,n._y,n._z)>0.0;
  }

  /**
   * Returns true iff point (x,y,z) is left of oriented plane abc.
   */
  private static boolean leftOfPlane(
    Node a, Node b, Node c,
    double x, double y, double z)
  {
    return Geometry.leftOfPlane(
      a._x,a._y,a._z,
      b._x,b._y,b._z,
      c._x,c._y,c._z,
      x,y,z)>0.0;
  }

  /**
   * Returns true iff node n is in circumsphere of tet abcd.
   * Perturbation of coordinates ensures that the node is not on the sphere.
   */
  private static boolean inSphere(
    Node a, Node b, Node c, Node d, Node n)
  {
    return Geometry.inSphere(
      a._x,a._y,a._z,
      b._x,b._y,b._z,
      c._x,c._y,c._z,
      d._x,d._y,d._z,
      n._x,n._y,n._z)>0.0;
  }

  /**
   * Returns true iff point (x,y,z) is in circumsphere of tet abcd.
   * (Currently unused.)
   */
  private static boolean inSphere(
    Node a, Node b, Node c, Node d, 
    double x, double y, double z)
  {
    return Geometry.inSphere(
      a._x,a._y,a._z,
      b._x,b._y,b._z,
      c._x,c._y,c._z,
      d._x,d._y,d._z,
      x,y,z)>0.0;
  }

  /**
   * Creates the first tet in the mesh from four nodes.
   */
  private void createFirstTet() {
    Check.state(_nnode==4,"exactly four nodes available for first tet");
    Node n0 = _nroot;
    Node n1 = n0._next;
    Node n2 = n1._next;
    Node n3 = n2._next;
    double orient = Geometry.leftOfPlane(
      n0._x,n0._y,n0._z,
      n1._x,n1._y,n1._z,
      n2._x,n2._y,n2._z,
      n3._x,n3._y,n3._z);
    if (orient==0.0) {
      trace("orient="+orient);
      trace("n0=("+n0._x+","+n0._y+","+n0._z+")");
      trace("n1=("+n1._x+","+n1._y+","+n1._z+")");
      trace("n2=("+n2._x+","+n2._y+","+n2._z+")");
      trace("n3=("+n3._x+","+n3._y+","+n3._z+")");
    }
    Check.state(orient!=0,"four nodes for first tet are not co-planar");
    if (orient>0.0) {
      makeTet(n0,n1,n2,n3);
    } else {
      makeTet(n0,n2,n1,n3);
    }
  }

  /**
   * Recursively appends nabors of the specified node to the specified list.
   * Both tet and node marks must be cleared before calling this method. 
   * This method could be made shorter by using another recursive method, 
   * but this longer inlined version is more efficient.
   */
  private void getNodeNabors(Node node, Tet tet, NodeList nabors) {
    mark(tet);
    Node n0 = tet._n0;
    Node n1 = tet._n1;
    Node n2 = tet._n2;
    Node n3 = tet._n3;
    Tet t0 = tet._t0;
    Tet t1 = tet._t1;
    Tet t2 = tet._t2;
    Tet t3 = tet._t3;
    if (node==n0) {
      if (!isMarked(n1)) {
        mark(n1);
        nabors.add(n1);
      }
      if (!isMarked(n2)) {
        mark(n2);
        nabors.add(n2);
      }
      if (!isMarked(n3)) {
        mark(n3);
        nabors.add(n3);
      }
      if (t1!=null && !isMarked(t1))
        getNodeNabors(node,t1,nabors);
      if (t2!=null && !isMarked(t2))
        getNodeNabors(node,t2,nabors);
      if (t3!=null && !isMarked(t3))
        getNodeNabors(node,t3,nabors);
    } else if (node==n1) {
      if (!isMarked(n3)) {
        mark(n3);
        nabors.add(n3);
      }
      if (!isMarked(n2)) {
        mark(n2);
        nabors.add(n2);
      }
      if (!isMarked(n0)) {
        mark(n0);
        nabors.add(n0);
      }
      if (t3!=null && !isMarked(t3))
        getNodeNabors(node,t3,nabors);
      if (t2!=null && !isMarked(t2))
        getNodeNabors(node,t2,nabors);
      if (t0!=null && !isMarked(t0))
        getNodeNabors(node,t0,nabors);
    } else if (node==n2) {
      if (!isMarked(n3)) {
        mark(n3);
        nabors.add(n3);
      }
      if (!isMarked(n0)) {
        mark(n0);
        nabors.add(n0);
      }
      if (!isMarked(n1)) {
        mark(n1);
        nabors.add(n1);
      }
      if (t3!=null && !isMarked(t3))
        getNodeNabors(node,t3,nabors);
      if (t0!=null && !isMarked(t0))
        getNodeNabors(node,t0,nabors);
      if (t1!=null && !isMarked(t1))
        getNodeNabors(node,t1,nabors);
    } else if (node==n3) {
      if (!isMarked(n1)) {
        mark(n1);
        nabors.add(n1);
      }
      if (!isMarked(n0)) {
        mark(n0);
        nabors.add(n0);
      }
      if (!isMarked(n2)) {
        mark(n2);
        nabors.add(n2);
      }
      if (t1!=null && !isMarked(t1))
        getNodeNabors(node,t1,nabors);
      if (t0!=null && !isMarked(t0))
        getNodeNabors(node,t0,nabors);
      if (t2!=null && !isMarked(t2))
        getNodeNabors(node,t2,nabors);
    } else {
      assert false:"node is referenced by tet";
    }
  }

  /**
   * Accumulates nabor nodes of the specified node. For each nabor node 
   * found, sets the corresponding step to the specified value. Returns 
   * the total number of nabor nodes and steps.
   * <p>
   * Note that this method does not clear the node marks.
   */
  private void getNodeNabors(Node node, int step, NodeStepList list) {
    Tet[] tets = getTetNabors(node);
    int ntet = tets.length;
    for (int itet=0; itet<ntet; ++itet) {
      Tet tet = tets[itet];
      Node n0 = tet._n0;
      Node n1 = tet._n1;
      Node n2 = tet._n2;
      Node n3 = tet._n3;
      if (node==n0) {
        if (!isMarked(n1)) {
          mark(n1);
          list.add(n1,step);
        }
        if (!isMarked(n2)) {
          mark(n2);
          list.add(n2,step);
        }
        if (!isMarked(n3)) {
          mark(n3);
          list.add(n3,step);
        }
      } else if (node==n1) {
        if (!isMarked(n0)) {
          mark(n0);
          list.add(n0,step);
        }
        if (!isMarked(n2)) {
          mark(n2);
          list.add(n2,step);
        }
        if (!isMarked(n3)) {
          mark(n3);
          list.add(n3,step);
        }
      } else if (node==n2) {
        if (!isMarked(n0)) {
          mark(n0);
          list.add(n0,step);
        }
        if (!isMarked(n1)) {
          mark(n1);
          list.add(n1,step);
        }
        if (!isMarked(n3)) {
          mark(n3);
          list.add(n3,step);
        }
      } else if (node==n3) {
        if (!isMarked(n0)) {
          mark(n0);
          list.add(n0,step);
        }
        if (!isMarked(n1)) {
          mark(n1);
          list.add(n1,step);
        }
        if (!isMarked(n2)) {
          mark(n2);
          list.add(n2,step);
        }
      }
    }
  }

  /**
   * Recursively adds tet nabors of the specified node to the specified array.
   * Returns the number of tet nabors found; the length of the array must be 
   * at least that large. The tet marks must be cleared before calling this 
   * method. This method could be made shorter by using another recursive 
   * method, but this longer inlined version is more efficient.
   */
  private void getTetNabors(Node node, Tet tet, TetList nabors) {
    if (tet!=null) {
      mark(tet);
      nabors.add(tet);
      Node n0 = tet._n0;
      Node n1 = tet._n1;
      Node n2 = tet._n2;
      Node n3 = tet._n3;
      Tet t0 = tet._t0;
      Tet t1 = tet._t1;
      Tet t2 = tet._t2;
      Tet t3 = tet._t3;
      if (node==n0) {
        if (t1!=null && !isMarked(t1))
          getTetNabors(node,t1,nabors);
        if (t2!=null && !isMarked(t2))
          getTetNabors(node,t2,nabors);
        if (t3!=null && !isMarked(t3))
          getTetNabors(node,t3,nabors);
      } else if (node==n1) {
        if (t3!=null && !isMarked(t3))
          getTetNabors(node,t3,nabors);
        if (t2!=null && !isMarked(t2))
          getTetNabors(node,t2,nabors);
        if (t0!=null && !isMarked(t0))
          getTetNabors(node,t0,nabors);
      } else if (node==n2) {
        if (t3!=null && !isMarked(t3))
          getTetNabors(node,t3,nabors);
        if (t0!=null && !isMarked(t0))
          getTetNabors(node,t0,nabors);
        if (t1!=null && !isMarked(t1))
          getTetNabors(node,t1,nabors);
      } else if (node==n3) {
        if (t1!=null && !isMarked(t1))
          getTetNabors(node,t1,nabors);
        if (t0!=null && !isMarked(t0))
          getTetNabors(node,t0,nabors);
        if (t2!=null && !isMarked(t2))
          getTetNabors(node,t2,nabors);
      } else {
        assert false:"node is referenced by tet";
      }
    }
  }

  /**
   * Recursively gets tet nabors of the edge between the specified 
   * nodes na and nb, starting with the specified tet, which must
   * reference both na and nb.
   * Tet marks must be cleared before calling this method.
   */
  private void getTetNabors(Node na, Node nb, Tet tet, TetList nabors) {
    if (tet!=null) {
      mark(tet);
      nabors.add(tet);
      Node n0 = tet._n0;
      Node n1 = tet._n1;
      Node n2 = tet._n2;
      Node n3 = tet._n3;
      Tet t0 = tet._t0;
      Tet t1 = tet._t1;
      Tet t2 = tet._t2;
      Tet t3 = tet._t3;
      Tet tc = null;
      Tet td = null;
      if (na==n0) {
        if (nb==n1) {
          tc = t2;
          td = t3;
        } else if (nb==n2) {
          tc = t1;
          td = t3;
        } else if (nb==n3) {
          tc = t1;
          td = t2;
        } else {
          assert false:"nodes na and nb are referenced by tet";
        }
      } else if (na==n1) {
        if (nb==n0) {
          tc = t2;
          td = t3;
        } else if (nb==n2) {
          tc = t0;
          td = t3;
        } else if (nb==n3) {
          tc = t0;
          td = t2;
        } else {
          assert false:"nodes na and nb are referenced by tet";
        }
      } else if (na==n2) {
        if (nb==n0) {
          tc = t1;
          td = t3;
        } else if (nb==n1) {
          tc = t0;
          td = t3;
        } else if (nb==n3) {
          tc = t0;
          td = t1;
        } else {
          assert false:"nodes na and nb are referenced by tet";
        }
      } else if (na==n3) {
        if (nb==n0) {
          tc = t1;
          td = t2;
        } else if (nb==n1) {
          tc = t0;
          td = t2;
        } else if (nb==n2) {
          tc = t0;
          td = t1;
        } else {
          assert false:"nodes na and nb are referenced by tet";
        }
      } else {
        assert false:"node na is referenced by tet";
      }
      if (tc!=null && !isMarked(tc)) {
        getTetNabors(na,nb,tc,nabors);
      }
      if (td!=null && !isMarked(td)) {
        getTetNabors(na,nb,td,nabors);
      }
    }
  }

  /**
   * Recursively searches for any tet that references nodes na and nb,
   * given a tet that references node na. If no such tet exists, then
   * returns null. Tet marks must be cleared before calling this method.
   */
  private Tet findTet(Tet tet, Node na, Node nb) {
    if (tet!=null) {
      mark(tet);
      Node n0 = tet._n0;
      Node n1 = tet._n1;
      Node n2 = tet._n2;
      Node n3 = tet._n3;
      Tet t0 = tet._t0;
      Tet t1 = tet._t1;
      Tet t2 = tet._t2;
      Tet t3 = tet._t3;
      if (na==n0) {
        if (nb==n1 || nb==n2 || nb==n3 ||
            t1!=null && !isMarked(t1) && (tet=findTet(t1,na,nb))!=null ||
            t2!=null && !isMarked(t2) && (tet=findTet(t2,na,nb))!=null ||
            t3!=null && !isMarked(t3) && (tet=findTet(t3,na,nb))!=null)
          return tet;
      } else if (na==n1) {
        if (nb==n3 || nb==n2 || nb==n0 ||
            t3!=null && !isMarked(t3) && (tet=findTet(t3,na,nb))!=null ||
            t2!=null && !isMarked(t2) && (tet=findTet(t2,na,nb))!=null ||
            t0!=null && !isMarked(t0) && (tet=findTet(t0,na,nb))!=null)
          return tet;
      } else if (na==n2) {
        if (nb==n3 || nb==n0 || nb==n1 ||
            t3!=null && !isMarked(t3) && (tet=findTet(t3,na,nb))!=null ||
            t0!=null && !isMarked(t0) && (tet=findTet(t0,na,nb))!=null ||
            t1!=null && !isMarked(t1) && (tet=findTet(t1,na,nb))!=null)
          return tet;
      } else if (na==n3) {
        if (nb==n1 || nb==n0 || nb==n2 ||
            t1!=null && !isMarked(t1) && (tet=findTet(t1,na,nb))!=null ||
            t0!=null && !isMarked(t0) && (tet=findTet(t0,na,nb))!=null ||
            t2!=null && !isMarked(t2) && (tet=findTet(t2,na,nb))!=null)
          return tet;
      } else {
        assert false:"node na is referenced by tet";
      }
    }
    return null;
  }

  /**
   * Recursively searches for any tet that references nodes na, nb, and nc,
   * given a tet that references nodes na and nb. If no such tet exists, 
   * then returns null. Tet marks must be cleared before calling this method.
   */
  private Tet findTet(Tet tet, Node na, Node nb, Node nc) {
    if (tet!=null) {
      mark(tet);
      Node n0 = tet._n0;
      Node n1 = tet._n1;
      Node n2 = tet._n2;
      Node n3 = tet._n3;
      Tet t0 = tet._t0;
      Tet t1 = tet._t1;
      Tet t2 = tet._t2;
      Tet t3 = tet._t3;
      if (na==n0) {
        if (nb==n1) {
          if (nc==n2 || nc==n3 ||
              t2!=null && !isMarked(t2) && (tet=findTet(t2,na,nb,nc))!=null ||
              t3!=null && !isMarked(t3) && (tet=findTet(t3,na,nb,nc))!=null)
          return tet;
        } else if (nb==n2) {
          if (nc==n1 || nc==n3 ||
              t1!=null && !isMarked(t1) && (tet=findTet(t1,na,nb,nc))!=null ||
              t3!=null && !isMarked(t3) && (tet=findTet(t3,na,nb,nc))!=null)
          return tet;
        } else if (nb==n3) {
          if (nc==n1 || nc==n2 ||
              t1!=null && !isMarked(t1) && (tet=findTet(t1,na,nb,nc))!=null ||
              t2!=null && !isMarked(t2) && (tet=findTet(t2,na,nb,nc))!=null)
          return tet;
        } else {
          assert false:"node nb is referenced by tet";
        }
      } else if (na==n1) {
        if (nb==n0) {
          if (nc==n2 || nc==n3 ||
              t2!=null && !isMarked(t2) && (tet=findTet(t2,na,nb,nc))!=null ||
              t3!=null && !isMarked(t3) && (tet=findTet(t3,na,nb,nc))!=null)
          return tet;
        } else if (nb==n2) {
          if (nc==n0 || nc==n3 ||
              t0!=null && !isMarked(t0) && (tet=findTet(t0,na,nb,nc))!=null ||
              t3!=null && !isMarked(t3) && (tet=findTet(t3,na,nb,nc))!=null)
          return tet;
        } else if (nb==n3) {
          if (nc==n0 || nc==n2 ||
              t0!=null && !isMarked(t0) && (tet=findTet(t0,na,nb,nc))!=null ||
              t2!=null && !isMarked(t2) && (tet=findTet(t2,na,nb,nc))!=null)
          return tet;
        } else {
          assert false:"node nb is referenced by tet";
        }
      } else if (na==n2) {
        if (nb==n0) {
          if (nc==n1 || nc==n3 ||
              t1!=null && !isMarked(t1) && (tet=findTet(t1,na,nb,nc))!=null ||
              t3!=null && !isMarked(t3) && (tet=findTet(t3,na,nb,nc))!=null)
          return tet;
        } else if (nb==n1) {
          if (nc==n0 || nc==n3 ||
              t0!=null && !isMarked(t0) && (tet=findTet(t0,na,nb,nc))!=null ||
              t3!=null && !isMarked(t3) && (tet=findTet(t3,na,nb,nc))!=null)
          return tet;
        } else if (nb==n3) {
          if (nc==n0 || nc==n1 ||
              t0!=null && !isMarked(t0) && (tet=findTet(t0,na,nb,nc))!=null ||
              t1!=null && !isMarked(t1) && (tet=findTet(t1,na,nb,nc))!=null)
          return tet;
        } else {
          assert false:"node nb is referenced by tet";
        }
      } else if (na==n3) {
        if (nb==n0) {
          if (nc==n1 || nc==n2 ||
              t1!=null && !isMarked(t1) && (tet=findTet(t1,na,nb,nc))!=null ||
              t2!=null && !isMarked(t2) && (tet=findTet(t2,na,nb,nc))!=null)
          return tet;
        } else if (nb==n1) {
          if (nc==n0 || nc==n2 ||
              t0!=null && !isMarked(t0) && (tet=findTet(t0,na,nb,nc))!=null ||
              t2!=null && !isMarked(t2) && (tet=findTet(t2,na,nb,nc))!=null)
          return tet;
        } else if (nb==n2) {
          if (nc==n0 || nc==n1 ||
              t0!=null && !isMarked(t0) && (tet=findTet(t0,na,nb,nc))!=null ||
              t1!=null && !isMarked(t1) && (tet=findTet(t1,na,nb,nc))!=null)
          return tet;
        } else {
          assert false:"node nb is referenced by tet";
        }
      } else {
        assert false:"node na is referenced by tet";
      }
    }
    return null;
  }

  /**
   * Recursively searches for any tet that references nodes na, nb, nc,
   * and nd, given a tet that references nodes na, nb, and nc. If no such 
   * tet exists, then returns null. Tet marks must be cleared before calling
   * this method.
   */
  private Tet findTet(Tet tet, Node na, Node nb, Node nc, Node nd) {
    if (tet!=null) {
      mark(tet);
      Node n0 = tet._n0;
      Node n1 = tet._n1;
      Node n2 = tet._n2;
      Node n3 = tet._n3;
      Tet t0 = tet._t0;
      Tet t1 = tet._t1;
      Tet t2 = tet._t2;
      Tet t3 = tet._t3;
      if (na==n0) {
        if (nb==n1) {
          if (nc==n2) {
            if (nd==n3 || t3!=null && !isMarked(t3) &&
                (tet=findTet(t3,na,nb,nc,nd))!=null)
              return tet;
          } else if (nc==n3) {
            if (nd==n2 || t2!=null && !isMarked(t2) &&
                (tet=findTet(t2,na,nb,nc,nd))!=null)
              return tet;
          } else {
            assert false:"node nc is referenced by tet";
          }
        } else if (nb==n2) {
          if (nc==n1) {
            if (nd==n3 || t3!=null && !isMarked(t3) &&
                (tet=findTet(t3,na,nb,nc,nd))!=null)
              return tet;
          } else if (nc==n3) {
            if (nd==n1 || t1!=null && !isMarked(t1) &&
                (tet=findTet(t1,na,nb,nc,nd))!=null)
              return tet;
          } else {
            assert false:"node nc is referenced by tet";
          }
        } else if (nb==n3) {
          if (nc==n1) {
            if (nd==n2 || t2!=null && !isMarked(t2) &&
                (tet=findTet(t2,na,nb,nc,nd))!=null)
              return tet;
          } else if (nc==n2) {
            if (nd==n1 || t1!=null && !isMarked(t1) &&
                (tet=findTet(t1,na,nb,nc,nd))!=null)
              return tet;
          } else {
            assert false:"node nc is referenced by tet";
          }
        } else {
          assert false:"node nb is referenced by tet";
        }
      } else if (na==n1) {
        if (nb==n0) {
          if (nc==n2) {
            if (nd==n3 || t3!=null && !isMarked(t3) &&
                (tet=findTet(t3,na,nb,nc,nd))!=null)
              return tet;
          } else if (nc==n3) {
            if (nd==n2 || t2!=null && !isMarked(t2) &&
                (tet=findTet(t2,na,nb,nc,nd))!=null)
              return tet;
          } else {
            assert false:"node nc is referenced by tet";
          }
        } else if (nb==n2) {
          if (nc==n0) {
            if (nd==n3 || t3!=null && !isMarked(t3) &&
                (tet=findTet(t3,na,nb,nc,nd))!=null)
              return tet;
          } else if (nc==n3) {
            if (nd==n0 || t0!=null && !isMarked(t0) &&
                (tet=findTet(t0,na,nb,nc,nd))!=null)
              return tet;
          } else {
            assert false:"node nc is referenced by tet";
          }
        } else if (nb==n3) {
          if (nc==n0) {
            if (nd==n2 || t2!=null && !isMarked(t2) &&
                (tet=findTet(t2,na,nb,nc,nd))!=null)
              return tet;
          } else if (nc==n2) {
            if (nd==n0 || t0!=null && !isMarked(t0) &&
                (tet=findTet(t0,na,nb,nc,nd))!=null)
              return tet;
          } else {
            assert false:"node nc is referenced by tet";
          }
        } else {
          assert false:"node nb is referenced by tet";
        }
      } else if (na==n2) {
        if (nb==n0) {
          if (nc==n1) {
            if (nd==n3 || t3!=null && !isMarked(t3) &&
                (tet=findTet(t3,na,nb,nc,nd))!=null)
              return tet;
          } else if (nc==n3) {
            if (nd==n1 || t1!=null && !isMarked(t1) &&
                (tet=findTet(t1,na,nb,nc,nd))!=null)
              return tet;
          } else {
            assert false:"node nc is referenced by tet";
          }
        } else if (nb==n1) {
          if (nc==n0) {
            if (nd==n3 || t3!=null && !isMarked(t3) &&
                (tet=findTet(t3,na,nb,nc,nd))!=null)
              return tet;
          } else if (nc==n3) {
            if (nd==n0 || t0!=null && !isMarked(t0) &&
                (tet=findTet(t0,na,nb,nc,nd))!=null)
              return tet;
          } else {
            assert false:"node nc is referenced by tet";
          }
        } else if (nb==n3) {
          if (nc==n0) {
            if (nd==n1 || t1!=null && !isMarked(t1) &&
                (tet=findTet(t1,na,nb,nc,nd))!=null)
              return tet;
          } else if (nc==n1) {
            if (nd==n0 || t0!=null && !isMarked(t0) &&
                (tet=findTet(t0,na,nb,nc,nd))!=null)
              return tet;
          } else {
            assert false:"node nc is referenced by tet";
          }
        } else {
          assert false:"node nb is referenced by tet";
        }
      } else if (na==n3) {
        if (nb==n0) {
          if (nc==n1) {
            if (nd==n2 || t2!=null && !isMarked(t2) &&
                (tet=findTet(t2,na,nb,nc,nd))!=null)
              return tet;
          } else if (nc==n2) {
            if (nd==n1 || t1!=null && !isMarked(t1) &&
                (tet=findTet(t1,na,nb,nc,nd))!=null)
              return tet;
          } else {
            assert false:"node nc is referenced by tet";
          }
        } else if (nb==n1) {
          if (nc==n0) {
            if (nd==n2 || t2!=null && !isMarked(t2) &&
                (tet=findTet(t2,na,nb,nc,nd))!=null)
              return tet;
          } else if (nc==n2) {
            if (nd==n0 || t0!=null && !isMarked(t0) &&
                (tet=findTet(t0,na,nb,nc,nd))!=null)
              return tet;
          } else {
            assert false:"node nc is referenced by tet";
          }
        } else if (nb==n2) {
          if (nc==n0) {
            if (nd==n1 || t1!=null && !isMarked(t1) &&
                (tet=findTet(t1,na,nb,nc,nd))!=null)
              return tet;
          } else if (nc==n1) {
            if (nd==n0 || t0!=null && !isMarked(t0) &&
                (tet=findTet(t0,na,nb,nc,nd))!=null)
              return tet;
          } else {
            assert false:"node nc is referenced by tet";
          }
        } else {
          assert false:"node nb is referenced by tet";
        }
      } else {
        assert false:"node na is referenced by tet";
      }
    }
    return null;
  }

  /**
   * Finds the node nearest to the point with specified coordinates.
   */
  private Node findNodeNearest(double x, double y, double z) {

    // If no nodes, ...
    if (_nnode==0)
      return null;

    // If fewer than 20 nodes, simply check all of them.
    if (_nnode<20) {
      _nmin = _nroot;
      _dmin = distanceSquared(_nmin,x,y,z);
      TetMesh.NodeIterator ni = getNodes();
      while (ni.hasNext()) {
        Node n = ni.next();
        double d = distanceSquared(n,x,y,z);
        if (d<_dmin) {
          _dmin = d;
          _nmin = n;
        }
      }
      return _nmin;
    }

    // If at least 20 nodes, walk along edges of the mesh.
    // First, find the nearest node among the sampled nodes.
    _nmin = _nroot;
    _dmin = distanceSquared(_nmin,x,y,z);
    for (Node n:_sampledNodes) {
      double d = distanceSquared(n,x,y,z);
      if (d<_dmin) {
        _dmin = d;
        _nmin = n;
      }
    }

    // Then, recursively search node nabors to find the nearest node.
    // Note that we clear the node marks once, so that we never compute
    // the distance to a node twice, but that we clear the tet marks
    // *inside* the loop, to ensure that we visit all tet nabors of the
    // current nearest node.
    clearNodeMarks();
    double dmin;
    do {
      clearTetMarks();
      dmin = _dmin;
      findNodeNaborNearest(x,y,z,_nmin,_nmin._tet);
    } while (_dmin<dmin);
    return _nmin;
  }

  /**
   * Recursively finds the node nabor of the specified node that is
   * nearest to the specified point, beginning with the nabors in the
   * specified tet, which must reference the specified node. 
   * Both tet and node marks must be cleared before calling this method.
   */
  private void findNodeNaborNearest(
    double x, double y, double z, Node node, Tet tet)
  {
    mark(tet);
    Node n0 = tet._n0;
    Node n1 = tet._n1;
    Node n2 = tet._n2;
    Node n3 = tet._n3;
    Tet t0 = tet._t0;
    Tet t1 = tet._t1;
    Tet t2 = tet._t2;
    Tet t3 = tet._t3;
    if (node==n0) {
      findNodeNaborNearest(x,y,z,node,n1,n2,n3,t1,t2,t3);
    } else if (node==n1) {
      findNodeNaborNearest(x,y,z,node,n3,n2,n0,t3,t2,t0);
    } else if (node==n2) {
      findNodeNaborNearest(x,y,z,node,n3,n0,n1,t3,t0,t1);
    } else if (node==n3) {
      findNodeNaborNearest(x,y,z,node,n1,n0,n2,t1,t0,t2);
    } else {
      assert false:"node is referenced by tet";
    }
  }
  private void findNodeNaborNearest(
    double x, double y, double z, Node node,
    Node na, Node nb, Node nc, Tet ta, Tet tb, Tet tc)
  {
    if (!isMarked(na)) {
      mark(na);
      double da = distanceSquared(na,x,y,z);
      if (da<_dmin) {
        _dmin = da;
        _nmin = na;
      }
    }
    if (!isMarked(nb)) {
      mark(nb);
      double db = distanceSquared(nb,x,y,z);
      if (db<_dmin) {
        _dmin = db;
        _nmin = nb;
      }
    }
    if (!isMarked(nc)) {
      mark(nc);
      double dc = distanceSquared(nc,x,y,z);
      if (dc<_dmin) {
        _dmin = dc;
        _nmin = nc;
      }
    }
    if (ta!=null && !isMarked(ta))
      findNodeNaborNearest(x,y,z,node,ta);
    if (tb!=null && !isMarked(tb))
      findNodeNaborNearest(x,y,z,node,tb);
    if (tc!=null && !isMarked(tc))
      findNodeNaborNearest(x,y,z,node,tc);
  }

  /**
   * Finds the node nearest to the specified plane.
   */
  private Node findNodeNearestPlane(double a, double b, double c, double d) {

    // First, find the nearest node among the sampled nodes.
    _nmin = _nroot;
    _dmin = distanceToPlaneSquared(_nmin,a,b,c,d);
    for (Node n:_sampledNodes) {
      double dp = distanceToPlaneSquared(n,a,b,c,d);
      if (dp<_dmin) {
        _dmin = dp;
        _nmin = n;
      }
    }

    // Then, recursively search node nabors to find the nearest node.
    // Note that we clear the node marks once, so that we never compute
    // the distance to a node twice, but that we clear the tet marks
    // *inside* the loop, to ensure that we visit all tet nabors of the
    // current nearest node.
    clearNodeMarks();
    double dmin;
    do {
      clearTetMarks();
      dmin = _dmin;
      findNodeNaborNearestPlane(a,b,c,d,_nmin,_nmin._tet);
    } while (_dmin<dmin);
    return _nmin;
  }

  /**
   * Recursively finds the node nabor of the specified node that is
   * nearest to the specified plane, beginning with the nabors in the
   * specified tet, which must reference the specified node. 
   * Both tet and node marks must be cleared before calling this method.
   */
  private void findNodeNaborNearestPlane(
    double a, double b, double c, double d, Node node, Tet tet)
  {
    mark(tet);
    Node n0 = tet._n0;
    Node n1 = tet._n1;
    Node n2 = tet._n2;
    Node n3 = tet._n3;
    Tet t0 = tet._t0;
    Tet t1 = tet._t1;
    Tet t2 = tet._t2;
    Tet t3 = tet._t3;
    if (node==n0) {
      findNodeNaborNearestPlane(a,b,c,d,node,n1,n2,n3,t1,t2,t3);
    } else if (node==n1) {
      findNodeNaborNearestPlane(a,b,c,d,node,n3,n2,n0,t3,t2,t0);
    } else if (node==n2) {
      findNodeNaborNearestPlane(a,b,c,d,node,n3,n0,n1,t3,t0,t1);
    } else if (node==n3) {
      findNodeNaborNearestPlane(a,b,c,d,node,n1,n0,n2,t1,t0,t2);
    } else {
      assert false:"node is referenced by tet";
    }
  }
  private void findNodeNaborNearestPlane(
    double a, double b, double c, double d, Node node,
    Node na, Node nb, Node nc, Tet ta, Tet tb, Tet tc)
  {
    if (!isMarked(na)) {
      mark(na);
      double da = distanceToPlaneSquared(na,a,b,c,d);
      if (da<_dmin) {
        _dmin = da;
        _nmin = na;
      }
    }
    if (!isMarked(nb)) {
      mark(nb);
      double db = distanceToPlaneSquared(nb,a,b,c,d);
      if (db<_dmin) {
        _dmin = db;
        _nmin = nb;
      }
    }
    if (!isMarked(nc)) {
      mark(nc);
      double dc = distanceToPlaneSquared(nc,a,b,c,d);
      if (dc<_dmin) {
        _dmin = dc;
        _nmin = nc;
      }
    }
    if (ta!=null && !isMarked(ta))
      findNodeNaborNearestPlane(a,b,c,d,node,ta);
    if (tb!=null && !isMarked(tb))
      findNodeNaborNearestPlane(a,b,c,d,node,tb);
    if (tc!=null && !isMarked(tc))
      findNodeNaborNearestPlane(a,b,c,d,node,tc);
  }

  /**
   * Locates a point.
   */
  private PointLocation locatePoint(double x, double y, double z) {

    // If no tets yet, search the node list for an exact match.
    // Here, we use unperturbed node coordinates.
    if (_troot==null) {
      if (_nroot!=null) {
        Node node = _nroot;
        do {
          if (x==node.x() && y==node.y() && z==node.z())
            return new PointLocation(node);
          node = node._next;
        } while (node!=_nroot);
      }
      return new PointLocation(null,false);
    }

    // Otherwise, find a good tet in which to begin the recursive search.
    Node nmin = _nroot;
    double dmin = distanceSquared(nmin,x,y,z);
    for (Node n:_sampledNodes) {
      double d = distanceSquared(n,x,y,z);
      if (d<dmin) {
        dmin = d;
        nmin = n;
      }
    }
    Tet tet = nmin._tet;
    return locatePoint(tet,x,y,z);
  }

  /**
   * Recursively searches tets beginning with the specified tet,
   * to locate the point (x,y,z).
   */
  private PointLocation locatePoint(Tet tet, double x, double y, double z) {

    // Begin future searches in the specified tet.
    _troot = tet;

    // Node coordinates.
    Node n0 = tet._n0;
    Node n1 = tet._n1;
    Node n2 = tet._n2;
    Node n3 = tet._n3;
    double x0 = n0._x;
    double y0 = n0._y;
    double z0 = n0._z;
    double x1 = n1._x;
    double y1 = n1._y;
    double z1 = n1._z;
    double x2 = n2._x;
    double y2 = n2._y;
    double z2 = n2._z;
    double x3 = n3._x;
    double y3 = n3._y;
    double z3 = n3._z;

    // If exactly on a node, the search is complete.
    // We assume that this scenario is rare, but that the cost of testing
    // for it is small compared to the left-of-plane tests below.
    if (x==x0 && y==y0 && z==z0) {
      return new PointLocation(n0);
    } else if (x==x1 && y==y1 && z==z1) {
      return new PointLocation(n1);
    } else if (x==x2 && y==y2 && z==z2) {
      return new PointLocation(n2);
    } else if (x==x3 && y==y3 && z==z3) {
      return new PointLocation(n3);
    }

    // Locate the search point with respect to the four faces of the tet.
    // If any left-of-plane test is positive, then recursively search in
    // the corresponding nabor tet, unless that nabor tet is null, in which
    // case the search point lies outside the mesh and the specified tet is
    // on the convex hull and is visible from the search point.
    // TODO: experiment to determine whether it is more efficient to
    // go through the face with the most positive left-of-plane test.
    double d0 = Geometry.leftOfPlane(x1,y1,z1,x2,y2,z2,x3,y3,z3,x,y,z);
    if (d0>0.0) {
      Tet tetNabor = tet.tetNabor(n0);
      if (tetNabor!=null) {
        return locatePoint(tetNabor,x,y,z);
      } else {
        return new PointLocation(tet,false);
      }
    }
    double d1 = Geometry.leftOfPlane(x3,y3,z3,x2,y2,z2,x0,y0,z0,x,y,z);
    if (d1>0.0) {
      Tet tetNabor = tet.tetNabor(n1);
      if (tetNabor!=null) {
        return locatePoint(tetNabor,x,y,z);
      } else {
        return new PointLocation(tet,false);
      }
    }
    double d2 = Geometry.leftOfPlane(x3,y3,z3,x0,y0,z0,x1,y1,z1,x,y,z);
    if (d2>0.0) {
      Tet tetNabor = tet.tetNabor(n2);
      if (tetNabor!=null) {
        return locatePoint(tetNabor,x,y,z);
      } else {
        return new PointLocation(tet,false);
      }
    }
    double d3 = Geometry.leftOfPlane(x0,y0,z0,x2,y2,z2,x1,y1,z1,x,y,z);
    if (d3>0.0) {
      Tet tetNabor = tet.tetNabor(n3);
      if (tetNabor!=null) {
        return locatePoint(tetNabor,x,y,z);
      } else {
        return new PointLocation(tet,false);
      }
    }

    // If strictly inside the tet, the search is complete.
    if (d0<0.0 && d1<0.0 && d2<0.0 && d3<0.0) {
      return new PointLocation(tet);
    }

    // Must be on an edge or face of the specified tet.
    if (d0==0.0 && d1==0.0) {
      return new PointLocation(new Edge(tet,n2,n3));
    } else if (d0==0.0 && d2==0.0) {
      return new PointLocation(new Edge(tet,n3,n1));
    } else if (d0==0.0 && d3==0.0) {
      return new PointLocation(new Edge(tet,n1,n2));
    } else if (d1==0.0 && d2==0.0) {
      return new PointLocation(new Edge(tet,n0,n3));
    } else if (d1==0.0 && d3==0.0) {
      return new PointLocation(new Edge(tet,n2,n0));
    } else if (d2==0.0 && d3==0.0) {
      return new PointLocation(new Edge(tet,n0,n1));
    } else if (d0==0.0) {
      return new PointLocation(new Face(tet,n0));
    } else if (d1==0.0) {
      return new PointLocation(new Face(tet,n1));
    } else if (d2==0.0) {
      return new PointLocation(new Face(tet,n2));
    } else if (d3==0.0) {
      return new PointLocation(new Face(tet,n3));
    }

    // Where are we?!
    assert false:"successfully located the point";
    return null;
  }

  /**
   * Beginning with a tet that contains a node located inside the
   * mesh, recursively adds Delaunay faces to the face set.
   * The tet marks must be cleared before calling this method.
   */
  private void getDelaunayFacesInside(Node node, Tet tet) {
    if (tet!=null && !isMarked(tet)) {
      mark(tet);
      Node n0 = tet._n0;
      Node n1 = tet._n1;
      Node n2 = tet._n2;
      Node n3 = tet._n3;
      if (inSphere(n0,n1,n2,n3,node)) {
        killTet(tet);
        Tet t0 = tet._t0;
        Tet t1 = tet._t1;
        Tet t2 = tet._t2;
        Tet t3 = tet._t3;
        _faceSet.addMate(tet,n0);
        _faceSet.addMate(tet,n1);
        _faceSet.addMate(tet,n2);
        _faceSet.addMate(tet,n3);
        getDelaunayFacesInside(node,t0);
        getDelaunayFacesInside(node,t1);
        getDelaunayFacesInside(node,t2);
        getDelaunayFacesInside(node,t3);
      }
    }
  }

  /**
   * Beginning with a tet that is visible from a node located outside
   * the mesh, recursively adds Delaunay faces to the face set.
   * The tet marks must be cleared before calling this method.
   */
  private void getDelaunayFacesOutside(Node node, Tet tet) {
    if (tet!=null && !isMarked(tet)) {
      mark(tet);
      Node n0 = tet._n0;
      Node n1 = tet._n1;
      Node n2 = tet._n2;
      Node n3 = tet._n3;
      Tet t0 = tet._t0;
      Tet t1 = tet._t1;
      Tet t2 = tet._t2;
      Tet t3 = tet._t3;
      if (t0==null && leftOfPlane(n1,n2,n3,node)) {
        _faceSet.add(tet,n0);
        getDelaunayFacesOutside(node,getNextTetOnHull(tet,n1,n0));
        getDelaunayFacesOutside(node,getNextTetOnHull(tet,n2,n0));
        getDelaunayFacesOutside(node,getNextTetOnHull(tet,n3,n0));
      }
      if (t1==null && leftOfPlane(n3,n2,n0,node)) {
        _faceSet.add(tet,n1);
        getDelaunayFacesOutside(node,getNextTetOnHull(tet,n3,n1));
        getDelaunayFacesOutside(node,getNextTetOnHull(tet,n2,n1));
        getDelaunayFacesOutside(node,getNextTetOnHull(tet,n0,n1));
      }
      if (t2==null && leftOfPlane(n3,n0,n1,node)) {
        _faceSet.add(tet,n2);
        getDelaunayFacesOutside(node,getNextTetOnHull(tet,n3,n2));
        getDelaunayFacesOutside(node,getNextTetOnHull(tet,n0,n2));
        getDelaunayFacesOutside(node,getNextTetOnHull(tet,n1,n2));
      }
      if (t3==null && leftOfPlane(n1,n0,n2,node)) {
        _faceSet.add(tet,n3);
        getDelaunayFacesOutside(node,getNextTetOnHull(tet,n1,n3));
        getDelaunayFacesOutside(node,getNextTetOnHull(tet,n0,n3));
        getDelaunayFacesOutside(node,getNextTetOnHull(tet,n2,n3));
      }
      if (inSphere(n0,n1,n2,n3,node)) {
        killTet(tet);
        _faceSet.addMate(tet,n0);
        _faceSet.addMate(tet,n1);
        _faceSet.addMate(tet,n2);
        _faceSet.addMate(tet,n3);
        getDelaunayFacesOutside(node,t0);
        getDelaunayFacesOutside(node,t1);
        getDelaunayFacesOutside(node,t2);
        getDelaunayFacesOutside(node,t3);
      }
    }
  }

  /**
   * Beginning with a tet that references the specified node, recursively
   * adds the Delaunay faces opposite this node to the face set. Each
   * face corresponds to a tet that will no longer exist when the node
   * is removed. Both the node and tet marks must be cleared before 
   * calling this method.
   */
  private void getDelaunayFacesOpposite(Node node, Tet tet) {
    if (tet!=null && !isMarked(tet)) {
      mark(tet);
      killTet(tet);
      Node n0 = tet._n0;
      Node n1 = tet._n1;
      Node n2 = tet._n2;
      Node n3 = tet._n3;
      Tet t0 = tet._t0;
      Tet t1 = tet._t1;
      Tet t2 = tet._t2;
      Tet t3 = tet._t3;
      if (node==n0) {
        _faceSet.addMate(tet,n0);
        getDelaunayFacesOpposite(node,n1,n2,n3,t1,t2,t3);
      } else if (node==n1) {
        _faceSet.addMate(tet,n1);
        getDelaunayFacesOpposite(node,n3,n2,n0,t3,t2,t0);
      } else if (node==n2) {
        _faceSet.addMate(tet,n2);
        getDelaunayFacesOpposite(node,n3,n0,n1,t3,t0,t1);
      } else if (node==n3) {
        _faceSet.addMate(tet,n3);
        getDelaunayFacesOpposite(node,n1,n0,n2,t1,t0,t2);
      } else {
        assert false:"node is referenced by tet";
      }
    }
  }
  private void getDelaunayFacesOpposite(
    Node node, Node na, Node nb, Node nc, Tet ta, Tet tb, Tet tc)
  {
    if (!isMarked(na)) {
      mark(na);
      _nodeList.add(na);
    }
    if (!isMarked(nb)) {
      mark(nb);
      _nodeList.add(nb);
    }
    if (!isMarked(nc)) {
      mark(nc);
      _nodeList.add(nc);
    }
    getDelaunayFacesOpposite(node,ta);
    getDelaunayFacesOpposite(node,tb);
    getDelaunayFacesOpposite(node,tc);
  }

  /**
   * Given a tet and node on the hull, and another node, typically
   * inside (not on) the hull, gets the next tet on the hull that
   * is opposite the node on the hull. If the other node is also
   * on the hull, this method simply returns the specified tet.
   */
  private Tet getNextTetOnHull(Tet tet, Node node, Node nodeOther) {
    for (Tet tnext=tet.tetNabor(node); tnext!=null; tnext=tet.tetNabor(node)) {
      node = nodeOther;
      nodeOther = tet.nodeNabor(tnext);
      tet = tnext;
    }
    return tet;
  }

  /**
   * Finds the node nearest to the point with specified coordinates.
   * This method appears to be 2-3 times slower than findNodeNearest,
   * but it is a lot of code, so we make it private for now.
   * (Currently unused.)
   */
  public Node findNodeNearestSlow(float x, float y, float z) {

    // Clear the marks.
    clearTetMarks();
    clearNodeMarks();

    // If no tets, simply search all of the nodes to find the nearest.
    _dmin = Double.MAX_VALUE;
    _nmin = null;
    if (_troot==null) {
      if (_nroot!=null) {
        Node node = _nroot;
        do {
          updateNodeNearest(x,y,z,node);
          node = node._next;
        } while (node!=_nroot);
      }
      assert _nmin!=null;
      return _nmin;
    }

    // Otherwise, where is the point?
    PointLocation pl = locatePoint(x,y,z);

    // If the point is exactly on a node, that node is nearest.
    if (pl.isOnNode()) {
      updateNodeNearest(x,y,z,pl.node());
      return _nmin;
    }

    // Otherwise (if not on a node), search for the nearest node,
    // beginning with the tet containing or visible from the point.
    if (pl.isInside()) {
      findNodeNearestInside(x,y,z,pl.tet());
    } else {
      findNodeNearestOutside(x,y,z,pl.tet());
    }
    return _nmin;
  }

  /**
   * Recursively updates the node nearest to a point with specified (x,y,z)
   * coordinates inside the mesh, beginning with the nodes referenced by 
   * the specified tet. Both node and tet marks must be cleared before
   * calling this method. The logic is much like that for adding a node.
   * (Currently unused.)
   */
  private void findNodeNearestInside(double x, double y, double z, Tet tet)
  {
    if (tet!=null && !isMarked(tet)) {
      mark(tet);
      Node n0 = tet._n0;
      Node n1 = tet._n1;
      Node n2 = tet._n2;
      Node n3 = tet._n3;
      updateNodeNearest(x,y,z,n0);
      updateNodeNearest(x,y,z,n1);
      updateNodeNearest(x,y,z,n2);
      updateNodeNearest(x,y,z,n3);
      if (inSphere(n0,n1,n2,n3,x,y,z)) {
        Tet t0 = tet._t0;
        Tet t1 = tet._t1;
        Tet t2 = tet._t2;
        Tet t3 = tet._t3;
        findNodeNearestInside(x,y,z,t0);
        findNodeNearestInside(x,y,z,t1);
        findNodeNearestInside(x,y,z,t2);
        findNodeNearestInside(x,y,z,t3);
      }
    }
  }

  /**
   * Recursively updates the node nearest to a point with specified (x,y,z)
   * coordinates outside the mesh, beginning with the nodes referenced by 
   * the specified tet. Both node and tet marks must be cleared before
   * calling this method. The logic is much like that for adding a node.
   * (Currently unused.)
   */
  private void findNodeNearestOutside(double x, double y, double z, Tet tet) {
    if (tet!=null && !isMarked(tet)) {
      mark(tet);
      Node n0 = tet._n0;
      Node n1 = tet._n1;
      Node n2 = tet._n2;
      Node n3 = tet._n3;
      updateNodeNearest(x,y,z,n0);
      updateNodeNearest(x,y,z,n1);
      updateNodeNearest(x,y,z,n2);
      updateNodeNearest(x,y,z,n3);
      Tet t0 = tet._t0;
      Tet t1 = tet._t1;
      Tet t2 = tet._t2;
      Tet t3 = tet._t3;
      if (t0==null && leftOfPlane(n1,n2,n3,x,y,z)) {
        findNodeNearestOutside(x,y,z,getNextTetOnHull(tet,n1,n0));
        findNodeNearestOutside(x,y,z,getNextTetOnHull(tet,n2,n0));
        findNodeNearestOutside(x,y,z,getNextTetOnHull(tet,n3,n0));
      }
      if (t1==null && leftOfPlane(n3,n2,n0,x,y,z)) {
        findNodeNearestOutside(x,y,z,getNextTetOnHull(tet,n3,n1));
        findNodeNearestOutside(x,y,z,getNextTetOnHull(tet,n2,n1));
        findNodeNearestOutside(x,y,z,getNextTetOnHull(tet,n0,n1));
      }
      if (t2==null && leftOfPlane(n3,n0,n1,x,y,z)) {
        findNodeNearestOutside(x,y,z,getNextTetOnHull(tet,n3,n2));
        findNodeNearestOutside(x,y,z,getNextTetOnHull(tet,n0,n2));
        findNodeNearestOutside(x,y,z,getNextTetOnHull(tet,n1,n2));
      }
      if (t3==null && leftOfPlane(n1,n0,n2,x,y,z)) {
        findNodeNearestOutside(x,y,z,getNextTetOnHull(tet,n1,n3));
        findNodeNearestOutside(x,y,z,getNextTetOnHull(tet,n0,n3));
        findNodeNearestOutside(x,y,z,getNextTetOnHull(tet,n2,n3));
      }
      if (inSphere(n0,n1,n2,n3,x,y,z)) {
        findNodeNearestOutside(x,y,z,t0);
        findNodeNearestOutside(x,y,z,t1);
        findNodeNearestOutside(x,y,z,t2);
        findNodeNearestOutside(x,y,z,t3);
      }
    }
  }

  /**
   * If the point with specified (x,y,z) coordinates is nearer to the
   * specified node than the nearest node found so far, then replaces
   * the nearest node (and the node root) with the specified node.
   * Does nothing if the specified node is marked.
   * (Currently unused.)
   */
  private void updateNodeNearest(double x, double y, double z, Node n) {
    if (!isMarked(n)) {
      mark(n);
      double d = distanceSquared(n,x,y,z);
      if (d<_dmin) {
        _dmin = d;
        _nmin = n;
        _nroot = n;
      }
    }
  }

  /**
   * Links one tet with its tet nabor.
   */
  private void linkTets(Tet tet, Node node, Tet tetNabor, Node nodeNabor) {
    if (tet!=null) {
      if (node==tet._n0) {
        tet._t0 = tetNabor;
      } else if (node==tet._n1) {
        tet._t1 = tetNabor;
      } else if (node==tet._n2) {
        tet._t2 = tetNabor;
      } else if (node==tet._n3) {
        tet._t3 = tetNabor;
      } else {
        assert false:"node referenced by tet";
      }
    }
    if (tetNabor!=null) {
      if (nodeNabor==tetNabor._n0) {
        tetNabor._t0 = tet;
      } else if (nodeNabor==tetNabor._n1) {
        tetNabor._t1 = tet;
      } else if (nodeNabor==tetNabor._n2) {
        tetNabor._t2 = tet;
      } else if (nodeNabor==tetNabor._n3) {
        tetNabor._t3 = tet;
      } else {
        assert false:"nodeNabor referenced by tetNabor";
      }
    }
  }

  /**
   * Recursively marks the specified tet and all unmarked neighbor tets.
   */
  private void markAllTets(Tet tet) {
    tet._mark = _tetMarkRed;
    Tet t0 = tet._t0;
    if (t0!=null && t0._mark!=_tetMarkRed)
      markAllTets(t0);
    Tet t1 = tet._t1;
    if (t1!=null && t1._mark!=_tetMarkRed)
      markAllTets(t1);
    Tet t2 = tet._t2;
    if (t2!=null && t2._mark!=_tetMarkRed)
      markAllTets(t2);
    Tet t3 = tet._t3;
    if (t3!=null && t3._mark!=_tetMarkRed)
      markAllTets(t3);
  }

  /**
   * Recursively zeros the mark in the specified tet and in all neighbor
   * tets with non-zero marks.
   */
  private void zeroTetMarks(Tet tet) {
    tet._mark = 0;
    Tet t0 = tet._t0;
    if (t0!=null && t0._mark!=0)
      zeroTetMarks(t0);
    Tet t1 = tet._t1;
    if (t1!=null && t1._mark!=0)
      zeroTetMarks(t1);
    Tet t2 = tet._t2;
    if (t2!=null && t2._mark!=0)
      zeroTetMarks(t2);
    Tet t3 = tet._t3;
    if (t3!=null && t3._mark!=0)
      zeroTetMarks(t3);
  }

  private Face getFaceOnHull(Tet tet) {
    ArrayList<Tet> stack = new ArrayList<Tet>(128);
    stack.add(tet);
    while (!stack.isEmpty()) {
      Tet t = stack.remove(stack.size()-1);
      mark(t);
      if (t._t0==null)
        return new Face(t,t._n0);
      if (t._t1==null)
        return new Face(t,t._n1);
      if (t._t2==null)
        return new Face(t,t._n2);
      if (t._t3==null)
        return new Face(t,t._n3);
      if (!isMarked(t._t0))
        stack.add(t._t0);
      if (!isMarked(t._t1))
        stack.add(t._t1);
      if (!isMarked(t._t2))
        stack.add(t._t2);
      if (!isMarked(t._t3))
        stack.add(t._t3);
    }
    return null;
  }

  /**
   * Beginning with any face on the hull, recursively adds all faces
   * on the hull to the specified set of faces.
   */
  private void getFacesOnHull(Face face, HashSet<Face> faces) {
    if (!faces.contains(face)) {
      faces.add(face);
      getFacesOnHull(getNextFaceOnHull(face.nodeA(),face),faces);
      getFacesOnHull(getNextFaceOnHull(face.nodeB(),face),faces);
      getFacesOnHull(getNextFaceOnHull(face.nodeC(),face),faces);
    }
  }

  /**
   * Given a node referenced by one face of the hull, gets the next
   * face on the hull that is opposite that node.
   */
  private Face getNextFaceOnHull(Node node, Face face) {
    Tet tet = face.tetLeft();
    Node next = face.nodeLeft();
    for (Tet tnext=tet.tetNabor(node); tnext!=null; tnext=tet.tetNabor(node)) {
      node = next;
      next = tet.nodeNabor(tnext);
      tet = tnext;
    }
    return new Face(tet,node);
  }

  /**
   * Beginning with any face on the hull that is visible from the
   * specified node (not yet in the mesh), recursively adds all
   * visible faces on the hull to the specified set of faces.
   */
  /*
  private void getVisibleFacesOnHull(
    Node node, Face face, HashSet<Face> faces) {
    if (!faces.contains(face) && face.isVisibleFromNode(node)) {
      faces.add(face);
      getVisibleFacesOnHull(node,getNextFaceOnHull(face.nodeA(),face),faces);
      getVisibleFacesOnHull(node,getNextFaceOnHull(face.nodeB(),face),faces);
      getVisibleFacesOnHull(node,getNextFaceOnHull(face.nodeC(),face),faces);
    }
  }
  */

  /**
   * Returns the edge in the specified tet that references the specified 
   * nodes. If the tet does not reference the specified nodes, returns null.
   */
  private static Edge edgeOfTet(Tet tet, Node na, Node nb) {
    Node n0 = tet._n0;
    Node n1 = tet._n1;
    Node n2 = tet._n2;
    Node n3 = tet._n3;
    if (na==n0) {
      if (nb==n1) {
        return new Edge(tet,n0,n1);
      } else if (nb==n2) {
        return new Edge(tet,n0,n2);
      } else if (nb==n3) {
        return new Edge(tet,n0,n3);
      } else {
        return null;
      }
    } else if (na==n1) {
      if (nb==n0) {
        return new Edge(tet,n0,n1);
      } else if (nb==n2) {
        return new Edge(tet,n1,n2);
      } else if (nb==n3) {
        return new Edge(tet,n1,n3);
      } else {
        return null;
      }
    } else if (na==n2) {
      if (nb==n0) {
        return new Edge(tet,n0,n2);
      } else if (nb==n1) {
        return new Edge(tet,n1,n2);
      } else if (nb==n3) {
        return new Edge(tet,n2,n3);
      } else {
        return null;
      }
    } else if (na==n3) {
      if (nb==n0) {
        return new Edge(tet,n0,n3);
      } else if (nb==n1) {
        return new Edge(tet,n1,n3);
      } else if (nb==n2) {
        return new Edge(tet,n2,n3);
      } else {
        return null;
      }
    } else {
      return null;
    }
  }

  /**
   * Returns the face in the specified tet that references the specified 
   * nodes. If the tet does not reference the specified nodes, returns null.
   */
  private static Face faceOfTet(Tet tet, Node na, Node nb, Node nc) {
    Node n0 = tet._n0;
    Node n1 = tet._n1;
    Node n2 = tet._n2;
    Node n3 = tet._n3;
    if (na==n0) {
      if (nb==n1) {
        if (nc==n2) {
          return new Face(tet,n3);
        } else if (nc==n3) {
          return new Face(tet,n2);
        } else {
          return null;
        }
      } else if (nb==n2) {
        if (nc==n1) {
          return new Face(tet,n3);
        } else if (nc==n3) {
          return new Face(tet,n1);
        } else {
          return null;
        }
      } else if (nb==n3) {
        if (nc==n1) {
          return new Face(tet,n2);
        } else if (nc==n2) {
          return new Face(tet,n1);
        } else {
          return null;
        }
      } else {
        return null;
      }
    } else if (na==n1) {
      if (nb==n0) {
        if (nc==n2) {
          return new Face(tet,n3);
        } else if (nc==n3) {
          return new Face(tet,n2);
        } else {
          return null;
        }
      } else if (nb==n2) {
        if (nc==n0) {
          return new Face(tet,n3);
        } else if (nc==n3) {
          return new Face(tet,n0);
        } else {
          return null;
        }
      } else if (nb==n3) {
        if (nc==n0) {
          return new Face(tet,n2);
        } else if (nc==n2) {
          return new Face(tet,n0);
        } else {
          return null;
        }
      } else {
        return null;
      }
    } else if (na==n2) {
      if (nb==n0) {
        if (nc==n1) {
          return new Face(tet,n3);
        } else if (nc==n3) {
          return new Face(tet,n1);
        } else {
          return null;
        }
      } else if (nb==n1) {
        if (nc==n0) {
          return new Face(tet,n3);
        } else if (nc==n3) {
          return new Face(tet,n0);
        } else {
          return null;
        }
      } else if (nb==n3) {
        if (nc==n0) {
          return new Face(tet,n1);
        } else if (nc==n1) {
          return new Face(tet,n0);
        } else {
          return null;
        }
      } else {
        return null;
      }
    } else if (na==n3) {
      if (nb==n0) {
        if (nc==n1) {
          return new Face(tet,n2);
        } else if (nc==n2) {
          return new Face(tet,n1);
        } else {
          return null;
        }
      } else if (nb==n1) {
        if (nc==n0) {
          return new Face(tet,n2);
        } else if (nc==n2) {
          return new Face(tet,n0);
        } else {
          return null;
        }
      } else if (nb==n2) {
        if (nc==n0) {
          return new Face(tet,n1);
        } else if (nc==n1) {
          return new Face(tet,n0);
        } else {
          return null;
        }
      } else {
        return null;
      }
    } else {
      return null;
    }
  }

  /**
   * Returns the specified tet, if it references the specified nodes. 
   * If the tet does not reference the specified nodes, returns null.
   * (The method name is strange, but consistent with edgeOfTet and
   * faceOfTet.)
   */
  /*
  private static Tet tetOfTet(Tet tet, Node na, Node nb, Node nc, Node nd) {
    Node n0 = tet._n0;
    Node n1 = tet._n1;
    Node n2 = tet._n2;
    Node n3 = tet._n3;
    if (na==n0) {
      if (nb==n1) {
        if (nc==n2) {
          return (nd==n3)?tet:null;
        } else if (nc==n3) {
          return (nd==n2)?tet:null;
        } else {
          return null;
        }
      } else if (nb==n2) {
        if (nc==n1) {
          return (nd==n3)?tet:null;
        } else if (nc==n3) {
          return (nd==n1)?tet:null;
        } else {
          return null;
        }
      } else if (nb==n3) {
        if (nc==n1) {
          return (nd==n2)?tet:null;
        } else if (nc==n2) {
          return (nd==n1)?tet:null;
        } else {
          return null;
        }
      } else {
        return null;
      }
    } else if (na==n1) {
      if (nb==n0) {
        if (nc==n2) {
          return (nd==n3)?tet:null;
        } else if (nc==n3) {
          return (nd==n2)?tet:null;
        } else {
          return null;
        }
      } else if (nb==n2) {
        if (nc==n0) {
          return (nd==n3)?tet:null;
        } else if (nc==n3) {
          return (nd==n0)?tet:null;
        } else {
          return null;
        }
      } else if (nb==n3) {
        if (nc==n0) {
          return (nd==n2)?tet:null;
        } else if (nc==n2) {
          return (nd==n0)?tet:null;
        } else {
          return null;
        }
      } else {
        return null;
      }
    } else if (na==n2) {
      if (nb==n0) {
        if (nc==n1) {
          return (nd==n3)?tet:null;
        } else if (nc==n3) {
          return (nd==n1)?tet:null;
        } else {
          return null;
        }
      } else if (nb==n1) {
        if (nc==n0) {
          return (nd==n3)?tet:null;
        } else if (nc==n3) {
          return (nd==n0)?tet:null;
        } else {
          return null;
        }
      } else if (nb==n3) {
        if (nc==n0) {
          return (nd==n1)?tet:null;
        } else if (nc==n1) {
          return (nd==n0)?tet:null;
        } else {
          return null;
        }
      } else {
        return null;
      }
    } else if (na==n3) {
      if (nb==n0) {
        if (nc==n1) {
          return (nd==n2)?tet:null;
        } else if (nc==n2) {
          return (nd==n1)?tet:null;
        } else {
          return null;
        }
      } else if (nb==n1) {
        if (nc==n0) {
          return (nd==n2)?tet:null;
        } else if (nc==n2) {
          return (nd==n0)?tet:null;
        } else {
          return null;
        }
      } else if (nb==n2) {
        if (nc==n0) {
          return (nd==n1)?tet:null;
        } else if (nc==n1) {
          return (nd==n0)?tet:null;
        } else {
          return null;
        }
      } else {
        return null;
      }
    } else {
      return null;
    }
  }
  */

  /**
   * Determines whether a specified tet has the specified nodes in order.
   */
  private static boolean nodesInOrder(
    Tet tet, Node na, Node nb, Node nc, Node nd) 
  {
    Node n0 = tet._n0;
    Node n1 = tet._n1;
    Node n2 = tet._n2;
    Node n3 = tet._n3;
    if (na==n0) {
      return nb==n1 && nc==n2 && nd==n3 ||
             nb==n2 && nc==n3 && nd==n1 ||
             nb==n3 && nc==n1 && nd==n2;
    } else if (na==n1) {
      return nb==n2 && nc==n0 && nd==n3 ||
             nb==n3 && nc==n2 && nd==n0 ||
             nb==n0 && nc==n3 && nd==n2;
    } else if (na==n2) {
      return nb==n3 && nc==n0 && nd==n1 ||
             nb==n0 && nc==n1 && nd==n3 ||
             nb==n1 && nc==n3 && nd==n0;
    } else if (na==n3) {
      return nb==n0 && nc==n2 && nd==n1 ||
             nb==n1 && nc==n0 && nd==n2 ||
             nb==n2 && nc==n1 && nd==n0;
    } else {
      assert false:"tet references na";
      return false;
    }
  }

  /**
   * Returns the other (fourth) node in the specified tet that references the 
   * specified three nodes. If the tet does not reference the specified nodes, 
   * returns null.
   */
  private static Node otherNode(Tet tet, Node na, Node nb, Node nc) {
    Node n0 = tet._n0;
    Node n1 = tet._n1;
    Node n2 = tet._n2;
    Node n3 = tet._n3;
    if (na==n0) {
      if (nb==n1) {
        if (nc==n2) {
          return n3;
        } else if (nc==n3) {
          return n2;
        } else {
          return null;
        }
      } else if (nb==n2) {
        if (nc==n1) {
          return n3;
        } else if (nc==n3) {
          return n1;
        } else {
          return null;
        }
      } else if (nb==n3) {
        if (nc==n1) {
          return n2;
        } else if (nc==n2) {
          return n1;
        } else {
          return null;
        }
      } else {
        return null;
      }
    } else if (na==n1) {
      if (nb==n0) {
        if (nc==n2) {
          return n3;
        } else if (nc==n3) {
          return n2;
        } else {
          return null;
        }
      } else if (nb==n2) {
        if (nc==n0) {
          return n3;
        } else if (nc==n3) {
          return n0;
        } else {
          return null;
        }
      } else if (nb==n3) {
        if (nc==n0) {
          return n2;
        } else if (nc==n2) {
          return n0;
        } else {
          return null;
        }
      } else {
        return null;
      }
    } else if (na==n2) {
      if (nb==n0) {
        if (nc==n1) {
          return n3;
        } else if (nc==n3) {
          return n1;
        } else {
          return null;
        }
      } else if (nb==n1) {
        if (nc==n0) {
          return n3;
        } else if (nc==n3) {
          return n0;
        } else {
          return null;
        }
      } else if (nb==n3) {
        if (nc==n0) {
          return n1;
        } else if (nc==n1) {
          return n0;
        } else {
          return null;
        }
      } else {
        return null;
      }
    } else if (na==n3) {
      if (nb==n0) {
        if (nc==n1) {
          return n2;
        } else if (nc==n2) {
          return n1;
        } else {
          return null;
        }
      } else if (nb==n1) {
        if (nc==n0) {
          return n2;
        } else if (nc==n2) {
          return n0;
        } else {
          return null;
        }
      } else if (nb==n2) {
        if (nc==n0) {
          return n1;
        } else if (nc==n1) {
          return n0;
        } else {
          return null;
        }
      } else {
        return null;
      }
    } else {
      return null;
    }
  }

  /**
   * Marks the specified tet inner or outer.
   * The outer box must valid.
   */
  private synchronized void markTetInnerOrOuter(Tet tet) {
    assert _xminOuter<_xmaxOuter:"outer box is valid";
    assert _yminOuter<_ymaxOuter:"outer box is valid";
    assert _zminOuter<_zmaxOuter:"outer box is valid";
    double[] po = {0.0,0.0,0.0};
    double s = tet.centerSphere(po);
    double r = sqrt(s);
    double xo = po[0];
    double yo = po[1];
    double zo = po[2];
    if (xo-r>=_xminOuter &&
        yo-r>=_yminOuter &&
        zo-r>=_zminOuter &&
        xo+r<=_xmaxOuter &&
        yo+r<=_ymaxOuter &&
        zo+r<=_zmaxOuter) {
      tet.setInner();
      tet.clearOuter();
    } else {
      tet.setOuter();
      tet.clearInner();
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

  private void fireTetAdded(Tet tet) {
    ++_version;
    if (_ntetListeners>0) {
      Object[] list = _listeners.getListenerList();
      for (int i=list.length-2; i>=0; i-=2)
        if (list[i]==TetListener.class)
          ((TetListener)list[i+1]).tetAdded(this,tet);
    }
  }

  private void fireTetRemoved(Tet tet) {
    ++_version;
    if (_ntetListeners>0) {
      Object[] list = _listeners.getListenerList();
      for (int i=list.length-2; i>=0; i-=2)
        if (list[i]==TetListener.class)
          ((TetListener)list[i+1]).tetRemoved(this,tet);
    }
  }

  /**
   * Validates the specified node.
   */
  private void validate(Node node) {
    Check.state(node==node._prev._next,"node==node._prev._next");
    Check.state(node==node._next._prev,"node==node._next._prev");
    Tet tet = node.tet();
    if (_troot!=null) {
      Check.state(tet!=null,"tet!=null");
      Check.state(node==tet.nodeA() ||
                  node==tet.nodeB() ||
                  node==tet.nodeC() ||
                  node==tet.nodeD(),
                  "node is one of tet nodes");
    }
  }

  /**
   * Validates the specified tet.
   */
  private void validate(Tet tet) {
    Node na = tet.nodeA();
    Node nb = tet.nodeB();
    Node nc = tet.nodeC();
    Node nd = tet.nodeD();
    if (!leftOfPlane(na,nb,nc,nd)) {
      trace("xa="+na._x+" ya="+na._y+" za="+na._z);
      trace("xb="+nb._x+" yb="+nb._y+" zb="+nb._z);
      trace("xc="+nc._x+" yc="+nc._y+" zc="+nc._z);
      trace("xd="+nd._x+" yd="+nd._y+" zd="+nd._z);
    }
    Check.state(leftOfPlane(na,nb,nc,nd),"leftOfPlane(na,nb,nc,nd)");
    validate(na);
    validate(nb);
    validate(nc);
    validate(nd);
    Tet ta = tet.tetA();
    Tet tb = tet.tetB();
    Tet tc = tet.tetC();
    Tet td = tet.tetD();
    if (ta!=null)
      Check.state(ta.tetNabor(tet.nodeNabor(ta))==tet,"a nabor ok");
    if (tb!=null)
      Check.state(tb.tetNabor(tet.nodeNabor(tb))==tet,"b nabor ok");
    if (tc!=null)
      Check.state(tc.tetNabor(tet.nodeNabor(tc))==tet,"c nabor ok");
    if (td!=null)
      Check.state(td.tetNabor(tet.nodeNabor(td))==tet,"d nabor ok");
  }

  private static void trace(String s) {
    if (TRACE) System.out.println(s);
  }
  static final boolean DEBUG = false;
  static final boolean TRACE = false;


  ///////////////////////////////////////////////////////////////////////////
  // Private classes for internal use only. (Big, so we put them at the end.)

  /**
   * A set of tet faces, specifically tuned for Delaunay tet meshing.
   * <p>
   * In the Bowyer-Watson-like method of Delaunay tetrahedralization
   * used here, star-shaped polyhedra are constructed and filled by
   * adding faces of non-Delaunay tets to this set. As faces are added,
   * those internal to the star-shaped polyhedron are matched with their
   * mates and removed from this set, leaving only the Delaunay faces on
   * the polyhedral surface, which is then filled with Delaunay tetrahedra.
   * <p>
   * The faces stored in this set are defined as in the class TetMesh.Face.
   * although, for efficiency, this set does not store objects of this class.
   * Rather, this set stores the components of a face (the nodes A, B, C,
   * and D, and the tet ABCD) separately in arrays. This scheme significantly
   * reduces the cost of creating and adding new faces, and then removing
   * them as they are matched by their mates. This scheme also makes this set
   * unsuitable for general use.
   */
  private static class FaceSet {

    /**
     * The current face, typically, the face added, or the mate removed.
     */
    Node a,b,c,d;
    Tet abcd;

    /**
     * Constructs an empty set with specified initial capacity and load factor.
     * The capacity will be rounded up to a power of two. The number of faces
     * that can be added without rehashing is approximately capacity*factor.
     * @param capacity the initial capacity.
     * @param factor the load factor.
     */
    FaceSet(int capacity, double factor) {
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
      _d = new Node[_nmax];
      _abcd = new Tet[_nmax];
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
     * Adds the face of the specified tet that is opposite the specified node,
     * unless its mate is in the set, in which case that mate is removed.
     * Sets the current face to the face added or the mate removed.
     * @param tet the tet that references nodes in the face.
     * @param node the other node in the tet that is not in the face.
     * @return true, if the face was added; false, if the mate was removed.
     */
    boolean add(Tet tet, Node node) {
      if (node==tet._n0) {
        return add(tet._n1,tet._n3,tet._n2,node,tet);
      } else if (node==tet._n1) {
        return add(tet._n2,tet._n3,tet._n0,node,tet);
      } else if (node==tet._n2) {
        return add(tet._n3,tet._n1,tet._n0,node,tet);
      } else if (node==tet._n3) {
        return add(tet._n0,tet._n1,tet._n2,node,tet);
      } else {
        assert false:"node is referenced by tet";
        return false;
      }
    }

    /**
     * Adds the mate of the face of the specified tet that is opposite
     * the specified node, unless the face is in the set, in which case
     * that face is removed. Sets the current face to the mate added or
     * the face removed.
     * @param tet the tet that references the nodes in the face.
     * @param node the other node in the tet that is not in the face.
     * @return true, if the mate was added; false, if the face was removed.
     */
    boolean addMate(Tet tet, Node node) {
      Tet tetNabor = tet.tetNabor(node);
      Node nodeNabor = (tetNabor!=null)?tet.nodeNabor(tetNabor):null;
      if (node==tet._n0) {
        return add(tet._n1,tet._n2,tet._n3,nodeNabor,tetNabor);
      } else if (node==tet._n1) {
        return add(tet._n3,tet._n2,tet._n0,nodeNabor,tetNabor);
      } else if (node==tet._n2) {
        return add(tet._n3,tet._n0,tet._n1,nodeNabor,tetNabor);
      } else if (node==tet._n3) {
        return add(tet._n1,tet._n0,tet._n2,nodeNabor,tetNabor);
      } else {
        assert false:"node is referenced by tet";
        return false;
      }
    }

    /**
     * If the set is not empty, removes a face from the set.
     * Sets the current face to the face removed.
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
     * Sets the current face to the first face in the set.
     * <em>While iterating over faces, the set should not be modified.</em>
     * @return true, if the set is not empty; false, otherwise.
     */
    boolean first() {
      _index = -1;
      return next();
    }

    /**
     * Sets the current face to the next face in the set.
     * <em>While iterating over faces, the set should not be modified.</em>
     * @return true, if the set contains another face; false, otherwise.
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

    private Node[] _a,_b,_c,_d; // arrays of nodes.
    private Tet[] _abcd; // array of tets.
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
    private int hash(Node a, Node b, Node c) {
      int key = a._hash^b._hash^c._hash;
      // Knuth, v. 3, 509-510. Randomize the 31 low-order bits of c*key
      // and return the highest nbits (where nbits <= 30) bits of these.
      // The constant c = 1327217885 approximates 2^31 * (sqrt(5)-1)/2.
      return ((1327217885*key)>>_shift)&_mask;
    }

    /**
     * Returns the index corresponding to the mate of the specified face,
     * or, if the mate is not found, the index of an empty slot in the set.
     */
    private int indexOfMate(Node a, Node b, Node c) {
      int i = hash(a,b,c);
      while (_filled[i]) {
        Node ai = _a[i];
        Node bi = _b[i];
        Node ci = _c[i];
        if ((a==ai && b==ci && c==bi) ||
            (a==bi && b==ai && c==ci) ||
            (a==ci && b==bi && c==ai))
          return i;
        i = (i-1)&_mask;
      }
      return i;
    }

    /**
     * Sets the current face.
     */
    private void setCurrent() {
      this.a = _a[_index];
      this.b = _b[_index];
      this.c = _c[_index];
      this.d = _d[_index];
      this.abcd = _abcd[_index];
    }

    /**
     * If the set does not contain the mate of the specified face, adds
     * the face to the set, remembers the face added, and returns true.
     * Otherwise, if the set already contains the mate, removes the mate
     * from the set, remembers the mate removed, and returns false.
     */
    private boolean add(Node a, Node b, Node c, Node d, Tet abcd) {
      _index = indexOfMate(a,b,c);
      if (_filled[_index]) {
        setCurrent();
        remove(_index);
        return false;
      } else {
        _a[_index] = a;
        _b[_index] = b;
        _c[_index] = c;
        _d[_index] = d;
        _abcd[_index] = abcd;
        _filled[_index] = true;
        ++_n;
        if (_n>_nmax*_factor && _nmax<MAX_CAPACITY)
          doubleCapacity();
        setCurrent();
        return true;
      }
    }

    /**
     * Removes the face with specified index.
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
          r = hash(_a[i],_b[i],_c[i]);
        } while ((i<=r && r<j) || (r<j && j<i) || (j<i && i<=r));
        _a[j] = _a[i];
        _b[j] = _b[i];
        _c[j] = _c[i];
        _d[j] = _d[i];
        _abcd[j] = _abcd[i];
        _filled[j] = _filled[i];
      }
    }

    /**
     * Doubles the capacity of the set.
     */
    private void doubleCapacity() {
      //trace("FaceSet.doubleCapacity");
      FaceSet set = new FaceSet(2*_nmax,_factor);
      if (_n>0) {
        for (int i=0; i<_nmax; ++i) {
          if (_filled[i])
            set.add(_a[i],_b[i],_c[i],_d[i],_abcd[i]);
        }
      }
      _a = set._a;
      _b = set._b;
      _c = set._c;
      _d = set._d;
      _abcd = set._abcd;
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
   * A set of tet edges, used with a face set in Delaunay tet meshing.
   * An edge set works like a face set, but supports fewer operations.
   * Although the face set alone is sufficient for meshing, the edge
   * set improves the efficiency of the method addNode; edges can be
   * added/removed faster than faces.
   */
  private static class EdgeSet {

    /**
     * The current edge, typically, the edge added, or the mate removed.
     */
    Node a,b,c;
    Tet nabc;

    /**
     * Constructs an empty set with specified initial capacity and load factor.
     * The capacity will be rounded up to a power of two. The number of edges
     * that can be added without rehashing is approximately capacity*factor.
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
      _nabc = new Tet[_nmax];
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
     * If the set does not contain the mate of the specified edge, adds
     * the edge to the set, remembers the edge added, and returns true.
     * Otherwise, if the set already contains the mate, removes the mate
     * from the set, remembers the mate removed, and returns false.
     */
    boolean add(Node a, Node b, Node c, Tet nabc) {
      _index = indexOfMate(a,b);
      if (_filled[_index]) {
        setCurrent();
        remove(_index);
        return false;
      } else {
        _a[_index] = a;
        _b[_index] = b;
        _c[_index] = c;
        _nabc[_index] = nabc;
        _filled[_index] = true;
        ++_n;
        if (_n>_nmax*_factor && _nmax<MAX_CAPACITY)
          doubleCapacity();
        setCurrent();
        return true;
      }
    }
    
    int size() {
      return _n;
    }

    private static final int MAX_SHIFT = 30; // max bit shift.
    private static final int MAX_CAPACITY = 1<<MAX_SHIFT; // 2^MAX_SHIFT

    private Node[] _a,_b,_c; // arrays of nodes.
    private Tet[] _nabc; // array of tets.
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
      this.nabc = _nabc[_index];
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
        _nabc[j] = _nabc[i];
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
            set.add(_a[i],_b[i],_c[i],_nabc[i]);
        }
      }
      _a = set._a;
      _b = set._b;
      _c = set._c;
      _nabc = set._nabc;
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
        node._z = in.readDouble();
        int nvalue = in.readInt();
        node._values = new Object[nvalue];
        for (int ivalue=0; ivalue<nvalue; ++ivalue) {
          Object value = in.readObject();
          node._values[ivalue] = value;
        }
      }

      // Tets.
      int ntet = _ntet = in.readInt();
      Tet[] tets = new Tet[ntet];
      for (int itet=0; itet<ntet; ++itet) {
        Tet tet = tets[itet] = (Tet)in.readObject();
        tet._quality = -1.0;
      }

      // Nodes linkage.
      _nroot = (Node)in.readObject();
      for (int inode=0; inode<nnode; ++inode) {
        Node node = nodes[inode];
        node._prev = (Node)in.readObject();
        node._next = (Node)in.readObject();
        node._tet = (Tet)in.readObject();
      }

      // Tets linkage.
      _troot = (Tet)in.readObject();
      for (int itet=0; itet<ntet; ++itet) {
        Tet tet = tets[itet];
        tet._n0 = (Node)in.readObject();
        tet._n1 = (Node)in.readObject();
        tet._n2 = (Node)in.readObject();
        tet._n3 = (Node)in.readObject();
        tet._t0 = (Tet)in.readObject();
        tet._t1 = (Tet)in.readObject();
        tet._t2 = (Tet)in.readObject();
        tet._t3 = (Tet)in.readObject();
      }

      // Outer box.
      _outerEnabled = in.readBoolean();
      _xminOuter = in.readDouble();
      _yminOuter = in.readDouble();
      _zminOuter = in.readDouble();
      _xmaxOuter = in.readDouble();
      _ymaxOuter = in.readDouble();
      _zmaxOuter = in.readDouble();

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
      out.writeDouble(node._z);
      int nvalue = node._values.length;
      out.writeInt(nvalue);
      for (int ivalue=0; ivalue<nvalue; ++ivalue) {
        Object value = node._values[ivalue];
        out.writeObject((value instanceof Serializable)?value:null);
      }
    }

    // Tets.
    int ntet = _ntet;
    out.writeInt(ntet);
    Tet[] tets = new Tet[ntet];
    TetIterator ti = getTets();
    for (int itet=0; itet<ntet; ++itet) {
      Tet tet = tets[itet] = ti.next();
      out.writeObject(tet);
    }

    // Nodes linkage.
    out.writeObject(_nroot);
    for (int inode=0; inode<nnode; ++inode) {
      Node node = nodes[inode];
      out.writeObject(node._prev);
      out.writeObject(node._next);
      out.writeObject(node._tet);
    }

    // Tets linkage.
    out.writeObject(_troot);
    for (int itet=0; itet<ntet; ++itet) {
      Tet tet = tets[itet];
      out.writeObject(tet._n0);
      out.writeObject(tet._n1);
      out.writeObject(tet._n2);
      out.writeObject(tet._n3);
      out.writeObject(tet._t0);
      out.writeObject(tet._t1);
      out.writeObject(tet._t2);
      out.writeObject(tet._t3);
    }

    // Outer box.
    out.writeBoolean(_outerEnabled);
    out.writeDouble(_xminOuter);
    out.writeDouble(_yminOuter);
    out.writeDouble(_zminOuter);
    out.writeDouble(_xmaxOuter);
    out.writeDouble(_ymaxOuter);
    out.writeDouble(_zmaxOuter);

    // Property maps and values.
    out.writeInt(_nnodeValues);
    out.writeInt(_lnodeValues);
    out.writeObject(_nodePropertyMaps);
  }

  /**
   * Randomly samples nodes to facilitate fast searches. Following
   * Mucke et al., 1996, the number of samples is proportional to
   * N^(1/4), where N equals the number of nodes. The factor of 2
   * below corresponds to Mucke et al.'s scale factor 0.5.
   */
  private void sampleNodes() {
    Random random = new Random();
    _sampledNodes.clear();
    int nsamp = 2*(int)pow(_nnode,0.25);
    Node node = _nroot;
    while (_sampledNodes.size()<nsamp) {
      int nskip = 1+random.nextInt(_nnode/2);
      while (--nskip>0)
        node = node._next;
      _sampledNodes.add(node);
    }
  }
}
