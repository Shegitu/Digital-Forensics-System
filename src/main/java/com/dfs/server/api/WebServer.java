package com.dfs.server.api;

import com.dfs.server.database.ServerData;
import com.dfs.shared.model.FileRecord;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class WebServer {

    public static void start() {

        try {

            HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

            server.createContext("/files", new FilesHandler());
            server.createContext("/duplicates", new DuplicatesHandler());
            server.createContext("/status", new StatusHandler());

            server.start();

            System.out.println("DFS WEB API RUNNING ON PORT 8080");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void send(HttpExchange exchange, String json) {

        try {

            exchange.getResponseHeaders().set("Content-Type", "application/json; charset=UTF-8");

            byte[] bytes = json.getBytes(StandardCharsets.UTF_8);

            exchange.sendResponseHeaders(200, bytes.length);

            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String toJson(List<FileRecord> records) {

        if (records == null) return "[]";

        StringBuilder sb = new StringBuilder("[");

        for (int i = 0; i < records.size(); i++) {

            FileRecord f = records.get(i);

            sb.append("{")
                    .append("\"clientIp\":\"").append(f.getClientIp()).append("\",")
                    .append("\"name\":\"").append(f.getFileName()).append("\",")
                    .append("\"path\":\"").append(f.getPath().replace("\\", "\\\\")).append("\",")
                    .append("\"size\":").append(f.getSize()).append(",")
                    .append("\"hash\":\"").append(f.getHash()).append("\",")
                    .append("\"keywords\":\"").append(f.getMatchedKeywords()).append("\"")
                    .append("}");

            if (i < records.size() - 1) sb.append(",");
        }

        sb.append("]");
        return sb.toString();
    }

    static class FilesHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) {
            send(exchange, toJson(ServerData.getAllFiles()));
        }
    }

    static class DuplicatesHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) {
            send(exchange, toJson(ServerData.getGlobalDuplicates()));
        }
    }

    static class StatusHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) {
            send(exchange, "{\"status\":\"ONLINE\",\"service\":\"DFS\"}");
        }
    }
}