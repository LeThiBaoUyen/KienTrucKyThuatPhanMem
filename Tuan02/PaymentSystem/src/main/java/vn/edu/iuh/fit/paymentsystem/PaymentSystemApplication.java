package vn.edu.iuh.fit.paymentsystem;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import vn.edu.iuh.fit.paymentsystem.payment.PaymentManager;

@SpringBootApplication
public class PaymentSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentSystemApplication.class, args);
    }

    @Bean
    CommandLineRunner run() {
        return args -> {

            PaymentManager manager = PaymentManager.getInstance();

            manager.processPayment("credit", 500);
            manager.processPayment("paypal", 300);
            manager.processPayment("momo", 200);

        };
    }
}