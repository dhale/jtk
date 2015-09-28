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

/**
 * A parameter - a named value (or array of values) with a type and 
 * (optional) units.
 * A parameter's type may be set explicitly or by by setting its value(s).
 * A parameter value set as one type may be got as another type, provided
 * that the implied conversion is supported. For example, any float may be 
 * converted to a String, but only some Strings may be converted to floats.
 * Getting a parameter value never changes the intrinsic parameter type.
 * A ParameterConvertException is thrown when a conversion fails.
 *
 * Parameters typically reside in a parameter set. A parameter set contains
 * parameters and parameter subsets, thereby creating a tree with parameters
 * as leaves.
 *
 * @see edu.mines.jtk.util.ParameterSet
 * @see edu.mines.jtk.util.ParameterConvertException
 *
 * @author Dave Hale, Colorado School of MInes
 * @version 07/10/2000, 08/24/2006.
 */
public class Parameter implements Cloneable {

  ///////////////////////////////////////////////////////////////////////////
  // Public.

  /**
   * Parameter type null.
   */ 
  public final static int NULL = 0;

  /**
   * Parameter type boolean.
   */
  public final static int BOOLEAN = 1;

  /**
   * Parameter type int.
   */
  public final static int INT = 2;

  /**
   * Parameter type long.
   */
  public final static int LONG = 3;

  /**
   * Parameter type float.
   */
  public final static int FLOAT = 4;

  /**
   * Parameter type double.
   */
  public final static int DOUBLE = 5;

  /**
   * Parameter type string.
   */
  public final static int STRING = 6;

  /**
   * Construct an empty named parameter.
   * @param name parameter name.
   */
  public Parameter(String name) {
    setNameAndParent(name,null);
  }

  /**
   * Construct a named parameter with boolean value.
   * @param name parameter name.
   * @param value parameter value.
   */
  public Parameter(String name, boolean value) {
    setNameAndParent(name,null);
    setBoolean(value);
  }

  /**
   * Construct a named parameter with int value.
   * @param name parameter name.
   * @param value parameter value.
   */
  public Parameter(String name, int value) {
    setNameAndParent(name,null);
    setInt(value);
  }

  /**
   * Construct a named parameter with long value.
   * @param name parameter name.
   * @param value parameter value.
   */
  public Parameter(String name, long value) {
    setNameAndParent(name,null);
    setLong(value);
  }

  /**
   * Construct a named parameter with float value.
   * @param name parameter name.
   * @param value parameter value.
   */
  public Parameter(String name, float value) {
    setNameAndParent(name,null);
    setFloat(value);
  }

  /**
   * Construct a named parameter with double value.
   * @param name parameter name.
   * @param value parameter value.
   */
  public Parameter(String name, double value) {
    setNameAndParent(name,null);
    setDouble(value);
  }

  /**
   * Construct a named parameter with String value.
   * @param name parameter name.
   * @param value parameter value.
   */
  public Parameter(String name, String value) {
    setNameAndParent(name,null);
    setString(value);
  }

  /**
   * Construct a named parameter with boolean values.
   * @param name parameter name.
   * @param values parameter values.
   */
  public Parameter(String name, boolean[] values) {
    setNameAndParent(name,null);
    setBooleans(values);
  }

  /**
   * Construct a named parameter with int values.
   * @param name parameter name.
   * @param values parameter values.
   */
  public Parameter(String name, int[] values) {
    setNameAndParent(name,null);
    setInts(values);
  }

  /**
   * Construct a named parameter with long values.
   * @param name parameter name.
   * @param values parameter values.
   */
  public Parameter(String name, long[] values) {
    setNameAndParent(name,null);
    setLongs(values);
  }

  /**
   * Construct a named parameter with float values.
   * @param name parameter name.
   * @param values parameter values.
   */
  public Parameter(String name, float[] values) {
    setNameAndParent(name,null);
    setFloats(values);
  }

  /**
   * Construct a named parameter with double values.
   * @param name parameter name.
   * @param values parameter values.
   */
  public Parameter(String name, double[] values) {
    setNameAndParent(name,null);
    setDoubles(values);
  }

  /**
   * Construct a named parameter with String values.
   * @param name parameter name.
   * @param values parameter values.
   */
  public Parameter(String name, String[] values) {
    setNameAndParent(name,null);
    setStrings(values);
  }

  /**
   * Construct a named parameter with boolean value and units.
   * @param name parameter name.
   * @param value parameter value.
   * @param units parameter units.
   */
  public Parameter(String name, boolean value, String units) {
    setNameAndParent(name,null);
    setBoolean(value);
    setUnits(units);
  }

  /**
   * Construct a named parameter with int value and units.
   * @param name parameter name.
   * @param value parameter value.
   * @param units parameter units.
   */
  public Parameter(String name, int value, String units) {
    setNameAndParent(name,null);
    setInt(value);
    setUnits(units);
  }

  /**
   * Construct a named parameter with long value and units.
   * @param name parameter name.
   * @param value parameter value.
   * @param units parameter units.
   */
  public Parameter(String name, long value, String units) {
    setNameAndParent(name,null);
    setLong(value);
    setUnits(units);
  }

  /**
   * Construct a named parameter with float value and units.
   * @param name parameter name.
   * @param value parameter value.
   * @param units parameter units.
   */
  public Parameter(String name, float value, String units) {
    setNameAndParent(name,null);
    setFloat(value);
    setUnits(units);
  }

  /**
   * Construct a named parameter with double value and units.
   * @param name parameter name.
   * @param value parameter value.
   * @param units parameter units.
   */
  public Parameter(String name, double value, String units) {
    setNameAndParent(name,null);
    setDouble(value);
    setUnits(units);
  }

  /**
   * Construct a named parameter with String value and units.
   * @param name parameter name.
   * @param value parameter value.
   * @param units parameter units.
   */
  public Parameter(String name, String value, String units) {
    setNameAndParent(name,null);
    setString(value);
    setUnits(units);
  }

  /**
   * Construct a named parameter with boolean values and units.
   * @param name parameter name.
   * @param values parameter values.
   * @param units parameter units.
   */
  public Parameter(String name, boolean[] values, String units) {
    setNameAndParent(name,null);
    setBooleans(values);
    setUnits(units);
  }

  /**
   * Construct a named parameter with int values and units.
   * @param name parameter name.
   * @param values parameter values.
   * @param units parameter units.
   */
  public Parameter(String name, int[] values, String units) {
    setNameAndParent(name,null);
    setInts(values);
    setUnits(units);
  }

  /**
   * Construct a named parameter with long values and units.
   * @param name parameter name.
   * @param values parameter values.
   * @param units parameter units.
   */
  public Parameter(String name, long[] values, String units) {
    setNameAndParent(name,null);
    setLongs(values);
    setUnits(units);
  }

  /**
   * Construct a named parameter with float values and units.
   * @param name parameter name.
   * @param values parameter values.
   * @param units parameter units.
   */
  public Parameter(String name, float[] values, String units) {
    setNameAndParent(name,null);
    setFloats(values);
    setUnits(units);
  }

  /**
   * Construct a named parameter with double values and units.
   * @param name parameter name.
   * @param values parameter values.
   * @param units parameter units.
   */
  public Parameter(String name, double[] values, String units) {
    setNameAndParent(name,null);
    setDoubles(values);
    setUnits(units);
  }

  /**
   * Construct a named parameter with String values and units.
   * @param name parameter name.
   * @param values parameter values.
   * @param units parameter units.
   */
  public Parameter(String name, String[] values, String units) {
    setNameAndParent(name,null);
    setStrings(values);
    setUnits(units);
  }

  /**
   * Clone this parameter.
   * The clone will be an orphan; its parent parameter set will be null.
   * @return a clone of this parameter.
   */
  public Object clone() throws CloneNotSupportedException {
    try {
      Parameter p = (Parameter)super.clone();
      p._parent = null;
      return p.replaceWith(this);
    } catch (CloneNotSupportedException e) {
      throw new InternalError();
    }
  }

  /**
   * Replace the contents of this parameter with a copy of
   * the contents of the specified parameter.
   * Do not change this parameter's parent.
   * @param par the parameter with contents that will replace
   *  those in this parameter.
   * @return this parameter, with contents replaced.
   */
  public Parameter replaceWith(Parameter par) {
    if (par==this) return this;
    setName(par.getName());
    setUnits(par.getUnits());
    setValues(par.getValues());
    return this;
  }

  /**
   * Copy this parameter to the specified parent parameter set 
   * without changing its name.
   * @param parent the parameter set into which to copy this parameter;
   *  the parent of the destination parameter.
   *  If the parent is null, the destination parameter will be an orphan.
   * @return the destination parameter.
   */
  public Parameter copyTo(ParameterSet parent) {
    return copyTo(parent,getName());
  }

  /**
   * Copy this parameter.to the specified parent parameter set 
   * while changing its name.
   * @param parent the parameter set into which to copy this parameter;
   *  the parent of the destination parameter.
   *  If the parent is null, the destination parameter will be an orphan.
   * @param name the destination parameter name.
   * @return the destination parameter.
   */
  public Parameter copyTo(ParameterSet parent, String name) {
    if (_parent==parent && _name==name) return this;
    Parameter p = (parent!=null)?parent.addParameter(name):new Parameter(name);
    p.setUnits(getUnits());
    p.setValues(getValues());
    return p;
  }

  /**
   * Move this parameter to the specified parent parameter set 
   * without changing its name.
   * @param parent the parameter set into which to move this parameter;
   *  the parent of the destination parameter.
   *  If the parent is null, the destination parameter will be an orphan.
   * @return the destination parameter.
   */
  public Parameter moveTo(ParameterSet parent) {
    return moveTo(parent,getName());
  }

  /**
   * Move this parameter to the specified parent parameter set 
   * while changing its name.
   * @param parent the parameter set into which to move this parameter;
   *  the parent of the destination parameter.
   *  If the parent is null, the destination parameter will be an orphan.
   * @param name the destination parameter name.
   * @return the destination parameter.
   */
  public Parameter moveTo(ParameterSet parent, String name) {
    if (_parent==parent && _name==name) return this;
    if (_parent!=null) _parent.remove(this);
    if (parent!=null) {
      parent.insert(name,this);
    } else {
      setNameAndParent(name,null);
    }
    return this;
  }

  /**
   * Remove this parameter from its parent parameter set.
   * In other words, orphan this parameter.
   * If this parameter is already an orphan, do nothing.
   */
  public void remove() {
    if (_parent!=null) _parent.remove(this);
  }

  /**
   * Get the parameter name.
   * @return parameter name.
   */
  public String getName() {
    return _name;
  }

  /**
   * Set the parameter name.
   * @param name parameter name.
   *  If the parameter has a parent parameter set, ignore a null name.
   */
  public void setName(String name) {
    moveTo(_parent,name);
  }

  /**
   * Get the parameter units.
   * @return parameter units; null, if the parameter has no units.
   */
  public String getUnits() {
    return _units;
  }

  /**
   * Set the parameter units.
   * @param units parameter units. For no units, specify null.
   */
  public void setUnits(String units) {
    _units = units;
  }

  /**
   * Get the parameter type.
   * @return parameter type.
   */
  public int getType() {
    if (_values instanceof boolean[]) {
      return BOOLEAN;
    } else if (_values instanceof int[]) {
      return INT;
    } else if (_values instanceof long[]) {
      return LONG;
    } else if (_values instanceof float[]) {
      return FLOAT;
    } else if (_values instanceof double[]) {
      return DOUBLE;
    } else if (_values instanceof String[]) {
      return STRING;
    } else {
      return NULL;
    }
  }

  /**
   * Set the parameter type.
   * To specify a null (empty) parameter value, set its type to null.
   * @param type parameter type.
   * @exception ParameterConvertException if any of the parameter 
   *  values cannot be converted to the specified type.
   */
  public void setType(int type) {
    if (type==BOOLEAN) {
      _values = valuesAsBooleans(_values,false);
    } else if (type==INT) {
      _values = valuesAsInts(_values,false);
    } else if (type==LONG) {
      _values = valuesAsLongs(_values,false);
    } else if (type==FLOAT) {
      _values = valuesAsFloats(_values,false);
    } else if (type==DOUBLE) {
      _values = valuesAsDoubles(_values,false);
    } else if (type==STRING) {
      _values = valuesAsStrings(_values,false);
    } else {
      _values = null;
    }
  }

  /**
   * Get the parameter set that contains this parameter.
   * @return parent parameter set; null, if the parameter has no
   *  parent, because it was orphaned by removing it from its parent.
   */
  public ParameterSet getParent() {
    return _parent;
  }

  /**
   * Get parameter value as boolean.
   * If the parameter contains an array of values, get the last value.
   * @return parameter value.
   * @exception ParameterConvertException if the parameter value cannot be 
   *  converted to boolean.
   */
  public boolean getBoolean() 
  throws ParameterConvertException {
    boolean[] values = valuesAsBooleans(_values,false);
    return values[values.length-1];
  }

  /**
   * Get parameter value as int.
   * If the parameter contains an array of values, get the last value.
   * @return parameter value.
   * @exception ParameterConvertException if the parameter value cannot be 
   *  converted to int.
   */
  public int getInt() 
  throws ParameterConvertException {
    int[] values = valuesAsInts(_values,false);
    return values[values.length-1];
  }

  /**
   * Get parameter value as long.
   * If the parameter contains an array of values, get the last value.
   * @return parameter value.
   * @exception ParameterConvertException if the parameter value cannot be 
   *  converted to long.
   */
  public long getLong() 
  throws ParameterConvertException {
    long[] values = valuesAsLongs(_values,false);
    return values[values.length-1];
  }

  /**
   * Get parameter value as float.
   * If the parameter contains an array of values, get the last value.
   * @return parameter value.
   * @exception ParameterConvertException if the parameter value cannot be 
   *  converted to float.
   */
  public float getFloat() 
  throws ParameterConvertException {
    float[] values = valuesAsFloats(_values,false);
    return values[values.length-1];
  }

  /**
   * Get parameter value as double.
   * If the parameter contains an array of values, get the last value.
   * @return parameter value.
   * @exception ParameterConvertException if the parameter value cannot be 
   *  converted to double.
   */
  public double getDouble() 
  throws ParameterConvertException {
    double[] values = valuesAsDoubles(_values,false);
    return values[values.length-1];
  }

  /**
   * Get parameter value as String.
   * If the parameter contains an array of values, get the last value.
   * @return parameter value.
   * @exception ParameterConvertException if the parameter value cannot be 
   *  converted to String.
   */
  public String getString() 
  throws ParameterConvertException {
    String[] values = valuesAsStrings(_values,false);
    return values[values.length-1];
  }

  /**
   * Get parameter values as array of booleans.
   * @return parameter values.
   * @exception ParameterConvertException if any of the parameter values 
   * cannot be converted to boolean.
   */
  public boolean[] getBooleans() 
  throws ParameterConvertException {
    return valuesAsBooleans(_values,true);
  }

  /**
   * Get parameter values as array of ints.
   * @return parameter values.
   * @exception ParameterConvertException if any of the parameter values 
   * cannot be converted to int.
   */
  public int[] getInts() 
  throws ParameterConvertException {
    return valuesAsInts(_values,true);
  }

  /**
   * Get parameter values as array of longs.
   * @return parameter values.
   * @exception ParameterConvertException if any of the parameter values 
   * cannot be converted to long.
   */
  public long[] getLongs() 
  throws ParameterConvertException {
    return valuesAsLongs(_values,true);
  }

  /**
   * Get parameter values as array of floats.
   * @return parameter values.
   * @exception ParameterConvertException if any of the parameter values 
   * cannot be converted to float.
   */
  public float[] getFloats() 
  throws ParameterConvertException {
    return valuesAsFloats(_values,true);
  }

  /**
   * Get parameter values as array of doubles.
   * @return parameter values.
   * @exception ParameterConvertException if any of the parameter values 
   * cannot be converted to double.
   */
  public double[] getDoubles() 
  throws ParameterConvertException {
    return valuesAsDoubles(_values,true);
  }

  /**
   * Get parameter values as array of Strings.
   * @return parameter values.
   * @exception ParameterConvertException if any of the parameter values 
   * cannot be converted to String.
   */
  public String[] getStrings() 
  throws ParameterConvertException {
    return valuesAsStrings(_values,true);
  }

  /**
   * Set parameter value as boolean.
   * @param value parameter value.
   */
  public void setBoolean(boolean value) {
    _values = new boolean[1];
    ((boolean[])_values)[0] = value;
  }

  /**
   * Set parameter value as int.
   * @param value parameter value.
   */
  public void setInt(int value) {
    _values = new int[1];
    ((int[])_values)[0] = value;
  }

  /**
   * Set parameter value as long.
   * @param value parameter value.
   */
  public void setLong(long value) {
    _values = new long[1];
    ((long[])_values)[0] = value;
  }

  /**
   * Set parameter value as float.
   * @param value parameter value.
   */
  public void setFloat(float value) {
    _values = new float[1];
    ((float[])_values)[0] = value;
  }

  /**
   * Set parameter value as double.
   * @param value parameter value.
   */
  public void setDouble(double value) {
    _values = new double[1];
    ((double[])_values)[0] = value;
  }

  /**
   * Set parameter value as String.
   * @param value parameter value.
   */
  public void setString(String value) {
    _values = new String[1];
    ((String[])_values)[0] = value;
  }

  /**
   * Set parameter values as an array of booleans.
   * @param values parameter values.
   */
  public void setBooleans(boolean[] values) {
    int length = (values==null)?0:values.length;
    _values = new boolean[length];
    if (length>0) System.arraycopy(values,0,_values,0,length);
  }

  /**
   * Set parameter values as an array of ints.
   * @param values parameter values.
   */
  public void setInts(int[] values) {
    int length = (values==null)?0:values.length;
    _values = new int[length];
    if (length>0) System.arraycopy(values,0,_values,0,length);
  }

  /**
   * Set parameter values as an array of longs.
   * @param values parameter values.
   */
  public void setLongs(long[] values) {
    int length = (values==null)?0:values.length;
    _values = new long[length];
    if (length>0) System.arraycopy(values,0,_values,0,length);
  }

  /**
   * Set parameter values as an array of floats.
   * @param values parameter values.
   */
  public void setFloats(float[] values) {
    int length = (values==null)?0:values.length;
    _values = new float[length];
    if (length>0) System.arraycopy(values,0,_values,0,length);
  }

  /**
   * Set parameter values as an array of doubles.
   * @param values parameter values.
   */
  public void setDoubles(double[] values) {
    int length = (values==null)?0:values.length;
    _values = new double[length];
    if (length>0) System.arraycopy(values,0,_values,0,length);
  }

  /**
   * Set parameter values as an array of Strings.
   * @param values parameter values.
   */
  public void setStrings(String[] values) {
    int length = (values==null)?0:values.length;
    _values = new String[length];
    if (length>0) System.arraycopy(values,0,_values,0,length);
  }

  /**
   * Tests if parameter type is null.
   * @return true, if parameter type is null; false, otherwise.
   */
  public boolean isNull() {
    return (getType()==NULL);
  }

  /**
   * Tests if parameter type is boolean.
   * @return true, if parameter type is boolean; false, otherwise.
   */
  public boolean isBoolean() {
    return (getType()==BOOLEAN);
  }

  /**
   * Tests if parameter type is int.
   * @return true, if parameter type is int; false, otherwise.
   */
  public boolean isInt() {
    return (getType()==INT);
  }

  /**
   * Tests if parameter type is long.
   * @return true, if parameter type is long; false, otherwise.
   */
  public boolean isLong() {
    return (getType()==LONG);
  }

  /**
   * Tests if parameter type is float.
   * @return true, if parameter type is float; false, otherwise.
   */
  public boolean isFloat() {
    return (getType()==FLOAT);
  }

  /**
   * Tests if parameter type is double.
   * @return true, if parameter type is double; false, otherwise.
   */
  public boolean isDouble() {
    return (getType()==DOUBLE);
  }

  /**
   * Tests if parameter type is String.
   * @return true, if parameter type is String; false, otherwise.
   */
  public boolean isString() {
    return (getType()==STRING);
  }

  /**
   * Get a string representation of this parameter.
   * This XML-formatted string represents the parameter name, type, 
   * (optional) units and value(s).
   * @return string representation of this parameter. 
   */
  public String toString() {

    // Indent according to our depth in the parset tree.
    String indent = "";
    for (ParameterSet parent=getParent(); 
         parent!=null; 
         parent=parent.getParent()) {
      indent = indent+"  ";
    }

    // Get par data as strings and encode any special XML entities.
    String name = XmlUtil.quoteAttributeValue(_name);
    String units = XmlUtil.quoteAttributeValue(_units);
    String type = XmlUtil.quoteAttributeValue(getTypeString());
    String[] values = valuesAsStrings(_values,true);
    for (int i=0; i<values.length; ++i) {
      values[i] = XmlUtil.quoteCharacterData(values[i]);
    }

    // Stringify this par.
    StringBuffer sb = new StringBuffer(256);
    sb.append(indent).append("<par name=").append(name);
    sb.append(" type=").append(type);
    if (units!=null) sb.append(" units=").append(units);
    if (isNull() || values.length==0) {
      sb.append("/>\n");
    } else if (values.length==1) {
      sb.append("> ").append(values[0]).append(" </par>\n");
    } else {
      sb.append(">\n");
      for (Object value:values)
        sb.append(indent).append("  ").append(value).append("\n");
      sb.append(indent).append("</par>\n");
    }
    return sb.toString();
  }

  /** 
   * Compares two parameters for equality.
   * Parameters are equal if their names, units, types, and values are equal.
   * @return true, if the parameters are equal; false, otherwise.
   */
  public boolean equals(Object o) {
    if (o==this) 
      return true;
    if (o==null || getClass()!=o.getClass())
      return false;
    Parameter other = (Parameter)o;

    // If names are not equal.
    if (_name==null) {
      if (other._name!=null) 
        return false;
    } else if (!_name.equals(other._name)) {
      return false;
    }

    // If units are not equal.
    if (_units==null) {
      if (other._units!=null) 
        return false;
    } else if (!_units.equals(other._units)) {
      return false;
    }

    // If types are not equal.
    if (getType() != other.getType())
      return false;

    // If numbers of values are not equal.
    int nvalues = countValues();
    if (nvalues!=other.countValues())
      return false;

    // If values are not equal.
    int type = getType();
    if (type==BOOLEAN) {
      boolean[] values = (boolean[])_values;
      boolean[] otherValues = (boolean[])other._values;
      for (int i=0; i<nvalues; ++i) {
        if (values[i]!=otherValues[i])
          return false;
      }
    } else if (type==INT) {
      int[] values = (int[])_values;
      int[] otherValues = (int[])other._values;
      for (int i=0; i<nvalues; ++i) {
        if (values[i]!=otherValues[i])
          return false;
      }
    } else if (type==LONG) {
      long[] values = (long[])_values;
      long[] otherValues = (long[])other._values;
      for (int i=0; i<nvalues; ++i) {
        if (values[i]!=otherValues[i])
          return false;
      }
    } else if (type==FLOAT) {
      float[] values = (float[])_values;
      float[] otherValues = (float[])other._values;
      for (int i=0; i<nvalues; ++i) {
        if (values[i]!=otherValues[i])
          return false;
      }
    } else if (type==DOUBLE) {
      double[] values = (double[])_values;
      double[] otherValues = (double[])other._values;
      for (int i=0; i<nvalues; ++i) {
        if (values[i]!=otherValues[i])
          return false;
      }
    } else if (type==STRING) {
      String[] values = (String[])_values;
      String[] otherValues = (String[])other._values;
      for (int i=0; i<nvalues; ++i) {
        if (!values[i].equals(otherValues[i]))
          return false;
      }
    }
      
    // If everything above was equal.
    return true;
  }

  /** 
   * Computes the hash code of this paramater.
   * The hash code depends on the parameter name, units, type, and values.
   * @return the hash code.
   */
  public int hashCode() {
    String name = (_name!=null)?_name:"name";
    String units = (_units!=null)?_units:"units";
    int type = getType();
    int code = name.hashCode()^units.hashCode()^type;
    int nvalues = countValues();
    if (type==BOOLEAN) {
      boolean[] values = (boolean[])_values;
      for (int i=0; i<nvalues; ++i) {
        code ^= (values[i])?1:0;
      }
    } else if (type==INT) {
      int[] values = (int[])_values;
      for (int i=0; i<nvalues; ++i) {
        code ^= values[i];
      }
    } else if (type==LONG) {
      long[] values = (long[])_values;
      for (int i=0; i<nvalues; ++i) {
        long bits = values[i];
        code ^= (int)bits^(int)(bits>>32);
      }
    } else if (type==FLOAT) {
      float[] values = (float[])_values;
      for (int i=0; i<nvalues; ++i)
        code ^= Float.floatToIntBits(values[i]);
    } else if (type==DOUBLE) {
      double[] values = (double[])_values;
      for (int i=0; i<nvalues; ++i) {
        long bits = Double.doubleToLongBits(values[i]);
        code ^= (int)bits^(int)(bits>>32);
      }
    } else if (type==STRING) {
      String[] values = (String[])_values;
      for (int i=0; i<nvalues; ++i) {
        code ^= values[i].hashCode();
      }
    }
    return code;
  }


  ///////////////////////////////////////////////////////////////////////////
  // Package.

  Parameter(String name, ParameterSet parent) {
    setNameAndParent(name,parent);
  }

  void setParent(ParameterSet parent) {
    _parent = parent;
  }

  void setNameAndParent(String name, ParameterSet parent) {
    _name = name;
    if (_name!=null && _name.equals("")) _name = null;
    _parent = parent;
  }

  Object getValues() {
    int n = 0;
    Object values = null;
    if (_values instanceof boolean[]) {
      n = ((boolean[])_values).length;
      values = new boolean[n];
    } else if (_values instanceof int[]) {
      n = ((int[])_values).length;
      values = new int[n];
    } else if (_values instanceof long[]) {
      n = ((long[])_values).length;
      values = new long[n];
    } else if (_values instanceof float[]) {
      n = ((float[])_values).length;
      values = new float[n];
    } else if (_values instanceof double[]) {
      n = ((double[])_values).length;
      values = new double[n];
    } else if (_values instanceof String[]) {
      n = ((String[])_values).length;
      values = new String[n];
    }
    if (n>0) System.arraycopy(_values,0,values,0,n);
    return values;
  }

  void setValues(Object values) {
    int n = 0;
    _values = null;
    if (values instanceof boolean[]) {
      n = ((boolean[])values).length;
      _values = new boolean[n];
    } else if (values instanceof int[]) {
      n = ((int[])values).length;
      _values = new int[n];
    } else if (values instanceof long[]) {
      n = ((long[])values).length;
      _values = new long[n];
    } else if (values instanceof float[]) {
      n = ((float[])values).length;
      _values = new float[n];
    } else if (values instanceof double[]) {
      n = ((double[])values).length;
      _values = new double[n];
    } else if (values instanceof String[]) {
      n = ((String[])values).length;
      _values = new String[n];
    }
    if (n>0) System.arraycopy(values,0,_values,0,n);
  }

  
  ///////////////////////////////////////////////////////////////////////////
  // Private.

  private String _name;
  private ParameterSet _parent;
  private String _units;
  private Object _values;

  private int countValues() {
    if (getType()==BOOLEAN) {
      return ((boolean[])_values).length;
    } else if (getType()==INT) {
      return ((int[])_values).length;
    } else if (getType()==LONG) {
      return ((long[])_values).length;
    } else if (getType()==FLOAT) {
      return ((float[])_values).length;
    } else if (getType()==DOUBLE) {
      return ((double[])_values).length;
    } else if (getType()==STRING) {
      return ((String[])_values).length;
    } else {
      return 0;
    }
  }

  private static boolean[] valuesAsBooleans(Object values, boolean copy) {
    boolean[] bvalues;
    if (values instanceof boolean[]) {
      bvalues = (boolean[])values;
      if (copy) {
        bvalues = new boolean[bvalues.length];
        System.arraycopy(values,0,bvalues,0,bvalues.length);
      }
    } else {
      try {
        if (values instanceof String[]) {
          String[] svalues = (String[])values;
          bvalues = new boolean[svalues.length];
          for (int i=0; i<svalues.length; ++i) {
            String s = svalues[i].toLowerCase();
            if (s.equals("true")) {
              bvalues[i] = true;
            } else if (s.equals("false")) {
              bvalues[i] = false;
            } else {
              throw new Exception();
            }
          }
        } else if (values==null) {
          bvalues = new boolean[0];
        } else {
          throw new Exception();
        }
      } catch (Exception e) {
        String msg = "Parameter cannot convert " +
          getTypeString(values)+" to boolean.";
        throw new ParameterConvertException(msg);
      }
    }
    return bvalues;
  }

  private static int[] valuesAsInts(Object values, boolean copy) {
    int[] ivalues;
    if (values instanceof int[]) {
      ivalues = (int[])values;
      if (copy) {
        ivalues = new int[ivalues.length];
        System.arraycopy(values,0,ivalues,0,ivalues.length);
      }
    } else {
      try {
        if (values instanceof String[]) {
          String[] svalues = (String[])values;
          ivalues = new int[svalues.length];
          for (int i=0; i<svalues.length; ++i) {
            ivalues[i] = Integer.valueOf(svalues[i]);
          }
        } else if (values==null) {
          ivalues = new int[0];
        } else {
          throw new Exception();
        }
      } catch (Exception e) {
        String msg = "Parameter cannot convert " +
          getTypeString(values)+" to int.";
        throw new ParameterConvertException(msg);
      }
    }
    return ivalues;
  }

  private static long[] valuesAsLongs(Object values, boolean copy) {
    long[] lvalues;
    if (values instanceof long[]) {
      lvalues = (long[])values;
      if (copy) {
        lvalues = new long[lvalues.length];
        System.arraycopy(values,0,lvalues,0,lvalues.length);
      }
    } else {
      try {
        if (values instanceof String[]) {
          String[] svalues = (String[])values;
          lvalues = new long[svalues.length];
          for (int i=0; i<svalues.length; ++i) {
            lvalues[i] = Long.valueOf(svalues[i]);
          }
        } else if (values==null) {
          lvalues = new long[0];
        } else {
          throw new Exception();
        }
      } catch (Exception e) {
        String msg = "Parameter cannot convert " +
          getTypeString(values)+" to long.";
        throw new ParameterConvertException(msg);
      }
    }
    return lvalues;
  }

  private static float[] valuesAsFloats(Object values, boolean copy) {
    float[] fvalues;
    if (values instanceof float[]) {
      fvalues = (float[])values;
      if (copy) {
        fvalues = new float[fvalues.length];
        System.arraycopy(values,0,fvalues,0,fvalues.length);
      }
    } else {
      try {
        if (values instanceof int[]) {
          int[] ivalues = (int[])values;
          fvalues = new float[ivalues.length];
          for (int i=0; i<ivalues.length; ++i) {
            fvalues[i] = (float)ivalues[i];
          }
        } else if (values instanceof double[]) {
          double[] dvalues = (double[])values;
          fvalues = new float[dvalues.length];
          for (int i=0; i<dvalues.length; ++i) {
            fvalues[i] = (float)dvalues[i];
          }
        } else if (values instanceof String[]) {
          String[] svalues = (String[])values;
          fvalues = new float[svalues.length];
          for (int i=0; i<svalues.length; ++i) {
            fvalues[i] = Float.valueOf(svalues[i]);
          }
        } else if (values==null) {
          fvalues = new float[0];
        } else {
          throw new Exception();
        }
      } catch (Exception e) {
        String msg = "Parameter cannot convert " +
          getTypeString(values)+" to float.";
        throw new ParameterConvertException(msg);
      }
    }
    return fvalues;
  }

  private static double[] valuesAsDoubles(Object values, boolean copy) {
    double[] dvalues;
    if (values instanceof double[]) {
      dvalues = (double[])values;
      if (copy) {
        dvalues = new double[dvalues.length];
        System.arraycopy(values,0,dvalues,0,dvalues.length);
      }
    } else {
      try {
        if (values instanceof int[]) {
          int[] ivalues = (int[])values;
          dvalues = new double[ivalues.length];
          for (int i=0; i<ivalues.length; ++i) {
            dvalues[i] = (double)ivalues[i];
          }
        } else if (values instanceof float[]) {
          float[] fvalues = (float[])values;
          dvalues = new double[fvalues.length];
          for (int i=0; i<fvalues.length; ++i) {
            dvalues[i] = (double)fvalues[i];
          }
        } else if (values instanceof String[]) {
          String[] svalues = (String[])values;
          dvalues = new double[svalues.length];
          for (int i=0; i<svalues.length; ++i) {
            dvalues[i] = Double.valueOf(svalues[i]);
          }
        } else if (values==null) {
          dvalues = new double[0];
        } else {
          throw new Exception();
        }
      } catch (Exception e) {
        String msg = "Parameter cannot convert " + 
          getTypeString(values)+" to double.";
        throw new ParameterConvertException(msg);
      }
    }
    return dvalues;
  }

  private static String[] valuesAsStrings(Object values, boolean copy) {
    String[] svalues;
    if (values instanceof String[]) {
      svalues = (String[])values;
      if (copy) {
        svalues = new String[svalues.length];
        System.arraycopy(values,0,svalues,0,svalues.length);
      }
    } else {
      try {
        if (values instanceof boolean[]) {
          boolean[] bvalues = (boolean[])values;
          svalues = new String[bvalues.length];
          for (int i=0; i<bvalues.length; ++i) {
            svalues[i] = (bvalues[i])?"true":"false";
          }
        } else if (values instanceof int[]) {
          int[] ivalues = (int[])values;
          svalues = new String[ivalues.length];
          for (int i=0; i<ivalues.length; ++i) {
            svalues[i] = Integer.toString(ivalues[i]);
          }
        } else if (values instanceof long[]) {
          long[] lvalues = (long[])values;
          svalues = new String[lvalues.length];
          for (int i=0; i<lvalues.length; ++i) {
            svalues[i] = Long.toString(lvalues[i]);
          }
        } else if (values instanceof float[]) {
          float[] fvalues = (float[])values;
          svalues = new String[fvalues.length];
          for (int i=0; i<fvalues.length; ++i) {
            svalues[i] = Float.toString(fvalues[i]);
          }
        } else if (values instanceof double[]) {
          double[] dvalues = (double[])values;
          svalues = new String[dvalues.length];
          for (int i=0; i<dvalues.length; ++i) {
            svalues[i] = Double.toString(dvalues[i]);
          }
        } else if (values==null) {
          svalues = new String[0];
        } else {
          throw new Exception();
        }
      } catch (Exception e) {
        String msg = "Parameter cannot convert " +
          getTypeString(values)+" to String.";
        throw new ParameterConvertException(msg);
      }
    }
    return svalues;
  }

  private String getTypeString() {
    return getTypeString(_values);
  }

  private static String getTypeString(Object values) {
    if (values instanceof boolean[]) {
      return "boolean";
    } else if (values instanceof int[]) {
      return "int";
    } else if (values instanceof long[]) {
      return "long";
    } else if (values instanceof float[]) {
      return "float";
    } else if (values instanceof double[]) {
      return "double";
    } else if (values instanceof String[]) {
      return "string";
    } else {
      return "null";
    }
  }
}
