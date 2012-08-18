package edu.mines.jtk.ogl;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Makes OpenGL wrapper class Gl.java for JOGL.
 *GL.
 * To make a new Gl.java, copy the javadoc files GL*.html from JOGL
 * and run this program. The currently required files GL*.html are
 * in the list of inputFileNames declared below.
 * <p>
 * NOTE: This program does not currently generate a Gl.java that will
 * compile, mostly due to duplicate declarations of constants and
 * functions in the various GL* interfaces provided by JOGL. Therefore,
 * some editing of the generated Gl.java is required.
 * <p>
 * This program will require modification if the format of JOGL's 
 * javadoc-generated html files changes.
 * <p>
 * An alternative to this program would be to use reflection on the JOGL
 * class file javax.media.opengl.GL.class, but this alternative would not
 * preserve the names of method parameters. A better alternative would be
 * for JOGL to provide these bindings.
 * @author Dave Hale, Colorado School of Mines
 * @version 2012.08.16
 */
class MakeGl {

  public static void main(String[] args) {
    String[] inputFileNames = {
      "GL.html",
      "GLBase.html",
      "GLLightingFunc.html",
      "GLMatrixFunc.html",
      "GLPointerFunc.html",
      "GL2.html",
      "GL2ES1.html",
      "GL2ES2.html",
      "GL2GL3.html",
    };
    String outputFileName = "Gl.java";
    try {
      BufferedWriter bw = new BufferedWriter(new FileWriter(outputFileName));
      for (String line:PROLOG)
        bw.write(line+NEWLINE);
      for (String inputFileName:inputFileNames) {
        BufferedReader br = new BufferedReader(new FileReader(inputFileName));
        bw.write(NEWLINE+"  // Generated from "+inputFileName+NEWLINE+NEWLINE);
        guts(br,bw);
        br.close();
      }
      for (String line:EPILOG)
        bw.write(line+NEWLINE);
      bw.close();
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
  }

  private static final String NEWLINE = System.getProperty("line.separator");

  private static final String[] PROLOG = {
"/****************************************************************************",
"Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.",
"This program and accompanying materials are made available under the terms of",
"the Common Public License - v1.0, which accompanies this distribution, and is",
"available at http://www.eclipse.org/legal/cpl-v10.html",
"****************************************************************************/",
"package edu.mines.jtk.ogl;",
"",
"import java.nio.*;",
"import javax.media.opengl.*;",
"import com.jogamp.common.nio.PointerBuffer;",
"",
"/**",
" * OpenGL standard constants and functions.",
" * @author Dave Hale, Colorado School of Mines",
" * @version 2012.08.17",
" */",
"public class Gl {",
"",
};

  private static final String[] EPILOG = {
"  public static boolean isExtensionAvailable(String extensionName) {",
"    return gl().isExtensionAvailable(extensionName);",
"  }",
"",
"  public static void setSwapInterval(int interval) {",
"    gl().setSwapInterval(interval);",
"  }",
"",
"  private static GL2 gl() {",
"    return (GL2)GLContext.getCurrentGL();",
"  }",
"",
"  ///////////////////////////////////////////////////////////////////////////",
"  // private",
"",
"  private Gl() {",
"  }",
"}",
};

  // These patterns and functions depend on format of javadoc html files!
  private static final Pattern _conName =
    Pattern.compile("int <B>(GL_\\w*)</B>");
  private static final Pattern _funType = 
    Pattern.compile("(\\w+)(?:</A>)?((?:\\[\\])*) <B>gl");
  private static final Pattern _parType = 
    Pattern.compile("(\\w+)(?:</A>)?((?:\\[\\])*)&nbsp");
  private static final Pattern _funName =
    Pattern.compile("<B>(gl\\w+)</B>");
  private static final Pattern _parName = 
    Pattern.compile("&nbsp;(\\w+)");
  private static boolean hasConstant(String input) {
    return input.contains("static final int <B>GL_");
  }
  private static boolean hasFunction(String input) {
    return input.contains(" <B>gl");
  }
  private static boolean endFunction(String input) {
    return input.endsWith(")</PRE>");
  }
  private static String getConName(String input) {
    Matcher m = _conName.matcher(input);
    return m.find()?m.group(1):null;
  }
  private static String getFunType(String input) {
    Matcher m = _funType.matcher(input);
    return m.find()?m.group(1)+m.group(2):null;
  }
  private static String getFunName(String input) {
    Matcher m = _funName.matcher(input);
    return m.find()?m.group(1):null;
  }
  private static String getParType(String input) {
    Matcher m = _parType.matcher(input);
    return m.find()?m.group(1)+m.group(2):null;
  }
  private static String getParName(String input) {
    Matcher m = _parName.matcher(input);
    return m.find()?m.group(1):null;
  }
  private static void guts(BufferedReader br, BufferedWriter bw)
    throws IOException 
  {
    ArrayList<String> parList = new ArrayList<String>();
    for (String input=br.readLine(); input!=null; input=br.readLine()) {
      if (hasConstant(input)) {
        String conName = getConName(input);
        String output = 
          "  public static final int "+conName+NEWLINE +
          "    = GL2."+conName+";"+NEWLINE+NEWLINE;
        bw.write(output);
      } else if (hasFunction(input)) {
        parList.clear();
        String funType = getFunType(input);
        String funName = getFunName(input);
        String output = 
          "  public static "+funType+" "+funName+"("+NEWLINE +
          "    ";
        String parType = getParType(input);
        String parName = getParName(input);
        if (parType!=null && parName!=null) {
          output += parType+" "+parName;
          parList.add(parName);
        }
        while (!endFunction(input)) {
          output += ","+NEWLINE;
          input = br.readLine(); 
          parType = getParType(input);
          parName = getParName(input);
          parList.add(parName);
          output += "    "+parType+" "+parName;
        }
        output += ") {"+NEWLINE;
        bw.write(output);
        if (funType.equals("void")) {
          output = "    gl()."+funName+"(";
        } else {
          output = "    return gl()."+funName+"(";
        }
        int n = parList.size();
        if (n==0) {
          bw.write(output+");"+NEWLINE);
        } else {
          bw.write(output+NEWLINE);
          for (int k=0; k<n; ++k) {
            if (k<n-1) {
              output = "      "+parList.get(k)+",";
            } else {
              output = "      "+parList.get(k)+");";
            }
            bw.write(output+NEWLINE);
          }
        }
        bw.write("  }"+NEWLINE+NEWLINE);
      }
    }
  }
}
