package Model;

import Client.Client;
import Client.IClientStrategy;
import Server.Server;
import Server.ServerStrategyGenerateMaze;
import Server.ServerStrategySolveSearchProblem;
import IO.MyCompressorOutputStream;
import IO.MyDecompressorInputStream;
import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import javafx.scene.input.KeyCode;

import java.io.*;
import java.net.InetAddress;

public class MyModel implements IModel {

    /* ---------- game state ---------- */
    private Maze      maze;
    private Solution  solution;
    private int       characterRow;
    private int       characterColumn;

    /* ---------- local servers ---------- */
    private final Server generateServer;
    private final Server solveServer;
    private static final int LISTENING_INTERVAL_MS = 1000;

    public MyModel() {
        generateServer = new Server(5400, LISTENING_INTERVAL_MS, new ServerStrategyGenerateMaze());
        solveServer    = new Server(5401, LISTENING_INTERVAL_MS, new ServerStrategySolveSearchProblem());
        generateServer.start();
        solveServer.start();
    }

    /* ---------- generate maze ---------- */
    @Override
    public void generateMaze(int rows, int cols) {

        IClientStrategy strat = (in, out) -> {
            try (ObjectOutputStream toSrv = new ObjectOutputStream(out);
                 ObjectInputStream  from  = new ObjectInputStream(in)) {

                toSrv.writeObject(new int[]{rows, cols});
                toSrv.flush();

                /* read compressed maze from server */
                byte[] compressed = (byte[]) from.readObject();

                /* decompress */
                int expectedSize = 12 + rows * cols;          // 12-byte header + body
                byte[] decompressed = new byte[expectedSize];
                new MyDecompressorInputStream(new ByteArrayInputStream(compressed))
                        .read(decompressed);

                maze = new Maze(decompressed);
                characterRow    = maze.getStartPosition().getRowIndex();
                characterColumn = maze.getStartPosition().getColumnIndex();
                solution        = null;

            } catch (Exception e) { e.printStackTrace(); }
        };

        try {
            new Client(InetAddress.getByName("localhost"), 5400, strat).communicateWithServer();
        } catch (Exception e) { e.printStackTrace(); }
    }

    /* ---------- character movement ---------- */
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
            characterRow    = r;
            characterColumn = c;
        }
    }
    private boolean isValidMove(int r, int c) {
        return maze != null &&
                r >= 0 && r < maze.getRows() &&
                c >= 0 && c < maze.getColumns() &&
                maze.getMaze()[r][c] == 0;
    }

    /* ---------- solve maze ---------- */
    @Override
    public void solveMaze() {
        if (maze == null) return;

        IClientStrategy strat = (in, out) -> {
            try (ObjectOutputStream toSrv = new ObjectOutputStream(out);
                 ObjectInputStream  from  = new ObjectInputStream(in)) {

                toSrv.writeObject(maze);
                toSrv.flush();
                solution = (Solution) from.readObject();

            } catch (Exception e) { e.printStackTrace(); }
        };

        try {
            new Client(InetAddress.getByName("localhost"), 5401, strat).communicateWithServer();
        } catch (Exception e) { e.printStackTrace(); }
    }

    /* ---------- save / load ---------- */
    @Override
    public void saveMaze(File file) throws Exception {
        if (maze == null) return;
        try (MyCompressorOutputStream cos = new MyCompressorOutputStream(new FileOutputStream(file))) {
            cos.write(maze.toByteArray());
            cos.flush();
        }
    }

    @Override
    public void loadMaze(File file) throws Exception {
        try (MyDecompressorInputStream dis = new MyDecompressorInputStream(new FileInputStream(file))) {

            /* read fully */
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int b;
            while ((b = dis.read()) != -1) baos.write(b);
            byte[] data = baos.toByteArray();

            maze = new Maze(data);
            characterRow    = maze.getStartPosition().getRowIndex();
            characterColumn = maze.getStartPosition().getColumnIndex();
            solution        = null;
        }
    }

    /* ---------- getters ---------- */
    @Override public Maze     getMaze()             { return maze; }
    @Override public Solution getSolution()         { return solution; }
    @Override public int      getCharacterRow()     { return characterRow; }
    @Override public int      getCharacterColumn()  { return characterColumn; }
    @Override public boolean  isGoalReached() {
        return maze != null &&
                characterRow == maze.getGoalPosition().getRowIndex() &&
                characterColumn == maze.getGoalPosition().getColumnIndex();
    }

    /* ---------- shutdown ---------- */
    @Override public void shutdown() {
        generateServer.stop();
        solveServer.stop();
    }
}
