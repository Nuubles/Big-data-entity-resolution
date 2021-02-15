import java.util.ArrayList;
import java.util.List;

public class Dataset implements Iterable<Entity> {
	public List<Entity> entities = new ArrayList<Entity>();

	public void addEntity(Entity entity) {
		entities.add(entity);
	}


	@Override
	public Iterator<Entity> iterator() {
		return entities.Iterator();
	}
}
