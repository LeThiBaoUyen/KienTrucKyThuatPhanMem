package com.flashsale.order;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.flashsale.order", "com.flashsale.common"})
public class Pu3OrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(Pu3OrderApplication.class, args);
    }
}
