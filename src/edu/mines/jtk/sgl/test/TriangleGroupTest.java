package edu.mines.jtk.sgl.test;

import java.awt.*;

import edu.mines.jtk.sgl.*;
import edu.mines.jtk.util.*;
import static edu.mines.jtk.opengl.Gl.*;

/**
 * Tests {@link edu.mines.jtk.sgl.TriangleGroup}.
 * @author Dave Hale
 * @version 2006.06.28
 */
public class TriangleGroupTest {

  public static void main(String[] args) {
    float[] xyz = makeSineWave();
    float[] rgb = makeColors(xyz);

    bench(xyz,rgb);

    TriangleGroup tg = new TriangleGroup(xyz,rgb);
    System.out.println("TriangleGroup bounding sphere =\n" +
      tg.getBoundingSphere(true));

    StateSet states = new StateSet();
    ColorState cs = new ColorState();
    cs.setColor(Color.CYAN);
    states.add(cs);
    LightModelState lms = new LightModelState();
    lms.setTwoSide(true);
    states.add(lms);
    MaterialState ms = new MaterialState();
    ms.setColorMaterial(GL_AMBIENT_AND_DIFFUSE);
    ms.setSpecular(Color.white);
    ms.setShininess(100.0f);
    states.add(ms);
    tg.setStates(states);

    World world = new World();
    world.addChild(tg);

    TestFrame frame = new TestFrame(world);
    frame.setVisible(true);
  }

  private static float sin(float x, float y) {
    return (float)(5.0+Math.sin(x+y));
  }

  private static float[] makeSineWave() {
    int nx = 100;
    int ny = 100;
    float dx = 10.0f/(float)nx;
    float dy = 10.0f/(float)ny;
    float[] xyz = new float[3*6*nx*ny];
    for (int ix=0,i=0; ix<nx; ++ix) {
      float x0 = (float)(ix*dx);
      float x1 = (float)((ix+1)*dx);
      for (int iy=0; iy<ny; ++iy) {
        float y0 = (float)(iy*dy);
        float y1 = (float)((iy+1)*dy);
        xyz[i++] = x0;  xyz[i++] = y0;  xyz[i++] = sin(x0,y0);
        xyz[i++] = x0;  xyz[i++] = y1;  xyz[i++] = sin(x0,y1);
        xyz[i++] = x1;  xyz[i++] = y0;  xyz[i++] = sin(x1,y0);
        xyz[i++] = x1;  xyz[i++] = y0;  xyz[i++] = sin(x1,y0);
        xyz[i++] = x0;  xyz[i++] = y1;  xyz[i++] = sin(x0,y1);
        xyz[i++] = x1;  xyz[i++] = y1;  xyz[i++] = sin(x1,y1);
      }
    }
    return xyz;
  }

  private static float[] makeColors(float[] xyz) {
    int nv = xyz.length/3;
    float[] rgb = new float[3*nv];
    for (int iv=0,jv=0,jc=0; iv<nv; ++iv) {
      float x = xyz[jv++];
      float y = xyz[jv++];
      float z = xyz[jv++];
      float s = 1.0f/(float)Math.sqrt(x*x+y*y+z*z);
      rgb[jc++] = x*s;
      rgb[jc++] = y*s;
      rgb[jc++] = z*s;
    }
    return rgb;
  }

  private static void bench(float[] xyz, float[] rgb) {
    System.out.println("Benchmarking TriangleGroup construction");
    int nv = xyz.length/3;
    int nt = nv/3;
    System.out.println("  number of vertices = "+nv);
    System.out.println("  number of triangles = "+nt);

    //int[] ijk = TriangleGroup.indexVertices(xyz);

    TriangleGroup tg;
    int ntg;
    Stopwatch sw = new Stopwatch();
    for (int ntrial=0; ntrial<3; ++ntrial) {
      sw.restart();
      for (ntg=0; sw.time()<1.0; ++ntg)
        //tg = new TriangleGroup(ijk,xyz,rgb);
        tg = new TriangleGroup(xyz,rgb);
      sw.stop();
      double rate = (double)ntg*(double)nt/sw.time();
      System.out.println("  triangles/sec = "+rate);
    }
  }
}
