/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import java.util.ArrayList;

/**
 * An array-based stack. For internal use only.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.05.23
 */
class ArrayStack<E> extends ArrayList<E> {

  public ArrayStack() {
    super();
  }

  public E push(E e) {
    add(e);
    return e;
  }

  public E pop() {
    return remove(size()-1);
  }

  public E peek() {
    return get(size()-1);
  }
}
