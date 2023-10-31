package org.example.indexer;

import java.io.*;
import java.util.HashMap;

public class IndexerDiskOperation {
    private final static HashMap<String, Indexer> indexers = new HashMap<>();
    private static String indexersPath = "SavedIndexers";

    public static HashMap<String, Indexer> loadIndexers() {
        File directory = new File(indexersPath);
        if (!directory.exists() || !directory.isDirectory()) {
            directory.mkdirs();
            return indexers;
        }

        File[] files = directory.listFiles();

        if (files == null) {
            return indexers;
        }

        for (File file : files) {
            if (file.isFile()) {
                try (FileInputStream fileInputStream = new FileInputStream(file);
                     ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {
                    Indexer indexer = (Indexer) objectInputStream.readObject();
                    String fileName = file.getName();
                    String key = fileName.substring(0, fileName.lastIndexOf('.'));
                    indexers.put(key, indexer);
                } catch (IOException | ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return indexers;
    }

    public static void saveIndex(String reference, Indexer indexer) throws IOException {
        String fileName = indexersPath + "/" + reference + ".txt";
        File file = new File(fileName);
        try (FileOutputStream fileOutputStream = new FileOutputStream(file);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
            objectOutputStream.writeObject(indexer);
            System.out.println("Indexer saved to file: " + fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deleteIndex(String reference) throws IOException {
        String fileName = indexersPath + "/" + reference + ".txt";
        File file = new File(fileName);
        file.delete();
    }


}
