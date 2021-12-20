package org.dx.clod;

import org.dx.clod.core.task.MonitorTask;
import org.dx.clod.utils.ScheduleUtil;

import java.util.concurrent.TimeUnit;

/**
 * @author lizifeng
 * @date 2021/12/9 10:06
 */
public class Bootstrap {

    public static void main(String[] args) {
        ScheduleUtil.SCHEDULED_THREAD_POOL_EXECUTOR.scheduleAtFixedRate(new MonitorTask(), 3L, 60L, TimeUnit.SECONDS);
    }
}
