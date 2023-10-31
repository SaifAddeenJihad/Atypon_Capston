package org.example.models;

import java.io.Serializable;

public class Query implements Serializable {
    private String databaseName;
    private String collectionName;
    private String documentName;
    private String attribute;
    private QueryType queryType;
    private String document;

    public Query() {
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }


    public QueryType getQueryType() {
        return queryType;
    }

    public void setQueryType(QueryType queryType) {
        this.queryType = queryType;
    }


    public String getDatabaseName() {
        return databaseName;
    }

    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public String getDocumentName() {
        return documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    @Override
    public String toString() {
        return "Query{" +
                "databaseName='" + databaseName + '\'' +
                ", collectionName='" + collectionName + '\'' +
                ", documentName='" + documentName + '\'' +
                ", attribute='" + attribute + '\'' +
                ", queryType=" + queryType +
                ", document=" + document +
                '}';
    }
}
