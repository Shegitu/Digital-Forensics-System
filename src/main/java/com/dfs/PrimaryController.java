package com.dfs;

import com.dfs.client.analyzer.DuplicateDetector;
import com.dfs.client.analyzer.KeywordAnalyzer;
import com.dfs.client.scanner.FileScanner;
import com.dfs.client.threading.TaskManager;
import com.dfs.client.watcher.FolderWatcher;
import com.dfs.shared.model.FileRecord;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;

import java.util.List;

public class PrimaryController {

    @FXML
    private TextArea outputArea;

    private final String folder =
            "C:/TestFolder";

    @FXML
    public void initialize() {

        FolderWatcher watcher =
                new FolderWatcher();

        watcher.watchFolder(
                folder,
                outputArea
        );
    }

    @FXML
    private void scanFiles() {

        outputArea.clear();

        TaskManager.runTask(() -> {

            FileScanner scanner =
                    new FileScanner();

            List<FileRecord> files =
                    scanner.scanFolder(folder);

            KeywordAnalyzer analyzer =
                    new KeywordAnalyzer();

            DuplicateDetector detector =
                    new DuplicateDetector();

            String duplicates =
                    detector.detect(files);

            Platform.runLater(() -> {

                for (FileRecord file :
                        files) {

                    outputArea.appendText(
                            file.toString()
                                    + "\n"
                    );

                    String keywords =
                            analyzer.analyze(
                                    file.getPath()
                            );

                    if (!keywords.isEmpty()) {

                        outputArea.appendText(
                                "Keywords → "
                                        + keywords
                                        + "\n"
                        );
                    }

                    outputArea.appendText(
                            "\n"
                    );
                }

                outputArea.appendText(
                        duplicates
                );
            });
        });
    }
}