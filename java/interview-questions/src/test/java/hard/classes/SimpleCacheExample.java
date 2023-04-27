package hard.classes;

import java.util.Map;

public class SimpleCacheExample<K, V> {
    private final Map<K, V> storage;

    public SimpleCacheExample(Map<K, V> storage) {
        this.storage = storage;
    }

    public void save(K key, V val) {
        storage.put(key, val);
    }

    public V get(K key) {
        return storage.get(key);
    }

    public int size() {
        return storage.size();
    }

}
