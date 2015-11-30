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
package edu.mines.jtk.sgl;

/**
 * An interface implemented by nodes that can be selected. This interface
 * serves as a <em>marker</em> interface, because its methods are implemented 
 * by the abstract base class {@link Node} for all nodes, whether selectable 
 * or not. The behavior of those implementations depends on whether or not
 * the class that extends {@link Node} also implements this marker interface.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.07.10
 */
public interface Selectable {

  /**
   * Determines whether this node is currently selected.
   * @return true, if selected; false, otherwise.
   */
  public boolean isSelected();

  /**
   * Sets the selected state for this node.
   * @param selected true, for selected; false, otherwise.
   */
  public void setSelected(boolean selected);
}
