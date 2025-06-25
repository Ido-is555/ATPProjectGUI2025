package Model;

import Client.Client;
import Server.Server;
import Server.ServerStrategyGenerateMaze;
import Server.ServerStrategySolveSearchProblem;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;

import java.io.*;

public class MyModel implements IModel {

    private Maze maze;
    private Solution solution;
    private int characterRow;
    private int characterColumn;

    private final Server generateServer;
    private final Server solveServer;

    public MyModel() {
        generateServer = new Server(5400, new ServerStrategyGenerateMaze());
        solveServer = new Server(5401, new ServerStrategySolveSearchProblem());
        generateServer.start();
        solveServer.start();
    }

    /* ---------- Maze Generation ---------- */
    @Override
    public void generateMaze(int rows, int columns) {
        Client client = new Client("localhost", 5400, (in, out) -> {
            try {
                ObjectOutputStream toServer = new ObjectOutputStream(out);
                ObjectInputStream fromServer = new ObjectInputStream(in);
                toServer.writeObject(new int[]{rows, columns});
                toServer.flush();

                byte[] compressedMaze = (byte[]) fromServer.readObject();
                maze = new Maze(compressedMaze);
                characterRow = maze.getStartPosition().getRowIndex();
                characterColumn = maze.getStartPosition().getColumnIndex();
                solution = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        client.communicateWithServer();
    }

    /* ---------- Movement ---------- */
    @Override
    public void moveCharacter(KeyCode code) {
        if (maze == null) return;
        switch (code) {
            case UP, KP_UP, W -> tryMove(characterRow - 1, characterColumn);
            case DOWN, KP_DOWN, S -> tryMove(characterRow + 1, characterColumn);
            case LEFT, KP_LEFT, A -> tryMove(characterRow, characterColumn - 1);
            case RIGHT, KP_RIGHT, D -> tryMove(characterRow, characterColumn + 1);
        }
    }

    private void tryMove(int r, int c) {
        if (maze.validMove(r, c)) {
            characterRow = r;
            characterColumn = c;
        }
    }

    /* ---------- Solve ---------- */
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

    /* ---------- Save / Load ---------- */
    @Override
    public void saveMaze(File target) throws Exception {
        if (maze == null) return;
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(target))) {
            oos.writeObject(maze.toByteArray());
        }
    }

    @Override
    public void loadMaze(File source) throws Exception {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(source))) {
            byte[] data = (byte[]) ois.readObject();
            maze = new Maze(data);
            characterRow = maze.getStartPosition().getRowIndex();
            characterColumn = maze.getStartPosition().getColumnIndex();
            solution = null;
        }
    }

    /* ---------- Getters ---------- */
    @Override public Maze getMaze() { return maze; }
    @Override public Solution getSolution() { return solution; }
    @Override public int getCharacterRow() { return characterRow; }
    @Override public int getCharacterColumn() { return characterColumn; }
    @Override public boolean isGoalReached() {
        return maze != null &&
               characterRow == maze.getGoalPosition().getRowIndex() &&
               characterColumn == maze.getGoalPosition().getColumnIndex();
    }

    /* ---------- Shutdown ---------- */
    @Override public void shutdown() {
        generateServer.stop();
        solveServer.stop();
    }
}
