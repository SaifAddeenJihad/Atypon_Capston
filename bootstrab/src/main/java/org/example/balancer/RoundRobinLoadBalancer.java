package org.example.balancer;


public class RoundRobinLoadBalancer {
    private static int nodeId = 1;
    private static int numNodes = 1;

    private RoundRobinLoadBalancer() {
    }

    public static synchronized int chooseNode() {
        int chosenNode = nodeId;
        nodeId = (nodeId % numNodes) + 1; // Cyclic increment
        return chosenNode;
    }

    public static void setNumNodes(int numNodes) {
        if (numNodes > 0) {
            RoundRobinLoadBalancer.numNodes = numNodes;
        }
    }

}