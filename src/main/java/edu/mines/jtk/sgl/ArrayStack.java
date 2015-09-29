/****************************************************************************
Copyright 2004, Colorado School of Mines and others.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
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
