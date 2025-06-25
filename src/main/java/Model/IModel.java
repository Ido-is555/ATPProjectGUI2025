package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;

import java.io.File;

public interface IModel {
    void generateMaze(int rows, int columns);
    void moveCharacter(KeyCode direction);
    Maze getMaze();
    Solution getSolution();
    int getCharacterRow();
    int getCharacterColumn();
    boolean isGoalReached();
    void solveMaze();

    /* שמירה/טעינה לקובץ */
    void saveMaze(File target) throws Exception;
    void loadMaze(File source) throws Exception;

    void shutdown();
}
