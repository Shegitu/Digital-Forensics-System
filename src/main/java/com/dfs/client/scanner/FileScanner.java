package com.dfs.client.scanner;

import com.dfs.shared.model.FileRecord;
import com.dfs.client.analyzer.HashGenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileScanner {

    public List<FileRecord> scanFolder(String path) {

        List<FileRecord> files =
                new ArrayList<>();

        File folder = new File(path);

        File[] fileList = folder.listFiles();

        if (fileList == null) {
            return files;
        }

        for (File file : fileList) {

            if (file.isFile()) {

                String hash =
                        HashGenerator.generateMD5(
                                file.getAbsolutePath());

                FileRecord record =
                        new FileRecord(
                                file.getName(),
                                file.getAbsolutePath(),
                                file.length(),
                                file.lastModified(),
                                hash
                        );

                files.add(record);
            }
        }

        return files;
    }
}