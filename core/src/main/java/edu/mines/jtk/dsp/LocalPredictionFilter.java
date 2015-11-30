/****************************************************************************
Copyright 2006, Colorado School of Mines and others.
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

import java.util.ArrayList;

import static edu.mines.jtk.util.ArrayMath.*;
import edu.mines.jtk.util.Check;

/**
 * Local prediction filtering.
 * <p>
 * <em>Warning: not yet completed or optimized for performance.</em>
 * @author Dave Hale, Colorado School of Mines
 * @version 2006.02.21
 */
public class LocalPredictionFilter {

  /**
   * Construct a prediction filter with specified Gaussian window half-width.
   * @param sigma the Gaussian window half-width; must not be less than 1.
   */
  public LocalPredictionFilter(double sigma) {
    Check.argument(sigma>=1.0,"sigma>=1.0");
    _lcf = new LocalCorrelationFilter(
      LocalCorrelationFilter.Type.SYMMETRIC,
      LocalCorrelationFilter.Window.GAUSSIAN,
      sigma);
  }

  public float[][][] apply(int[] lag1, int[] lag2, float[][] f, float[][] g) {
    Check.argument(lag1.length==lag2.length,"lag1.length==lag2.length");
    Check.argument(f!=g,"f!=g");

    // Compute local auto-correlation for all necessary lags.
    R2Cache rcache = new R2Cache(f);
    int m = lag1.length;
    float[][][][] rkj = new float[m][m][][];
    float[][][] rk0 = new float[m][][];
    for (int k=0; k<m; ++k) {
      int k1 = lag1[k];
      int k2 = lag2[k];
      for (int j=0; j<m; ++j) {
        int j1 = lag1[j];
        int j2 = lag2[j];
        rkj[k][j] = rcache.get(j1-k1,j2-k2);
      }
      rk0[k] = rcache.get(k1,k2);
    }

    // Compute prediction filters.
    int n1 = f[0].length;
    int n2 = f.length;
    double[][] rkjt = new double[m][m];
    double[] rk0t = new double[m];
    double[] at = new double[m];
    float[][][] a = new float[m][n2][n1];
    CgSolver cgs = new CgSolver(m,100);
    //DirectSolver ds = new DirectSolver(m);
    double niter = 0.0;
    for (int i2=0; i2<n2; ++i2) {
      int i1b = (i2%2==0)?0:n1-1;
      int i1e = (i2%2==0)?n1:-1;
      int i1s = (i2%2==0)?1:-1;
      for (int i1=i1b; i1!=i1e; i1+=i1s) {
        for (int k=0; k<m; ++k) {
          for (int j=0; j<m; ++j)
            rkjt[k][j] = rkj[k][j][i2][i1];
          rk0t[k] = rk0[k][i2][i1];
        }
        niter += cgs.solve(rkjt,rk0t,at);
        //boolean bad = ds.solve(rkjt,rk0t,at);
        //if (bad)
        //  System.out.println("bad for i1="+i1+" i2="+i2);
        for (int i=0; i<m; ++i)
          a[i][i2][i1] = (float)at[i];
      }
    }
    niter /= n1;
    niter /= n2;
    System.out.println("Average number of CG iterations = "+niter);

    // Apply prediction filters.
    zero(g);
    for (int j=0; j<m; ++j) {
      int j1 = lag1[j];
      int j2 = lag2[j];
      float[][] aj = a[j];
      int i1min = max(0,j1);
      int i1max = min(n1,n1+j1);
      int i2min = max(0,j2);
      int i2max = min(n2,n2+j2);
      for (int i2=i2min; i2<i2max; ++i2) {
        for (int i1=i1min; i1<i1max; ++i1) {
          g[i2][i1] += aj[i2][i1]*f[i2-j2][i1-j1];
        }
      }
    }

    return a;
    //return rcache.get();
    
    //copy(div(rcache.get(1,1),rcache.get(0,0)),g);
    //copy(a[0],g);
    //copy(a[m-1],f);
  }

  public void applyPef(int[] lag1, int[] lag2, float[][] f, float[][] g) {
    Check.argument(lag1.length==lag2.length,"lag1.length==lag2.length");
    Check.argument(f!=g,"f!=g");

    // Compute local auto-correlation for all necessary lags.
    R2Cache rcache = new R2Cache(f);
    int m = lag1.length;
    float[][][][] rkj = new float[m][m][][];
    float[][][] rk0 = new float[m][][];
    for (int k=0; k<m; ++k) {
      int k1 = lag1[k];
      int k2 = lag2[k];
      for (int j=0; j<m; ++j) {
        int j1 = lag1[j];
        int j2 = lag2[j];
        rkj[k][j] = rcache.get(j1-k1,j2-k2);
      }
      rk0[k] = rcache.get(k1,k2);
    }

    // Compute prediction filters.
    int n1 = f[0].length;
    int n2 = f.length;
    double[][] rkjt = new double[m][m];
    double[] rk0t = new double[m];
    double[] at = new double[m];
    float[][][] a = new float[m][n2][n1];
    CgSolver cgs = new CgSolver(m,100);
    double niter = 0.0;
    for (int i2=0; i2<n2; ++i2) {
      int i1b = (i2%2==0)?0:n1-1;
      int i1e = (i2%2==0)?n1:-1;
      int i1s = (i2%2==0)?1:-1;
      for (int i1=i1b; i1!=i1e; i1+=i1s) {
        for (int k=0; k<m; ++k) {
          for (int j=0; j<m; ++j)
            rkjt[k][j] = rkj[k][j][i2][i1];
          rk0t[k] = rk0[k][i2][i1];
        }
        niter += cgs.solve(rkjt,rk0t,at);
        for (int i=0; i<m; ++i)
          a[i][i2][i1] = (float)at[i];
      }
    }
    niter /= n1;
    niter /= n2;
    System.out.println("Average number of CG iterations = "+niter);

    // Apply prediction error filters.
    copy(f,g);
    for (int j=0; j<m; ++j) {
      int j1 = lag1[j];
      int j2 = lag2[j];
      float[][] aj = a[j];
      int i1min = max(0,j1);
      int i1max = min(n1,n1+j1);
      int i2min = max(0,j2);
      int i2max = min(n2,n2+j2);
      for (int i2=i2min; i2<i2max; ++i2) {
        for (int i1=i1min; i1<i1max; ++i1) {
          g[i2][i1] -= aj[i2][i1]*f[i2-j2][i1-j1];
        }
      }
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  /*
  private static class DirectSolver {
    DirectSolver(int m) {
      this.m = m;
      this.a = new DMatrix(m,m);
      this.b = new DMatrix(m,1);
      this.aa = a.getArray();
      this.bb = b.getArray();
    }
    boolean solve(double[][] a, double[] b, double[] x) {
      for (int i=0,k=0; i<m; ++i) {
        for (int j=0; j<m; ++j,++k) {
          aa[k] = a[i][j];
        }
        bb[i] = b[i];
      }
      double chuge = 1000000.0;
      double c = this.a.cond();
      if (c>chuge) {
        System.out.println("cond="+c);
        System.out.println("a=\n"+this.a);
        System.out.println("b=\n"+this.b);
      }
      DMatrix xm = this.a.solve(this.b);
      if (c>chuge) {
        System.out.println("x=\n"+xm);
        System.out.println("r=\n"+this.b.minus(this.a.times(xm)));
        System.out.println("d=\n"+this.a.evd().getD());
      }
      double[] xx = xm.getArray();
      for (int i=0; i<m; ++i)
        x[i] = xx[i];
      return c>chuge;
    }
    private int m;
    private DMatrix a,b;
    private double[] aa,bb;
  }
  */

  private static class CgSolver {
    CgSolver(int m, int maxiter) {
      this.m = m;
      this.maxiter = maxiter;
      this.p = new double[m];
      this.q = new double[m];
      this.r = new double[m];
    }
    int solve(double[][] a, double[] b, double[] x) {
      double rp = 0.0;
      double rr = 0.0;
      double bb = 0.0;
      for (int i=0; i<m; ++i) {
        double[] ai = a[i];
        double ax = 0.0;
        for (int j=0; j<m; ++j)
          ax += ai[j]*x[j];
        double bi = b[i];
        double ri = r[i] = bi-ax;
        bb += bi*bi;
        rr += ri*ri;
      }
      double small = bb*TINY;
      int niter;
      for (niter=0; niter<maxiter && rr>small; ++niter) {
        if (niter==0) {
          for (int i=0; i<m; ++i)
            p[i] = r[i];
        } else {
          double beta = rr/rp;
          for (int i=0; i<m; ++i)
            p[i] = r[i]+beta*p[i];
        }
        double pq = 0.0;
        for (int i=0; i<m; ++i) {
          double[] ai = a[i];
          double ap = 0.0;
          for (int j=0; j<m; ++j)
            ap += ai[j]*p[j];
          q[i] = ap;
          pq += p[i]*q[i];
        }
        double alpha = rr/pq;
        rp = rr;
        rr = 0.0;
        for (int i=0; i<m; ++i) {
          x[i] += alpha*p[i];
          r[i] -= alpha*q[i];
          rr += r[i]*r[i];
        }
      }
      if (rr>small)
        System.out.println("CgSolver.solve: failed to converge");

      /*
      // Check
      double rc = 0.0;
      for (int i=0; i<m; ++i) {
        double[] ai = a[i];
        double ax = 0.0;
        for (int j=0; j<m; ++j)
          ax += ai[j]*x[j];
        double bi = b[i];
        double ri = bi-ax;
        bb += bi*bi;
        rc += ri*ri;
      }
      System.out.println(
        "niter="+niter+" bb="+bb+" rr="+rr+" rc="+rc);
      */
      return niter;
    }
    private static final double TINY = ulp(1.0f);
    private int m,maxiter;
    private double[] p,q,r;
  }

  private class R2 {
    int l1,l2;
    float[][] r;
    R2(int l1, int l2, float[][] f) {
      int n1 = f[0].length;
      int n2 = f.length;
      this.l1 = l1;
      this.l2 = l2;
      this.r = new float[n2][n1];
      _lcf.setInputs(f,f);
      _lcf.correlate(l1,l2,r);
      if (l1==0 && l2==0) {
        for (int i2=0; i2<n2; ++i2) {
          for (int i1=0; i1<n1; ++i1) {
            r[i2][i1] *= 1.01f;
          }
        }
      }
    }
  }
  private class R2Cache {
    R2Cache(float[][] f) {
      _f = f;
    }
    float[][][] get() {
      int n = _rlist.size();
      float[][][] r = new float[n][][];
      int i = 0;
      for (R2 r2 : _rlist) {
        r[i] = r2.r;
        ++i;
      }
      return r;
    }
    float[][] get(int l1, int l2) {
      for (R2 r2 : _rlist) {
        if (l1==r2.l1 && l2==r2.l2 || -l1==r2.l1 && -l2==r2.l2)
          return r2.r;
      }
      R2 r2 = new R2(l1,l2,_f);
      _rlist.add(r2);
      return r2.r;
    }
    float[][] _f;
    ArrayList<R2> _rlist = new ArrayList<R2>();
  }

  private LocalCorrelationFilter _lcf;
}
