import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;

/**
 * Blocks entities within the
 */
public class TokenBlocker extends Blocker {
	private HashMap<String, List<Pair>> entityClusters = new HashMap<String, List<Pair>>();

	public TokenBlocker() {
		super("Token blocker");
	}


	@Override
	public void block(Dataset set1, Dataset set2) {
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


	/**
	 * Compares two entities between each other, and if any words between them
	 * match with each other, the entities will be added to a cluster with the name
	 * of the matched word
	 * @param firstEntity
	 * @param firstEntityIndex
	 * @param secondEntity
	 * @param secondEntityIndex
	 */
	private void compareAndStoreEntities(String[][] firstEntity, int firstEntityIndex, String[][] secondEntity, int secondEntityIndex) {
		// compare the two strings on how similar they are
		for(int m = 0; m < firstEntity.length; ++m) {
			for(int n = 0; n < secondEntity.length; ++n) {

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
	private void storeMatchesToCluster(String match, int firstEntityIndex, int secondEntityIndex) {
		Pair p1 = new Pair(0, firstEntityIndex + 1);
		Pair p2 = new Pair(1, secondEntityIndex + 1);

		if(entityClusters.containsKey(match)) {
			List<Pair> entities = entityClusters.get(match);
			if(!entities.contains(p1))
				entities.add(p1);
			if(!entities.contains(p2))
				entities.add(p2);
		} else {
			List<Pair> entities = new ArrayList<Pair>();
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
	private String[] intersection(String[] first, String[] second) {
		HashSet<String> splitFirstEntity = new HashSet<String>(Arrays.asList(first));
		HashSet<String> splitSecondEntity = new HashSet<String>(Arrays.asList(second));
		splitFirstEntity.retainAll(splitSecondEntity);
		return splitFirstEntity.toArray(new String[splitFirstEntity.size()]);
	}


	/**
	 * Writes the clusters generated in block to a file
	 */
	@Override
	public void writeResults(String filePath) throws IOException {
		File file = new File(filePath);
		file.createNewFile();
		FileOutputStream stream = new FileOutputStream(file);

		stream.write("\"block\",\"collection\",\"entity\"\n".getBytes());
		for(Entry<String, List<Pair>> entry : this.entityClusters.entrySet()) {
			for(Pair pair : entry.getValue()) {
				stream.write(String.format("\"%s\"", entry.getKey()).getBytes());
				stream.write(String.format(",\"%s\"", pair.collectionIndex).getBytes());
				stream.write(String.format(",\"%s\"\n", pair.entityIndex).getBytes());
			}
		}

		stream.close();
	}


	/**
	 * Class used to store collection - entity pairs into a list
	 */
	private class Pair {
		public final int collectionIndex;
		public final int entityIndex;


		public Pair(int collectionIndex, int entityIndex) {
			this.collectionIndex = collectionIndex;
			this.entityIndex = entityIndex;
		}


		@Override
		public boolean equals(Object o) {
			if(o instanceof Pair) {
				Pair p = (Pair)o;
				return p.collectionIndex == collectionIndex && p.entityIndex == entityIndex;
			}
			return false;
		}
	}
}
