package org.example.Factories;

import org.example.Utils.QueryUtils;
import org.example.models.Query;
import org.example.models.QueryType;

public class DatabaseQueryFactory {
    public static Query createDatabaseQuery(String response) {
        Query query = new Query();
        if (response.contains(QueryType.CREATE_DATABASE.name())) {
            query = QueryUtils.createDatabase(response);
        } else if (response.contains(QueryType.DELETE_DATABASE.name())) {
            query = QueryUtils.deleteDatabase(response);
        }
        return query;
    }
}
