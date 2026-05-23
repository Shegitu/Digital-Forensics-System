package com.dfs.server.service;

import com.dfs.server.database.ServerData;
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
    public void sendFileData(String clientIp, List<FileRecord> files) throws RemoteException {

        System.out.println("CLIENT: " + clientIp + " SENT DATA");
        System.out.println("FILES RECEIVED: " + files.size());

        ServerData.saveFiles(clientIp, files);

        System.out.println("DATA STORED SUCCESSFULLY");
    }
}