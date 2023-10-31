package org.example.Factories;

import org.example.indexer.IndexerManager;
import org.example.models.Query;
import org.example.models.QueryType;
import org.example.models.Result;
import org.example.services.SearchServices;

import java.io.IOException;

public class GeneralResultFactory extends ResultFactory {
    public static Result processGeneralQuery(Query recivedQuery, QueryType queryType) throws IOException {
        Result result = new Result();
        switch (queryType) {
            case CREATE_INDEX -> {
                result = IndexerManager.getInstance().creatIndex(recivedQuery);
                broadcastQuery(recivedQuery, result);
            }
            case DELETE_INDEX -> {
                result = IndexerManager.getInstance().removeAllIndexesForCollection(recivedQuery);
                broadcastQuery(recivedQuery, result);
            }
            case FIND_EQUAL -> result = SearchServices.FindEqual(recivedQuery);
        }
        return result;
    }
}
