package com.table;

import java.util.HashMap;
import java.util.Map;

public class MetaData {

    // Holds what columns the table has
    // Tells which row is where in saveFile
    String[] columNames;
    Map<Integer, Long> byteOffset = new HashMap<>();

    public MetaData(String[] columns){

    }

    //Convert id or Primary Key to BytesOffset in file



}



// When do we first create metadata file?
// How do we track saved files?
// How do I retrieve only relevant rows?
// Two metadata one for a table, one for the table of tables
