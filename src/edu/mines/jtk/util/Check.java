/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.util;

/**
 * Facilitates checks for common conditions. Methods in this class throw
 * appropriate exceptions when specified conditions are not satisfied.
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.11.24
 */
public class Check {

  /**
   * Ensures that the specified condition for an argument is true.
   * @param condition the condition.
   * @param message a description of the condition.
   * @exception IllegalArgumentException if the condition is false.
   */
  public static void argument(boolean condition, String message) {
    if (!condition)
      throw new IllegalArgumentException("required condition: "+message);
  }

  /**
   * Ensures that the specified condition of state is true.
   * @param condition the condition.
   * @param message a description of the condition.
   * @exception IllegalStateException if the condition is false.
   */
  public static void state(boolean condition, String message) {
    if (!condition)
      throw new IllegalStateException("required condition: "+message);
  }
}
