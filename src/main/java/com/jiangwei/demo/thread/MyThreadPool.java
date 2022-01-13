package com.jiangwei.demo.thread;

import org.springframework.data.redis.stream.Task;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author jiangwei
 * @date 2021/10/15
 */
public class MyThreadPool {


    private static int i = 1;

    public static void main(String[] args) {

        //缓存队列长度设置为2，为了快速除法rejectHandler
        BlockingDeque blockingDeque = new LinkedBlockingDeque(2);
        //假设外部线程的来源有 机房1 和 机房2的混合调用
        MyThreadFactory myThreadFactory1 = new MyThreadFactory("机房1");
        MyThreadFactory myThreadFactory2 = new MyThreadFactory("机房2");

        MyRejectHandler myRejectHandler = new MyRejectHandler();

        ThreadPoolExecutor threadPoolExecutor1 = new ThreadPoolExecutor(1, 2, 60, TimeUnit.SECONDS, blockingDeque, myThreadFactory1, myRejectHandler);
        ThreadPoolExecutor threadPoolExecutor2 = new ThreadPoolExecutor(1, 2, 60, TimeUnit.SECONDS, blockingDeque, myThreadFactory2, myRejectHandler);

        Runnable task = new MyTask();
        for (int i = 0; i < 200; i++) {
            threadPoolExecutor1.execute(task);
            threadPoolExecutor2.execute(task);
        }
    }



}
