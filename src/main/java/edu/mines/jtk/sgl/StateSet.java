/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import java.awt.Color;
import java.util.*;

import static edu.mines.jtk.ogl.Gl.*;

/**
 * A set of OpenGL states. State sets can be associated with nodes in
 * a scene graph. During the draw process for a node, these states are 
 * applied in the method {@link Node#drawBegin(DrawContext)}, before
 * the method {@link Node#draw(DrawContext)} is called.
 * <p>
 * If two states of the same class with the same attributes are added
 * to a state set, then the order in which those states are applied
 * is undefined. Generally, 
 * Nothing prevents two states of the same class from being added to
 * the same state set. Howev
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.05.31
 */
public class StateSet implements State {

  /**
   * Returns a new state set with color, light model, and material states.
   * The specified color is used only when per-vertex colors are not used.
   * The light model state is set for two-sided lighting. The material 
   * state is set with color material ambient and diffuse, specular color 
   * white, and shininess set to 100.
   * <p>
   * This method exists only to provide a simple way to construct a commonly 
   * used state set. It does nothing that cannot be accomplished (more 
   * tediously) by constructing a state set and adding each of its states 
   * using other methods.
   * @param color the color to be set.
   * @return the state set.
   */
  public static StateSet forTwoSidedShinySurface(Color color) {
    StateSet ss = new StateSet();
    if (color!=null) {
      ColorState cs = new ColorState();
      cs.setColor(color);
      ss.add(cs);
    }
    LightModelState lms = new LightModelState();
    lms.setTwoSide(true);
    ss.add(lms);
    MaterialState ms = new MaterialState();
    ms.setColorMaterial(GL_AMBIENT_AND_DIFFUSE);
    ms.setSpecular(Color.white);
    ms.setShininess(100.0f);
    ss.add(ms);
    return ss;
  }

  /**
   * Adds the specified state to this set.
   * @param state the state.
   */
  public void add(State state) {
    _states.add(state);
  }

  /**
   * Removes the specified state from this set.
   * @param state the state.
   */
  public void remove(State state) {
    _states.remove(state);
  }

  /**
   * Determines whether this set contains a state of the specified class.
   * @param stateClass the state class.
   * @return true; if this state contains such a state; false, otherwise.
   */
  public boolean contains(Class<?> stateClass) {
    return find(stateClass)!=null;
  }

  /**
   * Finds a state in this set of the specified class.
   * @param stateClass the state class.
   * @return the state; null, if the set contains no such state.
   */
  public State find(Class<?> stateClass) {
    for (State s : _states) {
      if (s.getClass().equals(stateClass))
        return s;
    }
    return null;
  }

  /**
   * Gets an iterator for all states in this set.
   * @return the iterator.
   */
  public Iterator<State> getStates() {
    return _states.iterator();
  }

  /**
   * Gets the blend state in this set, if present.
   * @return the blend state; null, if none.
   */
  public BlendState getBlendState() {
    return (BlendState)find(BlendState.class);
  }

  /**
   * Gets the color state in this set, if present.
   * @return the color state; null, if none.
   */
  public ColorState getColorState() {
    return (ColorState)find(ColorState.class);
  }

  /**
   * Gets the light model state in this set, if present.
   * @return the light model state; null, if none.
   */
  public LightModelState getLightModelState() {
    return (LightModelState)find(LightModelState.class);
  }

  /**
   * Gets the line state in this set, if present.
   * @return the line state; null, if none.
   */
  public LineState getLineState() {
    return (LineState)find(LineState.class);
  }

  /**
   * Gets the material state in this set, if present.
   * @return the material state; null, if none.
   */
  public MaterialState getMaterialState() {
    return (MaterialState)find(MaterialState.class);
  }

  /**
   * Gets the point state in this set, if present.
   * @return the point state; null, if none.
   */
  public PointState getPointState() {
    return (PointState)find(PointState.class);
  }

  /**
   * Gets the polygon state in this set, if present.
   * @return the polygon state; null, if none.
   */
  public PolygonState getPolygonState() {
    return (PolygonState)find(PolygonState.class);
  }

  /**
   * Applies all states in this set.
   */
  public void apply() {
    for (State s : _states)
      s.apply();
  }

  /**
   * Gets the combined attribute bits for all states in this set.
   * @return the attribute bits.
   */
  public int getAttributeBits() {
    int attributeBits = 0;
    for (State state : _states)
      attributeBits |= state.getAttributeBits();
    return attributeBits;
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private Set<State> _states = new HashSet<State>();
}
