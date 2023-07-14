package com.chen.Bi;

import com.chen.Bi.mapper.ChartMapper;
import com.chen.Bi.mq.MyMessageProducer;
import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.RSemaphore;
import org.redisson.api.RedissonClient;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@SpringBootTest
@Slf4j
public class Test1 {
    @Resource
    private ChartMapper chartMapper;
    @Resource
    private MyMessageProducer myMessageProducer;

    @Test
    void test01() {
        String chartId = "1";
        String sql = String.format("select * from chart_%s", chartId);
        List<Map<String, Object>> maps = chartMapper.queryChartData(sql);
        // 原始数据 通过id创建表储存
        String mapsString = maps.toString();
        System.out.println(mapsString);
    }


    @Test
    void test03() {
        Long chartId = 1663754496272003074L;
        String sql = String.format("select * from chart_%d", chartId);
        List<Map<String, Object>> listMap = chartMapper.queryChartData(sql);
        System.out.println(listMap.get(0).keySet());
        for (Map<String, Object> map : listMap) {
            System.out.println(map.values());
        }

    }

    // 限流 学习版
    //本地限流，单机限流
    // 每个服务器单机限流 适用于单机项目
    @Test
    void test04() {
        RateLimiter limiter = RateLimiter.create(5.0);
        while (true) {
            if (limiter.tryAcquire()) {
                System.out.println("通过");
            } else {
                System.out.println("拦截");
            }
        }
    }
    //分布式限流
    @Test
    void test05() {
        RedissonClient redissonClient = Redisson.create();
        RSemaphore semaphore = redissonClient.getSemaphore("mySemaphore");
        boolean result = semaphore.tryAcquire();
        if(result){
            System.out.println("通过");
        }else {
            System.out.println("拦截");
        }
    }

    //MQ 测试
    @Test
    void  test06(){
        myMessageProducer.sendMessage("code_exchange","my_routingKey","你好吖");
    }
}