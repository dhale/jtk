package edu.mines.jves.opengl;

public class GlContext {

  public GlContext(long handle) {
  }

  public void makeCurrent() {
  }

  public void swapBuffers() {
    nswapBuffers(_display,_handle);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private long _display;
  private long _handle;
  private long _context;

  private native void nswapBuffers(long display, long handle);

  private native long makeContext(
    int type, long display, long handle);

  ///////////////////////////////////////////////////////////////////////////
  // pointers to OpenGL functions after version 1.1

  // OpenGL 1.2
  long glBlendColor;

  // OpenGL 1.3
  long glActiveTexture;

  // OpenGL 1.4
  long glBlendFuncSeparate;

  // OpenGL 1.5
  long glGenQueries;

  private void init() {
    // OpenGL 1.2
    glBlendColor = getProcAddress("glBlendColor");
    // OpenGL 1.3
    glActiveTexture = getProcAddress("glActiveTexture");
    // OpenGL 1.4
    glBlendFuncSeparate = getProcAddress("glBlendFuncSeparate");
    // OpenGL 1.5
    glGenQueries = getProcAddress("glGenQueries");
  }
  private static native long getProcAddress(String functionName);
}

