package AI_PerfectSquare;
import java.util.*;

public class AiPerfectSquare {
    public static void main(String[] args) {
        Map<Integer, int[]> guessMap = new HashMap<Integer, int[]>();
        for (int i=0;i<3; i++) {
            for (int j=0; j<3; j++) {
                guessMap.put(i * 3 + j, new int[]{1,9});//starts with guessing between 1 and 9
            }
        }
        for(int iter=0;iter<10000;iter++) {
            System.out.println("Iteration " + (iter + 1));
            runIteration(guessMap);
            int[][] square= generateNewSquare(guessMap);
            PerfectSquare ps = new PerfectSquare(square);
            System.out.println("Cost: " + ps.cost());
            ps.print();
            if(ps.cost()==0){
                System.out.println("Found perfect square!");
                System.out.println(iter+1);
                break;
            }
        }
    }
    public static void runIteration(Map<Integer, int[]> guessMap) {
        int[][] square= generateNewSquare(guessMap);
        PerfectSquare ps = new PerfectSquare(square);
        System.out.println("Cost: " + ps.cost());
        ps.print();
        for (int i=0;i<3; i++) {
            for (int j=0; j<3; j++) {
                int cost = ps.singleCost(i,j);
                if (cost < 0) {
                    guessMap.put(i * 3 + j, new int[]{guessMap.get(i * 3 + j)[0]+1, guessMap.get(i * 3 + j)[1]+1});
                } else if (cost > 0) {
                    guessMap.put(i * 3 + j, new int[]{guessMap.get(i * 3 + j)[0]-1, guessMap.get(i * 3 + j)[1]-1});
                }
            }
        }
        for (int i=0;i<9;i++){
            if(guessMap.get(i)[0]>guessMap.get(i)[1]){
                guessMap.put(i,new int[]{guessMap.get(i)[1],guessMap.get(i)[0]});
                //System.out.println("Swapped range for cell " + i);
            }
            if(guessMap.get(i)[0]<1) guessMap.put(i,new int[]{1,guessMap.get(i)[1]});
            if(guessMap.get(i)[0]>9) guessMap.put(i,new int[]{9,guessMap.get(i)[0]});
            if(guessMap.get(i)[1]<1) guessMap.put(i,new int[]{guessMap.get(i)[1],1});
            if(guessMap.get(i)[1]>9) guessMap.put(i,new int[]{guessMap.get(i)[0],9});
        }
        //System.out.println("New Guess Ranges:");
        /*for (int i=0;i<9;i++){
            System.out.println("Cell " + i + ": " + Arrays.toString(guessMap.get(i)));
        }*/
    }
    public static int[][] generateNewSquare(Map<Integer, int[]> guessMap) {
        int[][] square = new int[3][3];
        List<Integer> used = new ArrayList<Integer>();
        for (int i=0;i<3; i++) {
            for (int j=0; j<3; j++) {
                int guess = (int)((Math.random() * (guessMap.get(i * 3 + j)[1] - guessMap.get(i * 3 + j)[0] + 1)) + guessMap.get(i * 3 + j)[0]);
                square[i][j] = guess;
                int iter=0;
                while (used.contains(guess)) {
                    guess = (int)((Math.random() * (guessMap.get(i * 3 + j)[1] - guessMap.get(i * 3 + j)[0] + 1)) + guessMap.get(i * 3 + j)[0]);
                    square[i][j] = guess;
                    iter++;
                    if(iter>20) {
                        guess=new Random().nextInt(9);
                        square[i][j]=guess+1;
                    }
                }
                used.add(square[i][j]);
                //System.out.println("Cell " + (i * 3 + j) + " guessed: " + square[i][j]);
            }
        }
        return square;
    }
}
