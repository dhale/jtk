/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.bench;

import edu.mines.jtk.util.Stopwatch;

/**
 * Benchmark ThreadLocal with respect to static native methods.
 * <p>
 * For example, in OpenGL, we may need to pass a function pointer
 * to a static native method. That function pointer is stored in an
 * OpenGL context. In any thread, either zero or one OpenGL context
 * is the current context. However, different threads can have
 * different (current) OpenGL contexts. Therefore, when a context
 * is made current in a thread, we set that context in a static 
 * ThreadLocal object, which maps the current thread to the current 
 * context. From that context, we can get the function pointer we
 * must pass to the native method.
 * <p>
 * Here, we benchmark the overhead of the ThreadLocal stuff, and 
 * compare with the overhead of a call to a trivial do-nothing native 
 * method.
 * @author Dave Hale, Colorado School of Mines
 * @version 2004.11.08
 */
public class ThreadLocalBench {

  public static void main(String[] args) {
    double maxtime = 1.0;
    Stopwatch sw = new Stopwatch();
    int ncall,rate;


    // Time calls to trivial native method.
    System.out.println("Benchmarking static native method calls:");
    sw.restart();
    for (ncall=0; sw.time()<maxtime; ++ncall) {
      nativeMethod(ncall);
    }
    sw.stop();
    rate = (int)(ncall/sw.time());
    System.out.println("  calls/sec = "+rate);

    // Time calls to trivial native method with thread-specific context.
    System.out.println("Benchmarking again with ThreadLocal context:");
    Context context = new Context();
    context.pointer = 314159;
    contexts.set(context);
    sw.restart();
    for (ncall=0; sw.time()<maxtime; ++ncall) {
      context = (Context)contexts.get();
      nativeMethodWithContext(context.pointer,ncall);
    }
    sw.stop();
    rate = (int)(ncall/sw.time());
    System.out.println("  calls/sec = "+rate);
  }

  private static class Context {
    long pointer;
  }

  private static ThreadLocal<Context> contexts = new ThreadLocal<Context>();
  private static native void nativeMethod(int i);
  private static native void nativeMethodWithContext(long pointer, int i);

  static {
    System.loadLibrary("edu_mines_jtk_bench");
  }
}
