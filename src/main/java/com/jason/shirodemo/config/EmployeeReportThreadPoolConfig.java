package com.jason.shirodemo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * <一句话简单描述>
 * <详细介绍>
 *
 * @author lihaitao on 2019/8/5
 */
@Configuration
public class EmployeeReportThreadPoolConfig {

    @Value("$employee.thread-pool.size.core")
    private int coreSize;
    @Value("$employee.thread-pool.size.max")
    private int maxSize;
    @Value("$employee.thread-pool.size.queue")
    private int queueSize;

    @Value("$employee.thread-pool.timeout")
    private long timeout;

    @Bean(name = "employeeReportThreadFactory")
    public ThreadFactory employeeReportThreadFactory() {
        return new ThreadFactory() {
            private AtomicInteger count = new AtomicInteger(0);
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setDaemon(false);
                thread.setName("employee-report-thread-" + count.getAndIncrement());
                return thread;
            }
        };
    }

    @Bean
    public ThreadPoolExecutor employeeReportThreadPoolExecutor(ThreadFactory employeeReportThreadFactory) {
        return new ThreadPoolExecutor(coreSize, maxSize, timeout, TimeUnit.SECONDS, new ArrayBlockingQueue<>(queueSize), employeeReportThreadFactory);
    }



}
