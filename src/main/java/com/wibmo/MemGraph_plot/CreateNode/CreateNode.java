package com.wibmo.MemGraph_plot.CreateNode;


import com.wibmo.MemGraph_plot.config.MemGraphDataSource;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;
import org.neo4j.driver.async.AsyncSession;
import org.neo4j.driver.async.ResultCursor;
import org.neo4j.driver.summary.ResultSummary;
import org.neo4j.driver.types.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionStage;

@Component
public class CreateNode {

    @Autowired
    MemGraphDataSource memGraphDataSource;


    //var session = driver.session(AsyncSession.class);

    public String createNode2() {
        try (Session session = memGraphDataSource.getDriver().session(SessionConfig.forDatabase("memgraph"))) {
            String str = session.writeTransaction(tx -> {
                tx.run("CREATE (n:Technology {name:'Memgraph'})");
                return "Node successfully created";
            });

            return str;
        }
    }

    public String createNodeWithProperties() {
        Map<String, Object> properties = new HashMap<>();
        properties.put("name", "Memgraph");
        properties.put("type", "graph database");
        String str;
        try (Session session = memGraphDataSource.getDriver().session(SessionConfig.forDatabase("memgraph"))) {
            str = session.writeTransaction(tx -> {
                tx.run("CREATE (n:Technology {name: $name, type: $type})", properties);
                return "Node successfully created";
            });
        }
        return str;
    }


    public List<Record> readNode() {
        List<Record> result;
        try (Session session = memGraphDataSource.getDriver().session(SessionConfig.forDatabase("memgraph"))) {
            result = session.readTransaction(tx -> {
                Result queryResult = tx.run("MATCH (n:Technology{name: 'Memgraph'}) RETURN n");
                List<Record> records = queryResult.list();
                return records;
            });
            System.out.println(result);

            // Print each node as map
            for (Record record : result) {
                System.out.println(record.get("n").asMap());
            }
        }
        return result;
    }

    public List<Record> readNodes() {
        String query = "MATCH (n:Technology {name: 'Memgraph'}) RETURN n;";
        try (Session session = memGraphDataSource.getDriver().session(SessionConfig.forDatabase("memgraph"))) {
            Result result = session.run(query);

            for (Result it = result; it.hasNext(); ) {
                Record record = it.next();
                System.out.println(record.get("n").asMap());

                Node n = record.get("n").asNode();
                System.out.println(n); // node<65>
                System.out.println(n.asMap()); // {name=Memgraph, createdAt=1693400712958077, description=Fastest graph DB in the world!, id=1}
                System.out.println(n.id()); // 65
                System.out.println(n.labels()); // [Technology]
                // System.out.println(n.get("id").asLong()); // 1
                //  System.out.println(n.get("name").asString()); // Memgraph
                // System.out.println(n.get("description").asString()); // Fastest graph DB in the world!
            }

            return result.list();
        }
    }

    //You can use the following transaction management methods via async session API:
    // Here is a basic example of reading data from the database via Async session API:
    public CompletionStage<ResultSummary> readNodesAsync() {
        String query = "MATCH (n:Technology {name: 'Memgraph'}) RETURN n;";
        var session = memGraphDataSource.getDriver().session(AsyncSession.class);

        return session.executeReadAsync(tx -> tx.runAsync(query)
                .thenCompose(cursor -> cursor.forEachAsync(record ->
                        // asynchronously print every record
                        System.out.println(record.get(0).asNode()))));

    }


    //Implicit auto-commit queries are the simplest way to run a Cypher query since they aren't automatically retried like writeTransactionAsync and readTransactionAsync are.
    //
    //Here is an example of an implicit transaction:
    public CompletionStage<ResultSummary> createNodeAsync() {
        String query = "CREATE (n:Technology {name:'Memgraph'})";
        var session = memGraphDataSource.getDriver().session(AsyncSession.class);

        return session.runAsync(query)
                .thenCompose(ResultCursor::consumeAsync)
                .whenComplete((resultSummary, error) -> session.closeAsync());
    }
}
