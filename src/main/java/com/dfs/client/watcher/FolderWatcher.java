package com.dfs.client.watcher;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.nio.file.*;

public class FolderWatcher {

    public void watchFolder(
            String folderPath,
            TextArea outputArea) {

        Thread watcherThread =
                new Thread(() -> {

                    try {

                        WatchService watchService =
                                FileSystems.getDefault()
                                        .newWatchService();

                        Path path =
                                Paths.get(folderPath);

                        path.register(
                                watchService,
                                StandardWatchEventKinds.ENTRY_CREATE,
                                StandardWatchEventKinds.ENTRY_DELETE,
                                StandardWatchEventKinds.ENTRY_MODIFY
                        );

                        while (true) {

                            WatchKey key =
                                    watchService.take();

                            for (WatchEvent<?> event :
                                    key.pollEvents()) {

                                String eventType =
                                        event.kind().name();

                                String fileName =
                                        event.context()
                                                .toString();

                                Platform.runLater(() -> {

                                    outputArea.appendText(
                                            eventType
                                                    + " → "
                                                    + fileName
                                                    + "\n"
                                    );
                                });
                            }

                            key.reset();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });

        watcherThread.setDaemon(true);
        watcherThread.start();
    }
}