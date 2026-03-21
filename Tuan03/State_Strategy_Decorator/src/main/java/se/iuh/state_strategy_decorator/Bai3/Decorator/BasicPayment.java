package se.iuh.state_strategy_decorator.Bai3.Decorator;

public class BasicPayment
        implements PaymentService{

    public void pay(int amount){

        System.out.println(
                "Base payment "+amount
        );
    }
}
