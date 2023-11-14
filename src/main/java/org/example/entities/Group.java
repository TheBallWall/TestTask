package org.example.entities;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Group {
    private static final AtomicInteger count = new AtomicInteger(0);

    private final Set<Row> rows;
    private final int groupId;

    public Group(Collection<Row> rows) {
        this.groupId = count.incrementAndGet();
        this.rows = new HashSet<>(rows);
    }

    public int getGroupId() {
        return groupId;
    }

    public Set<Row> getRows() {
        return rows;
    }

    public void addRow(Row row) {
        this.rows.add(row);
    }
    public void addRows(Collection<Row> rows) {
        this.rows.addAll(rows);
    }

    public boolean mergeGroups(Group incomingGroup) {
        if (this.equals(incomingGroup)) return false;
        //for(Row row: incomingGroup.getRows()) addRow(row);
        this.rows.addAll(incomingGroup.getRows());
        return true;
    }
}
