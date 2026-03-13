package com.file;
import java.nio.file.Path;
import java.nio.file.Files;

public class FilePaths {
    public static final Path BASE_DIR = Path.of("storage");
    public static final Path TABLE_DIR = BASE_DIR.resolve("tables");
    public static final Path META_DIR = BASE_DIR.resolve("metadata");

    static{

        try{
            if(!Files.exists(BASE_DIR)){
                Files.createDirectory(BASE_DIR);
            }
            if(!Files.exists(TABLE_DIR)){
                Files.createDirectory(TABLE_DIR);
            }if(!Files.exists(META_DIR)){
                Files.createDirectory(META_DIR);
            }
        }
            catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
