package network_data.compact_flow;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import network_data.header.Header;
import network_data.value.Any;
import network_data.value.Value;

/* The non-void compact flow, the one which is composed
 * of a list of Fields.
 *
 * Where a Field = Entry<Header, Value> ; note that
 * haskell had a similar:
 * type Field = (Header, Value)
 */
public class CompactFlowNonVoid extends CompactFlow {
	private HashMap<Header, Value> fields;

	public CompactFlowNonVoid(HashMap<Header, Value> fields) {
		this.fields = fields;
	}

	public HashMap<Header, Value> getFields() {
		return this.fields;
	}

	/* Creates a general compact flow, with Any for all fields. */
	public static CompactFlowNonVoid mostGeneralCF() {
		HashMap<Header, Value> cf = new HashMap<Header, Value>();
		for (Header h : Header.allHeaders)
			cf.put(h, Any.getInstance());
		return new CompactFlowNonVoid(cf);
	}

	/*
	 * TODO: Intersection of CompactFlows should be performed for each pair of
	 * corresponding headers. i.e. {(Src, "A"), (Dst, ANY), (Port, ANY)}
	 * intersect {(Src, ANY), (Dst, "B"), (Port, ANY)} -> {(Src, "A"), (Dst,
	 * "B"), (Port, ANY)} Intersection with the Void CompactFlow is Void
	 * 
	 * Uses deepClone, does not modify "this".
	 */
	@Override
	public CompactFlow intersect(CompactFlow a) {
		if ( a instanceof Void)
			return Void.getInstance();
		HashMap<Header, Value> result = new HashMap<Header, Value>();
		CompactFlowNonVoid b = (CompactFlowNonVoid) a.deepClone();
		for (Header h1 : this.fields.keySet()) {
			for (Header h2 : b.getFields().keySet()) {
				if (h1.equals(h2)) {
					if (this.fields.get(h1).equals(Any.getInstance()))
						result.put(h1, b.getFields().get(h2));
					else if (b.getFields().get(h2).equals(Any.getInstance()))
						result.put(h1, this.fields.get(h1));
					else if (this.fields.get(h1).equals(b.getFields().get(h2)))
						result.put(h1, this.fields.get(h1));
					else
						return Void.getInstance();

				}
			}

		}
		return new CompactFlowNonVoid(result);
	}

	/*
	 * TODO: Checks if "this" CompactFlow is subset of cf. Nothing is subset of
	 * Void (except from Void) The subset check works as in the case of
	 * intersect - the check should be applied in sequence for every
	 * corresponding header pairs in the two CompactFlows.
	 * 
	 * Uses deepClone, does not modify "this".
	 */
	@Override
	public boolean subset(CompactFlow cf) {
		
		if (cf.equals(Void.getInstance()))
			if (this.fields.equals(Void.getInstance()))
				return true;
			else
				return false;
		CompactFlowNonVoid b = (CompactFlowNonVoid) cf.deepClone();
		for(Header h1:this.fields.keySet()){
			for(Header h2:b.getFields().keySet()){
				if(h1.equals(h2)){
					Value v1 = this.fields.get(h1);
					Value v2=b.getFields().get(h2);
					if(v2.equals(Any.getInstance())) continue;				
					if(v1.equals(Any.getInstance())) if( !v2.equals(Any.getInstance())) return false;
					if(!v1.equals(v2)) return false;
				}
			}
		}
	return true;	
	}

	/*
	 * TODO: Rewrite a header value from a CompactFlow. This does nothing to the
	 * Void flow.
	 * 
	 * Uses deepClone, does not modify "this".
	 */
	@Override
	public CompactFlow rewrite(Header h, Value v) {
		HashMap<Header, Value> clone = cloneMap();
		clone.put(h, v);
		return new CompactFlowNonVoid(clone);
	}

	/*
	 * TODO: both hashCode and equals are required to be able to compare flows
	 * and compact flows. Comparison is done only with o1.equals(o2), NOT with
	 * o1 == o2. Check[1] why. [1]
	 * http://stackoverflow.com/questions/27581/overriding
	 * -equals-and-hashcode-in-java
	 */
	@Override
	public int hashCode() {
		int hash = 0;
		for (Entry<Header, Value> e : this.fields.entrySet())
			hash += e.getKey().hashCode() ^ e.getValue().hashCode();
		return hash;
	}

	/*
	 * TODO: Two compact flows are equal if they carry the same headers, bound
	 * to the same values.
	 */
	@Override
	public boolean equals(Object obj) {

		if (this.fields.equals(Void.getInstance()))
			if (obj.equals(Void.getInstance()))
				return true;
			else
				return false;
		if (obj.equals(Void.getInstance()))
			if (this.fields.equals(Void.getInstance()))
				return true;
			else
				return false;

	 CompactFlowNonVoid cf = (CompactFlowNonVoid) ((CompactFlow) obj).deepClone();
	 for(Header h1:this.fields.keySet()){
		 for(Header h2:cf.getFields().keySet())
			 if(h1.equals(h2)){
				
				 if(!this.fields.get(h1).equals(cf.getFields().get(h2))) return false;
			 }
	 }
		return true;
	}

	/*
	 * The deepClone is needed because tests and code in general considers that
	 * when doing o1.intersect(o2), a new object is created, and that o1 is not
	 * affected. o1 stands for both flows and compact flows.
	 */
	public CompactFlow deepClone() {
		return new CompactFlowNonVoid(cloneMap());
	}

	private HashMap<Header, Value> cloneMap() {
		HashMap<Header, Value> clone = new HashMap<Header, Value>();
		for (Entry<Header, Value> e : this.fields.entrySet())
			clone.put(e.getKey(), e.getValue());
		return clone;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("[CompactFlow ");
		for (Entry<Header, Value> e : this.fields.entrySet())
			sb.append("(" + e.getKey() + "," + e.getValue() + ")");
		sb.append("]");
		return sb.toString();
	}

}