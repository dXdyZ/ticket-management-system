package com.another.ticketmessageservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class TicketMessageServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(TicketMessageServiceApplication.class, args);
    }

}
