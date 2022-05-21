package com.rnmusicfiles.Utils;

import android.util.Log;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

import androidx.annotation.NonNull;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

import static com.rnmusicfiles.Utils.GeneralUtils.LOG;

public class FS {

    private static String SHAsum(byte[] byteArray){
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return byteArray2Hex(md.digest(byteArray));
    }

    private static String byteArray2Hex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }

    public static String saveToStorage(@NonNull String pathToImg, @NonNull byte[] imageBytes) throws IOException {
        File f = new File(pathToImg);
        File filePath = new File(pathToImg+ "/" + SHAsum(imageBytes));

        if(f.exists() && !f.isDirectory()){
            Log.e(LOG, "coverPath is a file");
            throw new IOException("coverPath is a file");
        }
        if(!f.exists()){
            f.mkdir();
            Log.d(LOG, "coverFolder created");
        }

        if(filePath.exists()){
           return filePath.getAbsolutePath();
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(filePath, false);
            fos.write(imageBytes);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            if (fos != null) {
                fos.flush();
                fos.close();
            }
            throw new IOException(e);
        } finally {
            if (fos != null) {
                fos.flush();
                fos.close();
                return  filePath.getAbsolutePath();
            }
        }
        return "-1";
    }

    public static void listFilesForFolder(File folder, int minFileSize, int maxFileSize, String extensionFilter, List<String> list) {

        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry, minFileSize, maxFileSize, extensionFilter, list);
            } else {

                long fileSize = fileEntry.length();
                if (
                        fileSize < maxFileSize && fileSize > minFileSize &&
                                fileEntry.toString().toLowerCase().endsWith(extensionFilter)
                )
                    list.add(fileEntry.getAbsolutePath());
            }
        }
    }

}
