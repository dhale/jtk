/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.bench;

import java.util.Scanner;

/**
 * Demonstrate bug in {@link java.util.Scanner}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.04.09
 */
public class ScannerBug {
  private static final String eol = System.getProperty("line.separator");
  private static final String input1 =
    "line 1" + eol +
    ""       + eol +
    "line 3" + eol +
    ""       + eol +
    "line 5" + eol +
    ""       + eol +
    "line 7" + eol;
  private static final String input2 =
    "line 1" + eol +
    " "      + eol +
    "line 3" + eol +
    " "      + eol +
    "line 5" + eol +
    " "      + eol +
    "line 7" + eol;
  private static void scan(String input) {
    Scanner s = new Scanner(input);
    while (s.hasNextLine()) {
      s.findInLine("5");
      System.out.println(s.nextLine());
    }
    s.close();
  }
  public static void main(String[] args) {
    System.out.println("Scanning input with empty lines (incorrectly):");
    scan(input1);
    System.out.println("Scanning input without empty lines (correctly):");
    scan(input2);
  }
}
