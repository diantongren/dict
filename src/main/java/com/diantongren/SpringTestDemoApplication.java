package com.diantongren;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class SpringTestDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringTestDemoApplication.class, args);
    }

}
