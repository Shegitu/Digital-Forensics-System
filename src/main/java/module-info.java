module com.dfs {

    requires javafx.controls;
    requires javafx.fxml;
    requires java.rmi;

    opens com.dfs to javafx.fxml;

    exports com.dfs;
    exports com.dfs.server;
    exports com.dfs.shared.model;
    exports com.dfs.shared.remote;
}