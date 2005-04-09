/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.dave;

import java.io.*;
import java.util.*;
import java.util.regex.*;

import edu.mines.jtk.dsp.*;
import edu.mines.jtk.util.*;
import static edu.mines.jtk.util.MathPlus.*;

/**
 * Reads files containing real-valued functions of one variable.
 * This class is used to read sampled signals submitted by students
 * in GPGN404.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.04.07
 */
public class Real1Reader {

  public static void main(String[] args) {
    //Real1 x = readData("data/bob1.txt");
    //Real1 x = readData("data/matt1.txt",2);
    Real1 x = readData("data/dylan1.txt");
    Sampling x1 = x.getX1();
    int nx1 = x1.getCount();
    double dx1 = x1.getDelta();
    double fx1 = x1.getFirst();
    System.out.println("nx1="+nx1+" dx1="+dx1+" fx1="+fx1);
  }

  public static Real1 readData(String fileName) {
    return readData(fileName,1);
  }

  public static Real1 readData(String fileName, int column) {
    BufferedReader br = null;
    try {
      br = new BufferedReader(new FileReader(fileName));
    } catch (IOException ioe) {
      throw new RuntimeException("Cannot open file: "+fileName);
    }
    try {
      Scanner s = new Scanner(br);
      s.findWithinHorizon("nx1=",0);
      int nx1 = s.nextInt();
      s.findWithinHorizon("dx1=",0);
      double dx1 = s.nextDouble();
      s.findWithinHorizon("fx1=",0);
      double fx1 = s.nextDouble();
      s.findWithinHorizon("f=",0);
      s.nextLine();
      float[] f = new float[nx1];
      for (int ix1=0; ix1<nx1; ++ix1) {
        for (int skip=1; skip<column; ++skip)
          s.next();
        f[ix1] = s.nextFloat();
        s.nextLine();
      }
      s.close();
      return new Real1(nx1,dx1,fx1,f);
    } catch (InputMismatchException ime) {
      throw new RuntimeException("Unknown format of file: "+fileName);
    }
  }
}
