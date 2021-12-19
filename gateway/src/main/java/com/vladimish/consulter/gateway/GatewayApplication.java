package com.vladimish.consulter.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GatewayApplication {
    public static void main(String[] args) {
        if (System.getenv("TOKEN") == null) {
            System.out.println("TOKEN variable isn't set.");
            System.exit(1);
        }
        SpringApplication.run(GatewayApplication.class, args);
    }
}
