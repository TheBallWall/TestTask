package org.example;

import org.example.entities.Row;

import java.io.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;


public class Main {

    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();

        Solution solution = new Solution(args[0]);
        solution.solve();
        solution.printGroups();
        //solution.printToFile();


        System.out.printf("\nTime elapsed (s): %d\n", (System.currentTimeMillis() - start) / 1000);
    }

}