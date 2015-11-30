/****************************************************************************
Copyright 2015, Colorado School of Mines and others.
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
package edu.mines.jtk.awt;

import junit.framework.TestCase;
import junit.framework.TestSuite;
 
/**
 * Tests {@link edu.mines.jtk.ColorMap}.
 * @author Chris Engelsma
 * @since 2015.09.25
 */
public class ColorMapTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(ColorMap.class);
    junit.textui.TestRunner.run(suite);
  }
 
  public void testRgbToHsl() {
    float r,g,b;
    float h,s,l;
 
    for (int i=0; i<_rgbhsl.length; ++i) {
 
      r = _rgbhsl[i][0]; g = _rgbhsl[i][1]; b = _rgbhsl[i][2];
      h = _rgbhsl[i][3]; s = _rgbhsl[i][4]; l = _rgbhsl[i][5];
 
      float[] expected  = { h, s, l };
      float[] test = ColorMap.rgbToHsl(r,g,b);
 
      assertEquals(expected[0],test[0],0);
      assertEquals(expected[1],test[1],0);
      assertEquals(expected[2],test[2],0);
    }
  }
 
  public void testHslToRgb() {
    float r,g,b;
    float h,s,l;
 
    for (int i=0; i<_rgbhsl.length; ++i) {
 
      r = _rgbhsl[i][0]; g = _rgbhsl[i][1]; b = _rgbhsl[i][2];
      h = _rgbhsl[i][3]; s = _rgbhsl[i][4]; l = _rgbhsl[i][5];
 
      float[] expected  = { r, g, b };
      float[] test = ColorMap.hslToRgb(h,s,l);
 
      assertEquals(expected[0],test[0],0);
      assertEquals(expected[1],test[1],0);
      assertEquals(expected[2],test[2],0);
    }
  }
 
  public void testRgbToCieLab() {
    float r,g,b;
    float Ls,as,bs;
 
    for (int i=0; i<_rgbcielab.length; ++i) {
 
      r  = _rgbcielab[i][0]; g  = _rgbcielab[i][1]; b  = _rgbcielab[i][2];
      Ls = _rgbcielab[i][3]; as = _rgbcielab[i][4]; bs = _rgbcielab[i][5];
 
      float[] expected  = { Ls, as, bs };
      float[] test = ColorMap.rgbToCieLab(r,g,b);
 
      assertEquals(expected[0],test[0],0.01);
      assertEquals(expected[1],test[1],0.01);
      assertEquals(expected[2],test[2],0.01);
    }
  }

  public void testCieLabToRgb() {
    float r,g,b;
    float Ls,as,bs;
 
    for (int i=0; i<_rgbcielab.length; ++i) {
 
      r  = _rgbcielab[i][0]; g  = _rgbcielab[i][1]; b  = _rgbcielab[i][2];
      Ls = _rgbcielab[i][3]; as = _rgbcielab[i][4]; bs = _rgbcielab[i][5];
 
      float[] expected  = { r, g, b };
      float[] test = ColorMap.cieLabToRgb(Ls,as,bs);
 
      assertEquals(expected[0],test[0],0.01);
      assertEquals(expected[1],test[1],0.01);
      assertEquals(expected[2],test[2],0.01);
    }
  }

//////////////////////////////////////////////////////////////////////////////
// private
 
  private static float[][] _rgbhsl = {
      //   r,    g,    b,      h,    s,    l
      { 1.0f, 0.0f, 0.0f,   0.0f, 1.0f, 0.5f}, // Red
      { 0.0f, 1.0f, 0.0f, 120.0f, 1.0f, 0.5f}, // Green
      { 0.0f, 0.0f, 1.0f, 240.0f, 1.0f, 0.5f}, // Blue
      { 0.0f, 0.0f, 0.0f,   0.0f, 0.0f, 0.0f}, // Black
      { 1.0f, 1.0f, 1.0f,   0.0f, 0.0f, 1.0f}, // White
      { 1.0f, 1.0f, 0.0f,  60.0f, 1.0f, 0.5f}, // Yellow
      { 0.0f, 1.0f, 1.0f, 180.0f, 1.0f, 0.5f}, // Cyan
      { 1.0f, 0.0f, 1.0f, 300.0f, 1.0f, 0.5f}  // Magenta
    };
 
  private static float[][] _rgbcielab = {
      //   r,    g,    b,      L*,      a,    b
      { 1.0f, 0.0f, 0.0f,  53.23f,  80.10f,  67.22f}, // Red
      { 0.0f, 1.0f, 0.0f,  87.73f,- 86.18f,  83.18f}, // Green
      { 0.0f, 0.0f, 1.0f,  32.30f,  79.20f,-107.86f}, // Blue
      { 0.0f, 0.0f, 0.0f,   0.00f,   0.00f,   0.00f}, // Black
      { 1.0f, 1.0f, 1.0f, 100.00f,   0.01f,-  0.01f}, // White
      { 1.0f, 1.0f, 0.0f,  97.14f,- 21.56f,  94.48f}, // Yellow
      { 0.0f, 1.0f, 1.0f,  91.12f,- 48.08f,- 14.14f}, // Cyan
      { 1.0f, 0.0f, 1.0f,  60.32f,  98.25f,- 60.84f}  // Magenta
    };
}
