package org.example;

import org.example.entities.Group;
import org.example.entities.Row;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class Solution implements Runnable {

    private ArrayList<Group> groups;
    private final Map<Integer, ArrayList<Group>> mapOfGroups;
    private final ArrayList<Row> rowsCollection;

    public Solution(ArrayList<Row> rows) {
        mapOfGroups = new HashMap<>();
        rowsCollection = rows;
    }

    public void solve(int maxRowSize) {
        // getting lists of groups of rows for each index
        // groups are based on the matching values of rows at the same index
        // groups with single row (w/o match) are ignored
        for (int i = 0; i < maxRowSize; i++) {
            groupRowsByColumn(i);
        }

        // each list of groups (starting from second) is being merged with the first one
        // could be multithreaded
        for (Integer key : mapOfGroups.keySet()) {
            if (key != 0) {
                ArrayList<Group> result = new ArrayList<>();
                // initialization of variables for (1) list of groups
                ArrayList<Group> firstListOfGroups = mapOfGroups.get(0);
                Map<Integer, Integer> firstGroupsReferences = new HashMap<>();
                Map<Integer, Group> firstGroupsMap = new HashMap<>();
                // filling of variables for (1) list of groups
                fillDefaultGroupsReferences(firstListOfGroups, firstGroupsReferences);
                fillGroupsMap(firstListOfGroups, firstGroupsMap);

                // initialization of variables for other list of groups
                ArrayList<Group> otherListOfGroups = mapOfGroups.get(key);
                Map<Integer, Group> otherGroupsMap = new HashMap<>();
                Map<Integer, ArrayList<Integer>> otherGroupsIntersections = new HashMap<>();
                // filling of variables for other list of groups
                fillGroupsMap(otherListOfGroups, otherGroupsMap);

                System.out.println(key + " Группа(столбец) выполняется - размер (0) списка групп: " + firstListOfGroups.size() + " - размер (" + key + ") списка групп: " + otherListOfGroups.size());

                // find group intersections between groups of otherList and firstList
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

                // merge groups based on the number of intersections:
                // number == 0: group from otherList is added in the resulting list (no intersections)
                // number == 1: group from otherList is merged with the group from firstList
                // number  > 1: group from otherList is merged with the first intersecting group from firstList,
                // then all groups from firstList mentioned in intersection list are merged with the first one
                for (Map.Entry<Integer, ArrayList<Integer>> e : otherGroupsIntersections.entrySet()) {
                    int groupId = e.getKey();
                    Group group = otherGroupsMap.get(groupId);
                    ArrayList<Integer> groupIntersections = e.getValue();
                    if (groupIntersections.isEmpty()) {
                        result.add(otherGroupsMap.get(groupId));
                    } else if (groupIntersections.size() == 1) {
                        firstGroupsMap.get(groupIntersections.getFirst()).mergeGroups(group);
                    } else {
                        firstGroupsMap.get(groupIntersections.get(0)).mergeGroups(group);
                        mergeFirstGroups(groupIntersections, firstGroupsMap, firstGroupsReferences);
                    }
                }

                // add merged groups from firstList to the resulting list
                result.addAll(createListOfMergedGroups(firstGroupsMap, firstGroupsReferences));
                // replace first list of groups with the result of merging of two lists of groups
                mapOfGroups.put(0, result);
            }
        }
        // resulting list of groups
        groups = mapOfGroups.get(0);
        //printSingleGroups(groups);
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

    private void fillDefaultGroupsReferences(ArrayList<Group> groups, Map<Integer, Integer> resultMap) {
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

//    private void createGroupsValuesSets(ArrayList<Group> groups, int index) {
//        for (Group group : groups) {
//            group.createValuesSet(index);
//        }
//    }

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
     * @param groupsMap        - references groupId to the group.
     * @param groupsReferences - references groupId to the groupId.
     * @return ArrayList of unique groups (such groups should have same id in key and value in the reference map).
     */
    private ArrayList<Group> createListOfMergedGroups(Map<Integer, Group> groupsMap, Map<Integer, Integer> groupsReferences) {
        ArrayList<Group> mergedGroups = new ArrayList<>();
        List<Integer> mergedGroupsIds = groupsReferences.entrySet().stream()
                .filter(e -> Objects.equals(e.getValue(), e.getKey()))
                .map(Map.Entry::getKey)
                .toList();

        for (Integer groupId : mergedGroupsIds) {
            mergedGroups.add(groupsMap.get(groupId));
        }
        //можно сделать всё в одном стриме...
        return mergedGroups;
    }

    @Override
    public void run() {
    }

    private void printSingleGroups(ArrayList<Group> resultGroups) {
        List<Row> resultRows = resultGroups.stream().map(Group::getRows).flatMap(List::stream).toList();
        rowsCollection.removeAll(resultRows);
        System.out.println("Одиночных групп: " + rowsCollection.size());
    }

    private Map<Integer, List<Group>> sortGroups(ArrayList<Group> groups) {
        return groups.stream().collect(
                Collectors.groupingBy(
                        g -> g.getRows().size(), TreeMap::new, toList())
        ).reversed();
    }

    public void printGroups() {
        int counter = 0;
        int maxSize = 0;
        Row groupFirstRow = null;
        Map<Integer, List<Group>> sGroups = sortGroups(groups);
        //for (Group group : mapOfGroups.get(0)) {
        for (Map.Entry<Integer, List<Group>> e : sGroups.entrySet()) {
            for (Group group : e.getValue()) {
                //if (group.getRows().size() > 1) {
                counter++;
                System.out.println("Группа " + counter);
                for (Row row : group.getRows()) {
                    //System.out.println(row.getInputString());
                    row.printRow();
                }
                if (group.getRows().size() > maxSize) {
                    maxSize = group.getRows().size();
                    groupFirstRow = group.getRows().getFirst();
                }
                //}
            }
        }
        System.out.println();
        System.out.println("Всего групп: " + mapOfGroups.get(0).size());
        System.out.println("Всего групп (>1 строки): " + counter);
        System.out.println("Максимальный размер группы: " + maxSize);
        System.out.println("Строка из группы с максимальным размером: " + groupFirstRow.getInputString());
    }
}
