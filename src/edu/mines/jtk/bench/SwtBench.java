/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.bench;

import java.io.*;
import java.util.*;
import java.awt.image.*;
import javax.imageio.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

import edu.mines.jtk.util.Stopwatch;

/**
 * SWT workbench.
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.11.02
 */
public class SwtBench {

  public static void main(String[] args) {
    testImage();
    //listScalableFonts();
    //testLineWidth();
    //benchPrimitives();
  }

  public static void testImage() {
    final Display display = new Display();
    //final int dpi = display.getDPI().x;
    final int dpi = 96; // Gnome wants 96
    Image image = createTestImage(display,dpi,600,600);
    System.out.print("Converting image to buffered image ... ");
    BufferedImage bi = convertToBufferedImage(image);
    System.out.println("done.");
    try {
      writeToPng(bi,"ImageSWT.png");
    } catch (IOException ioe) {
      System.out.println(ioe.getMessage());
    }
    Shell shell = new Shell(display);
    shell.setLayout(new FillLayout());
    Canvas canvas = new Canvas(shell,SWT.NONE);
    System.out.println("display dpi="+dpi);
    canvas.addPaintListener(new Painter() {
      public void paintControl(PaintEvent e) {
        GC gc = e.gc;
        Canvas canvas = (Canvas)e.widget;
        Point size = canvas.getSize();
        int width = size.x;
        int height = size.y;
        Image image = createTestImage(display,dpi,width,height);
        gc.drawImage(image,0,0);
        image.dispose();
      }
    });
    shell.setBounds(100,100,600,600);
    shell.open();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch())
        display.sleep();
    }
    display.dispose();
  }
  private static Image createTestImage(
    Display display, int dpi, int width, int height) 
  {
    Image image = new Image(display,width,height);
    Rectangle bounds = image.getBounds();
    GC gc = new GC(image);
    int lineWidth = 1;
    int fontHeight = 72;
    if (dpi>72) {
      double scale = dpi/72.0;
      lineWidth = (int)(lineWidth*scale);
      fontHeight = (int)(fontHeight*scale);
    }
    Font font = new Font(display,"SansSerif",fontHeight,SWT.NORMAL);
    gc.setFont(font);
    gc.setLineWidth(lineWidth);
    gc.setBackground(display.getSystemColor(SWT.COLOR_BLUE));
    gc.fillRectangle(bounds);
    gc.setBackground(display.getSystemColor(SWT.COLOR_GREEN));
    gc.fillOval(0,0,bounds.width,bounds.height);
    gc.setForeground(display.getSystemColor(SWT.COLOR_RED));
    gc.drawLine(0,0,bounds.width-1,bounds.height-1);
    gc.drawLine(bounds.width-1,0,0,bounds.height-1);
    gc.setForeground(display.getSystemColor(SWT.COLOR_BLACK));
    gc.drawString("Tyme (ms) 12345",width*2/4,height*1/4,true);
    gc.drawString("Tyme (ms) 12345",width*3/4,height*2/4,true);
    gc.drawString("Tyme (ms) 12345",width*2/4,height*3/4,true);
    gc.drawString("Tyme (ms) 12345",width*1/4,height*2/4,true);
    font.dispose();
    gc.dispose();
    return image;
  }
  private static BufferedImage convertToBufferedImage(Image image) {
    ImageData data = image.getImageData();
    int w = data.width;
    int h = data.height;
    int d = data.depth;
    PaletteData palette = data.palette;
    if (palette.isDirect) {
      int redMask = palette.redMask;
      int greenMask = palette.greenMask;
      int blueMask = palette.blueMask;
      ColorModel cm = new DirectColorModel(d,redMask,greenMask,blueMask);
      WritableRaster wr = cm.createCompatibleWritableRaster(w,h);
      BufferedImage bi = new BufferedImage(cm,wr,false,null);
      int[] pixel = new int[3];
      for (int y=0; y<h; ++y) {
        for (int x=0; x<w; ++x) {
          int p = data.getPixel(x,y);
          RGB rgb = palette.getRGB(data.getPixel(x,y));
          pixel[0] = rgb.red;
          pixel[1] = rgb.green;
          pixel[2] = rgb.blue;
          wr.setPixels(x,y,1,1,pixel);
        }
      }
      return bi;
    } else {
      RGB[] rgbs = palette.getRGBs();
      int nrgb = rgbs.length;
      byte[] red = new byte[nrgb];
      byte[] green = new byte[nrgb];
      byte[] blue = new byte[nrgb];
      for (int i=0; i<nrgb; ++i) {
        RGB rgb = rgbs[i];
        red[i] = (byte)rgb.red;
        green[i] = (byte)rgb.green;
        blue[i] = (byte)rgb.blue;
      }
      ColorModel cm = null;
      int tp = data.transparentPixel;
      if (tp!=-1) {
        cm = new IndexColorModel(d,nrgb,red,green,blue,tp);
      } else {
        cm = new IndexColorModel(d,nrgb,red,green,blue);
      }
      WritableRaster wr = cm.createCompatibleWritableRaster(w,h);
      BufferedImage bi = new BufferedImage(cm,wr,false,null);
      int[] pixel = new int[1];
      for (int y=0; y<h; ++y) {
        for (int x=0; x<w; ++x) {
          int p = data.getPixel(x,y);
          pixel[0] = data.getPixel(x,y);
          wr.setPixel(x,y,pixel);
        }
      }
      return bi;
    }
  }
  private static void writeToPng(BufferedImage image, String fileName) 
    throws IOException
  {
    File file = new File(fileName);
    ImageIO.write(image,"png",file);
  }

  public static void listScalableFonts() {
    System.out.println("Scalable fonts:");
    Display display = new Display();
    FontData[] list = display.getFontList(null,true);
    for (FontData fd : list) {
      String name = fd.getName();
      int height = fd.getHeight();
      String style = "normal";
      if (fd.getStyle()==(SWT.NORMAL|SWT.ITALIC)) {
        style = "italic";
      } else if (fd.getStyle()==SWT.BOLD) {
        style = "bold";
      } else if (fd.getStyle()==(SWT.BOLD|SWT.ITALIC)) {
        style = "bold italic";
      }
      System.out.println(name+" "+style+" "+height);
    }
    display.dispose();
  }

  public static void testLineWidth() {
    final Display display = new Display();
    Shell shell = new Shell(display);
    Canvas canvas = new Canvas(shell,SWT.NONE);
    canvas.addPaintListener(new Painter() {
      public void paintControl(PaintEvent e) {
        GC gc = e.gc;
        Canvas canvas = (Canvas)e.widget;
        Point size = canvas.getSize();
        int w = size.x;
        int h = size.y;
        gc.setBackground(canvas.getBackground());
        gc.setForeground(canvas.getForeground());
        gc.fillRectangle(0,0,w,h);

        int x1 = 10;
        int y1 = 10;
        int x2 = w-1-x1;
        int y2 = h-1-y1;
        gc.drawLine(x1,y1,x2,y1);
        gc.drawLine(x2,y1,x2,y2);
        gc.drawLine(x2,y2,x1,y2);
        gc.drawLine(x1,y2,x1,y1);
        gc.drawLine(x1,y1,x2,y2);
        gc.drawLine(x2,y1,x1,y2);

        int xa = x1+50;
        int ya = y1+50;
        int xb = x2-50;
        int yb = y2-50;
        gc.setLineWidth(5); // change to see how thick lines are drawn
        gc.drawLine(xa,y1,xb,y1);
        gc.drawLine(x2,ya,x2,yb);
        gc.drawLine(xa,y2,xb,y2);
        gc.drawLine(x1,ya,x1,yb);
        gc.drawLine(xa,ya,xb,yb);
        gc.drawLine(xb,ya,xa,yb);
      }
    });
    canvas.setSize(600,600);
    shell.setLocation(100,100);
    shell.open();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch())
        display.sleep();
    }
    display.dispose();
  }

  public static void benchPrimitives() {
    Display display = new Display();
    Shell shell = new Shell(display);
    shell.setText("Draw lines");
    shell.setLayout(new FillLayout());
    Canvas canvas = new Canvas(shell,SWT.NONE);
    Painter painter = new Painter();
    canvas.addControlListener(painter);
    canvas.addPaintListener(painter);
    shell.setBounds(100,100,600,600);
    shell.open();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch())
        display.sleep();
    }
    display.dispose();
  }
  private static class Painter implements PaintListener,ControlListener {
    private Painter() {
      _n = 1000;
      _p = new int[2*_n];
    }
    public void controlMoved(ControlEvent e) {
    }
    public void controlResized(ControlEvent e) {
      Random random = new Random();
      Rectangle bounds = ((Control)e.widget).getBounds();
      double width = (double)bounds.width;
      double height = (double)bounds.height;
      for (int i=0,j=0; i<_n; ++i,j+=2) {
        _p[j+X] = (int)(width*random.nextDouble());
        _p[j+Y] = (int)(height*random.nextDouble());
      }
    }
    public void paintControl(PaintEvent e) {
      GC gc = e.gc;
      Stopwatch sw = new Stopwatch();
      int ndraw,rate;
      double maxtime = 1.0;

      // Lines.
      System.out.print("Drawing lines: ");
      sw.restart();
      for (ndraw=0; sw.time()<maxtime; ++ndraw) {
        for (int i=0,j=0; j<2*_n; ++i) {
          int x1 = _p[j++];
          int y1 = _p[j++];
          int x2 = _p[j++];
          int y2 = _p[j++];
          gc.drawLine(x1,y1,x2,y2);
        }
      }
      sw.stop();
      rate = (int)((double)ndraw*(_n/2)/sw.time());
      System.out.println("lines/sec = "+rate);

      // Polylines.
      System.out.print("Drawing polylines: ");
      sw.restart();
      for (ndraw=0; sw.time()<maxtime; ++ndraw) {
        gc.drawPolyline(_p);
      }
      sw.stop();
      rate = (int)((double)ndraw*(_n-1)/sw.time());
      System.out.println("lines/sec = "+rate);
    }
    private int _n;
    private int[] _p;
    private static int X = 0;
    private static int Y = 1;
  }

  public static void testNative() {
    Display display = new Display();
    Shell shell = new Shell(display);
    System.out.println("shell handle="+shell.handle);
    printHandleNative(shell.handle);
    shell.open();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch())
        display.sleep();
    }
    display.dispose();
  }

  public static void drawLabelWithImage() {
    Display display = new Display();
    Image image = new Image(display,16,16);
    Color color = display.getSystemColor(SWT.COLOR_RED);
    GC gc = new GC(image);
    gc.setBackground(color);
    gc.fillRectangle(image.getBounds());
    gc.dispose();
    Shell shell = new Shell(display);
    Label label = new Label(shell,SWT.BORDER);
    label.setImage(image);
    label.pack();
    shell.pack();
    shell.open();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch())
        display.sleep();
    }
    image.dispose();
    display.dispose();
  }

  private static native void printHandleNative(int handle);

  static {
    System.loadLibrary("edu_mines_jtk_bench");
  }
}
