package com.share.aspect;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


//面向切面编程

@Aspect
@Component
public class LogAspect {
    Logger logger = LoggerFactory.getLogger(LogAspect.class);

    @Before("execution(* com.share.controller.IndexController.*(..))")
    public void beforeMethod(){
        logger.info("before method");
    }

    @After("execution(* com.share.controller.IndexController.*(..))")
    public void afterMethod(){
        logger.info("after method");
    }
}
