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

    private Table[] currentTables;

    public DBEngine(){
        this.currentTables = loadDBSaves();
    }


    public void executeTableOperation(String operation){

    }

    public Table[] loadDBSaves(){

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

        return loadedTables.toArray(new Table[0]);
    }

}
