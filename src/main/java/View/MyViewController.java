package View;

import ViewModel.MyViewModel;
import javafx.fxml.FXML;
import javafx.scene.input.KeyEvent;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;

public class MyViewController implements IView {

    private MyViewModel viewModel;

    @FXML
    private Button btnGiveUp;

    @FXML
    private MenuItem menuSave, menuNew, menuProperties, menuHelp, menuAbout, menuExit;

    @FXML
    private Pane mazeDisplayArea;

    // --- bind the ViewModel to the View ---
    @Override
    public void bindViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;

        // Observe when the player wins
        viewModel.goalReachedProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal)
                displayVictory();
        });
    }

    // --- called when user clicks "Give Up" ---
    @FXML
    public void onGiveUpClicked() {
        viewModel.solveMaze();
        displaySolution();
    }

    // --- handle key press events for character movement ---
    @FXML
    public void onKeyPressed(KeyEvent event) {
        if (viewModel != null)
            viewModel.moveCharacter(event.getCode());
    }

    // --- update the visual maze display (to be implemented later) ---
    @Override
    public void displayMaze() {
        // TODO: draw the maze and character
    }

    // --- show the solution path on screen ---
    @Override
    public void displaySolution() {
        // TODO: draw the solution path
    }

    // --- switch to victory screen ---
    @Override
    public void displayVictory() {
        // TODO: load and switch to victory scene
    }

    // --- show error message ---
    @Override
    public void showError(String message) {
        // TODO: pop up alert dialog
    }

    // --- enable or disable menu items based on current state ---
    public void updateMenuStates(boolean mazeLoaded) {
        menuSave.setDisable(!mazeLoaded);
        btnGiveUp.setDisable(!mazeLoaded);
    }
}
