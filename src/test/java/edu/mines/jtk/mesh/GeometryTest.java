/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mesh;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import static edu.mines.jtk.util.MathPlus.FLT_EPSILON;
import static edu.mines.jtk.util.MathPlus.FLT_PI;
import edu.mines.jtk.util.Stopwatch;

/**
 * Tests {@link edu.mines.jtk.mesh.Geometry}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2000.04.28, 2006.08.02
 */
public class GeometryTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(GeometryTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void testInCircle() {
  
    // Four co-circular points. We will perturb the point pd.
    float xxxx = 3.14159f;
    float yyyy = 1.00e15f;
    float pa[] = {xxxx,0.0f};
    float pb[] = {0.0f,yyyy};
    float pc[] = {0.0f,0.0f};
    float pd[] = {xxxx,yyyy};
    double ra,rf;

    trace("");

    // on
    ra = Geometry.inCircle(pa,pb,pc,pd);
    rf = Geometry.inCircleFast(pa,pb,pc,pd);
    trace("0 inCircle:     "+String.format("%26.18e",ra));
    trace("0 inCircleFast: "+String.format("%26.18e",rf));
    assertTrue(ra==0.0);

    // inside
    pd[X] = xxxx*(1.0f-FLT_EPSILON);
    ra = Geometry.inCircle(pa,pb,pc,pd);
    rf = Geometry.inCircleFast(pa,pb,pc,pd);
    trace("+ inCircle:     "+String.format("%26.18e",ra));
    trace("+ inCircleFast: "+String.format("%26.18e",rf));
    assertTrue(ra>0.0);

    // outside
    pd[X] = xxxx*(1.0f+FLT_EPSILON);
    ra = Geometry.inCircle(pa,pb,pc,pd);
    rf = Geometry.inCircleFast(pa,pb,pc,pd);
    trace("- inCircle:     "+String.format("%26.18e",ra));
    trace("- inCircleFast: "+String.format("%26.18e",rf));
    assertTrue(ra<0.0);
  }

  public void testInSphere() {
  
    // Five co-spherical points. We will perturb the point pe.
    float xxxx = 1.0f;
    float yyyy = FLT_PI;
    float zzzz = 1.0e6f;
    float pa[] = {xxxx,0.0f,0.0f};
    float pb[] = {0.0f,yyyy,0.0f};
    float pc[] = {0.0f,0.0f,zzzz};
    float pd[] = {0.0f,0.0f,0.0f};
    float pe[] = {xxxx,yyyy,zzzz};
    double ra,rf;

    trace("");

    // on
    ra = Geometry.inSphere(pa,pb,pc,pd,pe);
    rf = Geometry.inSphereFast(pa,pb,pc,pd,pe);
    trace("0 inSphere:     "+String.format("%26.18e",ra));
    trace("0 inSphereFast: "+String.format("%26.18e",rf));
    assertTrue(ra==0.0);

    // inside
    pe[X] = xxxx*(1.0f-FLT_EPSILON);
    ra = Geometry.inSphere(pa,pb,pc,pd,pe);
    rf = Geometry.inSphereFast(pa,pb,pc,pd,pe);
    trace("+ inSphere:     "+String.format("%26.18e",ra));
    trace("+ inSphereFast: "+String.format("%26.18e",rf));
    assertTrue(ra>0.0);

    // outside
    pe[X] = xxxx*(1.0f+FLT_EPSILON);
    ra = Geometry.inSphere(pa,pb,pc,pd,pe);
    rf = Geometry.inSphereFast(pa,pb,pc,pd,pe);
    trace("- inSphere:     "+String.format("%26.18e",ra));
    trace("- inSphereFast: "+String.format("%26.18e",rf));
    assertTrue(ra<0.0);
  }

  public void testLeftOfLine() {
  
    // Three co-linear points. We will perturb the point pc.
    float xxxx = 2.0f;
    float yyyy = 1.0f;
    float aaaa = 1.0e15f;
    float pa[] = {1.0f*xxxx,1.0f*yyyy};
    float pb[] = {2.0f*xxxx,2.0f*yyyy};
    float pc[] = {aaaa*xxxx,aaaa*yyyy};
    double ra,rf;

    trace("");

    // on
    ra = Geometry.leftOfLine(pa,pb,pc);
    rf = Geometry.leftOfLineFast(pa,pb,pc);
    trace("0 leftOfLine:     "+String.format("%26.18e",ra));
    trace("0 leftOfLineFast: "+String.format("%26.18e",rf));
    assertTrue(ra==0.0);

    // left
    pc[X] = aaaa*xxxx*(1.0f-FLT_EPSILON);
    ra = Geometry.leftOfLine(pa,pb,pc);
    rf = Geometry.leftOfLineFast(pa,pb,pc);
    trace("+ leftOfLine:     "+String.format("%26.18e",ra));
    trace("+ leftOfLineFast: "+String.format("%26.18e",rf));
    assertTrue(ra>0.0);

    // right
    pc[X] = aaaa*xxxx*(1.0f+FLT_EPSILON);
    ra = Geometry.leftOfLine(pa,pb,pc);
    rf = Geometry.leftOfLineFast(pa,pb,pc);
    trace("- leftOfLine:     "+String.format("%26.18e",ra));
    trace("- leftOfLineFast: "+String.format("%26.18e",rf));
    assertTrue(ra<0.0);
  }

  public void testLeftOfPlane() {
  
    // Four co-planar points. We will perturb the point pd.
    float xxxx = 1.0f;
    float yyyy = 1.0f;
    float zzzz = 1.0e15f;
    float pa[] = {xxxx,0.0f,0.1f};
    float pb[] = {0.0f,yyyy,3.3f};
    float pc[] = {0.0f,yyyy,6.7f};
    float pd[] = {xxxx,0.0f,zzzz};
    double ra,rf;

    trace("");

    // on
    ra = Geometry.leftOfPlane(pa,pb,pc,pd);
    rf = Geometry.leftOfPlaneFast(pa,pb,pc,pd);
    trace("0 leftOfPlane:     "+String.format("%26.18e",ra));
    trace("0 leftOfPlaneFast: "+String.format("%26.18e",rf));
    assertTrue(ra==0.0);

    // left
    pd[X] = xxxx*(1.0f-FLT_EPSILON);
    ra = Geometry.leftOfPlane(pa,pb,pc,pd);
    rf = Geometry.leftOfPlaneFast(pa,pb,pc,pd);
    trace("+ leftOfPlane:     "+String.format("%26.18e",ra));
    trace("+ leftOfPlaneFast: "+String.format("%26.18e",rf));
    assertTrue(ra>0.0);

    // right
    pd[X] = xxxx*(1.0f+FLT_EPSILON);
    ra = Geometry.leftOfPlane(pa,pb,pc,pd);
    rf = Geometry.leftOfPlaneFast(pa,pb,pc,pd);
    trace("- leftOfPlane:     "+String.format("%26.18e",ra));
    trace("- leftOfPlaneFast: "+String.format("%26.18e",rf));
    assertTrue(ra<0.0);
  }

  public void testLeftOfPlaneSpecial() {
    double[] pa = {99.50000003392293,125.85383672388726,4.712236446160304};
    double[] pb = {91.50000003119546,125.85383641401195,4.712236443259705};
    double[] pc = {107.5000000366504,125.85383703376256,4.712236449060903};
    double[] pd = {27.50000030246409,125.8538208916998,122.28777353807229};
    double ra = Geometry.leftOfPlane(pa,pb,pc,pd);
    double rf = Geometry.leftOfPlaneFast(pa,pb,pc,pd);
    assertTrue(ra==0.0);
    assertTrue(rf!=0.0);
  }

  public void testLeftOfPlaneSpecial2() {
    double[] pa = {111.50000056515266,125.85385176546224,4.712249324321081};
    double[] pb = {123.50000062597627,125.85385229716680,4.712249325708733};
    double[] pc = {105.50000053474086,125.85385224976321,4.712249323627476};
    double[] pd = { 93.50000047391725,125.85385171805865,4.712249322239824};
    double ra = Geometry.leftOfPlane(pa,pb,pc,pd);
    double rf = Geometry.leftOfPlaneFast(pa,pb,pc,pd);
    assertTrue(ra==0.0);
    assertTrue(rf!=0.0);
  }

  public void xtestInSphereSpeed() {
    float pa[] = {1.0f,0.0f,0.0f};
    float pb[] = {0.0f,1.0f,0.0f};
    float pc[] = {0.0f,0.0f,1.0f};
    float pd[] = {0.0f,0.0f,0.0f};
    float pe[] = {0.5f,0.5f,0.5f};

    float xa = pa[X],  ya = pa[Y],  za = pa[Z];
    float xb = pb[X],  yb = pb[Y],  zb = pb[Z];
    float xc = pc[X],  yc = pc[Y],  zc = pc[Z];
    float xd = pd[X],  yd = pd[Y],  zd = pd[Z];
    float xe = pe[X],  ye = pe[Y],  ze = pe[Z];
    
    trace("");
    Stopwatch sw = new Stopwatch();
    int nsphere;
    int niter = 100;
    double maxtime = 2.0;

    for(int nloop=0; nloop<5; ++nloop) {

    sw.reset();
    sw.start();
    for (nsphere=0; sw.time()<maxtime; nsphere+=niter) {
      for (int iter=0; iter<niter; ++iter) {
        Geometry.inSphere(xa,ya,za,xb,yb,zb,xc,yc,zc,xd,yd,zd,xe,ye,ze);
        //Geometry.inSphere(pa,pb,pc,pd,pe);
      }
    }
    sw.stop();
    trace("inSphere:     sphere/s = "+(int)(nsphere/sw.time()));

    sw.reset();
    sw.start();
    for (nsphere=0; sw.time()<maxtime; nsphere+=niter) {
      for (int iter=0; iter<niter; ++iter) {
        Geometry.inSphereFast(
          xa,ya,za,xb,yb,zb,xc,yc,zc,xd,yd,zd,xe,ye,ze);
        //rf = Geometry.inSphere(pa,pb,pc,pd,pe);
      }
    }
    sw.stop();
    trace("inSphereFast: sphere/s = "+(int)(nsphere/sw.time()));

    try {
      Thread.sleep(1000,0);
    } catch(InterruptedException e) {
      // do nothing
    }
    }

  }

  public void xtestLeftOfPlaneSpeed() {
    float pa[] = {1.0f,0.0f,0.0f};
    float pb[] = {0.0f,1.0f,0.0f};
    float pc[] = {0.0f,0.0f,1.0f};
    float pd[] = {0.0f,0.0f,0.0f};

    float xa = pa[X],  ya = pa[Y],  za = pa[Z];
    float xb = pb[X],  yb = pb[Y],  zb = pb[Z];
    float xc = pc[X],  yc = pc[Y],  zc = pc[Z];
    float xd = pd[X],  yd = pd[Y],  zd = pd[Z];
    
    trace("");
    Stopwatch sw = new Stopwatch();
    int nplane;
    int niter = 100;
    double maxtime = 2.0;

    sw.reset();
    sw.start();
    for (nplane=0; sw.time()<maxtime; nplane+=niter) {
      for (int iter=0; iter<niter; ++iter) {
        Geometry.leftOfPlane(xa,ya,za,xb,yb,zb,xc,yc,zc,xd,yd,zd);
        //Geometry.leftOfPlane(pa,pb,pc,pd);
      }
    }
    sw.stop();
    trace("leftOfPlane:     plane/s = "+(int)(nplane/sw.time()));

    sw.reset();
    sw.start();
    for (nplane=0; sw.time()<maxtime; nplane+=niter) {
      for (int iter=0; iter<niter; ++iter) {
        Geometry.leftOfPlaneFast(xa,ya,za,xb,yb,zb,xc,yc,zc,xd,yd,zd);
        //Geometry.leftOfPlaneFast(pa,pb,pc,pd);
      }
    }
    sw.stop();
    trace("leftOfPlaneFast: plane/s = "+(int)(nplane/sw.time()));
  }

  public void testCenterCircle3D() {
    double[] po = {0.0,0.0,0.0};
    Geometry.centerCircle3D(0,1,0,
                            0,1,1,
                            0,0,1,
                            po);
    assertTrue(po[0]==0.0);
    assertTrue(po[1]==0.5);
    assertTrue(po[2]==0.5);
    Geometry.centerCircle3D(0,0,1,
                            1,0,1,
                            1,0,0,
                            po);
    assertTrue(po[0]==0.5);
    assertTrue(po[1]==0.0);
    assertTrue(po[2]==0.5);
    Geometry.centerCircle3D(1,0,0,
                            1,1,0,
                            0,1,0,
                            po);
    assertTrue(po[0]==0.5);
    assertTrue(po[1]==0.5);
    assertTrue(po[2]==0.0);

    Geometry.centerCircle3D(1,1,0,
                            1,1,1,
                            1,0,1,
                            po);
    assertTrue(po[0]==1.0);
    assertTrue(po[1]==0.5);
    assertTrue(po[2]==0.5);
    Geometry.centerCircle3D(0,1,1,
                            1,1,1,
                            1,1,0,
                            po);
    assertTrue(po[0]==0.5);
    assertTrue(po[1]==1.0);
    assertTrue(po[2]==0.5);
    Geometry.centerCircle3D(1,0,1,
                            1,1,1,
                            0,1,1,
                            po);
    assertTrue(po[0]==0.5);
    assertTrue(po[1]==0.5);
    assertTrue(po[2]==1.0);
  }


  ///////////////////////////////////////////////////////////////////////////
  // private

  private static final int X = 0;
  private static final int Y = 1;
  private static final int Z = 2;

  private static final boolean TRACE = false;
  private static void trace(String s) {
    if (TRACE) System.out.println(s);
  }
}
