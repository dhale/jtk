/****************************************************************************
Copyright (c) 2004, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is 
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jves.ant;

import java.io.*;
import java.util.*;
import org.apache.tools.ant.*;
import org.apache.tools.ant.taskdefs.*;
import org.apache.tools.ant.types.*;
import org.apache.tools.ant.util.*;

/**
 * Custom Ant task that builds a JNI library from C++ source files.
 * This custom task is designed to build JNI libraries for packages 
 * in edu.mines.jves.*. It may not be suitable for other packages.
 * @author Dave Hale
 * @version 2004.11.04
 */
public class Jnicpp extends MatchingTask {

  public void setSrcdir(File srcdir) {
    if (!srcdir.exists())
      throwBuildException("srcdir \""+srcdir.getPath()+"\" does not exist!");
    _srcdir = srcdir;
  }
  public File getSrcdir() {
    return _srcdir;
  }

  public void setObjdir(File objdir) {
    if (!objdir.exists() && !objdir.mkdirs())
      throwBuildException("cannot make objdir \""+objdir.getPath()+"\"!");
    _objdir = objdir;
  }
  public File getObjdir() {
    return _objdir;
  }

  public void setJnidir(File jnidir) {
    if (!jnidir.exists())
      throwBuildException("jnidir \""+jnidir.getPath()+"\" does not exist!");
    _jnidir = jnidir;
  }
  public File getJnidir() {
    return _jnidir;
  }

  public void setJniname(String jniname) {
    _jniname = jniname;
  }
  public String getJniname() {
    return _jniname;
  }

  public class Incarg {
    public Incarg() {
    }
    String _arg;
    public void setArg(String arg) {
      _arg = arg;
    }
    public String getArg() {
      return _arg;
    }
  }
  public Incarg createIncarg() {
    Incarg incarg = new Incarg();
    _incargs.add(incarg);
    return incarg;
  }

  public class Libarg {
    public Libarg() {
    }
    String _arg;
    public void setArg(String arg) {
      _arg = arg;
    }
    public String getArg() {
      return _arg;
    }
  }
  public Libarg createLibarg() {
    Libarg libarg = new Libarg();
    _libargs.add(libarg);
    return libarg;
  }

  public void execute () throws BuildException {

    // Check task attributes.
    if (_srcdir==null)
      throwBuildException("must specify srcdir!");
    if (_objdir==null)
      throwBuildException("must specify objdir!");
    if (_jnidir==null)
      throwBuildException("must specify jnidir!");
    if (_jniname==null)
      throwBuildException("must specify jniname!");

    // Array of cpp files that must be compiled.
    DirectoryScanner ds = getDirectoryScanner(_srcdir);
    String[] files = ds.getIncludedFiles();
    GlobPatternMapper gpm = new GlobPatternMapper();
    gpm.setFrom("*.cpp");
    if (_isLinux) {
      gpm.setTo("*.o");
    } else if (_isWindows) {
      gpm.setTo("*.obj");
    }
    SourceFileScanner sfs = new SourceFileScanner(this);
    File[] cppFiles = sfs.restrictAsFiles(files,_srcdir,_objdir,gpm);

    // Must we compile and/or link?
    File jniFile = makeJniFile(_jnidir,_jniname);
    boolean mustCompile = cppFiles.length>0;
    boolean mustLink = mustCompile || !jniFile.exists();
    if (mustLink)
      log("Building JNI library "+_jniname);

    // If we must compile, compile each cpp file separately.
    if (mustCompile) {
      int narg = _incargs.size();
      String[] incArgs = new String[narg];
      for (int iarg=0; iarg<narg; ++iarg)
        incArgs[iarg] = ((Incarg)_incargs.get(iarg)).getArg();
      for (int i=0; i<cppFiles.length; ++i) {
        File cppFile = cppFiles[i];
        String objName = gpm.mapFileName(cppFile.getName())[0];
        File objFile = new File(_objdir,objName);
        log("  compiling "+cppFile.getName());
        compile(objFile,cppFile,incArgs);
      }
    }

    // If necessary, link object files into the JNI library.
    if (mustLink) {
      String[] objFiles = _objdir.list();
      if (_isWindows) {
        for (int i=0; i<objFiles.length; ++i)
          objFiles[i] = _objdir.getPath() + "\\" + objFiles[i];
      }
      int narg = _libargs.size();
      String[] libArgs = new String[narg];
      for (int iarg=0; iarg<narg; ++iarg)
        libArgs[iarg] = ((Libarg)_libargs.get(iarg)).getArg();
      log("  linking "+jniFile.getName());
      link(jniFile,objFiles,libArgs);
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // private
  
  private File _srcdir = null;
  private File _objdir = null;
  private File _jnidir = null;
  private String _jniname = null;
  private List _incargs = new ArrayList();
  private List _libargs = new ArrayList();
  private static boolean _isLinux;
  private static boolean _isWindows;
  static {
    /*
    System.out.println("os.name="+System.getProperty("os.name"));
    System.out.println("os.arch="+System.getProperty("os.arch"));
    System.out.println("os.version="+System.getProperty("os.version"));
    */
    String osName = System.getProperty("os.name");
    _isLinux = osName.equals("Linux");;
    // TODO: handle other Windows OS
    _isWindows = osName.equals("Windows XP");;
  }

  private void throwBuildException(String message) throws BuildException {
    throw new BuildException(message,getLocation());
  }

  private int execute(String[] cl) throws BuildException {
    return execute(cl,null);
  }

  private int execute(String[] cl, File wd) throws BuildException {
    Execute execute = new Execute();
    execute.setCommandline(cl);
    if (wd!=null)
      execute.setWorkingDirectory(wd);
    int exitCode = 0;
    try {
      exitCode = execute.execute();
    } catch (IOException ioe) {
      throwBuildException("cannot execute command \""+cl+"\"");
    } finally {
      return exitCode;
    }
  }

  ///////////////////////////////////////////////////////////////////////////
  // system-dependent

  // Compile command.
  private void compile(File objFile, File cppFile, String[] incArgs) 
    throws BuildException 
  {
    if (_isLinux) {
      compileGcc(objFile,cppFile,incArgs);
    } else if (_isWindows) {
      compileVc(objFile,cppFile,incArgs);
    }
  }
  private void compileGcc(File objFile, File cppFile, String[] incArgs) 
    throws BuildException 
  {
    String[] cl = {
      "g++","-c","-O2","-fPIC","-Wall",
    };
    cl = addArgs(cl,incArgs);
    cl = addArgs(cl,new String[]{"-o",objFile.getPath(),cppFile.getPath()});
    int exitCode = execute(cl);
    if (exitCode!=0)
      throw new BuildException("g++ compile failed!",getLocation());
  }
  private void compileVc(File objFile, File cppFile, String[] incArgs) 
    throws BuildException 
  {
    String[] cppArgs = {
      "/nologo","/EHs","/c","/W3","/O2","/MD","/DWIN32"
    };
    String[] cl = {"cl",cppFile.getPath()};
    cl = addArgs(cl,cppArgs);
    cl = addArgs(cl,incArgs);
    cl = addArgs(cl,new String[]{"/Fo"+objFile.getPath()});
    int exitCode = execute(cl);
    if (exitCode!=0)
      throw new BuildException("VC++ compile failed!",getLocation());
  }

  // Link command.
  private void link(File jniFile, String[] objFiles, String[] libArgs) 
    throws BuildException 
  {
    if (_isLinux) {
      linkGcc(jniFile,objFiles,libArgs);
    } else if (_isWindows) {
      linkVc(jniFile,objFiles,libArgs);
    }
  }
  private void linkGcc(File jniFile, String[] objFiles, String[] libArgs) 
    throws BuildException 
  {
    String[] cl = {
      "g++","-shared","-fPIC","-o",
      jniFile.getPath()
    };
    cl = addArgs(cl,objFiles);
    cl = addArgs(cl,libArgs);
    int exitCode = execute(cl,_objdir);
    if (exitCode!=0)
      throw new BuildException("g++ link failed!",getLocation());
  }
  private void linkVc(File jniFile, String[] objFiles, String[] libArgs)
    throws BuildException
  {
    String[] lnkArgs = {
      "/nologo","/LD","/MD","/link","/fixed:no","/incremental:no"
    };
    String[] cl = {"cl"};
    cl = addArgs(cl,objFiles);
    cl = addArgs(cl,lnkArgs);
    cl = addArgs(cl,libArgs);
    cl = addArgs(cl,new String[]{"/OUT:"+jniFile.getPath()});
    int exitCode = execute(cl,_objdir);
    if (exitCode!=0)
      throw new BuildException("VC++ link failed!",getLocation());
  }
  private String[] addArgs(String[] a, String[] b) {
    String[] c = new String[a.length+b.length];
    System.arraycopy(a,0,c,0,a.length);
    System.arraycopy(b,0,c,a.length,b.length);
    return c;
  }
  private void printArgs(String[] a) {
    for (int i=0; i<a.length; ++i)
      log(a[i]);
  }

  // JNI shared library name.
  private File makeJniFile(File jnidir, String jniname) {
    if (_isLinux) {
      return makeJniFileLinux(jnidir,jniname);
    } else {
      return makeJniFileWindows(jnidir,jniname);
    }
  }
  private File makeJniFileLinux(File jnidir, String jniname) {
    return new File(jnidir,"lib"+jniname+".so");
  }
  private File makeJniFileWindows(File jnidir, String jniname) {
    return new File(jnidir,jniname+".dll");
  }
}
