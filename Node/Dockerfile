
FROM openjdk:20
COPY target/Node-1.0-SNAPSHOT.jar Node.jar
COPY storage storage
COPY SavedIndexers SavedIndexers
ENTRYPOINT java -jar Node.jar $port $nodeId $NumOfNodes




