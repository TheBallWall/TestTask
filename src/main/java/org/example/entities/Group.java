package org.example.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Group {
    private static final AtomicInteger count = new AtomicInteger(0);

    private final ArrayList<Row> rows;
    private final int groupId;

    public Group(List<Row> rows) {
        groupId = count.incrementAndGet();
        this.rows = new ArrayList<>(rows);
    }

    public int getGroupId() {
        return groupId;
    }

    public ArrayList<Row> getRows() {
        return rows;
    }

    public void addRow(Row row) {
        if (!rows.contains(row)) rows.add(row);
    }

    public boolean mergeGroups(Group group) {
        if (this.equals(group)) return false;
        for(Row row: group.getRows()) addRow(row);
        return true;
    }
}
