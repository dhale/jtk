/****************************************************************************
Copyright (c) 2007, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.util;

/**
 * Utilities for working with multiple threads.
 * @author Dave Hale, Colorado School of Mines
 * @version 2007.07.27
 */
public class Threads {

  /**
   * Returns a new array for threads. The length of the array is twice 
   * the number of available processors. Note that this method does not 
   * actually construct any threads.
   * @return the array.
   */
  public static Thread[] makeArray() {
    return makeArray(2);
  }

  /**
   * Returns a new array for threads. The length of the array is at least 
   * one and is proportional to the number of available processors. Note 
   * that this method does not actually construct any threads.
   * @param multiple desired number of threads per processor.
   * @return the array, with length for at least one thread.
   */
  public static Thread[] makeArray(double multiple) {
    int processors = Runtime.getRuntime().availableProcessors();
    int nthread = Math.max(1,(int)(multiple*processors));
    return new Thread[nthread];
  }

  /**
   * Starts and joins all threads in the specified array.
   * @param threads array of threads.
   * @exception RuntimeException if any threads are interrupted.
   */
  public static void startAndJoin(Thread[] threads) {
    for (int ithread=0; ithread<threads.length; ++ithread)
      threads[ithread].start();
    try {
      for (int ithread=0; ithread<threads.length; ++ithread)
        threads[ithread].join();
    } catch (InterruptedException ie) {
      throw new RuntimeException(ie);
    }
  }
}
