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
 * A tile axis in a mosaic.
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.12.27
 */
public class TileAxis extends JPanel {

  public static final int TOP = 1;
  public static final int LEFT = 2;
  public static final int BOTTOM = 3;
  public static final int RIGHT = 4;

  public int getPlacement() {
    return _placement;
  }

  public int getIndex() {
    return _index;
  }

  public void setFont(Font font) {
    super.setFont(font);
    if (_mosaic!=null)
      _mosaic.validate();
  }

  public void setLabel(String label) {
    _label = label;
    _mosaic.validate();
  }

  ///////////////////////////////////////////////////////////////////////////
  // package

  TileAxis(Mosaic mosaic, int placement, int index) {
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
    Graphics g = getGraphics();
    FontMetrics fm = g.getFontMetrics();
    int width = 0;
    if (isVertical()) {
      width = countTicChars()*fm.charWidth('3')+fm.getHeight();
      if (_label!=null)
        width += fm.getHeight();
    } else {
      width = 20;
      if (_label!=null)
        width = Math.max(width,fm.stringWidth(_label));
    }
    g.dispose();
    return width;
  }

  /**
   * Gets the height minimum for this axis. This height does not include 
   * any border that will be drawn by the mosaic.
   */
  int getHeightMinimum() {
    Graphics g = getGraphics();
    FontMetrics fm = g.getFontMetrics();
    int height = 0;
    if (isHorizontal()) {
      height = 2*fm.getHeight();
      if (_label!=null)
        height += fm.getHeight();
    } else {
      height = 20;
      if (_label!=null)
        height = Math.max(height,fm.stringWidth(_label));
    }
    g.dispose();
    return height;
  }

  ///////////////////////////////////////////////////////////////////////////
  // protected

  protected void paintComponentX(Graphics g) {
    super.paintComponent(g);
    int width = getWidth();
    int height = getHeight();
    g.setColor(Color.RED);
    g.fillRect(0,0,width,height);
    FontMetrics fm = g.getFontMetrics();
    int sw = fm.stringWidth("Axis");
    int sh = fm.getAscent();
    int x = width/2-sw/2;
    int y = height/2+sh/2;
    g.setColor(Color.BLACK);
    g.drawString("Axis",x,y);
  }

  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D)g;

    // Axis size.
    int w = getWidth();
    int h = getHeight();

    // Font dimensions.
    Font font = getFont();
    FontMetrics fm = g.getFontMetrics();
    FontRenderContext frc = g2d.getFontRenderContext();
    LineMetrics lm = font.getLineMetrics("0.123456789",frc);
    int fh = round(lm.getHeight());
    int fa = round(lm.getAscent());
    int fd = round(lm.getDescent());
    int fl = round(lm.getLeading());

    // Length of major tics.
    int tl = 2*fa/3;

    // Projector and transcaler of adjacent tile.
    Projector p = getProjector();
    Transcaler t = getTranscaler();

    // Axis placement.
    boolean isHorizontal = isHorizontal();
    boolean isTop = isTop();
    boolean isLeft = isLeft();

    // Axis tics.
    AxisTics at;
    if (isHorizontal) {
      int nmax = 6; // TODO: compute maximum number of tics to fit
      double vmin = min(p.v0(),p.v1());
      double vmax = max(p.v0(),p.v1());
      double v0 = max(vmin,min(vmax,p.v(t.x(0))));
      double v1 = max(vmin,min(vmax,p.v(t.x(w-1))));
      at = new AxisTics(v0,v1,nmax);
    } else {
      int nmax = 4; // TODO: compute maximum number of tics to fit
      double vmin = min(p.v0(),p.v1());
      double vmax = max(p.v0(),p.v1());
      double v0 = max(vmin,min(vmax,p.v(t.y(0))));
      double v1 = max(vmin,min(vmax,p.v(t.y(h-1))));
      at = new AxisTics(v0,v1,nmax);
    }
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
          int x = t.x(utic);
          if (isTop) {
            g2d.drawLine(x,h-1,x,h-1-tl/2);
          } else {
            g2d.drawLine(x,0,x,tl/2);
          }
        } else {
          int y = t.y(utic);
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
    for (int itic=0; itic<nticMajor; ++itic) {
      double vtic = fticMajor+itic*dticMajor;
      double utic = p.u(vtic);
      String stic = formatTic(vtic);
      if (isHorizontal) {
        int x = t.x(utic);
        int y;
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
        int x;
        int y = t.y(utic);
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
        int ys = max(fa,min(h-1,y+fa/2));
        g2d.drawString(stic,xs,ys);
      }
    }

    // Axis label.
    if (isHorizontal) {
      int wl = fm.stringWidth(_label);
      int xl = max(0,min(w-wl,(w-wl)/2));
      int yl = isTop?h-1-tl-fh-fd:tl+fh+fa;
      g2d.drawString(_label,xl,yl);
    } else {
      int wl = fm.stringWidth(_label);
      int xl = isLeft?max(fa,w-1-tl-fd-wsmax-fd-fl):min(w-1-fd,tl+fd+wsmax+fh);
      int yl = max(wl,min(h,(h+wl)/2));
      g2d.translate(xl,yl);
      g2d.rotate(-PI/2.0);
      g2d.drawString(_label,0,0);
      g2d.rotate(PI/2.0);
      g2d.translate(-xl,-yl);
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private Mosaic _mosaic;
  private int _placement;
  private int _index;
  private String _format = "%1.6g";
  private String _label = "Axis Label";

  // Formats tic value, removing any trailing zeros and decimal point.
  private String formatTic(double v) {
    String s = String.format(_format,v);
    int len = s.length();
    int iend = s.indexOf('e');
    if (iend<0)
      iend = s.indexOf('E');
    if (iend<0)
      iend = len;
    int ibeg = iend;
    while (ibeg>0 && s.charAt(ibeg-1)=='0')
      --ibeg;
    if (ibeg>0 && s.charAt(ibeg-1)=='.')
      --ibeg;
    if (ibeg<iend) {
      String sb = s.substring(0,ibeg);
      s = (iend<len)?sb+s.substring(iend,len):sb;
    }
    return s;
  }

  private boolean isTop() {
    return _placement==TOP;
  }

  private boolean isLeft() {
    return _placement==LEFT;
  }

  private boolean isBottom() {
    return _placement==BOTTOM;
  }

  private boolean isRight() {
    return _placement==RIGHT;
  }

  private boolean isHorizontal() {
    return _placement==TOP || _placement==BOTTOM;
  }

  private boolean isVertical() {
    return _placement==LEFT || _placement==RIGHT;
  }

  private int countTicChars() {
    return 8;
  }

  private Tile getTile() {
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
  
  private Projector getProjector() {
    return (isHorizontal()) ?
      getTile().getHorizontalProjector() :
      getTile().getVerticalProjector();
  }
  
  private Transcaler getTranscaler() {
    return getTile().getTranscaler();
  }
}
