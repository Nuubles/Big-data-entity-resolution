import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class Dataset implements Iterable<Entity> {
	private List<Entity> entities = new ArrayList<Entity>();

	public void addEntity(Entity entity) {
		entities.add(entity);
	}


	@Override
	public Iterator<Entity> iterator() {
		return entities.iterator();
	}
}
