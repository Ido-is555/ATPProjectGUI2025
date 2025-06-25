package ViewModel;

import Model.IModel;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.beans.property.*;

public class MyViewModel {
    private IModel model;

    public IntegerProperty characterRow = new SimpleIntegerProperty();
    public IntegerProperty characterColumn = new SimpleIntegerProperty();
    public BooleanProperty goalReached = new SimpleBooleanProperty(false);
    public BooleanProperty solutionAvailable = new SimpleBooleanProperty(false);

    public MyViewModel(IModel model) {
        this.model = model;
        this.characterRow.set(model.getCharacterRow());
        this.characterColumn.set(model.getCharacterColumn());
    }

    // --- generate a new maze ---
    public void generateMaze(int rows, int cols) {
        model.generateMaze(rows, cols);
        updateCharacterLocation();
        goalReached.set(false);
        solutionAvailable.set(false);
    }

    // --- move the character ---
    public void moveCharacter(String direction) {
        model.moveCharacter(direction);
        updateCharacterLocation();
        goalReached.set(model.isGoalReached());
    }

    // --- solve the maze ---
    public void solveMaze() {
        model.solveMaze();
        solutionAvailable.set(model.getSolution() != null);
    }

    // --- get current maze ---
    public Maze getMaze() {
        return model.getMaze();
    }

    // --- get solution ---
    public Solution getSolution() {
        return model.getSolution();
    }

    // --- update character location bindings ---
    private void updateCharacterLocation() {
        characterRow.set(model.getCharacterRow());
        characterColumn.set(model.getCharacterColumn());
    }

    // --- shutdown model ---
    public void shutdown() {
        model.shutdown();
    }
}
