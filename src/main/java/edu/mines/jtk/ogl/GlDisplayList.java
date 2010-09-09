/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.ogl;

import javax.media.opengl.GLContext;

import static edu.mines.jtk.ogl.Gl.glDeleteLists;
import static edu.mines.jtk.ogl.Gl.glGenLists;
import edu.mines.jtk.util.Check;

/**
 * An OpenGL display list. When constructed, a display list calls
 * glGenLists to generate one or more display lists. When disposed, it 
 * calls glDeleteLists to delete any OpenGL resources bound to those 
 * lists. If not disposed explicitly, a display list will dispose itself 
 * when finalized during garbage collection.
 * <p>
 * This class exists to implement the finalize method and thereby reduce 
 * the likelihood of OpenGL resource leaks. However, it is not foolproof,
 * for two reasons. First, there is no guarantee that a display list will 
 * ever be finalized. Second, to call glDeleteLists, a display list must 
 * lock an OpenGL context, and the only context it knows is the one in which 
 * it was constructed. That context may have been disposed, but the display 
 * list may have been shared in a different unknown context. In this case, 
 * display list resources may be leaked in that unknown context.
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.07.08
 */
public class GlDisplayList {

  /**
   * Constructs a display list in the current OpenGL context.
   * Calls glGenLists to create one display list object.
   * @exception IllegalStateException if the current OpenGL context is null.
   */
  public GlDisplayList() {
    this(1);
  }

  /**
   * Constructs display lists in the current OpenGL context.
   * Calls glGenLists to create the specified number of display list objects.
   * @param range the number of display lists.
   * @exception IllegalStateException if the current OpenGL context is null.
   */
  public GlDisplayList(int range) {
    _context = GLContext.getCurrent();
    Check.state(_context!=null,"OpenGL context is not null");
    _list = glGenLists(range);
    _range = range;
  }

  /**
   * Returns the integer index corresponding to this display list. If more 
   * than one list, this method returns the index of the first list.
   * @return the index; zero, if this display list has been disposed.
   */
  public int list() {
    return _list;
  }

  /**
   * Returns the number of display lists.
   * @return the number of display lists; zero, if disposed.
   */
  public int range() {
    return _range;
  }

  /**
   * Disposes this display list. When practical, this method should be called
   * explicitly. Otherwise, it will be called when this object is finalized
   * during garbage collection.
   */
  public synchronized void dispose() {
    if (_context!=null) {
      GLContext current = GLContext.getCurrent();
      if (_context==current ||
          _context.makeCurrent()==GLContext.CONTEXT_CURRENT) {
        try {
          glDeleteLists(_list,_range);
        } finally {
          if (_context!=current) {
            _context.release();
            current.makeCurrent();
          }
        }
      }
      _context = null;
      _list = 0;
      _range = 0;
    }
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

  GLContext _context;
  int _list;
  int _range;
}
