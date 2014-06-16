package reachability;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import network_data.flow.Flow;
import network_data.flow.FlowNonVoid;
import network_data.header.Dst;
import network_data.header.Port;
import network_data.header.Src;
import network_data.value.StringAtom;
import network_function.Network;
import network_function.NetworkElement;

/*
 * Reachability is a Visitor and Flow is a Visitable.
 * The idea is that the Flow lets itself be visited by the
 * Reachability, as the Reachability holds all the NetworkElements,
 * thus with them, it can compute flow.succesors for each flow
 * by applying those functions that match(flow)
 *
 * Reachability class has the following flow:
 * - constructor receives input, source, destination from test
 *   and uses Network to parse input and obtain a list of NetworkElements.
 * - start with an initial flow (based on source)
 * - accepting reachability by a flow should have the following effects:
 *   -> the flow is visited by the reachability and the flow's successors
 *      are being filled this way (apply all matching functions on the flow)
 *   -> go through each of the new successors and them to should accept
 *      reachability.
 *   -> stop from visiting a flow if that flow has been visited before,
 *      exit recursion.
 */
public class Reachability implements Visitor {
	/* All network elements, obtained by parsing input. */
	private List<NetworkElement> elements = null;
	/* A list of all explored flows, used to know when to stop. */
	private Set<Flow> all = new HashSet<Flow>();
	/* A list of all the reachable flows to destination. */
	private Set<Flow> reachable = new HashSet<Flow>();

	private Set<Flow> a =new HashSet<Flow>();
	/* Call reachability with:
	 * - input: a special input, parsable by Network; see TestReachability.
	 * - src, dst are some strings telling us where to start, when to stop.
	 */
	public Reachability(String input, String src, String dst) {
		/* Parse input and get the list of all network elements:
		 * pairs of the form (match, modify).
		 */
		a.add(FlowNonVoid.mostGeneralF().rewrite(Port.getInstance(), new StringAtom(src)));
		Network network = new Network(input);
		this.elements = network.getNetworkElements();

		/* TODO: Trigger the visitor pattern by doing an accept on the initial
		 * flow. This does the following:
		 * - the flow will be visited by a visitor (Reachability)
		 *   which will compute the next set of flows (A' in hw),
		 *   the successors.
		 * - the flow will go through each of its successors
		 *   and trigger an accept on them again (so they can
		 *   be visited too).
		 *   
		 * In all this time, gather a list of all visited flows
		 * and stop when we have no new flows to explore.
		 */
		
		while(true){
			if(a.isEmpty()) break;
			Iterator<Flow> iterator =a.iterator();
			while(iterator.hasNext()){
				Flow f=iterator.next();
				f.accept(this);
			}
		 Set<Flow> b=new HashSet<Flow>();
		 b.addAll(a);
		 Iterator<Flow> iterator2=b.iterator();
		 all.addAll(a);
		 a.retainAll(a);
		 while(iterator2.hasNext()){
			 Flow f=iterator2.next();
			 a.addAll(f.successors);
			 }
		 a.removeAll(all);
		}

		/* TODO: after the acceptance, we've finished exploring all flows,
		 * the "all" is completed and converged.
		 * Reachability has finished, we can now select among the
		 * visited flows those with the destination dst.
		 *
		 * Fill this.reachable
		 */
		
		System.out.println(all);
		System.out.println();
		for(Flow  f:all){
			if(f.intersectNotEmpty(FlowNonVoid.mostGeneralF().rewrite(Port.getInstance(), new StringAtom(dst))))
				reachable.add(f.intersect(FlowNonVoid.mostGeneralF().rewrite(Port.getInstance(), new StringAtom(dst))));
		}
	}

	public Set<Flow> getReachable() {
		return reachable;
	}

	/* TODO:
	 * - for current Visitable (Flow), check which elements match
	 *   the flow and apply modify over it to populate its successors.
	 * - if all has seen this Visitable (Flow), stop, avoid loops.
	 *
	 * All will have converged to a fixed point once we've stoped
	 * with all recursion from every flow. Then the all will
	 * contain all possible flows obtained, and we can get the
	 * ones that contain dst in them.
	 */
	@Override
	public void visit(Visitable v) {
		for(NetworkElement e:elements){
			if(e.match((Flow) v)==true){
				System.out.println("modified "+e.modify((Flow)v));
				((Flow) v).successors.add(e.modify((Flow)v));
			}
		}
		/*all.add((Flow) v);
		a.remove((Flow)v);
		a.addAll(((Flow)v).successors);
		a.removeAll(all);
	*/
	}
}
