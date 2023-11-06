package org.example.entities;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Group {
    private static final AtomicInteger count = new AtomicInteger(0);

    private final ArrayList<Row> rows;
    //private final HashMap<Integer, HashSet<BigInteger>> valuesCollection;
    private ValuesHashSet<BigInteger> valuesSet;
    private final int groupId;

    public Group(Row row, int index) {
        rows = new ArrayList<Row>();
        valuesSet = new ValuesHashSet<BigInteger>();
        addRow(row, index);
        groupId = count.incrementAndGet();
    }

    public Group(List<Row> rows, int index) {
        this.rows = new ArrayList<Row>();
        valuesSet = new ValuesHashSet<BigInteger>();
        for (Row row : rows) {
            addRow(row, index);
        }
        groupId = count.incrementAndGet();
    }

    public int getGroupId() {
        return groupId;
    }

    public ArrayList<Row> getRows() {
        return rows;
    }

    public ValuesHashSet<BigInteger> getValuesSet() {
        return valuesSet;
    }

    //    public void addRow(Row row) {
//        group.add(row);
//        row.setMembership(new ArrayList<>() {{
//            add(groupId);
//        }});
//        updateValuesCollection(row);
//    }
    public void addRow(Row row, int index) {
        rows.add(row);
        row.setMembership(new ArrayList<>() {{
            add(groupId);
        }});
        updateValuesSet(row, index);
    }

    //    private void updateValuesCollection(Row row) {
//        ArrayList<BigInteger> rowValues = row.getValues();
//        for (int i = 0; i < rowValues.size(); i++) {
//            if (rowValues.get(i) != null) {
//                if (valuesCollection.containsKey(i)) {
//                    valuesCollection.get(i).add(rowValues.get(i));
//                } else {
//                    HashSet<BigInteger> set = new HashSet<>();
//                    set.add(rowValues.get(i));
//                    valuesCollection.put(i, set);
//                }
//            }
//        }
//    }
    public void createValuesSet(int index){
        valuesSet = new ValuesHashSet<>();
        for(Row row: rows){
            valuesSet.add(row.getValueAtIndex(index));
        }
    }

    private void updateValuesSet(Row row, int index) {
        valuesSet.add(row.getValueAtIndex(index));
    }

    /**
     * Could be optimized by comparing sizes of key sets and choosing the base map for merge.
     * In order to do so, object allocation must be done to the receiving group (or the donor group will not be removed
     * for space optimization)
     */
//    private void updateValuesCollection(HashMap<Integer, HashSet<BigInteger>> valuesCollection) {
//        for (Integer key : valuesCollection.keySet()) {
//            if (!this.valuesCollection.containsKey(key)) {
//                this.valuesCollection.put(key, new HashSet<BigInteger>() {{
//                    valuesCollection.get(key);
//                }});
//            } else {
//                this.valuesCollection.get(key).addAll(valuesCollection.get(key));
//            }
//        }
//    }
//    public void mergeGroups(Group group) {
//        this.group.addAll(group.getRows());
//        //updateValuesCollection(group.getValuesCollection());
//    }
    public boolean mergeGroups(Group group) {
        if(this.equals(group)) return false;
        if(valuesSet.equalsByAnyValue(group.getValuesSet())) {
            this.rows.addAll(group.getRows());
            this.valuesSet.addAll(group.getValuesSet());
            return true;
        }
        return false;
        //updateValuesCollection(group.getValuesCollection());
    }
}
