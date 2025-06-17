module projeto {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.materialdesign;
    requires jakarta.mail;
    requires org.eclipse.angus.mail;
    
    opens projeto to javafx.fxml;
    exports projeto;
    
    opens projeto.view to javafx.fxml;
    exports projeto.view;
    
    opens projeto.model to javafx.fxml;
    exports projeto.model;
}