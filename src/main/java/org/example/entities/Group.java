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
        rows.add(row);
    }

    public boolean mergeGroups(Group incomingGroup) {
        if (this.equals(incomingGroup)) return false;
        for(Row row: incomingGroup.getRows()) addRow(row);
        return true;
    }
}
