/****************************************************************************
 Copyright 2017, Colorado School of Mines and others.
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
package edu.mines.jtk.sgl;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import static edu.mines.jtk.sgl.OrbitViewLighting.*;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Tests {@link OrbitViewLighting}.
 * @author Chris Engelsma
 * @version 2017.01.18
 */
public class OrbitViewLightingTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(OrbitViewLightingTest.class);
    junit.textui.TestRunner.run(suite);
  }

  /**
   * To maintain functionality with the legacy l in the JTK, the
   * default l should have:
   * 1. Single directional light source at (-0.1,-0.1,1.0)
   * 2. Ambient light set to (0,0,0,1)
   * 3. Diffuse light set to (1,1,1,1)
   * 4. Specular light set to (1,1,1,1)
   */
  public void testDefaultParameters() {
    OrbitViewLighting l = new OrbitViewLighting();

    assertEquals(l.getLightSourceType(0), ld);
    assertArrayEquals(l.getAmbient(0),  BLACK,0);
    assertArrayEquals(l.getSpecular(0), WHITE,0);
    assertArrayEquals(l.getDiffuse(0),  WHITE,0);
    assertArrayEquals(l.getPosition(0), POS,  0);
    assertTrue(l.isLightOn(0));

    assertEquals(l.getLightSourceType(1), ld);
    assertArrayEquals(l.getAmbient(1),  BLACK,0);
    assertArrayEquals(l.getSpecular(1), WHITE,0);
    assertArrayEquals(l.getDiffuse(1),  WHITE,0);
    assertArrayEquals(l.getPosition(1), POS,  0);
    assertFalse(l.isLightOn(1));

    assertEquals(l.getLightSourceType(2), ld);
    assertArrayEquals(l.getAmbient(2),  BLACK,0);
    assertArrayEquals(l.getSpecular(2), WHITE,0);
    assertArrayEquals(l.getDiffuse(2),  WHITE,0);
    assertArrayEquals(l.getPosition(2), POS,  0);
    assertFalse(l.isLightOn(2));

  }

  /**
   * Passing no parameters only affects the primary light source.
   */
  public void testPrimaryLightFunctions() {
    OrbitViewLighting l = new OrbitViewLighting();
    float[] newpos = new float[] { 5.0f, 5.0f, 5.0f };

    l.setAmbientAndDiffuse(RED);
    l.setSpecular(GREEN);
    l.setLightSourceType(lp);
    l.setPosition(newpos);

    assertArrayEquals(l.getAmbient(), l.getAmbient(0),0);
    assertArrayEquals(l.getDiffuse(), l.getDiffuse(0),0);
    assertArrayEquals(l.getSpecular(),l.getSpecular(0),0);
    assertArrayEquals(l.getPosition(),l.getPosition(0),0);
    assertEquals(l.getLightSourceType(),l.getLightSourceType(0));

    assertArrayEquals(l.getAmbient(0), RED,0);
    assertArrayEquals(l.getDiffuse(0), RED,0);
    assertArrayEquals(l.getSpecular(0),GREEN,0);
    assertArrayEquals(l.getPosition(0),newpos,0);
    assertEquals(l.getLightSourceType(0),lp);

    assertArrayEquals(l.getAmbient(1), BLACK,0);
    assertArrayEquals(l.getDiffuse(1), WHITE,0);
    assertArrayEquals(l.getSpecular(1),WHITE,0);
    assertArrayEquals(l.getPosition(1),POS,0);
    assertEquals(l.getLightSourceType(1),ld);

    assertArrayEquals(l.getAmbient(2), BLACK,0);
    assertArrayEquals(l.getDiffuse(2), WHITE,0);
    assertArrayEquals(l.getSpecular(2),WHITE,0);
    assertArrayEquals(l.getPosition(2),POS,0);
    assertEquals(l.getLightSourceType(2),ld);
  }

  public void testEquality() {
    OrbitViewLighting sl0 = new OrbitViewLighting();
    OrbitViewLighting sl1 = new OrbitViewLighting();

    assertEquals(sl0,sl1);

    sl1.setPosition(1,new float[] { 2.0f, 0.0f, 0.0f });

    assertNotEquals(sl0,sl1);

    sl1 = new OrbitViewLighting();
    sl1.setAmbient(1, RED);

    assertNotEquals(sl0,sl1);

    sl1 = new OrbitViewLighting();
    sl1.setDiffuse(1, RED);

    assertNotEquals(sl0,sl1);

    sl1 = new OrbitViewLighting();
    sl1.setSpecular(1, RED);

    assertNotEquals(sl0,sl1);

    sl1 = new OrbitViewLighting();
    sl1.setLightSourceType(1, lp);

    assertNotEquals(sl0,sl1);

    sl1 = new OrbitViewLighting();
    sl1.setAmbientAndDiffuse(RED);
    sl0.setAmbientAndDiffuse(RED);

    assertEquals(sl0,sl1);
  }

  public void testLightToggle() {
    OrbitViewLighting l = new OrbitViewLighting();
    assertTrue(l.isLightOn());
    l.toggleLight(0); assertFalse(l.isLightOn());
    l.toggleLight(0); assertTrue(l.isLightOn());
  }


  ////////////////////////////////////////////////////////////////////////////
  // private

  private static final float[] BLACK = new float[] { 0.0f, 0.0f, 0.0f, 1.0f };
  private static final float[] WHITE = new float[] { 1.0f, 1.0f, 1.0f, 1.0f };
  private static final float[] RED   = new float[] { 1.0f, 0.0f, 0.0f, 1.0f };
  private static final float[] GREEN = new float[] { 1.0f, 0.0f, 0.0f, 1.0f };
  private static final float[] POS   = new float[] { -0.1f, -0.1f, 1.0f };

  private LightSourceType ld = LightSourceType.DIRECTIONAL;
  private LightSourceType lp = LightSourceType.POSITIONAL;

}
