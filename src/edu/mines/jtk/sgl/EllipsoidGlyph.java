/****************************************************************************
Copyright (c) 2009, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import java.nio.FloatBuffer;

import edu.mines.jtk.util.*;
import static edu.mines.jtk.ogl.Gl.*;
import static edu.mines.jtk.util.ArrayMath.*;

/**
 * An ellipsoid glyph represented by a small number of triangles.
 * @author Dave Hale and Chris Engelsma, Colorado School of Mines
 * @version 2009.08.10
 */
public class EllipsoidGlyph extends Node implements Selectable {

  /**
   * Constructs an ellipsoid glpyh for a specified metric tensor.
   * The metric tensor is a 3x3 matrix A such that p'Ap = 1 for all points
   * p on the ellipsoid, where p' denotes the transpose of a column vector 
   * p that contains the coordinates (x,y,z), minus the specified center.
   * <p>
   * Let the eigen-decomposition of the symmetric positive-definite matrix 
   * A be defined by A = VDV', where V' is the transpose of a matrix V of 
   * orthonormal eigenvectors and D is a diagonal matrix of corresponding 
   * positive eigenvalues.
   * @param xc x coordinate of center of ellipsoid.
   * @param yc y coordinate of center of ellipsoid.
   * @param zc z coordinate of center of ellipsoid.
   * @param m the approximation quality. The number of triangles used to
   *  approximate the ellipsoid is proportional to 4^m.
   * @param d array of three eigenvalues; must be positive.
   * @param v array of three orthonormal unit eigenvectors. There are
   *  three eigenvectors v[0], v[1] and v[2], each with three components.
   *  Eigenvector v[k] corresponds to eigenvalue d[k], for k = 0, 1, 2.
   */
  public EllipsoidGlyph(
    float xc, float yc, float zc, int m, float[] d, float[][] v) 
  {
    Check.argument(d[0]>0.0f,"d[0] is positive");
    Check.argument(d[1]>0.0f,"d[1] is positive");
    Check.argument(d[2]>0.0f,"d[2] is positive");

    // Elements of specified matrices D and V.
    float dx = d[0], dy = d[1], dz = d[2];
    float vxx = v[0][0], vxy = v[1][0], vxz = v[2][0];
    float vyx = v[0][1], vyy = v[1][1], vyz = v[2][1];
    float vzx = v[0][2], vzy = v[1][2], vzz = v[2][2];

    // Ensure that the eigenvectors in V form a right-handed coordinate 
    // system. This is necessary because of the ambiguity in the sign
    // of V in the eigen-decomposition A = VDV' = (-V)D(-V)'. We want
    // multiplication by V to be a pure rotation, to not include any 
    // reflection about the origin, so that triangle vertices will be 
    // in counter-clockwise order as viewed from outside the ellipsoid.
    float txz = vyx*vzy-vzx*vyy;
    float tyz = vzx*vxy-vxx*vzy;
    float tzz = vxx*vyy-vxy*vyx;
    if (txz*vxz+tyz*vyz+tzz*vzz<0.0) {
      vxx = -vxx; vxy = -vxy; vxz = -vxz;
      vyx = -vyx; vyy = -vyy; vyz = -vyz;
      vzx = -vzx; vzy = -vzy; vzz = -vzz;
    }

    // Six unique elements of the symmetric positive-definite matrix A = VDV'.
    float[] a = {
      vxx*dx*vxx+vxy*dy*vxy+vxz*dz*vxz, // axx
      vxx*dx*vyx+vxy*dy*vyy+vxz*dz*vyz, // axy = ayx
      vxx*dx*vzx+vxy*dy*vzy+vxz*dz*vzz, // axz = azx
      vyx*dx*vyx+vyy*dy*vyy+vyz*dz*vyz, // ayy
      vyx*dx*vzx+vyy*dy*vzy+vyz*dz*vzz, // ayz = azy
      vzx*dx*vzx+vzy*dy*vzy+vzz*dz*vzz, // azz
    };

    // Initial points p are vertices of an octahedron such that p'Ap = 1.
    // The six points are (pxx,pyx,pzx), (pyx,pyy,pyz), (pzx,pzy,pzz)
    // and the three reflections of those three points about the origin.
    float sx = 1.0f/sqrt(dx);
    float sy = 1.0f/sqrt(dy);
    float sz = 1.0f/sqrt(dz);
    float pxx = sx*vxx, pxy = sy*vxy, pxz = sz*vxz;
    float pyx = sx*vyx, pyy = sy*vyy, pyz = sz*vyz;
    float pzx = sx*vzx, pzy = sy*vzy, pzz = sz*vzz;

    // Arrays for vertices and unit normal vectors. The initial octahedron
    // has 8 triangular faces, each with 3 vertices with 3 coordinates. Each
    // subdivision increases the number of triangles by a factor of 4.
    int n = 8*3*3;
    for (int i=0; i<m; ++i)
      n *= 4;
    float[] xyz = new float[n];
    float[] uvw = new float[n];

    // Compute vertices and unit normal vectors for the ellipsoid by 
    // recursively subdividing the eight triangular faces of the 
    // octahedron. The order of the three vertices in each triangle is 
    // counter-clockwise as viewed from outside the ellipsoid.
    n = 0;
    n = addTri(a, pxx, pyx, pzx, pxy, pyy, pzy, pxz, pyz, pzz,m,n,xyz,uvw);
    n = addTri(a,-pxx,-pyx,-pzx, pxz, pyz, pzz, pxy, pyy, pzy,m,n,xyz,uvw);
    n = addTri(a, pxx, pyx, pzx,-pxz,-pyz,-pzz, pxy, pyy, pzy,m,n,xyz,uvw);
    n = addTri(a,-pxx,-pyx,-pzx,-pxy,-pyy,-pzy, pxz, pyz, pzz,m,n,xyz,uvw);
    n = addTri(a, pxx, pyx, pzx, pxz, pyz, pzz,-pxy,-pyy,-pzy,m,n,xyz,uvw);
    n = addTri(a,-pxx,-pyx,-pzx, pxy, pyy, pzy,-pxz,-pyz,-pzz,m,n,xyz,uvw);
    n = addTri(a, pxx, pyx, pzx,-pxy,-pyy,-pzy,-pxz,-pyz,-pzz,m,n,xyz,uvw);
    n = addTri(a,-pxx,-pyx,-pzx,-pxz,-pyz,-pzz,-pxy,-pyy,-pzy,m,n,xyz,uvw);

    // Shift all vertices (x,y,z) to center the ellipsoid at (xc,yc,zc).
    for (int i=0; i<n; i+=3) {
      xyz[i  ] += xc;
      xyz[i+1] += yc;
      xyz[i+2] += zc;
    }

    // Bounding sphere.
    double radius0 = 1.0/sqrt(d[0]);
    double radius1 = 1.0/sqrt(d[1]);
    double radius2 = 1.0/sqrt(d[2]);
    double radius = max(radius0,radius1,radius2);
    _bs = new BoundingSphere(xc,yc,zc,radius);

    // Float buffers for vertices and normals.
    _nt = xyz.length/9;
    _vb = Direct.newFloatBuffer(xyz.length);
    _nb = Direct.newFloatBuffer(uvw.length);
    _vb.put(xyz);
    _nb.put(uvw);
    _vb.rewind();
    _nb.rewind();

    // Remember ellipsoid parameters.
    _xc = xc;
    _yc = yc;
    _zc = zc;
    _d = copy(d);
    _v = copy(v);
  }

  public void pick(PickContext pc) {
    Segment ps = pc.getPickSegment();
    for (int it=0,jt=0; it<_nt; ++it) {
      double xi = _vb.get(jt++);
      double yi = _vb.get(jt++);
      double zi = _vb.get(jt++);
      double xj = _vb.get(jt++);
      double yj = _vb.get(jt++);
      double zj = _vb.get(jt++);
      double xk = _vb.get(jt++);
      double yk = _vb.get(jt++);
      double zk = _vb.get(jt++);
      Point3 p = ps.intersectWithTriangle(xi,yi,zi,xj,yj,zj,xk,yk,zk);
      if (p!=null)
        pc.addResult(p);
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // protected

  protected void selectedChanged() {
    dirtyDraw();
  }

  protected BoundingSphere computeBoundingSphere(boolean finite) {
    return _bs;
  }

  protected void draw(DrawContext dc) {
    boolean selected = isSelected();
    glEnableClientState(GL_VERTEX_ARRAY);
    glVertexPointer(3,GL_FLOAT,0,_vb);
    glEnableClientState(GL_NORMAL_ARRAY);
    glNormalPointer(GL_FLOAT,0,_nb);
    if (selected) {
      glEnable(GL_POLYGON_OFFSET_FILL);
      glPolygonOffset(1.0f,1.0f);
    }
    glDrawArrays(GL_TRIANGLES,0,3*_nt);
    glDisableClientState(GL_NORMAL_ARRAY);
    if (selected) {
      glPolygonMode(GL_FRONT_AND_BACK,GL_LINE);
      glDisable(GL_LIGHTING);
      glColor3d(1.0,1.0,1.0);
      glDrawArrays(GL_TRIANGLES,0,3*_nt);
    }
    glDisableClientState(GL_VERTEX_ARRAY);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private float _xc,_yc,_zc; // center
  private float[] _d; // eigenvalues
  private float[][] _v; // eigenvectors
  private BoundingSphere _bs; // pre-computed bounding sphere
  private int _nt; // number of triangles
  private FloatBuffer _vb; // vertex buffer
  private FloatBuffer _nb; // normal buffer

  private void init(
    float xc, float yc, float zc, int m, float[] d, float[][] v,
    float[] xyz, float[] uvw) 
  {
  }

  private static int addTri(
    float[] a,
    float xa, float ya, float za,
    float xb, float yb, float zb,
    float xc, float yc, float zc,
    int m, int n, float[] xyz, float[] uvw)
  {
    float axx = a[0], axy = a[1], axz = a[2],
                      ayy = a[3], ayz = a[4],
                                  azz = a[5];

    // If no longer subdividing, ...
    if (m==0) {

      // Append the coordinates of the vertices a, b, c of triangle abc.
      int k = n;
      xyz[k++] = xa; xyz[k++] = ya; xyz[k++] = za;
      xyz[k++] = xb; xyz[k++] = yb; xyz[k++] = zb;
      xyz[k++] = xc; xyz[k++] = yc; xyz[k++] = zc;

      // Compute and append unit normal vectors for vertices a, b, c.
      float ua = axx*xa+axy*ya+axz*za;
      float va = axy*xa+ayy*ya+ayz*za;
      float wa = axz*xa+ayz*ya+azz*za;
      float ub = axx*xb+axy*yb+axz*zb;
      float vb = axy*xb+ayy*yb+ayz*zb;
      float wb = axz*xb+ayz*yb+azz*zb;
      float uc = axx*xc+axy*yc+axz*zc;
      float vc = axy*xc+ayy*yc+ayz*zc;
      float wc = axz*xc+ayz*yc+azz*zc;
      float da = sqrt(ua*ua+va*va+wa*wa);
      float db = sqrt(ub*ub+vb*vb+wb*wb);
      float dc = sqrt(uc*uc+vc*vc+wc*wc);
      float sa = 1.0f/da;
      float sb = 1.0f/db;
      float sc = 1.0f/dc;
      uvw[n++] = ua*sa; uvw[n++] = va*sa; uvw[n++] = wa*sa;
      uvw[n++] = ub*sb; uvw[n++] = vb*sb; uvw[n++] = wb*sb;
      uvw[n++] = uc*sc; uvw[n++] = vc*sc; uvw[n++] = wc*sc;
    } 

    // Else, if subdividing, ...
    else {

      // New vertices at midpoints ab, bc, and ca of triangle edges.
      float xab = 0.5f*(xa+xb), yab = 0.5f*(ya+yb), zab = 0.5f*(za+zb);
      float xbc = 0.5f*(xb+xc), ybc = 0.5f*(yb+yc), zbc = 0.5f*(zb+zc);
      float xca = 0.5f*(xc+xa), yca = 0.5f*(yc+ya), zca = 0.5f*(zc+za);

      // Metric distances from new vertices to origin.
      float dab = sqrt(xab*(axx*xab+axy*yab+axz*zab) +
                       yab*(axy*xab+ayy*yab+ayz*zab) +
                       zab*(axz*xab+ayz*yab+azz*zab));
      float dbc = sqrt(xbc*(axx*xbc+axy*ybc+axz*zbc) +
                       ybc*(axy*xbc+ayy*ybc+ayz*zbc) +
                       zbc*(axz*xbc+ayz*ybc+azz*zbc));
      float dca = sqrt(xca*(axx*xca+axy*yca+axz*zca) +
                       yca*(axy*xca+ayy*yca+ayz*zca) +
                       zca*(axz*xca+ayz*yca+azz*zca));

      // Scale new vertices to put them on the ellipsoid.
      float sab = 1.0f/dab;
      float sbc = 1.0f/dbc;
      float sca = 1.0f/dca;
      xab *= sab; yab *= sab; zab *= sab;
      xbc *= sbc; ybc *= sbc; zbc *= sbc;
      xca *= sca; yca *= sca; zca *= sca;

      // Recursively subdivide triangle abc into four triangles.
      m -= 1;
      n = addTri(a, xa, ya, za,xab,yab,zab,xca,yca,zca,m,n,xyz,uvw);
      n = addTri(a, xb, yb, zb,xbc,ybc,zbc,xab,yab,zab,m,n,xyz,uvw);
      n = addTri(a, xc, yc, zc,xca,yca,zca,xbc,ybc,zbc,m,n,xyz,uvw);
      n = addTri(a,xab,yab,zab,xbc,ybc,zbc,xca,yca,zca,m,n,xyz,uvw);
    }

    return n;
  }
}
