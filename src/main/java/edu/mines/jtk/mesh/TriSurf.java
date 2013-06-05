/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mesh;

import static java.lang.Math.*;
import java.util.*;

import edu.mines.jtk.util.Check;

/** 
 * A 3-D triangulated manifold oriented surface, possibly with boundary.
 * <p>
 * This class currently enables construction of a surface from a set of 
 * points, using the algorithm of Cohen-Steiner and Da, 2002, A greedy
 * Delaunay based surface reconstruction algorithm: The Visual Computer,
 * v. 20, p. 4-16.
 *
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.06.14, 2007.01.12
 */
public class TriSurf {

  /**
   * A node, which can be added or removed from the surface.
   */
  public static class Node {

    /**
     * An integer index associated with this node.
     * Intended for external use only; the surface does not use it.
     */
    public int index;

    /**
     * A data object associated with this node.
     * Intended for external use only; the surface does not use it.
     */
    public Object data;

    /**
     * Constructs a node with specified coordinates.
     * @param x the x coordinate.
     * @param y the y coordinate.
     * @param z the z coordinate.
     */
    public Node(float x, float y, float z) {
      _meshNode = new TetMesh.Node(x,y,z);
      _meshNode.data = this;
    }

    /**
     * Returns the x coordinate of this node.
     * @return the x coordinate.
     */
    public final float x() {
      return _meshNode.x();
    }

    /**
     * Returns the y coordinate of this node.
     * @return the y coordinate.
     */
    public final float y() {
      return _meshNode.y();
    }

    /**
     * Returns the z coordinate of this node.
     * @return the z coordinate.
     */
    public final float z() {
      return _meshNode.z();
    }

    /**
     * Determines whether this node is in the surface.
     * @return true, if in surface; false, otherwise.
     */
    public boolean isInSurface() {
      return _face!=null;
    }

    /**
     * Determines whether this node is on the surface boundary.
     * @return true, if on the boundary; false, otherwise.
     */
    public boolean isOnBoundary() {
      return _edgeBefore!=null;
    }

    /**
     * Returns the edge before this node on the surface boundary.
     * Returns null, if this node is not on the surface boundary. 
     * If on the surface boundary, this is node B of the returned edge.
     * @return previous edge; null if node not on surface boundary.
     */
    public Edge edgeBefore() {
      return _edgeBefore;
    }

    /**
     * Returns the edge after this node on the surface boundary.
     * Returns null, if this node is not on the surface boundary. 
     * If on the surface boundary, this is node A of the returned edge.
     * @return next edge; null if node not on surface boundary.
     */
    public Edge edgeAfter() {
      return _edgeAfter;
    }

    /**
     * Returns the area-weighted average normal vector for this node.
     * @return array containing the {X,Y,Z} components of the normal vector.
     */
    public float[] normalVector() {
      float[] vn = new float[3];
      normalVector(vn);
      return vn;
    }

    /**
     * Computes the area-weighted average normal vector for this node.
     * @param vn array to contain the {X,Y,Z} components of the normal vector.
     */
    public void normalVector(float[] vn) {
      vn[0] = vn[1] = vn[2] = 0.0f;
      FaceIterator fi = getFaces();
      while (fi.hasNext()) {
        Face face = fi.next();
        accNormalVector(face,vn);
      }
      float x = vn[0];
      float y = vn[1];
      float z = vn[2];
      float s = 1.0f/(float)sqrt(x*x+y*y+z*z);
      vn[0] *= s;
      vn[1] *= s;
      vn[2] *= s;
    }

    /**
     * Returns the number of faces that reference this node.
     * @return the number of faces.
     */
    public int countFaces() {
      int nface = 0;
      FaceIterator fi = getFaces();
      while (fi.hasNext()) {
        fi.next();
        ++nface;
      }
      return nface;
    }
    
    /**
     * Gets an iterator for all faces that reference this node.
     * @return the iterator.
     */
    public FaceIterator getFaces() {
      return new FaceIterator() {
        public boolean hasNext() {
          return _next!=null;
        }
        public Face next() {
          if (_next==null)
            throw new NoSuchElementException();
          Face face = _next;
          loadNext();
          return face;
        }
        private Face _next = _face;
        private boolean _ccw = true;
        private void loadNext() {
          if (_ccw) {
            _next = faceNext(_next);
            if (_next==null) {
              _ccw = false;
              _next = _face;
            } else if (_next==_face) {
              _next = null;
            }
          }
          if (!_ccw) {
            _next = facePrev(_next);
          }
        }
      };
    }

    public String toString() {
      return _meshNode.toString();
    }

    private TetMesh.Node _meshNode;
    private Face _face; // null if node not in surface
    private Edge _edgeBefore; // non-null if on surface boundary
    private Edge _edgeAfter; // non-null if on surface boundary
    private void validate() {
      assert _meshNode!=null;
      assert _face==null || _face.references(this);
      if (_edgeBefore==null) {
        assert _edgeAfter==null;
      } else {
        assert this==_edgeBefore.nodeB();
        assert this==_edgeAfter.nodeA();
        assert this==_edgeBefore.nodeA().edgeAfter().nodeB();
        assert this==_edgeAfter.nodeB().edgeBefore().nodeA();
      }
      assert _edgeBefore==null && _edgeAfter==null ||
             _edgeBefore!=null && this==_edgeBefore.nodeB() && 
             _edgeAfter!=null && this==_edgeAfter.nodeA();
    }
    private void init() {
      _face = null;
      _edgeBefore = null;
      _edgeAfter = null;
    }
    private void setFace(Face face) {
      _face = face;
    }
    private void setEdgeBefore(Edge edgeBefore) {
      _edgeBefore = edgeBefore;
    }
    private void setEdgeAfter(Edge edgeAfter) {
      _edgeAfter = edgeAfter;
    }
    private Face face() {
      return _face;
    }
    private Face faceNext(Face face) {
      if (this==face.nodeA()) {
        return face.faceB();
      } else if (this==face.nodeB()) {
        return face.faceC();
      } else {
        return face.faceA();
      }
    }
    private Face facePrev(Face face) {
      if (this==face.nodeA()) {
        return face.faceC();
      } else if (this==face.nodeB()) {
        return face.faceA();
      } else {
        return face.faceB();
      }
    }
    private static void accNormalVector(Face face, float[] v) {
      Node na = face.nodeA();
      Node nb = face.nodeB();
      Node nc = face.nodeC();
      float xa = na.x();
      float ya = na.y();
      float za = na.z();
      float xb = nb.x();
      float yb = nb.y();
      float zb = nb.z();
      float xc = nc.x();
      float yc = nc.y();
      float zc = nc.z();
      float x0 = xc-xa;
      float y0 = yc-ya;
      float z0 = zc-za;
      float x1 = xa-xb;
      float y1 = ya-yb;
      float z1 = za-zb;
      v[0] += y0*z1-y1*z0;
      v[1] += x1*z0-x0*z1;
      v[2] += x0*y1-x1*y0;
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
   * A directed edge.
   * <p>
   * An edge is specified by two nodes A and B. The order of these nodes 
   * is significant. An edge is directed from A to B.
   * <p>
   * Every edge has a mate. An edge and its mate reference the same two
   * nodes, but in the opposite order, so they have opposite directions.
   * Therefore, an edge does not equal its mate.
   * <p>
   * An edge within the surface has a left and right face. An edge on the 
   * boundary of the surface has a right face and a null left face. An edge
   * on the boundary is linked to the previous and next edge on the boundary.
   *
   */
  public static class Edge {
    public Node nodeA() {
      return (Node)_meshEdge.nodeA().data;
    }
    public Node nodeB() {
      return (Node)_meshEdge.nodeB().data;
    }
    public Face faceLeft() {
      return _faceLeft;
    }
    public Face faceRight() {
      return _faceRight;
    }
    public Node nodeLeft() {
      return (_faceLeft!=null)?otherNode(_faceLeft,nodeA(),nodeB()):null;
    }
    public Node nodeRight() {
      return (_faceRight!=null)?otherNode(_faceRight,nodeA(),nodeB()):null;
    }
    public Edge edgeBefore() {
      return nodeA()._edgeBefore;
    }
    public Edge edgeAfter() {
      return nodeB()._edgeAfter;
    }
    public Edge mate() {
      return new Edge(_meshEdge.mate(),_faceRight);
    }
    public boolean isInSurface() {
      return _faceRight!=null;
    }
    public boolean isOnBoundary() {
      return _faceLeft==null;
    }
    public boolean equals(Object object) {
      if (object==this) 
        return true;
      if (object!=null && object.getClass()==getClass()) {
        Edge other = (Edge)object;
        return other.nodeA()==nodeA() && other.nodeB()==nodeB();
      }
      return false;
    }
    public int hashCode() {
      return nodeA().hashCode()^nodeB().hashCode();
    }
    private TetMesh.Edge _meshEdge;
    private Face _faceLeft; // null if edge on surface boundary
    private Face _faceRight; // null if edge not in surface
    private void validate() {
      assert _meshEdge!=null;
      assert _faceLeft==null || _faceLeft.references(nodeA(),nodeB());
      assert _faceRight==null || _faceRight.references(nodeA(),nodeB());
    }
    private Edge(TetMesh.Edge meshEdge, Face face) {
      _meshEdge = meshEdge;
      Node nodeA = (Node)meshEdge.nodeA().data;
      Node nodeB = (Node)meshEdge.nodeB().data;
      Node nodeC = (face!=null)?otherNode(face,nodeA,nodeB):null;
      Check.argument(face==null || nodeC!=null,"face references edge");
      if (nodeC!=null) {
        if (nodesInOrder(face,nodeA,nodeB,nodeC)) {
          _faceLeft = face;
          _faceRight = face.faceNabor(nodeC);
        } else {
          _faceLeft = face.faceNabor(nodeC);
          _faceRight = face;
        }
      }
    }
  }

  /**
   * A type-safe iterator for edges.
   */
  public interface EdgeIterator {
    public boolean hasNext();
    public Edge next();
  }

  /**
   * One triangular face in the surface.
   * Each face references three nodes (A, B, and C), and up to three
   * face neighbors (nabors) opposite those nodes. A null nabor denotes
   * an edge (opposite the corresponding node) on the surface boundary.
   * The nodes A, B, and C are in CCW order.
   */
  public static class Face {

    /**
     * An integer index associated with this face.
     * Intended for external use only; the surface does not use it.
     */
    public int index;

    /**
     * A data object associated with this face.
     * Intended for external use only; the surface does not use it.
     */
    public Object data;

    /**
     * Returns the node A referenced by this face.
     * @return the node A.
     */
    public final Node nodeA() {
      return (Node)_meshFace.nodeA().data;
    }

    /**
     * Returns the node B referenced by this face.
     * @return the node B.
     */
    public final Node nodeB() {
      return (Node)_meshFace.nodeB().data;
    }

    /**
     * Returns the node C referenced by this face.
     * @return the node C.
     */
    public final Node nodeC() {
      return (Node)_meshFace.nodeC().data;
    }

    /**
     * Returns the face nabor A (opposite node A) referenced by this face.
     * @return the face nabor A.
     */
    public final Face faceA() {
      return _faceA;
    }

    /**
     * Returns the face nabor B (opposite node B) referenced by this face.
     * @return the face nabor B.
     */
    public final Face faceB() {
      return _faceB;
    }

    /**
     * Returns the face nabor C (opposite node C) referenced by this face.
     * @return the face nabor C.
     */
    public final Face faceC() {
      return _faceC;
    }

    /**
     * Returns the mate of this face.
     * @return the mate of this face.
     */
    public Face mate() {
      return new Face(_meshFace.mate());
    }

    /**
     * Returns the node referenced by this face that is nearest to
     * the point with specified coordinates.
     * @param x the x coordinate.
     * @param y the y coordinate.
     * @param z the z coordinate.
     * @return the node nearest to the point (x,y,z).
     */
    public final Node nodeNearest(float x, float y, float z) {
      Node na = nodeA();
      Node nb = nodeB();
      Node nc = nodeC();
      double da = distanceSquared(na,x,y,z);
      double db = distanceSquared(nb,x,y,z);
      double dc = distanceSquared(nc,x,y,z);
      double dmin = da;
      Node nmin = na;
      if (db<dmin) {
        dmin = db;
        nmin = nb;
      }
      if (dc<dmin) {
        dmin = dc;
        nmin = nc;
      }
      return nmin;
    }

    /**
     * Gets the face nabor opposite the specified node.
     */
    public final Face faceNabor(Node node) {
      if (node==nodeA()) return _faceA;
      if (node==nodeB()) return _faceB;
      if (node==nodeC()) return _faceC;
      Check.argument(false,"node is referenced by face");
      return null;
    }

    /**
     * Gets the node in the specified face nabor that is opposite this face.
     */
    public final Node nodeNabor(Face faceNabor) {
      if (faceNabor._faceA==this) return faceNabor.nodeA();
      if (faceNabor._faceB==this) return faceNabor.nodeB();
      if (faceNabor._faceC==this) return faceNabor.nodeC();
      Check.argument(false,"faceNabor is a nabor of face");
      return null;
    }

    /**
     * Computes the circumcenter of this face.
     * @param cc array of circumcenter coordinates {xc,yc,zc}.
     * @return radius-squared of circumcircle.
     */
    public double centerCircle(double[] cc) {
      Node na = nodeA();
      Node nb = nodeB();
      Node nc = nodeC();
      double xa = na.x();
      double ya = na.y();
      double za = na.z();
      double xb = nb.x();
      double yb = nb.y();
      double zb = nb.z();
      double xc = nc.x();
      double yc = nc.y();
      double zc = nc.z();
      Geometry.centerCircle3D(xa,ya,za,xb,yb,zb,xc,yc,zc,cc);
      double xcc = cc[0];
      double ycc = cc[1];
      double zcc = cc[2];
      double dx = xcc-xc;
      double dy = ycc-yc;
      double dz = zcc-yc;
      return dx*dx+dy*dy+dz*dz;
    }

    /**
     * Returns the circumcenter of this face.
     * @return array of circumcenter coordinates {xc,yc,zc}.
     */
    public double[] centerCircle() {
      double[] cc = new double[3];
      centerCircle(cc);
      return cc;
    }

    /**
     * Returns the area of this face.
     * @return the area.
     */
    public float area() {
      return TriSurf.normalVector(_meshFace,(float[])null);
    }

    /**
     * Returns the normal vector for this face.
     * @return array containing the {X,Y,Z} components of the normal vector.
     */
    public float[] normalVector() {
      float[] vn = new float[3];
      TriSurf.normalVector(_meshFace,vn);
      return vn;
    }

    /**
     * Computes the normal vector and returns the area for this face.
     * @param vn array to contain the {X,Y,Z} components of the normal vector.
     * @return the area.
     */
    public float normalVector(float[] vn) {
      return TriSurf.normalVector(_meshFace,vn);
    }

    /**
     * Determines whether this face references the specified node.
     * @param node the node.
     * @return true, if this face references the node; false, otherwise.
     */
    public boolean references(Node node) {
      return node==nodeA() || node==nodeB() || node==nodeC();
    }

    /**
     * Determines whether this face references the specified nodes.
     * @param node1 a node.
     * @param node2 a node.
     * @return true, if this face references the nodes; false, otherwise.
     */
    public boolean references(Node node1, Node node2) {
      Node na = nodeA();
      Node nb = nodeB();
      Node nc = nodeC();
      if (node1==na) {
        return node2==nb || node2==nc;
      } else if (node1==nb) {
        return node2==na || node2==nc;
      } else if (node1==nc) {
        return node2==na || node2==nb;
      } else {
        return false;
      }
    }

    /**
     * Determines whether this face references the specified nodes.
     * @param node1 a node.
     * @param node2 a node.
     * @param node3 a node.
     * @return true, if this face references the nodes; false, otherwise.
     */
    public boolean references(Node node1, Node node2, Node node3) {
      Node na = nodeA();
      Node nb = nodeB();
      Node nc = nodeC();
      if (node1==na) {
        if (node2==nb) {
          return node3==nc;
        } else if (node2==nc) {
          return node3==nb;
        } else {
          return false;
        }
      } else if (node1==nb) {
        if (node2==na) {
          return node3==nc;
        } else if (node2==nc) {
          return node3==na;
        } else {
          return false;
        }
      } else if (node1==nc) {
        if (node2==na) {
          return node3==nb;
        } else if (node2==nb) {
          return node3==na;
        } else {
          return false;
        }
      } else {
        return false;
      }
    }

    private TetMesh.Face _meshFace;
    private Face _faceA,_faceB,_faceC;
    private int _mark;
    private void validate() {
      assert _meshFace!=null;
    }

    /**
     * Constructs a new face.
     */
    private Face(TetMesh.Face meshFace) {
      _meshFace = meshFace;
    }
  }

  /**
   * A type-safe iterator for faces.
   */
  public interface FaceIterator {
    public boolean hasNext();
    public Face next();
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
     * Facems this list so that its array length equals the number of faces.
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
   * Adds the specified node to this surface, if not already present.
   * @param node the node.
   * @return true, if node was added; false, otherwise.
   */
  public synchronized boolean addNode(Node node) {
    boolean added = _mesh.addNode(node._meshNode);
    if (added)
      rebuild();
    return added;
  }

  /**
   * Adds the specified nodes to this surface, if not already present.
   * @param nodes the nodes.
   * @return true, if all nodes were added; false, otherwise.
   */
  public synchronized boolean addNodes(Node[] nodes) {
    int nnode = nodes.length;
    int nadded = 0;
    for (int inode=0; inode<nnode; ++inode) {
      if (_mesh.addNode(nodes[inode]._meshNode))
        ++nadded;
    }
    if (nadded>0)
      rebuild();
    return nadded==nnode;
  }

  /**
   * Removes the specified node from this surface, if present.
   * @param node the node.
   * @return true, if node was removed; false, otherwise.
   */
  public synchronized boolean removeNode(Node node) {
    boolean removed = _mesh.removeNode(node._meshNode);
    if (removed)
      rebuild();
    return removed;
  }

  /**
   * Removes the specified nodes from this surface, if present.
   * @param nodes the nodes.
   * @return true, if all nodes were removed; false, otherwise.
   */
  public synchronized boolean removeNodes(Node[] nodes) {
    int nnode = nodes.length;
    int nremoved = 0;
    for (int inode=0; inode<nnode; ++inode) {
      if (_mesh.removeNode(nodes[inode]._meshNode))
        ++nremoved;
    }
    if (nremoved>0)
      rebuild();
    return nremoved==nnode;
  }

  /**
   * Returns the number of nodes in the surface.
   * @return the number of nodes.
   */
  public int countNodes() {
    return _mesh.countNodes();
  }

  /**
   * Returns the number of faces in the surface.
   * @return the number of faces.
   */
  public int countFaces() {
    return _faceMap.size();
  }

  /**
   * Gets an iterator for all nodes in this surface.
   * @return the iterator.
   */
  public synchronized NodeIterator getNodes() {
    return new NodeIterator() {
      public final boolean hasNext() {
        return _i.hasNext();
      }
      public final Node next() {
        return (Node)_i.next().data;
      }
      private TetMesh.NodeIterator _i = _mesh.getNodes();
    };
  }

  /**
   * Gets an iterator for all faces in this surface.
   * @return the iterator.
   */
  public synchronized FaceIterator getFaces() {
    return new FaceIterator() {
      public final boolean hasNext() {
        return _i.hasNext();
      }
      public final Face next() {
        return _i.next();
      }
      private Iterator<Face> _i = _faceMap.values().iterator();
    };
  }

  /**
   * Finds the node nearest to the point with specified coordinates.
   * @param x the x coordinate.
   * @param y the y coordinate.
   * @param z the z coordinate.
   * @return the nearest node.
   */
  public synchronized Node findNodeNearest(float x, float y, float z) {
    TetMesh.Node meshNode = _mesh.findNodeNearest(x,y,z);
    return (Node)meshNode.data;
  }

  /**
   * Gets an array of face nabors of the specified node.
   * @param node the node for which to get nabors.
   * @return the array of nabors. 
   */
  public synchronized Face[] getFaceNabors(Node node) {
    FaceList nabors = new FaceList();
    getFaceNabors(node,nabors);
    return nabors.trim();
  }

  /** 
   * Appends the face nabors of the specified node to the specified list.
   * @param node the node for which to get nabors.
   * @param nabors the list to which nabors are appended.
   */
  public synchronized void getFaceNabors(Node node, FaceList nabors) {
    clearFaceMarks();
    getFaceNabors(node,node._face,nabors);
  }

  /**
   * Returns a face that references the specified node.
   * @param node the node.
   * @return a face that references the specified node; or null, if 
   *  the node is not in the surface or the surface has no faces.
   */
  public Face findFace(Node node) {
    return node._face;
  }

  /**
   * Returns a face that references the specified nodes.
   * @param node1 a node.
   * @param node2 a node.
   * @return a face that references the specified nodes; or null, 
   *  if a node is not in the surface or the surface has no faces.
   */
  public synchronized Face findFace(Node node1, Node node2) {
    Face face = findFace(node1);
    if (face!=null) {
//    clearFaceMarks();
//    return findFace(face,node1,node2);
      if (face.references(node2))
        return face;
      Face face1 = face;
      face = node1.faceNext(face1);
      while (face!=face1 && face!=null) {
        if (face.references(node2))
          return face;
        face = node1.faceNext(face);
      }
      if (face==null) {
        face = node1.facePrev(face1);
        while (face!=face1 && face!=null) {
          if (face.references(node2))
            return face;
          face = node1.facePrev(face);
        }
      }
    }
    return null;
  }

  /**
   * Returns a face that references the specified nodes.
   * @param node1 a node.
   * @param node2 a node.
   * @param node3 a node.
   * @return a face that references the specified nodes; or null, 
   *  if a node is not in the surface or the surface has no faces.
   */
  public synchronized Face findFace(Node node1, Node node2, Node node3) {
    Face face = findFace(node1,node2);
//    if (face!=null) {
//      clearFaceMarks();
//      face = findFace(face,node1,node2,node3);
//    }
    if (face!=null) {
      if (face.references(node3))
        return face;
      face = face.faceNabor(node3);
      if (face!=null && face.references(node3))
        return face;
    }
    return null;
  }

  /**
   * Returns a directed edge AB that references the specified nodes.
   * @param nodeA a node.
   * @param nodeB a node.
   * @return a directed edge that references the specified nodes; 
   *  or null, if nodes A and B are not adjacent in the surface.
   */
  public synchronized Edge findEdge(Node nodeA, Node nodeB) {
    TetMesh.Edge meshEdge = findMeshEdge(nodeA,nodeB);
    Edge edge = getEdge(meshEdge);
    if (meshEdge!=null && edge==null) {
      Face face = findFace(nodeA,nodeB);
      if (face!=null) {
        Node nodeC = otherNode(face,nodeA,nodeB);
        if (nodesInOrder(face,nodeA,nodeB,nodeC))
          edge = new Edge(meshEdge,face);
      }
    }
    return edge;
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private static class EdgeFace implements Comparable<EdgeFace> {
    Edge edge;
    Face face;
    double grade;
    EdgeFace(Edge edge, Face face, double grade) {
      this.edge = edge;
      this.face = face;
      this.grade = grade;
    }
    public int compareTo(EdgeFace other) {
      double gradeOther = other.grade;
      if (grade<gradeOther) {
        return -1;
      } else if (grade>gradeOther) {
        return 1;
      } else {
        Edge edgeOther = other.edge;
        int hash = edge.hashCode();
        int hashOther = edgeOther.hashCode();
        if (hash<hashOther) {
          return -1;
        } else if (hash>hashOther) {
          return 1;
        } else {
          return 0;
        }
      }
    }
  }

  private static final int FACE_MARK_MAX = Integer.MAX_VALUE-1;

  // tet mesh
  private TetMesh _mesh = new TetMesh();

  // mesh faces not yet in surf
  private Set<TetMesh.Face> _faceSet = new HashSet<TetMesh.Face>(); 

  // mesh face -> surf face
  private Map<TetMesh.Face,Face> _faceMap = new HashMap<TetMesh.Face,Face>();

  // mesh edge -> surf edge-face
  private Map<TetMesh.Edge,EdgeFace> _edgeMap = 
    new HashMap<TetMesh.Edge,EdgeFace>(); 

  // edge-face sorted by grade
  private SortedSet<EdgeFace> _edgeQueue = new TreeSet<EdgeFace>(); 

  private int _faceMarkRed; // current value of red face mark
  private int _faceMarkBlue; // current value of blue face mark

  private void validate() {
    NodeIterator ni = getNodes();
    while (ni.hasNext()) {
      Node node = ni.next();
      node.validate();
    }
    FaceIterator fi = getFaces();
    while (fi.hasNext()) {
      Face face = fi.next();
      face.validate();
    }
  }

  /**
   * Returns the distance squared between the specified node and a point
   * with specified coordinates.
   */
  private static double distanceSquared(
    Node node, double x, double y, double z)
  {
    double dx = x-node.x();
    double dy = y-node.y();
    double dz = z-node.z();
    return dx*dx+dy*dy+dz*dz;
  }

  /**
   * Recursively searches for any face that references n1 and n2, given 
   * a face that references n1. If no such face exists, then returns null. 
   * Face marks must be cleared before calling this method.
   */
  private Face findFace(Face face, Node n1, Node n2) {
    if (face!=null) {
      mark(face);
      Node na = face.nodeA();
      Node nb = face.nodeB();
      Node nc = face.nodeC();
      Face fa = face.faceA();
      Face fb = face.faceB();
      Face fc = face.faceC();
      if (n1==na) {
        if (n2==nb || n2==nc ||
            fb!=null && !isMarked(fb) && (face=findFace(fb,n1,n2))!=null ||
            fc!=null && !isMarked(fc) && (face=findFace(fc,n1,n2))!=null)
          return face;
      } else if (n1==nb) {
        if (n2==nc || n2==na ||
            fc!=null && !isMarked(fc) && (face=findFace(fc,n1,n2))!=null ||
            fa!=null && !isMarked(fa) && (face=findFace(fa,n1,n2))!=null)
          return face;
      } else if (n1==nc) {
        if (n2==na || n2==nb ||
            fa!=null && !isMarked(fa) && (face=findFace(fa,n1,n2))!=null ||
            fb!=null && !isMarked(fb) && (face=findFace(fb,n1,n2))!=null)
          return face;
      } else {
        assert false:"n1 is referenced by face";
      }
    }
    return null;
  }

  /**
   * Recursively searches for any face that references n1, n2, and n3, 
   * given a face that references n1 and n2. If no such face exists, then 
   * returns null. Face marks must be cleared before calling this method.
   */
  private Face findFace(Face face, Node n1, Node n2, Node n3) {
    if (face!=null) {
      mark(face);
      Node na = face.nodeA();
      Node nb = face.nodeB();
      Node nc = face.nodeC();
      Face fa = face.faceA();
      Face fb = face.faceB();
      Face fc = face.faceC();
      if (n1==na) {
        if (n2==nb) {
          if (n3==nc ||
              fc!=null && !isMarked(fc) && (face=findFace(fc,n1,n2,n3))!=null)
          return face;
        } else if (n2==nc) {
          if (n3==nb ||
              fb!=null && !isMarked(fb) && (face=findFace(fb,n1,n2,n3))!=null)
          return face;
        } else {
          assert false:"n2 is referenced by face";
        }
      } else if (n1==nb) {
        if (n2==na) {
          if (n3==nc ||
              fc!=null && !isMarked(fc) && (face=findFace(fc,n1,n2,n3))!=null)
          return face;
        } else if (n2==nc) {
          if (n3==na ||
              fa!=null && !isMarked(fa) && (face=findFace(fa,n1,n2,n3))!=null)
          return face;
        } else {
          assert false:"n2 is referenced by face";
        }
      } else if (n1==nc) {
        if (n2==na) {
          if (n3==nb ||
              fb!=null && !isMarked(fb) && (face=findFace(fb,n1,n2,n3))!=null)
          return face;
        } else if (n2==nb) {
          if (n3==na ||
              fa!=null && !isMarked(fa) && (face=findFace(fa,n1,n2,n3))!=null)
          return face;
        } else {
          assert false:"n2 is referenced by face";
        }
      } else {
        assert false:"n1 is referenced by face";
      }
    }
    return null;
  }

  /**
   * Marks the specified face (red). Marks are used during iterations
   * over faces. Because faces (e.g., those faces containing a particular
   * node) are linked in an unordered structure, such iterations are
   * often performed by recursively visiting faces, and marks are used
   * to tag faces that have already been visited.
   * @param face the face to mark (red).
   */
  private void mark(Face face) {
    face._mark = _faceMarkRed;
  }

  /**
   * Marks the specified face red.
   * This is equivalent to simply marking the face.
   * @param face the face to mark red.
   */
  private void markRed(Face face) {
    face._mark = _faceMarkRed;
  }

  /**
   * Marks the specified face blue.
   * @param face the face to mark blue.
   */
  private void markBlue(Face face) {
    face._mark = _faceMarkBlue;
  }

  /**
   * Determines whether the specified face is marked (red).
   * @param face the face.
   * @return true, if the face is marked (red); false, otherwise.
   */
  private boolean isMarked(Face face) {
    return face._mark==_faceMarkRed;
  }

  /**
   * Determines whether the specified face is marked red.
   * @param face the face.
   * @return true, if the face is marked red; false, otherwise.
   */
  private boolean isMarkedRed(Face face) {
    return face._mark==_faceMarkRed;
  }

  /**
   * Determines whether the specified face is marked blue.
   * @param face the face.
   * @return true, if the face is marked blue; false, otherwise.
   */
  private boolean isMarkedBlue(Face face) {
    return face._mark==_faceMarkBlue;
  }

  /**
   * Clears all face marks, so that no face is marked. This can usually
   * be accomplished without iterating over all faces in the mesh.
   */
  private synchronized void clearFaceMarks() {

    // If the mark is about to overflow, we must zero all the marks.
    if (_faceMarkRed==FACE_MARK_MAX) {
      Iterator<Face> fi = _faceMap.values().iterator();
      while (fi.hasNext()) {
        Face face = fi.next();
        face._mark = 0;
      }
      _faceMarkRed = 0;
      _faceMarkBlue = 0;
    }

    // Usually, we simply increment/decrement the mark values.
    ++_faceMarkRed;
    --_faceMarkBlue;
  }

  /**
   * Recursively adds face nabors of the specified node to the specified list.
   * The face marks must be cleared before calling this method. This method 
   * could be made shorter by using another recursive method, but this longer 
   * inlined version is more efficient.
   */
  private void getFaceNabors(Node node, Face face, FaceList nabors) {
    if (face!=null) {
      mark(face);
      nabors.add(face);
      Node na = face.nodeA();
      Node nb = face.nodeB();
      Node nc = face.nodeC();
      Face fa = face.faceA();
      Face fb = face.faceB();
      Face fc = face.faceC();
      if (node==na) {
        if (fb!=null && !isMarked(fb))
          getFaceNabors(node,fb,nabors);
        if (fc!=null && !isMarked(fc))
          getFaceNabors(node,fc,nabors);
      } else if (node==nb) {
        if (fc!=null && !isMarked(fc))
          getFaceNabors(node,fc,nabors);
        if (fa!=null && !isMarked(fa))
          getFaceNabors(node,fa,nabors);
      } else if (node==nc) {
        if (fa!=null && !isMarked(fa))
          getFaceNabors(node,fa,nabors);
        if (fb!=null && !isMarked(fb))
          getFaceNabors(node,fb,nabors);
      } else {
        assert false:"node is referenced by face";
      }
    }
  }

  private Edge getEdge(TetMesh.Edge meshEdge) {
    EdgeFace edgeFace = _edgeMap.get(meshEdge);
    return (edgeFace!=null)?edgeFace.edge:null;
  }

  private EdgeFace getEdgeFace(Edge edge) {
    return _edgeMap.get(edge._meshEdge);
  }

  private EdgeFace getBestEdgeFace() {
    return (!_edgeQueue.isEmpty())?_edgeQueue.last():null;
  }

  private EdgeFace getNextEdgeFace(EdgeFace edgeFace) {
    SortedSet<EdgeFace> headSet = _edgeQueue.headSet(edgeFace);
    return (!headSet.isEmpty())?headSet.last():null;
  }

  private EdgeFace addEdge(Edge edge) {
//    trace("addEdge: edge="+edge);
    EdgeFace edgeFace = makeEdgeFace(edge);
    assert edgeFace!=null:"edgeFace!=null";
    Object edgeFaceOld = _edgeMap.put(edge._meshEdge,edgeFace);
    assert edgeFaceOld==null:"edge was not mapped";
    boolean added = _edgeQueue.add(edgeFace);
    assert added:"edgeFace was not in queue";
    return edgeFace;
  }

  private void removeEdge(Edge edge) {
//    trace("removeEdge: edge="+edge);
    EdgeFace edgeFace = getEdgeFace(edge);
    assert edgeFace!=null:"edgeFace!=null";
    Object edgeFaceOld = _edgeMap.remove(edge._meshEdge);
    assert edgeFaceOld!=null:"edge was mapped";
    boolean removed = _edgeQueue.remove(edgeFace);
    assert removed:"edgeFace was in queue";
  }

  private void addFace(Face face) {
    boolean removed = _faceSet.remove(face._meshFace) ||
                      _faceSet.remove(face._meshFace.mate());
    assert removed:"face not already in surface";
    Face faceOld = _faceMap.put(face._meshFace,face);
    assert faceOld==null:"face not already in surface";
  }

  private void removeFace(Face face) {
    _faceMap.remove(face._meshFace);
  }

  private void init(Face face) {
    trace("init: face="+face);
    trace("  meshFace A="+face._meshFace.nodeA());
    trace("  meshFace B="+face._meshFace.nodeB());
    trace("  meshFace C="+face._meshFace.nodeC());
    face._faceA = null;
    face._faceB = null;
    face._faceC = null;
    Node nodeA = face.nodeA();
    Node nodeB = face.nodeB();
    Node nodeC = face.nodeC();
    nodeA.setFace(face);
    nodeB.setFace(face);
    nodeC.setFace(face);
    Edge edgeCB = makeEdge(nodeC,nodeB,face);
    Edge edgeBA = makeEdge(nodeB,nodeA,face);
    Edge edgeAC = makeEdge(nodeA,nodeC,face);
    nodeA.setEdgeBefore(edgeBA);
    nodeB.setEdgeBefore(edgeCB);
    nodeC.setEdgeBefore(edgeAC);
    nodeA.setEdgeAfter(edgeAC);
    nodeB.setEdgeAfter(edgeBA);
    nodeC.setEdgeAfter(edgeCB);
    addEdge(edgeCB);
    addEdge(edgeBA);
    addEdge(edgeAC);
    addFace(face);
  }

  private void extend(Edge edge, Face face) {
    trace("extend: edge="+edge+" face="+face);
    trace("  meshEdge A="+edge._meshEdge.nodeA());
    trace("  meshEdge B="+edge._meshEdge.nodeB());
    trace("  meshFace A="+face._meshFace.nodeA());
    trace("  meshFace B="+face._meshFace.nodeB());
    trace("  meshFace C="+face._meshFace.nodeC());
    assert edge.isOnBoundary();
    Node nodeA = edge.nodeA();
    Node nodeB = edge.nodeB();
    Node nodeC = otherNode(face,nodeA,nodeB);
    nodeC.setFace(face);
    linkFaces(face,nodeC,edge.faceRight(),edge.nodeRight());
    Edge edgeAC = makeEdge(nodeA,nodeC,face);
    Edge edgeCB = makeEdge(nodeC,nodeB,face);
    nodeA.setEdgeAfter(edgeAC);
    nodeB.setEdgeBefore(edgeCB);
    nodeC.setEdgeAfter(edgeCB);
    nodeC.setEdgeBefore(edgeAC);
    removeEdge(edge);
    addFace(face);
    addEdge(edgeAC);
    addEdge(edgeCB);
  }

  private void fillEar(Edge edge, Face face) {
    trace("fillEar: edge="+edge+" face="+face);
    trace("  meshEdge A="+edge._meshEdge.nodeA());
    trace("  meshEdge B="+edge._meshEdge.nodeB());
    trace("  meshFace A="+face._meshFace.nodeA());
    trace("  meshFace B="+face._meshFace.nodeB());
    trace("  meshFace C="+face._meshFace.nodeC());
    Node nodeA = edge.nodeA();
    Node nodeB = edge.nodeB();
    Node nodeC = otherNode(face,nodeA,nodeB);
    Edge edge1 = nodeC.edgeBefore();
    Edge edge2 = nodeC.edgeAfter();
    Node node1 = edge1.nodeA();
    Node node2 = edge2.nodeB();
    if (node2==nodeA) {
      linkFaces(face,nodeC,edge.faceRight(),edge.nodeRight());
      linkFaces(face,nodeB,edge2.faceRight(),edge2.nodeRight());
      Edge edgeCB = makeEdge(nodeC,nodeB,face);
      nodeC.setEdgeAfter(edgeCB);
      nodeB.setEdgeBefore(edgeCB);
      nodeA.setEdgeAfter(null);
      nodeA.setEdgeBefore(null);
      removeEdge(edge);
      removeEdge(edge2);
      addFace(face);
      addEdge(edgeCB);
    } else if (node1==nodeB) {
      linkFaces(face,nodeC,edge.faceRight(),edge.nodeRight());
      linkFaces(face,nodeA,edge1.faceRight(),edge1.nodeRight());
      Edge edgeAC = makeEdge(nodeA,nodeC,face);
      nodeA.setEdgeAfter(edgeAC);
      nodeC.setEdgeBefore(edgeAC);
      nodeB.setEdgeAfter(null);
      nodeB.setEdgeBefore(null);
      removeEdge(edge);
      removeEdge(edge1);
      addFace(face);
      addEdge(edgeAC);
    } else {
      assert false:"ear is valid";
    }
  }

  private void fillHole(Edge edge, Face face) {
    trace("fillHole: edge="+edge+" face="+face);
    trace("  meshEdge A="+edge._meshEdge.nodeA());
    trace("  meshEdge B="+edge._meshEdge.nodeB());
    trace("  meshFace A="+face._meshFace.nodeA());
    trace("  meshFace B="+face._meshFace.nodeB());
    trace("  meshFace C="+face._meshFace.nodeC());
    Edge edgeAB = edge;
    Edge edgeBC = edge.edgeAfter();
    Edge edgeCA = edge.edgeBefore();
    assert edgeAB.isOnBoundary();
    assert edgeBC.isOnBoundary();
    assert edgeCA.isOnBoundary();
    Face faceAB = edgeAB.faceRight();
    Face faceBC = edgeBC.faceRight();
    Face faceCA = edgeCA.faceRight();
    Node nodeA = edgeAB.nodeA();
    Node nodeB = edgeBC.nodeA();
    Node nodeC = edgeCA.nodeA();
    linkFaces(face,nodeA,faceBC,otherNode(faceBC,nodeB,nodeC));
    linkFaces(face,nodeB,faceCA,otherNode(faceCA,nodeA,nodeC));
    linkFaces(face,nodeC,faceAB,otherNode(faceAB,nodeA,nodeB));
    nodeA.setEdgeBefore(null);
    nodeB.setEdgeBefore(null);
    nodeC.setEdgeBefore(null);
    nodeA.setEdgeAfter(null);
    nodeB.setEdgeAfter(null);
    nodeC.setEdgeAfter(null);
    removeEdge(edgeAB);
    removeEdge(edgeBC);
    removeEdge(edgeCA);
    addFace(face);
  }

  /**
   * Returns a valid twin with grade higher than the specified edge-face.
   * Returns null, if no such twin exists.
   */
  private EdgeFace findTwin(EdgeFace edgeFace) {
//    trace("findTwin");
    Edge edge = edgeFace.edge;
    Face face = edgeFace.face;
    double grade = edgeFace.grade;
    Node nodeA = edge.nodeA();
    Node nodeB = edge.nodeB();
    Node nodeC = otherNode(face,nodeA,nodeB);
    assert nodeA.isOnBoundary();
    assert nodeB.isOnBoundary();
    assert nodeC.isOnBoundary();
    Node node1 = nodeC.edgeBefore().nodeA();
    assert node1!=nodeA;
    assert node1!=nodeB;
    if (node1.isOnBoundary()) {
      Edge edgeTwin = node1.edgeAfter();
      assert nodeC==edgeTwin.nodeB();
      removeEdge(edgeTwin);
      EdgeFace edgeFaceTwin = addEdge(edgeTwin);
      Face faceTwin = edgeFaceTwin.face;
      double gradeTwin = edgeFaceTwin.grade;
      if (faceTwin!=null && 
          nodesInOrder(faceTwin,node1,nodeC,nodeB) && 
          gradeTwin>grade) 
        return edgeFaceTwin;
    }
    Node node2 = nodeC.edgeAfter().nodeB();
    assert node2!=nodeA;
    assert node2!=nodeB;
    if (node2.isOnBoundary()) {
      Edge edgeTwin = node2.edgeBefore();
      assert nodeC==edgeTwin.nodeA();
      removeEdge(edgeTwin);
      EdgeFace edgeFaceTwin = addEdge(edgeTwin);
      Face faceTwin = edgeFaceTwin.face;
      double gradeTwin = edgeFaceTwin.grade;
      if (faceTwin!=null && 
          nodesInOrder(faceTwin,node2,nodeA,nodeC) && 
          gradeTwin>grade) 
        return edgeFaceTwin;
    }
    return null;
  }

  /**
   * Glues the specified edge-face to its twin.
   */
  private void glue(Edge edge, Face face, Edge edgeTwin, Face faceTwin) {
    trace("glue: edge="+edge+" face="+face);
    trace("  meshEdge A="+edge._meshEdge.nodeA());
    trace("  meshEdge B="+edge._meshEdge.nodeB());
    trace("  meshFace A="+face._meshFace.nodeA());
    trace("  meshFace B="+face._meshFace.nodeB());
    trace("  meshFace C="+face._meshFace.nodeC());
    trace("  meshEdgeTwin A="+edgeTwin._meshEdge.nodeA());
    trace("  meshEdgeTwin B="+edgeTwin._meshEdge.nodeB());
    trace("  meshFaceTwin A="+faceTwin._meshFace.nodeA());
    trace("  meshFaceTwin B="+faceTwin._meshFace.nodeB());
    trace("  meshFaceTwin C="+faceTwin._meshFace.nodeC());
    Node nodeA = edge.nodeA();
    Node nodeB = edge.nodeB();
    Node nodeC = otherNode(face,nodeA,nodeB);
    assert nodeA.isOnBoundary();
    assert nodeB.isOnBoundary();
    assert nodeC.isOnBoundary();

    // Remove edge and its twin; add face and its twin.
    removeEdge(edge);
    removeEdge(edgeTwin);
    addFace(face);
    addFace(faceTwin);

    // If face is ABC and its twin is ACD, ...
    if (faceTwin.references(nodeA)) {
      Node nodeD = nodeC.edgeAfter().nodeB();
      assert nodeD.isOnBoundary();

      // If face twin is a hole, fill it.
      if (nodeD.edgeAfter()==nodeA.edgeBefore()) {
        Edge edgeDA = nodeD.edgeAfter();
        nodeA.setEdgeBefore(null);
        nodeD.setEdgeBefore(null);
        nodeA.setEdgeAfter(null);
        nodeD.setEdgeAfter(null);
        removeEdge(edgeDA);
      }

      // Else face twin is an ear, so fill it.
      else {
        Edge edgeAD = makeEdge(nodeA,nodeD,faceTwin);
        nodeA.setEdgeAfter(edgeAD);
        nodeD.setEdgeBefore(edgeAD);
        addEdge(edgeAD);
      }

      // For either hole or ear, ...
      Edge edgeCB = makeEdge(nodeC,nodeB,face);
      nodeC.setEdgeAfter(edgeCB);
      nodeB.setEdgeBefore(edgeCB);
      addEdge(edgeCB);
      linkFaces(face,nodeB,faceTwin,nodeD);
      linkFaces(face,nodeC,edge.faceRight(),edge.nodeRight());
      linkFaces(faceTwin,nodeA,edgeTwin.faceRight(),edgeTwin.nodeRight());
    } 
    
    // Else if face is ABC and its twin is BDC, ...
    else if (faceTwin.references(nodeB)) {
      Node nodeD = nodeC.edgeBefore().nodeA();
      assert nodeD.isOnBoundary();

      // If face twin is a hole, fill it.
      if (nodeD.edgeBefore()==nodeB.edgeAfter()) {
        Edge edgeBD = nodeD.edgeBefore();
        nodeB.setEdgeBefore(null);
        nodeD.setEdgeBefore(null);
        nodeB.setEdgeAfter(null);
        nodeD.setEdgeAfter(null);
        removeEdge(edgeBD);
      }

      // Else face twin is an ear, so fill it.
      else {
        Edge edgeDB = makeEdge(nodeD,nodeB,faceTwin);
        nodeD.setEdgeAfter(edgeDB);
        nodeB.setEdgeBefore(edgeDB);
        addEdge(edgeDB);
      }

      // For either hole or ear, ...
      Edge edgeAC = makeEdge(nodeA,nodeC,face);
      nodeA.setEdgeAfter(edgeAC);
      nodeC.setEdgeBefore(edgeAC);
      addEdge(edgeAC);
      linkFaces(face,nodeA,faceTwin,nodeD);
      linkFaces(face,nodeC,edge.faceRight(),edge.nodeRight());
      linkFaces(faceTwin,nodeB,edgeTwin.faceRight(),edgeTwin.nodeRight());
    }
  }

  private boolean stitch(EdgeFace edgeFace) {
//    validate();
    Edge edge = edgeFace.edge;
    Face face = edgeFace.face;
//    assert face!=null;
//    assert _faceSet.contains(face._meshFace) ||
//            _faceSet.contains(face._meshFace.mate());

    // Nodes A and B of edge, and the other node C in the face.
    Node nodeA = edge.nodeA();
    Node nodeB = edge.nodeB();
    Node nodeC = otherNode(face,nodeA,nodeB);
    
    // If face is not valid, replace it with a valid one, if possible.
    if (!validForFace(nodeA,nodeB,nodeC)) {
      removeEdge(edge);
      addEdge(edge);
      return true;
    }

    // If node C is not in the surface, then extend.
    if (!nodeC.isInSurface()) {
      extend(edge,face);
      return true;
    }

    // Else if node C is on the surface boundary, ...
    else if (nodeC.isOnBoundary()) {

      // Nabor nodes 1 and 2 of node C, also on the surface boundary.
      Node node1 = nodeC.edgeBefore().nodeA();
      Node node2 = nodeC.edgeAfter().nodeB();

      // If both edge nodes A and B are nabors of node C, fill hole.
      if (node1==nodeB && node2==nodeA) {
        fillHole(edge,face);
        return true;
      }

      // Else if either node A or node B is a nabor of node C, fill ear.
      else if (node1==nodeB || node2==nodeA) {
        fillEar(edge,face);
        return true;
      }

      // Else ...
      else {

        // If face has a valid twin with higher grade, glue.
        EdgeFace edgeFaceTwin = findTwin(edgeFace);
        if (edgeFaceTwin!=null) {
          Edge edgeTwin = edgeFaceTwin.edge;
          Face faceTwin = edgeFaceTwin.face;
          glue(edge,face,edgeTwin,faceTwin);
          return true;
        } else {
          return false;
        }
      }
    }
    
    // Else the face is not valid, and we should not be here!
    else {
      assert false:"valid face for extend, fill ear, fill hole, or glue";
      return false;
    }
  }

  private void rebuild() {
    trace("rebuild");
    init();
    while (surf())
      ;
  }

  private void init() {
    trace("  init: ntets="+_mesh.countTets());
    _faceSet.clear();
    _faceMap.clear();
    _edgeMap.clear();
    _edgeQueue.clear();
    TetMesh.TetIterator ti = _mesh.getTets();
    while (ti.hasNext()) {
      TetMesh.Tet tet = ti.next();
      TetMesh.Node a = tet.nodeA();
      TetMesh.Node b = tet.nodeB();
      TetMesh.Node c = tet.nodeC();
      TetMesh.Node d = tet.nodeD();
      TetMesh.Face[] meshFaces = {
        new TetMesh.Face(a,b,c,tet),
        new TetMesh.Face(b,d,c,tet),
        new TetMesh.Face(c,d,a,tet),
        new TetMesh.Face(d,b,a,tet),
      };
      for (int i=0; i<4; ++i) {
        TetMesh.Face meshFacei = meshFaces[i];
        if (!_faceSet.contains(meshFacei.mate())) {
          _faceSet.add(meshFacei);
          trace("  init: added face"+meshFacei);
          trace("        node A="+meshFacei.nodeA());
          trace("        node B="+meshFacei.nodeB());
          trace("        node C="+meshFacei.nodeC());
        }
      }
      ((Node)a.data).init();
      ((Node)b.data).init();
      ((Node)c.data).init();
      ((Node)d.data).init();
    }
    trace("  init: _faceSet size="+_faceSet.size());
  }

  /**
   * Creates a part of the surface. Returns true, if any faces were created.
   * Typically, this method is called repeatedly until it creates no faces
   * and returns false.
   */
  private boolean surf() {
    int nface = countFaces();

    // If no mesh faces left in set, simply return.
    if (_faceSet.isEmpty())
      return false;

    // Among mesh faces in set, find mesh face with smallest circumradius.
    TetMesh.Face meshFace = null;
    double rrmin = Double.MAX_VALUE;
    double[] cc = new double[3];
    Iterator<TetMesh.Face> mfi = _faceSet.iterator();
    while (mfi.hasNext()) {
      TetMesh.Face meshFacei = mfi.next();
      double rr = meshFacei.centerCircle(cc);
      if (rr<rrmin) {
        meshFace = meshFacei;
        rrmin = rr;
      }
    }
    assert meshFace!=null;

    // Initialize a part of surface with that mesh face.
    Face face = new Face(meshFace);
    init(face);

    // While boundary edges in part remain to be processed, stitch surface.
    trace("  surf: stitching");
    EdgeFace edgeFace = getBestEdgeFace();
    while (edgeFace!=null && edgeFace.face!=null) {
      if (stitch(edgeFace)) {
        edgeFace = getBestEdgeFace();
      } else {
        edgeFace = getNextEdgeFace(edgeFace);
      }
    }

    // Remove all faces in set that reference nodes already in surface.
    trace("  surf: removing faces");
    ArrayList<TetMesh.Face> faceList = new ArrayList<TetMesh.Face>();
    mfi = _faceSet.iterator();
    while (mfi.hasNext()) {
      TetMesh.Face meshFacei = mfi.next();
      Node nodeA = (Node)meshFacei.nodeA().data;
      Node nodeB = (Node)meshFacei.nodeB().data;
      Node nodeC = (Node)meshFacei.nodeC().data;
      if (nodeA.isInSurface() || nodeB.isInSurface() || nodeC.isInSurface()) {
        faceList.add(meshFacei);
      }
    }
    mfi = faceList.iterator();
    while (mfi.hasNext()) {
      TetMesh.Face meshFacei = mfi.next();
      _faceSet.remove(meshFacei);
    }

    // We may have more faces.
    trace("  surf: more faces = "+(countFaces()>nface));
    return countFaces()>nface;
  }

  private static boolean nodesInOrder(Face face, Node na, Node nb, Node nc) {
    Node fa = face.nodeA();
    Node fb = face.nodeB();
    Node fc = face.nodeC();
    return na==fa && nb==fb && nc==fc ||
           na==fb && nb==fc && nc==fa ||
           na==fc && nb==fa && nc==fb;
  }

  private static Node otherNode(Face face, Node na, Node nb) {
    Node fa = face.nodeA();
    Node fb = face.nodeB();
    Node fc = face.nodeC();
    if (na==fa) {
      if (nb==fb) {
        return fc;
      } else if (nb==fc) {
        return fb;
      } else {
        return null;
      }
    } else if (na==fb) {
      if (nb==fa) {
        return fc;
      } else if (nb==fc) {
        return fa;
      } else {
        return null;
      }
    } else if (na==fc) {
      if (nb==fa) {
        return fb;
      } else if (nb==fb) {
        return fa;
      } else {
        return null;
      }
    } else {
      return null;
    }
  }

  private static TetMesh.Node otherNode(
    TetMesh.Face face, TetMesh.Node na, TetMesh.Node nb)
  {
    TetMesh.Node fa = face.nodeA();
    TetMesh.Node fb = face.nodeB();
    TetMesh.Node fc = face.nodeC();
    if (na==fa) {
      if (nb==fb) {
        return fc;
      } else if (nb==fc) {
        return fb;
      } else {
        return null;
      }
    } else if (na==fb) {
      if (nb==fa) {
        return fc;
      } else if (nb==fc) {
        return fa;
      } else {
        return null;
      }
    } else if (na==fc) {
      if (nb==fa) {
        return fb;
      } else if (nb==fb) {
        return fa;
      } else {
        return null;
      }
    } else {
      return null;
    }
  }

  private static void linkFaces(
    Face face, Node node, Face faceNabor, Node nodeNabor)
  {
    if (face!=null) {
      if (node==face.nodeA()) {
        face._faceA = faceNabor;
      } else if (node==face.nodeB()) {
        face._faceB = faceNabor;
      } else if (node==face.nodeC()) {
        face._faceC = faceNabor;
      } else {
        assert false:"node referenced by face";
      }
    }
    if (faceNabor!=null) {
      if (nodeNabor==faceNabor.nodeA()) {
        faceNabor._faceA = face;
      } else if (nodeNabor==faceNabor.nodeB()) {
        faceNabor._faceB = face;
      } else if (nodeNabor==faceNabor.nodeC()) {
        faceNabor._faceC = face;
      } else {
        assert false:"nodeNabor referenced by faceNabor";
      }
    }
  }

  static float normalVector(TetMesh.Face meshFace, float[] v) {
    TetMesh.Node na = meshFace.nodeA();
    TetMesh.Node nb = meshFace.nodeB();
    TetMesh.Node nc = meshFace.nodeC();
    double xa = na.x();
    double ya = na.y();
    double za = na.z();
    double xb = nb.x();
    double yb = nb.y();
    double zb = nb.z();
    double xc = nc.x();
    double yc = nc.y();
    double zc = nc.z();
    double x0 = xc-xa;
    double y0 = yc-ya;
    double z0 = zc-za;
    double x1 = xa-xb;
    double y1 = ya-yb;
    double z1 = za-zb;
    double x2 = y0*z1-y1*z0;
    double y2 = x1*z0-x0*z1;
    double z2 = x0*y1-x1*y0;
    double alpha = x2*x2+y2*y2+z2*z2;
    double delta = sqrt(alpha);
    double scale = (delta>0.0)?1.0/delta:1.0;
    if (v!=null) {
      v[0] = (float)(x2*scale);
      v[1] = (float)(y2*scale);
      v[2] = (float)(z2*scale);
    }
    return (float)(0.5*scale*alpha);
  }

  static double normalVector(TetMesh.Face meshFace, double[] v) {
    TetMesh.Node na = meshFace.nodeA();
    TetMesh.Node nb = meshFace.nodeB();
    TetMesh.Node nc = meshFace.nodeC();
    double xa = na.x();
    double ya = na.y();
    double za = na.z();
    double xb = nb.x();
    double yb = nb.y();
    double zb = nb.z();
    double xc = nc.x();
    double yc = nc.y();
    double zc = nc.z();
    double x0 = xc-xa;
    double y0 = yc-ya;
    double z0 = zc-za;
    double x1 = xa-xb;
    double y1 = ya-yb;
    double z1 = za-zb;
    double x2 = y0*z1-y1*z0;
    double y2 = x1*z0-x0*z1;
    double z2 = x0*y1-x1*y0;
    double alpha = x2*x2+y2*y2+z2*z2;
    double delta = sqrt(alpha);
    double scale = (delta>0.0)?1.0/delta:1.0;
    if (v!=null) {
      v[0] = x2*scale;
      v[1] = y2*scale;
      v[2] = z2*scale;
    }
    return 0.5*scale*alpha;
  }

  private static double angle(TetMesh.Face face1, TetMesh.Face face2) {
    double[] v1 = new double[3];
    double[] v2 = new double[3];
    normalVector(face1,v1);
    normalVector(face2,v2);
    double cos12 = v1[0]*v2[0]+v1[1]*v2[1]+v1[2]*v2[2];
    return acos(cos12);
  }

  private TetMesh.Edge findMeshEdge(Node nodeA, Node nodeB) {
    TetMesh.Node meshNodeA = nodeA._meshNode;
    TetMesh.Node meshNodeB = nodeB._meshNode;
    TetMesh.Edge meshEdge = _mesh.findEdge(meshNodeA,meshNodeB);
    if (meshEdge!=null && meshNodeA!=meshEdge.nodeA())
      meshEdge = meshEdge.mate();
    return meshEdge;
  }

  private Edge makeEdge(Node nodeA, Node nodeB, Face face) {
    TetMesh.Edge meshEdge = findMeshEdge(nodeA,nodeB);
    return (meshEdge!=null)?new Edge(meshEdge,face):null;
  }

  private static final double VV_SLIVER = cos(5.0*PI/6.0);
  private static final double VV_LARGE = cos(1.1*PI/2.0);

  /**
   * Makes and edge-face for the specified edge. The edge already has
   * a mesh face incident on its right side. This method computes the
   * best candidate face for the left side of the edge. The edge and
   * candidate face form an edge-face pair, with a grade that depends
   * on the circumradius of the candidate and on the angle between
   * the normal vectors for the right and left faces. Small circumradii 
   * and small angles correspond to high grades. 
   */
  private EdgeFace makeEdgeFace(Edge edge) {
    assert edge.isOnBoundary();
    Node nodeA = edge.nodeA();
    Node nodeB = edge.nodeB();
    
    // Mesh face incident on right side of edge is already in surface.
    TetMesh.Face meshFace = edge.faceRight()._meshFace;
    
    // Vector normal to mesh face already in surface.
    double[] v = new double[3];
    normalVector(meshFace,v);
    
    // Mesh nodes A and B of edge.
    TetMesh.Edge meshEdge = edge._meshEdge;
    TetMesh.Node meshNodeA = meshEdge.nodeA();
    TetMesh.Node meshNodeB = meshEdge.nodeB();
    
    // Variables used to find the best mesh face not yet in surface. 
    double[] cc = new double[3];
    double[] vi = new double[3];
    double rrBest = Double.MAX_VALUE;
    double vvBest = -1.0;
    TetMesh.Face mfBest = null;
    TetMesh.Face mfMate = meshFace.mate();
    
    // Mesh faces incident on left side of edge. One of these is the
    // mate of the right mesh face that is already in the surface.
    TetMesh.Face[] meshFaces = _mesh.getFaceNabors(meshEdge);
    int n = meshFaces.length;
    
    // Find the best mesh face not yet in the surface.
    for (int i=0; i<n; ++i) {
      TetMesh.Face mf = meshFaces[i];
      
      // Ignore the mate of the right mesh face already in the surface.
      if (mf.equals(mfMate))
        continue;
      
      // Node C of the face does not equal nodes A or B of the edge.
      TetMesh.Node fa = mf.nodeA();
      TetMesh.Node fb = mf.nodeB();
      TetMesh.Node fc = mf.nodeC();
      Node nodeC;
      if (fc==meshNodeA) {
        nodeC = (Node)fb.data;
      } else if (fc==meshNodeB) {
        nodeC = (Node)fa.data;
      } else {
        nodeC = (Node)fc.data;
      }
      
      // If nodes A, B, and C would make a valid face ABC, ...
      if (validForFace(nodeA,nodeB,nodeC)) {
        
        // Normal vector of mesh face.
        normalVector(mf,vi);
        
        // Dot product equals the cosine of angle between normal vectors.
        double vv = v[0]*vi[0]+v[1]*vi[1]+v[2]*vi[2];
        
        // If the angle is not too close to PI, ...
        if (vv>VV_SLIVER) {
          
          // Square of mesh face circumradius.
          double rr = mf.centerCircle(cc);
          
          // If circumradius is the best found so far, ...
          if (rr<rrBest) {
            rrBest = rr;
            vvBest = vv;
            mfBest = mf;
          }
        }
      }
    }
    
    // Candidate face corresponding to best mesh face found, or null,
    // if no valid candidate. The best candidate has a grade; a null
    // best candidate has the lowest possible grade = -2.0.
    assert !_faceMap.containsKey(mfBest) && !_faceMap.containsKey(mfBest);
    Face face = (mfBest!=null)?new Face(mfBest):null;
    double grade = (vvBest>VV_LARGE)?1.0/rrBest:vvBest-1.0;
    if (grade<=0.0)
      face = null;
    return new EdgeFace(edge,face,grade);
  }
  
  /**
   * Determines whether the specified nodes are an internal edge.
   * An internal edge is not on the boundary; it has two nabor faces.
   */
  private boolean hasInternalEdge(Node nodeA, Node nodeB) {
    Face face = findFace(nodeA,nodeB);
    if (face==null)
      return false;
    face = face.faceNabor(otherNode(face,nodeA,nodeB));
    if (face==null)
      return false;
    return true;
  }

  /**
   * Determines whether the specified nodes would form a valid face.
   * Assumes that nodes A and B are a boundary edge. Node C is the
   * other node that would form the face in question.
   */
  private boolean validForFace(Node nodeA, Node nodeB, Node nodeC) {
    return !nodeC.isInSurface() || 
            nodeC.isOnBoundary() &&
            !hasInternalEdge(nodeB,nodeC) &&
            !hasInternalEdge(nodeC,nodeA);
  }

  private static final boolean TRACE = false;
  private static void trace(String s) {
    if (TRACE)
      System.out.println(s);
  }
}
