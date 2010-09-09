/****************************************************************************
Copyright (c) 2003, Landmark Graphics and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.util;

import java.util.*;
import java.util.logging.Logger;
import java.util.regex.*;

/** Filter a string with specified ResourceBundles.
    @author W.S. Harlan, Landmark Graphics
*/
public class Localize {
  private static final Logger LOG = Logger.getLogger(Localize.class.getName());
  private static final Pattern s_tokens = Pattern.compile("[$][{](.+?)[}]");

  /** Filter the specified string with the specified resource bundle.
      First the entire string is filtered, then substrings
      delimited as ${key} are filtered.
      @param message Filter this string
      @param catalog Get keys and values from this.
      @return Filtered string.
   */
  public static String filter(String message, ResourceBundle catalog) {
    if (catalog == null)
      return message;

    // see if entire string is a key
    try {
      message = catalog.getString(message);
    } catch (MissingResourceException ex) {} // do not substitute

    // see if substrings are keys
    Matcher matcher = s_tokens.matcher(message);
    int numberMatches = 0;
    while (matcher.find()) {++numberMatches;}

    // substitute from end backwards
    for (int match=numberMatches; match>0; --match) {
      matcher.reset();
      for (int i=0; i<match; ++i) {
        matcher.find();
      }
      MatchResult mr = matcher.toMatchResult();
      try {
        String key = mr.group(1);
        String replacement = catalog.getString(key);
        int start = mr.start();
        int end = mr.end();
        message = message.substring(0,start) + replacement
          + message.substring(end);
      } catch (MissingResourceException ex) {} // do not substitute
    }
    return message;
  }

  /** Filter the specified string with a ResourceBundle
      for the specified class.
      First the entire string is filtered, then substrings
      delimited as ${key} are filtered.
      @param message Filter this string
      @param resourceClass Use the class loader for this class
      and load a localized PropertyResourceBundle with the same
      name as the class.  For example, a class Foo has
      a Foo.properties and a Foo_ch.properties file in the same
      directory as the compiled Foo.class.
      Test with java -Duser.language=ch
      @return Filtered string.
   */
  public static String filter(String message, Class<?> resourceClass) {
    ClassLoader cl = resourceClass.getClassLoader();
    if (cl == null) {
      LOG.warning("Could not get ClassLoader from "+resourceClass.getName());
      cl = ClassLoader.getSystemClassLoader();
    }
    Locale currentLocale = Locale.getDefault();
    String name = resourceClass.getName();
    ResourceBundle catalog =
      ResourceBundle.getBundle(name, currentLocale, cl);
    if (catalog == null) {
      LOG.warning("Could not get ResourceBundle "+name+" for "+
                  currentLocale+" from "+cl);
    }
    return filter(message, catalog);
  }

  /** Convert a number of seconds into words
      @param seconds Number of seconds
      @return Localized words describing the number of seconds.
  */
  public static String timeWords(long seconds) {
    if (seconds == 0) {
      return filter("0 ${seconds}", Localize.class);
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

    return filter(result.trim(), Localize.class);
  }

}
