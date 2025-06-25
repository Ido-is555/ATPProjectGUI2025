package View;

import ViewModel.MyViewModel;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;

import java.io.File;

//Game Screen
public class MyViewController implements IView {

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
    }

    /* ---------- Menu Actions ---------- */
    @FXML private void onNew() { SceneManager.switchTo("/fxml/PropertiesScreen.fxml"); }

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

    @FXML private void onKeyPressed(KeyEvent e) {
        viewModel.moveCharacter(e.getCode());
    }

    /* ---------- IView ---------- */
    @Override public void bindViewModel(MyViewModel vm) {
        this.viewModel = vm;

        // disable-bindings
        menuSave.disableProperty().bind(vm.mazeLoaded.not());
        btnGiveUp.disableProperty().bind(vm.mazeLoaded.not());

        vm.mazeLoaded.addListener((o,oldVal,n)-> { if(n) displayMaze(); });
        vm.solutionShown.addListener((o,oldVal,n)-> { if(n) displaySolution(); });
        vm.goalReached.addListener((o,oldVal,n)-> { if(n) displayVictory(); });

        // ציור ראשוני אם חזרנו לכאן עם מבוך כבר קיים
        if (vm.mazeLoaded.get()) displayMaze();
    }

    @Override public void displayMaze() {
        displayer.drawMaze(viewModel.getMaze(),
                           viewModel.getCharacterRow(),
                           viewModel.getCharacterColumn());
    }

    @Override public void displaySolution() {
        displayer.drawSolution(viewModel.getSolution());
    }

    @Override public void displayVictory() {
        SceneManager.switchTo("/fxml/VictoryScreen.fxml");
    }

    @Override public void showError(String message) {
        new Alert(Alert.AlertType.ERROR, message).showAndWait();
    }
}
