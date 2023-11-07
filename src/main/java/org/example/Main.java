package org.example;

import org.example.entities.Row;

import java.io.*;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;


public class Main {

    public static void main(String[] args) throws IOException {
        long beforeUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        long start = System.currentTimeMillis();

//        InputStream fs = new URL("https://github.com/PeacockTeam/new-job/releases/download/v1.0/lng-4.txt.gz").openStream();
//        InputStream gs = new GZIPInputStream(fs);
//        InputStream gs = new FileInputStream("src/main/resources/test.txt");
//        InputStream gs = new FileInputStream("src/main/resources/test10.txt");
//        InputStream gs = new FileInputStream("src/main/resources/test20k.txt");
        InputStream gs = new FileInputStream("src/main/resources/test100k.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(gs));

        String temp;
        ArrayList<Row> rows = new ArrayList<>();
        int maxRowSize = 0;
        while ((temp = reader.readLine()) != null) {
            ArrayList<BigInteger> inputRow = checkRow(temp);
            if (inputRow != null) {
                Row row = new Row(inputRow);
                row.setInputString(temp);
                rows.add(row);
                if (inputRow.size() > maxRowSize) maxRowSize = inputRow.size();
            }
        }

        Solution solution = new Solution(rows);
        solution.solve(maxRowSize);
        solution.printGroups();

        //start(rows);

        //Solution s = new Solution();
        //s.resolve();
        //s.printGroups();

        long afterUsedMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        System.out.printf("\nTime elapsed (s): %d\n", (System.currentTimeMillis() - start) / 1000);
        System.out.printf("Memory used (Mb): %d", (afterUsedMem - beforeUsedMem) / 1024 / 1024);
    }

//    public static void start(ArrayList<Row> rows) throws InterruptedException {
//        ArrayList<Thread> threads = new ArrayList<>();
//        int threadsCount = 100;
//        int threadPoolSize = rows.size() / threadsCount;
//        for (int i = 0; i < threadsCount; i++) {
//            Solution solution;
//            if (i == threadPoolSize - 1) {
//                solution = new Solution(new ArrayList<Row>(rows.subList(threadPoolSize * i, rows.size())));
//            } else {
//                solution = new Solution(new ArrayList<Row>(rows.subList(threadPoolSize * i, threadPoolSize * (i + 1))));
//            }
//            threads.add(new Thread(solution));
//        }
//        for (Thread t : threads) {
//            t.start();
//        }
//        for (Thread t : threads) {
//            t.join();
//        }
//    }

    public static ArrayList<BigInteger> checkRow(String inputString) {
        ArrayList<BigInteger> row = new ArrayList<>();
        for (String s : inputString.split(";")) {
            if (s.chars().filter(ch -> ch == '\"').count() != 2 ||
                    s.charAt(0) != '\"' ||
                    s.charAt(s.length() - 1) != '\"'
            ) {
                return null;
            }
            if (!s.equals("\"\"")) {
                s = s.replaceAll("\"", "");
                row.add(new BigInteger(s));
            } else row.add(null);
        }
        return row;
    }
}