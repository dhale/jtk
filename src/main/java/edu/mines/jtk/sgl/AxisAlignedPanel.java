/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

/**
 * An axis-aligned panel is a special child of an axis-aligned frame.
 * Nodes that draw themselves in an axis-aligned frame typically extend 
 * this abstract class, which handles some node responsibilities, such 
 * as picking and computing the node's bounding sphere.
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.06.01
 */
public abstract class AxisAlignedPanel extends Node {

  /**
   * Constructs a panel with null frame.
   */
  public AxisAlignedPanel() {
  }

  /**
   * Constructs a panel with specified frame.
   * @param frame the frame.
   */
  public AxisAlignedPanel(AxisAlignedFrame frame) {
    setFrame(frame);
  }

  /**
   * Gets the frame for this panel.
   * @return the frame; null, if none.
   */
  public AxisAlignedFrame getFrame() {
    return _frame;
  }

  /**
   * Sets the frame for this panel. If the frame is not null and this panel 
   * has a non-null constraint, then this method sets the constraint of its
   * frame accordingly.
   * @param frame the frame; null, if none.
   */
  public void setFrame(AxisAlignedFrame frame) {
    _frame = frame;
    if (_frame!=null) {
      BoxConstraint constraint = getBoxConstraint();
      if (constraint!=null)
        _frame.setBoxConstraint(constraint);
    }
    dirtyBoundingSphere();
    dirtyDraw();
  }

  /**
   * Gets the box constraint for this panel. This implementation simply
   * returns null, for no constraint. Panels that extend this class may
   * override this method to return a more appropriate constraint.
   * @return the box constraint; null, if none.
   */
  public BoxConstraint getBoxConstraint() {
    return null;
  }

  /**
   * Picks this panel. This implementation delegates picking to its frame;
   * or; if not in a frame, this implementation does nothing.
   * <p>
   * Panels that extend this class and that precisely fill their quad 
   * frame when drawn may simply inherit this implementation.
   */
  public void pick(PickContext pc) {
    if (_frame!=null)
      _frame.pickOnFrame(pc);
  }

  ///////////////////////////////////////////////////////////////////////////
  // protected

  /**
   * Computes the bounding sphere for this panel. This implementation
   * delegates this computation to its frame; or, if not in a frame, 
   * this implementation returns an empty bounding sphere.
   * <p>
   * Panels that extend this class typically inherit this implementation,
   * but may of course override it as necessary.
   */
  protected BoundingSphere computeBoundingSphere(boolean finite) {
    if (_frame==null) {
      return new BoundingSphere();
    } else {
      return _frame.computeBoundingSphereOfFrame(finite);
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private AxisAlignedFrame _frame;
}
