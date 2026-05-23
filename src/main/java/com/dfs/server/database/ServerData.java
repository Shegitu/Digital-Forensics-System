package com.dfs.server.database;

import com.dfs.shared.model.FileRecord;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class ServerData {

    // =========================
    // SAVE FILES
    // =========================
    public static void saveFiles(String clientIp, List<FileRecord> files) {

        String sql = "INSERT INTO files (client_ip, file_name, path, size, last_modified, hash, keywords) VALUES (?, ?, ?, ?, ?, ?, ?)";

        String dupSql = "INSERT INTO duplicates (file_name, hash, client_ip) VALUES (?, ?, ?)";

        String keySql = "INSERT INTO keywords (file_name, keyword, client_ip) VALUES (?, ?, ?)";

        String logSql = "INSERT INTO client_logs (client_ip, event_type, message) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection()) {

            PreparedStatement fileStmt = conn.prepareStatement(sql);
            PreparedStatement dupStmt = conn.prepareStatement(dupSql);
            PreparedStatement keyStmt = conn.prepareStatement(keySql);
            PreparedStatement logStmt = conn.prepareStatement(logSql);

            Set<String> hashTracker = new HashSet<>();

            for (FileRecord f : files) {

                // ================= FILE TABLE =================
                fileStmt.setString(1, clientIp);
                fileStmt.setString(2, f.getFileName());
                fileStmt.setString(3, f.getPath());
                fileStmt.setLong(4, f.getSize());
                fileStmt.setLong(5, f.getLastModified());
                fileStmt.setString(6, f.getHash());
                fileStmt.setString(7, f.getMatchedKeywords());
                fileStmt.addBatch();

                // ================= DUPLICATES =================
                if (!hashTracker.add(f.getHash())) {
                    dupStmt.setString(1, f.getFileName());
                    dupStmt.setString(2, f.getHash());
                    dupStmt.setString(3, clientIp);
                    dupStmt.addBatch();
                }

                // ================= KEYWORDS =================
                if (f.getMatchedKeywords() != null && !f.getMatchedKeywords().isEmpty()) {

                    String[] words = f.getMatchedKeywords().split(" ");

                    for (String w : words) {
                        keyStmt.setString(1, f.getFileName());
                        keyStmt.setString(2, w);
                        keyStmt.setString(3, clientIp);
                        keyStmt.addBatch();
                    }
                }

                // ================= CLIENT LOG =================
                logStmt.setString(1, clientIp);
                logStmt.setString(2, "FILE_SCAN");
                logStmt.setString(3, "Processed: " + f.getFileName());
                logStmt.addBatch();
            }

            fileStmt.executeBatch();
            dupStmt.executeBatch();
            keyStmt.executeBatch();
            logStmt.executeBatch();

            System.out.println("DATABASE UPDATED SUCCESSFULLY");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<FileRecord> getGlobalDuplicates() {

    List<FileRecord> list = new ArrayList<>();

    String sql =
            "SELECT file_name, path, size, last_modified, hash, client_ip, keywords " +
            "FROM files " +
            "WHERE hash IN (" +
            "SELECT hash FROM files GROUP BY hash HAVING COUNT(*) > 1" +
            ") ORDER BY hash";

    try (Connection conn = DBConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {

            FileRecord r = new FileRecord(
                    rs.getString("file_name"),
                    rs.getString("path"),
                    rs.getLong("size"),
                    rs.getLong("last_modified"),
                    rs.getString("hash")
            );

            r.setClientIp(rs.getString("client_ip"));
            r.setMatchedKeywords(rs.getString("keywords"));

            list.add(r);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }

    return list;
}
    // =========================
    // FETCH FILES (API)
    // =========================
    public static List<FileRecord> getAllFiles() {

        List<FileRecord> list = new ArrayList<>();

        String sql = "SELECT * FROM files ORDER BY id DESC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                FileRecord r = new FileRecord(
                        rs.getString("file_name"),
                        rs.getString("path"),
                        rs.getLong("size"),
                        rs.getLong("last_modified"),
                        rs.getString("hash")
                );

                r.setClientIp(rs.getString("client_ip"));
                r.setMatchedKeywords(rs.getString("keywords"));

                list.add(r);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}