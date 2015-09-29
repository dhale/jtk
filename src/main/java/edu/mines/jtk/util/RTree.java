/****************************************************************************
Copyright 2006, Colorado School of Mines and others.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
****************************************************************************/
package edu.mines.jtk.util;

import java.util.*;

import static edu.mines.jtk.util.ArrayMath.quickIndexSort;

/** 
 * A tree of bounded objects with methods for fast updates and queries.
 * An R-tree's entries are bounded objects with min/max coordinates in N
 * dimensions, where N is a parameter specified when constructing an R-tree.
 * We refer to these N-dimensional bounds as <em>boxes</em>.
 * <p>
 * An R-tree facilitates a variety of queries, such as a search for all
 * objects with bounds that overlap a specified box, or a search for the
 * k objects nearest to a specified point. An R-tree is also dynamic; 
 * objects may be efficiently added and removed from the tree at any time.
 * <p>
 * An R-tree uses the N-dimensional coordinate bounds of each object to 
 * build a hierarchy (a tree) of internal nodes. Each node contains a list 
 * of child nodes or objects. Each node maintains bounds that tightly 
 * contain its children. The R-tree attempts to minimize any overlap of 
 * these internal nodes, so that objects can be quickly found, added, or 
 * removed.
 * <p>
 * An R-tree is a set; it contains no duplicate objects. Specifically, an 
 * R-tree contains no objects b1 and b2 such that b1.equals(b2). Also, an
 * R-tree contains no null objects.
 * <p>
 * To reduce memory consumption, an object's bounds are not stored in the 
 * R-tree. These bounds may be either computed on demand or cached by the 
 * object itself. However, while an object is in an R-tree, it should not 
 * be changed in any way that would affect its equality comparison or its 
 * bounds. The result of such a change is undefined.
 * <p>
 * References:
 * <ul><li>
 * Guttman A., 1984, R-trees - a dynamic index structure for spatial 
 * searching: Proceedings of the ACM, SIGMOD, p. 47-57.
 * </li><li>
 * Roussopoulos, N., Kelley, S., and Vincent, F., 1995, Nearest neighbor
 * queries: Proceedings of the ACM, SIGMOD, p. 71-79.
 * </li><li>
 * Cheung, K.L., and Fu, A.W.C., 1998, Enhanced nearest neighbor search
 * on the R-tree: SIGMOD Record, v. 27, n. 3, p. 16-21.
 * </li></ul>
 *
 * @author Dave Hale and Zach Pember, Colorado School of Mines
 * @version 2003.05.02, 2006.07.13
 */
public class RTree extends AbstractSet<Object> {

  /**
   * An N-dimensional object in a box defined by N min/max coordinates.
   */
  public interface Boxed {

    /**
     * Gets the N min/max coordinates for this boxed object. While an 
     * object is in an R-tree, these bounds should be constant.
     * @param min array to contain N min coordinates.
     * @param max array to contain N max coordinates.
     */
    public void getBounds(float[] min, float[] max);

    /**
     * Gets the distance-squared from this boxed object to a point.
     * Note: this distance is typically <em>not</em> the same as the 
     * distance from the boxed object's <em>bounds</em> to the point. 
     * Rather, it is the distance from the object within those bounds.
     * @param point array of N point coordinates.
     * @return the distance-squared.
     */
    public float getDistanceSquared(float[] point);
  }

  /**
   * Gets bounds and computes distances for objects added to an R-tree. 
   * An R-tree constructed with a boxer uses this interface to get the
   * bounds of and distances to all objects added to the tree, even if 
   * those objects are {@link RTree.Boxed}.
   */
  public interface Boxer {

    /**
     * Gets the min/max coordinates for the specified object.
     * While the object is in an R-tree, its bounds should be constant.
     * @param object the object for which to compute bounds.
     * @param min array to contain min coordinates.
     * @param max array to contain max coordinates.
     * @exception ClassCastException if this boxer cannot get bounds
     *  for the type of object specified.
     */
    public void getBounds(Object object, float[] min, float[] max);

    /**
     * Gets the distance squared from the specified object to a point.
     * @param object the object for which to compute distance squared.
     * @param point array of N point coordinates.
     * @return the distance squared.
     */
    public float getDistanceSquared(Object object, float[] point);
  }

  /**
   * A simple N-dimensional box.
   */
  public static class Box implements Boxed {

    /**
     * Constructs a 2-dimensional box with specified min/max bounds.
     * @param xmin the min X coordinate.
     * @param ymin the min Y coordinate.
     * @param xmax the max X coordinate.
     * @param ymax the max Y coordinate.
     */
    public Box(float xmin, float ymin, float xmax, float ymax) {
      _ndim = 2;
      _min = new float[]{xmin,ymin};
      _max = new float[]{xmax,ymax};
    }

    /**
     * Constructs a 3-dimensional box with specified min/max bounds.
     * @param xmin the min X coordinate.
     * @param ymin the min Y coordinate.
     * @param zmin the min Z coordinate.
     * @param xmax the max X coordinate.
     * @param ymax the max Y coordinate.
     * @param zmax the max Z coordinate.
     */
    public Box(
      float xmin, float ymin, float zmin,
      float xmax, float ymax, float zmax)
    {
      _ndim = 3;
      _min = new float[]{xmin,ymin,zmin};
      _max = new float[]{xmax,ymax,zmax};
    }

    /**
     * Constructs an N-dimensional box with specified min/max bounds.
     * Here, N is the length of the array of min/max coordinates.
     * @param min array of min coordinates.
     * @param max array of max coordinates.
     */
    public Box(float[] min, float[] max) {
      Check.argument(min.length==max.length,"min/max lengths are equal");
      _ndim = min.length;
      _min = new float[_ndim];
      _max = new float[_ndim];
      for (int idim=0; idim<_ndim; ++idim) {
        _min[idim] = min[idim];
        _max[idim] = max[idim];
      }
    }

    /**
     * Constructs an N-dimensional box for the specified boxed object.
     * @param ndim number N of min/max coordinates.
     * @param boxed the boxed object.
     */
    public Box(int ndim, Boxed boxed) {
      _ndim = ndim;
      _min = new float[_ndim];
      _max = new float[_ndim];
      boxed.getBounds(_min,_max);
    }

    /**
     * Gets the N min/max coordinates for this box.
     * @param min array to contain the min coordinates.
     * @param max array to contain the max coordinates.
     */
    public void getBounds(float[] min, float[] max) {
      for (int idim=0; idim<_ndim; ++idim) {
        min[idim] = _min[idim];
        max[idim] = _max[idim];
      }
    }

    /**
     * Gets the distance-squared from this box to a point.
     * @param point array of N point coordinates.
     * @return the distance squared.
     */
    public float getDistanceSquared(float[] point) {
      float sum = 0.0f;
      for (int idim=0; idim<_ndim; ++idim) {
        float p = point[idim];
        float s = _min[idim];
        float t = _max[idim];
        float d = (p<s)?p-s:(p>t)?p-t:0.0f;
        sum += d*d;
      }
      return sum;
    }

    /**
     * Determines whether this box overlaps the specified box.
     * @param box the box.
     * @return true, if overlaps; false, otherwise.
     */
    public boolean overlaps(Box box) {
      float[] bmin = box._min;
      float[] bmax = box._max;
      for (int idim=0; idim<_ndim; ++idim) {
        if (_min[idim]>bmax[idim] || _max[idim]<bmin[idim])
          return false;
      }
      return true;
    }

    private int _ndim;
    private float[] _min;
    private float[] _max;
  }

  /**
   * Constructs an R-tree with specified parameters.
   * @param ndim the number of dimensions per object;
   *  equals the number of min/max coordinates per object.
   * @param nmin the minimum number of objects per node;
   *  must not be less than one or greater than nmax/2.
   * @param nmax the maximum number of objects per node;
   *  must not be less than 4.
   */
  public RTree(int ndim, int nmin, int nmax) {
    this(ndim,nmin,nmax,new DefaultBoxer());
  }

  /**
   * Constructs an R-tree with specified parameters.
   * @param ndim the number of dimensions per object;
   *  equals the number of min/max coordinates per object.
   * @param nmin the minimum number of objects per node;
   *  must not be less than one or greater than nmax/2.
   * @param nmax the maximum number of objects per node;
   *  must not be less than 4.
   * @param boxer the {@link Boxer} used to compute the bounds of 
   *  objects added to this tree.
   */
  public RTree(int ndim, int nmin, int nmax, Boxer boxer) {
    Check.argument(nmin>0,"nmin>0");
    Check.argument(nmin<=nmax/2,"nmin<=nmax/2");
    Check.argument(nmax>=4,"nmax>=4");
    _ndim = ndim;
    _nmin = nmin;
    _nmax = nmax;
    _boxer = boxer;
    _root = new Node(1);
    _smin = new float[_ndim];
    _smax = new float[_ndim];
    _tmin = new float[_ndim];
    _tmax = new float[_ndim];
  }

  /**
   * Returns the size of this tree. The size is the number of objects 
   * added minus the number of objects removed from this tree.
   * @return the number of boxed objects.
   */
  public int size() {
    return _size;
  }

  /** 
   * Removes all objects from this tree.
   */
  public void clear() {
    _root = new Node(1);
    _size = 0;
  }

  /**
   * Returns true if this tree contains no objects.
   * @return true, if empty; false, otherwise.
   */
  public boolean isEmpty() {
    return _size==0;
  }

  /**
   * Adds the specified object to this tree, if not already present.
   * @param object the object.
   * @return true, if the object was added; false, otherwise.
   */
  public boolean add(Object object) {
    Check.argument(object!=null,"object is not null");
    Node leaf = _root.chooseNodeFor(object);
    if (leaf.indexOf(object)>=0)
      return false;
    leaf.add(object);
    ++_size;
    ++_modIndex;
    //validate(); // TODO: remove this statement; it is for DEBUG ONLY!
    return true;
  }

  /**
   * Adds the specified objects to this tree, if not already present.
   * This method packs the objects, which means that it adds them in a
   * special order. The goal is to reduce (1) the cost of building the 
   * R-tree, (2) the cost of subsequent queries, and (3) the memory 
   * consumed by the R-tree. However, depending on the locations and
   * sizes of the added objects, packing may increase these costs.
   * Packing seems to work best for uniformly distributed objects with
   * similar size. Also, packing is best when this method is called for 
   * an empty R-tree.
   * @param objects the objects.
   * @return the number of objects added.
   */
  public int addPacked(Object[] objects) {

    // Remember number of objects in this tree before adding.
    int size = _size;

    // Object indices and centers.
    int n = objects.length;
    int[] index = new int[n];
    float[][] x = new float[_ndim][n];
    float[] xmin = new float[3];
    float[] xmax = new float[3];
    for (int i=0; i<n; ++i) {
      index[i] = i;
      _boxer.getBounds(objects[i],xmin,xmax);
      for (int idim=0; idim<_ndim; ++idim)
        x[idim][i] = 0.5f*(xmin[idim]+xmax[idim]);
    }

    // Add objects, packed recursively along each dimension.
    addPacked(0,x,0,n,index,objects);

    // Return number of objects added.
    return _size-size;
  }

  /**
   * Deletes the specified object from this tree, if present.
   * @param object the object.
   * @return true, if the object was removed; false, otherwise.
   */
  public boolean remove(Object object) {
    Check.argument(object!=null,"object is not null");
    Node leaf = _root.findLeafWith(object);
    if (leaf==null)
      return false;
    leaf.remove(object,null);
    --_size;
    ++_modIndex;
    //validate(); // TODO: remove this statement; it is for DEBUG ONLY!
    return true;
  }

  /**
   * Determines whether this tree contains the specified object.
   * @param object the object.
   * @return true, if the object is in this tree; false, otherwise.
   */
  public boolean contains(Object object) {
    Node leaf = _root.findLeafWith(object);
    return leaf!=null;
  }

  /**
   * Returns an iterator for all objects in this tree. 
   * <p>
   * The iterator does not support removal. A call to the method 
   * {@link java.util.Iterator#remove()} will cause a
   * {@link java.lang.UnsupportedOperationException}.
   * <p>
   * The iterator does not support concurrent modification. If this tree 
   * is modified after the iterator has been returned, a subsequent call 
   * to the method {@link java.util.Iterator#next()} will cause a
   * {@link java.util.ConcurrentModificationException}.
   * @return the iterator.
   */
  public Iterator<Object> iterator() {
    return new RTreeIterator();
  }

  /**
   * Gets the number of levels in this tree.
   * @return the number of levels.
   */
  public int getLevels() {
    return _root.level();
  }

  /**
   * Finds all objects with bounds that overlap the specified bounds.
   * @param min array of bounding min coordinates.
   * @param max array of bounding max coordinates.
   * @return the array of objects found.
   */ 
  public Object[] findOverlapping(float[] min, float[] max) {
    Check.argument(min.length==_ndim,"min.length equals tree ndim");
    Check.argument(max.length==_ndim,"max.length equals tree ndim");
    ArrayList<Object> list = new ArrayList<Object>();
    _root.findOverlapping(min,max,list);
    return list.toArray();
  }

  /**
   * Finds all objects with bounds that overlap the specified box.
   * @param box the box.
   * @return the array of objects found.
   * @see Box
   */ 
  public Object[] findOverlapping(Box box) {
    Check.argument(box!=null,"box is not null");
    Check.argument(box._ndim==_ndim,"box ndim = tree ndim");
    return findOverlapping(box._min,box._max);
  }

  /**
   * Finds all objects in a specified sphere. An object is considered 
   * <em>in</em> the sphere if the distance from the sphere's center to 
   * the object (not its bounds) is less than or equal to the sphere's 
   * radius.
   * @param center array of sphere center coordinates.
   * @param radius the sphere radius.
   * @return the array of objects found.
   */
  public Object[] findInSphere(float[] center, float radius) {
    Check.argument(center.length==_ndim,"center.length equals tree ndim");
    ArrayList<Object> list = new ArrayList<Object>();
    _root.findInSphere(center,radius,list);
    return list.toArray();
  }

  /**
   * Finds the object nearest to the specified point.
   * @param point array of point coordinates.
   * @return the nearest object; null, if this tree is empty.
   */
  public Object findNearest(float[] point) {
    if (isEmpty())
      return null;
    return findNearest(1,point)[0];
  }

  /**
   * Finds the k objects nearest to the specified point.
   * @param k the number of nearest objects to find.
   * @param point array of point coordinates.
   * @return the array of objects, ordered by increasing distance to the point.
   */
  public Object[] findNearest(int k, float[] point) {
    Check.argument(point.length==_ndim,"point.length equals tree ndim");
    Nearest nearest = new Nearest(k,point);
    _root.findNearest(nearest);
    return nearest.toArray();
  }

  /**
   * Gets the leaf node area, the sum of the areas of all leaf node boxes.
   * @return the area.
   */
  public float getLeafArea() {
    return _root.areaLeaf();
  }

  /**
   * Gets the leaf node volume, the sum of the volumes of all leaf node boxes.
   * @return the volume.
   */
  public float getLeafVolume() {
    return _root.volumeLeaf();
  }
  
  /**
   * Prints this tree to the standard output stream.
   * Intended for debugging, only.
   */
  public void dump() {
    _root.dump(_root);
  }

  /**
   * Validates the internal state of this tree. 
   * Intended for debugging, only, with assertions enabled.
   */
  public void validate() {
    _root.validate();
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  /**
   * The default boxer simply assumes that all objects are boxed.
   */
  private static class DefaultBoxer implements Boxer {
    public final void getBounds(Object object, float[] min, float[] max) {
      ((Boxed)object).getBounds(min,max);
    }
    public final float getDistanceSquared(Object object, float[] point) {
      return ((Boxed)object).getDistanceSquared(point);
    }
  }

  /**
   * A node is a box with a list of box children. The node's bounds tightly 
   * cover the bounds of its children; all children of a node lie inside the 
   * node's bounds. 
   * <p> 
   * All nodes except for the root node have a non-null parent. The parent 
   * of the root is null. Leaf nodes have children that are not nodes. 
   * Non-leaf nodes contain nodes.
   * <p>
   * Nodes exist within numbered levels of the tree. Leaf nodes are at 
   * level 1, parents of leaf nodes are at level 2, and so on. A node's
   * level is constant; nodes do not move up or down within the tree.
   * <p>
   * Every leaf node contains between nmin and nmax children. Every non-leaf 
   * node except for the root node also contains between nmin and nmax node 
   * children. The root node contains at least two children, unless it is a 
   * leaf node, in which case it may contain between zero and nmax children.
   */
  private class Node {

    /**
     * Constructs an empty new node at the specified level.
     * @param level the level.
     */
    Node(int level) {
      _level = level;
      for (int idim=0; idim<_ndim; ++idim) {
        _min[idim] =  INFINITY;
        _max[idim] = -INFINITY;
      }
    }

    /**
     * Returns the level of this node.
     * @return the level.
     */
    int level() {
      return _level;
    }

    /**
     * Determines whether this node is a leaf node.
     * @return true, if a leaf node; false, otherwise.
     */
    boolean isLeaf() {
      return _level==1;
    }

    /**
     * Returns the size of this node. The size is the number of box children.
     * @return the size.
     */
    int size() {
      return _nbox;
    }

    /**
     * Adds the specified box to this node. If addition causes this node 
     * to have more then the maximum number of boxes, then this method 
     * splits this node, thereby creating a new node, and recursively adds 
     * that new node to this node's parent.
     * @param box the box to add.
     * @return true, if the box was added; false, otherwise.
     */
    void add(Object box) {

      // If this node has room for the specified box, ...
      if (_nbox<_nmax) {

        // Append the box to this node's list of boxes.
        append(box);

        // Expand this node (and parents, if necessary) for the box added.
        expandUp(box);

      // Else, if this node is full, ...
      } else {

        // Split this node, thereby creating a new node.
        Node nodeNew = split(box);

        // The split modified this old node, so update up the tree.
        updateUp();
        
        // If this node is the root, construct new root with this one child.
        if (_parent==null) {
          _root = _parent = new Node(_level+1);
          _parent.add(this);
        }

        // Recursively add the new node to this node's parent.
        _parent.add(nodeNew);
      }
    }

    /**
     * Removes the specified box from this node. If removal causes this 
     * node to have fewer than the minimum number of boxes, then this 
     * method moves any remaining boxes to the specified list of orphans, 
     * and recursively removes this node from its parent. When the recursion 
     * terminates, this method re-adds all orphan boxes to the tree.
     * @param box the box to remove.
     * @param orphans the list of orphans; non-null for recursive calls.
     */
    void remove(Object box, ArrayList<Object> orphans) {

      // Index of box in this node's list of boxes.
      int ibox = indexOf(box);
      assert ibox>=0:"box is a child of this node";

      // Remove the box from this node's list.
      for (int jbox=ibox+1; jbox<_nbox; ++jbox)
        _boxs[jbox-1] = _boxs[jbox];
      --_nbox;

      // If this node has sufficient boxes, ...
      if (_nbox>=_nmin || _parent==null) {

        // Update this node and up the tree.
        updateUp();

      // Else, if this node has too few boxes, ...
      } else {

        // If necessary, create the list for orphaned siblings.
        if (orphans==null)
          orphans = new ArrayList<Object>();

        // Remaining boxes in this node become orphans.
        for (int jbox=0; jbox<_nbox; ++jbox)
          orphans.add(_boxs[jbox]);
        _nbox = 0;

        // Recursively remove this node from its parent.
        _parent.remove(this,orphans);

        // Add all orphan boxes. Some may be nodes with non-zero level.
        int norphan = orphans.size();
        for (int iorphan=0; iorphan<norphan; ++iorphan) {
          Object orphan = orphans.get(iorphan);
          Node parent = _root.chooseNodeFor(orphan);
          parent.add(orphan);
        }
        orphans.clear();
      }

      // If the root node has one box and the root node is not a leaf node,
      // then the box is a node, and it becomes the root node.
      if (_root._nbox==1 && _root._level>1) {
        _root = (Node)_root._boxs[0];
        _root._parent = null;
      }
    }

    /**
     * Searches this node recursively for boxes that overlap the specified 
     * bounds. Appends any overlapping non-node boxes to the specified list.
     * @param min array of min coordinates.
     * @param max array of max coordinates.
     * @param list the list to which overlapping boxes are appended.
     */
    void findOverlapping(float[] min, float[] max, ArrayList<Object> list) {
      for (int ibox=0; ibox<_nbox; ++ibox) {
        Object boxi = _boxs[ibox];
        if (overlaps(boxi,min,max)) {
          if (isLeaf()) {
            list.add(boxi);
          } else {
            ((Node)boxi).findOverlapping(min,max,list);
          }
        }
      }
    }

    /**
     * Searches this node recursively for objects inside the specified sphere.
     */
    void findInSphere(float[] center, float radius, ArrayList<Object> list) {
      float ss = radius*radius;
      for (int ibox=0; ibox<_nbox; ++ibox) {
        Object boxi = _boxs[ibox];
        float ds = distanceSquared(boxi,center);
        if (ds<=ss) {
          if (isLeaf()) {
            list.add(boxi);
          } else {
            ((Node)boxi).findInSphere(center,radius,list);
          }
        }
      }
    }

    /**
     * Chooses a node to contain the specified box. The box may be a node 
     * with a non-zero level. (Non-node boxes have level zero.) The chosen 
     * node will have a level one greater than that of the specified box.
     * <p>
     * If this node has the correct level, then this method simply returns
     * this node. Otherwise, it searches for the node child that will yield 
     * the smallest increase in volume, and recursively chooses that node. 
     * In case of a tie, it chooses the node child with smallest volume.
     * @param box the box, with level less than the level of this node.
     * @return the chosen node.
     */
    Node chooseNodeFor(Object box) {

      // Level of the desired node equals one plus the level of the box.
      // The level of the box must be less than the level of this node.
      int level = 1+level(box);
      assert _level>=level:"level of this node exceeds level of box";

      // If this node has the correct level, choose it.
      if (_level==level)
        return this;

      // Otherwise, recursively choose the most suitable node child.
      Node node = null;
      float dmin = INFINITY;
      float vmin = INFINITY;
      for (int ibox=0; ibox<_nbox; ++ibox) {
        Node nodei = (Node)_boxs[ibox];
        float d = nodei.volumeDelta(box);
        if (d<=dmin) {
          float v = nodei.volume();
          if (d<dmin || v<vmin) {
            node = nodei;
            dmin = d;
            vmin = v;
          }
        }
      }
      return node.chooseNodeFor(box);
    }

    /**
     * Finds the leaf node, if any, that contains the specified box.
     * @param box the box.
     * @return the leaf node; null, if none.
     */
    Node findLeafWith(Object box) {
      if (isLeaf()) {
        for (int ibox=0; ibox<_nbox; ++ibox) {
          if (_boxs[ibox].equals(box))
            return this;
        }
      } else {
        for (int ibox=0; ibox<_nbox; ++ibox) {
          Node nodei = (Node)_boxs[ibox];
          if (nodei.overlaps(box)) {
            Node node = nodei.findLeafWith(box);
            if (node!=null)
              return node;
          }
        }
      }
      return null;
    }

    /**
     * Recursively finds the k nearest boxes among this node's children.
     */
    void findNearest(Nearest nearest) {

      // If this node is a leaf node, update the nearest for each box.
      if (isLeaf()) {
        for (int ibox=0; ibox<_nbox; ++ibox)
          nearest.update(_boxs[ibox]);

      // Else, if this node is not a leaf node, ...
      } else {

        // Make a list of this node's children, sorted by distance.
        ArrayList<BoxDistance> list = new ArrayList<BoxDistance>(_nbox);
        for (int ibox=0; ibox<_nbox; ++ibox) {
          Object box = _boxs[ibox];
          float distance = distanceSquared(box,nearest.point());
          list.add(new BoxDistance(box,distance));
        }
        Collections.sort(list);

        // For each child of this node, ...
        for (int ibox=0; ibox<_nbox; ++ibox) {
          BoxDistance bd = list.get(ibox);

          // If the child cannot be pruned, recursively find nearest boxes.
          if (bd.distance<nearest.cutoff())
            ((Node)bd.box).findNearest(nearest);
        }
      }
    }

    /**
     * Returns the distance-squared from this node to a point.
     */
    float distanceSquared(float[] point) {
      float sum = 0.0f;
      for (int idim=0; idim<_ndim; ++idim) {
        float p = point[idim];
        float s = _min[idim];
        float t = _max[idim];
        float d = (p<s)?p-s:(p>t)?p-t:0.0f;
        sum += d*d;
      }
      return sum;
    }

    /**
     * Returns the distance-squared from specified box to a point.
     */
    float distanceSquared(Object box, float[] point) {
      if (box instanceof Node) {
        return ((Node)box).distanceSquared(point);
      } else {
        return _boxer.getDistanceSquared(box,point);
      }
    }

    /**
     * Gets the index of the specified box child of this node. If the 
     * specified box is a node, then we quickly look for the same child 
     * node. Otherwise, if the box is not a node, we look for a box that 
     * equals the specified box. If we find an equal box, it must be the 
     * only one, since each box in the tree is unique.
     * @param box the box.
     * @return the index; -1, if the box is not a child of this node.
     */
    int indexOf(Object box) {
      if (box instanceof Node) {
        for (int ibox=0; ibox<_nbox; ++ibox) {
          if (box==_boxs[ibox])
            return ibox;
        }
      } else {
        for (int ibox=0; ibox<_nbox; ++ibox) {
          if (box.equals(_boxs[ibox]))
            return ibox;
        }
      }
      return -1;
    }

    /**
     * Returns the level of the specified box. This method works for any
     * box, including non-node boxes with level zero.
     */
    int level(Object box) {
      return (box instanceof Node)?((Node)box)._level:0;
    }

    /**
     * Returns the sum of the volumes of all leaf nodes within this node.
     */
    float volumeLeaf() {
      float volume = 0.0f;
      for (int ibox=0; ibox<_nbox; ++ibox) {
        Object box = _boxs[ibox];
        if (box instanceof Node) {
          Node node = (Node)box;
          if (node.isLeaf()) {
            volume += volume(box);
          } else {
            volume += node.volumeLeaf();
          }
        }
      }
      return volume;
    }

    /**
     * Returns the sum of the areas of all leaf nodes within this node.
     */
    float areaLeaf() {
      float area = 0.0f;
      for (int ibox=0; ibox<_nbox; ++ibox) {
        Object box = _boxs[ibox];
        if (box instanceof Node) {
          Node node = (Node)box;
          if (node.isLeaf()) {
            area += area(box);
          } else {
            area += node.areaLeaf();
          }
        }
      }
      return area;
    }

    void dump(Object box) {
      int indent = 2*(level(_root)-level(box));
      StringBuffer sb = new StringBuffer(indent);
      for (int i=0; i<indent; ++i)
        sb.append(' ');
      System.out.println(sb.toString()+box);
      if (box instanceof Node) {
        Node node = (Node)box;
        int nbox = node._nbox;
        Object[] boxs = node._boxs;
        for (int ibox=0; ibox<nbox; ++ibox)
          dump(boxs[ibox]);
      }
    }

    void validate() {
      assert _parent!=null || _root==this:"node without parent is root";
      if (_root!=this) {
        assert _nbox>=_nmin:"_nbox>=_nmin";
        assert _nbox<=_nmax:"_nbox<=_nmin";
      } else if (isLeaf()) {
        assert _nbox>=0:"_nbox>=0";
      } else {
        assert _nbox>=2:"_nbox>=2";
      }
      assert _nbox<=_nmax:"_nbox<=_nmin";
      Node tmp = new Node(0);
      for (int ibox=0; ibox<_nbox; ++ibox) {
        if (!isLeaf()) {
          Node node = (Node)_boxs[ibox];
          assert node._parent==this:"node._parent==this";
          assert node._level==_level-1:"node._level==_level-1";
          node.validate();
        }
        tmp.expand(_boxs[ibox]);
      }
      for (int idim=0; idim<_ndim; ++idim) {
        assert _min[idim]==tmp._min[idim]:"minimum bounds are correct";
        assert _max[idim]==tmp._max[idim]:"maximum bounds are correct";
      }
    }

    private static final float INFINITY = Float.MAX_VALUE;

    private float[] _min = new float[_ndim]; // min coordinate bounds
    private float[] _max = new float[_ndim]; // max coordinate bounds
    private Node _parent; // parent node; null, if root node
    private int _level; // node level; one, if leaf node
    private int _nbox; // number of boxes in this node
    private Object[] _boxs = new Object[_nmax]; // array of boxes 

    /**
     * Determines whether this node overlaps (intersects) the specified box.
     */
    private boolean overlaps(Object box) {
      loadB(box);
      for (int idim=0; idim<_ndim; ++idim) {
        if (_min[idim]>_bmax[idim] || _max[idim]<_bmin[idim])
          return false;
      }
      return true;
    }

    /**
     * Determines whether the specified box overlaps the specified bounds.
     */
    private boolean overlaps(Object box, float[] min, float[] max) {
      loadB(box);
      for (int idim=0; idim<_ndim; ++idim) {
        if (_bmin[idim]>max[idim] || _bmax[idim]<min[idim])
          return false;
      }
      return true;
    }

    /**
     * Returns the volume of this node.
     */
    private float volume() {
      float v = 1.0f;
      for (int idim=0; idim<_ndim; ++idim)
        v *= _max[idim]-_min[idim];
      return v;
    }

    /**
     * Returns the volume of the specified box.
     */
    private float volume(Object box) {
      loadB(box);
      float v = 1.0f;
      for (int idim=0; idim<_ndim; ++idim)
        v *= _bmax[idim]-_bmin[idim];
      return v;
    }

    /**
     * Returns the area of the specified box.
     */
    private float area(Object box) {
      float v = volume(box);
      loadB(box);
      float area = 0.0f;
      for (int idim=0; idim<_ndim; ++idim) {
        float d = _bmax[idim]-_bmin[idim];
        area += 2.0f*v/d;
      }
      return area;
    }

    /**
     * Returns the increase in volume for this node due to the specified box.
     */
    private float volumeDelta(Object box) {
      loadB(box);
      float vnew = 1.0f;
      float vold = 1.0f;
      for (int idim=0; idim<_ndim; ++idim) {
        float amin = _min[idim];
        float amax = _max[idim];
        vold *= amax-amin;
        float bmin = _bmin[idim];
        float bmax = _bmax[idim];
        if (bmin<amin) 
          amin = bmin;
        if (bmax>amax) 
          amax = bmax;
        vnew *= amax-amin;
      }
      return vnew-vold;
    }

    /**
     * Returns the increase in volume of box A if expanded to cover box B.
     */
    private float volumeDelta(Object abox, Object bbox) {
      loadA(abox);
      loadB(bbox);
      float vnew = 1.0f;
      float vold = 1.0f;
      for (int idim=0; idim<_ndim; ++idim) {
        float amin = _amin[idim];
        float amax = _amax[idim];
        vold *= amax-amin;
        float bmin = _bmin[idim];
        float bmax = _bmax[idim];
        if (bmin<amin) 
          amin = bmin;
        if (bmax>amax) 
          amax = bmax;
        vnew *= amax-amin;
      }
      return vnew-vold;
    }

    /**
     * Appends the specified box to this node's list of boxes.
     * If the box is a node, sets its parent to this node.
     * @param box the box.
     */
    private void append(Object box) {
      _boxs[_nbox++] = box;
      if (box instanceof Node)
        ((Node)box)._parent = this;
    }

    /**
     * Expands bounds of this node, if necessary, for the specified box.
     * @param box the box.
     * @return true, if this node changed; false, otherwise.
     */
    private boolean expand(Object box) {
      loadB(box);
      boolean changed = false;
      for (int idim=0; idim<_ndim; ++idim) {
        if (_bmin[idim]<_min[idim]) {
          _min[idim] = _bmin[idim];
          changed = true;
        }
        if (_bmax[idim]>_max[idim]) {
          _max[idim] = _bmax[idim];
          changed = true;
        }
      }
      return changed;
    }

    /**
     * Updates bounds of this node, if necessary.
     * @return true, if this node changed; false, otherwise.
     */
    private boolean update() {
      for (int idim=0; idim<_ndim; ++idim) {
        _smin[idim] = _min[idim];
        _smax[idim] = _max[idim];
        _min[idim] =  INFINITY;
        _max[idim] = -INFINITY;
      }
      for (int ibox=0; ibox<_nbox; ++ibox)
        expand(_boxs[ibox]);
      for (int idim=0; idim<_ndim; ++idim) {
        if (_min[idim]!=_smin[idim] || _max[idim]!=_smax[idim])
          return true;
      }
      return false;
    }

    /**
     * Expands bounds of this node, if necessary, for the specified box. 
     * If this node is changed and has a non-null parent, this method 
     * recursively expands that parent node.
     * @param box the box.
     */
    private void expandUp(Object box) {
      if (expand(box) && _parent!=null)
        _parent.expandUp(this);
    }

    /**
     * Updates bounds of this node, if necessary. If this node is changed 
     * and has a a non-null parent, this method recursively updates that 
     * parent node.
     */
    private void updateUp() {
      if (update() && _parent!=null)
        _parent.updateUp();
    }

    /**
     * Splits this node in two while adding the specified box. After 
     * creating a new node, assigns the specified box and all of the 
     * box children of this node to either this node or the new node,
     * using Guttman's quadratic method.
     * @param box the box that causes this node to be split.
     * @return the new node.
     */
    private Node split(Object box) {

      // Move the boxes in this node to a temporary array.
      // Append the specified box to the end of that array.
      int nbox = _nbox+1;
      Object[] boxs = new Object[nbox];
      for (int ibox=0; ibox<_nbox; ++ibox)
        boxs[ibox] = _boxs[ibox];
      boxs[_nbox] = box;
      _nbox = 0;

      // This old node to be rebuilt and new node created by the split.
      Node node1 = this;
      Node node2 = new Node(_level);

      // Find the two boxes that least belong in the same node.
      float dmax = -INFINITY;
      int imax = -1;
      int jmax = -1;
      for (int ibox=0; ibox<nbox; ++ibox) {
        Object bbox = boxs[ibox];
        float bvol = volume(bbox);
        for (int jbox=ibox+1; jbox<nbox; ++jbox) {
          Object abox = boxs[jbox];
          float dbox = volumeDelta(abox,bbox)-bvol;
          if (dbox>dmax) {
            dmax = dbox;
            imax = ibox;
            jmax = jbox;
          }
        }
      }

      // Append the two boxes found to their respective nodes. Mark them so 
      // that we will not consider them again. Note that we do not call the
      // method add, because we do not want expansion of nodes up the tree.
      node1.append(boxs[imax]);
      node2.append(boxs[jmax]);
      boxs[imax] = null;
      boxs[jmax] = null;

      // For all of the remaining boxes, ...
      for (int ibox=2; ibox<nbox; ++ibox) {

        // The index of the box to be appended to one of the two nodes.
        int kbox = -1;

        // The node to which we will append the box.
        Node nodek = null;

        // If one of the nodes needs all of the remaining boxes, choose it.
        int nsmall = _nmin-(nbox-ibox);
        if (nsmall==node1.size()) {
          nodek = node1;
        } else if (nsmall==node2.size()) {
          nodek = node2;
        }

        // If the node has been chosen, then any box will do.
        if (nodek!=null) {
          for (int jbox=0; jbox<nbox && kbox<0; ++jbox) {
            if (boxs[jbox]!=null)
              kbox = jbox;
          }

        // Else, choose the box before choosing the node.
        } else {

          // Choose the box with greatest preference for either node.
          dmax = -INFINITY;
          for (int jbox=0; jbox<nbox; ++jbox) {
            Object boxj = boxs[jbox];
            if (boxj!=null) {
              float d1 = node1.volumeDelta(boxj);
              float d2 = node2.volumeDelta(boxj);
              float dbox = (d1>=d2)?d1-d2:d2-d1;
              if (dbox>dmax) {
                dmax = dbox;
                kbox = jbox;
              }
            }
          }

          // Choose the node that (1) yields the smaller increase in volume, 
          // or (2) that has the smaller volume or (3) the smaller size.
          float delta1 = node1.volumeDelta(boxs[kbox]);
          float delta2 = node2.volumeDelta(boxs[kbox]);
          if (delta1==delta2) {
            float volume1 = node1.volume();
            float volume2 = node2.volume();
            if (volume1==volume2) {
              int size1 = node1.size();
              int size2 = node1.size();
              nodek = (size1<size2)?node1:node2;
            } else {
              nodek = (volume1<volume2)?node1:node2;
            }
          } else {
            nodek = (delta1<delta2)?node1:node2;
          }
        }

        // Append the chosen box to the chosen node, and mark the box gone.
        nodek.append(boxs[kbox]);
        boxs[kbox] = null;
      }

      // Update and return the new node. The new node still has no parent. 
      // It will be adopted by this node's parent, which may not yet exist.
      node2.update();
      return node2;
    }
  }

  /**
   * Set of boxes and distances used for k-nearest-boxed-object queries.
   * Nearest boxed objects are kept in a sorted set, ordered by increasing
   * distance to the query point. The set is updated for any object at a
   * distance less than the cutoff distance to the k'th object in the set. 
   * Initially, and until the set is full with k objects, that cutoff 
   * distance is infinity. So the first k objects always go into the set.
   */
  private class Nearest {
    Nearest(int k, float[] point) {
      _k = k;
      _point = point;
      _set = new TreeSet<BoxDistance>();
      _full = false;
      _cutoff = Float.MAX_VALUE;
    }
    void update(Object box) {
      float d = _boxer.getDistanceSquared(box,_point);
      if (d<_cutoff) {
        BoxDistance bd = new BoxDistance(box,d);
        if (_full)
          _set.remove(_set.last());
        _set.add(bd);
        _full = _full || _k==_set.size();
        if (_full)
          _cutoff = _set.last().distance;
      }
    }
    float[] point() {
      return _point;
    }
    float cutoff() {
      return _cutoff;
    }
    Object[] toArray() {
      Object[] boxs = new Object[_set.size()];
      Iterator<BoxDistance> i = _set.iterator();
      for (int ibox=0; i.hasNext(); ++ibox)
        boxs[ibox] = i.next().box;
      return boxs;
    }
    private int _k;
    private float[] _point;
    private TreeSet<BoxDistance> _set;
    private boolean _full;
    private float _cutoff;
  }

  /**
   * A box and a distance used for k-nearest-boxed-object queries.
   */
  private static class BoxDistance implements Comparable<BoxDistance> {
    Object box;
    float distance;
    BoxDistance(Object box, float distance) {
      this.box = box;
      this.distance = distance;
    }
    public int compareTo(BoxDistance bd) {
      if (distance<bd.distance) {
        return -1;
      } else if (distance>bd.distance) {
        return 1;
      } else {
        int th = System.identityHashCode(box);
        int oh = System.identityHashCode(bd.box);
        if (th<oh) {
          return -1;
        } else if (th>oh) {
          return 1;
        } else {
          return 0;
        }
      }
    }
    public boolean equals(Object o) {
      BoxDistance bd = (BoxDistance)o;
      return box==bd.box && distance==bd.distance;
    }
  }

  private class RTreeIterator implements Iterator<Object> {
    public boolean hasNext() {
      return _next!=null;
    }
    public Object next() {
      if (_next==null)
        throw new NoSuchElementException();
      if (_modIndexExpected!=_modIndex)
        throw new ConcurrentModificationException();
      Object object = _next;
      loadNext();
      return object;
    }
    public void remove() {
      throw new UnsupportedOperationException();
    }
    RTreeIterator() {
      _leaf = _root;
      while (!_leaf.isLeaf())
        _leaf = (Node)_leaf._boxs[0];
      _ibox = 0;
      _next = (_ibox<_leaf._nbox)?_leaf._boxs[0]:null;
      _modIndexExpected = _modIndex;
    }
    private Node _leaf; // current leaf node
    private int _ibox; // index of next object in current leaf node
    private Object _next; // next object in current leaf node
    private int _modIndexExpected; // for fail-fast iteration
    /**
     * Loads the next non-node object. If the current leaf node has another 
     * object, we choose that one. Otherwise, we walk up the tree, looking 
     * for a branch not yet visited. If we find such branch, we then walk 
     * down the tree to a new leaf node. If we do not find such a branch, 
     * because we have visited every child of the root node, or because the 
     * root node is a leaf, then iteration is complete.
     */
    private void loadNext() {
      ++_ibox;
      if (_ibox==_leaf._nbox) {
        Node node = _leaf;
        Node parent = node;
        while (node==parent && parent!=_root) {
          parent = node._parent;
          int ibox = 1+parent.indexOf(node);
          if (ibox<parent._nbox) {
            node = (Node)parent._boxs[ibox];
            while (!node.isLeaf())
              node = (Node)node._boxs[0];
            _leaf = node;
            _ibox = 0;
          } else {
            node = parent;
          }
        }
      }
      _next = (_ibox<_leaf._nbox)?_leaf._boxs[_ibox]:null;
    }
  }

  private Node _root; // root of the tree
  private int _ndim; // number of dimensions per box
  private int _nmin; // minimum number of boxes per node (except root)
  private int _nmax; // maximum number of boxes per node
  private int _size; // number of (non-node) boxes in this tree
  private Boxer _boxer; // computes bounds of objects
  private int _modIndex; // for fail-fast iteration
  
  // To reduce memory consumption, the R-tree does not cache the bounds 
  // of external objects. It caches bounds for only its internal nodes.
  // (Each node has arrays of min/max coordinates.) For simplicity, methods 
  // that access the box bounds of external objects and internal nodes are 
  // written without regard for this difference. Instead, box bounds for 
  // generic (external or internal) object arguments to such methods are 
  // loaded into box registers A or B. When node bounds are loaded into 
  // these registers, the A or B arrays are set to simply reference the 
  // min/max arrays of the node. In other words, node bounds are loaded 
  // quickly, by reference. When non-node object bounds are loaded, the 
  // R-tree's boxer gets the min and max coordinates for the object in the 
  // arrays S or T, and sets the A or B arrays to reference those S or T 
  // arrays, accordingly. See the methods loadA and loadB, below.
  private float[] _amin; // min coordinates for box register A
  private float[] _amax; // max coordinates for box register A
  private float[] _bmin; // min coordinates for box register B
  private float[] _bmax; // max coordinates for box register B
  private float[] _smin; // temporary min coordinates; used for register A
  private float[] _smax; // temporary max coordinates; used for register A
  private float[] _tmin; // temporary min coordinates; used for register B
  private float[] _tmax; // temporary max coordinates; used for register B

  /**
   * Loads the bounds of the specified box into the box register A.
   * If the box is a node, the node bounds are copied by reference.
   * Otherwise, the bounds are copied by value via the boxer interface.
   */
  private void loadA(Object box) {
    if (box instanceof Node) {
      Node node = (Node)box;
      _amin = node._min;
      _amax = node._max;
    } else {
      _boxer.getBounds(box,_smin,_smax);
      _amin = _smin;
      _amax = _smax;
    }
  }

  /**
   * Loads the bounds of the specified box into the box register B.
   * If the box is a node, the node bounds are copied by reference.
   * Otherwise, the bounds are copied by value via the boxer interface.
   */
  private void loadB(Object box) {
    if (box instanceof Node) {
      Node node = (Node)box;
      _bmin = node._min;
      _bmax = node._max;
    } else {
      _boxer.getBounds(box,_tmin,_tmax);
      _bmin = _tmin;
      _bmax = _tmax;
    }
  }

  private void addPacked(
    int idim, float[][] x, int p, int q, int[] index, Object[] objects) 
  {
    int kdim = _ndim-idim;

    // If nothing to add, simply return.
    if (p>=q) {
      //return;
    }

    // Otherwise, if packed for all dimensions, add the objects.
    else if (kdim==0) {
      //System.out.println("  p="+p+" q="+q);
      for (int i=p; i<q; ++i) {
        //System.out.println("    index="+index[i]);
        add(objects[index[i]]);
      }
    }

    // Otherwise, ...
    else {

      // Sort slab by object center coordinates for current dimension.
      int n = index.length;
      int nsort = q-p;
      int[] isort = index;
      float[] xsort = x[idim];
      if (nsort<n) {
        isort = new int[nsort];
        xsort = new float[nsort];
        float[] xidim = x[idim];
        for (int jsort=0; jsort<nsort; ++jsort) {
          isort[jsort] = jsort;
          xsort[jsort] = xidim[index[p+jsort]];
        }
      }
      quickIndexSort(xsort,isort);
      if (nsort<n) {
        for (int jsort=0; jsort<nsort; ++jsort)
          isort[jsort] = index[p+isort[jsort]];
        for (int jsort=0; jsort<nsort; ++jsort)
          index[p+jsort] = isort[jsort];
      }

      // Number of leaf nodes required for the sorted slab.
      int nleaf = 1+nsort/_nmax;

      // Number of slabs in next dimension.
      int nslab = (int)Math.ceil(Math.pow(nleaf,1.0/kdim));

      // Number of objects per slab in next dimension.
      int mslab = (int)Math.ceil((double)nsort/(double)nslab);
      //System.out.println(
      //  "idim="+idim+" nleaf="+nleaf+" nslab="+nslab+" mslab="+mslab);

      // Recursively add packed objects, one slab at a time.
      for (int pslab=p; pslab<q; pslab+=mslab) {
        int qslab = pslab+mslab;
        if (qslab>q)
          qslab = q;
        if (qslab>pslab)
          addPacked(idim+1,x,pslab,qslab,index,objects);
      }
    }
  }
}
