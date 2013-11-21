/****************************************************************************
Copyright (c) 2013, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.sgl;

import java.awt.*;

import edu.mines.jtk.dsp.*;
import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Tests {@link edu.mines.jtk.sgl.TensorsPanel}.
 * @author Dave Hale
 * @version 2013.11.20
 */
public class TensorsPanelTest {

  public static void main(String[] args) {
    test0();
  }

  public static void test0() {
    int n1 = 1;
    int n2 = 111;
    int n3 = 111;
    EigenTensors3 et = new EigenTensors3(n1,n2,n3,false);
    float eu = 1.0f, ev = 1.0f, ew = 0.01f;
    float u1 = 1.0f, u2 = 0.0f, u3 = 0.0f;
    float w1 = 0.0f, w2 = 0.0f, w3 = 1.0f;
    for (int i3=0; i3<n3; ++i3) {
      float a = i3*FLT_PI/2.0f/(n3-1);
      w2 = sin(a);
      w3 = cos(a);
      for (int i2=0; i2<n2; ++i2) {
        for (int i1=0; i1<n1; ++i1) {
          et.setEigenvalues(i1,i2,i3,eu,ev,ew);
          et.setEigenvectorU(i1,i2,i3,u1,u2,u3);
          et.setEigenvectorW(i1,i2,i3,w1,w2,w3);
        }
      }
    }
    show(et);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private static void show(EigenTensors3 et) {
    int n1 = et.getN1();
    int n2 = et.getN2();
    int n3 = et.getN3();
    TensorsPanel tp = new TensorsPanel(et);
    AxisAlignedQuad aaq = new AxisAlignedQuad(Axis.Z,
      new Point3(   0,   0,n1/2),
      new Point3(n3-1,n2-1,n1/2));
    aaq.getFrame().addChild(tp);
    World world = new World();
    world.addChild(aaq);
    SimpleFrame sf = new SimpleFrame(world);
    sf.getOrbitView().setScale(2.0);
  }
}
