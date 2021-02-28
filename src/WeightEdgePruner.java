import java.util.ArrayList;
import java.util.List;

public class WeightEdgePruner extends Pruner {

	public WeightEdgePruner() {
		super("Weight edge pruner");
	}


	@Override
	public void prune(Graph graph) {
		// remove all edges that have a lower edge weight than the minimum edge weight
		for(Node node : graph.getNodes().values()) {
			List<CollectionIndex> valuesToRemove = new ArrayList<CollectionIndex>();

			for(Edge edge : node.getEdges()) {
				if(edge.weight < this.getGlobalDiscardLevel()) {
					valuesToRemove.add(edge.node.getEntityId());
				}
			}

			for(CollectionIndex i : valuesToRemove)
				node.removeEdge(i);
		}
	}
}
