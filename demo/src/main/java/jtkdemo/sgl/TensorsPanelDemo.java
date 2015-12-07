/****************************************************************************
Copyright 2013, Colorado School of Mines and others.
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

import java.awt.*;

import edu.mines.jtk.dsp.*;
import edu.mines.jtk.sgl.*;
import static edu.mines.jtk.util.ArrayMath.*;

/**
 * Demos {@link edu.mines.jtk.sgl.TensorsPanel}.
 * @author Dave Hale
 * @version 2013.11.20
 */
public class TensorsPanelDemo {

  public static void main(String[] args) {
    demo1();
    demo2();
    demo3();
  }

  public static void demo1() {
    int n1 = 1;
    int n2 = 111;
    int n3 = 111;
    EigenTensors3 et = new EigenTensors3(n1,n2,n3,false);
    float eu = 1.00f, ev = 1.00f, ew = 0.01f;
    float u1 = 1.00f, u2 = 0.00f, u3 = 0.00f;
    float w1 = 0.00f, w2 = 0.00f, w3 = 1.00f;
    int i1 = 0;
    for (int i3=0; i3<n3; ++i3) {
      float a = i3*FLT_PI/2.0f/(n3-1);
      w2 = sin(a);
      w3 = cos(a);
      for (int i2=0; i2<n2; ++i2) {
        et.setEigenvalues(i1,i2,i3,eu,ev,ew);
        et.setEigenvectorU(i1,i2,i3,u1,u2,u3);
        et.setEigenvectorW(i1,i2,i3,w1,w2,w3);
      }
    }
    TensorsPanel tp = new TensorsPanel(et);
    AxisAlignedQuad aaq = new AxisAlignedQuad(Axis.Z,
      new Point3(   0,   0,i1),
      new Point3(n3-1,n2-1,i1));
    aaq.getFrame().addChild(tp);
    show(aaq);
  }

  public static void demo2() {
    int n1 = 111;
    int n2 = 1;
    int n3 = 111;
    EigenTensors3 et = new EigenTensors3(n1,n2,n3,false);
    float eu = 0.01f, ev = 1.00f, ew = 1.00f;
    float u1 = 1.00f, u2 = 0.00f, u3 = 0.00f;
    float w1 = 0.00f, w2 = 1.00f, w3 = 0.00f;
    int i2 = 0;
    for (int i3=0; i3<n3; ++i3) {
      float a = i3*FLT_PI/2.0f/(n3-1);
      u1 = cos(a);
      u3 = sin(a);
      for (int i1=0; i1<n1; ++i1) {
        et.setEigenvalues(i1,i2,i3,eu,ev,ew);
        et.setEigenvectorU(i1,i2,i3,u1,u2,u3);
        et.setEigenvectorW(i1,i2,i3,w1,w2,w3);
      }
    }
    TensorsPanel tp = new TensorsPanel(et);
    AxisAlignedQuad aaq = new AxisAlignedQuad(Axis.Y,
      new Point3(   0,i2,   0),
      new Point3(n3-1,i2,n1-1));
    aaq.getFrame().addChild(tp);
    show(aaq);
  }

  public static void demo3() {
    int n1 = 111;
    int n2 = 111;
    int n3 = 1;
    EigenTensors3 et = new EigenTensors3(n1,n2,n3,false);
    float eu = 0.01f, ev = 1.00f, ew = 1.00f;
    float u1 = 1.00f, u2 = 0.00f, u3 = 0.00f;
    float w1 = 0.00f, w2 = 0.00f, w3 = 1.00f;
    int i3 = 0;
    for (int i2=0; i2<n2; ++i2) {
      float a = i2*FLT_PI/2.0f/(n2-1);
      u1 = cos(a);
      u2 = sin(a);
      for (int i1=0; i1<n1; ++i1) {
        et.setEigenvalues(i1,i2,i3,eu,ev,ew);
        et.setEigenvectorU(i1,i2,i3,u1,u2,u3);
        et.setEigenvectorW(i1,i2,i3,w1,w2,w3);
      }
    }
    TensorsPanel tp = new TensorsPanel(et);
    AxisAlignedQuad aaq = new AxisAlignedQuad(Axis.X,
      new Point3(i3,   0,   0),
      new Point3(i3,n2-1,n1-1));
    aaq.getFrame().addChild(tp);
    show(aaq);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private static void show(Node node) {
    World world = new World();
    world.addChild(node);
    SimpleFrame sf = new SimpleFrame(world);
    sf.getOrbitView().setScale(2.0);
  }
}
