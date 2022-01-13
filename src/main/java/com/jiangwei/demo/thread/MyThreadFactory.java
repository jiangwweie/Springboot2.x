package com.jiangwei.demo.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义ThreadFactory
 * @author jiangwei
 * @date 2021/10/15
 */
public class MyThreadFactory implements ThreadFactory {


    private final String namePrefix;

    private final AtomicInteger nextId = new AtomicInteger(1);

    /**
     * 定义线程组名称，在使用jstack来排查问题时，非常有帮助
     *
     * @param whatFeatureOfGroup
     */
    public MyThreadFactory(String whatFeatureOfGroup) {
        this.namePrefix = "MyThreadFactory's" + whatFeatureOfGroup + "-Worker";
    }


    @Override
    public Thread newThread(Runnable r) {
        String name = this.namePrefix + nextId.getAndDecrement();
        //group target  name stackSize
        Thread thread = new Thread(null, r, name, 0);
        System.out.println(thread.getName());
        return thread;
    }
}

//任务执行体
class MyTask implements Runnable {

    private final AtomicInteger count = new AtomicInteger(0);

    @Override
    public void run() {
        System.out.println("running_  " + count.getAndIncrement());
    }
}