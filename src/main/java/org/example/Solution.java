package org.example;

import org.example.entities.Group;
import org.example.entities.Row;
import org.example.entities.ValuesHashSet;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

public class Solution implements Runnable {

    //private ArrayList<ArrayList<ArrayList<String>>> groups = new ArrayList<>();
    //private ArrayList<Group> groups = new ArrayList<Group>();
    private final HashMap<Integer, Group> groups;
    private ArrayList<Group> groupsS;
    private final ArrayList<Row> rowsCollection;

    public Solution(ArrayList<Row> rows) {
        groups = new HashMap<>();
        groupsS = new ArrayList<>();
        rowsCollection = rows;
    }


    public void solve(int maxRowSize) {
        Map<BigInteger, List<Row>> groupedRows = rowsCollection.stream()
                .collect(Collectors.
                        groupingBy(
                                row -> row.getValueAtIndex(0) == null ? BigInteger.valueOf(-1) : row.getValueAtIndex(0)
                        ));
        //индекс не нужен
        for (Row row : groupedRows.get(BigInteger.valueOf(-1))) groupsS.add(new Group(row, 1));
        groupedRows.remove(BigInteger.valueOf(-1));
        for (BigInteger key : groupedRows.keySet()) {
            //индекс не нужен
            groupsS.add(new Group(groupedRows.get(key), 1));
        }
        for (int i = 1; i < maxRowSize; i++) {
            System.out.println(i + "Колонка в работе");
            for (Group group : groupsS) {
                group.createValuesSet(i);
            }
            Map<ValuesHashSet<BigInteger>, List<Group>> groupedGroups = groupsS.stream()
                    .collect(Collectors.
                            groupingBy(
                                    Group::getValuesSet
                            ));
            groupsS = new ArrayList<>();
            for (ValuesHashSet<BigInteger> key : groupedGroups.keySet()) {
                Group firstGroup = groupedGroups.get(key).getFirst();
                for (Group group : groupedGroups.get(key)) {
                    if (group.getGroupId() != firstGroup.getGroupId()) {
                        firstGroup.mergeGroups(group);
                    }
                }
                groupsS.add(firstGroup);
            }
        }
    }

    @Override
    public void run() {
//        //long start = System.currentTimeMillis();
//        int h = 0;
//
//        String temp;
//        //while ((temp = reader.readLine()) != null) {
//        for(Row row: rowsCollection){
//
//            if (h % 1000 == 0) {
//                System.out.println(h);
//                //System.out.printf("Time elapsed: %d\n", System.currentTimeMillis() - start);
//            }
//            h++;
//
//            //ArrayList<BigInteger> inputRow = checkRow(temp);
//            //if (inputRow != null) {
//            //    Row row = new Row(inputRow);
//            //    row.setInputString(temp);
//                if (groups.isEmpty()) {
//                    Group newGroup = new Group(row);
//                    groups.put(newGroup.getGroupId(), newGroup);
//                } else {
//                    int rowMemberships = findGroupForRow(row);
//                    if (rowMemberships == 0) {
//                        Group newGroup = new Group(row);
//                        groups.put(newGroup.getGroupId(), newGroup);
//                    } else if (rowMemberships == 1) {
//                        groups.get(row.getMembership().get(0)).addRow(row);
//                    } else if (rowMemberships > 1) {
//                        Group firstGroup = groups.get(row.getMembership().get(0));
//                        for (Integer groupId : row.getMembership()) {
//                            if (groupId != firstGroup.getGroupId()) {
//                                Group currentGroup = groups.get(groupId);
//                                firstGroup.mergeGroups(currentGroup);
//                                groups.remove(currentGroup.getGroupId());
//                            }
//                        }
//                    }
//                }
//            }
//        //}
    }

    public ArrayList<BigInteger> checkRow(String inputString) {
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

//    private int findGroupForRow(Row row) {
//        for (Group group : groups.values()) {
//            if (checkGroupForRowInsertion(group, row) < 0) return -1;
//        }
//        return row.getMembership().size();
//    }

//    private int checkGroupForRowInsertion(Group group, Row row) {
////        if (group.getRows().contains(row)){
////            return -1;};
//
//        for (int i = 0; i < row.getValues().size(); i++) {
//            if (group.getValuesSet().get(i) == null) return 0;
//            if (group.getValuesSet().get(i).contains(row.getValueAtIndex(i))) {
//                row.addMembership(group.getGroupId());
//                return 1;
//            }
//        }
//        return 0;
//    }

    private void checkGroupsForDuplicatedRows() {

    }

//    public void printGroups() {
//        for (Group group : groups.values()) {
//            if (group.getRows().size() > 1) {
//                System.out.println("Группа " + group.getGroupId());
//                for (Row row : group.getRows()) {
//                    //System.out.println(row.getInputString());
//                    row.printRow();
//                }
//            }
//        }
//    }
    public void printGroups() {
        for (Group group : groupsS) {
            if (group.getRows().size() > 1) {
                System.out.println("Группа " + group.getGroupId());
                for (Row row : group.getRows()) {
                    //System.out.println(row.getInputString());
                    row.printRow();
                }
            }
        }
    }

}
