/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;
import static java.lang.Math.*;
import javax.swing.*;

import edu.mines.jtk.util.AxisTics;
import edu.mines.jtk.util.StringUtil;

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
   * Determines whether this axis is placed at left or right of mosaic, and 
   * is rotated to read normal to the vertical axis.
   * @return true, if vertical and rotated; false, otherwise.
   */
  public boolean isVerticalRotated() {
    return isVertical() && _isRotated;
  }
  /**
   * Sets the interval between major labeled tics for this axis.
   * The default tic interval is zero, in which case a readable tic
   * interval is computed automatically. This default is especially
   * useful when interactively zooming and scrolling.
   * @param interval the major labeled tic interval.
   */
  public void setInterval(double interval) {
    _interval = interval;
    if (updateAxisTics())
      revalidate();
    repaint();
  }

  /**
   * Sets the label for this axis.
   * @param label the label.
   */
  public void setLabel(String label) {
    _label = label;
    if (updateAxisTics())
      revalidate();
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
    if (updateAxisTics())
      revalidate();
    repaint();
  }

  // Override base class implementation, so we can update axis tics.
  public void setFont(Font font) {
    super.setFont(font);
    if (updateAxisTics())
      revalidate();
    repaint();
  }

  // Override base class implementation, so we can update axis tics.
  public void setBounds(int x, int y, int width, int height) {
    super.setBounds(x,y,width,height);
    if (updateAxisTics())
      revalidateLater(); // revalidating now will not work!
    repaint();
  }

  /**
   * Sets the rotation of tic labels in the vertical axis.
   * Tic labels for a rotated vertical axis are rotated 90 degrees
   * counter-clockwise.
   * @param rotated true if rotated; false, otherwise.
   */
  public void setVerticalAxisRotated(boolean rotated) {
    _isRotated = rotated;
    if (updateAxisTics())
      revalidate();
    repaint();
  }

  /**
   * Gets the axis tics painted by this tile axis.
   * @return the axis tics.
   */
  public AxisTics getAxisTics() {
    return _axisTics;
  }

  public void paintToRect(Graphics2D g2d, int x, int y, int w, int h) {

    // If no axis tics, paint nothing.
    if (_axisTics==null)
      return;

    // Adjacent tile; if none, do nothing.
    Tile tile = getTile();
    if (tile==null)
      return;

    // Create graphics context.
    g2d = createGraphics(g2d,x,y,w,h);
    g2d.setRenderingHint(
      RenderingHints.KEY_ANTIALIASING,
      RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setRenderingHint(
      RenderingHints.KEY_TEXT_ANTIALIASING,
      RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

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
    int tl = fa/3;

    // Axis placement.
    boolean isHorizontal = isHorizontal();
    boolean isTop = isTop();
    boolean isLeft = isLeft();
    boolean isVerticalRotated = isVerticalRotated();

    // Axis tic sampling.
    int nticMajor = _axisTics.getCountMajor();
    double dticMajor = _axisTics.getDeltaMajor();
    double fticMajor = _axisTics.getFirstMajor();
    int nticMinor = _axisTics.getCountMinor();
    double dticMinor = _axisTics.getDeltaMinor();
    double fticMinor = _axisTics.getFirstMinor();
    int mtic = _axisTics.getMultiple();

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

      } else if (isVerticalRotated) {
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
        int xs = x;
        int ys = max(ws,min(h,y+ws/2));
        g2d.translate(xs,ys);
        g2d.rotate(-PI/2.0);
        g2d.drawString(stic,0,0);
        g2d.rotate(PI/2.0);
        g2d.translate(-xs,-ys);
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
        int ys = max(fa,min(h-1,y+(int)round(0.3*fa)));
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
        //int xl = isLeft ?
        //  max(fa,w-1-tl-fd-wsmax-fd-fd-fl) :
        //  min(w-1-fd,tl+fd+wsmax+fa);
        int xl = isLeft?fa+fd:w-1-fd-fd-fl;
        int yl = max(wl,min(h,(h+wl)/2));
        g2d.translate(xl,yl);
        g2d.rotate(-PI/2.0);
        g2d.drawString(_label,0,0);
        g2d.rotate(PI/2.0);
        g2d.translate(-xl,-yl);
      }
    }

    // Dispose graphics context.
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

  // Called by constructor for mosaic.
  TileAxis(Mosaic mosaic, Placement placement, int index) {
    _mosaic = mosaic;
    _placement = placement;
    _index = index;
    //setBackground(Color.CYAN); // for debugging only
    mosaic.add(this);
  }

  /**
   * Gets the width minimum for this axis. This width does not include 
   * any border that will be drawn by the mosaic.
   */
  int getWidthMinimum() {
    if (_widthMinimum!=0)
      return _widthMinimum;
    FontMetrics fm = getFontMetrics(getFont());
    if (_ticLabelWidth==0)
      if (updateAxisTics())
        //revalidateLater(); // postpone any revalidate?
        revalidate();
    int ticLabelWidth = _ticLabelWidth;
    if (ticLabelWidth==0)
      ticLabelWidth = maxTicLabelWidth(fm);
    int width;
    if (isVertical()) {
      if (isVerticalRotated()) {
        width = fm.getAscent()+fm.getHeight();
      } else {
        width = ticLabelWidth+fm.getHeight();
      }
      if (_label!=null)
        width += fm.getHeight();
    } else {
      width = 50;
      if (_label!=null)
        width = max(width,fm.stringWidth(_label));
    }
    return width;
  }
  // Hack!
  void setWidthMinimum(int widthMinimum) {
    _widthMinimum = widthMinimum;
    revalidate();
  }
  private int _widthMinimum;

  /**
   * Gets the height minimum for this axis. This height does not include 
   * any border that will be drawn by the mosaic.
   */
  int getHeightMinimum() {
    FontMetrics fm = getFontMetrics(getFont());
    int height;
    if (isHorizontal()) {
      height = fm.getHeight()+fm.getAscent();
      if (_label!=null)
        height += fm.getHeight();
    } else {
      height = 50;
      if (_label!=null)
        height = max(height,fm.stringWidth(_label));
    }
    return height;
  }

  /**
   * Updates the axis tics for this axis. Axes tics depend on many different
   * parameters, including the axis width, height, font, tic label format, 
   * and tile projector. Methods that change these parameters should call
   * this method.
   * <p>
   * The dependency on axis width and height must be handled carefully. A 
   * mosaic lays out its tile axes using their minimum (preferred) widths 
   * and heights. The minimum width and height depend on axis tic labels 
   * that, in turn, depend on the axis width and height. To help manage 
   * this circular dependency, this method remembers whether or not the
   * size of this axis is valid, so that the mosaic can adjust its size
   * if necessary.
   * @return true, if this update changes the preferred size of this axis;
   *  false, otherwise.
   */
  boolean updateAxisTics() {

    // Adjacent tile.
    Tile tile = getTile();
    if (tile==null)
      return false;

    // Width and height of this axis.
    int w = getWidth();
    int h = getHeight();
    if (w==0 || h==0)
      return false;

    // Projector and transcaler from adjacent tile.
    Projector p = (isHorizontal()) ?
      tile.getHorizontalProjector() :
      tile.getVerticalProjector();
    Transcaler t = tile.getTranscaler(w,h);

    // Font and font render context.
    Font font = getFont();
    FontRenderContext frc = new FontRenderContext(null,true,false);

    // Axis orientation.
    boolean isHorizontal = isHorizontal();
    boolean isVerticalRotated = isVerticalRotated();

    // Min/max normalized coordinates for tics.
    // (We do not want any tics in the margins.)
    double umin = p.u0();
    double umax = p.u1();

    // Min/max world coordinates.
    double vmin = min(p.v0(),p.v1());
    double vmax = max(p.v0(),p.v1());

    // Normalized coordinates range [u0,u1] that is currently visible.
    // u0 is the smallest (closest to zero) normalized coordinate visible.
    // u1 is the largest (farthest from zero) normalized coordinate visible.
    // du is the range of normalized coordinates u currently visible.
    double u0,u1,du;
    if (isHorizontal) {
      u0 = max(umin,t.x(0));
      u1 = min(umax,t.x(w-1));
      du = min(umax-umin,t.width(w));
    } else {
      u0 = max(umin,t.y(0));
      u1 = min(umax,t.y(h-1));
      du = min(umax-umin,t.height(h));
    }

    // The corresponding world coordinate range [v0,v1]. Again, we do not 
    // want any tics in the margins. Also, we ensure that [v0,v1] lies
    // inside the min/max world coordinate bounds. (Is this necessary?)
    // v0 is the world coordinate corresponding to u0.
    // v1 is the world coordinate corresponding to u1.
    // dv is the range of world coordinates v currently visible.
    double v0 = max(vmin,min(vmax,p.v(u0)));
    double v1 = max(vmin,min(vmax,p.v(u1)));
    double dv = abs(p.v(u0+du)-p.v(u0));

    // Maximum width and height of any tic label.
    int ticLabelWidth = 0;
    int ticLabelHeight = 0;

    // Begin with an excessive maximum number of tics, and decrease that 
    // maximum number until we find a good fit. As a special case, if the
    // tic interval is specified, then this loop exits quickly after we
    // compute the max width and height of tic labels.
    int ntic;
    double dtic = 0.0;
    for (int nmax=20; nmax>=2; nmax=ntic-1) {

      // Compute the actual number of tics and a readable tic interval.
      // We do not construct these axis tics for [v0,v1], because we want 
      // the tic interval dtic to depend only on the span dv of world 
      // coordinates visible, not the actual coordinate values. In this way,
      // we maintain a constant tic interval as this axis is scrolled.
      AxisTics at;
      if (_interval==0.0) {
        at= new AxisTics(vmin,vmin+dv,nmax);
      } else {
        at= new AxisTics(vmin,vmin+dv,_interval);
      }
      ntic = at.getCountMajor();
      dtic = at.getDeltaMajor();

      // The tic values nearest vmin and vmax. 
      double va = ceil(vmin/dtic)*dtic;
      double vb = floor(vmax/dtic)*dtic;

      // Format the smallest two tic values and the largest two tic values
      // to obtain four tic labels. From these, compute the max width and 
      // height of tic labels.
      Rectangle2D.Double r = new Rectangle2D.Double();
      Rectangle2D.union(font.getStringBounds(formatTic(va     ),frc),r,r);
      Rectangle2D.union(font.getStringBounds(formatTic(va+dtic),frc),r,r);
      Rectangle2D.union(font.getStringBounds(formatTic(vb-dtic),frc),r,r);
      Rectangle2D.union(font.getStringBounds(formatTic(vb     ),frc),r,r);
      ticLabelWidth = (int)ceil(r.width);
      ticLabelHeight = (int)ceil(r.height);

      // If tic interval is specified explicitly, we're done.
      if (_interval!=0.0)
        break;

      // Otherwise, assume that all tic labels have the max width and height.
      // If those tic labels use less than a fraction of the space available, 
      // then we have a good fit and stop looking.
      if (isHorizontal) {
        if (ticLabelWidth*ntic<0.7*w)
          break;
      } else {
        if (isVerticalRotated) {
          if (ticLabelWidth*ntic<0.7*h)
            break;
        } else {
          if (ticLabelHeight*ntic<0.6*h)
            break;
        }
      }
    }

    // Compute new axis tics constructed with the best-fit tic interval 
    // computed above, but now for the currently visible range [v0,v1] 
    // of world coordinates. These axis tics are painted by this axis.
    _axisTics = new AxisTics(v0,v1,dtic);

    // If either the tic label max width or height has changed,
    // then the preferred size of this axis may have changed as well.
    if (_ticLabelWidth!=ticLabelWidth || _ticLabelHeight!=ticLabelHeight) {
      _ticLabelWidth = ticLabelWidth;
      _ticLabelHeight = ticLabelHeight;
      return true;
    } else {
      return false;
    }
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

  static boolean revalidatePending(Container parent) {
    int n = parent.getComponentCount();
    for (int i=0; i<n; ++i) {
      Component child = parent.getComponent(i);
      if (child instanceof TileAxis) {
        TileAxis ta = (TileAxis)child;
        if (ta._revalidatePending)
          return true;
      } else if (child instanceof Container) {
        if (revalidatePending((Container)child))
          return true;
      }
    }
    return false;
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private Mosaic _mosaic;
  private Placement _placement;
  private boolean _isRotated;
  private int _index;
  private String _label;
  private String _format = "%1.4G";
  private int _xtrack,_ytrack;
  private boolean _tracking;
  private double _interval;
  private int _ticLabelWidth;
  private int _ticLabelHeight;
  private AxisTics _axisTics;
  private boolean _revalidatePending;

  /**
   * Called by this axis when it needs to be revalidated because a 
   * change in its bounds has caused its preferred size to change.
   * Typically, we would simply call revalidate. However, calling
   * that method during layout in a validate traversal will not
   * work, because the parent will set its valid flag to true
   * after layout of this axis. We must therefore call revalidate 
   * later, after the validate traversal is complete.
   */
  private void revalidateLater() {
    if (!_revalidatePending) {
      SwingUtilities.invokeLater(new Runnable() {
        public void run() {
          revalidate();
          _revalidatePending = false;
        }
      });
      _revalidatePending = true;
    }
  }

  // Returns the maximum possible tic label width.
  // This method should be called only when this axis does not yet have axis 
  // tics, perhaps because it does not yet have a tile or width or height.
  // It is used to compute the minimum size of this axis.
  private int maxTicLabelWidth(FontMetrics fm) {
    double vtic = -123456789.0E-10;
    return fm.stringWidth(formatTic(vtic));
  }

  // Formats tic value, removing any trailing zeros after a decimal point.
  private String formatTic(double v) {
    String s = String.format(_format,v);
    s = StringUtil.removeTrailingZeros(s);
    return s;
  }
}
