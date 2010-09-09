/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.util;

/**
 * Exception thrown by Parameter when it cannot convert a
 * parameter value to some specified type.
 * 
 * @see edu.mines.jtk.util.Parameter
 *
 * @author Dave Hale, Colorado School of Mines
 * @version 06/25/1998, 08/24/2006.
 */
public class ParameterConvertException extends RuntimeException {
  ParameterConvertException() {
    super();
  }
  ParameterConvertException(String s) {
    super(s);
  }
}
