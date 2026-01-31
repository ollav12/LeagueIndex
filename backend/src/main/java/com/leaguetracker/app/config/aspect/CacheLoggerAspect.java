package com.leaguetracker.app.config.aspect;

import java.util.Arrays;
import org.aopalliance.intercept.MethodInvocation;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class CacheLoggerAspect {
    private static final Logger logger = LoggerFactory.getLogger(CacheLoggerAspect.class);

    @Around("@annotation(org.springframework.cache.annotation.Cacheable)")
    public Object logCacheAccess(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        // Print before execution
        System.out.println("\n==================================================");
        System.out.println("CACHE OPERATION: " + methodName);
        System.out.println("ARGS: " + Arrays.toString(args));

        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long timeTaken = System.currentTimeMillis() - startTime;

        // Determine if likely a cache hit based on execution time
        if (timeTaken < 20) {
            System.out.println("CACHE HIT: " + methodName + " completed in " + timeTaken + "ms");
            logger.info("CACHE HIT: {} executed in {}ms", methodName, timeTaken);
        } else {
            System.out.println("CACHE MISS: " + methodName + " completed in " + timeTaken + "ms");
            logger.info("CACHE MISS: {} executed in {}ms", methodName, timeTaken);
        }

        // Log the result object
        System.out.println("RESULT: " + (result != null ? result : "null"));
        System.out.println("==================================================\n");

        return result;
    }

    @Around("execution(* org.springframework.cache.interceptor.CacheInterceptor.execute(..))")
    public Object logCacheInterceptor(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        String methodName = "unknown";
        String cacheKey = "unknown";

        if (args.length > 0 && args[0] instanceof MethodInvocation) {
            MethodInvocation invocation = (MethodInvocation) args[0];
            methodName = invocation.getMethod().toString();
        }

        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long timeTaken = System.currentTimeMillis() - startTime;

        System.out.println("\n==================================================");
        System.out.println("CACHE INTERCEPTOR: " + methodName);
        System.out.println(
                (timeTaken < 5) ? "CACHE HIT (took " + timeTaken + "ms)" : "CACHE MISS (took " + timeTaken + "ms)");
        System.out.println("==================================================\n");

        return result;
    }
}
