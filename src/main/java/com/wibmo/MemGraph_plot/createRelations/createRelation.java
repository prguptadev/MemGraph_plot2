package com.wibmo.MemGraph_plot.createRelations;

import com.wibmo.MemGraph_plot.config.MemGraphDataSource;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.SessionConfig;
import org.neo4j.driver.types.Path;
import org.neo4j.driver.types.Relationship;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class createRelation {

    @Autowired
    MemGraphDataSource memGraphDataSource;


    //Create relationship
    public String createRelationship() {
        String str;
        try (Session session = memGraphDataSource.getDriver().session(SessionConfig.forDatabase("memgraph"))) {
            String query = "CREATE (d:Developer {name: 'John Doe'})-[:LOVES {id:99}]->(t:Technology {id: 1, name:'Memgraph', description: 'Fastest graph DB in the world!', createdAt: date()})";
            str = session.writeTransaction(tx -> {
                tx.run(query);
                return "Relation created";
            });

        }

        return str;
    }

    //Read relationship
    public List<Record> readRelationship() {
        try (Session session = memGraphDataSource.getDriver().session(SessionConfig.forDatabase("memgraph"))) {
            String query = "MATCH (d:Developer)-[r:LOVES]->(t:Technology) RETURN r";
            List<Record> result = session.readTransaction(tx -> {
                Result queryResult = tx.run(query);
                List<Record> records = queryResult.list();


                //Process results
                for (Record rel : records) {
                    Relationship relationship = rel.get("r").asRelationship();
                    System.out.println(relationship);
                    System.out.println(relationship.asMap());
                    System.out.println(relationship.id());
                    System.out.println(relationship.type());
                    System.out.println(relationship.startNodeId());
                    System.out.println(relationship.endNodeId());
                    System.out.println(relationship.get("id").asLong());
                }

                return records;
            });
            return result;
        }
    }


    public List<Record> readPath() {
        String query = "MATCH path=(d:Developer)-[r:LOVES]->(t:Technology) RETURN path";
        try (Session session = memGraphDataSource.getDriver().session(SessionConfig.forDatabase("memgraph"))) {
            List<Record> result = session.readTransaction(tx -> {
                Result queryResult = tx.run(query);
                List<Record> records = queryResult.list();

                for (Record path : records) {
                    Path p = path.get("path").asPath();
                    System.out.println(p);
                    System.out.println(p.start());
                    System.out.println(p.end());
                    System.out.println(p.length());
                    System.out.println(p.nodes());
                    System.out.println(p.relationships());
                }

                return records;
            });
            return result;
        }
    }


}
