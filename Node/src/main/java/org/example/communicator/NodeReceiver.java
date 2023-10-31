package org.example.communicator;

import org.example.indexer.IndexerManager;
import org.example.models.AffinityRequest;
import org.example.models.Query;
import org.example.models.QueryType;
import org.example.services.AffinityServices;
import org.example.services.CollectionServices;
import org.example.services.DatabaseService;
import org.example.services.DocumentService;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class NodeReceiver implements Runnable {
    private final ServerSocket serverSocket;

    public NodeReceiver(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                String requestType = (String) inputStream.readObject();
                if ("affinity".equals(requestType)) {
                    AffinityRequest affinityRequest = (AffinityRequest) inputStream.readObject();
                    processAffinityQuery(affinityRequest);
                } else if ("broadcast".equals(requestType)) {
                    Query query = (Query) inputStream.readObject();
                    processBroadcastQuery(query);
                }
                inputStream.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void processAffinityQuery(AffinityRequest affinityRequest) throws IOException {
        System.out.println(affinityRequest);
        if (affinityRequest.getQueryType() == QueryType.UPDATE_DOCUMENT) {
            AffinityServices.updateDocument(affinityRequest);
        } else if (affinityRequest.getQueryType() == QueryType.DELETE_DOCUMENT) {
            AffinityServices.deleteDocument(affinityRequest);
        }
    }

    private void processBroadcastQuery(Query query) throws IOException {
        System.out.println(query);
        switch (query.getQueryType()) {
            case CREATE_DATABASE -> DatabaseService.creatDatabase(query);
            case CREATE_COLLECTION -> CollectionServices.creatCollection(query);
            case CREATE_DOCUMENT -> DocumentService.creatDocument(query);
            case CREATE_INDEX -> IndexerManager.getInstance().creatIndex(query);
            case UPDATE_DOCUMENT -> DocumentService.updateBroadcastDocument(query);
            case DELETE_DATABASE -> DatabaseService.deleteDatabase(query);
            case DELETE_COLLECTION -> CollectionServices.deleteCollection(query);
            case DELETE_DOCUMENT -> DocumentService.deleteBroadcastDocument(query);
            case DELETE_INDEX -> IndexerManager.getInstance().removeAllIndexesForCollection(query);
            default -> {
            }
        }
    }

}
