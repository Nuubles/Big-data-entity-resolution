import java.util.HashMap;

public class Entity {
	private HashMap<String, String> data = new HashMap<String, String>();
	
	public Entity(String key, String value) {
		data.put(key, value);
	}
	
	public Entity(String[] keys, String[] values) {
		for(int i = 0; i < keys.length; ++i) {
			data.put(keys[i], values[i]);
		}
	}
	
	
	public Set<String> getAttributeKeys() {
		return data.keySet();
	}
	
	
	public String getAttribute(String key) {
		return data.get(key);
	}
}
