package org.example;

import org.example.entities.Group;
import org.example.entities.Row;

import java.io.*;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class Solution {

    private ArrayList<Group> finalGroups;
    private ArrayList<Group> groups;
    private ArrayList<Row> singleRowGroups;
    private final Map<Integer, ArrayList<Group>> mapOfGroups;
    private final ArrayList<Row> rows;
    private HashSet<Row> groupedRows;
    private int maxRowSize;
    private final String fileName;

    public Solution(String fileName) {
        this.mapOfGroups = new HashMap<>();
        this.rows = new ArrayList<>();
        this.maxRowSize = Integer.MIN_VALUE;
        this.fileName = fileName;
        this.groupedRows = new HashSet<>();
        this.groups = new ArrayList<>();
        this.finalGroups = new ArrayList<>();
    }

    public void solve() throws IOException {
        // Create list of rows and find maxRowSize
        long start = System.currentTimeMillis();
        createRowsListFromFile();
        System.out.printf("\nTime elapsed for file processing (s): %d\n", (System.currentTimeMillis() - start) / 1000);

        // Group all rows by value in column for each column (index)
        start = System.currentTimeMillis();
        groupRowsByColumn();
        System.out.printf("\nTime elapsed for grouping by column (s): %d\n", (System.currentTimeMillis() - start) / 1000);

        // Create groups for each union of rows
        start = System.currentTimeMillis();
        createGroups();
        System.out.printf("\nTime elapsed for final grouping (s): %d\n", (System.currentTimeMillis() - start) / 1000);


        // Merge all lists of groups into one list
//        finalGroups = mapOfGroups.get(0);
//        for (int i = 1; i < maxRowSize; i++) {
//            mergeTwoListsOfGroups(finalGroups, mapOfGroups.get(i));
//        }

        // Calculate single row groups
//        calculateSingleRowGroups();
    }

    /**
     * Stores the result of merging two lists of groups into the first list.
     *
     * @param firstListOfGroups - first list of groups.
     * @param otherListOfGroups - second list of groups.
     */
    public void mergeTwoListsOfGroups(ArrayList<Group> firstListOfGroups, ArrayList<Group> otherListOfGroups) {
        // List of groups to a map of groups (first)
        Map<Integer, Group> firstGroupsMap = firstListOfGroups.stream()
                .collect(Collectors.toMap(Group::getGroupId, Function.identity()));

        // List of groups to a map of groups (other)
        Map<Integer, Group> otherGroupsMap = otherListOfGroups.stream()
                .collect(Collectors.toMap(Group::getGroupId, Function.identity()));

        // All groups reference to themselves at the beginning
        Map<Integer, Integer> firstGroupsReferences = firstListOfGroups.stream()
                .collect(Collectors.toMap(Group::getGroupId, Group::getGroupId));

        // Find all intersections of otherGroups with firstGroups
        Map<Integer, ArrayList<Integer>> otherGroupsIntersections =
                findGroupIntersections(firstListOfGroups, otherListOfGroups);

        // Store result of merging
        finalGroups = solveIntersections(firstGroupsMap, otherGroupsMap, firstGroupsReferences, otherGroupsIntersections);
    }

    /**
     * Creates a list of groups for each column (index):
     * 1. Groups of single rows are ignored
     * 2. Group of null value is ignored
     * 3. Additionally, sets isGrouped param to true for all rows in filtered groups.
     */
    private void groupRowsByColumn() {
        for (int i = 0; i < maxRowSize; i++) {
            int finalI = i;
            Map<BigDecimal, List<Row>> groupedRows = rows.stream().collect(Collectors.groupingBy(r -> r.getValueAtIndex(finalI)));

            // rows with null value at index or groups of 1 are ignored
            groupedRows.entrySet().removeIf(entry -> entry.getKey().equals(BigDecimal.valueOf(-1)) || entry.getValue().size() < 2);

            int counter = 0;
            int size = groupedRows.size();
            for (List<Row> rows : groupedRows.values()) {
                groups.add(new Group(rows));
                //Row firstRow = rows.getFirst();
//                HashSet<Row> rowHashSet = new HashSet<>(rows);
//                rows.forEach(r -> {
//                    {
//                        r.updateIntersections(rowHashSet);
//                        r.setNeeded();
//                    }
//                });
//
//                if (i == 0) {
//                    rows.getFirst().updateIntersections(rows);
//                } else {
//                    boolean intersected = rows.stream().filter(this.groupedRows::contains).map(row -> row.updateIntersections(rows)).findFirst().isPresent();
//                    if (!intersected) {
//                        rows.getFirst().updateIntersections(rows);
//                    }
//                }
                // this.groupedRows.addAll(rows);
                if (counter % 1000 == 0)
                    System.out.println("Групп обработано: " + counter + " - групп всего: " + size + " - Столбец: " + i);
                counter++;
            }
            //System.out.println();
        }
    }

    private void createGroups() {
        //for (Row row : groupedRows) {
        //int size = groupedRows.size();
        //ArrayList<Row> rowsList = new ArrayList<>(groupedRows);
        // for (int i = 0; i < size; i++) {
        Comparator<Group> comparator = (o1, o2) -> (-1) * Integer.compare(o1.getRows().size(), o2.getRows().size());
        groups.sort(comparator);
        int size = groups.size();
        Integer counter = 0;
        for (int i = 0; i < size; i++) {
            Group finalGroup = groups.get(i);
            Set<Row> finalGroupRows = finalGroup.getRows();
            if (i == size - 1) {
                finalGroups.add(finalGroup);
                break;
            }
            for (int j = i + 1; j < size; j++) {
                Group comparingGroup = groups.get(j);
                Set<Row> comparingGroupRows = groups.get(j).getRows();
                if (finalGroupRows.size() > comparingGroupRows.size()) {
                    for (Row row : comparingGroupRows) {
                        if (finalGroupRows.contains(row)) {
                            finalGroup.addRows(comparingGroupRows);
                            groups.remove(comparingGroup);
                            size--;
                            j--;
                            break;
                        }
                    }
                } else {
                    for (Row row : finalGroupRows) {
                        if (comparingGroupRows.contains(row)) {
                            finalGroup.addRows(comparingGroupRows);
                            groups.remove(comparingGroup);
                            size--;
                            j--;
                            break;
                        }
                    }
                }
            }
            finalGroups.add(finalGroup);
            if (i % 100 == 0)
                System.out.println("Групп обработано: " + i + " - Групп всего: " + size);
            //counter++;
        }
//        for (Row row : rows) {
//            if (row.isNeeded() && !row.isGrouped() && !row.getIntersections().isEmpty()) {
//                row.setGrouped();
//                HashSet<Row> intersectingRows = new HashSet<>() {{
//                    add(row);
//                }};
//                //HashSet<Row> intersectingRows = new HashSet<>();
//                System.out.println(row.getIntersections().size());
//                findAllIntersectingRows(row, intersectingRows, counter);
//                //size = rowsList.size();
//                finalGroups.add(new Group(intersectingRows));
//            }
//            if (counter % 10000 == 0)
//                System.out.println("Строк обработано: " + counter + " - Строк всего: " + size);
//            counter++;
//        }
    }

    private void findAllIntersectingRows(Row row, HashSet<Row> intersections, Integer counter) {
//        counter++;
//        System.out.println(counter);
//        Integer finalCounter = counter;
//        row.getIntersections().forEach(r -> {
//            {
//                if (!intersections.contains(r)) {
//                    intersections.add(r);
//                    r.setGrouped();
//                    findAllIntersectingRows(r, intersections, finalCounter);
//                }
//            }
//        });
        for (Row intersectingFirstRow : row.getIntersections()) {
            Row nextRow = intersectingFirstRow;
            while (nextRow != null) {
                intersections.add(nextRow);
                row.setGrouped();
                nextRow = iterateOverIntersectionsForNextRow(nextRow.getIntersections(), intersections);
            }
        }
//        for(Row intersectingRow: row.getIntersections()){
//            if(!intersections.contains(intersectingRow)){
//                intersections.add(intersectingRow);
//                row.setGrouped();
//                findAllIntersectingRows(intersectingRow,intersections,counter);
//            }
//        }
    }

    private Row iterateOverIntersectionsForNextRow(HashSet<Row> rowIntersections, HashSet<Row> completedIntersections) {
        for (Row currentRow : rowIntersections) {
            if (!completedIntersections.contains(currentRow)) return currentRow;
        }
        return null;
    }

    /**
     * Finds intersections between two lists of groups.
     *
     * @param firstListOfGroups - first list of groups.
     * @param otherListOfGroups - second list of groups.
     * @return Map<Id, List < Id>>, which contains all intersections of groups from second list with groups from first list.
     */
    private Map<Integer, ArrayList<Integer>> findGroupIntersections(ArrayList<Group> firstListOfGroups,
                                                                    ArrayList<Group> otherListOfGroups) {
        Map<Integer, ArrayList<Integer>> otherGroupsIntersections = new HashMap<>();
        for (Group otherGroup : otherListOfGroups) {
            int groupId = otherGroup.getGroupId();
            otherGroupsIntersections.put(groupId, new ArrayList<>());
            for (Row row : otherGroup.getRows()) {
                int intersection = findGroupContainingRow(firstListOfGroups, row);
                if (intersection != -1) {
                    otherGroupsIntersections.get(groupId).add(intersection);
                }
            }
        }
        return otherGroupsIntersections;
    }

    /**
     * Searches a list of rows for occurrence of the passed row.
     *
     * @param groups - list of groups in which to search.
     * @param row    - search row
     * @return group id if found or else -1;
     */
    private int findGroupContainingRow(ArrayList<Group> groups, Row row) {
        for (Group group : groups) {
            if (group.getRows().contains(row)) return group.getGroupId();
        }
        return -1;
    }

    /**
     * Merges groups based on the number of intersections:
     * number == 0: group from otherList is added in the resulting list (no intersections)
     * number == 1: group from otherList is merged with the group from firstList
     * number  > 1: group from otherList is merged with the first intersecting group from firstList,
     * then all groups from firstList mentioned in intersection list are merged with the first one.
     *
     * @param firstGroupsMap           - Map<Id, Group>, represents first list of groups.
     * @param otherGroupsMap           - Map<Id, Group>, represents other list of groups.
     * @param firstGroupsReferences    - Map<Id, Id>, represents which group is part of which group
     * @param otherGroupsIntersections - Map<Id, List<Id>, contains all intersections with groups from first list for all groups of other list.
     * @return ArrayList of merged groups.
     */
    private ArrayList<Group> solveIntersections(Map<Integer, Group> firstGroupsMap,
                                                Map<Integer, Group> otherGroupsMap,
                                                Map<Integer, Integer> firstGroupsReferences,
                                                Map<Integer, ArrayList<Integer>> otherGroupsIntersections) {
        ArrayList<Group> mergedGroups = new ArrayList<>();
        for (Map.Entry<Integer, ArrayList<Integer>> e : otherGroupsIntersections.entrySet()) {
            int groupId = e.getKey();
            Group group = otherGroupsMap.get(groupId);
            ArrayList<Integer> groupIntersections = e.getValue();
            if (groupIntersections.isEmpty()) {
                mergedGroups.add(group);
            } else if (groupIntersections.size() == 1) {
                firstGroupsMap.get(groupIntersections.getFirst()).mergeGroups(group);
            } else {
                firstGroupsMap.get(groupIntersections.get(0)).mergeGroups(group);
                mergeFirstGroups(groupIntersections, firstGroupsMap, firstGroupsReferences);
            }
        }
        // add merged groups from firstList to the resulting list
        mergedGroups.addAll(createListOfMergedGroups(firstGroupsMap, firstGroupsReferences));
        return mergedGroups;
    }

    /**
     * Merges groups in the first list of groups based on the provided intersections:
     * 1. First group in a list of intersections becomes the receiving group.
     * 2. All groups that are left in a list of intersections are merged into the receiving group.
     * 3. Their Ids are made to reference the receiving group in the map of relations.
     *
     * @param intersections - ArrayList<Id>, represents all groups, that should be merged into one.
     * @param groups        - Map<Id, Group>,represents all groups from the first list of groups.
     * @param relations     - Map<Id,Id>, represents which group is part of which group
     */
    private void mergeFirstGroups(ArrayList<Integer> intersections,
                                  Map<Integer, Group> groups,
                                  Map<Integer, Integer> relations) {
        int receivingGroupId = intersections.get(0);
        Group receivingGroup = groups.get(receivingGroupId);
        for (int i = 1; i < intersections.size(); i++) {
            int groupId = intersections.get(i);
            // if groups are not already merged, then merge,
            // make groupId of merged group refer to a receivingGroup,
            // make groupId of merged group refer to a groupId of receivingGroup
            if (groupId != receivingGroupId) {
                Group group = groups.get(groupId);
                receivingGroup.mergeGroups(group);
                groups.put(groupId, receivingGroup);
                relations.put(groupId, receivingGroupId);
            }
        }


    }

    /**
     * Creates an ArrayList of groups, based on the reference map.
     *
     * @param groupsMap        - Map<Id, Group>, represents list of groups.
     * @param groupsReferences - Map<Id, Id>, if a group was merged it will reference group to which it was merged.
     * @return ArrayList of unique groups (such groups should have same id in key and value in the reference map).
     */
    private List<Group> createListOfMergedGroups(Map<Integer, Group> groupsMap, Map<Integer, Integer> groupsReferences) {
        return groupsReferences.entrySet().stream()
                .filter(e -> Objects.equals(e.getValue(), e.getKey()))
                .map(Map.Entry::getKey)
                .map(groupsMap::get)
                .toList();
    }

    /**
     * Creates a list of rows and calculates maximum row size from provided file
     *
     * @throws IOException - reading a file
     */
    private void createRowsListFromFile() throws IOException {
        //InputStream gs = Files.newInputStream(Paths.get(fileName));
        InputStream gs = new FileInputStream("src/main/resources/lng-big.csv");
        //InputStream gs = new FileInputStream("src/main/resources/full.txt");

        BufferedReader reader = new BufferedReader(new InputStreamReader(gs));

        String temp = "";
        while ((temp = reader.readLine()) != null) {
            ArrayList<BigDecimal> rowValues = checkRow(temp);
            if (rowValues != null) {
                Row row = new Row(rowValues);
                row.setInputString(temp);
                rows.add(row);
                maxRowSize = Math.max(maxRowSize, rowValues.size());
            }
        }
    }

    /**
     * Splits an input string into BigDecimal values if the string is correct.
     *
     * @param inputString - line red from file.
     * @return ArrayList of values if the string is correct or else null.
     */
    private ArrayList<BigDecimal> checkRow(String inputString) {
        ArrayList<BigDecimal> row = new ArrayList<>();
        String fixedString = makeFixedString(inputString);

        for (String s : fixedString.split(";")) {
            long numOfQuotes = s.chars().filter(ch -> ch == '\"').count();
            if (!(numOfQuotes == 2 || numOfQuotes == 0) || s.charAt(0) != '\"' || s.charAt(s.length() - 1) != '\"') {
                //for incorrect string
                return null;
            }
            if (!s.equals("\"\"")) {
                s = s.replaceAll("\"", "");
                row.add(new BigDecimal(s));
            } else {
                //empty value in row
                row.add(null);
            }
        }
        return row;
    }

    private String makeFixedString(String originalString) {
        String result = "";
        String[] strArray = originalString.split("");
        boolean endsWithEmpty = false;
        for (int i = 0; i < strArray.length; i++) {
            if (i == 0 && strArray[i].equals(";")) {
                result = result.concat("\"\"");
            }

            result = result.concat(strArray[i]);

            if (strArray[i].equals(";")) {
                if (i + 1 <= strArray.length - 1 && strArray[i + 1].equals(";")) {
                    result = result.concat("\"\"");
                } else if (i == strArray.length - 1 && strArray[i].equals(";")) {
                    result = result.concat("\"\"");
                }
            }
        }

        return result;
    }

    private void calculateSingleRowGroups() {
        singleRowGroups = rows.stream().filter(Row::notGrouped).collect(Collectors.toCollection(ArrayList::new));
    }

    private Map<Integer, List<Group>> sortGroups(ArrayList<Group> groups) {
        return groups.stream().collect(
                Collectors.groupingBy(
                        g -> g.getRows().size(), TreeMap::new, toList())
        ).reversed();
    }

    public void printToFile() throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("output.txt"));
        writer.write("Всего групп (>1 строки): " + finalGroups.size() + "\n");

        Integer groupNumber = 1;
        for (List<Group> groups : sortGroups(finalGroups).values()) {
            for (Group group : groups) {
                writer.write("Группа " + groupNumber + "\n");
                groupNumber++;
                for (Row row : group.getRows()) {
                    writer.write(row.getInputString() + "\n");
                }
            }
        }
        for (Row row : singleRowGroups) {
            writer.write("Группа " + groupNumber + "\n");
            groupNumber++;
            writer.write(row.getInputString() + "\n");
        }

        writer.close();
    }


    public void printGroups() {
        int counter = 0;
        int maxSize = 0;
        Row groupFirstRow = null;
        Map<Integer, List<Group>> sGroups = sortGroups(finalGroups);
        for (Map.Entry<Integer, List<Group>> e : sGroups.entrySet()) {
            for (Group group : e.getValue()) {
                counter++;
                System.out.println("Группа " + counter);
                for (Row row : group.getRows()) {
                    row.printRow();
                }
                // взять случайную стрку (удалить)
                if (group.getRows().size() > maxSize) {
                    maxSize = group.getRows().size();
                    groupFirstRow = group.getRows().iterator().next();
                }
            }
        }
        System.out.println();
        System.out.println("Всего групп: " + finalGroups.size());
        System.out.println("Всего групп (>1 строки): " + counter);
        System.out.println("Максимальный размер группы: " + maxSize);
        System.out.println("Строка из группы с максимальным размером: " + groupFirstRow.getInputString());
    }
}
