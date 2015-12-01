/****************************************************************************
Copyright 2006, Colorado School of Mines and others.
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
    boolean isValid, defined;
    try {
      isValid = Units.isValidDefinition("degrees F");
      assertTrue(isValid);
      Units.define("degrees F",false,"degF");
      defined = Units.isDefined("degrees F");
      assertTrue(defined);
      isValid = Units.isValidDefinition("degrees C");
      assertTrue(isValid);
      Units.define("degrees C",false,"degC");
      defined = Units.isDefined("degrees C");
      assertTrue(defined);
      Units.define("cubic_inches",false,"in^3");
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
