package reachability;

/* THE CONTENTS OF THIS FILE SHOULD NOT BE CHANGED */

/* The visitor is going to update the visitable when the
 * visitable calls visit with itself in accept.
 */
public interface Visitor {
	public void visit(Visitable v);
}
