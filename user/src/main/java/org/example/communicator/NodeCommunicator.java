package org.example.communicator;

import org.example.Factories.QueryFactory;
import org.example.models.Query;
import org.example.models.QueryType;
import org.example.models.Result;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;


public class NodeCommunicator {
    private static int nodePort;

    private NodeCommunicator() {
    }

    public static void initializer(int nodePort) {
        NodeCommunicator.nodePort = nodePort;
    }

    public static void start(Scanner scanner) {
        communicator(scanner);
    }

    private static void communicator(Scanner scanner) {

        try {
            Socket socket = new Socket("localhost", nodePort);
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            System.out.println("Please enter your OTP");
            while (true) {
                outputStream.writeObject(BootstrapCommunicator.getUsername());
                outputStream.writeObject(scanner.nextLine());
                if ("valid".equals((String) inputStream.readObject()))
                    break;
                System.out.println("Please enter correct OTP");
            }
            sendQuery(scanner ,outputStream,inputStream);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void sendQuery(Scanner scanner,ObjectOutputStream outputStream,ObjectInputStream inputStream) throws IOException, ClassNotFoundException {
        boolean running = true;
        String response;
        while (running) {
            System.out.println("Please enter valid query or help or exit");
            Query query;
            response = scanner.nextLine();
            switch (response) {
                case "help":
                    help();
                    continue;
                case "exit": {
                    running = false;
                    query = new Query();
                    query.setQueryType(QueryType.EXIT);
                    outputStream.writeObject(query);
                }
                continue;
                default: {
                    QueryFactory queryFactory = new QueryFactory();
                    query = queryFactory.createQuery(response);
                    if (query.getQueryType() == null)
                        continue;
                    outputStream.writeObject(query);
                    Result result = (Result) inputStream.readObject();
                    result.toString();
                }
            }

        }
    }

    private static void help() {
        System.out.println(
                        "    CREATE_DATABASE(databaseName)\n" +
                        "    databaseName.CREATE_COLLECTION(collectionName)\n" +
                        "    databaseName.collectionName.CREATE_DOCUMENT({id:value,attribute1:value,attribute2:value})\n" +
                        "    databaseName.collectionName.CREATE_INDEX(attributeName),\n" +
                        "    databaseName.READ_COLLECTION(collectionName),\n" +
                        "    databaseName.collectionName.READ_DOCUMENT(documentName),\n" +
                        "    databaseName.collectionName.documentName.UPDATE_DOCUMENT({attribute1:value,attribute2:value}),\n" +
                        "    DELETE_DATABASE(databaseName),\n" +
                        "    databaseName.DELETE_COLLECTION(collectionName),\n" +
                        "    databaseName.collectionName.DELETE_DOCUMENT(documentName),\n" +
                        "    databaseName.DELETE_INDEX(collectionName),\n" +
                        "    databaseName.collectionName.FIND_EQUAL({attribute:value}),");
    }

}
