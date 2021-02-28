
/**
 * Class that combines collection and enitity into a single index
 */
public class CollectionIndex {
	public int collectionIndex;
	public int entityIndex;

	public CollectionIndex(int collectionIndex, int entityIndex) {
		this.collectionIndex = collectionIndex;
		this.entityIndex = entityIndex;
	}


	@Override
	public boolean equals(Object o) {
		if(o instanceof CollectionIndex) {
			CollectionIndex p = (CollectionIndex)o;
			return p.collectionIndex == collectionIndex && p.entityIndex == entityIndex;
		}
		return false;
	}


    @Override
    public int hashCode() {
        int result = (int) (collectionIndex ^ (collectionIndex >>> 32));
        result = 31 * result + Integer.hashCode(entityIndex);
        return result;
    }
}
