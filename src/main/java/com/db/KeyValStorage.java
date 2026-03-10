package com.db;

import com.file.SaveFile;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class KeyValStorage {

    private static final String tombstone = "TOMBSTONEDELETEDMUSTREMOVE";
    private Map<String, String> userDB;
    private SaveFile save = new SaveFile();

    public KeyValStorage(){
        this.userDB = new HashMap<>();
        this.save.loadAllFile(this);
    }

    private boolean contains(String key){
        return this.userDB.containsKey(key);
    }

    public String get(String key) throws Exception {
        if(!contains(key)){
            throw new IllegalArgumentException(" STORAGE::get - KEY DOES NOT EXIST IN THE DB ");
        }

        return this.userDB.get(key);
    }

    public void set(String key, String val){
        this.userDB.put(key,val);
        this.save.saveToFile(key, val);
    }

    public void delete(String key) throws Exception{
        if(!contains(key)){
            throw new IllegalArgumentException( " STORAGE::delete - KEY DOES NOT EXIST IN THE DB ");
        }
        this.userDB.remove(key);
        this.save.saveToFile(key,tombstone);
    }

    public String keys(){
        if(this.userDB.isEmpty()){
            return "";
        }

        return this.userDB.keySet().stream().collect(Collectors.joining(", "));
    }

    public void addKVPair(String key, String value){
        if(value.equalsIgnoreCase(tombstone)){
            if(this.userDB.containsKey(key)) {
                this.userDB.remove(key);
            }
        }
        else{
            this.userDB.put(key,value);
        }


    }
}
