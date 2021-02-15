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
		}

		// blockers.add(new Blocker());

		Project project = new Project();
		Dataset set1 = project.readData(args[0]);
		Dataset set2 = project.readData(args[1]);
		project.performBlocks(set1, set2);
	}


	public void performBlocks(Dataset set1, Dataset set2) {
		for(Blocker blocker : blockers) {
			Object o = blocker.block(set1, set2);
		}
	}


	/**
	 * @returns a data set given from path
	 */
	public Dataset readData(String path1) {
		CSVReader reader = new CSVReader();
		Dataset dataset = reader.read(path1);
		return dataset;
	}


	public void addBlocker(Blocker blocker) {
		this.blockers.add(blocker);
	}
}
