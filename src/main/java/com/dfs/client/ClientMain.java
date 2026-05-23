package com.dfs.client;

import com.dfs.client.scanner.FileScanner;
import com.dfs.shared.model.FileRecord;

import java.util.List;

public class ClientMain {

    public static void main(String[] args) {

        FileScanner scanner =
                new FileScanner();

        List<FileRecord> files =
                scanner.scanFolder("C:/TestFolder");

        for (FileRecord file : files) {
            System.out.println(file);
        }
    }
}