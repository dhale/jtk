/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic;

import java.awt.*;
import java.util.*;
import edu.mines.jtk.dsp.Sampling;
import edu.mines.jtk.util.*;
import static edu.mines.jtk.util.MathPlus.*;

/**
 * Grid lines that extend tics in tile axes into tiles. Grid lines can be 
 * painted above or below other tiled views in a tile, simply by adding 
 * a grid view before or after those other tiled views.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.12.29
 */
public class GridView extends TiledView {

  /**
   * The grid direction.
   * The default direction is both (horizontal and vertical).
   */
  public enum Direction {
    NONE,
    HORIZONTAL,
    VERTICAL,
    BOTH,
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
   * Constructs a grid view with default direction, style, and color.
   */
  public GridView() {
  }

  /**
   * Constructs a grid view with specified direction.
   * @param direction the grid direction.
   */
  public GridView(Direction direction) {
    setDirection(direction);
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
   * Constructs a grid view with specified direction and color.
   * @param direction the grid direction.
   * @param color the grid color.
   */
  public GridView(Direction direction, Color color) {
    setDirection(direction);
    setColor(color);
  }

  /**
   * Constructs a grid view with specified direction, style, and color.
   * @param direction the grid direction.
   * @param color the grid color.
   * @param style the grid style.
   */
  public GridView(Direction direction, Color color, Style style) {
    setDirection(direction);
    setColor(color);
    setStyle(style);
  }

  /**
   * Constructs a grid view with specified parameters string. See the method 
   * {@link #set(String)} for the format of the parameters string.
   * @param parameters the color and style of grid lines.
   */
  public GridView(String parameters) {
    set(parameters);
  }

  /**
   * Sets the grid direction.
   * The default grid direction is both (horizontal and vertical).
   * @param direction the direction.
   */
  public void setDirection(Direction direction) {
    if (_direction!=direction) {
      _direction = direction;
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
   * Sets the grid direction, color, and style from a parameter string.
   * This method provides a convenient way to set the direction, color,
   * and style of grid lines painted by this view.
   * <p>
   * To specify a grid direction the parameters string may contain "h"
   * for horizontal grid lines, "v" for vertical grid lines, or both
   * "h" and "v" for both horizontal and vertical grid lines. If the
   * parameters string contains neither of these directions, then no
   * grid lines are painted.
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
  public void set(String parameters) {

    // Direction.
    if (parameters.contains("h") && parameters.contains("v")) {
      setDirection(Direction.BOTH);
    } else if (parameters.contains("h")) {
      setDirection(Direction.HORIZONTAL);
    } else if (parameters.contains("v")) {
      setDirection(Direction.VERTICAL);
    } else {
      setDirection(Direction.NONE);
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
    if (_direction==Direction.NONE || _style==Style.NONE)
      return;

    // Tile and mosaic. If no mosaic, paint nothing.
    Tile tile = getTile();
    Mosaic mosaic = tile.getMosaic();
    if (mosaic==null)
      return;

    // Tile axes. If no axes, paint nothing.
    int irow = tile.getRowIndex();
    int icol = tile.getColumnIndex();
    TileAxis axisTop = mosaic.getTileAxisTop(icol);
    TileAxis axisLeft = mosaic.getTileAxisLeft(irow);
    TileAxis axisBottom = mosaic.getTileAxisBottom(icol);
    TileAxis axisRight = mosaic.getTileAxisRight(irow);
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

    // Line style and color.
    float[] dash = null;
    if (_style!=Style.SOLID) {
      float dotLength = lineWidth;
      float dashLength = 5.0f*lineWidth;
      float gapLength = 5.0f*lineWidth;
      if (_style==Style.DASH) {
        dash = new float[]{dashLength,gapLength};
      } else if (_style==Style.DOT) {
        dash = new float[]{dotLength,gapLength};
      } else if (_style==Style.DASH_DOT) {
        dash = new float[]{dashLength,gapLength,dotLength,gapLength};
      }
    }
    float width = lineWidth;
    BasicStroke bs = null;
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
    boolean horizontal = 
      _direction==Direction.HORIZONTAL ||
      _direction==Direction.BOTH;
    if (horizontal && axisLeftRight!=null) {
      AxisTics at = axisLeftRight.getAxisTics(g2d,w,h);
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
    boolean vertical = 
      _direction==Direction.VERTICAL ||
      _direction==Direction.BOTH;
    if (vertical && axisTopBottom!=null) {
      AxisTics at = axisTopBottom.getAxisTics(g2d,w,h);
      int nticMajor = at.getCountMajor();
      double dticMajor = at.getDeltaMajor();
      double fticMajor = at.getFirstMajor();
      int y0 = ts.y(0.0);
      int y1 = ts.y(1.0);
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

  private Direction _direction = Direction.BOTH;
  private Style _style = Style.SOLID;
  private Color _color = null;

  private boolean equalColors(Color ca, Color cb) {
    return (ca==null)?cb==null:ca.equals(cb);
  }
}
