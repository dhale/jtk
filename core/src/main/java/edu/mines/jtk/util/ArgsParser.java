/****************************************************************************
Copyright 2006, Colorado School of Mines and others.
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

import java.util.ArrayList;

/**
 * Parses command-line arguments for options and values, using conventions 
 * like those used by the UNIX function getopt; e.g., "-a". Also supports 
 * GNU-style long options; e.g., "--alpha".
 * <p>
 * Short options in command-line arguments have the form "-a". A short 
 * options may have a value, as in "-a3.14" or "-a 3.14". Multiple short 
 * options without values may be specified together, so that "-ab" is
 * equivalent to "-a -b". (Note that if a value is expected for option 
 * "-a", then "-ab" specifies "b" as that value.)
 * <p>
 * Long options in command-line arguments have the form "--alpha". A long
 * option may also have a value, as in "--alpha=3.14" or "--alpha 3.14". 
 * Long option names may be abbreviated, provided that the abbreviation is 
 * unique among all long options. For example, "--a=3.14" is equivalent to 
 * "--alpha=3.14", provided that no other long option begins with "--a".
 * <p>
 * For both short and long options, it is an error to specify a value where
 * one is not expected, or to omit a value where one is expected.
 * <p>
 * Option parsing ends with the first argument that is not an option, i.e.,
 * one that does not begin with a hyphen "-". As a special case, parsing
 * also ends after the special option "--".
 * <p>
 * Typical usage within the standard method main is:
 * <pre><code>
 * public static void main(String[] args) {
 *   float a = 3.14f;
 *   boolean b = false;
 *   String fileName = null;
 *   try {
 *     String shortOpts = "ha:b";
 *     String[] longOpts = {"help","alpha=","beta"};
 *     ArgsParser ap = new ArgsParser(args,shortOpts,longOpts);
 *     String[] opts = ap.getOptions();
 *     String[] vals = ap.getValues();
 *     for (int i=0; i&lt;opts.length; ++i) {
 *       String opt = opts[i];
 *       String val = vals[i];
 *       if (opt.equals("-h") || opt.equals("--help")) {
 *         printUsageAndExit(0);
 *       } else if (opt.equals("-a") || opt.equals("--alpha")) {
 *         a = ap.toFloat(val);
 *       } else if (opt.equals("-b") || opt.equals("--beta")) {
 *         b = true;
 *       }
 *     }
 *     args = ap.getOtherArgs();
 *     if (args.length!=1)
 *       printUsageAndExit(-1);
 *     fileName = args[0];
 *   } catch (OptionException oe) {
 *     System.err.println(oe.getMessage());
 *     printUsageAndExit(-1);
 *   }
 *   // ...
 * }
 * </code></pre>
 *
 * @author Dave Hale, Colorado School of Mines
 * @version 2001.02.04, 2006.07.12
 */
public class ArgsParser {

  /**
   * Option exceptions are thrown when options specified in arguments
   * are inconsistent with the parser's specifications.
   */
  public static class OptionException extends Exception {
    private OptionException(String msg) {
      super(msg);
    }
  }

  /** 
   * Constructs an argument parser for the specified arguments and
   * short options specification.
   * @param args the command-line arguments, as passed to the method main.
   * @param shortOpts the short options specification. Each option is
   *  specified by a single character. For options that require values,
   *  this character must be followed by a single colon ':'. For example, 
   *  "a:b" specifies two options "-a" and "-b", and the first option "-a"
   *  requires a value.
   */
  public ArgsParser(String[] args, String shortOpts) throws OptionException {
    _args = args;
    _shortOpts = shortOpts;
    _longOpts = new String[0];
    init();
  }

  /** 
   * Constructs an argument parser for the specified arguments and
   * short and long options specifications.
   * @param args the command-line arguments, as passed to the method main.
   * @param shortOpts the short options specification. Each option is
   *  specified by a single character. For options that require values,
   *  this character must be followed by a single colon ':'. For example, 
   *  "a:b" specifies two options "-a" and "-b", and the first option "-a"
   *  requires a value.
   * @param longOpts the long options specification. Each option is
   *  is specified by a single string, containing the long option name.
   *  For options that require values, this string must end in the 
   *  character "=". For example, the string "--alpha=" specifies a
   *  long option "--alpha" that requires a value.
   */
  public ArgsParser(String[] args, String shortOpts, String[] longOpts) 
    throws OptionException
  {
    _args = args;
    _shortOpts = shortOpts;
    _longOpts = longOpts;
    init();
  }

  /**
   * Gets the options parsed.
   * @return the options parsed.
   *  Each string is of the form "-a", for short options, or "--alpha", for
   *  long options. Note that the hyphen "-" or double-hyphen "--" is
   *  included in the strings. For long options, the complete names are 
   *  returned, even if abbreviations were parsed.
   */
  public String[] getOptions() {
    return getStrings(_optList);
  }

  /**
   * Gets the values corresponding to the options parsed.
   * @return the values parsed.
   *  The value is null for any option that does not expect a value.
   */
  public String[] getValues() {
    return getStrings(_valList);
  }

  /**
   * Gets the other arguments, those that do not correspond to options.
   * @return the other arguments.
   */
  public String[] getOtherArgs() {
    return getStrings(_argList);
  }

  /**
   * Converts a string value to a boolean.
   * @param s the string value.
   * @return the boolean.
   * @exception OptionException if the string is not a valid boolean.
   */
  public static boolean toBoolean(String s) throws OptionException {
    s = s.toLowerCase();
    if (s.equals("true"))
      return true;
    if (s.equals("false"))
      return false;
    throw new OptionException("the value "+s+" is not a valid boolean");
  }

  /**
   * Converts a string value to a double.
   * @param s the string value.
   * @return the double.
   * @exception OptionException if the string is not a valid double.
   */
  public static double toDouble(String s) throws OptionException {
    try {
      return Double.valueOf(s);
    } catch (NumberFormatException e) {
      throw new OptionException("the value "+s+" is not a valid double");
    }
  }

  /**
   * Converts a string value to a float.
   * @param s the string value.
   * @return the float.
   * @exception OptionException if the string is not a valid float.
   */
  public static float toFloat(String s) throws OptionException {
    try {
      return Float.valueOf(s);
    } catch (NumberFormatException e) {
      throw new OptionException("the value "+s+" is not a valid float");
    }
  }

  /**
   * Converts a string value to an int.
   * @param s the string value.
   * @return the int.
   * @exception OptionException if the string is not a valid int.
   */
  public static int toInt(String s) throws OptionException {
    try {
      return Integer.parseInt(s);
    } catch (NumberFormatException e) {
      throw new OptionException("the value "+s+" is not a valid int");
    }
  }

  /**
   * Converts a string value to a long.
   * @param s the string value.
   * @return the long.
   * @exception OptionException if the string is not a valid long.
   */
  public static long toLong(String s) throws OptionException {
    try {
      return Long.parseLong(s);
    } catch (NumberFormatException e) {
      throw new OptionException("the value "+s+" is not a valid long");
    }
  }


  ///////////////////////////////////////////////////////////////////////////
  // private

  private String[] _args;
  private String _shortOpts;
  private String[] _longOpts;

  private String _longOpt;

  private ArrayList<String> _optList;
  private ArrayList<String> _valList;
  private ArrayList<String> _argList;

  private void init() throws OptionException {
    int n = _args.length;
    _optList = new ArrayList<String>(n);
    _valList = new ArrayList<String>(n);
    _argList = new ArrayList<String>(n);
    for (int i=0; i<n; ++i)
      _argList.add(_args[i]);
    while (!_argList.isEmpty()) {
      String arg = _argList.get(0);
      if (arg.charAt(0)!='-' || arg.equals("-"))
        break;
      _argList.remove(0);
      if (arg.equals("--"))
        break;
      if (arg.charAt(1)=='-') {
        doLongOption(arg);
      } else {
        doShortOption(arg);
      }
    }
  }

  private void doShortOption(String arg) throws OptionException {
    String argString = arg.substring(1);
    while (!argString.equals("")) {
      char opt = argString.charAt(0);
      argString = argString.substring(1);
      String val = "";
      if (shortOptionHasValue(opt)) {
        if (argString.equals("")) {
          if (_argList.isEmpty())
            throw new OptionException("option -"+opt+" requires a value");
          argString = _argList.remove(0);
        }
        val = argString;
        argString = "";
      }
      _optList.add("-"+opt);
      _valList.add(val);
    }
  }

  private boolean shortOptionHasValue(char opt) throws OptionException {
    for (int i=0; i<_shortOpts.length(); ++i) {
      if (opt==_shortOpts.charAt(i) && opt!=':')
        return i+1<_shortOpts.length() && _shortOpts.charAt(i+1)==':';
    }
    throw new OptionException("option -"+opt+" not recognized");
  }

  private void doLongOption(String arg) throws OptionException {
    String argString = arg.substring(2);
    String opt = argString;
    String val = null;
    int i = argString.indexOf('=');
    if (i>=0) {
      opt = argString.substring(0,i);
      val = (i+1<argString.length())?argString.substring(i+1):"";
    }
    boolean hasValue = longOptionHasValue(opt);
    opt = _longOpt;
    if (hasValue) {
      if (val==null) {
        if (_argList.isEmpty())
          throw new OptionException("option --"+opt+" requires a value");
        val = _argList.remove(0);
      }
    } else if (val!=null) {
      throw new OptionException("option --"+opt+" must not have a value");
    }
    _optList.add("--"+opt);
    _valList.add(val);
  }

  private boolean longOptionHasValue(String opt) throws OptionException {
    int lenOpt = opt.length();
    for (int i=0; i<_longOpts.length; ++i) {
      String longOpt = _longOpts[i];
      int lenLongOpt = longOpt.length();
      if (lenOpt>lenLongOpt)
        continue;
      String x = longOpt.substring(0,lenOpt);
      if (!x.equals(opt))
        continue;
      String y = longOpt.substring(lenOpt,longOpt.length());
      if (!y.equals("") && !y.equals("=") && i+1<_longOpts.length)
        if (opt.equals(_longOpts[i+1].substring(0,lenOpt)))
          throw new OptionException("option --"+opt+" not a unique prefix");
      int last = longOpt.length()-1;
      if (longOpt.charAt(last)=='=') {
        _longOpt = longOpt.substring(0,last);
        return true;
      }
      _longOpt = longOpt;
      return false;
    }
    throw new OptionException("option --"+opt+" not recognized");
  }

  private static String[] getStrings(ArrayList<String> list) {
    int n = list.size();
    String[] a = new String[n];
    for (int i=0; i<n; ++i)
      a[i] = list.get(i);
    return a;
  }
}
