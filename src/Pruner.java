
public abstract class Pruner {
	private String name;
	private double discardLevel;

	public Pruner(String name) {
		this.name = name;
	}


	public String getName() {
		return this.name;
	}


	public void setGlobalDiscordLevel(double discardLevel) {
		this.discardLevel = discardLevel;
	}


	public double getGlobalDiscardLevel() {
		return this.discardLevel;
	}


	public abstract void prune(Graph graph);
}
