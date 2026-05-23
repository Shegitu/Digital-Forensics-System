package com.dfs;

import com.dfs.client.scanner.FileScanner;
import com.dfs.client.watcher.FolderWatcher;
import com.dfs.shared.model.FileRecord;
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

        FileScanner scanner =
                new FileScanner();

        List<FileRecord> files =
                scanner.scanFolder(folder);

        outputArea.clear();

        for (FileRecord file : files) {

            outputArea.appendText(
                    file.toString()
                            + "\n\n"
            );
        }
    }
}