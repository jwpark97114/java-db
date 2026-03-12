package com.file;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import static org.assertj.core.api.Assertions.*;

public class SaveFileTest {

    KeyValStorage resultStorage;
    SaveFile saveSystem;
    String testSaveFile = "testSaveFile";

    @BeforeEach
    public void setUp(){
        saveSystem = new SaveFile();
        saveSystem.setFileName(testSaveFile);
    }

    @AfterEach
    public void removeTestSave(){
        File testSave = new File(testSaveFile);
        if(testSave.exists()){
            testSave.delete();
        }

    }


    @ParameterizedTest
    @CsvSource({
            "a , 1000",
            "b, 200012",
            "jon, asdasda",
            "caaa, asda"
    })
    public void testSerialization(String k, String v){

        byte[] serialized = saveSystem.serializeKVPair(k,v);

        ByteBuffer bufferedBytes = ByteBuffer.wrap(serialized);

        int kLength = bufferedBytes.getInt();
        byte[] keyBytes = new byte[kLength];
        bufferedBytes.get(keyBytes);
        int vLength = bufferedBytes.getInt();
        byte[] valBytes = new byte[vLength];
        bufferedBytes.get(valBytes);

        assertThat(keyBytes).isEqualTo(k.getBytes());
        assertThat(kLength).isEqualTo(k.getBytes().length);
        assertThat(valBytes).isEqualTo(v.getBytes());
        assertThat(vLength).isEqualTo(v.getBytes().length);
    }

    @ParameterizedTest
    @CsvSource({
            "a , 1000",
            "b, 200012",
            "jon, asdasda",
            "caaa, asda"
    })
    public void testSave(String k, String v){
        saveSystem.saveToFile(k, v);
        File savedFile = new File(testSaveFile);
        byte[] savedBytes;
        try(BufferedInputStream instream = new BufferedInputStream(new FileInputStream(savedFile))){
            savedBytes = instream.readAllBytes();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        assertThat(savedBytes).isEqualTo(saveSystem.serializeKVPair(k,v));
    }

    @Test
    public void testLoad(){
        saveSystem.saveToFile("testKey1", "testValue001");
        saveSystem.saveToFile("testKey2", "testValue002");
        saveSystem.saveToFile("testKey3", "testValue003");
        saveSystem.saveToFile("testKey4", "testValue004");
        KeyValStorage loadedStorage = new KeyValStorage();
        saveSystem.loadAllFile(loadedStorage);

        try{

            assertThat(loadedStorage.keys().contains("testKey1")).isEqualTo(true);
            assertThat(loadedStorage.keys().contains("testKey2")).isEqualTo(true);
            assertThat(loadedStorage.keys().contains("testKey3")).isEqualTo(true);
            assertThat(loadedStorage.keys().contains("testKey4")).isEqualTo(true);
            assertThat(loadedStorage.get("testKey1")).isEqualTo("testValue001");
            assertThat(loadedStorage.get("testKey2")).isEqualTo("testValue002");
            assertThat(loadedStorage.get("testKey3")).isEqualTo("testValue003");
            assertThat(loadedStorage.get("testKey4")).isEqualTo("testValue004");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }



}
