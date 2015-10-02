/****************************************************************************
Copyright 2004, Colorado School of Mines and others.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
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
