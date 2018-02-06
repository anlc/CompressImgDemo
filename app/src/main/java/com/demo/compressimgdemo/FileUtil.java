package com.demo.compressimgdemo;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;

/**
 * Created by Administrator on 2018/1/31.
 */

public class FileUtil {

    public static long getFileSize(File file) {
        long size = 0;

        try {
            if (file.exists()) {
                FileInputStream stream = new FileInputStream(file);
                size = stream.available();
                Log.e("tag", "file size: " + size);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    public static String formatFileSize(long fileL) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileL == 0) {
            return wrongSize;
        }
        if (fileL < 1024) {
            fileSizeString = df.format((double) fileL) + "B";
        } else if (fileL < 1048576) {
            fileSizeString = df.format((double) fileL / 1024) + "KB";
        } else if (fileL < 1073741824) {
            fileSizeString = df.format((double) fileL / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileL / 1073741824) + "GB";
        }
        return fileSizeString;
    }
}
