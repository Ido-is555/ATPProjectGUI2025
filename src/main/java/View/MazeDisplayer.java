package View;

import algorithms.mazeGenerators.Maze;
import algorithms.search.Solution;
import algorithms.search.AState;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class MazeDisplayer {

    private final Canvas canvas;

    public MazeDisplayer(Canvas canvas) {
        this.canvas = canvas;
    }

    public void drawMaze(Maze maze, int characterRow, int characterCol) {
        if (maze == null) return;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        double h = canvas.getHeight();
        double w = canvas.getWidth();
        int rows = maze.getRows(), cols = maze.getColumns();
        double cellH = h / rows, cellW = w / cols;

        /* רקע */
        gc.clearRect(0,0,w,h);

        /* קירות */
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++)
                if (maze.getMaze()[r][c] == 1)
                    gc.fillRect(c * cellW, r * cellH, cellW, cellH);

        /* דמות */
        gc.setFill(javafx.scene.paint.Color.RED);
        gc.fillOval(characterCol * cellW, characterRow * cellH,
                    cellW, cellH);
    }

    public void drawSolution(Solution solution) {
        if (solution == null) return;
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setStroke(javafx.scene.paint.Color.LIMEGREEN);
        gc.setLineWidth(3);

        double h = canvas.getHeight(), w = canvas.getWidth();
        int rows = solution.getSolutionPath().get(0).getState().getRowIndex();
        // נעזר ב-cell size מהציור האחרון
        Maze dummy = new Maze(1,1); // למניעת NPE, לא באמת משתמשים
        int cols = rows; // placeholder
        if (solution.getSolutionPath().isEmpty()) return;
        AState first = solution.getSolutionPath().get(0);
        for (int i = 1; i < solution.getSolutionPath().size(); i++) {
            AState prev = solution.getSolutionPath().get(i - 1);
            AState curr = solution.getSolutionPath().get(i);
            // cast מאולץ, נסמך על MazeState
            int pr = prev.getRow(), pc = prev.getCol();
            int cr = curr.getRow(), cc = curr.getCol();
            double cellH = h / rows, cellW = w / cols;
            gc.strokeLine(pc * cellW + cellW / 2, pr * cellH + cellH / 2,
                          cc * cellW + cellW / 2, cr * cellH + cellH / 2);
        }
    }
}
