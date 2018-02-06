package com.demo.compressimgdemo.compress;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/2/2.
 */

public class CompressUtil {

    public static List<Bitmap> compress(File file, Bitmap... bmps) {
        List<Bitmap> bitmapList = new ArrayList<>();
        for (Bitmap bitmap : bmps) {
            bitmapList.add(compress(bitmap, file));
        }
        return bitmapList;
    }

    //压缩并保存图片
    public static Bitmap compress(Bitmap bmp, File file) {
        return compress(bmp, 30, file);
    }

    //压缩并保存图片
    public static Bitmap compress(Bitmap bmp, int quality, File file) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
        byte[] buf = outputStream.toByteArray();
        Bitmap result = BitmapFactory.decodeByteArray(buf, 0, buf.length);
        if (file != null) {
            toSave(outputStream, file);
        }
        return result;
    }

    private static void toSave(ByteArrayOutputStream outputStream, File file) {
        if (file != null) {
            try {
                FileOutputStream os = new FileOutputStream(file);
                os.write(outputStream.toByteArray());
                os.flush();
                os.close();
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
