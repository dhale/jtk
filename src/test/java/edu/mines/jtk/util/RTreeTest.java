/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.util;

import java.util.*;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import static edu.mines.jtk.util.MathPlus.abs;
import static edu.mines.jtk.util.MathPlus.sin;

/**
 * Tests {@link edu.mines.jtk.util.RTree}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2003.05.02, 2006.07.13
 */
public class RTreeTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(RTreeTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void testRandom() {
    RTree rt = new RTree(3,4,12);
    STree st = new STree(3);
    int n = 1000;
    for (int i=0; i<n; ++i) {
      RTree.Box box = randomBox(0.2f);
      rt.add(box);
      st.add(box);
      assertEquals(rt.size(),st.size());
    }
    rt.validate();
    while (rt.size()>0) {
      RTree.Box box = randomBox(0.4f);
      Object[] rb = rt.findOverlapping(box);
      Object[] sb = st.findOverlapping(box);
      assertEquals(rb.length,sb.length);
      for (int ib=0; ib<rb.length; ++ib) {
        RTree.Box rbi = (RTree.Box)rb[ib];
        RTree.Box sbi = (RTree.Box)sb[ib];
        rt.remove(rbi);
        st.remove(sbi);
        assertEquals(rt.size(),st.size());
      }
    }
  }

  public void testNearest() {
    RTree rt = new RTree(3,4,12);
    STree st = new STree(3);
    int n = 100;
    for (int i=0; i<n; ++i) {
      RTree.Box box = randomBox(0.2f);
      rt.add(box);
      st.add(box);
    }
    rt.validate();
    int k = 3;
    for (int i=0; i<n; ++i) {
      float[] point = randomPoint();
      Object[] rb = rt.findNearest(k,point);
      Object[] sb = st.findNearest(k,point);
      assertEquals(k,rb.length);
      assertEquals(k,sb.length);
      for (int j=0; j<k; ++j) {
        float rd = ((RTree.Box)rb[j]).getDistanceSquared(point);
        float sd = ((RTree.Box)sb[j]).getDistanceSquared(point);
        assertEquals(sd,rd,0.0f);
      }
    }
  }

  public void testIterator() {
    RTree rt = new RTree(3,4,12);
    int n = 100;
    for (int i=0; i<n; ++i)
      rt.add(randomBox(0.2f));

    Iterator<Object> rti = rt.iterator();
    Object box = rti.next();
    rt.remove(box);
    rt.add(box);
    boolean cmeThrown = false;
    try {
      rti.next();
    } catch (ConcurrentModificationException cme) {
      cmeThrown = true;
    }
    assertTrue(cmeThrown);

    Object[] boxs = rt.toArray();
    int nbox = boxs.length;
    for (int ibox=0; ibox<nbox; ++ibox) {
      boolean removed = rt.remove(boxs[ibox]);
      assertTrue(removed);
    }
  }

  public void xtestTriangle() {
    RTree rt = new RTree(3,6,12);
    STree st = new STree(3);

    // Five surfaces, points on surfaces, and random points.
    int nsurf = 5;
    int nx = 100;
    int ny = 100;
    int nt = nsurf*2*nx*ny;
    int np = nt;
    float radius = 2.0f/(float)nx;
    Triangle[] ts = makeSurfaceTriangles(nsurf,nx,ny);
    //Triangle[] ts = makeRandomTriangles(nt,radius);
    Point[] ps = makeSurfacePoints(nsurf,np);
    Point[] pr = makeRandomPoints(np);
    Point[][] psr = {ps,pr};

    System.out.println();
    Stopwatch sw = new Stopwatch();
    double time = 3.0;
    float[] point = new float[3];

    // RTree and STree.
    boolean pack = true;
    sw.restart();
    if (pack) {
      rt.addPacked(ts);
    } else {
      for (int it=0; it<nt; ++it)
        rt.add(ts[it]);
    }
    sw.stop();
    System.out.println(
      "RTree added "+rt.size()+" triangles at " +
      (int)(rt.size()/sw.time())+" triangle/sec");
    System.out.println(
      "  leaf area="+rt.getLeafArea()+" volume="+rt.getLeafVolume());
    sw.restart();
    for (int it=0; it<nt; ++it)
      st.add(ts[it]);
    sw.stop();
    System.out.println(
      "STree added "+st.size()+" triangles at " +
      (int)(st.size()/sw.time())+" triangle/sec");
    assertEquals(rt.size(),st.size());
    for (int ip=0; ip<100; ++ip) {
      Point p = ps[ip%np];
      point[0] = p.x; point[1] = p.y; point[2] = p.z;
      Object[] rb = rt.findInSphere(point,radius);
      Object[] sb = st.findInSphere(point,radius);
      assertEquals(sb.length,rb.length);
    }
    System.out.println(
      "RTree has "+rt.size()+" objects in "+rt.getLevels()+" levels.");

    // Points on surfaces, then random points.
    for (int isr=0; isr<2; ++isr) {
      Point[] pp = psr[isr];
      if (isr==0) {
        System.out.println("Points on surfaces:");
      } else {
        System.out.println("Random points:");
      }

      // Find triangles nearest to points.
      int nr = 0;
      for (sw.restart(); sw.time()<time;  ++nr) {
        Point p = pp[nr%np];
        point[0] = p.x; point[1] = p.y; point[2] = p.z;
        rt.findNearest(point);
      }
      sw.stop();
      System.out.println("  RTree findNearest/sec = "+(int)(nr/sw.time()));
      int ns = 0;
      for (sw.restart(); sw.time()<time;  ++ns) {
        Point p = pp[nr%np];
        point[0] = p.x; point[1] = p.y; point[2] = p.z;
        st.findNearest(point);
      }
      sw.stop();
      System.out.println("  STree findNearest/sec = "+(int)(ns/sw.time()));

      // Find triangles inside a sphere.
      nr = 0;
      for (sw.restart(); sw.time()<time;  ++nr) {
        Point p = ps[nr%np];
        point[0] = p.x; point[1] = p.y; point[2] = p.z;
        rt.findInSphere(point,radius);
      }
      sw.stop();
      System.out.println("  RTree findInSphere/sec = "+(int)(nr/sw.time()));
      ns = 0;
      for (sw.restart(); sw.time()<time;  ++ns) {
        Point p = ps[nr%np];
        point[0] = p.x; point[1] = p.y; point[2] = p.z;
        st.findInSphere(point,radius);
      }
      sw.stop();
      System.out.println("  STree findInSphere/sec = "+(int)(ns/sw.time()));
    }
  }

  public void xtestPoint() {
    RTree rt = new RTree(3,6,12);
    STree st = new STree(3);
    int n = 100000;
    Point[] ps = new Point[n];
    for (int i=0; i<n; ++i) {
      ps[i] = new Point();
    }
    System.out.println();
    Stopwatch sw = new Stopwatch();
    sw.restart();
    for (int i=0; i<n; ++i)
      rt.add(ps[i]);
    sw.stop();
    System.out.println("RTree add points/sec="+(int)(n/sw.time()));
    sw.restart();
    for (int i=0; i<n; ++i)
      st.add(ps[i]);
    sw.stop();
    float radius = 0.1f;
    System.out.println("STree add points/sec="+(int)(n/sw.time()));
    for (int i=0; i<100; ++i) {
      float[] point = randomPoint();
      Object[] rb = rt.findInSphere(point,radius);
      Object[] sb = st.findInSphere(point,radius);
      assertEquals(sb.length,rb.length);
    }
    double time = 3.0;
    float[] point = new float[3];
    int nr = 0;
    for (sw.restart(); sw.time()<time;  ++nr) {
      Point p = ps[nr%n];
      point[0] = p.x; point[1] = p.y; point[2] = p.z;
      rt.findInSphere(point,radius);
    }
    sw.stop();
    System.out.println("RTree findInSphere/sec = "+(int)(nr/sw.time()));
    int ns = 0;
    for (sw.restart(); sw.time()<time;  ++ns) {
      Point p = ps[nr%n];
      point[0] = p.x; point[1] = p.y; point[2] = p.z;
      st.findInSphere(point,radius);
    }
    sw.stop();
    System.out.println("STree findInSphere/sec = "+(int)(ns/sw.time()));
  }

  ///////////////////////////////////////////////////////////////////////////
  // private


  // A *very* slow but simple substitute for an R-tree.
  private static class STree {
    public STree(int ndim) {
      _ndim = ndim;
      _amin = new float[_ndim];
      _amax = new float[_ndim];
      _bmin = new float[_ndim];
      _bmax = new float[_ndim];
    }
    public boolean add(RTree.Boxed boxed) {
      return _set.add(boxed);
    }
    public boolean remove(RTree.Boxed boxed) {
      return _set.remove(boxed);
    }
    public int size() {
      return _set.size();
    }
    public boolean contains(RTree.Boxed boxed) {
      return _set.contains(boxed);
    }
    public Object[] findOverlapping(RTree.Boxed boxed) {
      boxed.getBounds(_amin,_amax);
      ArrayList<RTree.Boxed> list = new ArrayList<RTree.Boxed>();
      for (RTree.Boxed b:_set) {
        b.getBounds(_bmin,_bmax);
        if (overlapsAB())
          list.add(b);
      }
      return list.toArray();
    }
    public Object findNearest(float[] point) {
      return findNearest(1,point)[0];
    }
    public Object[] findNearest(int n, float[] point) {
      ArrayList<RTree.Boxed> list = new ArrayList<RTree.Boxed>();
      for (int i=0; i<n; ++i) {
        float dmin = Float.MAX_VALUE;
        RTree.Boxed bmin = null;
        for (RTree.Boxed b:_set) {
          if (list.contains(b))
            continue;
          float d = b.getDistanceSquared(point);
          if (d<dmin) {
            dmin = d;
            bmin = b;
          }
        }
        list.add(bmin);
      }
      return list.toArray();
    }
    public Object[] findInSphere(float[] point, float radius) {
      ArrayList<RTree.Boxed> list = new ArrayList<RTree.Boxed>();
      Iterator<RTree.Boxed> i = _set.iterator();
      float s = radius*radius;
      while (i.hasNext()) {
        RTree.Boxed b = i.next();
        if (b.getDistanceSquared(point)<s)
          list.add(b);
      }
      return list.toArray();
    }
    private HashSet<RTree.Boxed> _set = new HashSet<RTree.Boxed>();
    private int _ndim;
    private float[] _amin;
    private float[] _amax;
    private float[] _bmin;
    private float[] _bmax;
    private boolean overlapsAB() {
      for (int idim=0; idim<_ndim; ++idim) {
        if (_amin[idim]>_bmax[idim] || _amax[idim]<_bmin[idim])
          return false;
      }
      return true;
    }
  }

  private static Random _random = new Random();
  static {
    int seed = _random.nextInt();
    //System.out.println("seed="+seed);
    _random.setSeed(seed);
  }

  private RTree.Box randomBox(float size) {
    float xmin = (1.0f-size)*_random.nextFloat();
    float ymin = (1.0f-size)*_random.nextFloat();
    float zmin = (1.0f-size)*_random.nextFloat();
    float xmax = xmin+size*_random.nextFloat();
    float ymax = ymin+size*_random.nextFloat();
    float zmax = zmin+size*_random.nextFloat();
    return new RTree.Box(xmin,ymin,zmin,xmax,ymax,zmax);
  }

  private float[] randomPoint() {
    float x = _random.nextFloat();
    float y = _random.nextFloat();
    float z = _random.nextFloat();
    return new float[]{x,y,z};
  }

  /*
  private Triangle[] makeSphere(int nsd) {

  }
  */

  private static class Point implements RTree.Boxed {
    float x,y,z;
    Point() {
      this.x = _random.nextFloat();
      this.y = _random.nextFloat();
      this.z = _random.nextFloat();
    }
    Point(float x, float y, float z) {
      this.x = x;
      this.y = y;
      this.z = z;
    }
    public void getBounds(float[] min, float[] max) {
      min[0] = x;  min[1] = y;  min[2] = z;
      max[0] = x;  max[1] = y;  max[2] = z;
    }
    public float getDistanceSquared(float[] point) {
      float dx = x-point[0];
      float dy = y-point[1];
      float dz = z-point[2];
      return dx*dx+dy*dy+dz*dz;
    }
  }

  private static class Triangle implements RTree.Boxed {
    float x0,y0,z0;
    float x1,y1,z1;
    float x2,y2,z2;
    Triangle(float size) {
      x0 = (1.0f-size)*_random.nextFloat();
      y0 = (1.0f-size)*_random.nextFloat();
      z0 = (1.0f-size)*_random.nextFloat();
      x1 = x0+size*_random.nextFloat();
      y1 = y0+size*_random.nextFloat();
      z1 = z0+size*_random.nextFloat();
      x2 = x0+size*_random.nextFloat();
      y2 = y0+size*_random.nextFloat();
      z2 = z0+size*_random.nextFloat();
    }
    Triangle(
      float x0, float y0, float z0,
      float x1, float y1, float z1,
      float x2, float y2, float z2)
    {
      this.x0 = x0;  this.y0 = y0;  this.z0 = z0;
      this.x1 = x1;  this.y1 = y1;  this.z1 = z1;
      this.x2 = x2;  this.y2 = y2;  this.z2 = z2;
    }
    public void getBounds(float[] min, float[] max) {
      min[0] = (x0<=x1)?((x0<=x2)?x0:x2):((x1<=x2)?x1:x2);
      min[1] = (y0<=y1)?((y0<=y2)?y0:y2):((y1<=y2)?y1:y2);
      min[2] = (z0<=z1)?((z0<=z2)?z0:z2):((z1<=z2)?z1:z2);
      max[0] = (x0>=x1)?((x0>=x2)?x0:x2):((x1>=x2)?x1:x2);
      max[1] = (y0>=y1)?((y0>=y2)?y0:y2):((y1>=y2)?y1:y2);
      max[2] = (z0>=z1)?((z0>=z2)?z0:z2):((z1>=z2)?z1:z2);
    }
    public float getDistanceSquared(float[] point) {
      float xp = point[0];
      float yp = point[1];
      float zp = point[2];
      float x0p = x0-xp;
      float y0p = y0-yp;
      float z0p = z0-zp;
      float x10 = x1-x0;
      float y10 = y1-y0;
      float z10 = z1-z0;
      float x20 = x2-x0;
      float y20 = y2-y0;
      float z20 = z2-z0;
      float a = x10*x10+y10*y10+z10*z10;
      float b = x10*x20+y10*y20+z10*z20;
      float c = x20*x20+y20*y20+z20*z20;
      float d = x10*x0p+y10*y0p+z10*z0p;
      float e = x20*x0p+y20*y0p+z20*z0p;
      float f = x0p*x0p+y0p*y0p+z0p*z0p;
      float det = abs(a*c-b*b);
      float s = b*e-c*d;
      float t = b*d-a*e;
      float q;
      if (s+t<=det) {
        if (s<0.0f) {
          if (t<0.0f) { // region 4
            if (d<0.0f) {
              if (-d>=a) {
                q = a+2.0f*d+f;
              } else {
                s = -d/a;
                q = d*s+f;
              }
            } else {
              if (e>=0.0f) {
                q = f;
              } else if (-e>=c) {
                q = c+2.0f*e+f;
              } else {
                t = -e/c;
                q = e*t+f;
              }
            }
          } else { // region 3
            if (e>=0.0f) {
              q = f;
            } else if (-e>=c) {
              q = c+2.0f*e+f;
            } else {
              t = -e/c;
              q = e*t+f;
            }
          }
        } else if (t<0.0f) { // region 5
          if (d>=0.0f) {
            q = f;
          } else if (-d>=a) {
            q = a+2.0f*d+f;
          } else {
            s = -d/a;
            q = d*s+f;
          }
        } else { // region 0 (inside triangle)
          float odet = 1.0f/det;
          s *= odet;
          t *= odet;
          q = s*(a*s+b*t+2.0f*d)+t*(b*s+c*t+2.0f*e)+f;
        }
      } else {
        if (s<0.0f) { // region 2
          float bd = b+d;
          float ce = c+e;
          if (ce>bd) {
            float num = ce-bd;
            float den = a-2.0f*b+c;
            if (num>=den) {
              q = a+2.0f*d+f;
            } else {
              s = num/den;
              t = 1.0f-s;
              q = s*(a*s+b*t+2.0f*d)+t*(b*s+c*t+2.0f*e)+f;
            }
          } else {
            if (ce<=0.0f) {
              q = c+2.0f*e+f;
            } else if (e>=0.0f) {
              q = f;
            } else {
              t = -e/c;
              q = e*t+f;
            }
          }
        } else if (t<0.0f) { // region 6
          float be = b+e;
          float ad = a+d;
          if (ad>be) {
            float num = ad-be;
            float den = a-2.0f*b+c;
            if (num>=den) {
              q = c+2.0f*e+f;
            } else {
              t = num/den;
              s = 1.0f-t;
              q = s*(a*s+b*t+2.0f*d)+t*(b*s+c*t+2.0f*e)+f;
            }
          } else {
            if (ad<=0.0f) {
              q = a+2.0f*d+f;
            } else if (d>=0.0f) {
              q = f;
            } else {
              s = -d/a;
              q = d*s+f;
            }
          }
        } else { // region 1
          float num = c+e-b-d;
          if (num<=0.0f) {
            q = c+2.0f*e+f;
          } else {
            float den = a-2.0f*b+c;
            if (num>=den) {
              q = a+2.0f*d+f;
            } else {
              s = num/den;
              t = 1.0f-s;
              q = s*(a*s+b*t+2.0f*d)+t*(b*s+c*t+2.0f*e)+f;
            }
          }
        }
      }
      return q;
    }
  }

  private Triangle[] makeSurfaceTriangles(int nsurf, int nx, int ny) {
    int nt = nsurf*2*nx*ny;
    Triangle[] t = new Triangle[nt];
    float dx = 1.0f/(float)nx;
    float dy = 1.0f/(float)ny;
    for (int isurf=0,it=0; isurf<nsurf; ++isurf) {
      for (int ix=0; ix<nx; ++ix) {
        float x = (float)ix*dx;
        for (int iy=0; iy<ny; ++iy) {
          float y = (float)iy*dy;
          Point pa = pointOnSurface(isurf,x,y);
          Point pb = pointOnSurface(isurf,x+dx,y);
          Point pc = pointOnSurface(isurf,x+dx,y+dy);
          Point pd = pointOnSurface(isurf,x,y+dy);
          t[it++] = new Triangle(pa.x,pa.y,pa.z,pb.x,pb.y,pb.z,pd.x,pd.y,pd.z);
          t[it++] = new Triangle(pb.x,pb.y,pb.z,pc.x,pc.y,pc.z,pd.x,pd.y,pd.z);
        }
      }
    }
    return t;
  }

  private Point[] makeSurfacePoints(int nsurf, int np) {
    Point[] p = new Point[np];
    for (int ip=0; ip<np; ++ip) {
      float x = _random.nextFloat();
      float y = _random.nextFloat();
      int isurf = _random.nextInt(nsurf);
      p[ip] = pointOnSurface(isurf,x,y);
    }
    return p;
  }

  private Point pointOnSurface(int isurf, float x, float y) {
    float z = 0.1f+(float)isurf*0.1f+0.05f*sin(20.0f*x+20.0f*y);
    return new Point(x,y,z);
  }

  /*
  private Triangle[] makeRandomTriangles(int nt, float size) {
    Triangle[] t = new Triangle[nt];
    for (int it=0; it<nt; ++it)
      t[it] = new Triangle(size);
    return t;
  }
  */

  private Point[] makeRandomPoints(int np) {
    Point[] p = new Point[np];
    for (int ip=0; ip<np; ++ip)
      p[ip] = new Point();
    return p;
  }
}
