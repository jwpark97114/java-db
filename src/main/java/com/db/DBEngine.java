package com.db;
import com.table.Row;
import com.table.Table;
import org.jspecify.annotations.Nullable;

import java.nio.file.Path;
import java.nio.file.Files;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;
import java.util.stream.IntStream;

import static com.file.FilePaths.*;
public class DBEngine {

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

    private @Nullable Table findTable(String s){
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
        Row[] returnedResult;
        String[] nameAndElse = queryLine.split(" ",2);
        Table targetTable = findTable(nameAndElse[0]);
        String query = nameAndElse[1].strip();
        if(query.startsWith("WHERE")){
            returnedResult = targetTable.queryResult(query.split(" ",2)[1]);
            System.out.println(Arrays.toString(targetTable.getColumnNames()));
        }
        else{
            String[] colsAndQuery = query.split("WHERE");
            returnedResult = targetTable.queryResult(colsAndQuery[1].strip());
            printRowsWithColumnCondition(targetTable, colsAndQuery[0].strip(), returnedResult);
        }
    }

    // Extremely inefficient version where I used nested for loop is removed with stream
    // I got help from Gemini to get ideas how to make it one-line

    private int[] indicesToCheckInRow(String[] targetCols, String[] columns){
        Set<String> targetSet = new HashSet<>(Arrays.asList(targetCols));
        return IntStream.range(0,columns.length).filter( n -> targetSet.contains(columns[n])).toArray();
    }

    private void systemOutChosenRows(int[] indices, Row[] rowsToPrint){
        StringBuilder sb = new StringBuilder();
        for(Row row : rowsToPrint){
            String[] values = row.getValues();
            for(int i : indices){
                sb.append(values[i]);
                sb.append(" ");
            }
            sb.append("\n");
        }
        System.out.println(sb.toString());
    }

    private void printRowsWithColumnCondition(Table t, String col, Row[] rowsRetrieved){
        Pattern colPattern = Pattern.compile("\\(([^)]+)\\)");
        Matcher m = colPattern.matcher(col);
        if(m.matches()){
            String[] targetCols = Arrays.stream(m.group(1).split(",")).map(String::strip).toArray(String[]::new);
            String[] columns = t.getColumnNames();
            int[] indexToCheck = indicesToCheckInRow(targetCols,columns);
            StringBuilder sb = new StringBuilder();
            for(int i : indexToCheck){
                sb.append(columns[i]);
                sb.append(" ");
            }
            System.out.println(sb.toString());
            systemOutChosenRows(indexToCheck,rowsRetrieved);
        }
    }


}
