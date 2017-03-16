/****************************************************************************
 Copyright 2017, Colorado School of Mines and others.
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

import com.jogamp.opengl.util.awt.TextRenderer;

import java.awt.*;
import java.awt.geom.Rectangle2D;

import static edu.mines.jtk.util.MathPlus.sqrt;

/**
 * An annotation. An annotation is fixed-scale text that anchors to a point
 * in space and always faces the camera regardless of the scene's rotation.
 *
 * @author Chris Engelsma
 * @version 2017.03.15
 */
public class Annotation extends Handle {


  /**
   * The alignment of this annotation relative to a point in space (anchor).
   *
   * The default alignment is to the EAST (right) of the anchor.
   */
  public enum Alignment {
    NORTH,
    SOUTH,
    EAST,
    WEST
  }

  /**
   * Constructs an empty annotation at a point in space.
   *
   * Note: The coordinate is in the world context.
   * @param p a point.
   */
  public Annotation(Point3 p) {
    this(p,"");
  }

  /**
   * Constructs an empty annotation at a point in space.
   *
   * Note: The coordinate is in the world context.
   * @param x a x-coordinate.
   * @param y a y-coordinate.
   * @param z a z-coordinate.
   */
  public Annotation(float x, float y, float z) {
    this(new Point3(x,y,z));
  }

  /**
   * Constructs an annotation at a point in space.
   *
   * Note: The coordinate is in the world context.
   * @param x a x-coordinate.
   * @param y a y-coordinate.
   * @param z a z-coordinate.
   * @param text the text for this annotation.
   */
  public Annotation(float x, float y, float z, String text) {
    this(new Point3(x,y,z),text);
  }

  /**
   * Constructs an annotation at a point in space.
   *
   * Note: The coordinate is in the world context.
   * @param p a point.
   * @param text the text for this annotation.
   */
  public Annotation(Point3 p, String text) {
    super(p);
    _text = text;
    this.addChild(new AnnotationText());
    init();
  }

  /**
   * Sets the font of this annotation.
   * @param font a font.
   */
  public void setFont(Font font) {
    _font = font;
    updateTextRenderer();
  }

  /**
   * Gets the font for this annotation.
   * @return the font.
   */
  public Font getFont() {
    return _font;
  }

  /**
   * Sets the alignment for this annotation.
   * The default alignment is to the East (right) of the anchor.
   * @param alignment an alignment.
   */
  public void setAlignment(Alignment alignment) {
    _alignment = alignment;
    updateTextRenderer();
  }

  /**
   * Gets the alignment for this annotation.
   * @return the alignment.
   */
  public Alignment getAlignment() {
    return _alignment;
  }

  /**
   * Sets the text for this annotation.
   * @param text some text.
   */
  public void setText(String text) {
    _text = text;
  }

  /**
   * Gets the text for this annotation.
   * @return the text.
   */
  public String getText() {
    return _text;
  }

  /**
   * Sets the color for this annotation.
   * The default color is white.
   * @param color a color.
   */
  public void setColor(Color color) {
    _color = color;
  }

  /**
   * Gets this annotation's color.
   * @return the color.
   */
  public Color getColor() {
    return _color;
  }

  @Override
  public String toString() {
    return (this.getLocation().toString()+" "+_text);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Annotation that = (Annotation) o;

    if (_alignment != that._alignment) return false;
    if (!_font.equals(that._font)) return false;
    if (!_renderer.equals(that._renderer)) return false;
    if (!_text.equals(that._text)) return false;
    return _color.equals(that._color);
  }

  @Override
  public int hashCode() {
    int result = _alignment.hashCode();
    result = 31 * result + _font.hashCode();
    result = 31 * result + _renderer.hashCode();
    result = 31 * result + _text.hashCode();
    result = 31 * result + _color.hashCode();
    return result;
  }

  ////////////////////////////////////////////////////////////////////////////
  // private

  private class AnnotationText extends Node {
    AnnotationText() { }

    protected void draw(DrawContext dc) {
      Matrix44 localToPixel = dc.getLocalToPixel();

      Point3 p = localToPixel.times(new Point3(0,0,0));
      int w = dc.getViewCanvas().getWidth();
      int h = dc.getViewCanvas().getHeight();

      Rectangle2D rect = _renderer.getBounds(_text);
      double rw = rect.getWidth();
      double rh = rect.getHeight();

      int nudge = 10;

      _renderer.beginRendering(w,h);
      _renderer.setColor(_color);
      int x,y;
      switch(_alignment) {
        case NORTH:
          x = (int)(p.x-rw/2.0);
          y = (int)(h-p.y+rh/4.0)-nudge;
          break;
        case EAST:
          x = (int)(p.x)+nudge;
          y = (int)(h-p.y-rh/4.0);
          break;
        case WEST:
          x = (int)(p.x-rw)-nudge;
          y = (int)(h-p.y-rh/4.0);
          break;
        default: // SOUTH
          x = (int)(p.x-rw/2.0);
          y = (int)(h-p.y-rh/2.0)-nudge;
      }
      _renderer.draw(_text,x,y);
      _renderer.endRendering();
    }
    protected BoundingSphere computeBoundingSphere(boolean finite) {
      Rectangle2D rect = _renderer.getBounds(_text);
      double w = rect.getWidth();
      double h = rect.getHeight();
      return new BoundingSphere(0.0,0.0,0.0,sqrt(w*w+h*h));
    }
  }

  private void updateTextRenderer() {
    _renderer = new TextRenderer(_font);
  }

  private void init() {
    _alignment = Alignment.EAST;
    _font = new Font("SansSerif", Font.PLAIN, 18);
    _color = Color.WHITE;
    updateTextRenderer();
  }

  private Alignment _alignment;
  private Font _font;
  private TextRenderer _renderer;
  private String _text;
  private Color _color;

}
