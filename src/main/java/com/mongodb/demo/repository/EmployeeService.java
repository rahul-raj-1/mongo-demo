package com.mongodb.demo.repository;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.util.CloseableIterator;
import org.springframework.expression.ParseException;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.demo.entity.Employee;


@Service
public class EmployeeService {
    private static final int PAGE_SIZE = 2; // Specify the batch size

	private final MongoTemplate mongoTemplate;
    
    public EmployeeService(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }
    
    
    public List<Employee> getEmployeesByDate(LocalDate date) {
        LocalDateTime startDateTime = date.atStartOfDay();
        LocalDateTime endDateTime = date.atTime(LocalTime.MAX);
        
        Query query = new Query(Criteria.where("created")
                .gte(startDateTime.toString())
                .lt(endDateTime.plusNanos(1).toString()));
        
        return mongoTemplate.find(query, Employee.class);
    }
   
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
                query.limit(PAGE_SIZE).skip(pageNum * PAGE_SIZE);
                List<Employee> employees = mongoTemplate.find(query, Employee.class);
                
                System.out.println("employees size : " + employees.size());

                for (Employee employee : employees) {
                    // Write the employee as JSON to the file
                    String json = new ObjectMapper().writeValueAsString(employee);
                    writer.write(json + "\n");
                    writtenCount++;
                }

                processedCount += employees.size();
                pageNum++;

                System.out.println("Processed " + processedCount + " employees.");
            }

            if (writtenCount != totalCount) {
                System.out.println("Mismatch between fetched and written documents!");
                return true;
            } else {
                System.out.println("All documents fetched and written successfully.");
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean writeEmployeesToJsonByDate2(LocalDate date, String outputPath) {
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
                query.limit(PAGE_SIZE).skip(pageNum * PAGE_SIZE);
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
        query.limit(PAGE_SIZE).skip(pageNum * PAGE_SIZE);
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

