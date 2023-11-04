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

    public String getInputString() {
        return inputString;
    }

    public Row(ArrayList<BigInteger> inputRow) {
        //this.inputString = inputString;
        memberOfGroups = new ArrayList<Integer>();
        row = inputRow;
    }

    public BigInteger getStringAtIndex(int index){
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



//    public boolean checkStringAtIndex(String s, int index){
//        return row.get(index).equals(s);
//    }

    public boolean compareStringsAtIndex(Row r, int index){
        return row.get(index).equals(r.getStringAtIndex(index));
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
