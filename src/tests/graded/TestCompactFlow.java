package tests.graded;

import network_data.compact_flow.CompactFlow;
import network_data.compact_flow.CompactFlowNonVoid;
import network_data.compact_flow.Void;
import network_data.header.Dst;
import network_data.header.Port;
import network_data.header.Src;
import network_data.value.Any;
import network_data.value.StringAtom;
import junit.framework.TestCase;

public class TestCompactFlow extends TestCase {
	private CompactFlow cf1, cf2, cf3, p;

	@Override
	protected void setUp() {
		cf1 = CompactFlowNonVoid.mostGeneralCF();
		cf2 = CompactFlowNonVoid.mostGeneralCF();
		cf3 = CompactFlowNonVoid.mostGeneralCF();
		p = CompactFlowNonVoid.mostGeneralCF();

		cf1 = cf1.rewrite(Src.getInstance(), new StringAtom("a"));
		cf2 = cf2.rewrite(Src.getInstance(), new StringAtom("b"));
		cf3 = cf3.rewrite(Dst.getInstance(), new StringAtom("c"));
		p = p.rewrite(Port.getInstance(), new StringAtom("x"));
	}

	/* --- Test basic equalities between CFs. --- */

	public void testEquals() {
		assertFalse(cf1.equals(cf2));
		assertFalse(cf1.equals(p));
		assertEquals(cf1, cf1);
		CompactFlow cf = cf2.rewrite(Src.getInstance(), new StringAtom("a"));
		assertEquals(cf1, cf);
	}

	/* --- Test intersecting CFs. --- */

	public void testIntersect0() {
		CompactFlow expected = Void.getInstance();
		CompactFlow actual1 = cf1.intersect(expected);
		CompactFlow actual2 = expected.intersect(cf1);
		assertEquals(expected, actual1);
		assertEquals(expected, actual2);
	}
	public void testIntersect1() {
		CompactFlow actual1 = cf1.intersect(CompactFlowNonVoid.mostGeneralCF());
		CompactFlow actual2 = CompactFlowNonVoid.mostGeneralCF().intersect(cf1);
		CompactFlow expected = cf1;
		/* Intersection is commutative. */
		assertEquals(expected, actual1);
		assertEquals(expected, actual2);
	}
	public void testIntersect2() {
		CompactFlow actual = CompactFlowNonVoid.mostGeneralCF().intersect(
			CompactFlowNonVoid.mostGeneralCF());
		CompactFlow expected = CompactFlowNonVoid.mostGeneralCF();
		assertEquals(expected, actual);
	}
	public void testIntersect3() {
		assertEquals(cf1, cf1.intersect(cf1));
	}
	public void testIntersect4() {
		assertEquals(Void.getInstance(), cf1.intersect(cf2));
		assertEquals(Void.getInstance(), cf2.intersect(cf1));
	}
	public void testIntersect5() {
		CompactFlow actual = cf1.intersect(cf3);
		CompactFlow expected = CompactFlowNonVoid.mostGeneralCF();
		expected = expected.rewrite(Dst.getInstance(), new StringAtom("c"))
		                   .rewrite(Src.getInstance(), new StringAtom("a"));
		assertEquals(expected, actual);
	}
	/* Test that intersection doesn't modify the CF, but creates a new one. */
	public void testIntersect6() {
		CompactFlow expected = CompactFlowNonVoid.mostGeneralCF();
		expected = expected.rewrite(Src.getInstance(), new StringAtom("a"));
		cf1.intersect(cf2);
		assertEquals(expected, cf1);
	}

	/* --- Test subset --- */

	public void testSubset0() {
		CompactFlow most_general = CompactFlowNonVoid.mostGeneralCF();
		assertEquals(true, most_general.subset(most_general));
	}
	public void testSubset1() {
		assertEquals(true, cf1.subset(cf1));
	}
	public void testSubset2() {
		CompactFlow i = cf1.intersect(cf3);
		System.out.println(i);
		System.out.println(cf1);
		assertEquals(true, i.subset(cf1));
	}
	public void testSubset3() {
		CompactFlow i = cf3.intersect(cf1);
		System.out.println(i);
		System.out.println(cf3);
		assertEquals(true, i.subset(cf3));
	}
	public void testSubset4() {
		CompactFlow i = cf1.intersect(cf2);
		assertEquals(true, i.subset(cf1));
	}
	public void testNotSubset0() {
		CompactFlow i = cf1.intersect(cf3);
		assertEquals(false, cf1.subset(i));
	}
	public void testNotSubset1() {
		CompactFlow i = cf3.intersect(cf1);
		assertEquals(false, cf3.subset(i));
	}
	public void testNotSubset2() {
		CompactFlow i = cf1.intersect(cf2);
		assertEquals(false, cf1.subset(i));
	}
	public void testNotSubset3() {
		assertEquals(false, cf1.subset(cf2));
		assertEquals(false, cf1.subset(cf3));
	}

	/* --- Test rewrite --- */

	public void testRewriteNoOrder() {
		CompactFlow cf = CompactFlowNonVoid.mostGeneralCF();
		CompactFlow cf1 = cf.rewrite(Dst.getInstance(), new StringAtom("c")).
		                     rewrite(Src.getInstance(), new StringAtom("a"));
		CompactFlow cf2 = cf.rewrite(Dst.getInstance(), new StringAtom("c")).
		                     rewrite(Src.getInstance(), new StringAtom("a"));
		assertEquals(cf1, cf2);
		/* Also rewrite should not update the given instance but
		 * create a new one.
		 */
		assertEquals(cf, CompactFlowNonVoid.mostGeneralCF());
	}
	public void testRewrite0() {
		CompactFlow expected = cf1;
		CompactFlow actual = cf2.rewrite(Src.getInstance(),
		                                 new StringAtom("a"));
		assertEquals(expected, actual);
	}
	public void testRewrite1() {
		CompactFlow expected = CompactFlowNonVoid.mostGeneralCF();
		CompactFlow actual = cf2.rewrite(Src.getInstance(), Any.getInstance());
		assertEquals(expected, actual);
	}
}
