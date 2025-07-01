package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;
import java.io.File;

// --- Model interface ---
public interface IModel {
    void generateMaze(int rows, int columns);
    void moveCharacter(KeyCode direction);
    Maze getMaze();
    Solution getSolution();
    int getCharacterRow();
    int getCharacterColumn();
    boolean isGoalReached();
    void solveMaze();
    void saveMaze(File target) throws Exception;
    void shutdown();
}
