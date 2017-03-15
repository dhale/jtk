/****************************************************************************
 Copyright 2017, Colorado School of Mines and others.
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
package jtkdemo.sgl;

import edu.mines.jtk.sgl.*;
import edu.mines.jtk.util.MathPlus;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.Random;

import static edu.mines.jtk.util.MathPlus.cos;

/**
 * Demos {@link edu.mines.jtk.sgl.Annotation}.
 *
 * @author Chris Engelsma
 * @version 2017.03.15
 */
public class AnnotationDemo {

  public static void main(final String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        go(args);
      }
    });
  }

  public static void go(String[] args) {
    demo0(10);
  }

  private static float[] generate(int n) {
    Random r = new Random();
    float[] c = new float[n*3];
    float x,y,z;
    for (int i=0; i<n; ++i) {
      x = (float)cos(2* MathPlus.PI*i);
      y = i*10;
      z = r.nextFloat() * 100;
      c[3*i  ] = x;
      c[3*i+1] = y;
      c[3*i+2] = z;
    }
    return c;
  }

  private static void demo0(int n) {
    Annotation[] ans = new Annotation[2*n];
    float[] c = generate(n);
    float x,y,z;

    for (int i=0,k=0; i<n; ++i) {
      x = c[3*i  ];
      y = c[3*i+1];
      z = c[3*i+2];

      String text0 = "(" + fmt(x) + ", " + fmt(y) + ")";
      Annotation a0 = new Annotation(x,y,z,text0);
      a0.setColor(new Color(194,91,86));

      Annotation a1 = new Annotation(x,y,z,fmt(z));
      a1.setAlignment(Annotation.Alignment.WEST);
      a1.setColor(new Color(111, 190, 155));

      ans[k++] = a0;
      ans[k++] = a1;
    }

    SimpleFrame sf = new SimpleFrame();
    World world = sf.getWorld();
    sf.getViewCanvas().setBackground(new Color(254,246,235));
    StateSet ss = new StateSet();
    PointState ps = new PointState();
    ps.setSize(10f);
    ColorState cs = new ColorState();
    cs.setColor(new Color(82,85,100));
    ss.add(ps);
    ss.add(cs);
    world.setStates(ss);

    PointGroup pg = new PointGroup(c);
    LineGroup lg = new LineGroup(c);
    world.addChild(pg);
    world.addChild(lg);
    for (Annotation a: ans) world.addChild(a);
  }

  private static String fmt(float f) {
    return new DecimalFormat("#.##").format(f);
  }
}
