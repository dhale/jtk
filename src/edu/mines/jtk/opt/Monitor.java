/****************************************************************************
Copyright (c) 2004, Landmark Graphics and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.opt;

/** Implement this interface to receive notifications of progress  
    @author W.S. Harlan
*/
public interface Monitor {
  /** Initialize the fraction of the work that was
      completed when the process started.
      If not called, then assumed to be 0.
      @param initFraction Fraction of work done when
      process started, from 0 to 1.
  */
  public void initReport(double initFraction);

  /** This method will be called with the current fraction
      of work done.  Values range from 0 at the beginning
      to 1 when all work is done.
      @param fraction Fraction of work done so far, from 0 to 1.
      This value must equal or exceed all values previously
      passed to this method or to initReport.
  */
  public void report(double fraction);
}

