package Troubling_Salesman ; 
public class GridEnvironment {
    int[][] grid;
    int[] state;  // current agent state [row, col]
    private int[] endState;  // goal state
    private int[][] actions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};  // right, down, left, up

    public GridEnvironment(int[][] grid) {
        this.grid = grid;
        this.state = new int[] {0, 0};  // start at top-left corner
        this.endState = new int[] {grid.length - 1, grid[0].length - 1};  // bottom-right corner
    }

    public boolean isTerminalState(int[] state) {
        return state[0] == endState[0] && state[1] == endState[1];
    }

    public int[] getNextState(int[] state, int[] action) {
        int newRow = state[0] + action[0];
        int newCol = state[1] + action[1];
        if (newRow >= 0 && newRow < grid.length && newCol >= 0 && newCol < grid[0].length && grid[newRow][newCol] == 1) {
            return new int[] {newRow, newCol};
        }
        return state;
    }

    public int getReward(int[] state) {
        return isTerminalState(state) ? 1 : 0;
    }

    public int[][] getActions() {
        return actions;
    }
    
    public void displayGrid(int[] agentState) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (i == agentState[0] && j == agentState[1]) {
                    System.out.print("A ");  // Mark the agent's current position
                } else if (grid[i][j] == 0) {
                    System.out.print("# ");  // Obstacle
                } else {
                    System.out.print(". ");  // Free space
                }
            }
            System.out.println();
        }
        System.out.println();
    }
}

