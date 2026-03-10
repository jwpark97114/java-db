package com.table;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

public class RowTest {

    @Test
    public void constructorTest(){
        String[] paramString = new String[]{"one", "two","three"};
        Row testRow = new Row(paramString);
        assertThat(testRow.getValues().length).isEqualTo(3);
        assertThat(testRow.getValues()[0]).isEqualTo("one");
        assertThat(testRow.getValues()[1]).isEqualTo("two");
        assertThat(testRow.getValues()[2]).isEqualTo("three");
    }

    @ParameterizedTest
    @CsvSource({
            "testStrign1",
            "aaaaa",
            "1",
            "'' ",
            "' '"
    })
    public void getSizeInByteTest(String testString){
        Row newRow = new Row(new String[]{testString});
        assertThat(newRow.getSizeInBytes()).isEqualTo(testString.getBytes().length + 4);

    }

    @Test
    public void RowToByteTest(){
        Row newRow = new Row(new String[]{"one", "two", "three"});
        byte[] byteRow = Row.rowToBytes(newRow);
        ByteBuffer buffer = ByteBuffer.wrap(byteRow);
        int sizeOfRow = buffer.getInt();
        List<String> result = new ArrayList<>();
        List<Integer> sizeList = new ArrayList<>();
        while(buffer.hasRemaining()){
            int size = buffer.getInt();
            sizeList.add(size);
            byte[] valueByte = new byte[size];
            buffer.get(valueByte);
            result.add(new String(valueByte));
        }
        assertThat(sizeOfRow).isEqualTo(sizeList.stream().reduce(0, (a,b) -> a+b) + 12);
        assertThat(result.get(0)).isEqualTo("one");
        assertThat(result.get(1)).isEqualTo("two");
        assertThat(result.get(2)).isEqualTo("three");
    }


    //These two tests below are result of ChatGPT 5.4
    // being asked to show what is the right way to test two static methods

    @Test
    public void BytesToRowTest(){
        ByteBuffer buffer = ByteBuffer.allocate(
                (Integer.BYTES + "one".getBytes().length) +
                        (Integer.BYTES + "two".getBytes().length) +
                        (Integer.BYTES + "three".getBytes().length)
        );

        buffer.putInt("one".getBytes().length);
        buffer.put("one".getBytes());
        buffer.putInt("two".getBytes().length);
        buffer.put("two".getBytes());
        buffer.putInt("three".getBytes().length);
        buffer.put("three".getBytes());

        Row row = Row.BytesToRow(buffer.array());

        assertThat(row.getValues()).containsExactly("one", "two", "three");
    }

}
