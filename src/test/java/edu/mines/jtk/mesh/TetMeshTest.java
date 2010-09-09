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
 * Tests {@link edu.mines.jtk.mesh.TetMesh}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2003.08.26, 2006.08.02
 */
public class TetMeshTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(TetMeshTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void testNabors() {
    TetMesh tm = new TetMesh();
    TetMesh.Node n0 = new TetMesh.Node(1.0f,0.0f,0.0f);
    TetMesh.Node n1 = new TetMesh.Node(0.0f,1.0f,0.0f);
    TetMesh.Node n2 = new TetMesh.Node(0.0f,0.0f,1.0f);
    TetMesh.Node n3 = new TetMesh.Node(0.0f,0.0f,0.0f);
    TetMesh.Node n4 = new TetMesh.Node(1.1f,1.1f,1.1f);
    tm.addNode(n0);
    tm.addNode(n1);
    tm.addNode(n2);
    tm.addNode(n3);
    tm.addNode(n4);
    assertEquals(2,tm.getTetNabors(n0).length);
    assertEquals(2,tm.getTetNabors(n1).length);
    assertEquals(2,tm.getTetNabors(n2).length);
    assertEquals(1,tm.getTetNabors(n3).length);
    assertEquals(1,tm.getTetNabors(n4).length);
    assertEquals(2,tm.getTetNabors(tm.findEdge(n0,n1)).length);
    assertEquals(2,tm.getTetNabors(tm.findEdge(n1,n2)).length);
    assertEquals(2,tm.getTetNabors(tm.findEdge(n2,n0)).length);
    assertEquals(1,tm.getTetNabors(tm.findEdge(n0,n3)).length);
    assertEquals(1,tm.getTetNabors(tm.findEdge(n0,n4)).length);
    assertEquals(1,tm.getTetNabors(tm.findEdge(n1,n3)).length);
    assertEquals(1,tm.getTetNabors(tm.findEdge(n1,n4)).length);
    assertEquals(1,tm.getTetNabors(tm.findEdge(n2,n3)).length);
    assertEquals(1,tm.getTetNabors(tm.findEdge(n2,n4)).length);
    assertEquals(2,tm.getTetNabors(tm.findFace(n0,n1,n2)).length);
    assertEquals(1,tm.getTetNabors(tm.findFace(n0,n2,n3)).length);
    assertEquals(1,tm.getTetNabors(tm.findFace(n1,n3,n2)).length);
    assertEquals(1,tm.getTetNabors(tm.findFace(n0,n3,n1)).length);
    assertEquals(1,tm.getTetNabors(tm.findFace(n0,n4,n2)).length);
    assertEquals(1,tm.getTetNabors(tm.findFace(n0,n1,n4)).length);
    assertEquals(1,tm.getTetNabors(tm.findFace(n1,n2,n4)).length);
    assertEquals(3,tm.getFaceNabors(tm.findEdge(n0,n1)).length);
    assertEquals(3,tm.getFaceNabors(tm.findEdge(n1,n2)).length);
    assertEquals(3,tm.getFaceNabors(tm.findEdge(n2,n0)).length);
    assertEquals(2,tm.getFaceNabors(tm.findEdge(n0,n3)).length);
    assertEquals(2,tm.getFaceNabors(tm.findEdge(n0,n4)).length);
    assertEquals(2,tm.getFaceNabors(tm.findEdge(n1,n3)).length);
    assertEquals(2,tm.getFaceNabors(tm.findEdge(n1,n4)).length);
    assertEquals(2,tm.getFaceNabors(tm.findEdge(n2,n3)).length);
    assertEquals(2,tm.getFaceNabors(tm.findEdge(n2,n4)).length);
    assertEquals(4,tm.getEdgeNabors(n0).length);
    assertEquals(4,tm.getEdgeNabors(n1).length);
    assertEquals(4,tm.getEdgeNabors(n2).length);
    assertEquals(3,tm.getEdgeNabors(n3).length);
    assertEquals(3,tm.getEdgeNabors(n4).length);
    assertEquals(4,tm.getNodeNabors(n0).length);
    assertEquals(4,tm.getNodeNabors(n1).length);
    assertEquals(4,tm.getNodeNabors(n2).length);
    assertEquals(3,tm.getNodeNabors(n3).length);
    assertEquals(3,tm.getNodeNabors(n4).length);
  }

  public void testIO() throws IOException,ClassNotFoundException {

    // Make tet mesh.
    TetMesh.Node n000 = new TetMesh.Node(0.0f,0.0f,0.0f);
    TetMesh.Node n001 = new TetMesh.Node(0.0f,0.0f,1.0f);
    TetMesh.Node n010 = new TetMesh.Node(0.0f,1.0f,0.0f);
    TetMesh.Node n011 = new TetMesh.Node(0.0f,1.0f,1.0f);
    TetMesh.Node n100 = new TetMesh.Node(1.0f,0.0f,0.0f);
    TetMesh.Node n101 = new TetMesh.Node(1.0f,0.0f,1.0f);
    TetMesh.Node n110 = new TetMesh.Node(1.0f,1.0f,0.0f);
    TetMesh.Node n111 = new TetMesh.Node(1.0f,1.0f,1.0f);
    TetMesh tm = new TetMesh();
    tm.addNode(n000);
    tm.addNode(n001);
    tm.addNode(n010);
    tm.addNode(n011);
    tm.addNode(n100);
    tm.addNode(n101);
    tm.addNode(n110);
    tm.addNode(n111);
    int nnode = tm.countNodes();
    int ntet = tm.countTets();
    assertEquals(8,nnode);
    TetMesh.NodePropertyMap map = tm.getNodePropertyMap("foo");
    map.put(n000,0);
    map.put(n001,1);
    map.put(n010,2);
    map.put(n011,3);
    map.put(n100,4);
    map.put(n101,5);
    map.put(n110,6);
    map.put(n111,7);

    // Write and read it.
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ObjectOutputStream oos = new ObjectOutputStream(baos);
    oos.writeObject(tm);
    baos.close();
    ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
    ObjectInputStream ois = new ObjectInputStream(bais);
    tm = (TetMesh)ois.readObject();
    bais.close();
    assertEquals(nnode,tm.countNodes());
    assertEquals(ntet,tm.countTets());

    // Check property values.
    String name = tm.getNodePropertyMapNames()[0];
    assertEquals("foo",name);
    map = tm.getNodePropertyMap(name);
    n000 = tm.findNodeNearest(0.0f,0.0f,0.0f);
    n001 = tm.findNodeNearest(0.0f,0.0f,1.0f);
    n010 = tm.findNodeNearest(0.0f,1.0f,0.0f);
    n011 = tm.findNodeNearest(0.0f,1.0f,1.0f);
    n100 = tm.findNodeNearest(1.0f,0.0f,0.0f);
    n101 = tm.findNodeNearest(1.0f,0.0f,1.0f);
    n110 = tm.findNodeNearest(1.0f,1.0f,0.0f);
    n111 = tm.findNodeNearest(1.0f,1.0f,1.0f);
    assertEquals(0,((Integer)map.get(n000)).intValue());
    assertEquals(1,((Integer)map.get(n001)).intValue());
    assertEquals(2,((Integer)map.get(n010)).intValue());
    assertEquals(3,((Integer)map.get(n011)).intValue());
    assertEquals(4,((Integer)map.get(n100)).intValue());
    assertEquals(5,((Integer)map.get(n101)).intValue());
    assertEquals(6,((Integer)map.get(n110)).intValue());
    assertEquals(7,((Integer)map.get(n111)).intValue());
  }

  public void testTetListener() {
    TetMesh tm = new TetMesh();
    tm.addNode(new TetMesh.Node(0.0f,0.0f,0.0f));
    tm.addNode(new TetMesh.Node(1.0f,0.0f,0.0f));
    tm.addNode(new TetMesh.Node(0.0f,1.0f,0.0f));
    tm.addNode(new TetMesh.Node(0.0f,0.0f,1.0f));
    TetListener tl = new TetListener();
    tm.addTetListener(tl);
    TetMesh.Node node = new TetMesh.Node(0.1f,0.1f,0.1f);
    tm.addNode(node);
    assertEquals(4,tl.countAdded());
    assertEquals(1,tl.countRemoved());
    tm.removeNode(node);
    assertEquals(5,tl.countAdded());
    assertEquals(5,tl.countRemoved());
  }
  private static class TetListener implements TetMesh.TetListener {
    public void tetAdded(TetMesh mesh, TetMesh.Tet tet) {
      ++_nadded;
    }
    public void tetRemoved(TetMesh mesh, TetMesh.Tet tet) {
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

  public void testFinds() {
    TetMesh tm = new TetMesh();
    TetMesh.Node n0 = new TetMesh.Node(0.0f,0.0f,0.0f);
    TetMesh.Node n1 = new TetMesh.Node(1.0f,0.0f,0.0f);
    TetMesh.Node n2 = new TetMesh.Node(0.0f,1.0f,0.0f);
    TetMesh.Node n3 = new TetMesh.Node(0.0f,0.0f,1.0f);
    TetMesh.Node n4 = new TetMesh.Node(9.0f,9.0f,9.0f);
    tm.addNode(n0);
    tm.addNode(n1);
    tm.addNode(n2);
    tm.addNode(n3);
    tm.addNode(n4);

    TetMesh.Tet tet = tm.findTet(n0);
    assertTrue(tet!=null);
    assertTrue(tet==tm.findTet(n0,n1));
    assertTrue(tet==tm.findTet(n0,n2));
    assertTrue(tet==tm.findTet(n0,n3));
    assertTrue(tet==tm.findTet(n0,n1,n3));
    assertTrue(tet==tm.findTet(n0,n1,n3));
    assertTrue(tet==tm.findTet(n0,n2,n3));
    assertTrue(tet==tm.findTet(n0,n1,n2,n3));
    assertTrue(null==tm.findTet(n0,n4));
    assertTrue(null==tm.findTet(n0,n1,n4));
    assertTrue(null==tm.findTet(n0,n2,n4));
    assertTrue(null==tm.findTet(n0,n3,n4));
    assertTrue(null==tm.findTet(n0,n1,n2,n4));
    assertTrue(null==tm.findTet(n0,n1,n3,n4));
    assertTrue(null==tm.findTet(n0,n2,n3,n4));

    TetMesh.Edge edge;
    edge = tm.findEdge(n0,n1);
    TetMesh.Edge e01 = new TetMesh.Edge(n0,n1,tet);
    TetMesh.Edge e10 = new TetMesh.Edge(n1,n0,tet);
    assertTrue(edge.equals(e01) || edge.equals(e10));
    edge = tm.findEdge(n0,n2);
    TetMesh.Edge e02 = new TetMesh.Edge(n0,n2,tet);
    TetMesh.Edge e20 = new TetMesh.Edge(n2,n0,tet);
    assertTrue(edge.equals(e02) || edge.equals(e20));
    edge = tm.findEdge(n0,n3);
    TetMesh.Edge e03 = new TetMesh.Edge(n0,n3,tet);
    TetMesh.Edge e30 = new TetMesh.Edge(n3,n0,tet);
    assertTrue(edge.equals(e03) || edge.equals(e30));
    edge = tm.findEdge(n1,n2);
    TetMesh.Edge e12 = new TetMesh.Edge(n1,n2,tet);
    TetMesh.Edge e21 = new TetMesh.Edge(n2,n1,tet);
    assertTrue(edge.equals(e12) || edge.equals(e21));
    edge = tm.findEdge(n1,n3);
    TetMesh.Edge e13 = new TetMesh.Edge(n1,n3,tet);
    TetMesh.Edge e31 = new TetMesh.Edge(n3,n1,tet);
    assertTrue(edge.equals(e13) || edge.equals(e31));
    edge = tm.findEdge(n2,n3);
    TetMesh.Edge e23 = new TetMesh.Edge(n2,n3,tet);
    TetMesh.Edge e32 = new TetMesh.Edge(n3,n2,tet);
    assertTrue(edge.equals(e23) || edge.equals(e32));

    TetMesh.Face face;
    face = tm.findFace(n0,n1,n2);
    TetMesh.Face f102 = new TetMesh.Face(n1,n0,n2,tet);
    assertTrue(face.equals(f102));
    face = tm.findFace(n0,n1,n3);
    TetMesh.Face f301 = new TetMesh.Face(n3,n0,n1,tet);
    assertTrue(face.equals(f301));
    face = tm.findFace(n0,n2,n3);
    TetMesh.Face f320 = new TetMesh.Face(n3,n2,n0,tet);
    assertTrue(face.equals(f320));
    assertTrue(null==tm.findFace(n0,n1,n4));
    assertTrue(null==tm.findFace(n0,n2,n4));
    assertTrue(null==tm.findFace(n0,n3,n4));
  }

  public void testSimple() {
    TetMesh tm = new TetMesh();
    TetMesh.Node n0 = new TetMesh.Node(1.0f,0.0f,0.0f);
    TetMesh.Node n1 = new TetMesh.Node(0.0f,1.0f,0.0f);
    TetMesh.Node n2 = new TetMesh.Node(0.0f,0.0f,1.0f);
    TetMesh.Node n3 = new TetMesh.Node(0.0f,0.0f,0.0f);
    TetMesh.Node n4 = new TetMesh.Node(0.9f,0.9f,0.9f);
    tm.addNode(n0);
    tm.addNode(n1);
    tm.addNode(n2);
    tm.addNode(n3);
    tm.addNode(n4);
    tm.removeNode(n4);
    tm.validate();
  }

  public void testLine() {
    TetMesh tm = new TetMesh();
    int n = 100;
    for (int i=0; i<n; ++i) {
      float x = (float)i;
      float y = 3.14f;
      float z = 4.13f;
      TetMesh.Node node = new TetMesh.Node(x,y,z);
      tm.addNode(node);
    }
    tm.validate();
    for (int i=0; i<n; ++i) {
      float x = (float)i;
      float y = 3.14f;
      float z = 4.13f;
      TetMesh.Node node = tm.findNodeNearest(x,y,z);
      tm.removeNode(node);
    }
    tm.validate();
  }

  public void testCube() {
    TetMesh tm = new TetMesh();
    TetMesh.Node n0 = new TetMesh.Node(0.0f,0.0f,0.0f);
    TetMesh.Node n1 = new TetMesh.Node(1.0f,0.0f,0.0f);
    TetMesh.Node n2 = new TetMesh.Node(0.0f,1.0f,0.0f);
    TetMesh.Node n3 = new TetMesh.Node(0.0f,0.0f,1.0f);
    TetMesh.Node n4 = new TetMesh.Node(1.0f,1.0f,0.0f);
    TetMesh.Node n5 = new TetMesh.Node(1.0f,0.0f,1.0f);
    TetMesh.Node n6 = new TetMesh.Node(0.0f,1.0f,1.0f);
    TetMesh.Node n7 = new TetMesh.Node(1.0f,1.0f,1.0f);
    tm.addNode(n0);
    tm.addNode(n1);
    tm.addNode(n2);
    tm.addNode(n3);
    tm.addNode(n4);
    tm.addNode(n5);
    tm.addNode(n6);
    tm.addNode(n7);
    tm.removeNode(n7);
    tm.removeNode(n6);
    tm.removeNode(n5);
    tm.removeNode(n4);
    tm.removeNode(n3);
    tm.removeNode(n2);
    tm.removeNode(n1);
    tm.removeNode(n0);
    tm.validate();
  }

  public void testAddFindRemove() {
    java.util.Random random = new java.util.Random();
    TetMesh tm = new TetMesh();
    //int nadd = 0;
    //int nremove = 0;
    for (int niter=0; niter<1000; ++niter) {
      float x = random.nextFloat();
      float y = random.nextFloat();
      float z = random.nextFloat();
      if (tm.countNodes()<10 || random.nextFloat()>0.5f) {
        TetMesh.Node node = new TetMesh.Node(x,y,z);
        boolean ok = tm.addNode(node);
        assertTrue(ok);
        tm.validate();
        //++nadd;
      } else if (tm.countNodes()>0) {
        TetMesh.Node node = tm.findNodeNearest(x,y,z);
        assertTrue(node!=null);
        TetMesh.Node nodeSlow = tm.findNodeNearestSlow(x,y,z);
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
    for (int itest=0; itest<16; ++itest) {
      for (int nnode=1000; nnode<=64000; nnode*=2) {
        Stopwatch sw = new Stopwatch();
        sw.reset();
        sw.start();
        TetMesh tm = new TetMesh();
        for (int inode=0; inode<nnode; ++inode) {
          float x = random.nextFloat();
          float y = random.nextFloat();
          float z = random.nextFloat();
          TetMesh.Node node = new TetMesh.Node(x,y,z);
          tm.addNode(node);
          /*
          if (inode%10000==0)
            System.out.println("Added "+inode+" nodes in "+sw.time()+" sec.");
          */
        }
        sw.stop();
        System.out.println(
          "Added "+nnode+" nodes to make "+tm.countTets() +
          " tets in "+sw.time()+" seconds.");
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
    TetMesh tm = new TetMesh();
    for (int inode=0; inode<nnode; ++inode) {
      float x = random.nextFloat();
      float y = random.nextFloat();
      float z = random.nextFloat();
      TetMesh.Node node = new TetMesh.Node(x,y,z);
      tm.addNode(node);
    }
    for (int itest=0; itest<ntest; ++itest) {
      float[] x = new float[nfind];
      float[] y = new float[nfind];
      float[] z = new float[nfind];
      for (int ifind=0; ifind<nfind; ++ifind) {
        x[ifind] = random.nextFloat();
        y[ifind] = random.nextFloat();
        z[ifind] = random.nextFloat();
      }
      TetMesh.Node[] nfast = new TetMesh.Node[nfind];
      sw.reset();
      sw.start();
      for (int ifind=0; ifind<nfind; ++ifind)
        nfast[ifind] = tm.findNodeNearest(x[ifind],y[ifind],z[ifind]);
      sw.stop();
      int sfast = (int)(nfind/sw.time());
      TetMesh.Node[] nslow = new TetMesh.Node[nfind];
      sw.reset();
      sw.start();
      for (int ifind=0; ifind<nfind; ++ifind)
        nslow[ifind] = tm.findNodeNearestSlow(x[ifind],y[ifind],z[ifind]);
      sw.stop();
      int sslow = (int)(nfind/sw.time());
      for (int ifind=0; ifind<nfind; ++ifind) {
        if (nfast[ifind]!=nslow[ifind]) {
          float xfast = nfast[ifind].x();
          float yfast = nfast[ifind].y();
          float zfast = nfast[ifind].z();
          float xslow = nslow[ifind].x();
          float yslow = nslow[ifind].y();
          float zslow = nslow[ifind].z();
          float dxfast = xfast-x[ifind];
          float dyfast = yfast-y[ifind];
          float dzfast = zfast-z[ifind];
          float dxslow = xslow-x[ifind];
          float dyslow = yslow-y[ifind];
          float dzslow = zslow-z[ifind];
          float dsfast = dxfast*dxfast+dyfast*dyfast+dzfast*dzfast;
          float dsslow = dxslow*dxslow+dyslow*dyslow+dzslow*dzslow;
          System.out.println("ifind="+ifind+" fast/slow="+dsfast+"/"+dsslow);
        }
        assertTrue(nfast[ifind]==nslow[ifind]);
      }
      System.out.println("Find fast/slow nodes per sec = "+sfast+"/"+sslow);
    }
  }
}
