/****************************************************************************
Copyright (c) 2009, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl.test;

import java.awt.*;

import edu.mines.jtk.la.*;
import edu.mines.jtk.sgl.*;
import edu.mines.jtk.util.*;
import static edu.mines.jtk.ogl.Gl.*;
import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Tests {@link edu.mines.jtk.sgl.EllipsoidGlyph}.
 * @author Dave Hale
 * @version 2009.08.10
 */
public class EllipsoidGlyphTest {

  public static void main(String[] args) {
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

    Group g = new Group();
    g.setStates(states);

    EllipsoidGlyph eg = makeRandomEllipsoidGlyph();
    g.addChild(eg);

    World world = new World();
    world.addChild(g);

    TestFrame frame = new TestFrame(world);
    OrbitView view = frame.getOrbitView();
    view.setWorldSphere(new BoundingSphere(5,5,5,5));
    frame.setSize(new Dimension(800,600));
    frame.setVisible(true);
  }

  private static EllipsoidGlyph makeRandomEllipsoidGlyph() {
    int m = 3; // approximation quality
    DMatrix a = new DMatrix(sub(0.5,randdouble(3,3))); // random A
    a = a.transpose().times(a); // A now positive-semidefinite
    //a = DMatrix.identity(3,3);
    DMatrixEvd evd = new DMatrixEvd(a); // eigen-decomposition of A
    DMatrix va = evd.getV(); // eigenvectors of A in columns of V
    double[] da = {1.0,1.0,36.0}; // array of eigenvalues D
    da = mul(0.25,da); // so ellipsoid will be larger
    float[] d = new float[3];
    float[][] v = new float[3][3];
    for (int i=0; i<3; ++i) {
      d[i] = (float)da[i];
      for (int j=0; j<3; ++j) {
        v[j][i] = (float)va.get(i,j);
      }
    }
    return new EllipsoidGlyph(5.0f,5.0f,5.0f,m,d,v);
  }
}
