package View.Controllers;

import View.IView;
import View.SceneManager;
import ViewModel.MyViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class PropertiesController implements IView {

    @FXML private TextField txtRows;
    @FXML private TextField txtCols;
    @FXML private Button btnPlay;

    private MyViewModel viewModel;

    @FXML private void onPlayClicked() {
        try {
            int rows = Integer.parseInt(txtRows.getText());
            int cols = Integer.parseInt(txtCols.getText());
            viewModel.generateMaze(rows, cols);
            SceneManager.switchTo("/fxml/GameScreen.fxml");
        } catch (NumberFormatException nfe) {
            showError("Rows/Cols must be integers");
        }
    }

    /* --- IView (minimal for this screen) --- */
    @Override public void bindViewModel(MyViewModel vm) { this.viewModel = vm; }
    @Override public void displayMaze()    {}
    @Override public void displaySolution(){}
    @Override public void displayVictory() {}
    @Override public void showError(String msg){
        javafx.scene.control.Alert a=new javafx.scene.control.Alert(
                javafx.scene.control.Alert.AlertType.ERROR,msg);
        a.showAndWait();
    }
}
