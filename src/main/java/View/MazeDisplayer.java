package View;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.MazeState;
import algorithms.search.Solution;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

/* --- draws the maze, player and solution path --- */
public class MazeDisplayer {

    /* --- canvas injected from Game.fxml --- */
    private final Canvas canvas;

    /* --- cached geometry --- */
    private int rows, cols;
    private double cellW, cellH;

    /* --- ctor --- */
    public MazeDisplayer(Canvas canvas) { this.canvas = canvas; }

    /* --------------------------------------------------
       --- draw walls  +  goal cell  +  player red dot ---
       -------------------------------------------------- */
    public void drawMaze(Maze maze, int playerRow, int playerCol) {
        if (maze == null) return;

        GraphicsContext gc = canvas.getGraphicsContext2D();

        rows  = maze.getRows();
        cols  = maze.getColumns();
        cellH = canvas.getHeight() / rows;
        cellW = canvas.getWidth()  / cols;

        /* --- clear background --- */
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        /* --- walls --- */
        gc.setFill(Color.BLACK);
        int[][] grid = maze.getMaze();
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                if (grid[r][c] == 1)          // 1 = wall
                    drawCell(gc, r, c);

        /* --- goal cell --- */
        Position goal = maze.getGoalPosition();
        gc.setFill(Color.GOLD);
        drawCell(gc, goal.getRowIndex(), goal.getColumnIndex());

        /* --- player --- */
        gc.setFill(Color.RED);
        gc.fillOval(playerCol * cellW, playerRow * cellH, cellW, cellH);
    }

    /* --------------------------------------------------
       --- draw solution path in green (no diagonals) ---
       -------------------------------------------------- */
    public void drawSolution(Solution solution) {
        if (solution == null || solution.getSolutionPath().isEmpty()) return;

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.LIMEGREEN);
        gc.setLineWidth(3);

        AState prev = null;
        for (AState curr : solution.getSolutionPath()) {
            if (prev != null) {
                int[] p = toRC(prev);   // previous cell
                int[] c = toRC(curr);   // current  cell
                gc.strokeLine(p[1] * cellW + cellW / 2,
                        p[0] * cellH + cellH / 2,
                        c[1] * cellW + cellW / 2,
                        c[0] * cellH + cellH / 2);
            }
            prev = curr;
        }
    }

    /* --- safe converter: AState ➜ {row,col} --- */
    private int[] toRC(AState s) {
        /* preferred: if the JAR used MazeState, use it directly */
        if (s instanceof MazeState ms) {
            return new int[]{ms.getRow(), ms.getCol()};
        }

        /* fallback: strip all non-digit / non-comma chars then split */
        String clean = s.getState().replaceAll("[^0-9,]", "");
        String[] parts = clean.split(",");
        return new int[]{
                Integer.parseInt(parts[0]),
                Integer.parseInt(parts[1])
        };
    }

    /* --- helper to paint one rectangle --- */
    private void drawCell(GraphicsContext gc, int r, int c) {
        gc.fillRect(c * cellW, r * cellH, cellW, cellH);
    }
}
