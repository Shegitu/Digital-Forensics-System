package com.dfs.client.analyzer;

import com.dfs.shared.model.FileRecord;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DuplicateDetector {

    public String detect(
            List<FileRecord> files) {

        Map<String, Integer> hashMap =
                new HashMap<>();

        StringBuilder duplicates =
                new StringBuilder();

        for (FileRecord file : files) {

            String hash =
                    file.getHash();

            hashMap.put(
                    hash,
                    hashMap.getOrDefault(
                            hash,
                            0
                    ) + 1
            );
        }

        for (FileRecord file : files) {

            if (hashMap.get(
                    file.getHash()) > 1) {

                duplicates.append(
                        "Duplicate → "
                ).append(
                        file.getFileName()
                ).append("\n");
            }
        }

        return duplicates.toString();
    }
}