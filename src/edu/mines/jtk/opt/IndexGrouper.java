package edu.mines.jtk.opt;

import java.util.logging.*;

/** Arrange a sorted set of indices into groups of like elements.
    This works well after IndexSorter or with RecursiveArraySorter.
    Implement the IndexGrouper.Grouper interface to identify
    whether adjacent indices are in the same group or not.
    @author W.S. Harlan, Landmark Graphics
*/
public class IndexGrouper {
  @SuppressWarnings("unused")
private static final Logger LOG
    = Logger.getLogger(IndexGrouper.class.getName());

  private int _nGroups = -1;
  private int[] _first = null;
  private int[] _nElements = null;
  private int[] _group = null;
  private int _maxElements = -1;

  /** Implement this IndexGrouper.Grouper interface to identify
      whether adjacent indices are in the same group or not.
  */
  public static interface Grouper {
    /** Return true if these two interfaces are in the same group or not.
        (Should be transitive: if index1 and index2 are in the same group,
        and if index2 and index3 are in the same group, then
        index1 and index3 are in the same group.)
        @param index1 First index to compare
        @param index2 Second index to compare
        @return True if the two indices are in the same group.
     */
    public boolean inSameGroup(int index1, int index2);
  }

  /** Find adjacent groups in an arbitrary set of indices.
      @param indices Groups will be formed from adjacent
      elements in this array of indices.
      @param grouper Specifies the rule for grouping.
   */
  public IndexGrouper(Grouper grouper, int[] indices) {
    init(grouper, indices);
  }

  /** Assume indices are sequential, starting from 0.
      @param grouper Specifies the rule for grouping.
      @param numberIndices number of indices to group.
   */
  public IndexGrouper(Grouper grouper, int numberIndices) {
    int indices[] = new int[numberIndices];
    for (int i=0; i<numberIndices; ++i) {indices[i] = i;}
    init(grouper, indices);
  }

  /** Find adjacent groups in an arbitrary set of indices.
      @param indices Groups will be formed from adjacent
      elements in this array of indices.
      @param grouper Specifies the rule for grouping.
   */
  protected void init(Grouper grouper, int[] indices) {
    _nGroups = 0;
    _group = new int[indices.length];
    boolean same = false;
    int nElements = 0;
    _maxElements = 0;
    for (int i=0; i<indices.length; ++i) {
      if (same) {
        same = same && grouper.inSameGroup(indices[i], indices[i-1]);
      }
      if (same) {
        ++ nElements;
        if (nElements > _maxElements) _maxElements = nElements;
      } else {
        same = true;
        nElements = 1;
        ++_nGroups;
      }
      _group[i] = _nGroups - 1;
    }
    _first = new int[_nGroups];
    _nElements = new int[_nGroups];
    for (int i=indices.length-1; i>=0; --i) {
      _first[_group[i]] = i;
      _nElements[_group[i]] += 1;
    }
  }

  /** Return the number of identified groups.
      @return The number of groups.
   */
  public int getNumberGroups() {
    return _nGroups;
  }

  /** Identify the first member of a group.
      @param group The group of interest, in the range 0 to
      getNumberGroups()-1.
      @return Returns a element so that indices[element]
      is the first member of the specified group.
   */
  public int getFirstElement(int group) {
    return _first[group];
  }

  /** Identify the number of members in a group.
      @param group The group of interest, in the range 0 to
      getNumberGroups()-1.
      @return The number of consecutive members
      of the specified group.
   */
  public int getNumberElements(int group) {
    return _nElements[group];
  }

  /** What is the size of the largest group?
      @return The number of elements in the largest
      group.
   */

  public int getMaxElements() {
    return _maxElements;
  }

  /** Return the group for an element
      @param element Specifies one the input indices as
      indices[element].  Starts with 0 and must be less
      than the number of input indices.
      @return The group to which this element belongs.
  */
  public int getGroup(int element) {
    return _group[element];
  }

  /** Identify the last member of a group.
      @param group The group of interest, in the range 0 to
      getNumberGroups()-1.
      @return Returns a element so that indices[element]
      is the last member of the specified group.
   */
  public int getLastElement(int group) {
    return getFirstElement(group) + getNumberElements(group) - 1;
  }

  /** test code
 * @param args command line */
  public static void main(String[] args) {
    final double[] array = new double[] {3, 4, 3, 4, 1, 2, 3, 4, 2, 4};
    int[] indices = new IndexSorter(array).getSortedIndices();
    //check sorting
    for (int element=1; element<array.length; ++element) {
      assert array[indices[element]] >= array[indices[element-1]];
    }
    Grouper grouper = new Grouper() {
        public boolean inSameGroup(int index1, int index2) {
          return Almost.FLOAT.equal(array[index1], array[index2]);
        }
      };
    IndexGrouper ig = new IndexGrouper(grouper, indices);
    int nGroups = ig.getNumberGroups();
    assert nGroups == 4;
    assert ig.getMaxElements() == 4;
    for (int element=0; element<array.length; ++element) {
      int index = indices[element];
      assert (array[index]-1) == ig.getGroup(element) :
        "element="+element+" index="+index+" array[index]="+array[index]+
        " ig.getGroup(element)="+ig.getGroup(element);
    }

    for (int group=0; group<nGroups; ++group) {
      assert ig.getNumberElements(group) == group+1;
    }
    assert ig.getFirstElement(0) == 0;
    assert ig.getFirstElement(1) == 1;
    assert ig.getFirstElement(2) == 3;
    assert ig.getFirstElement(3) == 6;
    assert ig.getLastElement(0) == 0;
    assert ig.getLastElement(1) == 2;
    assert ig.getLastElement(2) == 5;
    assert ig.getLastElement(3) == 9;
  }
}

