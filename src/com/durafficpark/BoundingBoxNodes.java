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
import java.util.concurrent.CountDownLatch;

public class BoundingBoxNodes {

    private MongoDatabase database;
    final CountDownLatch latch = new CountDownLatch(1);

    public BoundingBoxNodes() {
        MongoClient mongoClient = MongoClients.create(new ConnectionString("mongodb://ollie:goodpeoplegoodtimes@ds135186.mlab.com:35186/?authSource=durassic-park"));

        this.database = mongoClient.getDatabase("durassic-park");
    }

    public ArrayList<JSONObject> getWithinBoundingBox(double left, double right, double top, double bottom) {
        ArrayList<JSONObject> finalList = new ArrayList<>();

//        MongoCollection<Document> nodeCollection = this.database.getCollection("nodes");
//        MongoCollection<Document> waysCollection = this.database.getCollection("ways");
        MongoCollection<Document> new_data = this.database.getCollection("ollie_roads");

        Block<Document> getBlock = new Block<Document>() {
            @Override
            public void apply(final Document document) {
                finalList.add(new JSONObject(document));
                System.out.println(document.toJson());
            }
        };


        SingleResultCallback<Void> callbackWhenFinished = new SingleResultCallback<Void>() {
            @Override
            public void onResult(final Void result, final Throwable t) {
                System.out.println("Query Done!");
                latch.countDown();
            }
        };

//        nodeCollection.find(and(gte("lat", left), lte("lat", right), gte("lon", bottom), lte("lon", top))).
//                forEach(getBlock, callbackWhenFinished);
//        waysCollection.find().forEach(getBlock, callbackWhenFinished);
        new_data.find(and(gte("lat", left), lte("lat", right), gte("lon", bottom), lte("lon", top))).
                forEach(getBlock, callbackWhenFinished);

        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return finalList;
    }


    public static void main(String[] args) {
        BoundingBoxNodes bb = new BoundingBoxNodes();
        System.out.println(bb.getWithinBoundingBox(54.5, 54.8, -1.55, -1.57));
    }
}