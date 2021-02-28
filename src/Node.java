import java.util.HashMap;
import java.util.Collection;
import java.util.HashSet;

public class Node {
	private CollectionIndex entityKey;
	private HashMap<CollectionIndex, Edge> edges = new HashMap<CollectionIndex, Edge>();


	public Node(CollectionIndex entityId) {
		this.entityKey = entityId;
	}


	public CollectionIndex getEntityId() {
		return this.entityKey;
	}


	public Collection<Edge> getEdges() {
		return edges.values();
	}


	public Edge getEdge(CollectionIndex nextNode) {
		return edges.get(nextNode);
	}


	/**
	 * Adds an edge to the given node
	 * @param node node to which the directional edge points to
	 */
	public void addEdge(Node node) {
		edges.put(node.getEntityId(), new Edge(Double.NaN, node, this));
	}


	public void removeEdge(CollectionIndex index) {
		edges.remove(index);
	}


	public HashSet<Node> getNeighbors() {
		HashSet<Node> neighbors = new HashSet<Node>();
		for(Edge e : edges.values()) {
			neighbors.add(e.node);
		}
		return neighbors;
	}


	@Override
	public boolean equals(Object o) {
		if(o instanceof Node) {
			Node node = (Node)o;
			return this.entityKey.equals(node.entityKey);
		}
		return false;
	}
}

class Edge implements Comparable<Edge> {
	public Node node;
	public Node parent;
	public double weight;

	public Edge(double weight, Node node, Node parent) {
		this.weight = weight;
		this.node = node;
		this.parent = parent;
	}


	@Override
	public int compareTo(Edge o) {
		return Double.compare(weight, o.weight);
	}

	@Override
	public boolean equals(Object o) {
		if(o instanceof Edge) {
			Edge e = (Edge)o;
			return e.node.equals(node) && e.parent.equals(parent);
		}
		return false;
	}
}
