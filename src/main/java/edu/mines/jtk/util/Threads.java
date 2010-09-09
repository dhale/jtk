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
   * Gets the number of available processors (cores).
   * @return the number of available processors (cores).
   */
  public static int getAvailableProcessors() {
    return Runtime.getRuntime().availableProcessors();
  }

  /**
   * Returns a new array for threads. The length of the array equals the
   * number of available processors (or cores). 
   * <p>
   * Note that this method does not actually construct any threads.
   * @return the array.
   */
  public static Thread[] makeArray() {
    return makeArray(1);
  }

  /**
   * Returns a new array for threads. The length of the array is greater
   * than zero and is proportional to the number of available processors 
   * (or cores). 
   * <p>
   * Note that this method does not actually construct any threads.
   * @param multiple desired number of threads per processor (or core).
   * @return the array, with length for at least one thread.
   */
  public static Thread[] makeArray(double multiple) {
    int nprocessors = getAvailableProcessors();
    int nthread = Math.max(1,(int)(multiple*nprocessors));
    return new Thread[nthread];
  }

  /**
   * Starts and joins all threads in the specified array.
   * @param threads array of threads.
   * @exception RuntimeException if any threads are interrupted.
   */
  public static void startAndJoin(Thread[] threads) {
    for (Thread thread:threads)
      thread.start();
    try {
      for (Thread thread:threads)
        thread.join();
    } catch (InterruptedException ie) {
      throw new RuntimeException(ie);
    }
  }
}
