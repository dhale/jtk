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
package edu.mines.jtk.util;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import static edu.mines.jtk.util.MathPlus.*;

/**
 * Tests {@link edu.mines.jtk.util.AxisTics}.
 * @author Dave Hale, Colorado School of Mines 
 * @version 2004.12.14
 */
public class AxisTicsTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(AxisTicsTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void test180() {
    check(-180.0,180.0,4, 3,100.0,-100.0, 37,10.0,-180.0);
    check(-180.0,180.0,5, 3,100.0,-100.0, 37,10.0,-180.0);
    check(-180.0,180.0,6, 3,100.0,-100.0, 37,10.0,-180.0);
    check(-180.0,180.0,7, 7, 50.0,-150.0, 37,10.0,-180.0);
    check(-180.0,180.0,8, 7, 50.0,-150.0, 37,10.0,-180.0);
    check(-180.0,180.0,9, 7, 50.0,-150.0, 37,10.0,-180.0);
  }

  public void testAll() {

    check(0.0, 10.0, 1.0,  11, 1.0,   0.0,  101, 0.1,   0.0);
    check(0.0, 10.0,  11,  11, 1.0,   0.0,  101, 0.1,   0.0);

    check(0.0,-10.0, 1.0,  11, 1.0, -10.0,  101, 0.1, -10.0);
    check(0.0,-10.0,  11,  11, 1.0, -10.0,  101, 0.1, -10.0);

    check(1.0, 10.0, 1.0,  10, 1.0,   1.0,   91, 0.1,   1.0);
    check(1.0, 10.0,  10,  10, 1.0,   1.0,   91, 0.1,   1.0);

    check(1.0, 10.0, 2.0,   5, 2.0,   2.0,   10, 1.0,   1.0);
    check(1.0, 10.0,   9,   5, 2.0,   2.0,   10, 1.0,   1.0);

    check(0.9, 10.1, 2.0,   5, 2.0,   2.0,   10, 1.0,   1.0);
    check(0.9, 10.1,   9,   5, 2.0,   2.0,   10, 1.0,   1.0);
  }

  private void check(
    double xmin, double xmax, double dtic,
    int nmajor, double dmajor, double fmajor,
    int nminor, double dminor, double fminor)
  {
    check(new AxisTics(xmin,xmax,dtic),
      nmajor,dmajor,fmajor,nminor,dminor,fminor);
  }

  private void check(
    double xmin, double xmax, int ntic,
    int nmajor, double dmajor, double fmajor,
    int nminor, double dminor, double fminor)
  {
    check(new AxisTics(xmin,xmax,ntic),
      nmajor,dmajor,fmajor,nminor,dminor,fminor);
  }

  private void check(AxisTics at, 
    int nmajor, double dmajor, double fmajor,
    int nminor, double dminor, double fminor)
  {
    assertEquals(nmajor,at.getCountMajor());
    assertEquals(dmajor,at.getDeltaMajor());
    assertEquals(fmajor,at.getFirstMajor());
    assertEquals(nminor,at.getCountMinor());
    assertEquals(dminor,at.getDeltaMinor());
    assertEquals(fminor,at.getFirstMinor());
  }

  private void assertEquals(double e, double a) {
    double tiny = max(abs(e),abs(a))*100.0*DBL_EPSILON;
    assertEquals(e,a,tiny);
  }
}
