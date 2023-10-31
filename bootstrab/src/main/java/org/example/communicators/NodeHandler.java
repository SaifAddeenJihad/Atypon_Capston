package org.example.communicators;

import org.example.balancer.RoundRobinLoadBalancer;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

public class NodeHandler {

    private static Map<Integer, Integer> nodePorts;

    private NodeHandler() {
    }
    public static void portInitiator(int numberOfNodes){
        nodePorts = new HashMap<>();
        int port = 7020;
        for (int i = 0; i < numberOfNodes; i++) {
            nodePorts.put(i+1,port);
            port = port + 10;
        }
    }
    public static int sendUserData(String userName ,String oneTimePassword) {
        int nodePort= nodePorts.get(RoundRobinLoadBalancer.chooseNode());

        try (Socket socket = new Socket("localhost", nodePort+5);
             ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream())
        )
        {
            outputStream.writeObject(userName);
            outputStream.writeObject(oneTimePassword);
            outputStream.flush();
        }
        catch (IOException e) {
            e.printStackTrace();

        }
        return nodePort;
    }

}