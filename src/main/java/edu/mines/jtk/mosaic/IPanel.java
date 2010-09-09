/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import static java.lang.Math.*;
import java.util.Iterator;
import javax.imageio.*;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.ImageOutputStream;
import javax.swing.*;

/**
 * A panel that can paint itself to fit an image. 
 * Some components in this package, such as mosaics, tiles, and tile axes,
 * must be able to render themselves to images as well as on screen. For
 * various reasons, those images often have resolution that is higher 
 * than that of a display screen. Simply scaling an on-screen rendering
 * does not exploit this higher resolution, because screen coordinates 
 * are typically specified as integers. Rounding to the nearest integer
 * screen coordinates and then scaling to a high resolution image yields
 * visual artifacts, such as curves that appear jagged in the image.
 * <p>
 * Classes that extend this base class work differently. They paint
 * themselves to fit any specified rectangle of a specified graphics 
 * context. When painting to a display screen, that graphics rectangle 
 * is simply the panel's rectangle, in screen coordinates. However, when 
 * painting to an image, the dimensions of that rectangle may be much 
 * larger, corresponding to the higher resolution of the image. When
 * painting, these panels round coordinates to the nearest pixel of that 
 * graphics rectangle, not the panel's on-screen rectangle. In this way, 
 * panels can paint themselves with any desired resolution.
 * <p>
 * One complication is font size. Another is line width. Such properties
 * are typically specified in points, which are roughly equivalant to 
 * on-screen pixels. Therefore, when drawing to a high-resolution image, 
 * font sizes and line widths must be increased. This base class provides 
 * methods that panels in this package use to properly scale font sizes, 
 * line widths, and other resolution-dependent properties.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.12.21
 */
public class IPanel extends JPanel {
  private static final long serialVersionUID = 1L;

  /**
   * Paints this panel to a specified rectangle of a graphics context.
   * This implementation simply paints any IPanel children of this panel. 
   * It ignores and does not draw any children that are not IPanels.
   * <p>
   * Classes that extend this base class typically override this method
   * to draw something besides children of this panel. When appropriate,
   * those extensions may also call this method.
   * @param g2d the graphics context.
   * @param x the x-coordinate of the graphics rectangle.
   * @param y the y-coordinate of the graphics rectangle.
   * @param w the width of the graphics rectangle.
   * @param h the height of the graphics rectangle.
   */
  public void paintToRect(Graphics2D g2d, int x, int y, int w, int h) {
    g2d = createGraphics(g2d,x,y,w,h);

    // Paint any IPanel children.
    double ws = (double)w/(double)getWidth();
    double hs = (double)h/(double)getHeight();
    int nc = getComponentCount();
    for (int ic=0; ic<nc; ++ic) {
      Component c = getComponent(ic);
      int xc = c.getX();
      int yc = c.getY();
      int wc = c.getWidth();
      int hc = c.getHeight();
      xc = (int)round(xc*ws);
      yc = (int)round(yc*hs);
      wc = (int)round(wc*ws);
      hc = (int)round(hc*hs);
      if (c instanceof IPanel) {
        IPanel ip = (IPanel)c;
        ip.paintToRect(g2d,xc,yc,wc,hc);
      }
    }

    g2d.dispose();
  }

  /**
   * Paints this panel to fit the specified image.
   * @param image the image.
   */
  public void paintToImage(BufferedImage image) {
    Graphics2D g2d = image.createGraphics();

    // Set panel properties.
    g2d.setColor(getBackground());
    g2d.fillRect(0,0,image.getWidth(),image.getHeight());
    g2d.setColor(getForeground());
    g2d.setFont(getFont());

    // Turn on anti-aliasing.
    g2d.setRenderingHint(
      RenderingHints.KEY_ANTIALIASING,
      RenderingHints.VALUE_ANTIALIAS_ON);

    // Paint this panel to fit the image.
    paintToRect(g2d,0,0,image.getWidth(),image.getHeight());

    g2d.dispose();
  }

  /**
   * Paints this panel to fit a new image with specified width in pixels.
   * The image height is computed so that the image has the same aspect 
   * ratio as this panel.
   * @param width the image width, in pixels.
   * @return the image.
   */
  public BufferedImage paintToImage(int width) {
    int wpanel = getWidth();
    int hpanel = getHeight();
    double scale = (double)width/(double)wpanel;
    int wimage = (int)(scale*wpanel+0.5);
    int himage = (int)(scale*hpanel+0.5);
    int type = BufferedImage.TYPE_INT_RGB;
    BufferedImage image = new BufferedImage(wimage,himage,type);
    paintToImage(image);
    return image;
  }

  /**
   * Paints this panel to a PNG image with specified resolution and width.
   * The image height is computed so that the image has the same aspect 
   * ratio as this panel.
   * @param dpi the image resolution, in dots per inch.
   * @param win the image width, in inches.
   * @param fileName the name of the file to contain the PNG image.  
   */
  public void paintToPng(double dpi, double win, String fileName) 
    throws IOException 
  {
    BufferedImage image = paintToImage((int)ceil(dpi*win));
    // The two lines below are simple, but do not write resolution info to 
    // the PNG file. We want that info, especially for high-res images.
    //File file = new File(fileName);
    //ImageIO.write(image,"png",file);
    Iterator<ImageWriter> i = ImageIO.getImageWritersBySuffix("png");
    if (!i.hasNext())
      throw new IOException("cannot get a PNG image writer");
    ImageWriter iw = i.next();
    FileOutputStream fos = new FileOutputStream(fileName);
    ImageOutputStream ios = ImageIO.createImageOutputStream(fos);
    iw.setOutput(ios);
    ImageWriteParam iwp = iw.getDefaultWriteParam();
    ImageTypeSpecifier its = new ImageTypeSpecifier(image);
    IIOMetadata imd = iw.getDefaultImageMetadata(its,iwp);
    String format = "javax_imageio_png_1.0";
    IIOMetadataNode tree = (IIOMetadataNode)imd.getAsTree(format);
    IIOMetadataNode node = new IIOMetadataNode("pHYs");
    String dpm = Integer.toString((int)ceil(dpi/0.0254));
    node.setAttribute("pixelsPerUnitXAxis",dpm);
    node.setAttribute("pixelsPerUnitYAxis",dpm);
    node.setAttribute("unitSpecifier","meter");
    tree.appendChild(node);
    imd.setFromTree(format,tree);
    iw.write(new IIOImage(image,null,imd));
    ios.flush();
    ios.close();
    fos.flush();
    fos.close();
    iw.dispose();
  }

  ///////////////////////////////////////////////////////////////////////////
  // protected

  /**
   * Returns a scale factor for painting this panel to fit a rectangle.
   * The scale factor depends on the specified width and height and the
   * corresponding width and height of this panel.
   * @param w the rectangle width.
   * @param h the rectangle height.
   * @return the scale factor.
   */
  protected double computeScale(int w, int h) {
    double wscale = (double)w/(double)getWidth();
    double hscale = (double)h/(double)getHeight();
    double scale = min(wscale,hscale);
    return scale;
  }

  /**
   * Creates a graphics context for the specified graphics rectangle.
   * First computes a scale factor from the dimensions of this panel 
   * and those of the specified graphics rectangle. Then 
   * (1) sets the clip rectangle using the specified graphics rectangle,
   * (2) translates the coordinate system by the specified (x,y), 
   * (3) sets the line width, and 
   * (4) sets the font to be a scaled version of this panel's font.
   * <p>
   * Classes that extend this base class typically call this method in 
   * their implementation of 
   * {@link #paintToRect(java.awt.Graphics2D,int,int,int,int)}.
   * When painting to a high-resolution image, this method makes lines 
   * and text appear as they would on screen, neither too thin nor too 
   * small.
   * <p>
   * When the returned graphics context is no longer needed, it should
   * be disposed.
   * @param g2d the graphics context.
   * @param x the x-coordinate of the graphics rectangle.
   * @param y the y-coordinate of the graphics rectangle.
   * @param w the width of the graphics rectangle.
   * @param h the height of the graphics rectangle.
   */
  protected Graphics2D createGraphics(
    Graphics2D g2d, int x, int y, int w, int h) 
  {
    g2d = (Graphics2D)g2d.create();

    // Scale factor.
    double scale = computeScale(w,h);

    // Clip rectangle.
    Rectangle clipRect = g2d.getClipBounds();
    Rectangle g2dRect = new Rectangle(x,y,w,h);
    g2d.setClip((clipRect==null)?g2dRect:clipRect.intersection(g2dRect));

    // Translate.
    g2d.translate(x,y);

    // Scaled line width.
    float lineWidth = (float)scale;
    g2d.setStroke(new BasicStroke(lineWidth));

    // Scaled font.
    Font font = getFont();
    float fontSize = (float)scale*font.getSize2D();
    g2d.setFont(font.deriveFont(fontSize));

    return g2d;
  }

  /**
   * Gets the line width for the specified graphics context.
   * @param g2d the graphics context.
   * @return the line width.
   */
  protected float getLineWidth(Graphics2D g2d) {
    float lineWidth = 1.0f;
    Stroke stroke = g2d.getStroke();
    if (stroke instanceof BasicStroke) {
      BasicStroke bs = (BasicStroke)stroke;
      lineWidth = bs.getLineWidth();
    }
    return lineWidth;
  }

  /**
   * Scales the line width for the specified graphics context.
   * Classes that extend this base class should not assume that the
   * current line width is one, because the line width may have been
   * set to a larger value in any graphics context created by 
   * {@link #createGraphics(java.awt.Graphics2D,int,int,int,int)}.
   * @param g2d the graphics context.
   * @param scale the scale factor.
   */
  protected void scaleLineWidth(Graphics2D g2d, double scale) {
    float lineWidth = getLineWidth(g2d);
    lineWidth *= scale;
    g2d.setStroke(new BasicStroke(lineWidth));
  }
}
