package org.example.disk;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;

public class DatabaseOperation {
    public static void createDatabase(String databaseName) throws IOException {
        if (DiskUtils.databaseExists(databaseName)) {
            throw new FileAlreadyExistsException("Database Exists!");
        }
        DiskUtils.createDirectoryIfNotFound(DiskUtils.databasePath(databaseName));
    }

    public static void deleteDatabase(String databaseName) throws IOException {
        File databaseDirectory = new File(DiskUtils.databasePath(databaseName));
        FileUtils.deleteDirectory(databaseDirectory);
    }
}
