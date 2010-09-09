/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.util;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests {@link edu.mines.jtk.util.Units}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2000.02.21, 2006.07.27
 */
public class UnitsTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(UnitsTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void testDefine() {
    boolean defined;
    try {
      defined = Units.define("degrees F",false,"degF");
      assertTrue(defined);
      defined = Units.define("degrees C",false,"degC");
      assertTrue(defined);
      defined = Units.define("cubic_inches",false,"in^3");
      assertTrue(defined);
      defined = Units.isDefined("m");
      assertTrue(defined);
      defined = Units.define("m",false,"meters");
      assertTrue(!defined);
    } catch (UnitsFormatException e) {
      assertTrue(false);
    }
    defined = true;
    try {
      Units.define("foo_inches",false,"foo inches");
    } catch (UnitsFormatException e) {
      defined = false;
    }
    assertTrue(!defined);
  }

  public void testConversion() {
    try {
      Units.define("degrees F",false,"degF");
      Units.define("degrees C",false,"degC");
      Units.define("cubic_inches",false,"in^3");
    } catch (UnitsFormatException e) {
      assertTrue(false);
    }

    // Table of conversions = {fromUnits, toUnits, shift, scale}
    String[][] conversions = {
      {"m","foo","invalid","invalid"},
      {"foo","m","invalid","invalid"},
      {"s","m","incompatible","incompatible"},
      {"m","meters","0","1"},
      {"-1 cm","m","0","-0.01"},
      {"degC","degF","32","1.8"},
      {"degrees C","degrees F","32","1.8"},
      {"degrees_Celsius","degrees_Fahrenheit","32","1.8"},
      {"10^-2 m^2","(0.1 m)^2","0","1"},
      {"ft/s","m/s","0","0.3048"},
      {"ampere hour","coulomb","0","3600"},
      {"cubic_inches/min","m^3/s","0","2.73117733333E-7"},
      {"avoirdupois_ounce/ft^2","kg/m^2","0","0.305151693637"},
      {"kgf*s^2/m","kg","0","9.80665"},
      {"kilogram*meter/second^2","kilogram*meter/second/second","0","1.0"},
    };

    for (String[] conversion:conversions) {
      String from = conversion[0];
      String to = conversion[1];
      Units fromUnits,toUnits;
      try {
        fromUnits = new Units(from);
        toUnits = new Units(to);
      } catch (UnitsFormatException e) {
        assertTrue(conversion[2].equals("invalid"));
        continue;
      }
      if (fromUnits.haveDimensionsOf(toUnits)) {
        float shift = toUnits.floatShiftFrom(fromUnits);
        float scale = toUnits.floatScaleFrom(fromUnits);
        float shiftExpected = Float.parseFloat(conversion[2]);
        float scaleExpected = Float.parseFloat(conversion[3]);
        assertTrue(shift==shiftExpected);
        assertTrue(scale==scaleExpected);
      } else {
        assertTrue(conversion[2].equals("incompatible"));
      }
    }
  }

  public void testSpecification() {
    try {
      Units.define("degrees F",false,"degF");
    } catch (UnitsFormatException e) {
      assertTrue(false);
    }

    String[][] specifications = {
      {"",""},
      {"foo","invalid"},
      {"%","0.01"},
      {"kHz","1000.0 second^-1"},
      {"kgf*s^2/m","9.80665 kilogram"},
      {"degrees F","0.5555555555555556 kelvin - 459.67"},
    };

    for (String[] specification:specifications) {
      try {
        String sd = new Units(specification[0]).standardDefinition();
        assertTrue(sd.equals(specification[1]));
      } catch (UnitsFormatException e) {
        assertTrue(specification[1].equals("invalid"));
      }
    }
  }
}
