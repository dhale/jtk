/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic;

import java.awt.*;

import edu.mines.jtk.util.AxisTics;

/**
 * Grid lines that extend tics in tile axes into tiles. Grid lines can be 
 * painted above or below other tiled views in a tile, simply by adding 
 * a grid view before or after those other tiled views.
 *
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.12.29
 */
public class GridView extends TiledView {

  /**
   * The grid horizontal type. The type can be none for no horizontal
   * grid lines, zero for one horizontal grid line at value zero, and
   * major for horizontal grid lines at all values of major axis tics.
   * The default type is major.
   */
  public enum Horizontal {
    NONE,
    ZERO,
    MAJOR,
  }

  /**
   * The grid vertical type. The type can be none for no horizontal
   * grid lines, zero for one vertical grid line at value zero, and
   * major for vertical grid lines at all values of major axis tics.
   * The default type is major.
   */
  public enum Vertical {
    NONE,
    ZERO,
    MAJOR
  }

  /**
   * The grid style.
   * The default style is solid.
   */
  public enum Style {
    NONE,
    SOLID,
    DASH,
    DOT,
    DASH_DOT,
  }

  /**
   * Constructs a grid view with default types, style, and color.
   */
  public GridView() {
  }

  /**
   * Constructs a grid view with specified types.
   * @param horizontal the grid horizontal type.
   * @param vertical the grid vertical type.
   */
  public GridView(Horizontal horizontal, Vertical vertical) {
    setHorizontal(horizontal);
    setVertical(vertical);
  }

  /**
   * Constructs a grid view with specified style.
   * @param style the grid style.
   */
  public GridView(Style style) {
    setStyle(style);
  }

  /**
   * Constructs a grid view with specified color.
   * @param color the grid color.
   */
  public GridView(Color color) {
    setColor(color);
  }

  /**
   * Constructs a grid view with specified types and color.
   * @param horizontal the grid horizontal type.
   * @param vertical the grid vertical type.
   * @param color the grid color.
   */
  public GridView(Horizontal horizontal, Vertical vertical, Color color) {
    setHorizontal(horizontal);
    setVertical(vertical);
    setColor(color);
  }

  /**
   * Constructs a grid view with specified types, style, and color.
   * @param horizontal the grid horizontal type.
   * @param vertical the grid vertical type.
   * @param color the grid color.
   * @param style the grid style.
   */
  public GridView(
    Horizontal horizontal, Vertical vertical, Color color, Style style) 
  {
    setHorizontal(horizontal);
    setVertical(vertical);
    setColor(color);
    setStyle(style);
  }

  /**
   * Constructs a grid view with specified parameters string. See the method 
   * {@link #setParameters(String)} for the format of the parameters string.
   * @param parameters the color and style of grid lines.
   */
  public GridView(String parameters) {
    setParameters(parameters);
  }

  /**
   * Sets the grid horizontal type.
   * The default grid horizontal type is major.
   * @param horizontal the grid horizontal type
   */
  public void setHorizontal(Horizontal horizontal) {
    if (_horizontal!=horizontal) {
      _horizontal = horizontal;
      repaint();
    }
  }

  /**
   * Sets the grid vertical type.
   * The default grid vertical type is major.
   * @param vertical the grid vertical type
   */
  public void setVertical(Vertical vertical) {
    if (_vertical!=vertical) {
      _vertical = vertical;
      repaint();
    }
  }

  /**
   * Sets the grid color.
   * The default grid color is the tile foreground color. 
   * That default is used if the specified color is null.
   * @param color the color; null, for default color.
   */
  public void setColor(Color color) {
    if (!equalColors(_color,color)) {
      _color = color;
      repaint();
    }
  }

  /**
   * Sets the grid style.
   * The default grid style is solid.
   * @param style the style.
   */
  public void setStyle(Style style) {
    if (_style!=style) {
      _style = style;
      repaint();
    }
  }

  /**
   * Sets the grid types, color, and style parameters from a string.
   * This method provides a convenient way to set the grid horizontal
   * and vertical types, color, and style of grid lines for this view.
   * <p>
   * To specify grid horizontal type, the parameters string may contain 
   * "H0" for grid horizontal type zero, or simply "H" for grid horizontal
   * type major. Otherwise, the grid horizontal type is none.
   * <p>
   * To specify grid vertical type, the parameters string may contain 
   * "V0" for grid vertical type zero, or simply "V" for grid vertical
   * type major. Otherwise, the grid vertical type is none.
   * <p>
   * To specify a grid color, the parameters string may contain one of "r" 
   * for red, "g" for green, "b" for blue, "c" for cyan, "m" for magenta, 
   * "y" for yellow, "k" for black, or "w" for white. If the parameter 
   * string contains none of these colors, then the default color is used.
   * <p>
   * To specify a grid style, the parameters string may contain one of "-" 
   * for solid lines, "--" for dashed lines, "-." for dotted lines, or "--."
   * for dash-dotted lines. If the parameters string contains none of these
   * line styles, then no grid lines are painted.
   * @param parameters the grid parameters string.
   */
  public void setParameters(String parameters) {

    // Horizontal.
    if (parameters.contains("H0")) {
      setHorizontal(Horizontal.ZERO);
    } else if (parameters.contains("H")) {
      setHorizontal(Horizontal.MAJOR);
    } else {
      setHorizontal(Horizontal.NONE);
    }

    // Vertical.
    if (parameters.contains("V0")) {
      setVertical(Vertical.ZERO);
    } else if (parameters.contains("V")) {
      setVertical(Vertical.MAJOR);
    } else {
      setVertical(Vertical.NONE);
    }

    // Color.
    if (parameters.contains("r")) {
      setColor(Color.RED);
    } else if (parameters.contains("g")) {
      setColor(Color.GREEN);
    } else if (parameters.contains("b")) {
      setColor(Color.BLUE);
    } else if (parameters.contains("c")) {
      setColor(Color.CYAN);
    } else if (parameters.contains("m")) {
      setColor(Color.MAGENTA);
    } else if (parameters.contains("y")) {
      setColor(Color.YELLOW);
    } else if (parameters.contains("k")) {
      setColor(Color.BLACK);
    } else if (parameters.contains("w")) {
      setColor(Color.WHITE);
    } else {
      setColor(null);
    }

    // Style.
    if (parameters.contains("--.")) {
      setStyle(Style.DASH_DOT);
    } else if (parameters.contains("--")) {
      setStyle(Style.DASH);
    } else if (parameters.contains("-.")) {
      setStyle(Style.DOT);
    } else if (parameters.contains("-")) {
      setStyle(Style.SOLID);
    } else {
      setStyle(Style.NONE);
    }
  }

  public void paint(Graphics2D g2d) {

    // If no lines, then paint nothing.
    if (_horizontal==Horizontal.NONE && _vertical==Vertical.NONE) 
      return;
    if (_style==Style.NONE)
      return;

    // Tile axes. If no axes, paint nothing.
    Tile tile = getTile();
    TileAxis axisTop = tile.getTileAxisTop();
    TileAxis axisLeft = tile.getTileAxisLeft();
    TileAxis axisBottom = tile.getTileAxisBottom();
    TileAxis axisRight = tile.getTileAxisRight();
    TileAxis axisLeftRight = (axisLeft!=null)?axisLeft:axisRight;
    TileAxis axisTopBottom = (axisTop!=null)?axisTop:axisBottom;
    if (axisLeftRight==null && axisTopBottom==null)
      return;

    // Anti-alias grid lines.
    g2d.setRenderingHint(
      RenderingHints.KEY_ANTIALIASING,
      RenderingHints.VALUE_ANTIALIAS_ON);

    // Line width.
    float lineWidth = 1.0f;
    Stroke stroke = g2d.getStroke();
    if (stroke instanceof BasicStroke) {
      BasicStroke bs = (BasicStroke)stroke;
      lineWidth = bs.getLineWidth();
    }
    float width = lineWidth;

    // Line style and color.
    float[] dash = null;
    if (_style!=Style.SOLID) {
      float dotLength = 0.5f*width;
      float dashLength = 2.0f*width;
      float gapLength = 2.0f*dotLength+dashLength;
      if (_style==Style.DASH) {
        dash = new float[]{dashLength,gapLength};
      } else if (_style==Style.DOT) {
        dash = new float[]{dotLength,gapLength};
      } else if (_style==Style.DASH_DOT) {
        dash = new float[]{dashLength,gapLength,dotLength,gapLength};
      }
    }
    BasicStroke bs;
    if (dash!=null) {
      int cap = BasicStroke.CAP_ROUND;
      int join = BasicStroke.JOIN_ROUND;
      float miter = 10.0f;
      float phase = 0.0f;
      bs = new BasicStroke(width,cap,join,miter,dash,phase);
    } else {
      bs = new BasicStroke(width);
    }
    g2d.setStroke(bs);
    if (_color!=null)
      g2d.setColor(_color);

    // Projectors and transcaler.
    Projector hp = getHorizontalProjector();
    Projector vp = getVerticalProjector();
    Transcaler ts = getTranscaler();

    // Width and height.
    DRectangle vr = tile.getViewRectangle();
    int w = ts.width(vr.width);
    int h = ts.height(vr.height);
    
    // Horizontal grid lines.
    if (_horizontal==Horizontal.ZERO) {
      int y = ts.y(vp.u(0.0));
      g2d.drawLine(0,y,w-1,y);
    } else if (_horizontal==Horizontal.MAJOR && axisLeftRight!=null) {
      AxisTics at = axisLeftRight.getAxisTics();
      int nticMajor = at.getCountMajor();
      double dticMajor = at.getDeltaMajor();
      double fticMajor = at.getFirstMajor();
      for (int itic=0; itic<nticMajor; ++itic) {
        double vtic = fticMajor+itic*dticMajor;
        double utic = vp.u(vtic);
        int y = ts.y(utic);
        g2d.drawLine(0,y,w-1,y);
      }
    }
    
    // Vertical grid lines.
    if (_vertical==Vertical.ZERO) {
      int x = ts.x(hp.u(0.0));
      g2d.drawLine(x,0,x,h-1);
    } else if (_vertical==Vertical.MAJOR && axisTopBottom!=null) {
      AxisTics at = axisTopBottom.getAxisTics();
      int nticMajor = at.getCountMajor();
      double dticMajor = at.getDeltaMajor();
      double fticMajor = at.getFirstMajor();
      for (int itic=0; itic<nticMajor; ++itic) {
        double vtic = fticMajor+itic*dticMajor;
        double utic = hp.u(vtic);
        int x = ts.x(utic);
        g2d.drawLine(x,0,x,h-1);
      }
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private Horizontal _horizontal = Horizontal.MAJOR;
  private Vertical _vertical = Vertical.MAJOR;
  private Style _style = Style.SOLID;
  private Color _color = null;

  private boolean equalColors(Color ca, Color cb) {
    return (ca==null)?cb==null:ca.equals(cb);
  }
}
