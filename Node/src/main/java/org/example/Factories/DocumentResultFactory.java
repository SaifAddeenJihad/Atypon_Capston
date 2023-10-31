package org.example.Factories;

import org.example.models.Query;
import org.example.models.QueryType;
import org.example.models.Result;
import org.example.services.DocumentService;

import java.io.IOException;

public class DocumentResultFactory extends ResultFactory {
    public static Result processDocumentQuery(Query recivedQuery, QueryType queryType) throws IOException {
        Result result = new Result();
        switch (queryType) {
            case CREATE_DOCUMENT:
                result = DocumentService.creatDocument(recivedQuery);
                break;
            case READ_DOCUMENT:
                result = DocumentService.readDocument(recivedQuery);
                break;
            case UPDATE_DOCUMENT:
                result = DocumentService.updateDocument(recivedQuery);
                break;
            case DELETE_DOCUMENT:
                result = DocumentService.deleteDocument(recivedQuery);
                break;

        }
        return result;
    }
}
