package network_function;

import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import network_data.compact_flow.CompactFlow;
import network_data.flow.Flow;
import network_data.flow.FlowNonVoid;
import network_data.flow.VoidFlow;
import network_data.header.Header;
import network_data.value.StringAtom;

/* A rewriter is an element built on top of a wire or a filter.
 * It performs the logic of the previously specified elements plus a number of rewritings of headers.
 * 
 * Here we'll only treat the rewriter part, so we'll receive a list
 * of rewrites to be done and we'll create a pair of (match,modify)
 * functions that should help us on doing that.
 * 
 * [] - the rewrite list is empty, do nothing
 * [(Dst,2)] - rewrites Dst to 2 for every flow it processes.
 * [(Dst,2),(Src,1)] - rewrites Dst to 2 and Src to 1 for every flow it processes.
 */
public class Rewriter implements Element<List<Entry<Header, List<String>>>> {

	/*
	 * TODO: implement match/modify for a rewriter NetworkElement. Params are
	 * like in case of Filter NetworkElement, check description there.
	 */
	@Override
	public NetworkElement getMatchAndModify(
			final List<Entry<Header, List<String>>> rewritings) {
		return new NetworkElement() {
			@Override
			public Flow modify(Flow f) {
				Flow j = f.deepClone();
				Set<CompactFlow> result = new HashSet<CompactFlow>();
				System.out.println(rewritings);
				if (rewritings.isEmpty())
					return j;
				if (rewritings.size() == 1) {
					for (CompactFlow cf : ((FlowNonVoid) j).getSet()) {
						result.add(cf.rewrite(rewritings.get(0).getKey(),
								new StringAtom(rewritings.get(0).getValue()
										.get(0))));

					}
				return new FlowNonVoid(result);
				}
				if (rewritings.size() == 2) {
					for (CompactFlow cf : ((FlowNonVoid) j).getSet())
						result.add(cf.rewrite(
								rewritings.get(0).getKey(),
								new StringAtom(rewritings.get(0).getValue()
										.get(0))).rewrite(
								rewritings.get(1).getKey(),
								new StringAtom(rewritings.get(1).getValue()
										.get(0))));

					return new FlowNonVoid(result);
				}
				return VoidFlow.getInstance();
			}

			@Override
			public boolean match(Flow f) {
				return true;
			}
		};
	}
}
