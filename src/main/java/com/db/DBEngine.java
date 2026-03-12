package com.db;
import com.table.Table;

import java.nio.file.Path;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static com.file.FilePaths.*;
public class DBEngine {

    private Path storagePath = BASE_DIR;
    private List<Table> currentTables;

    public DBEngine(){
        this.currentTables = loadDBSaves();
    }


    public void executeTableOperation(String operation){

    }

    public List<Table> loadDBSaves(){

        List<Table> loadedTables = new ArrayList<>();

        try(Stream<Path> pathsInsideMetaFolder = Files.list(META_DIR);){
            for(Path possibleMetaPath : pathsInsideMetaFolder.toList()){
                String name = possibleMetaPath.getFileName().toString();
                if(name.endsWith(".meta")){
                    String tableName = name.replaceFirst("\\.meta$","");
                    Table newTable = new Table(tableName);
                    loadedTables.add(newTable);
                }
            }
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }

        return loadedTables;
    }


    public void createTable(String inputString){
        String[] splitStrings = inputString.split(" ",2);
        Table newTable = new Table(splitStrings[0],splitStrings[1]);
        this.currentTables.add(newTable);
    }

    private Table findTable(String s){
        return this.currentTables.stream().filter(t -> t.getTableName().equals(s)).findFirst().orElse(null);
    }

    public void insertIntoTable(String newEntryString){
        String[] splitStrings = newEntryString.split(" ",2);
        Table targetTable = findTable(splitStrings[0]);
        if(targetTable == null){
            return;
        }
        targetTable.insert(splitStrings[1]);
    }


    public void selectFromTable(String queryLine){
        List<Row> result
        String[] nameAndElse = queryLine.split(" ",2);
        Table targetTable = findTable(nameAndElse[0]);
        String query = nameAndElse[1].strip();
        if(query.startsWith("WHERE")){

        }
        else{
            String[] colsAndQuery = query.split("WHERE");

        }

    }

}
