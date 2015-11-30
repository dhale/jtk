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
