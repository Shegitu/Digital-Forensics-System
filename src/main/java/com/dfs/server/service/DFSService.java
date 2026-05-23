package com.dfs.server.service;

import com.dfs.server.database.InMemoryDatabase;
import com.dfs.shared.model.FileRecord;
import com.dfs.shared.remote.DFSRemote;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class DFSService extends UnicastRemoteObject implements DFSRemote {

    public DFSService() throws RemoteException {
        super();
    }

    @Override
    public void sendFileData(List<FileRecord> files) throws RemoteException {

        System.out.println("\nCLIENT CONNECTED");

        InMemoryDatabase.saveFiles(files);

        System.out.println("FILES STORED: " + files.size());

        detectDuplicates(files);
    }

    private void detectDuplicates(List<FileRecord> files) {

        for (FileRecord f1 : files) {

            for (FileRecord f2 : InMemoryDatabase.getAllFiles()) {

                if (!f1.getPath().equals(f2.getPath())
                        && f1.getHash().equals(f2.getHash())) {

                    System.out.println(
                            "DUPLICATE DETECTED: "
                                    + f1.getFileName()
                                    + " == "
                                    + f2.getFileName()
                    );
                }
            }
        }
    }
}