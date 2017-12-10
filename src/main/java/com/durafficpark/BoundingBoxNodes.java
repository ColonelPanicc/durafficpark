package com.durafficpark;

import java.lang.*;

import com.mongodb.*;
import com.mongodb.async.client.*;
import com.mongodb.async.client.MongoClient;
import com.mongodb.async.client.MongoDatabase;
import com.mongodb.async.SingleResultCallback;
import org.bson.Document;
import org.json.simple.JSONObject;

import static com.mongodb.client.model.Filters.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;

public class BoundingBoxNodes {

    private MongoDatabase database;
    final CountDownLatch latch = new CountDownLatch(1);

    public BoundingBoxNodes() {
        MongoClient mongoClient = MongoClients.create(new ConnectionString("mongodb://ollie:goodpeoplegoodtimes@ds135186.mlab.com:35186/?authSource=durassic-park"));

        this.database = mongoClient.getDatabase("durassic-park");
    }

    public List<JSONObject> getWithinBoundingBox(double left, double right, double top, double bottom) {
        List<JSONObject> finalList = Collections.synchronizedList(new ArrayList<>());

        MongoCollection<Document> roadsNodes = this.database.getCollection("ollie_roads");

        Block<Document> getBlock = new Block<Document>() {
            @Override
            public void apply(final Document document) {
                finalList.add(new JSONObject(document));
            }
        };


        SingleResultCallback<Void> callbackWhenFinished = new SingleResultCallback<Void>() {
            @Override
            public void onResult(final Void result, final Throwable t) {
                latch.countDown();
            }
        };

        roadsNodes.find(and(gte("startNode.latitude", left), lte("startNode.latitude", right), gte("startNode.longitude", bottom), lte("startNode.longitude", top),
                        gte("startNode.latitude", left), lte("startNode.latitude", right), gte("startNode.longitude", bottom), lte("startNode.longitude", top))).
                        forEach(getBlock, callbackWhenFinished);

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return finalList;
    }


    public static void main(String[] args) {
        // BoundingBoxNodes bb = new BoundingBoxNodes();
        // System.out.println(bb.getWithinBoundingBox(54.5, 54.8, -1.55, -1.57));
    }
}