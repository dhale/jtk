package edu.mines.jves.bench;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * SWT workbench.
 * @author Dave Hale
 * @version 2004.11.02
 */
public class SwtBench {

  public static void simple() {
    Display display = new Display();
    Shell shell = new Shell(display);
    System.out.println("shell handle="+shell.handle);
    printHandle(shell.handle);
    shell.open();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch())
        display.sleep();
    }
    display.dispose();
  }

  public static void drawRectangle() {
    Display display = new Display();
    Shell shell = new Shell(display);
    shell.setText("Draw rectangle");
    shell.open();
    GC gc = new GC(shell);
    gc.setLineWidth(4);
    gc.drawRectangle(20,20,100,100);
    gc.dispose();
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

  public static void main(String[] args) {
    simple();
  }

  private static native void printHandle(int handle);

  static {
    System.loadLibrary("edu_mines_jves_bench");
  }
}
