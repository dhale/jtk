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
package edu.mines.jtk.dsp;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Tests {@link edu.mines.jtk.dsp.LocalShiftFinder}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2007.01.15
 */
public class LocalShiftFinderTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(LocalShiftFinderTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void testCosine() {
    float w = 0.02f*2.0f*FLT_PI;
    int n1 = 1001;
    float shift = 10.0f;
    float sigma = 8.0f*shift;
    float[] f = cos(rampfloat(w*shift,w,n1));
    float[] g = cos(rampfloat(0.0f,w,n1));
    float[] u = new float[n1];
    float[] c = new float[n1];
    float[] d = new float[n1];
    int min1 = -2*(int)shift;
    int max1 =  2*(int)shift;
    LocalShiftFinder lsf = new LocalShiftFinder(shift);
    lsf.find1(min1,max1,f,g,u,c,d);
    for (int i1=n1/4; i1<3*n1/4; ++i1) {
      assertEquals(shift,u[i1],0.02f);
      assertEquals(1.0f,c[i1],0.02f);
      assertEquals(1.0f,d[i1],0.02f);
    }
    /*
    edu.mines.jtk.mosaic.SimplePlot.asPoints(f);
    edu.mines.jtk.mosaic.SimplePlot.asPoints(g);
    edu.mines.jtk.mosaic.SimplePlot.asPoints(u);
    edu.mines.jtk.mosaic.SimplePlot.asPoints(c);
    edu.mines.jtk.mosaic.SimplePlot.asPoints(d);
    */
  }
}
