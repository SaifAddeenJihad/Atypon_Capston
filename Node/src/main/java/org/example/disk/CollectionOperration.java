package org.example.disk;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;

public class CollectionOperration {
    public static void createCollection(String databaseName, String collectionName) throws IOException {
        if (!DiskUtils.databaseExists(databaseName)) {
            throw new IllegalArgumentException();
        }
        if (DiskUtils.collectionExists(databaseName, collectionName)) {
            throw new FileAlreadyExistsException("Collection Exists!");
        }
        DiskUtils.createDirectoryIfNotFound(DiskUtils.databasePath(databaseName) + "/" + collectionName);
    }

    public static void deleteCollection(String databaseName, String collectionName) throws IOException {
        File collectionDirectory = new File(DiskUtils.collectionPath(databaseName, collectionName));
        FileUtils.deleteDirectory(collectionDirectory);
    }

    public static JSONArray readCollection(String databaseName, String collectionName) throws IOException {
        JSONArray jsonArray = new JSONArray();
        String collectionPath = DiskUtils.collectionPath(databaseName, collectionName);
        File collectionDirectory = new File(collectionPath);
        if (collectionDirectory.exists() && collectionDirectory.isDirectory()) {
            File[] files = collectionDirectory.listFiles((dir, name) -> name.endsWith(".json"));

            if (files != null) {
                for (File file : files) {
                    try (FileReader fileReader = new FileReader(file)) {
                        JSONTokener token = new JSONTokener(fileReader);
                        JSONObject jsonObject = new JSONObject(token);
                        jsonArray.put(jsonObject);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return jsonArray;
    }

}
