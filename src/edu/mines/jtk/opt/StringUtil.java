package edu.mines.jtk.opt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.CharacterIterator;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.text.StringCharacterIterator;
import java.util.Date;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Logger;

/**
 * Utilities for manipulating strings.
 */
public class StringUtil {
  private static final Logger LOG
    = Logger.getLogger(StringUtil.class.getName() ,StringUtil.class.getName());

  /**
   * Line separator
   */
  public static final String NL = System.getProperty("line.separator");

  /** The format used by getTimeStamp() methods,
      like 20050621-153318 */
  public static final DateFormat TIMESTAMP_FORMAT =
    new SimpleDateFormat("yyyyMMdd-HHmmss");

  /** The format used by getLongTimeStamp() methods,
      like 20050621-153318-255 */
  public static final DateFormat TIMESTAMP_LONG_FORMAT =
    new SimpleDateFormat("yyyyMMdd-HHmmss-SSS");

  /** Lock the previous Date. */
  private static final Object s_uniqueDateLock = new Object();

  /** Most recent unique Date. */
  private static long s_uniqueTime = 0;

  /** Join together Strings into a single String
      @param strings The Strings to join together.
      Gets the String from Object.toString().
      @param join Place this string between the joined strings,
      but not at the beginning or end.
      @return The joined String
  */
  public static String join(Object[] strings, String join) {
    if (strings == null) return null;
    StringBuilder result = new StringBuilder();
    for (int i=0; i<strings.length; ++i) {
      result.append(strings[i].toString());
      if (i<strings.length-1) result.append(join);
    }
    return result.toString();
  }

  /** Join together Strings into a single String.
      Split again with String.split(String regex).
      @param strings The Strings to join together.
      Gets the String from Object.toString().
      @param join Place this string between the joined strings,
      but not at the beginning or end.
      @return The joined String
  */
  public static String join(List strings, String join) {
    return join(strings.toArray(), join);
  }

  /** Return the stack trace of an Throwable as a String
      @param throwable The Throwable containing the stack trace.
      @return The stack trace that would go to stderr with
      throwable.printStackTrace().
  */
  public static String getStackTrace(Throwable throwable) {
    StackTraceElement[] stackTraceElements = throwable.getStackTrace();
    StringBuilder result = new StringBuilder();
    for (StackTraceElement se: stackTraceElements) {
      result.append("  ");
      result.append(se.toString());
      result.append("\n");
    }
    return result.toString();
  }

  /** Prepend a string to every line of text in a String
      @param prepend String to be prepended
      @param lines Lines separated by newline character, from
      System.getProperty("line.separator");
      @return Modified lines
  */
  public static String prependToLines(String prepend, String lines) {
    if (lines == null) return null;
    if (prepend == null) return lines;
    StringBuilder result = new StringBuilder();
    boolean hasFinalNL = lines.endsWith(NL);
    StringTokenizer divided = new StringTokenizer(lines, NL);
    while (divided.hasMoreTokens()) {
      result.append(prepend + divided.nextToken());
      if (divided.hasMoreTokens() || hasFinalNL) result.append(NL);
    }
    return result.toString();
  }

  /** Return a concise string that can be added as a timestamp to
      filenames
      @return String in format TIMESTAMP_FORMAT.
  */
  public static String getTimeStamp() {
    return getTimeStamp(new Date());
  }

  /** Return a concise string that can be added as a timestamp to
      filenames
      @param date Time in milliseconds since 1970
      @return String in format TIMESTAMP_FORMAT.
  */
  public static String getTimeStamp(long date) {
    return getTimeStamp(new Date(date));
  }

  /** Return a concise string that can be added as a timestamp to
      filenames
      @param date Date for timestamp
      @return String in format TIMESTAMP_FORMAT.
  */
  public static String getTimeStamp(Date date) {
    synchronized (TIMESTAMP_FORMAT) { // format is not thread-safe
      return TIMESTAMP_FORMAT.format(date);
    }
  }

  /** Convert a time stamp from getTimeStamp into the equivalent Date object.
      @param timeStamp A date created in the concise format TIMESTAMP_FORMAT
      returned by getTimeStamp().
      @return The Date equivalent to the time stamp.
      @throws ParseException When the string is in the wrong format.
   */
  public static Date parseTimeStamp(String timeStamp) throws ParseException {
    synchronized (TIMESTAMP_FORMAT) { // format is not thread-safe
      return TIMESTAMP_FORMAT.parse(timeStamp);
    }
  }

  /** Return a concise string that can be added as a timestamp to
      filenames.  Includes milliseconds.
      Guaranteed to return a unique value each time it is called.
      @return String in format TIMESTAMP_LONG_FORMAT.
  */
  public static String getLongTimeStamp() {
      return getLongTimeStamp(getUniqueDate());
  }

  /** Return a concise string that can be added as a timestamp to
      filenames.  Includes milliseconds.
      @param date Time in milliseconds since 1970
      @return String in format TIMESTAMP_FORMAT.
  */
  public static String getLongTimeStamp(long date) {
    return getLongTimeStamp(new Date(date));
  }

  /** Return a concise string that can be added as a timestamp to
      filenames.  Includes milliseconds.
      @param date Create a timestamp for this date.
      @return String in format TIMESTAMP_LONG_FORMAT.
  */
  public static String getLongTimeStamp(Date date) {
    synchronized (TIMESTAMP_LONG_FORMAT) { // format is not thread-safe
      return TIMESTAMP_LONG_FORMAT.format(date);
    }
  }

  /** Convert a time stamp from getTimeStamp into the equivalent Date object.
      Time stamp includes milliseconds.
      @param timeStamp A date created in the concise format TIMESTAMP_FORMAT
      returned by getTimeStamp().
      @return The Date equivalent to the time stamp.
      @throws ParseException When the string is in the wrong format.
   */
  public static Date parseLongTimeStamp(String timeStamp)
    throws ParseException {
    synchronized (TIMESTAMP_LONG_FORMAT) {
      try {
        return TIMESTAMP_LONG_FORMAT.parse(timeStamp);
      } catch (RuntimeException e) {
        LOG.severe("Could not parse "+timeStamp);
        throw e;
      }
    }
  }

  /** Read build time stamp as a Date
      from resource file created at time of build.
      @return Build time stamp.
 * @throws IOException file system error
  */
  public static Date getBuildDate() throws IOException {
    synchronized (TIMESTAMP_FORMAT) {
      try {
        return TIMESTAMP_FORMAT.parse(getBuildTimeStamp());
      } catch (ParseException e) {
        IOException ioe = new IOException(e.getMessage());
        ioe.initCause(e);
        throw ioe;
      }
    }
  }

  /** Read build time stamp from resource file created at time of build.
      @return Build time stamp or null if not available.
      @throws IOException if can't get timestamp file.
  */
  public static String getBuildTimeStamp() throws IOException {
    try {
      BufferedReader br = new BufferedReader(new InputStreamReader(
        (StringUtil.class.getResourceAsStream("BUILD_VERSION"))));
      String result = br.readLine();
      br.close();
      return result;
    } catch (Exception e) {
      throw new IOException("This build is incomplete.");
    }
  }

  /**
   * Replaces all occurences in string s of string x with string y.
   * Stole from com.lgc.gpr.util.XmlUtil.replaceAll.
   *
   * @param x string to be replaced.
   * @param y string replacing each occurence of x.
   * @param s string containing zero or more x to be replaced.
   * @return string s with all x replaced with y.
   */
  public static String replaceAll(String x, String y, String s) {
    if (s==null) return null;
    int from = 0;
    int to = s.indexOf(x,from);
    if (to<0) return s;
    StringBuilder d = new StringBuilder(s.length()+32);
    while (to>=0) {
      d.append(s.substring(from,to));
      d.append(y);
      from = to+x.length();
      to = s.indexOf(x,from);
    }
    return d.append(s.substring(from)).toString();
  }

  /** Break lines at convenient locations and indent
      if lines are too long.  Try to break at word boundaries,
      but resort to breaking in the middle of a word with a backslash
      if necessary.
      @param s String that needs to be broken.
      @param maxchars The maximum number of characters to use per line.
      @return String with newlines and indentations included as necessary.
      The result will always have a final newline
   */
  public static String breakLines(String s, int maxchars) {
    StringBuilder sb = new StringBuilder();
    StringTokenizer st = new StringTokenizer(s,NL);
    while (st.hasMoreTokens()) {
      sb.append(breakLine(st.nextToken(),maxchars));
    }
    return sb.toString();
  }

  /**
   * Remove the whitespace from the string
 * @param str clean this string
   * @return the string without any whitespace; null, if the string is null
   */
  public static String removeWhiteSpaces(String str) {
    if (str == null) return null;

    StringBuilder sb = new StringBuilder();
    CharacterIterator iter = new StringCharacterIterator(str);
    for (char c = iter.first(); c != CharacterIterator.DONE; c = iter.next()) {
      if (!Character.isWhitespace(c))
        sb.append(c);
    }

    return sb.toString();
  }

  /** Convert a number of seconds into words
      @param seconds Number of seconds
      @return Localized words describing the number of seconds.
  */
  public static String timeWords(long seconds) {
    if (seconds == 0) {
      return Localize.filter("0 ${seconds}", StringUtil.class);
    }
    String result = "";
    long minutes = seconds/60;
    long hours = minutes/60;
    long days = hours/24;
    seconds %= 60;
    minutes %= 60;
    hours %= 24;
    if (days >= 10) {
      if (hours >=12) ++days;
      hours = minutes = seconds = 0;
    } else if (hours >= 10 || days > 0) {
      if (minutes >=30) {
        ++hours;
        days += hours/24;
        hours %= 24;
      }
      minutes = seconds = 0;
    } else if (minutes >= 10 || hours > 0) {
      if (seconds >=30) {
        ++minutes;
        hours += minutes/60;
        minutes %= 60;
      }
      seconds = 0;
    }
    if (seconds != 0)
      result = " " + seconds + " ${second"+ ((seconds>1)?"s}":"}") + result;
    if (minutes != 0)
      result = " " + minutes + " ${minute"+ ((minutes>1)?"s}":"}") + result;
    if (hours != 0)
      result = " " + hours + " ${hour" + ((hours>1)?"s}":"}") + result;
    if (days != 0)
      result = " " + days + " ${day" + ((days>1)?"s}":"}") + result;

    return Localize.filter(result.trim(), StringUtil.class);
  }

  /** Make a string representing the array,
      for debugging and error messages
      @param v Array to print.
      @return String representing the vector,
  */
  public static String toString(int[] v) {
    StringBuilder sb = new StringBuilder("(");
    for (int i=0; i<v.length; ++i) {
      sb.append(Integer.toString(v[i]));
      if (i<v.length-1) {sb.append(",");}
    }
    sb.append(")");
    return sb.toString();
  }

  /** Make a string representing the array,
      for debugging and error messages
      @param v Array to print.
      @return String representing the vector,
  */
  public static String toString(float[] v) {
    StringBuilder sb = new StringBuilder("(");
    for (int i=0; i<v.length; ++i) {
      sb.append(Float.toString(v[i]));
      if (i<v.length-1) {sb.append(",");}
    }
    sb.append(")");
    return sb.toString();
  }

  /** Make a string representing the array,
      for debugging and error messages
      @param v Array to print.
      @return String representing the vector,
  */
  public static String toString(int[][] v) {
    StringBuilder sb = new StringBuilder();
    for (int[] col: v) {
      sb.append(toString(col));
    }
    return sb.toString();
  }

  /** Make a string representing the array,
      for debugging and error messages
      @param v Array to print.
      @return String representing the vector,
  */
  public static String toString(float[][] v) {
    StringBuilder sb = new StringBuilder();
    for (float[] col: v) {
      sb.append(toString(col));
    }
    return sb.toString();
  }

  /** Make a string representing the array,
      for debugging and error messages
      @param v Array to print.
      @return String representing the vector,
  */
  public static String toString(double[] v) {
    StringBuilder sb = new StringBuilder("(");
    for (int i=0; i<v.length; ++i) {
      sb.append(Double.toString(v[i]));
      if (i<v.length-1) {sb.append(",");}
    }
    sb.append(")");
    return sb.toString();
  }

  /** Convert a double to a string appropriate for writing
      to a file.  Larger numbers are treated as integers.
      This name is short and cryptic because it appears so often
      on a single line.  (Stands for Number String.)
      @param d Convert this double to a String.
      @return String suitable for a human to read, with acceptable precision.
  */
  public static String ns(double d) {
    if ( d == (int) d ) {
      return Integer.toString((int)d);
    } else if (d > 10000 || d < -10000) {
      return Integer.toString((int) Math.floor(d+0.5));
    } else if (d > -1.e-15 && d < 1.e-15) {
      return "0";
    } else {
      return Float.toString((float)d);
    }
  }

  /** Get a date for a timestamp, and avoid any previous
      dates by adding a millisecond if necessary.
      @return unique Date
   */
  public static Date getUniqueDate() {
    synchronized (s_uniqueDateLock) {
      long time = System.currentTimeMillis();
      if (time <= s_uniqueTime) {
        time = s_uniqueTime+1;
      }
      s_uniqueTime = time;
      return new Date(s_uniqueTime);
    }
  }

  ///////////////////////   PRIVATE   //////////////////////////

  /** Break a line at convenient locations and indent
      if lines are too long.  Try to break at word boundaries,
      but resort to breaking in the middle of a word with a backslash
      if necessary.
      @param s String that needs to be broken.  Do not pass a string
      that already contains newlines.
      @param maxchars The maximum number of characters to use per line.
      @return String with newlines and indentations included as necessary.
      The result will always have a final newline
   */
  private static String breakLine(String s, int maxchars) {
    StringBuilder sb = new StringBuilder();
    printLine(s, maxchars, sb);
    return sb.toString();
  }

  private static void printLine(String s, int maxline, StringBuilder sb) {
    int maxbreak = maxline/3, maxindent = 2*maxline/3;
    if (s.length() < maxline) {sb.append(s+NL); return;}
    int n = maxline-1;
    try {
      while (n>=maxbreak && s.charAt(n) !=' ') {--n;}
    } catch (java.lang.StringIndexOutOfBoundsException e) {
      e.printStackTrace();
      throw new IllegalStateException
        ("s=|"+s+"|"+NL+"s.length()="+s.length()+NL+
         "n="+n+NL+"maxline="+maxline+NL+"maxbreak="+maxbreak+NL+
         e.getMessage());
    }
    String extra = "";
    String indent ="";
    // See if we stopped after all spaces
    boolean allAreSpaces = true;
    for (int i=0; allAreSpaces && i<n; ++i) {
      allAreSpaces = allAreSpaces && s.charAt(i) == ' ';}
    if (allAreSpaces ||
       s.charAt(n) !=' ') {// If must break a word, do so at the end
      n = maxline-2; extra = "\\";} // break at the end of the line
    else {indent = indentString(s, maxindent);} // break at word with same indentation
    printLine(s.substring(0,n)+extra, maxline, sb); // print before break
    printLine(indent + s.substring(n), maxline, sb); // print after break
  }

  private static String indentString(String s, int maxindent) {
    int n=0; while (n<maxindent && n<s.length() && s.charAt(n)==' ') {++n;}
    if (n<1) n = 1;
    return s.substring(0,n-1);
  }
}
