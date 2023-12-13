module com.example.graphicslab {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.swing;
    requires animated.gif.lib;
    requires java.desktop;


    opens com.example.graphicslab to javafx.fxml;
    exports com.example.graphicslab;
}
