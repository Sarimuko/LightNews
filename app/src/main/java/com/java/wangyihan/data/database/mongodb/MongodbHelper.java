package com.java.wangyihan.data.database.mongodb;

import com.java.wangyihan.data.model.Comment;
import com.java.wangyihan.data.model.User;
import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Collation;
import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;

import java.util.ArrayList;
import java.util.List;

public class MongodbHelper {

    private MongoClient mongoClient;
    private MongoDatabase mongoDatabase;

    private static MongodbHelper instance = new MongodbHelper();
    private MongodbHelper()
    {
        try{
            // 连接到 mongodb 服务
            MongoClient mongoClient = new MongoClient( "123.206.43.232" , 27017 );

            // 连接到数据库
            MongoDatabase mongoDatabase = mongoClient.getDatabase("LightNews");
            System.out.println("Connect to database successfully");

        }catch(Exception e){
            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
        }
    }

    public static MongodbHelper getInstance()
    {
        return instance;
    }

    public void addComment(User user, String title, String comment)
    {
        MongoCollection<Document> collection = mongoDatabase.getCollection("comment");
        Document document = new Document("username", user.getUsername()).append("comment", comment).append("title", title);
        List<Document> documents = new ArrayList<>();
        documents.add(document);
        collection.insertMany(documents);
    }

    public List<Comment> getCommentByTitle(String title)
    {
        MongoCollection<Document> collection = mongoDatabase.getCollection("comment");

        Document query = new Document("title", title);
        FindIterable<Document> res = collection.find(query);
        final List<Comment> commentList = new ArrayList<Comment>();

        res.forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
                commentList.add(new Comment(document.getString("username"), document.getString("comment")));
            }
        });

        return commentList;
    }

}
