/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.opengl;

import static edu.mines.jtk.opengl.Gl.glDeleteTextures;
import static edu.mines.jtk.opengl.Gl.glGenTextures;

import edu.mines.jtk.util.Check;

/**
 * An OpenGL texture name. When constructed, a texture name calls
 * glGenTextures to generate a single texture name. When disposed, it 
 * calls glDeleteTextures to delete any OpenGL resources bound to that 
 * name. If not disposed explicitly, a texture name will dispose itself 
 * when finalized during garbage collection.
 * <p>
 * This class exists to implement the finalize method and thereby reduce 
 * the likelihood of OpenGL resource leaks. However, it is not foolproof,
 * for two reasons. First, there is no guarantee that a texture name will 
 * ever be finalized. Second, to call glDeleteTextures, a texture name must 
 * lock an OpenGL context, and the only context it knows is the one in which 
 * it was constructed. That context may have been disposed, but the texture 
 * name may have been shared in a different unknown context. In this case, 
 * texture resources may be leaked in that unknown context.
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.05.24
 */
public class GlTextureName {

  /**
   * Constructs a texture name in the current OpenGL context.
   * Calls glGenTextures to create one texture object.
   * @exception IllegalStateException if the current OpenGL context is null.
   */
  public GlTextureName() {
    _context = Gl.getContext();
    Check.state(_context!=null,"OpenGL context is not null");
    int[] names = new int[1];
    glGenTextures(1,names);
    _name = names[0];
  }

  /**
   * Returns the integer name corresponding to this texture name.
   * @return the name; zero, if this texture name has been disposed.
   */
  public int name() {
    return _name;
  }

  /**
   * Disposes this texture name. When practical, this method should be called
   * explicitly. Otherwise, it will be called when this object is finalized
   * during garbage collection.
   */
  public synchronized void dispose() {
    if (_context!=null && !_context.isDisposed()) {
      _context.lock();
      try {
        int[] names = {_name};
        glDeleteTextures(1,names);
      } finally {
        _context.unlock();
      }
    }
    _context = null;
    _name = 0;
  }

  ///////////////////////////////////////////////////////////////////////////
  // protected

  protected void finalize() throws Throwable {
    try {
      dispose();
    } finally {
      super.finalize();
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  GlContext _context;
  int _name;
}
