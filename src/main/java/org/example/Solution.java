package org.example;

import org.example.entities.Group;
import org.example.entities.Row;

import java.math.BigInteger;
import java.util.*;

public class Solution implements Runnable {

    private ArrayList<Group> groups;
    private ArrayList<Row> rowsCollection;

    public Solution(ArrayList<Row> rows) {
        groups = new ArrayList<>();
        rowsCollection = rows;
    }

    public void solve(int maxRowSize) {
        Integer h = 0;

        int rowsSize = rowsCollection.size();
        while (rowsSize > 0) {
            Row row = rowsCollection.get(0);

            logThousand(h);

            Group rowGroup = new Group(row);
            groups.add(rowGroup);

            getIntersection(row, rowGroup);
            rowsSize = rowsCollection.size();
        }
    }

    private void getIntersection(Row row, Group group) {
        rowsCollection.remove(row);
        int rowsSize = rowsCollection.size();

        Set<Row> intersectingRows = new HashSet<>();
        for (int i = 0; i < rowsSize; i++) {
            Row nextRow = rowsCollection.get(i);
            if (row.haveIntersectingValues(nextRow)) {
                intersectingRows.add(nextRow);
                group.addRow(nextRow);
                rowsCollection.remove(i);
                i--;
                rowsSize--;
            }
        }
        if (!intersectingRows.isEmpty()) {
            for (Row intersectingRow : intersectingRows) {
                getIntersection(intersectingRow, group);
            }
        }
    }

    @Override
    public void run() {

//            if (h % 1000 == 0) {
//                System.out.println(h);
//                //System.out.printf("Time elapsed: %d\n", System.currentTimeMillis() - start);
//            }
//            h++;

    }

    public void printGroups() {
        for (Group group : groups) {
            if (group.getRows().size() > 1) {
                System.out.println("Группа " + group.getGroupId());
                for (Row row : group.getRows()) {
                    //System.out.println(row.getInputString());
                    row.printRow();
                }
            }
        }
    }

    private void logThousand(Integer h) {
        if (h % 1000 == 0)
            System.out.println(h + " Строк обработано - " + "Групп: " + groups.size() + " - строк всего осталось: " + rowsCollection.size());
        h++;
    }
}
