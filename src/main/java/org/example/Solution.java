package org.example;

import org.example.entities.Group;
import org.example.entities.Row;
import org.w3c.dom.ls.LSOutput;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

public class Solution implements Runnable {

    private ArrayList<Group> groups;
    private Map<Integer, ArrayList<Group>> mapOfGroups;
    private final ArrayList<Row> rowsCollection;

    public Solution(ArrayList<Row> rows) {
//        groups = new ArrayList<>();
        mapOfGroups = new HashMap<>();
        rowsCollection = rows;
    }

    public void solve(int maxRowSize) {
//        int h = 0;
//        logThousand(h);
//        h++;

        for (int i = 0; i < maxRowSize; i++) {
            groupRowsByColumn(i);
        }


        for (Integer key : mapOfGroups.keySet()) {
            //int key = 1;
            if (key != 0) {
                ArrayList<Group> result = new ArrayList<>();
                //init for (1) group
                ArrayList<Group> firstListOfGroups = mapOfGroups.get(0);
                Map<Integer, Integer> firstGroupsRelations = new HashMap<>();
                Map<Integer, Group> firstGroupsMap = new HashMap<>();
                //filling structures for (1) group
                createGroupsValuesSets(firstListOfGroups, key);
                fillDefaultGroupRelations(firstListOfGroups, firstGroupsRelations);
                fillGroupsMap(firstListOfGroups, firstGroupsMap);

                //init for (2) group
                ArrayList<Group> otherListOfGroups = mapOfGroups.get(key);
                Map<Integer, Group> otherGroupsMap = new HashMap<>();
                Map<Integer, ArrayList<Integer>> otherGroupRelations = new HashMap<>();
                //filling structures for (2) group
                fillGroupsMap(otherListOfGroups, otherGroupsMap);

                System.out.println(key + " Группа(столбец) выполняется - размер (0) списка групп: " + firstListOfGroups.size() + " - размер (" + key + ") списка групп: " + otherListOfGroups.size());

                for (Group otherGroup : otherListOfGroups) {
                    int groupId = otherGroup.getGroupId();
                    otherGroupRelations.put(groupId, new ArrayList<>());
                    for (Row row : otherGroup.getRows()) {
                        int intersection = findGroupContainingRow(firstListOfGroups, row);
                        if (intersection != -1) {
                            otherGroupRelations.get(groupId).add(intersection);
                        }
                    }
                }
                for (Map.Entry<Integer, ArrayList<Integer>> e : otherGroupRelations.entrySet()) {
                    int otherGroupId = e.getKey();
                    Group otherGroup = otherGroupsMap.get(otherGroupId);
                    ArrayList<Integer> otherGroupIntersections = e.getValue();
                    if (otherGroupIntersections.isEmpty()) {
                        result.add(otherGroupsMap.get(otherGroupId));
                    } else if (otherGroupIntersections.size() == 1) {
                        firstGroupsMap.get(otherGroupIntersections.getFirst()).mergeGroups(otherGroup);
                    } else {
                        //add other group to the first group of intersecting groups, then merge all first groups in intersections
                        firstGroupsMap.get(otherGroupIntersections.get(0)).mergeGroups(otherGroup);
                        mergeFirstGroups(otherGroupIntersections, firstGroupsMap, firstGroupsRelations);
                    }
                }
                result.addAll(createListOfMergedGroups(firstGroupsMap, firstGroupsRelations));
                mapOfGroups.put(0, result);
            }
        }
        groups = mapOfGroups.get(0);
        printSingleGroups(groups);
    }

    private void groupRowsByColumn(int index) {
        System.out.println(index + " Столбец инициализируется");
        Map<BigInteger, List<Row>> m = rowsCollection.stream().
                collect(Collectors
                        .groupingBy(r ->
                                r.getValueAtIndex(index) != null ?
                                        r.getValueAtIndex(index) :
                                        BigInteger.valueOf(-1)));
        m.entrySet().removeIf(e -> e.getValue().size() < 2 || e.getKey().equals(BigInteger.valueOf(-1)));
        ArrayList<Group> groups = new ArrayList<>();
        m.values().forEach(rows -> groups.add(new Group(rows)));
        mapOfGroups.put(index, groups);
    }

    private void fillDefaultGroupRelations(ArrayList<Group> groups, Map<Integer, Integer> resultMap) {
        for (Group group : groups) {
            resultMap.put(group.getGroupId(), group.getGroupId());
        }
    }

    private void fillGroupsMap(ArrayList<Group> groups, Map<Integer, Group> groupsMap) {
        for (Group group : groups) {
            groupsMap.put(group.getGroupId(), group);
        }
    }

    private int findGroupContainingRow(ArrayList<Group> groups, Row row) {
        for (Group group : groups) {
            if (group.getRows().contains(row)) return group.getGroupId();
        }
        return -1;
    }

    private void createGroupsValuesSets(ArrayList<Group> groups, int index) {
        for (Group group : groups) {
            group.createValuesSet(index);
        }
    }

    private void mergeFirstGroups(ArrayList<Integer> intersections,
                                  Map<Integer, Group> groups,
                                  Map<Integer, Integer> relations) {
        int receivingGroupId = intersections.get(0);
//        int receivingGroupIdRelation = relations.get(receivingGroupId);
        Group receivingGroup = groups.get(receivingGroupId);
        for (int i = 1; i < intersections.size(); i++) {
            int groupId = intersections.get(i);
            //if groups are not already merged then merge and make groupId refer to a receiving group
            if (groupId != receivingGroupId) {
                Group group = groups.get(groupId);
                receivingGroup.mergeGroups(group);
                groups.put(groupId, receivingGroup);
                relations.put(groupId, receivingGroupId);
            }
        }


    }

    private ArrayList<Group> createListOfMergedGroups(Map<Integer, Group> groupsMap, Map<Integer, Integer> groupsRelations) {
        ArrayList<Group> mergedGroups = new ArrayList<>();
        List<Integer> mergedGroupsIds = groupsRelations.entrySet().stream()
                .filter(e -> Objects.equals(e.getValue(), e.getKey()))
                .map(Map.Entry::getKey)
                .toList();

        for (Integer groupId : mergedGroupsIds) {
            mergedGroups.add(groupsMap.get(groupId));
        }

        return mergedGroups;
    }

    @Override
    public void run() {

//            if (h % 1000 == 0) {
//                System.out.println(h);
//                //System.out.printf("Time elapsed: %d\n", System.currentTimeMillis() - start);
//            }
//            h++;

    }
    private void printSingleGroups(ArrayList<Group>resultGroups){
        List<Row> resultRows = resultGroups.stream().map(Group::getRows).flatMap(List::stream).toList();
        rowsCollection.removeAll(resultRows);
        System.out.println("Одиночных групп: " + rowsCollection.size());
    }

    public void printGroups() {
        int counter = 0;
        int maxSize = 0;
        Row groupFirstRow = null;
        //for (Group group : groups) {
        for (Group group : mapOfGroups.get(0)) {
            if (group.getRows().size() > 1) {
                System.out.println("Группа " + group.getGroupId());
                for (Row row : group.getRows()) {
                    //System.out.println(row.getInputString());
                    row.printRow();
                }
                counter++;
                if (group.getRows().size() > maxSize) {
                    maxSize = group.getRows().size();
                    groupFirstRow = group.getRows().getFirst();
                }
            }
        }
        System.out.println("Всего групп: " + mapOfGroups.get(0).size());
        System.out.println("Всего групп (>1 строки): " + counter);
        System.out.println("Максимальный размер группы: " + maxSize);
        System.out.println("Строка из группы с максимальным размером: " + groupFirstRow.getInputString());
    }

//    private void logThousand(int h) {
//        if (h % 1000 == 0)
//            System.out.println(h + " Строк обработано - " + "Групп: " + groups.size() + " - строк всего осталось: " + rowsCollection.size());
//    }
}
