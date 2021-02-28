import java.util.HashMap;
import java.util.Map.Entry;
import java.util.HashSet;

public class JaccardScheme extends Scheme {

	public JaccardScheme() {
		super("Jaccard Scheme");
	}


	@Override
	public Graph weightEdges(Graph graph) {
		HashMap<CollectionIndex, Node> nodes = graph.getNodes();
		int edgeCount = 0;
		double totalWeight = 0;

		for(Entry<CollectionIndex, Node> entry : nodes.entrySet()) {
			Node node = entry.getValue();

			for(Edge edge : node.getEdges()) {
				// if this edge has already been weighted, do not weight again
				if(!Double.isNaN(edge.weight))
					continue;

				// neighbors of the current node
				HashSet<Node> neighbors = new HashSet<Node>(node.getNeighbors());
				int neighborSize = neighbors.size();

				// get the neighbors of the neighboring node
				// store the amount of same neighboring nodes as edge weight
				neighbors.retainAll(edge.node.getNeighbors());
				double intersectionValue = neighbors.size();

				// perform jaccard operation to set the edge weight
				edge.weight = intersectionValue /
					(neighborSize + edge.node.getNeighbors().size() - intersectionValue);
				++edgeCount;
				totalWeight += edge.weight;
			}
		}

		graph.setEdgeWeightMean(totalWeight/edgeCount);
		return graph;
	}
}
