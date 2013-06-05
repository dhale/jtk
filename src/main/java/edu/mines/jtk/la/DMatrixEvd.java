/****************************************************************************
Copyright (c) 2005, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.la;

import static edu.mines.jtk.util.ArrayMath.*;
import edu.mines.jtk.util.Check;

/**
 * Eigenvalue and eigenvector decomposition of a square matrix A.
 * <p>
 * If A is symmetric, then A = V*D*V' where the matrix of eigenvalues D
 * is diagonal and the matrix of eigenvectors V is orthogonal (V*V' = I).
 * <p>
 * If A is not symmetric, then the eigenvalue matrix D is block diagonal
 * with real eigenvalues in 1-by-1 blocks and any complex eigenvalues
 * lambda + i*mu in 2-by-2 block [lambda, mu; -mu, lambda]. The columns
 * of V represent the eigenvectors in the sense that A*V = V*D. The matrix
 * V may be badly conditioned or even singular, so the validity of the
 * equation A = V*D*inverse(V) depends on the condition number of V.
 * <p>
 * This class was adapted from the package Jama, which was developed by 
 * Joe Hicklin, Cleve Moler, and Peter Webb of The MathWorks, Inc., and by
 * Ronald Boisvert, Bruce Miller, Roldan Pozo, and Karin Remington of the
 * National Institue of Standards and Technology.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.12.01
 */
public class DMatrixEvd {

  /** 
   * Constructs an eigenvalue decomposition for the specified square matrix.
   * @param a the square matrix
   */
  public DMatrixEvd(DMatrix a) {
    Check.argument(a.isSquare(),"matrix a is square");
    double[][] aa = a.getArray();
    int n = a.getN();
    _n = n;
    _v = new double[n][n];
    _d = new double[n];
    _e = new double[n];
    if (a.isSymmetric()) {
      for (int i=0; i<n; ++i) {
        for (int j=0; j<n; ++j) {
          _v[i][j] = aa[i][j];
        }
      }
      tred2(); // tridiagonalize
      tql2(); // diagonalize
    } else {
      _h = new double[n][n];
      for (int i=0; i<n; ++i) {
        for (int j=0; j<n; ++j) {
          _h[i][j] = aa[i][j];
        }
      }
      orthes(); // reduce to Hessenberg form
      hqr2(); // reduce Hessenberg to real Schur form
    }
  }

  /** 
   * Gets the matrix of eigenvectors V.
   * @return the matrix V.
   */
  public DMatrix getV() {
    return new DMatrix(_n,_n,_v);
  }

  /** 
   * Gets the block diagonal matrix of eigenvalues D.
   * @return the matrix D.
   */
  public DMatrix getD() {
    DMatrix d = new DMatrix(_n,_n);
    double[][] da = d.getArray();
    for (int i=0; i<_n; ++i) {
      for (int j=0; j<_n; ++j) {
        da[i][j] = 0.0;
      }
      da[i][i] = _d[i];
      if (_e[i]>0.0) {
        da[i][i+1] = _e[i];
      } else if (_e[i]<0.0) {
        da[i][i-1] = _e[i];
      }
    }
    return d;
  }

  /** 
   * Gets the real parts of the eigenvalues.
   * @return array of real parts = real(diag(D)).
   */
  public double[] getRealEigenvalues() {
    return copy(_d);
  }

  /** 
   * Gets the imaginary parts of the eigenvalues
   * @return array of imaginary parts = imag(diag(D))
   */
  public double[] getImagEigenvalues() {
    return copy(_e);
  }

  ///////////////////////////////////////////////////////////////////////////
  // private

  private int _n; // row and column dimensions for square matrix V
  private double[][] _v; // eigenvectors V
  private double[] _d, _e; // eigenvalues
  private double[][] _h; // nonsymmetric Hessenberg form

  // Symmetric Householder reduction to tridiagonal form.
  // This is derived from the Algol procedures tred2 by
  // Bowdler, Martin, Reinsch, and Wilkinson, Handbook for
  // Auto. Comp., Vol.ii-Linear Algebra, and the corresponding
  // Fortran subroutine in EISPACK.
  private void tred2() {
    int n = _n;
    for (int j=0; j<n; ++j)
      _d[j] = _v[n-1][j];

    // Householder reduction to tridiagonal form.
    for (int i=n-1; i>0; --i) {
   
      // Scale to avoid under/overflow.
      double scale = 0.0;
      double h = 0.0;
      for (int k=0; k<i; ++k)
        scale += abs(_d[k]);
      if (scale==0.0) {
        _e[i] = _d[i-1];
        for (int j=0; j<i; ++j) {
          _d[j] = _v[i-1][j];
          _v[i][j] = 0.0;
          _v[j][i] = 0.0;
        }
      } else {
   
        // Generate Householder vector.
        for (int k=0; k<i; ++k) {
          _d[k] /= scale;
          h += _d[k]*_d[k];
        }
        double f = _d[i-1];
        double g = sqrt(h);
        if (f>0.0)
          g = -g;
        _e[i] = scale*g;
        h -= f * g;
        _d[i-1] = f-g;
        for (int j=0; j<i; ++j)
          _e[j] = 0.0;
   
        // Apply similarity transformation to remaining columns.
        for (int j=0; j<i; ++j) {
          f = _d[j];
          _v[j][i] = f;
          g = _e[j]+_v[j][j]*f;
          for (int k = j+1; k<=i-1; ++k) {
            g += _v[k][j]*_d[k];
            _e[k] += _v[k][j]*f;
          }
          _e[j] = g;
        }
        f = 0.0;
        for (int j=0; j<i; ++j) {
          _e[j] /= h;
          f += _e[j]*_d[j];
        }
        double hh = f/(h+h);
        for (int j=0; j<i; ++j) {
          _e[j] -= hh*_d[j];
        }
        for (int j=0; j<i; ++j) {
          f = _d[j];
          g = _e[j];
          for (int k=j; k<=i-1; ++k) {
            _v[k][j] -= f*_e[k]+g*_d[k];
          }
          _d[j] = _v[i-1][j];
          _v[i][j] = 0.0;
        }
      }
      _d[i] = h;
    }
   
    // Accumulate transformations.
    for (int i=0; i<n-1; ++i) {
      _v[n-1][i] = _v[i][i];
      _v[i][i] = 1.0;
      double h = _d[i+1];
      if (h!=0.0) {
        for (int k=0; k<=i; ++k)
          _d[k] = _v[k][i+1]/h;
        for (int j=0; j<=i; ++j) {
          double g = 0.0;
          for (int k=0; k<=i; ++k)
            g += _v[k][i+1]*_v[k][j];
          for (int k=0; k<=i; ++k)
            _v[k][j] -= g*_d[k];
        }
      }
      for (int k=0; k<=i; ++k)
        _v[k][i+1] = 0.0;
    }
    for (int j=0; j<n; ++j) {
      _d[j] = _v[n-1][j];
      _v[n-1][j] = 0.0;
    }
    _v[n-1][n-1] = 1.0;
    _e[0] = 0.0;
  }

  // Symmetric tridiagonal QL algorithm.
  // This is derived from the Algol procedures tql2, by
  // Bowdler, Martin, Reinsch, and Wilkinson, Handbook for
  // Auto. Comp., Vol.ii-Linear Algebra, and the corresponding
  // Fortran subroutine in EISPACK.
  private void tql2() {
    int n = _n;
    for (int i=1; i<n; ++i)
      _e[i-1] = _e[i];
    _e[n-1] = 0.0;
    double f = 0.0;
    double tst1 = 0.0;
    double eps = pow(2.0,-52.0);
    for (int l=0; l<n; ++l) {

      // Find small subdiagonal element.
      tst1 = max(tst1,abs(_d[l])+abs(_e[l]));
      int m = l;
      while (m<n) {
        if (abs(_e[m])<=eps*tst1)
          break;
        ++m;
      }
   
      // If m==l, d[l] is an eigenvalue; otherwise, iterate.
      if (m>l) {
        //int iter = 0;
        do {
          //++iter;  // (Could check iteration count here.)

          // Compute implicit shift
          double g = _d[l];
          double p = (_d[l+1] - g) / (2.0 * _e[l]);
          double r = hypot(p,1.0);
          if (p<0)
            r = -r;
          _d[l] = _e[l]/(p+r);
          _d[l+1] = _e[l]*(p+r);
          double dl1 = _d[l+1];
          double h = g-_d[l];
          for (int i=l+2; i<n; ++i)
            _d[i] -= h;
          f += h;
   
          // Implicit QL transformation.
          p = _d[m];
          double c = 1.0;
          double c2 = c;
          double c3 = c;
          double el1 = _e[l+1];
          double s = 0.0;
          double s2 = 0.0;
          for (int i=m-1; i>=l; --i) {
            c3 = c2;
            c2 = c;
            s2 = s;
            g = c * _e[i];
            h = c * p;
            r = hypot(p,_e[i]);
            _e[i+1] = s*r;
            s = _e[i]/r;
            c = p/r;
            p = c*_d[i]-s*g;
            _d[i+1] = h+s*(c*g+s*_d[i]);
   
            // Accumulate transformation.
            for (int k=0; k<n; ++k) {
              h = _v[k][i+1];
              _v[k][i+1] = s*_v[k][i]+c*h;
              _v[k][i] = c*_v[k][i]-s*h;
            }
          }
          p = -s*s2*c3*el1*_e[l]/dl1;
          _e[l] = s*p;
          _d[l] = c*p;
   
          // Check for convergence.
        } while (abs(_e[l])>eps*tst1);
      }
      _d[l] += f;
      _e[l] = 0.0;
    }
     
    // Sort eigenvalues and corresponding vectors.
    for (int i=0; i<n-1; ++i) {
      int k = i;
      double p = _d[i];
      for (int j = i+1; j<n; ++j) {
        if (_d[j]<p) {
          k = j;
          p = _d[j];
        }
      }
      if (k!=i) {
        _d[k] = _d[i];
        _d[i] = p;
        for (int j=0; j<n; ++j) {
          p = _v[j][i];
          _v[j][i] = _v[j][k];
          _v[j][k] = p;
        }
      }
    }
  }

  // Nonsymmetric reduction to Hessenberg form.
  // This is derived from the Algol procedures orthes and ortran,
  // by Martin and Wilkinson, Handbook for Auto. Comp.,
  // Vol.ii-Linear Algebra, and the corresponding
  // Fortran subroutines in EISPACK.
  private void orthes() {
    int n = _n;
    int low = 0;
    int high = n-1;
    double[] ort = new double[n];
    for (int m=low+1; m<=high-1; ++m) {
   
      // Scale column.
      double scale = 0.0;
      for (int i=m; i<=high; ++i)
        scale += abs(_h[i][m-1]);
      if (scale!=0.0) {
   
        // Compute Householder transformation.
        double h = 0.0;
        for (int i=high; i>=m; --i) {
          ort[i] = _h[i][m-1]/scale;
          h += ort[i]*ort[i];
        }
        double g = sqrt(h);
        if (ort[m]>0.0)
          g = -g;
        h -= ort[m]*g;
        ort[m] -= g;
   
        // Householder similarity transformation H = (I-u*u'/h)*H*(I-u*u')/h).
        for (int j=m; j<n; ++j) {
          double f = 0.0;
          for (int i=high; i>=m; --i)
            f += ort[i]*_h[i][j];
          f /= h;
          for (int i=m; i<=high; ++i)
            _h[i][j] -= f*ort[i];
        }
        for (int i=0; i<=high; ++i) {
          double f = 0.0;
          for (int j=high; j>=m; --j)
            f += ort[j]*_h[i][j];
          f /= h;
          for (int j=m; j<=high; ++j)
            _h[i][j] -= f*ort[j];
        }
        ort[m] = scale*ort[m];
        _h[m][m-1] = scale*g;
      }
    }
 
    // Accumulate transformations (Algol's ortran).
    for (int i=0; i<n; ++i) {
      for (int j=0; j<n; ++j)
        _v[i][j] = (i==j?1.0:0.0);
    }

    for (int m=high-1; m>=low+1; --m) {
      if (_h[m][m-1]!=0.0) {
        for (int i=m+1; i<=high; ++i)
          ort[i] = _h[i][m-1];
        for (int j=m; j<=high; ++j) {
          double g = 0.0;
          for (int i=m; i<=high; ++i)
            g += ort[i]*_v[i][j];
          g = (g/ort[m])/_h[m][m-1]; // double division avoids underflow
          for (int i=m; i<=high; ++i)
            _v[i][j] += g*ort[i];
        }
      }
    }
  }


  // Complex scalar division.
  private double _cdivr, _cdivi;
  private void cdiv(double xr, double xi, double yr, double yi) {
    double r,d;
    if (abs(yr) > abs(yi)) {
      r = yi/yr;
      d = yr+r*yi;
      _cdivr = (xr+r*xi)/d;
      _cdivi = (xi-r*xr)/d;
    } else {
      r = yr/yi;
      d = yi+r*yr;
      _cdivr = (r*xr+xi)/d;
      _cdivi = (r*xi-xr)/d;
    }
  }


  // Nonsymmetric reduction from Hessenberg to real Schur form.
  // This is derived from the Algol procedure hqr2,
  // by Martin and Wilkinson, Handbook for Auto. Comp.,
  // Vol.ii-Linear Algebra, and the corresponding
  // Fortran subroutine in EISPACK.
  private void hqr2() {
   
      // Initialize
    int nn = _n;
    int n = nn-1;
    int low = 0;
    int high = nn-1;
    double eps = pow(2.0,-52.0);
    double exshift = 0.0;
    double p=0.0,q=0.0,r=0.0,s=0.0,z=0.0,t,w,x,y;
   
    // Store roots isolated by balance and compute matrix norm
    double norm = 0.0;
    for (int i=0; i<nn; ++i) {
      if (i<low || i>high) {
        _d[i] = _h[i][i];
        _e[i] = 0.0;
      }
      for (int j=max(i-1,0); j<nn; ++j)
        norm += abs(_h[i][j]);
    }
   
    // Outer loop over eigenvalue index
    int iter = 0;
    while (n>=low) {
   
      // Look for single small sub-diagonal element
      int l = n;
      while (l>low) {
        s = abs(_h[l-1][l-1])+abs(_h[l][l]);
        if (s==0.0)
          s = norm;
        if (abs(_h[l][l-1])<eps*s)
          break;
        --l;
      }
       
      // Check for convergence.

      // If one root found, ...
      if (l==n) {
        _h[n][n] = _h[n][n] + exshift;
        _d[n] = _h[n][n];
        _e[n] = 0.0;
        --n;
        iter = 0;
      }
         
      // else if two roots found, ...
      else if (l==n-1) {
        w = _h[n][n-1]*_h[n-1][n];
        p = (_h[n-1][n-1]-_h[n][n])/2.0;
        q = p*p+w;
        z = sqrt(abs(q));
        _h[n][n] = _h[n][n]+exshift;
        _h[n-1][n-1] = _h[n-1][n-1]+exshift;
        x = _h[n][n];
   
        // If real pair, ...
        if (q>=0) {
          if (p>=0) {
            z = p+z;
          } else {
            z = p-z;
          }
          _d[n-1] = x+z;
          _d[n] = _d[n-1];
          if (z!=0.0)
            _d[n] = x-w/z;
          _e[n-1] = 0.0;
          _e[n] = 0.0;
          x = _h[n][n-1];
          s = abs(x)+abs(z);
          p = x/s;
          q = z/s;
          r = sqrt(p*p+q*q);
          p /= r;
          q /= r;
   
          // Row modification
          for (int j=n-1; j<nn; ++j) {
            z = _h[n-1][j];
            _h[n-1][j] = q*z+p*_h[n][j];
            _h[n][j] = q*_h[n][j]-p*z;
          }
   
          // Column modification
          for (int i=0; i<=n; ++i) {
            z = _h[i][n-1];
            _h[i][n-1] = q*z+p*_h[i][n];
            _h[i][n] = q*_h[i][n]-p*z;
          }

          // Accumulate transformations
          for (int i=low; i<=high; ++i) {
            z = _v[i][n-1];
            _v[i][n-1] = q*z+p*_v[i][n];
            _v[i][n] = q*_v[i][n]-p*z;
          }
        }
 
        // else if complex pair, ...
        else {
          _d[n-1] = x+p;
          _d[n] = x+p;
          _e[n-1] = z;
          _e[n] = -z;
        }
        n -= 2;
        iter = 0;
      }
   
      // else if no convergence yet, ...
      else {
   
        // Form shift
        x = _h[n][n];
        y = 0.0;
        w = 0.0;
        if (l<n) {
          y = _h[n-1][n-1];
          w = _h[n][n-1]*_h[n-1][n];
        }
   
        // Wilkinson's original ad hoc shift
        if (iter==10) {
          exshift += x;
          for (int i=low; i<=n; ++i)
            _h[i][i] -= x;
          s = abs(_h[n][n-1])+abs(_h[n-1][n-2]);
          x = y = 0.75*s;
          w = -0.4375*s*s;
        }

        // MATLAB's new ad hoc shift
        if (iter==30) {
          s = (y-x)/2.0;
          s = s*s+w;
          if (s>0.0) {
            s = sqrt(s);
            if (y<x)
              s = -s;
            s = x-w/((y-x)/2.0+s);
            for (int i=low; i<=n; ++i)
              _h[i][i] -= s;
            exshift += s;
            x = y = w = 0.964;
          }
        }
 
        ++iter;   // (Could check iteration count here.)
   
        // Look for two consecutive small sub-diagonal elements
        int m = n-2;
        while (m>=l) {
          z = _h[m][m];
          r = x-z;
          s = y-z;
          p = (r*s-w)/_h[m+1][m]+_h[m][m+1];
          q = _h[m+1][m+1]-z-r-s;
          r = _h[m+2][m+1];
          s = abs(p)+abs(q)+abs(r);
          p /= s;
          q /= s;
          r /= s;
          if (m==l)
            break;
          if (abs(_h[m][m-1])*(abs(q)+abs(r)) <
              eps*(abs(p)*(abs(_h[m-1][m-1])+abs(z)+abs(_h[m+1][m+1])))) {
            break;
          }
          --m;
        }
        for (int i=m+2; i<=n; ++i) {
          _h[i][i-2] = 0.0;
          if (i>m+2)
            _h[i][i-3] = 0.0;
        }
   
        // Double QR step involving rows l:n and columns m:n
        for (int k=m; k<=n-1; ++k) {
          boolean notlast = (k!=n-1);
          if (k!=m) {
            p = _h[k][k-1];
            q = _h[k+1][k-1];
            r = notlast?_h[k+2][k-1]:0.0;
            x = abs(p)+abs(q)+abs(r);
            if (x!=0.0) {
              p /= x;
              q /= x;
              r /= x;
            }
          }
          if (x==0.0)
            break;
          s = sqrt(p*p+q*q+r*r);
          if (p<0.0)
            s = -s;
          if (s!=0.0) {
            if (k!=m) {
              _h[k][k-1] = -s*x;
            } else if (l!=m) {
              _h[k][k-1] = -_h[k][k-1];
            }
            p = p+s;
            x = p/s;
            y = q/s;
            z = r/s;
            q = q/p;
            r = r/p;
   
            // Row modification
            for (int j=k; j<nn; ++j) {
              p = _h[k][j]+q*_h[k+1][j];
              if (notlast) {
                p = p+r*_h[k+2][j];
                _h[k+2][j] = _h[k+2][j]-p*z;
              }
              _h[k][j] = _h[k][j]-p*x;
              _h[k+1][j] = _h[k+1][j]-p*y;
            }
 
            // Column modification
            for (int i=0; i<=min(n,k+3); ++i) {
              p = x*_h[i][k]+y*_h[i][k+1];
              if (notlast) {
                p = p+z*_h[i][k+2];
                _h[i][k+2] = _h[i][k+2]-p*r;
              }
              _h[i][k] = _h[i][k]-p;
              _h[i][k+1] = _h[i][k+1]-p*q;
            }
 
            // Accumulate transformations
            for (int i=low; i<=high; ++i) {
              p = x*_v[i][k]+y*_v[i][k+1];
              if (notlast) {
                p = p+z*_v[i][k+2];
                _v[i][k+2] = _v[i][k+2]-p*r;
              }
              _v[i][k] = _v[i][k]-p;
              _v[i][k+1] = _v[i][k+1]-p*q;
            }
          }  // (s!=0.0)
        }  // k loop
      }  // check convergence
    }  // while (n>=low)
      
    // Backsubstitute to find vectors of upper triangular form
    if (norm==0.0)
      return;
   
    for (n=nn-1; n>=0; --n) {
      p = _d[n];
      q = _e[n];
 
      // If real vector, ...
      if (q==0.0) {
        int l = n;
        _h[n][n] = 1.0;
        for (int i=n-1; i>=0; --i) {
          w = _h[i][i]-p;
          r = 0.0;
          for (int j=l; j<=n; ++j)
            r = r+_h[i][j]*_h[j][n];
          if (_e[i]<0.0) {
            z = w;
            s = r;
          } else {
            l = i;
            if (_e[i]==0.0) {
              if (w!=0.0) {
                _h[i][n] = -r/w;
              } else {
                _h[i][n] = -r/(eps*norm);
              }
            }

            // Solve real equations
            else {
              x = _h[i][i+1];
              y = _h[i+1][i];
              q = (_d[i]-p)*(_d[i]-p)+_e[i]*_e[i];
              t = (x*s-z*r)/q;
              _h[i][n] = t;
              if (abs(x)>abs(z)) {
                _h[i+1][n] = (-r-w*t)/x;
              } else {
                _h[i+1][n] = (-s-y*t)/z;
              }
            }
 
            // Overflow control
            t = abs(_h[i][n]);
            if ((eps*t)*t>1.0) {
              for (int j=i; j<=n; ++j)
                _h[j][n] /= t;
            }
          }
        }
      }

      // else if omplex vector, ...
      else if (q<0.0) {
        int l = n-1;

        // Last vector component imaginary so matrix is triangular
        if (abs(_h[n][n-1])>abs(_h[n-1][n])) {
          _h[n-1][n-1] = q/_h[n][n-1];
          _h[n-1][n] = -(_h[n][n]-p)/_h[n][n-1];
        } else {
          cdiv(0.0,-_h[n-1][n],_h[n-1][n-1]-p,q);
          _h[n-1][n-1] = _cdivr;
          _h[n-1][n] = _cdivi;
        }
        _h[n][n-1] = 0.0;
        _h[n][n] = 1.0;
        for (int i=n-2; i>=0; --i) {
          double ra,sa,vr,vi;
          ra = 0.0;
          sa = 0.0;
          for (int j=l; j<=n; ++j) {
            ra += _h[i][j]*_h[j][n-1];
            sa += _h[i][j]*_h[j][n];
          }
          w = _h[i][i]-p;
          if (_e[i]<0.0) {
            z = w;
            r = ra;
            s = sa;
          } else {
            l = i;
            if (_e[i]==0.0) {
              cdiv(-ra,-sa,w,q);
              _h[i][n-1] = _cdivr;
              _h[i][n] = _cdivi;
            } else {
 
              // Solve complex equations
              x = _h[i][i+1];
              y = _h[i+1][i];
              vr = (_d[i]-p)*(_d[i]-p)+_e[i]*_e[i]-q*q;
              vi = (_d[i]-p)*2.0*q;
              if (vr==0.0 && vi==0.0)
                vr = eps*norm*(abs(w)+abs(q)+abs(x)+abs(y)+abs(z));
              cdiv(x*r-z*ra+q*sa,x*s-z*sa-q*ra,vr,vi);
              _h[i][n-1] = _cdivr;
              _h[i][n] = _cdivi;
              if (abs(x)>(abs(z)+abs(q))) {
                _h[i+1][n-1] = (-ra-w*_h[i][n-1]+q*_h[i][n])/x;
                _h[i+1][n] = (-sa-w*_h[i][n]-q*_h[i][n-1])/x;
              } else {
                cdiv(-r-y*_h[i][n-1],-s-y*_h[i][n],z,q);
                _h[i+1][n-1] = _cdivr;
                _h[i+1][n] = _cdivi;
              }
            }
   
            // Overflow control
            t = max(abs(_h[i][n-1]),abs(_h[i][n]));
            if ((eps*t)*t>1.0) {
              for (int j=i; j< n; ++j) {
                _h[j][n-1] /= t;
                _h[j][n] /= t;
              }
            }
          }
        }
      }
    }
   
    // Vectors of isolated roots
    for (int i=0; i<nn; ++i) {
      if (i<low || i>high) {
        for (int j=i; j<nn; ++j)
          _v[i][j] = _h[i][j];
      }
    }
 
    // Back transformation to get eigenvectors of original matrix
    for (int j=nn-1; j>=low; --j) {
      for (int i=low; i<=high; ++i) {
        z = 0.0;
        for (int k=low; k<=min(j,high); ++k)
          z += _v[i][k]*_h[k][j];
        _v[i][j] = z;
      }
    }
  }
}
