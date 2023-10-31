package org.example.Factories;

import org.example.Utils.QueryUtils;
import org.example.models.Query;
import org.example.models.QueryType;

public class CollectionQueryFactory {
    public static Query createCollectionQuery(String response) {
        Query query = new Query();
        if (response.contains(QueryType.CREATE_COLLECTION.name())) {
            query = QueryUtils.createCollection(response);
        } else if (response.contains(QueryType.READ_COLLECTION.name())) {
            query = QueryUtils.readCollection(response);
        } else if (response.contains(QueryType.DELETE_COLLECTION.name())) {
            query = QueryUtils.deleteCollection(response);
        }
        return query;
    }
}
