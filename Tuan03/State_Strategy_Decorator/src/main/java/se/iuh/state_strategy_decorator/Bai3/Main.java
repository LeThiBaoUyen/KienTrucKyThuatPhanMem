package se.iuh.state_strategy_decorator.Bai3;

import se.iuh.state_strategy_decorator.Bai3.Decorator.BasicPayment;
import se.iuh.state_strategy_decorator.Bai3.Decorator.FeeDecorator;
import se.iuh.state_strategy_decorator.Bai3.Decorator.PaymentService;
import se.iuh.state_strategy_decorator.Bai3.Strategy.CreditCardPayment;
import se.iuh.state_strategy_decorator.Bai3.Strategy.Payment;

public class Main {

    public static void main(String[] args) {

        Payment payment=new Payment();

        payment.setStrategy(
                new CreditCardPayment());

        payment.pay(500);

        PaymentService service =
                new FeeDecorator(
                        new BasicPayment()
                );

        service.pay(500);
    }
}