import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

public class MetaBlocker extends Blocker {
	private Pruner pruner;
	private Scheme scheme;
	private Graph results;
	HashMap<CollectionIndex, CollectionIndex> resultPairs = new HashMap<CollectionIndex, CollectionIndex>();

	public MetaBlocker(Scheme scheme, Pruner pruner) {
		super("Meta blocker");
		this.pruner = pruner;
		this.scheme = scheme;
		this.results = null;
	}


	@Override
	public void block(Dataset set1, Dataset set2, double jaccardSimilarity) {
		Graph graph = new Graph();
		this.constructBasicBlocks(set1, set2);
		graph.addBlocks(entityClusters);

		// add weights to the edges in the graph
		this.scheme.weightEdges(graph);
		// make the graph discard edges with the edge weight mean
		// if the pruner uses global discard levels
		this.pruner.setGlobalDiscordLevel(graph.getEdgeWeightMean());
		// prune the edges in the graph
		this.pruner.prune(graph);
		//System.out.println(7);
		this.results = graph;

		HashMap<CollectionIndex, CollectionIndex> pairs = new HashMap<CollectionIndex, CollectionIndex>();

		for(List<CollectionIndex> block : results.getBlocks()) {
			for(int i = 0; i < block.size(); ++i) {
				for(int j = i+1; j < block.size(); ++j) {
					CollectionIndex first = block.get(i);
					CollectionIndex second = block.get(j);
					// if the elements are in the same collection, skip them
					if(first.collectionIndex == second.collectionIndex)
						continue;

					String[][] firstEntity;
					String[][] secondEntity;

					if(first.collectionIndex == 0) {
						firstEntity = set1.getStoppedEntity(first.entityIndex);
						secondEntity = set2.getStoppedEntity(second.entityIndex);
					} else {
						firstEntity = set2.getStoppedEntity(first.entityIndex);
						secondEntity = set1.getStoppedEntity(second.entityIndex);
					}

					if(jaccardSimilarity <= Similarity.jaccardSimilarity(firstEntity, secondEntity)) {
						if(first.collectionIndex == 0)
							pairs.put(first, second);
						else
							pairs.put(second, first);
					}
				}
			}
		}

		this.resultPairs = pairs;
	}


	@Override
	public void writeResults(String filePath) throws IOException {
		if(results == null) {
			System.out.println("No graph generated yet, thus there are no results in " + getBlockerType() + ".");
			return;
		}

		File file = new File(filePath);
		file.createNewFile();
		FileOutputStream stream = new FileOutputStream(file);
		stream.write("\"collection-entity\",\"collection-entity\"\n".getBytes());

		// iterate each tree/block in the graph
		for(Entry<CollectionIndex, CollectionIndex> resultPair : this.resultPairs.entrySet()) {
			// iterate each entity in block
			stream.write(String.format("%d-%d", resultPair.getKey().collectionIndex, resultPair.getKey().entityIndex).getBytes());
			stream.write(String.format(",%d-%d\n", resultPair.getValue().collectionIndex, resultPair.getValue().entityIndex).getBytes());
		}

		stream.close();
	}


	@Override
	public String getBlockerType() {
		return super.getBlockerType() + " with " + scheme.getName() + " using " + pruner.getName();
	}
}
