package uk.co.slc;

import com.mongodb.*;

import java.net.UnknownHostException;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class MongoDB {

    DB db;
    DBCollection coll;
    DBCursor cursor;

    public MongoDB() {
        try {
            MongoClient mongoClient = new MongoClient("localhost", 27017);
            db = mongoClient.getDB("mydb");
            // boolean auth = db.authenticate(myUserName, myPassword);
            coll = db.getCollection("testCollection");
            System.out.println("Connected to MongoDB instance using mydb OK :)");

            System.out.println("\nGetting Database Names");

            for (String s : mongoClient.getDatabaseNames()) {
                System.out.println(s);
            }

            System.out.println("Getting Database Names Complete\n");

            // The following are admin functions that will all be commented out.

            // Dropping a database
            // mongoClient.dropDatabase("databaseToBeDropped");

            // Creating a collection
//            db.createCollection("testCollection", new BasicDBObject("capped", true)
//                    .append("size", 1048576));

            // Creating a list of collections
//            for (String s : db.getCollectionNames()) {
//                System.out.println(s);
//            }

            // Dropping a collection
//            DBCollection testCollection = db.getCollection("testCollection");
//            testCollection.drop();
//            System.out.println(db.getCollectionNames());

            // Getting a list of indexes on a collection
//            List<DBObject> list = coll.getIndexInfo();
//
//            for (DBObject o : list) {
//                System.out.println(o.get("key"));
//            }

            // Creating an index
//            coll.createIndex(new BasicDBObject("i", 1));  // create index on "i", ascending

            // Geo Indexes and Text Indexes omitted here...

        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void listCollections() {
        Set<String> colls = db.getCollectionNames();

        for (String s : colls) {
            System.out.println(s);
        }
    }

    public void insertIntoCollection() {

        BasicDBObject doc = new BasicDBObject("Name", "MongoDB");
        doc.append("type", "database");
        doc.append("count", 1);
        doc.append("info", new BasicDBObject("x", 203).append("y", 102));
        coll.insert(doc);

        System.out.println("New doc:\n " + doc.toString() + "\n\nInserted\n");
    }

    public void insertMultipleValuesIntoCollection() {

        for (int i = 0; i < 100; i++) {
            coll.insert(new BasicDBObject("i", i));
        }

        System.out.println("\n100 objects inserted into testCollection.");

    }

    public void findOne() {

        DBObject myDoc = coll.findOne();
        System.out.println(myDoc);
    }

    public void getCount() {

        System.out.println(coll.getCount());
    }

    public void getAllDocuments() {
        cursor = coll.find();

        try {
            while (cursor.hasNext()) {
                System.out.println(cursor.next());
            }
        } finally {
            cursor.close();
        }
    }

    public void getSingleDocumentWithQuery() {
        BasicDBObject query = new BasicDBObject("i", 64);

        cursor = coll.find(query);

        try {
            while (cursor.hasNext()) {
                System.out.println(cursor.next());
            }
        } finally {
            cursor.close();
        }
        System.out.println("----\n");
    }

    public void executeQuery1() {
        BasicDBObject query = new BasicDBObject("i", new BasicDBObject("$gte", 60));

        cursor = coll.find(query);

        try {
            while (cursor.hasNext()) {
                System.out.println(cursor.next());
            }
        } finally {
            cursor.close();
        }
    }

    public void executeQuery2() {
        BasicDBObject query = new BasicDBObject("i", new BasicDBObject("$gte", 60).append("$lte", 90));
        System.out.println(query.toString());

        cursor = coll.find(query).maxTime(1, TimeUnit.SECONDS);

        try {
            while (cursor.hasNext()) {
                System.out.println(cursor.next());
            }
        } finally {
            cursor.close();
        }
    }

    public void executeQuery3() {


        DBObject clause1 = new BasicDBObject("i", 50);
        DBObject clause2 = new BasicDBObject("i", 60);
        BasicDBList or = new BasicDBList();
        or.add(clause1);
        or.add(clause2);
        DBObject query = new BasicDBObject("$or", or);

        System.out.println(query.toString());

        cursor = coll.find(query).maxTime(1, TimeUnit.SECONDS);

        try {
            while (cursor.hasNext()) {
                System.out.println(cursor.next());
            }
        } finally {
            cursor.close();
        }
    }

    public void executeQuery4() {

        BasicDBObject clause1 = new BasicDBObject("i", new BasicDBObject("$gte", 60).append("$lte", 90));
        System.out.println(clause1.toString());

        DBObject clause2 = new BasicDBObject("i", 50);
        BasicDBList or = new BasicDBList();
        or.add(clause1);
        or.add(clause2);
        DBObject query = new BasicDBObject("$or", or);

        cursor = coll.find(query).maxTime(1, TimeUnit.SECONDS);

        try {
            while (cursor.hasNext()) {
                System.out.println(cursor.next());
            }
        } finally {
            cursor.close();
        }
    }

    public void bulkOperations() {
        // 1. Ordered Bulk Operations
        BulkWriteOperation builder = coll.initializeOrderedBulkOperation();
        builder.insert(new BasicDBObject("_id", 1));
        builder.insert(new BasicDBObject("_id", 2));
        builder.insert(new BasicDBObject("_id", 3));

        builder.find(new BasicDBObject("_id", 1)).updateOne(new BasicDBObject("$set", new BasicDBObject("x", 2)));
        builder.find(new BasicDBObject("_id", 2)).removeOne();
        builder.find(new BasicDBObject("_id", 3)).replaceOne(new BasicDBObject("_id", 3).append("x", 4));

        BulkWriteResult result = builder.execute();

        // 2. Unordered bulk operation - no guarantee of order of operation

        builder = coll.initializeUnorderedBulkOperation();
        builder.find(new BasicDBObject("_id", 1)).removeOne();
        builder.find(new BasicDBObject("_id", 2)).removeOne();

        result = builder.execute();

        System.out.println("Bulk Operations complete");
    }

    public static void main(String[] args) {

        System.out.println("MongoDB Driver Test");
        MongoDB mongoDB = new MongoDB();
        mongoDB.listCollections();
        // mongoDB.insertIntoCollection();
        mongoDB.findOne();
        // mongoDB.insertMultipleValuesIntoCollection();
        mongoDB.getCount();
        // mongoDB.getAllDocuments();
        mongoDB.getSingleDocumentWithQuery();
        // mongoDB.executeQuery1();
        // mongoDB.executeQuery2();
        // mongoDB.executeQuery3();
        mongoDB.executeQuery4();
        // mongoDB.bulkOperations();
    }
}