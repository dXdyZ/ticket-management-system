package com.another.ticketmessageservice.logging;

import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ReceiverLog {


    @Pointcut("execution(* com.another.ticketmessageservice.rabbit.*.*(..))")
    public void receiverMethod(){}

}
