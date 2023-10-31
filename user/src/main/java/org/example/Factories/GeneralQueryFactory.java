package org.example.Factories;

import org.example.Utils.QueryUtils;
import org.example.models.Query;
import org.example.models.QueryType;

public class GeneralQueryFactory {
    public static Query createGeneralQuery(String response) {
        Query query = new Query();
        if (response.contains(QueryType.CREATE_INDEX.name())) {
            query = QueryUtils.createIndex(response);
        } else if (response.contains(QueryType.DELETE_INDEX.name())) {
            query = QueryUtils.deleteIndex(response);
        } else if (response.contains(QueryType.FIND_EQUAL.name())) {
            query = QueryUtils.findEqual(response);
        }
        return query;
    }
}
