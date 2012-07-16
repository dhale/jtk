package edu.mines.jtk.util

/**
 * Demonstrates the use of {@link edu.mines.jtk.util.Stopwatch}.
 * @author Dave Hale, Colorado School of Mines
 * @version 2010.08.15
 */
object StopwatchDemo {
  def main(args: Array[String]) {
    val sw = new Stopwatch
    var niter = 0
    sw.start()
    while (sw.time<1.0) {
      // Normally we would have some significant computation.
      // With this loop we are simply counting calls to sw.time.
      niter += 1
    }
    sw.stop()
    println(niter+" iterations in "+sw.time+" (about 1) second.")
  }
}
