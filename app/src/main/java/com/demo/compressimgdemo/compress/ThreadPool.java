package com.demo.compressimgdemo.compress;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Administrator on 2018/2/5.
 */

public class ThreadPool {

    private final static int POOL_SIZE = 6;
    private static ExecutorService service;

    private ThreadPool() {
        service = Executors.newFixedThreadPool(POOL_SIZE);
    }

    private static final class Singleton {
        private static final ThreadPool INSTANCE = new ThreadPool();
    }

    public static ThreadPool getInstance(){
        return Singleton.INSTANCE;
    }

    public Future<?> submit(Runnable runnable) {
        return service.submit(runnable);
    }

    public void execute(Runnable runnable) {
        service.execute(runnable);
    }

    public void release() {
        service.shutdown();
    }

}
