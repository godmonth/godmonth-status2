package com.godmonth.status2.executor.impl;

import com.google.common.util.concurrent.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ListenableFutureTest {
    private static ExecutorService executorService = Executors.newCachedThreadPool();
    private static ListeningExecutorService listeningExecutorService = MoreExecutors.listeningDecorator(executorService);

    @Test
    void name() throws InterruptedException {
        ListenableFuture<String> future = listeningExecutorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                // 执行一些代码，可能抛出异常
                return "result";
            }
        });
        Thread.sleep(500);
        Futures.addCallback(future, new FutureCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println(result);
            }

            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
            }
        }, executorService);

    }

    @Test
    void name2() throws InterruptedException {
        ListenableFuture<String> future = listeningExecutorService.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {

                throw new IllegalArgumentException("aaa");
            }
        });
        Thread.sleep(500);
        Futures.addCallback(future, new FutureCallback<String>() {
            @Override
            public void onSuccess(String result) {
                System.out.println(result);
            }

            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
            }
        }, executorService);

    }

    @AfterAll
    public static void shutdown() {
        listeningExecutorService.shutdown();
    }
}
