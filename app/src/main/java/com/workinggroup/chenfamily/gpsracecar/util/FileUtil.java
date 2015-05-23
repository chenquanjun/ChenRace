package com.workinggroup.chenfamily.gpsracecar.util;

import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;

/**
 * Created by cheneven on 5/19/15.
 */
public class FileUtil {

    //Storage root path
    public static final File STORAGE_ROOT_BASE=  Environment.getExternalStorageDirectory();
    public static final String STORAGE_ROOT_FOLDER = "/RoadRace/";
    /* File path for storing extracted IM DB
    * @return File path
    */
    public static final String EXTRACTED_DB_FILEPATH(){
        String path = STORAGE_ROOT_BASE + STORAGE_ROOT_FOLDER;
        IS_DIRECTORY_EXIST(path);
        return path;
    }

    public static final void IS_DIRECTORY_EXIST(String filePath){
        File dir = new File(filePath);

        if (!dir.exists()){
            dir.mkdirs();
        }
    }

    public static String getExtractDbFileName(int version) {
        //File format:race_v + DB_VERSION + _ + timestamp

        Long time = System.currentTimeMillis();
        String dateStr = new SimpleDateFormat("yyyyMMdd_kkmmss").format(time);
        return "Race_v" + Integer.toString(version) + "_" + dateStr;
    }
}
