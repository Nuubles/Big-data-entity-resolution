import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class CSVReader {
	public Dataset read(String path1) {
		Dataset set = new Dataset();

		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(path1));

			// read key line
			String line = reader.readLine();
			String[] keys = line.split(",");

			// read attribute values, one line is one entity
			while((line = reader.readLine()) != null) {
				String[] values = line.split(",");

				// remove quotes from strings
				for(int i = 0; i < keys.length; ++i) {
					if(values[i].startsWith("\n") && values[i].endsWith("\n")) {
						values[i] = values[i].substring(1, values[i].length()-1);
					}
				}

				Entity entity = new Entity(keys, values);
				set.addEntity(entity);
			}
		} catch(IOException e) {
			e.printStackTrace();
		}

		return set;
	}
}
