package network_data.flow;

import network_data.header.Header;
import network_data.value.Value;

/* THE CONTENTS OF THIS FILE SHOULD NOT BE CHANGED */

/* This VoidFlow already implements the FlowLike methods
 * in an easy manner, similar to haskell's base axioms.
 */
public class VoidFlow extends Flow {
	protected VoidFlow() {
		/* Exists only to defeat instantiation. */
	}

	/* Use this SingletonHolder to instantiate class only once,
	 * in a thread-safe way. The class is final by default,
	 * and the instance being final guarantees it won't change.
	 * The class loading is also thread safe, so we won't have
	 * any unwanted behavior.
	 */
	private static class SingletonHolder {
		public static final VoidFlow instance = new VoidFlow();
	}

	public static VoidFlow getInstance() {
		return SingletonHolder.instance;
	}

	@Override
	public Flow intersect(Flow a) {
		return getInstance();
	}

	@Override
	public boolean subset(Flow a) {
		return true;
	}

	@Override
	public Flow rewrite(Header h, Value v) {
		return getInstance();
	}
	@Override
	public Flow deepClone() {
		return getInstance();
	}

}
