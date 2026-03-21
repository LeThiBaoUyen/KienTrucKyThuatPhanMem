package se.iuh.state_strategy_decorator.Bai3.Strategy;

public class CreditCardPayment
        implements PaymentStrategy{

    public void pay(int amount){

        System.out.println(
                "Paid by Credit Card: "
                        + amount
        );
    }
}