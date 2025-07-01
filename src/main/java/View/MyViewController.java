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

    // --- FXML controls ---
    @FXML private Button   btnGiveUp;
    @FXML private Button   btnNewGame;
    @FXML private Canvas   mazeCanvas;

    private final SceneManager sceneManager = new SceneManager();
    private MyViewModel  viewModel;
    private MazeDisplayer displayer;

    @FXML
    private void initialize() {
        displayer = new MazeDisplayer(mazeCanvas);

        // --- keep keyboard focus ---
        mazeCanvas.setFocusTraversable(true);
        mazeCanvas.setOnMouseClicked(e -> mazeCanvas.requestFocus());
        Platform.runLater(() -> mazeCanvas.requestFocus());

        // --- hide New-Game button when starting ---
        btnNewGame.setVisible(false);
        btnNewGame.setManaged(false);

        Platform.runLater(() -> {
            Scene scene = mazeCanvas.getScene();
            if (scene != null)
                scene.setOnKeyPressed(this::onKeyPressed);
            mazeCanvas.requestFocus();
        });
    }

    @FXML private void onGiveUp() {
        viewModel.solveMaze();
        AudioManager.get().stopBackground();
        AudioManager.get().playBackground(
                sceneManager.getCurrentTheme().sfxGiveUp());
    }

    @FXML
    private void onKeyPressed(KeyEvent e) {
        if (viewModel == null) return;
        viewModel.moveCharacter(e.getCode());
        e.consume();
    }

    @Override
    public void bindViewModel(MyViewModel vm) {
        this.viewModel = vm;

        // --- disable buttons when no maze is displayed ---
        btnGiveUp.disableProperty().bind(vm.mazeLoaded.not());

        // --- draw when maze loads ---
        vm.mazeLoaded.addListener((o,ov,n)-> { if (n) displayMaze(); });

        // --- redraw whenever the character moves ---
        vm.characterRow   .addListener((o,ov,n)-> displayMaze());
        vm.characterColumn.addListener((o,ov,n)-> displayMaze());

        vm.solutionShown.addListener((o,ov,n)-> { if (n) displaySolution(); });
        vm.goalReached  .addListener((o,ov,n)-> { if (n) displayVictory(); });

        if (vm.mazeLoaded.get()) displayMaze();
    }

    @Override
    public void displayMaze() {
        displayer.drawMaze(viewModel.getMaze(),
                viewModel.getCharacterRow(),
                viewModel.getCharacterColumn());
        mazeCanvas.requestFocus();
    }
    @Override public void displaySolution() {
        displayer.drawSolution(viewModel.getSolution());
         // --- reveal the New-Game button ---
        btnNewGame.setManaged(true);
        btnNewGame.setVisible(true);
    }
    @Override public void displayVictory()  {
        try {
            AudioManager.get().stopBackground();
            AudioManager.get().playBackground("win");
            sceneManager.switchToVictory("style.css");
        } catch (Exception e){
            showError(e.getMessage());
        }
    }

    @Override public void showError(String msg) {
        new Alert(Alert.AlertType.ERROR, msg).showAndWait();
    }

    @FXML
    private void onNewGame(ActionEvent e) {
        try {
            AudioManager.get().stopBackground();
            sceneManager.switchToStart(e);   // ---> same flow as menu “New”
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
}
}