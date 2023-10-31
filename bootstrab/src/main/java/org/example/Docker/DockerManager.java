package org.example.Docker;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.example.Docker.DockerCommandRunner.executeCommand;

public class DockerManager {
    private  Map<String, NodeConfig> nodeConfigs;
    private static DockerManager instance;
    private String imageName;
    private DockerManager() {
        nodeConfigs = new HashMap<>();
    }

    private static String generateDockerRunCommand(String containerName, NodeConfig config) {

        return String.format("docker run -d -p %d:%d -p %d:%d -p %d:%d " +
                        "-e port=%d -e nodeId=%d -e NumOfNodes=%d " +
                        "--name %s %s",
                config.getHostPort1(), config.getContainerPort1(),
                config.getHostPort2(), config.getContainerPort2(),
                config.getHostPort3(), config.getContainerPort3(),
                config.getPort(), config.getNodeId(), config.getNumOfNodes(),
                containerName, config.getImage());

    }

    public void startContainers(int numberOfContainers,String imageName) {
        this.imageName=imageName;
        for (int i = 0; i < numberOfContainers; i++) {
            int offset = i * 10;
            NodeConfig nodeConfig = new NodeConfig(imageName, 7020 + offset, i, numberOfContainers);
            String containerName="Node_" + (i+1);
            nodeConfigs.put(containerName, nodeConfig);
            String dockerCommand = "docker start " +containerName;
            try {
                if (!executeCommand(dockerCommand)) {
                    executeCommand(generateDockerRunCommand(containerName,nodeConfig));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Thread stopContainers() {
        Thread stopThread = new Thread(() -> {
            for (String containerName : nodeConfigs.keySet()) {
                try {
                    executeCommand("docker stop " + containerName);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        });

        return stopThread;
    }

    public static DockerManager getInstance() {
        if (instance == null) {
            instance = new DockerManager();
        }
        return instance;
    }
}
