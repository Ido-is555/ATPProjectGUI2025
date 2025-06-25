package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;

public interface IModel {
    // --- generate a new maze with the given rows and columns ---
    void generateMaze(int rows, int columns);

    // --- move the character in the given direction (UP, DOWN, LEFT, RIGHT) ---
    void moveCharacter(String direction);

    // --- return the current maze object ---
    Maze getMaze();

    // --- return the solution for the current maze ---
    Solution getSolution();

    // --- return the current character row position ---
    int getCharacterRow();

    // --- return the current character column position ---
    int getCharacterColumn();

    // --- check if the character reached the goal ---
    boolean isGoalReached();

    // --- solve the current maze and return the solution ---
    void solveMaze();

    // --- shut down the servers (cleanup resources) ---
    void shutdown();
}
