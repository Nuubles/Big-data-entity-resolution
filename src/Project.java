import java.io.IOException;
import java.util.ArrayList;

public class Project {
	private ArrayList<Blocker> blockers = new ArrayList<Blocker>();

	/**
	 * @param args contains 2 input strings which refer to the
	 * csv files containing the data to be blocked
	 */
	public static void main(String[] args) {
		if(args.length != 2) {
			System.out.println("Please give paths to 2 files containing the data to be blocked when booting the program.");
			return;
		}

		// Initialize blockers
		Project project = new Project();
		project.addBlocker(new TokenBlocker());

		// Read data from CSV files
		Dataset set1 = project.readData(args[0]);
		if(set1 == null) {
			System.out.println(args[0] + " is an invalid file, see error output for details.");
			return;
		}

		Dataset set2 = project.readData(args[1]);
		if(set2 == null) {
			System.out.println(args[1] + " is an invalid file, see error output for details.");
			return;
		}

		// Performs blocks for the two given data sets
		project.performBlocks(set1, set2);
	}


	/**
	 * Runs all blockers with the two given data sets
	 * @param set1
	 * @param set2
	 */
	public void performBlocks(Dataset set1, Dataset set2) {
		for(Blocker blocker : blockers) {
			System.out.println("Running blocker " + blocker.getBlockerType() + "...");
			blocker.block(set1, set2);
			try {
				String resultsFile = ".\\" + blocker.getBlockerType() + ".csv";
				System.out.println("Writing blocker " + blocker.getBlockerType() + " results to " + resultsFile);
				blocker.writeResults(resultsFile);
				System.out.println("Finished writing.");
			} catch(IOException e) {
				e.printStackTrace();
				System.out.println("Failed to write output results for blocker " + blocker.getBlockerType() + ", please clean previous results and try again.");
			}
		}
		System.out.println("All blockers have been run.");
	}


	/**
	 * @returns a data set given from path
	 */
	public Dataset readData(String path1) {
		CSVReader reader = new CSVReader();
		Stopper stopper = null;
		try {
			stopper = Stopper.loadFromFile("english_stopwords.txt");
		} catch(IOException | SecurityException e) {
			e.printStackTrace();
			System.out.println("Failed to load stop words, no word stopping will be used.");
		}
		Dataset dataset = reader.read(path1, stopper);
		return dataset;
	}


	/**
	 * Add a new blocker to be run when the project is run
	 * @param blocker blocker that should run
	 */
	public void addBlocker(Blocker blocker) {
		this.blockers.add(blocker);
	}
}
