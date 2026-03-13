package com.db;

import com.table.Row;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.FileSystemException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import static com.file.FilePaths.*;
import java.nio.file.Files;
public class MetaData {

    // Holds what columns the table has
    // Tells which row is where in saveFile
    private Path metadataFile;
    private String tableName;
    private String[] columnNames;
    private Map<Integer, Long> idxByteOffsetMap = new HashMap<>();
    private int nextIndex = 0;
    private long currentSize= 0;

    public MetaData(String tableName, String[] columns) {
        this.tableName = tableName;
        this.columnNames = columns.clone();
        this.metadataFile = META_DIR.resolve(this.tableName+".meta");
        if(!Files.exists(this.metadataFile)){
            try{
                Files.createFile(this.metadataFile);
            }
            catch(Exception e){
                throw new RuntimeException(e);
            }
        }
        saveMetaData();
    }

    public MetaData(String tableName){
        this.metadataFile = META_DIR.resolve(tableName+".meta");
        this.loadMetaData(tableName);
    }

    public String getTableName(){
        return this.tableName;
    }

    public String[] getColumnNames(){return  this.columnNames.clone();}

    public int addRowToMetaData(Row rowToAdd){
        long sizeOfRow = rowToAdd.getSizeInBytes();
        idxByteOffsetMap.put(nextIndex, currentSize);
        currentSize += sizeOfRow + Row.sizeOfInteger;
        int returnIndex = nextIndex;
        nextIndex ++;
        saveMetaData(); // Right now it rewrites all metadata everytime
        return returnIndex;
    }

    public Long getRowOffsetFromFile(int index){
        return this.idxByteOffsetMap.get(index);
    }

    public Integer[] getKeysInOffsetMap(){
        return this.idxByteOffsetMap.keySet().toArray(new Integer[0]);
    }

    private void writeOffsetMapToOutputStream(DataOutputStream outStream) throws IOException {
        int numElem = this.idxByteOffsetMap.size();
        outStream.writeInt(numElem);

        for(int key : this.idxByteOffsetMap.keySet()){
            Long toWrite = this.idxByteOffsetMap.get(key);
            outStream.writeInt(key);
            outStream.writeLong(toWrite);
        }
    }

    private void writeColumnNamesToOutputStream(DataOutputStream outStream) throws IOException{
        int numElem = this.columnNames.length;
        outStream.writeInt(numElem);

        for(int i=0; i < numElem; i++){
            byte[] stringInBytes = this.columnNames[i].getBytes();
            int byteSizeOfString = stringInBytes.length;
            outStream.writeInt(byteSizeOfString);
            outStream.write(stringInBytes);
        }
    }

    public void saveMetaData(){
        // we want to store column names, table name,  byteOffset, int, long
        // lets separate saving of byteOffset
        try(DataOutputStream saveStream = new DataOutputStream( new BufferedOutputStream( new FileOutputStream(this.metadataFile.toFile())));){
            byte[] tableNameBytes = this.tableName.getBytes();
            saveStream.writeInt(tableNameBytes.length);
            saveStream.write(tableNameBytes);
            this.writeColumnNamesToOutputStream(saveStream);
            this.writeOffsetMapToOutputStream(saveStream);
            saveStream.writeInt(this.nextIndex);
            saveStream.writeLong(this.currentSize);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private void loadColumnNames(ByteBuffer buffer){
        int colCount = buffer.getInt();
        this.columnNames = new String[colCount];

        for(int i =0; i < colCount; i++){
            int size = buffer.getInt();
            byte[] nameInBytes = new byte[size];
            buffer.get(nameInBytes);
            this.columnNames[i] = new String(nameInBytes);
        }
    }

    private void loadByteOffset(ByteBuffer buffer){
        int mapSize = buffer.getInt();
        this.idxByteOffsetMap = new HashMap<>();
        for(int i=0; i< mapSize; i++){
            int index = buffer.getInt();
            Long tmp = buffer.getLong();
            this.idxByteOffsetMap.put(index, tmp);
        }
    }

    public void loadMetaData(String tableName){
        try(BufferedInputStream inStream = new BufferedInputStream(new FileInputStream(this.metadataFile.toFile()))){
            ByteBuffer readBuffer = ByteBuffer.wrap(inStream.readAllBytes());
            int tableNameByteLength = readBuffer.getInt();
            byte[] tableNameBytes = new byte[tableNameByteLength];
            readBuffer.get(tableNameBytes);
            this.tableName = new String(tableNameBytes);
            this.loadColumnNames(readBuffer);
            this.loadByteOffset(readBuffer);
            this.nextIndex = readBuffer.getInt();
            this.currentSize = readBuffer.getLong();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
