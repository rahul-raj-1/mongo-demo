package com.mongodb.demo.service;

import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;

import software.amazon.awssdk.transfer.s3.S3TransferManager;
import software.amazon.awssdk.transfer.s3.model.CompletedFileUpload;
import software.amazon.awssdk.transfer.s3.model.FileUpload;
import software.amazon.awssdk.transfer.s3.model.UploadFileRequest;
import software.amazon.awssdk.transfer.s3.progress.LoggingTransferListener;


// NEED TO BE TESTED
public class S3Uploader {

	@Autowired
	private S3TransferManager transferManager;
	
	public String uploadFile( String bucketName, String key, String filePath) {
		UploadFileRequest uploadFileRequest = UploadFileRequest.builder()
				.putObjectRequest(b -> b.bucket(bucketName).key(key))
				.addTransferListener(LoggingTransferListener.create())
				.source(Paths.get(filePath))
				.build();

		FileUpload fileUpload = transferManager.uploadFile(uploadFileRequest);

		CompletedFileUpload uploadResult = fileUpload.completionFuture().join();
		return uploadResult.response().eTag();
	}

}
