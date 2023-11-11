package org.example;

import org.example.entities.Row;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;


public class Main {

    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();

        Solution solution = new Solution(args[0]);
        solution.solve();
        solution.printGroups();
        //solution.printToFile();

//        InputStream gs = new FileInputStream("src/main/resources/more.txt");
//
//        BufferedReader reader = new BufferedReader(new InputStreamReader(gs));
//
//        ArrayList<String> s = new ArrayList<>();
//        String temp = "";
//        while ((temp = reader.readLine()) != null) {
//            s.add(temp);
//        }
//        Map<String, List<String>> f = s.stream().collect(Collectors.groupingBy(String::toString));
//        List<List<String>> a = f.values().stream().filter(i -> i.size() > 1).toList();

        System.out.printf("\nTime elapsed (s): %d\n", (System.currentTimeMillis() - start) / 1000);
    }

}