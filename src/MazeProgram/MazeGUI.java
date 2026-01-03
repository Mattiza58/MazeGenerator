package MazeProgram;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * The Graphical Components and Model behind the Maze Application
 */
public class MazeGUI {

    /**
     * Dimensions
     */
    static final int WIDTH = 750;
    static final int HEIGHT = 470;
    static final int MAZE_WIDTH = 400;
    static final int MAZE_HEIGHT = 400;
    static final int CELL_WIDTH = 10;
    static final int ROWS = (MAZE_WIDTH / CELL_WIDTH);
    static final int COLS = (MAZE_HEIGHT / CELL_WIDTH);

    /**
     * List of cells in the maze
     */
    static ArrayList<Cell> cells = new ArrayList<>();

    /**
     * The current color of the maze
     */
    static Color currentColor = Color.CYAN;

    /**
     * Record class representing a cell within the maze. 'i' and 'j' correspond to the cell's column
     * and row position in the grid, 'xPos' and 'yPos' correspond to the cell's position on the
     * window, 'walls' represents whether a wall exists between a cell's top [0], right [1], bottom
     * [2], or left [3] neighbor in that index order (initialized as all 'true' upon creation), and
     * 'neighbors' is a list of a cell's neighbors with their direction as a NeighborPair object.
     */
    public record Cell(int i, int j, int xPos, int yPos, int cellWidth,
                       ArrayList<NeighborPair> neighbors,
                       boolean[] walls) {

        @Override
        public String toString() {
            return "Cell At: " + this.i() + ", " + this.j();
        }

    }

    /**
     * Record class for a cell's neighbor. 'cell' responds to the cell object and 'type' represents
     * the direction of the neighbor. Requires that type is either 'left', 'right', 'top', or
     * 'bottom.'
     */
    public record NeighborPair(Cell cell, String type) {

    }

    /**
     * Record class for a location pair representing a position on the maze grid based on row and
     * column.
     */
    public record LocPair(int row, int col) {

    }


    /**
     * Constructs a new Maze application
     */
    public MazeGUI() {
        JFrame frame = new JFrame("Maze");

        frame.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel mazePanel = new JPanel(new BorderLayout());
        mazePanel.setPreferredSize(new Dimension(100, 100));
        mazePanel.setBackground(Color.BLACK);

        setup(CELL_WIDTH, cells); // set up the grid
        GridCell cellGrid = new GridCell(cells, currentColor); // create the grid of cells
        mazePanel.add(cellGrid, BorderLayout.CENTER);
        frame.add(mazePanel, BorderLayout.CENTER);
        frame.setBackground(Color.BLACK);
        mazePanel.repaint();

        JButton generateButton = new JButton("Generate New Maze");

        MazeGenerator mazeGenerator = new MazeGenerator(1, cellGrid, mazePanel, cells);

        /*
        Button action listener that paints a new grid
         */
        generateButton.addActionListener(e -> {
            cells = new ArrayList<>();
            setup(CELL_WIDTH, cells);
//            mazeGenerator(cells, cellGrid, mazePanel, frame);
            mazeGenerator.updateCells(cells);
            mazeGenerator.generateMaze();
            cellGrid.updateCells(cells);
            mazePanel.repaint();
            frame.pack();
        });



        mazePanel.add(generateButton, BorderLayout.SOUTH);

        /*
        Settings panel providing the user to change colors
         */
        JPanel settingsPanel = new JPanel();
        settingsPanel.setBackground(Color.BLACK);
        settingsPanel.setPreferredSize(new Dimension((WIDTH - MAZE_WIDTH) / 2 - 10, HEIGHT));
        JButton settingsButton = new JButton("Settings");
        settingsPanel.add(settingsButton, BorderLayout.NORTH);
        mazePanel.add(settingsPanel, BorderLayout.EAST);

        SettingsWindow settingsWindow = new SettingsWindow(cellGrid, mazePanel, mazeGenerator);

        /*
        Allows the settingsWindow to be shown
         */
        settingsButton.addActionListener(e -> {
            settingsWindow.setVisible(true);
            settingsWindow.pack();
        });

        frame.setResizable(false);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);




    }

    /**
     * Sets up the grid layout
     */
    private static void setup(int w, ArrayList<Cell> cells) {
        HashMap<LocPair, Cell> locMap = new HashMap<>();

        int xOffset = (WIDTH - MAZE_WIDTH) / 2;
        int yOffset = 10;

        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                Cell newCell = new Cell(i, j, (j * w) + xOffset, (i * w) + yOffset, w,
                        new ArrayList<>(),
                        new boolean[]{true, true, true, true});
                cells.add(newCell);
                locMap.put(new LocPair(i, j), newCell);
            }
        }

        // find cells neighbors
        for (int i = 0; i < cells.size(); i++) {
            Cell currentCell = cells.get(i);

            int bottomCell = currentCell.i() + 1;
            int topCell = currentCell.i() - 1;
            int leftCell = currentCell.j() - 1;
            int rightCell = currentCell.j() + 1;

            // check left
            if (locMap.containsKey(new LocPair(currentCell.i(), leftCell))) {
                currentCell.neighbors()
                        .add(new NeighborPair(
                                locMap.get(new LocPair(currentCell.i(), leftCell)),
                                "left"));
            }
            // check right
            if (locMap.containsKey(new LocPair(currentCell.i(), rightCell))) {
                currentCell.neighbors()
                        .add(new NeighborPair(
                                locMap.get(new LocPair(currentCell.i(), rightCell)),
                                "right"));
            }
            // check top
            if (locMap.containsKey(new LocPair(topCell, currentCell.j()))) {
                currentCell.neighbors()
                        .add(new NeighborPair(
                                locMap.get(new LocPair(topCell, currentCell.j())),
                                "top"));
            }
            // check bottom
            if (locMap.containsKey(new LocPair(bottomCell, currentCell.j()))) {
                currentCell.neighbors()
                        .add(new NeighborPair(
                                locMap.get(new LocPair(bottomCell, currentCell.j())),
                                "bottom"));
            }


        }

    }

    private static void mazeGeneratorAnimate(ArrayList<Cell> cells, GridCell cellGrid, JPanel panel, JButton reset){
        assert cells != null && !cells.isEmpty();
        Deque<Cell> stack = new LinkedList<>();
        HashSet<String> visited = new HashSet<>();
        Random rand = new Random();

        int ranIndex = rand.nextInt(cells.size());

        Cell srcCell = cells.get(ranIndex); // choose the inital cell
        visited.add(srcCell.toString()); // mark it as visited
        stack.push(srcCell); // push the cell onto the stack

        ActionListener gridDelay = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("timer going");
                if (!stack.isEmpty()) {
                    Cell currentCell = stack.pop();
                    // pop the cell off the stack and mark it as current
                    NeighborPair neighbor = selectNeighbor(currentCell, visited);
                    if (neighbor != null) { // if the cell has a neighbor that has not been visited
                        stack.push(currentCell); // push the current cell onto the stack
                /*
                Remove the wall between the current neighbor and the current cell
                 */
                        switch (neighbor.type()) {
                            case "left":
                                currentCell.walls()[3] = false;
                                neighbor.cell().walls()[1] = false;
                                currentCell.neighbors().remove(neighbor);
                                break;
                            case "right":
                                currentCell.walls()[1] = false;
                                neighbor.cell().walls()[3] = false;
                                currentCell.neighbors().remove(neighbor);
                                break;
                            case "top":
                                currentCell.walls()[0] = false;
                                neighbor.cell().walls()[2] = false;
                                currentCell.neighbors().remove(neighbor);
                                break;
                            case "bottom":
                                currentCell.walls()[2] = false;
                                neighbor.cell().walls()[0] = false;
                                currentCell.neighbors().remove(neighbor);
                                break;
                        }

                        visited.add(neighbor.cell().toString()); // mark the chosen cell as visited
                        stack.push(neighbor.cell()); // push the chosen cell onto the stack
                    }
                    cellGrid.updateCells(cells);
                    cellGrid.repaint();
                    panel.repaint();
                }
                else{
                    ((Timer) (e.getSource())).stop();
                    System.out.println("timer stopped");
                }
            }
        };
        Timer timer = new Timer(5, gridDelay);
        timer.setRepeats(true);
        timer.start();
    }

    private static void mazeGenerator2(ArrayList<Cell> cells, GridCell cellGrid, JPanel panel, JFrame frame) {
        assert cells != null && !cells.isEmpty();
        Deque<Cell> stack = new LinkedList<>();
        HashSet<String> visited = new HashSet<>();
        Random rand = new Random();

        int ranIndex = rand.nextInt(cells.size());

        Cell srcCell = cells.get(ranIndex); // choose the inital cell
        visited.add(srcCell.toString()); // mark it as visited
        stack.push(srcCell); // push the cell onto the stack

        while (!stack.isEmpty()) {
            Timer timer = new Timer(10, evt -> {
                Cell currentCell = stack.pop();
                // pop the cell off the stack and mark it as current
                NeighborPair neighbor = selectNeighbor(currentCell, visited);
                if (neighbor != null) { // if the cell has a neighbor that has not been visited
                    stack.push(currentCell); // push the current cell onto the stack
                /*
                Remove the wall between the current neighbor and the current cell
                 */
                    switch (neighbor.type()) {
                        case "left":
                            currentCell.walls()[3] = false;
                            neighbor.cell().walls()[1] = false;
                            currentCell.neighbors().remove(neighbor);
                            break;
                        case "right":
                            currentCell.walls()[1] = false;
                            neighbor.cell().walls()[3] = false;
                            currentCell.neighbors().remove(neighbor);
                            break;
                        case "top":
                            currentCell.walls()[0] = false;
                            neighbor.cell().walls()[2] = false;
                            currentCell.neighbors().remove(neighbor);
                            break;
                        case "bottom":
                            currentCell.walls()[2] = false;
                            neighbor.cell().walls()[0] = false;
                            currentCell.neighbors().remove(neighbor);
                            break;
                    }

                    visited.add(neighbor.cell().toString()); // mark the chosen cell as visited
                    stack.push(neighbor.cell()); // push the chosen cell onto the stack
                    cellGrid.updateCells(cells);
                    cellGrid.repaint();
                    panel.repaint();
                    frame.pack();
                }
            });
            timer.start();

        }

    }

    private static void mazeGen(ArrayList<Cell> cells){

    }


    /**
     * Randomly generates a new Maze using a recursive backtracking depth-first search algorithim
     * (implementing iteratively to avoid stack overflows). By visualizing a maze as a grid, with
     * each cell having four walls, the algorithim starts with a random cell, marks it as visited
     * and selects a random unvisited neighbor, removing the wall between the chosen cell and
     * neighbor. The neighbor is then marked as a visited and will be the next cell to be chosen. A
     * stack is implemented to allow for backtracking in the event that a chosen cell's neighbors
     * have all been visited, in which the algorithim will backtrack to a cell with an unvisited
     * neighbor until all cells have been visited. 'cells' is the list of cell objects in the maze
     * grid. Requires that 'cells' is not null or empty.
     */
    private static void mazeGenerator(ArrayList<Cell> cells) {
        assert cells != null && !cells.isEmpty();
        Deque<Cell> stack = new LinkedList<>();
        HashSet<String> visited = new HashSet<>();
        Random rand = new Random();

        int ranIndex = rand.nextInt(cells.size());

        Cell srcCell = cells.get(ranIndex); // choose the inital cell
        visited.add(srcCell.toString()); // mark it as visited
        stack.push(srcCell); // push the cell onto the stack

        while (!stack.isEmpty()) {
            Cell currentCell = stack.pop();
            // pop the cell off the stack and mark it as current
            NeighborPair neighbor = selectNeighbor(currentCell, visited);
            if (neighbor != null) { // if the cell has a neighbor that has not been visited
                stack.push(currentCell); // push the current cell onto the stack
                /*
                Remove the wall between the current neighbor and the current cell
                 */
                switch (neighbor.type()) {
                    case "left":
                        currentCell.walls()[3] = false;
                        neighbor.cell().walls()[1] = false;
                        currentCell.neighbors().remove(neighbor);
                        break;
                    case "right":
                        currentCell.walls()[1] = false;
                        neighbor.cell().walls()[3] = false;
                        currentCell.neighbors().remove(neighbor);
                        break;
                    case "top":
                        currentCell.walls()[0] = false;
                        neighbor.cell().walls()[2] = false;
                        currentCell.neighbors().remove(neighbor);
                        break;
                    case "bottom":
                        currentCell.walls()[2] = false;
                        neighbor.cell().walls()[0] = false;
                        currentCell.neighbors().remove(neighbor);
                        break;
                }

                visited.add(neighbor.cell().toString()); // mark the chosen cell as visited
                stack.push(neighbor.cell()); // push the chosen cell onto the stack
            }
        }

    }

    /**
     * Selects a random unvisited neighbor from the chosen cell by shuffling the list of the cell's
     * neighbors and adding all unvisited neighbors to a seperate list. A random neighbor from that
     * unvisited list is then chosen and returned. Returns 'null' if all neighbors have been
     * visited. Requires that 'cell' is not null and 'visited' is not null.
     */
    private static NeighborPair selectNeighbor(Cell cell, HashSet<String> visited) {
        assert cell != null && visited != null;
        shuffle(cell.neighbors()); // shuffle the array of neighbors
        Random rand = new Random();
        ArrayList<NeighborPair> unvisited = new ArrayList<>();
        for (NeighborPair neighbor : cell.neighbors()) {
            if (!visited.contains(neighbor.cell().toString())) {
                unvisited.add(neighbor);
            }
        }
        if (unvisited.isEmpty()) {
            return null;
        }

        return unvisited.get(rand.nextInt(unvisited.size()));
    }

    /**
     * Swap method that swaps two elements in a list by their index. Requires that 'arr' is not null
     * and 'x' and 'y' are valid indices in 'arr'.
     */
    private static void swap(ArrayList<NeighborPair> arr, int x, int y) {
        assert arr != null;
        assert (0 <= x && x < arr.size()) && (0 <= y && y < arr.size());
        NeighborPair temp = arr.get(x);
        arr.set(x, arr.get(y));
        arr.set(y, temp);
    }


    /**
     * Fisher-yates shuffle. Requires that 'neighbors' is not null.
     */
    private static void shuffle(ArrayList<NeighborPair> neighbors) {
        assert neighbors != null;
        Random rand = new Random();
        int n = neighbors.size();

        for (int i = n - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            swap(neighbors, i, j);
        }

    }

}
