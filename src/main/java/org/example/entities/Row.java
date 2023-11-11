package org.example.entities;

import java.math.BigDecimal;
import java.util.*;

public class Row {
    private final ArrayList<BigDecimal> values;
    private String inputString;
    private boolean isGrouped;
    private boolean isNeeded;

    private HashSet<Row> intersections;

    public Row(ArrayList<BigDecimal> values) {
        this.values = values;
        this.isGrouped = false;
        this.isNeeded = false;
        this.intersections = new HashSet<>();
    }

    public void setInputString(String inputString) {
        this.inputString = inputString;
    }

    public String getInputString() {
        return inputString;
    }

    public boolean isGrouped() {
        return isGrouped;
    }

    public boolean notGrouped() {
        return !isGrouped;
    }

    public void setGrouped() {
        this.isGrouped = true;
    }

    public boolean isNeeded() {
        return isNeeded;
    }

    public void setNeeded() {
        this.isNeeded = true;
    }

    public HashSet<Row> getIntersections() {
        return intersections;
    }

    public void setIntersections(HashSet<Row> intersections) {
        this.intersections = intersections;
    }

    public void updateIntersections(HashSet<Row> intersections) {
        if (this.intersections.equals(intersections)) {
            return;
        }
        if (this.intersections.isEmpty()) {
            this.intersections = intersections;
            return;
        }

        this.intersections.addAll(intersections);
        // not redundant!!!
        intersections = this.intersections;
    }

    public BigDecimal getValueAtIndex(int index) {
        if (values.size() <= index) return BigDecimal.valueOf(-1);
        return values.get(index) != null ? values.get(index) : BigDecimal.valueOf(-1);
    }

    public void printRow() {
        System.out.println(inputString);
    }

    public boolean compareValueAtIndex(ArrayList<BigDecimal> otherValues, int index) {
        return values.get(index) != null
                && otherValues.get(index) != null
                && Objects.equals(values.get(index), otherValues.get(index));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null)
            return false;
        if (getClass() != o.getClass())
            return false;
        Row other = (Row) o;
        return Objects.equals(inputString, other.inputString);
    }

    @Override
    public int hashCode() {
        return Objects.hash(inputString);
    }
}
