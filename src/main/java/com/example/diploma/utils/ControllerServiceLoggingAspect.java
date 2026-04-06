package com.example.diploma.utils;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ControllerServiceLoggingAspect {

    @Around("within(@org.springframework.stereotype.Service *) || within(@org.springframework.web.bind.annotation.RestController *)")
    public Object logControllerAndServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        String loggerName = joinPoint.getSignature().getDeclaringTypeName();
        Logger logger = LoggerFactory.getLogger(loggerName);
        String method = joinPoint.getSignature().toShortString();
        Object[] args = joinPoint.getArgs();

        MDC.put("classLog", loggerName);
        try {
            logger.info("ENTER {} | argsCount={}", method, args == null ? 0 : args.length);
            Object result = joinPoint.proceed();
            logger.info("EXIT {} | success=true", method);
            return result;
        } catch (Throwable exception) {
            logger.info(
                    "EXIT {} | success=false | errorType={} | message={}",
                    method,
                    exception.getClass().getSimpleName(),
                    exception.getMessage()
            );
            throw exception;
        } finally {
            MDC.remove("classLog");
        }
    }
}

