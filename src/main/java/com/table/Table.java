package com.table;

import com.db.MetaData;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.RandomAccessFile;
import java.nio.file.Path;

import static com.file.FilePaths.TABLE_DIR;

public class Table {

    private static final Pattern columnPattern = Pattern.compile("\\(([^)]+)\\)");
    private Matcher columMatcher;
    private Path savePath;
    private String tableName;
    private MetaData meta;

    public Table(String name, String columns){
        String[] parsedColumns = this.parseColumns(columns);
        this.tableName = name;
        this.savePath = TABLE_DIR.resolve(this.tableName + ".table");
        try{
            Files.createFile(this.savePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.meta = new MetaData(this.tableName, parsedColumns);

    }

    public Table(String tableNameToLoad){
        this.meta = new MetaData(tableNameToLoad);
        this.tableName = this.meta.getTableName();
        this.savePath = TABLE_DIR.resolve(this.tableName + ".table");
    }

    public String[] getColumnNames(){
        return this.meta.getColumnNames().clone();
    }

    private String[] parseColumns(String input){
        this.columMatcher= columnPattern.matcher(input);
        if(this.columMatcher.matches()){
            String result = this.columMatcher.group(1);
            return Arrays.stream(result.split(",")).map(String::strip).toArray(String[]::new);
        }
        return null;
    }

    private void addRowInBothTableAndMeta(Row row){
        int idx = this.meta.addRowToMetaData(row);
        this.saveRowToFile(row, this.meta.getRowOffsetFromFile(idx));
    }

    private int indexFinder(String[] target, String key){
        for(int i=0; i < target.length; i++){
            if(target[i].equals(key)){
                return i;
            }
        }
        return -1;
    }

    public void insert(String input){
        String[] values = input.split(" ");
        if(values.length != this.meta.getColumnNames().length){
            System.out.println("The given number of the input values does not match the table's Column number");
            return;
        }
        Row newRow = new Row(values);
        this.addRowInBothTableAndMeta(newRow);
    }

    public Row[] retrieveAllRow(){
        List<Row> rows = new ArrayList<>();
        Integer[] pkToFollow =this.meta.getKeysInOffsetMap();
        try(RandomAccessFile raf = new RandomAccessFile(this.savePath.toFile(),"r")){
            for(Integer primaryKey : pkToFollow){
                Long rowStartsAt = this.meta.getRowOffsetFromFile(primaryKey);
                raf.seek(rowStartsAt);
                Row currentRow = this.loadRowFromFile(raf);
                rows.add(currentRow);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return rows.toArray(new Row[0]);
    }


    public Row[] queryResult(String condition){
        if(!condition.contains("=")){
            System.out.println("Unknown WHERE Condition");
            return null;
        }
        String column = condition.split("=")[0].strip();
        String value = condition.split("=")[1].strip();
        int columnIndex = indexFinder(this.meta.getColumnNames(),column);
        if(columnIndex == -1){
            throw new RuntimeException(new ArrayIndexOutOfBoundsException());
        }
        Row[] allRows = retrieveAllRow();

        return  Arrays.stream(allRows).filter(r -> r.getValues()[columnIndex].equals(value)).toArray(Row[]::new);
    }

    private Row loadRowFromFile(RandomAccessFile raf){
        try{
            int rowSize = raf.readInt();
            byte[] rowInBytes = new byte[rowSize];
            raf.readFully(rowInBytes);
            return Row.BytesToRow(rowInBytes);
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private void saveRowToFile(Row rowToWrite, Long location){
        try(RandomAccessFile raf = new RandomAccessFile(this.savePath.toFile(), "rw")){
            raf.seek(location);
            byte[] rowsInByte = Row.rowToBytes(rowToWrite);
            raf.write(rowsInByte);
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }

}
