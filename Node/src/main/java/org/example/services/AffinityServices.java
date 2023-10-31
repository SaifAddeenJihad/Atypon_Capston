package org.example.services;

import org.example.communicator.SenderHelper.NodeBroadcastHelper;
import org.example.disk.DocumentOperation;
import org.example.models.AffinityRequest;
import org.example.models.Query;
import org.example.security.DocumentHasher;
import org.json.JSONObject;

import java.io.IOException;

public class AffinityServices {
    private AffinityServices(){}
    public static void updateDocument(AffinityRequest affinityRequest) throws IOException {
        if (affinityRequest.getHashcode().equals(getLocalHashCode(affinityRequest))) {
            Query query = creatQuery(affinityRequest);
            DocumentService.updateBroadcastDocument(query);
            NodeBroadcastHelper.broadcastQuery(query);
        }
    }

    public static void deleteDocument(AffinityRequest affinityRequest) throws IOException {

        if (affinityRequest.getHashcode().equals(getLocalHashCode(affinityRequest))) {
            Query query = creatQuery(affinityRequest);
            DocumentService.deleteBroadcastDocument(query);
            NodeBroadcastHelper.broadcastQuery(query);
        }
    }

    private static Query creatQuery(AffinityRequest affinityRequest) {
        Query query = new Query();
        query.setQueryType(affinityRequest.getQueryType());
        query.setDocument(affinityRequest.getDocument());
        query.setDatabaseName(affinityRequest.getDatabaseName());
        query.setCollectionName(affinityRequest.getCollectionName());
        query.setDocumentName(affinityRequest.getDocumentName());
        return query;
    }

    private static String getLocalHashCode(AffinityRequest affinityRequest) throws IOException {
        String databaseName = affinityRequest.getDatabaseName();
        String collectionName = affinityRequest.getCollectionName();
        String documentName = affinityRequest.getDocumentName();
        JSONObject oldDocument = DocumentOperation.readDocument(databaseName, collectionName, documentName);
        return DocumentHasher.hashDocument(oldDocument.toString());
    }
}
