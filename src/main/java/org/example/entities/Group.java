package org.example.entities;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Group {
    private static final AtomicInteger count = new AtomicInteger(0);

    private final ArrayList<Row> rows;
    //private final HashMap<Integer, HashSet<BigInteger>> valuesCollection;
    //private ValuesHashSet<BigInteger> valuesSet;
    private final int groupId;


    public Group(Row row){
        groupId = count.incrementAndGet();
        rows = new ArrayList<>();
        rows.add(row);
    }
//    public Group(Row row, int index) {
//        rows = new ArrayList<Row>();
//        valuesSet = new ValuesHashSet<BigInteger>();
//        addRow(row, index);
//        groupId = count.incrementAndGet();
//    }
//
//    public Group(List<Row> rows, int index) {
//        this.rows = new ArrayList<Row>();
//        valuesSet = new ValuesHashSet<BigInteger>();
//        for (Row row : rows) {
//            addRow(row, index);
//        }
//        groupId = count.incrementAndGet();
//    }

    public int getGroupId() {
        return groupId;
    }

    public ArrayList<Row> getRows() {
        return rows;
    }

//    public ValuesHashSet<BigInteger> getValuesSet() {
//        return valuesSet;
//    }
//
    public void addRow(Row row) {
        if(!rows.contains(row)) rows.add(row);
    }
//
//    public void createValuesSet(int index) {
//        valuesSet = new ValuesHashSet<>();
//        for (Row row : rows) {
//            valuesSet.add(row.getValueAtIndex(index));
//        }
//    }
//
//    private void updateValuesSet(Row row, int index) {
//        valuesSet.add(row.getValueAtIndex(index));
//    }
//
//    public boolean mergeGroups(Group group) {
//        if (this.equals(group)) return false;
//        if (valuesSet.equalsByAnyValue(group.getValuesSet())) {
//            this.rows.addAll(group.getRows());
//            this.valuesSet.addAll(group.getValuesSet());
//            return true;
//        }
//        return false;
//        //updateValuesCollection(group.getValuesCollection());
//    }
}
