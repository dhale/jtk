/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
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
