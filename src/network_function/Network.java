package network_function;

import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import network_data.header.Dst;
import network_data.header.Header;
import network_data.header.Port;
import network_data.header.Src;

/* THE CONTENTS OF THIS FILE SHOULD NOT BE CHANGED */

/* Internal class used to parse input for reachability test.
 * This parses lines like
 * "a-b?[]![];b-c?[]![]" or
 * "a-b?[(Src,p)]![]"
 *
 * where the meaning is:
 * first part before ? is the wire source - destination.
 * next between brackets is a list of (Header, [Value])
 * for conditions (Filter NetworkElement), and last one
 * after ! is a list of the same form, (Header, [Value]),
 * but for rewritings.
 *
 * This class also calls fuse on the resulting NetworkElements.
 * The main method of interest is getNetworkElements which returns
 * a list of [NetworkElement] like in haskell.
 *
 * The response of getNetworkElements() will be used in Reachability.
 */
public class Network {
	private final String WIRES_DELIM = ";";
	private final String FILTER_DELIM = "?";
	private final String REWRITE_DELIM = "!";
	private final String WIRE_DELIM = "-";
	private final String ENTRY_START = "(";
	private final String ENTRY_END = ")";
	private final String ENTRY_DELIM = ",";

	/* Keeps all the available wires.
	 * This element represents a network wire connecting two ports.
	 * i.e. "a" :-> "b" The port "a" is connected to "b".
	 * Use these to create Wire NetworkElements.
	 */
	public Set<Entry<String, String>> wires;

	/*
	 * Keep a mapping from every wire (Entry<String, String>) to a
	 * list of conditions List<Entry<Header, List<String>>> to be
	 * used to create Filter NetworkElements.
	 */
	public HashMap<Entry<String, String>, List<Entry<Header, List<String>>>> conditions;

	/*
	 * In case we have a rewrite, we need to know to rewrite in
	 * association with a wire, so keep a mapping from
	 * Wire (Entry<String,String>) to
	 * a list of rewritings (List<Entry<Header, List<String>>>).
	 * Used to obtain multiple Rewriter NetworkElements.
	 */
	public HashMap<Entry<String, String>, List<Entry<Header, List<String>>>> rewritings;

	private Wire wire;
	private Filter filter;
	private Rewriter rewriter;

	/* Parese input and fill  wire, filter and rewritings.
	 * Input of the form:
	 * only wire:
	 * - "a-b;b-c;c-d;"
	 */
	public Network(String input) {
		this.wires = new HashSet<Entry<String, String>>();
		this.conditions = new HashMap<Entry<String, String>,
		                              List<Entry<Header, List<String>>>>();
		this.rewritings = new HashMap<Entry<String, String>,
		                              List<Entry<Header, List<String>>>>();

		String[] wires_s = input.split(WIRES_DELIM);
		for (String w : wires_s) {
			int filter_delim = w.indexOf(FILTER_DELIM);
			int rewrite_delim = w.indexOf(REWRITE_DELIM);

			String wire_s = w.substring(0, filter_delim);
			/* Also skip [], so add +1 in left and -1 in right to indexes. */
			String filter_s = w.substring(filter_delim+2, rewrite_delim-1);
			String rewrite_s = w.substring(rewrite_delim+2, w.length()-1);

			Entry<String, String> wire = parse_wire(wire_s);
			this.wires.add(new AbstractMap.SimpleEntry<String, String>(
				wire.getKey(), wire.getValue()));
			this.conditions.put(wire, parse_cond(filter_s));
			this.rewritings.put(wire, parse_cond(rewrite_s));
		}

		this.wire = new Wire();
		this.filter = new Filter();
		this.rewriter = new Rewriter();
	}

	/* Returns the list of Network Elements, similar with the
	 * haskell implementation. Each NetworkElement has its own
	 * type (Wire,Filter,Rewriter) and has a set of (match,modify)
	 * methods that we can use to update a flow and compute
	 * reachability.
	 */
	public List<NetworkElement> getNetworkElements() {
		List<NetworkElement> result = new LinkedList<NetworkElement>();

		for (Entry<String, String> wire : this.wires) {
			List<Entry<Header, List<String>>> conditions = this.conditions.get(wire);
			List<Entry<Header, List<String>>> rewritings = this.rewritings.get(wire);

			NetworkElement e1 = this.wire.getMatchAndModify(wire);
			NetworkElement e2 = this.filter.getMatchAndModify(conditions);
			NetworkElement e3 = this.rewriter.getMatchAndModify(rewritings);
			/* Fusing all three together has no negative impact when
			 * one or two of them are empty lists, as they do nothing.
			 */
			result.add(NetworkElement.fuse(NetworkElement.fuse(e1, e2), e3));
		}

		return result;
	}
	
	/* --- Parsing methods. --- */
	private Entry<String, String> parse_wire(String wire_s) {
		String[] s = wire_s.split(WIRE_DELIM);
		if (s.length != 2)
			throw new AssertionError("Should be exactly two ends on wire!");
		return new AbstractMap.SimpleEntry<String, String>(s[0], s[1]);
	}
	private List<Entry<Header, List<String>>> parse_cond(String cond_s) {
		List<Entry<Header, List<String>>> result =
			new LinkedList<Entry<Header, List<String>>>();

		int fromIndex = 0;
		int entry_start = cond_s.indexOf(ENTRY_START, fromIndex);
		int entry_end = cond_s.indexOf(ENTRY_END, fromIndex);
		while (entry_start != -1 || entry_end != -1) {
			String[] array = cond_s.substring(entry_start+1, entry_end)
				.split(ENTRY_DELIM);
			if (array.length < 2)
				throw new AssertionError("Array cannot be of size < 2!");

			/* Parse array line into Header and its values. */
			List<String> values = new LinkedList<String>();
			Header h = parse_header(array[0]);
			String[] array_vals = Arrays.copyOfRange(array, 1, array.length);
			for (String val : array_vals)
				values.add(val);

			/* Add entry to list for this current wire. */
			Entry<Header, List<String>> entry =
				new AbstractMap.SimpleEntry<Header, List<String>>(h, values);
			result.add(entry);

			/* Update index and search for a new (Header, val1, val2, ...). */
			fromIndex = entry_end + 1;
			entry_start = cond_s.indexOf(ENTRY_START, fromIndex);
			entry_end = cond_s.indexOf(ENTRY_END, fromIndex);
		}
		return result;
	}
	private Header parse_header(String h) {
		if (h.equals("Src"))
			return Src.getInstance();
		else if (h.equals("Dst"))
			return Dst.getInstance();
		else if (h.equals("Port"))
			return Port.getInstance();
		else
			throw new AssertionError("Unknown header!");
	}

}