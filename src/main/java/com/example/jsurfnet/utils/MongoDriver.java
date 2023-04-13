package com.example.jsurfnet.utils;
import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClient;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public final class MongoDriver {

    private static final ConnectionString connectionString = new ConnectionString("mongodb://Amruth:ZGylboqDSEk54hDl@node-shard-00-00.ls94s.mongodb.net:27017,node-shard-00-01.ls94s.mongodb.net:27017,node-shard-00-02.ls94s.mongodb.net:27017/?ssl=true&replicaSet=atlas-brjk9l-shard-0&authSource=admin&retryWrites=true&w=majority");

    private static final MongoClientSettings settings = MongoClientSettings.builder()
            .applyConnectionString(connectionString)
            .build();
    private static final MongoClient mc = MongoClients.create(settings);
    private static final MongoDatabase database = mc.getDatabase("JSurfNet");

    public static MongoDatabase getMongo() {
        return database;
    }

    public static MongoClient getClient() {
        return mc;
    }
}