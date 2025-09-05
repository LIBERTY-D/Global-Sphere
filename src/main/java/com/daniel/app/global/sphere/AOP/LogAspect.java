package com.daniel.app.global.sphere.AOP;


import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Aspect
@Component
@Slf4j
public class LogAspect {

    @Around("@annotation(com.daniel.app.global.sphere.annotation" +
            ".LogAspectAnnotation)")
    public Object logCustomAnnotation(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("logCustomAnnotation Method: " + joinPoint.getSignature());
       Instant start =  Instant.now();
       Object obj = joinPoint.proceed();
       Instant finish =  Instant.now();
       Long timeElapsed = Duration.between(start,finish).toMillis();
       log.info("Time taken for method {} to execute is {} millis",
               joinPoint.getSignature(),timeElapsed);

       return obj;
    }
}
