package org.example;

import java.io.IOException;


public class Main {

    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();
        Solution s = new Solution();
        s.resolve();
        s.printGroups();
        System.out.printf("\nTime elapsed: %d\n", System.currentTimeMillis() - start);
    }
}