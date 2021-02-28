import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.ArrayList;
import java.util.Collection;

public class Graph {
	private double weightMean;
	// map<collection, map<entityIndex, entity>>
	private HashMap<CollectionIndex, Node> nodes = new HashMap<CollectionIndex, Node>();
	// map<token, map<collection, entity>>
	//private HashMap<String, HashMap<Integer, List<Node>>> entityTokens = new HashMap<String, HashMap<Integer, List<Node>>>();

	/**
	 * Adds an entity into the graph, and creates edges between other nodes if they
	 * share tokens. The edges have NaN weights by default
	 * @param entity
	 * @param entityId
	 */
	/*public void addEntity(String[][] stoppedEntity, CollectionIndex entityId) {
		if(!nodes.containsKey(entityId.collectionIndex))
			nodes.put(entityId.collectionIndex, new HashMap<Integer, Node>());

		Node node = new Node(entityId);
		// add the node to the node map
		nodes.get(entityId.collectionIndex).put(entityId.entityIndex, node);

		for(String[] tokens : stoppedEntity) {
			for(String token : tokens) {
				if(!entityTokens.containsKey(token))
					entityTokens.put(token, new HashMap<Integer, List<Node>>());

				HashMap<Integer, List<Node>> entityMap = entityTokens.get(token);

				// some nodes with this token already exist, add an edge
				// between them
				for(Entry<Integer, List<Node>> entry : entityMap.entrySet()) {
					// the entities belong to the same collection, no need to add an edge between them
					if(entry.getKey() == node.getEntityId().collectionIndex)
						continue;

					for(Node n : entry.getValue()) {
						n.addEdge(node);
						node.addEdge(n);
					}
				}

				// add the created node to the entitytoken collection
				if(entityMap.containsKey(entityId.collectionIndex)) {
					entityMap.get(entityId.collectionIndex).add(node);
				} else {
					List<Node> nodeList = new ArrayList<Node>();
					nodeList.add(node);
					entityMap.put(entityId.collectionIndex, nodeList);
				}
			}
		}
	}*/


	public void addBlocks(HashMap<String, List<CollectionIndex>> clusters) {
		for(Entry<String, List<CollectionIndex>> cluster : clusters.entrySet()) {
			List<CollectionIndex> matchingEntities = cluster.getValue();

			// add the nodes to the node map
			for(CollectionIndex index : matchingEntities)
			for(CollectionIndex index_ : matchingEntities) {
				Node node = null;
				if(!nodes.containsKey(index_)) {
					node = new Node(index_);
					nodes.put(index_, node);
				} else {
					node = nodes.get(index_);
				}

				if(!index.equals(index_) && nodes.get(index).getEdge(node.getEntityId()) == null) {
					Node connectNode = nodes.get(index);
					connectNode.addEdge(node);
				}
			}
		}
	}


	/**
	 * Returns a map of all nodes in graph.
	 * The outer map divides the nodes into different collections,
	 * and the inner map serves as a way to access the nodes quickly
	 * by using their entity index.
	 * The nodes have edges created between them, but by default they have
	 * the weight of 0 unless they have been weighted
	 * @return graph nodes divided into collections
	 */
	public HashMap<CollectionIndex, Node> getNodes() {
		return this.nodes;
	}


	public double getEdgeWeightMean() {
		return weightMean;
	}


	public void setEdgeWeightMean(double newMean) {
		this.weightMean = newMean;
	}


	/**
	 * Converts the graph into a blocks
	 * @return
	 */
	public List<List<CollectionIndex>> getBlocks() {
		List<List<CollectionIndex>> blocks = new ArrayList<List<CollectionIndex>>();
		HashSet<CollectionIndex> looped = new HashSet<CollectionIndex>();

		// loop each node in graph
		for(Node node : nodes.values()) {
			// if this node has already been looped, no need to loop again
			if(looped.contains(node.getEntityId()))
				continue;

			List<CollectionIndex> block = new ArrayList<CollectionIndex>();

			LinkedList<Node> queue = new LinkedList<Node>();
			queue.add(node);
			// run through all the neighbors of this node to get the tree
			while(!queue.isEmpty()) {
				Node n = queue.pop();
				if(looped.contains(n.getEntityId()))
					continue;

				// add the current node to the tree
				block.add(n.getEntityId());
				looped.add(n.getEntityId());
				//System.out.println(n.getEntityId().collectionIndex + " " + n.getEntityId().entityIndex);

				HashSet<Node> neigh = n.getNeighbors();
				neigh.removeAll(queue);
				queue.addAll(neigh);
			}

			blocks.add(block);
		}

		return blocks;
	}
}
