package network_data.flow;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

import network_data.compact_flow.CompactFlow;
import network_data.compact_flow.CompactFlowNonVoid;
import network_data.compact_flow.Void;
import network_data.header.Header;
import network_data.value.Value;

public class FlowNonVoid extends Flow {
	private Set<CompactFlow> cfs;

	public FlowNonVoid(Set<CompactFlow> cfs) {
		this.cfs = cfs;
	}

	/* Creates a general flow, with a mostGeneralCF in it. */
	public static FlowNonVoid mostGeneralF() {
		Set<CompactFlow> cfs = new HashSet<CompactFlow>();
		cfs.add(CompactFlowNonVoid.mostGeneralCF());
		return new FlowNonVoid(cfs);
	}
   
	public Set<CompactFlow> getSet(){
		return this.cfs;
	}
	/* TODO:
	 * Intersection of Flows works the same as the intersection of math sets.
	 * Check this out if confused:
	 * http://en.wikipedia.org/wiki/Union_(set_theory)#Union_and_intersection
	 *
	 * Uses deepClone, does not modify "this".
	 */
	@Override
	public Flow intersect(Flow a) {
		if(a instanceof VoidFlow  ) return VoidFlow.getInstance();
		Set<CompactFlow> result = new HashSet<CompactFlow>();
		for(CompactFlow cf1 : this.cfs){
			for(CompactFlow cf2: ((FlowNonVoid)a).getSet() ){
				if(cf1.intersect(cf2) instanceof Void)
					continue;
			  result.add(cf1.intersect(cf2));
		}
			}
		if(result.isEmpty()) return VoidFlow.getInstance();
		return new FlowNonVoid(result);
	}

	/* TODO:
	 * A flow is subset of another if all its CompactFlows have a corresponding
	 * CompactFlow in the second flow to which they are a subset of.
	 * i.e. if for all CompactFlows in the first flow you can find another
	 * CompactFlow in the second flow that includes the CompactFlow picked from
	 * the first Flow.
	 */
	@Override
	public boolean subset(Flow flow) {
		if(flow instanceof VoidFlow) return false;
	   ArrayList<Boolean> bool = new ArrayList<Boolean>();
	   for(CompactFlow cf1:this.cfs){
		   for(CompactFlow cf2:((FlowNonVoid)flow).getSet()){
			   if(cf1.subset(cf2)) {
				   bool.add(true);
				   break;
			   }
		   }
	   }
	  
		if(this.cfs.size()==bool.size()) return true;
		
		return false;
	}

	/* TODO: Applies a rewrite in all member CompactFlows.
	 *
	 * Uses deepClone, does not modify "this".
	 */
	@Override
	public Flow rewrite(Header h, Value v) {
		Set<CompactFlow> result=new HashSet<CompactFlow>();
		for(CompactFlow cf:this.cfs){
			result.add(cf.rewrite(h, v));
		}
		
		return new FlowNonVoid(result);
	}

	/* TODO:
	 * both hashCode and equals are required to be able to
	 * compare flows and compact flows.
	 * Comparison is done only with o1.equals(o2), NOT with o1 == o2.
	 * Check[1] why.
	 * [1] http://stackoverflow.com/questions/27581/overriding-equals-and-hashcode-in-java
	 */
	@Override
	public int hashCode() {
		int hash=0;
		for(CompactFlow cf:this.cfs)
			hash=hash+cf.hashCode();
		return hash;
	}
	/* TODO:
	 * Two flows are equal when they represent the same set of network packets.
	 * Hint: You should implement this after the above functions were
	 * implemented and use them.
	 */
	@Override
	public boolean equals(Object obj) {
		return this.subset((Flow)obj)&& ((Flow)obj).subset(this);
	}

	/* The deepClone is needed because tests and code in general
	 * considers that when doing o1.intersect(o2), a new object
	 * is created, and that o1 is not affected. o1 stands for both
	 * flows and compact flows.
	 */
	@Override
	public Flow deepClone() {
		return new FlowNonVoid(cloneSet());
	}
	private Set<CompactFlow> cloneSet() {
		Set<CompactFlow> cfs = new HashSet<CompactFlow>();
		for (CompactFlow cf : this.cfs)
			cfs.add(cf.deepClone());
		return cfs;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("[Flow ");
		for (CompactFlow cf : this.cfs)
			sb.append(cf.toString() + " ");
		sb.append("]\n");
		return sb.toString();
	}

}