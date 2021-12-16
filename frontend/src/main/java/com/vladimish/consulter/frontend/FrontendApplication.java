package com.vladimish.consulter.frontend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FrontendApplication {

    public static void main(String[] args) {
        EnvConfig.Load();
        SpringApplication.run(FrontendApplication.class, args);
    }

}
