package org.example.Factories;

import org.example.models.Query;
import org.example.models.QueryType;
import org.example.models.Result;
import org.example.services.DatabaseService;

import java.io.IOException;

public class DatabaseResultFactory extends ResultFactory {
    public static Result processDatabaseQuery(Query recivedQuery, QueryType queryType) throws IOException {
        Result result = new Result();
        switch (queryType) {
            case CREATE_DATABASE -> {
                result = DatabaseService.creatDatabase(recivedQuery);
                broadcastQuery(recivedQuery, result);
            }
            case DELETE_DATABASE -> {
                result = DatabaseService.deleteDatabase(recivedQuery);
                broadcastQuery(recivedQuery, result);
            }
        }
        return result;
    }


}
