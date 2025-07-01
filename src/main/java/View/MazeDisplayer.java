package View;

import algorithms.mazeGenerators.Maze;
import algorithms.mazeGenerators.Position;
import algorithms.search.AState;
import algorithms.search.Solution;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import java.util.HashSet;
import java.util.Set;

/* --------------------------------------------------------------------- */
/* ---  MazeDisplayer  --- */
/* --------------------------------------------------------------------- */
public class MazeDisplayer {

    /* --- canvas injected from MyView.fxml --- */
    private final Canvas canvas;

    /* --- geometry (updated each draw) --- */
    private int rows, cols;
    private double cellW, cellH;

    /* --- theme-dependent sprites --- */
    private Image playerImg, goalImg;

    /* --- visited cells for footprints --- */
    private Set<Integer> visited = new HashSet<>();

    public MazeDisplayer(Canvas canvas) { this.canvas = canvas; }

    /* ────────────────────────────────────────────────────────────────
       --- main draw: floor (α 0.5), walls, footprints, sprites     ---
       ──────────────────────────────────────────────────────────────── */
    public void drawMaze(Maze maze, int playerRow, int playerCol) {
        if (maze == null) return;

        /* --- update theme sprites --- */
        syncTheme(canvas.getScene());

        rows  = maze.getRows();
        cols  = maze.getColumns();
        cellH = canvas.getHeight() / rows;
        cellW = canvas.getWidth()  / cols;

        /* --- remember current cell for footprints --- */
        visited.add(playerRow * cols + playerCol);

        GraphicsContext gc = canvas.getGraphicsContext2D();

        /* --- clear previous frame --- */
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        /* --- semi-transparent white floor (shows background) --- */
        gc.setGlobalAlpha(0.5);
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setGlobalAlpha(1.0);

        /* --- draw black walls --- */
        gc.setFill(Color.BLACK);
        int[][] grid = maze.getMaze();
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                if (grid[r][c] == 1)
                    drawCell(gc, r, c);

        /* --- footprints (light-gray tint, drawn once per visited cell) --- */
        gc.setFill(Color.rgb(200, 200, 200, 0.35));
        for (int code : visited) {
            int r = code / cols, c = code % cols;
            drawCell(gc, r, c);
        }

        /* --- goal sprite (exit) --- */
        Position g = maze.getGoalPosition();
        gc.drawImage(goalImg,
                g.getColumnIndex() * cellW,
                g.getRowIndex()    * cellH,
                cellW, cellH);

        /* --- player sprite --- */
        gc.drawImage(playerImg,
                playerCol * cellW,
                playerRow * cellH,
                cellW, cellH);
    }

    /* ────────────────────────────────────────────────────────────────
       --- green solution path (diagonals split into L-shapes)      ---
       ──────────────────────────────────────────────────────────────── */
    public void drawSolution(Solution sol) {
        if (sol == null || sol.getSolutionPath().isEmpty()) return;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.LIMEGREEN);
        gc.setLineWidth(3);

        AState prev = null;
        for (AState curr : sol.getSolutionPath()) {
            if (prev != null) {
                int[] p = rc(prev), c = rc(curr);
                    gc.strokeLine(p[1]*cellW + cellW/2, p[0]*cellH + cellH/2,
                            c[1]*cellW + cellW/2, c[0]*cellH + cellH/2);
            }
            prev = curr;
        }
    }

    /* --- convert AState state-string to {row,col} --- */
    private int[] rc(AState s) {
        String[] parts = s.getState().replaceAll("[^0-9,]", "").split(",");
        return new int[]{ Integer.parseInt(parts[0]), Integer.parseInt(parts[1]) };
    }

    /* --- draw a single cell rectangle --- */
    private void drawCell(GraphicsContext gc, int r, int c) {
        gc.fillRect(c * cellW, r * cellH, cellW, cellH);
    }

    /* --- theme switcher: picks sprites by first stylesheet name --- */
    private void syncTheme(Scene scene) {
        String css = (scene != null && !scene.getStylesheets().isEmpty())
                ? scene.getStylesheets().get(0).toLowerCase() : "";

        if (css.contains("hauntedhouse")) {
            playerImg = load("witch.jpg");  goalImg = load("door.jpg");
        } else if (css.contains("princess")) {
            playerImg = load("princess.jpg");  goalImg = load("crown.jpg");
        } else if (css.contains("nemo")){
            playerImg = load("dory.jpg");  goalImg = load("nemo.jpg");
        }
        else{
            playerImg= load("start_background.jpg"); goalImg = load("start_background.jpg");
        }
    }

    /* --- utility: load from /images/ --- */
    private Image load(String file) {
        return new Image(getClass().getResource("/images/" + file).toExternalForm());
    }
}
