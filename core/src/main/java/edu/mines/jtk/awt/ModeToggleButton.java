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
package edu.mines.jtk.awt;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.*;

/**
 * A toggle button for a mode. When the mode is active, the mode toggle
 * button appears to be selected. 
 * <p>
 * Ordinary toggle buttons constructed with an action (such as a mode) 
 * update their appearance when the action is enabled or disabled. A mode 
 * toggle button does that, and also updates its appearance when the mode 
 * is activated or deactivated.
 *
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.12.30
 */
public class ModeToggleButton extends JToggleButton {
  private static final long serialVersionUID = 1L;
  
  /**
   * Constructs a mode toggle button for the specified mode.
   * @param mode the mode.
   */
  public ModeToggleButton(Mode mode) {
    super(mode);
    setText(null); // ignore mode text (is for menus)
    setMnemonic(0); // ignore mnemonic (is for menus)
    mode.addPropertyChangeListener(new PropertyChangeListener() {
      public void propertyChange(PropertyChangeEvent e) {
        if (e.getPropertyName().equals("active"))
          setSelected((Boolean)e.getNewValue());
      }
    });
  }
}
