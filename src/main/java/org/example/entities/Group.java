package org.example.entities;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicInteger;

public class Group {
    private static final AtomicInteger count = new AtomicInteger(0);

    private final ArrayList<Row> group;
    private final HashMap<Integer, HashSet<BigInteger>> valuesCollection;
    private final int groupId;

    public Group(Row row) {
        group = new ArrayList<Row>();
        valuesCollection = new HashMap<Integer, HashSet<BigInteger>>();
        addRow(row);
        groupId = count.incrementAndGet();
    }

    public int getGroupId() {
        return groupId;
    }

    public ArrayList<Row> getRows() {
        return group;
    }

    public HashMap<Integer, HashSet<BigInteger>> getValuesCollection() {
        return valuesCollection;
    }

    public void addRow(Row row) {
        group.add(row);
        row.setMembership(new ArrayList<>() {{
            add(groupId);
        }});
        updateValuesCollection(row);
    }

    private void updateValuesCollection(Row row) {
        ArrayList<BigInteger> rowValues = row.getValues();
        for (int i = 0; i < rowValues.size(); i++) {
            if (rowValues.get(i) != null) {
                if (valuesCollection.containsKey(i)) {
                    valuesCollection.get(i).add(rowValues.get(i));
                } else {
                    HashSet<BigInteger> set = new HashSet<>();
                    set.add(rowValues.get(i));
                    valuesCollection.put(i, set);
                }
            }
        }
    }

    /**
     * Could be optimized by comparing sizes of key sets and choosing the base map for merge.
     * In order to do so, object allocation must be done to the receiving group (or the donor group will not be removed
     * for space optimization)
     */
    private void updateValuesCollection(HashMap<Integer, HashSet<BigInteger>> valuesCollection) {
        for (Integer key : valuesCollection.keySet()) {
            if (!this.valuesCollection.containsKey(key)) {
                this.valuesCollection.put(key, new HashSet<BigInteger>() {{
                    valuesCollection.get(key);
                }});
            } else {
                this.valuesCollection.get(key).addAll(valuesCollection.get(key));
            }
        }
    }

    public void mergeGroups(Group group) {
        this.group.addAll(group.getRows());
        updateValuesCollection(group.getValuesCollection());
    }
}
