import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CSVReader {

	/**
	 * Reads a given file from path into a data set
	 * @param filePath path to .csv file
	 * @param stopper stopper used to remove stop words from read results
	 * @return csv converted to dataset
	 */
	public Dataset read(String filePath, Stopper stopper) throws IOException {
		BufferedReader reader;
		Dataset set = null;

		int lineCount = getLineCount(filePath);
		if(lineCount <= 0)
			return null;

		reader = new BufferedReader(new FileReader(filePath));

		// read key line
		String line = reader.readLine();
		String[] keys = line.split(",");

		// remove ID column from data set keys
		int idColumnIndex = getIdColumnIndex(keys);
		int index = 0;
		for(int i = 0; i < keys.length; ++i) {
			if(i != idColumnIndex)
				keys[index++] = keys[i];
		}

		set = new Dataset(keys, lineCount-1, stopper);
		index = 0;

		// read attribute values, one line is one entity
		while((line = reader.readLine()) != null) {
			String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
			removeQuotes(values);

			// add entity to dataset, remove column with ID
			int columnIndex = 0;
			for(int i = 0; i < values.length; ++i)
			if(i != idColumnIndex)
				set.setToken(columnIndex++, index, values[i]);
			++index;
		}
		reader.close();

		return set;
	}


	/**
	 * Removes quotes from a string
	 * @param text
	 * @return
	 */
	private String removeQuotes(String text) {
		if(text.startsWith("\"") && text.endsWith("\""))
			return text.substring(1, text.length()-1);
		return text;
	}


	/**
	 * Removes quotes from the strings in array
	 * @param texts array to remove the quotes from
	 */
	private void removeQuotes(String[] texts) {
		for(int i = 0; i < texts.length; ++i) {
			texts[i] = removeQuotes(texts[i]);
		}
	}


	/**
	 * Checks all columns and checks if the string equals "id"
	 * @param columns
	 * @return index of column with id, or -1 if none found
	 */
	private int getIdColumnIndex(String[] columns) {
		for(int i = 0; i < columns.length; ++i) {
			if(columns[i].equals("id")) {
				return i;
			}
		}

		return -1;
	}


	private int getLineCount(String file) throws IOException {
		BufferedReader reader = new BufferedReader(new FileReader(file));
		int lines = 0;
		String line;
		while ((line = reader.readLine()) != null && line.trim().length() > 0)
			++lines;
		reader.close();
		return lines;
	}
}
