package org.example.indexer;

import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Indexer implements Serializable {
    private final String reference;
    private final ConcurrentHashMap<String, ConcurrentHashMap<String, ArrayList<String>>> index;

    public Indexer(String reference) {
        this.reference = reference;
        this.index = new ConcurrentHashMap<>();
    }

    public void addToIndexer(JSONObject jsonObject, String attribute) {
        String attributeValue = String.valueOf(jsonObject.get(attribute));

        ConcurrentHashMap<String, ArrayList<String>> subIndex = getIndex(attribute);
        subIndex.putIfAbsent(attributeValue, new ArrayList<>());
        ArrayList<String> objectIds = subIndex.get(attributeValue);
        objectIds.add(String.valueOf(jsonObject.get("id")));
    }

    public void removeFromIndexer(JSONObject jsonObject, String attribute) {
        String attributeValue = String.valueOf(jsonObject.get(attribute));

        ConcurrentHashMap<String, ArrayList<String>> subIndex = getIndex(attribute);
        if (subIndex.containsKey(attributeValue)) {
            ArrayList<String> objectIds = subIndex.get(attributeValue);
            objectIds.remove(String.valueOf(jsonObject.get("id")));
            if (objectIds.isEmpty()) {
                subIndex.remove(attributeValue);
            }
        }

    }

    public List<String> getIdsFromIndex(String attribute, String value) {
        ConcurrentHashMap<String, ArrayList<String>> subIndex = getIndex(attribute);
        return subIndex.getOrDefault(value, new ArrayList<>());
    }

    public boolean hasAttribute(String attribute) {
        return index.containsKey(attribute);
    }

    private ConcurrentHashMap<String, ArrayList<String>> getIndex(String attribute) {
        return index.computeIfAbsent(attribute, k -> new ConcurrentHashMap<>());
    }

    @Override
    public String toString() {
        return "Indexer{" +
                "reference='" + reference + '\'' +
                ", index=" + index +
                '}';
    }
}
