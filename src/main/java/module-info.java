module com.dfs {

    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;
    requires java.sql;
    requires jdk.httpserver;
    opens com.dfs to javafx.fxml;

    exports com.dfs;
    exports com.dfs.server;
    exports com.dfs.shared.model;
    exports com.dfs.shared.remote;
}