package org.example.disk;

import org.apache.commons.io.FileUtils;
import org.example.Cache.DocumentCache;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;

import static org.example.disk.DiskUtils.*;

public class DocumentOperation {

    public static void createDocument(String databaseName, String collectionName, JSONObject document) throws IOException {
        if (!databaseExists(databaseName)) {
            throw new IllegalArgumentException();
        }
        if (!collectionExists(databaseName, collectionName)) {
            throw new IllegalArgumentException();
        }
        String documentId = (String) document.get("id");
        if (documentId.isEmpty()) {
            throw new IllegalArgumentException("Document ID is empty.");
        }
        if (documentExists(databaseName, collectionName, documentId)) {
            throw new FileAlreadyExistsException("Document Exists!");
        }

        String path = documentPath(databaseName, collectionName, documentId);
        try (FileWriter fileWriter = new FileWriter(path)) {
            DocumentCache.getInstance().putDocument(databaseName, collectionName, documentId, document);
            fileWriter.write(document.toString());
            System.out.println("Document written to file: " + path);
        }
    }

    public static void deleteDocument(String databaseName, String collectionName, String documentName) throws IOException {
        DocumentCache.getInstance().removeDocument(databaseName, collectionName, documentName);
        File document = new File(documentPath(databaseName, collectionName, documentName));
        FileUtils.delete(document);
    }

    public static void updateDocument(String databaseName, String collectionName, String documentName, JSONObject document) throws IOException {
        if (!databaseExists(databaseName)) {
            throw new IllegalArgumentException();
        }
        if (!collectionExists(databaseName, collectionName)) {
            throw new IllegalArgumentException();
        }
        if (!documentExists(databaseName, collectionName, documentName)) {
            throw new IllegalArgumentException();
        }
        String path = documentPath(databaseName, collectionName, documentName);
        JSONObject oldDocument = readJsonFromFile(path);
        for (String key : document.keySet()) {
            oldDocument.put(key, document.get(key));
        }
        DocumentCache.getInstance().putDocument(databaseName, collectionName, documentName, oldDocument);
        try (FileWriter fileWriter = new FileWriter(path)) {
            fileWriter.write(oldDocument.toString());
        }
    }

    public static JSONObject readDocument(String databaseName, String collectionName, String documentName) throws IOException {
        if (!databaseExists(databaseName)) {
            throw new IllegalArgumentException();
        }
        if (!collectionExists(databaseName, collectionName)) {
            throw new IllegalArgumentException();
        }
        if (!documentExists(databaseName, collectionName, documentName)) {
            throw new IllegalArgumentException();
        }
        String temp = DocumentCache.getInstance().getDocument(databaseName, collectionName, documentName);
        JSONObject jsonObject;
        if (temp != null){
            jsonObject = new JSONObject(temp);
            return jsonObject;
        }
        String path = documentPath(databaseName, collectionName, documentName);
        return readJsonFromFile(path);

    }

    private static JSONObject readJsonFromFile(String filePath) throws IOException {
        JSONObject jsonObject;
        try (FileReader fileReader = new FileReader(filePath)) {
            JSONTokener token = new JSONTokener(fileReader);
            jsonObject = new JSONObject(token);
        }
        return jsonObject;
    }

}
