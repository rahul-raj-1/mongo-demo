package com.mongodb.demo.test;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class ReadFileInChunks {
	

	public static void main(String[] args) throws IOException {

		Path filePath = Paths.get("C:\\Users\\rahul\\OneDrive\\Desktop\\my_file.txt");

		try (BufferedInputStream inputStream = new BufferedInputStream(Files.newInputStream(filePath));) {

			// Read the file in chunks
			byte[] buffer = new byte[8192];
			int bytesRead = inputStream.read(buffer);
			System.out.write(buffer, 0, bytesRead);

		}
	}
}