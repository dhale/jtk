/****************************************************************************
Copyright 2003, Landmark Graphics and others.
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
package edu.mines.jtk.util;

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
  void initReport(double initFraction);

  /** This method will be called with the current fraction
      of work done.  Values range from 0 at the beginning
      to 1 when all work is done.
      @param fraction Fraction of work done so far, from 0 to 1.
      This value must equal or exceed all values previously
      passed to this method or to initReport.
  */
  void report(double fraction);

    /**
     * If true, then any further progress should be cancelled.
     * @return true, when any requested work should be interrupted.
     * False, if progress is expected to run to completion.
     */
  boolean isCanceled();

  /** Empty implementation that does nothing.
   */
  Monitor NULL_MONITOR = new Monitor() {
      @Override
      public void initReport(final double initFraction) {}
      @Override
      public void report(final double fraction) {}
      @Override
      public boolean isCanceled() { return false; }
  };
}

