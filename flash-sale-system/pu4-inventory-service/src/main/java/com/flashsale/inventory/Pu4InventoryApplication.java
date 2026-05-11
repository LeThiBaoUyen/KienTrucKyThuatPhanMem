package com.flashsale.inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"com.flashsale.inventory", "com.flashsale.common"})
public class Pu4InventoryApplication {
    public static void main(String[] args) {
        SpringApplication.run(Pu4InventoryApplication.class, args);
    }
}
