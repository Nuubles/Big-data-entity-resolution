import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class CommonBlocksScheme extends Scheme {

	public CommonBlocksScheme() {
		super("Common Block Scheme");
	}


	@Override
	public Graph weightEdges(Graph graph) {
		HashMap<CollectionIndex, Node> nodes = graph.getNodes();
		int edgeCount = 0;
		double edgeWeights = 0;

		for(Entry<CollectionIndex, Node> entry : nodes.entrySet()) {
			Node node = entry.getValue();

			List<Edge> edgesToRemove = new ArrayList<Edge>();

			for(Edge edge : node.getEdges()) {
				// if this edge has already been weighted, do not weight again
				if(!Double.isNaN(edge.weight))
					continue;

				// neighbors of the current node
				HashSet<Node> neighbors = new HashSet<Node>(node.getNeighbors());

				// get the neighbors of the neighboring node
				// store the amount of same neighboring nodes as edge weight
				neighbors.retainAll(edge.node.getNeighbors());

				if(neighbors.size() > 0) {
					edge.weight = neighbors.size();
					edge.node.getEdge(edge.parent.getEntityId()).weight = edge.weight;
					edgeWeights += edge.weight;
					++edgeCount;
				} else {
					edgesToRemove.add(edge);
					edgesToRemove.add(edge.node.getEdge(edge.parent.getEntityId()));
				}
			}

			for(Edge e : edgesToRemove) {
				e.parent.removeEdge(e.node.getEntityId());
			}
		}

		graph.setEdgeWeightMean(edgeWeights / edgeCount);
		return graph;
	}
}
