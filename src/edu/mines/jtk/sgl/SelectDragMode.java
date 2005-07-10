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

import edu.mines.jtk.gui.*;

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
    Class cls = SelectDragMode.class;
    setIcon(loadIcon(cls,"resources/SelectDragIcon16.png"));
    setCursor(loadCursor(cls,"resources/SelectDragCursor16.png",1,1));
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
      }
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private ViewCanvas _canvas; // the canvas
  private View _view; // the view
  private World _world; // the world
  private Selectable _node; // the selected node; null, if none
  private PickResult _pickResult; // pick result; null, if none

  private MouseListener _ml = new MouseAdapter() {
    public void mousePressed(MouseEvent e) {
      doSelect(e);
    }
    public void mouseReleased(MouseEvent e) {
    }
  };

  private MouseMotionListener _mml = new MouseMotionAdapter() {
    public void mouseDragged(MouseEvent e) {
    }
  };

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

  to select (or de-select) a node,
    press and release mouse 
      without dragging significantly (less than two pixels)
    call setSelected

  multiple selections are possible
    by default, selection of one node causes deselection of all others
    shift-click to extend selection set
    control-click to add/subtract node to/from current selection set

  each world has a selected set of nodes
    when a branch is added/removed to/from a group in a world,
      the branch is searched to update the world's selected set

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

  private void doSelect(MouseEvent event) {
    _canvas = (ViewCanvas)event.getSource();
    _canvas.addMouseMotionListener(_mml);
    _view = _canvas.getView();
    _world = _view.getWorld();
    PickContext pc = new PickContext(event);
    _world.pickApply(pc);
    _pickResult = pc.getClosest();
    if (_pickResult!=null) {
      Point3 pointLocal = _pickResult.getPointLocal();
      Point3 pointWorld = _pickResult.getPointWorld();
      System.out.println("Pick");
      System.out.println("  local="+pointLocal);
      System.out.println("  world="+pointWorld);
      Selectable node = _pickResult.getSelectableNode();
      if (node!=null) {
        _node = node;
        if (event.isControlDown()) {
          node.setSelected(!node.isSelected(),false);
        } else {
          node.setSelected(true,true);
        }
      } else {
        _node = null;
      }
    } else {
      System.out.println("Pick nothing");
    }
  }
}

