/****************************************************************************
Copyright (c) 2008, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.util;

/**
 * Utilities for manipulating strings of characters.
 * @author Dave Hale, Colorado School of Mines
 */
public class StringUtil {

  /**
   * Removes any trailing zeros from a string representing a float or double.
   * Also removes any insignificant trailing decimal point. For example,
   * converts the string "1.00" to "1"; converts the string "1.2300e-6" 
   * to "1.23e-6".
   * @param s a string representing a float or double.
   * @return the string with insignificant trailing zeros removed.
   */
  public static String removeTrailingZeros(String s) {
    int len = s.length();
    int iend = s.indexOf('e');
    if (iend<0)
      iend = s.indexOf('E');
    if (iend<0)
      iend = len;
    int ibeg = iend;
    if (s.indexOf('.')>0) {
      while (ibeg>0 && s.charAt(ibeg-1)=='0')
        --ibeg;
      if (ibeg>0 && s.charAt(ibeg-1)=='.')
        --ibeg;
    }
    if (ibeg<iend) {
      String sb = s.substring(0,ibeg);
      s = (iend<len)?sb+s.substring(iend,len):sb;
    }
    return s;
  }
}
