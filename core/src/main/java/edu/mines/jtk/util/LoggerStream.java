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
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Wrap a Logger as a PrintStream.
    Useful mainly for porting code that previously
    logged to a System PrintStream or to a proxy.
    Calling LoggerStream.println()
    will call Logger.info() for Level.INFO.
    A call to flush() or to a println() method
    will flush previously written text, and
    complete a call to Logger.  You may be surprised
    by extra newlines, if you call print("\n")
    and flush() instead of println();

    @author W.S. Harlan, Landmark Graphics
 */
public class LoggerStream extends PrintStream {
  private Level _level = null;
  private Logger _logger = null;
  private ByteArrayOutputStream _baos = null;

  /** Wrap a Logger as a PrintStream .
      @param logger Everything written to this PrintStream
      will be passed to the appropriate method of the Logger
      @param level This indicates which method of the
      Logger should be called.
   */
  public LoggerStream(Logger logger, Level level) {
    super (new ByteArrayOutputStream(), true);
    _baos = (ByteArrayOutputStream) (this.out);
    _logger = logger;
    _level = level;
  }

  // from PrintStream
  @Override public synchronized void flush() {
    super.flush();
    if (_baos.size() ==0) return;
    String out1 = _baos.toString();

    _logger.log(_level,out1);

    _baos.reset();
  }

  // from PrintStream
  @Override public synchronized void println() {
    flush();
  }

  // from PrintStream
  @Override public synchronized void println(Object x) {
    print(x); // flush already adds a newline
    flush();
  }

  // from PrintStream
  @Override public synchronized void println(String x) {
    print(x); // flush already adds a newline
    flush();
  }

  // from PrintStream
  @Override public synchronized void close() {
    flush();
    super.close();
  }

  // from PrintStream
  @Override public synchronized boolean checkError() {
    flush();
    return super.checkError();
  }

  /** test code
      @param args command line
  */
  public static void main(String[] args) {
    Logger logger = Logger.getLogger("edu.mines.jtk.util");
    PrintStream psInfo = new LoggerStream(logger, Level.INFO);
    //PrintStream psWarning = new LoggerStream(logger, Level.WARNING);
    psInfo.print(3.);
    psInfo.println("*3.=9.");
    //if (false) {
    //  psWarning.print(3.);
    //  psWarning.println("*3.=9.");
    //}
    psInfo.print(3.);
    psInfo.flush();
    psInfo.println("*3.=9.");
    psInfo.println();
    psInfo.print("x");
    psInfo.close();
  }
}

