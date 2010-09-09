/****************************************************************************
Copyright (c) 2007, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mesh;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests {@link edu.mines.jtk.mesh.TriSurf}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2003.08.26, 2007.01.12
 */
public class TriSurfTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(TriSurfTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void testCube() {
    TriSurf.Node n000 = new TriSurf.Node(0.0f,0.0f,0.0f);
    TriSurf.Node n001 = new TriSurf.Node(0.0f,0.0f,1.0f);
    TriSurf.Node n010 = new TriSurf.Node(0.0f,1.0f,0.0f);
    TriSurf.Node n011 = new TriSurf.Node(0.0f,1.0f,1.0f);
    TriSurf.Node n100 = new TriSurf.Node(1.0f,0.0f,0.0f);
    TriSurf.Node n101 = new TriSurf.Node(1.0f,0.0f,1.0f);
    TriSurf.Node n110 = new TriSurf.Node(1.0f,1.0f,0.0f);
    TriSurf.Node n111 = new TriSurf.Node(1.0f,1.0f,1.0f);

    TriSurf ts = new TriSurf();
    ts.addNode(n000);
    ts.addNode(n001);
    ts.addNode(n010);
    ts.addNode(n011);
    assertEquals(2,ts.countFaces());
    ts.addNode(n100);
    assertEquals(6,ts.countFaces());
    ts.addNode(n101);
    ts.addNode(n110);
    ts.addNode(n111);
    assertEquals(12,ts.countFaces());

    TriSurf.Node[] nodes = {n000,n001,n010,n011,n100,n101,n110,n111};
    ts.removeNodes(nodes);
    ts.addNodes(nodes);
    assertEquals(12,ts.countFaces());
  }
}
