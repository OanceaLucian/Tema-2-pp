package network_data.value;

/* THE CONTENTS OF THIS FILE SHOULD NOT BE CHANGED */

/* A value type, standing for no value. */
public class Null extends Value {
	protected Null() {
		/* Exists only to defeat instantiation. */
	}

	/* Use this SingletonHolder to instantiate class only once,
	 * in a thread-safe way. The class is final by default,
	 * and the instance being final guarantees it won't change.
	 * The class loading is also thread safe, so we won't have
	 * any unwanted behavior.
	 */
	private static class SingletonHolder {
		public static final Null instance = new Null();
	}

	public static Null getInstance() {
		return SingletonHolder.instance;
	}

	@Override
	public Value intersect(Value v) {
		return getInstance();
	}

	@Override
	public String toString() {
		return "Null";
	}
}