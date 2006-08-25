package edu.mines.jtk.util;

/**
 * Exception thrown when the format of a parameter set is not valid.
 *
 * @author Dave Hale, Colorado School of Mines
 * @version 06/25/1998, 08/24/2006.
 */
public class ParameterSetFormatException extends RuntimeException {
  ParameterSetFormatException() {
    super();
  }
  ParameterSetFormatException(String s) {
    super(s);
  }
}
