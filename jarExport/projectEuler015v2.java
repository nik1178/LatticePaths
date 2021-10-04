import java.util.*;
public class projectEuler015v2 {
    public static void main(String[] args) {
        //Starting in the top left corner of a 2×2 grid, and only being able to move to the right and down, there are exactly 6 routes to the bottom right corner.
        //How many such routes are there through a 20×20 grid?
        //Same as v1, but half the execution time, because we just double the amount of paths, once we reach horizontal=0 (draw in paint to understand better) - WIP
        Scanner scan = new Scanner(System.in);
        System.out.println("How many spaces in the grid?"); int gridSize = scan.nextInt();
        scan.close();

        int horizontal = 0;
        int vertical = 0;
        long howManyPaths[] = new long[1];
        howManyPaths[0] = 0;
        algo(gridSize, howManyPaths, vertical, horizontal);
        System.out.println(howManyPaths[0]);
    }
    public static void algo(int gridSize, long[] howManyPaths, int vertical, int horizontal)
    {
        if(horizontal<gridSize)
        {
            algo(gridSize, howManyPaths, vertical, horizontal+1);
        }
        else howManyPaths[0]++;
        if(vertical<gridSize && horizontal<gridSize)
        {
            algo(gridSize, howManyPaths, vertical+1, horizontal);
        }
    }
}
