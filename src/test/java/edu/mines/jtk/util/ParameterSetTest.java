/****************************************************************************
Copyright (c) 2006, Colorado School of Mines and others. All rights reserved.
This program and accompanying materials are made available under the terms of
the Common Public License - v1.0, which accompanies this distribution, and is
available at http://www.eclipse.org/legal/cpl-v10.html
****************************************************************************/
package edu.mines.jtk.util;

import java.io.FileNotFoundException;
import java.io.IOException;

import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Tests {@link edu.mines.jtk.util.ParameterSet}.
 * @author Dave Hale, Colorado School of Mines
 * @version 02/21/2000, 08/24/2006.
 */
public class ParameterSetTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(ParameterSetTest.class);
    junit.textui.TestRunner.run(suite);
  }

  public void testClone() {
    ParameterSet root = new ParameterSet("root");
    String s1 = root.toString();
    ParameterSet foo = root.addParameterSet("foo");
    foo.addParameter("bar");
    try {
      ParameterSet temp = (ParameterSet)root.clone();
      temp.remove("foo");
      root.replaceWith(temp);
      String s2 = root.toString();
      assertTrue(s1.equals(s2));
    } catch (CloneNotSupportedException e) {
      assertTrue(false);
    }
  }

  public void testSpecialCharacters() {
    ParameterSet root = new ParameterSet("foo<&>\"\'\\ bar");
    Parameter foo = root.addParameter("foo");
    String[] sa = {
      "foo",
      "foo<&>\"\'\\ bar",
      "foo\nbar",
      "foo\\nbar",
      "foo\tbar",
      "foo\\tbar",
      "foo\"bar",
      "foo\\",
      "foo \\",
    };
    foo.setStrings(sa);
    String s1 = root.toString();
    //System.out.print(s1);
    root.fromString(s1);
    String s2 = root.toString();
    //System.out.print(s2);
    assertTrue(s1.equals(s2));
    foo = root.getParameter("foo");
    String[] sb = foo.getStrings();
    assertTrue(sa.length==sb.length);
    for (int i=0; i<sa.length; ++i) {
      assertTrue(sa[i].equals(sb[i]));
    }
  }

  public void testGeneral() {
    ParameterSet psroot = new ParameterSet();

    ParameterSet ss1 = psroot.addParameterSet("ss1");
    Parameter pb1 = ss1.addParameter("pb1");
    pb1.setBoolean(true);
    Parameter pi1 = ss1.addParameter("pi1");
    pi1.setInt(1);
    Parameter pf1 = ss1.addParameter("pf1");
    pf1.setFloat(1.0f);
    Parameter pd1 = ss1.addParameter("pd1");
    pd1.setDouble(1.0);
    Parameter ps1 = ss1.addParameter("ps1");
    ps1.setString("1.0");

    ParameterSet ss2 = ss1.copyTo(psroot,"ss2");
    ss1.remove();
    ss2.moveTo(psroot,"ss1");

    ss1 = psroot.getParameterSet("ss1");
    ss1.setName("ss2");
    ss2 = psroot.getParameterSet("ss2");
    ss2.setName("ss1");
    ss1 = psroot.getParameterSet("ss1");

    Parameter pfind = ss1.getParameter("ps1");
    assertTrue(pfind!=null);
    pfind = ss1.getParameter("foo");
    assertTrue(pfind==null);

    boolean b1 = ss1.getParameter("pb1").getBoolean();
    assertTrue(b1);
    int i1 = ss1.getParameter("pi1").getInt();
    assertTrue(i1==1);
    float f1 = ss1.getParameter("pf1").getFloat();
    assertTrue(f1==1.0f);
    double d1 = ss1.getParameter("pd1").getDouble();
    assertTrue(d1==1.0);
    String s1 = ss1.getParameter("ps1").getString();
    assertTrue(s1.equals("1.0"));

    String str1 = psroot.toString();
    //System.out.println(str1);
    psroot.fromString(str1);
    String str2 = psroot.toString();
    //System.out.println(str2);
    assertTrue(str1.equals(str2));

    try {
      ParameterSet psrootClone = (ParameterSet)psroot.clone();
      assertTrue(psroot.equals(psrootClone));
      assertTrue(psroot.hashCode()==psrootClone.hashCode());
    } catch (CloneNotSupportedException e) {
      assertTrue(false);
    }
  }

  public void testSetGet() {
    ParameterSet ps = new ParameterSet();

    ps.setBoolean("pb",true);
    ps.setInt("pi",1);
    ps.setLong("pl",1);
    ps.setFloat("pf",1.0f);
    ps.setDouble("pd",1.0);
    ps.setString("ps","1.0");

    boolean b = ps.getBoolean("pb",false);
    assertTrue(b);
    int i = ps.getInt("pi",0);
    assertTrue(i==1);
    long l = ps.getLong("pl",0);
    assertTrue(l==1);
    float f = ps.getFloat("pf",0.0f);
    assertTrue(f==1.0f);
    double d = ps.getDouble("pd",0.0);
    assertTrue(d==1.0);
    String s = ps.getString("ps","0.0");
    assertTrue(s.equals("1.0"));

    b = ps.getBoolean("qb",false);
    assertTrue(!b);
    i = ps.getInt("qi",0);
    assertTrue(i==0);
    l = ps.getLong("ql",0);
    assertTrue(l==0);
    f = ps.getFloat("qf",0.0f);
    assertTrue(f==0.0f);
    d = ps.getDouble("qd",0.0);
    assertTrue(d==0.0);
    s = ps.getString("qs","0.0");
    assertTrue(s.equals("0.0"));
  }

  public void disable_testFile() {
    java.io.FileReader file = null;
    try {
      file = new java.io.FileReader("Input_Display.pwflow");
      //file = new java.io.FileReader("test.parset");
    } catch (FileNotFoundException e) {
      System.out.println(
        "ParameterSet.testFile: FileNotFoundException: "+
        e.getMessage());
      assertTrue(false);
    }
    java.io.BufferedReader input = new java.io.BufferedReader(file);
    StringBuffer buffer = new StringBuffer();
    String s;
    try {
      while ((s=input.readLine())!=null) {
        buffer.append(s);
        buffer.append('\n');
      }
      input.close();
    } catch (IOException e) {
      System.out.println("ParameterSet.testFile: IOException: "+
                         e.getMessage());
      assertTrue(false);
    }
    ParameterSet parset = new ParameterSet();
    parset.fromString(buffer.toString());
    System.out.println("parset read from file:\n"+parset.toString());
  }

  /*
  public void disable_testIO() {
    try {
      ParameterSet parset = ParameterSetIO.readFile("Input_Display.pwflow");
      System.out.println("parset read from file:\n"+parset.toString());
    } catch (IOException e) {
      System.out.println("ParameterSet.testIO: IOException: "+
                         e.getMessage());
      assertTrue(false);
    }
  }
  */
}
