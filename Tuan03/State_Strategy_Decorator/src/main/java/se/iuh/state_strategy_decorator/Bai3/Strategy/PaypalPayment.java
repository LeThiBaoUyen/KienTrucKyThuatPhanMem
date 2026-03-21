package se.iuh.state_strategy_decorator.Bai3.Strategy;

public class PaypalPayment
        implements PaymentStrategy{

    public void pay(int amount){

        System.out.println(
                "Paid by Paypal: "
                        + amount
        );
    }
}
