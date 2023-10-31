package org.example.Factories;

import org.example.Utils.QueryUtils;
import org.example.models.Query;
import org.example.models.QueryType;

public class DocumentQueryFactory {
    public static Query createDocumentQuery(String response) {
        Query query = new Query();
        if (response.contains(QueryType.CREATE_DOCUMENT.name())) {
            query = QueryUtils.createDocument(response);
        } else if (response.contains(QueryType.READ_DOCUMENT.name())) {
            query = QueryUtils.readDocument(response);
        } else if (response.contains(QueryType.UPDATE_DOCUMENT.name())) {
            query = QueryUtils.updateDocument(response);
        } else if (response.contains(QueryType.DELETE_DOCUMENT.name())) {
            query = QueryUtils.deleteDocument(response);
        }
        return query;
    }
}
