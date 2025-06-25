package Model;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import Client.*;
import Server.*;

import java.io.*;

public class MyModel implements IModel {
    private Maze maze;
    private Solution solution;
    private int characterRow;
    private int characterColumn;

    private Server generateServer;
    private Server solveServer;

    public MyModel() {
        generateServer = new Server(5400, new ServerStrategyGenerateMaze());
        solveServer = new Server(5401, new ServerStrategySolveSearchProblem());
        generateServer.start();
        solveServer.start();
    }

    @Override
    public void generateMaze(int rows, int columns) {
        Client client = new Client("localhost", 5400, (in, out) -> {
            try {
                ObjectOutputStream toServer = new ObjectOutputStream(out);
                ObjectInputStream fromServer = new ObjectInputStream(in);
                int[] dimensions = new int[]{rows, columns};
                toServer.writeObject(dimensions);
                toServer.flush();
                byte[] compressedMaze = (byte[]) fromServer.readObject();
                maze = new Maze(compressedMaze);
                characterRow = maze.getStartPosition().getRowIndex();
                characterColumn = maze.getStartPosition().getColumnIndex();
                solution = null; // --- reset solution when new maze is generated ---
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        client.communicateWithServer();
    }

    @Override
    public void moveCharacter(String direction) {
        switch (direction) {
            case "UP" -> {
                if (maze.validMove(characterRow - 1, characterColumn)) characterRow--;
            }
            case "DOWN" -> {
                if (maze.validMove(characterRow + 1, characterColumn)) characterRow++;
            }
            case "LEFT" -> {
                if (maze.validMove(characterRow, characterColumn - 1)) characterColumn--;
            }
            case "RIGHT" -> {
                if (maze.validMove(characterRow, characterColumn + 1)) characterColumn++;
            }
        }
    }

    @Override
    public Maze getMaze() {
        return maze;
    }

    @Override
    public Solution getSolution() {
        return solution;
    }

    @Override
    public int getCharacterRow() {
        return characterRow;
    }

    @Override
    public int getCharacterColumn() {
        return characterColumn;
    }

    @Override
    public boolean isGoalReached() {
        return maze != null && maze.getGoalPosition().getRowIndex() == characterRow
                && maze.getGoalPosition().getColumnIndex() == characterColumn;
    }

    @Override
    public void solveMaze() {
        if (maze == null) return;
        Client client = new Client("localhost", 5401, (in, out) -> {
            try {
                ObjectOutputStream toServer = new ObjectOutputStream(out);
                ObjectInputStream fromServer = new ObjectInputStream(in);
                toServer.writeObject(maze);
                toServer.flush();
                solution = (Solution) fromServer.readObject();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        client.communicateWithServer();
    }

    @Override
    public void shutdown() {
        generateServer.stop();
        solveServer.stop();
    }
}
