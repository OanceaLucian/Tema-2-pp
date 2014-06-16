package tests.not_graded;

import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;

import junit.framework.TestCase;
import network_data.header.Dst;
import network_data.header.Header;
import network_data.header.Port;
import network_data.header.Src;
import network_function.Network;

/* This file is NOT graded, it's only used to test the parsing
 * methods from Network.
 */
public class TestNetwork extends TestCase {
	private Network network1, network2;

	public void setUp() {
		String net1 = "a-b?[]![];b-c?[]![]";
		network1 = new Network(net1);
		String net2 = "a-b?[(Src,P:0,c),(Dst,b)]![(Port,P:0)];b-F:0?[]![];c-a?[]![]";
		network2 = new Network(net2);
	}

	public void testWireNumber() {
		assertEquals(2, network1.wires.size());
		assertEquals(3, network2.wires.size());
	}
	public void testFilters() {
		for (Entry<String, String> e : network1.conditions.keySet())
			assertEquals(0, network1.conditions.get(e).size());
		
		/* mighty hack but easier to do. */
		Entry<String, String> ab =
			new AbstractMap.SimpleEntry<String, String>("a", "b");
		List<Entry<Header, List<String>>> l = network2.conditions.get(ab);
		assertEquals(2, l.size());
		assertEquals(Src.getInstance(), l.get(0).getKey());
		assertEquals(new LinkedList<String>() {{ add("P:0"); add("c"); }},
		             l.get(0).getValue());
		assertEquals(Dst.getInstance(), l.get(1).getKey());
		assertEquals(new LinkedList<String>() {{ add("b"); }},
		             l.get(1).getValue());
	}
	public void testRewritings() {
		for (Entry<String, String> e : network1.rewritings.keySet())
			assertEquals(0, network1.conditions.get(e).size());
		
		/* mighty hack but easier to do. */
		Entry<String, String> ab =
			new AbstractMap.SimpleEntry<String, String>("a", "b");
		List<Entry<Header, List<String>>> l = network2.rewritings.get(ab);
		assertEquals(1, l.size());
		assertEquals(Port.getInstance(), l.get(0).getKey());
		assertEquals(new LinkedList<String>() {{ add("P:0"); }},
		             l.get(0).getValue());
	}

	public void testGetNetworkElementsNumber() {
		assertEquals(2, network1.getNetworkElements().size());
		assertEquals(3, network2.getNetworkElements().size());
	}

	public void testFinalParseWiresNumber() {
		String net = "F:0-F:1?[(Src,1,3)]![];"			+
		             "F:2-F:1?[(Src,1,3)]![];"			+
		             "F:1-P:0?[]![];"					+
		             "P:0-P:1?[]![(Dst,2)];" 			+
		             "P:0-P:2?[]![(Dst,2)];"			+
		             "P:2-R:0?[]![];"					+
		             "R:0-R:1?[]![(Dst,3),(Src,3)];"	+
		             "R:1-F:2?[]![]";
		Network network = new Network(net);
		assertEquals(8, network.wires.size());
	}

	/* --- Check errors are thrown. --- */

	public void testThrowUnknownHeader() {
		Throwable e = null;
		Network network = null;
		String net = "a-b?[(SrcNotKnown,P:0,c)]![]";

		try {
			network = new Network(net);
		} catch (Throwable ex) {
			e = ex;
		}
		assertTrue(e instanceof AssertionError);
		assertEquals("Unknown header!", e.getMessage());
	}
	public void testThrowValueSize() {
		Throwable e = null;
		Network network = null;
		String net = "a-b?[(Src)]![]";

		try {
			network = new Network(net);
		} catch (Throwable ex) {
			e = ex;
		}
		assertTrue(e instanceof AssertionError);
		assertEquals("Array cannot be of size < 2!", e.getMessage());
	}
}
