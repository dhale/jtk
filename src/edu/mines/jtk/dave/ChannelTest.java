/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dave;

import static edu.mines.jtk.util.MathPlus.*;

import java.util.Random;

import javax.swing.JFrame;

import edu.mines.jtk.awt.ColorMap;
import edu.mines.jtk.dsp.SincInterpolator;
import edu.mines.jtk.mosaic.*;
import edu.mines.jtk.util.Array;

/**
 * Peak interpolation errors for channel model.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.10.30
 */
public class ChannelTest {

  private static float[][] makeChannel() {
    Random random = new Random();
    int nx = 101;
    int ny = 101;
    double xmin = 0.0;
    double xmax = nx-1;
    double ymin = 0.0;
    double ymax = ny-1;
    double ay = 0.5*(ymin+ymax);
    double by = 0.25*(ymax-ymin)/xmax;
    double cy = 4.0*DBL_PI/xmax;
    double az = 1000.0;
    double bz = 0.0/xmax;
    double cz = 2.0;
    double dz = -0.5/32.0;
    float[][] z = new float[nx][ny];
    for (int ix=0; ix<nx; ++ix) {
      double x = ix;
      double yc = ay+by*x*sin(cy*x);
      for (int iy=0; iy<ny; ++iy) {
        double y = iy;
        double dy = y-yc;
        z[ix][iy] = (float)(az+bz*x+cz*exp(dz*dy*dy));
        //z[ix][iy] += random.nextFloat();
      }
    }
    float zmin = Array.min(z);
    float zmax = Array.max(z);
    System.out.println("zmin="+zmin+" zmax="+zmax);
    plot("Depth",z);
    return z;
  }

  private static int nz;
  private static double dz;
  private static double fz;

  private static float[][][] makeData(float[][] z) {
    int nx = z.length;
    int ny = z[0].length;
    dz = 4.0; // 4 meters
    float zmin = Array.min(z);
    float zmax = Array.max(z);
    nz = 20+(int)((zmax-zmin)/dz);
    fz = ((int)(zmin/dz)-10.0)*dz;
    double kz = 0.6/dz; // 0.6 = fraction of nyquist
    float[][][] d = new float[nx][ny][nz];
    float[][] dxy = new float[nx][ny];
    for (int ix=0; ix<nx; ++ix) {
      for (int iy=0; iy<ny; ++iy) {
        double zc = z[ix][iy];
        for (int iz=0; iz<nz; ++iz) {
          double zi = fz+iz*dz;
          d[ix][iy][iz] = (float)sinc(kz*(zi-zc));
          if (iz==nz/2)
            dxy[ix][iy] = d[ix][iy][iz];
        }
      }
    }
    float dmin = Array.min(d);
    float dmax = Array.max(d);
    System.out.println("dmin="+dmin+" dmax="+dmax);
    plot("DataYZ",d[nx/2]);
    plot("DataXY",dxy);
    return d;
  }
  private static double sinc(double x) {
    return (x==0.0)?1.0:sin(DBL_PI*x)/(DBL_PI*x);
  }

  private static void testSinc(float[][] zc, float[][][] d) {
    int nx = d.length;
    int ny = d[0].length;
    float[][] ae = new float[nx][ny];
    float[][] ze = new float[nx][ny];
    for (int ix=0; ix<nx; ++ix) {
      for (int iy=0; iy<ny; ++iy) {
        ae[ix][iy] = ampSinc(nz,dz,fz,d[ix][iy]);
        ze[ix][iy] = depSinc(nz,dz,fz,d[ix][iy]);
      }
    }
    float aemin = Array.min(ae);
    float aemax = Array.max(ae);
    System.out.println("sinc: aemin="+aemin+" aemax="+aemax);
    plot("SincA",0.95f,1.00f,ae);
    float zemin = Array.min(ze);
    float zemax = Array.max(ze);
    System.out.println("sinc: zemin="+zemin+" zemax="+zemax);
    plot("SincZ",ze);
  }

  private static void testPar(float[][] zc, float[][][] d) {
    int nx = d.length;
    int ny = d[0].length;
    float[][] ae = new float[nx][ny];
    float[][] ze = new float[nx][ny];
    for (int ix=0; ix<nx; ++ix) {
      for (int iy=0; iy<ny; ++iy) {
        ae[ix][iy] = ampPar(nz,dz,fz,d[ix][iy]);
        ze[ix][iy] = depPar(nz,dz,fz,d[ix][iy]);
      }
    }
    float aemin = Array.min(ae);
    float aemax = Array.max(ae);
    System.out.println("par: aemin="+aemin+" aemax="+aemax);
    plot("ParA",0.95f,1.00f,ae);
    float zemin = Array.min(ze);
    float zemax = Array.max(ze);
    System.out.println("par: zemin="+zemin+" zemax="+zemax);
    plot("ParZ",ze);
  }

  private static float depSinc(int nz, double dz, double fz, float[] p) {
    int n = p.length;
    int[] imax = new int[1];
    Array.max(p,imax);
    _si.setUniform(nz,dz,fz,p);
    return (float)_si.findMax(fz+imax[0]*dz);
  }
  private static float ampSinc(int nz, double dz, double fz, float[] p) {
    double zmax = depSinc(nz,dz,fz,p);
    return _si.interpolate(zmax);
  }
  private static SincInterpolator _si = new SincInterpolator();

  private static float depPar(int nz, double dz, double fz, float[] p) {
    int n = p.length;
    int[] imax = new int[1];
    Array.max(p,imax);
    _pi.setInput(nz,dz,fz,p);
    return (float)_pi.findMax(fz+imax[0]*dz);
  }
  private static float ampPar(int nz, double dz, double fz, float[] p) {
    double zmax = depPar(nz,dz,fz,p);
    return _pi.interpolate(zmax);
  }
  private static ParabolicInterpolator _pi = new ParabolicInterpolator();

  private static void plot(String filename, float[][] f) {
    plot(filename,Array.min(f),Array.max(f),f);
  }
  private static PlotFrame plot(
    float fmin, float fmax, float[][] f) 
  {
    PlotPanel panel = new PlotPanel(PlotPanel.Orientation.X1DOWN_X2RIGHT);
    PixelsView pv = panel.addPixels(f);
    pv.setInterpolation(PixelsView.Interpolation.LINEAR);
    pv.setColorModel(ColorMap.JET);
    pv.setClips(fmin,fmax);
    panel.addColorBar();

    PlotFrame frame = new PlotFrame(panel);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(750,600);
    frame.setVisible(true);
    return frame;
  }
  private static void plot(
    String filename, float fmin, float fmax, float[][] f) 
  {
    PlotFrame frame = plot(fmin,fmax,f);
    frame.paintToPng(300,6,filename+"Flat.png");
  }

  // Do not use this elsewhere; it does not handle ends correctly.
  private static class ParabolicInterpolator {
    public void setInputSampling(int nxin, double dxin, double fxin) {
      _nxin = nxin;
      _dxin = dxin;
      _fxin = fxin;
    }
    public void setInputSamples(float[] yin) {
      _yin = yin;
    }
    public void setInput(int nxin, double dxin, double fxin, float[] yin) {
      setInputSampling(nxin,dxin,fxin);
      setInputSamples(yin);
    }
    public float interpolate(double xout) {
      double xoutn = (xout-_fxin)/_dxin;
      int kyin = (int)(xoutn+0.5);
      if (kyin<1) kyin = 1;
      if (kyin>_nxin-2) kyin = _nxin-2;
      float ym = _yin[kyin-1];
      float y0 = _yin[kyin];
      float yp = _yin[kyin+1];
      float a = 0.5f*(ym-2.0f*y0+yp);
      float b = 0.5f*(yp-ym);
      float c = y0;
      float x = (float)(xoutn-kyin);
      return (a*x+b)*x+c;
    }
    public double findMax(double x) {
      double xn = (x-_fxin)/_dxin;
      int kyin = (int)(xn+0.5);
      if (kyin<1) kyin = 1;
      if (kyin>_nxin-2) kyin = _nxin-2;
      float ym = _yin[kyin-1];
      float y0 = _yin[kyin];
      float yp = _yin[kyin+1];
      float a = 0.5f*(ym-2.0f*y0+yp);
      float b = 0.5f*(yp-ym);
      float xmin = -0.5f*b/a;
      return _fxin+(kyin+xmin)*_dxin;
    }
    private int _nxin;
    private double _dxin;
    private double _fxin;
    private float[] _yin;
  }

  public static void main(String[] args) {
    float[][] z = makeChannel();
    float[][][] d = makeData(z);
    testSinc(z,d);
    testPar(z,d);
  }
}
