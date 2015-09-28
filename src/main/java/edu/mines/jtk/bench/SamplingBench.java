/****************************************************************************
Copyright 2004, Colorado School of Mines and others.
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
package edu.mines.jtk.bench;

import static edu.mines.jtk.util.MathPlus.*;

/**
 * Demonstrate roundoff error for typical computations with uniform sampling.
 * @author Dave Hale, Colorado School of Mines
 * @version 2005.03.08
 */
public class SamplingBench {

  public static void main(String[] args) {
    float f = 0.0f;
    float d = 1.0f/3.0f;
    int n = 10000;
    double errorMaximum = 0.0;
    float x = f;
    for (int i=0; i<n; ++i,x+=d) {
      double xd = f+i*d;
      errorMaximum = max(errorMaximum,abs(xd-x));
    }
    double errorEstimate = FLT_EPSILON*n*(abs(f)+abs(f+(n-1)*d))/2.0;
    System.out.println("error estimate ="+errorEstimate/d);
    System.out.println("error maximum  ="+errorMaximum/d);
  }
}
