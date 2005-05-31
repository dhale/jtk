/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import java.util.*;

/**
 * A set of OpenGL states.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.05.31
 */
public class StateSet implements State {

  public void add(State state) {
    _states.put(state.getClass(),state);
    _attributeBits |= state.getAttributeBits();
  }

  public void remove(State state) {
    _states.remove(state.getClass());
    _attributeBits &= ~state.getAttributeBits();
  }

  public Iterator<State> getStates() {
    return _states.values().iterator();
  }

  public void apply() {
    Iterator<State> is = getStates();
    while (is.hasNext()) {
      State s = is.next();
      s.apply();
    }
  }

  public int getAttributeBits() {
    return _attributeBits;
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private Map<Class,State> _states = new HashMap<Class,State>();
  private int _attributeBits = 0;
}
