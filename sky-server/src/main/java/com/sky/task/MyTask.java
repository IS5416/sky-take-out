package com.sky.task;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 自定义定时任务类
 */
@Slf4j
@Component
public class MyTask {

    /**
     * 定时任务，每5s触发一次
     */
    //@Scheduled(cron = "0/5 * * * * ?")
    public void executeTask() {
        log.info("定时任务执行-----{}", new Date());
    }
}
