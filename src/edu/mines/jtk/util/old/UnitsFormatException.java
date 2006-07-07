package com.lgc.idh.util;

/**
 * Exception thrown by Units when it detects a bad units definition.
 * 
 * @see com.lgc.idh.util.Units
 *
 * @author Dave Hale
 * @version 1998.07.20
 */
public class UnitsFormatException extends Exception {
  UnitsFormatException() {
    super();
  }
  UnitsFormatException(String s) {
    super(s);
  }
}
