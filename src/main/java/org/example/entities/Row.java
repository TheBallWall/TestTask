package org.example.entities;

import java.math.BigInteger;
import java.util.*;

public class Row {
    private final ArrayList<BigInteger> values;
    private String inputString;
    private ArrayList<Integer> memberOfGroups;

    public void setInputString(String inputString) {
        this.inputString = inputString;
    }

    public String getInputString() {
        return inputString;
    }

    public Row(ArrayList<BigInteger> inputRow) {
        //this.inputString = inputString;
        memberOfGroups = new ArrayList<Integer>();
        values = inputRow;
    }

    public BigInteger getValueAtIndex(int index) {
        if (values.size() <= index) return null;
        return values.get(index);
    }

    public ArrayList<BigInteger> getValues() {
        return values;
    }

//    public ArrayList<Integer> getMembership(){
//        return memberOfGroups;
//    }

//    public void setMembership(ArrayList<Integer> memberOfGroups) {
//        this.memberOfGroups = memberOfGroups;
//    }
//    public void addMembership(Integer groupIndex) {
//        memberOfGroups.add(groupIndex);
//    }

    public void printRow() {
        System.out.println(inputString);
//        System.out.println(row.stream().map(BigInteger::toString).collect(Collectors.joining(";")));
//        for(BigInteger value: row){
//            System.out.print();
//        }
    }

//    public boolean checkStringAtIndex(String s, int index){
//        return row.get(index).equals(s);
//    }

    public boolean compareValueAtIndex(ArrayList<BigInteger> otherValues, int index) {
        return values.get(index) != null
                && otherValues.get(index) != null
                && Objects.equals(values.get(index), otherValues.get(index));
    }

    public boolean haveIntersectingValues(Row row) {
        ArrayList<BigInteger> otherValues = row.getValues();
        if (values.size() > otherValues.size()) {
            for (int i = 0; i < otherValues.size(); i++) {
                if (compareValueAtIndex(otherValues, i))
                    return true;
            }
        } else {
            for (int i = 0; i < values.size(); i++) {
                if (compareValueAtIndex(otherValues, i))
                    return true;
            }
        }
        return false;
    }

    /**
     * Поменять зависимость equals и hashcode с исходной строки на biginteger значения
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
