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
			String[] firstEntity = set1.getEntity(i);

			for(int j = 0; j < set2.size(); ++j) {
				String[] secondEntity = set2.getEntity(j);

				// compare the two strings on how similar they are
				for(int m = 0; m < firstEntity.length; ++m) {
					for(int n = 0; n < secondEntity.length; ++n) {

						// if similarity score is over 0.65, we determine the entities to be referring to the same entity
						String[] intersection = intersection(firstEntity[m].toLowerCase(), secondEntity[n].toLowerCase());
						for(String match : intersection) {
							// if a match is found, add the intersections of the strings to entity clusters
							Pair p1 = new Pair(0, i);
							Pair p2 = new Pair(1, j);

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
					}
				}
			}
		}
	}


	/**
	 * Creates an intersection from the words in the given strings
	 * @param first
	 * @param second
	 * @return words that are in both strings
	 */
	private String[] intersection(String first, String second) {
		HashSet<String> splitFirstEntity = new HashSet<String>(Arrays.asList(first.replace(".", "").split(",\\s|,|\\s")));
		HashSet<String> splitSecondEntity = new HashSet<String>(Arrays.asList(second.replace(".", "").split(",\\s|,|\\s")));
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
