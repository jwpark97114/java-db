package com.table;
import java.nio.file.Path;
import java.nio.file.Files;

public class FilePaths {
    public static final Path BASE_DIR = Path.of("storage");
    public static final Path TABLE_DIR = BASE_DIR.resolve("tables");
    public static final Path META_DIR = BASE_DIR.resolve("metadata");

    static{

        try{
            Files.createDirectory(TABLE_DIR);
            Files.createDirectory(META_DIR);
        }
            catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
