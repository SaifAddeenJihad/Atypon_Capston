package org.example.balncer;


public class AffinityBalancer {
    private static int nodeId;
    private static int numNodes;

    private AffinityBalancer() {
    }

    public static synchronized int choseAffinity() {
        int chosenAffinity = nodeId;
        nodeId = (nodeId % numNodes) + 1;
        System.out.println(chosenAffinity);
        return chosenAffinity;
    }

    public static void initialize(int numNodes, int nodeId) {
        setNumNodes(numNodes);
        setNodeId(nodeId);
    }

    private static void setNumNodes(int numNodes) {
        if (numNodes > 0) {
            AffinityBalancer.numNodes = numNodes;
        }
    }

    private static void setNodeId(int nodeId) {
        if (nodeId > 0) {
            AffinityBalancer.nodeId = nodeId;
        }
    }

}