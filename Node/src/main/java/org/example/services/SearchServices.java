package org.example.services;

import org.example.disk.CollectionOperration;
import org.example.disk.DocumentOperation;
import org.example.indexer.IndexerManager;
import org.example.models.Query;
import org.example.models.Result;
import org.example.models.Status;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import static org.example.disk.DiskUtils.collectionExists;

public class SearchServices {
    private SearchServices() {

    }

    public static Result FindEqual(Query query) throws IOException {
        Result result = new Result();
        result.setStatus(Status.ERROR);
        result.setMessage("There is no such collection to search in");
        String collectionName = query.getCollectionName();
        String databaseName = query.getDatabaseName();
        if (!collectionExists(databaseName, collectionName))
            return result;
        result.setStatus(Status.OK);
        result.setMessage("Here is the search result");
        JSONObject document = new JSONObject(query.getDocument());
        String searchAttribute = document.keys().next();
        String searchValue = document.get(searchAttribute) + "";
        if (IndexerManager.getInstance().attributeHasIndex(databaseName, collectionName, searchAttribute)) {
            List<String> idsFromIndex = IndexerManager.getInstance().getIdsFromIndex(databaseName, collectionName, searchAttribute, searchValue);
            for (String id : idsFromIndex) {
                result.addToJsonArray(DocumentOperation.readDocument(databaseName, collectionName, id));
            }
        } else {
            JSONArray jsonArray = CollectionOperration.readCollection(databaseName, collectionName);
            for (Object obj : jsonArray) {
                JSONObject doc = (JSONObject) obj;
                if (doc.has(searchAttribute) && doc.get(searchAttribute).toString().equals(searchValue)) {
                    result.addToJsonArray(doc);
                }
            }
        }
        return result;
    }
}
