import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Arrays;

public abstract class Blocker {
	protected String blockerType;
	protected HashMap<String, List<CollectionIndex>> entityClusters = new HashMap<String, List<CollectionIndex>>();

	public Blocker(String blockerType) {
		this.blockerType = blockerType;
	}


	/**
	 * Constructs basic blocks from the given sets, where each entity
	 * is stored to a block if the blocks key matches some token of
	 * the entity
	 * @param set1
	 * @param set2
	 */
	public void constructBasicBlocks(Dataset set1, Dataset set2) {
		for(int i = 0; i < set1.size(); ++i) {
			String[][] firstEntity = set1.getStoppedEntity(i);
			for(int j = 0; j < set2.size(); ++j) {
				String[][] secondEntity = set2.getStoppedEntity(j);
				compareAndStoreEntities(firstEntity, i, secondEntity, j);
				// remove empty strings from results
				entityClusters.remove("");
			}
		}
	}


	public String getBlockerType() {
		return this.blockerType;
	}


		/**
	 * Compares two entities between each other, and if any words between them
	 * match with each other, the entities will be added to a cluster with the name
	 * of the matched word
	 * @param firstEntity
	 * @param firstEntityIndex
	 * @param secondEntity
	 * @param secondEntityIndex
	 */
	protected void compareAndStoreEntities(String[][] firstEntity, int firstEntityIndex, String[][] secondEntity, int secondEntityIndex) {
		// compare the two strings on how similar they are
		for(int m = 0; m < firstEntity.length; ++m) {
			if(firstEntity[m] == null)
				continue;
			for(int n = 0; n < secondEntity.length; ++n) {
				if(secondEntity[n] == null)
					continue;
				String[] intersection = intersection(firstEntity[m], secondEntity[n]);

				for(String match : intersection) {
					// if a match is found, add the intersections of the strings to entity clusters
					storeMatchesToCluster(match, firstEntityIndex, secondEntityIndex);
				}
			}
		}
	}


	/**
	 * Saves the given entities to a cluster. If no cluster exists for the given match,
	 * a new one will be created
	 * @param match name of cluster
	 * @param firstEntityIndex index (row) for the entity in the first collection
	 * @param secondEntityIndex index (row) for the entity in the second collection
	 */
	protected void storeMatchesToCluster(String match, int firstEntityIndex, int secondEntityIndex) {
		CollectionIndex p1 = new CollectionIndex(0, firstEntityIndex + 1);
		CollectionIndex p2 = new CollectionIndex(1, secondEntityIndex + 1);

		if(entityClusters.containsKey(match)) {
			List<CollectionIndex> entities = entityClusters.get(match);
			if(!entities.contains(p1))
				entities.add(p1);
			if(!entities.contains(p2))
				entities.add(p2);
		} else {
			List<CollectionIndex> entities = new ArrayList<CollectionIndex>();
			entities.add(p1);
			entities.add(p2);
			entityClusters.put(match, entities);
		}
	}


	/**
	 * Creates an intersection from the words in the given strings
	 * @param first
	 * @param second
	 * @return words that are in both strings
	 */
	protected String[] intersection(String[] first, String[] second) {
		HashSet<String> splitFirstEntity = new HashSet<String>(Arrays.asList(first));
		HashSet<String> splitSecondEntity = new HashSet<String>(Arrays.asList(second));
		splitFirstEntity.retainAll(splitSecondEntity);
		return splitFirstEntity.toArray(new String[splitFirstEntity.size()]);
	}


	public abstract void block(Dataset set1, Dataset set2, double jaccardSimilarity);
	public abstract void writeResults(String filePath) throws IOException;
}
