package network_data.value;

/* THE CONTENTS OF THIS FILE SHOULD NOT BE CHANGED */

/* A value type, standing for any type of value. */
public class Any extends Value {
	protected Any() {
		/* Exists only to defeat instantiation. */
	}

	/* Use this SingletonHolder to instantiate class only once,
	 * in a thread-safe way. The class is final by default,
	 * and the instance being final guarantees it won't change.
	 * The class loading is also thread safe, so we won't have
	 * any unwanted behavior.
	 */
	private static class SingletonHolder {
		public static final Any instance = new Any();
	}

	public static Any getInstance() {
		return SingletonHolder.instance;
	}

	@Override
	public Value intersect(Value v) {
		return v;
	}

	@Override
	public String toString() {
		return "Any";
	}
}