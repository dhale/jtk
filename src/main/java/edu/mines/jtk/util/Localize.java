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

/**
 * Localize messages for end users, using a Formatter pattern and a localized resource bundle.
 * <p/>
 * 
 * <pre>
 * In a class like MyPanel.java you might have lines like this:
 * 
 * double value = 3.14;
 * String msg = "The value "+value+" is too large";
 * 
 * Instead, in the same package as MyPanel.java, create a file
 * MyPanel.properties, with this line:
 * 
 * too_large = The value %g is too large.
 * 
 * Then you can replace the original lines by this.
 * 
 * private static Localize local = new Localize(MyPanel.class);
 * ...
 * 
 * double value = 3.14;
 * String msg = local.format("too_large", value);
 * 
 * If you create an additional file called MyPanel_es.properties, then
 * that file will be used automatically in Spanish-speaking locales.
 * 
 * @author W.S. Harlan, Landmark Graphics
 */
public class Localize {
    private static final Logger LOG = Logger.getLogger(Localize.class.getName());
    private static final Pattern s_tokens = Pattern.compile("[$][{](.+?)[}]");

    private static final Map<String, ResourceBundle> s_rb = new HashMap<String, ResourceBundle>();

    private final ResourceBundle resourceBundle;
    private final Locale locale;
    private final Class<?> clazz;
    private final String resourceBundleName;

    /**
     * Construct for localized messages.
     * 
     * @param clazz Name of client class that requires localization Class<Foo> is assumed to have property files
     *        Foo.properties, Foo_es.properties, etc. Uses default Locale.
     */
    public Localize(final Class<?> clazz) {
        this(clazz, null);
    }

    /**
     * Construct for localized messages.
     * 
     * @param clazz Name of client class that requires localization
     * @param resourceBundleName Name of ResourceBundle to be used for localizing messages. If null, then Class<Foo> is
     *        assumed to have property files Foo.properties, Foo_es.properties, etc. Uses default Locale.
     */
    public Localize(final Class<?> clazz, final String resourceBundleName) {
        this(clazz, resourceBundleName, null);
    }

    /**
     * Construct for localized messages.
     * 
     * @param clazz Name of client class that requires localization
     * @param resourceBundleName Name of ResourceBundle to be used for localizing messages. If null, then Class<Foo> is
     *        assumed to have property files Foo.properties, Foo_es.properties, etc.
     * @param locale Locale to use for localization. If null, then will use default Locale.
     */
    public Localize(final Class<?> clazz, final String resourceBundleName, final Locale locale) {
        this.clazz = clazz;
        this.resourceBundleName = resourceBundleName;
        resourceBundle = getResourceBundle(clazz, resourceBundleName, locale);
        this.locale = locale;
    }

    /**
     * Format a localized message, with java.util.Formatter and the appropriate resource.
     * 
     * @param key used to specify format string in properties file. If not found, then will be used as the format.
     * @param args Optional arguments to be passed to Formatter.format method.
     * @return formatted localized String
     */
    public String format(final String key, final Object... args) {
        if (key == null) {
            return null;
        }
        String format;
        try {
            if (resourceBundle != null) {
                format = resourceBundle.getString(key);
            } else {
                format = key;
            }
        } catch (final MissingResourceException mre) {
            format = key;
        }
        final Formatter formatter = new Formatter(locale);
        return formatter.format(format, args).out().toString().trim();
    }

    /**
     * Get a resource bundle associated with a class.
     * 
     * @param clazz Name of client class that requires resource bundle.
     * @param resourceBundleName Name of ResourceBundle to be used for localizing messages. If null, then Class<Foo> is
     *        assumed to have property files Foo.properties, Foo_es.properties, etc.
     * @param locale Locale to use for localization. If null, then will use default Locale.
     * @return Get ResourceBundle for this locale.
     */
    public static ResourceBundle getResourceBundle(final Class<?> clazz, String resourceBundleName, Locale locale) {

        if (locale == null) {
            locale = Locale.getDefault();
        }
        final String key = clazz.getName() + ";" + resourceBundleName + ";" + locale.toString();
        synchronized (s_rb) {

            ResourceBundle resourceBundle = s_rb.get(key);
            if (resourceBundle == null) {
                if (resourceBundleName == null) {
                    String cn = clazz.getName();
                    if (cn == null) {
                        final Class<?> c2 = clazz.getEnclosingClass();
                        if (c2 != null) {
                            cn = c2.getName();
                        }
                    }
                    if (cn == null) {
                        throw new IllegalArgumentException("Specify top-level class.  "
                                + "This class does not have a canonical name: " + clazz.getName());
                    }
                    resourceBundleName = cn;
                } else {
                    resourceBundleName = clazz.getPackage().getName() + "." + resourceBundleName;
                }
                try { // see if throws MissingResourceException
                    resourceBundle = ResourceBundle.getBundle(resourceBundleName, locale, clazz.getClassLoader());
                    s_rb.put(key, resourceBundle);
                } catch (final MissingResourceException e) {
                    // No resource file provided. Plan to return null.
                }
            }
            return s_rb.get(key);
        }
    }

    /**
     * Get the best localized message from a Throwable that may contain other Throwables as a cause.
     * 
     * @param throwable a Throwable that may contain other Throwables as a cause.
     * @return best localized message, unwrapping as necessary.
     */
    public static String getMessage(final Throwable throwable) {
        if (throwable.getCause() == null) {
            final String localizedMessage = throwable.getLocalizedMessage();
            if (localizedMessage != null) {
                return localizedMessage;
            }
            final String message = throwable.getMessage();
            if (message != null) {
                return message;
            }
            return throwable.toString();
        }
        final String causeToString = throwable.getCause().toString();
        final String localized = throwable.getLocalizedMessage();
        if (localized == null || localized.equals(causeToString)) {
            return getMessage(throwable.getCause());
        }
        return localized;
    }

    @Override
    public String toString() {
        return "Localize{" + ", locale=" + locale + ", clazz=" + clazz + ", resourceBundleName='" + resourceBundleName
                + '\'' + '}';
    }

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
        } catch (MissingResourceException ex) {/*no substitution*/}

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
            } catch (MissingResourceException ex) {/* do not substitute */}
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
      @deprecated Prefer other methods for standard localization.
     */
    @Deprecated
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
