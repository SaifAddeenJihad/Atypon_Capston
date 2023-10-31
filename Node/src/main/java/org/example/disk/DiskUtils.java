package org.example.disk;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DiskUtils {
    private static final String storageDirectoryPath = "storage";

    private DiskUtils() {

    }

    public static void createDirectoryIfNotFound(String directory) throws IOException {
        Files.createDirectories(Paths.get(directory));
    }

    public static boolean collectionExists(String databaseName, String collectionName) {
        return directoryOrFileExists(collectionPath(databaseName, collectionName));
    }

    public static boolean databaseExists(String databaseName) {
        return directoryOrFileExists(databasePath(databaseName));
    }

    public static boolean documentExists(String databaseName, String collectionName, String documentName) {
        return directoryOrFileExists(documentPath(databaseName, collectionName, documentName));
    }

    private static boolean directoryOrFileExists(String pathForFileOrDirectory) {
        Path path = Paths.get(pathForFileOrDirectory);
        return Files.exists(path);
    }

    public static String databasePath(String databaseName) {
        return storageDirectoryPath + "/" + databaseName;
    }

    public static String documentPath(String databaseName, String collectionName, String documentName) {
        return databasePath(databaseName) + "/" + collectionName + "/" + documentName + ".json";
    }

    public static String collectionPath(String databaseName, String collectionName) {
        return databasePath(databaseName) + "/" + collectionName;
    }

}
