package org.example.Docker;

public class NodeConfig {
    protected String image;
    protected int hostPort1, hostPort2, hostPort3;
    protected int containerPort1, containerPort2, containerPort3;
    protected int port, nodeId, numOfNodes;

    protected NodeConfig(String image, int port , int nodeId, int numOfNodes) {
        this.image = image;
        this.hostPort1 = port;
        this.hostPort2 = port+3;
        this.hostPort3 = port+5;
        this.containerPort1 = port;
        this.containerPort2 = port+3;
        this.containerPort3 = port+5;
        this.port = port;
        this.nodeId = nodeId+1;
        this.numOfNodes = numOfNodes;
    }

    public String getImage() {
        return image;
    }

    public int getHostPort1() {
        return hostPort1;
    }

    public int getHostPort2() {
        return hostPort2;
    }

    public int getHostPort3() {
        return hostPort3;
    }

    public int getContainerPort1() {
        return containerPort1;
    }

    public int getContainerPort2() {
        return containerPort2;
    }

    public int getContainerPort3() {
        return containerPort3;
    }

    public int getPort() {
        return port;
    }

    public int getNodeId() {
        return nodeId;
    }

    public int getNumOfNodes() {
        return numOfNodes;
    }

    @Override
    public String toString() {
        return "NodeConfig{" +
                "image='" + image + '\'' +
                ", hostPort1=" + hostPort1 +
                ", hostPort2=" + hostPort2 +
                ", hostPort3=" + hostPort3 +
                ", containerPort1=" + containerPort1 +
                ", containerPort2=" + containerPort2 +
                ", containerPort3=" + containerPort3 +
                ", port=" + port +
                ", nodeId=" + nodeId +
                ", numOfNodes=" + numOfNodes +
                '}';
    }
}