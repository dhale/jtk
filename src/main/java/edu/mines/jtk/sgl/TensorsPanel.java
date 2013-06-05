/****************************************************************************
Copyright (c) 2009, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import java.awt.Color;

import edu.mines.jtk.dsp.*;
import static edu.mines.jtk.util.ArrayMath.*;

/**
 * An axis-aligned panel that displays a slice of a 3D metric tensor field.
 * The tensors correspond to symmetric positive-definite 3x3 matrices, and
 * are rendered as ellipsoids.
 * @author Chris Engelsma and Dave Hale, Colorado School of Mines.
 * @version 2009.08.29
 */
public class TensorsPanel extends AxisAlignedPanel {

  /**
   * Constructs a tensors panel for the specified tensor field.
   * Assumes default unit samplings.
   * @param et the eigentensors; by reference, not by copy.
   */
  public TensorsPanel(EigenTensors3 et) {
    this(new Sampling(et.getN1()),
         new Sampling(et.getN2()),
         new Sampling(et.getN3()),
         et);
  }

  /**
   * Constructs a tensors panel for the specified tensor field.
   * @param s1 sampling of 1st dimension (Z axis).
   * @param s2 sampling of 2nd dimension (Y axis).
   * @param s3 sampling of 3rd dimension (X axis).
   * @param et the eigentensors; by reference, not by copy.
   */
  public TensorsPanel(
    Sampling s1, Sampling s2, Sampling s3, EigenTensors3 et)
  {
    _sx = s3;
    _sy = s2;
    _sz = s1;
    _et = et;
    _emax = findMaxEigenvalue();
    setStates(StateSet.forTwoSidedShinySurface(Color.CYAN));
  }

  /**
   * Updates the panel. 
   * This method should be called when the tensor field
   * referenced by this tensors panel has been modified.
   */
  public void update() {
    dirtyDraw();
  }

  /**
   * Sets the maximum size of the ellipsoids.
   * As this size is increased, the number of ellipsoids decreases.
   * @param size the maximum ellipsoid size, in samples.
   */
  public void setEllipsoidSize(int size) {
    _ellipsoidSize = size;
    dirtyDraw();
  }

  /////////////////////////////////////////////////////////////////////////////
  // protected

  protected void draw(DrawContext dc) {
    AxisAlignedFrame aaf = this.getFrame();
    if (aaf==null)
      return;
    Axis axis = aaf.getAxis();
    drawEllipsoids(axis);
  }

  /////////////////////////////////////////////////////////////////////////////
  // private

  private Sampling _sx,_sy,_sz;
  private EigenTensors3 _et;
  private float _emax;
  private int _ellipsoidSize = 10;
  private EllipsoidGlyph _eg = new EllipsoidGlyph();

  /**
   * Draws the tensors as ellipsoids.
   */
  private void drawEllipsoids(Axis axis) {

    // Tensor sampling.
    int nx = _sx.getCount();
    int ny = _sy.getCount();
    int nz = _sz.getCount();
    double dx = _sx.getDelta();
    double dy = _sy.getDelta();
    double dz = _sz.getDelta();
    double fx = _sx.getFirst();
    double fy = _sy.getFirst();
    double fz = _sz.getFirst();

    // Min/max (x,y,z) coordinates.
    double xmin = _sx.getFirst();
    double xmax = _sx.getLast();
    double ymin = _sy.getFirst();
    double ymax = _sy.getLast();
    double zmin = _sz.getFirst();
    double zmax = _sz.getLast();

    // Maximum length of eigenvectors u, v and w.
    float dmax = 0.5f*_ellipsoidSize;
    float dxmax = (float)dx*dmax;
    float dymax = (float)dy*dmax;
    float dzmax = (float)dz*dmax;

    // Distance between ellipsoid centers (in samples).
    int kec = (int)(2.0*dmax);

    // Scaling factor for the eigenvectors.
    float scale = dmax/sqrt(_emax);

    // Smallest eigenvalue permitted.
    float etiny = 0.0001f*_emax;

    // Current frame.
    AxisAlignedFrame aaf = this.getFrame();

    if (axis==Axis.X) {

      // Y-Axis.
      int nyc = (int)((ymax-ymin)/(2.0f*dymax));
      double dyc = kec*dy;
      double fyc = 0.5f*((ymax-ymin)-(nyc-1)*dyc);
      int jyc = (int)(fyc/dy);

      // Z-Axis.
      int nzc = (int)((zmax-zmin)/(2.0f*dzmax));
      double dzc = kec*dz;
      double fzc = 0.5f*((zmax-zmin)-(nzc-1)*dzc);
      int jzc = (int)(fzc/dz);

      xmin = aaf.getCornerMin().x;
      xmax = aaf.getCornerMax().x;
      ymin = aaf.getCornerMin().y;
      ymax = aaf.getCornerMax().y;
      zmin = aaf.getCornerMin().z;
      zmax = aaf.getCornerMax().z;

      // X-Axis.
      float xc = 0.5f*(float)(xmax+xmin);
      int ix = _sx.indexOfNearest(xc);

      for (int iy=jyc; iy<ny; iy+=kec) {
        float yc = (float)(fy+iy*dy);
        if (ymin<yc-dymax && yc+dymax<ymax) {
          for (int iz=jzc; iz<nz; iz+=kec) {
            float zc = (float)(fz+iz*dz);
            if (zmin<zc-dzmax && zc+dzmax<zmax) {
              float[] e = _et.getEigenvalues(iz,iy,ix);
              float[] u = _et.getEigenvectorU(iz,iy,ix);
              float[] v = _et.getEigenvectorV(iz,iy,ix);
              float[] w = _et.getEigenvectorW(iz,iy,ix);
              float eu = e[0], ev = e[1], ew = e[2];
              if (eu<=etiny) eu = etiny;
              if (ev<=etiny) ev = etiny;
              if (ew<=etiny) ew = etiny;
              float uz = u[0], uy = u[1], ux = u[2];
              float vz = v[0], vy = v[1], vx = v[2];
              float wz = w[0], wy = w[1], wx = w[2];
              float su = scale*sqrt(eu);
              float sv = scale*sqrt(ev);
              float sw = scale*sqrt(ew);
              ux *= su*dx; uy *= su*dy; uz *= su*dz;
              vx *= sv*dx; vy *= sv*dy; vz *= sv*dz;
              wx *= sw*dx; wy *= sw*dy; wz *= sw*dz;
              _eg.draw(xc,yc,zc,ux,uy,uz,vx,vy,vz,wx,wy,wz);
            }
          }
        }
      }
    } else if (axis==Axis.Y) {

      // X-Axis.
      int nxc = (int)((xmax-xmin)/(2.0f*dxmax));
      double dxc = kec*dx;
      double fxc = 0.5f*((xmax-xmin)-(nxc-1)*dxc);
      int jxc = (int)(fxc/dx);

      // Z-Axis.
      int nzc = (int)((zmax-zmin)/(2.0f*dzmax));
      double dzc = kec*dz;
      double fzc = 0.5f*((zmax-zmin)-(nzc-1)*dzc);
      int jzc = (int)(fzc/dz);

      xmin = aaf.getCornerMin().x;
      xmax = aaf.getCornerMax().x;
      ymin = aaf.getCornerMin().y;
      ymax = aaf.getCornerMax().y;
      zmin = aaf.getCornerMin().z;
      zmax = aaf.getCornerMax().z;

      // Y-Axis.
      float yc = 0.5f*(float)(ymax+ymin);
      int iy = _sy.indexOfNearest(yc);

      for (int ix=jxc; ix<nx; ix+=kec) {
        float xc = (float)(fx+ix*dx);
        if (xmin<xc-dxmax && xc+dxmax<xmax) {
          for (int iz=jzc; iz<nz; iz+=kec) {
            float zc = (float)(fz+iz*dz);
            if (zmin<zc-dzmax && zc+dzmax<zmax) {
              float[] e = _et.getEigenvalues(iz,iy,ix);
              float[] u = _et.getEigenvectorU(iz,iy,ix);
              float[] v = _et.getEigenvectorV(iz,iy,ix);
              float[] w = _et.getEigenvectorW(iz,iy,ix);
              float eu = e[0], ev = e[1], ew = e[2];
              if (eu<=etiny) eu = etiny;
              if (ev<=etiny) ev = etiny;
              if (ew<=etiny) ew = etiny;
              float uz = u[0], uy = u[1], ux = u[2];
              float vz = v[0], vy = v[1], vx = v[2];
              float wz = w[0], wy = w[1], wx = w[2];
              float su = scale*sqrt(eu);
              float sv = scale*sqrt(ev);
              float sw = scale*sqrt(ew);
              ux *= su*dx; uy *= su*dy; uz *= su*dz;
              vx *= sv*dx; vy *= sv*dy; vz *= sv*dz;
              wx *= sw*dx; wy *= sw*dy; wz *= sw*dz;
              _eg.draw(xc,yc,zc,ux,uy,uz,vx,vy,vz,wx,wy,wz);
            }
          }
        }
      }
    } else if (axis==Axis.Z) {

      // X-Axis.
      int nxc = (int)((xmax-xmin)/(2.0f*dxmax));
      double dxc = kec*dx;
      double fxc = 0.5f*((xmax-xmin)-(nxc-1)*dxc);
      int jxc = (int)(fxc/dx);

      // Y-Axis.
      int nyc = (int)((ymax-ymin)/(2.0f*dymax));
      double dyc = kec*dy;
      double fyc = 0.5f*((ymax-ymin)-(nyc-1)*dyc);
      int jyc = (int)(fyc/dy);

      xmin = aaf.getCornerMin().x;
      xmax = aaf.getCornerMax().x;
      ymin = aaf.getCornerMin().y;
      ymax = aaf.getCornerMax().y;
      zmin = aaf.getCornerMin().z;
      zmax = aaf.getCornerMax().z;

      // Z-Axis.
      float zc = 0.5f*(float)(zmax+zmin);
      int iz = _sz.indexOfNearest(zc);

      for (int ix=jxc; ix<nx; ix+=kec) {
        float xc = (float)(fx+ix*dx);
        if (xmin<xc-dxmax && xc+dxmax<xmax) {
          for (int iy=jyc; iy<ny; iy+=kec) {
            float yc = (float)(fy+iy*dy);
            if (ymin<yc-dymax && yc+dymax<ymax) {
              float[] e = _et.getEigenvalues(iz,iy,ix);
              float[] u = _et.getEigenvectorU(iz,iy,ix);
              float[] v = _et.getEigenvectorV(iz,iy,ix);
              float[] w = _et.getEigenvectorW(iz,iy,ix);
              float eu = e[0], ev = e[1], ew = e[2];
              if (eu<=etiny) eu = etiny;
              if (ev<=etiny) ev = etiny;
              if (ew<=etiny) ew = etiny;
              float uz = u[0], uy = u[1], ux = u[2];
              float vz = v[0], vy = v[1], vx = v[2];
              float wz = w[0], wy = w[1], wx = w[2];
              float su = scale*sqrt(eu);
              float sv = scale*sqrt(ev);
              float sw = scale*sqrt(ew);
              ux *= su*dx; uy *= su*dy; uz *= su*dz;
              vx *= sv*dx; vy *= sv*dy; vz *= sv*dz;
              wx *= sw*dx; wy *= sw*dy; wz *= sw*dz;
              _eg.draw(xc,yc,zc,ux,uy,uz,vx,vy,vz,wx,wy,wz);
            }
          }
        }
      }
    }
  }

  /**
   * Finds the largest eigenvalue to be used for scaling.
   */
  private float findMaxEigenvalue() {
    int n1 = _et.getN1();
    int n2 = _et.getN2();
    int n3 = _et.getN3();
    float[] e = new float[3];
    float emax = 0.0f;
    for (int i3=0; i3<n3; ++i3) {
      for (int i2=0; i2<n2; ++i2) {
        for (int i1=0; i1<n1; ++i1) {
          _et.getEigenvalues(i1,i2,i3,e);
          float emaxi = max(e[0],e[1],e[2]);
          if (emax<emaxi)
            emax = emaxi;
        }
      }
    }
    return emax;
  }
}
