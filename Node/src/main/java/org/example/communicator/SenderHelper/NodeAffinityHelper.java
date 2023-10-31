package org.example.communicator.SenderHelper;

import org.example.disk.DocumentOperation;
import org.example.models.AffinityRequest;
import org.example.models.Query;
import org.example.security.DocumentHasher;
import org.json.JSONObject;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Map;

public class NodeAffinityHelper {
    private static Map<Integer, Integer> receivePorts;

    private NodeAffinityHelper() {
    }

    public static void initializePorts(int numberOfNodes) {
        receivePorts = new HashMap<>();
        int value = 7023;
        for (int i = 0; i < numberOfNodes; i++) {
            receivePorts.put(i + 1, value);
            value = value + 10;
        }
    }

    public static void sendAffinity(Query query, int nodeId) throws IOException {
        AffinityRequest affinityRequest = creatRequest(query);
        int port = receivePorts.get(nodeId);
        try {
            Socket socket = new Socket("host.docker.internal", port);
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.writeObject("affinity");
            outputStream.writeObject(affinityRequest);
            socket.close();
        } catch (SocketException socketException) {
            System.out.println("port " + port + " is not available");
        }
    }

    private static AffinityRequest creatRequest(Query query) throws IOException {
        AffinityRequest affinityRequest = new AffinityRequest();
        affinityRequest.setDatabaseName(query.getDatabaseName());
        affinityRequest.setCollectionName(query.getCollectionName());
        affinityRequest.setDocumentName(query.getDocumentName());
        affinityRequest.setQueryType(query.getQueryType());
        JSONObject oldObject = DocumentOperation.readDocument(query.getDatabaseName(), query.getCollectionName(), query.getDocumentName());
        String document = query.getDocument();
        affinityRequest.setDocument(document);
        affinityRequest.setHashcode(DocumentHasher.hashDocument(oldObject.toString()));
        return affinityRequest;
    }

}
