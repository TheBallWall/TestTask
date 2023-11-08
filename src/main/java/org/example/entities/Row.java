package org.example.entities;

import java.math.BigInteger;
import java.util.*;

public class Row {
    private final ArrayList<BigInteger> values;
    private String inputString;

    public void setInputString(String inputString) {
        this.inputString = inputString;
    }

    public String getInputString() {
        return inputString;
    }

    public Row(ArrayList<BigInteger> values) {
        this.values = values;
    }

    public BigInteger getValueAtIndex(int index) {
        if (values.size() <= index) return null;
        return values.get(index);
    }

    public ArrayList<BigInteger> getValues() {
        return values;
    }

    public void printRow() {
        System.out.println(inputString);
    }

    public boolean compareValueAtIndex(ArrayList<BigInteger> otherValues, int index) {
        return values.get(index) != null
                && otherValues.get(index) != null
                && Objects.equals(values.get(index), otherValues.get(index));
    }

    /**
     * Поменять зависимость equals и hashcode с исходной строки на biginteger значения
     * и id (добавить, если будет создаваться Map<Id,Row> для поиска групп размером 1)
     *
     */
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
