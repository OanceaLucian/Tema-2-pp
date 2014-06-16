package network_data.flow;

import java.util.LinkedList;
import java.util.List;

import reachability.Visitable;
import reachability.Visitor;
import network_data.FlowLike;

/* Flow data type, equivalent from Haskell.
 * The flow is a collection of CompactFlows or the impossible flow.
 */
public abstract class Flow implements FlowLike<Flow>, Visitable {
	/* A list of successors which is used to do reachability.
	 * Initially empty, will be populated on the fly by the visitor
	 * (reachability class).
	 */
	public List<Flow> successors = new LinkedList<Flow>();

	/* Helper function to use with (match, modify) implementation. */
	public boolean intersectNotEmpty(Flow f) {
		return !(this.intersect(f) instanceof VoidFlow);
	}

	/* TODO: a flow accepts to be visited by a visitor:
	 * - visit flow by visitor will determine the visitor
	 *   to fill the flow's successors
	 * - flow's successors are filled up by checking which elements
	 *   match on the flow, and "modify" the flow with all elements.
	 * - for each new successor, also accept visitor.
	 */
	@Override
	public void accept(Visitor v) {
		v.visit(this);
	}
}
