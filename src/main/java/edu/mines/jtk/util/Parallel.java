/****************************************************************************
Copyright (c) 2010, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.util;

import jsr166y.*; // until JDK 7 is available

import static edu.mines.jtk.util.ArrayMath.*; // for testing only

/**
 * Utilities for parallel computing in loops over independent tasks.
 * This class provides convenient methods for parallel processing of
 * tasks that require loops over indices, in which computations for 
 * different indices do not modify any shared mutable data.
 * <p>
 * As a simple example, consider the following function that adds 
 * floats in two arrays and stores the results in a third array.
 * <pre><code>
 * static void add(float[] a, float[] b, float[] c) {
 *   int n = a.length;
 *   for (int i=0; i&lt;n; ++i)
 *     c[i] = a[i]+b[i];
 * }
 * </code></pre>
 * A serial version of a similar function for 2D arrays is:
 * <pre><code>
 * static void addSerial(
 *   float[][] a, 
 *   float[][] b, 
 *   float[][] c) 
 * {
 *   int n = a.length;
 *   for (int i=0; i&lt;n; ++i) {
 *     add(a[i],b[i],c[i]);
 * }
 * </code></pre>
 * Using this class, the parallel version for 2D arrays is:
 * <pre><code>
 * static void addParallel(
 *   final float[][] a, 
 *   final float[][] b, 
 *   final float[][] c) 
 * {
 *   int n = a.length;
 *   loop(n,new LoopInt() {
 *     public void compute(int i) {
 *       add(a[i],b[i],c[i]);
 *     }
 *   });
 * }
 * </code></pre>
 * In the parallel version, the method compute defined by the interface
 * LoopInt will be called n times for n different indices i in the range
 * [0,n-1]. The order of indices is both indeterminant and irrelevant 
 * because the computation for each index i is independent.
 * <p>
 * A similar method facilitates tasks that reduce a sequence of indexed
 * values to one (or more) values. For example, given the following
 * method
 * <pre><code>
 * static float sum(float[] a) {
 *   int n = a.length;
 *   float s = 0.0f;
 *   for (int i=0; i&lt;n; ++i)
 *     s += a[i];
 *   return s;
 * }
 * </code></pre>
 * the serial and parallel versions for 2D arrays are:
 * <pre><code>
 * static float sumSerial(float[][] a) {
 *   int n = a.length;
 *   float s = 0.0f;
 *   for (int i=0; i&lt;n; ++i)
 *     s += sum(a[i]);
 *   return s;
 * }
 * </code></pre>
 * and
 * <pre><code>
 * static float sumParallel(final float[][] a) {
 *   int n = a.length;
 *   return reduce(n,ReduceInt&lt;Float&gt;() {
 *     public Float compute(int i) {
 *       return sum(a[i]);
 *     }
 *     public Float combine(Float s1, Float s2) {
 *       return s1+s2;
 *     }
 *   });
 * }
 * </code></pre>
 * In the parallel version, we implement the interface ReduceInt
 * with two methods, one to compute sums of array elements and
 * another to combine two such sums together. The same pattern
 * works for other reduce operations. For example, with similar 
 * functions we could compute both minimum and maximum values and
 * other summary statistics for any indexed sequence of values.
 * <p>
 * Static methods loop and reduce submit tasks to a fork-join framework
 * that maintains a pool of threads shared by all users of these methods.
 * These methods recursively fork tasks so that disjoint sets of indices 
 * are processed in parallel on different threads.
 * <p>
 * More general loops are supported, equivalent to the following code:
 * <pre><code>
 * for (int i=begin; i&lt;end; i+=stride)
 *   // some computation that depends on i
 * </code></pre>
 * In addition to the three loop parameters begin, end, and stride, a 
 * fourth parameter chunk may be specified. This chunk parameter is 
 * a threshold for splitting tasks so that they can be performed in
 * parallel. Tasks are split only for sets of indices that are larger 
 * than the specified chunk size. Smaller sets of indices are processed 
 * serially. Increasing the chunk size will therefore reduce the number 
 * of tasks (and task overhead) but limit parallelism. The best chunk
 * size depends on the amount of computation performed for each index 
 * in the body of the loop. However, performance is typically stable 
 * for a wide range of chunk sizes.
 *
 * TODO: discuss nested parallelism.
 *
 * @author Dave Hale, Colorado School of Mines
 * @version 2010.11.23
 */
public class Parallel {

  /** Loop body. */
  public interface LoopInt {
    /**
     * Computes for the specified loop index.
     * @param i loop index.
     */
    public void compute(int i);
  }

  /** Loop body that returns a value. */
  public interface ReduceInt<V> {
    /**
     * Returns a value computed for the specified loop index.
     * @param i loop index.
     * @return the computed value.
     */
    public V compute(int i);

    /**
     * Returns the combination of two specified values.
     * @param v1 a value.
     * @param v2 a value.
     * @return the combined value.
     */
    public V combine(V v1, V v2);
  }

  /**
   * Performs a loop <code>for (int i=0; i&lt;end; ++i)</code>.
   * @param end the end index for the loop.
   * @param body the loop body.
   */
  public static void loop(int end, LoopInt body) {
    loop(0,end,1,1,body);
  }
  public static void loop(int begin, int end, LoopInt body) {
    loop(begin,end,1,1,body);
  }
  public static void loop(int begin, int end, int step, LoopInt body) {
    loop(begin,end,step,1,body);
  }
  /**
   * Performs a loop <code>for (int i=begin; i&lt;end; i+=stride)</code>.
   * This method recursively splits tasks only for sets of indices that 
   * are larger than the specified chunk size. Smaller sets of indices
   * are processed serially. A larger chunk size will therefore reduce 
   * the number of tasks but limit the parallelism.

   * while increasing the number of indices processed 
   * serially within each task. The chunk size should 
   * be chosen so that each task requires computation significant
   * @param begin the being index for the loop.
   * @param end the end index for the loop.
   * @param step the index increment.
   * @param chunk the chunk size.
   * @param body the loop body.
   */
  public static void loop(
    int begin, int end, int step, int chunk, LoopInt body) 
  {
    if (begin>=end)
      return;
    LoopIntAction task = new LoopIntAction(begin,end,step,chunk,body);
    if (LoopIntAction.inForkJoinPool()) {
      // TODO: check number of tasks before parallelizing!
      System.out.println("nested loop!");
      task.invoke();
    } else {
      _pool.invoke(task);
    }
  }

  public static <V> V reduce(int end, ReduceInt<V> body) {
    return reduce(0,end,1,1,body);
  }
  public static <V> V reduce(int begin, int end, ReduceInt<V> body) {
    return reduce(begin,end,1,1,body);
  }
  public static <V> V reduce(
    int begin, int end, int step, ReduceInt<V> body) 
  {
    return reduce(begin,end,step,1,body);
  }
  public static <V> V reduce(
    int begin, int end, int step, int chunk, ReduceInt<V> body) 
  {
    if (begin>=end)
      return null;
    ReduceIntTask<V> task = new ReduceIntTask<V>(begin,end,step,chunk,body);
    if (ReduceIntTask.inForkJoinPool()) {
      // TODO: check number of tasks before parallelizing!
      System.out.println("nested reduce!");
      return task.invoke();
    } else {
      return _pool.invoke(task);
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  // The pool shared by all fork-join tasks created through this class.
  private static ForkJoinPool _pool = new ForkJoinPool();

  /**
   * Splits range [begin:end) into [begin:middle) and [middle:end). The
   * returned middle index equals begin plus an integer multiple of step.
   */
  private static int middle(int begin, int end, int step) {
    return begin+step+((end-begin-1)/2)/step*step;
  }

  // Each fork-join task below has a range of indices to be processed.
  // If the range is less than or equal to the chunk size, just process 
  // it on the current thread. Otherwise, split the range into two parts 
  // that are approximately equal, but ensure that the left part is not
  // empty and not smaller than right part. If the right part is not empty,
  // fork a new task. Then compute the left part in the current thread,
  // and, if necessary, join the right part.

  /**
   * Fork-join task for parallel loop.
   */
  private static class LoopIntAction extends RecursiveAction {
    LoopIntAction(int begin, int end, int step, int chunk, LoopInt body) {
      assert begin<end:"begin < end";
      _begin = begin;
      _end = end;
      _step = step;
      _chunk = chunk;
      _body = body;
    }
    protected void compute() {
      if (_end-_begin<=_chunk*_step) {
        for (int i=_begin; i<_end; i+=_step) {
          _body.compute(i);
        }
      } else {
        int middle = middle(_begin,_end,_step);
        LoopIntAction l =
          new LoopIntAction(_begin,middle,_step,_chunk,_body);
        LoopIntAction r = (middle<_end) ?
          new LoopIntAction(middle,_end,_step,_chunk,_body) :
          null;
        if (r!=null) 
          r.fork();
        l.compute();
        if (r!=null) 
          r.join();
      }
    }
    private int _begin,_end,_step,_chunk;
    private LoopInt _body;
  }

  /**
   * Fork-join task for parallel reduce.
   */
  private static class ReduceIntTask<V> extends RecursiveTask<V> {
    ReduceIntTask(int begin, int end, int step, int chunk, ReduceInt<V> body) {
      assert begin<end:"begin < end";
      _begin = begin;
      _end = end;
      _step = step;
      _chunk = chunk;
      _body = body;
    }
    protected V compute() {
      if (_end-_begin<=_chunk*_step) {
        V v = _body.compute(_begin);
        for (int i=_begin+_step; i<_end; i+=_step) {
          V vi = _body.compute(i);
          v = _body.combine(v,vi);
        }
        return v;
      } else {
        int middle = middle(_begin,_end,_step);
        ReduceIntTask<V> l = 
          new ReduceIntTask<V>(_begin,middle,_step,_chunk,_body);
        ReduceIntTask<V> r = (middle<_end) ?
          new ReduceIntTask<V>(middle,  _end,_step,_chunk,_body) :
          null;
        if (r!=null) 
          r.fork();
        V v = l.compute();
        if (r!=null)
          v = _body.combine(v,r.join());
        return v;
      }
    }
    private int _begin,_end,_step,_chunk;
    private ReduceInt<V> _body;
  }

  ///////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////////////////////////////////////////////
  ///////////////////////////////////////////////////////////////////////////
  // Benchmark tests

  public static void main(String[] args) {
    benchArrayNorm();
    benchArrayAdd();
    benchMatrixMultiply();
  }

  // L2 norm of 3D array
  private static void benchArrayNorm() {
    int n1 = 501;
    int n2 = 502;
    int n3 = 503;
    System.out.println("L2 norm of 3D array n1="+n1+" n2="+n2+" n3="+n3);
    int niter;
    double maxtime = 5.0;
    double mflop = 2.0e-6*n1*n2*n3;
    Stopwatch sw = new Stopwatch();
    float[][][] as = randfloat(n1,n2,n3);
    float[][][] ap = copy(as);
    for (int ntest=0; ntest<3; ++ntest) {
      float rs = 0.0f;
      float rp = 0.0f;
      sw.restart();
      for (niter=0; sw.time()<maxtime; ++niter)
        rp = arrayNormParallel(ap);
      sw.stop();
      System.out.println("parallel: rate = "+(niter*mflop)/sw.time());
      sw.restart();
      for (niter=0; sw.time()<maxtime; ++niter)
        rs = arrayNormSerial(as);
      sw.stop();
      System.out.println("  serial: rate = "+(niter*mflop)/sw.time());
      System.out.println("     err: "+abs(rp-rs));
    }
  }
  private static float arrayNormSerial(float[][][] a) {
    int n1 = a[0][0].length;
    int n2 = a[0].length;
    int n3 = a.length;
    double aa = 0.0;
    for (int i3=0; i3<n3; ++i3) {
      for (int i2=0; i2<n2; ++i2) {
        float[] ai3i2 = a[i3][i2];
        for (int i1=0; i1<n1; ++i1) {
          float ai = ai3i2[i1];
          aa += ai*ai;
        }
      }
    }
    return (float)sqrt(aa);
  }
  private static float arrayNormParallel(final float[][][] a) {
    final int n1 = a[0][0].length;
    final int n2 = a[0].length;
    final int n3 = a.length;
    double aa = reduce(n3,new ReduceInt<Double>() {
      public Double compute(int i3) {
        double aa = 0.0;
        for (int i2=0; i2<n2; ++i2) {
          float[] ai3i2 = a[i3][i2];
          for (int i1=0; i1<n1; ++i1) {
            float ai = ai3i2[i1];
            aa += ai*ai;
          }
        }
        return aa;
      }
      public Double combine(Double v1, Double v2) {
        return v1+v2;
      }
    });
    return (float)sqrt(aa);
  }

  // Add a constant to a 3D array.
  private static void benchArrayAdd() {
    int n1 = 501;
    int n2 = 502;
    int n3 = 503;
    System.out.println("Add constant to 3D array n1="+n1+" n2="+n2+" n3="+n3);
    int niter;
    double maxtime = 5.0;
    double mflop = 2.0e-6*n1*n2*n3;
    Stopwatch sw = new Stopwatch();
    float[][][] as = randfloat(n1,n2,n3);
    float[][][] ap = copy(as);
    for (int ntest=0; ntest<3; ++ntest) {
      sw.restart();
      for (niter=0; sw.time()<maxtime; ++niter) {
        arrayAddParallel( 3.14f,ap);
        arrayAddParallel(-3.14f,ap);
      }
      sw.stop();
      System.out.println("parallel: rate = "+(niter*mflop)/sw.time());
      sw.restart();
      for (niter=0; sw.time()<maxtime; ++niter) {
        arrayAddSerial( 3.14f,as);
        arrayAddSerial(-3.14f,as);
      }
      sw.stop();
      System.out.println("  serial: rate = "+(niter*mflop)/sw.time());
      System.out.println("     err: "+max(abs(sub(as,ap))));
    }
  }
  private static void arrayAddSerial(float x, float[][][] a) {
    int n1 = a[0][0].length;
    int n2 = a[0].length;
    int n3 = a.length;
    for (int i3=0; i3<n3; ++i3) {
      for (int i2=0; i2<n2; ++i2) {
        float[] ai3i2 = a[i3][i2];
        for (int i1=0; i1<n1; ++i1) {
          ai3i2[i1] += x;
        }
      }
    }
  }
  private static void arrayAddParallel(final float x, final float[][][] a) {
    final int n1 = a[0][0].length;
    final int n2 = a[0].length;
    final int n3 = a.length;
    loop(n3,new LoopInt() {
      public void compute(int i3) {
        for (int i2=0; i2<n2; ++i2) {
          float[] ai3i2 = a[i3][i2];
          for (int i1=0; i1<n1; ++i1) {
            ai3i2[i1] += x;
          }
        }
      }
    });
  }

  // Matrix multiply
  private static void benchMatrixMultiply() {
    int m = 1001;
    int n = 1002;
    System.out.println("Matrix multiply for m="+m+" n="+n);
    float[][] a = randfloat(n,m);
    float[][] b = randfloat(m,n);
    float[][] cs = zerofloat(m,m);
    float[][] cp = zerofloat(m,m);
    double maxtime = 5.0;
    double mflop = 2.0e-6*m*m*n;
    Stopwatch sw = new Stopwatch();
    for (int ntest=0; ntest<3; ++ntest) {
      int niter;
      sw.restart();
      for (niter=0; sw.time()<maxtime; ++niter) {
        matrixMultiplyParallel(a,b,cp);
      }
      sw.stop();
      System.out.println("parallel: rate = "+(niter*mflop)/sw.time());
      sw.restart();
      for (niter=0; sw.time()<maxtime; ++niter) {
        matrixMultiplySerial(a,b,cs);
      }
      sw.stop();
      System.out.println("  serial: rate = "+(niter*mflop)/sw.time());
    }
  }
  private static void matrixMultiplySerial(
    float[][] a, 
    float[][] b, 
    float[][] c) 
  {
    int nj = c[0].length;
    for (int j=0; j<nj; ++j)
      computeColumn(j,a,b,c);
  }
  private static void matrixMultiplyParallel(
    final float[][] a, 
    final float[][] b, 
    final float[][] c) 
  {
    int nj = c[0].length;
    loop(nj,new LoopInt() {
      public void compute(int j) {
        computeColumn(j,a,b,c);
      }
    });
  }
  private static void computeColumn(
    int j, float[][] a, float[][] b, float[][] c) 
  {
    int ni = c.length;
    int nk = b.length;
    float[] bj = new float[nk];
    for (int k=0; k<nk; ++k)
      bj[k] = b[k][j];
    for (int i=0; i<ni; ++i) {
      float[] ai = a[i];
      float cij = 0.0f;
      int mk = nk%4;
      for (int k=0; k<mk; ++k)
        cij += ai[k]*bj[k];
      for (int k=mk; k<nk; k+=4) {
        cij += ai[k  ]*bj[k  ];
        cij += ai[k+1]*bj[k+1];
        cij += ai[k+2]*bj[k+2];
        cij += ai[k+3]*bj[k+3];
      }
      c[i][j] = cij;
    }
  }
}
