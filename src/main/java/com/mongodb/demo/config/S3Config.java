package com.mongodb.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.transfer.s3.S3TransferManager;
@Configuration
public class S3Config {
		
		@Value("${aws.accessKeyId}")
	    private String accessKeyId;

	    @Value("${aws.secretKey}")
	    private String secretKey;
	    
	    
	    @Bean
	    public AwsCredentialsProvider awsCredentialsProvider() {
	        return StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKeyId, secretKey));

	    }

		   @Bean
		    public S3TransferManager s3TransferManager() {
			 
			   S3AsyncClient s3AsyncClient = S3AsyncClient.crtBuilder()
					    .credentialsProvider(awsCredentialsProvider())
					    .region(Region.EU_CENTRAL_1)
					    .build();
			 


			 
				S3TransferManager transferManager = S3TransferManager
				        .builder()
				        .s3Client(s3AsyncClient)
				        .build();
				
				
				
				
				return transferManager;
					   
			 
		    }
}
