package com.dfs.client.watcher;

import com.dfs.client.analyzer.HashGenerator;
import com.dfs.client.analyzer.KeywordAnalyzer;
import com.dfs.client.rmi.RMIClient;
import com.dfs.shared.model.FileRecord;
import javafx.application.Platform;
import java.io.File;
import java.net.InetAddress;
import java.nio.file.*;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class FolderWatcher {

    public void watchFolder(String folderPath, Consumer<String> logCallback, Consumer<FileRecord> uiUpdateCallback) {
        Thread thread = new Thread(() -> {
            try (WatchService watchService = FileSystems.getDefault().newWatchService()) {
                Path path = Paths.get(folderPath);
                path.register(watchService, 
                    StandardWatchEventKinds.ENTRY_CREATE, 
                    StandardWatchEventKinds.ENTRY_MODIFY);

                KeywordAnalyzer keywordAnalyzer = new KeywordAnalyzer();
                RMIClient rmiClient = new RMIClient();
                String localIp = InetAddress.getLocalHost().getHostAddress();

                while (true) {
                    WatchKey key = watchService.take();
                    for (WatchEvent<?> event : key.pollEvents()) {
                        Path fileName = (Path) event.context();
                        Path fullPath = path.resolve(fileName);
                        File file = fullPath.toFile();

                        if (file.exists() && file.isFile()) {
                            Thread.sleep(300); // Guard window for write operations to settle
                            
                            String hash = HashGenerator.generateMD5(file.getAbsolutePath());
                            FileRecord record = new FileRecord(
                                file.getName(), file.getAbsolutePath(), file.length(), file.lastModified(), hash
                            );
                            record.setClientIp(localIp);
                            
                            String matched = keywordAnalyzer.analyze(file.getAbsolutePath());
                            record.setMatchedKeywords(matched);

                            List<FileRecord> list = Collections.singletonList(record);
                            rmiClient.sendData(list);

                            Platform.runLater(() -> {
                                logCallback.accept("AUTO-DETECTED & FORWARDED: " + file.getName());
                                uiUpdateCallback.accept(record);
                            });
                        }
                    }
                    if (!key.reset()) break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        thread.setDaemon(true);
        thread.start();
    }
}