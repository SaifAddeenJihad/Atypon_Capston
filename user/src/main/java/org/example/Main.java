package org.example;

import org.example.communicator.BootstrapCommunicator;
import org.example.communicator.NodeCommunicator;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int nodePort = BootstrapCommunicator.start(scanner);
        System.out.println(nodePort);
        NodeCommunicator.initializer(nodePort);
        NodeCommunicator.start(scanner);
    }
}
