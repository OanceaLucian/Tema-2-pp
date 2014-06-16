package network_data.header;

/* THE CONTENTS OF THIS FILE SHOULD NOT BE CHANGED */

/* A header type, standing for the source. */
public class Src extends Header {
	protected Src() {
		/* Exists only to defeat instantiation. */
	}

	/* Use this SingletonHolder to instantiate class only once,
	 * in a thread-safe way. The class is final by default,
	 * and the instance being final guarantees it won't change.
	 * The class loading is also thread safe, so we won't have
	 * any unwanted behavior.
	 */
	private static class SingletonHolder {
		public static final Src instance = new Src();
	}

	public static Src getInstance() {
		return SingletonHolder.instance;
	}

	@Override
	public String toString() {
		return "Src";
	}
}