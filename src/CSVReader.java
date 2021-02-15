import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CSVReader {
	/**
	 * Reads a given file from path into a data set
	 * @param path1 path to .csv file
	 * @return csv converted to dataset
	 */
	public Dataset read(String filePath) {
		BufferedReader reader;
		Dataset set = null;

		int lineCount = getLineCount(filePath);
		if(lineCount <= 0)
			return null;

		try {
			reader = new BufferedReader(new FileReader(filePath));

			// read key line
			String line = reader.readLine();
			String[] keys = line.split(",");
			set = new Dataset(keys, lineCount-1);
			int index = 0;

			// read attribute values, one line is one entity
			while((line = reader.readLine()) != null) {
				String[] values = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

				// remove quotes from strings
				for(int i = 0; i < keys.length; ++i) {
					if(values[i].startsWith("\"") && values[i].endsWith("\"")) {
						values[i] = values[i].substring(1, values[i].length()-1);
					}
				}

				set.setEntity(index++, values);
			}
		} catch(IOException e) {
			e.printStackTrace();
		}

		return set;
	}


	private int getLineCount(String file) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			int lines = 0;
			String line;
			while ((line = reader.readLine()) != null && line.trim().length() > 0)
				++lines;
			reader.close();
			return lines;
		} catch(IOException e) {
			e.printStackTrace();
		}
		return -1;
	}
}
