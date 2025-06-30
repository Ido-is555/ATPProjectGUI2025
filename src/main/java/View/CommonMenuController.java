package View;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.stage.Modality;
import javafx.scene.control.MenuItem;
import java.io.IOException;

public class CommonMenuController {
    @FXML private MenuItem menuNew, menuSave;
    @FXML private MenuItem menuProperties, menuScreenshot;
    @FXML private MenuItem menuExit, menuHelp, menuAbout;


    @FXML private void onSave() { /* ... save logic ... */ }
    @FXML private void onProperties() { /* ... show properties dialog ... */ }

    @FXML private void onExit() {
        // clean shutdown & close window
        Stage s = (Stage) menuExit.getParentPopup().getOwnerWindow();
        /* viewModel.shutdown() if needed */
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
    } }
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
