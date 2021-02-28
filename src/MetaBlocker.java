import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class MetaBlocker extends Blocker {
	private Pruner pruner;
	private Scheme scheme;
	private Graph results;

	public MetaBlocker(Scheme scheme, Pruner pruner) {
		super("Meta blocker");
		this.pruner = pruner;
		this.scheme = scheme;
		this.results = null;
	}


	@Override
	public void block(Dataset set1, Dataset set2) {
		Graph graph = new Graph();
		this.constructBasicBlocks(set1, set2);
		graph.addBlocks(entityClusters);

		// construct graph nodes and edges
		/*System.out.println(1);
		for(int i = 0; i < set1.size(); ++i) {
			String[][] stoppedEntity = set1.getStoppedEntity(i);
			graph.addEntity(stoppedEntity, new CollectionIndex(0, i));
		}
		System.out.println(2);
		for(int i = 0; i < set2.size(); ++i) {
			String[][] stoppedEntity = set2.getStoppedEntity(i);
			graph.addEntity(stoppedEntity, new CollectionIndex(1, i));
		}*/
		//System.out.println(3);
		// add weights to the edges in the graph
		this.scheme.weightEdges(graph);
		//System.out.println(4);
		// make the graph discard edges with the edge weight mean
		// if the pruner uses global discard levels
		this.pruner.setGlobalDiscordLevel(graph.getEdgeWeightMean());
		//System.out.println(5);
		if(pruner instanceof CardinalityNodePruner) {
			this.pruner.setGlobalDiscordLevel(set1.size() + set2.size());
		}
		//System.out.println(6);
		// prune the edges in the graph
		this.pruner.prune(graph);
		//System.out.println(7);
		this.results = graph;
	}


	@Override
	public void writeResults(String filePath) throws IOException {
		if(results == null) {
			System.out.println("No graph generated yet, thus there are no results in " + getBlockerType() + ".");
			return;
		}

		List<List<CollectionIndex>> blocks = results.getBlocks();
		System.out.println("Blocks size: " + blocks.size());

		File file = new File(filePath);
		file.createNewFile();
		FileOutputStream stream = new FileOutputStream(file);
		stream.write("\"block\",\"collection\",\"entity\"\n".getBytes());
		int blockIndex = 0;

		// iterate each tree/block in the graph
		for(List<CollectionIndex> block : blocks) {
			// iterate each entity in block
			for(CollectionIndex i : block) {
				stream.write(Integer.toString(blockIndex).getBytes());
				stream.write(String.format(",%d", i.collectionIndex).getBytes());
				stream.write(String.format(",%d\n", i.entityIndex).getBytes());
			}
			++blockIndex;
		}

		stream.close();
	}


	@Override
	public String getBlockerType() {
		return super.getBlockerType() + " with " + scheme.getName() + " using " + pruner.getName();
	}
}
