package ViewModel;

import Model.IModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.beans.property.*;
import javafx.scene.input.KeyCode;

import java.io.File;

public class MyViewModel {

    private final IModel model;

    /* --- Properties שה-View מאזין להם --- */
    public final IntegerProperty characterRow    = new SimpleIntegerProperty();
    public final IntegerProperty characterColumn = new SimpleIntegerProperty();
    public final BooleanProperty goalReached     = new SimpleBooleanProperty(false);
    public final BooleanProperty solutionShown   = new SimpleBooleanProperty(false);
    public final BooleanProperty mazeLoaded      = new SimpleBooleanProperty(false);

    public MyViewModel(IModel model) {
        this.model = model;
    }

    /* ---------- פונקציות שה-View מפעיל ---------- */
    public void generateMaze(int rows, int cols) {
        model.generateMaze(rows, cols);
        syncAll();
    }

    public void moveCharacter(KeyCode key) {
        model.moveCharacter(key);
        syncCharacter();
        goalReached.set(model.isGoalReached());
    }

    public void solveMaze() {
        model.solveMaze();
        solutionShown.set(model.getSolution() != null);
    }

    public void saveMaze(File f) throws Exception { model.saveMaze(f); }
    public void loadMaze(File f) throws Exception { model.loadMaze(f); syncAll(); }

    /* ---------- Getters ל-View ---------- */
    public Maze getMaze()               { return model.getMaze(); }
    public Solution getSolution()       { return model.getSolution(); }
    public int getCharacterRow()        { return characterRow.get(); }
    public int getCharacterColumn()     { return characterColumn.get(); }

    public void shutdown() { model.shutdown(); }

    /* ---------- עזרה ---------- */
    private void syncAll() {
        syncCharacter();
        goalReached.set(false);
        solutionShown.set(false);
        mazeLoaded.set(model.getMaze() != null);
    }

    private void syncCharacter() {
        characterRow.set(model.getCharacterRow());
        characterColumn.set(model.getCharacterColumn());
    }
}
