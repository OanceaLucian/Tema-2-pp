package tests.graded;

import java.util.HashSet;
import java.util.Set;

import reachability.Reachability;

import network_data.compact_flow.CompactFlow;
import network_data.compact_flow.CompactFlowNonVoid;
import network_data.flow.Flow;
import network_data.flow.FlowNonVoid;
import network_data.header.Dst;
import network_data.header.Port;
import network_data.header.Src;
import network_data.value.StringAtom;
import junit.framework.TestCase;

public class TestReachability extends TestCase {

	public void testReachabilityLink() {
		String net = "a-b?[]![];b-c?[]![]";

		/* Compute the reachable flows. */
		Reachability r = new Reachability(net, "a", "c");
		assertEquals(1, r.getReachable().size());

		Set<Flow> actual = r.getReachable();

		/* Result: [Flow [CompactFlow (Port,c)(Src,Any)(Dst,Any)]]. */
		final Flow f = FlowNonVoid.mostGeneralF()
			.rewrite(Port.getInstance(), new StringAtom("c"));
		Set<Flow> expected = new HashSet<Flow>() {{ add(f); }};

		assertEquals(expected, actual);
	}

	public void testReachabilityDiamond() {
		String net = "a-b?[]![];b-c?[]![];a-d?[]![];d-c?[]![]";

		/* Compute the reachable flows. */
		Reachability r = new Reachability(net, "a", "c");
		assertEquals(1, r.getReachable().size());

		Set<Flow> actual = r.getReachable();

		/* Result: [Flow [CompactFlow (Port,c)(Src,Any)(Dst,Any)]]. */
		final Flow f = FlowNonVoid.mostGeneralF()
			.rewrite(Port.getInstance(), new StringAtom("c"));
		Set<Flow> expected = new HashSet<Flow>() {{ add(f); }};

		assertEquals(expected, actual);
	}

	public void testReachabilityLoop() {
		String net = "a-b?[]![];b-a?[]![]";

		/* Compute the reachable flows. */
		Reachability r = new Reachability(net, "a", "a");
		assertEquals(1, r.getReachable().size());

		Set<Flow> actual = r.getReachable();

		/* Result: [Flow [CompactFlow (Port,a)(Src,Any)(Dst,Any)]]. */
		final Flow f = FlowNonVoid.mostGeneralF()
			.rewrite(Port.getInstance(), new StringAtom("a"));
		Set<Flow> expected = new HashSet<Flow>() {{ add(f); }};

		assertEquals(expected, actual);
	}

	public void testFilter() {
		String net = "a-b?[(Src,p)]![]";
		Reachability r = new Reachability(net, "a", "b");
		Set<Flow> actual = r.getReachable();

		/* Result: [ Flow [CompactFlow (Dst,Any)(Port,b)(Src,p)] ]. */
		final Flow f = FlowNonVoid.mostGeneralF()
			.rewrite(Port.getInstance(), new StringAtom("b"))
			.rewrite(Src.getInstance(), new StringAtom("p"));
		Set<Flow> expected = new HashSet<Flow>() {{ add(f); }};

		assertEquals(expected, actual);
	}

	public void testOneRuleFilter() {
		String net = "a-b?[(Src,p,q)]![]";
		Reachability r = new Reachability(net, "a", "b");
		Set<Flow> actual = r.getReachable();

		/* Result: [Flow [CompactFlow (Src,q)(Dst,Any)(Port,b)]
		 *               [CompactFlow (Src,p)(Dst,Any)(Port,b)] ].
		 */
		final CompactFlow cf1 = CompactFlowNonVoid.mostGeneralCF()
			.rewrite(Port.getInstance(), new StringAtom("b"))
			.rewrite(Src.getInstance(), new StringAtom("q"));
		final CompactFlow cf2 = CompactFlowNonVoid.mostGeneralCF()
			.rewrite(Port.getInstance(), new StringAtom("b"))
			.rewrite(Src.getInstance(), new StringAtom("p"));
		final Set<CompactFlow> cfs =
			new HashSet<CompactFlow>() {{ add(cf1); add(cf2); }};
		Set<Flow> expected = new HashSet<Flow>() {{ add(new FlowNonVoid(cfs)); }};

		assertEquals(expected, actual);
	}

	public void testcascadeFilter1() {
		String net = "a-b?[(Src,p,q)]![];b-c?[(Src,q)]![]";
		Reachability r = new Reachability(net, "a", "c");
		Set<Flow> actual = r.getReachable();

		/* Result: [[Flow [CompactFlow (Dst,Any)(Port,c)(Src,q)] ].
		 */
		final Flow f = FlowNonVoid.mostGeneralF()
			.rewrite(Port.getInstance(), new StringAtom("c"))
			.rewrite(Src.getInstance(), new StringAtom("q"));
		Set<Flow> expected = new HashSet<Flow>() {{ add(f); }};

		assertEquals(expected, actual);
	}

	public void testcascadeFilter2() {
		String net = "a-b?[(Src,p,q)]![];b-c?[(Src,s)]![]";
		Reachability r = new Reachability(net, "a", "c");
		Set<Flow> actual = r.getReachable();

		/* Result: [].
		 */
		assertEquals(0, actual.size());
	}

	public void testRewriteTest() {
		String net = "a-b?[]![];b-c?[]![(Src,192.0.0.1)]";
		Reachability r = new Reachability(net, "a", "c");
		Set<Flow> actual = r.getReachable();

		/* Result: [[Flow [CompactFlow (Dst,Any)(Port,c)(Src,q)] ].
		 */
		final Flow f = FlowNonVoid.mostGeneralF()
			.rewrite(Port.getInstance(), new StringAtom("c"))
			.rewrite(Src.getInstance(), new StringAtom("192.0.0.1"));
		Set<Flow> expected = new HashSet<Flow>() {{ add(f); }};

		assertEquals(expected, actual);
	}

	public void testFinal() {
		String net = "F:0-F:1?[(Src,1,3)]![];"			+
		             "F:2-F:1?[(Src,1,3)]![];"			+
		             "F:1-P:0?[]![];"					+
		             "P:0-P:1?[]![(Dst,2)];" 			+
		             "P:0-P:2?[]![(Dst,2)];"			+
		             "P:2-R:0?[]![];"					+
		             "R:0-R:1?[]![(Dst,3),(Src,3)];"	+
		             "R:1-F:2?[]![]";
		Reachability r = new Reachability(net, "F:0", "P:1");
		Set<Flow> actual = r.getReachable();

		/* Result:
		 * [ [Flow [CompactFlow (Dst,2)(Src,1)(Port,P:1)]
		 *         [CompactFlow (Dst,2)(Src,3)(Port,P:1)]]
		 *   [Flow [CompactFlow (Dst,2)(Src,3)(Port,P:1)]]
		 * ]
		 */
		final Flow f1 = FlowNonVoid.mostGeneralF()
			.rewrite(Port.getInstance(), new StringAtom("P:1"))
			.rewrite(Src.getInstance(), new StringAtom("3"))
			.rewrite(Dst.getInstance(), new StringAtom("2"));
		final CompactFlow cf21 = CompactFlowNonVoid.mostGeneralCF()
			.rewrite(Port.getInstance(), new StringAtom("P:1"))
			.rewrite(Src.getInstance(), new StringAtom("1"))
			.rewrite(Dst.getInstance(), new StringAtom("2"));
		final CompactFlow cf22 = CompactFlowNonVoid.mostGeneralCF()
			.rewrite(Port.getInstance(), new StringAtom("P:1"))
			.rewrite(Src.getInstance(), new StringAtom("3"))
			.rewrite(Dst.getInstance(), new StringAtom("2"));
		Set<CompactFlow> cfs = new HashSet<CompactFlow>() {{ add(cf21); add(cf22); }};
		final Flow f2 = new FlowNonVoid(cfs);

		Set<Flow> expected = new HashSet<Flow>() {{ add(f1); add(f2); }};

		assertEquals(expected, actual);
	}

}
