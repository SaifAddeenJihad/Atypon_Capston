package org.example.Factories;

import org.example.models.Query;
import org.example.models.QueryType;
import org.example.models.Result;
import org.example.services.CollectionServices;

import java.io.IOException;

public class CollectionResultFactory extends ResultFactory {
    public static Result processCollectionQuery(Query recivedQuery, QueryType queryType) throws IOException {
        Result result = new Result();
        switch (queryType) {
            case CREATE_COLLECTION -> {
                result = CollectionServices.creatCollection(recivedQuery);
                broadcastQuery(recivedQuery, result);
            }
            case READ_COLLECTION -> {
                result = CollectionServices.readCollection(recivedQuery);
                broadcastQuery(recivedQuery, result);
            }
            case DELETE_COLLECTION -> {
                result = CollectionServices.deleteCollection(recivedQuery);
                broadcastQuery(recivedQuery, result);
            }
        }
        return result;
    }
}
