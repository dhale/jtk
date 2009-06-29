/****************************************************************************
Copyright (c) 2008, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mesh;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.util.*;

import edu.mines.jtk.mosaic.*;

/**
 * Tiled view of a tri mesh.
 *
 * @author Dave Hale, Colorado School of Mines
 * @version 2008.09.11
 */
public class TriMeshView extends TiledView {

  /**
   * Orientation of tri-mesh axes x and y. For example, the default
   * orientation XRIGHT_YUP corresponds to x increasing horizontally
   * from left to right, and y increasing vertically from bottom to top.
   */
  public enum Orientation {
    XRIGHT_YUP,
    XDOWN_YRIGHT
  }
  
  /**
   * Constructs a new view of the specified tri mesh.
   */
  public TriMeshView(TriMesh mesh) {
    setMesh(mesh);
  }

  /**
   * Sets the tri mesh rendered by this view.
   * @param mesh the tri mesh.
   */
  public void setMesh(TriMesh mesh) {
    _mesh = mesh;
    updateMinMax();
    updateBestProjectors();
    repaint();
  }

  /**
   * Sets the orientation of mesh (x,y) axes.
   * @param orientation the orientation.
   */
  public void setOrientation(Orientation orientation) {
    if (_orientation!=orientation) {
      _orientation = orientation;
      updateBestProjectors();
      repaint();
    }
  }

  /**
   * Gets the orientation of (x,y) axes.
   * @return the orientation.
   */
  public Orientation getOrientation() {
    return _orientation;
  }


  /**
   * Sets the visibility (drawing or no drawing) of nodes.
   * @param drawNodes true, to draw nodes; false, otherwise.
   *  The default is true.
   */
  public void setNodesVisible(boolean drawNodes) {
    if (_drawNodes!=drawNodes) {
      _drawNodes = drawNodes;
      repaint();
    }
  }

  /**
   * Sets the visibility (drawing or no drawing) of tris.
   * @param drawTris true, to draw tris; false, otherwise.
   *  The default is true.
   */
  public void setTrisVisible(boolean drawTris) {
    if (_drawTris!=drawTris) {
      _drawTris = drawTris;
      repaint();
    }
  }

  /**
   * Sets the visibility (drawing or no drawing) of subdivided tris.
   * @param drawSubTris true, to draw subdivided tris; false, otherwise.
   *  The default is false.
   */
  public void setSubTrisVisible(boolean drawSubTris) {
    if (_drawSubTris!=drawSubTris) {
      _drawSubTris = drawSubTris;
      repaint();
    }
  }

  /**
   * Sets the visibility (drawing or no drawing) of polygons.
   * @param drawPolys true, to draw polygons; false, otherwise.
   *  The default is false.
   */
  public void setPolysVisible(boolean drawPolys) {
    if (_drawPolys!=drawPolys) {
      _drawPolys = drawPolys;
      repaint();
    }
  }

  /**
   * Sets the visibility (drawing or no drawing) of tri bounds.
   * @param drawTriBounds true, to draw tri bounds; false, otherwise.
   *  The default is false.
   */
  public void setTriBoundsVisible(boolean drawTriBounds) {
    if (_drawTriBounds!=drawTriBounds) {
      _drawTriBounds = drawTriBounds;
      repaint();
    }
  }

  /**
   * Sets the visibility (drawing or no drawing) of poly bounds.
   * @param drawPolyBounds true, to draw poly bounds; false, otherwise.
   *  The default is false.
   */
  public void setPolyBoundsVisible(boolean drawPolyBounds) {
    if (_drawPolyBounds!=drawPolyBounds) {
      _drawPolyBounds = drawPolyBounds;
      repaint();
    }
  }

  /**
   * Shows (draws) the nodes.
   */
  public void showNodes() {
    setNodesVisible(true);
  }

  /**
   * Hides (does not draw) the nodes.
   */
  public void hideNodes() {
    setNodesVisible(false);
  }

  /**
   * Shows (draws) the tris.
   */
  public void showTris() {
    setTrisVisible(true);
  }

  /**
   * Hides (does not draw) the tris.
   */
  public void hideTris() {
    setTrisVisible(false);
  }

  /**
   * Shows (draws) the polygons.
   */
  public void showPolys() {
    setPolysVisible(true);
  }

  /**
   * Hides (does not draw) the polygons.
   */
  public void hidePolys() {
    setPolysVisible(false);
  }

  /**
   * Shows (draws) the tri bounds.
   */
  public void showTriBounds() {
    setTriBoundsVisible(true);
  }

  /**
   * Hides (does not draw) the tri bounds.
   */
  public void hideTriBounds() {
    setTriBoundsVisible(false);
  }

  /**
   * Shows (draws) the poly bounds.
   */
  public void showPolyBounds() {
    setPolyBoundsVisible(true);
  }

  /**
   * Hides (does not draw) the poly bounds.
   */
  public void hidePolyBounds() {
    setPolyBoundsVisible(false);
  }

  /**
   * Sets the color of lines used to draw edges of triangles and polygons.
   * @param lineColor the line color.
   */
  public void setLineColor(Color lineColor) {
    _triLineColor = lineColor;
    _polyLineColor = lineColor;
    repaint();
  }

  /**
   * Sets the color of lines used to draw edges of triangles.
   * @param lineColor the line color.
   */
  public void setTriColor(Color lineColor) {
    _triLineColor = lineColor;
    repaint();
  }

  /**
   * Sets the map from triangle edges to weights.
   * If set, this map overrides the tri line color.
   */
  public void setTriEdgeWeights(Map<TriMesh.Edge,Float> triEdgeWeights) {
    _triEdgeWeights = triEdgeWeights;
    _triEdgeColors = null;
    repaint();
  }

  /**
   * Sets the color of lines used to draw edges of polygons.
   * @param lineColor the line color.
   */
  public void setPolyColor(Color lineColor) {
    _polyLineColor = lineColor;
    repaint();
  }

  /**
   * Sets the width of lines used to draw edges of triangles.
   * @param lineWidth line width in pixels (or points, as for a font).
   */
  public void setLineWidth(int lineWidth) {
    _lineWidth = lineWidth;
    updateBestProjectors();
    repaint();
  }

  /**
   * Sets the color of marks used to draw nodes.
   * @param markColor mark color.
   */
  public void setMarkColor(Color markColor) {
    _markColor = markColor;
    repaint();
  }

  /**
   * Sets the width of marks used to draw nodes.
   * @param markWidth mark width in pixels (or points, as for a font).
   */
  public void setMarkWidth(int markWidth) {
    _markWidth = markWidth;
    updateBestProjectors();
    repaint();
  }

  /**
   * Interface for custom triangle painting.
   */
  public interface TriPainter {

    /**
     * Paints the specified triangle.
     * @param g the graphics context.
     * @param tri the triangle.
     * @param na node A of the triangle.
     * @param xa x pixel coordinate of node A.
     * @param ya y pixel coordinate of node A.
     * @param nb node B of the triangle.
     * @param xb x pixel coordinate of node B.
     * @param yb y pixel coordinate of node B.
     * @param nc node C of the triangle.
     * @param xc x pixel coordinate of node C.
     * @param yc y pixel coordinate of node C.
     */
    public void paint(
      Graphics2D g, TriMesh.Tri tri,
      TriMesh.Node na, int xa, int ya,
      TriMesh.Node nb, int xb, int yb,
      TriMesh.Node nc, int xc, int yc);
  }

  /**
   * Sets the custom triangle painter.
   * @param triPainter the painter.
   */
  public void setTriPainter(TriPainter triPainter) {
    _triPainter = triPainter;
  }
  
  public void paint(Graphics2D g2d) {
    g2d.setRenderingHint(
      RenderingHints.KEY_ANTIALIASING,
      RenderingHints.VALUE_ANTIALIAS_ON);
    Transcaler ts = getTranscaler();

    // Linear transformation from x value to x device coordinates.
    Projector px = getHorizontalProjector();
    double xv0 = px.v0();
    double xv1 = px.v1();
    double xu0 = px.u(xv0);
    double xu1 = px.u(xv1);
    double xd0 = ts.x(xu0);
    double xd1 = ts.x(xu1);
    double xscale = (xd1-xd0)/(xv1-xv0);
    double xshift = 0.5+xd0-xv0*xscale;

    // Linear transformation from y value to y device coordinates.
    Projector py = getVerticalProjector();
    double yv0 = py.v0();
    double yv1 = py.v1();
    double yu0 = py.u(yv0);
    double yu1 = py.u(yv1);
    double yd0 = ts.y(yu0);
    double yd1 = ts.y(yu1);
    double yscale = (yd1-yd0)/(yv1-yv0);
    double yshift = 0.5+yd0-yv0*yscale;

    // If drawing tris or polygons or bounds, ...
    if (_drawTris || _drawPolys || _drawTriBounds || _drawPolyBounds) {

      // Line attributes.
      Stroke defaultStroke = g2d.getStroke();
      Stroke stroke = null;
      GeneralPath path = null;
      if (_lineWidth>1) {
        stroke = new BasicStroke((float)_lineWidth);
        path = new GeneralPath();
      }
      if (stroke!=null)
        g2d.setStroke(stroke);

      // If drawing triangles, draw their edges; this draws each edge twice.
      // If drawing tri bounds, use node colors to draw line segments.
      if (_drawTris || _drawTriBounds) {
        if (_triEdgeWeights!=null && _triEdgeColors==null)
          _triEdgeColors = convertEdgeWeightsToColors(_triEdgeWeights);
        boolean haveEdgeColors = _triEdgeColors!=null;
        TriMesh.NodePropertyMap colorMap = null;
        if (_mesh.hasNodePropertyMap("Color"))
          colorMap = _mesh.getNodePropertyMap("Color");
        g2d.setColor(_triLineColor);
        TriMesh.TriIterator ti = _mesh.getTris();
        while (ti.hasNext()) {
          TriMesh.Tri tri = ti.next();
          if (_mesh.isOuter(tri))
            continue;
          TriMesh.Node na = tri.nodeA();
          TriMesh.Node nb = tri.nodeB();
          TriMesh.Node nc = tri.nodeC();
          Color ca = Color.WHITE;
          Color cb = Color.WHITE;
          Color cc = Color.WHITE;
          if (colorMap!=null) {
            ca = (Color)colorMap.get(na);
            cb = (Color)colorMap.get(nb);
            cc = (Color)colorMap.get(nc);
          }
          Color cab = null;
          Color cbc = null;
          Color cca = null;
          if (_drawTris && haveEdgeColors) {
            cab = _triEdgeColors.get(_mesh.findEdge(na,nb));
            cbc = _triEdgeColors.get(_mesh.findEdge(nb,nc));
            cca = _triEdgeColors.get(_mesh.findEdge(nc,na));
            if (cab==null) cab = _triLineColor;
            if (cbc==null) cbc = _triLineColor;
            if (cca==null) cca = _triLineColor;
          }
          double xaa = xshift+x(na)*xscale;
          double yaa = yshift+y(na)*yscale;
          double xbb = xshift+x(nb)*xscale;
          double ybb = yshift+y(nb)*yscale;
          double xcc = xshift+x(nc)*xscale;
          double ycc = yshift+y(nc)*yscale;
          int xa = (int)xaa;
          int ya = (int)yaa;
          int xb = (int)xbb;
          int yb = (int)ybb;
          int xc = (int)xcc;
          int yc = (int)ycc;
          int xd = 0;
          int yd = 0;
          int xe = 0;
          int ye = 0;
          int xf = 0;
          int yf = 0;
          int xg = 0;
          int yg = 0;
          if (_drawSubTris || _drawTriBounds) {
            double xab = 0.5*(xaa+xbb);
            double yab = 0.5*(yaa+ybb);
            double xbc = 0.5*(xbb+xcc);
            double ybc = 0.5*(ybb+ycc);
            double xca = 0.5*(xcc+xaa);
            double yca = 0.5*(ycc+yaa);
            double xabc = 0.333*(xaa+xbb+xcc);
            double yabc = 0.333*(yaa+ybb+ycc);
            xd = (int)xab;
            yd = (int)yab;
            xe = (int)xbc;
            ye = (int)ybc;
            xf = (int)xca;
            yf = (int)yca;
            xg = (int)xabc;
            yg = (int)yabc;
          }
          if (_drawTris) {
            if (_triPainter!=null)
              _triPainter.paint(g2d,tri,na,xa,ya,nb,xb,yb,nc,xc,yc);
            if (path==null) {
              if (haveEdgeColors) {
                if (cab!=null) g2d.setColor(cab);
                g2d.drawLine(xa,ya,xb,yb);
                if (cbc!=null) g2d.setColor(cbc);
                g2d.drawLine(xb,yb,xc,yc);
                if (cca!=null) g2d.setColor(cca);
                g2d.drawLine(xc,yc,xa,ya);
                g2d.setColor(_triLineColor);
              } else {
                g2d.drawLine(xa,ya,xb,yb);
                g2d.drawLine(xb,yb,xc,yc);
                g2d.drawLine(xc,yc,xa,ya);
              }
              if (_drawSubTris) {
                g2d.drawLine(xd,yd,xe,ye);
                g2d.drawLine(xe,ye,xf,yf);
                g2d.drawLine(xf,yf,xd,yd);
              }
            } else {
              if (haveEdgeColors) {
                GeneralPath gp = new GeneralPath();
                g2d.setColor(cab);
                gp.moveTo(xa,ya); 
                gp.lineTo(xb,yb); 
                g2d.draw(gp); 
                gp.reset();
                g2d.setColor(cbc);
                gp.moveTo(xb,yb);
                gp.lineTo(xc,yc);
                g2d.draw(gp);
                gp.reset();
                g2d.setColor(cbc);
                gp.moveTo(xc,yc);
                gp.lineTo(xa,ya);
                g2d.draw(gp);
                gp.reset();
              } else {
                path.moveTo(xa,ya);
                path.lineTo(xb,yb);
                path.lineTo(xc,yc);
                path.lineTo(xa,ya);
              }
              if (_drawSubTris) {
                path.moveTo(xd,yd);
                path.lineTo(xe,ye);
                path.lineTo(xf,yf);
                path.lineTo(xd,yd);
              }
            }
          }
          if (_drawTriBounds) {
            if (!ca.equals(cb) && !cb.equals(cc) && !cc.equals(ca)) {
              drawLine(g2d,path,xd,yd,xg,yg);
              drawLine(g2d,path,xe,ye,xg,yg);
              drawLine(g2d,path,xf,yf,xg,yg);
            } else if (ca.equals(cb) && !cb.equals(cc)) {
              drawLine(g2d,path,xe,ye,xf,yf);
            } else if (cb.equals(cc) && !cc.equals(ca)) {
              drawLine(g2d,path,xd,yd,xf,yf);
            } else if (cc.equals(ca) && !ca.equals(cb)) {
              drawLine(g2d,path,xd,yd,xe,ye);
            }
          }
        }
      }

      // If drawing polygons or poly bounds, draw their edges (twice).
      if (_drawPolys || _drawPolyBounds) {
        TriMesh.NodePropertyMap colorMap = null;
        if (_mesh.hasNodePropertyMap("Color"))
          colorMap = _mesh.getNodePropertyMap("Color");
        g2d.setColor(_polyLineColor);
        float[] pa = new float[2];
        float[] pb = new float[2];
        float[] pc = new float[2];
        float[] pn = new float[2];
        float[] qc = new float[2];
        TriMesh.TriIterator ti = _mesh.getTris();
        while (ti.hasNext()) {
          TriMesh.Tri tri = ti.next();
          if (_mesh.isOuter(tri))
            continue;
          TriMesh.Node na = tri.nodeA();
          TriMesh.Node nb = tri.nodeB();
          TriMesh.Node nc = tri.nodeC();
          Color ca = Color.WHITE;
          Color cb = Color.WHITE;
          Color cc = Color.WHITE;
          if (colorMap!=null) {
            ca = (Color)colorMap.get(na);
            cb = (Color)colorMap.get(nb);
            cc = (Color)colorMap.get(nc);
          }
          pa[0] = x(na);
          pa[1] = y(na);
          pb[0] = x(nb);
          pb[1] = y(nb);
          pc[0] = x(nc);
          pc[1] = y(nc);
          circumcenter(pa,pb,pc,qc);
          int x1 = (int)(xshift+qc[0]*xscale);
          int y1 = (int)(yshift+qc[1]*yscale);
          if (_drawPolys || !cb.equals(cc)) {
            TriMesh.Tri ta = tri.triNabor(na);
            if (ta!=null && _mesh.isInner(ta)) {
              TriMesh.Node nn = tri.nodeNabor(ta);
              pn[0] = x(nn);
              pn[1] = y(nn);
              circumcenter(pn,pc,pb,qc);
              int x2 = (int)(xshift+qc[0]*xscale);
              int y2 = (int)(yshift+qc[1]*yscale);
              drawLine(g2d,path,x1,y1,x2,y2);
            } else {
              float xb = pb[0];
              float yb = pb[1];
              float xc = pc[0];
              float yc = pc[1];
              /*
              int xs = (int)(xshift+xb*xscale);
              int ys = (int)(yshift+yb*yscale);
              int xt = (int)(xshift+xc*xscale);
              int yt = (int)(yshift+yc*yscale);
              drawLine(g2d,path,xs,ys,xt,yt);
              */
              int x2 = (int)(xshift+0.5f*(xb+xc)*xscale);
              int y2 = (int)(yshift+0.5f*(yb+yc)*yscale);
              drawLine(g2d,path,x1,y1,x2,y2);
            }
          }
          if (_drawPolys || !ca.equals(cc)) {
            TriMesh.Tri tb = tri.triNabor(nb);
            if (tb!=null && _mesh.isInner(tb)) {
              TriMesh.Node nn = tri.nodeNabor(tb);
              pn[0] = x(nn);
              pn[1] = y(nn);
              circumcenter(pn,pa,pc,qc);
              int x2 = (int)(xshift+qc[0]*xscale);
              int y2 = (int)(yshift+qc[1]*yscale);
              drawLine(g2d,path,x1,y1,x2,y2);
            } else {
              float xa = pa[0];
              float ya = pa[1];
              float xc = pc[0];
              float yc = pc[1];
              /*
              int xs = (int)(xshift+xa*xscale);
              int ys = (int)(yshift+ya*yscale);
              int xt = (int)(xshift+xc*xscale);
              int yt = (int)(yshift+yc*yscale);
              drawLine(g2d,path,xs,ys,xt,yt);
              */
              int x2 = (int)(xshift+0.5f*(xa+xc)*xscale);
              int y2 = (int)(yshift+0.5f*(ya+yc)*yscale);
              drawLine(g2d,path,x1,y1,x2,y2);
            }
          }
          if (_drawPolys || !ca.equals(cb)) {
            TriMesh.Tri tc = tri.triNabor(nc);
            if (tc!=null && _mesh.isInner(tc)) {
              TriMesh.Node nn = tri.nodeNabor(tc);
              pn[0] = x(nn);
              pn[1] = y(nn);
              circumcenter(pn,pb,pa,qc);
              int x2 = (int)(xshift+qc[0]*xscale);
              int y2 = (int)(yshift+qc[1]*yscale);
              drawLine(g2d,path,x1,y1,x2,y2);
            } else {
              float xa = pa[0];
              float ya = pa[1];
              float xb = pb[0];
              float yb = pb[1];
              /*
              int xs = (int)(xshift+xa*xscale);
              int ys = (int)(yshift+ya*yscale);
              int xt = (int)(xshift+xb*xscale);
              int yt = (int)(yshift+yb*yscale);
              drawLine(g2d,path,xs,ys,xt,yt);
              */
              int x2 = (int)(xshift+0.5f*(xa+xb)*xscale);
              int y2 = (int)(yshift+0.5f*(ya+yb)*yscale);
              drawLine(g2d,path,x1,y1,x2,y2);
            }
          }
        }
      }
      if (path!=null)
        g2d.draw(path);
      if (stroke!=null)
        g2d.setStroke(defaultStroke);
    }
    
    // If drawing nodes, ...
    if (_drawNodes) {

      // Node attributes.
      g2d.setColor(_markColor);
      int halfWidth = (int)(_markWidth/2.0+0.51);
      int markWidth = 1+2*halfWidth;
      
      // Draw nodes.
      TriMesh.NodePropertyMap colorMap = null;
      if (_mesh.hasNodePropertyMap("Color"))
        colorMap = _mesh.getNodePropertyMap("Color");
      TriMesh.NodeIterator ni = _mesh.getNodes();
      while (ni.hasNext()) {
        TriMesh.Node node = ni.next();
        float xn,yn;
        if (_orientation==Orientation.XRIGHT_YUP) {
          xn = node.x();
          yn = node.y();
        } else {
          xn = node.y();
          yn = node.x();
        }
        int x = (int)(xshift+xn*xscale);
        int y = (int)(yshift+yn*yscale);
        if (colorMap!=null) {
          Color color = (Color)colorMap.get(node);
          if (color!=null) {
            g2d.setColor((Color)colorMap.get(node));
            g2d.fillOval(x-halfWidth,y-halfWidth,markWidth,markWidth);
          } else {
            g2d.setColor(Color.white);
            g2d.drawOval(x-halfWidth,y-halfWidth,markWidth,markWidth);
          }
        } else {
          g2d.fillOval(x-halfWidth,y-halfWidth,markWidth,markWidth);
        }
      }
    }
  }
  private static void drawLine(
    Graphics2D g2d, GeneralPath path, int x1, int y1, int x2, int y2)
  {
    if (path==null) {
      g2d.drawLine(x1,y1,x2,y2);
    } else {
      path.moveTo(x1,y1);
      path.lineTo(x2,y2);
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private TriMesh _mesh;
  private float _xmin,_xmax,_ymin,_ymax;
  private Color _markColor = Color.red;
  private double _markWidth = 6;
  private Color _triLineColor = Color.yellow;
  private Color _polyLineColor = Color.yellow;
  private double _lineWidth = 0;
  private boolean _drawNodes = true;
  private boolean _drawTris = true;
  private boolean _drawPolys = false;
  private boolean _drawSubTris = false;
  private boolean _drawTriBounds = false;
  private boolean _drawPolyBounds = false;
  private TriPainter _triPainter = null;
  private Map<TriMesh.Edge,Float> _triEdgeWeights = null;
  private Map<TriMesh.Edge,Color> _triEdgeColors = null;

  private Orientation _orientation = Orientation.XRIGHT_YUP;

  private float x(TriMesh.Node node) {
    if (_orientation==Orientation.XRIGHT_YUP) {
      return node.x();
    } else {
      return node.y();
    }
  }

  private float y(TriMesh.Node node) {
    if (_orientation==Orientation.XRIGHT_YUP) {
      return node.y();
    } else {
      return node.x();
    }
  }

  /**
   * Called when bounds on (x,y) coordinates in mesh may have changed.
   */
  private void updateMinMax() {
    if (_mesh==null)
      return;
    _xmin = Float.MAX_VALUE;
    _ymin = Float.MAX_VALUE;
    _xmax = -Float.MAX_VALUE;
    _ymax = -Float.MAX_VALUE;
    TriMesh.NodeIterator ni = _mesh.getNodes();
    while (ni.hasNext()) {
      TriMesh.Node node = ni.next();
      float x = node.x();
      float y = node.y();
      if (x<_xmin) _xmin = x;
      if (y<_ymin) _ymin = y;
      if (x>_xmax) _xmax = x;
      if (y>_ymax) _ymax = y;
    }
  }

  /**
   * Called when we might new realignment.
   */
  private void updateBestProjectors() {

    // Assume mark sizes and line widths less than 2% of plot dimensions.
    // The goal is to avoid clipping big marks and wide lines. The problem
    // is that mark sizes and line widths are specified in screen pixels
    // (or points), but margins u0 and u1 are specified in normalized 
    // coordinates, fractions of our tile's width and height. Here, we do 
    // not know those dimensions.
    double u0 = 0.0;
    double u1 = 1.0;
    if ((_drawNodes && _markWidth>1.0f) || _lineWidth>1.0f) {
      u0 = 0.01;
      u1 = 0.99;
    }

    // Best projectors.
    Projector bhp = null;
    Projector bvp = null;
    if (_orientation==Orientation.XRIGHT_YUP) {
      bhp = (_xmin<_xmax)?new Projector(_xmin,_xmax,u0,u1):null;
      bvp = (_ymin<_ymax)?new Projector(_ymax,_ymin,u0,u1):null;
    } else if (_orientation==Orientation.XDOWN_YRIGHT) {
      bhp = (_ymin<_ymax)?new Projector(_ymin,_ymax,u0,u1):null;
      bvp = (_xmin<_xmax)?new Projector(_xmin,_xmax,u0,u1):null;
    }
    setBestProjectors(bhp,bvp);
  }


  private void circumcenter(float[] pa, float[] pb, float[] pc, float[] cc) {
    double xa = pa[0];
    double ya = pa[1];
    double xb = pb[0];
    double yb = pb[1];
    double xc = pc[0];
    double yc = pc[1];
    double xba = xb-xa;
    double yba = yb-ya;
    double xca = xc-xa;
    double yca = yc-ya;
    double dba = xba*xba+yba*yba;
    double dca = xca*xca+yca*yca;
    double scl = 0.5/(xba*yca-yba*xca);
    double xcc = xa+scl*(yca*dba-yba*dca);
    double ycc = ya+scl*(xba*dca-xca*dba);
    cc[0] = (float)xcc;
    cc[1] = (float)ycc;
  }

  /**
   * Converts edge weights to colors.
   */
  private static Map<TriMesh.Edge,Color> convertEdgeWeightsToColors(
      Map<TriMesh.Edge,Float> edgeWeights) 
  {
    float wmin = Float.MAX_VALUE;
    float wmax = -wmin;
    Iterator<Map.Entry<TriMesh.Edge,Float>> ei = 
      edgeWeights.entrySet().iterator();
    while (ei.hasNext()) {
      Map.Entry<TriMesh.Edge,Float> entry = ei.next();
      float w = entry.getValue();
      if (w<wmin) wmin = w;
      if (w>wmax) wmax = w;
    }
    Map<TriMesh.Edge,Color> edgeColors = new HashMap<TriMesh.Edge,Color>();
    float hueMin = 0.000f;
    float hueMax = 0.500f;
    ei = edgeWeights.entrySet().iterator();
    while (ei.hasNext()) {
      Map.Entry<TriMesh.Edge,Float> entry = ei.next();
      TriMesh.Edge e = entry.getKey();
      float w = entry.getValue();
      float hue = hueMin+(w-wmin)*(hueMax-hueMin)/(wmax-wmin);
      Color color = Color.getHSBColor(hue,1.0f,1.0f);
      edgeColors.put(e,color);
    }
    return edgeColors;
  }
}
