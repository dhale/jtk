/****************************************************************************
Copyright 2006, Colorado School of Mines and others.
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
