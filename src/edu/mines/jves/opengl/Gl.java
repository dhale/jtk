package edu.mines.jves.opengl;

public class Gl {

  public static native void glFlush();

  ///////////////////////////////////////////////////////////////////////////
  // OpenGL 1.2

  public static void glBlendColor(
    float red, float green, float blue, float alpha) 
  {
    nglBlendColor(context().glBlendColor,
      red,green,blue,alpha);
  }
  private static native void nglBlendColor(long glBlendColor,
    float red, float green, float blue, float alpha);

  ///////////////////////////////////////////////////////////////////////////
  // private

  private static ThreadLocal _context = new ThreadLocal();
  private static GlContext context() {
    return (GlContext)_context.get();
  }
}
