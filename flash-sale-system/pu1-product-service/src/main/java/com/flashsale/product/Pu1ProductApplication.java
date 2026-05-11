package com.flashsale.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.flashsale.product", "com.flashsale.common"})
public class Pu1ProductApplication {
    public static void main(String[] args) {
        SpringApplication.run(Pu1ProductApplication.class, args);
    }
}
