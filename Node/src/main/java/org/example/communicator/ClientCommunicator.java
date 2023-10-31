package org.example.communicator;

import org.example.Factories.ResultFactory;
import org.example.models.Query;
import org.example.models.QueryType;
import org.example.models.Result;
import org.example.security.UserValidator;

import java.io.*;
import java.net.Socket;

public class ClientCommunicator implements Runnable {
    private final Socket socket;

    public ClientCommunicator(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream());
            while (true) {
                String userName = (String) inputStream.readObject();
                String otp = (String) inputStream.readObject();
                if (UserValidator.getInstance().userIsValid(userName, otp)) {
                    outputStream.writeObject("valid");
                    break;
                }
                outputStream.writeObject("invalid");
            }
            while (true) {
                Query query;
                query = (Query) inputStream.readObject();
                System.out.println(query);
                if (query.getQueryType().equals(QueryType.EXIT))
                    break;
                ResultFactory resultFactory = new ResultFactory();
                Result result = resultFactory.processQuery(query);
                outputStream.writeObject(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
