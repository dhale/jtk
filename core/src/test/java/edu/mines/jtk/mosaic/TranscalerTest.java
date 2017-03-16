/****************************************************************************
Copyright 2016, Colorado School of Mines and others.
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
package edu.mines.jtk.mosaic;

import junit.framework.TestCase;
import junit.framework.TestResult;
import junit.framework.TestSuite;
import static edu.mines.jtk.mosaic.ProjectorTest.assertVeryClose;

public class TranscalerTest extends TestCase {
    public static void main(String[] args) {
      TestSuite suite = new TestSuite(TranscalerTest.class);
      TestResult result = junit.textui.TestRunner.run(suite);

      // Check result and exit with nonzero status if any failed.
      if (!result.wasSuccessful())
        fail("Tests failed.");
    }
    
    public void testBasicTranscale () {
      Transcaler tr = new Transcaler(0.0, 0.0, 1.0, 1.0, 0, 0, 100, 100);
      assertVeryClose(0.0, tr.x(0));
      assertVeryClose(0.5, tr.y(50));
      assertVeryClose(0.77, tr.x(77));
      assertVeryClose(10, tr.y(0.1));
      assertVeryClose(83, tr.x(0.83));
      assertVeryClose(0.36, tr.x(tr.x(0.36)));
      assertVeryClose(0.74, tr.y(tr.y(0.74)));
      assertVeryClose(12, tr.x(tr.x(12)));
      assertVeryClose(63, tr.y(tr.y(63)));
    }
    
    public void testCombineWithLinearA () {
      Transcaler tr = new Transcaler(0.0, 0.0, 1.0, 1.0, 0, 0, 100, 100);
      Projector xp = new Projector(0.0, 10.0, AxisScale.LINEAR);
      Projector yp = new Projector(0.0, 50.0, AxisScale.LINEAR);
      
      Transcaler tr_c = tr.combineWith(xp, yp);
      assertVeryClose(5.0, tr_c.x(tr_c.x(5.0)));
      assertVeryClose(5.0, tr_c.y(tr_c.y(5.0)));
      assertVeryClose(75, tr_c.x(tr_c.x(75)));
      assertVeryClose(75, tr_c.y(tr_c.y(75)));
    }
    
    public void testCombineWithLinearB () {
      Transcaler tr = new Transcaler(0.0, 0.0, 1.0, 1.0, 0, 0, 100, 100);
      Projector xp = new Projector(0.0, 10.0, AxisScale.LINEAR);
      Projector yp = new Projector(50.0, 0.0, AxisScale.LINEAR);
      
      Transcaler tr_c = tr.combineWith(xp, yp);
      assertVeryClose(5.0, tr_c.x(tr_c.x(5.0)));
      assertVeryClose(5.0, tr_c.y(tr_c.y(5.0)));
      assertVeryClose(75, tr_c.x(tr_c.x(75)));
      assertVeryClose(75, tr_c.y(tr_c.y(75)));
    }
    
    
    // tolerance assertion used to compensate for loss of accuracy
    // when Transcaler converts float to int in tr.x() and tr.y()
    // combined with the logarithm
    public void testCombineWithLogA () {
      Transcaler tr = new Transcaler(0.0, 0.0, 1.0, 1.0, 0, 0, 100, 100);
      Projector xp = new Projector(10.0, 1000.0, AxisScale.LOG10);
      Projector yp = new Projector(1.0, 1000.0, AxisScale.LOG10);
      
      Transcaler tr_c = tr.combineWith(xp, yp);
      assertToleranceClose(Math.log10(15.0), tr_c.x(tr_c.x(15.0)), 0.02);
      assertToleranceClose(Math.log10(5.0), tr_c.y(tr_c.y(5.0)), 0.02);
      assertToleranceClose(75, tr_c.x(Math.pow(10,tr_c.x(75))), 0.02);
      assertToleranceClose(75, tr_c.y(Math.pow(10,tr_c.y(75))), 0.02);
    }
    
    public void testCombineWithLogB () {
      Transcaler tr = new Transcaler(0.0, 0.0, 1.0, 1.0, 0, 0, 100, 100);
      Projector xp = new Projector(1.0, 10.0, AxisScale.LOG10);
      Projector yp = new Projector(50.0, 1.0, AxisScale.LOG10);
      
      Transcaler tr_c = tr.combineWith(xp, yp);
      assertToleranceClose(Math.log10(5.0), tr_c.x(tr_c.x(5.0)), 0.02);
      assertToleranceClose(Math.log10(26.0), tr_c.y(tr_c.y(26.0)), 0.02);
      assertToleranceClose(75, tr_c.x(Math.pow(10,tr_c.x(75))), 0.02);
      assertToleranceClose(13, tr_c.y(Math.pow(10,tr_c.y(13))), 0.02);
    }
    
  public static void assertToleranceClose (double expected, double actual, double tol) {
      boolean success = true;
      success &= Math.abs(expected-actual)/Math.abs(actual) <= tol;
      if (!success)
        fail("Expected: <"+expected+"> but was:<"+actual+">");
    }    
    
}
