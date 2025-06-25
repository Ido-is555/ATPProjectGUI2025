package View;

import algorithms.mazeGenerators.Maze;
import algorithms.search.AState;
import algorithms.search.Solution;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class MazeDisplayer {

    private final Canvas canvas;
    private int rows, cols;
    private double cellW, cellH;

    public MazeDisplayer(Canvas canvas) {
        this.canvas = canvas;
    }

    /* מצייר קירות + דמות */
    public void drawMaze(Maze maze, int characterRow, int characterCol) {
        if (maze == null) return;

        GraphicsContext gc = canvas.getGraphicsContext2D();
        rows   = maze.getRows();
        cols   = maze.getColumns();
        cellH  = canvas.getHeight() / rows;
        cellW  = canvas.getWidth()  / cols;

        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());

        gc.setFill(Color.BLACK);
        int[][] grid = maze.getMaze();
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                if (grid[r][c] == 1)
                    gc.fillRect(c * cellW, r * cellH, cellW, cellH);

        gc.setFill(Color.RED);
        gc.fillOval(characterCol * cellW, characterRow * cellH, cellW, cellH);
    }

    /* מושך קו ירוק על הפתרון */
    public void drawSolution(Solution solution) {
        if (solution == null || solution.getSolutionPath().isEmpty()) return;

        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(Color.LIMEGREEN);
        gc.setLineWidth(3);

        AState prev = null;
        for (AState curr : solution.getSolutionPath()) {
            if (prev != null) {
                int[] p = toRC(prev);
                int[] c = toRC(curr);
                gc.strokeLine(p[1] * cellW + cellW / 2,
                              p[0] * cellH + cellH / 2,
                              c[1] * cellW + cellW / 2,
                              c[0] * cellH + cellH / 2);
            }
            prev = curr;
        }
    }

    /* מפרק "row,column" למערך int[2] */
    private int[] toRC(AState s) {
        String[] parts = s.getState().split(",");
        return new int[]{ Integer.parseInt(parts[0]), Integer.parseInt(parts[1]) };
    }
}
