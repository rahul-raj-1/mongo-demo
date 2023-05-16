package com.mongodb.demo.entity;

public class S3UploadRequest {
	
	  private String filePath;
	    private String bucketName;
	    private String objectKey;
	    private String checksumAlgorithm;
		public String getFilePath() {
			return filePath;
		}
		public void setFilePath(String filePath) {
			this.filePath = filePath;
		}
		public String getBucketName() {
			return bucketName;
		}
		public void setBucketName(String bucketName) {
			this.bucketName = bucketName;
		}
		public String getObjectKey() {
			return objectKey;
		}
		public void setObjectKey(String objectKey) {
			this.objectKey = objectKey;
		}
		public String getChecksumAlgorithm() {
			return checksumAlgorithm;
		}
		public void setChecksumAlgorithm(String checksumAlgorithm) {
			this.checksumAlgorithm = checksumAlgorithm;
		}
	    
	    
	    
	    

}
