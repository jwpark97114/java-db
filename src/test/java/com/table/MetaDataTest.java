package com.table;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class MetaDataTest {

    // This is Ai generated so that I can see how AI does the testing
    // Copilot gave me tons of Testing related comments
    // So I wanted to see what it says about ChatGPT's version of test class (5.4 Thinking)

    private static final String TABLE_NAME = "test_meta_data";

    @AfterEach
    void cleanUp() {
        File metaFile = new File(TABLE_NAME + ".meta");
        if (metaFile.exists()) {
            metaFile.delete();
        }
    }

    @Test
    @DisplayName("addRowToMetaData assigns sequential indexes and correct byte offsets")
    void addRowToMetaData_assignsSequentialIndexesAndOffsets() {
        MetaData metaData = new MetaData(TABLE_NAME, new String[]{"id", "name"});
        Row firstRow = new Row(new String[]{"1", "Alice"});
        Row secondRow = new Row(new String[]{"2", "Bob"});

        int firstIndex = metaData.addRowToMetaData(firstRow);
        int secondIndex = metaData.addRowToMetaData(secondRow);

        assertThat(firstIndex).isEqualTo(0);
        assertThat(secondIndex).isEqualTo(1);
        assertThat(metaData.getRowOffsetFromFile(0)).isEqualTo(0L);
        assertThat(metaData.getRowOffsetFromFile(1))
                .isEqualTo((long) firstRow.getSizeInBytes());
    }

    @Test
    @DisplayName("saveMetaData and loadMetaData restore saved state")
    void saveAndLoadMetaData_restoresSavedState() throws Exception {
        MetaData original = new MetaData(TABLE_NAME, new String[]{"id", "name"});
        Row firstRow = new Row(new String[]{"1", "Alice"});
        Row secondRow = new Row(new String[]{"2", "Bob"});

        original.addRowToMetaData(firstRow);
        original.addRowToMetaData(secondRow);
        original.saveMetaData();

        MetaData loaded = new MetaData(TABLE_NAME, new String[]{"temp"});
        loaded.loadMetaData(TABLE_NAME);

        assertThat(readStringArrayField(loaded, "columNames"))
                .containsExactly("id", "name");
        assertThat(loaded.getRowOffsetFromFile(0)).isEqualTo(0L);
        assertThat(loaded.getRowOffsetFromFile(1))
                .isEqualTo((long) firstRow.getSizeInBytes());
        assertThat(readIntField(loaded, "nextIndex")).isEqualTo(2);
        assertThat(readLongField(loaded, "currentSize"))
                .isEqualTo((long) firstRow.getSizeInBytes() + secondRow.getSizeInBytes());
    }

    @Test
    @DisplayName("constructor with table name loads existing metadata file")
    void constructorWithTableName_loadsExistingFile() throws Exception {
        MetaData original = new MetaData(TABLE_NAME, new String[]{"id", "name"});
        Row row = new Row(new String[]{"1", "Alice"});
        original.addRowToMetaData(row);
        original.saveMetaData();
        MetaData loaded = assertDoesNotThrow(() -> new MetaData(TABLE_NAME));

        assertThat(readStringArrayField(loaded, "columNames"))
                .containsExactly("id", "name");
        assertThat(loaded.getRowOffsetFromFile(0)).isEqualTo(0L);
        assertThat(readIntField(loaded, "nextIndex")).isEqualTo(1);
        assertThat(readLongField(loaded, "currentSize"))
                .isEqualTo((long) row.getSizeInBytes());
    }

    private String[] readStringArrayField(Object target, String fieldName) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return (String[]) field.get(target);
    }

    private int readIntField(Object target, String fieldName) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.getInt(target);
    }

    private long readLongField(Object target, String fieldName) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.getLong(target);
    }
}