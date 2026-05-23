package com.dfs.client.rmi;

import com.dfs.shared.model.FileRecord;
import com.dfs.shared.remote.DFSRemote;

import java.rmi.Naming;
import java.util.List;

public class RMIClient {

    public void sendData(
            List<FileRecord> files) {

        try {

            DFSRemote server =
                    (DFSRemote)
                            Naming.lookup(
                                    "rmi://localhost/DFSService"
                            );

            server.sendFileData(
                    files
            );

            System.out.println(
                    "Files Sent"
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}