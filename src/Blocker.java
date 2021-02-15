import java.io.IOException;

public abstract class Blocker {
	protected String blockerType;


	public Blocker(String blockerType) {
		this.blockerType = blockerType;
	}


	public String getBlockerType() {
		return this.blockerType;
	}


	public abstract void block(Dataset set1, Dataset set2);
	public abstract void writeResults(String filePath) throws IOException;
}
