package org.example.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Cache;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DocumentCache {
    private final Cache<String, String> cache;
    private static DocumentCache instance;
    private DocumentCache() {
        cache = Caffeine.newBuilder()
                .maximumSize(100) // Maximum number of documents to cache
                .build();

    }

    public static DocumentCache getInstance() {
        if (instance != null) {
            return instance;
        }
        synchronized (DocumentCache.class) {
            if (instance == null) {
                instance = new DocumentCache();
            }
            return instance;
        }
    }

    public String getDocument(String dbName, String collectionName, String docName) {
        String key = generateKey(dbName, collectionName, docName);
        return cache.getIfPresent(key);
    }

    public void putDocument(String dbName, String collectionName, String docName, JSONObject document) {
        String key = generateKey(dbName, collectionName, docName);
        cache.put(key, document.toString());
    }

    public void removeDocument(String dbName, String collectionName, String docName) {
        String key = generateKey(dbName, collectionName, docName);
        cache.invalidate(key);
    }
    public void removeAllDocumentsForDatabase(String databaseName) {
        List<String> keysToRemove = new ArrayList<>();
        for (String key : cache.asMap().keySet()) {
            if (key.startsWith(databaseName + ",")) {
                keysToRemove.add(key);
            }
        }
        cache.invalidateAll(keysToRemove);
    }
    public void removeAllDocumentsForCollection(String databaseName,String collectionName) {
        List<String> keysToRemove = new ArrayList<>();
        for (String key : cache.asMap().keySet()) {
            if (key.startsWith(databaseName + ","+collectionName+",")) {
                keysToRemove.add(key);
            }
        }
        cache.invalidateAll(keysToRemove);
    }
    private String generateKey(String dbName, String collectionName, String docName) {
        return dbName + "," + collectionName + "," + docName;
    }
}
