package com.dfs.shared.remote;

import com.dfs.shared.model.FileRecord;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface DFSRemote
        extends Remote {

    void sendFileData(
            List<FileRecord> files)
            throws RemoteException;
}