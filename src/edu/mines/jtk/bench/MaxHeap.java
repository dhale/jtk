/****************************************************************************
Copyright (c) 2007, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.bench;

/**
 * Tests allocation from Java heap.
 * @author Dave Hale, Colorado School of Mines
 * @version 2007.06.22
 */
public class MaxHeap {

  public static void main(String[] args) {
    int m = 1000*1000;
    for (int n=100; n<2000000000; n=(int)(1.1*n)) {
      byte[][] a = new byte[n][m];
      n = a.length;
      System.out.println("allocated n="+n+" MB");
    }
  }
}
