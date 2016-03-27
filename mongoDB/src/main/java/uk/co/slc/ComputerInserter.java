package uk.co.slc;

import com.mongodb.*;
import com.mongodb.util.JSON;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by dev on 15/08/14.
 */
public class ComputerInserter {

    DB db;
    DBCollection coll;
    DBCursor cursor;

    public ComputerInserter() {
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

        BasicDBObject computerDoc = createComputer("Enterprise 1000", "Silver");

        List<BasicDBObject> hdds = new ArrayList<BasicDBObject>();

        // ********* HDD 1 ************

        List<BasicDBObject> installedSoftware = new ArrayList<BasicDBObject>();
        List<BasicDBObject> files = new ArrayList<BasicDBObject>();

        files.add(createFile("opt.sh", "/progs/opt/", "2.5 Mb"));
        files.add(createFile("db.obj", "/progs/opt/", "1.5 Mb"));

        installedSoftware.add(createSoftware("Optimiser", files));
        BasicDBObject hdd1Doc = createHDD("Teranova Fire", "12000 rpm");
        hdd1Doc.put("installed_software", installedSoftware);

        hdds.add(hdd1Doc);

        // ********* end: HDD 1 ************

        // *********** HHD 2 ***************
        installedSoftware = new ArrayList<BasicDBObject>();
        files = new ArrayList<BasicDBObject>();
        List<BasicDBObject> files2 = new ArrayList<BasicDBObject>();

        files.add(createFile("cadmad.exe", "c:\\progs\\cad-master", "4.5 Mb"));
        files.add(createFile("sys.ini", "c:\\progs\\cad-master", "20 Kb"));

        files2.add(createFile("gg.exe", "c:\\progs\\games-galore", "8 Mb"));

        installedSoftware.add(createSoftware("CAD Master", files));
        installedSoftware.add(createSoftware("Games Galore", files2));
        BasicDBObject hdd2Doc = createHDD("Felix", "85000 rpm");
        hdd2Doc.put("installed_software", installedSoftware);

        hdds.add(hdd2Doc);
        // ********* end: HDD 2 ************

        computerDoc.put("HDDs", hdds);

        BasicDBObject videoCardDoc = createVideoCard("Royal Blue +", "6 Gb", "micro wiz", "2GHz");

        computerDoc.append("video_card", videoCardDoc);

        coll.insert(computerDoc);
    }

    public void insertJupiterElite() {

        BasicDBObject computerDoc = createComputer("Jupiter Elite", "White");

        List<BasicDBObject> hdds = new ArrayList<BasicDBObject>();

        // ********* HDD 1 ************

        List<BasicDBObject> installedSoftware = new ArrayList<BasicDBObject>();
        List<BasicDBObject> files = new ArrayList<BasicDBObject>();

        files.add(createFile("sn.sh", "/progs/sn/", "20 Mb"));
        files.add(createFile("dbm.obj", "/progs/sn/", "4 Mb"));

        installedSoftware.add(createSoftware("Simulation Nation", files));
        BasicDBObject hdd1Doc = createHDD("Gigabit", "15000 rpm");
        hdd1Doc.put("installed_software", installedSoftware);

        hdds.add(hdd1Doc);

        // ********* end: HDD 1 ************

        // *********** HHD 2 ***************
        installedSoftware = new ArrayList<BasicDBObject>();
        files = new ArrayList<BasicDBObject>();
        List<BasicDBObject> files2 = new ArrayList<BasicDBObject>();

        files.add(createFile("Deep-Space.exe", "c:\\progs\\deepspace", "30 Mb"));
        files.add(createFile("ds.ini", "c:\\progs\\deepspace", "35 Kb"));

        files2.add(createFile("pru.exe", "c:\\progs\\pru", "50 Mb"));

        installedSoftware.add(createSoftware("Deep Space", files));
        installedSoftware.add(createSoftware("Physics RU", files2));
        BasicDBObject hdd2Doc = createHDD("Sonafield", "16000 rpm");
        hdd2Doc.put("installed_software", installedSoftware);

        hdds.add(hdd2Doc);
        // ********* end: HDD 2 ************

        computerDoc.put("HDDs", hdds);

        BasicDBObject videoCardDoc = createVideoCard("Rocket 220", "16 Gb", "micro master", "16GHz");

        computerDoc.append("video_card", videoCardDoc);

        coll.insert(computerDoc);
    }

    private BasicDBObject createFile(String name, String location, String size) {

        BasicDBObject fileDoc = new BasicDBObject("name", name);
        fileDoc.append("location", location);
        fileDoc.append("size", size);
        return fileDoc;
    }

    private BasicDBObject createSoftware(String name, List<BasicDBObject> files) {

        BasicDBObject installedSoftware1Doc = new BasicDBObject("name", name);
        installedSoftware1Doc.put("files", files);

        return installedSoftware1Doc;
    }

    private BasicDBObject createHDD(String model, String speed) {

        BasicDBObject hdd1Doc = new BasicDBObject("model", model);
        hdd1Doc.append("speed", speed);

        return  hdd1Doc;
    }

    private BasicDBObject createComputer(String model, String colour) {

        BasicDBObject computerDoc = new BasicDBObject("model", model);
        computerDoc.append("colour", colour);

        return computerDoc;
    }

    private BasicDBObject createVideoCard(String model, String size, String type, String clockSpeed) {
        BasicDBObject videoCardDoc = new BasicDBObject("model", model);
        videoCardDoc.append("VMemory", new BasicDBObject("size", size));
        videoCardDoc.append("GPU", new BasicDBObject("type", type).append("clock_speed", clockSpeed));

        return videoCardDoc;
    }

    private void query1() {

        cursor = coll.find();

        try {
            while (cursor.hasNext()) {
                System.out.println(cursor.next());
            }
        } finally {
            cursor.close();
        }
    }

    private void query2() {


       CommandResult commmandResult = db.doEval("db.computers.find().forEach(function(x){db.comptemp.save(x);})");
       System.out.println(commmandResult.toString());
    }

    private void findVideoCardByMemory() {

        // BasicDBObject query = new BasicDBObject("model", "xCalibre");

        BasicDBObject q = new BasicDBObject("video_card.VMemory.size", new BasicDBObject("$gte", 5));

        BasicDBObject fields = new BasicDBObject();
        fields.put("model", 1);
        fields.put("video_card.VMemory.size", 1);
        fields.put("_id", 0);

        System.out.println(q.toString());

        cursor = coll.find(q, fields).maxTime(1, TimeUnit.SECONDS);

        try {
            while (cursor.hasNext()) {
                System.out.println(cursor.next());
            }
        } finally {
            cursor.close();
        }

    }

    public static void main(String args[]) {
        System.out.println("Computer Inserter Application");
        ComputerInserter computerInserter = new ComputerInserter();
        // computerInserter.insertEnterprise();
        // computerInserter.insertJupiterElite();
        // computerInserter.query1();
        // computerInserter.query2();
        computerInserter.findVideoCardByMemory();
    }
}
