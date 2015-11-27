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
 * class file com.jogamp.opengl.GL.class, but this alternative would not
 * preserve the names of method parameters. A better alternative would be
 * for JOGL to provide these bindings.
 * @author Dave Hale, Colorado School of Mines
 * @version 2014.06.10
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
      "GL2ES3.html",
      "GL2GL3.html",
    };
    String outputFileName = "Gl.java";
    HashSet<String> cs = new HashSet<>();
    HashSet<String> fs = new HashSet<>();
    try {
      trace("MakeGl begin ...");
      BufferedWriter bw = new BufferedWriter(new FileWriter(outputFileName));
      for (String line:PROLOG)
        bw.write(line+NEWLINE);
      for (String inputFileName:inputFileNames) {
        trace("processing "+inputFileName);
        BufferedReader br = new BufferedReader(new FileReader(inputFileName));
        bw.write(NEWLINE+"  // Generated from "+inputFileName+NEWLINE+NEWLINE);
        guts(cs,fs,br,bw);
        br.close();
      }
      for (String line:EPILOG)
        bw.write(line+NEWLINE);
      bw.close();
      trace("... done");
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    }
  }

  private static final String NEWLINE = System.getProperty("line.separator");

  private static final String[] PROLOG = {
"/****************************************************************************",
"Copyright 2006, Colorado School of Mines and others.",
"Licensed under the Apache License, Version 2.0 (the \"License\");",
"you may not use this file except in compliance with the License.",
"You may obtain a copy of the License at",
"",
"    http://www.apache.org/licenses/LICENSE-2.0",
"",
"Unless required by applicable law or agreed to in writing, software",
"distributed under the License is distributed on an \"AS IS\" BASIS,",
"WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.",
"See the License for the specific language governing permissions and",
"limitations under the License.",
"****************************************************************************/",
"package edu.mines.jtk.ogl;",
"",
"import java.nio.*;",
"import com.jogamp.opengl.*;",
"import com.jogamp.common.nio.PointerBuffer;",
"",
"/**",
" * OpenGL standard constants and functions.",
" * @author Dave Hale, Colorado School of Mines",
" * @version 2014.06.10",
" */",
"@SuppressWarnings(\"deprecation\")",
"public class Gl {",
"",
};

  private static final String[] EPILOG = {
//"  public static boolean isExtensionAvailable(String extensionName) {",
//"    return gl().isExtensionAvailable(extensionName);",
//"  }",
//"",
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
    Pattern.compile("final&nbsp;int (GL_\\w*)</pre>");
  private static final Pattern _funType = 
    Pattern.compile("(\\w+)(?:</a>)?((?:\\[\\])*)&nbsp;\\w+\\(");
  private static final Pattern _parType = 
    Pattern.compile("(\\w+)(?:</a>)?((?:\\[\\])*)&nbsp;\\w+[,\\)]");
  private static final Pattern _funName =
    Pattern.compile("&nbsp;(\\w+)\\(");
  private static final Pattern _parName = 
    Pattern.compile("(\\w+)(?:</a>)?((?:\\[\\])*)&nbsp;(\\w+)[,\\)]");
  private static boolean hasConstant(String input) {
    return input.contains("static final&nbsp;int GL_");
  }
  private static boolean hasFunction(String input) {
    return input.startsWith("<pre>") &&
           (input.contains("&nbsp;gl") || 
            input.contains("&nbsp;is"));
  }
  private static boolean endFunction(String input) {
    return input.endsWith(")</pre>");
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
    return m.find()?m.group(3):null;
  }
  private static void guts(
      HashSet<String> cs, HashSet<String> fs, 
      BufferedReader br, BufferedWriter bw)
    throws IOException 
  {
    ArrayList<String> parList = new ArrayList<>();
    for (String input=br.readLine(); input!=null; input=br.readLine()) {
      if (hasConstant(input)) {
        String conName = getConName(input);
        if (!cs.add(conName)) {
          trace("  duplicate: "+conName);
          continue;
        }
        String output = 
          "  public static final int "+conName+NEWLINE +
          "    = GL2."+conName+";"+NEWLINE+NEWLINE;
        bw.write(output);
      } else if (hasFunction(input)) {
        String funType = getFunType(input);
        String funName = getFunName(input);
        String funHash = funName+"(";
        String output = 
          "  public static "+funType+" "+funName+"("+NEWLINE +
          "    ";
        String parType = getParType(input);
        String parName = getParName(input);
        parList.clear();
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
          funHash += parType;
          output += "    "+parType+" "+parName;
        }
        output += ") {"+NEWLINE;
        funHash += ")";
        if (!fs.add(funHash)) {
          trace("  duplicate: "+funHash);
          continue;
        }
        bw.write(output);
        if ("void".equals(funType)) {
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
  private static void trace(String s) {
    System.out.println(s);
  }
}
