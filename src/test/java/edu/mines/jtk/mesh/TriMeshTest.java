/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mesh;

import java.io.*;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import edu.mines.jtk.util.Stopwatch;

/**
 * Tests {@link edu.mines.jtk.mesh.TriMesh}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2003.08.26, 2006.08.02
 */
public class TriMeshTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(TriMeshTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void testNabors() {
    TriMesh tm = new TriMesh();
    TriMesh.Node n0 = new TriMesh.Node(1.0f,0.0f);
    TriMesh.Node n1 = new TriMesh.Node(0.0f,1.0f);
    TriMesh.Node n2 = new TriMesh.Node(0.0f,0.0f);
    TriMesh.Node n3 = new TriMesh.Node(1.1f,1.1f);
    tm.addNode(n0);
    tm.addNode(n1);
    tm.addNode(n2);
    tm.addNode(n3);
    assertEquals(2,tm.getTriNabors(n0).length);
    assertEquals(2,tm.getTriNabors(n1).length);
    assertEquals(1,tm.getTriNabors(n2).length);
    assertEquals(1,tm.getTriNabors(n3).length);
    assertEquals(2,tm.getTriNabors(tm.findEdge(n0,n1)).length);
    assertEquals(1,tm.getTriNabors(tm.findEdge(n0,n2)).length);
    assertEquals(1,tm.getTriNabors(tm.findEdge(n1,n2)).length);
    assertEquals(1,tm.getTriNabors(tm.findEdge(n0,n3)).length);
    assertEquals(1,tm.getTriNabors(tm.findEdge(n1,n3)).length);
    assertEquals(3,tm.getEdgeNabors(n0).length);
    assertEquals(3,tm.getEdgeNabors(n1).length);
    assertEquals(2,tm.getEdgeNabors(n2).length);
    assertEquals(2,tm.getEdgeNabors(n3).length);
    assertEquals(3,tm.getNodeNabors(n0).length);
    assertEquals(3,tm.getNodeNabors(n1).length);
    assertEquals(2,tm.getNodeNabors(n2).length);
    assertEquals(2,tm.getNodeNabors(n3).length);
  }

  public void testIO() throws IOException,ClassNotFoundException {

    // Make tri mesh.
    TriMesh.Node n00 = new TriMesh.Node(0.0f,0.0f);
    TriMesh.Node n01 = new TriMesh.Node(0.0f,1.0f);
    TriMesh.Node n10 = new TriMesh.Node(1.0f,0.0f);
    TriMesh.Node n11 = new TriMesh.Node(1.0f,1.0f);
    TriMesh tm = new TriMesh();
    tm.addNode(n00);
    tm.addNode(n01);
    tm.addNode(n10);
    tm.addNode(n11);
    int nnode = tm.countNodes();
    int ntri = tm.countTris();
    assertEquals(4,nnode);
    TriMesh.NodePropertyMap map = tm.getNodePropertyMap("foo");
    map.put(n00,0);
    map.put(n01,1);
    map.put(n10,2);
    map.put(n11,3);

    // Write and read it.
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(baos);
    oos.writeObject(tm);
    baos.close();
    ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
    ObjectInputStream ois = new ObjectInputStream(bais);
    tm = (TriMesh)ois.readObject();
    bais.close();
    assertEquals(nnode,tm.countNodes());
    assertEquals(ntri,tm.countTris());

    // Check property values.
    String name = tm.getNodePropertyMapNames()[0];
    assertEquals("foo",name);
    map = tm.getNodePropertyMap(name);
    n00 = tm.findNodeNearest(0.0f,0.0f);
    n01 = tm.findNodeNearest(0.0f,1.0f);
    n10 = tm.findNodeNearest(1.0f,0.0f);
    n11 = tm.findNodeNearest(1.0f,1.0f);
    assertEquals(0,((Integer)map.get(n00)).intValue());
    assertEquals(1,((Integer)map.get(n01)).intValue());
    assertEquals(2,((Integer)map.get(n10)).intValue());
    assertEquals(3,((Integer)map.get(n11)).intValue());
  }

  public void testTriListener() {
    TriMesh tm = new TriMesh();
    tm.addNode(new TriMesh.Node(0.0f,0.0f));
    tm.addNode(new TriMesh.Node(1.0f,0.0f));
    tm.addNode(new TriMesh.Node(0.0f,1.0f));
    TriListener tl = new TriListener();
    tm.addTriListener(tl);
    TriMesh.Node node = new TriMesh.Node(0.1f,0.1f);
    tm.addNode(node);
    assertEquals(3,tl.countAdded());
    assertEquals(1,tl.countRemoved());
    tm.removeNode(node);
    assertEquals(4,tl.countAdded());
    assertEquals(4,tl.countRemoved());
  }
  private static class TriListener implements TriMesh.TriListener {
    public void triAdded(TriMesh mesh, TriMesh.Tri tri) {
      ++_nadded;
    }
    public void triRemoved(TriMesh mesh, TriMesh.Tri tri) {
      ++_nremoved;
    }
    public int countAdded() {
      return _nadded;
    }
    public int countRemoved() {
      return _nremoved;
    }
    private int _nadded;
    private int _nremoved;
  }

  public void testSimple() {
    TriMesh tm = new TriMesh();
    TriMesh.Node n0 = new TriMesh.Node(0.0f,0.0f);
    TriMesh.Node n1 = new TriMesh.Node(1.0f,0.0f);
    TriMesh.Node n2 = new TriMesh.Node(0.0f,1.0f);
    TriMesh.Node n3 = new TriMesh.Node(0.3f,0.3f);
    tm.addNode(n0);
    tm.addNode(n1);
    tm.addNode(n2);
    tm.addNode(n3);
    tm.removeNode(n3);
    tm.validate();
  }

  public void testSquare() {
    TriMesh tm = new TriMesh();
    TriMesh.Node n0 = new TriMesh.Node(0.0f,0.0f);
    TriMesh.Node n1 = new TriMesh.Node(1.0f,0.0f);
    TriMesh.Node n2 = new TriMesh.Node(0.0f,1.0f);
    TriMesh.Node n3 = new TriMesh.Node(1.0f,1.0f);
    tm.addNode(n0);
    tm.addNode(n1);
    tm.addNode(n2);
    tm.addNode(n3);
    tm.removeNode(n3);
    tm.removeNode(n2);
    tm.removeNode(n1);
    tm.removeNode(n0);
    tm.validate();
  }

  public void testAddFindRemove() {
    java.util.Random random = new java.util.Random();
    TriMesh tm = new TriMesh();
    //int nadd = 0;
    //int nremove = 0;
    for (int niter=0; niter<1000; ++niter) {
      float x = random.nextFloat();
      float y = random.nextFloat();
      if (tm.countNodes()<10 || random.nextFloat()>0.5f) {
        TriMesh.Node node = new TriMesh.Node(x,y);
        boolean ok = tm.addNode(node);
        assertTrue(ok);
        tm.validate();
        //++nadd;
      } else if (tm.countNodes()>0) {
        TriMesh.Node node = tm.findNodeNearest(x,y);
        assertTrue(node!=null);
        TriMesh.Node nodeSlow = tm.findNodeNearestSlow(x,y);
        assertTrue(node==nodeSlow);
        tm.removeNode(node);
        tm.validate();
        //++nremove;
      }
    }
    //System.out.println("Nodes added/removed = "+nadd+"/"+nremove);
  }

  public void benchAddNode() {
    java.util.Random random = new java.util.Random();
    for (int itest=0; itest<3; ++itest) {
      for (int nnode=1000; nnode<=64000; nnode*=2) {
        Stopwatch sw = new Stopwatch();
        sw.restart();
        TriMesh tm = new TriMesh();
        for (int inode=0; inode<nnode; ++inode) {
          float x = random.nextFloat();
          float y = random.nextFloat();
          TriMesh.Node node = new TriMesh.Node(x,y);
          tm.addNode(node);
        }
        sw.stop();
        System.out.println(
          "Added "+nnode+" nodes to make "+tm.countTris() +
          " tris in "+sw.time()+" seconds.");
        tm.validate();
      }
      try {
        System.out.println("Sleeping");
        Thread.sleep(5000,0);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public void benchFind() {
    int nnode = 1000;
    int nfind = 10000;
    int ntest = 3;
    java.util.Random random = new java.util.Random();
    Stopwatch sw = new Stopwatch();
    TriMesh tm = new TriMesh();
    for (int inode=0; inode<nnode; ++inode) {
      float x = random.nextFloat();
      float y = random.nextFloat();
      TriMesh.Node node = new TriMesh.Node(x,y);
      tm.addNode(node);
    }
    for (int itest=0; itest<ntest; ++itest) {
      float[] x = new float[nfind];
      float[] y = new float[nfind];
      for (int ifind=0; ifind<nfind; ++ifind) {
        x[ifind] = random.nextFloat();
        y[ifind] = random.nextFloat();
      }
      TriMesh.Node[] nfast = new TriMesh.Node[nfind];
      sw.reset();
      sw.start();
      for (int ifind=0; ifind<nfind; ++ifind)
        nfast[ifind] = tm.findNodeNearest(x[ifind],y[ifind]);
      sw.stop();
      int sfast = (int)(nfind/sw.time());
      TriMesh.Node[] nslow = new TriMesh.Node[nfind];
      sw.reset();
      sw.start();
      for (int ifind=0; ifind<nfind; ++ifind)
        nslow[ifind] = tm.findNodeNearestSlow(x[ifind],y[ifind]);
      sw.stop();
      int sslow = (int)(nfind/sw.time());
      for (int ifind=0; ifind<nfind; ++ifind) {
        if (nfast[ifind]!=nslow[ifind]) {
          float xfast = nfast[ifind].x();
          float yfast = nfast[ifind].y();
          float xslow = nslow[ifind].x();
          float yslow = nslow[ifind].y();
          float dxfast = xfast-x[ifind];
          float dyfast = yfast-y[ifind];
          float dxslow = xslow-x[ifind];
          float dyslow = yslow-y[ifind];
          float dsfast = dxfast*dxfast+dyfast*dyfast;
          float dsslow = dxslow*dxslow+dyslow*dyslow;
          System.out.println("ifind="+ifind+" fast/slow="+dsfast+"/"+dsslow);
        }
        assertTrue(nfast[ifind]==nslow[ifind]);
      }
      System.out.println("Find fast/slow nodes per sec = "+sfast+"/"+sslow);
    }
  }
}
