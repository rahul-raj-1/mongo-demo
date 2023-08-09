package com.mongodb.demo.service;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.result.DeleteResult;

@Service
public class GenericService {

    @Value("${page.size}")
    private int pageSize;

    @Autowired
    private  MongoTemplate mongoTemplate;

   

    // Delete documents by date and return the count of deleted documents
    public long deleteDocumentsByDate(String collectionName, LocalDate date, String lenderName) {
        LocalDateTime startDateTime = date.atStartOfDay();
        LocalDateTime endDateTime = date.atTime(LocalTime.MAX);

        Query query = new Query(Criteria.where("created")
                .gte(startDateTime.toString())
                .lt(endDateTime.plusNanos(1).toString()));

        if (lenderName != null && !lenderName.isEmpty()) {
            query.addCriteria(Criteria.where("lenderName").is(lenderName));
        }

        DeleteResult result = mongoTemplate.remove(query, Document.class, collectionName);
        long deletedCount = result.getDeletedCount();

        System.out.println("deletedCount: " + deletedCount);

        return deletedCount;
    }

    

    // Write documents to a JSON file by date using separate methods
    public boolean writeDocumentsToJsonByDate(String collectionName, LocalDate date, String outputPath) {
        LocalDateTime startDateTime = date.atStartOfDay();
        LocalDateTime endDateTime = date.atTime(LocalTime.MAX);

        Query query = new Query(Criteria.where("created")
                .gte(startDateTime.toString())
                .lt(endDateTime.plusNanos(1).toString()));

        long totalCount = mongoTemplate.count(query, Document.class, collectionName);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            int processedCount = 0;
            int writtenCount = 0;
            int pageNum = 0;

            while (processedCount < totalCount) {
                List<Document> documents = fetchDocumentsByPage(collectionName, query, pageNum);

                System.out.println("Documents fetched: " + documents.size());

                writtenCount = writtenCount + writeDocumentsToFile(documents, writer);
                processedCount = processedCount + documents.size();
                pageNum++;

                System.out.println("Processed " + processedCount + " documents.");
            }

            if (writtenCount != totalCount) {
                System.out.println("Mismatch between fetched and written documents!");
                return false;
            } else {
                System.out.println("All documents fetched and written successfully." + writtenCount);
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private List<Document> fetchDocumentsByPage(String collectionName, Query query, int pageNum) {
        query.limit(pageSize).skip(pageNum * pageSize);
        return mongoTemplate.find(query, Document.class, collectionName);
    }
    
    private List<Document> fetchDocumentsByPage(String collectionName, String lenderName, Query query, int pageNum) {
        if (lenderName != null && !lenderName.isEmpty()) {
            query.addCriteria(Criteria.where("lenderName").is(lenderName));
        }
        query.limit(pageSize).skip(pageNum * pageSize);
        return mongoTemplate.find(query, Document.class, collectionName);
    }


    private int writeDocumentsToFile(List<Document> documents, BufferedWriter writer) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        int writtenCount = 0;
        for (Document document : documents) {
            String json = objectMapper.writeValueAsString(document);
            writer.write(json + "\n");
            writtenCount++;
        }
        writer.flush();
        return writtenCount;
    }
}
