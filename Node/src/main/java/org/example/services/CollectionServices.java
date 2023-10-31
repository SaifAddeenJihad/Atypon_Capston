package org.example.services;

import org.example.Cache.DocumentCache;
import org.example.disk.CollectionOperration;
import org.example.indexer.IndexerManager;
import org.example.models.Query;
import org.example.models.Result;
import org.example.models.Status;
import org.json.JSONArray;

import java.io.IOException;

import static org.example.disk.DiskUtils.collectionExists;

public class CollectionServices {
    private CollectionServices(){

    }
    public static Result creatCollection(Query query) {
        Result result = new Result();
        result.setStatus(Status.ERROR);
        result.setMessage("Collection is already created");
        if (collectionExists(query.getDatabaseName(), query.getCollectionName()))
            return result;
        try {
            CollectionOperration.createCollection(query.getDatabaseName(), query.getCollectionName());
            result.setStatus(Status.OK);
            result.setMessage("Collection has been created ");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static Result deleteCollection(Query query) {
        Result result = new Result();
        result.setStatus(Status.ERROR);
        result.setMessage("There is no such collection to delete");
        if (!collectionExists(query.getDatabaseName(), query.getCollectionName()))
            return result;
        try {
            CollectionOperration.deleteCollection(query.getDatabaseName(), query.getCollectionName());
            IndexerManager.getInstance().removeAllIndexesForCollection(query);
            DocumentCache.getInstance().removeAllDocumentsForCollection(query.getDatabaseName(), query.getCollectionName());
            result.setStatus(Status.OK);
            result.setMessage("Collection has been deleted ");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return result;
    }

    public static Result readCollection(Query query) {
        Result result = new Result();
        result.setStatus(Status.ERROR);
        result.setMessage("There is no such collection to read");
        if (!collectionExists(query.getDatabaseName(), query.getCollectionName()))
            return result;
        try {
            JSONArray jsonArray = CollectionOperration.readCollection(query.getDatabaseName(), query.getCollectionName());
            result.setStatus(Status.OK);
            result.setJsonArray(jsonArray);
            result.setMessage("Collection has been read successfully");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

}
