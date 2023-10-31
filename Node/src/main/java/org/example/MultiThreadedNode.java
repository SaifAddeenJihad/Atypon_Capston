package org.example;

import org.example.balncer.AffinityBalancer;
import org.example.communicator.BootstrapCommunicator;
import org.example.communicator.ClientCommunicator;
import org.example.communicator.NodeReceiver;
import org.example.communicator.SenderHelper.NodeAffinityHelper;
import org.example.communicator.SenderHelper.NodeBroadcastHelper;
import org.example.indexer.IndexerManager;
import org.example.services.DocumentService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiThreadedNode {
    public static void main(String[] args) throws IOException {
        int port = Integer.parseInt(args[0]);
        int nodeId = Integer.parseInt(args[1]);
        int numberOfNodes = Integer.parseInt(args[2]);
        ServerSocket bootstrabServerSocket = new ServerSocket(port + 5);
        ServerSocket nodeServerSocket = new ServerSocket(port + 3);
        ServerSocket clientServerSocket = new ServerSocket(port);
        BootstrapCommunicator bootstrapCommunicator = new BootstrapCommunicator(bootstrabServerSocket);
        NodeReceiver nodeReceiver = new NodeReceiver(nodeServerSocket);
        IndexerManager.getInstance();
        DocumentService.setNodeId(nodeId);
        NodeBroadcastHelper.initializePorts(port, numberOfNodes);
        NodeAffinityHelper.initializePorts(numberOfNodes);
        AffinityBalancer.initialize(numberOfNodes, nodeId);
        new Thread(bootstrapCommunicator).start();
        new Thread(nodeReceiver).start();

        System.out.println("Node" +nodeId+" Is ready to Use");

        while (true) {
            Socket socket = clientServerSocket.accept();
            System.out.println("connected");
            Runnable nodeThread = new ClientCommunicator(socket);
            new Thread(nodeThread).start();
        }
    }
}
//        System.out.println("Hello world!");
//                DiskUtils.createDatabase("db3");
//                DiskUtils.deleteDatabase("db2" );
//                DiskUtils.createCollection("db2","c1");
//                DiskUtils.deleteCollection("db2","c1");
//                DiskUtils.createCollection("db2","c2");
//                JSONObject document = new JSONObject();
//                document.put("id","1");
//                document.put("length", 180);
//                document.put("weight", 90);
//                DiskUtils.createDocument("db2","c1",document);
//                document.put("id","2");
//                DiskUtils.createDocument("db2","c1",document);
//                document.put("id","3");
//                DiskUtils.createDocument("db2","c1",document);
//                document.remove("id");
//                System.out.println(DiskUtils.readDocument("db2","c1","2"));
//                DiskUtils.updateDocument("db2","c2","2",document);
//                System.out.println(DiskUtils.readDocument("db2","c1","2"));
//                System.out.println(DiskUtils.readCollection("db2","c1"));
//                DiskUtils.deleteCollection("db2","c1");
//                DiskUtils.deleteDocument("db2","c1","2");
