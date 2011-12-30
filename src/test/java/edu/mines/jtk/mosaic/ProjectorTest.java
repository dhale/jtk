package edu.mines.jtk.mosaic;

import junit.framework.TestCase;

public class ProjectorTest extends TestCase {
	private static final double eps = 1.0e-10;

	public void testMergeA () {
		Projector pa = new Projector(0, 1);
		Projector pb = new Projector(0, 1);
		pa.merge(pb);
		assertEquals(0, pa.u0(), eps);
		assertEquals(1, pa.u1(), eps);
		assertEquals(0, pa.v0(), eps);
		assertEquals(1, pa.v1(), eps);
	}
	public void testMergeB () {
		Projector pa = new Projector(0, 1);
		Projector pb = new Projector(1, 0);
		pa.merge(pb);
		assertEquals(0, pa.u0(), eps);
		assertEquals(1, pa.u1(), eps);
		assertEquals(0, pa.v0(), eps);
		assertEquals(1, pa.v1(), eps);
	}
	public void testMergeC () {
		Projector pa = new Projector(1, 0);
		Projector pb = new Projector(0, 1);
		pa.merge(pb);                  // Fails if assertions are enabled.
		assertEquals(0, pa.u0(), eps); // Fails if assertions not enabled.
		assertEquals(1, pa.u1(), eps); // Fails if assertions not enabled.
		assertEquals(1, pa.v0(), eps);
		assertEquals(0, pa.v1(), eps);
	}
	public void testMergeD () {
		Projector pa = new Projector(1, 0);
		Projector pb = new Projector(1, 0);
		pa.merge(pb);
		assertEquals(0, pa.u0(), eps);
		assertEquals(1, pa.u1(), eps);
		assertEquals(1, pa.v0(), eps);
		assertEquals(0, pa.v1(), eps);
	}

	public void testAsserting () {
		try { assert false; fail("Assertions not enabled!"); }
		catch (AssertionError ex) { /* Good. */ }
	}
}
