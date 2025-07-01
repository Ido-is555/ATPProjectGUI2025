package View;

import ViewModel.MyViewModel;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.event.ActionEvent;
import java.io.IOException;

public class PropertiesController implements IView {
    private SceneManager sceneManager = new SceneManager();

    @FXML private TextField txtRows;
    @FXML private TextField txtCols;

    private MyViewModel viewModel = SceneManager.getViewModel();

    @FXML private void onPlayClicked(ActionEvent event) {
        try {
            int rows = Integer.parseInt(txtRows.getText());
            int cols = Integer.parseInt(txtCols.getText());
            viewModel.generateMaze(rows, cols);
                try {
                    sceneManager.switchToGame(event);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
        } catch (NumberFormatException nfe) {
            showError("Rows/Cols must be integers");
        }
    }

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