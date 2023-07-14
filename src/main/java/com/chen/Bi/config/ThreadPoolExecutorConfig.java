package com.chen.Bi.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程池执行器配置
 *
 * @author CSY
 * @date 2023/06/09
 */
@Configuration
public class ThreadPoolExecutorConfig {
    @Bean
    public ThreadPoolExecutor threadPoolExecutor() {
        ThreadFactory threadFactory = new ThreadFactory() {
            private int count = 1;

            @Override
            public Thread newThread(@NotNull Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("线程" + count);
                count++;
                return thread;
            }
        };
        //corePoolSize（核心线程数 => 正式员工数）：正常情况下，可以设置为 2 - 4
        //maximumPoolSize：设置为极限情况，设置为 <= 4
        //keepAliveTime（空闲线程存活时间）：一般设置为秒级或者分钟级
        //TimeUnit unit（空闲线程存活时间的单位）：分钟、秒
        //workQueue（工作队列）：结合实际请况去设置，可以设置为 20  排队数量
        //threadFactory（线程工厂）：控制每个线程的生成、线程的属性（比如线程名）
        //RejectedExecutionHandler（拒绝策略）：抛异常，标记数据库的任务状态为 “任务满了已拒绝” 默认抛出异常
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(2, 4,
                100, TimeUnit.SECONDS, new ArrayBlockingQueue<>(4), threadFactory);
        return threadPoolExecutor;
    }
}
