/****************************************************************************
Copyright (c) 2008, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.interp;

import static edu.mines.jtk.util.ArrayMath.*;
import edu.mines.jtk.util.Check;

/**
 * A min- or max-heap of times sampled in a 3D array. 
 * Such a heap is typically used in fast marching methods. It enhances
 * a conventional heap by maintaining a map of heap entries in a 3D array 
 * of indices. Given array indices (i1,i2,i3), this index map enables O(1) 
 * access to heap entries. Such fast access is required as times in the
 * heap are reduced while marching.
 * <p>
 * Depending on the type of heap, min or max, the entry with either the 
 * smallest or largest time is stored at the top (root) of the heap. This
 * entry can be accessed with complexity O(1) and removed with complexity 
 * O(log N), where N is the number of entries in the heap. Complexity for
 * inserting new entries or reducing the times for existing entries is
 * O(log N).
 * 
 * @author Dave Hale, Colorado School of Mines
 * @version 2008.07.07
 */
class TimeHeap3 {

  /**
   * The heap type determines whether the entry at the top has the minimum
   * or maximum time. That top entry can be accessed with O(1) complexity
   * and removed in O(log N) complexity, where N is the number times in the
   * heap.
   */
  public enum Type {MIN,MAX}

  /**
   * An entry in the heap has sample indices (i1,i2) and a time t.
   * The mark is for external use and is not used by the heap.
   */
  public static class Entry {
    public int i1,i2,i3;
    public float time;
    public int mark;
  }

  /**
   * Constructs a heap with specified type and array dimensions.
   * @param type the type of heap.
   * @param n1 number of samples in 1st dimension.
   * @param n2 number of samples in 2nd dimension.
   * @param n3 number of samples in 3rd dimension.
   */
  public TimeHeap3(Type type, int n1, int n2, int n3) {
    _type = type;
    _n1 = n1;
    _n2 = n2;
    _n3 = n3;
    _imap = new int[n3][n2][n1];
  }

  /**
   * Gets the type of this heap.
   * @return the type.
   */
  public Type getType() {
    return _type;
  }

  /**
   * Gets the number of samples in the 1st dimension.
   * @return the number of samples.
   */
  public int getN1() {
    return _n1;
  }

  /**
   * Gets the number of samples in the 2nd dimension.
   * @return the number of samples.
   */
  public int getN2() {
    return _n2;
  }

  /**
   * Gets the number of samples in the 3rd dimension.
   * @return the number of samples.
   */
  public int getN3() {
    return _n3;
  }

  /**
   * Inserts a new entry into this heap with specified time and indices.
   * The heap must not already contain an entry with those indices.
   * @param i1 the sample index in 1st dimension.
   * @param i2 the sample index in 2nd dimension.
   * @param i3 the sample index in 2nd dimension.
   * @param time the time.
   */
  public void insert(int i1, int i2, int i3, float time) {
    insert(i1,i2,i3,time,0);
  }

  /**
   * Inserts a new entry into this heap with specified time and indices.
   * The heap must not already contain an entry with those indices.
   * @param i1 the sample index in 1st dimension.
   * @param i2 the sample index in 2nd dimension.
   * @param i3 the sample index in 2nd dimension.
   * @param time the time.
   * @param mark a mark to associate with the new entry.
   */
  public void insert(int i1, int i2, int i3, float time, int mark) {
    int i = indexOf(i1,i2,i3); // index of entry with time to reduce
    Check.argument(i<0,"entry with indices (i1,i2) is not in the heap");
    i = _n; // index at which to insert the entry
    if (_n==_e.length) // if necessary, ...
      grow(_n+1); // increase the capacity of this heap
    Entry ei = _e[i];
    if (ei==null) // if an unused entry does not already exist, ...
      ei = new Entry(); // construct a new entry
    ei.i1 = i1;
    ei.i2 = i2;
    ei.i3 = i3;
    ei.time = time;
    ei.mark = mark;
    set(i,ei);
    siftUp(i);
    ++_n;
  }

  /**
   * Reduces the time of the entry in this heap with specified indices.
   * This heap must already contain an entry with those indices, and
   * the specified time must be less than the time for that entry.
   * @param i1 the sample index in 1st dimension.
   * @param i2 the sample index in 2nd dimension.
   * @param i3 the sample index in 2nd dimension.
   * @param time the reduced time.
   */
  public void reduce(int i1, int i2, int i3, float time) {
    int i = indexOf(i1,i2,i3); // index of entry with time to reduce
    Check.argument(i>=0,"entry with indices (i1,i2) is in the heap");
    Check.argument(time<_e[i].time,"specified time less than time in heap");
    _e[i].time = time; // reduce the time
    if (_type==Type.MIN) { // for a min-heap, ...
      siftUp(i); // the entry may need to move up
    } else { // but for a max heap, ...
      siftDown(i); // the entry may need to move down
    }
  }

  /**
   * Removes and returns the heap entry with smallest/largest time.
   * The heap must not be empty.
   */
  public Entry remove() {
    Check.state(_n>0,"heap is not empty");
    Entry e0 = _e[0];
    --_n;
    if (_n>0) {
      set(0,_e[_n]);
      set(_n,e0);
      siftDown(0);
    }
    return e0;
  }

  /**
   * Determines whether this help contains an entry with the specified indices.
   * @param i1 the sample index in 1st dimension.
   * @param i2 the sample index in 2nd dimension.
   * @param i3 the sample index in 3rd dimension.
   * @return true, if in the heap; false, otherwise.
   */
  public boolean contains(int i1, int i2, int i3) {
    return indexOf(i1,i2,i3)>=0;
  }

  /**
   * Removes all entries from this heap.
   */
  public void clear() {
    _n = 0;
  }

  /**
   * Returns the number of entries in this heap.
   */
  public int size() {
    return _n;
  }

  /**
   * Returns true if this heap is empty; false, otherwise.
   */
  public boolean isEmpty() {
    return _n==0;
  }

  /**
   * Dumps this heap to standard output; leading spaces show level in tree.
   */
  public void dump() {
    dump("",0);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private Type _type; // heap type, either MIN or MAX
  private int _n1,_n2,_n3; // array dimensions
  private int _n; // number of entries in this heap
  private int[][][] _imap; // maps array indices (i1,i2,i3) to heap index i
  private Entry[] _e = new Entry[1024]; // array of entries in this heap

  /**
   * Returns the heap index of the entry with array indices (i1,i2).
   * If such an entry is not in the heap, this method returns -1, but
   * throws an exception if the indices i1 or i2 are out of bounds.
   */
  private int indexOf(int i1, int i2, int i3) {
    int i = _imap[i3][i2][i1];
    if (i<0 || i>=_n) 
      return -1;
    Entry ei = _e[i];
    if (ei.i1!=i1 || ei.i2!=i2 || ei.i3!=i3)
      return -1;
    return i;
  }

  /**
   * Sets the i'th entry, and updates the index map accordingly.
   */
  private void set(int i, Entry ei) {
    _e[i] = ei;
    _imap[ei.i3][ei.i2][ei.i1] = i;
  }

  /**
   * If necessary, moves entry e[i] down so not greater/less than children.
   */
  private void siftDown(int i) {
    Entry ei = _e[i]; // entry ei that may move down
    float eit = ei.time; // cached time for entry ei
    int m = _n>>>1; // number of entries with at least one child
    while (i<m) { // while not childless, ...
      int c = (i<<1)+1; // index of left child
      int r = c+1; // index of right child
      Entry ec = _e[c]; // initially assume left child smallest/largest
      if (_type==Type.MIN) { // if min-heap
        if (r<_n && _e[r].time<ec.time) // if right child smallest, ...
          ec = _e[c=r]; // the smaller of left and right children
        if (eit<=ec.time) // break if ei not greater than smaller child
          break;
      } else { // if max-heap
        if (r<_n && _e[r].time>ec.time) // if right child largest, ...
          ec = _e[c=r]; // the larger of left and right children
        if (eit>=ec.time) // break if ei not less than larger child
          break;
      }
      set(i,ec); // move smaller/larger child up
      i = c;
    }
    if (ei!=_e[i]) // if necessary, ...
      set(i,ei); // set ei where it belongs
  }

  /**
   * If necessary, moves entry e[i] up so not less/greater than parent.
   */
  private void siftUp(int i) {
    Entry ei = _e[i]; // entry ei that may move up
    float eit = ei.time; // cached time for entry ei
    while (i>0) { // while a parent (not the root entry), ...
      int p = (i-1)>>>1; // index of parent
      Entry ep = _e[p]; // the parent
      if (_type==Type.MIN) { // if min-heap
        if (eit>=ep.time) // break if ei not less than parent
          break;
      } else {
        if (eit<=ep.time) // break if ei not greater than parent
          break;
      }
      set(i,ep); // ei less/greater than parent, so move parent down
      i = p;
    }
    if (ei!=_e[i]) // if necessary, ...
      set(i,ei); // set ei where it belongs
  }

  /**
   * Grows this heap to have at least the specified capacity.
   */
  private void grow(int minCapacity) {
    if (minCapacity<0) // overflow
      throw new OutOfMemoryError();
    int oldCapacity = _e.length;
    int newCapacity = oldCapacity*2;
    if (newCapacity<0) // overflow
      newCapacity = Integer.MAX_VALUE;
    if (newCapacity<minCapacity)
      newCapacity = minCapacity;
    Entry[] e = new Entry[newCapacity];
    System.arraycopy(_e,0,e,0,oldCapacity);
    _e = e;
  }

  /**
   * Recursively dumps heap entries with leading spaces.
   */
  private void dump(String s, int i) {
    if (i<_n) {
      s = s+"  ";
      Entry e = _e[i];
      System.out.println(s+e.i1+" "+e.i2+" "+e.i3+" "+e.time);
      dump(s,2*i+1);
      dump(s,2*i+2);
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // testing

  public static void main(String[] args) {
    testHeap(new TimeHeap3(TimeHeap3.Type.MIN,9,11,13));
    testHeap(new TimeHeap3(TimeHeap3.Type.MAX,9,11,13));
  }

  private static void testHeap(TimeHeap3 heap) {
    int n1 = heap.getN1();
    int n2 = heap.getN2();
    int n3 = heap.getN3();
    int n = n1*n2*n3;
    float[] s = randfloat(n);
    float[][][] t = reshape(n1,n2,n3,s);
    for (int i3=0,i=0; i3<n3; ++i3) {
      for (int i2=0; i2<n2; ++i2) {
        for (int i1=0; i1<n1; ++i1,++i) {
          float ti = t[i3][i2][i1];
          heap.insert(i1,i2,i3,ti);
          s[i] = ti;
        }
      }
    }
    for (int i3=0,i=0; i3<n3; ++i3) {
      for (int i2=0; i2<n2; ++i2) {
        for (int i1=0; i1<n1; ++i1,++i) {
          s[i] -= 0.5f;
          t[i3][i2][i1] -= 0.5f;
          heap.reduce(i1,i2,i3,t[i3][i2][i1]);
        }
      }
    }
    assert !heap.isEmpty();
    assert heap.size()==n;
    quickSort(s); // increasing order
    if (heap.getType()==TimeHeap3.Type.MAX)
      s = reverse(s); // decreasing order
    for (int i=0; i<n; ++i) {
      Entry e = heap.remove();
      float ti = e.time;
      assert ti==s[i];
    }
    assert heap.isEmpty();
    assert heap.size()==0;
  }
}
