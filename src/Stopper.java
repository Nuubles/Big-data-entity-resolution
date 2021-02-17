import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Stopper {
	private List<String> stopWords;

	public Stopper(List<String> stopWords) {
		this.stopWords = stopWords;
	}


	/**
	 * Creates a new stopper from the given filepath
	 * @param filePath
	 * @return
	 * @throws IOException
	 * @throws SecurityException
	 */
	public static Stopper loadFromFile(String filePath) throws IOException, SecurityException {
		List<String> lines = Files.readAllLines(Paths.get(filePath));
		lines.remove(0);
		return new Stopper(lines);
	}


	/**
	 * Removes stop words from the tokens
	 * @param tokens
	 * @return
	 */
	public String[] stop(String[] tokens) {
		Set<String> words = new HashSet<String>(Arrays.asList(tokens));
		words.removeAll(stopWords);
		return words.toArray(new String[words.size()]);
	}
}
