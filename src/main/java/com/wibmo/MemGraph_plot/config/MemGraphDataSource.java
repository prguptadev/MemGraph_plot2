package com.wibmo.MemGraph_plot.config;

import lombok.Getter;
import lombok.Setter;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MemGraphDataSource implements AutoCloseable {

    String uri = "bolt://192.168.8.130:7687";
    String username = "memgraph";
    String password = "memgraph";

    @Getter
    @Setter
    private Driver driver = GraphDatabase.driver(uri, AuthTokens.basic(username, password));


    public static void main(String[] args) {

//        try (var helloMemgraph = new MemGraphDataSource(uri, username, password)) {
//            // Use the driver
//        }
    }

    @Override
    public void close() throws RuntimeException {
        driver.close();
    }
}