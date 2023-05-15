package com.mongodb.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.mongodb.demo.entity.MyBean;

@Configuration
public class MyAppConfig {
    
    @Bean
    MyBean beanName() {
        return new MyBean(1);
    }
    
  

  
    
}