package com.flashsale.cart;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.flashsale.cart", "com.flashsale.common"})
public class Pu2CartApplication {
    public static void main(String[] args) {
        SpringApplication.run(Pu2CartApplication.class, args);
    }
}
