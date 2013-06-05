/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.util;

import java.util.NoSuchElementException;

/**
 * Parse a string containing zero or more string literals.
 * @author Dave Hale, Colorado School of Mines
 * @version 06/20/1998, 08/24/2006.
 */
public class StringParser {

  /**
   * Construct a string parser for the specified string.
   * @param s string to parse.
   */
  public StringParser(String s) {
    _str = s;
    _len = s.length();
    _pos = 0;
    _end = -1;
  }

  /**
   * Determine whether the parser has more strings.
   * @return true, if more strings; false, otherwise.
   */
  public boolean hasMoreStrings() {
    final char space = ' ';
    final char quote = '\"';
    final char backslash = '\\';

    // If already positioned on a string that has not yet been got.
    if (_pos<=_end) return true;

    // Nothing left if current position beyond string length.
    if (_pos>_len || _str==null) return false;

    // Skip whitespace and remember next non-whitespace character.
    char c = 0;
    while (_pos<_len) {
      c = _str.charAt(_pos);
      if (c>space) break;
      ++_pos;
    }

    // Nothing left if current position beyond string length.
    if (_pos>=_len) return false;

    // If non-whitespace character is a quote, then string is
    // everything (including whitespace) up to matching quote
    // or end of string, whichever comes first.
    // Otherwise, if not quoted, string is everything up to next 
    // whitespace or end of string, whichever comes first.
    // When inside quotes, handle escaped quotes.
    if (c==quote) {
      ++_pos;
      boolean in_escape = false;
      for (_end=_pos; _end<_len; ++_end) {
        c = _str.charAt(_end);
        if (in_escape) {
          in_escape = false;
        } else if (c==backslash) {
          in_escape = true;
        } else if (c==quote) {
          break;
        }
      }
    } else {
      for (_end=_pos+1; _end<_len; ++_end) {
        c = _str.charAt(_end);
        if (c<=space) break;
      }
    }
    return true;
  }

  /**
   * Get the next string.
   * @return next string.
   * @exception NoSuchElementException if parser has no more strings.
   */
  public String nextString() {

    // Ensure we have have another string to get.
    if (!hasMoreStrings()) {
      throw new NoSuchElementException(
        "StringParser.nextString: no more strings in "+_str+".");
    }

    // Update the current position so that it will be correct after
    // we return the current string.
    int pos = _pos;
    _pos = _end+1;
    return replaceEscapes(_str.substring(pos,_end));
  }

  ///////////////////////////////////////////////////////////////////////////
  // Private.

  private String _str = null; // The string to parse for strings.
  private int _pos = 0; // Current position (< _end if have string at _pos).
  private int _end = 0; // One position beyond end of current string.
  private int _len = 0; // Length of string.

  // Note this does not handle all possible escapes.
  private String replaceEscapes(String s) {
    final char backslash = '\\';
    if (s==null || s.indexOf(backslash)<0) return s;
    StringBuilder sb = new StringBuilder(s.length());
    boolean in_escape = false;
    for (int i=0; i<s.length(); ++i) {
      char c = s.charAt(i);
      if (in_escape) {
        switch(c) {
        case '\\':
          sb.append('\\');
          break;
        case 't':
          sb.append('\t');
          break;
        case 'n':
          sb.append('\n');
          break;
        case 'r':
          sb.append('\r');
          break;
        case 'f':
          sb.append('\f');
          break;
        case 'b':
          sb.append('\b');
          break;
        default:
          sb.append(c);
        }
        in_escape = false;
      } else if (c==backslash) {
        in_escape = true;
      } else {
        sb.append(c);
      }
    }
    return sb.toString();
  }
}
