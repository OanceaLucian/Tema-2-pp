package network_function;

import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import network_data.compact_flow.CompactFlow;
import network_data.compact_flow.CompactFlowNonVoid;
import network_data.flow.Flow;
import network_data.flow.FlowNonVoid;
import network_data.flow.VoidFlow;
import network_data.header.Header;
import network_data.value.StringAtom;

/* The filter element represents a list of conditions that need to
 * be respected in order to traverse a wire. The wire is not given,
 * we don't need it here, we'll fuse with the resulted Filter
 * Element outside, where we instantiate the Filter NetworkElement.
 * 
 * [(Src,p)] says that in order to go on the wire (we don't care which
 * here), the Src has to be bound to "p"
 * [(Src,p,q)] says that in order to go on the wire,
 * the Src has to be bound to "p" OR "q"
 * [(Src,p,q),(Dst,"happiness")] says the wire is going to be traversed
 * if and only if Src is bound to "p" OR "q" AND Dst is bound to "happiness".
 */
public class Filter implements Element<List<Entry<Header, List<String>>>> {

	/*
	 * TODO: implement match/modify for filter NetworkElement. You receive as
	 * input a list of entries Header - Conditions, just like we would parse it
	 * [(Src,2,3),(Dst,a)] would translate into: - for header Src, a list of
	 * strings ["2", "3"] - for header Dst, a list of one string ["a"]
	 */
	@Override
	public NetworkElement getMatchAndModify(
			final List<Entry<Header, List<String>>> conditions) {
		return new NetworkElement() {

			@Override
			public Flow modify(Flow f) {
				Set<CompactFlow> filtru1 = new HashSet<CompactFlow>();
				Set<CompactFlow> filtru2 = new HashSet<CompactFlow>();
				
				if (conditions.size() == 1) {
					for (String s : conditions.get(0).getValue())
						filtru1.add(CompactFlowNonVoid.mostGeneralCF().rewrite(
								conditions.get(0).getKey(), new StringAtom(s)));
                      
					return f.intersect(new FlowNonVoid(filtru1));
				}
				if (conditions.size() == 2) {
					for (String s : conditions.get(0).getValue())
						filtru1.add(CompactFlowNonVoid.mostGeneralCF().rewrite(
								conditions.get(0).getKey(), new StringAtom(s)));
					for (String s : conditions.get(1).getValue())
						filtru2.add(CompactFlowNonVoid.mostGeneralCF().rewrite(
								conditions.get(1).getKey(), new StringAtom(s)));
					
                 return f.intersect(new FlowNonVoid(filtru1)).intersect(new FlowNonVoid(filtru2));
				}
				return f;
			}

			@Override
			public boolean match(Flow f) {
				return !this.modify(f).equals(VoidFlow.getInstance());
			}
		};
	}
}
