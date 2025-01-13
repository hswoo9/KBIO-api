package egovframework.com.devjitsu.model.common;

import java.util.HashMap;
import java.util.Map;


public class SearchDto {
    private Map<String, Object> data = new HashMap<>();

    public void put(String key, Object value) {
        data.put(key, value);
    }

    public Object get(String key) {
        return data.get(key);
    }

    public Map<String, Object> getAll() {
        return data;
    }

    @Override
    public String toString() {
        return "UniversalDTO{" +
                "data=" + data +
                '}';
    }
}
