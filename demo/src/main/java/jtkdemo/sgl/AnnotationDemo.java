package jtkdemo.sgl;

import edu.mines.jtk.sgl.*;

import javax.swing.*;
import java.awt.*;

public class AnnotationDemo {

  public static void main(final String[] args) {
    SwingUtilities.invokeLater(new Runnable() {
      public void run() {
        go(args);
      }
    });
  }

  public static void go(String[] args) {
    demo0();
  }

  private static void demo0() {
    SimpleFrame sf = new SimpleFrame();
    World world = sf.getWorld();
    sf.getViewCanvas().setBackground(Color.BLACK);
    float[] c = cube();
    int np = c.length/3;
    Point3 p;
    for (int i=0; i<np; ++i) {
      p = new Point3(c[3*i],c[3*i+1],c[3*i+2]);
      world.addChild(new Annotation(p));
    }
    StateSet ss = new StateSet();
    PointState ps = new PointState();
    ps.setSize(10f);
    ColorState cs = new ColorState();
    cs.setColor(Color.RED);
    ss.add(ps);
    ss.add(cs);
    world.setStates(ss);

    PointGroup pg = new PointGroup(c);
    world.addChild(pg);
  }

  private static float[] cube() {
    return new float[] {
      1.0f, 0.0f, 0.0f,
      1.0f, 1.0f, 0.0f,
      1.0f, 1.0f, 1.0f,
      1.0f, 0.0f, 1.0f,
      0.0f, 0.0f, 1.0f,
      0.0f, 1.0f, 1.0f,
      0.0f, 1.0f, 0.0f,
      0.0f, 0.0f, 0.0f
    };
  }
}
