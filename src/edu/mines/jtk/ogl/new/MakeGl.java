import java.io.*;
import java.util.*;
import java.util.regex.*;

public class MakeGl {

  public static void main(String[] args) {
    String inputFileName = "Gl.txt";
    String outputFileName = "Gl.java";
    try {
      BufferedReader br = null;
      BufferedWriter bw = null;
      br = new BufferedReader(new FileReader(inputFileName));
      bw = new BufferedWriter(new FileWriter(outputFileName));
      for (int i=0; i<PROLOG.length; ++i)
        bw.write(PROLOG[i]+NEWLINE);
      guts(br,bw);
      for (int i=0; i<EPILOG.length; ++i)
        bw.write(EPILOG[i]+NEWLINE);
      br.close();
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
"import javax.media.opengl.GL;",
"import javax.media.opengl.GLContext;",
"",
"/**",
" * OpenGL standard constants and methods.",
" * @author Dave Hale, Colorado School of Mines",
" * @version 2006.07.07",
" */",
"public class Gl {",
"",
};

  private static final String[] EPILOG = {
"  public static boolean isExtensionAvailable(String extensionName) {",
"    return gl().isExtensionAvailable(extensionName);",
"  }",
"",
"  private static GL gl() {",
"    return GLContext.getCurrent().getGL();",
"  }",
"",
" ///////////////////////////////////////////////////////////////////////////",
"  // private",
"",
"  private Gl() {",
"  }",
"}",
};

  private static void guts(BufferedReader br, BufferedWriter bw) 
    throws IOException 
  {
    String input,output,name,par;
    for (input=br.readLine(); input!=null; input=br.readLine()) {
      if (input.startsWith("public")) {
        if (!input.startsWith("public static"))
          break;
        int i = input.indexOf("GL_");
        name = input.substring(i);
        output = "  "+input+NEWLINE +
                        "    = GL."+name+";"+NEWLINE+NEWLINE;
        bw.write(output);
      }
    }
    ArrayList<String> parList = new ArrayList<String>();
    Pattern parPattern = Pattern.compile(".*\\s(\\w*)[,\\)]");
    for (; input!=null; input=br.readLine()) {
      if (input.startsWith("public")) {
        int i = input.indexOf("gl");
        int j = input.indexOf("(");
        if (i<0 || j<=i)
          break;
        boolean isVoid = input.indexOf("void ")>=0;
        name = input.substring(i,j);
        parList.clear();
        output = "";
        for (boolean first=true; input!=null; input=br.readLine(),first=false) {
          if (first) {
            int k = input.indexOf(" ");
            output = "  public static"+input.substring(k);
          } else {
            output = "         "+input;
          }
          Matcher parMatcher = parPattern.matcher(input);
          if (parMatcher.matches())
            parList.add(parMatcher.group(1));
          if (input.endsWith(")")) {
            output = output+" {";
            break;
          }
          bw.write(output+NEWLINE);
        }
        bw.write(output+NEWLINE);
        if (isVoid) {
          output = "    gl()."+name+"(";
        } else {
          output = "    return gl()."+name+"(";
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

/*
read input line
for(;;) {
  if no input line,
    break
  if input line begins with "public " {
    if input line does not begin with "public static ", 
      break
    extract name that begins with GL and ends input line
    new empty output line
    append "  " to output line
    append input line to output line
    append " = \n    GL."+name to output line
    write output line
  }
  read input line
}
for(;;) {
  if no input line,
    break
  if input line begins with "public " {
    extract method name that begins after " " and ends before "("
    new par list
    for (;;) {
      new output line
      append "  " to output line
      append input line to output line
      extract par that begins after " " and ends before "," or ")"
      if par not empty
        add par to par list
      if input line ends with ")" {
        append " {" to output line
        break
      }
      write output line
      read input line
    }
    write output line
    append "    gl()."+name+"(" to output line
    write output line
    for each par in par list {
      new output line
      append "      "+par to output line
      if last par
        append ");" to output line
      else
        append "," to output line
      write output line
    }
  }
  read input line
}
*/
