/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic;

import java.awt.*;
import java.util.*;

/**
 * A manager for a set of modes and components. A mode manager handles 
 * activation and deactivation for all modes in its set of modes. It does 
 * this by passing the active state (true or false) to its modes, for each 
 * of the components in its set of components. Typically, when activated,
 * a mode adds input event listeners to the specified components. Then, 
 * when deactivated, such a mode removes those event listeners.
 *
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.12.29
 */
public class ModeManager {

  /**
   * Constructs a mode manager with an empty set of modes.
   */
  public ModeManager() {
  }

  /**
   * Adds the specified component.
   * @param c the component.
   */
  public void add(Component c) {
    _cset.add(c);
  }

  /**
   * Removes the specified component.
   * @param c the component.
   */
  public void remove(Component c) {
    _cset.remove(c);
  }

  ///////////////////////////////////////////////////////////////////////////
  // package

  /**
   * Called by a mode when it is constructed.
   */
  public void add(Mode m) {
    _mset.add(m);
  }

  /**
   * Called by a mode when its action is performed.
   */
  void setActive(Mode mode, boolean active) {

    // If mode active state is unchanged, do nothing.
    if (active==mode.isActive())
      return;

    // If activating an exclusive mode, deactive all other exclusive modes.
    if (active && mode.isExclusive()) {
      for (Mode m : _mset) {
        if (m!=mode && m.isExclusive() && m.isActive())
          setActiveInternal(m,false);
      }
    }

    // Activate or deactivate the mode.
    setActiveInternal(mode,active);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private Set<Mode> _mset = new HashSet<Mode>();
  private Set<Component> _cset = new HashSet<Component>();

  private void setActiveInternal(Mode mode, boolean active) {
    for (Component c : _cset) {
      mode.setActive(c,active);
    }
  }
}

