package org.example.services;

import org.example.Cache.DocumentCache;
import org.example.balncer.AffinityBalancer;
import org.example.communicator.SenderHelper.NodeAffinityHelper;
import org.example.communicator.SenderHelper.NodeBroadcastHelper;
import org.example.disk.DocumentOperation;
import org.example.indexer.IndexerManager;
import org.example.lock.Locker;
import org.example.models.Query;
import org.example.models.Result;
import org.example.models.Status;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

import static org.example.disk.DiskUtils.documentExists;

public class DocumentService {
    private static int nodeId;

    public static void setNodeId(int nodeId) {
        DocumentService.nodeId = nodeId;
    }

    private DocumentService() {

    }

    public static Result creatDocument(Query query) {
        Result result = creatStartingResult();

        result.setMessage("Document has no id");
        JSONObject document = new JSONObject(query.getDocument());
        String documentName="";
        if(document.has("id"))
            documentName = String.valueOf(document.get("id"));
        else return result;
        result.setMessage("Document is already created");
        String collectionName = query.getCollectionName();
        String databaseName = query.getDatabaseName();
        if (documentExists(databaseName, collectionName, documentName))
            return result;
        int affinityNodeId = AffinityBalancer.choseAffinity();
        if (!document.has("affinity"))
            document.put("affinity", affinityNodeId);
        query.setDocument(document.toString());
        ReentrantLock reentrantLock = getLock(databaseName + "/" + collectionName + "/" + documentName);
        reentrantLock.lock();
        try {
            IndexerManager.getInstance().addToIndex(databaseName, collectionName, document);
            DocumentOperation.createDocument(databaseName, collectionName, document);
            result.setStatus(Status.OK);
            result.setMessage("Document has been created");
            NodeBroadcastHelper.broadcastQuery(query);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            reentrantLock.unlock();
        }
        return result;
    }

    public static Result deleteDocument(Query query) throws IOException {
        Result result = creatStartingResult();

        result.setMessage("There is no such document to delete");
        String documentName = query.getDocumentName();
        String collectionName = query.getCollectionName();
        String databaseName = query.getDatabaseName();
        if (!documentExists(databaseName, collectionName, documentName))
            return result;
        JSONObject jsonObject = DocumentOperation.readDocument(databaseName, collectionName, documentName);
        //check affinity
        result.setMessage("Document has been deleted ");
        affinityCheck(query, result, jsonObject);
        if (result.getStatus().equals(Status.OK)) return result;
        ReentrantLock reentrantLock = getLock(databaseName + "/" + collectionName + "/" + documentName);
        reentrantLock.lock();
        try {
            DocumentOperation.deleteDocument(databaseName, collectionName, documentName);
            IndexerManager.getInstance().deleteFromIndex(databaseName, collectionName, jsonObject);
            result.setStatus(Status.OK);
            NodeBroadcastHelper.broadcastQuery(query);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            reentrantLock.unlock();
        }
        return result;
    }

    public static Result updateDocument(Query query) throws IOException {
        Result result = creatStartingResult();

        result.setMessage("There is no such document to update");
        String documentName = query.getDocumentName();
        String collectionName = query.getCollectionName();
        String databaseName = query.getDatabaseName();
        if (!documentExists(databaseName, collectionName, documentName))
            return result;
        JSONObject oldObject = DocumentOperation.readDocument(databaseName, collectionName, documentName);
        result.setMessage("Document has been updated ");
        affinityCheck(query, result, oldObject);
        if (result.getStatus().equals(Status.OK)) return result;
        JSONObject document = new JSONObject(query.getDocument());
        ReentrantLock reentrantLock = getLock(databaseName + "/" + collectionName + "/" + documentName);
        reentrantLock.lock();
        try {
            DocumentOperation.updateDocument(databaseName, collectionName, documentName, document);
            IndexerManager.getInstance().updateToIndex(databaseName, collectionName, oldObject, document.put("id", documentName));
            result.setStatus(Status.OK);
            NodeBroadcastHelper.broadcastQuery(query);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            reentrantLock.unlock();
        }
        return result;
    }

    public static Result readDocument(Query query) {
        Result result = creatStartingResult();
        result.setMessage("There is no such document to read");

        String documentName = query.getDocumentName();
        String collectionName = query.getCollectionName();
        String databaseName = query.getDatabaseName();
        if (!documentExists(databaseName, collectionName, documentName))
            return result;

        try {
            JSONObject jsonObject = DocumentOperation.readDocument(databaseName, collectionName, documentName);
            result.setStatus(Status.OK);
            DocumentCache.getInstance().putDocument(databaseName, collectionName, documentName, jsonObject);
            result.setMessage("Document has been read successfully");
            result.addToJsonArray(jsonObject);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void updateBroadcastDocument(Query query) throws IOException {
        String documentName = query.getDocumentName();
        String collectionName = query.getCollectionName();
        String databaseName = query.getDatabaseName();
        JSONObject oldObject = DocumentOperation.readDocument(databaseName, collectionName, documentName);
        JSONObject document = new JSONObject(query.getDocument());
        ReentrantLock reentrantLock = getLock(databaseName + "/" + collectionName + "/" + documentName);
        reentrantLock.lock();
        try {
            DocumentOperation.updateDocument(databaseName, collectionName, documentName, document);
            IndexerManager.getInstance().updateToIndex(databaseName, collectionName, oldObject, document.put("id", documentName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            reentrantLock.unlock();
        }
    }

    public static void deleteBroadcastDocument(Query query) throws IOException {
        String documentName = query.getDocumentName();
        String collectionName = query.getCollectionName();
        String databaseName = query.getDatabaseName();
        JSONObject jsonObject = DocumentOperation.readDocument(databaseName, collectionName, documentName);

        ReentrantLock reentrantLock = getLock(databaseName + "/" + collectionName + "/" + documentName);
        reentrantLock.lock();
        try {
            DocumentOperation.deleteDocument(databaseName, collectionName, documentName);
            IndexerManager.getInstance().deleteFromIndex(databaseName, collectionName, jsonObject);
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            reentrantLock.unlock();
        }

    }

    private static ReentrantLock getLock(String lockId) {
        return Locker.getLock(lockId);
    }


    private static void affinityCheck(Query query, Result result, JSONObject jsonObject) throws IOException {
        if (nodeId != (Integer) jsonObject.get("affinity")) {
            NodeAffinityHelper.sendAffinity(query, (Integer) jsonObject.get("affinity"));
            result.setStatus(Status.OK);
        }

    }

    private static Result creatStartingResult() {
        Result result = new Result();
        result.setStatus(Status.ERROR);
        return result;
    }
}
