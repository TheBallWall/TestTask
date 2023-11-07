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
//        Map<BigInteger, List<Row>> groupedRows = rowsCollection.stream()
//                .collect(Collectors.
//                        groupingBy(
//                                row -> row.getValueAtIndex(0) == null ? BigInteger.valueOf(-1) : row.getValueAtIndex(0)
//                        ));
//        //индекс не нужен
//        for (Row row : groupedRows.get(BigInteger.valueOf(-1))) groupsS.add(new Group(row, 1));
//        groupedRows.remove(BigInteger.valueOf(-1));

        int h = 0;
//        Iterator<Row> rowIterator = rowsCollection.iterator();
//        while (rowIterator.hasNext()) {
        int rowsSize = rowsCollection.size();
        while (rowsSize > 0) {
//            Row row = rowIterator.next();
            Row row = rowsCollection.get(0);
            if (h % 1000 == 0) {
                System.out.println(h + "Строк обработано - " + "Групп: " + groups.size() + " - строк всего осталось: " + rowsCollection.size());
            }
            h++;
            Group rowGroup = new Group(row);
            groups.add(rowGroup);

            getIntersection(row, rowGroup);
            rowsSize = rowsCollection.size();
        }
    }

    private void getIntersection(Row row, Group group) {
        Set<Row> intersectingRows = new HashSet<>();
        rowsCollection.remove(row);
        for (int i = 0; i < row.getValues().size(); i++) {
            ArrayList<Row> modifiedRowsCollection = new ArrayList<>(rowsCollection);
            BigInteger value = row.getValueAtIndex(i);

            if (value != null) {
                int finalI = i;
                rowsCollection.stream()
                        .filter(r -> Objects.equals(r.getValueAtIndex(finalI), value))
                        .forEach(r -> {
                            intersectingRows.add(r);
                            group.addRow(r);
                            modifiedRowsCollection.remove(r);
                        });
            }
            rowsCollection = modifiedRowsCollection;
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

    private void checkGroupsForDuplicatedRows() {

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

}
