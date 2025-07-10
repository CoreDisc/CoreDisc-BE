package com.coredisc.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
@EnableAsync
public class AsyncConfig implements AsyncConfigurer {

    @Override
    @Bean(name = "mailExecutor")
    public Executor getAsyncExecutor() { // @Async 메서드가 실행될 때 사용할 스레드 풀(Executor)을 리턴
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2); // 기본적으로 유지할 스레드 수 (항상 실행 대기)
        executor.setMaxPoolSize(5); // 최대 허용할 스레드 수
        executor.setQueueCapacity(10); // 작업 대기 큐의 크기
        executor.setThreadNamePrefix("Async MailExecutor-"); // 스레드 이름 접두사 (디버깅 시 보기 좋게)
        executor.initialize(); // 설정이 끝난 후 Executor 초기화
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() { // @Async 메서드에서 예외가 발생했을 때 어떻게 처리할지 지정하는 부분
        return AsyncConfigurer.super.getAsyncUncaughtExceptionHandler();
    }
}
