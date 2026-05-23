package com.dfs.client.rmi;

import com.dfs.shared.model.FileRecord;
import com.dfs.shared.remote.DFSRemote;
import java.net.InetAddress;
import java.rmi.Naming;
import java.util.List;

public class RMIClient {
    public void sendData(List<FileRecord> files) {
        try {
            String clientIp = InetAddress.getLocalHost().getHostAddress();
            for (FileRecord f : files) {
                f.setClientIp(clientIp);
            }
            DFSRemote server = (DFSRemote) Naming.lookup("rmi://localhost/DFSService");
            server.sendFileData(clientIp, files);
        } catch (Exception e) {
            System.err.println("RMI Layer pipeline failure: " + e.getMessage());
        }
    }
}