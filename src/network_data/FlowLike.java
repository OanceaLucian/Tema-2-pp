package network_data;

import network_data.header.Header;
import network_data.value.Value;

/* THE CONTENTS OF THIS FILE SHOULD NOT BE CHANGED */

/* Type class to which Flow and CompactFlow must be enrolled.
 * It defines the three major operations that one must be able to perform
 * on flows.
 *
 * This is the class FlowLike equivalent from Haskell.
 * You can see that the "class" keyword from haskell is actually
 * used with interface in Java, while "data" from haskell is used
 * with (abstract) class in Java.
 */
public interface FlowLike<T> {
	public T intersect(T a);
	public boolean subset(T a);
	public T rewrite(Header h, Value v);
	public T deepClone();
}
