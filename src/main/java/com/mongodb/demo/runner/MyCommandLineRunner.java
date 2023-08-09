package com.mongodb.demo.runner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.mongodb.demo.repository.DocumentFetcher;
import com.mongodb.demo.repository.EmployeeRepository;
import com.mongodb.demo.repository.EmployeeService;
import com.mongodb.demo.service.CalculateDate;

@Component
public class MyCommandLineRunner implements CommandLineRunner {

    @Autowired
    private EmployeeRepository employeeRepository;
    
    @Autowired
    private CalculateDate calculateDate;
    
    @Autowired
    private EmployeeService employeeService;
    
    @Autowired
    private DocumentFetcher documentFetcher;

    @Override
    public void run(String... args) throws Exception {

        System.out.println("Running ");
        
        documentFetcher.fetchDocumentsInBatches(1);
		/*
		 * employeeRepository.findAll().forEach(emp ->
		 * System.out.println(emp.getEmployeeName())); MaxMinCreatedDate d=
		 * calculateDate.calculateMaxCreatedDate();
		 * 
		 * 
		 * LocalDate targetDate = LocalDate.of(2023, 5, 5); DateTimeFormatter formatter
		 * = DateTimeFormatter.ofPattern("MMM-yyyy").withLocale(Locale.US);
		 * 
		 * String fileName = targetDate.format(formatter).toUpperCase() + ".json";
		 * 
		 * // Create the full output path String outputDirectory =
		 * "C:\\Users\\rahul\\OneDrive\\Desktop";
		 * 
		 * String outputPath = outputDirectory + "\\" + fileName;
		 * 
		 * 
		 * employeeService.writeEmployeesToJsonByDate(targetDate,outputPath);
		 * 
		 * calculateDate.calculateMaxCreatedDate();
		 */
        
       

        
        System.out.println("End app");

    }

}