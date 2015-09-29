/****************************************************************************
Copyright 2007, Colorado School of Mines and others.
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
