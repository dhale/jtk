/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import edu.mines.jtk.awt.Mode;
import edu.mines.jtk.awt.ModeManager;

/**
 * A mode for selecting and dragging nodes.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.07.08
 */
public class SelectDragMode extends Mode {
  private static final long serialVersionUID = 1L;

  /**
   * Constructs a select-drag mode with specified manager.
   * @param modeManager the mode manager for this mode.
   */
  public SelectDragMode(ModeManager modeManager) {
    super(modeManager);
    setName("Select");
    Class<SelectDragMode> cls = SelectDragMode.class;
    setIcon(loadIcon(cls,"SelectDragIcon16.png"));
    setCursor(loadCursor(cls,"SelectDragCursor16.png",1,1));
    setMnemonicKey(KeyEvent.VK_S);
    setAcceleratorKey(KeyStroke.getKeyStroke(KeyEvent.VK_S,0));
    setShortDescription("Select/drag");
  }

  ///////////////////////////////////////////////////////////////////////////
  // protected
  
  protected void setActive(Component component, boolean active) {
    if ((component instanceof ViewCanvas)) {
      if (active) {
        component.addMouseListener(_ml);
      } else {
        component.removeMouseListener(_ml);
        _selecting = false;
        _selectable = null;
        _dragable = null;
        _dragContext = null;
      }
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private ViewCanvas _canvas; // canvas when mouse pressed
  private View _view; // view when mouse pressed; null, if none
  private World _world; // world when mouse pressed; null, if none
  private PickResult _pickResult; // pick result when mouse pressed
  private Selectable _selectable; // used iff selecting
  private Dragable _dragable; // used iff dragging
  private DragContext _dragContext; // non-null iff dragging
  private boolean _selecting; // true iff mouse moves too little for drag

  private MouseListener _ml = new MouseAdapter() {

    public void mousePressed(MouseEvent e) {

      // Initially, assume we are selecting, not dragging.
      _selecting = true;

      // Pick and look in the result for dragable and selectable nodes.
      _pickResult = pick(e);
      if (_pickResult!=null) {
        _selectable = _pickResult.getSelectableNode();
        _dragable = _pickResult.getDragableNode();
      }

      // Remember the canvas, view, and world.
      _canvas = (ViewCanvas)e.getSource();
      _view = _canvas.getView();
      if (_view!=null)
        _world = _view.getWorld();

      // Begin listening for mouse movement.
      _canvas.addMouseMotionListener(_mml);
    }
      
    public void mouseReleased(MouseEvent e) {

      // If dragging, end the drag.
      if (_dragable!=null && _dragContext!=null) {
        _dragable.dragEnd(_dragContext);
      }
      
      // Else if selecting (or deselecting), ...
      else if (_selecting) {

        // If control select, toggle selection of the picked node.
        if (e.isControlDown() || e.isAltDown()) { // Alt/Option for Mac
          if (_selectable!=null)
            _selectable.setSelected(!_selectable.isSelected());
        }

        // Else if shift select, extend the selection.
        else if (e.isShiftDown()) {
          if (_selectable!=null)
            _selectable.setSelected(true);
        }

        // Else, select the picked node, if any, after deselecting others.
        else {
          if (_selectable!=null) {
            _world.clearSelectedExcept(_selectable);
            _selectable.setSelected(true);
          } else {
            if (_world!=null)
              _world.clearSelected();
          }
        }
      }

      // No longer dragging or selecting.
      _dragable = null;
      _dragContext = null;
      _selectable = null;
      _selecting = false;

      // End listening for mouse movement.
      _canvas.removeMouseMotionListener(_mml);
    }
  };

  private MouseMotionListener _mml = new MouseMotionAdapter() {

    public void mouseDragged(MouseEvent e) {

      // See if mouse has moved too much for selecting.
      _selecting = _dragContext==null;
      if (_selecting && _pickResult!=null) {
        Point3 pp = _pickResult.getPointPixel();
        Point3 pd = new Point3(e.getX(),e.getY(),pp.z);
        if (pp.distanceTo(pd)>=2.0)
          _selecting = false;
      }

      // If (1) not selecting, and (2) the picked node is dragable, and 
      // (3) we are not yet dragging, then initiate dragging.
      if (!_selecting && _dragable!=null && _dragContext==null) {
        _dragContext = new DragContext(_pickResult);
        _dragable.dragBegin(_dragContext);
      }

      // If we are now dragging, update the drag context and drag.
      if (_dragable!=null && _dragContext!=null) {
        _dragContext.update(e);
        _dragable.drag(_dragContext);
      }
    }
  };

  private PickResult pick(MouseEvent event) {
    ViewCanvas canvas = (ViewCanvas)event.getSource();
    //canvas.addMouseMotionListener(_mml); ??
    View view = canvas.getView();
    if (view==null)
      return null;
    World world = view.getWorld();
    if (world==null)
      return null;
    PickContext pc = new PickContext(event);
    world.pickApply(pc);
    PickResult pickResult = pc.getClosest();
    if (pickResult!=null) {
      Point3 pointLocal = pickResult.getPointLocal();
      Point3 pointWorld = pickResult.getPointWorld();
      System.out.println("Pick");
      System.out.println("  local="+pointLocal);
      System.out.println("  world="+pointWorld);
    } else {
      System.out.println("Pick nothing");
    }
    return pickResult;
  }
}

/*
Design of selectable and dragable:

  dragable and selectable are independent interfaces
    nodes may implement dragable, selectable, neither, or both
      can drag without selecting
      can select without dragging
    dragging requires a mouse (a drag context)
    selecting requires no mouse or context
      e.g., may select a node from a list of nodes
    when selected, a node may reveal handles,
      which may be used for editing, and
      are typically dragable, but
      are typically not selectable
    selected nodes typically highlight themselves, perhaps 
      by displaying handles (if editable), or
      by changing their rendering in some other way

  selectable nodes can have selectable children and parents
    selecting a node does not cause children and/or parents to be selected

  multiple selections are possible
    by default, selection of one node causes deselection of all others
    shift-click to extend selection set
    control-click to add/subtract node to/from current selection set

  each world has a selected set of nodes
    when a branch is added/removed to/from a group in a world,
      the branch is searched to update the world's selected set

  to select (or de-select) a node,
    press and release mouse 
      without dragging significantly (less than two pixels)
    call setSelected

  to drag a node,
    press mouse on the node
    drag mouse significantly (at least two pixels)
      call drawBegin with new drag context
      call drag with current drag context
    continue dragging mouse
      call drag with current drag context
    release mouse anywhere
      call dragEnd with final drag context
  
  a significant drag is two or more pixels in any direction
    prevents inadvertant dragging when selecting is intended
*/
