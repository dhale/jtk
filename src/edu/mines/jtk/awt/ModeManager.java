/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.awt;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

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
   * @param m the mode.
   */
  void add(Mode m) {
    _mset.add(m);
  }

  /**
   * Called by a mode when its action is performed.
   * @param mode the mode.
   * @param active true, for active; false, otherwise.
   */
  void setActive(Mode mode, boolean active) {

    // If mode active state is unchanged, do nothing.
    if (active==mode.isActive())
      return;

    // If activating an exclusive mode, deactivate any other exclusive modes.
    // If we deactivate another node, remember it, so that we can reactivate
    // it later.
    Mode modeDeactivated = null;
    if (active && mode.isExclusive()) {
      for (Mode m : _mset) {
        if (m!=mode && m.isExclusive() && m.isActive()) {
          setActiveInternal(m,false);
          modeDeactivated = m;
          break;
        }
      }
    }

    // Activate or deactivate the mode.
    setActiveInternal(mode,active);

    // If an exclusive mode was deactivated, reactivate the most recently 
    // active exclusive mode. This enables a quick toggle between two modes.
    if (!active && mode.isExclusive() && _modeDeactivated!=null) {
      setActiveInternal(_modeDeactivated,true);
    }

    // Remember any mode that was deactivated during this call.
    _modeDeactivated = modeDeactivated;
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private Set<Mode> _mset = new HashSet<Mode>();
  private Set<Component> _cset = new HashSet<Component>();
  private Mode _modeDeactivated;

  private void setActiveInternal(Mode mode, boolean active) {
    Cursor cursor = (active)?mode.getCursor():null;
    if (cursor==null)
      cursor = Cursor.getDefaultCursor();
    mode.setActiveInternal(active);
    for (Component c : _cset) {
      c.setCursor(cursor);
      mode.setActive(c,active);
    }
  }
}

