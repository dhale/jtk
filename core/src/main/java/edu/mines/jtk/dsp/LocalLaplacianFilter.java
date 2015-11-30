/****************************************************************************
Copyright 2008, Colorado School of Mines and others.
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
package edu.mines.jtk.dsp;

import edu.mines.jtk.util.Parallel;

/**
 * Local Laplacian filter defined by non-constant diffusion tensors. 
 * The Laplacian operator appears in diffusion equations, and this 
 * filter is useful in the context of anisotropic diffusion filtering.
 * <p>
 * This filter computes y = y+G'DGx where G is an approximation to the 
 * gradient operator, G' is its adjoint, and D is a local diffusion 
 * tensor that determines for each image sample the direction of the 
 * Laplacian filter.
 * <p>
 * Local Laplacian filters are rarely used alone. While zeroing some 
 * features in images, they tend to attenuate many other features as 
 * well. Therefore, these filters are typically used in combinations 
 * with others.
 * <p>
 * For example, the filter implied by (I+G'DG)y = G'DGx acts as a 
 * notch filter. It attenuates features for which G'DGx is zero while 
 * preserving other features. Diffusivities d (inside D) control the 
 * width of the notch. Note that application of this filter requires 
 * solving a sparse symmetric positive-definite system of equations.
 * <p>
 * An even simpler example is the filter implied by (I+G'DG)y = x. 
 * This filter smooths features in the directions implied by the 
 * tensors D. Again, application of this filter requires solving a 
 * sparse symmetric positive-definite system of equations.
 * <p>
 * The accumulation of the filter output in y = y+G'DGx is useful when
 * constructing such combination filters. For y = 0, this filter computes 
 * y = G'DGx. For y = x, this filter computes y = (I+G'DG)x.
 *
 * @author Dave Hale, Colorado School of Mines
 * @version 2008.08.27
 */
public class LocalLaplacianFilter {

  /**
   * Constructs a local Laplacian filter.
   */
  public LocalLaplacianFilter() {
    this(1.0);
  }

  /**
   * Constructs a local Laplacian filter.
   * @param scale scale factor for all diffusion coefficients.
   */
  public LocalLaplacianFilter(double scale) {
    _scale = (float)scale;
  }

  /**
   * Computes y = y+G'DGx for 2D arrays x and y.
   * @param x input array. Must be distinct from the array y.
   * @param y input/output array. Must be distinct from the array x.
   */
  public void apply(Tensors2 d, float[][] x, float[][] y) {
    float[] di = new float[3];
    int n1 = x[0].length;
    int n2 = x.length;
    for (int i2=1; i2<n2; ++i2) {
      for (int i1=1; i1<n1; ++i1) {
        d.getTensor(i1,i2,di);
        float d11 = di[0]*_scale;
        float d12 = di[1]*_scale;
        float d22 = di[2]*_scale;
        float x00 = x[i2  ][i1  ];
        float x01 = x[i2  ][i1-1];
        float x10 = x[i2-1][i1  ];
        float x11 = x[i2-1][i1-1];
        float xa = x00-x11;
        float xb = x01-x10;
        float x1 = 0.5f*(xa-xb);
        float x2 = 0.5f*(xa+xb);
        float y1 = d11*x1+d12*x2;
        float y2 = d12*x1+d22*x2;
        float ya = 0.5f*(y1+y2);
        float yb = 0.5f*(y1-y2);
        y[i2  ][i1  ] += ya;
        y[i2  ][i1-1] -= yb;
        y[i2-1][i1  ] += yb;
        y[i2-1][i1-1] -= ya;
      }
    }
  }

  /**
   * Computes y = y+G'DGx for 3D arrays x and y.
   * @param x input array. Must be distinct from the array y.
   * @param y input/output array. Must be distinct from the array x.
   */
  public void apply(Tensors3 d, float[][][] x, float[][][] y) {
    //applySerial(d,x,y);
    applyParallel(d,x,y);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private float _scale; // scale factor for diffusion coefficients

  //private void applySerial(Tensors3 d, float[][][] x, float[][][] y) {
  //  int n3 = x.length;
  //  for (int i3=1; i3<n3; ++i3)
  //    applySlice3(i3,d,x,y);
  //}

  private void applyParallel(
    final Tensors3 d, final float[][][] x, final float[][][] y) 
  {
    final int n3 = x.length;

    // i3 = 1, 3, 5, ...
    Parallel.loop(1,n3,2,new Parallel.LoopInt() {
      public void compute(int i3) {
        applySlice3(i3,d,x,y);
      }
    });

    // i3 = 2, 4, 6, ...
    Parallel.loop(2,n3,2,new Parallel.LoopInt() {
      public void compute(int i3) {
        applySlice3(i3,d,x,y);
      }
    });
  }

  // Computes y = y+G'DGx for one constant-i3 slice.
  private void applySlice3(int i3, Tensors3 d, float[][][] x, float[][][] y) {
    float[] di = new float[6];
    int n1 = x[0][0].length;
    int n2 = x[0].length;
    for (int i2=1; i2<n2; ++i2) {
      float[] x00 = x[i3  ][i2  ];
      float[] x01 = x[i3  ][i2-1];
      float[] x10 = x[i3-1][i2  ];
      float[] x11 = x[i3-1][i2-1];
      float[] y00 = y[i3  ][i2  ];
      float[] y01 = y[i3  ][i2-1];
      float[] y10 = y[i3-1][i2  ];
      float[] y11 = y[i3-1][i2-1];
      for (int i1=1,i1m=0; i1<n1; ++i1,++i1m) {
        d.getTensor(i1,i2,i3,di);
        float d11 = di[0]*_scale;
        float d12 = di[1]*_scale;
        float d13 = di[2]*_scale;
        float d22 = di[3]*_scale;
        float d23 = di[4]*_scale;
        float d33 = di[5]*_scale;
        apply(i1,d11,d12,d13,d22,d23,d33,x00,x01,x10,x11,y00,y01,y10,y11);
      }
    }
  }
  // Computes y = y+G'DGx for one sample.
  // Operations per sample for this method:
  //    16 loads + 8 stores + 4+12+6+8 adds + 3+9+4 muls
  //  = 16 loads + 8 stores +       30 adds +    16 muls
  // For alternative (more complicated) method with 27-point stencil:
  //    28 loads + 1 store  +       27 adds +    27 muls
  private static void apply(int i1,
   float d11, float d12, float d13, float d22, float d23, float d33,
   float[] x00, float[] x01, float[] x10, float[] x11,
   float[] y00, float[] y01, float[] y10, float[] y11)
  {
    int i1m = i1-1;
    float x000 = x00[i1 ];
    float x001 = x00[i1m];
    float x010 = x01[i1 ];
    float x100 = x10[i1 ];
    float x011 = x01[i1m];
    float x101 = x10[i1m];
    float x110 = x11[i1 ];
    float x111 = x11[i1m];
    //float x1 = 0.25f*(x000+x010+x100+x110-x001-x011-x101-x111);
    //float x2 = 0.25f*(x000+x001+x100+x101-x010-x011-x110-x111);
    //float x3 = 0.25f*(x000+x001+x010+x011-x100-x101-x110-x111);
    float xa = x000-x111;
    float xb = x001-x110;
    float xc = x010-x101;
    float xd = x100-x011;
    float x1 = 0.25f*(xa-xb+xc+xd);
    float x2 = 0.25f*(xa+xb-xc+xd);
    float x3 = 0.25f*(xa+xb+xc-xd);
    float y1 = d11*x1+d12*x2+d13*x3;
    float y2 = d12*x1+d22*x2+d23*x3;
    float y3 = d13*x1+d23*x2+d33*x3;
    float ya = 0.25f*(y1+y2+y3);
    float yb = 0.25f*(y1-y2+y3);
    float yc = 0.25f*(y1+y2-y3);
    float yd = 0.25f*(y1-y2-y3);
    y00[i1 ] += ya;
    y00[i1m] -= yd;
    y01[i1 ] += yb;
    y10[i1 ] += yc;
    y01[i1m] -= yc;
    y10[i1m] -= yb;
    y11[i1 ] += yd;
    y11[i1m] -= ya;
  }
}
