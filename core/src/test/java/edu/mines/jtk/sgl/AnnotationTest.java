package edu.mines.jtk.sgl;

import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.junit.Assert;

import java.awt.*;
import java.util.Random;

/**
 * Tests {@link edu.mines.jtk.sgl.Annotation}.
 *
 * @author Chris Engelsma
 * @version 2017.03.15
 */
public class AnnotationTest extends TestCase {
  public static void main(String[] args) {
    TestSuite suite = new TestSuite(Annotation.class);
    junit.textui.TestRunner.run(suite);
  }

  public void testConstructorsAllEqual() {
    Random r = new Random();
    int x = (int)(r.nextFloat()*100);
    int y = (int)(r.nextFloat()*100);
    int z = (int)(r.nextFloat()*100);
    Point3 p = new Point3(x,y,z);

    Annotation[] ans = new Annotation[4];
    ans[0] = new Annotation(x,y,z);
    ans[1] = new Annotation(p);
    ans[2] = new Annotation(x,y,z,"");
    ans[3] = new Annotation(p,"");

    for (int i=0; i<ans.length; ++i) {
      Assert.assertEquals(Color.WHITE,ans[i].getColor());
      Assert.assertEquals(p,ans[i].getLocation());
      Assert.assertEquals("",ans[i].getText());
      Assert.assertEquals(Annotation.Alignment.EAST,ans[i].getAlignment());
      Assert.assertEquals(
        new Font("SansSerif",Font.PLAIN,18),ans[i].getFont());
    }
  }

  public void testParametersGetSet() {
    Random r = new Random();
    int x = (int)(r.nextFloat()*100);
    int y = (int)(r.nextFloat()*100);
    int z = (int)(r.nextFloat()*100);

    Annotation actual = new Annotation(0,0,0);
    actual.setLocation(x,y,z);
    actual.setColor(Color.RED);
    actual.setFont(new Font("Impact",Font.BOLD,24));
    actual.setText("Hello World");
    actual.setAlignment(Annotation.Alignment.NORTH);

    Assert.assertEquals(new Point3(x,y,z),actual.getLocation());
    Assert.assertEquals(Color.RED,actual.getColor());
    Assert.assertEquals(new Font("Impact",Font.BOLD,24),actual.getFont());
    Assert.assertEquals("Hello World", actual.getText());
    Assert.assertEquals(Annotation.Alignment.NORTH,actual.getAlignment());
  }

}
