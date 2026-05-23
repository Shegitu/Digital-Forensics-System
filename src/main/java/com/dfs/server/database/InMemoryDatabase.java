package com.dfs.server.database;

import com.dfs.shared.model.FileRecord;

import java.util.ArrayList;
import java.util.List;

public class InMemoryDatabase {

    private static final List<FileRecord> allFiles =
            new ArrayList<>();

    public static synchronized void saveFiles(List<FileRecord> files) {

        allFiles.addAll(files);
    }

    public static synchronized List<FileRecord> getAllFiles() {

        return new ArrayList<>(allFiles);
    }
}