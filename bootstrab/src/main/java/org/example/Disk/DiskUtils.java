package org.example.Disk;


import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class DiskUtils {
    private DiskUtils() {

    }

    public static void createDocument(String documentName, JSONObject document) throws IOException {
        try (FileWriter fileWriter = new FileWriter(documentName)) {
            fileWriter.write(document.toString());
            System.out.println("Document written to file: " + document);
        }
    }

    public static void updateDocument(String documentName, JSONObject document) throws IOException {
        if (!directoryOrFileExists(documentName)) {
            createDocument(documentName, document);
        } else {
            try (FileWriter fileWriter = new FileWriter(documentName, true)) {
                fileWriter.write(document.toString());
                fileWriter.write("\n");
                System.out.println("Document written to file: " + document);
            }
        }
    }

    public static JSONArray readDocument(String documentName) throws IOException {

        if (!directoryOrFileExists(documentName)) {
            return new JSONArray();
        }
        return readJsonFromFile(documentName);

    }

    public static JSONArray readJsonFromFile(String filePath) throws IOException {
        JSONArray jsonArray = new JSONArray();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                JSONTokener tokener = new JSONTokener(line);
                JSONObject jsonObject = new JSONObject(tokener);
                jsonArray.put(jsonObject);
            }
        }

        return jsonArray;
    }

    private static boolean directoryOrFileExists(String pathForFileOrDirectory) {
        Path path = Paths.get(pathForFileOrDirectory);
        return Files.exists(path);
    }
}
