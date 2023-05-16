package com.mongodb.demo.service;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ChecksumCalculator {
    private final MessageDigest digest;

    public ChecksumCalculator(String algorithm) throws NoSuchAlgorithmException {
        digest = MessageDigest.getInstance(algorithm);
    }

    public String convertChecksumToString(byte[] checksumBytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : checksumBytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }

    public byte[] calculateChecksum(byte[] data) {
        return digest.digest(data);
    }
}

