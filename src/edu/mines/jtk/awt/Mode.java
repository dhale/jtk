/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.awt;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.image.*;
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
    if (isEnabled() && _active!=active)
      _manager.setActive(this,active);
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
  @Override
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

  /**
   * Sets the cursor for this mode.
   * If not null, the specified cursor is used when this mode is active.
   * @param cursor the cursor; null, if the default cursor should be used.
   */
  public void setCursor(Cursor cursor) {
    _cursor = cursor;
  }

  /**
   * Gets the cursor for this mode.
   * @return the cursor; null, if the default cursor should be used.
   */
  public Cursor getCursor() {
    return _cursor;
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
   * @return the icon.
   */
  protected static Icon loadIcon(Class<?> cls, String res) {
    java.net.URL url = cls.getResource(res);
    return (url!=null)?new ImageIcon(url):null;
  }

  /**
   * Loads a cursor from the specified resource name. The resource
   * with specified name is found relative to the specified class.
   * @param cls the class used to find the resource.
   * @param res the name of the resource that contains the cursor image.
   * @param x the x coordinate of the cursor hot spot
   * @param y the y coordinate of the cursor hot spot
   * @return the cursor.
   */
  protected static Cursor loadCursor(Class<?> cls, String res, int x, int y) {
    java.net.URL url = cls.getResource(res);
    if (url==null)
      return null;
    Toolkit toolkit = Toolkit.getDefaultToolkit();
    Image image = toolkit.getImage(url);
    if (image==null)
      return null;
    image = resizeCursorImage(image);
    Point point = new Point(x,y);
    return toolkit.createCustomCursor(image,point,res);
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
  // package

  /**
   * Called by the mode manager. When changing the active state of this
   * mode, the manager may also change the active state of other modes.
   * @param active true, for active; false, otherwise.
   */
  void setActiveInternal(boolean active) {
    firePropertyChange("active",_active,active);
    _active = active;
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private ModeManager _manager;
  private boolean _active = false;
  private Cursor _cursor = null;

  private static Image resizeCursorImage(Image image) {
    image = new ImageIcon(image).getImage(); // ensure all pixels loaded
    int w = image.getWidth(null); // so we can get width
    int h = image.getHeight(null); // and height
    Dimension size;
    try {
      size = Toolkit.getDefaultToolkit().getBestCursorSize(w,h);
    } catch (HeadlessException e) { // no screen?
      return image;
    }
    if (w==size.width && h==size.height)
      return image;
    w = size.width;
    h = size.height;
    boolean hasAlpha = hasAlpha(image);
    BufferedImage bimage;
    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
    try {
      int transparency = hasAlpha?Transparency.BITMASK:Transparency.OPAQUE;
      GraphicsDevice gs = ge.getDefaultScreenDevice();
      GraphicsConfiguration gc = gs.getDefaultConfiguration();
      bimage = gc.createCompatibleImage(w,h,transparency);
    } catch (HeadlessException e) { // no screen?
      int type = hasAlpha ?
                 BufferedImage.TYPE_INT_ARGB :
                 BufferedImage.TYPE_INT_RGB;
      bimage = new BufferedImage(w,h,type);
    }
    Graphics g = bimage.createGraphics();
    g.drawImage(image,0,0,null);
    g.dispose();
    return bimage;
  }

  private static boolean hasAlpha(Image image) {
    if (image instanceof BufferedImage) {
      BufferedImage bimage = (BufferedImage)image;
      return bimage.getColorModel().hasAlpha();
    }
    PixelGrabber pg = new PixelGrabber(image,0,0,1,1,false);
    try {
      pg.grabPixels();
    } catch (InterruptedException e) {
      return true;
    }
    ColorModel cm = pg.getColorModel();
    return cm.hasAlpha();
  }
}

