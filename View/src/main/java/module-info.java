
module pl.comp.viewmodule {
    requires javafx.controls;
    requires javafx.fxml;
    requires pl.first.firstjava;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.desktop;
    requires log4j;
    requires java.sql;

    opens pl.comp.viewmodule to javafx.fxml;
    exports pl.comp.viewmodule;
}
