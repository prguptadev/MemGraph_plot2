package com.wibmo.MemGraph_plot.controller;


import com.wibmo.MemGraph_plot.CreateNode.CreateNode;
import com.wibmo.MemGraph_plot.createRelations.createRelation;
import org.neo4j.driver.Record;
import org.neo4j.driver.summary.ResultSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletionStage;

@RestController
@RequestMapping("/api")
public class MemgraphController {

    @Autowired
    CreateNode node;

    @Autowired
    createRelation createRelation;

    @GetMapping("/createNode")
    public String createNode1() {
        return node.createNode2();
    }

    @GetMapping("/createNodeWithProps")
    public String createNodeWithProps() {
        return node.createNodeWithProperties();
    }

    @GetMapping("/readNode")
    public List<Record> readNode() {
        return node.readNode();
    }

    @GetMapping("/readNodes")
    public List<Record> readNodes() {
        return node.readNodes();
    }

    @GetMapping("/readNodesAsync")
    public CompletionStage<ResultSummary> readNodesAsync() {
        return node.readNodesAsync();
    }

    @GetMapping("/createNodeAsync")
    public CompletionStage<ResultSummary> createNodeAsync() {
        return node.createNodeAsync();
    }

    @GetMapping("/createRelationship")
    public String createRelationship() {
        return createRelation.createRelationship();
    }

    @GetMapping("/readRelationship")
    public List<Record> readRelationship() {
        return createRelation.readRelationship();
    }


    @GetMapping("/readPath")
    public List<Record> readPath() {
        return createRelation.readPath();
    }


}
