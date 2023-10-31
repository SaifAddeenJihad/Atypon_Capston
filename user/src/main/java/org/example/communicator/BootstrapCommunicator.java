package org.example.communicator;

import org.example.Security.EncryptionUtil;
import org.example.Security.PasswordHasher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class BootstrapCommunicator {
    private static String username;

    private BootstrapCommunicator() {

    }

    public static int start(Scanner scanner) {
        return communicator(scanner);
    }

    private static int communicator(Scanner scanner) {
        int serverPort = 7000;

        try (
                Socket socket = new Socket("localhost", serverPort);
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
        ) {
            System.out.println("Connected to the server.");
            String response;
            boolean running = true;
            while (running) {
                System.out.print("would you like to log in as \n1-new user\n2-user\n3-exit\n");
                response = scanner.nextLine().trim();
                writer.println(response);
                switch (response) {
                    case "1" -> {
                        return newUser(reader, writer, scanner);
                    }
                    case "2" -> {
                        return oldUser(reader, writer, scanner);
                    }
                    case "3" -> {
                        writer.println("exit");
                        running = false;
                    }
                    default -> throw new IllegalStateException("Unexpected value: " + response);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private static int newUser(BufferedReader reader, PrintWriter writer, Scanner scanner) throws IOException {
        while (true) {
            System.out.print("Enter username: ");
            username = scanner.nextLine();
            writer.println(username);
            if (reader.readLine().equals("valid"))
                break;
            System.out.println("Enter valid username please");
        }
        System.out.print("Enter password: ");
        String password = PasswordHasher.hashPassword(scanner.nextLine());
        writer.println(EncryptionUtil.encrypt(password));
        String oneTimePassword = reader.readLine();
        int port = Integer.parseInt(reader.readLine());
        System.out.println("Your one time password is :" + oneTimePassword);
        return port;
    }

    private static int oldUser(BufferedReader reader, PrintWriter writer, Scanner scanner) throws IOException {
        System.out.print("Enter username: ");
        while (true) {
            username = scanner.nextLine();
            writer.println(username);
            if (reader.readLine().equals("valid")) break;
            System.out.println("Enter valid username please");
        }
        System.out.print("Enter password: ");
        while (true) {
            String password = PasswordHasher.hashPassword(scanner.nextLine());
            writer.println(EncryptionUtil.encrypt(password));
            if (reader.readLine().equals("login succeeded")) break;
            System.out.println("Enter valid password please");
        }
        String oneTimePassword = reader.readLine();
        int port = Integer.parseInt(reader.readLine());
        System.out.println("Your one time password is :" + oneTimePassword);
        return port;
    }

    public static String getUsername() {
        return username;
    }

}
