package com.dfs;

import com.dfs.client.analyzer.DuplicateDetector;
import com.dfs.client.analyzer.KeywordAnalyzer;
import com.dfs.client.scanner.FileScanner;
import com.dfs.client.threading.TaskManager;
import com.dfs.client.watcher.FolderWatcher;
import com.dfs.client.rmi.RMIClient;
import com.dfs.shared.model.FileRecord;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.InetAddress;
import java.util.List;

public class PrimaryController {

    @FXML private TextArea outputArea;
    @FXML private TableView<FileRecord> tblFiles;
    @FXML private TableColumn<FileRecord, String> colIp;
    @FXML private TableColumn<FileRecord, String> colName;
    @FXML private TableColumn<FileRecord, Long> colSize;
    @FXML private TableColumn<FileRecord, String> colHash;
    @FXML private TableColumn<FileRecord, String> colKeywords;
    @FXML private Label lblStatus;

    private final String folder = "C:/TestFolder";

    private final ObservableList<FileRecord> uiRecordsMaster =
            FXCollections.observableArrayList();

    @FXML
    public void initialize() {

        colIp.setCellValueFactory(new PropertyValueFactory<>("clientIp"));
        colName.setCellValueFactory(new PropertyValueFactory<>("fileName"));
        colSize.setCellValueFactory(new PropertyValueFactory<>("size"));
        colHash.setCellValueFactory(new PropertyValueFactory<>("hash"));
        colKeywords.setCellValueFactory(new PropertyValueFactory<>("matchedKeywords"));

        tblFiles.setItems(uiRecordsMaster);

        FolderWatcher watcher = new FolderWatcher();

        watcher.watchFolder(
                folder,
                msg -> outputArea.appendText(msg + "\n"),
                record -> uiRecordsMaster.add(0, record)
        );

        outputArea.appendText("DFS Monitoring Engine Started\n");
    }

    @FXML
    private void scanFiles() {

        outputArea.clear();
        uiRecordsMaster.clear();
        lblStatus.setText("Scanning...");

        TaskManager.runTask(() -> {

            try {

                FileScanner scanner = new FileScanner();
                List<FileRecord> files = scanner.scanFolder(folder);

                KeywordAnalyzer keywordAnalyzer = new KeywordAnalyzer();
                DuplicateDetector duplicateDetector = new DuplicateDetector();

                String ip = InetAddress.getLocalHost().getHostAddress();

                for (FileRecord f : files) {
                    f.setClientIp(ip);
                    f.setMatchedKeywords(keywordAnalyzer.analyze(f.getPath()));
                }

                String duplicates = duplicateDetector.detect(files);

                new RMIClient().sendData(files);

                Platform.runLater(() -> {

                    uiRecordsMaster.addAll(files);

                    outputArea.appendText("SCAN REPORT\n\n");

                    outputArea.appendText("DUPLICATES\n");
                    outputArea.appendText(
                            duplicates.isEmpty()
                                    ? "No duplicates found\n"
                                    : duplicates
                    );

                    lblStatus.setText("Scan Completed & Sent to Server");
                });

            } catch (Exception e) {
                e.printStackTrace();
                Platform.runLater(() ->
                        lblStatus.setText("Scan Failed"));
            }
        });
    }
}