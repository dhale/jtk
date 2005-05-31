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
class ArrayStack<E> {

  public E push(E e) {
    _list.add(e);
    return e;
  }

  public E pop() {
    return _list.remove(_list.size()-1);
  }

  public E peek() {
    return _list.get(_list.size()-1);
  }

  public E get(int index) {
    return _list.get(index);
  }

  public int size() {
    return _list.size();
  }

  private ArrayList<E> _list = new ArrayList<E>();
}
