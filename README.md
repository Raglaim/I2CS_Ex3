# Pac-Man Autonomous Client: Ex3 - Object-Oriented Programming

## 1. Background
This project was developed as part of the "Introduction to Computer Science" course at Ariel University (2026). The assignment, "Ex3 - Object-Oriented Programming & Gaming," focuses on designing an intelligent autonomous agent for the Pac-Man game.

**Scope of Work:**
This project focuses entirely on the **Client Side**. The goal was to implement the `Ex3Algo` class—an algorithm that connects to the game server, analyzes the board state, and autonomously controls Pac-Man to maximize points while avoiding ghosts.

## 2. The Pac-Man Algorithm
The autonomous agent is implemented in the `Ex3Algo` class. It uses a decision-making process balancing "Greedy" scoring with survival instincts.

### Core Logic (`move` method)
The algorithm analyzes the board state on every frame using the `MyMap` class to represent the grid as a graph. It operates in two modes:

* **Target Selection (Scoring):** The agent scans the map for "Pink Dots" (score items). It calculates the shortest path to all targets using Breadth-First Search (BFS) via the `allDistance` method. It prioritizes the closest target but applies a penalty if the path passes too close to a ghost.

* **Danger Assessment & Escape:**
  Before moving, the agent calculates a `Danger Level` based on the proximity of ghosts.
    * If a ghost is within 3 steps, the danger level increases significantly.
    * If the danger level exceeds a threshold (25), the agent switches to **Escape Mode**.
    * In Escape Mode (`run` method), the agent simulates moves in all 4 directions (UP, DOWN, LEFT, RIGHT). It checks for dead ends and selects the direction that has the lowest `Danger Level`.

### Navigation
* **BFS Pathfinding:** The agent uses `shortestPath` to find the optimal route to the selected target, avoiding walls and obstacles.
* **Cyclic Awareness:** The algorithm supports cyclic maps (wraparound borders), allowing Pac-Man to cross from one side of the screen to the other to escape or reach targets faster.

## 3. Video Demo
[Insert a link to your 120-second video clip here as required by the assignment]

## 4. How to Run

### Prerequisites
* **Java Runtime Environment (JRE):** You must have Java installed (Java 23 or higher is recommended).
* **Operating System:** Windows, macOS, or Linux.

### Installation
1. Go to the Releases page of this repository.
2. Download the latest `.zip` file (e.g., `Ex3_2.zip`).
3. **Extract the ZIP file** to a folder on your computer.

### ⚠️ Important: Assets
Do not move the `.jar` file out of the folder. The game requires the following files to be in the **same directory** as the jar:

* `test.bit` (Level map)
* `*.png` (Game assets)

### Execution
You can run the game using one of the following methods:

**Method 1: Command Line (Recommended)** Open your terminal (Command Prompt or PowerShell) in the game folder and run:
```bash
java --enable-preview -jar Ex3_2.jar
```

**Method 2: Run Script (Windows)** If a run.bat file is included in the folder, simply double-click it to start the game.

Author: Elad Nagar | ID: 216770164
