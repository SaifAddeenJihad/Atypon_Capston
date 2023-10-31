package org.example.services;

import org.example.Cache.DocumentCache;
import org.example.disk.DatabaseOperation;
import org.example.indexer.IndexerManager;
import org.example.models.Query;
import org.example.models.Result;
import org.example.models.Status;

import java.io.IOException;

import static org.example.disk.DiskUtils.databaseExists;

public class DatabaseService {
    private DatabaseService(){

    }
    public static Result creatDatabase(Query query) {
        Result result = new Result();
        result.setStatus(Status.ERROR);
        result.setMessage("Database is already created");
        if (databaseExists(query.getDatabaseName()))
            return result;
        try {
            DatabaseOperation.createDatabase(query.getDatabaseName());
            result.setStatus(Status.OK);
            result.setMessage("Database has been created ");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static Result deleteDatabase(Query query) {
        Result result = new Result();
        result.setStatus(Status.ERROR);
        result.setMessage("There is no such database to delete");
        if (!databaseExists(query.getDatabaseName()))
            return result;
        try {
            DatabaseOperation.deleteDatabase(query.getDatabaseName());
            IndexerManager.getInstance().removeAllIndexesForDatabase(query.getDatabaseName());
            DocumentCache.getInstance().removeAllDocumentsForDatabase(query.getDatabaseName());
            result.setStatus(Status.OK);
            result.setMessage("Database has been deleted ");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }
}
