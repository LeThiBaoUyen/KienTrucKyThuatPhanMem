package se.iuh.state_strategy_decorator.Bai3.Decorator;

public abstract class PaymentDecorator
        implements PaymentService{

    protected PaymentService service;

    PaymentDecorator(
            PaymentService service){

        this.service=service;
    }

    public void pay(int amount){

        service.pay(amount);
    }
}