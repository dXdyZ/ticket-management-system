package com.another.eureka_serive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class EurekaSeriveApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaSeriveApplication.class, args);
    }

}
