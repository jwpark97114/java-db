package com.table;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;

public class TableTest {

    @TempDir
    Path testDir;

    @TempDir
    Path saveFile;

    Table testTable;

    @BeforeEach
    public void setUp(){
//        testTable = new Table();
    }

    @ParameterizedTest
    @MethodSource("colParserCases")
    @DisplayName("Table's String Parser for Column Name")
    public void columnStringParserTest(String original, String[] result){
//        assertThat(testTable.parseColumns(original)).isEqualTo(result);
    }

    private static Stream<Arguments> colParserCases(){
        return Stream.of(
                Arguments.of("(1,2,3)", new String[] {"1","2","3"}),
                Arguments.of("(a,c,d,e)",new String[]{"a","c","d","e"}),
                Arguments.of("(a 1, b..sd, a?!e, asdfs)", new String[]{"a 1", "b..sd", "a?!e", "asdfs"})
        );
    }



}
