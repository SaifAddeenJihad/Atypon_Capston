package org.example;

import org.example.Disk.DiskUtils;
import org.example.Docker.DockerManager;
import org.example.balancer.RoundRobinLoadBalancer;
import org.example.communicators.ClientHandler;
import org.example.communicators.NodeHandler;
import java.io.*;
import java.net.*;

public class BootstrapStarter {
    public static void main(String[] args) throws IOException, InterruptedException {
        int numberOfNodes=Integer.parseInt(args[0]);
        DockerManager dockerManager=DockerManager.getInstance();
        dockerManager.startContainers(numberOfNodes,args[1]);
        Runtime.getRuntime().addShutdownHook(dockerManager.stopContainers());
        ServerSocket serverSocket = new ServerSocket(7000);
        System.out.println("Server is running and listening on port 7000.");
        DiskUtils.readDocument("users");
        NodeHandler.portInitiator(numberOfNodes);
        RoundRobinLoadBalancer.setNumNodes(numberOfNodes);
        Thread.sleep(1000);
       while (true) {
            Socket clientSocket = serverSocket.accept();
            ClientHandler clientHandler = new ClientHandler(clientSocket);
            new Thread(clientHandler).start();
        }
    }
}