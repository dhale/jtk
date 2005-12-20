/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.mosaic;

import static java.lang.Math.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.imageio.*;
import javax.imageio.metadata.*;
import javax.imageio.stream.*;
import javax.swing.*;

/**
 * A panel that can paint itself to fit an image.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.12.19
 */
public class IPanel extends JPanel {
  private static final long serialVersionUID = 1L;

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
    int wimage = (int)(scale*(wpanel-1)+1.5);
    int himage = (int)(scale*(hpanel-1)+1.5);
    int type = BufferedImage.TYPE_INT_RGB;
    BufferedImage image = new BufferedImage(wimage,himage,type);
    paintToImage(image);
    return image;
  }

  /**
   * Paints this panel to fit the specified image.
   * @param image the image.
   */
  public void paintToImage(BufferedImage image) {
    Rectangle rp = new Rectangle(0,0,getWidth(),getHeight());
    paintToImage(rp,image);
  }

  /**
   * Paints the specified rectangle of this panel to fit the specified image.
   * @param rp the rectangle of this panel to be painted.
   * @param image the image.
   */
  public void paintToImage(Rectangle rp, BufferedImage image) {
    int wpanel = rp.width;
    int hpanel = rp.height;
    int wimage = image.getWidth();
    int himage = image.getHeight();

    // Graphics context with scaled font and line width.
    double wscale = (double)wimage/(double)wpanel;
    double hscale = (double)himage/(double)hpanel;
    double scale = min(wscale,hscale);
    Graphics2D g2d = createGraphics(image,scale);

    // Clear the image.
    g2d.setColor(getBackground());
    g2d.fillRect(0,0,wimage,himage);
    g2d.setColor(getForeground());

    // Paint this panel to the entire image.
    Rectangle rg = new Rectangle(0,0,wimage,himage);
    paintToRect(g2d,rp,rg);
  }

  /**
   * Paints this panel to a rectangle of a graphics context.
   * Clips painting to the intersection of the current clip rectangle
   * and the specified rectangle of the graphics context.
   * <p>
   * Classes that extend this base class typically override this method.
   * <p>
   * This implementation simply paints any children of this panel. It paints 
   * nothing itself and restores any changes to the graphics context.
   * @param g2d the graphics context.
   * @param rg the graphics rectangle.
   */
  public void paintToRect(Graphics2D g2d, Rectangle rg) {
    Rectangle rp = new Rectangle(0,0,getWidth(),getHeight());
    paintToRect(g2d,rp,rg);
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
    Iterator i = ImageIO.getImageWritersBySuffix("png");
    if (!i.hasNext())
      throw new IOException("cannot get a PNG image writer");
    ImageWriter iw = (ImageWriter)i.next();
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
   * Creates a new graphics context for the specified image.
   * Sets the font and line width for the specified scale factor.
   * @param image the image.
   * @param scale the scale factor.
   */
  protected Graphics2D createGraphics(BufferedImage image, double scale) {
    int wimage = image.getWidth();
    int himage = image.getHeight();

    // Turn on anti-aliasing.
    Graphics2D g2d = image.createGraphics();
    g2d.setRenderingHint(
      RenderingHints.KEY_ANTIALIASING,
      RenderingHints.VALUE_ANTIALIAS_ON);

    // Scaled line width rounded to an odd number of pixels.
    float lineWidth = 1.0f+(float)(2.0*(int)(scale/2.0));
    g2d.setStroke(new BasicStroke(lineWidth));

    // Scale the font size.
    Font font = getFont();
    float fontSize = font.getSize2D();
    fontSize *= (float)scale;
    font = font.deriveFont(fontSize);
    g2d.setFont(font);

    return g2d;
  }

  /**
   * Paints a rectangle of this panel to a rectangle of a graphics context.
   * Clips painting to the intersection of the current clip rectangle
   * and the specified rectangle of the graphics context.
   * <p>
   * This implementation simply paints any children of this panel. It paints 
   * nothing itself and restores any changes to the graphics context.
   * @param g2d the graphics context.
   * @param rp the panel rectangle.
   * @param rg the graphics rectangle.
   */
  protected void paintToRect(Graphics2D g2d, Rectangle rp, Rectangle rg) {
    Rectangle clipRect = g2d.getClipBounds();
    g2d.setClip((clipRect==null)?rg:clipRect.intersection(rg));
    int xp = rp.x;
    int yp = rp.y;
    int wp = rp.width;
    int hp = rp.height;
    int xg = rg.x;
    int yg = rg.y;
    int wg = rg.width;
    int hg = rg.height;
    g2d.translate(xg-xp,yg-yp);
    double ws = (double)wg/(double)wp;
    double hs = (double)hg/(double)hp;
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
      g2d.translate(xc,yc);
      if (c instanceof IPanel) {
        Rectangle rc = new Rectangle(xc,yc,wc,hc);
        ((IPanel)c).paintToRect(g2d,rc);
      } else {
        g2d.scale(ws,hs);
        c.paint(g2d);
        g2d.scale(1.0/ws,1.0/hs);
      }
      g2d.translate(-xc,-yc);
    }
    g2d.translate(xp-xg,yp-yg);
    g2d.setClip(clipRect);
  }
}
