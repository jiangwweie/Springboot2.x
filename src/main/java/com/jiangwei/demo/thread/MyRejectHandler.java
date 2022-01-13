package com.jiangwei.demo.thread;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author jiangwei
 * @date 2021/10/15
 */
public class MyRejectHandler implements RejectedExecutionHandler {


    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        System.out.println(" task reject . " + executor.toString());
    }
}
