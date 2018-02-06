package com.demo.compressimgdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by Administrator on 2018/1/31.
 */

public class CompressUtil {

    //质量压缩
    public static Bitmap compressWithQuality(Bitmap bmp, File file) {
        int options = 30;
        return compress(bmp, options, file);
    }

    //大小压缩
    public static Bitmap compressWithSize(Bitmap bmp, File file) {
        int ratio = 3;
        Bitmap result = Bitmap.createBitmap(bmp.getWidth() / ratio, bmp.getHeight() / ratio, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Rect rect = new Rect(0, 0, bmp.getWidth() / ratio, bmp.getHeight() / ratio);
        canvas.drawBitmap(bmp, null, rect, null);

        return compress(result, 100, file);
    }

    //采样率压缩
    public static Bitmap compressSamplingRate(String filePath, File file) {
        int sampling = 2;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = sampling;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
        return compress(bitmap, 100, file);
    }

    //压缩并保存图片
    public static Bitmap compress(Bitmap bmp, int options, File file) {
        Bitmap result;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, options, outputStream);
        result = getBitmapFromStream(outputStream);
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
        return result;
    }

    public static Bitmap getBitmapFromStream(ByteArrayOutputStream outputStream) {
        byte[] buf = outputStream.toByteArray();
        return BitmapFactory.decodeByteArray(buf, 0, buf.length);
    }

    public static void deleteFile(File file) {
        if (file.exists()) {
            file.delete();
        }
    }
}
