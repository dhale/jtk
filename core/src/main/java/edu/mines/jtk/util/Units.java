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

import java.util.Hashtable;
import java.util.StringTokenizer;

/**
 * A class for dimensional analysis of and conversions among units of measure.
 * For example, you can determine that ft/s and km/s have the same physical 
 * dimensions (length/time), and you can convert ft/s to km/s and vice-versa.
 * However, you cannot convert s/ft to ft/s.
 * <p>
 * A typical pattern for dealing with units is to (1) check the dimensions, 
 * (2) convert parameters to SI units, and (3) use the parameters, now with
 * consistent (SI) units.
 * <p>
 * Here is a simple example:
 * <pre><code>
 *  // Ensure frequency units have dimensions of inverse time.
 *  if (!freqUnits.haveDimensionsOf(Units.inv(timeUnits))) {
 *    // handle error
 *  }
 * 
 *  // Convert both frequency and time to SI units.
 *  freq = freqUnits.toSI(freq);
 *  time = timeUnits.toSI(time);
 * 
 *  // Use frequency and time, without worrying about units.
 *  ...
 * </code></pre>
 * <p>
 * When converting many values from some units to some other units
 * (with the same dimensions) it is more efficient to compute a shift
 * and scale factor and then use these instead of calling conversion 
 * methods. For example,
 * <pre><code>
 *  // Determine shift and scale.
 *  double shift = toUnits.doubleShiftFrom(fromUnits);
 *  double scale = toUnits.doubleScaleFrom(fromUnits);
 * 
 *  // Use shift and scale to convert lots of values.
 *  for (int i=0; i&lt;n; ++i) to[i] = shift+scale*from[i];
 * </code></pre>
 *
 * @author Dave Hale, Colorado School of Mines
 * @version 2000.02.17, 2006.07.19
 */
public final class Units implements Cloneable {

  /**
   * Constructs dimensionless units.
   */
  public Units() {
  }

  /**
   * Constructs units from a units definition.
   * @param definition the units definition (e.g., "coulomb/volt").
   * @exception UnitsFormatException if the units definition is not a valid
   *  combination of units already defined.
   */
  public Units(String definition) throws UnitsFormatException {
    Units units = unitsFromDefinition(definition);
    _scale = units._scale;
    _shift = units._shift;
    for (int i=0; i<_power.length; ++i) {
      _power[i] = units._power[i];
    }
  }

  /**
   * Clones these units.
   * @return a clone of these units.
   */
  public Object clone() {
    try {
      Units units = (Units)super.clone();
      units._power = _power.clone();
      return units;
    } catch (CloneNotSupportedException e) {
      throw new InternalError();
    }
  }

  /**
   * Determines whether specified object equals these units.
   * @param object the object to compare with these units.
   * @return true, if equal; false, otherwise.
   */
  public boolean equals(Object object) {
    if (object instanceof Units) return equals((Units)object);
    return false;
  }

  /**
   * Determines whether specified units equal these units.
   * @param units the units to compare with these units.
   * @return true, if equal; false, otherwise.
   */
  public boolean equals(Units units) {
    if (_scale!=units._scale) return false;
    if (_shift!=units._shift) return false;
    for (int i=0; i<_power.length; ++i) {
      if (_power[i]!=units._power[i]) return false;
    }
    return true;
  }

  /**
   * Converts the specified value in these units to the corresponding value
   * in SI base units (seconds, meters, moles, etc.). For example, if these 
   * units are "km", then convert will multiply the specified value by 1000.
   * @param value the value to convert.
   * @return the converted value.
   */
  public float toSI(float value) {
    return (float)((value-_shift)*_scale);
  }

  /**
   * Converts the specified value in these units to the corresponding value
   * in SI base units (seconds, meters, moles, etc.). For example, if these 
   * units are "km", then convert will multiply the specified value by 1000.
   * @param value the value to convert.
   * @return the converted value.
   */
  public double toSI(double value) {
    return (value-_shift)*_scale;
  }

  /**
   * Converts the specified value in SI base units (seconds, meters, moles, 
   * etc.) to the corresponding value in these units. For example, if these
   * units are "km", then convert will divide the specified value by 1000.
   * @param value the value to convert.
   * @return the converted value.
   */
  public float fromSI(float value) {
    return (float)(_shift+value/_scale);
  }

  /**
   * Converts the specified value in SI base units (seconds, meters, moles, 
   * etc.) to the corresponding value in these units. For example, if these
   * units are "km", then convert will divide the specified value by 1000.
   * @param value the value to convert.
   * @return the converted value.
   */
  public double fromSI(double value) {
    return _shift+value/_scale;
  }

  /**
   * Returns the shift needed to convert values from the specified 
   * units to these units.
   * The specified units must have the dimensions of these units.
   * @param units the units from which to convert.
   * @return the shift.
   */
  public float floatShiftFrom(Units units) {
    return (float)doubleShiftFrom(units);
  }

  /**
   * Returns the shift needed to convert values from the specified 
   * units to these units.
   * The specified units must have the dimensions of these units.
   * @param units the units from which to convert.
   * @return the shift.
   */
  public double doubleShiftFrom(Units units) {
    Check.argument(units.haveDimensionsOf(this),"same dimensions");
    return fromSI(units.toSI(0.0));
  }

  /**
   * Returns the scale factor needed to convert values from the specified 
   * units to these units.
   * The specified units must have the dimensions of these units.
   * @param units the units from which to convert.
   * @return the scale factor.
   */
  public float floatScaleFrom(Units units) {
    return (float)doubleScaleFrom(units);
  }

  /**
   * Returns the scale factor needed to convert values from the specified 
   * units to these units.
   * The specified units must have the dimensions of these units.
   * @param units the units from which to convert.
   * @return the scale factor.
   */
  public double doubleScaleFrom(Units units) {
    Check.argument(units.haveDimensionsOf(this),"same dimensions");
    return fromSI(units.toSI(1.0))-doubleShiftFrom(units);
  }

  /**
   * Determines whether these units have dimensions.
   * @return true, if these units have dimensions; false, otherwise.
   */
  public boolean haveDimensions() {
    for (int power:_power)
      if (power!=0) return true;
    return false;
  }

  /**
   * Determines whether these units have the dimensions of specified units.
   * @param units units with which to compare dimensions.
   * @return true, if units are the same; false, otherwise.
   */
  public boolean haveDimensionsOf(Units units) {
    for (int i=0; i<_power.length; ++i) {
      if (_power[i]!=units._power[i]) return false;
    }
    return true;
  }

  /**
   * Gets the standard definition of these units. A standard definition is 
   * the definition expressed entirely in base SI units.
   * @return the standard definition of these units.
   */
  public String standardDefinition() {
    String sd = "";
    boolean appending = false;
    if (_scale!=1.0) {
      sd += _scale;
      appending = true;
    }
    for (int i=0; i<_nbase; ++i) {
      if (_power[i]!=0) {
        if (appending) sd += " ";
        sd += _bases[i];
        if (_power[i]!=1) sd += "^"+_power[i];
        appending = true;
      }
    }
    if (_shift!=0.0) {
      double abs_shift = (_shift>0.0)?_shift:-_shift;
      if (appending) sd += " ";
      sd += (_shift>0.0)?"+ ":"- ";
      sd += abs_shift;
    }
    return sd;
  }

  /**
   * Adds a scalar to units.
   * @param units first operand.
   * @param s second operand.
   * @return the sum units + s.
   */
  public static Units add(Units units, double s) {
    return ((Units)units.clone()).shift(s);
  }

  /**
   * Subtracts a scalar from units.
   * @param units first operand.
   * @param s second operand.
   * @return the difference units - s.
   */
  public static Units sub(Units units, double s) {
    return ((Units)units.clone()).shift(-s);
  }

  /**
   * Multiplies units by scalar.
   * @param units first operand.
   * @param s second operand.
   * @return the product units * s.
   */
  public static Units mul(Units units, double s) {
    return ((Units)units.clone()).scale(s);
  }

  /**
   * Divides units by scalar.
   * @param units first operand.
   * @param s second operand.
   * @return the quotient units1 / s.
   */
  public static Units div(Units units, double s) {
    return ((Units)units.clone()).scale(1.0/s);
  }

  /**
   * Multiplies units by units.
   * @param units1 first operand.
   * @param units2 second operand.
   * @return the product units1 * units2.
   */
  public static Units mul(Units units1, Units units2) {
    return ((Units)units1.clone()).mul(units2);
  }

  /**
   * Divides units by units.
   * @param units1 first operand.
   * @param units2 second operand.
   * @return the quotient units1 / units2.
   */
  public static Units div(Units units1, Units units2) {
    return ((Units)units1.clone()).div(units2);
  }

  /**
   * Inverts units.
   * @param units to invert.
   * @return the inverse 1 / units.
   */
  public static Units inv(Units units) {
    return ((Units)units.clone()).inv();
  }

  /**
   * Raises units to a power.
   * @param units first operand.
   * @param p second operand.
   * @return the power units^p.
   */
  public static Units pow(Units units, int p) {
    return ((Units)units.clone()).pow(p);
  }

  /**
   * Adds a definition of units to the table of units.
   * <p>
   * An extensive default table of units may be provided, so that explicit
   * definition using this method may be unnecessary.
   * <p>
   * Most units are defined in terms of other units already defined. However,
   * some units, such as "ampere" and "second", are base units representing
   * the physical dimensions, such as electric current and time, respectively.
   * We define derived units in terms of base units and other derived
   * units. Only one base units should be defined for each physical dimension.
   * @param name the string by which the units will be known (e.g,, "farad").
   * @param plural true, if the name has a simple plural form, as in "farads".
   * @param definition the units definition, as in "coulomb/volt". If the 
   *  definition is null, then the name is assumed to define new base units, 
   *  such as "meter".
   *  Multiplication is denoted by a space, '.', or '*'.
   *  Division is denoted by a '/'.
   *  Addition and subtraction are denoted by '+' and '-'.
   *  Note: only addition and subtraction of constants is
   *  supported, and the constant must appear on the right-hand-side
   *  of the '+' or '-', as in "degK - 273.15", not "-273.15 + degK".
   *  Exponentiation is denoted by '^', as in s^2.
   * @return true, if the units were successfully defined; false, otherwise.
   *  Units will not be defined if (1) units with the specified name
   *  already exist or (2) the limit on the number of base units
   *  has been exceeded.
   * @exception UnitsFormatException if the definition is not a valid 
   *  combination of existing units.
   */
  public static synchronized boolean define(String name, boolean plural,
      String definition) 
    throws UnitsFormatException {
    return addDefinition(name,plural,definition);
  }

  /**
   * Determines if a string is a valid units definition.
   * Valid definitions are those consistent with
   * the format described above.
   * @param definition the units definition in question (e.g., "coulomb/volt").
   * @return true, if the units definition is valid; false, otherwise.
   */
  public static synchronized boolean isValidDefinition(String definition) {
    try {
      unitsFromDefinition(definition);
    } catch (UnitsFormatException e) {
      return false;
    }
    return true;
  }

  /**
   * Determines whether units with the specified name are defined.
   * @param name the string by which the units are known (e.g., "farad")
   * @return true, if the units are currently defined; false, otherwise.
   */
  public static synchronized boolean isDefined(String name) {
    return _table.containsKey(name);
  }

  ///////////////////////////////////////////////////////////////////////////
  // package

  // Scale these units.
  Units scale(double s) {
    _scale *= s;
    _shift /= s;
    return this;
  }

  // Shift these units.
  Units shift(double s) {
    _shift += s;
    return this;
  }

  // Multiply these units by specified units.
  Units mul(Units u) {
    _shift = (_shift!=0.0)?_shift/u._scale:u._shift/_scale;
    _scale *= u._scale;
    for (int i=0; i<_power.length; ++i) {
      _power[i] += u._power[i];
    }
    return this;
  }

  // Divide these units by specified units.
  Units div(Units u) {
    _shift *= u._scale;
    _scale /= u._scale;
    for (int i=0; i<_power.length; ++i) {
      _power[i] -= u._power[i];
    }
    return this;
  }

  // Invert these units.
  Units inv() {
    _scale = 1.0/_scale;
    _shift = 0.0;
    for (int i=0; i<_power.length; ++i) {
      _power[i] = (byte)(-_power[i]);
    }
    return this;
  }

  // Raise these units to a power.
  Units pow(int p) {
    _scale = Math.pow(_scale,p);
    _shift = 0.0;
    for (int i=0; i<_power.length; ++i) {
      _power[i] *= (byte)p;
    }
    return this;
  }

  // Construct new Units corresponding to the specified name in the table
  // of Units. If the name is not found in the table, return null.
  static synchronized Units unitsFromName(String name) {

    // Special cases.
    if (name==null || name.equals("")) return new Units();

    // First, search the table.
    UnitsTableEntry entry = _table.get(name);
    if (entry!=null) {
      return (Units)entry._units.clone();
    }

    // Second, if the name has a prefix (like "milli").
    double factor = 1.0;
    int index = findPrefix(name);
    boolean prefix = (index>=0);
    if (prefix) {
      factor = _prefix_factor[index];
      String temp = name.substring(_prefix_string[index].length());
      entry = _table.get(temp);
      if (entry!=null) {
        Units units = (Units)entry._units.clone();
        units.scale(factor);
        return units;
      }
    }

    // Third, if the name has a suffix (like "s").
    boolean suffix = (name.length()>0 && name.charAt(name.length()-1)=='s');
    if (suffix) {
      name = name.substring(0,name.length()-1);
      entry = _table.get(name);
      if (entry!=null && entry._plural) {
        Units units = (Units)entry._units.clone();
        return units;
      }
    }

    // Fourth, if the name has both prefix and suffix.
    if (prefix && suffix) {
      name = name.substring(_prefix_string[index].length());
      entry = _table.get(name);
      if (entry!=null && entry._plural) {
        Units units = (Units)entry._units.clone();
        units.scale(factor);
        return units;
      }
    }

    // Finally, give up.
    return null;
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  // For internal use only. An entry in the table of units.
  private static class UnitsTableEntry {
    UnitsTableEntry(String name, boolean plural, Units units) {
      _name = name;
      _plural = plural;
      _units = units;
    }
    String _name = null;
    boolean _plural = false;
    Units _units = null;
  }

  private static final int NPOWERS = 16;
  private static Hashtable<String, UnitsTableEntry> _table = null;
  private static String[] _bases = null;
  private static int _nbase = 0;
  private static final String _prefix_string[] = {
    "E","G","M","P","T","Y","Z",
    "a","atto","c","centi","d","da","deca","deci","deka",
    "exa","f","femto","giga","h","hecto","k","kilo",
    "m","mega","micro","milli","n","nano","p","peta","pico",
    "tera","u","y","yocto","yotta","z","zepto","zetta"
  };
  private static final double _prefix_factor[] = {
    1e18,1e9,1e6,1e15,1e12,1e24,1e21,
    1e-18,1e-18,1e-2,1e-2,1e-1,1e1,1e1,1e-1,1e1,
    1e18,1e-15,1e-15,1e9,1e2,1e2,1e3,1e3,
    1e-3,1e6,1e-6,1e-3,1e-9,1e-9,1e-12,1e15,1e-12,
    1e12,1e-6,1e-24,1e-24,1e24,1e-21,1e-21,1e21
  };
  static {
    _table = new Hashtable<String, UnitsTableEntry>();
    _bases = new String[NPOWERS];
    loadTable();
  }

  private double _scale = 1.0;
  private double _shift = 0.0;
  private byte _power[] = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};

  // Construct new Units by parsing the specified definition.
  private static synchronized Units unitsFromDefinition(String definition) 
    throws UnitsFormatException {

    // If the definition corresponds to a name in the table,
  // construct and return the named Units.
    Units units = unitsFromName(definition);
    if (units!=null) return units;

    // Otherwise, parse the definition to construct new Units.
    try {
      return UnitsParser.parse(definition);
    } catch (Exception e) {
      throw new UnitsFormatException(e.getMessage());
    }
  }

  // Search the prefix table for matching prefixes in the specified name.
  // If a match is found, return the index of the longest matching prefix.
  // Otherwise, return -1.
  private static int findPrefix(String name) {
    if (name.length()<1) return -1;
    char name0 = name.charAt(0);
    int length = 0;
    int index = -1;
    for (int i=0; i<_prefix_string.length; ++i) {
      String prefix = _prefix_string[i];
      char prefix0 = prefix.charAt(0);
      if (name0>prefix0) continue;
      if (name0<prefix0) break;
      if (name.startsWith(prefix) && length<prefix.length()) {
        length = prefix.length();
        index = i;
      }
    }
    return index;
  }

  // Add one units definition to the units table.
  private static synchronized boolean addDefinition(String name, 
      boolean plural,
      String definition) 
    throws UnitsFormatException {

    // Because the table is shared, cannot overwrite existing entries.
    if (_table.containsKey(name)) return false;

    // If the definition is null, try to create new base units.
    if (definition==null || definition.equals("")) {
      Units units = new Units();
      if (_nbase>=units._power.length-1) return false;
      ++units._power[_nbase];
      _bases[_nbase++] = name;
      _table.put(name,new UnitsTableEntry(name,plural,units));
      return true;
    }

    // Construct new Units from the specified definition.
    Units units = unitsFromDefinition(definition);

    // Add the new Units to the table.
    _table.put(name,new UnitsTableEntry(name,plural,units));
    return true;
  }

  // Load the default units table.
  private static synchronized void loadTable() {
    String[] specs = UnitsSpecs.specs;
    for (String spec:specs) {
      if (spec.startsWith("#")) continue;
      StringTokenizer st = new StringTokenizer(spec);
      if (st.countTokens()<2) continue;
      String name = st.nextToken();
      String plural_str = st.nextToken();
      if (!plural_str.equals("S") && !plural_str.equals("P")) continue;
      boolean plural = plural_str.equals("P");
      String definition = (st.hasMoreTokens())?st.nextToken(""):null;
      if (definition!=null) {
        int index = definition.indexOf("#");
        if (index>=0) definition = definition.substring(0,index);
        definition = definition.trim();
      }
      if (definition!=null && definition.equals("")) definition = null;
      try {
        boolean defined = addDefinition(name,plural,definition);
        if (!defined) {
          System.err.println("Units.loadTable: failed to define " +
            name+" = "+definition);
        }
      } catch (UnitsFormatException e) {
        System.err.println("Units.loadTable: failed to define " +
          name+" = "+definition+" because "+e.getMessage());
      }
    }
  }
}
