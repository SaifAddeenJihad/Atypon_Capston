package org.example.communicator.SenderHelper;

import org.example.models.Query;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

public class NodeBroadcastHelper {
    private static List<Integer> receivePorts;

    private NodeBroadcastHelper() {
    }

    public static void initializePorts(int nodePort, int numberOfNodes) {
        receivePorts = new ArrayList<>();
        int value = 7023;
        for (int i = 0; i < numberOfNodes; i++) {
            if (value != (nodePort + 3)) {
                receivePorts.add(value);
            }
            value = value + 10;
        }
    }

    public static void broadcastQuery(Query query) throws IOException {
        for (int port : receivePorts) {
            try {
                Socket socket = new Socket("host.docker.internal", port);
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
                outputStream.writeObject("broadcast");
                outputStream.writeObject(query);
                socket.close();
            } catch (SocketException socketException) {
                System.out.println("port " + port + " is not available");
            }
        }
    }

}
