package edu.mines.jves.bench;

import edu.mines.jves.util.Stopwatch;

/**
 * Benchmark ThreadLocal with respect to static native methods.
 * @author Dave Hale
 * @version 2004.11.08
 */
public class TlBench {

  public static void main(String[] args) {
    double maxtime = 1.0;
    Stopwatch sw = new Stopwatch();
    int ncall,rate;

    // Time calls to trivial native method.
    sw.restart();
    for (ncall=0; sw.time()<maxtime; ++ncall) {
      foo(ncall);
    }
    sw.stop();
    rate = (int)(ncall/sw.time());
    System.out.println("foo: rate="+rate);

    // Time calls to trivial native method with thread-specific context.
    Context context = new Context();
    context.pointer = 314159;
    contexts.set(context);
    sw.restart();
    for (ncall=0; sw.time()<maxtime; ++ncall) {
      context = (Context)contexts.get();
      bar(context.pointer,ncall);
    }
    sw.stop();
    rate = (int)(ncall/sw.time());
    System.out.println("bar: rate="+rate);

  }

  private static class Context {
    long pointer;
  }

  private static ThreadLocal contexts = new ThreadLocal();
  private static native void foo(int i);
  private static native void bar(long pointer, int i);

  static {
    System.loadLibrary("edu_mines_jves_bench");
  }
}
