package com.vergilyn.examples.scheduler;

import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author vergilyn
 * @date 2020-01-26
 */
@Component
@Slf4j
public class SchedulerTask {

    @Scheduled(cron = "0/5 * * * * ?")
    public void task() throws InterruptedException{
        log.info("scheduler task local-time: {}", LocalTime.now());
        Thread.sleep(TimeUnit.SECONDS.toMillis(3));
    }
}
