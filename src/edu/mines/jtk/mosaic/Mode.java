/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * An abstract mode of interaction.
 * <p>
 * A mode can be activated or deactivated, by setting its active state.
 * An active mode responds to input events in some mode-specific manner.
 * An inactive mode ignores all input events.
 * <p> 
 * A mode is an action, so it can be used to construct toggle buttons and 
 * menu items. A mode, like any action, may be enabled or disabled. While 
 * enabled, a mode may be active or inactive. While disabled, a mode is 
 * inactive, and cannot be activated.
 * <p>
 * A mode has a manager, which coordinates the activation of modes that may
 * be mutually exclusive. When an exclusive mode is activated, the mode's 
 * manager first deactivates any other exclusive modes, thereby ensuring 
 * that no more than one exclusive mode is active at any time. By default, 
 * modes are exclusive, but this property may be overridden.
 *
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.12.29
 */
public abstract class Mode extends AbstractAction {

  /**
   * Activates or deactivates this mode. If this mode is not enabled,
   * this method does nothing.
   * @param active true, to activate; false, to deactivate.
   */
  public void setActive(boolean active) {
    if (isEnabled() && _active!=active) {
      Boolean oldValue = Boolean.valueOf(_active);
      Boolean newValue = Boolean.valueOf(active);
      firePropertyChange("active",oldValue,newValue);
      _manager.setActive(this,active);
      _active = active;
    }
  }

  /**
   * Determines whether this mode is active.
   * @return true, if active; false, if inactive.
   */
  public boolean isActive() {
    return _active;
  }

  /**
   * Determines whether or not this mode is exclusive. Exclusive modes
   * cannot coexist with other exclusive modes that have the same manager.
   * This implementation simply returns true. Non-exclusive modes should
   * override this method to return false.
   * @return true, if exclusive; false, otherwise.
   */
  public boolean isExclusive() {
    return true;
  }

  /**
   * Toggles the active state of this mode.
   * @param event the action event (ignored).
   */
  public void actionPerformed(ActionEvent event) {
    setActive(!_active);
  }

  // Override base class implementation. Disabled modes cannot be active.
  public void setEnabled(boolean enabled) {
    if (!enabled && isActive())
      setActive(false);
    super.setEnabled(enabled);
  }

  /**
   * Sets the name (text) for this mode.
   * Used for mode menu items.
   * <p>
   * Typically, this method is called by constructors in classes that 
   * extend this abstract base class.
   * @param name the name.
   */
  public void setName(String name) {
    putValue(Action.NAME,name);
  }

  /**
   * Sets the icon for this mode.
   * Used for mode toggle buttons.
   * <p>
   * Typically, this method is called by constructors in classes that 
   * extend this abstract base class.
   * @param icon the icon.
   */
  public void setIcon(Icon icon) {
    putValue(Action.SMALL_ICON,icon);
  }

  /**
   * Sets the mnemonic key for this mode.
   * Used for mode menu items.
   * <p>
   * Typically, this method is called by constructors in classes that 
   * extend this abstract base class.
   * @param mk the mnemonic key; e.g., KeyEvent.VK_K.
   */
  public void setMnemonicKey(int mk) {
    putValue(Action.MNEMONIC_KEY,new Integer(mk));
  }

  /**
   * Sets the accelerator key stroke for this mode.
   * <p>
   * Typically, this method is called by constructors in classes that 
   * extend this abstract base class.
   * @param ak the accelerator key stroke.
   */
  public void setAcceleratorKey(KeyStroke ak) {
    putValue(Action.ACCELERATOR_KEY,ak);
  }

  /**
   * Sets the short description for this mode.
   * Used in tool tips for mode menu items and toggle buttons.
   * <p>
   * Typically, this method is called by constructors in classes that 
   * extend this abstract base class.
   * @param sd the short description.
   */
  public void setShortDescription(String sd) {
    putValue(Action.SHORT_DESCRIPTION,sd);
  }

  /**
   * Sets the long description for this mode.
   * <p>
   * Typically, this method is called by constructors in classes that 
   * extend this abstract base class.
   * @param ld the long description.
   */
  public void setLongDescription(String ld) {
    putValue(Action.LONG_DESCRIPTION,ld);
  }

  ///////////////////////////////////////////////////////////////////////////
  // protected

  /**
   * Constructs a mode with specified manager, name, and icon.
   * @param manager the manager.
   */
  protected Mode(ModeManager manager) {
    manager.add(this);
    _manager = manager;
  }

  /**
   * Loads an icon from the specified resource name. The resource
   * with specified name is found relative to the specified class.
   * @param cls the class used to find the resource.
   * @param res the name of the resource that contains the icon.
   */
  protected static Icon loadIcon(Class cls, String res) {
    java.net.URL url = cls.getResource(res);
    return (url!=null)?new ImageIcon(url):null;
  }
  
  /**
   * Activates or deactivates this mode for the specified component. 
   * Typically, in their implementations of this method, modes add or 
   * remove input event listeners, when the mode is applicable to the 
   * specified component or type of component.
   * <p>
   * This method should not be called directly. The mode's manager calls 
   * this method for all of its components. Implementations of this method
   * should do nothing for components for which the mode is inapplicable.
   * @param component the component for which to enable the mode.
   * @param active true, to activate; false, to deactivate.
   */
  protected abstract void setActive(Component component, boolean active);

  ///////////////////////////////////////////////////////////////////////////
  // private

  private ModeManager _manager;
  private boolean _active = false;
}

