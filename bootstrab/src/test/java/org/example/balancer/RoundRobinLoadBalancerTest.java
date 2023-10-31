package org.example.balancer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RoundRobinLoadBalancerTest {
    @Test
    void oneNode() {
        int nodeId;
        RoundRobinLoadBalancer.setNumNodes(1);
        nodeId = RoundRobinLoadBalancer.chooseNode();
        assertEquals(nodeId, 1);
        nodeId = RoundRobinLoadBalancer.chooseNode();
        assertEquals(nodeId, 1);
    }
    @Test
    void fourNodes() {
        int nodeId;
        RoundRobinLoadBalancer.setNumNodes(4);
        nodeId = RoundRobinLoadBalancer.chooseNode();
        assertEquals(nodeId, 1);
        nodeId = RoundRobinLoadBalancer.chooseNode();
        assertEquals(nodeId, 2);
        nodeId = RoundRobinLoadBalancer.chooseNode();
        assertEquals(nodeId, 3);
        nodeId = RoundRobinLoadBalancer.chooseNode();
        assertEquals(nodeId, 4);
        nodeId = RoundRobinLoadBalancer.chooseNode();
        assertEquals(nodeId, 1);
    }
}