package com.mongodb.demo.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.stereotype.Service;

@Service
public class RequestAggregationExample {

    private final MongoTemplate mongoTemplate;
    
    

    public RequestAggregationExample(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    public void executeAggregationQuery() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
        
        ProjectionOperation projection = Aggregation.project()
                .andExpression("dateToString('%Y-%m-%dT%H:%M:%S.%L', { $dateFromString: { dateString: \"$created\", format: \"%Y-%m-%dT%H:%M:%S.%L\" } })")
                .as("createdDate");


        GroupOperation group = Aggregation.group()
                .min("createdDate").as("minDate")
                .max("createdDate").as("maxDate");

        Aggregation aggregation = Aggregation.newAggregation(projection, group);

        AggregationResults<AggregationResult> results = mongoTemplate.aggregate(aggregation, "employee", AggregationResult.class);
        List<AggregationResult> aggregationResults = results.getMappedResults();

        if (!aggregationResults.isEmpty()) {
            AggregationResult result = aggregationResults.get(0);
            LocalDateTime minDate = result.getMinDate();
            LocalDateTime maxDate = result.getMaxDate();
            
            System.out.println("Min Date: " + minDate);
            System.out.println("Max Date: " + maxDate);
        } else {
            System.out.println("No results found.");
        }
    }

    private static class AggregationResult {
        private LocalDateTime minDate;
        private LocalDateTime maxDate;

        public LocalDateTime getMinDate() {
            return minDate;
        }

        public void setMinDate(LocalDateTime minDate) {
            this.minDate = minDate;
        }

        public LocalDateTime getMaxDate() {
            return maxDate;
        }

        public void setMaxDate(LocalDateTime maxDate) {
            this.maxDate = maxDate;
        }
    }
}

