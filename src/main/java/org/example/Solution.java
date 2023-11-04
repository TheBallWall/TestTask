package org.example;

import org.example.entities.Group;
import org.example.entities.Row;
import org.w3c.dom.ls.LSOutput;

import java.io.*;
import java.math.BigInteger;
import java.net.URL;
import java.util.*;
import java.util.zip.GZIPInputStream;

public class Solution {

    //private ArrayList<ArrayList<ArrayList<String>>> groups = new ArrayList<>();
    private ArrayList<Group> groups = new ArrayList<Group>();

    public void resolve() throws IOException {
        //InputStream fs = new URL("https://github.com/PeacockTeam/new-job/releases/download/v1.0/lng-4.txt.gz").openStream();
        //InputStream gs = new GZIPInputStream(fs);
        InputStream gs = new FileInputStream("src/main/resources/test.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(gs));
        String temp;
        long start = System.currentTimeMillis();
        int h = 0;
        while ((temp = reader.readLine()) != null) {
            if (h % 1000 == 0) {
                System.out.println(h);
                //System.out.printf("Time elapsed: %d\n", System.currentTimeMillis() - start);
            }
            h++;
            ArrayList<BigInteger> inputRow = checkRow(temp);
            if (inputRow != null) {
                Row row = new Row(inputRow);
                if (groups.isEmpty()) {
                    groups.add(new Group(row));
                } else {
                    int rowMemberships = findGroupForRow(row);
                    if (rowMemberships == 0) groups.add(new Group(row));
                    else if (rowMemberships == 1) {
                        groups.get(row.getMembership().get(0)).addRow(row);
                    } else if (rowMemberships > 1) {
                        Group firstGroup = groups.get(row.getMembership().get(0));
                        for (Integer groupId : row.getMembership()) {
                            if (groupId != firstGroup.getGroupId()) {
                                Group currentGroup = groups.get(groupId);
                                firstGroup.mergeGroups(currentGroup);
                                groups.remove(currentGroup);

                            }
                        }
                    }
                }
            }
        }

    }

    public ArrayList<BigInteger> checkRow(String inputString) {
        ArrayList<BigInteger> row = new ArrayList<>();
        for (String s : inputString.split(";")) {
            if (s.chars().filter(ch -> ch == '\"').count() != 2 ||
                    s.charAt(0) != '\"' ||
                    s.charAt(s.length() - 1) != '\"'
            ) {
                return null;
            }
            if (!s.equals("\"\"")) {
                s = s.replaceAll("\"", "");
                row.add(new BigInteger(s));
            }
            else row.add(null);
        }
        return row;
    }

    private int findGroupForRow(Row row) {
        for (Group group : groups) {
            if (checkGroupForRowInsertion(group, row) < 0) return -1;
        }
        return row.getMembership().size();
    }

    private int checkGroupForRowInsertion(Group group, Row row) {
//        if (group.getRows().contains(row)){
//            return -1;};

        for (int i = 0; i < row.getValues().size(); i++) {
            if (group.getValuesCollection().get(i) == null) return 0;
            if (group.getValuesCollection().get(i).contains(row.getStringAtIndex(i))) {
                row.addMembership(group.getGroupId());
                return 1;
            }
        }
        return 0;
    }

    private void checkGroupsForDuplicatedRows() {

    }

    public void printGroups() {
        for (Group group : groups) {
            System.out.println("Группа " + group.getGroupId());
            for (Row row : group.getRows()) {
                System.out.println(row.getInputString());
            }
        }
    }

}