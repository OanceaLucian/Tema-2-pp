package tests.graded;

import java.util.HashSet;
import java.util.Map.Entry;

import network_data.compact_flow.CompactFlow;
import network_data.compact_flow.CompactFlowNonVoid;
import network_data.compact_flow.Void;
import network_data.flow.Flow;
import network_data.flow.FlowNonVoid;
import network_data.flow.VoidFlow;
import network_data.header.Dst;
import network_data.header.Header;
import network_data.header.Port;
import network_data.header.Src;
import network_data.value.Any;
import network_data.value.StringAtom;
import network_data.value.Value;
import junit.framework.TestCase;

public class TestFlow extends TestCase {
	private CompactFlow cf1, cf2, cf3, p;
	private Flow f1, f2, f3, ff2, ff3, ff4, pf;

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

		pf = new FlowNonVoid(new HashSet<CompactFlow>() {{ add(p); }});
		f1 = new FlowNonVoid(new HashSet<CompactFlow>() {{ add(cf1); }});
		f2 = new FlowNonVoid(new HashSet<CompactFlow>() {{ add(cf2); }});
		f3 = new FlowNonVoid(new HashSet<CompactFlow>() {{ add(cf3); }});

		ff2 = new FlowNonVoid(new HashSet<CompactFlow>()
			{{ add(cf1); add(cf2); }});
		ff3 = new FlowNonVoid(new HashSet<CompactFlow>()
			{{ add(cf3); add(cf1); }});
		ff4 = new FlowNonVoid(new HashSet<CompactFlow>()
			{{ add(cf1); add(cf2); add(cf3); }});
	}

	/* --- Test basic equalities between CFs. --- */

	public void testDeepClone() {
		Flow f = f1.deepClone();
		assertEquals(f1, f);
		f = f.rewrite(Src.getInstance(), new StringAtom("b"));
		assertEquals(f2, f);
		assertNotSame(f1, f);
	}
	public void testEquals0() {
		assertEquals(FlowNonVoid.mostGeneralF(), FlowNonVoid.mostGeneralF());
		assertEquals(f1, f1);
	}
	public void testEquals1() {
		assertNotSame(f1, ff2);
	}
	public void testEquals2() {
		assertNotSame(f1, ff3);
	}
	public void testEquals3() {
		assertNotSame(f1, ff4);
	}
	public void testEquals4() {
		assertNotSame(f1, FlowNonVoid.mostGeneralF());
	}
	public void testEquals5() {
		assertNotSame(ff3, ff2);
	}
	public void testEquals6() {
		assertNotSame(ff2, ff4);
		assertNotSame(ff3, ff4);
	}
	public void testSetRemoveDuplicates() {
		Flow f = new FlowNonVoid(new HashSet<CompactFlow>()
			{{ add(cf2); add(cf1); add(cf1); add(cf2); }});
		assertEquals(ff2, f);
	}

	/* --- Test subset flows. --- */

	public void testSubsetF0() {
		assertTrue(f1.subset(ff2));
	}
	public void testSubsetF1() {
		assertTrue(f1.subset(ff3));
	}
	public void testSubsetF2() {
		assertTrue(f1.subset(FlowNonVoid.mostGeneralF()));
	}
	public void testSubsetF3() {
		assertTrue(f1.subset(f1));
	}
	public void testSubsetF4() {
		assertFalse(f1.subset(f2));
	}
	public void testSubsetF5() {
		assertTrue(ff2.subset(ff4));
	}
	public void testSubsetF6() {
		assertTrue(ff3.subset(ff4));
	}
	public void testSubsetF7() {
		assertTrue(f3.subset(ff3));
	}
	public void testSubsetF8() {
		assertTrue(ff3.subset(FlowNonVoid.mostGeneralF()));
	}
	public void testSubsetF9() {
		assertFalse(ff2.subset(f1));
	}
	public void testSubsetF10() {
		assertFalse(ff3.subset(f1));
	}
	public void testSubsetF11() {
		assertFalse(FlowNonVoid.mostGeneralF().subset(ff3));
	}

	/* --- Test intersect flows. --- */
	public void testIntersectF0() {
		Flow expected = f1;
		Flow actual = f1.intersect(ff2);
		assertEquals(expected, actual);
	}
	public void testIntersectF1() {
		assertTrue(f1.subset(ff3.intersect(ff2)));
	}
	public void testIntersectF2() {
		assertTrue(f2.subset(ff4.intersect(ff2)));
	}
	public void testIntersectF3() {
		assertTrue(f3.subset(ff4.intersect(ff3)));
	}
	public void testIntersectF4() {
		assertFalse(f2.subset(ff3.intersect(ff2)));
	}
	public void testIntersectF5() {
		assertFalse(f3.subset(ff3.intersect(ff2)));
	}
	public void testIntersectF6() {
		assertFalse(pf.subset(ff2));
	}
	public void testIntersectF7() {
		Flow expected = VoidFlow.getInstance();
		Flow actual = pf.intersect(ff2);
		assertNotSame(expected, actual);
	}
	public void testIntersectF8() {
		Flow expected = f2;
		Flow actual = ff3.intersect(ff2);
		assertNotSame(expected, actual);
	}
	public void testIntersectF9() {
		Flow expected = f3;
		Flow actual = ff3.intersect(ff2);
		assertNotSame(expected, actual);
	}
	public void testIntersectF10() {
		Flow expected = ff3;
		Flow actual = ff3.intersect(FlowNonVoid.mostGeneralF());
		assertEquals(expected, actual);
	}
	public void testIntersectF11() {
		assertTrue(ff3.subset(ff3.intersect(FlowNonVoid.mostGeneralF())));
	}
	public void testIntersectF12() {
		assertFalse(ff3.subset(ff2.intersect(FlowNonVoid.mostGeneralF())));
	}
	public void testIntersectF13() {
		Flow mgf = FlowNonVoid.mostGeneralF();
		assertEquals(mgf, mgf.intersect(mgf));
	}
}
