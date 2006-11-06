/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dave;

import java.io.*;

import javax.swing.JFrame;

import edu.mines.jtk.awt.ColorMap;
import edu.mines.jtk.mosaic.*;
import edu.mines.jtk.util.Array;

/**
 * Test reading and display of some 3-D data.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.11.29
 */
public class Data3dTest {

  public static void main(String[] args) {
    int i3 = 128;
    if (args.length>0)
      i3 = Integer.valueOf(args[0]);
    //convertData();
    int n1 = 180;
    int n2 = 180;
    int n3 = 180;
    float[][][] f = readData("segy180.dat",n1,n2,n3);
    plot(f[i3]);
  }

  private static float[][][] readData(String file, int n1, int n2, int n3) {
    float[][][] f = new float[n3][n2][n1];
    try {
      FileInputStream fis = new FileInputStream(file);
      BufferedInputStream bis = new BufferedInputStream(fis);
      DataInputStream dis = new DataInputStream(bis);
      for (int i3=0; i3<n3; ++i3) {
        for (int i2=0; i2<n2; ++i2) {
          for (int i1=0; i1<n1; ++i1) {
            f[i3][i2][i1] = dis.readFloat();
          }
        }
      }
      fis.close();
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
    float fmin = Array.min(f);
    float fmax = Array.max(f);
    System.out.println("fmin="+fmin+" fmax="+fmax);
    return f;
  }

  /*
  private static void writeData(String file, float[][][] f) {
    int n3 = f.length;
    int n2 = f[0].length;
    int n1 = f[0][0].length;
    try {
      FileOutputStream fos = new FileOutputStream(file);
      BufferedOutputStream bos = new BufferedOutputStream(fos);
      DataOutputStream dos = new DataOutputStream(bos);
      for (int i3=0; i3<n3; ++i3) {
        for (int i2=0; i2<n2; ++i2) {
          for (int i1=0; i1<n1; ++i1) {
            dos.writeFloat(f[i3][i2][i1]);
          }
        }
      }
      bos.close();
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
  }
  */
  
  /*
  private static void convertData() {
    int n3 = 256;
    int n2 = 256;
    int n1 = 256;
    float[][][] f = new float[n3][n2][n1];
    try {
      FileInputStream fis = new FileInputStream("segy256.vol");
      DataInputStream dis = new DataInputStream(fis);
      dis.skipBytes(128); // header
      byte[] b = new byte[n1];
      for (int i3=0; i3<n3; ++i3) {
        for (int i2=0; i2<n2; ++i2) {
          dis.readFully(b,0,n1);
          for (int i1=0; i1<n1; ++i1) {
            f[i1][i2][i3] = (float)(b[i1]&0xff)-128.0f;
          }
        }
      }
      fis.close();
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
    float fmin = Array.min(f);
    float fmax = Array.max(f);
    System.out.println("fmin="+fmin+" fmax="+fmax);
    int m1 = 180, j1 = 60;
    int m2 = 180, j2 = 8;
    int m3 = 180, j3 = 30;
    f = Array.copy(m1,m2,m3,j1,j2,j3,f);
    writeData("segy180.dat",f);
  }
  */

  private static void plot(float[][] f) {
    plot(Array.min(f),Array.max(f),f);
  }
  /*
  private static void plot(String filename, float[][] f) {
    plot(filename,Array.min(f),Array.max(f),f);
  }
  */
  private static PlotFrame plot(
    float fmin, float fmax, float[][] f) 
  {
    PlotPanel panel = new PlotPanel(PlotPanel.Orientation.X1DOWN_X2RIGHT);
    PixelsView pv = panel.addPixels(f);
    pv.setInterpolation(PixelsView.Interpolation.NEAREST);
    pv.setColorModel(ColorMap.GRAY);
    //pv.setClips(fmin,fmax);

    PlotFrame frame = new PlotFrame(panel);
    frame.setSize(550,500);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
    return frame;
  }
  /*
  private static void plot(
    String filename, float fmin, float fmax, float[][] f) 
  {
    PlotFrame frame = plot(fmin,fmax,f);
    frame.paintToPng(300,6,filename+".png");
  }
  */

}
