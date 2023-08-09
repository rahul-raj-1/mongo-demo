package com.mongodb.demo.controller;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class MyScheduledTask {



    @Scheduled(cron = "${scheduler.cron.expression}")
    public void scheduledTask() {
        // This method will be executed based on the cron expression from the properties file
        System.out.println("Scheduled task executed with cron expression: " );
        // Add your desired logic here
    }
}
