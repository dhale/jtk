package jtkdemo.sgl;

import edu.mines.jtk.sgl.*;
import java.awt.*;

import static edu.mines.jtk.sgl.OrbitViewLighting.*;

/**
 * Demos {@link OrbitViewLighting}.
 * @author Chris Engelsma
 * @version 2017.01.17
 */
public class OrbitViewLightingDemo {

  public static void main(String[] args) {
    demo0();
    demo1();
  }

  private static void demo0() {
    Sphere sphere = new Sphere();
    StateSet states = StateSet.forTwoSidedShinySurface(Color.WHITE);
    sphere.setStates(states);
    World world = new World();
    world.addChild(sphere);
    SimpleFrame sf = new SimpleFrame(world);
    OrbitViewLighting ovl = new OrbitViewLighting();
    ovl.setPosition(0,1.0f,1.0f,0.0f);
    ovl.setLightSourceType(LightSourceType.DIRECTIONAL);
    sf.getOrbitView().setOrbitViewLighting(ovl);
  }

  private static void demo1() {
    Sphere sphere = new Sphere();
    StateSet states = StateSet.forTwoSidedShinySurface(Color.WHITE);
    sphere.setStates(states);
    World world = new World();
    world.addChild(sphere);
    SimpleFrame sf = new SimpleFrame(world);
    OrbitViewLighting ovl = new OrbitViewLighting();

    ovl.setPosition(0,-2.0f, 2.0f,0.0f);
    ovl.setPosition(1, 2.0f, 2.0f,0.0f);
    ovl.setPosition(2, 0.0f,-2.0f,0.0f);

    ovl.setLightSourceType(0, LightSourceType.POSITIONAL);
    ovl.setLightSourceType(1, LightSourceType.POSITIONAL);
    ovl.setLightSourceType(2, LightSourceType.POSITIONAL);

    ovl.setDiffuse(0,RED);
    ovl.setDiffuse(1,GREEN);
    ovl.setDiffuse(2,BLUE);

    ovl.toggleLight(1);
    ovl.toggleLight(2);

    sf.getOrbitView().setOrbitViewLighting(ovl);
  }

  public static class Sphere extends Node {
    public Sphere() {}
    @Override
    public void draw(DrawContext dc) {
      _ellipsoid.draw(0,0,0,0,0,1,0,1,0,1,0,0);
    }
    @Override
    public BoundingSphere computeBoundingSphere(boolean finite) {
      return _bs;
    }
    private EllipsoidGlyph _ellipsoid = new EllipsoidGlyph();
    private BoundingSphere _bs =
      new BoundingSphere(new BoundingBox(-1,-1,-1,1,1,1));
  }

  private static float[] RED   = new float[] { 1.0f, 0.0f, 0.0f, 0.2f };
  private static float[] GREEN = new float[] { 0.0f, 1.0f, 0.0f, 0.2f };
  private static float[] BLUE  = new float[] { 0.0f, 0.0f, 1.0f, 0.2f };

}
