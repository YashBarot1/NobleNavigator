package Troubling_Salesman ; 
import java.util.Random;
import java.util.Arrays ;
import java.util.List; 
import java.util.ArrayList; 

public class QLearning {
    private GridEnvironment env;
    private double[][][] qTable;
    private Random random = new Random();
    private double learningRate = 0.1;
    private double discountFactor = 0.95;
    private double epsilon = 0.1;

    public QLearning(GridEnvironment env) {
        this.env = env;
        int rows = env.grid.length;
        int cols = env.grid[0].length;
        qTable = new double[rows][cols][4];  // 4 possible actions
    }

    public void train(int episodes) {
        for (int episode = 0; episode < episodes; episode++) {
            int[] state = new int[] {0, 0};  // start state

            while (!env.isTerminalState(state)) {
                int[] action = chooseAction(state);
                int[] nextState = env.getNextState(state, action);
                int reward = env.getReward(nextState);

                int actionIndex = findActionIndex(action);
                double oldQValue = qTable[state[0]][state[1]][actionIndex];
                double bestFutureQ = maxQValue(nextState);
                
                // Q-learning formula
                double newQValue = oldQValue + learningRate * (reward + discountFactor * bestFutureQ - oldQValue);
                qTable[state[0]][state[1]][actionIndex] = newQValue;

                state = nextState;
            }
        }
    }

    private int[] chooseAction(int[] state) {
        if (random.nextDouble() < epsilon) {
            return env.getActions()[random.nextInt(env.getActions().length)];  // Explore
        } else {
            return env.getActions()[argMax(qTable[state[0]][state[1]])];  // Exploit
        }
    }

    private int findActionIndex(int[] action) {
        for (int i = 0; i < env.getActions().length; i++) {
            if (action[0] == env.getActions()[i][0] && action[1] == env.getActions()[i][1]) {
                return i;
            }
        }
        return -1;  // Should not happen
    }

    private double maxQValue(int[] state) {
        double[] values = qTable[state[0]][state[1]];
        return Arrays.stream(values).max().getAsDouble();
    }

    private int argMax(double[] array) {
        int bestIndex = 0;
        for (int i = 1; i < array.length; i++) {
            if (array[i] > array[bestIndex]) {
                bestIndex = i;
            }
        }
        return bestIndex;
    }
    
    public List<int[]> extractOptimalPath() {
        List<int[]> path = new ArrayList<>();
        int[] state = {0, 0};  // Starting at the top-left corner, for example
        while (!env.isTerminalState(state)) {
            int actionIndex = argMax(qTable[state[0]][state[1]]);
            state = env.getNextState(state, env.getActions()[actionIndex]);
            path.add(state.clone());
            // Break if stuck in a loop: simple check for larger grids or complex policies
            if (path.size() > env.grid.length * env.grid[0].length) break;
        }
        return path;
    }
    
    public void displayQTable() {
        for (int i = 0; i < qTable.length; i++) {
            for (int j = 0; j < qTable[i].length; j++) {
                System.out.print("State (" + i + "," + j + "): ");
                System.out.print("[");
                for (int k = 0; k < qTable[i][j].length; k++) {
                    System.out.print(String.format("%.2f", qTable[i][j][k]) + (k < qTable[i][j].length - 1 ? ", " : ""));
                }
                System.out.println("]");
            }
        }
    }



    // Main method or similar entry point
    public static void main(String[] args) {
        GridEnvironment env = new GridEnvironment(new int[][] {
            {1, 1, 0, 1, 1},
            {0, 1, 1, 0, 1},
            {1, 1, 0, 0, 0},
            {1, 1, 1, 1, 1}
        });
        
        QLearning ql = new QLearning(env);
        ql.train(1000); // Number of episodes
        List<int[]> optimalPath = ql.extractOptimalPath();
       
        ql.displayQTable(); 
        
        for (int[] state : optimalPath) {
        	env.displayGrid(state);
            System.out.println("Step to: (" + state[0] + ", " + state[1] + ")");
        }
    }
}

