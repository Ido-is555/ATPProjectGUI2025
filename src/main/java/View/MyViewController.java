package View;

import javafx.event.ActionEvent;
import ViewModel.MyViewModel;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;

//Game Screen
public class MyViewController implements IView {
    private SceneManager sceneManager = new SceneManager();

    @FXML private MenuItem menuNew, menuSave, menuExit;
    @FXML private MenuItem menuHelp, menuAbout;
    @FXML private MenuItem menuProperties;
    @FXML private Button btnGiveUp;
    @FXML private Canvas mazeCanvas;

    private MyViewModel viewModel;
    private MazeDisplayer displayer;

    @FXML private void initialize() {
        displayer = new MazeDisplayer(mazeCanvas);
        mazeCanvas.setFocusTraversable(true);
        javafx.application.Platform.runLater(() -> mazeCanvas.requestFocus());
        mazeCanvas.setOnMouseClicked(e -> mazeCanvas.requestFocus());
    }

    /* ---------- Menu Actions ---------- */
    @FXML private void onNew(ActionEvent event) {
        try {
            sceneManager.switchToProperties(event);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML private void onSave() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Maze (*.maze)","*.maze"));
        File f = fc.showSaveDialog(menuSave.getParentPopup().getScene().getWindow());
        if (f != null) try { viewModel.saveMaze(f); }
        catch (Exception e){ showError(e.getMessage()); }
    }

    @FXML private void onExit() {
        viewModel.shutdown();
        System.exit(0);
    }

    /* ---------- Gameplay Actions ---------- */
    @FXML private void onGiveUp() { viewModel.solveMaze(); }

   @FXML
    private void onKeyPressed(KeyEvent e) {
    viewModel.moveCharacter(e.getCode());   // מעדכן את המודל
    displayMaze();                          // מצייר שוב מיד את המפה + שחקן
    }

    /* ---------- IView ---------- */
    @Override public void bindViewModel(MyViewModel vm) {
         this.viewModel = vm;

        menuSave.disableProperty().bind(vm.mazeLoaded.not());
        btnGiveUp.disableProperty().bind(vm.mazeLoaded.not());

        // draw when the maze is first generated / loaded
        vm.mazeLoaded.addListener((o, oldV, n) -> { if (n) displayMaze(); });

        // redraw every time the character moves
        vm.characterRow   .addListener((o, oldV, n) -> displayMaze());
        vm.characterColumn.addListener((o, oldV, n) -> displayMaze());

        // show solution / victory screens
        vm.solutionShown.addListener((o, oldV, n) -> { if (n) displaySolution(); });
        vm.goalReached  .addListener((o, oldV, n) -> { if (n) displayVictory(); });

        // if we on this screen with a maze already loaded
        if (vm.mazeLoaded.get()) displayMaze();
    }

    @Override public void displayMaze() {
        System.out.println("displayMaze called");
        displayer.drawMaze(viewModel.getMaze(),
                           viewModel.getCharacterRow(),
                           viewModel.getCharacterColumn());
    }

    @Override public void displaySolution() {
        displayer.drawSolution(viewModel.getSolution());
    }

    @Override public void displayVictory() {
        try {
            sceneManager.switchToVictory();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override public void showError(String message) {
        new Alert(Alert.AlertType.ERROR, message).showAndWait();
    }

}
