package edu.mines.jtk.dsp;

import edu.mines.jtk.util.Check;
import static edu.mines.jtk.util.ArrayMath.*;

/**
 * A mask for samples that are zero or near zero.
 * Values in the mask image are either true or false. Samples for which 
 * the mask is false may be unreliable in some applications, and this
 * class can be used to identify these samples.
 *
 * For example, at samples in 3D images where the zero mask is false, 
 * we may set structure tensors to represent default horizontal layering.
 * 
 * A sample in the mask is set to false if the mean absolute value
 * of samples in a local Gaussian window is less than some specified
 * fraction of the global mean absolute value of all image samples.
 * Note that the global mean can be altered significantly by just a
 * few samples with unusually large negative or positive values. Such
 * outliers should be replaced before constructing a zero mask.
 *
 * @author Dave Hale, Colorado School of Mines
 * @version 2009.09.09
 */
public class ZeroMask {

  /**
   * Constructs a zero mask for a 2D image.
   * The mask will be zero for samples where the local mean absolute
   * amplitude is less than the specified small fraction of the global
   * mean absolute amplitude.
   * @param small a small fraction; e.g., 0.1.
   * @param sigma1 Gaussian window half-width for 1st dimension.
   * @param sigma2 Gaussian window half-width for 2nd dimension.
   * @param x array of image values from which mask is derived.
   */
  public ZeroMask(
    double small, double sigma1, double sigma2,
    float[][] x) 
  {
    _n1 = x[0].length;
    _n2 = x.length;
    float[][] t = abs(x);
    float a = ((sum(t)/_n1)/_n2); // global mean absolute amplitude
    RecursiveGaussianFilter rgf1 = new RecursiveGaussianFilter(sigma1);
    RecursiveGaussianFilter rgf2 = new RecursiveGaussianFilter(sigma2);
    float[][] b = zerofloat(_n1,_n2);
    rgf1.apply0X(t,b);
    rgf2.applyX0(b,t);
    _mask2 = new boolean[_n2][_n1];
    for (int i2=0; i2<_n2; ++i2) {
      for (int i1=0; i1<_n1; ++i1) {
        _mask2[i2][i1] = t[i2][i1]>=small*a;
      }
    }
  }

  /**
   * Constructs a zero mask for a 3D image.
   * @param small small value; zeros in mask where labs &lt; small*gabs.
   * @param sigma1 Gaussian window half-width for 1st dimension.
   * @param sigma2 Gaussian window half-width for 2nd dimension.
   * @param sigma3 Gaussian window half-width for 3rd dimension.
   * @param x array of image values from which mask is derived.
   */
  public ZeroMask(
    double small, double sigma1, double sigma2, double sigma3,
    float[][][] x) 
  {
    _n1 = x[0][0].length;
    _n2 = x[0].length;
    _n3 = x.length;
    float[][][] t = abs(x);
    float a = ((sum(t)/_n1)/_n2)/_n3; // global mean absolute amplitude
    RecursiveGaussianFilter rgf1 = new RecursiveGaussianFilter(sigma1);
    RecursiveGaussianFilter rgf2 = new RecursiveGaussianFilter(sigma2);
    RecursiveGaussianFilter rgf3 = new RecursiveGaussianFilter(sigma3);
    float[][][] b = zerofloat(_n1,_n2,_n3);
    rgf1.apply0XX(t,b);
    rgf2.applyX0X(b,t);
    rgf3.applyXX0(t,b); // local mean absolute amplitude
    _mask3 = new boolean[_n3][_n2][_n1];
    for (int i3=0; i3<_n3; ++i3) {
      for (int i2=0; i2<_n2; ++i2) {
        for (int i1=0; i1<_n1; ++i1) {
          _mask3[i3][i2][i1] = b[i3][i2][i1]>=small*a;
        }
      }
    }
  }

  /**
   * Constructs a zero mask from a specified array of floats.
   * Mask is true for all non-zero samples in the array; false, otherwise.
   * @param x array of values from which mask is derived.
   */
  public ZeroMask(float[][] x) {
    _n1 = x[0].length;
    _n2 = x.length;
    _mask2 = new boolean[_n2][_n1];
    for (int i2=0; i2<_n2; ++i2) {
      for (int i1=0; i1<_n1; ++i1) {
        if (x[i2][i1]!=0.0f)
          _mask2[i2][i1] = true;
      }
    }
  }

  /**
   * Constructs a zero mask from a specified array of floats.
   * Mask is true for all non-zero samples in the array; false, otherwise.
   * @param x array of values from which mask is derived.
   */
  public ZeroMask(float[][][] x) {
    _n1 = x[0][0].length;
    _n2 = x[0].length;
    _n3 = x.length;
    _mask3 = new boolean[_n3][_n2][_n1];
    for (int i3=0; i3<_n3; ++i3) {
      for (int i2=0; i2<_n2; ++i2) {
        for (int i1=0; i1<_n1; ++i1) {
          if (x[i3][i2][i1]!=0.0f)
            _mask3[i3][i2][i1] = true;
        }
      }
    }
  }

  /**
   * Returns a 2D array of floats representing this mask.
   * The returned array has values 0.0f (false) and 1.0f (true).
   * @return mask array of floats.
   */
  public float[][] getAsFloats2() {
    Check.state(_mask2!=null,"mask constructed for a 2D image");
    float[][] mask = new float[_n2][_n1];
    getAsFloats(mask);
    return mask;
  }

  /**
   * Fills a 2D array of floats representing this mask.
   * The returned array has values 0.0f (false) and 1.0f (true).
   * @param mask array of floats representing this mask.
   */
  public void getAsFloats(float[][] mask) {
    Check.state(_mask2!=null,"mask constructed for a 2D image");
    for (int i2=0; i2<_n2; ++i2)
      for (int i1=0; i1<_n1; ++i1)
        mask[i2][i1] = (_mask2[i2][i1])?1.0f:0.0f;
  }

  /**
   * Returns a 3D array of floats representing this mask.
   * The returned array has values 0.0f (false) and 1.0f (true).
   * @return mask array of floats.
   */
  public float[][][] getAsFloats3() {
    Check.state(_mask3!=null,"mask constructed for a 3D image");
    float[][][] mask = new float[_n3][_n2][_n1];
    getAsFloats(mask);
    return mask;
  }

  /**
   * Fills a 3D array of floats representing this mask.
   * The returned array has values 0.0f (false) and 1.0f (true).
   * @param mask array of floats representing this mask.
   */
  public void getAsFloats(float[][][] mask) {
    Check.state(_mask3!=null,"mask constructed for a 3D image");
    for (int i3=0; i3<_n3; ++i3)
      for (int i2=0; i2<_n2; ++i2)
        for (int i1=0; i1<_n1; ++i1)
          mask[i3][i2][i1] = (_mask3[i3][i2][i1])?1.0f:0.0f;
  }

  /**
   * Applies this mask to a specified array of values.
   * @param vfalse value to use where mask is false.
   * @param v array of values to be masked.
   */
  public void apply(float vfalse, float[][] v) {
    Check.state(_mask2!=null,"mask constructed for a 2D image");
    for (int i2=0; i2<_n2; ++i2) {
      for (int i1=0; i1<_n1; ++i1) {
        if (!_mask2[i2][i1])
          v[i2][i1] = vfalse;
      }
    }
  }

  /**
   * Applies this mask to a specified array of values.
   * @param vfalse value to use where mask is false.
   * @param v array of values to be masked.
   */
  public void apply(float vfalse, float[][][] v) {
    Check.state(_mask3!=null,"mask constructed for a 3D image");
    for (int i3=0; i3<_n3; ++i3) {
      for (int i2=0; i2<_n2; ++i2) {
        for (int i1=0; i1<_n1; ++i1) {
          if (!_mask3[i3][i2][i1])
            v[i3][i2][i1] = vfalse;
        }
      }
    }
  }

  /**
   * Applies this mask to a specified eigentensor field.
   * @param efalse eigentensor {e11,e12,e22} to use for samples
   *  where the mask is false.
   * @param e eigentensors to be masked.
   */
  public void apply(float[] efalse, EigenTensors2 e) {
    Check.state(_mask2!=null,"mask constructed for a 2D image");
    for (int i2=0; i2<_n2; ++i2) {
      for (int i1=0; i1<_n1; ++i1) {
        if (!_mask2[i2][i1])
          e.setTensor(i1,i2,efalse);
      }
    }
  }

  /**
   * Applies this mask to a specified eigentensor field.
   * @param efalse eigentensor {e11,e12,e13,e22,e23,e33} to use 
   *  for samples where the mask is false.
   * @param e eigentensors to be masked.
   */
  public void apply(float[] efalse, EigenTensors3 e) {
    Check.state(_mask3!=null,"mask constructed for a 3D image");
    for (int i3=0; i3<_n3; ++i3) {
      for (int i2=0; i2<_n2; ++i2) {
        for (int i1=0; i1<_n1; ++i1) {
          if (!_mask3[i3][i2][i1])
            e.setTensor(i1,i2,i3,efalse);
        }
      }
    }
  }

  private int _n1,_n2,_n3;
  private boolean[][] _mask2;
  private boolean[][][] _mask3;
}
