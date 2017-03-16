package edu.mines.jtk.sgl;

import com.jogamp.opengl.util.awt.TextRenderer;
import edu.mines.jtk.util.MathPlus;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Annotation extends Handle {

  public enum Alignment {
    NW
  }

  public Annotation(Point3 p) {
    super(p);
    _p = p;
    this.addChild(_an);
  }

  public Annotation(double x, double y, double z) {
    super (x,y,z);
    _p = new Point3(x,y,z);
    this.addChild(_an);
  }

  public void setFont(Font font) {
    _font = font;
    updateTextRenderer();
  }

  public void setText(String text) {
    _text = text;
  }

  public String getText() {
    return _text;
  }

  ////////////////////////////////////////////////////////////////////////////
  // private

  private static class AnnotationText extends Node {
    AnnotationText() { }

    protected void draw(DrawContext dc) {
      _renderer.begin3DRendering();
      _renderer.setColor(_color);
      _renderer.draw(_text,0,0);
      _renderer.end3DRendering();
    }
    protected BoundingSphere computeBoundingSphere(boolean finite) {
      return new BoundingSphere(0,0,0,24);
    }
  }

  @Override
  protected Matrix44 computeTransform(TransformContext tc) {
    Matrix44 mat = super.computeTransform(tc);
    mat.timesEquals(tc.getViewToLocal());
    return mat;
  }

  private void updateTextRenderer() {
    _renderer = new TextRenderer(_font);
  }

  private static Point3 _p;
  private static AnnotationText _an = new AnnotationText();
  private static int _size = 24;
  private static Font _font = new Font("SansSerif", Font.BOLD,_size);
  private static TextRenderer _renderer = new TextRenderer(_font);
  private static String _text = "Hello World";
  private static Color _color = Color.WHITE;

}
