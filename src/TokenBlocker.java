import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map.Entry;

/**
 * Blocks entities within the
 */
public class TokenBlocker extends Blocker {

	public TokenBlocker() {
		super("Token blocker");
	}


	@Override
	public void block(Dataset set1, Dataset set2) {
		constructBasicBlocks(set1, set2);
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
		for(Entry<String, List<CollectionIndex>> entry : this.entityClusters.entrySet()) {
			for(CollectionIndex pair : entry.getValue()) {
				stream.write(String.format("\"%s\"", entry.getKey()).getBytes());
				stream.write(String.format(",\"%s\"", pair.collectionIndex).getBytes());
				stream.write(String.format(",\"%s\"\n", pair.entityIndex).getBytes());
			}
		}

		stream.close();
	}
}
