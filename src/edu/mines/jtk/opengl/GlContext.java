/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.opengl;

import java.awt.Canvas;

import edu.mines.jtk.util.Check;

/**
 * An OpenGL context. All OpenGL rendering is performed using the current
 * OpenGL context. A thread sets the current context by locking it. After
 * rendering, that thread unlocks the context. At any time, a single thread 
 * may hold the lock to no more than one context. Multiple threads cannot 
 * simultaneously hold the lock to one context. Therefore, contexts should
 * be unlocked sometime after they are locked.
 * <p>
 * To ensure that each call to {@link #lock()} is paired with a call to 
 * {@link #unlock()}, perform the latter in a finally block, as in this 
 * example:
 * <pre><code>
 *   context.lock();
 *   try {
 *     // ...
 *     // render using various OpenGL methods
 *     // ...
 *     context.swapBuffers();
 *   } finally {
 *     context.unlock();
 *   }
 * </code></pre>
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.11.24
 */
public class GlContext {

  /**
   * Constructs an OpenGL context for the specified canvas.
   * @param canvas the canvas.
   */
  public GlContext(Canvas canvas) {
    _peer = makeGlAwtCanvasContext(canvas);
    Check.state(_peer!=0,"successfully created OpenGL context peer");
  }

  /**
   * Locks this context. If another thread has locked this context,
   * this method waits until that lock is released.
   * @exception IllegalStateException if the current thread already 
   *  has another OpenGL context locked.
   */
  public void lock() {
    Check.state(_peer!=0,"this OpenGL context has not been disposed");
    _lock.acquire();
    if (_lock.holds()==1) {
      if (Gl.getContext()!=null) {
        _lock.release();
        Check.state(false,"current thread has no other OpenGL context locked");
      }
      if (!lock(_peer)) {
        _lock.release();
        Check.state(false,"successfully locked OpenGL context peer");
      }
      _locked = true;
      Gl.setContext(this);
      getProcAddresses();
    }
  }

  /**
   * Unlocks this context.
   * @exception IllegalStateException if the current thread does
   *  not already have this OpenGL context locked.
   */
  public void unlock() {
    Check.state(_peer!=0,"this OpenGL context has not been disposed");
    if (_lock.holds()==1) {
      Check.state(Gl.getContext()==this,
        "this OpenGL context is locked in current thread");
      if (!unlock(_peer))
        Check.state(false,"successfully unlocked OpenGL context peer");
      Gl.setContext(null);
      _locked = false;
    }
    _lock.release();
  }

  /**
   * Determines whether the current thread has this context locked.
   * Used only for debugging, not for synchronization.
   * @return true, if locked; false, otherwise.
   */
  public boolean isLocked() {
    return _locked;
  }

  /**
   * Swaps the front and back buffers for this context.
   */
  public void swapBuffers() {
    Check.state(_peer!=0,"this OpenGL context has not been disposed");
    Check.state(Gl.getContext()==this,
      "this OpenGL context is locked in current thread");
    swapBuffers(_peer);
  }

  /**
   * Dispose this context.
   */
  public synchronized void dispose() {
    Check.state(_peer!=0,"this OpenGL context has not been disposed");
    Check.state(!_locked,"this OpenGL context is not locked in any thread");
    killGlContext(_peer);
    _peer = 0;
    _gotProcAddresses = false;
  }

  /**
   * Determines whether this context has been disposed.
   * @return true, if disposed; false, otherwise.
   */
  public boolean isDisposed() {
    return _peer==0;
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

  private long _peer; // C++ peer of this OpenGL context
  private ReentrantLock _lock = new ReentrantLock(); // mutual exclusion lock
  private boolean _gotProcAddresses; // true if got addresses for 1.[2-5]
  private boolean _locked; // true, if this context locked in any thread

  private static native void killGlContext(long peer);
  private static native long makeGlAwtCanvasContext(Canvas canvas);
  private static native boolean lock(long peer);
  private static native boolean unlock(long peer);
  private static native boolean swapBuffers(long peer);

  /**
   * Stripped-down version of Doug Lea's reentrant lock.
   * TODO: replace with JDK 5.0 standard lock?
   */
  private static class ReentrantLock {
    public void acquire() {
      Check.state(!Thread.interrupted(),"thread is not interrupted");
      Thread caller = Thread.currentThread();
      synchronized(this) {
        if (_owner==caller) 
          ++_holds;
        else {
          try {  
            while (_owner != null) 
              wait(); 
            _owner = caller;
            _holds = 1;
          }
          catch (InterruptedException ex) {
            notify();
            Check.state(false,"thread is not interrupted");
          }
        }
      }
    }
    public synchronized void release() {
      Check.state(_owner==Thread.currentThread(),"thread owns the lock");
      if (--_holds == 0) {
        _owner = null;
        notify(); 
      }
    }
    public synchronized long holds() {
      return _holds;
    }
    private Thread _owner = null;
    private long _holds = 0;
  }

  static {
    System.loadLibrary("edu_mines_jtk_opengl");
  }

  ///////////////////////////////////////////////////////////////////////////
  // pointers to OpenGL functions after version 1.1

  // OpenGL 1.2
  long glBlendColor;
  long glBlendEquation;
  long glDrawRangeElements;
  long glColorTable;
  long glColorTableParameterfv;
  long glColorTableParameteriv;
  long glCopyColorTable;
  long glGetColorTable;
  long glGetColorTableParameterfv;
  long glGetColorTableParameteriv;
  long glColorSubTable;
  long glCopyColorSubTable;
  long glConvolutionFilter1D;
  long glConvolutionFilter2D;
  long glConvolutionParameterf;
  long glConvolutionParameterfv;
  long glConvolutionParameteri;
  long glConvolutionParameteriv;
  long glCopyConvolutionFilter1D;
  long glCopyConvolutionFilter2D;
  long glGetConvolutionFilter;
  long glGetConvolutionParameterfv;
  long glGetConvolutionParameteriv;
  long glGetSeparableFilter;
  long glSeparableFilter2D;
  long glGetHistogram;
  long glGetHistogramParameterfv;
  long glGetHistogramParameteriv;
  long glGetMinmax;
  long glGetMinmaxParameterfv;
  long glGetMinmaxParameteriv;
  long glHistogram;
  long glMinmax;
  long glResetHistogram;
  long glResetMinmax;
  long glTexImage3D;
  long glTexSubImage3D;
  long glCopyTexSubImage3D;

  // OpenGL 1.3
  long glActiveTexture;
  long glClientActiveTexture;
  long glMultiTexCoord1d;
  long glMultiTexCoord1dv;
  long glMultiTexCoord1f;
  long glMultiTexCoord1fv;
  long glMultiTexCoord1i;
  long glMultiTexCoord1iv;
  long glMultiTexCoord1s;
  long glMultiTexCoord1sv;
  long glMultiTexCoord2d;
  long glMultiTexCoord2dv;
  long glMultiTexCoord2f;
  long glMultiTexCoord2fv;
  long glMultiTexCoord2i;
  long glMultiTexCoord2iv;
  long glMultiTexCoord2s;
  long glMultiTexCoord2sv;
  long glMultiTexCoord3d;
  long glMultiTexCoord3dv;
  long glMultiTexCoord3f;
  long glMultiTexCoord3fv;
  long glMultiTexCoord3i;
  long glMultiTexCoord3iv;
  long glMultiTexCoord3s;
  long glMultiTexCoord3sv;
  long glMultiTexCoord4d;
  long glMultiTexCoord4dv;
  long glMultiTexCoord4f;
  long glMultiTexCoord4fv;
  long glMultiTexCoord4i;
  long glMultiTexCoord4iv;
  long glMultiTexCoord4s;
  long glMultiTexCoord4sv;
  long glLoadTransposeMatrixf;
  long glLoadTransposeMatrixd;
  long glMultTransposeMatrixf;
  long glMultTransposeMatrixd;
  long glSampleCoverage;
  long glCompressedTexImage3D;
  long glCompressedTexImage2D;
  long glCompressedTexImage1D;
  long glCompressedTexSubImage3D;
  long glCompressedTexSubImage2D;
  long glCompressedTexSubImage1D;
  long glGetCompressedTexImage;

  // OpenGL 1.4
  long glBlendFuncSeparate;
  long glFogCoordf;
  long glFogCoordfv;
  long glFogCoordd;
  long glFogCoorddv;
  long glFogCoordPointer;
  long glMultiDrawArrays;
  long glMultiDrawElements;
  long glPointParameterf;
  long glPointParameterfv;
  long glPointParameteri;
  long glPointParameteriv;
  long glSecondaryColor3b;
  long glSecondaryColor3bv;
  long glSecondaryColor3d;
  long glSecondaryColor3dv;
  long glSecondaryColor3f;
  long glSecondaryColor3fv;
  long glSecondaryColor3i;
  long glSecondaryColor3iv;
  long glSecondaryColor3s;
  long glSecondaryColor3sv;
  long glSecondaryColor3ub;
  long glSecondaryColor3ubv;
  long glSecondaryColor3ui;
  long glSecondaryColor3uiv;
  long glSecondaryColor3us;
  long glSecondaryColor3usv;
  long glSecondaryColorPointer;
  long glWindowPos2d;
  long glWindowPos2dv;
  long glWindowPos2f;
  long glWindowPos2fv;
  long glWindowPos2i;
  long glWindowPos2iv;
  long glWindowPos2s;
  long glWindowPos2sv;
  long glWindowPos3d;
  long glWindowPos3dv;
  long glWindowPos3f;
  long glWindowPos3fv;
  long glWindowPos3i;
  long glWindowPos3iv;
  long glWindowPos3s;
  long glWindowPos3sv;

  // OpenGL 1.5
  long glGenQueries;
  long glDeleteQueries;
  long glIsQuery;
  long glBeginQuery;
  long glEndQuery;
  long glGetQueryiv;
  long glGetQueryObjectiv;
  long glGetQueryObjectuiv;
  long glBindBuffer;
  long glDeleteBuffers;
  long glGenBuffers;
  long glIsBuffer;
  long glBufferData;
  long glBufferSubData;
  long glGetBufferSubData;
  long glMapBuffer;
  long glUnmapBuffer;
  long glGetBufferParameteriv;
  long glGetBufferPointerv;

  private void getProcAddresses() {

    // Do this only once.
    if (_gotProcAddresses)
      return;

    // OpenGL 1.2
    glBlendColor = getProcAddress("glBlendColor");
    glBlendEquation = getProcAddress("glBlendEquation");
    glDrawRangeElements = getProcAddress("glDrawRangeElements");
    glColorTable = getProcAddress("glColorTable");
    glColorTableParameterfv = getProcAddress("glColorTableParameterfv");
    glColorTableParameteriv = getProcAddress("glColorTableParameteriv");
    glCopyColorTable = getProcAddress("glCopyColorTable");
    glGetColorTable = getProcAddress("glGetColorTable");
    glGetColorTableParameterfv = getProcAddress("glGetColorTableParameterfv");
    glGetColorTableParameteriv = getProcAddress("glGetColorTableParameteriv");
    glColorSubTable = getProcAddress("glColorSubTable");
    glCopyColorSubTable = getProcAddress("glCopyColorSubTable");
    glConvolutionFilter1D = getProcAddress("glConvolutionFilter1D");
    glConvolutionFilter2D = getProcAddress("glConvolutionFilter2D");
    glConvolutionParameterf = getProcAddress("glConvolutionParameterf");
    glConvolutionParameterfv = getProcAddress("glConvolutionParameterfv");
    glConvolutionParameteri = getProcAddress("glConvolutionParameteri");
    glConvolutionParameteriv = getProcAddress("glConvolutionParameteriv");
    glCopyConvolutionFilter1D = getProcAddress("glCopyConvolutionFilter1D");
    glCopyConvolutionFilter2D = getProcAddress("glCopyConvolutionFilter2D");
    glGetConvolutionFilter = getProcAddress("glGetConvolutionFilter");
    glGetConvolutionParameterfv = getProcAddress("glGetConvolutionParameterfv");
    glGetConvolutionParameteriv = getProcAddress("glGetConvolutionParameteriv");
    glGetSeparableFilter = getProcAddress("glGetSeparableFilter");
    glSeparableFilter2D = getProcAddress("glSeparableFilter2D");
    glGetHistogram = getProcAddress("glGetHistogram");
    glGetHistogramParameterfv = getProcAddress("glGetHistogramParameterfv");
    glGetHistogramParameteriv = getProcAddress("glGetHistogramParameteriv");
    glGetMinmax = getProcAddress("glGetMinmax");
    glGetMinmaxParameterfv = getProcAddress("glGetMinmaxParameterfv");
    glGetMinmaxParameteriv = getProcAddress("glGetMinmaxParameteriv");
    glHistogram = getProcAddress("glHistogram");
    glMinmax = getProcAddress("glMinmax");
    glResetHistogram = getProcAddress("glResetHistogram");
    glResetMinmax = getProcAddress("glResetMinmax");
    glTexImage3D = getProcAddress("glTexImage3D");
    glTexSubImage3D = getProcAddress("glTexSubImage3D");
    glCopyTexSubImage3D = getProcAddress("glCopyTexSubImage3D");

    // OpenGL 1.3
    glActiveTexture = getProcAddress("glActiveTexture");
    glClientActiveTexture = getProcAddress("glClientActiveTexture");
    glMultiTexCoord1d = getProcAddress("glMultiTexCoord1d");
    glMultiTexCoord1dv = getProcAddress("glMultiTexCoord1dv");
    glMultiTexCoord1f = getProcAddress("glMultiTexCoord1f");
    glMultiTexCoord1fv = getProcAddress("glMultiTexCoord1fv");
    glMultiTexCoord1i = getProcAddress("glMultiTexCoord1i");
    glMultiTexCoord1iv = getProcAddress("glMultiTexCoord1iv");
    glMultiTexCoord1s = getProcAddress("glMultiTexCoord1s");
    glMultiTexCoord1sv = getProcAddress("glMultiTexCoord1sv");
    glMultiTexCoord2d = getProcAddress("glMultiTexCoord2d");
    glMultiTexCoord2dv = getProcAddress("glMultiTexCoord2dv");
    glMultiTexCoord2f = getProcAddress("glMultiTexCoord2f");
    glMultiTexCoord2fv = getProcAddress("glMultiTexCoord2fv");
    glMultiTexCoord2i = getProcAddress("glMultiTexCoord2i");
    glMultiTexCoord2iv = getProcAddress("glMultiTexCoord2iv");
    glMultiTexCoord2s = getProcAddress("glMultiTexCoord2s");
    glMultiTexCoord2sv = getProcAddress("glMultiTexCoord2sv");
    glMultiTexCoord3d = getProcAddress("glMultiTexCoord3d");
    glMultiTexCoord3dv = getProcAddress("glMultiTexCoord3dv");
    glMultiTexCoord3f = getProcAddress("glMultiTexCoord3f");
    glMultiTexCoord3fv = getProcAddress("glMultiTexCoord3fv");
    glMultiTexCoord3i = getProcAddress("glMultiTexCoord3i");
    glMultiTexCoord3iv = getProcAddress("glMultiTexCoord3iv");
    glMultiTexCoord3s = getProcAddress("glMultiTexCoord3s");
    glMultiTexCoord3sv = getProcAddress("glMultiTexCoord3sv");
    glMultiTexCoord4d = getProcAddress("glMultiTexCoord4d");
    glMultiTexCoord4dv = getProcAddress("glMultiTexCoord4dv");
    glMultiTexCoord4f = getProcAddress("glMultiTexCoord4f");
    glMultiTexCoord4fv = getProcAddress("glMultiTexCoord4fv");
    glMultiTexCoord4i = getProcAddress("glMultiTexCoord4i");
    glMultiTexCoord4iv = getProcAddress("glMultiTexCoord4iv");
    glMultiTexCoord4s = getProcAddress("glMultiTexCoord4s");
    glMultiTexCoord4sv = getProcAddress("glMultiTexCoord4sv");
    glLoadTransposeMatrixf = getProcAddress("glLoadTransposeMatrixf");
    glLoadTransposeMatrixd = getProcAddress("glLoadTransposeMatrixd");
    glMultTransposeMatrixf = getProcAddress("glMultTransposeMatrixf");
    glMultTransposeMatrixd = getProcAddress("glMultTransposeMatrixd");
    glSampleCoverage = getProcAddress("glSampleCoverage");
    glCompressedTexImage3D = getProcAddress("glCompressedTexImage3D");
    glCompressedTexImage2D = getProcAddress("glCompressedTexImage2D");
    glCompressedTexImage1D = getProcAddress("glCompressedTexImage1D");
    glCompressedTexSubImage3D = getProcAddress("glCompressedTexSubImage3D");
    glCompressedTexSubImage2D = getProcAddress("glCompressedTexSubImage2D");
    glCompressedTexSubImage1D = getProcAddress("glCompressedTexSubImage1D");
    glGetCompressedTexImage = getProcAddress("glGetCompressedTexImage");

    // OpenGL 1.4
    glBlendFuncSeparate = getProcAddress("glBlendFuncSeparate");
    glFogCoordf = getProcAddress("glFogCoordf");
    glFogCoordfv = getProcAddress("glFogCoordfv");
    glFogCoordd = getProcAddress("glFogCoordd");
    glFogCoorddv = getProcAddress("glFogCoorddv");
    glFogCoordPointer = getProcAddress("glFogCoordPointer");
    glMultiDrawArrays = getProcAddress("glMultiDrawArrays");
    glMultiDrawElements = getProcAddress("glMultiDrawElements");
    glPointParameterf = getProcAddress("glPointParameterf");
    glPointParameterfv = getProcAddress("glPointParameterfv");
    glPointParameteri = getProcAddress("glPointParameteri");
    glPointParameteriv = getProcAddress("glPointParameteriv");
    glSecondaryColor3b = getProcAddress("glSecondaryColor3b");
    glSecondaryColor3bv = getProcAddress("glSecondaryColor3bv");
    glSecondaryColor3d = getProcAddress("glSecondaryColor3d");
    glSecondaryColor3dv = getProcAddress("glSecondaryColor3dv");
    glSecondaryColor3f = getProcAddress("glSecondaryColor3f");
    glSecondaryColor3fv = getProcAddress("glSecondaryColor3fv");
    glSecondaryColor3i = getProcAddress("glSecondaryColor3i");
    glSecondaryColor3iv = getProcAddress("glSecondaryColor3iv");
    glSecondaryColor3s = getProcAddress("glSecondaryColor3s");
    glSecondaryColor3sv = getProcAddress("glSecondaryColor3sv");
    glSecondaryColor3ub = getProcAddress("glSecondaryColor3ub");
    glSecondaryColor3ubv = getProcAddress("glSecondaryColor3ubv");
    glSecondaryColor3ui = getProcAddress("glSecondaryColor3ui");
    glSecondaryColor3uiv = getProcAddress("glSecondaryColor3uiv");
    glSecondaryColor3us = getProcAddress("glSecondaryColor3us");
    glSecondaryColor3usv = getProcAddress("glSecondaryColor3usv");
    glSecondaryColorPointer = getProcAddress("glSecondaryColorPointer");
    glWindowPos2d = getProcAddress("glWindowPos2d");
    glWindowPos2dv = getProcAddress("glWindowPos2dv");
    glWindowPos2f = getProcAddress("glWindowPos2f");
    glWindowPos2fv = getProcAddress("glWindowPos2fv");
    glWindowPos2i = getProcAddress("glWindowPos2i");
    glWindowPos2iv = getProcAddress("glWindowPos2iv");
    glWindowPos2s = getProcAddress("glWindowPos2s");
    glWindowPos2sv = getProcAddress("glWindowPos2sv");
    glWindowPos3d = getProcAddress("glWindowPos3d");
    glWindowPos3dv = getProcAddress("glWindowPos3dv");
    glWindowPos3f = getProcAddress("glWindowPos3f");
    glWindowPos3fv = getProcAddress("glWindowPos3fv");
    glWindowPos3i = getProcAddress("glWindowPos3i");
    glWindowPos3iv = getProcAddress("glWindowPos3iv");
    glWindowPos3s = getProcAddress("glWindowPos3s");
    glWindowPos3sv = getProcAddress("glWindowPos3sv");

    // OpenGL 1.5
    glGenQueries = getProcAddress("glGenQueries");
    glDeleteQueries = getProcAddress("glDeleteQueries");
    glIsQuery = getProcAddress("glIsQuery");
    glBeginQuery = getProcAddress("glBeginQuery");
    glEndQuery = getProcAddress("glEndQuery");
    glGetQueryiv = getProcAddress("glGetQueryiv");
    glGetQueryObjectiv = getProcAddress("glGetQueryObjectiv");
    glGetQueryObjectuiv = getProcAddress("glGetQueryObjectuiv");
    glBindBuffer = getProcAddress("glBindBuffer");
    glDeleteBuffers = getProcAddress("glDeleteBuffers");
    glGenBuffers = getProcAddress("glGenBuffers");
    glIsBuffer = getProcAddress("glIsBuffer");
    glBufferData = getProcAddress("glBufferData");
    glBufferSubData = getProcAddress("glBufferSubData");
    glGetBufferSubData = getProcAddress("glGetBufferSubData");
    glMapBuffer = getProcAddress("glMapBuffer");
    glUnmapBuffer = getProcAddress("glUnmapBuffer");
    glGetBufferParameteriv = getProcAddress("glGetBufferParameteriv");
    glGetBufferPointerv = getProcAddress("glGetBufferPointerv");

    // Do this only once.
    _gotProcAddresses = true;
  }
  private static native long getProcAddress(String functionName);
}
