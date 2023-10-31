package org.example.communicators;


import org.example.security.DecryptionUtil;
import org.example.security.OTPGenerator;
import org.example.User.User;
import org.example.User.UsersManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private static Map<Integer, Integer> nodePorts;
    public ClientHandler(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    @Override
    public void run() {
        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
        ) {
            String clientMessage;
            while ((clientMessage = reader.readLine()) != null) {
                System.out.println("Received from client: " + clientMessage);
                if(clientMessage.equals("1")){
                    newUser(reader,writer);
                }
                else if(clientMessage.equals("2")){
                    oldUser(reader,writer);
                }
                else if (clientMessage.equals("3"))break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
                System.out.println("Client disconnected: " + clientSocket.getInetAddress().getHostAddress());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void oldUser(BufferedReader reader, PrintWriter writer) throws IOException {
        String username=reader.readLine();
        while (true) {
            String validation=UsersManager.getInstance().validateOldUsername(username);
            writer.println(validation);
            if (validation.equals("valid"))break;
            username=reader.readLine();
        }
        User user =UsersManager.getInstance().getUser(username);
        String password=DecryptionUtil.decrypt(reader.readLine());
        while (true) {
            if (password.equals(user.getPassword()))
                break;
            writer.println("pleas enter correct password");
            password=DecryptionUtil.decrypt(reader.readLine());
        }
        String oneTimePassword= OTPGenerator.generateOTP(6);
        int nodePort =NodeHandler.sendUserData(username,oneTimePassword);
        writer.println("login succeeded");
        writer.println(oneTimePassword);
        writer.println(nodePort);
    }



    private void newUser(BufferedReader reader,PrintWriter writer) throws IOException {
        String username=reader.readLine();
        while (true) {
            String validation= UsersManager.getInstance().validateNewUsername(username);
            writer.println(validation);
            if (validation.equals("valid"))break;
            username=reader.readLine();
        }
        String password=DecryptionUtil.decrypt(reader.readLine());
        User user=new User(username, password);
        UsersManager.getInstance().addUser(user);
        String oneTimePassword= OTPGenerator.generateOTP(6);
        int nodePort =NodeHandler.sendUserData(username,oneTimePassword);
        writer.println(oneTimePassword);
        writer.println(nodePort);
    }

}