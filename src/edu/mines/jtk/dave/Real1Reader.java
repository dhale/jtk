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
 * Used to read sampled signals submitted by students in GPGN404.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.04.07
 */
public class Real1Reader {

  public static void main(String[] args) {
    //Real1 x = readData("data/bob1.txt");
    //Real1 x = readData("data/matt1.txt",2);
    //Real1 x = readData("data/dylan1.txt");
    Real1 x = readData("data/emily1.txt",5);
    Sampling st = x.getT();
    int nt = st.getCount();
    double dt = st.getDelta();
    double ft = st.getFirst();
    System.out.println("nt="+nt+" dt="+dt+" ft="+ft);
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
      int nt = s.nextInt();
      s.findWithinHorizon("dx1=",0);
      double dt = s.nextDouble();
      s.findWithinHorizon("fx1=",0);
      double ft = s.nextDouble();
      s.findWithinHorizon("f=",0);
      s.nextLine();
      float[] x = new float[nt];
      for (int it=0; it<nt; ++it) {
        for (int skip=1; skip<column; ++skip)
          s.next();
        x[it] = s.nextFloat();
        s.nextLine();
      }
      s.close();
      return new Real1(nt,dt,ft,x);
    } catch (InputMismatchException ime) {
      throw new RuntimeException("Unknown format of file: "+fileName);
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // Not used, yet.

  /*
  scanf()               Regular Expression 
  %c 	                .
  %5c 	                .{5}
  %d 	                [-+]?\d+
  %e, %E, %f, %g 	[-+]?(\d+(\.\d*)?|\d*\.\d+)([eE][-+]?\d+)?
  %i 	                [-+]?(0[xX][\dA-Fa-f]+|0[0-7]*|\d+)
  %o 	                0[0-7]*
  %s 	                \S+
  %u 	                \d+
  %x, %X 	        0[xX][\dA-Fa-f]+
  */
  // quoted string: "[^"\\\r\n]*(\\.[^"\\\r\n]*)*"

  private static class Named {
    public String name;
    public boolean found;
    public Named(String name) {
      this(name,"\\s");
    }
    protected Named(String name, String pattern) {
      this.name = name;
      _pattern = Pattern.compile(name+"=("+pattern+")");
    }
    protected String findString(String s) {
      Matcher m = _pattern.matcher(s);
      if (!m.find())
        return null;
      found = true;
      return m.group(1);
    }
    private Pattern _pattern;

    private static class Integer extends Named {
      public int value;
      public Integer(String name, int value) {
        super(name,"([-+]?\\d+)");
      }
      public boolean find(String s) {
        String v = super.findString(s);
        if (v==null)
          return false;
        value = java.lang.Integer.parseInt(v);
        found = true;
        return true;
      }
    }

    private static class Double extends Named {
      public double value;
      public Double(String name, double value) {
        super(name,"[-+]?(\\d+(\\.\\d*)?|\\d*\\.\\d+)([eE][-+]?\\d+)?");
      }
      public boolean find(String s) {
        String v = super.findString(s);
        if (v==null)
          return false;
        value = java.lang.Double.parseDouble(v);
        found = true;
        return true;
      }
    }
  }
}
