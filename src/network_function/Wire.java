package network_function;

import java.util.Map.Entry;

import network_data.flow.Flow;
import network_data.flow.FlowNonVoid;
import network_data.header.Port;
import network_data.value.StringAtom;

/* This element represents a network wire connecting two ports.
 * i.e. a-b The port "a" is connected to "b".
 * 
 * Wire receives a pair of Strings, where param.getKey() could
 * represent "a" in the above example (the source), and
 * param.getValue() "b" (the destination).
 */
public class Wire implements Element<Entry<String, String>> {

	/* TODO: implement match/modify for a wire NetworkElement. */
	@Override
	public NetworkElement getMatchAndModify(final Entry<String, String> param) {

		return new NetworkElement() {
			@Override
			public Flow modify(Flow f) {
				return f.rewrite(Port.getInstance(),
						new StringAtom(param.getValue()));
			}

			@Override
			public boolean match(Flow f) {
				Flow compare = FlowNonVoid.mostGeneralF().rewrite(
						Port.getInstance(), new StringAtom(param.getKey()));
                  
				return f.subset(compare);
			}
		};
	}
}
