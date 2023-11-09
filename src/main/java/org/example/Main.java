package org.example;

import java.io.IOException;


public class Main {

    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();

        Solution solution = new Solution(args[0]);
        solution.solve();
        //solution.printGroups();
        solution.printToFile();

        System.out.printf("\nTime elapsed (s): %d\n", (System.currentTimeMillis() - start) / 1000);
    }

}