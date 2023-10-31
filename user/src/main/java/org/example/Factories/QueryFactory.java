package org.example.Factories;

import org.example.models.Query;

public class QueryFactory {
    public Query createQuery(String response){
        if(response.contains("_DATABASE")){
            return DatabaseQueryFactory.createDatabaseQuery(response);
        }else if(response.contains("_COLLECTION")){
            return CollectionQueryFactory.createCollectionQuery(response);
        }
        else if(response.contains("_DOCUMENT")){
            return DocumentQueryFactory.createDocumentQuery(response);
        }
        else {
            return GeneralQueryFactory.createGeneralQuery(response);
        }
    }
}
