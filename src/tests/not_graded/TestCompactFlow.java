package tests.not_graded;

import network_data.compact_flow.CompactFlow;
import network_data.compact_flow.CompactFlowNonVoid;
import network_data.header.Dst;
import network_data.header.Port;
import network_data.header.Src;
import network_data.value.StringAtom;
import junit.framework.TestCase;

public class TestCompactFlow extends TestCase {

	/* Asserts nothing, just displays a compact flow. */
	public void testNothingJustDisplayCF() {
		CompactFlow testCf = CompactFlowNonVoid.mostGeneralCF()
				.rewrite(Src.getInstance(), new StringAtom("1"))
				.rewrite(Dst.getInstance(), new StringAtom("2"))
				.rewrite(Port.getInstance(), new StringAtom("3"));
		System.out.println(testCf.toString());
		assertTrue(true);
	}

}
