package edu.mines.jtk.opt;

import java.util.*;

/** Get indices that order an array of values. 
    @author W.S. Harlan, Landmark Graphics
 */
public class IndexSorter {
  private static Almost s_almost = new Almost();
  private double[] _dvalues = null;
  private float[] _fvalues = null;
  private int _length = 0;

  /** The array of values that determine the sort.
      @param values Array of values to be sorted.
      These are held without modification or cloning.
  */
  public IndexSorter(double[] values) {
    _dvalues = values;
    _length = values.length;
  }

  /** The array of values that determine the sort.
      @param values Array of values to be sorted.
      These are held without modification or cloning.
  */
  public IndexSorter(float[] values) {
    _fvalues = values;
    _length = values.length;
  }

  /** Get an array of indices such that
      values[index[i]] >= values[index[j]] if i >= j;
      @return indices that address original array of values
      in increasing order.
  */
  public int[] getSortedIndices() {
    MyComparable[] c = new MyComparable[_length];
    for (int i=0; i< c.length; ++i) {c[i] = new MyComparable(i);}

    Arrays.sort(c);
    int[] result = new int[c.length];
    for (int i=0; i< result.length; ++i) {
      result[i] = c[i].index;
    }
    return result;
  }

  /** For sorting with Arrays.sort */
  private class MyComparable implements Comparable {
    /**  */
    public int index = 0;

    /** Constructor.
       @param index
     */
    public MyComparable(int index) {this.index = index;}

    public int compareTo(Object o) {
      MyComparable other = (MyComparable) o;
      if (_dvalues != null)
        return s_almost.cmp(_dvalues[index], _dvalues[other.index]);
      return s_almost.cmp(_fvalues[index], _fvalues[other.index]);
    }
  }
}

