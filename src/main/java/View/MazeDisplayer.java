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

public class MazeDisplayer {
    private final Canvas canvas;
    private int rows, cols;
    private double cellW, cellH;
    private Image playerImg, goalImg;
    private Set<Integer> visited = new HashSet<>();

    public MazeDisplayer(Canvas canvas) { this.canvas = canvas; }

    public void drawMaze(Maze maze, int playerRow, int playerCol) {
        if (maze == null) return;
        // --- updating the theme ---
        syncTheme(canvas.getScene());

        rows  = maze.getRows();
        cols  = maze.getColumns();
        cellH = canvas.getHeight() / rows;
        cellW = canvas.getWidth()  / cols;
        visited.add(playerRow * cols + playerCol);
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setGlobalAlpha(0.5);
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setGlobalAlpha(1.0);

        gc.setFill(Color.BLACK);
        int[][] grid = maze.getMaze();
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                if (grid[r][c] == 1)
                    drawCell(gc, r, c);

        gc.setFill(Color.rgb(200, 200, 200, 0.35));
        for (int code : visited) {
            int r = code / cols, c = code % cols;
            drawCell(gc, r, c);
        }

        Position g = maze.getGoalPosition();
        gc.drawImage(goalImg,
                g.getColumnIndex() * cellW,
                g.getRowIndex()    * cellH,
                cellW, cellH);

        gc.drawImage(playerImg,
                playerCol * cellW,
                playerRow * cellH,
                cellW, cellH);
    }

    // --- showing a green line for solution path ---
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

    private int[] rc(AState s) {
        String[] parts = s.getState().replaceAll("[^0-9,]", "").split(",");
        return new int[]{ Integer.parseInt(parts[0]), Integer.parseInt(parts[1]) };
    }

    private void drawCell(GraphicsContext gc, int r, int c) {
        gc.fillRect(c * cellW, r * cellH, cellW, cellH);
    }

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

    private Image load(String file) {
        return new Image(getClass().getResource("/images/" + file).toExternalForm());
    }
}
