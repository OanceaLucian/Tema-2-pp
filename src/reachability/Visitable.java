package reachability;

/* THE CONTENTS OF THIS FILE SHOULD NOT BE CHANGED */

/* The visitable is going to call visit on itself in accept. */
public interface Visitable {
	public void accept(Visitor v);
}
