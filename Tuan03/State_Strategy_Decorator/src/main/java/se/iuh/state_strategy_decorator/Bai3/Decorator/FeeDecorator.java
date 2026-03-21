package se.iuh.state_strategy_decorator.Bai3.Decorator;

public class FeeDecorator
        extends PaymentDecorator{

    public FeeDecorator(
            PaymentService service){

        super(service);
    }

    public void pay(int amount){

        super.pay(amount);

        System.out.println(
                "Add fee 10$"
        );
    }
}