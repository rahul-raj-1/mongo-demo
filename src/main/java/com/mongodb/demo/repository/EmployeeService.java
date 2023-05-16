package com.mongodb.demo.repository;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.demo.entity.Employee;


@Service
public class EmployeeService {
    
    @Value("${page.size}")
    private int pageSize;

	private final MongoTemplate mongoTemplate;
    
    public EmployeeService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
    
    
    // Delete employees by date and return the count of deleted documents
    public long deleteEmployeesByDate(LocalDate date) {
        LocalDateTime startDateTime = date.atStartOfDay();
        LocalDateTime endDateTime = date.atTime(LocalTime.MAX);
        
        Query query = new Query(Criteria.where("created")
                .gte(startDateTime.toString())
                .lt(endDateTime.plusNanos(1).toString()));
        
        DeleteResult result = mongoTemplate.remove(query, Employee.class);
        long deletedCount = result.getDeletedCount();
        
        System.out.println("deletedCount: " + deletedCount);
        
        return deletedCount;
    }

  
   
    // Write employees to a JSON file by date using separate methods
    public boolean writeEmployeesToJsonByDate(LocalDate date, String outputPath) {
        LocalDateTime startDateTime = date.atStartOfDay();
        LocalDateTime endDateTime = date.atTime(LocalTime.MAX);

        Query query = new Query(Criteria.where("created")
                .gte(startDateTime.toString())
                .lt(endDateTime.plusNanos(1).toString()));

        long totalCount = mongoTemplate.count(query, Employee.class);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            int processedCount = 0;
            int writtenCount = 0;
            int pageNum = 0;

            while (processedCount < totalCount) {
                List<Employee> employees = fetchEmployeesByPage(query, pageNum);

                System.out.println("Employees fetched: " + employees.size());

                writtenCount = writtenCount + writeEmployeesToFile(employees, writer);
                processedCount =processedCount +  employees.size();
                pageNum++;

                System.out.println("Processed " + processedCount + " employees.");
            }

            if (writtenCount != totalCount) {
                System.out.println("Mismatch between fetched and written documents!");
                return false;
            } else {
                System.out.println("All documents fetched and written successfully.");
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private List<Employee> fetchEmployeesByPage(Query query, int pageNum) {
        query.limit(pageSize).skip(pageNum * pageSize);
        return mongoTemplate.find(query, Employee.class);
    }

    private int writeEmployeesToFile(List<Employee> employees, BufferedWriter writer) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        int writtenCount = 0;
        for (Employee employee : employees) {
            String json = objectMapper.writeValueAsString(employee);
            writer.write(json + "\n");
            writtenCount++;
        }
        writer.flush();
        return writtenCount;
    }

}

