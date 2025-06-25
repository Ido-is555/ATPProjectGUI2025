package Model;

import Client.Client;
import Client.IClientStrategy;
import Server.Server;
import Server.ServerStrategyGenerateMaze;
import Server.ServerStrategySolveSearchProblem;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;

import java.io.*;
import java.net.InetAddress;

public class MyModel implements IModel {

    /* ---------- מצב משחק ---------- */
    private Maze maze;
    private Solution solution;
    private int characterRow;
    private int characterColumn;

    /* ---------- שרתים מקומיים ---------- */
    private final Server generateServer;
    private final Server solveServer;
    private static final int LISTENING_INTERVAL_MS = 1000;

    public MyModel() {
        /* קונסטרקטור Server שב-JAR:  (port, intervalMS, strategy) */
        generateServer = new Server(5400, LISTENING_INTERVAL_MS, new ServerStrategyGenerateMaze());
        solveServer    = new Server(5401, LISTENING_INTERVAL_MS, new ServerStrategySolveSearchProblem());
        generateServer.start();
        solveServer.start();
    }

    /* ---------- יצירת מבוך ---------- */
    @Override
    public void generateMaze(int rows, int columns) {
        IClientStrategy strat = (in, out) -> {
            try (ObjectOutputStream toServer = new ObjectOutputStream(out);
                 ObjectInputStream  fromSrv  = new ObjectInputStream(in)) {

                toServer.writeObject(new int[]{rows, columns});
                toServer.flush();

                byte[] compressed = (byte[]) fromSrv.readObject();
                maze = new Maze(compressed);

                characterRow    = maze.getStartPosition().getRowIndex();
                characterColumn = maze.getStartPosition().getColumnIndex();
                solution = null;
            } catch (Exception e) { e.printStackTrace(); }
        };

        /* קונסטרקטור Client שב-JAR:  (InetAddress, port, strategy) */
        try {
            new Client(InetAddress.getByName("localhost"), 5400, strat).communicateWithServer();
        } catch (Exception e) { e.printStackTrace(); }
    }

    /* ---------- תזוזת דמות ---------- */
    @Override
    public void moveCharacter(KeyCode key) {
        if (maze == null) return;
        switch (key) {
            case UP, KP_UP, W    -> tryMove(characterRow - 1, characterColumn);
            case DOWN, KP_DOWN, S-> tryMove(characterRow + 1, characterColumn);
            case LEFT, KP_LEFT, A-> tryMove(characterRow, characterColumn - 1);
            case RIGHT, KP_RIGHT,D-> tryMove(characterRow, characterColumn + 1);
        }
    }

    private void tryMove(int r, int c) {
        if (isValidMove(r, c)) {
            characterRow = r;
            characterColumn = c;
        }
    }

    private boolean isValidMove(int r, int c) {
        return maze != null &&
               r >= 0 && r < maze.getRows() &&
               c >= 0 && c < maze.getColumns() &&
               maze.getMaze()[r][c] == 0;       // 0 = דרך, 1 = קיר
    }

    /* ---------- פתרון מבוך ---------- */
    @Override
    public void solveMaze() {
        if (maze == null) return;

        IClientStrategy strat = (in, out) -> {
            try (ObjectOutputStream toServer = new ObjectOutputStream(out);
                 ObjectInputStream  fromSrv  = new ObjectInputStream(in)) {

                toServer.writeObject(maze);
                toServer.flush();
                solution = (Solution) fromSrv.readObject();
            } catch (Exception e) { e.printStackTrace(); }
        };

        try {
            new Client(InetAddress.getByName("localhost"), 5401, strat).communicateWithServer();
        } catch (Exception e) { e.printStackTrace(); }
    }

    /* ---------- שמירה / טעינה ---------- */
    @Override
    public void saveMaze(File file) throws Exception {
        if (maze == null) return;
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
            oos.writeObject(maze.toByteArray());
        }
    }

    @Override
    public void loadMaze(File file) throws Exception {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            byte[] data = (byte[]) ois.readObject();
            maze = new Maze(data);
            characterRow    = maze.getStartPosition().getRowIndex();
            characterColumn = maze.getStartPosition().getColumnIndex();
            solution = null;
        }
    }

    /* ---------- Getters ---------- */
    @Override public Maze      getMaze()            { return maze; }
    @Override public Solution  getSolution()        { return solution; }
    @Override public int       getCharacterRow()    { return characterRow; }
    @Override public int       getCharacterColumn() { return characterColumn; }
    @Override public boolean   isGoalReached() {
        return maze != null &&
               characterRow == maze.getGoalPosition().getRowIndex() &&
               characterColumn == maze.getGoalPosition().getColumnIndex();
    }

    /* ---------- כיבוי ---------- */
    @Override public void shutdown() {
        generateServer.stop();
        solveServer.stop();
    }
}
