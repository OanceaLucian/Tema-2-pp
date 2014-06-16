package network_data.header;
import java.util.LinkedList;
import java.util.List;

/* THE CONTENTS OF THIS FILE SHOULD NOT BE CHANGED */

/* The Header type equivalent from haskell, declared with "data". */
public abstract class Header {
	/* The list of all headers we'll use. */
	public static List<Header> allHeaders = new LinkedList<Header>() {{
		add(Src.getInstance()); add(Dst.getInstance()); add(Port.getInstance());
	}};

}
