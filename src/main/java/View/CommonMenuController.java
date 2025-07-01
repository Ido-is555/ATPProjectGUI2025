package View;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.stage.Modality;
import javafx.scene.control.MenuItem;
import java.io.IOException;

public class CommonMenuController {
    @FXML private MenuItem menuExit;

    @FXML private void onSave() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Save.fxml"));
            Parent root = loader.load();
            SaveController controller = loader.getController();
            controller.init(SceneManager.getViewModel());

            Stage stage = new Stage();
            stage.setTitle("Save Maze");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Could not open Save window.").showAndWait();
        }
    }
    @FXML private void onProperties() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/View/Config.fxml"));
            Scene scene = new Scene(loader.load(), 400, 300);
            Stage stage = new Stage();
            stage.setTitle("Application Properties");
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --- clean shutdown ---
    @FXML private void onExit() {
        Stage s = (Stage) menuExit.getParentPopup().getOwnerWindow();
        s.close();
    }

    @FXML private void onHelp()    {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Help.fxml"));
            Parent aboutRoot = loader.load();

            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle("Help");
            dialog.setScene(new Scene(aboutRoot));
            dialog.setResizable(false);
            dialog.showAndWait();
    } catch (IOException e) {
        e.printStackTrace();
        }
    }

    @FXML private void onAbout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("About.fxml"));
            Parent aboutRoot = loader.load();

            Stage dialog = new Stage();
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.setTitle("About");
            dialog.setScene(new Scene(aboutRoot));
            dialog.setResizable(false);
            dialog.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
