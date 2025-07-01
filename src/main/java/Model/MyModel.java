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
import algorithms.search.BreadthFirstSearch;
import algorithms.search.SearchableMaze;
import javafx.scene.input.KeyCode;

import java.io.*;
import java.net.InetAddress;

// --- generating only solvable mazes (without diagonal moves) ---
public class MyModel implements IModel {

    private Maze maze;  // ---> the current maze
    private Solution solution;    // ---> solution (null until user clicks on "solve maze")
    // --- current player coordinates ---
    private int characterRow;
    private int characterColumn;
    // --- servers ---
    private final Server generateServer;    // ---> generates mazes on port 5400
    private final Server solveServer;   // ---> solves mazes on port 5401
    private static final int LISTENING_INTERVAL_MS = 1000;

    // --- constructor ---
    public MyModel() {
        // --- start the two local servers defined in the JAR ---
        generateServer = new Server(5400, LISTENING_INTERVAL_MS, new ServerStrategyGenerateMaze());
        solveServer    = new Server(5401, LISTENING_INTERVAL_MS, new ServerStrategySolveSearchProblem());
        generateServer.start();
        solveServer.start();
    }

    // --- generating a solvable maze (retry until BFS finds a path) ---
    @Override
    public void generateMaze(int rows, int cols) {
        while (true) {  // ---> repeat until a valid maze with a path is created
            Maze candidate = remoteGenerate(rows, cols);    // ---> asking the server for a new maze


            // --- running BFS locally ---
            Solution testSol = solveLocally(candidate);

            // --- if BFS found a path, accept this maze ---
            if (testSol != null && !testSol.getSolutionPath().isEmpty()) {
                maze           = candidate;
                solution       = null;                // ---> reset solution
                characterRow   = maze.getStartPosition().getRowIndex();
                characterColumn= maze.getStartPosition().getColumnIndex();
                break;                                // ---> exit the loop
            }
        }
    }

    // --- getting as maze bytes from server and decompress ---
    private Maze remoteGenerate(int rows, int cols) {
        final Maze[] result = new Maze[1];

        IClientStrategy strat = (in, out) -> {
            try (ObjectOutputStream toSrv = new ObjectOutputStream(out);
                 ObjectInputStream  from  = new ObjectInputStream(in)) {

                // --- send size ---
                toSrv.writeObject(new int[]{rows, cols});
                toSrv.flush();

                // --- receive compressed maze ---
                byte[] compressed = (byte[]) from.readObject();

                // --- decompress ---
                int size = 12 + rows * cols;                 // --- header + body ---
                byte[] data = new byte[size];
                new MyDecompressorInputStream(new ByteArrayInputStream(compressed))
                        .read(data);

                result[0] = new Maze(data);

            } catch (Exception e) { e.printStackTrace(); }
        };

        try {
            new Client(InetAddress.getByName("localhost"), 5400, strat).communicateWithServer();
        } catch (Exception e) { e.printStackTrace(); }

        return result[0];
    }

    // --- running BFS on the maze ---
    private Solution solveLocally(Maze m) {
        if (m == null) return null;
        SearchableMaze searchable = new SearchableMaze(m);
        BreadthFirstSearch bfs    = new BreadthFirstSearch();
        return bfs.solve(searchable);
    }

    // ---  player's movement ---
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
        // --- update position only if the target cell is a path (0) ---
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

    // ---  solve maze on server (for "show solution" button) ---
    @Override
    public void solveMaze() {
        if (maze == null) return;

        IClientStrategy strat = (in, out) -> {
            try (ObjectOutputStream toSrv = new ObjectOutputStream(out);
                 ObjectInputStream  from  = new ObjectInputStream(in)) {

                toSrv.writeObject(maze);
                toSrv.flush();
                solution = (Solution) from.readObject();     // --- receive full path ---

            } catch (Exception e) { e.printStackTrace(); }
        };

        try {
            new Client(InetAddress.getByName("localhost"), 5401, strat).communicateWithServer();
        } catch (Exception e) { e.printStackTrace(); }
    }

    // ---  saving maze to file (compressed format) ---
    @Override
    public void saveMaze(File file) throws Exception {
        if (maze == null) return;
        try (MyCompressorOutputStream cos = new MyCompressorOutputStream(new FileOutputStream(file))) {
            cos.write(maze.toByteArray());
            cos.flush();
        }
    }

    // --- getters ---
    @Override public Maze     getMaze()            { return maze; }
    @Override public Solution getSolution()        { return solution; }
    @Override public int      getCharacterRow()    { return characterRow; }
    @Override public int      getCharacterColumn() { return characterColumn; }
    @Override public boolean  isGoalReached() {
        return maze != null &&
                characterRow == maze.getGoalPosition().getRowIndex() &&
                characterColumn == maze.getGoalPosition().getColumnIndex();
    }

    // --- shutdown ---
    @Override
    public void shutdown() {
        generateServer.stop();
        solveServer.stop();
    }
}