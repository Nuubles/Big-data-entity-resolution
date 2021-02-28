import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

/**
 * Blocks entities within the
 */
public class AttributeClusterBlocker extends Blocker {
	private Graph results;
	private HashMap<CollectionIndex, CollectionIndex> resultPairs;

	public AttributeClusterBlocker() {
		super("Attribute Cluster Blocker");
	}


	@Override
	public void block(Dataset set1, Dataset set2, double jaccard) {
		constructBasicBlocks(set1, set2);
		HashMap<Integer, HashSet<String>> set1ColumnStrings = getColumnStringSets(set1);
		HashMap<Integer, HashSet<String>> set2ColumnStrings = getColumnStringSets(set2);

		Graph jaccardGraph = new Graph();
		connectMatchingColumns(set1ColumnStrings, 0, set2ColumnStrings, 1, jaccardGraph);
		connectMatchingColumns(set2ColumnStrings, 1, set1ColumnStrings, 0, jaccardGraph);
		this.results = jaccardGraph;

		HashMap<CollectionIndex, CollectionIndex> pairs = new HashMap<CollectionIndex, CollectionIndex>();
		List<List<CollectionIndex>> blocks = jaccardGraph.getBlocks();

		for(List<CollectionIndex> blockCollection : blocks) {
			for(int i = 0; i < blockCollection.size(); ++i) {
				CollectionIndex firstColumn = blockCollection.get(i);

				for(int j = 0; j < blockCollection.size(); ++j) {
					CollectionIndex secondColumn = blockCollection.get(j);
					if(firstColumn.collectionIndex == secondColumn.collectionIndex)
						continue;
					// loop entities
					for(int m = 0; m < set1.size(); ++m) {
						for(int n = 0; n < set2.size(); ++n) {
							if(firstColumn.collectionIndex == 0) {
								if(set1.getStoppedEntity(m)[firstColumn.entityIndex] != null
									&& set2.getStoppedEntity(n)[secondColumn.entityIndex] != null
									&& jaccard/2 <= Similarity.jaccardSimilarity(set1.getStoppedEntity(m)[firstColumn.entityIndex], set2.getStoppedEntity(n)[secondColumn.entityIndex])) {
									pairs.put(new CollectionIndex(0, m), new CollectionIndex(1, n));
								}
							} else {
								if(set1.getStoppedEntity(n)[secondColumn.entityIndex] != null
									&& set2.getStoppedEntity(m)[firstColumn.entityIndex] != null
									&& jaccard/2 <= Similarity.jaccardSimilarity(set2.getStoppedEntity(m)[firstColumn.entityIndex], set1.getStoppedEntity(n)[secondColumn.entityIndex])) {
									pairs.put(new CollectionIndex(0, n), new CollectionIndex(1, m));
								}
							}
						}
					}
				}
			}
		}

		this.resultPairs = pairs;
	}


	private void connectMatchingColumns(
		HashMap<Integer, HashSet<String>> set1ColumnStrings,
		int setNum,
		HashMap<Integer, HashSet<String>> set2ColumnStrings,
		int setNum_,
		Graph jaccardGraph) {

		for(Entry<Integer, HashSet<String>> entry1 : set1ColumnStrings.entrySet()) {
			int highestColumn = -1;
			double highestResult = -1;

			for(Entry<Integer, HashSet<String>> entry2 : set2ColumnStrings.entrySet()) {
				HashSet<String> intersection1 = new HashSet<String>(entry1.getValue());
				HashSet<String> intersection2 = new HashSet<String>(entry2.getValue());
				intersection1.retainAll(intersection2);

				HashSet<String> union1 = new HashSet<String>(entry1.getValue());
				HashSet<String> union2 = new HashSet<String>(entry2.getValue());
				union1.addAll(union2);

				double jaccardResult = (double)intersection1.size()/union1.size();
				if(jaccardResult > highestResult) {
					highestResult = jaccardResult;
					highestColumn = entry2.getKey();
				}
			}

			CollectionIndex entry1Index = new CollectionIndex(setNum, entry1.getKey());
			CollectionIndex entry2Index = new CollectionIndex(setNum_, highestColumn);

			if(jaccardGraph.getNode(entry1Index) == null) {
				jaccardGraph.addNode(entry1Index);
			}
			if(jaccardGraph.getNode(entry2Index) == null) {
				jaccardGraph.addNode(entry2Index);
			}

			jaccardGraph.getNode(entry1Index).addEdge(jaccardGraph.getNode(entry2Index));
			jaccardGraph.getNode(entry2Index).addEdge(jaccardGraph.getNode(entry1Index));
		}
	}


	private HashMap<Integer, HashSet<String>> getColumnStringSets(Dataset set) {
		HashMap<Integer, HashSet<String>> set1ColumnStrings = new HashMap<Integer, HashSet<String>>();

		for(int i = 0; i < set.getColumnCount(); ++i) {
			HashSet<String> set1Strings = new HashSet<String>();
			set1ColumnStrings.put(i, set1Strings);

			for(int j = 0; j < set.size(); ++j) {
				String[][] stoppedEntity = set.getStoppedEntity(j);

				if(stoppedEntity[i] != null)
				for(String columnText : stoppedEntity[i]) {
					if(columnText != null)
						set1Strings.add(columnText);
				}
			}
		}
		return set1ColumnStrings;
	}


	/**
	 * Writes the clusters generated in block to a file
	 */
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
}
