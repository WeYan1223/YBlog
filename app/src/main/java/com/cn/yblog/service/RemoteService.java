package com.cn.yblog.service;

import com.cn.yblog.util.LogUtil;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * description: none
 *
 * @author yan.w.s@qq.com
 * <p>time: 2022/12/23
 * <p>version: 1.0
 * <p>update: none
 */
public class RemoteService {
    private static final int CORE_POOL_SIZE = 3;
    private static final int MAXIMUM_POOL_SIZE = 6;
    private static final int KEEP_ALIVE_TIME = 5;
    private static final TimeUnit KEEP_ALIVE_TIME_UNIT = TimeUnit.SECONDS;
    private static final int BLOCK_QUEUE_LENGTH = 3;

    private static RemoteService INSTANCE;

    private final ThreadPoolExecutor mExecutor;

    private RemoteService() {
        mExecutor = new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAXIMUM_POOL_SIZE,
                KEEP_ALIVE_TIME,
                KEEP_ALIVE_TIME_UNIT,
                new ArrayBlockingQueue<>(BLOCK_QUEUE_LENGTH),
                new InnerThreadFactory()
        );
    }

    public static RemoteService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RemoteService();
        }
        return INSTANCE;
    }

    /**
     * 提交任务
     *
     * @param r r
     */
    public void execute(Runnable r) {
        mExecutor.execute(r);
    }

    /**
     * 提交任务，增加线程超时终端
     *
     * @param r        r
     * @param timeout  超时时间，单位为秒
     * @param listener 超时监听
     */
    public void submitWithTimeout(Runnable r, int timeout, TimeoutListener listener) {
        Future<?> future = mExecutor.submit(r);
        try {
            future.get(timeout, TimeUnit.SECONDS);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            LogUtil.e("RemoteDbService#submit()请求超时，Runnable = " + r);
            future.cancel(true);
            listener.onTimeout();
            e.printStackTrace();
        }
    }

    /**
     * 执行完剩余任务终止线程池
     */
    public void shutdown() {
        mExecutor.shutdown();
    }

    /**
     * 立刻终止线程池
     */
    public void shutdownNow() {
        mExecutor.shutdownNow();
    }

    public interface TimeoutListener {
        /**
         * 请求超时回调
         */
        void onTimeout();
    }

    /**
     * 自定义线程工厂，这里主要起到修改线程名称作用
     */
    private static class InnerThreadFactory implements ThreadFactory {
        static final String THREAD_NAME = "remote_db-";
        int count;

        @Override
        public Thread newThread(Runnable r) {
            return new Thread(r, THREAD_NAME + count++);
        }
    }
}
