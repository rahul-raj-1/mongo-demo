package com.mongodb.demo.repository;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.BulkOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

@Component
public class DocumentFetcher {

	@Autowired
    private  MongoTemplate mongoTemplate;

   
    public void fetchDocumentsInBatches(int batchSize) {
        int pageNum = 0;
        boolean hasMoreDocuments = true;

        while (hasMoreDocuments) {
        	
        	Criteria criteria = Criteria.where("update_date").exists(true);

        	Query query = new Query(criteria).with(PageRequest.of(pageNum, batchSize));
        	query.fields().include("_id", "update_date"); // Specify the fields you want to fetch

        	List<Document> documents = mongoTemplate.find(query, Document.class, "my_col");
        	
        	System.out.println(" DOC SIZE " + documents.size());


        	    // Update the fetched documents as per your requirements
        	updateDocuments(documents);

        	    hasMoreDocuments = (documents.size() == batchSize);
        	    pageNum++;
        }
    }
    
    public void updateDocuments(List<Document> documents) {
    	
    	if (documents.isEmpty()) {
            return; // No documents to update
        }

        List<ObjectId> documentIds = documents.stream()
                .map(document -> (ObjectId) document.get("_id"))
                .collect(Collectors.toList());

      
       
        BulkOperations bulkOperations = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, Document.class,"my_col");

        for (Document document : documents) {
        	 Update update = new Update();
            String updateDateStr = document.getString("update_date");
            Query query = Query.query(Criteria.where("_id").in(document.get("_id")));

          //  LocalDateTime updateDate = LocalDateTime.parse(updateDateStr, DateTimeFormatter.ISO_DATE_TIME);
            LocalDateTime updateDate = LocalDateTime.parse(updateDateStr, DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneOffset.UTC));

            System.out.println(" updateDate " + updateDate);
            update.set("update_date", updateDate);
            bulkOperations.updateMulti(query,update);
        }

     //   bulkOperations.execute();
    }
    
    public void updateDocuments2(List<Document> documents) {
        if (documents.isEmpty()) {
            return; // No documents to update
        }

        BulkOperations bulkOperations = mongoTemplate.bulkOps(BulkOperations.BulkMode.UNORDERED, Document.class, "my_col");
   //     BulkOperations bulkOperationsWithWrite = bulkOperations.initializeOrderedBulkOperation();

        for (Document document : documents) {
            ObjectId documentId = document.getObjectId("_id");
            String updateDateStr = document.getString("update_date");
            LocalDateTime updateDate = LocalDateTime.parse(updateDateStr, DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneOffset.UTC));

           // bulkOperationsWithWrite.find(Query.query(Criteria.where("_id").is(documentId)))
                //    .updateOne(Update.update("update_date", updateDate));
        }

        bulkOperations.execute();
    }


    


  
}

