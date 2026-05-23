package com.dfs.client.analyzer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class KeywordAnalyzer {

    private final List<String> keywords =
            List.of(
                    "password",
                    "admin",
                    "secret",
                    "bank",
                    "login"
            );

    public String analyze(String path) {

        try {

            String content =
                    Files.readString(
                            Paths.get(path)
                    ).toLowerCase();

            StringBuilder result =
                    new StringBuilder();

            for (String keyword : keywords) {

                if (content.contains(keyword)) {

                    result.append(
                            keyword
                    ).append(" ");
                }
            }

            return result.toString();

        } catch (IOException e) {
            return "";
        }
    }
}