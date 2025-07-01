package View;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import ViewModel.MyViewModel;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;

public class MyViewController implements IView {

    /* ---------- FXML controls ---------- */
    @FXML private MenuItem menuNew, menuSave, menuExit;
    @FXML private MenuItem menuHelp, menuAbout, menuProperties;
    @FXML private Button   btnGiveUp;
    @FXML private Button   btnNewGame;
    @FXML private Canvas   mazeCanvas;

    /* ---------- helpers ---------- */
    private final SceneManager sceneManager = new SceneManager();
    private MyViewModel  viewModel;
    private MazeDisplayer displayer;

    /* ---------- init ---------- */
    @FXML
    private void initialize() {
        displayer = new MazeDisplayer(mazeCanvas);

        // --- keep keyboard focus ---
        mazeCanvas.setFocusTraversable(true);
        mazeCanvas.setOnMouseClicked(e -> mazeCanvas.requestFocus());
        Platform.runLater(() -> mazeCanvas.requestFocus());

        // --- hide New-Game button at startup ---
        btnNewGame.setVisible(false);
        btnNewGame.setManaged(false);

        // scene-level key handler (runs after FXML is attached to a Scene)
        Platform.runLater(() -> {
            Scene scene = mazeCanvas.getScene();
            if (scene != null)
                scene.setOnKeyPressed(this::onKeyPressed);
            mazeCanvas.requestFocus();            // initial focus
        });
    }

    /* ---------- gameplay actions ---------- */
    @FXML private void onGiveUp() {
        viewModel.solveMaze();
        AudioManager.get().stopBackground();
        AudioManager.get().playBackground(
                sceneManager.getCurrentTheme().sfxGiveUp());
    }

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
    @Override public void displaySolution() {
        displayer.drawSolution(viewModel.getSolution());

         // --- reveal the New-Game button ---
        btnNewGame.setManaged(true);
        btnNewGame.setVisible(true);
    }
    @Override public void displayVictory()  {
        try {
            AudioManager.get().stopBackground();          // fade loop
            AudioManager.get().playBackground("win");                 // play win jingle
            sceneManager.switchToVictory("style.css");
        } catch (Exception e){
            showError(e.getMessage());
        }
    }

    /* ---------- misc ---------- */
    @Override public void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }

    @FXML
    private void onNewGame(ActionEvent e) {
        try {
            AudioManager.get().stopBackground();
            sceneManager.switchToStart(e);   // --- same flow as menu “New” ---
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
}
}

