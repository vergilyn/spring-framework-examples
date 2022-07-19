package com.vergilyn.examples.springboot.shutdown;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.context.ShutdownEndpoint;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

@SpringBootTest(classes = SpringbootShutdownApplication.class)
public abstract class AbstractSpringbootShutdownApplicationTests {
	@Autowired
	protected ConfigurableApplicationContext configurableApplicationContext;

	/**
	 * 模拟服务器重启， 触发 shutdown-hook。
	 *
	 * <p> <h3>备注</h3>
	 * 正常情况下，IDEA 中无法通过 `stop按钮` 或 win10任务管理器结束PID 来触发 IDEA对{@link AbstractApplicationContext#close()} 方法中的断点。
	 *
	 * <p> <b>解决方式：</b>
	 * <p> 方式一：通过 spring-actuator 请求 `http://127.0.0.1:8080/actuator/shutdown`，等价于 {@link ShutdownEndpoint#shutdown()}
	 * <p> 方式二：例如以下代码，手动调用 {@link AbstractApplicationContext#close()}
	 *
	 * @param delayMillis 延迟xx毫秒 调用 {@link AbstractApplicationContext#close()}
	 */
	protected void asyncDelayClose(long delayMillis){
		// 触发 shutdown-hook
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				System.out.printf("[vergilyn] >>>> manual-invoke-close, delay: %d ms \n", delayMillis);
				if (configurableApplicationContext.isActive() && configurableApplicationContext.isRunning()){
					configurableApplicationContext.close();
				}
			}
		}, delayMillis);
	}

	public static void sleepSafeByMillis(long timeout){
		sleepSafe(timeout, TimeUnit.MILLISECONDS);
	}

	public static void sleepSafe(long timeout, TimeUnit timeUnit){
		try {
			timeUnit.sleep(timeout);
		}catch (Exception ignored){

		}
	}
}