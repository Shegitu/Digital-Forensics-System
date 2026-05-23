module com.dfs {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.dfs to javafx.fxml;
    exports com.dfs;
}
