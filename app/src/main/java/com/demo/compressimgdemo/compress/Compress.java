package com.demo.compressimgdemo.compress;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/2/5.
 */

public class Compress {

    public static abstract class CallBack {
        protected void onStart() {
        }

        protected void onSuccess(List<Bitmap> bitmaps) {
        }

        protected void onSuccess(Bitmap bitmap) {
        }

        protected void onError() {
        }
    }

    Handler handler;
    int imgCount = 0;
    Build build;
    List<Bitmap> result;
    CallBack callBack;

    CallBack localCallBack = new CallBack() {
        @Override
        public void onStart() {
            if (callBack != null) {
                handler.post(new Runnable() {//切换回主线程
                    @Override
                    public void run() {
                        callBack.onStart();
                    }
                });
            }
        }

        @Override
        public void onSuccess(Bitmap bitmap) {
            result.add(bitmap);
            if (result.size() == build.imgList.size()) {
                if (callBack != null) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {//切换回主线程
                            callBack.onSuccess(result);
                        }
                    });
                }
            }
            if (callBack != null) {//在非主线程中，如果在这个回调中处理UI，需要切换回主线程
                callBack.onSuccess(bitmap);
            }
        }

        @Override
        public void onError() {
            if (callBack != null) {//切换回主线程
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onStart();
                    }
                });
            }
        }
    };

    private Compress(Build build, CallBack callBack) {
        this.callBack = callBack;
        this.build = build;
        imgCount = 0;
        result = new ArrayList<>();
        handler = new Handler(Looper.getMainLooper());
    }

    public static Build create() {
        return new Build();
    }

    public static class Build {

        private List<Bitmap> imgList;
        private int quality;
        private File file;

        public Build() {
            imgList = new ArrayList<>();
            quality = 30;
        }

        public Build saveFile(File file) {
            this.file = file;
            return this;
        }

        public Build quality(int quality) {
            this.quality = quality;
            return this;
        }

        public Build load(File... file) {
            for (int i = 0; i < file.length; i++) {
                imgList.add(BitmapFactory.decodeFile(file[i].getAbsolutePath()));
            }
            return this;
        }

        public Build load(String... filePath) {
            for (int i = 0; i < filePath.length; i++) {
                imgList.add(BitmapFactory.decodeFile(filePath[i]));
            }
            return this;
        }

        public Build load(Bitmap... bitmap) {
            for (int i = 0; i < bitmap.length; i++) {
                imgList.add(bitmap[i]);
            }
            return this;
        }

        public void compress(CallBack callBack) {
            if (imgList == null || imgList.size() < 1) {
                throw new RuntimeException("no img to compress");
            }
            new Compress(this, callBack).compress();
        }

        public Bitmap syncCompress() {
            return new Compress(this, null).syncCompress();
        }
    }

    private void compress() {
        ThreadPool pool = ThreadPool.getInstance();
        try {
            localCallBack.onStart();

            for (int i = 0; i < build.imgList.size(); i++) {
                pool.execute(new Run(build.imgList.get(i), build.file));
            }
        } catch (Exception e) {
            localCallBack.onError();
        }
    }

    private Bitmap syncCompress() {
        return CompressUtil.compress(build.imgList.get(0), build.file);
    }

    class Run implements Runnable {

        private Bitmap bitmap;
        private File file;

        public Run(Bitmap bitmap, File file) {
            this.bitmap = bitmap;
            this.file = file;
        }

        @Override
        public void run() {
            Bitmap result = CompressUtil.compress(bitmap, file);
            localCallBack.onSuccess(result);
        }
    }
}
