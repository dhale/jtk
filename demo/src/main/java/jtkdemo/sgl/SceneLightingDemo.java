package jtkdemo.sgl;

import edu.mines.jtk.sgl.*;
import java.awt.*;

import static edu.mines.jtk.sgl.SceneLighting.*;

/**
 * Demos {@link SceneLighting}.
 * @author Chris Engelsma
 * @version 2017.01.17
 */
public class SceneLightingDemo {

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
    SceneLighting sl = new SceneLighting();
    sl.setPosition(0,1.0f,1.0f,0.0f);
    sl.setLightSourceType(LightSourceType.DIRECTIONAL);
    sf.getOrbitView().setSceneLighting(sl);
  }

  private static void demo1() {
    Sphere sphere = new Sphere();
    StateSet states = StateSet.forTwoSidedShinySurface(Color.WHITE);
    sphere.setStates(states);
    World world = new World();
    world.addChild(sphere);
    SimpleFrame sf = new SimpleFrame(world);
    SceneLighting sl = new SceneLighting();

    sl.setPosition(0,-2.0f, 2.0f,0.0f);
    sl.setPosition(1, 2.0f, 2.0f,0.0f);
    sl.setPosition(2, 0.0f,-2.0f,0.0f);

    sl.setLightSourceType(0, LightSourceType.POSITIONAL);
    sl.setLightSourceType(1, LightSourceType.POSITIONAL);
    sl.setLightSourceType(2, LightSourceType.POSITIONAL);

    sl.setDiffuse(0,RED);
    sl.setDiffuse(1,GREEN);
    sl.setDiffuse(2,BLUE);

    sl.toggleLight(1);
    sl.toggleLight(2);

    sf.getOrbitView().setSceneLighting(sl);
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
