package com.ccxh.top;

import java.util.Locale;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 创建线程池帮助类
 * @author honey
 */
public class ThreadPoolUtil {
    private  ThreadPoolConfig threadPoolConfig=new ThreadPoolConfig();

    /**
     * 线程池参数
     */
    public static class ThreadPoolConfig{
        /**
         * 线程池的基本大小
         */
        private int corePoolSize = 10;
        /**
         * 线程池最大数量
         */
        private int maximumPoolSizeSize = 100;
        /**
         * 线程活动保持时间
         */
        private long keepAliveTime = 1;
        /**
         * 任务队列
         */
        private ArrayBlockingQueue workQueue = new ArrayBlockingQueue(10);



        /**
         * 拒绝策略
         * AbortPolicy
         * DiscardPolicy
         * DiscardOldestPolicy
         * CallerRunsPolicy
         */
        private RejectedExecutionHandler rejectedExecutionHandler=new ThreadPoolExecutor.CallerRunsPolicy();
        public int getCorePoolSize() {
            return corePoolSize;
        }

        public void setCorePoolSize(int corePoolSize) {
            this.corePoolSize = corePoolSize;
        }

        public int getMaximumPoolSizeSize() {
            return maximumPoolSizeSize;
        }

        public void setMaximumPoolSizeSize(int maximumPoolSizeSize) {
            this.maximumPoolSizeSize = maximumPoolSizeSize;
        }

        public long getKeepAliveTime() {
            return keepAliveTime;
        }

        public void setKeepAliveTime(long keepAliveTime) {
            this.keepAliveTime = keepAliveTime;
        }

        public ArrayBlockingQueue getWorkQueue() {
            return workQueue;
        }

        public void setWorkQueue(ArrayBlockingQueue workQueue) {
            this.workQueue = workQueue;
        }
        public RejectedExecutionHandler getRejectedExecutionHandler() {
            return rejectedExecutionHandler;
        }

        public void setRejectedExecutionHandler(RejectedExecutionHandler rejectedExecutionHandler) {
            this.rejectedExecutionHandler = rejectedExecutionHandler;
        }
    }

    /**
     * 创建线程池
     * @return
     */
    public static ThreadPoolExecutor getThreadPool(){
        return ThreadPoolUtil.getThreadPool(null,null);
    }

    /**
     * 自定义线程池属性
     * @param threadPoolConfig
     * @return
     */
    public static ThreadPoolExecutor getThreadPool(ThreadPoolConfig threadPoolConfig){
        return ThreadPoolUtil.getThreadPool(null,threadPoolConfig);
    }

    /**
     * 自定义任务前戳
     * @param beforeName
     * @return
     */
    public static ThreadPoolExecutor getThreadPool(String beforeName){
        return ThreadPoolUtil.getThreadPool(beforeName,null);
    }



    /**
     * 创建指定前戳的任务名称和配置
     * @param beforeName 前戳名称
     * @param threadPoolConfig  线程池配置 为null时自动创建
     * @return
     */
    public static ThreadPoolExecutor getThreadPool(String beforeName,ThreadPoolConfig threadPoolConfig){
        NamedThreadFactory namedThreadFactory = null;
        if ("".equals(beforeName)||beforeName==null){
            namedThreadFactory=new NamedThreadFactory();
        }else{
            namedThreadFactory=new NamedThreadFactory(beforeName);
        }
        if (threadPoolConfig==null){
            threadPoolConfig=new ThreadPoolConfig();
        }
        return new ThreadPoolExecutor(threadPoolConfig.getCorePoolSize(), threadPoolConfig.getMaximumPoolSizeSize(), threadPoolConfig.getKeepAliveTime(),
                TimeUnit.SECONDS,
                threadPoolConfig.getWorkQueue(),namedThreadFactory,threadPoolConfig.getRejectedExecutionHandler());
    }


    private static class NamedThreadFactory implements ThreadFactory {
        /**
         * 任务名称前戳
         */
        private String beforeName="task";
        private final AtomicInteger threadNumberAtomicInteger = new AtomicInteger(1);
        public NamedThreadFactory(String beforeName){
            this.beforeName=beforeName;
        }
        public NamedThreadFactory(){

        }
        @Override
        public Thread newThread(Runnable r) {
            Thread thread=  new Thread(r,String.format(Locale.CHINA,"%s-%d", this.beforeName,threadNumberAtomicInteger.getAndIncrement()));
            //是否是守护线程
           thread.setDaemon(false);
            //设置优先级 1~10 有3个常量 默认 Thread.MIN_PRIORITY*/
           // thread.setPriority(Thread.NORM_PRIORITY);
            return thread;
        }
    }

    /**
     * 获取一个默认的线程池配置
     * @return
     */
    public static ThreadPoolConfig threadPoolConfigBuid(){
        return new ThreadPoolConfig();
    }

}