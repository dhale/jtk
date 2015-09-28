/****************************************************************************
Copyright 2007, Colorado School of Mines and others.
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
