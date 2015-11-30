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
package edu.mines.jtk.util;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import static edu.mines.jtk.util.ArrayMath.FLT_EPSILON;
import static edu.mines.jtk.util.ArrayMath.rampfloat;

/**
 * Tests {@link edu.mines.jtk.util.Clips}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2007.01.19
 */
public class ClipsTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(ClipsTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void testPercentiles() {
    double tiny = 10.0f*FLT_EPSILON;
    int n = 101;
    //float[][][] f = rampfloat(0.0f,1.0f,0.0f,0.0f,n,n,n);
    //SimpleFloat3 f3 = new SimpleFloat3(f);
    //Clips clips = new Clips(f3);
    float[] f = rampfloat(0.0f,1.0f,n);
    Clips clips = new Clips(f);
    for (int imin=0,imax=n-1; imin<imax; ++imin,--imax) {
      double pmin = 100.0*imin/(n-1);
      double pmax = 100.0*imax/(n-1);
      clips.setPercentiles(pmin,pmax);
      float cmin = clips.getClipMin();
      float cmax = clips.getClipMax();
      assertEquals(imin,cmin,tiny);
      assertEquals(imax,cmax,tiny);
    }
  }
}
