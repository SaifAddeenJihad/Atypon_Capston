package org.example.communicator;

import org.example.security.UserValidator;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class BootstrapCommunicator implements Runnable {
    private final ServerSocket serverSocket;

    public BootstrapCommunicator(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        while (true) {
            try  {
                Socket socket = serverSocket.accept();
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                String userName = (String) inputStream.readObject();
                String oneTimePassword = (String) inputStream.readObject();
                System.out.println("Received user data on Node: " + userName);
                UserValidator.getInstance().addUser(userName,oneTimePassword);
                inputStream.close();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}

