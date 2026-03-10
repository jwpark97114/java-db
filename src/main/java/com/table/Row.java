package com.table;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.io.ByteArrayOutputStream;

public class Row {

    // Row's Byte[] holds int at its first section
    List<String> rowValues = new ArrayList<>();
    public static final int sizeOfInteger = 4;

    public Row(String[] values){
        Collections.addAll(rowValues, values);
    }

    public String[] getValues(){
        return this.rowValues.toArray(new String[1]);
    }

    public int getSizeInBytes(){
        int sum = 0;
        for(String val : rowValues){
            sum += val.getBytes().length;
        }
        return sum + (rowValues.size() * sizeOfInteger);
    }

    // method to create Row from save file

    public static byte[] rowToBytes(Row rowEntry){
        String[] valuesToSerialize = rowEntry.getValues();
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.writeBytes(ByteBuffer.allocate(sizeOfInteger).putInt(rowEntry.getSizeInBytes()).array());
        for(String val : valuesToSerialize){
            int sizeOfVal = val.getBytes().length;
            output.writeBytes(ByteBuffer.allocate(sizeOfInteger).putInt(sizeOfVal).array());
            output.writeBytes(val.getBytes());
        }

        return output.toByteArray();
    }

    private static void addOneValueOntoList(ByteBuffer buffer, List<String> list){
        int size = buffer.getInt();
        byte[] valBytes = new byte[size];
        buffer.get(valBytes);
        String value = new String(valBytes);
        list.add(value);
    }

    // Table must supply BytesToRow the right sized byte array
    // Or it will break everything
    public static Row BytesToRow(byte[] bytesWithoutRowSizeFromTable){
        ByteBuffer workingBuffer = ByteBuffer.wrap(bytesWithoutRowSizeFromTable);
        List<String> tmpValueStorage = new ArrayList<>();
        while(workingBuffer.hasRemaining()){
            addOneValueOntoList(workingBuffer,tmpValueStorage);
        }
       return new Row(tmpValueStorage.toArray(new String[1]));
    }


}
