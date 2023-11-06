package org.example.entities;

import java.math.BigInteger;
import java.util.*;

public class Row {
    private final ArrayList<BigInteger> row;
    private String inputString;
    private ArrayList<Integer> memberOfGroups;

//    public Row(ArrayList<Integer> row) {
//        this.row = row;
//    }

    public void setInputString(String inputString) {
        this.inputString = inputString;
    }

    public String getInputString() {
        return inputString;
    }

    public Row(ArrayList<BigInteger> inputRow) {
        //this.inputString = inputString;
        memberOfGroups = new ArrayList<Integer>();
        row = inputRow;
    }

    public BigInteger getValueAtIndex(int index){
        if(row.size() <= index) return null;
        return row.get(index);
    }
    public ArrayList<BigInteger> getValues(){
        return row;
    }

    public ArrayList<Integer> getMembership(){
        return memberOfGroups;
    }

    public void setMembership(ArrayList<Integer> memberOfGroups) {
        this.memberOfGroups = memberOfGroups;
    }
    public void addMembership(Integer groupIndex) {
        memberOfGroups.add(groupIndex);
    }

    public void printRow(){
        System.out.println(inputString);
//        System.out.println(row.stream().map(BigInteger::toString).collect(Collectors.joining(";")));
//        for(BigInteger value: row){
//            System.out.print();
//        }
    }

//    public boolean checkStringAtIndex(String s, int index){
//        return row.get(index).equals(s);
//    }

    public boolean compareStringsAtIndex(Row r, int index){
        return row.get(index).equals(r.getValueAtIndex(index));
    }

    @Override
    public boolean equals(Object o){
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
    public int hashCode(){
        return Objects.hash(inputString);
    }
}
