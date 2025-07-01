package View;
import ViewModel.MyViewModel;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.io.File;
public class SaveController {
    @FXML
    private TextField txtFileName;

    private MyViewModel viewModel;

    public void init(MyViewModel vm) {
        this.viewModel = vm;
    }

    @FXML
    private void onSaveClicked() {
        if (viewModel == null || viewModel.getMaze() == null) {
            new Alert(Alert.AlertType.INFORMATION, "No maze to save. Please start a game first.").showAndWait();
            return;
        }

        String name = txtFileName.getText().trim();
        if (name.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter a file name.").showAndWait();
            return;
        }

        if (!name.endsWith(".maze")) {
            name += ".maze";
        }

        FileChooser chooser = new FileChooser();
        chooser.setInitialFileName(name);
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Maze Files", "*.maze"));

        File file = chooser.showSaveDialog(txtFileName.getScene().getWindow());
        if (file != null) {
            try {
                viewModel.saveMaze(file);
                new Alert(Alert.AlertType.INFORMATION, "Maze saved successfully.").showAndWait();
                ((Stage) txtFileName.getScene().getWindow()).close();
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR, "Failed to save: " + e.getMessage()).showAndWait();
            }
        }
    }

    // static helper for opening the Save window from menu or anywhere else
    public static void openSaveWindow(MyViewModel viewModel) {
        try {
            FXMLLoader loader = new FXMLLoader(SaveController.class.getResource("/View/Save.fxml"));
            Parent root = loader.load();

            SaveController controller = loader.getController();
            controller.init(viewModel);

            Stage stage = new Stage();
            stage.setTitle("Save Maze");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Could not open Save window.").showAndWait();
        }
    }

}