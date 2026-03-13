package com.table;

import org.junit.jupiter.api.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static com.file.FilePaths.*;

public class TableTest {

    private final String tableName = "test_table";
    private Path testFilePath;

    @BeforeEach
    void setUp() throws IOException{
        Files.deleteIfExists(testFilePath);
        this.testFilePath = TABLE_DIR.resolve(tableName + ".table");
    }

    @AfterEach
    void clearTestSave() throws IOException {
        Files.deleteIfExists(testFilePath);
    }

    @Test
    void createNewTableFileAndParseColumns(){
        Table testTable = new Table(tableName, "(col1, col2, col3");
        assertThat(testTable.getColumnNames()).containsExactly("col1", "col2","col3");
        assertThat(Files.exists(testFilePath)).isTrue();
    }

    @Test
    void queryUnknownColumns(){
        Table testTable = new Table(tableName, "(id, name");
        assertThatThrownBy(()->testTable.queryResult("amount = 1000")).isInstanceOf(RuntimeException.class);
    }


}
