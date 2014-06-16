package network_function;

/* THE CONTENTS OF THIS FILE SHOULD NOT BE CHANGED */

/* Equivalent to "class Element" from haskell which had a similar method.
 * Every type belonging to this class should be able to produce a pair of
 * Match and Modify functions, to express its logic.
 */
public interface Element<T> {
	public NetworkElement getMatchAndModify(T param);
}
