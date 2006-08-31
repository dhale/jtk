package edu.mines.jtk.opt;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.*;

/** Format log messages without any extras. 
    @author W.S. Harlan, Landmark Graphics
 */
public class CleanFormatter extends Formatter {
  private static String s_prefix = "";

  /** Prefix a string to all warnings or severe errors
      We use this method only for unit tests,
      to distinguish the source of warnings.
      @param prefix new prefix for all lines
   */
  public static void setWarningPrefix(String prefix) {
    s_prefix = prefix;
  }

  private Level lastLevel = Level.INFO;
  // from Formatter
  @Override public synchronized String format(LogRecord record) {
    // First try default localization of entire string
    String message = formatMessage(record);
    if (message == null) return null;
    if (message.length() == 0) return message;

    // More advanced localization of substrings
    message = Localize.filter(message, record.getResourceBundle());

    if (message.endsWith("\\")) {
      message = message.substring(0,message.length()-1);
    } else if (message.matches("^\\s*\n?$")) {
    } else {
      message = message +"\n";
    }
    Level level = record.getLevel();
    if (level.equals(Level.INFO)) {
      // do nothing
    } else if (level.equals(Level.WARNING)) {
      if (message.indexOf("WARNING") == -1) {
        message = StringUtil.prependToLines(s_prefix+level+": ", message);
      } else {
        message = s_prefix+message;
      }
    } else if (level.equals(Level.SEVERE)) {
      message = StringUtil.prependToLines(level+": ", message);
      if (!lastLevel.equals(Level.SEVERE)) {
        message =
          s_prefix + "**** SEVERE WARNING **** ("+
          record.getSourceClassName()+ "." + record.getSourceMethodName()+" "+
          StringUtil.getTimeStamp(record.getMillis())+" "+
          "#" + record.getThreadID() + ")\n" + message;
      }
    } else if (level.equals(Level.FINE)
               || level.equals(Level.FINER)
               || level.equals(Level.FINEST)) {
      String shortPackage = record.getLoggerName();
      int index = shortPackage.lastIndexOf(".");
      if (index>0) shortPackage = shortPackage.substring(index+1);
      message =
        StringUtil.prependToLines
        (level+" "+shortPackage+": ", message);
    } else {
      message =
        StringUtil.prependToLines
        (level+ " " + s_time_formatter.format(new Date())
         + " "+ record.getLoggerName()+": ",
         message);
    }
    lastLevel = level;
    return message;
  }
  private static DateFormat s_time_formatter
    = new SimpleDateFormat("HH:mm:ss.SSS");

  /** Run tests.
      @param argv command line
  */
  public static void main(String[] argv) {
    CleanHandler.setDefaultHandler();
    Logger logger = Logger.getLogger("edu.mines.jtk.opt.CleanFormatter");
    CleanFormatter cf = new CleanFormatter();
    String[] messages = new String[] {"one", "two", "three"};
    Level[] levels = new Level[] {Level.INFO, Level.WARNING, Level.SEVERE};
    String[] s = new String[3];
    for (int i=0; i<messages.length; ++i) {
      LogRecord lr = new LogRecord(levels[i], messages[i]);
      lr.setSourceClassName("Class");
      lr.setSourceMethodName("method");
      s[i] = cf.format(lr);
      assert s[i].endsWith(messages[i]+"\n");
      logger.info("|"+s[i]+"|");
    }
    assert s[0].equals("one\n"): s[0];
    assert s[1].equals("WARNING: two\n") : s[1];
    assert s[2].matches("^\\*\\*\\*\\* SEVERE WARNING \\*\\*\\*\\* "+
                        "\\(Class.method \\d+-\\d+ #.*\\)\n"+
                        "SEVERE: three\n$") :s[2];
  }
}

