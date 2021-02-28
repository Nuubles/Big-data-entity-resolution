import java.util.ArrayList;
import java.util.PriorityQueue;

public class CardinalityNodePruner extends Pruner {

	public CardinalityNodePruner() {
		super("Cardinality pruner");
	}


	@Override
	public void prune(Graph graph) {
		// sort all the edges in the node
		PriorityQueue<Edge> queue = new PriorityQueue<Edge>();

		for(Node node : graph.getNodes().values()) {
			double limit = node.getEdges().size() * 0.5;

			ArrayList<Edge> list = new ArrayList<Edge>(node.getEdges());
			for(int i = 0; i < list.size(); ++i) {
				Edge edge = list.get(i);
				queue.add(edge);

				if(queue.size() > limit) {
					Edge e = queue.poll();
					e.parent.removeEdge(e.node.getEntityId());
				}
			}

			queue.clear();
		}
	}
}
