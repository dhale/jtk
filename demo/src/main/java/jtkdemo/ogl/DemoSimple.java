/****************************************************************************
Copyright 2006, Colorado School of Mines and others.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
****************************************************************************/
package jtkdemo.ogl;

import java.awt.*;
import javax.swing.*;

import edu.mines.jtk.ogl.GlCanvas;

/**
 * Simple OpenGL test harness. 
 * Constructs a frame that contains the specified OpenGL canvas.
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.07.08
 */
class DemoSimple {
  public static void run(String[] args, GlCanvas canvas) {
    run(args,canvas,false);
  }
  public static void run(
    String[] args, GlCanvas canvas, boolean autoRepaint) 
  {
    run(canvas,autoRepaint);
  }
  public static void run(GlCanvas canvas, boolean autoRepaint) {
    run(canvas,autoRepaint,null);
  }
  public static void run(
    final GlCanvas canvas, final boolean autoRepaint, final String fileName) 
  {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        canvas.setAutoRepaint(autoRepaint);
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(SIZE,SIZE));
        frame.getContentPane().add(canvas,BorderLayout.CENTER);
        frame.setVisible(true);
        if (fileName!=null)
          canvas.paintToFile(fileName);
      }
    });
  }
  private static final int SIZE = 600;
}
