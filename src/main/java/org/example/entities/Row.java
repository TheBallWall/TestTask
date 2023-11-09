package org.example.entities;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Objects;

public class Row {
    private final ArrayList<BigInteger> values;
    private String inputString;

    private boolean isGrouped;

    public Row(ArrayList<BigInteger> values) {
        this.values = values;
        this.isGrouped = false;
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

    public BigInteger getValueAtIndex(int index) {
        if (values.size() <= index) return BigInteger.valueOf(-1);
        return values.get(index) != null ? values.get(index):BigInteger.valueOf(-1);
    }

    public void printRow() {
        System.out.println(inputString);
    }

    public boolean compareValueAtIndex(ArrayList<BigInteger> otherValues, int index) {
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
