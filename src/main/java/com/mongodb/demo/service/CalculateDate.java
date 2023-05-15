package com.mongodb.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.stereotype.Service;

import com.mongodb.demo.entity.MaxMinCreatedDate;

@Service
public class CalculateDate {

	@Autowired
	private MongoTemplate mongoTemplate;
	
	public MaxMinCreatedDate calculateMaxCreatedDate() {
		Aggregation aggregation = Aggregation.newAggregation(
	            Aggregation.project("created"),
	            Aggregation.group()
	                .min("created").as("minDate")
	        );
			
			
			        AggregationResults<MaxMinCreatedDate> result = mongoTemplate.aggregate(
			            aggregation, "employee", MaxMinCreatedDate.class);

			        MaxMinCreatedDate maxMinCreatedDate = result.getUniqueMappedResult();

			        System.out.println("Result is: " + maxMinCreatedDate);
			        return maxMinCreatedDate;
	  
	}



	

}
