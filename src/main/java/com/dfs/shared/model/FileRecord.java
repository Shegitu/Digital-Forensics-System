package com.dfs.shared.model;

import java.io.Serializable;

public class FileRecord
        implements Serializable {

    private String fileName;
    private String path;
    private long size;
    private long lastModified;
    private String hash;

    public FileRecord(String fileName,
                      String path,
                      long size,
                      long lastModified,
                      String hash) {

        this.fileName = fileName;
        this.path = path;
        this.size = size;
        this.lastModified = lastModified;
        this.hash = hash;
    }

    public String getFileName() {
        return fileName;
    }

    public String getPath() {
        return path;
    }

    public long getSize() {
        return size;
    }

    public long getLastModified() {
        return lastModified;
    }

    public String getHash() {
        return hash;
    }

    @Override
    public String toString() {

        return fileName +
                " | " +
                path +
                " | " +
                size +
                " bytes | " +
                hash;
    }
}