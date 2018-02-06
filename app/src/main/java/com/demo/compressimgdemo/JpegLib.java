package com.demo.compressimgdemo;

/**
 * Created by Administrator on 2018/2/2.
 */

public class JpegLib {
    static {
        System.loadLibrary("JpegLib");
    }

    public native void compress();
}
