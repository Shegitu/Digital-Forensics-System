package com.dfs.client.analyzer;

import java.io.FileInputStream;
import java.security.MessageDigest;

public class HashGenerator {

    public static String generateMD5(String filePath) {

        try {

            MessageDigest md =
                    MessageDigest.getInstance("MD5");

            FileInputStream fis =
                    new FileInputStream(filePath);

            byte[] dataBytes = new byte[1024];
            int bytesRead;

            while ((bytesRead =
                    fis.read(dataBytes)) != -1) {

                md.update(dataBytes, 0, bytesRead);
            }

            fis.close();

            byte[] hashBytes = md.digest();

            StringBuilder sb =
                    new StringBuilder();

            for (byte b : hashBytes) {
                sb.append(
                        String.format("%02x", b)
                );
            }

            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }
}