package org.example.Factories;
import org.example.models.Query;
import org.example.models.QueryType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
class QueryFactoryTest {
    QueryFactory queryFactory=new QueryFactory();
    @Test
    void createDatabaseQuery() {
        String response="CREATE_DATABASE(db2)";
        Query query = queryFactory.createQuery(response);
        assertNotNull(query);
        assertEquals(QueryType.CREATE_DATABASE, query.getQueryType());
        assertEquals("db2", query.getDatabaseName());
    }

    @Test
    void createCollectionQuery() {
        String response = "db2.CREATE_COLLECTION(c1)";
        Query query = queryFactory.createQuery(response);
        assertNotNull(query);
        assertEquals(QueryType.CREATE_COLLECTION, query.getQueryType());
        assertEquals("db2", query.getDatabaseName());
        assertEquals("c1", query.getCollectionName());
    }

    @Test
    void createDocumentQuery() {
        String response = "db2.c1.CREATE_DOCUMENT({id:\"1\",length:159,weight:70})";
        Query query = queryFactory.createQuery(response);
        assertNotNull(query);
        assertEquals(QueryType.CREATE_DOCUMENT, query.getQueryType());
        assertEquals("db2", query.getDatabaseName());
        assertEquals("c1", query.getCollectionName());
        assertEquals("{id:\"1\",length:159,weight:70}", query.getDocument());
    }
    @Test
    void readDocumentQuery() {
        String response = "db2.c1.READ_DOCUMENT(1)";
        Query query = queryFactory.createQuery(response);
        assertNotNull(query);
        assertEquals(QueryType.READ_DOCUMENT, query.getQueryType());
        assertEquals("db2", query.getDatabaseName());
        assertEquals("c1", query.getCollectionName());
        assertEquals("1", query.getDocumentName());
    }

    @Test
    void updateDocumentQuery() {
        String response = "db2.c1.1.UPDATE_DOCUMENT({firstName:\"saif\",lastName:\"jihad\",length:175})";
        Query query = queryFactory.createQuery(response);
        assertNotNull(query);
        assertEquals(QueryType.UPDATE_DOCUMENT, query.getQueryType());
        assertEquals("db2", query.getDatabaseName());
        assertEquals("c1", query.getCollectionName());
        assertEquals("1", query.getDocumentName());
        assertEquals("{firstName:\"saif\",lastName:\"jihad\",length:175}", query.getDocument());
    }

    @Test
    void deleteIndexQuery() {
        String response = "db2.DELETE_INDEX(c2)";
        Query query = queryFactory.createQuery(response);
        assertNotNull(query);
        assertEquals(QueryType.DELETE_INDEX, query.getQueryType());
        assertEquals("db2", query.getDatabaseName());
        assertEquals("c2", query.getCollectionName());
    }

    @Test
    void deleteDocumentQuery() {
        String response = "db1.c1.DELETE_DOCUMENT(4)";
        Query query = queryFactory.createQuery(response);
        assertNotNull(query);
        assertEquals(QueryType.DELETE_DOCUMENT, query.getQueryType());
        assertEquals("db1", query.getDatabaseName());
        assertEquals("c1", query.getCollectionName());
        assertEquals("4", query.getDocumentName());
    }

    @Test
    void deleteCollectionQuery() {
        String response = "db2.DELETE_COLLECTION(c2)";
        Query query = queryFactory.createQuery(response);
        assertNotNull(query);
        assertEquals(QueryType.DELETE_COLLECTION, query.getQueryType());
        assertEquals("db2", query.getDatabaseName());
        assertEquals("c2", query.getCollectionName());
    }

    @Test
    void deleteDatabaseQuery() {
        String response = "DELETE_DATABASE(db2)";
        Query query = queryFactory.createQuery(response);
        assertNotNull(query);
        assertEquals(QueryType.DELETE_DATABASE, query.getQueryType());
        assertEquals("db2", query.getDatabaseName());
    }

    @Test
    void readCollectionQuery() {
        String response = "db1.READ_COLLECTION(c1)";
        Query query = queryFactory.createQuery(response);
        assertNotNull(query);
        assertEquals(QueryType.READ_COLLECTION, query.getQueryType());
        assertEquals("db1", query.getDatabaseName());
        assertEquals("c1", query.getCollectionName());
    }

    @Test
    void findEqualQuery() {
        String response = "db1.c1.FIND_EQUAL({length:159})";
        Query query = queryFactory.createQuery(response);
        assertNotNull(query);
        assertEquals(QueryType.FIND_EQUAL, query.getQueryType());
        assertEquals("db1", query.getDatabaseName());
        assertEquals("c1", query.getCollectionName());
        assertEquals("{length:159}", query.getDocument());
    }

    @Test
    void createIndexQuery() {
        String response = "db1.c1.CREATE_INDEX(length)";
        Query query = queryFactory.createQuery(response);
        assertNotNull(query);
        assertEquals(QueryType.CREATE_INDEX, query.getQueryType());
        assertEquals("db1", query.getDatabaseName());
        assertEquals("c1", query.getCollectionName());
        assertEquals("length", query.getAttribute());
    }

}
