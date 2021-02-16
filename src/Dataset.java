import java.util.Iterator;

public class Dataset implements Iterable<String[]> {
	// tokens in the format of [entity][attribute]
	private String[][] tokens;
	private String[] attributes;

	public Dataset(String[] attributes, int length) {
		this.attributes = attributes;
		tokens = new String[length][attributes.length];
	}


	public void setToken(int attribute, int entity, String token) {
		this.tokens[entity][attribute] = token;
	}


	/**
	 * Sets the entity to a given index
	 * @param entity
	 * @param tokens
	 * @return false if given entity is invalid
	 */
	public boolean setEntity(int entity, String[] tokens) {
		if(tokens.length != attributes.length)
			return false;
		this.tokens[entity] = tokens;
		return true;
	}


	public int size() {
		return tokens.length;
	}


	public String getAttributeName(int attribute) {
		return attributes[attribute];
	}


	public String[] getEntity(int entity) {
		return tokens[entity];
	}


	@Override
	public Iterator<String[]> iterator() {
		DatasetIterator iter = new DatasetIterator(this);
		return iter;
	}

	/**
	 * Iterator to iterate over the entities in a dataset
	 */
	class DatasetIterator implements Iterator<String[]> {
		private int index = 0;
		private Dataset set;

		public DatasetIterator(Dataset set) {
			this.set = set;
		}


		@Override
		public boolean hasNext() {
			return set.tokens.length != index;
		}


		@Override
		public String[] next() {
			return set.tokens[index++];
		}
	}
}
