
public abstract class Scheme {
	private String name;

	public Scheme(String name) {
		this.name = name;
	}


	public String getName() {
		return this.name;
	}


	public abstract Graph weightEdges(Graph graph);
}
