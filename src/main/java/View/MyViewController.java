package View;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import ViewModel.MyViewModel;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;

public class MyViewController implements IView {

    /* ---------- FXML controls ---------- */
    @FXML private MenuItem menuNew, menuSave, menuExit;
    @FXML private MenuItem menuHelp, menuAbout, menuProperties;
    @FXML private Button   btnGiveUp;
    @FXML private Canvas   mazeCanvas;

    /* ---------- helpers ---------- */
    private final SceneManager sceneManager = new SceneManager();
    private MyViewModel  viewModel;
    private MazeDisplayer displayer;

    /* ---------- init ---------- */
    @FXML
    private void initialize() {
        displayer = new MazeDisplayer(mazeCanvas);

        // allow keyboard focus + click-to-focus
        mazeCanvas.setFocusTraversable(true);
        mazeCanvas.setOnMouseClicked(e -> mazeCanvas.requestFocus());

        // scene-level key handler (runs after FXML is attached to a Scene)
        Platform.runLater(() -> {
            Scene scene = mazeCanvas.getScene();
            if (scene != null)
                scene.setOnKeyPressed(this::onKeyPressed);
            mazeCanvas.requestFocus();            // initial focus
        });
    }

    /* ---------- menu actions ---------- */
    @FXML private void onNew(ActionEvent e) { try { sceneManager.switchToProperties(e); } catch (Exception ex) { showError(ex.getMessage()); } }
    @FXML private void onSave() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Maze (*.maze)", "*.maze"));
        File f = fc.showSaveDialog(menuSave.getParentPopup().getScene().getWindow());
        if (f != null) try { viewModel.saveMaze(f); } catch (Exception ex) { showError(ex.getMessage()); }
    }
    @FXML private void onExit() { viewModel.shutdown(); System.exit(0); }

    /* ---------- gameplay actions ---------- */
    @FXML private void onGiveUp() { viewModel.solveMaze(); }

    /** unified key handler (scene-wide) */
    @FXML
    private void onKeyPressed(KeyEvent e) {
        if (viewModel == null) return;
        viewModel.moveCharacter(e.getCode());   // update model
        e.consume();                            // stop MenuBar etc. from using the key
        // draw is triggered by listeners – no need to call displayMaze() here
    }

    /* ---------- IView binding ---------- */
    @Override
    public void bindViewModel(MyViewModel vm) {
        this.viewModel = vm;

        /* disable buttons when no maze */
        menuSave.disableProperty().bind(vm.mazeLoaded.not());
        btnGiveUp.disableProperty().bind(vm.mazeLoaded.not());

        /* draw when maze loads */
        vm.mazeLoaded.addListener((o,ov,n)-> { if (n) displayMaze(); });

        /* redraw whenever character moves */
        vm.characterRow   .addListener((o,ov,n)-> displayMaze());
        vm.characterColumn.addListener((o,ov,n)-> displayMaze());

        /* other events */
        vm.solutionShown.addListener((o,ov,n)-> { if (n) displaySolution(); });
        vm.goalReached  .addListener((o,ov,n)-> { if (n) displayVictory(); });

        /* came back to this screen with maze already loaded */
        if (vm.mazeLoaded.get()) displayMaze();
    }

    /* ---------- drawing ---------- */
    @Override
    public void displayMaze() {
        displayer.drawMaze(viewModel.getMaze(),
                viewModel.getCharacterRow(),
                viewModel.getCharacterColumn());
        mazeCanvas.requestFocus();          // keep focus after redraw
    }
    @Override public void displaySolution() { displayer.drawSolution(viewModel.getSolution()); }
    @Override public void displayVictory()  { try { sceneManager.switchToVictory(); } catch (Exception ex) { showError(ex.getMessage()); } }

    /* ---------- misc ---------- */
    @Override public void showError(String msg) { new Alert(Alert.AlertType.ERROR, msg).showAndWait(); }
}

