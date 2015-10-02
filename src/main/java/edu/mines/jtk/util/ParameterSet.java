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

import java.io.*;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * A parameter set - a collection of named parameters and parameter subsets.
 * A parameter set forms a tree in which the parameters represent leaf nodes
 * and parameter subsets represent branches.
 *
 * @see edu.mines.jtk.util.Parameter
 *
 * @author Dave Hale, Colorado School of Mines
 * @version 06/25/1998, 08/24/2006.
 */
public class ParameterSet implements Cloneable, Externalizable {

  ///////////////////////////////////////////////////////////////////////////
  // Public.

  /**
   * Construct an empty nameless root parameter set.
   */
  public ParameterSet() {
  }

  /**
   * Construct an empty named root parameter set.
   * @param name parameter set name.
   */
  public ParameterSet(String name) {
    setNameAndParent(name,null);
  }

  /**
   * Clone this parameter set.
   * The clone will be an orphan; its parent parameter set will be null.
   * @return a clone of this parameter set.
   */
  public Object clone() throws CloneNotSupportedException {
    try {
      ParameterSet ps = (ParameterSet)super.clone();
      ps._parent = null;
      ps._pars = new LinkedHashMap<String, Parameter>();
      ps._parsets = new LinkedHashMap<String, ParameterSet>();
      return ps.replaceWith(this);
    } catch (CloneNotSupportedException e) {
      throw new InternalError();
    }
  }

  /**
   * Replace the contents of this parameter set with a copy of
   * the contents of the specified parameter set.
   * Do not change this parameter set's parent.
   * @param parset the parameter set with contents that will replace
   *  those in this parameter set.
   * @return this parameter set, with contents replaced.
   */
  public ParameterSet replaceWith(ParameterSet parset) {
    if (parset==this) return this;
    setName(parset.getName());
    clear();
    Iterator<Parameter> pi = parset.getParameters();
    while (pi.hasNext()) {
      pi.next().copyTo(this);
    }
    Iterator<ParameterSet> psi = parset.getParameterSets();
    while (psi.hasNext()) {
      psi.next().copyTo(this);
    }
    return this;
  }

  /**
   * Get the parameter set name.
   * @return parameter set name.
   */
  public String getName() {
    return _name;
  }

  /**
   * Set the parameter set name.
   * @param name parameter set name.
   *  If the parameter set has a parent parameter set, ignore a null name.
   */
  public void setName(String name) {
    moveTo(_parent,name);
  }

  /**
   * Get a parameter.
   * @param name name of the parameter to get.
   * @return parameter; null, if the parameter set does not contain a
   *  parameter with the specified name.
   */
  public Parameter getParameter(String name) {
    return _pars.get(name);
  }

  /**
   * Get a parameter subset.
   * @param name name of the subset to get.
   * @return subset; null, if the parameter set does not contain a
   *  subset with the specified name.
   */
  public ParameterSet getParameterSet(String name) {
    return _parsets.get(name);
  }

  /**
   * Add a new parameter to this parameter set.
   * If this parameter set already contains a parameter or subset
   * with the specified name, replace that parameter or subset with
   * with the new parameter.
   * @param name name of the parameter to add.
   * @return parameter; null, if name is null.
   */
  public Parameter addParameter(String name) {
    if (name==null) return null;
    Parameter par = new Parameter(name);
    insert(name,par);
    return par;
  }

  /**
   * Add a new parameter subset to this parameter set.
   * If this parameter set already contains a parameter or subset
   * with the specified name, replace that parameter or subset with
   * with the new parameter subset.
   * @param name name of the parameter subset to add.
   * @return parameter subset; null, if name is null.
   */
  public ParameterSet addParameterSet(String name) {
    if (name==null) return null;
    ParameterSet parset = new ParameterSet(name,null);
    insert(name,parset);
    return parset;
  }

  /**
   * Get the boolean value of a named parameter in this parameter set.
   * @param name name of the parameter.
   * @param defaultValue default value returned if this parameter set 
   *  does not contain a parameter with the specified name.
   * @return parameter value or default value.
   * @exception ParameterConvertException if the parameter value cannot be 
   *  converted to boolean.
   */
  public boolean getBoolean(String name, boolean defaultValue) 
  throws ParameterConvertException {
    Parameter par = getParameter(name);
    return (par!=null)?par.getBoolean():defaultValue;
  }

  /**
   * Get the int value of a named parameter in this parameter set.
   * @param name name of the parameter.
   * @param defaultValue default value returned if this parameter set 
   *  does not contain a parameter with the specified name.
   * @return parameter value or default value.
   * @exception ParameterConvertException if the parameter value cannot be 
   *  converted to int.
   */
  public int getInt(String name, int defaultValue) 
  throws ParameterConvertException {
    Parameter par = getParameter(name);
    return (par!=null)?par.getInt():defaultValue;
  }

  /**
   * Get the long value of a named parameter in this parameter set.
   * @param name name of the parameter.
   * @param defaultValue default value returned if this parameter set 
   *  does not contain a parameter with the specified name.
   * @return parameter value or default value.
   * @exception ParameterConvertException if the parameter value cannot be 
   *  converted to long.
   */
  public long getLong(String name, long defaultValue) 
  throws ParameterConvertException {
    Parameter par = getParameter(name);
    return (par!=null)?par.getLong():defaultValue;
  }

  /**
   * Get the float value of a named parameter in this parameter set.
   * @param name name of the parameter.
   * @param defaultValue default value returned if this parameter set 
   *  does not contain a parameter with the specified name.
   * @return parameter value or default value.
   * @exception ParameterConvertException if the parameter value cannot be 
   *  converted to float.
   */
  public float getFloat(String name, float defaultValue) 
  throws ParameterConvertException {
    Parameter par = getParameter(name);
    return (par!=null)?par.getFloat():defaultValue;
  }

  /**
   * Get the double value of a named parameter in this parameter set.
   * @param name name of the parameter.
   * @param defaultValue default value returned if this parameter set 
   *  does not contain a parameter with the specified name.
   * @return parameter value or default value.
   * @exception ParameterConvertException if the parameter value cannot be 
   *  converted to double.
   */
  public double getDouble(String name, double defaultValue) 
  throws ParameterConvertException {
    Parameter par = getParameter(name);
    return (par!=null)?par.getDouble():defaultValue;
  }

  /**
   * Get the String value of a named parameter in this parameter set.
   * @param name name of the parameter.
   * @param defaultValue default value returned if this parameter set 
   *  does not contain a parameter with the specified name.
   * @return parameter value or default value.
   * @exception ParameterConvertException if the parameter value cannot be 
   *  converted to String.
   */
  public String getString(String name, String defaultValue) 
  throws ParameterConvertException {
    Parameter par = getParameter(name);
    return (par!=null)?par.getString():defaultValue;
  }

  /**
   * Get the boolean values of a named parameter in this parameter set.
   * @param name name of the parameter.
   * @param defaultValues default values returned if this parameter set 
   *  does not contain a parameter with the specified name.
   * @return parameter values or default values.
   * @exception ParameterConvertException if the parameter values cannot be 
   *  converted to boolean.
   */
  public boolean[] getBooleans(String name, boolean[] defaultValues) 
  throws ParameterConvertException {
    Parameter par = getParameter(name);
    return (par!=null)?par.getBooleans():defaultValues;
  }

  /**
   * Get the int values of a named parameter in this parameter set.
   * @param name name of the parameter.
   * @param defaultValues default values returned if this parameter set 
   *  does not contain a parameter with the specified name.
   * @return parameter values or default values.
   * @exception ParameterConvertException if the parameter values cannot be 
   *  converted to int.
   */
  public int[] getInts(String name, int[] defaultValues) 
  throws ParameterConvertException {
    Parameter par = getParameter(name);
    return (par!=null)?par.getInts():defaultValues;
  }

  /**
   * Get the long values of a named parameter in this parameter set.
   * @param name name of the parameter.
   * @param defaultValues default values returned if this parameter set 
   *  does not contain a parameter with the specified name.
   * @return parameter values or default values.
   * @exception ParameterConvertException if the parameter values cannot be 
   *  converted to long.
   */
  public long[] getLongs(String name, long[] defaultValues) 
  throws ParameterConvertException {
    Parameter par = getParameter(name);
    return (par!=null)?par.getLongs():defaultValues;
  }

  /**
   * Get the float values of a named parameter in this parameter set.
   * @param name name of the parameter.
   * @param defaultValues default values returned if this parameter set 
   *  does not contain a parameter with the specified name.
   * @return parameter values or default values.
   * @exception ParameterConvertException if the parameter values cannot be 
   *  converted to float.
   */
  public float[] getFloats(String name, float[] defaultValues) 
  throws ParameterConvertException {
    Parameter par = getParameter(name);
    return (par!=null)?par.getFloats():defaultValues;
  }

  /**
   * Get the double values of a named parameter in this parameter set.
   * @param name name of the parameter.
   * @param defaultValues default values returned if this parameter set 
   *  does not contain a parameter with the specified name.
   * @return parameter values or default values.
   * @exception ParameterConvertException if the parameter values cannot be 
   *  converted to double.
   */
  public double[] getDoubles(String name, double[] defaultValues) 
  throws ParameterConvertException {
    Parameter par = getParameter(name);
    return (par!=null)?par.getDoubles():defaultValues;
  }

  /**
   * Get the String values of a named parameter in this parameter set.
   * @param name name of the parameter.
   * @param defaultValues default values returned if this parameter set 
   *  does not contain a parameter with the specified name.
   * @return parameter values or default values.
   * @exception ParameterConvertException if the parameter values cannot be 
   *  converted to String.
   */
  public String[] getStrings(String name, String[] defaultValues) 
  throws ParameterConvertException {
    Parameter par = getParameter(name);
    return (par!=null)?par.getStrings():defaultValues;
  }

  /**
   * Get the units of a named parameter in this parameter set.
   * @param name name of the parameter.
   * @param defaultUnits default units returned if this parameter set 
   *  does not contain a parameter with the specified name.
   * @return parameter units or default units.
   */
  public String getUnits(String name, String defaultUnits) {
    Parameter par = getParameter(name);
    return (par!=null)?par.getUnits():defaultUnits;
  }

  /**
   * Set the boolean value of a named parameter in this parameter set.
   * If this parameter set does not contain the named parameter, add
   * the parameter to this set before setting its value.
   * @param name name of the parameter.
   * @param value parameter value.
   */
  public void setBoolean(String name, boolean value) {
    Parameter par = getParameter(name);
    if (par==null) par = addParameter(name);
    par.setBoolean(value);
  }

  /**
   * Set the int value of a named parameter in this parameter set.
   * If this parameter set does not contain the named parameter, add
   * the parameter to this set before setting its value.
   * @param name name of the parameter.
   * @param value parameter value.
   */
  public void setInt(String name, int value) {
    Parameter par = getParameter(name);
    if (par==null) par = addParameter(name);
    par.setInt(value);
  }

  /**
   * Set the long value of a named parameter in this parameter set.
   * If this parameter set does not contain the named parameter, add
   * the parameter to this set before setting its value.
   * @param name name of the parameter.
   * @param value parameter value.
   */
  public void setLong(String name, long value) {
    Parameter par = getParameter(name);
    if (par==null) par = addParameter(name);
    par.setLong(value);
  }

  /**
   * Set the float value of a named parameter in this parameter set.
   * If this parameter set does not contain the named parameter, add
   * the parameter to this set before setting its value.
   * @param name name of the parameter.
   * @param value parameter value.
   */
  public void setFloat(String name, float value) {
    Parameter par = getParameter(name);
    if (par==null) par = addParameter(name);
    par.setFloat(value);
  }

  /**
   * Set the double value of a named parameter in this parameter set.
   * If this parameter set does not contain the named parameter, add
   * the parameter to this set before setting its value.
   * @param name name of the parameter.
   * @param value parameter value.
   */
  public void setDouble(String name, double value) {
    Parameter par = getParameter(name);
    if (par==null) par = addParameter(name);
    par.setDouble(value);
  }

  /**
   * Set the String value of a named parameter in this parameter set.
   * If this parameter set does not contain the named parameter, add
   * the parameter to this set before setting its value.
   * @param name name of the parameter.
   * @param value parameter value.
   */
  public void setString(String name, String value) {
    Parameter par = getParameter(name);
    if (par==null) par = addParameter(name);
    par.setString(value);
  }

  /**
   * Set the boolean values of a named parameter in this parameter set.
   * If this parameter set does not contain the named parameter, add
   * the parameter to this set before setting its values.
   * @param name name of the parameter.
   * @param values parameter values.
   */
  public void setBooleans(String name, boolean[] values) {
    Parameter par = getParameter(name);
    if (par==null) par = addParameter(name);
    par.setBooleans(values);
  }

  /**
   * Set the int values of a named parameter in this parameter set.
   * If this parameter set does not contain the named parameter, add
   * the parameter to this set before setting its values.
   * @param name name of the parameter.
   * @param values parameter values.
   */
  public void setInts(String name, int[] values) {
    Parameter par = getParameter(name);
    if (par==null) par = addParameter(name);
    par.setInts(values);
  }

  /**
   * Set the long values of a named parameter in this parameter set.
   * If this parameter set does not contain the named parameter, add
   * the parameter to this set before setting its values.
   * @param name name of the parameter.
   * @param values parameter values.
   */
  public void setLongs(String name, long[] values) {
    Parameter par = getParameter(name);
    if (par==null) par = addParameter(name);
    par.setLongs(values);
  }

  /**
   * Set the float values of a named parameter in this parameter set.
   * If this parameter set does not contain the named parameter, add
   * the parameter to this set before setting its values.
   * @param name name of the parameter.
   * @param values parameter values.
   */
  public void setFloats(String name, float[] values) {
    Parameter par = getParameter(name);
    if (par==null) par = addParameter(name);
    par.setFloats(values);
  }

  /**
   * Set the double values of a named parameter in this parameter set.
   * If this parameter set does not contain the named parameter, add
   * the parameter to this set before setting its values.
   * @param name name of the parameter.
   * @param values parameter values.
   */
  public void setDoubles(String name, double[] values) {
    Parameter par = getParameter(name);
    if (par==null) par = addParameter(name);
    par.setDoubles(values);
  }

  /**
   * Set the String values of a named parameter in this parameter set.
   * If this parameter set does not contain the named parameter, add
   * the parameter to this set before setting its values.
   * @param name name of the parameter.
   * @param values parameter values.
   */
  public void setStrings(String name, String[] values) {
    Parameter par = getParameter(name);
    if (par==null) par = addParameter(name);
    par.setStrings(values);
  }

  /**
   * Set the units of a named parameter in this parameter set.
   * If this parameter set does not contain the named parameter, add
   * the parameter to this set before setting its units.
   * @param name name of the parameter.
   * @param units parameter units.
   */
  public void setUnits(String name, String units) {
    Parameter par = getParameter(name);
    if (par==null) par = addParameter(name);
    par.setUnits(units);
  }

  /**
   * Copy this parameter.set to the specified parent parameter set 
   * without changing its name.
   * @param parent the parameter set into which to copy this parameter set;
   *  the parent of the destination parameter set.
   *  If the parent is null, the destination parameter set will be an orphan.
   * @return the destination parameter set.
   */
  public ParameterSet copyTo(ParameterSet parent) {
    return copyTo(parent,getName());
  }

  /**
   * Copy this parameter.set to the specified parent parameter set 
   * while changing its name.
   * @param parent the parameter set into which to copy this parameter set;
   *  the parent of the destination parameter set.
   *  If the parent is null, the destination parameter set will be an orphan.
   * @param name the destination parameter set name.
   * @return the destination parameter set.
   */
  public ParameterSet copyTo(ParameterSet parent, String name) {
    if (_parent==parent && _name==name) return this;

    // Create a new destination ParameterSet to hold the copy.
    // Do not make it a child of the parent, yet, because
    // the parent may be one of our children, and we must
    // avoid endless recursion.
    ParameterSet ps = new ParameterSet(name);

    // Copy our pars.
    Iterator<Parameter> pi = getParameters();
    while (pi.hasNext()) {
      pi.next().copyTo(ps);
    }

    // Copy our parsets.
    Iterator<ParameterSet> psi = getParameterSets();
    while (psi.hasNext()) {
      psi.next().copyTo(ps);
    }

    // Insert the destination parset into the specified parent.
    if (parent!=null) parent.insert(name,ps);
    return ps;
  }

  /**
   * Move this parameter set to the specified parent parameter set 
   * without changing its name.
   * @param parent the parameter set into which to move this parameter set;
   *  the parent of the destination parameter set.
   *  If the parent is null, the destination parameter set will be an orphan.
   * @return the destination parameter set.
   */
  public ParameterSet moveTo(ParameterSet parent) {
    return moveTo(parent,getName());
  }

  /**
   * Move this parameter set to the specified parent parameter set 
   * while changing its name.
   * @param parent the parameter set into which to move this parameter set;
   *  the parent of the destination parameter set.
   *  If the parent is null, the destination parameter set will be an orphan.
   *  The parent cannot be this parameter set or a subset of this parameter set.
   * @param name the destination parameter set name.
   * @return the destination parameter set.
   */
  public ParameterSet moveTo(ParameterSet parent, String name) {
    if (_parent==parent && _name==name) return this;

    // Parent cannot be this parset or a child of this parset.
    for (ParameterSet ps=parent; ps!=null; ps=ps.getParent()) {
      if (ps==this) {
        throw new IllegalArgumentException(
          "ParameterSet.moveTo: specified parent \""+parent.getName()+"\"" +
          " cannot be this parameter set \""+getName()+"\"" +
          " or a subset of this parameter set.");
      }
    }

    // OK to move this parset.
    if (_parent!=null) _parent.remove(this);
    if (parent!=null) {
      parent.insert(name,this);
    } else {
      setNameAndParent(name,null);
    }

    return this;
  }

  /**
   * Remove this parameter set from its parent parameter set.
   * In other words, orphan this parameter set.
   * If this parameter set is already an orphan, do nothing.
   */
  public void remove() {
    if (_parent!=null) _parent.remove(this);
  }

  /**
   * Remove a named parameter or subset from this parameter set.
   * If this parameter set does not contain a parameter or subset
   * with the specified name, do nothing.
   * @param name name of the parameter or subset to remove.
   */
  public void remove(String name) {
    Parameter par;
    ParameterSet parset;
    if ((par=getParameter(name))!=null) {
      par.remove();
    } else if ((parset=getParameterSet(name))!=null) {
      parset.remove();
    }
  }

  /**
   * Count the parameters in this parameter set.
   * @return number of parameters in this parameter set.
   */
  public int countParameters() {
    return _pars.size();
  }

  /**
   * Count the parameter subsets in this parameter set.
   * @return number of subsets in this parameter set.
   */
  public int countParameterSets() {
    return _parsets.size();
  }

  /**
   * Clear this parameter set by removing all its parameters
   * and parameter subsets.
   */
  public void clear() {

    // Orphan our children.
    Iterator<Parameter> pi = getParameters();
    while (pi.hasNext()) {
      pi.next().setParent(null);
    }
    Iterator<ParameterSet> psi = getParameterSets();
    while (psi.hasNext()) {
      psi.next().setParent(null);
    }

    // Clear our maps.
    _pars.clear();
    _parsets.clear();
  }

  /**
   * Get the parameter set that contains this parameter set.
   * @return parent parameter set; null, if the parameter set is
   *  a root parameter set with no parent, perhaps because it was
   *  orphaned by removing it from its parent.
   */
  public ParameterSet getParent() {
    return _parent;
  }

  /**
   * Gets an iterator for the parameters in this parameter set.
   * @return the iterator.
   */
  public Iterator<Parameter> getParameters() {
    return _pars.values().iterator();
  }

  /**
   * Gets an iterator for the parameter sets in this parameter set.
   * @return the iterator.
   */
  public Iterator<ParameterSet> getParameterSets() {
    return _parsets.values().iterator();
  }

  /**
   * Replace this parameter set with that represented in the specified
   * XML-formatted string (the same format written by method toString).
   * @param s XML-formatted string representation of parameter set.
   * @exception ParameterSetFormatException if the string is not properly 
   *  formatted.
   */
  public void fromString(String s) 
    throws ParameterSetFormatException {
    clear();
    ParameterSetParser psp = new ParameterSetParser();
    psp.parse(new BufferedReader(new StringReader(s)),this);
  }

  /**
   * Get a string representation of this parameter set.
   * This XML-formatted string represents the parameter name and
   * all parameters and subsets contained in this parameter set.
   * @return string representation of this parameter set. 
   */
  public String toString() {

    // Indent according to our depth in the parset tree.
    String indent = "";
    for (ParameterSet parent=getParent(); 
         parent!=null; 
         parent=parent.getParent()) {
      indent = indent+"  ";
    }

    // Begin this set.
    StringBuffer sb = new StringBuffer(256);
    String name = XmlUtil.quoteAttributeValue((_name!=null)?_name:"");
    sb.append(indent).append("<parset name=").append(name).append(">\n");

    // Stringify our parameters.
    Iterator<Parameter> pi = getParameters();
    while (pi.hasNext()) {
      Parameter p = pi.next();
      sb.append(p.toString());
    }

    // Stringify our parameter subsets.
    Iterator<ParameterSet> psi = getParameterSets();
    while (psi.hasNext()) {
      ParameterSet ps = psi.next();
      sb.append(ps.toString());
    }

    // End this set.
    sb.append(indent).append("</parset>\n");
    return sb.toString();
  }

  /**
   * Compares two parameter sets for equality.
   * Parameter sets are equal if their names and any contained 
   * parameters and parameter sets are equal.
   * @return true, if the parameter sets are equal; false, otherwise.
   */
  public boolean equals(Object o) {
    if (o==this)
      return true;
    if (o==null || getClass()!=o.getClass())
      return false;
    ParameterSet other = (ParameterSet)o;

    // If names are not equal.
    if (_name==null) {
      if (other._name!=null) 
        return false;
    } else if (!_name.equals(other._name)) {
      return false;
    }

    // If numbers of parameters are not equal.
    int npars = countParameters();
    if (npars!=other.countParameters()) 
      return false;

    // If numbers of parameter sets are not equal.
    int nparsets = countParameterSets();
    if (nparsets!=other.countParameterSets()) 
      return false;

    // If parameters (sorted by name) are not equal.
    if (npars>0) {
      Parameter[] these = new Parameter[npars];
      Parameter[] those = new Parameter[npars];
      int i = 0;
      Iterator<Parameter> pi = getParameters();
      while (pi.hasNext()) {
        these[i++] = pi.next();
      }
      i = 0;
      pi = other.getParameters();
      while (pi.hasNext()) {
        those[i++] = pi.next();
      }
      sortParametersByName(these);
      sortParametersByName(those);
      for (i=0; i<npars; ++i) {
        if (!these[i].equals(those[i]))
          return false;
      }
    }

    // If parameter sets (sorted by name) are not equal.
    if (nparsets>0) {
      ParameterSet[] these = new ParameterSet[nparsets];
      ParameterSet[] those = new ParameterSet[nparsets];
      int i = 0;
      Iterator<ParameterSet> psi = getParameterSets();
      while (psi .hasNext()) {
        these[i++] = psi.next();
      }
      i = 0;
      psi = other.getParameterSets();
      while (psi.hasNext()) {
        those[i++] = psi.next();
      }
      sortParameterSetsByName(these);
      sortParameterSetsByName(those);
      for (i=0; i<nparsets; ++i) {
        if (!these[i].equals(those[i])) 
          return false;
      }
    }

    // If everything was equal.
    return true;
  }

  /**
   * Computes the hash code of this parameter set.
   * @return the hash code.
   */
  public int hashCode() {
    String name = (_name!=null)?_name:"name";
    int code = name.hashCode();
    Iterator<Parameter> pars = getParameters();
    while (pars.hasNext()) {
      code ^= pars.next().hashCode();
    }
    Iterator<ParameterSet> parsets = getParameterSets();
    while (parsets.hasNext()) {
      code ^= parsets.next().hashCode();
    }
    return code;
  }

  /**
   * Serializes this parameter set by writing its XML string 
   * representation to the specified object output.
   * @param out the object output to which to write this parameter set.
   * @exception IOException if any I/O exception occurs.
   */
  public void writeExternal(ObjectOutput out) throws IOException
  {
    out.writeUTF(toString());
  }

  /**
   * Restores this parameter set by reading its XML string 
   * representation from the specified object input.
   * @param in the object input from which to read this parameter set.
   * @exception IOException if any I/exception occurs. 
   * @exception ClassNotFoundException if the class for an object being 
   *  restored cannot be found.
   * @exception ParameterSetFormatException if the XML string representation
   *  of this parameter set is not properly formatted.
   */
  public void readExternal(ObjectInput in) 
    throws IOException, ClassNotFoundException, ParameterSetFormatException
  {
    String s = in.readUTF();
    fromString(s);
  }

  ///////////////////////////////////////////////////////////////////////////
  // Package.
  
  void remove(Parameter par) {
    if (par==null) return;
    _pars.remove(par.getName());
    par.setParent(null);
  }

  void insert(String name, Parameter par) {
    if (name==null || par==null) return;
    par.remove();
    remove(name);
    _pars.put(name,par);
    par.setNameAndParent(name,this);
  }

  ///////////////////////////////////////////////////////////////////////////
  // Private.

  private String _name;
  private ParameterSet _parent;
  private LinkedHashMap<String, Parameter> _pars = 
    new LinkedHashMap<String, Parameter>(8);
  private LinkedHashMap<String, ParameterSet> _parsets = 
    new LinkedHashMap<String, ParameterSet>(8);

  private ParameterSet(String name, ParameterSet parent) {
    setNameAndParent(name,parent);
  }

  private void setParent(ParameterSet parent) {
    _parent = parent;
  }

  private void setNameAndParent(String name, ParameterSet parent) {
    _name = name;
    if (_name!=null && _name.equals("")) _name = null;
    _parent = parent;
  }

  private void remove(ParameterSet parset) {
    if (parset==null) return;
    _parsets.remove(parset.getName());
    parset.setParent(null);
  }

  private void insert(String name, ParameterSet parset) {
    if (name==null || parset==null) return;
    parset.remove();
    remove(name);
    _parsets.put(name,parset);
    parset.setNameAndParent(name,this);
  }

  private static void swap(Object[] v, int i, int j) {
    Object temp = v[i];
    v[i] = v[j];
    v[j] = temp;
  }

  private static void qsortParameters(Parameter[] v, int left, int right) {
    if (left>=right) return;
    swap(v,left,(left+right)/2);
    int last = left;
    for (int i=left+1; i<=right; ++i)
      if (v[i].getName().compareTo(v[left].getName())<0)
        swap(v,++last,i);
    swap(v,left,last);
    qsortParameters(v,left,last-1);
    qsortParameters(v,last+1,right);
  }

  private static void qsortParameterSets(ParameterSet[] v, 
                                         int left, int right) {
    if (left>=right) return;
    swap(v,left,(left+right)/2);
    int last = left;
    for (int i=left+1; i<=right; ++i)
      if (v[i].getName().compareTo(v[left].getName())<0)
        swap(v,++last,i);
    swap(v,left,last);
    qsortParameterSets(v,left,last-1);
    qsortParameterSets(v,last+1,right);
  }

  private static void sortParametersByName(Parameter[] v) {
    qsortParameters(v,0,v.length-1);
  }

  private static void sortParameterSetsByName(ParameterSet[] v) {
    qsortParameterSets(v,0,v.length-1);
  }
}
