package network_function;

import network_data.flow.Flow;

/* THE CONTENTS OF THIS FILE SHOULD NOT BE CHANGED */

/* A NetworkElement is an object which stands for a pair of methods
 * match and modify, as in haskell. Check Wire,Filter,Rewriter
 * on how to do this.
 */
public abstract class NetworkElement {
	/* Methods one NetworkElement needs to define in order to be
	 * a concrete NetworkElement (Wire, Filter, Rewriter).
	 */
	public abstract boolean match(Flow f);
	public abstract Flow modify(Flow f);

	/* Method that fuses together two network elements of types
	 * Wire, Filter, Rewrite.
	 *
	 * It is just like a function composition, where you apply
	 * this fused = (match_fuse, modify_fuse) only if match_fuse
	 * does match, so if both e1.match and e2.match the same flow.
	 *
	 * If so, apply e1.modify on flow, and what results is passed
	 * to e2.modify (just like function composition
	 * (f o g)(x) = f ( g ( x ) );
	 *
	 * It's already used for you in Network.getNetworkElements().
	 */
	public static NetworkElement fuse(final NetworkElement e1,
	                                  final NetworkElement e2) {
		return new NetworkElement() {
			@Override
			public Flow modify(Flow f) {
				return e2.modify(e1.modify(f));
			}
			@Override
			public boolean match(Flow f) {
				return e1.match(f) && e2.match(f);
			}
		};
	}
}
