import java.util.Iterator;

public class Dataset {
	// tokens in the format of [entity][attribute]
	private String[][][] stoppedTokens;
	private String[][] tokens;
	private String[] attributes;
	private Stopper stopper;

	public Dataset(String[] attributes, int length, Stopper stopper) {
		this.stopper = stopper;
		this.attributes = attributes;
		tokens = new String[length][attributes.length];
		stoppedTokens = new String[length][attributes.length][];
	}


	/**
	 * Sets token to the given entity
	 * @param attribute
	 * @param entity index of entity. With CSV files the entity index is line of entity - 2.
	 * When starting from 0 the CSV format index is line - 1
	 * @param token
	 */
	public void setToken(int attribute, int entity, String token) {
		this.tokens[entity][attribute] = token;
		String[] splitted = split(token.toLowerCase());
		for(int i = 0; i < splitted.length; ++i)
		if(stopper != null)
			this.stoppedTokens[entity][attribute] = stopper.stop(splitted);
		else
			this.stoppedTokens[entity][attribute] = splitted;
	}


	/**
	 * Sets the entity to a given index
	 * Index in CSV format is line - 2, starting from 1.
	 * When starting from 0 the CSV format index is line - 1
	 * @param entity
	 * @param tokens
	 * @return false if given entity is invalid
	 */
	public boolean setEntity(int entity, String[] tokens) {
		if(tokens.length != attributes.length)
			return false;
		this.tokens[entity] = tokens;

		for(int i = 0; i < tokens.length; ++i) {
			String[] splitted = split(tokens[i].toLowerCase());
			if(stopper != null)
				this.stoppedTokens[entity][i] = stopper.stop(splitted);
			else
				this.stoppedTokens[entity][i] = splitted;
		}
		return true;
	}


	/**
	 * Splits the given string into tokens
	 * @param token
	 * @return
	 */
	private String[] split(String token) {
		return token.split(":\\s|,\\s|,|\\.\\s|\\s");
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


	public String[][] getStoppedEntity(int entity) {
		return stoppedTokens[entity];
	}


	public Iterator<String[]> getSetIterator() {
		DatasetIterator iter = new DatasetIterator(this);
		return iter;
	}


	public Iterator<String[][]> getStoppedSetIterator() {
		StoppedDatasetIterator iter = new StoppedDatasetIterator(this);
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


	/**
	 * Iterator to iterate over the entities in a stopped dataset
	 */
	class StoppedDatasetIterator implements Iterator<String[][]> {
		private int index = 0;
		private Dataset set;

		public StoppedDatasetIterator(Dataset set) {
			this.set = set;
		}


		@Override
		public boolean hasNext() {
			return set.stoppedTokens.length != index;
		}


		@Override
		public String[][] next() {
			return set.stoppedTokens[index++];
		}
	}
}
