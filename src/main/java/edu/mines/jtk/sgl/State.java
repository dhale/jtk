/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

/**
 * OpenGL state, a collection of OpenGL state attributes.
 * <p>
 * Nodes in a scene graph can set OpenGL attributes by calling OpenGL 
 * functions explicitly. Alternatively, these attributes can be set by 
 * applying a state class that implements this interface. This second 
 * alternative offers two advantages.
 * <p>
 * First, classes that implement this interface define standard collections 
 * of OpenGL state attributes, with a convenient interface for getting and 
 * setting those attributes. For example, color attributes can be set by 
 * passing a {@link java.awt.Color}.
 * <p>
 * Second, states can be associated with nodes and shared among multiple 
 * nodes in a scene graph. Then, changes to any state's attributes are 
 * reflected in all nodes that share that state.
 * <p>
 * Classes that implement this interface have fields that correspond to 
 * OpenGL state attributes. States have methods that set and unset the 
 * values of these attribute fields. A state applies itself by calling 
 * relevant OpenGL functions for all attribute fields that are set. A 
 * state does not call OpenGL functions for fields that are not set.
 * <p>
 * States also have methods that determine whether or not an attribute 
 * field has been set and methods that get the values of a field. For 
 * attribute fields not set, these get methods return OpenGL default 
 * values.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.05.31
 */
public interface State {

  /**
   * Applies this OpenGL state. This method makes OpenGL state consistent 
   * with any attribute fields in this state that are currently set.
   */
  public void apply();

  /**
   * Gets the OpenGL attribute bits for this state. 
   * These bits indicate what OpenGL state is changed by the method 
   * {@link #apply()}. If these bits are passed to glPushAttrib before 
   * calling {@link #apply()}, then glPopAttrib will restore any OpenGL 
   * state that is changed by that method.
   */
  public int getAttributeBits();
}
