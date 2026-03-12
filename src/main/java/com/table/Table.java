package com.table;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Table {

    private static final Pattern columnPattern = Pattern.compile("\\(([^)]+)\\)");
    private Matcher columMatcher;

    String tableName;

    MetaData meta;

    List<Row> dataPoints;

    // method to return its save file directory

    // method to add row into the table

    public Table(){

    }

    public void setTableName(String name){
        this.tableName = name;
    }

    public void setMetaData(MetaData meta){
        this.meta = meta;
    }

    public Table(String name, String columns){
        // This is constructor for a new table
        // when loading a table that is saved just construct it with single String
        String[] parsedColumns = this.parseColumns(columns);
        this.tableName = name;
        this.meta = new MetaData(this.tableName, parsedColumns);
        // Load dataPoints with MetaData

    }

    public Table(String tableNameToLoad){
        //Loading from saved metadata
        this.meta = new MetaData(tableNameToLoad);
        this.tableName = this.meta.getTableName();
    }

    public String[] parseColumns(String input){
        // remove parenthesis
        this.columMatcher= columnPattern.matcher(input);
        if(this.columMatcher.matches()){
            String result = this.columMatcher.group(1);
            return Arrays.stream(result.split(",")).map(String::strip).toArray(String[]::new);
        }
        return null;

    }

    public void enterRow(){

    }


    public void findRow(){

    }

    public void loadTableFromFile(){

    }


}
