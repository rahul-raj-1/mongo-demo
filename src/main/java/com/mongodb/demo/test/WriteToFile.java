package com.mongodb.demo.test;

import java.io.File;
import java.io.FileWriter;

public class WriteToFile {

    public static void main(String[] args) throws Exception {

        // Create a text file
        File file = new File("C:\\Users\\rahul\\OneDrive\\Desktop\\my_file.txt");
        FileWriter writer = new FileWriter(file);

        // Write more than 8192*2 +1 characters to the file
        for (int i = 0; i < (8193*2 + 1); i++) {
            writer.write("This is a character " + i );
            writer.write("\n");

        }

        // Close the file
        writer.close();
    }
}

