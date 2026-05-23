package com.dfs.client.core;

import com.dfs.client.analyzer.KeywordAnalyzer;
import com.dfs.client.analyzer.DuplicateDetector;
import com.dfs.client.rmi.RMIClient;
import com.dfs.shared.model.FileRecord;

import java.net.InetAddress;
import java.util.List;

public class FileProcessingEngine {

    private static final KeywordAnalyzer keywordAnalyzer = new KeywordAnalyzer();
    private static final DuplicateDetector duplicateDetector = new DuplicateDetector();
    private static final RMIClient rmiClient = new RMIClient();

    public static String process(List<FileRecord> files) {

        try {

            String localIp = InetAddress.getLocalHost().getHostAddress();

            for (FileRecord f : files) {
                f.setClientIp(localIp);
                f.setMatchedKeywords(keywordAnalyzer.analyze(f.getPath()));
            }

            String duplicates = duplicateDetector.detect(files);

            rmiClient.sendData(files);

            return duplicates;

        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }
}