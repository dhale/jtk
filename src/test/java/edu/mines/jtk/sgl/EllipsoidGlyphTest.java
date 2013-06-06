/****************************************************************************
Copyright (c) 2009, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import java.awt.*;

import edu.mines.jtk.la.*;
import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Tests {@link edu.mines.jtk.sgl.EllipsoidGlyph}.
 * @author Dave Hale
 * @version 2009.08.10
 */
public class EllipsoidGlyphTest {

  public static void main(String[] args) {
    test0();
    test1();
    test2();
  }

  public static void test0() {
    show(new OneEllipsoid(1.0f));
    show(new OneEllipsoid(0.3f));
    show(new OneEllipsoid(0.1f));
  }
  public static void test1() {
    show(new EightEllipsoidsInCube());
  }

  public static void test2() {
    show(new RandomEllipsoidsOnGrid());
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private static void show(Node node) {
    StateSet states = StateSet.forTwoSidedShinySurface(Color.CYAN);
    node.setStates(states);
    World world = new World();
    world.addChild(node);
    SimpleFrame sf = new SimpleFrame(world);
  }

  // One ellipsoid with specified thickness.
  public static class OneEllipsoid extends Node {
    public OneEllipsoid(float thickness) {
      _thickness = thickness;
    }
    public void draw(DrawContext dc) {
      float cx = 0.0f, cy = 0.0f, cz = 0.0f;
      float ux = 0.0f, uy = 0.0f, uz = _thickness;
      float vx = 0.0f, vy = 1.0f, vz = 0.0f;
      float wx = 1.0f, wy = 0.0f, wz = 0.0f;
      _ellipsoid.draw(cx,cy,cz,ux,uy,uz,vx,vy,vz,wx,wy,wz);
    }
    public BoundingSphere computeBoundingSphere(boolean finite) {
      return _bs;
    }
    private float _thickness;
    private EllipsoidGlyph _ellipsoid = new EllipsoidGlyph();
    private BoundingSphere _bs = 
      new BoundingSphere(new BoundingBox(-1,-1,-1,1,1,1));
  }

  // Eight ellipsoids at the corners of a cube.
  public static class EightEllipsoidsInCube extends Node {
    public void draw(DrawContext dc) {
      float cx = 0.5f, cy = 0.5f, cz = 0.5f;
      float ux = 0.5f, uy = 0.0f, uz = 0.0f;
      float vx = 0.0f, vy = 0.1f, vz = 0.0f;
      float wx = 0.0f, wy = 0.0f, wz = 0.5f;
      _ellipsoid.draw(-cx, cy,-cz,ux,uy,uz,vx,vy,vz,wx,wy,wz);
      _ellipsoid.draw( cx, cy,-cz,ux,uy,uz,vx,vy,vz,wx,wy,wz);
      _ellipsoid.draw(-cx,-cy,-cz,ux,uy,uz,vx,vy,vz,wx,wy,wz);
      _ellipsoid.draw( cx,-cy,-cz,ux,uy,uz,vx,vy,vz,wx,wy,wz);
      _ellipsoid.draw(-cx, cy, cz,ux,uy,uz,vx,vy,vz,wx,wy,wz);
      _ellipsoid.draw( cx, cy, cz,ux,uy,uz,vx,vy,vz,wx,wy,wz);
      _ellipsoid.draw(-cx,-cy, cz,ux,uy,uz,vx,vy,vz,wx,wy,wz);
      _ellipsoid.draw( cx,-cy, cz,ux,uy,uz,vx,vy,vz,wx,wy,wz);
    }
    public BoundingSphere computeBoundingSphere(boolean finite) {
      return _bs;
    }
    private EllipsoidGlyph _ellipsoid = new EllipsoidGlyph();
    private BoundingSphere _bs = 
      new BoundingSphere(new BoundingBox(-1,-1,-1,1,1,1));
  }

  // A 2D grid of random ellipsoids.
  public static class RandomEllipsoidsOnGrid extends Node {
    public RandomEllipsoidsOnGrid() {
      int ns = 4;
      int nt = 5;
      _ux = new float[ns][nt];
      _uy = new float[ns][nt];
      _uz = new float[ns][nt];
      _vx = new float[ns][nt];
      _vy = new float[ns][nt];
      _vz = new float[ns][nt];
      _wx = new float[ns][nt];
      _wy = new float[ns][nt];
      _wz = new float[ns][nt];
      _cx = new float[ns][nt];
      _cy = new float[ns][nt];
      _cz = new float[ns][nt];
      float ds = 0.5f/ns;
      float dt = 0.5f/nt;
      float dd = min(ds,dt);
      for (int is=0; is<ns; ++is) {
        for (int it=0; it<nt; ++it) {
          DMatrix matrix = new DMatrix(sub(randdouble(3,3),0.5));
          matrix = matrix.transpose().times(matrix); // random SPD matrix
          DMatrixEvd evd = new DMatrixEvd(matrix); // eigen-decomp
          DMatrix ev = evd.getV(); // three orthogonal unit vectors
          float[] d = mul(dd,randfloat(3)); // random scale factors
          float du = d[0], dv = d[1], dw = d[2];
          if (du<0.01f) du = 0.001f; // make some
          if (dv<0.01f) dv = 0.001f; // ellipsoids
          if (dw<0.01f) dw = 0.001f; // very thin
          _ux[is][it] = (float)ev.get(0,0)*du;
          _uy[is][it] = (float)ev.get(1,0)*du;
          _uz[is][it] = (float)ev.get(2,0)*du*0.5f; // squeeze vertically
          _vx[is][it] = (float)ev.get(0,1)*dv;
          _vy[is][it] = (float)ev.get(1,1)*dv;
          _vz[is][it] = (float)ev.get(2,1)*dv*0.5f; // squeeze vertically
          _wx[is][it] = (float)ev.get(0,2)*dw;
          _wy[is][it] = (float)ev.get(1,2)*dw;
          _wz[is][it] = (float)ev.get(2,2)*dw*0.5f; // squeeze vertically
          _cx[is][it] = (1+2*is)*ds;
          _cy[is][it] = 0.5f; // arbitrarily choose y to be constant
          _cz[is][it] = (1+2*it)*dt;
        }
      }
      _ellipsoid = new EllipsoidGlyph();
      _bs = new BoundingSphere(new BoundingBox(0,0,0,1,1,1));
    }
    public void draw(DrawContext dc) {
      int ns = _cx.length;
      int nt = _cx[0].length;
      for (int is=0; is<ns; ++is) {
        for (int it=0; it<nt; ++it) {
          _ellipsoid.draw(
            _cx[is][it],_cy[is][it],_cz[is][it],
            _ux[is][it],_uy[is][it],_uz[is][it],
            _vx[is][it],_vy[is][it],_vz[is][it],
            _wx[is][it],_wy[is][it],_wz[is][it]);
        }
      }
    }
    public BoundingSphere computeBoundingSphere(boolean finite) {
      return _bs;
    }
    private float[][] _cx,_cy,_cz;
    private float[][] _ux,_uy,_uz;
    private float[][] _vx,_vy,_vz;
    private float[][] _wx,_wy,_wz;
    private EllipsoidGlyph _ellipsoid;
    private BoundingSphere _bs;
  }
}
