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
package edu.mines.jtk.ogl;

import javax.media.opengl.GLContext;

import static edu.mines.jtk.ogl.Gl.glDeleteTextures;
import static edu.mines.jtk.ogl.Gl.glGenTextures;
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
 * @version 2006.07.08
 */
public class GlTextureName {

  /**
   * Constructs a texture name in the current OpenGL context.
   * Calls glGenTextures to create one texture object.
   * @exception IllegalStateException if the current OpenGL context is null.
   */
  public GlTextureName() {
    _context = GLContext.getCurrent();
    Check.state(_context!=null,"OpenGL context is not null");
    int[] names = new int[1];
    glGenTextures(1,names,0);
    _name = names[0];
    //System.out.println("GlTextureName: generated name="+_name);
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
    if (_context!=null) {
      GLContext current = GLContext.getCurrent();
      if (_context==current ||
          _context.makeCurrent()==GLContext.CONTEXT_CURRENT) {
        try {
          //System.out.println("dispose: deleting name="+_name);
          int[] names = {_name};
          glDeleteTextures(1,names,0);
        } finally {
          if (_context!=current) {
            _context.release();
            current.makeCurrent();
          }
        }
      }
      _context = null;
      _name = 0;
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
  int _name;
}
