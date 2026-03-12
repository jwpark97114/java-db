package com.file;
import com.google.common.primitives.Bytes;
import java.io.*;
import java.nio.ByteBuffer;

public class SaveFile {

    private String fileName = "save";

    private static final int keyLengthByte = 4;
    private static final int valLengthByte = 4;

    public void setFileName(String dir){
        this.fileName = dir;
    }

    public byte[] serializeKVPair(String key, String val){
        int sizeKey = key.getBytes().length;
        byte[] keySizeInByte = ByteBuffer.allocate(keyLengthByte).putInt(sizeKey).array();
        int sizeVal = val.getBytes().length;
        byte[] valSizeInByte = ByteBuffer.allocate(valLengthByte).putInt(sizeVal).array();
        byte[] keyBody = key.getBytes();
        byte[] valBody = val.getBytes();
        return Bytes.concat(keySizeInByte,keyBody,valSizeInByte,valBody);
    }

    public void loadAllFile(KeyValStorage newStorage){
        File saveFile = new File(fileName);
        if(!saveFile.exists()){
            return;
        }

        try(InputStream istream = new FileInputStream(saveFile);
            DataInputStream bufferedStream = new DataInputStream(istream);
        ){
            while(true){
                try{
                    int keyLength =  bufferedStream.readInt();
                    byte[] keyString = new byte[keyLength];
                    bufferedStream.readFully(keyString);
                    int valLength = bufferedStream.readInt();
                    byte[] valString = new byte[valLength];
                    bufferedStream.readFully(valString);
                    newStorage.addKVPair(new String(keyString), new String(valString));
                } catch (EOFException eof) {
                    break;
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public void saveToFile(String key, String val){
        File newFile = new File(fileName);

        try(OutputStream fileWriter = new FileOutputStream(newFile,true);
            BufferedOutputStream bw = new BufferedOutputStream(fileWriter)){
            if(!newFile.exists()){
                newFile.createNewFile();
            }
            bw.write(serializeKVPair(key, val));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
