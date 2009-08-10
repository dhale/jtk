/****************************************************************************
Copyright (c) 2009, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl.test;

import java.awt.*;
import java.util.*;

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
    test1();
  }

  public static void test1() {
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

    Group group = new Group();
    group.setStates(states);
    World world = new World();
    world.addChild(group);

    addRandomEllipsoidGlyphs(group);
    SimpleFrame sf = new SimpleFrame(world);
    //sf.setWorldSphere(new BoundingSphere(5,5,5,5));
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private static Random _random = new Random();

  private static void addRandomEllipsoidGlyphs(Group group) {
    for (int i=0; i<10; ++i)
      group.addChild(makeRandomEllipsoidGlyph());
  }

  private static EllipsoidGlyph makeRandomEllipsoidGlyph() {
    int m = 3; // approximation quality
    float xc = 10.0f*_random.nextFloat();
    float yc = 10.0f*_random.nextFloat();
    float zc = 10.0f*_random.nextFloat();
    float[] d = randomEigenvalues();
    float[][] v = randomEigenvectors();
    return new EllipsoidGlyph(xc,yc,zc,m,d,v);
  }

  private static float[][] randomEigenvectors() {
    DMatrix a = new DMatrix(sub(0.5,randdouble(3,3))); // random A
    a = a.transpose().times(a); // A now positive-semidefinite
    DMatrixEvd evd = new DMatrixEvd(a); // eigen-decomposition of A
    DMatrix va = evd.getV(); // eigenvectors of A in columns of V
    float[][] v = new float[3][3];
    for (int j=0; j<3; ++j) // for all columns of A, ...
      for (int i=0; i<3; ++i) // for all rows in i'th column of A, ...
        v[j][i] = (float)va.get(i,j); // j'th column is one eigenvector
    return v;
  }
  private static float[] randomEigenvalues() {
    int type = _random.nextInt(3);
    float[] axis = new float[3];
    if (type==0) {
      axis[0] = 1.0f; axis[1] = 1.0f; axis[2] = 1.0f; // sphere
    } else if (type==1) {
      axis[0] = 1.0f; axis[1] = 1.0f; axis[2] = 0.2f; // oblate
    } else {
      axis[0] = 1.0f; axis[1] = 0.2f; axis[2] = 0.2f; // prolate
    }
    return div(1.0f,mul(axis,axis));
  }
}
