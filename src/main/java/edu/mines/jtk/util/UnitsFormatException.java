/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.util;

/**
 * Exception thrown by Units for a malformed units definition.
 * @see edu.mines.jtk.util.Units
 *
 * @author Dave Hale, Colorado School of Mines
 * @version 1998.07.20, 2006.07.27
 */
public class UnitsFormatException extends Exception {
  UnitsFormatException() {
    super();
  }
  UnitsFormatException(String s) {
    super(s);
  }
}
