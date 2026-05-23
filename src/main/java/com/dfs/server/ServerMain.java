package com.dfs.server;

import com.dfs.server.service.DFSService;

import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;

public class ServerMain {

    public static void main(
            String[] args) {

        try {

            LocateRegistry
                    .createRegistry(1099);

            Naming.rebind(
                    "DFSService",
                    new DFSService()
            );

            System.out.println(
                    "DFS Server Running..."
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}