/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic;

import static java.lang.Math.*;
import java.awt.*;
import java.awt.font.*;
import javax.swing.*;
import edu.mines.jtk.util.*;

/**
 * A tile axis in a mosaic. Tile axes may be placed along the top, left,
 * bottom, and right sides of the mosaic of tiles. Each horizontal (top or
 * bottom) axis annotates the tiles in its column, and each vertical (left
 * or right) axis annotates the tiles in its row.
 * <p>
 * Axis tics, tic annotations, and the (optional) axis label are painted 
 * using the tile axis font and foreground colors.
 *
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.12.27
 * @version 2005.12.23
 */
public class TileAxis extends IPanel {
  private static final long serialVersionUID = 1L;

  /**
   * Placement of a tile axis.
   */
  public enum Placement {
    TOP, LEFT, BOTTOM, RIGHT
  }

  /**
   * Gets the mosaic that contains this axis.
   * @return the mosaic.
   */
  public Mosaic getMosaic() {
    return _mosaic;
  }

  /**
   * Gets the row or column index for this axis.
   * @return the row or column index.
   */
  public int getIndex() {
    return _index;
  }

  /**
   * Gets the placement of this axis.
   * @return the placement.
   */
  public Placement getPlacement() {
    return _placement;
  }

  /**
   * Gets the tile adjacent to this axis.
   * @return the tile.
   */
  public Tile getTile() {
    if (isTop()) {
      return _mosaic.getTile(0,_index);
    } else if (isLeft()) {
      return _mosaic.getTile(_index,0);
    } else if (isBottom()) {
      int irow = _mosaic.countRows()-1;
      return _mosaic.getTile(irow,_index);
    } else if (isRight()) {
      int icol = _mosaic.countColumns()-1;
      return _mosaic.getTile(_index,icol);
    } else {
      return null;
    }
  }

  /**
   * Determines whether this axis is placed at top of mosaic.
   * @return true, if at top; false, otherwise.
   */
  public boolean isTop() {
    return _placement==Placement.TOP;
  }

  /**
   * Determines whether this axis is placed at left of mosaic.
   * @return true, if at left; false, otherwise.
   */
  public boolean isLeft() {
    return _placement==Placement.LEFT;
  }

  /**
   * Determines whether this axis is placed at bottom of mosaic.
   * @return true, if at bottom; false, otherwise.
   */
  public boolean isBottom() {
    return _placement==Placement.BOTTOM;
  }

  /**
   * Determines whether this axis is placed at right of mosaic.
   * @return true, if at right; false, otherwise.
   */
  public boolean isRight() {
    return _placement==Placement.RIGHT;
  }

  /**
   * Determines whether this axis is placed at top or bottom of mosaic.
   * An axis placed at the top or bottom is a horizontal axis.
   * @return true, if horizontal (at top or bottom); false, otherwise.
   */
  public boolean isHorizontal() {
    return _placement==Placement.TOP || _placement==Placement.BOTTOM;
  }

  /**
   * Determines whether this axis is placed at left or right of mosaic.
   * An axis placed at the left or right is a vertical axis.
   * @return true, if vertical (at left or right); false, otherwise.
   */
  public boolean isVertical() {
    return _placement==Placement.LEFT || _placement==Placement.RIGHT;
  }

  /**
   * Sets the label for this axis.
   * @param label the label.
   */
  public void setLabel(String label) {
    _label = label;
    updateSizes();
    if (_mosaic!=null)
      _mosaic.revalidate();
    repaint();
  }

  /**
   * Sets the format for major tic annotation for this axis.
   * The default format is "%1.4G", which yields a minimum of 1 digit,
   * with up to 4 digits of precision. Any trailing zeros and decimal
   * point are removed from tic annotation.
   * @param format the format.
   */
  public void setFormat(String format) {
    _format = format;
    updateSizes();
    if (_mosaic!=null)
      _mosaic.revalidate();
    repaint();
  }

  // Override base class, so we can revalidate the mosaic layout. Must check 
  // for a null mosaic, because this method is called by the base class 
  // constructor, before our constructor sets the (non-null) mosaic.
  public void setFont(Font font) {
    super.setFont(font);
    updateSizes();
    if (_mosaic!=null)
      _mosaic.revalidate();
    repaint();
  }

  /**
   * Gets the axis tics painted by this tile axis. Axis tics depend on
   * the graphics context in which the axis will be painted. For example,
   * the font size in the graphics context font used to ensure that the
   * labels of major tics can be painted without overlap.
   * <p>
   * The axis tics depend also on the projectors and transcaler of the
   * tile adjacent to this axis. These attributes are assumed to be valid
   * when this method is called.
   * @param g2d the graphics context.
   * @param w the width of the graphics rectangle.
   * @param h the height of the graphics rectangle.
   * @return the axis tics.
   */
  public AxisTics getAxisTics(Graphics2D g2d, int w, int h) {

    // Adjacent tile.
    Tile tile = getTile();
    if (tile==null)
      return null;

    // Projector and transcaler from adjacent tile.
    Projector p = (isHorizontal()) ?
      tile.getHorizontalProjector() :
      tile.getVerticalProjector();
    Transcaler t = tile.getTranscaler(w,h);

    // Font height.
    Font font = g2d.getFont();
    FontMetrics fm = g2d.getFontMetrics();
    FontRenderContext frc = g2d.getFontRenderContext();
    LineMetrics lm = font.getLineMetrics("0.123456789",frc);
    int fh = round(lm.getHeight());

    // Axis placement.
    boolean isHorizontal = isHorizontal();

    // Axis tics.
    AxisTics at;
    double umin = p.u0();
    double umax = p.u1();
    double vmin = min(p.v0(),p.v1());
    double vmax = max(p.v0(),p.v1());
    if (isHorizontal) {
      int nmax = 2+w/maxTicStringWidth(fm);
      double u0 = max(umin,t.x(0));
      double u1 = min(umax,t.x(w-1));
      double v0 = max(vmin,min(vmax,p.v(u0)));
      double v1 = max(vmin,min(vmax,p.v(u1)));
      double du = min(umax-umin,t.width(w));
      double dv = abs(p.v(u0+du)-p.v(u0));
      at = new AxisTics(vmin,vmin+dv,nmax);
      at = new AxisTics(v0,v1,at.getDeltaMajor());
    } else {
      int nmax = 2+h/(4*fh);
      double u0 = max(umin,t.y(0));
      double u1 = min(umax,t.y(h-1));
      double v0 = max(vmin,min(vmax,p.v(u0)));
      double v1 = max(vmin,min(vmax,p.v(u1)));
      double du = min(umax-umin,t.height(h));
      double dv = abs(p.v(u0+du)-p.v(u0));
      at = new AxisTics(vmin,vmin+dv,nmax);
      at = new AxisTics(v0,v1,at.getDeltaMajor());
    }
    return at;
  }

  public void paintToRect(Graphics2D g2d, int x, int y, int w, int h) {
    g2d = createGraphics(g2d,x,y,w,h);
    g2d.setRenderingHint(
      RenderingHints.KEY_ANTIALIASING,
      RenderingHints.VALUE_ANTIALIAS_ON);

    // Adjacent tile; if none, do nothing.
    Tile tile = getTile();
    if (tile==null)
      return;

    // Projector and transcaler from adjacent tile.
    Projector p = (isHorizontal()) ?
      tile.getHorizontalProjector() :
      tile.getVerticalProjector();
    Transcaler t = tile.getTranscaler(w,h);

    // Font dimensions.
    Font font = g2d.getFont();
    FontMetrics fm = g2d.getFontMetrics();
    FontRenderContext frc = g2d.getFontRenderContext();
    LineMetrics lm = font.getLineMetrics("0.123456789",frc);
    int fh = round(lm.getHeight());
    int fa = round(lm.getAscent());
    int fd = round(lm.getDescent());
    int fl = round(lm.getLeading());

    // Length of major tics.
    int tl = 2*fa/3;

    // Axis placement.
    boolean isHorizontal = isHorizontal();
    boolean isTop = isTop();
    boolean isLeft = isLeft();

    // Axis tics.
    AxisTics at = getAxisTics(g2d,w,h);
    int nticMajor = at.getCountMajor();
    double dticMajor = at.getDeltaMajor();
    double fticMajor = at.getFirstMajor();
    int nticMinor = at.getCountMinor();
    double dticMinor = at.getDeltaMinor();
    double fticMinor = at.getFirstMinor();
    int mtic = at.getMultiple();

    // Minor tics. Skip major tics, which may not coincide, due to rounding.
    int ktic = (int)round((fticMajor-fticMinor)/dticMinor);
    for (int itic=0; itic<nticMinor; ++itic) {
      if (itic==ktic) {
        ktic += mtic;
      } else {
        double vtic = fticMinor+itic*dticMinor;
        double utic = p.u(vtic);
        if (isHorizontal) {
          x = t.x(utic);
          if (isTop) {
            g2d.drawLine(x,h-1,x,h-1-tl/2);
          } else {
            g2d.drawLine(x,0,x,tl/2);
          }
        } else {
          y = t.y(utic);
          if (isLeft) {
            g2d.drawLine(w-1,y,w-1-tl/2,y);
          } else {
            g2d.drawLine(0,y,tl/2,y);
          }
        }
      }
    }

    // Major tics.
    int wsmax = 0;
    double tiny = 1.0e-6*abs(dticMajor);
    for (int itic=0; itic<nticMajor; ++itic) {
      double vtic = fticMajor+itic*dticMajor;
      double utic = p.u(vtic);
      if (abs(vtic)<tiny)
        vtic = 0.0;
      String stic = formatTic(vtic);
      if (isHorizontal) {
        x = t.x(utic);
        if (isTop) {
          y = h-1;
          g2d.drawLine(x,y,x,y-tl);
          y -= tl+fd;
        } else {
          y = 0;
          g2d.drawLine(x,y,x,y+tl);
          y += tl+fa;
        }
        int ws = fm.stringWidth(stic);
        int xs = max(0,min(w-ws,x-ws/2));
        int ys = y;
        g2d.drawString(stic,xs,ys);
      } else {
        y = t.y(utic);
        if (isLeft) {
          x = w-1;
          g2d.drawLine(x,y,x-tl,y);
          x -= tl+fd;
        } else {
          x = 0;
          g2d.drawLine(x,y,x+tl,y);
          x += tl+fd;
        }
        int ws = fm.stringWidth(stic);
        if (ws>wsmax)
          wsmax = ws;
        int xs = (isLeft)?x-ws:x;
        int ys = max(fa,min(h-1,y+fa/3));
        g2d.drawString(stic,xs,ys);
      }
    }

    // Axis label.
    if (_label!=null) {
      if (isHorizontal) {
        int wl = fm.stringWidth(_label);
        int xl = max(0,min(w-wl,(w-wl)/2));
        int yl = isTop?h-1-tl-fh-fd:tl+fh+fa;
        g2d.drawString(_label,xl,yl);
      } else {
        int wl = fm.stringWidth(_label);
        int xl = isLeft ?
          max(fa,w-1-tl-fd-wsmax-fd-fl) :
          min(w-1-fd,tl+fd+wsmax+fh);
        int yl = max(wl,min(h,(h+wl)/2));
        g2d.translate(xl,yl);
        g2d.rotate(-PI/2.0);
        g2d.drawString(_label,0,0);
        g2d.rotate(PI/2.0);
        g2d.translate(-xl,-yl);
      }
    }

    g2d.dispose();
  }

  ///////////////////////////////////////////////////////////////////////////
  // protected

  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    endTracking();
    paintToRect((Graphics2D)g,0,0,getWidth(),getHeight());
  }

  ///////////////////////////////////////////////////////////////////////////
  // package

  TileAxis(Mosaic mosaic, Placement placement, int index) {
    _mosaic = mosaic;
    _placement = placement;
    _index = index;
    mosaic.add(this);
  }

  /**
   * Gets the width minimum for this axis. This width does not include 
   * any border that will be drawn by the mosaic.
   */
  int getWidthMinimum() {
    FontMetrics fm = getFontMetrics(getFont());
    int width = 0;
    if (isVertical()) {
      width = maxTicStringWidth(fm)+fm.getHeight();
      if (_label!=null)
        width += fm.getHeight();
    } else {
      width = 20;
      if (_label!=null)
        width = max(width,fm.stringWidth(_label));
    }
    return width;
  }

  /**
   * Gets the height minimum for this axis. This height does not include 
   * any border that will be drawn by the mosaic.
   */
  int getHeightMinimum() {
    FontMetrics fm = getFontMetrics(getFont());
    int height = 0;
    if (isHorizontal()) {
      height = 2*fm.getHeight();
      if (_label!=null)
        height += fm.getHeight();
    } else {
      height = 20;
      if (_label!=null)
        height = max(height,fm.stringWidth(_label));
    }
    return height;
  }

  private void updateSizes() {
    int w = getWidthMinimum();
    int h = getHeightMinimum();
    Dimension size = new Dimension(w,h);
    this.setMinimumSize(size);
    this.setPreferredSize(size);
  }

  // Tracking methods called by MouseTrackMode.
  void beginTracking(int x, int y) {
    if (!_tracking) {
      _xtrack = x;
      _ytrack = y;
      _tracking = true;
      paintTrack(x,y);
    }
  }
  void duringTracking(int x, int y) {
    if (_tracking)
      paintTrack(_xtrack,_ytrack);
    _xtrack = x;
    _ytrack = y;
    _tracking = true;
    paintTrack(_xtrack,_ytrack);
  }
  void endTracking() {
    if (_tracking) {
      paintTrack(_xtrack,_ytrack);
      _tracking = false;
    }
  }
  private void paintTrack(int x, int y) {
    int w = this.getWidth();
    int h = this.getHeight();
    Graphics g = this.getGraphics();
    g.setColor(Color.BLUE);
    g.setXORMode(this.getBackground());
    if (this.isHorizontal()) {
      g.drawLine(x,-1,x,h);
    } else {
      g.drawLine(-1,y,w,y);
    }
    g.dispose();
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private Mosaic _mosaic;
  private Placement _placement;
  private int _index;
  private String _label;
  private String _format = "%1.4G";
  private int _xtrack,_ytrack;
  private boolean _tracking;

  // Returns the maximum width of a formatted tic string.
  private int maxTicStringWidth(FontMetrics fm) {
    double vtic = -123456789.0E-10;
    //System.out.println("TileAxis.maxTicStringWidth: vtic="+formatTic(vtic));
    return fm.stringWidth(formatTic(vtic));
  }

  // Formats tic value, removing any trailing zeros after a decimal point.
  private String formatTic(double v) {
    String s = String.format(_format,v);
    int len = s.length();
    int iend = s.indexOf('e');
    if (iend<0)
      iend = s.indexOf('E');
    if (iend<0)
      iend = len;
    int ibeg = iend;
    if (s.indexOf('.')>0) {
      while (ibeg>0 && s.charAt(ibeg-1)=='0')
        --ibeg;
      if (ibeg>0 && s.charAt(ibeg-1)=='.')
        --ibeg;
    }
    if (ibeg<iend) {
      String sb = s.substring(0,ibeg);
      s = (iend<len)?sb+s.substring(iend,len):sb;
    }
    return s;
  }
}
