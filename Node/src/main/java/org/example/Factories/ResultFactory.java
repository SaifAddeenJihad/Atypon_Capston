package org.example.Factories;

import org.example.communicator.SenderHelper.NodeBroadcastHelper;
import org.example.models.Query;
import org.example.models.QueryType;
import org.example.models.Result;
import org.example.models.Status;

import java.io.IOException;

public class ResultFactory {
    public Result processQuery(Query recivedQuery) throws IOException {
        QueryType queryType = recivedQuery.getQueryType();
        if (queryType.name().contains("_DATABASE")) {
            return DatabaseResultFactory.processDatabaseQuery(recivedQuery, queryType);
        } else if (queryType.name().contains("_COLLECTION")) {
            return CollectionResultFactory.processCollectionQuery(recivedQuery, queryType);
        } else if (queryType.name().contains("_DOCUMENT")) {
            return DocumentResultFactory.processDocumentQuery(recivedQuery, queryType);
        } else {
            return GeneralResultFactory.processGeneralQuery(recivedQuery, queryType);
        }
    }
    public static void broadcastQuery(Query query, Result result) throws IOException {
        if (result.getStatus() == Status.OK)
            NodeBroadcastHelper.broadcastQuery(query);
    }
}
