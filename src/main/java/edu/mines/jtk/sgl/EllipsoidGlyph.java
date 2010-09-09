/****************************************************************************
Copyright (c) 2009, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import java.nio.FloatBuffer;

import edu.mines.jtk.ogl.*;
import edu.mines.jtk.util.*;
import static edu.mines.jtk.ogl.Gl.*;
import static edu.mines.jtk.util.ArrayMath.*;

/**
 * A glyph for fast rendering of ellipsoids (including spheres).
 * This class is especially useful in any node that must render a large 
 * number of ellipsoids. Such a node would first construct a single 
 * ellipsoid glyph, and then call one of the glyph's draw methods for 
 * each ellipsoid to be rendered. 
 * <p>
 * Internally, each ellipsoid is drawn by transforming an approximation to 
 * a unit sphere (with radius one) that has been precomputed and stored in 
 * an OpenGL display list.
 * <p>
 * The unit sphere is approximated by recursively subdividing the triangular 
 * faces of an octahedron. The quality of the approximation increases with 
 * the number of subdivisions, and the number of triangles increases by a 
 * factor of four with each subdivision.
 * 
 * @author Dave Hale, Colorado School of Mines
 * @version 2009.08.12
 */
public class EllipsoidGlyph {

  /**
   * Constructs an ellipsoid glyph with default quality of four subdivisions.
   */
  public EllipsoidGlyph() {
    this(4);
  }

  /**
   * Constructs an ellipsoid glyph with specified quality.
   * @param m the quality = the number of subdivisions.
   */
  public EllipsoidGlyph(int m) {
    makeTransformMatrix();
    makeUnitSphere(m);
  }

  /**
   * Returns the number of vertices used to approximate this glyph.
   * @return the number of vertices.
   */
  public int countVertices() {
    return _nv;
  }

  /**
   * Gets the vertices of the unit sphere used to approximate this glyph.
   * @return array of packed (x,y,z) coordinates of vertices; by reference,
   *  not by copy. The array length is 3 times the number of vertices.
   */
  public float[] getVertices() {
    return _xyz;
  }

  /**
   * Draws a unit sphere centered at the origin.
   */
  public void draw() {
    if (_displayList==null) {
      FloatBuffer xyz = Direct.newFloatBuffer(3*_nv);
      xyz.put(_xyz); xyz.rewind();
      _displayList = new GlDisplayList();
      glEnableClientState(GL_VERTEX_ARRAY);
      glEnableClientState(GL_NORMAL_ARRAY);
      glNewList(_displayList.list(),GL_COMPILE);
      glVertexPointer(3,GL_FLOAT,0,xyz);
      glNormalPointer(GL_FLOAT,0,xyz);
      glDrawArrays(GL_TRIANGLES,0,_nv);
      glEndList();
      glDisableClientState(GL_NORMAL_ARRAY);
      glDisableClientState(GL_VERTEX_ARRAY);
    }
    glCallList(_displayList.list());
  }

  /**
   * Draws a sphere centered at a specified point with specified radius.
   * @param cx x coordinate of the center point.
   * @param cy y coordinate of the center point.
   * @param cz z coordinate of the center point.
   * @param r radius of the sphere.
   */
  public void draw(float cx, float cy, float cz, float r) {
    draw(cx,cy,cz,r,r,r);
  }

  /**
   * Draws an axis-aligned ellipsoid centered at a specified point.
   * The lengths of the specified semi-principal axes must be positive.
   * @param cx x coordinate of the center point.
   * @param cy y coordinate of the center point.
   * @param cz z coordinate of the center point.
   * @param dx semi-principal length in direction of x axis.
   * @param dy semi-principal length in direction of y axis.
   * @param dz semi-principal length in direction of z axis.
   */
  public void draw(
    float cx, float cy, float cz,
    float dx, float dy, float dz)
  {
    glPushMatrix();
    glTranslatef(cx,cy,cz);
    glScalef(dx,dy,dz);
    draw();
    glPopMatrix();
  }

  /**
   * Draws an arbitrary ellipsoid centered at a specified point.
   * The semi-principal axes of the ellipsoid are represented by three 
   * vectors u, v, and w. The lengths of these three vectors are the 
   * semi-principal lengths of the ellipsoid, and must be non-zero.
   * @param cx x coordinate of the center point.
   * @param cy y coordinate of the center point.
   * @param cz z coordinate of the center point.
   * @param ux x component of vector u.
   * @param uy y component of vector u.
   * @param uz z component of vector u.
   * @param vx x component of vector v.
   * @param vy y component of vector v.
   * @param vz z component of vector v.
   * @param wx x component of vector w.
   * @param wy y component of vector w.
   * @param wz z component of vector w.
   */
  public void draw(
    float cx, float cy, float cz,
    float ux, float uy, float uz,
    float vx, float vy, float vz,
    float wx, float wy, float wz)
  {
    // Ensure vectors u, v, and w form a right-handed coordinate system.
    // This is necessary to keep triangle vertices in counter-clockwise
    // order as viewed from outside the ellipsoid.
    if (ux*(vy*wz-vz*wy)+uy*(vz*wx-vx*wz)+uz*(vx*wy-vy*wx)<0.0) {
      ux = -ux; uy = -uy; uz = -uz;
      vx = -vx; vy = -vy; vz = -vz;
      wx = -wx; wy = -wy; wz = -wz;
    }

    // The transformation matrix.
    _m[ 0] = ux; _m[ 4] = vx; _m[ 8] = wx; _m[12] = cx;
    _m[ 1] = uy; _m[ 5] = vy; _m[ 9] = wy; _m[13] = cy;
    _m[ 2] = uz; _m[ 6] = vz; _m[10] = wz; _m[14] = cz;

    // Draw the transformed unit sphere.
    glPushMatrix();
    glMultMatrixf(_m,0);
    draw();
    glPopMatrix();
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private float[] _m; // transform matrix used when drawing
  private int _nv; // number of vertices for unit sphere
  private float[] _xyz; // vertices on the unit sphere
  private GlDisplayList _displayList; // draws unit sphere when called

  private void makeTransformMatrix() {
    _m = new float[16];
    _m[15] = 1.0f;
  }

  private void makeUnitSphere(int m) {

    // Buffers for vertices of triangles used to approximate the unit sphere. 
    // The initial octahedron has 8 triangular faces, each with 3 vertices. 
    // Each subdivision increases the number of triangles by a factor of 4.
    _nv = 8*3;
    for (int i=0; i<m; ++i)
      _nv *= 4;
    int n = _nv*3;
    _xyz = new float[n];

    // Compute vertices and unit normal vectors for the ellipsoid by 
    // recursively subdividing the eight triangular faces of the 
    // octahedron. The order of the three vertices in each triangle is 
    // counter-clockwise as viewed from outside the ellipsoid.
    float xm = -1.0f, x0 = 0.0f, xp = 1.0f;
    float ym = -1.0f, y0 = 0.0f, yp = 1.0f;
    float zm = -1.0f, z0 = 0.0f, zp = 1.0f;
    n = 0;
    n = addTri(xp,y0,z0,x0,yp,z0,x0,y0,zp,m,n);
    n = addTri(xm,y0,z0,x0,y0,zp,x0,yp,z0,m,n);
    n = addTri(xp,y0,z0,x0,y0,zp,x0,ym,z0,m,n);
    n = addTri(xm,y0,z0,x0,ym,z0,x0,y0,zp,m,n);
    n = addTri(xp,y0,z0,x0,y0,zm,x0,yp,z0,m,n);
    n = addTri(xm,y0,z0,x0,yp,z0,x0,y0,zm,m,n);
    n = addTri(xp,y0,z0,x0,ym,z0,x0,y0,zm,m,n);
    n = addTri(xm,y0,z0,x0,y0,zm,x0,ym,z0,m,n);
  }
  private int addTri(
    float xa, float ya, float za,
    float xb, float yb, float zb,
    float xc, float yc, float zc,
    int m, int n)
  {
    // If no longer subdividing, ...
    if (m==0) {

      // Append coordinates of vertices a, b, c of triangle abc.
      _xyz[n++] = xa; _xyz[n++] = ya; _xyz[n++] = za;
      _xyz[n++] = xb; _xyz[n++] = yb; _xyz[n++] = zb;
      _xyz[n++] = xc; _xyz[n++] = yc; _xyz[n++] = zc;
    } 

    // Else, if subdividing, ...
    else {

      // New vertices at midpoints ab, bc, and ca of triangle edges.
      float xab = 0.5f*(xa+xb), yab = 0.5f*(ya+yb), zab = 0.5f*(za+zb);
      float xbc = 0.5f*(xb+xc), ybc = 0.5f*(yb+yc), zbc = 0.5f*(zb+zc);
      float xca = 0.5f*(xc+xa), yca = 0.5f*(yc+ya), zca = 0.5f*(zc+za);

      // Distances from new vertices to origin.
      float dab = sqrt(xab*xab+yab*yab+zab*zab);
      float dbc = sqrt(xbc*xbc+ybc*ybc+zbc*zbc);
      float dca = sqrt(xca*xca+yca*yca+zca*zca);

      // Scale new vertices to put them on the sphere.
      float sab = 1.0f/dab;
      float sbc = 1.0f/dbc;
      float sca = 1.0f/dca;
      xab *= sab; yab *= sab; zab *= sab;
      xbc *= sbc; ybc *= sbc; zbc *= sbc;
      xca *= sca; yca *= sca; zca *= sca;

      // Recursively subdivide triangle abc into four triangles.
      m -= 1;
      n = addTri( xa, ya, za,xab,yab,zab,xca,yca,zca,m,n);
      n = addTri( xb, yb, zb,xbc,ybc,zbc,xab,yab,zab,m,n);
      n = addTri( xc, yc, zc,xca,yca,zca,xbc,ybc,zbc,m,n);
      n = addTri(xab,yab,zab,xbc,ybc,zbc,xca,yca,zca,m,n);
    }

    return n;
  }
}
