package org.example.models;

import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectStreamException;
import java.io.Serializable;

public class AffinityRequest implements Serializable {
    private String hashcode;
    private String databaseName;
    private String collectionName;
    private String documentName;
    private String document;
    private QueryType queryType;
    private Object writeReplace() throws ObjectStreamException {
        return new SerializationProxy(this);
    }

    private void readObject(ObjectInputStream stream) throws InvalidObjectException {
        if ( queryType == null) {
            throw new InvalidObjectException("QueryType cant be null");
        }
        throw new InvalidObjectException("Proxy required");
    }

    private static class SerializationProxy implements Serializable {
        private String hashcode;
        private String databaseName;
        private String collectionName;
        private String documentName;
        private String document;
        private QueryType queryType;

        SerializationProxy(AffinityRequest request) {
            this.hashcode = request.getHashcode();
            this.databaseName = request.getDatabaseName();
            this.collectionName = request.getCollectionName();
            this.documentName = request.getDocumentName();
            this.document = request.getDocument();
            this.queryType = request.getQueryType();
        }

        private Object readResolve() throws ObjectStreamException {
            AffinityRequest request = new AffinityRequest();
            request.setHashcode(hashcode);
            request.setDatabaseName(databaseName);
            request.setCollectionName(collectionName);
            request.setDocumentName(documentName);
            request.setDocument(document);
            request.setQueryType(queryType);
            return request;
        }
    }
    public AffinityRequest() {
    }

    public String getHashcode() {
        return hashcode;
    }

    public void setHashcode(String hashcode) {
        this.hashcode = hashcode;
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

    public QueryType getQueryType() {
        return queryType;
    }

    public void setQueryType(QueryType queryType) {
        this.queryType = queryType;
    }

    @Override
    public String toString() {
        return "AffinityRequest{" +
                "hashcode='" + hashcode + '\'' +
                ", databaseName='" + databaseName + '\'' +
                ", collectionName='" + collectionName + '\'' +
                ", documentName='" + documentName + '\'' +
                ", document='" + document + '\'' +
                '}';
    }
}
