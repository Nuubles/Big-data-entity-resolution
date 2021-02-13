import java.util.HashMap;

public class Entity {
	private HashMap<String, String> data = new HashMap<String, String>();
	
	public Entity(String key, String value) {
		data.put(key, value);
	}
	
	public Entity(List<String> keys, List<String> values) {
		for(int i = 0; i < keys.size(); ++i) {
			data.put(keys.get(i), values.get(i));
		}
	}
	
	
	public Set<String> getAttributeKeys() {
		return data.keySet();
	}
	
	
	public String getAttribute(String key) {
		return data.get(key);
	}
}