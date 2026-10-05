package se233.adpro_project1.Controller;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Pane;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import se233.adpro_project1.Util.ZipUtil;
import java.io.IOException;
import javafx.scene.control.Alert;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.scene.Node;

public class DropZoneController {

    @FXML private Pane dropPane;
    @FXML private ListView<String> fileListView;

    private final List<File> imageFiles = new ArrayList<>();

    @FXML
    private void handleDragOver(DragEvent event) {
        if (event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.COPY);
            dropPane.setStyle("-fx-background-color: #d0e8ff;");
        }
        event.consume();
    }

    @FXML
    private void handleDragExited(DragEvent event) {
        dropPane.setStyle("");
        event.consume();
    }

    @FXML
    private void handleDragDropped(DragEvent event) {
        Dragboard db = event.getDragboard();
        boolean success = false;

        if (db.hasFiles()) {
            for (File file : db.getFiles()) {
                String name = file.getName().toLowerCase();

                if (name.endsWith(".zip")) {
                    try {
                        List<File> extracted = ZipUtil.extractImages(file);
                        if (extracted.isEmpty()) {
                            new Alert(Alert.AlertType.WARNING,
                                    "Not found jpg/png in " + file.getName()).show();
                        }
                        for (File img : extracted) {
                            success |= addImage(img);
                        }
                    } catch (IOException e) {
                        new Alert(Alert.AlertType.ERROR,
                                "Can't read zip file: " + file.getName()).show();
                    }
                } else if (isImage(file)) {
                    success |= addImage(file);
                }
            }
        }

        event.setDropCompleted(success);
        event.consume();
    }

    @FXML
    private void handleStart(ActionEvent event) {
        if (imageFiles.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Drag and drop the image file first.").show();
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/se233/adpro_project1/editor.fxml"));
            Parent root = loader.load();

            EditorController editor = loader.getController();
            editor.setImages(new ArrayList<>(imageFiles));

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.getScene().setRoot(root);
            stage.setTitle("Editor");
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Unable to open the editor page: " + e.getMessage()).show();
        }
    }

    private boolean addImage(File file) {
        if (imageFiles.contains(file)) return false;
        imageFiles.add(file);
        fileListView.getItems().add(file.getName());
        return true;
    }

    private boolean isImage(File file) {
        String name = file.getName().toLowerCase();
        return name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg");
    }



}