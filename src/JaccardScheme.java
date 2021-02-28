import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

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
			List<Edge> edgesToRemove = new ArrayList<Edge>();

			for(Edge edge : node.getEdges()) {
				// if this edge has already been weighted, do not weight again
				if(!Double.isNaN(edge.weight))
					continue;

				// neighbors of the current node
				HashSet<Node> neighbors = new HashSet<Node>(node.getNeighbors());
				double neighborSize = neighbors.size();

				// get the neighbors of the neighboring node
				// store the amount of same neighboring nodes as edge weight
				neighbors.retainAll(edge.node.getNeighbors());
				double intersectionValue = neighbors.size();

				if(intersectionValue > 0) {
					// perform jaccard operation to set the edge weight
					edge.weight = intersectionValue /
					(neighborSize + edge.node.getNeighbors().size() - intersectionValue);
					// also set the incoming edge
					edge.node.getEdge(edge.parent.getEntityId()).weight = edge.weight;
					++edgeCount;
					totalWeight += edge.weight;
				} else {
					// remove edge if weight is 0
					edgesToRemove.add(edge);
					edgesToRemove.add(edge.node.getEdge(edge.parent.getEntityId()));
				}
			}

			// remove edges with 0 weight
			for(Edge edge : edgesToRemove) {
				edge.parent.removeEdge(edge.node.getEntityId());
			}
		}

		graph.setEdgeWeightMean(totalWeight/edgeCount);
		return graph;
	}
}
