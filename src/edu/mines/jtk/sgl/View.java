/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

/**
 * A view of a world.
 * <p>
 * A view has a world-to-view transform. This transform is that part of the
 * OpenGL ModelView transform that depends on the view.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.05.21
 */
public class View {

  private World _world;
  private Matrix44 _worldToView;
}
