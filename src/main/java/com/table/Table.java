package com.table;

import java.util.List;

public class Table {

    String tableName;

    MetaData meta;

    List<Row> dataPoints;

    // method to return its save file directory

    // method to add row into the table

    public Table(String name, boolean loadSave){
        this.tableName = name;
        if(loadSave){
            //load
        }
    }
//
//    private static Table createNewTable(String tableName){
//        return Table(tableName,false);
//    }



    public void enterRow(){

    }


    public void findRow(){

    }

    public void loadTableFromFile(){

    }


}
