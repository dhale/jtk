package edu.mines.jtk.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
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
  private static List<PrintStream> s_printStreams
    = new LinkedList<PrintStream>();

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
    s_printStreams.add(new PrintStream(new FileOutputStream(fileName),
                                       true));
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
    if (System.getProperties().getProperty
        ("java.util.logging.config.file") == null &&
        System.getProperties().getProperty
        ("java.util.logging.config.class") == null) {
      try {
        LogManager.getLogManager().readConfiguration
          (new java.io.ByteArrayInputStream
           ("handlers=edu.mines.jtk.util.CleanHandler\n.level=INFO\n"
            .getBytes()));
      } catch (java.io.IOException e) {
        e.printStackTrace();
        throw new IllegalStateException("This should never fail "+
                                        e.getMessage());
      }
    }
  }
}

