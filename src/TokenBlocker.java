import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 * Blocks entities within the
 */
public class TokenBlocker extends Blocker {
	HashMap<CollectionIndex, CollectionIndex> resultPairs = new HashMap<CollectionIndex, CollectionIndex>();


	public TokenBlocker() {
		super("Token blocker");
	}


	@Override
	public void block(Dataset set1, Dataset set2, double jaccardSimilarity) {
		constructBasicBlocks(set1, set2);
		resultPairs = new HashMap<CollectionIndex, CollectionIndex>();

		for(List<CollectionIndex> cluster : this.entityClusters.values())
		for(int i = 0; i < cluster.size(); ++i) {
			for(int j = i+1; j < cluster.size(); ++j) {
				CollectionIndex first = cluster.get(i);
				CollectionIndex second = cluster.get(j);

				if(first.collectionIndex == second.collectionIndex)
					continue;

				if(first.collectionIndex == 0) {
					if(jaccardSimilarity <= Similarity.jaccardSimilarity(set1.getStoppedEntity(first.entityIndex), set2.getStoppedEntity(second.entityIndex))) {
						resultPairs.put(first, second);
					}
				} else {
					if(jaccardSimilarity <= Similarity.jaccardSimilarity(set2.getStoppedEntity(first.entityIndex), set1.getStoppedEntity(second.entityIndex))) {
						resultPairs.put(first,second);
					}
				}
			}
		}
	}


	/**
	 * Writes the clusters generated in block to a file
	 */
	@Override
	public void writeResults(String filePath) throws IOException {
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
}
