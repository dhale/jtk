/****************************************************************************
Copyright (c) 2003, Landmark Graphics and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.LinkedList;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

/** An alternative to ConsoleHandler.  Uses CleanFormatter
    and System.out instead of SimpleFormatter and System.err
    @author W.S. Harlan, Landmark Graphics
*/
public class CleanHandler extends Handler {
  private static Collection<PrintStream> s_printStreams = new LinkedList<PrintStream>();

  /** Keep track of whether CleanHandler has been set as a default */
  private static boolean s_setDefault = false;

  /** Construct a new CleanHandler. */
  public CleanHandler() {
    setFormatter(new CleanFormatter());
  }

  /** All CleanHandlers will also log to this file.
      @param fileName Name of file to log to.
      @throws FileNotFoundException if file not found
   */
  public static void addGlobalLogFile(String fileName)
    throws FileNotFoundException
  {
    s_printStreams.add(new PrintStream(new FileOutputStream(fileName), true));
  }

  @Override public void publish(LogRecord record) {
    if (record == null || !isLoggable(record)) return;
    String message = getFormatter().format(record);
    if (message == null) return;
    if (record.getLevel().intValue() > Level.INFO.intValue()) {
      System.err.print(message);
      System.err.flush();
    } else {
      System.out.print(message);
      System.out.flush();
    }
    for (PrintStream ps: s_printStreams) {
      ps.print(message);
    }
  }

  @Override public void close() {}

  @Override public void flush() {}

  /** Call this from your code to test each type of log message */
  public static void testLogger() {
    setDefaultHandler();

    assert null !=
      CleanHandler.class.getResource("CleanHandler.properties");

    assert null != java.util.ResourceBundle.getBundle
      ("edu.mines.jtk.util.CleanHandler") : "can't find rb";

    Logger logger = Logger.getLogger("edu.mines.jtk.util",
                                     "edu.mines.jtk.util.CleanHandler");

    logger.severe("test a severe");
    logger.warning("test a warning");
    logger.info("test an info");
    logger.info("test a\\");
    logger.info(" continued info");
    logger.config("test an config");
    logger.fine("test a fine");
    logger.finer("test a finer");
    logger.finest("test a finest");
    logger.info("testmessage");
    logger.info("Try this:>>${testmessage}<<");
    logger.info("Try this:>>${testmessage}<< >>${testmessage}<<");
  }

  /** Test code
 * @param args command line */
  public static void main(String[] args) {
    testLogger();
  }

  /** If the user has not specified a java property for the global
      Handler, then set the default global handler to
      this CleanHandler at an INFO level.
   */
  public static void setDefaultHandler() {
    synchronized (CleanHandler.class) {
      if (s_setDefault)
        return;
      if (System.getProperties().getProperty
          ("java.util.logging.config.file") == null &&
          System.getProperties().getProperty
          ("java.util.logging.config.class") == null) {
        overrideExistingHandlers(Level.INFO); // nothing set previously
      }
      s_setDefault = true;
    }
  }

  /** Override any previously specified Handlers with the CleanHandler, and set Level.
      @param level Logging level for CleanHandler.
   */
  public static void overrideExistingHandlers(Level level) {
    try {
      LogManager.getLogManager().readConfiguration
        (new java.io.ByteArrayInputStream
         (("handlers=edu.mines.jtk.util.CleanHandler\n.level="+level.getName()+"\n")
          .getBytes()));
    } catch (java.io.IOException e) {
      throw new IllegalStateException
        ("This should never fail with I/O from a byte array"+e.getMessage());
    }
  }

}

