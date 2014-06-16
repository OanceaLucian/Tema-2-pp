package network_data.compact_flow;

import network_data.header.Header;
import network_data.value.Value;

/* THE CONTENTS OF THIS FILE SHOULD NOT BE CHANGED */

public class Void extends CompactFlow {
	protected Void() {
		/* Exists only to defeat instantiation. */
	}

	/* Use this SingletonHolder to instantiate class only once,
	 * in a thread-safe way. The class is final by default,
	 * and the instance being final guarantees it won't change.
	 * The class loading is also thread safe, so we won't have
	 * any unwanted behavior.
	 */
	private static class SingletonHolder {
		public static final Void instance = new Void();
	}

	public static Void getInstance() {
		return SingletonHolder.instance;
	}

	@Override
	public CompactFlow intersect(CompactFlow a) {
		return getInstance();
	}

	/* Void is subset of any CompactFlow. */
	@Override
	public boolean subset(CompactFlow a) {
		return true;
	}

	@Override
	public CompactFlow rewrite(Header h, Value v) {
		return getInstance();
	}
	@Override
	public CompactFlow deepClone() {
		return getInstance();
	}

}
