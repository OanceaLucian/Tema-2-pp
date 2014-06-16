package network_data.value;

/* A value type, standing for a string type of value. */
public class StringAtom extends Value {
	private String str;
    
	
	public StringAtom(String str) {
		this.str = str;
	}

	 public String getStr(){
		 return this.str;
	 }
	/* TODO:
	 * also implement hashCode, as we will be implementing equals below.
	 */
	@Override
	public int hashCode() {
		return this.str.hashCode();
	}
	/* TODO:
	 * can be useful to be able to compare values with v1.equals(v2)
	 * when implementing equals on a compact flow.
	 * You can compare the other values (Any, Null) with equals, as they
	 * are singletons and represent a single instantiated entitity.
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj.equals(Any.getInstance())) return false;
		if(obj.equals(Null.getInstance())) return false;
		return this.str.equals(((StringAtom)obj).getStr());
	}

	/* TODO:
	 * Implementing the intersect for the Value type will make it easier
	 * to do intersection of compact flows between corresponding headers.
	 * 
	 * Intersect between x and Any is x.
	 * Intersect between x and Null is Null.
	 * Intersect between x and y where x == y is possible, else Null.
	 */
	@Override
	public Value intersect(Value v) {
		return null;
	}

	@Override
	public String toString() {
		return str;
	}
}