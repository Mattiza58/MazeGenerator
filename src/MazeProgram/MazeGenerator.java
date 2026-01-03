package MazeProgram;


import MazeProgram.MazeGUI.Cell;
import MazeProgram.MazeGUI.NeighborPair;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

public class MazeGenerator implements ActionListener {

    private final Timer timer;
    private Deque<Cell> stack;
    private HashSet<String> visited;
    private final GridCell cellGrid;
    private final JPanel panel;
    private ArrayList<Cell> cells;
    private boolean isAnimated;
    private boolean isRunning;

    public MazeGenerator(int delay, GridCell cellGrid, JPanel panel, ArrayList<Cell> cells) {
        this.isRunning = false;
        this.isAnimated = true;
        this.timer = new Timer(delay, this);
        this.stack = new LinkedList<>();
        this.visited = new HashSet<>();

        Random rand = new Random();

        int ranIndex = rand.nextInt(cells.size());

        Cell srcCell = cells.get(ranIndex); // choose the inital cell
        visited.add(srcCell.toString()); // mark it as visited
        stack.push(srcCell); // push the cell onto the stack

        this.cellGrid = cellGrid;
        this.panel = panel;
        this.cells = cells;

    }

    public void setAnimated(){
        this.isAnimated = !isAnimated;
    }

    public void generateMaze(){
        if (isAnimated){
            startAnimation();
        }
        else{
            mazeGenerator(cells);
        }
    }

    public void updateCells(ArrayList<Cell> newCells){
        this.cells = newCells;
    }

    public void changeDelayTime(int newDelay){
        timer.setDelay(newDelay);
    }


    private void reset(){
        this.stack = new LinkedList<>();
        this.visited = new HashSet<>();

        Random rand = new Random();

        int ranIndex = rand.nextInt(cells.size());

        Cell srcCell = cells.get(ranIndex); // choose the inital cell
        visited.add(srcCell.toString()); // mark it as visited
        stack.push(srcCell); // push the cell onto the stack
    }

    public void startAnimation(){
        reset();
        isRunning = true;
        timer.setRepeats(true);
        timer.start();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!stack.isEmpty() && isRunning) {
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
        } else {
            ((Timer) (e.getSource())).stop();
        }
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
    private void mazeGenerator(ArrayList<Cell> cells) {
        reset();

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
    private NeighborPair selectNeighbor(Cell cell, HashSet<String> visited) {
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
