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
		Dataset[] sets = project.readData(args[0], args[1]);
		project.performBlocks(sets[0], sets[1]);
	}
	
	
	public void performBlocks(Dataset set1, Dataset set2) {
		for(Blocker blocker : blockers) {
			Object o = blocker.block(set1, set2);
		}
	}
	
	
	/**
	 * @returns 2 data sets read into 2 Dataset objects
	 */
	public Dataset readData(String path1, String path2) {
		CSVReader reader = new CSVReader();
		Dataset[] datasets = new Dataset[] { reader.read(path1), reader.read(path2) };
		return datasets;
	}
}