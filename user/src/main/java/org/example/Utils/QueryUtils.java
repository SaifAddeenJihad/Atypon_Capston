package org.example.Utils;

import org.example.models.Query;
import org.example.models.QueryType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryUtils {
    public static Query createDatabase(String response) {
        Query query = new Query();
        String databaseName = extractArgument(response);
        query.setQueryType(QueryType.CREATE_DATABASE);
        query.setDatabaseName(databaseName);
        return query;
    }

    public static Query createCollection(String response) {
        Query query = new Query();
        String[] args = response.split("\\.");
        if (args.length == 2) {
            String databaseName = args[0].trim();
            String collectionName = extractArgument(response);
            query.setQueryType(QueryType.CREATE_COLLECTION);
            query.setDatabaseName(databaseName);
            query.setCollectionName(collectionName);
        }
        return query;
    }

    public static Query createDocument(String response) {
        Query query = new Query();

        String[] args = response.split("\\.");
        if (args.length == 3) {
            String databaseName = args[0].trim();
            String collectionName = args[1].trim();
            String documentContent = extractArgument(response);
            query.setQueryType(QueryType.CREATE_DOCUMENT);
            query.setDatabaseName(databaseName);
            query.setCollectionName(collectionName);
            query.setDocument(documentContent);
        }

        return query;
    }

    public static Query createIndex(String response) {
        Query query = new Query();

        String[] args = response.split("\\.");
        if (args.length == 3) {
            String databaseName = args[0].trim();
            String collectionName = args[1].trim();
            String attributeName = extractArgument(args[2]);
            query.setQueryType(QueryType.CREATE_INDEX);
            query.setDatabaseName(databaseName);
            query.setCollectionName(collectionName);
            query.setAttribute(attributeName);
        }

        return query;
    }

    public static Query readCollection(String response) {
        Query query = new Query();
        String[] args = response.split("\\.");
        if (args.length == 2) {
            String databaseName = args[0].trim();
            String collectionName = extractArgument(response);
            query.setQueryType(QueryType.READ_COLLECTION);
            query.setDatabaseName(databaseName);
            query.setCollectionName(collectionName);
        }

        return query;
    }

    public static Query readDocument(String response) {
        Query query = new Query();

        String[] args = response.split("\\.");
        if (args.length == 3) {
            String databaseName = args[0].trim();
            String collectionName = args[1].trim();
            String documentName = extractArgument(response);
            query.setQueryType(QueryType.READ_DOCUMENT);
            query.setDatabaseName(databaseName);
            query.setCollectionName(collectionName);
            query.setDocumentName(documentName);
        }

        return query;
    }

    public static Query updateDocument(String response) {
        Query query = new Query();
        String[] args = response.split("\\.");
        if (args.length == 4) {
            String databaseName = args[0].trim();
            String collectionName = args[1].trim();
            String documentName = args[2].trim();
            String documentContent = extractArgument(response);
            query.setQueryType(QueryType.UPDATE_DOCUMENT);
            query.setDatabaseName(databaseName);
            query.setCollectionName(collectionName);
            query.setDocumentName(documentName);
            query.setDocument(documentContent);
        }

        return query;
    }

    public static Query deleteDatabase(String response) {
        Query query = new Query();
        String databaseName = extractArgument(response);
        query.setQueryType(QueryType.DELETE_DATABASE);
        query.setDatabaseName(databaseName);

        return query;
    }


    public static Query deleteCollection(String response) {
        Query query = new Query();
        String[] args = response.split("\\.");
        if (args.length == 2) {
            String databaseName = args[0].trim();
            String collectionName = extractArgument(response);
            query.setQueryType(QueryType.DELETE_COLLECTION);
            query.setDatabaseName(databaseName);
            query.setCollectionName(collectionName);
        }

        return query;
    }


    public static Query deleteDocument(String response) {
        Query query = new Query();

        String[] args = response.split("\\.");
        if (args.length == 3) {
            String databaseName = args[0].trim();
            String collectionName = args[1].trim();
            String documentName = extractArgument(response);
            query.setQueryType(QueryType.DELETE_DOCUMENT);
            query.setDatabaseName(databaseName);
            query.setCollectionName(collectionName);
            query.setDocumentName(documentName);
        }

        return query;
    }

    public static Query deleteIndex(String response) {
        Query query = new Query();
        String[] args = response.split("\\.");
        if (args.length == 2) {
            String databaseName = args[0].trim();
            String collectionName = extractArgument(response);
            query.setQueryType(QueryType.DELETE_INDEX);
            query.setDatabaseName(databaseName);
            query.setCollectionName(collectionName);
        }

        return query;
    }

    public static Query findEqual(String response) {
        Query query = new Query();

        String[] args = response.split("\\.");
        if (args.length == 3) {
            String databaseName = args[0].trim();
            String collectionName = args[1].trim();
            String attributeContent = extractArgument(response);
            query.setQueryType(QueryType.FIND_EQUAL);
            query.setDatabaseName(databaseName);
            query.setCollectionName(collectionName);
            query.setDocument(attributeContent);
        }

        return query;
    }

    private static String extractArgument(String response) {
        String regex = "\\((.*?)\\)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(response);

        if (matcher.find()) {
            return matcher.group(1).trim();
        } else {
            return "";
        }
    }

}
