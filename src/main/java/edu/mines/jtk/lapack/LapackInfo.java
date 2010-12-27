/****************************************************************************
Copyright (c) 2010, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.lapack;

import org.netlib.util.intW;

/**
 * Info output parameter for most LAPACK methods.
 * For internal use only.
 * @author Dave Hale, Colorado School of Mines
 * @version 2010.12.26
 */
class LapackInfo extends intW {

  LapackInfo() {
    super(0);
  }

  void check(String name) {
    if (val<0) {
      throw new IllegalArgumentException(
        "LAPACK "+name+": error in argument number "+(-val));
    }
  }
  
  int get(String name) {
    check(name);
    return val;
  }
}
