package org.example.indexer;

import org.example.disk.CollectionOperration;
import org.example.models.Query;
import org.example.models.Result;
import org.example.models.Status;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.example.disk.DiskUtils.collectionExists;

public class IndexerManager {
    private static IndexerManager instance;
    private static Map<String, Indexer> indexers;
    private IndexerManager() {
        indexers = IndexerDiskOperation.loadIndexers();
    }

    public Result creatIndex(Query query) throws IOException {
        Result result = new Result();
        result.setStatus(Status.ERROR);
        String databaseName = query.getDatabaseName();
        String collectionName = query.getCollectionName();
        result.setMessage("There is no such collection");
        if (!collectionExists(query.getDatabaseName(), query.getCollectionName()))
            return result;
        String attribute = query.getAttribute();
        String reference = databaseName + "," + collectionName;
        indexers.putIfAbsent(reference, new Indexer(reference));
        Indexer indexer = indexers.get(reference);
        result.setMessage("The index is already exist");
        if (indexer.hasAttribute(attribute))
            return result;
        JSONArray jsonArray = CollectionOperration.readCollection(databaseName, collectionName);
        boolean indexerCreated=false;
        for (Object obj : jsonArray) {
            if (obj instanceof JSONObject jsonObject) {
                if (jsonObject.has(attribute)) {
                    indexer.addToIndexer(jsonObject, attribute);
                    indexerCreated=true;
                }
            }
        }
        result.setMessage("There is no such attribute");
        if(!indexerCreated)
            return result;
        System.out.println(indexer);
        IndexerDiskOperation.saveIndex(reference, indexer);
        result.setStatus(Status.OK);
        result.setMessage("The index has been created");
        return result;
    }

    public void addToIndex(String databaseName, String collectionName, JSONObject jsonObject) throws IOException {
        String reference = databaseName + "," + collectionName;
        Indexer indexer;
        if (indexers.containsKey(reference)) {
            indexer = indexers.get(reference);
            for (String attribute : jsonObject.keySet()) {
                if (indexer.hasAttribute(attribute)) {
                    indexer.addToIndexer(jsonObject, attribute);
                }
            }
            IndexerDiskOperation.saveIndex(reference, indexer);
            System.out.println(indexer);
        }
    }

    public void updateToIndex(String databaseName, String collectionName, JSONObject oldObject, JSONObject newObject) throws IOException {
        String reference = databaseName + "," + collectionName;
        Indexer indexer;
        if (indexers.containsKey(reference)) {
            indexer = indexers.get(reference);
            for (String attribute : oldObject.keySet()) {
                if (indexer.hasAttribute(attribute)) {
                    indexer.removeFromIndexer(oldObject, attribute);
                    if (newObject.keySet().contains(attribute))
                        indexer.addToIndexer(newObject, attribute);
                }
            }

            IndexerDiskOperation.saveIndex(reference, indexer);
            System.out.println(indexer);
        }
    }

    public void deleteFromIndex(String databaseName, String collectionName, JSONObject jsonObject) throws IOException {
        String reference = databaseName + "," + collectionName;
        Indexer indexer;
        if (indexers.containsKey(reference)) {
            indexer = indexers.get(reference);
            for (String attribute : jsonObject.keySet()) {
                if (indexer.hasAttribute(attribute)) {
                    indexer.removeFromIndexer(jsonObject, attribute);
                }
            }
            IndexerDiskOperation.saveIndex(reference, indexer);
            System.out.println(indexer);
        }
    }

    public Result removeAllIndexesForCollection(Query query) throws IOException {
        Result result = new Result();
        result.setStatus(Status.ERROR);
        result.setMessage("there is no such index");
        String reference = query.getDatabaseName() + "," + query.getCollectionName();
        if (indexers.containsKey(reference)) {
            indexers.remove(reference);
            IndexerDiskOperation.deleteIndex(reference);
            result.setStatus(Status.OK);
            result.setMessage("index has been deleted");
        }
        return result;
    }

    public void removeAllIndexesForDatabase(String databaseName) throws IOException {

        for (String key : indexers.keySet()) {
            if (key.startsWith(databaseName + ",")) {
                indexers.remove(key);
                IndexerDiskOperation.deleteIndex(key);
            }
        }

    }

    public List<String> getIdsFromIndex(String databaseName, String collectionName, String attribute, String value) {
        String reference = databaseName + "," + collectionName;
        Indexer indexer = indexers.get(reference);
        if (indexer != null) {
            return indexer.getIdsFromIndex(attribute, value);
        }
        return new ArrayList<>();
    }

    public boolean attributeHasIndex(String databaseName, String collectionName, String attribute) {
        String reference = databaseName + "," + collectionName;
        Indexer indexer = indexers.get(reference);
        if (indexer != null) {
            return indexer.hasAttribute(attribute);
        }
        return false;
    }

    public static IndexerManager getInstance() {
        if (instance == null) {
            instance = new IndexerManager();
        }
        return instance;
    }
}
