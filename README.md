# **Maze Generator**
A Java Swing GUI maze generator utilizing a recursive backtracking algorithm

<img width="748" height="437" alt="Screenshot 2026-01-28 at 10 08 04 PM" src="https://github.com/user-attachments/assets/9de2bc38-f467-4503-ae11-0e2c11c0e0d4" />


**NOTE:** No generative AI, or "vibe-coding," was used during this project


## The Algorithm
Information can be found here: https://en.wikipedia.org/wiki/Maze_generation_algorithm

- A big inspiration behind this project is the field of **graph theory** and algorithms involving it. Especially when it comes to **Depth-First Search** and **Breadth-First Search**, I wanted to find 
a simple way to apply these concepts in a fun way.

Mazes can be generated through various algorithms. There's Prim's, Kruskal's, and Wilson's. However, the simplest of which, is the "recursive backtracker," which is what I chose to use. Although, 
called "recursive", I implemented it iteratively to avoid stack overflow.

### Steps

Note: This is for the iterative implementation

* Choose an initial cell, visit it, and push it to the stack
* While the stack is not empty
  * Pop a cell from the stack and mark it as the current cell
  * If the cell has any unvisited neighbors
    - Push the current cell onto the stack
    - Choose a random neighbor
    - Remove the wall between the current cell and neighbor cell
    - Mark the chosen cell as visited and push it to the stack
   

## Demo (With Animation)
![Maze Demo (1)](https://github.com/user-attachments/assets/4ab69429-833b-44d8-afdf-8f702efb0307)



