package com.dfs.shared.model;

import java.io.Serializable;

public class FileRecord implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String fileName;
    private String path;
    private long size;
    private long lastModified;
    private String hash;
    private String clientIp;
    private String matchedKeywords;

    public FileRecord(String fileName, String path, long size, long lastModified, String hash) {
        this.fileName = fileName;
        this.path = path;
        this.size = size;
        this.lastModified = lastModified;
        this.hash = hash;
        this.clientIp = "UNKNOWN";
        this.matchedKeywords = "";
    }

    public String getFileName() { return fileName; }
    public String getPath() { return path; }
    public long getSize() { return size; }
    public long getLastModified() { return lastModified; }
    public String getHash() { return hash; }
    public String getClientIp() { return clientIp; }
    public void setClientIp(String clientIp) { this.clientIp = clientIp; }
    public String getMatchedKeywords() { return matchedKeywords; }
    public void setMatchedKeywords(String keywords) { this.matchedKeywords = keywords; }

    @Override
    public String toString() {
        return fileName + " [" + clientIp + "] | " + hash;
    }
}