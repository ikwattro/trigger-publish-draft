package com.ikwattro.publisher;

import org.junit.Rule;
import org.junit.Test;
import org.neo4j.driver.v1.*;
import org.neo4j.harness.junit.Neo4jRule;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class NotificationTest {

    @Rule
    public Neo4jRule neo4j = new Neo4jRule()

            // This is the Procedure we want to test
            .withProcedure( PublisherProcedure.class );

    @Test
    public void testNotification() {
        try( Driver driver = GraphDatabase.driver( neo4j.boltURI() , Config.build().withoutEncryption().toConfig() ) )
        {
            Session session = driver.session();
            Map<String, Object> params = new HashMap<>();
            params.put("url", "https://demo.mercure.rocks/hub");
            params.put("topic", "https://demo.mercure.rocks/demo/books/1.jsonld");
            params.put("body", "{\"value\":\"" + System.currentTimeMillis() + "\"}");
            params.put("params", Collections.singletonMap("token", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJtZXJjdXJlIjp7InN1YnNjcmliZSI6WyJmb28iLCJiYXIiXSwicHVibGlzaCI6WyJmb28iXX19.LRLvirgONK13JgacQ_VbcjySbVhkSmHy3IznH3tA9PM"));

            // When I use the index procedure to index a node
            session.run( "CALL ga.notify($url, $topic, $body, $params)", params);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
