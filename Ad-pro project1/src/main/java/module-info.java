module se233.adpro_project1 {
    requires javafx.controls;
    requires javafx.fxml;


    opens se233.adpro_project1 to javafx.fxml;
    opens se233.adpro_project1.Controller to javafx.fxml;
    exports se233.adpro_project1;
}