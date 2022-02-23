package org.mahefa.common.conf;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * Activate Spring Async and override executor
 */
@Configuration
@EnableAsync
public class SpringConfiguration implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        return new ThreadPoolTaskExecutor();
    }
}
