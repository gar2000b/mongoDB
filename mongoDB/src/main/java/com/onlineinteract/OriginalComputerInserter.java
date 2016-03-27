package com.onlineinteract;

import com.mongodb.*;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by dev on 15/08/14.
 */
public class OriginalComputerInserter {

    DB db;
    DBCollection coll;
    DBCursor cursor;

 // TODO: uncomment all the following and upgrade to MongoDB 3.2.2 (may not do this)
    
/*    public OriginalComputerInserter() {
        try {
            MongoClient mongoClient = new MongoClient("localhost", 27017);
            db = mongoClient.getDB("mydb");
            // boolean auth = db.authenticate(myUserName, myPassword);
            coll = db.getCollection("computers");
            System.out.println("Connected to MongoDB instance using mydb OK :)");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public void insertEnterprise() {

        BasicDBObject computerDoc = new BasicDBObject("model", "Enterprise 1000");
        computerDoc.append("colour", "Silver");

        List<BasicDBObject> hdds = new ArrayList<BasicDBObject>();

        // ********* HDD 1 ************
        BasicDBObject hdd1Doc = new BasicDBObject("model", "Teranova Fire");
        hdd1Doc.append("speed", "12000 rpm");

        List<BasicDBObject> installedSoftware = new ArrayList<BasicDBObject>();
        BasicDBObject installedSoftware1Doc = new BasicDBObject("name", "Optimiser");


        List<BasicDBObject> files = new ArrayList<BasicDBObject>();

        // Files 1
        BasicDBObject file1Doc = new BasicDBObject("name", "opt.sh");
        file1Doc.append("location", "/progs/opt/");
        file1Doc.append("size", "2.5 Mb");
        files.add(file1Doc);
        // Files 2
        BasicDBObject file2Doc = new BasicDBObject("name", "db.obj");
        file2Doc.append("location", "/progs/opt/");
        file2Doc.append("size", "1.5 Mb");
        files.add(file2Doc);


        installedSoftware1Doc.put("files", files);
        installedSoftware.add(installedSoftware1Doc);
        //hdd1Doc.append("installed_software", installedSoftware1Doc); // and this would go away
        hdd1Doc.put("installed_software", installedSoftware);


        // ********* end: HDD 1 ************

        // *********** HHD 2 ***************
        // ********* end: HDD 2 ************
        hdds.add(hdd1Doc);
        computerDoc.put("HDDs", hdds);

        BasicDBObject videoCardDoc = new BasicDBObject("model", "Royal Blue +");
        videoCardDoc.append("VMemory", new BasicDBObject("size", "6 Gb"));
        videoCardDoc.append("GPU", new BasicDBObject("type", "micro wiz").append("clock_speed", "2GHz"));

        computerDoc.append("video_card", videoCardDoc);

        coll.insert(computerDoc);
    }

    public static void main(String args[]) {
        System.out.println("Computer Inserter Application");
        OriginalComputerInserter originalComputerInserter = new OriginalComputerInserter();
        originalComputerInserter.insertEnterprise();

    }*/
}
