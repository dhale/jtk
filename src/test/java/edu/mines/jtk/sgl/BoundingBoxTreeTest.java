/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Tests {@link edu.mines.jtk.sgl.BoundingBoxTree}.
 * @author Dave Hale
 * @version 2006.06.24
 */
public class BoundingBoxTreeTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(BoundingBoxTreeTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void testRandom() {
    int minSize = 10;
    int n = 10000;
    float[] x = randfloat(n);
    float[] y = randfloat(n);
    float[] z = randfloat(n);
    BoundingBoxTree bbt = new BoundingBoxTree(minSize,x,y,z);
    BoundingBoxTree.Node root = bbt.getRoot();
    test(root,minSize,x,y,z);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private void test(BoundingBoxTree.Node node, 
    int minSize, float[] x, float[] y, float[] z) 
  {
    int[] i = node.getIndices();
    int n = i.length;
    assertTrue(minSize<=n);
    BoundingBox bb = node.getBoundingBox();
    for (int j=0; j<n; ++j) {
      int ij = i[j];
      assertTrue(bb.contains(x[ij],y[ij],z[ij]));
    }
    BoundingBoxTree.Node left = node.getLeft();
    BoundingBoxTree.Node right = node.getRight();
    if (left!=null) {
      assertTrue(bb.contains(left.getBoundingBox()));
      assertTrue(bb.contains(right.getBoundingBox()));
      test(left,minSize,x,y,z);
      test(right,minSize,x,y,z);
    } else {
      assertTrue(n/2<minSize);
    }
  }
}
